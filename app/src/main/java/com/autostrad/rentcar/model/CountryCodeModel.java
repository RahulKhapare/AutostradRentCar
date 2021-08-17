package com.autostrad.rentcar.model;

public class CountryCodeModel {

    String id;
    String country_name;
    String phone_code;

    public CountryCodeModel(String phone_code) {
        this.phone_code = phone_code;
    }

    public CountryCodeModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }
}
