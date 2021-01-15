package com.example.fastuae.model;

public class FAQModel {

    String title;
    String description;
    boolean clickFlag;


    public FAQModel(String title, String description,boolean clickFlag) {
        this.title = title;
        this.description = description;
        this.clickFlag = clickFlag;
    }

    public boolean isClickFlag() {
        return clickFlag;
    }

    public void setClickFlag(boolean clickFlag) {
        this.clickFlag = clickFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
