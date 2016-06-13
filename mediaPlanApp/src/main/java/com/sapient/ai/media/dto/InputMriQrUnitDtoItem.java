package com.sapient.ai.media.dto;

import java.util.Arrays;

public class InputMriQrUnitDtoItem extends KbDtoItem
{
    String operator;
    String question;
    String answers[];

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String[] getAnswers()
    {
        return answers;
    }

    public void setAnswers(String[] answers)
    {
        this.answers = answers;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(answers);
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((question == null) ? 0 : question.hashCode());
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
        InputMriQrUnitDtoItem other = (InputMriQrUnitDtoItem) obj;
        if (!Arrays.equals(answers, other.answers))
            return false;
        if (operator == null)
        {
            if (other.operator != null)
                return false;
        }
        else if (!operator.equals(other.operator))
            return false;
        if (question == null)
        {
            if (other.question != null)
                return false;
        }
        else if (!question.equals(other.question))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "InputQueryDtoItem [operator=" + operator + ", question=" + question + ", answers=" + Arrays.toString(answers) + "]";
    }

}
