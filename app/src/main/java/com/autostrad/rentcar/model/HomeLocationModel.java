package com.autostrad.rentcar.model;

import com.adoisstudio.helper.JsonList;

public class HomeLocationModel {

    String id;
    String emirate_id;
    String emirate_name;
    String location_name;
    String address;
    String status;
    String contact_number;
    String contact_email;
    JsonList location_time_data;

    public HomeLocationModel(String location_name) {
        this.location_name = location_name;
    }

    public HomeLocationModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmirate_id() {
        return emirate_id;
    }

    public void setEmirate_id(String emirate_id) {
        this.emirate_id = emirate_id;
    }

    public String getEmirate_name() {
        return emirate_name;
    }

    public void setEmirate_name(String emirate_name) {
        this.emirate_name = emirate_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public JsonList getLocation_time_data() {
        return location_time_data;
    }

    public void setLocation_time_data(JsonList location_time_data) {
        this.location_time_data = location_time_data;
    }
}
