package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

import com.sapient.ai.cyc.session.MpConstants;

public class ConceptDto extends BaseDtoItem
{
    //TODO: revisit. this is a temporary solution to maintain session for cross-domain requests.
    String               sessionId = "";
    List<ConceptDtoItem> concepts  = new ArrayList<ConceptDtoItem>();

    public ConceptDto()
    {
    }

    public ConceptDto(List<ConceptDtoItem> concepts)
    {
        if (concepts != null)
            this.concepts = concepts;
    }

    public ConceptDto(String error)
    {
        if (error != null)
        {
            setError(error);
            setStatusCode(MpConstants.ERROR_CODE);
        }
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public List<ConceptDtoItem> getConcepts()
    {
        return concepts;
    }

    public void setConcepts(List<ConceptDtoItem> concepts)
    {
        this.concepts = concepts;
    }

}
