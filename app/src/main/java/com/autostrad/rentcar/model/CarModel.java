package com.autostrad.rentcar.model;

import org.json.JSONArray;

public class CarModel {

    String id;
    String car_name;
    String transmission_name;
    String fuel_type_name;
    String group_name;
    String category_name;
    String air_bags;
    String air_conditioner;
    String parking_sensors;
    String rear_parking_camera;
    String bluetooth;
    String cruise_control;
    String sunroof;
    String car_image;
    String door;
    String passenger;
    String suitcase;
    String pay_later_rate;
    String pay_now_rate;
    JSONArray more_car_image;

    public JSONArray getMore_car_image() {
        return more_car_image;
    }

    public void setMore_car_image(JSONArray more_car_image) {
        this.more_car_image = more_car_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getTransmission_name() {
        return transmission_name;
    }

    public void setTransmission_name(String transmission_name) {
        this.transmission_name = transmission_name;
    }

    public String getFuel_type_name() {
        return fuel_type_name;
    }

    public void setFuel_type_name(String fuel_type_name) {
        this.fuel_type_name = fuel_type_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getAir_bags() {
        return air_bags;
    }

    public void setAir_bags(String air_bags) {
        this.air_bags = air_bags;
    }

    public String getAir_conditioner() {
        return air_conditioner;
    }

    public void setAir_conditioner(String air_conditioner) {
        this.air_conditioner = air_conditioner;
    }

    public String getParking_sensors() {
        return parking_sensors;
    }

    public void setParking_sensors(String parking_sensors) {
        this.parking_sensors = parking_sensors;
    }

    public String getRear_parking_camera() {
        return rear_parking_camera;
    }

    public void setRear_parking_camera(String rear_parking_camera) {
        this.rear_parking_camera = rear_parking_camera;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getCruise_control() {
        return cruise_control;
    }

    public void setCruise_control(String cruise_control) {
        this.cruise_control = cruise_control;
    }

    public String getSunroof() {
        return sunroof;
    }

    public void setSunroof(String sunroof) {
        this.sunroof = sunroof;
    }

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getSuitcase() {
        return suitcase;
    }

    public void setSuitcase(String suitcase) {
        this.suitcase = suitcase;
    }

    public String getPay_later_rate() {
        return pay_later_rate;
    }

    public void setPay_later_rate(String pay_later_rate) {
        this.pay_later_rate = pay_later_rate;
    }

    public String getPay_now_rate() {
        return pay_now_rate;
    }

    public void setPay_now_rate(String pay_now_rate) {
        this.pay_now_rate = pay_now_rate;
    }
}
