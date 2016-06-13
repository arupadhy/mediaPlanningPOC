package com.sapient.ai.media.dto;

public class BaseDtoItem
{
    String error      = "";
    int    statusCode = 200;

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

}
