package com.sapient.ai.media.dto;

public class QueryAnswerKbDtoItem extends KbDtoItem
{
    String index;

    public QueryAnswerKbDtoItem()
    {
    }

    public QueryAnswerKbDtoItem(String index)
    {
        this.index = index;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

}
