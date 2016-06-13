package com.sapient.ai.media.dto;

public class MriAnswerDtoItem extends KbDtoItem
{
    String mriCode = "";
    boolean recommended = false;

    public MriAnswerDtoItem()
    {
    }

    public MriAnswerDtoItem(String nl, String cycl, boolean recommended)
    {
        if (nl != null)
            this.nl = nl;
        if (cycl != null)
            this.cycl = cycl;
        
        this.recommended = recommended;
    }

    public boolean isRecommended()
    {
        return recommended;
    }

    public void setRecommended(boolean recommended)
    {
        this.recommended = recommended;
    }

    public String getMriCode()
    {
        return mriCode;
    }

    public void setMriCode(String mriCode)
    {
        this.mriCode = mriCode;
    }

}
