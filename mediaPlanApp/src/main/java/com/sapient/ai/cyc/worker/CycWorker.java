package com.sapient.ai.cyc.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyc.baseclient.nl.ParaphraserFactory;
import com.cyc.kb.KbCollectionFactory;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbIndividualFactory;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbTermFactory;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.nl.Paraphrase;
import com.cyc.nl.Paraphraser;
import com.cyc.query.Query;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryFactory;
import com.cyc.query.QueryResultSet;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.UnsupportedCycOperationException;


public class CycWorker
{
    private static Logger LOGGER = LoggerFactory.getLogger(CycWorker.class);

    // This class cannot be instantiated
    private CycWorker()
    {
    }

   
    public final static Query getQuery(final String queryName, final int maxAnswerCount, final int maxTimeSec, final boolean ifContinue,
            final Map<String, String> parameters) throws Exception
    {
        LOGGER.debug("Creating Cyc query based on provided query properties");
        LOGGER.debug("Retrieving query by name [" + queryName + "]");

        Query query = null;
        try
        {
            // Find query in KB
            query = QueryFactory.getQuery(KbIndividualFactory.get(queryName));

            LOGGER.info("Found query [" + queryName + "]");
            LOGGER.debug("Setting query properties [maxAnswerCount: " + maxAnswerCount + ", maxTimeSec: " + maxTimeSec + ", ifContinue: " + ifContinue + "]");

            // TODO: revisit this logic when KB is available
            query = setQueryParameters(query, parameters);

            // Max number of answers
            if (maxAnswerCount > 0)
            {
                query.setMaxAnswerCount(maxAnswerCount);
            }

            // Max time in seconds to execute
            if (maxTimeSec > 0)
            {
                query.setMaxTime(maxTimeSec);
            }

            // Set if to close the query after completion
            query.setContinuable(ifContinue);
        }
        catch (UnsupportedCycOperationException | QueryConstructionException | KbException e)
        {
            String errorMsg = "Failed to get query [" + queryName + "]. Reason: ";
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg + e.getMessage(), e);
        }

