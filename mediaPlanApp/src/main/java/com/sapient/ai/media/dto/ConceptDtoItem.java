package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "valid", "concepts", "cycl" })
public class ConceptDtoItem extends QueryAnswerKbDtoItem
{
    List<ConceptDtoItem>  concepts  = new ArrayList<ConceptDtoItem>();
    List<SentenceDtoItem> sentences = new ArrayList<SentenceDtoItem>();

    public ConceptDtoItem()
    {
    }

    public ConceptDtoItem(String nl, String cycl)
    {
        if (nl != null)
            this.nl = nl;
        if (cycl != null)
            this.cycl = cycl;
    }

    public ConceptDtoItem(List<ConceptDtoItem> concepts, List<SentenceDtoItem> sentences)
    {
        if (concepts != null)
            this.concepts = concepts;
        if (sentences != null)
            this.sentences = sentences;
    }

    public List<ConceptDtoItem> getConcepts()
    {
        return concepts;
    }

    public void setConcepts(List<ConceptDtoItem> concepts)
    {
        this.concepts = concepts;
    }

    public List<SentenceDtoItem> getSentences()
    {
        return sentences;
    }

    public void setSentences(List<SentenceDtoItem> sentences)
    {
        this.sentences = sentences;
    }

    public boolean isValid()
    {
        if ((sentences != null && !sentences.isEmpty()) || (concepts != null && !concepts.isEmpty()))
        {
            return true;
        }

        return false;
    }

}
