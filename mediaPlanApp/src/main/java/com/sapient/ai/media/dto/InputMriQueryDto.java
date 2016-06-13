package com.sapient.ai.media.dto;

import java.util.Arrays;

public class InputMriQueryDto
{
    private InputMriQrUnitDtoItem[] queryTerms;

    public InputMriQrUnitDtoItem[] getQueryTerms()
    {
        return queryTerms;
    }

    public void setQueryTerms(InputMriQrUnitDtoItem[] queryTerms)
    {
        this.queryTerms = queryTerms;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(queryTerms);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InputMriQueryDto other = (InputMriQueryDto) obj;
        if (!Arrays.equals(queryTerms, other.queryTerms))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "InputMriQueryDto [queryTerms=" + Arrays.toString(queryTerms) + "]";
    }

}
