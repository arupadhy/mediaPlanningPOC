package com.sapient.ai.cyc.worker;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.justification.Justification;
import com.cyc.baseclient.justification.JustificationWalker;
import com.cyc.baseclient.nl.ParaphraserFactory;
import com.cyc.kb.ContextFactory;
import com.cyc.kb.KbFunction;
import com.cyc.kb.KbFunctionFactory;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbIndividualFactory;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.KbPredicateFactory;
import com.cyc.kb.KbTerm;
import com.cyc.kb.KbTermFactory;
import com.cyc.kb.Sentence;
import com.cyc.kb.VariableFactory;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.km.query.answer.justification.ProofViewJustification;
import com.cyc.nl.Paraphraser;
import com.cyc.query.Query;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryFactory;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.exception.UnsupportedCycOperationException;
import com.sapient.ai.cyc.session.CycConstants;
import com.sapient.ai.cyc.session.MpConstants;
import com.sapient.ai.cyc.session.SessionWorker;
import com.sapient.ai.media.dto.AudienceInsights;
import com.sapient.ai.media.dto.ConceptDto;
import com.sapient.ai.media.dto.ConceptDtoItem;
import com.sapient.ai.media.dto.InputMriQrUnitDtoItem;
import com.sapient.ai.media.dto.MriAnswerDtoItem;
import com.sapient.ai.media.dto.MriFieldDtoItem;
import com.sapient.ai.media.dto.MriFieldsDto;
import com.sapient.ai.media.dto.ProofDto;
import com.sapient.ai.media.dto.ProofEntryDtoItem;
import com.sapient.ai.media.dto.ReachDto;
import com.sapient.ai.media.dto.SentenceDtoItem;
import com.sapient.ai.media.utils.MpUtil;
import com.sapient.ai.media.utils.WebUtil;

@Component
public class MediaAppCycWorker
{
  private Logger   LOGGER      = LoggerFactory.getLogger(MediaAppCycWorker.class);

  Paraphraser      paraphraser = ParaphraserFactory.getInstance(ParaphraserFactory.ParaphrasableType.DEFAULT);

  public ConceptDto getConceptsByText(HttpSession httpSession, String text, String taskCycl) throws Exception
  {
    String errorMsg = "Failed to retrieve concepts. Reason: ";
    SessionWorker.cleanUpQueryStore(httpSession);
    List<ConceptDtoItem> conceptList = new ArrayList<ConceptDtoItem>();
    try
    {
      setMediaMarketDefaultContext();

      // TODO: when is it provided?
      KbIndividual taskKb = CycWorker.getKbIndividualByName(taskCycl);
      SessionWorker.setCurrentTask(httpSession, taskKb);

      Map<String, String> querySubstitutes = new HashMap<String, String>();
      querySubstitutes.put("MediaPlanningAllotment", taskKb.toString());
      querySubstitutes.put("CharacterString", text);

      Query query = CycWorker.getQuery("GetConceptsFromUserString-MediaPlanningQuery", -1, -1, true, querySubstitutes);

      LOGGER.debug("Query to retrieve concepts by text [" + text + "]\n" + query.toString() + "\n");

      KbPredicate predParents = KbPredicateFactory.get(MpConstants.MP_CONCEPT_TASK_TYPE);
      KbPredicate predSentence = KbPredicateFactory.get(MpConstants.MP_CONCEPT_SENTENCE_TYPE);
      List<ConceptDtoItem> conceptDtoItems = null;
      List<SentenceDtoItem> sentenceDtoItems = null;
      ConceptDtoItem conceptDtoItem = null;

      List<QueryAnswer> answerList = query.getAnswers();
      if (answerList != null && !answerList.isEmpty())
      {
        String queryIdInSession = SessionWorker.addQuery(httpSession, query);
        int counter = 0;
        String currentIndex;
        for (QueryAnswer mriFieldConstraint : answerList)
        {
          currentIndex = MpUtil.getIndex(queryIdInSession, counter);

          KbTerm conceptBinding = mriFieldConstraint.getBinding(VariableFactory.get("?MATCH"));

          LOGGER.debug("Found concept: " + conceptBinding.toString());

          conceptDtoItem = new ConceptDtoItem(conceptBinding.toNlString(), conceptBinding.toString());

          KbTerm conceptKb = KbTermFactory.get(conceptBinding.toString());
          conceptDtoItems = getSubConcepts(conceptKb, taskKb, predParents, predSentence, currentIndex);
          if (conceptDtoItems != null && !conceptDtoItems.isEmpty())
          {
            conceptList.addAll(conceptDtoItems);
            // concept.setConcepts(concepts);
          } else
          {
            sentenceDtoItems = getSentencesByConcept(conceptKb, taskKb, predParents, predSentence);
            conceptDtoItem.setSentences(sentenceDtoItems);
            if (conceptDtoItem.isValid())
            {
              conceptDtoItem.setIndex(currentIndex);
              conceptList.add(conceptDtoItem);
            }
          }

          counter++;
        }
      }
    } catch (
        KbTypeException | CreateException | SessionException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }

    ConceptDto conceptDto = new ConceptDto(getUniqueConceptsResults(conceptList));

    return conceptDto;
  }

