package com.sapient.ai.media.dto;

import java.util.Arrays;

import com.sapient.ai.cyc.session.MpConstants;

public class MpInputConstraints
{
    private SelectedSentence[] sentences;
    private String task = MpConstants.MP_DEMO_TASK;
    private String target = MpConstants.MP_DEMO_TARGET;
    private String mriSource = MpConstants.MP_DEMO_MRI_SOURCE;

    public SelectedSentence[] getSentences()
    {
        return sentences;
    }

    public void setSentences(SelectedSentence[] setences)
    {
        this.sentences = setences;
    }

    public String getTask()
    {
        return task;
    }

    public void setTask(String task)
    {
        this.task = task;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getMriSource()
    {
        return mriSource;
    }

    public void setMriSource(String mriSource)
    {
        this.mriSource = mriSource;
    }

    @Override
    public String toString()
    {
        return "MpInputConstraints [sentences=" + Arrays.toString(sentences) + ", task=" + task + ", target=" + target + ", mriSource=" + mriSource + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mriSource == null) ? 0 : mriSource.hashCode());
        result = prime * result + Arrays.hashCode(sentences);
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        result = prime * result + ((task == null) ? 0 : task.hashCode());
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
        MpInputConstraints other = (MpInputConstraints) obj;
        if (mriSource == null)
        {
            if (other.mriSource != null)
                return false;
        }
        else if (!mriSource.equals(other.mriSource))
            return false;
        if (!Arrays.equals(sentences, other.sentences))
            return false;
        if (target == null)
        {
            if (other.target != null)
                return false;
        }
        else if (!target.equals(other.target))
            return false;
        if (task == null)
        {
            if (other.task != null)
                return false;
        }
        else if (!task.equals(other.task))
            return false;
        return true;
    }
    
    
   

}
