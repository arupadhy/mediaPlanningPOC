package com.sapient.ai.media.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyc.kb.KbFunction;
import com.cyc.kb.KbFunctionFactory;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbIndividualFactory;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbPredicateFactory;
import com.cyc.kb.KbTermFactory;
import com.cyc.kb.Sentence;
import com.cyc.kb.VariableFactory;
import com.cyc.kb.exception.KbException;
import com.cyc.query.Query;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryFactory;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.UnsupportedCycOperationException;
import com.sapient.ai.cyc.session.MpConstants;
import com.sapient.ai.cyc.worker.CycWorker;

public class MpUtil
{
    private static Logger LOGGER = LoggerFactory.getLogger(MpUtil.class);

    //This class cannot be instantiated
    private MpUtil()
    {
    }

    public static String getMriCodeForQuestionAndResponsePair(KbIndividual mriDataSourceKb, String questionCycL, String answerCycL) throws Exception
    {
        String errorMsg = "Failed to retrieve Mri Code for Question And Response Pair. Reason: ";

        String code = null;
        Query query = null;
        try
        {
            Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
            substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), mriDataSourceKb);
            substitutions.put(KbTermFactory.get("(TheFn SurveyQuestion)"), CycWorker.getKbIndividualByName(questionCycL));

            query = QueryFactory.getQuery(KbIndividualFactory.get("GetCodeForQuestionAndResponsePair-MediaPlanningQuery"));
            query.substituteTerms(substitutions);

            Sentence querySentence = query.getQuerySentence();

            List<Object> answerFnSetArg = new ArrayList<Object>();
            answerFnSetArg.add(KbTermFactory.get("(TheFn Set-Mathematical)"));

            List<Object> answerSetArg = new ArrayList<Object>();
            Set<KbObject> answerFnSet = new HashSet<>();
            KbIndividual answerKb = CycWorker.getKbIndividualByName(answerCycL);
            answerFnSet.add(answerKb);
            answerSetArg.add(answerFnSet);

            querySentence = querySentence.replaceTerms(answerFnSetArg, answerSetArg);

            query.setQuerySentence(querySentence);

            System.out.println("Query to retrieve Mri Code for Question And Response Pair:\n" + query + "\n");

