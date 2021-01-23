package com.example.fastuae.model;

public class FAQModel {

    String question;
    String answer;
    String faq_category_name;
    boolean clickFlag;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFaq_category_name() {
        return faq_category_name;
    }

    public void setFaq_category_name(String faq_category_name) {
        this.faq_category_name = faq_category_name;
    }

    public boolean isClickFlag() {
        return clickFlag;
    }

    public void setClickFlag(boolean clickFlag) {
        this.clickFlag = clickFlag;
    }
}
