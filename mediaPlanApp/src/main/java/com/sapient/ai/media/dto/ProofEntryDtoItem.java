package com.sapient.ai.media.dto;

public class ProofEntryDtoItem
{
    String title;
    String content;

    public ProofEntryDtoItem()
    {
    }

    public ProofEntryDtoItem(String title, String content)
    {
        if (title != null)
            this.title = title;
        if (content != null)
            this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}
