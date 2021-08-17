package com.autostrad.rentcar.model;

import org.json.JSONArray;

public class CarUpgradeModel {

    String car_id;
    String car_name;
    String car_image;
    String amount_difference;
    JSONArray more_car_image;

    public JSONArray getMore_car_image() {
        return more_car_image;
    }

    public void setMore_car_image(JSONArray more_car_image) {
        this.more_car_image = more_car_image;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public String getAmount_difference() {
        return amount_difference;
    }

    public void setAmount_difference(String amount_difference) {
        this.amount_difference = amount_difference;
    }
}
