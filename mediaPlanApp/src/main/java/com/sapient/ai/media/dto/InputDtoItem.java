package com.sapient.ai.media.dto;

public class InputDtoItem extends KbDtoItem
{
    String key   = "";
    String value = "";

    public InputDtoItem()
    {
    }

    public InputDtoItem(String key, String value)
    {
        if (key != null)
            this.key = key;
        if (value != null)
            this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}
