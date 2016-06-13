package com.sapient.ai.media.dto;

import com.sapient.ai.cyc.session.MpConstants;

public class ReachDto extends BaseDtoItem
{
    int currentReach;
    int targetReach;
    int reachGap;
    int vertical;
    int horizontal;
    int index;

    public ReachDto()
    {
    }

    public ReachDto(String error)
    {
        if (error != null)
        {
            setError(error);
            setStatusCode(MpConstants.ERROR_CODE);
        }
    }

    public int getCurrentReach()
    {
        return currentReach;
    }

    public void setCurrentReach(int currentReach)
    {
        this.currentReach = currentReach;
    }

    public int getTargetReach()
    {
        return targetReach;
    }

    public void setTargetReach(int targetReach)
    {
        this.targetReach = targetReach;
    }

    public int getReachGap()
    {
        return reachGap;
    }

    public void setReachGap(int reachGap)
    {
        this.reachGap = reachGap;
    }

    public int getVertical()
    {
        return vertical;
    }

    public void setVertical(int vertical)
    {
        this.vertical = vertical;
    }

    public int getHorizontal()
    {
        return horizontal;
    }

    public void setHorizontal(int horizontal)
    {
        this.horizontal = horizontal;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

}
