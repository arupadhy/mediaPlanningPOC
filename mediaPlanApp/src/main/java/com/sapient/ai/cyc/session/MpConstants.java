package com.sapient.ai.cyc.session;

public class MpConstants
{
    
	// Default context
    public static final String MP_DEFAULT_QUERY                               = "MediaPlanningQueryMt";
    public static final String MP_DEFAULT_ASSERT                              = "MediaPlanningBridgingMt";

    //
    public static final String MP_CONCEPT_TASK_TYPE                           = "hasParentConceptWRTTask";
    public static final String MP_CONCEPT_SENTENCE_TYPE                       = "characteristicSentenceForConceptForTask";

    //Default demo values
    public static final String MP_DEMO_TASK                                   = "MediaPlanningTask-Allotment";
    public static final String MP_DEMO_TARGET                                 = "TestAudience-Target";
    public static final String MP_DEMO_MRI_SOURCE                             = "MediaPlanningDataSources-MRI-2015-Doublebase";
    public static final int    MP_DEMO_TARGET_REACH                           = 42000000;
    public static final int    MP_DEMO_MIN_TARGET_REACH                       = 20000;
    public static final int    MP_DEMO_MAX_TARGET_REACH                       = 60000000;

    // MRIFieldConstraint answer bindings
    public static final String MRI_FIELD_CONSTRAINT_ANSWER_BINDING_QUESTION   = "?QUESTION";
    public static final String MRI_FIELD_CONSTRAINT_ANSWER_BINDING_SUGGESTION = "?SUGGESTED-SET";

    public static final int    ERROR_CODE                                     = 500;

    public static final String INDEX_DLMTR                                    = "-";
    public static final int    INDEX_POSITION_QUERY_ID                        = 0;
    public static final int    INDEX_POSITION_ANSWER_INDEX                    = 1;

    // Nl strip sub-strings
    public static final String CYC_NL_STRIP_SUBSTRINGS[][]                    = { { "StatementResponse-", "0" }, { "The survey respondent ", "0" },
            { "the survey question derived from the statement: ", "1" }      };

    // Audience insights statements groups
    public static final String AUDIENCE_INSIGHTS_interestingDataSentence_GROUP_ID   = "interestingDataSentence";
    public static final String AUDIENCE_INSIGHTS_totalPercentageOfAudienceFrequency_GROUP_ID   = "totalPercentageOfAudienceFrequency";
    public static final String AUDIENCE_INSIGHTS_contrastingSentencesForAudience_GROUP_ID   = "contrastingSentencesForAudience";
    
    
    
    //(and (interestingDataSentence-VerticalPercent 
    //        TestAudience-Target (QuestionResponseUnitFn (SurveyQuestionFromBinPredFn hasBiologicalSex 2) (TheSet Female)) 63.88) 
    //        (interestingDataSentence-Index TestAudience-Target (QuestionResponseUnitFn (SurveyQuestionFromBinPredFn hasBiologicalSex 2) (TheSet Female)) 123))

    
    public static final String AUDIENCE_INSIGHTS_interestingDataSentence_TMPL = "<?VERT>% of your target audience answered <Question from ?QRU> with <Answer 1 from ?QRU>[, <Answer 2 from ?QRU> _, or <Answer n from ?QRU>]; this is [(<?INDEX> - 100)%  higher | (100 - <?INDEX>) % lower] than the proportion in the general population.";

    
    
    //(totalPercentageOfAudienceFrequencyAtLeast MediaPlanningDataSources-MRI-2015-Doublebase TestAudience-Target 
    //        (SurveyQuestionFromStatementFn ShoppedAtDaveMart-Last30Days-Specification) 1 48.02)
    
    public static final String AUDIENCE_INSIGHTS_contrastingSentencesForAudience_TMPL = "Your target audience is defined according to this sentence: <?SENTENCE-1 in English with quotations>.  However, a significant percentage of your audience can also be described by this somewhat contradictory sentence <?SENTENCE-2 in english with quotations>.  This result was unexpected.";

    
    
    //(totalPercentageOfAudienceFrequencyAtLeast MediaPlanningDataSources-MRI-2015-Doublebase TestAudience-Target 
    //        (SurveyQuestionFromStatementFn ShoppedAtDaveMart-Last30Days-Specification) 1 48.02)
    
    public static final String AUDIENCE_INSIGHTS_totalPercentageOfAudienceFrequency_TMPL = "For reference: <?TOTAL>% of your audience responded with an answer greater than or equal to <?NUM> to this question : <?QUESTION in english in quotations>";

}
