package com.sapient.ai.media.dto;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "valid", "index" })
public class SentenceDtoItem extends KbDtoItem
{

    public SentenceDtoItem()
    {
    }

    public SentenceDtoItem(String nl, String cycl)
    {
        if (nl != null)
            this.nl = nl;
        if (cycl != null)
            this.cycl = cycl;
    }

    public boolean isValid()
    {
        if (StringUtils.isNotBlank(nl) && StringUtils.isNotBlank(cycl))
        {
            return true;
        }

        return false;
    }
}
