package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

import com.sapient.ai.cyc.session.MpConstants;

public class MriFieldsDto extends BaseDtoItem
{
    List<MriFieldDtoItem> mriFieldConstraints = new ArrayList<MriFieldDtoItem>();

    public MriFieldsDto()
    {
    }

    public MriFieldsDto(String error)
    {
        if (error != null)
        {
            setError(error);
            setStatusCode(MpConstants.ERROR_CODE);
        }
    }

    public MriFieldsDto(List<MriFieldDtoItem> mriFieldConstraints)
    {
        if (mriFieldConstraints != null)
            this.mriFieldConstraints = mriFieldConstraints;
    }

    public List<MriFieldDtoItem> getMriFieldConstraints()
    {
        return mriFieldConstraints;
    }

    public void setMriFieldConstraints(List<MriFieldDtoItem> mriFieldConstraints)
    {
        this.mriFieldConstraints = mriFieldConstraints;
    }



}
