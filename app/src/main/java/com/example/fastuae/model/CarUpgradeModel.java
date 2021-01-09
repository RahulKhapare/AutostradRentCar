package com.example.fastuae.model;

public class CarUpgradeModel {

    int image;
    String carName;
    String reservationFee;
    String estimateTotal;

    public CarUpgradeModel(int image, String carName, String reservationFee, String estimateTotal) {
        this.image = image;
        this.carName = carName;
        this.reservationFee = reservationFee;
        this.estimateTotal = estimateTotal;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(String reservationFee) {
        this.reservationFee = reservationFee;
    }

    public String getEstimateTotal() {
        return estimateTotal;
    }

    public void setEstimateTotal(String estimateTotal) {
        this.estimateTotal = estimateTotal;
    }
}
