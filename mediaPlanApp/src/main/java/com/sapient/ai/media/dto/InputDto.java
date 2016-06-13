package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

public class InputDto extends KbDtoItem
{
    List<InputDtoItem> parameters = new ArrayList<InputDtoItem>();

    public List<InputDtoItem> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<InputDtoItem> parameters)
    {
        this.parameters = parameters;
    }

    public void addParameters(InputDtoItem parameter)
    {
        parameters.add(parameter);
    }

}
