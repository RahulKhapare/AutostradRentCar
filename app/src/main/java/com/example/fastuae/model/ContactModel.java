package com.example.fastuae.model;

public class ContactModel {

    String area;
    String email;
    String number;

    public ContactModel(String area, String number, String email) {
        this.area = area;
        this.number = number;
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
