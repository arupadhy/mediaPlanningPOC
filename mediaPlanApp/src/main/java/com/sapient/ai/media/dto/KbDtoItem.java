package com.sapient.ai.media.dto;

import com.sapient.ai.media.utils.MpUtil;

public class KbDtoItem
{
    String nl;
    String cycl;

    public KbDtoItem()
    {
    }

    public KbDtoItem(String nl, String cycl)
    {
        if (nl != null)
            this.nl = nl;
        if (cycl != null)
            this.cycl = cycl;
    }

    public String getNl()
    {
        return MpUtil.stripNl(nl);
    }

    public void setNl(String nl)
    {
        this.nl = nl;
    }

    public String getCycl()
    {
        return cycl;
    }

    public void setCycl(String cycl)
    {
        this.cycl = cycl;
    }

}