            List<QueryAnswer> answerList = query.getAnswers();
            if (answerList != null && !answerList.isEmpty() && answerList.get(0) != null)
            {
                code = answerList.get(0).getBinding(VariableFactory.get("?CODE")).toString();
            }
        }
        catch (UnsupportedCycOperationException | KbException | SessionCommunicationException | QueryConstructionException e)
        {
            errorMsg += e.getMessage();
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
        finally
        {
            if (query != null)
            {
                query.close();
            }
        }

        return code;
    }

    public static String getNumericMRIData(String predicateCycL, KbIndividual mriDataSourceKb, String targetPopulationMRIQueryString, String questionCycL,
            String answerCycL) throws Exception
    {
        String errorMsg = "Failed to select unique constraints. Reason: ";

        String numericMRIData = null;
        Query query = null;
        try
        {
            Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
            substitutions.put(KbTermFactory.get("(TheFn MRIExportDataPredicate-Quaternary)"), KbPredicateFactory.get(predicateCycL));
            substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), mriDataSourceKb);
            substitutions.put(KbTermFactory.get("(TheFn CharacterString)"), targetPopulationMRIQueryString);

            query = QueryFactory.getQuery(KbIndividualFactory.get("GetNumericMRIDataFromExport-MediaPlanningQuery"));
            query.substituteTerms(substitutions);

            Sentence querySentence = query.getQuerySentence();

            List<Object> qrFnArg = new ArrayList<Object>();
            qrFnArg.add(KbTermFactory.get("(TheFn QuestionAndAnswerSet-Specification)"));

            List<Object> qrSetArg = new ArrayList<Object>();
            // Find the operative function in Cyc
            KbFunction qrUnitFn = KbFunctionFactory.findOrCreate("QuestionResponseUnitFn");

            KbIndividual questionKb = CycWorker.getKbIndividualByName(questionCycL);

            // Make the question-response unit
            Set<KbObject> answerFnSet = new HashSet<>();
            answerFnSet.add(CycWorker.getKbIndividualByName(answerCycL));

            // Create the functional term
            KbObject questionAnswerUnit = qrUnitFn.findOrCreateFunctionalTerm(KbObject.class, questionKb, answerFnSet);

            qrSetArg.add(questionAnswerUnit);

            querySentence = querySentence.replaceTerms(qrFnArg, qrSetArg);

            query.setQuerySentence(querySentence);

            System.out.println("\n" + query + "\n");

            List<QueryAnswer> answerList = query.getAnswers();
            if (answerList != null && !answerList.isEmpty() && answerList.get(0) != null)
            {
                numericMRIData = answerList.get(0).getBinding(VariableFactory.get("?NUMBER")).toString();
            }
        }
        catch (UnsupportedCycOperationException | KbException | QueryConstructionException | SessionException e)
        {
            errorMsg += e.getMessage();
            LOGGER.error(errorMsg, e);
        }
        finally
        {
            if (query != null)
            {
                query.close();
            }
        }

        return numericMRIData;
    }

    public static String getTargetPopulationMRIQueryString(KbIndividual mriDataSourceKb, KbIndividual targetKb) throws Exception
    {
        String errorMsg = "Failed to select unique constraints. Reason: ";

        String argetPopulationMRIQueryString = null;
        Query query = null;
        try
        {
            Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
            substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), mriDataSourceKb);
            substitutions.put(KbTermFactory.get("(TheFn TargetPopulation-Advertising)"), targetKb);

            query = QueryFactory.getQuery("(targetPopulationHasMRIQueryStringInSource (TheFn MRIDataStore-CW) (TheFn TargetPopulation-Advertising) ?STRING)");
            query.substituteTerms(substitutions);

            System.out.println("\n" + query + "\n");

            List<QueryAnswer> answerList = query.getAnswers();
            if (answerList != null && !answerList.isEmpty() && answerList.get(0) != null)
            {
                argetPopulationMRIQueryString = answerList.get(0).getBinding(VariableFactory.get("?STRING"));
            }
        }
        catch (UnsupportedCycOperationException | KbException | QueryConstructionException | SessionException e)
        {
            errorMsg += e.getMessage();
            LOGGER.error(errorMsg, e);
        }
        finally
        {
            if (query != null)
            {
                query.close();
            }
        }

        return argetPopulationMRIQueryString;
    }

    public static String getMriTermString(String questionCycL, String answerCycL) throws Exception
    {
        String errorMsg = "Failed to retrieve QR MRI representation. Reason: ";

        String mriTermString = null;
        Query query = null;
        try
        {
            Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
            substitutions.put(KbTermFactory.get("(TheFn MRIDataStore-CW)"), CycWorker.getKbIndividualByName(MpConstants.MP_DEMO_MRI_SOURCE));

            query = QueryFactory.getQuery(KbIndividualFactory.get("GetDescriptionForQRU-MediaPlanningQuery"));
            query.substituteTerms(substitutions);

            Sentence querySentence = query.getQuerySentence();

            List<Object> qrFnArg = new ArrayList<Object>();
            qrFnArg.add(KbTermFactory.get("(TheFn QuestionAndAnswerSet-Specification)"));

            List<Object> qrSetArg = new ArrayList<Object>();
            // Find the operative function in Cyc
            KbFunction qrUnitFn = KbFunctionFactory.findOrCreate("QuestionResponseUnitFn");

            KbIndividual questionKb = CycWorker.getKbIndividualByName(questionCycL);

            // Make the question-response unit
            Set<KbObject> answerFnSet = new HashSet<>();
            answerFnSet.add(CycWorker.getKbIndividualByName(answerCycL));

            // Create the functional term
            KbObject questionAnswerUnit = qrUnitFn.findOrCreateFunctionalTerm(KbObject.class, questionKb, answerFnSet);

            qrSetArg.add(questionAnswerUnit);

            querySentence = querySentence.replaceTerms(qrFnArg, qrSetArg);

            query.setQuerySentence(querySentence);

            System.out.println("Query to retrieve MRI term string:\n" + query + "\n");

            List<QueryAnswer> answerList = query.getAnswers();

            if (answerList != null && !answerList.isEmpty())
            {
                mriTermString = answerList.get(0).getBinding(VariableFactory.get("?DESCRIPTION"));
            }
        }
        catch (SessionException | UnsupportedCycOperationException | QueryConstructionException | KbException e)
        {
            errorMsg += e.getMessage();
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
        finally
        {
            if (query != null)
            {
                query.close();
            }
        }

        return mriTermString;
    }

    public static String wrapLI(String entry)
    {
        return "<ul>" + entry + "</ul>";
    }

    public static String wrapUL(String entry)
    {
        return "<ul>" + entry + "</ul>";
    }

    public static String getIndex(String queryId, int index)
    {
        return queryId + MpConstants.INDEX_DLMTR + index;
    }

    public static String getQueryIdFromIndex(String index)
    {
        if (StringUtils.isNotBlank(index))
        {
            String[] indexTokens = index.trim().split(MpConstants.INDEX_DLMTR);
            if (indexTokens.length > 1)
            {
                return indexTokens[MpConstants.INDEX_POSITION_QUERY_ID];
            }
        }

        return null;
    }

    public static String getAnswerIndexFromIndex(String index)
    {
        if (StringUtils.isNotBlank(index))
        {
            String[] indexTokens = index.trim().split(MpConstants.INDEX_DLMTR);
            if (indexTokens.length > 1)
            {
                return indexTokens[MpConstants.INDEX_POSITION_ANSWER_INDEX];
            }
        }

        return null;
    }

    public static String stripNl(String stringNl)
    {
        if (StringUtils.isNotBlank(stringNl))
        {
            for (String[] stripSubstringsCombo : MpConstants.CYC_NL_STRIP_SUBSTRINGS)
            {
                if (stringNl.startsWith(stripSubstringsCombo[0]))
                {
                    stringNl = stringNl.replaceFirst(stripSubstringsCombo[0], "");
                    int stripSubSetLength = Integer.parseInt(stripSubstringsCombo[1]);
                    stringNl = stringNl.substring(stripSubSetLength, stringNl.length() - stripSubSetLength);
                    break;
                }
            }
        }

        return stringNl;
    }

}
