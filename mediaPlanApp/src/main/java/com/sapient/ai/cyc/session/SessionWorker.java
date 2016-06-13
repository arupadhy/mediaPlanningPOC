package com.sapient.ai.cyc.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyc.kb.Fact;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.exception.DeleteException;
import com.cyc.query.Query;

public class SessionWorker
{
    private static Logger      LOGGER                         = LoggerFactory.getLogger(SessionWorker.class);

    // Session attributes
    public static final String SESSION_ATT_CYC_QUERY_MAP      = "CYC_QUERY-STORE";
    public static final String SESSION_ATT_MRI_QUERY          = "MRI_QUERY";
    public static final String SESSION_ATT_ASSERTIONS         = "ASSERTS";
    public static final String SESSION_ATT_CURRENT_TASK       = "CURRENT_TASK";
    public static final String SESSION_ATT_CURRENT_TARGET     = "CURRENT_TARGET";
    public static final String SESSION_ATT_CURRENT_MRI_SOURCE = "CURRENT_MRI_SOURCE";
    public static final String SESSION_ATT_TARGET_REACH       = "TARGET_REACH";

    private SessionWorker()
    {
    }

    public static int getTargetReach(HttpSession httpSession)
    {
        if (httpSession.getAttribute(SESSION_ATT_TARGET_REACH) == null)
        {
            setTargetReach(httpSession, MpConstants.MP_DEMO_TARGET_REACH);
        }
        return (int) httpSession.getAttribute(SESSION_ATT_TARGET_REACH);
    }

    public static void setTargetReach(HttpSession httpSession, int targetReach)
    {
        httpSession.setAttribute(SESSION_ATT_TARGET_REACH, targetReach);
    }

    public static String getMriQuery(HttpSession httpSession)
    {
        return (String) httpSession.getAttribute(SESSION_ATT_MRI_QUERY);
    }

    public static void setMriQuery(HttpSession httpSession, String mriQuery)
    {
        httpSession.setAttribute(SESSION_ATT_MRI_QUERY, mriQuery);
    }

    public static String getCurrentKbIndividualCycl(HttpSession httpSession, String kbIndividualType, String kbIndividualCycl)
    {
        KbIndividual kbIndividual = getCurrentKbIndividual(httpSession, kbIndividualType);
        return (kbIndividual == null) ? kbIndividualCycl : kbIndividual.toString();
    }

    public static void setCurrentMriSource(HttpSession httpSession, KbIndividual mriSource)
    {
        httpSession.setAttribute(SESSION_ATT_CURRENT_MRI_SOURCE, mriSource);
    }

    public static KbIndividual getCurrentMriSource(HttpSession httpSession)
    {
        return getCurrentKbIndividual(httpSession, SESSION_ATT_CURRENT_MRI_SOURCE);
    }

    public static void setCurrentTarget(HttpSession httpSession, KbIndividual targetKb)
    {
        httpSession.setAttribute(SESSION_ATT_CURRENT_TARGET, targetKb);
    }

    public static KbIndividual getCurrentTarget(HttpSession httpSession)
    {
        return getCurrentKbIndividual(httpSession, SESSION_ATT_CURRENT_TARGET);
    }

    public static void setCurrentTask(HttpSession httpSession, KbIndividual taskKb)
    {
        httpSession.setAttribute(SESSION_ATT_CURRENT_TASK, taskKb);
    }

    public static KbIndividual getCurrentTask(HttpSession httpSession)
    {
        return getCurrentKbIndividual(httpSession, SESSION_ATT_CURRENT_TASK);
    }

    public static KbIndividual getCurrentKbIndividual(HttpSession httpSession, String type)
    {
        return (KbIndividual) httpSession.getAttribute(type);
    }

    @SuppressWarnings("unchecked")
    public static String addQuery(HttpSession httpSession, Query query)
    {
        Map<String, Query> queryMap = (Map<String, Query>) httpSession.getAttribute(SESSION_ATT_CYC_QUERY_MAP);
        if (queryMap == null)
        {
            queryMap = new HashMap<String, Query>();
        }

        String queryId = "" + System.currentTimeMillis();
        queryMap.put(queryId, query);

        httpSession.setAttribute(SESSION_ATT_CYC_QUERY_MAP, queryMap);

        return queryId;
    }

    @SuppressWarnings("unchecked")
    public static void removeQuery(HttpSession httpSession, String queryId)
    {
        Map<String, Query> queryMap = (Map<String, Query>) httpSession.getAttribute(SESSION_ATT_CYC_QUERY_MAP);
        if (queryMap != null)
        {
            queryMap.remove(queryId);
            httpSession.setAttribute(SESSION_ATT_CYC_QUERY_MAP, queryMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static Query getQuery(HttpSession httpSession, String queryId)
    {
        Map<String, Query> queryMap = (Map<String, Query>) httpSession.getAttribute(SESSION_ATT_CYC_QUERY_MAP);
        if (queryMap != null)
        {
            return queryMap.get(queryId);
        }

        return null;
    }

    public static void cleanUpQueryStore(HttpSession httpSession)
    {
        httpSession.setAttribute(SESSION_ATT_CYC_QUERY_MAP, new HashMap<String, Query>());
    }

    @SuppressWarnings("unchecked")
    public static void addAssertion(HttpSession httpSession, Fact assertion)
    {
        List<Fact> assertionList = (List<Fact>) httpSession.getAttribute(SESSION_ATT_ASSERTIONS);
        if (assertionList == null)
        {
            assertionList = new ArrayList<Fact>();
        }

        assertionList.add(assertion);
        httpSession.setAttribute(SESSION_ATT_ASSERTIONS, assertionList);
    }

    @SuppressWarnings("unchecked")
    public static void cleanUpAssertions(HttpSession httpSession)
    {
        List<Fact> assertionList = (List<Fact>) httpSession.getAttribute(SESSION_ATT_ASSERTIONS);
        if (assertionList != null && !assertionList.isEmpty())
        {
            for (Fact assertion : assertionList)
            {
                try
                {
                    assertion.delete();
                }
                catch (DeleteException e)
                {
                    LOGGER.error("Failed to delete assertion. Reason: " + e.getMessage(), e);
                }
            }

            httpSession.removeAttribute(SESSION_ATT_ASSERTIONS);
        }
    }

    public static HttpSession getSession(ServletContext context, String sessionId, HttpServletRequest request)
    {
        HttpSession session = null;
        if (!StringUtils.isBlank(sessionId))
        {
            session = (HttpSession) context.getAttribute(sessionId);
        }

        if (session == null)
        {
            System.out.println(">>>>>>>>>>>>> getSession/ failed to find session id [" + sessionId + "], getting session from request");
            session = request.getSession();
        }
        else
        {
            System.out.println(">>>>>>>>>>>>> getSession/ found session id [" + sessionId + "]");
        }

        return session;
    }

    public static String setSession(ServletContext context, HttpSession session)
    {
        String sessionId = session.getId();
        context.setAttribute(sessionId, session);
        System.out.println(">>>>>>>>>>>>> setSession/ added new session id [" + sessionId + "]");

        return sessionId;
    }
}