  private List<ConceptDtoItem> getUniqueConceptsResults(List<ConceptDtoItem> items)
  {
    List<ConceptDtoItem> itemListOut = new ArrayList<ConceptDtoItem>();

    Set<String> itemSet = new HashSet<String>();
    String itemCycl;
    for (ConceptDtoItem item : items)
    {
      itemCycl = item.getCycl();
      if (!itemSet.contains(itemCycl))
      {
        itemListOut.add(item);
        itemSet.add(itemCycl);
      }
    }

    return itemListOut;
  }

  public MriFieldsDto getMRIFieldConstraints(HttpSession httpSession, String[] sentenceCyclList, String taskCycl, String mriSourceCycl) throws Exception
  {
    List<MriFieldDtoItem> mriFieldConstraints = new ArrayList<MriFieldDtoItem>();
    setMediaMarketDefaultContext();

    Map<String, String> querySubstitutes = new HashMap<String, String>();

    querySubstitutes.put("MediaPlanningAllotment", SessionWorker.getCurrentKbIndividualCycl(httpSession, SessionWorker.SESSION_ATT_CURRENT_TASK, taskCycl));
    querySubstitutes.put("MRIDataStore-CW", SessionWorker.getCurrentKbIndividualCycl(httpSession, SessionWorker.SESSION_ATT_CURRENT_MRI_SOURCE, mriSourceCycl));

    for (String sentenceCycl : sentenceCyclList)
    {
      if (StringUtils.isNotBlank(sentenceCycl))
      {
        mriFieldConstraints.addAll(getMRIFieldConstraintsBySentence(httpSession, sentenceCycl, querySubstitutes));
      }
    }

    return new MriFieldsDto(getUniqueMRIFieldConstraintsResults(mriFieldConstraints));
  }

  public ReachDto getReachByMriQuery(HttpSession httpSession, String mriQuery) throws Exception
  {
    String errorMsg = "Failed to retrieve reach. Reason: ";
    if (StringUtils.isBlank(mriQuery))
    {
      errorMsg += "MRI query is not provided.";
      LOGGER.error(errorMsg);
      throw new Exception(errorMsg);
    }

    ReachDto reachDto = new ReachDto();
    int currentReach = -1;
    int targetReach = SessionWorker.getTargetReach(httpSession);
    int reachGap = 100;

    try
    {
      mriQuery = WebUtil.decode(mriQuery);

        // When reach is not found in the mapping, assign reach with a random value
        Random rand = new Random();
        if (currentReach < 0)
        {
          currentReach = rand.nextInt((MpConstants.MP_DEMO_MAX_TARGET_REACH - MpConstants.MP_DEMO_MIN_TARGET_REACH) + 1) + MpConstants.MP_DEMO_MIN_TARGET_REACH;
          reachDto.setHorizontal(rand.nextInt(100));
          reachDto.setVertical(rand.nextInt(100));
          reachDto.setIndex(rand.nextInt(120));
        }

      reachDto.setCurrentReach(currentReach);
      reachGap = (currentReach - targetReach) * 100 / targetReach;
      reachDto.setTargetReach(SessionWorker.getTargetReach(httpSession));
      reachDto.setReachGap(reachGap);
    } catch (UnsupportedEncodingException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }

    return reachDto;
  }