        return query;
    }

    /**
     * Retrieves definition of the query in natural language
     *
     * @param query
     *            Cyc query
     * 
     * @return A definition of the query in natural language
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public final static String getQueryDefinition(final Query query) throws Exception
    {
        if (query == null)
        {
            throw new Exception("Failed to get query definition. Reason: The query is not provided.");
        }

        LOGGER.debug("Retrieving sentence of the query [" + query.getId() + "] in natural language");

        Paraphraser paraphraser = ParaphraserFactory.getInstance(ParaphraserFactory.ParaphrasableType.QUERY);
        Paraphrase paraphrase = paraphraser.paraphrase(query);
        String queryNlDefinition = paraphrase.getString();

        LOGGER.debug("Definition of query [" + query.getId() + "]: " + queryNlDefinition);

        return queryNlDefinition;
    }

    /**
     * Closes the query if it is open
     *
     * @param query
     *            Cyc query.
     *
     */
    public final static void closeQuery(final Query query)
    {
        if (query != null)
        {
            query.close();
            LOGGER.info("Query is closed");
        }
    }

    /**
     * Closes the query and the connection to Cyc
     *
     * @param query
     *            Cyc query. null if there is no query to close
     *
     */
    public final static void closeQueryAndConnection(final Query query)
    {
        closeQuery(query);
        try
        {
            CycSessionManager.getInstance().close();
            LOGGER.info("Connection is closed");
        }
        catch (IOException e)
        {
            LOGGER.error("Unexpected exception while closing connection. Reason: ", e);
        }
    }

    /**
     * Sets Cyc query with parameters.
     *
     * @param query
     *            Cyc query
     *
     * @param parametersMap
     *            A map of query parameters
     * 
     * @return Cyc query with parameters.
     */
    public static Query setQueryParameters(Query query, final Map<String, String> parametersMap) throws Exception
    {
        LOGGER.debug("Setting query parameters");

        if (parametersMap == null || parametersMap.size() < 1)
        {
            LOGGER.warn("Query parameters are not set. Reason: parameters are not provided.");
        }

        String errorMsgPrefix = "Failed to set query parameter ";
        Map<KbObject, Object> querySubstitutesMap = new HashMap<KbObject, Object>();

        for (Map.Entry<String, String> parameter : parametersMap.entrySet())
        {
            String parameterType = parameter.getKey();
            String parameterValue = parameter.getValue();
            String errorMsg = errorMsgPrefix + "[" + parameterType + "]. Reason: ";

            try
            {
                // TODO: verify the function name and syntax when KB is ready
                final KbObject kbType = KbTermFactory.get("(TheFn " + parameterType + ")");
                LOGGER.debug("Found query parameter: type [" + parameterType + "], value [" + parameterValue + "]");

                KbObject kbItem = null;
                try
                {
                    kbItem = KbTermFactory.get(parameterValue);
                }
                catch (KbObjectNotFoundException e2)
                {
                    LOGGER.warn("Parameter value [" + parameterValue + "] does not exist as KbObject. Trying as is.");
                }

                querySubstitutesMap.put(kbType, (kbItem != null) ? kbItem : parameterValue);
            }
            catch (KbTypeException | CreateException e)
            {
                LOGGER.error(errorMsg, e);
                throw new Exception(errorMsg + e.getMessage(), e);
            }
        }

        query.substituteTerms(querySubstitutesMap);

        LOGGER.debug("Query is set with parameters [" + query.toString() + "]");

        return query;
    }

    /**
     * Retrieves sentence of answer
     *
     * @param query
     *            Cyc query
     * @param paraphraser
     *            An instance of Cyc paraphraser
     * 
     * @return A Map containing index and sentence of the answer
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String getAnswerSentence(final Query query, final QueryAnswer queryAnswer, Paraphraser<Sentence> paraphraser) throws Exception
    {
        LOGGER.debug("Retrieving sentence of answer [" + queryAnswer + "]");

        try
        {
            Sentence sentence = query.getAnswerSentence(queryAnswer);
            if (paraphraser == null)
            {
                paraphraser = ParaphraserFactory.getInstance(ParaphraserFactory.ParaphrasableType.DEFAULT);
            }

            Paraphrase paraphrase = paraphraser.paraphrase(sentence);
            String sentense = paraphrase.getString();
            return sentense;
        }
        catch (KbException e)
        {
            String errorMsg = "Failed to retrieve a sentence of the answer. Reason: ";
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a query answer by the index.
     *
     * @param query
     *            Cyc query
     * @param answerIndex
     *            An index of the answer in the answers list
     * 
     * @return The query answer by its index in the answers list.
     * @throws Exception
     */
    public final static QueryAnswer getAnswerByIndex(final Query query, final int answerIndex) throws Exception
    {
        LOGGER.debug("Retrieving answer by index [" + answerIndex + "]");

        try
        {
            QueryAnswer queryAnswer = query.getAnswer(answerIndex);
            return queryAnswer;
        }
        catch (SessionCommunicationException e)
        {
            String errorMsg = "Failed to retrieve answer by index [" + answerIndex + "]. Reason: ";
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg + e.getMessage(), e);
        }
    }

    /**
     * Checks if this is justification 'Because'
     *
     * @param justificationNode
     *            A justification
     * 
     * @return True if this is 'Because' justification, false otherwise
     */
    public static String getJustificationLabel(com.cyc.base.justification.Justification.Node justificationNode)
    {
        if (justificationNode != null)
        {
            return justificationNode.getLabel();
        }

        return null;
    }

    /**
     * Checks if this is justification 'Because'
     *
     * @param justificationNode
     *            A justification
     * 
     * @return True if this is 'Because' justification, false otherwise
     */
    public static boolean isJustificationOfType(com.cyc.base.justification.Justification.Node justificationNode, String type)
    {
        if (justificationNode != null && justificationNode.getLabel() != null && justificationNode.getLabel().startsWith(type))
        {
            return true;
        }

        return false;
    }

    /**
     * Calculates the index of the offset of results to return.
     *
     * @param query
     *            Cyc query
     * 
     * @return Integer value of index of the offset of results to return.
     */
    private static int getQueryOffset(final Query query)
    {
        LOGGER.debug("Calculating offset");

        int queryOffset = 0;
        if (query != null && query.getMaxAnswerCount() != null)
        {
            QueryResultSet queryResultSet = query.getResultSet();
            if (queryResultSet.last())
            {
                int queryResultsCount = queryResultSet.getCurrentRowCount();
                int queryMaxAnswerCount = query.getMaxAnswerCount();
                if (queryResultsCount > queryMaxAnswerCount)
                {
                    int reminder = queryResultsCount % queryMaxAnswerCount;
                    if (reminder > 0)
                    {
                        queryOffset = queryResultsCount - reminder;
                    }
                    else
                    {
                        queryOffset = queryResultsCount - queryMaxAnswerCount;
                    }
                }
            }
        }

        LOGGER.debug("The offset is " + queryOffset);

        return queryOffset;
    }

    /**
     * Checks if Cyc query has finished the inference.
     *
     * @param query
     *            Cyc query
     * 
     * @return true if the query has finished the inference, false otherwise.
     */
    private final static boolean isQueryComplete(final Query query)
    {
        LOGGER.debug("Checks if Cyc query has finished the inference");
        if (query == null || query.getSuspendReason() == null || query.getSuspendReason().getInferenceStatusString() == null)
        {
            return true;
        }
        return query.getSuspendReason().getInferenceStatusString().contains(">Finished<");
    }

    public static KbIndividual getKbIndividualByName(String kbNameCycl, String kbType) throws Exception
    {
        KbIndividual kbIndividual = getKbIndividualByName(kbNameCycl);
        if (kbIndividual.isInstanceOf(kbType))
        {
            return kbIndividual;
        }
        else
        {
            String errorMsg = "Failed to retrieve kbIndividual [" + kbNameCycl + "]. Reason: it is not KB type [" + kbType + "].";
            throw new Exception(errorMsg);
        }
    }

    public static KbIndividual getKbIndividualByName(String kbNameCycl) throws Exception
    {
        try
        {
            return KbIndividualFactory.get(kbNameCycl);
        }
        catch (KbTypeException | CreateException e)
        {
            String errorMsg = "Failed to retrieve KbIndividual [" + kbNameCycl + "]. Reason: " + e.getMessage();
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }

    public static List<KbIndividual> getKbIndividualsByType(String kbType) throws Exception
    {
        try
        {
            return new ArrayList<KbIndividual>(KbCollectionFactory.get(kbType).getInstances());
        }
        catch (KbTypeException | CreateException e)
        {
            String errorMsg = "Failed to retrieve KbIndividuals by type [" + kbType + "]. Reason: " + e.getMessage();
            LOGGER.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        }
    }
}
