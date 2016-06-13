package com.sapient.ai.media.dto;

import java.util.ArrayList;
import java.util.List;

public class MriFieldDtoItem extends QueryAnswerDtoItem
{
    KbDtoItem              question;
    List<MriAnswerDtoItem> answers = new ArrayList<MriAnswerDtoItem>();

    public List<MriAnswerDtoItem> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<MriAnswerDtoItem> answers)
    {
        this.answers = answers;
    }

    public KbDtoItem getQuestion()
    {
        return question;
    }

    public void setQuestion(KbDtoItem question)
    {
        this.question = question;
    }

    public void setQuestion(String questionNl, String questionCycl)
    {
        this.question = new KbDtoItem(questionNl, questionCycl);
    }
}
