package com.example.fastuae.model;

public class CountryCodeModel {

    String image;
    String code;

    public CountryCodeModel(String code, String image) {
        this.code = code;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
