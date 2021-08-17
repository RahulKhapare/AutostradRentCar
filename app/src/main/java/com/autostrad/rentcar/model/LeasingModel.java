package com.autostrad.rentcar.model;

import org.json.JSONArray;

public class LeasingModel {

    String title;
    JSONArray jsonArray;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