  public List<MriFieldDtoItem> getSuggestedMriFields(HttpSession httpSession, InputMriQrUnitDtoItem[] mriQrUnits) throws Exception
  {
    String errorMsg = "Failed to retrieve MRI fields for export. Reason: ";

    if (mriQrUnits == null || mriQrUnits.length < 1)
    {
      errorMsg += "MRI query Q/R units are not provided.";
      LOGGER.error(errorMsg);
      throw new Exception(errorMsg);
    }

    List<MriFieldDtoItem> exportFieldList = new ArrayList<MriFieldDtoItem>();
    try
    {
      setMediaMarketDefaultContext();

      Set<KbObject> qrFnSet = new HashSet<>();
      for (InputMriQrUnitDtoItem mriQrUnit : mriQrUnits)
      {
        // Find the operative function in Cyc
        KbFunction qrUnitFn = KbFunctionFactory.findOrCreate("QuestionResponseUnitFn");

        KbIndividual questionKb = CycWorker.getKbIndividualByName(mriQrUnit.getQuestion());

        // Make the set of responses and the question-response unit
        Set<KbObject> rFnSet = new HashSet<>();
        for (String answer : mriQrUnit.getAnswers())
        {
          KbIndividual rUnit = CycWorker.getKbIndividualByName(answer);
          rFnSet.add(rUnit);
        }

        // Create the functional term
        KbObject qrUnit = qrUnitFn.findOrCreateFunctionalTerm(KbObject.class, questionKb, rFnSet);
        qrFnSet.add(qrUnit);
      }

      Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
      substitutions.put(KbTermFactory.get("(TheFn MediaPlanningAllotment)"), CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_TASK));
      substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_MRI_SOURCE));

      Query query = QueryFactory.getQuery(KbIndividualFactory.get("FindFieldsForExport-MediaPlanningQuery"));

      query.substituteTerms(substitutions);

      Sentence querySentence = query.getQuerySentence();

      List<Object> qrFnArg = new ArrayList<Object>();
      qrFnArg.add(KbTermFactory.get("(TheFn Set-Mathematical)"));

      List<Object> qrSetArg = new ArrayList<Object>();
      qrSetArg.add(qrFnSet);

      querySentence = querySentence.replaceTerms(qrFnArg, qrSetArg);

      query.setQuerySentence(querySentence);

      System.out.println("Query to retrieve MRI fields for export:\n" + query + "\n");

      Map<KbObject, Set<KbObject>> questionMap = new HashMap<KbObject, Set<KbObject>>();
      Set<KbObject> answerSet;
      List<QueryAnswer> answerList = query.getAnswers();
      if (answerList != null && !answerList.isEmpty())
      {
        for (QueryAnswer answer : answerList)
        {
          try
          {
            KbTerm mriField = answer.getBinding(VariableFactory.get("?SUGGESTION"));

            KbObject questionKb = (KbObject) mriField.getArgument(1);
            if (questionKb == null)
            {
              continue;
            }

            Set<KbObject> answerKbSet = mriField.getArgument(2);
            if (answerKbSet == null)
            {
              continue;
            }

            answerSet = questionMap.get(questionKb);
            if (answerSet == null)
            {
              answerSet = new HashSet<KbObject>();
            }

            answerSet.addAll(answerKbSet);
            questionMap.put(questionKb, answerSet);
          } catch (KbException e1)
          {
            LOGGER.warn("Failed to retrieve FindFieldsForExport-MediaPlanningQuery answer binding. Reason: " + e1);
          }
        }
      }

      if (questionMap.isEmpty())
      {
        errorMsg += "There are no valid MRI fields found.";
        LOGGER.warn(errorMsg);
      } else
      {
        // Construct DTO for output
        for (Map.Entry<KbObject, Set<KbObject>> item : questionMap.entrySet())
        {
          KbObject questionKb = item.getKey();
          MriFieldDtoItem mriFieldDtoItem = new MriFieldDtoItem();
          mriFieldDtoItem.setQuestion(questionKb.toNlString(), questionKb.toString());

          List<MriAnswerDtoItem> answers = new ArrayList<MriAnswerDtoItem>();
          for (Object answerKb : item.getValue())
          {
            if (KbObject.class.isAssignableFrom(answerKb.getClass()))
            {
              KbObject k = (KbObject) answerKb;
              answers.add(new MriAnswerDtoItem(k.toNlString(), k.toString(), true));
            }
          }

          mriFieldDtoItem.setAnswers(answers);
          exportFieldList.add(mriFieldDtoItem);
        }
      }
    } catch (
        SessionException | UnsupportedCycOperationException | QueryConstructionException | KbException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }

    return exportFieldList;
  }

  public Map<String, List<Map<String, String>>> getAudienceInsights(HttpSession httpSession) throws Exception, KbException
  {
    AudienceInsights results = new AudienceInsights();
    String errorMsg = "Failed to find interesting data for audience. Reason: ";

    Map<String, List<Map<String, String>>> returnMap = new HashMap<String, List<Map<String, String>>>();

    setMediaMarketDefaultContext();

    Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
    substitutions.put(KbTermFactory.get("(TheFn MediaPlanningAllotment)"), CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_TASK));
    substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_MRI_SOURCE));
    substitutions.put(KbTermFactory.get("(TheFn TargetPopulation-Advertising)"), CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_TARGET));

    Query query;
    try
    {
      query = QueryFactory.getQuery(KbIndividualFactory.get("FindInterestingDataForAudience-MediaPlanningQuery"));

      query.substituteTerms(substitutions);

      System.out.println("Query to find interesting data for audience :\n" + query + "\n");
      SessionWorker.addQuery(httpSession, query);

      // Not enabling this until Justifications are turned on.
      // query.retainInference();

      List<QueryAnswer> answerList = query.getAnswers();

      if (answerList != null && !answerList.isEmpty())
      {
        String cat1 = "INTERESTING_VALUES";
        String cat2 = "CONTRASTING_FACTS";
        String cat3 = "INTERESTING_FACTS";

        KbTerm andPred = KbTermFactory.get("and");
        KbPredicate contrastingSentencesPred = KbPredicateFactory.get("contrastingSentencesForAudience");
        KbPredicate totalPercentagePred = KbPredicateFactory.get("totalPercentageOfAudienceFrequencyAtLeast");
        // KbPredicate interestingVert = KbPredicateFactory.get("interestingDataSentence-VerticalPercent");
        // KbPredicate interestingIndex = KbPredicateFactory.get("interestingDataSentence-Index");

        for (QueryAnswer answer : answerList)
        {
          KbTerm answerTerm = answer.getBinding(VariableFactory.get("?THING"));

          if (answerTerm != null)
          {
            Justification justification = new ProofViewJustification(answer);
            justification.populate();

            KbTerm answerPred = answerTerm.getArgument(0);
            if (answerPred.equals(andPred))
            {
              KbTerm sent1 = answerTerm.getArgument(1);
              KbTerm sent2 = answerTerm.getArgument(2);
              // KbTerm pred1 = sent1.getArgument(0);
              // KbTerm pred2 = sent2.getArgument(0);
              KbTerm qru1 = sent1.getArgument(2);
              // KbTerm qru2 = sent2.getArgument(2);
              // KbTerm question1 = qru1.getArgument(1);
              // KbTerm question2 = qru2.getArgument(1);
              // Set<KbTerm> answerSet1 = qru1.getArgument(2);
              // Set<KbTerm> answerSet2 = qru2.getArgument(2);
              double vertical = sent1.getArgument(3);
              int index = sent2.getArgument(3);

              /*
               * if (interestingVert.equals(pred1)) { vertical = } else if (interestingIndex.equals(pred1)){ index = sent1.getArgument(3); } else { throw new KbException(
               * "Unexpected Predicate in Sentence 1"); } if (interestingIndex.equals(pred2)) { index = sent2.getArgument(3); } else if (interestingVert.equals(pred2)){ vertical =
               * sent2.getArgument(3); } else { throw new KbException("Unexpected Predicate in Sentence 2"); }
               */

              if (!returnMap.containsKey(cat1))
              {
                returnMap.put(cat1, new ArrayList<Map<String, String>>());
              }

              String questionString = "";
              questionString = paraphraser.paraphrase(qru1).getString();

              Map<String, String> rowMap = new HashMap<>();
              rowMap.put("resp", questionString);
              rowMap.put("vert", String.valueOf(vertical));
              rowMap.put("index", String.valueOf(index));
              // rowMap.put("InferenceId", query.getInferenceIdentifier().toString());
              rowMap.put("AnswerIndex", answer.getId().toString());

              returnMap.get(cat1).add(rowMap);

            } else if (answerPred.equals(contrastingSentencesPred))
            {
              if (!returnMap.containsKey(cat2))
              {
                returnMap.put(cat2, new ArrayList<Map<String, String>>());
              }
              Map<String, String> rowMap = new HashMap<>();
              String insightString = paraphraser.paraphrase(answerTerm).getString();
              rowMap.put("insight", insightString);
              // rowMap.put("InferenceId", query.getInferenceIdentifier().toString());
              rowMap.put("AnswerIndex", answer.getId().toString());

              returnMap.get(cat2).add(rowMap);
            } else if (answerPred.equals(totalPercentagePred))
            {
              if (!returnMap.containsKey(cat3))
              {
                returnMap.put(cat3, new ArrayList<Map<String, String>>());
              }
              Map<String, String> rowMap = new HashMap<>();
              String insightString = paraphraser.paraphrase(answerTerm).getString();
              rowMap.put("insight", insightString);
              // rowMap.put("InferenceId", query.getInferenceIdentifier().toString());
              rowMap.put("AnswerIndex", answer.getId().toString());

              returnMap.get(cat3).add(rowMap);
            } else
            {
              query.close();
              throw new KbException("Unexpected Insight Predicate: " + answerPred + "\n" + returnMap);
            }
          }
          // System.out.println(returnMap);
        }
      }
    } catch (
        UnsupportedCycOperationException | QueryConstructionException | SessionCommunicationException | CycConnectionException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return returnMap;
  }


  // getInsightJustifcation
  public final ProofDto getInsightJustifcation(HttpSession httpSession, String index) throws Exception
  {
    String errorMsg = "Failed to retrieve proof for insight with index [" + index + "]. Reason: ";
    ProofDto proof = new ProofDto();
    try
    {
      String queryId = MpUtil.getQueryIdFromIndex(index);
      String answerIndexStr = MpUtil.getAnswerIndexFromIndex(index);
      int answerIndex = -1;

      try
      {
        answerIndex = Integer.parseInt(answerIndexStr);
      } catch (NumberFormatException e)
      {
        throw new Exception("Invalid index for annswer [" + answerIndexStr + "]");
      }

      Query query = SessionWorker.getQuery(httpSession, queryId);
      if (query == null)
      {
        throw new Exception("There is no active query for id [" + queryId + "]");
      }

      LOGGER.debug("Found query in session for index [" + index + "]\n" + query.toString() + "\n");

      QueryAnswer queryAnswer = CycWorker.getAnswerByIndex(query, answerIndex);
      
      Justification justification = new ProofViewJustification(queryAnswer);
      justification.populate();
      
      for (final JustificationWalker walker = new JustificationWalker(justification); walker.hasNext();)
      {
        Justification.Node proofNode = walker.next();
        String proofEntry = proofNode.getHTML();
        if (!StringUtils.isEmpty(proofEntry))
        {
          proof.addProof(new ProofEntryDtoItem("Justification", proofEntry));
          break;
        }
      }
    } catch (
        OpenCycUnsupportedFeatureException | CycConnectionException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }

    return proof;
  }

  public final ProofDto getProof(HttpSession httpSession, String index) throws Exception
  {
    String errorMsg = "Failed to retrieve proof for answer with index [" + index + "]. Reason: ";
    ProofDto proof = new ProofDto();
    try
    {
      String queryId = MpUtil.getQueryIdFromIndex(index);
      String answerIndexStr = MpUtil.getAnswerIndexFromIndex(index);
      int answerIndex = -1;

      try
      {
        answerIndex = Integer.parseInt(answerIndexStr);
      } catch (NumberFormatException e)
      {
        throw new Exception("Invalid index for annswer [" + answerIndexStr + "]");
      }

      Query query = SessionWorker.getQuery(httpSession, queryId);
      if (query == null)
      {
        throw new Exception("There is no active query for id [" + queryId + "]");
      }

      LOGGER.debug("Found query in session for index [" + index + "]\n" + query.toString() + "\n");

      QueryAnswer queryAnswer = CycWorker.getAnswerByIndex(query, answerIndex);
      com.cyc.base.justification.Justification justification = new ProofViewJustification(queryAnswer);
      justification.populate();

      String proofEntryContentBecause = "";
      String proofEntryContentDetailed = "";
      String proofEntryContentLinear = "";

      String proofEntry = "";
      int depth = -1;
      int depthOld = -1;
      String currentType = "";

      for (final JustificationWalker walker = new JustificationWalker(justification); walker.hasNext();)
      {
        com.cyc.base.justification.Justification.Node proofNode = walker.next();
        proofEntry = proofNode.getHTML();

        if (CycWorker.isJustificationOfType(proofNode.getParent(), CycConstants.JUSTIF_LABEL_BECAUSE))
        {
          currentType = CycConstants.JUSTIF_LABEL_BECAUSE;
        } else if (CycWorker.isJustificationOfType(proofNode.getParent(), CycConstants.JUSTIF_LABEL_DETAILED))
        {
          currentType = CycConstants.JUSTIF_LABEL_DETAILED;
        } else if (CycWorker.isJustificationOfType(proofNode.getParent(), CycConstants.JUSTIF_LABEL_lINEAR))
        {
          currentType = CycConstants.JUSTIF_LABEL_lINEAR;
        }

        if (StringUtils.isNotBlank(proofEntry))
        {
          depth = proofNode.getDepth() - 2;

          // Remove redundant content
          if (depth > 1 && proofEntry.trim().toLowerCase().startsWith("entered by") && !proofEntry.trim().toLowerCase().startsWith("entered by cyc"))
          {
            continue;
          }

          String label = CycWorker.getJustificationLabel(proofNode);
          proofEntry = getProofEntryHtml(proofEntry, label, currentType, depth, depthOld);

          if (currentType.equals(CycConstants.JUSTIF_LABEL_BECAUSE))
          {
            proofEntryContentBecause += proofEntry;
          } else if (currentType.equals(CycConstants.JUSTIF_LABEL_DETAILED))
          {
            proofEntryContentDetailed += proofEntry;
            depthOld = depth;
          } else if (currentType.equals(CycConstants.JUSTIF_LABEL_lINEAR))
          {
            proofEntryContentLinear += proofEntry;
          }
        }

        proof.addProof(new ProofEntryDtoItem(CycConstants.JUSTIF_LABEL_BECAUSE,
            getProofEntryHtmlPrefix(CycConstants.JUSTIF_LABEL_BECAUSE) + proofEntryContentBecause + getProofEntryHtmlPostfix(CycConstants.JUSTIF_LABEL_BECAUSE, depth)));
        proof.addProof(new ProofEntryDtoItem(CycConstants.JUSTIF_LABEL_DETAILED,
            getProofEntryHtmlPrefix(CycConstants.JUSTIF_LABEL_DETAILED) + proofEntryContentDetailed + getProofEntryHtmlPostfix(CycConstants.JUSTIF_LABEL_DETAILED, depth)));
        proof.addProof(new ProofEntryDtoItem(CycConstants.JUSTIF_LABEL_lINEAR,
            getProofEntryHtmlPrefix(CycConstants.JUSTIF_LABEL_lINEAR) + proofEntryContentLinear + getProofEntryHtmlPostfix(CycConstants.JUSTIF_LABEL_lINEAR, depth)));
      }
    } catch (
        OpenCycUnsupportedFeatureException | CycConnectionException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }

    return proof;
  }

  private List<MriFieldDtoItem> getUniqueMRIFieldConstraintsResults(List<MriFieldDtoItem> MriFieldConstraintsDtoItems) throws Exception
  {
    String errorMsg = "Failed to select unique constraints. Reason: ";

    List<MriFieldDtoItem> mriFieldConstraints = new ArrayList<MriFieldDtoItem>();
    try
    {
      KbIndividual mriDataSourceKb = CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_MRI_SOURCE);
      Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
      substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), mriDataSourceKb);

      Query queryAllAnswersByQuestion = QueryFactory.getQuery("(surveyQuestionHasResponseSet (TheFn MRIDataStore-CW) (TheFn SurveyQuestion) ?RESPONSE-SET)");
      queryAllAnswersByQuestion.substituteTerms(substitutions);

      Set<String> questionSet = new HashSet<String>();
      String nextQuestion;
      for (MriFieldDtoItem MriFieldConstraintsDtoItem : MriFieldConstraintsDtoItems)
      {
        nextQuestion = MriFieldConstraintsDtoItem.getQuestion().getCycl();
        if (!questionSet.contains(nextQuestion))
        {
          List<MriAnswerDtoItem> recommendedAnswers = MriFieldConstraintsDtoItem.getAnswers();
          Set<KbTerm> answersKb = getAllAnswersByQuestion(queryAllAnswersByQuestion, nextQuestion);
          // FIXME : here is the class cast exception
          // java.lang.ClassCastException: java.lang.Integer cannot be cast to com.cyc.kb.KbTerm
          // at com.sapient.ai.mps.model.MpWorker.getUniqueMRIFieldConstraintsResults(MpWorker.java:640)
          // this is not a set of KbTerm, since it contains: [0, 1, 2, 3, 4, 5, (Unity 10 14), (IntervalMinFn 15), (Unity 6 9)]
          for (Object answerKb : answersKb)
          {
            // check the value is actually a KbTerm HACK (Shoudl not have to do that given the Cyc API
            if (KbTerm.class.isAssignableFrom(answerKb.getClass()))
            {
              KbTerm kb = (KbTerm) answerKb;
              // Check for duplicate answers
              if (isUniqueAnswer(recommendedAnswers, (KbTerm) answerKb))
              {
                MriAnswerDtoItem mriAnswerDtoItem = new MriAnswerDtoItem(kb.toNlString(), answerKb.toString(), false);
                recommendedAnswers.add(mriAnswerDtoItem);
              }
            }
          }

          // Retrieve and set MRI code for all answers
          for (MriAnswerDtoItem answer : recommendedAnswers)
          {
            String mriCode = MpUtil.getMriCodeForQuestionAndResponsePair(mriDataSourceKb, nextQuestion, answer.getCycl());
            if (StringUtil.isNotBlank(mriCode))
            {
              answer.setMriCode(mriCode);
            }
          }

          MriFieldConstraintsDtoItem.setAnswers(recommendedAnswers);
          mriFieldConstraints.add(MriFieldConstraintsDtoItem);

          questionSet.add(nextQuestion);
        }
      }
    } catch (
        UnsupportedCycOperationException | KbException | QueryConstructionException | SessionException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
    }

    return mriFieldConstraints;
  }

  private boolean isUniqueAnswer(List<MriAnswerDtoItem> recommendedAnswers, KbTerm answerKb)
  {
    String answerStr = answerKb.toString();
    for (MriAnswerDtoItem recommendedAnswer : recommendedAnswers)
    {
      if (recommendedAnswer.getCycl().equals(answerStr))
      {
        return false;
      }
    }

    return true;
  }

  private Set<KbTerm> getAllAnswersByQuestion(Query query, String questionCycl) throws Exception
  {
    String errorMsg = "Failed to retrieve concepts. Reason: ";

    Set<KbTerm> answersKb = null;
    try
    {
      Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
      substitutions.put(KbTermFactory.get("(TheFn SurveyQuestion)"), CycWorker.getKbIndividualByName(questionCycl));

      query.substituteTerms(substitutions);

      System.out.println("Query to retrieve all answers for a question:\n" + query + "\n");

      List<QueryAnswer> answerList = query.getAnswers();
      if (answerList != null && !answerList.isEmpty() && answerList.get(0) != null)
      {
        answersKb = answerList.get(0).getBinding(VariableFactory.get("?RESPONSE-SET"));
      }
    } catch (
        UnsupportedCycOperationException | KbException | SessionCommunicationException e)
    {
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }

    return answersKb;
  }

  private List<MriFieldDtoItem> getMRIFieldConstraintsBySentence(HttpSession httpSession, String sentenceCycl, Map<String, String> querySubstitutes) throws Exception
  {
    List<MriFieldDtoItem> mriFieldConstraints = new ArrayList<MriFieldDtoItem>();
    try
    {
      sentenceCycl = WebUtil.decode(sentenceCycl);
      querySubstitutes.put("CycLSentence", sentenceCycl);

      Query query = CycWorker.getQuery("SuggestAnMRIFieldConstraint-WithIndexical-MediaPlanningQuery", -1, -1, true, querySubstitutes);

      System.out.println("\n" + query + "\n");

      LOGGER.debug("Query to retrieve MRI Field Constraints by sentence [" + sentenceCycl + "]\n" + query + "\n");

      List<QueryAnswer> answerList = query.getAnswers();
      if (answerList != null && !answerList.isEmpty())
      {
        String queryIdInSession = SessionWorker.addQuery(httpSession, query);

        int counter = 0;
        String currentIndex;
        for (QueryAnswer queryAnswer : answerList)
        {
          currentIndex = MpUtil.getIndex(queryIdInSession, counter);
          MriFieldDtoItem mriFieldConstraintsDtoItem = new MriFieldDtoItem();

          // Set question
          KbTerm question = queryAnswer.getBinding(VariableFactory.get(MpConstants.MRI_FIELD_CONSTRAINT_ANSWER_BINDING_QUESTION));
          mriFieldConstraintsDtoItem.setQuestion(question.toNlString(), question.toString());
          mriFieldConstraintsDtoItem.setIndex(currentIndex);

          // Set answers
          List<MriAnswerDtoItem> answers = new ArrayList<MriAnswerDtoItem>();
          Set<KbTerm> suggestedSet = queryAnswer.getBinding(VariableFactory.get(MpConstants.MRI_FIELD_CONSTRAINT_ANSWER_BINDING_SUGGESTION));
          for (KbTerm suggestion : suggestedSet)
          {
            answers.add(new MriAnswerDtoItem(suggestion.toNlString(), suggestion.toString(), true));
          }

          mriFieldConstraintsDtoItem.setAnswers(answers);
          mriFieldConstraints.add(mriFieldConstraintsDtoItem);

          counter++;
        }
      }
    } catch (
        SessionException | KbTypeException | UnsupportedEncodingException e)
    {
      String errorMsg = "Failed to retrieve MRI field constraints by sentence [" + sentenceCycl + "]. Reason: ";
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg + e.getMessage(), e);
    }

    return mriFieldConstraints;
  }

  /*
   * public MriFieldConstraintsComboDto processMRIFieldConstraintsCombo(HttpSession httpSession, String answerIndex, KbIndividual mriSource, KbIndividual target) throws Exception
   * { List<MriFieldConstraintsComboDtoItem> mriFieldConstraintsCombo = new ArrayList<MriFieldConstraintsComboDtoItem>(); try { QueryAnswer qAnswer =
   * CycWorker.getAnswerByIndex(query, answerIndex); setMediaMarketDefaultContext(); KbTerm question =
   * qAnswer.getBinding(VariableFactory.get(MpConstants.MRI_FIELD_CONSTRAINT_ANSWER_BINDING_QUESTION)); Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
   * substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), mriSource); Set<KbTerm> suggestedSet =
   * qAnswer.getBinding(VariableFactory.get(MpConstants.MRI_FIELD_CONSTRAINT_ANSWER_BINDING_SUGGESTION)); //For each recommended Question-Response combo, get the MRI code, if
   * available. for (KbTerm suggestion : suggestedSet) { String code = "Not available"; Query queryMRICodes =
   * QueryFactory.getQuery(KbIndividualFactory.get("GetCodeForQuestionAndResponsePair-MediaPlanningQuery")); substitutions.put(KbTermFactory.get("(TheFn SurveyQuestion)"),
   * question); substitutions.put(KbTermFactory.get("(TheFn SurveyResponseType)"), suggestion); queryMRICodes.substituteTerms(substitutions); if (queryMRICodes.getAnswerCount() >
   * 0) { QueryAnswer queryAnswerCode = queryMRICodes.getAnswer(0); code = queryAnswerCode.getBinding(VariableFactory.get("?CODE")); } //As per Chris's instructions, we have to
   * make an assertion using "targetDefiningAttributeInDataset", for each combo we select as //representative of our Target Audience. Fact assertion =
   * FactFactory.findOrCreate(SentenceFactory.get(KbPredicateFactory.get("targetDefiningAttributeInDataset"), mriSource, target, question, suggestion)); //TODO: implement
   * //combos.addAnswer(new MriFieldConstraintsComboDtoItem(suggestion.toNlString(), code, assertion.toNlString())); assertion.delete(); } //TODO: implement
   * combos.setQuestion(question.toNlString()); combos.getInput().addParameters(new InputDtoItem("Strategic Target", target.toNlString())); combos.getInput().addParameters(new
   * InputDtoItem("MRI Source", mriSource.toNlString())); } catch (Exception | UnsupportedCycOperationException | QueryConstructionException | KbException | SessionException e) {
   * String errorMsg = "Failed to retrieve MRI field constraints. Reason: " + e.getMessage(); LOGGER.error(errorMsg, e); throw new Exception(errorMsg, e); } return new
   * MriFieldConstraintsComboDto(mriFieldConstraintsCombo); }
   */

  private void setMediaMarketDefaultContext() throws Exception
  {
    String errorMsg = "Failed to set default context [" + MpConstants.MP_DEFAULT_QUERY + " | " + MpConstants.MP_DEFAULT_ASSERT + "]. Reason: ";
    try
    {
      CycSessionManager.getCurrentSession().getOptions()
          .setDefaultContext(ContextFactory.getDefaultContext(ContextFactory.get(MpConstants.MP_DEFAULT_ASSERT), ContextFactory.get(MpConstants.MP_DEFAULT_QUERY)));
    } catch (
        SessionConfigurationException | SessionCommunicationException | SessionInitializationException | KbTypeException | CreateException | ExceptionInInitializerError e)
    {
      CycWorker.closeQueryAndConnection(null);
      errorMsg += e.getMessage();
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg, e);
    }
  }

  private List<ConceptDtoItem> getSubConcepts(KbTerm kbConcept, KbIndividual kbTask, KbPredicate predParents, KbPredicate predSentence, String index) throws Exception
  {
    List<ConceptDtoItem> subConceptList = new ArrayList<ConceptDtoItem>();

    try
    {
      List<SentenceDtoItem> sentences = null;
      ConceptDtoItem subConcept = null;

      Set<KbTerm> subConceptsKb = new HashSet<KbTerm>(kbTask.getValues(predParents, 1, 3, kbConcept, 2, ContextFactory.get(MpConstants.MP_DEFAULT_QUERY)));
      for (KbTerm subConceptKb : subConceptsKb)
      {
        LOGGER.debug("Found sub-concept: " + subConceptKb.toString());

        subConcept = new ConceptDtoItem(subConceptKb.toNlString(), subConceptKb.toString());
        sentences = getSentencesByConcept(subConceptKb, kbTask, predParents, predSentence);
        subConcept.setSentences(sentences);
        if (subConcept.isValid())
        {
          subConcept.setIndex(index);
          subConceptList.add(subConcept);
        }
      }
    } catch (
        SessionException | KbTypeException | CreateException e)
    {
      String errorMsg = "Failed to retrieve sub-concepts. Reason: ";
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg + e.getMessage(), e);
    }

    return subConceptList;
  }

  private List<SentenceDtoItem> getSentencesByConcept(KbTerm kbConcept, KbIndividual kbTask, KbPredicate predParents, KbPredicate predSentence) throws Exception
  {
    List<SentenceDtoItem> sentenceList = new ArrayList<SentenceDtoItem>();
    try
    {
      if (kbConcept.isInstanceOf("StrategicTargetTranslationConcept-LeafNode"))
      {
        SentenceDtoItem sentenceItem = null;
        List<KbObject> conceptCycLSentences = new ArrayList<KbObject>(kbTask.getValues(predSentence, 1, 3, kbConcept, 2, ContextFactory.get(MpConstants.MP_DEFAULT_QUERY)));

        // For the Tracer bullet, We are assuming that the user always picks only the first characteristic sentence of a leaf node
        for (KbObject conceptCycLSentence : conceptCycLSentences)
        {
          LOGGER.debug("Found sentence: " + conceptCycLSentence.toString());

          sentenceItem = new SentenceDtoItem(conceptCycLSentence.toNlString(), conceptCycLSentence.toString());
          if (sentenceItem.isValid())
          {
            // sentenceItem.setIndex(currentIndex);
            sentenceList.add(sentenceItem);
          }
        }
      }
    } catch (
        SessionException | KbTypeException | CreateException e)
    {
      String errorMsg = "Failed to retrieve sentences. Reason: ";
      LOGGER.error(errorMsg, e);
      throw new Exception(errorMsg + e.getMessage(), e);
    }

    return sentenceList;
  }

  private String getProofEntryHtml(String entry, String label, String currentType, int depth, int depthOld)
  {
    if (currentType.equals(CycConstants.JUSTIF_LABEL_DETAILED))
    {
      entry = entry.replaceAll("</span>", "").replaceAll("<span>", "").replaceAll("<ul><li>", ":<br>").replaceAll("</li><li>", ":<br>").replaceAll("</li>", "")
          .replaceAll("<li>", "").replaceAll("</ul>", "").replaceAll("<ul>", "");

      if (depthOld > -1)
      {
        entry = "<li>" + entry;
        if (depth == depthOld)
        {
          entry = "<li></li>" + entry;
        } else if (depth > depthOld)
        {
          entry = "<ul>" + entry;
        } else if (depth < depthOld)
        {
          entry = "</li></ul></li>" + entry;
        }
      }
    } else if (currentType.equals(CycConstants.JUSTIF_LABEL_BECAUSE) || currentType.equals(CycConstants.JUSTIF_LABEL_lINEAR))
    {
      entry = entry.replaceAll("</span>", "</span><br><br>");
      if (currentType.equals(CycConstants.JUSTIF_LABEL_lINEAR) && StringUtils.isNotBlank(label))
      {
        entry = "<b>" + label + "</b><br>" + entry;
      }
    }

    return entry;
  }

  private String getProofEntryHtmlPrefix(String currentType)
  {
    // FIXME: remove or refactor this method
    if (currentType.equals(CycConstants.JUSTIF_LABEL_DETAILED))
    {
      return "<ul id=\"expList\"><li>";
    } else
    {
      return "<ul><li>";
    }
  }

  private String getProofEntryHtmlPostfix(String currentType, int depth)
  {
    String postfix = "</li></ul>";
    if (currentType.equals(CycConstants.JUSTIF_LABEL_DETAILED))
    {
      for (int i = 0; i < depth; i++)
      {
        postfix = postfix + "</li></ul>";
      }
    }

    return postfix;
  }
}
