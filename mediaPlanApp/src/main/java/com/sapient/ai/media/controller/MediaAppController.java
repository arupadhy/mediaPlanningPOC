package com.sapient.ai.media.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.ai.cyc.session.MpConstants;
import com.sapient.ai.cyc.session.SessionWorker;
import com.sapient.ai.cyc.worker.MediaAppCycWorker;
import com.sapient.ai.media.dto.AudienceInsightsDto;
import com.sapient.ai.media.dto.ConceptDto;
import com.sapient.ai.media.dto.InputMriQrUnitDtoItem;
import com.sapient.ai.media.dto.InputMriQueryDto;
import com.sapient.ai.media.dto.MpInputConstraints;
import com.sapient.ai.media.dto.MriFieldDtoItem;
import com.sapient.ai.media.dto.MriFieldsDto;
import com.sapient.ai.media.dto.ProofDto;
import com.sapient.ai.media.dto.ReachDto;
import com.sapient.ai.media.dto.SelectedSentence;
import com.sapient.ai.media.utils.WebUtil;

@RestController
public class MediaAppController
{
    @Autowired
    private ServletContext context;

    @Autowired
    private MediaAppCycWorker  mpWorker;
    
  

    @RequestMapping("/conceptsSvc")
    public ConceptDto conceptsSvc(@RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "task", defaultValue = MpConstants.MP_DEMO_TASK) String task, HttpServletRequest request)
    {
        ConceptDto conceptDto;
        try
        {
            conceptDto = mpWorker.getConceptsByText(request.getSession(), WebUtil.decode(text), WebUtil.decode(task));
            conceptDto.setSessionId(SessionWorker.setSession(context, request.getSession()));
        }
        catch (Exception e)
        {
            conceptDto = new ConceptDto(e.getMessage());
        }

        return conceptDto;
    }

    @RequestMapping(value = "/mriFieldConstraintsSvc", method = RequestMethod.POST)
    public MriFieldsDto mriFieldConstraintsSvc(@RequestBody MpInputConstraints selectedContraints,
            @RequestParam(value = "sessionId", defaultValue = "") String sessionId, HttpServletRequest request)
    {
        MriFieldsDto mriFieldConstraintsDto;
        String task = selectedContraints.getTask();
        String mriSource = selectedContraints.getMriSource();
        List<String> selectedSent = new ArrayList<>();
        for (SelectedSentence sentence : selectedContraints.getSentences())
        {
            selectedSent.add(sentence.getCycl());
        }
        //must have java8
        String[] sentences = selectedSent.stream().toArray(String[]::new);

        HttpSession currentSession = SessionWorker.getSession(context, sessionId, request);

        try
        {
            mriFieldConstraintsDto = mpWorker.getMRIFieldConstraints(currentSession, sentences, WebUtil.decode(task), WebUtil.decode(mriSource));
            SessionWorker.setSession(context, currentSession);
        }
        catch (Exception  e)
        {
            mriFieldConstraintsDto = new MriFieldsDto(e.getMessage());
        }

        return mriFieldConstraintsDto;
    }

    @RequestMapping("/proofSvc")
    public ProofDto proofMvc(@RequestParam(value = "index", defaultValue = "") String index,
            @RequestParam(value = "sessionId", defaultValue = "") String sessionId, HttpServletRequest request)
    {
        ProofDto proofDto;
        try
        {
            HttpSession currentSession = SessionWorker.getSession(context, sessionId, request);
            proofDto = mpWorker.getProof(currentSession, WebUtil.decode(index));
        }
        catch (Exception  e)
        {
            proofDto = new ProofDto(e.getMessage());
        }

        return proofDto;
    }

    @RequestMapping("/mriQueryCheckSvc")
    public ReachDto mriQueryCheckSvc(@RequestParam(value = "query") String query, HttpServletRequest request)
    {
        ReachDto reachDto;
        try
        {
            reachDto = mpWorker.getReachByMriQuery(request.getSession(), query);
        }
        catch (Exception e)
        {
            reachDto = new ReachDto(e.getMessage());
        }

        return reachDto;
    }
    
    //Quick fix to convert the input to a structure that was already supported.
    @SuppressWarnings("unchecked")
    @RequestMapping("/suggestedMriFields")
    public MriFieldsDto suggestedMriFieldsSvc(@RequestBody Map<String,Object> input,
            @RequestParam(value = "sessionId", defaultValue = "") String sessionId, HttpServletRequest request)
    {
    	InputMriQueryDto mriQueryDto = new InputMriQueryDto();
		ArrayList<Object> questionsList =  (ArrayList<Object>) input.get("queryTerms");
    	List<InputMriQrUnitDtoItem> itemList = new ArrayList<>();
    	for(Object question: questionsList) {
    		LinkedHashMap<String, Object> q = (LinkedHashMap<String, Object>)question;
    		InputMriQrUnitDtoItem item = new InputMriQrUnitDtoItem();
    		item.setCycl((String) q.get("cycl"));
    		item.setNl((String) q.get("nl"));
    		item.setQuestion((String) q.get("cycl"));
    		
    		ArrayList<Object> answers = (ArrayList<Object>) q.get("answers");
    		List<String>answersToBeSet = new ArrayList<>();
    		for(Object ans: answers) {
        		LinkedHashMap<String, Object> a = (LinkedHashMap<String, Object>)ans;
        		if((Boolean)a.get("recommended")) {
        			answersToBeSet.add((String)a.get("cycl"));
        		}
    		}
    		item.setAnswers(answersToBeSet.stream().toArray(String[]::new));
    		itemList.add(item);
    	}
    	mriQueryDto.setQueryTerms(itemList.stream().toArray(InputMriQrUnitDtoItem[]::new));
    	
    	MriFieldsDto suggestedMriFieldsDto;
        try
        {
            HttpSession currentSession = SessionWorker.getSession(context, sessionId, request);
            suggestedMriFieldsDto = new MriFieldsDto();
            List<MriFieldDtoItem> suggestedMriFields = mpWorker.getSuggestedMriFields(currentSession, mriQueryDto.getQueryTerms());
            suggestedMriFieldsDto.setMriFieldConstraints(suggestedMriFields);
            SessionWorker.setSession(context, currentSession);
        }
        catch (Exception e)
        {
            suggestedMriFieldsDto = new MriFieldsDto(e.getMessage());
        }

        return suggestedMriFieldsDto;
    }

    @RequestMapping("/suggestedMriFieldsSvc")
    public MriFieldsDto suggestedMriFieldsSvc(@RequestBody InputMriQueryDto mriQueryDto,
            @RequestParam(value = "sessionId", defaultValue = "") String sessionId, HttpServletRequest request)
    {
        MriFieldsDto suggestedMriFieldsDto;
        try
        {
            HttpSession currentSession = SessionWorker.getSession(context, sessionId, request);
            suggestedMriFieldsDto = new MriFieldsDto();
            List<MriFieldDtoItem> suggestedMriFields = mpWorker.getSuggestedMriFields(currentSession, mriQueryDto.getQueryTerms());
            suggestedMriFieldsDto.setMriFieldConstraints(suggestedMriFields);
            SessionWorker.setSession(context, currentSession);
        }
        catch (Exception e)
        {
            suggestedMriFieldsDto = new MriFieldsDto(e.getMessage());
        }

        return suggestedMriFieldsDto;
    }

    @RequestMapping("/audienceInsightsSvc")
    public AudienceInsightsDto audienceInsightsSvc(HttpServletRequest request)
    {
      AudienceInsightsDto audienceInsightsDto = new AudienceInsightsDto();
      try
      {
        audienceInsightsDto = new AudienceInsightsDto(mpWorker.getAudienceInsights(request.getSession()));
      } catch (Exception e)
      {
        audienceInsightsDto = new AudienceInsightsDto(e.getMessage());
      } 

      return audienceInsightsDto;
    }
}
