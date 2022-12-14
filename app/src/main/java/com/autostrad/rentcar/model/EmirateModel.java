package com.autostrad.rentcar.model;

public class EmirateModel {

    String id;
    String emirate_name;
    String status;

    public EmirateModel(String emirate_name, String id) {
        this.emirate_name = emirate_name;
        this.id = id;
    }

    public EmirateModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmirate_name() {
        return emirate_name;
    }

    public void setEmirate_name(String emirate_name) {
        this.emirate_name = emirate_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
