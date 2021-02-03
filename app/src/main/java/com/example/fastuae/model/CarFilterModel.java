package com.example.fastuae.model;

public class CarFilterModel {

    String id;
    String name;
    String status;

    public CarFilterModel(String name) {
        this.name = name;
    }

    public CarFilterModel() {

    }

    public CarFilterModel(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
