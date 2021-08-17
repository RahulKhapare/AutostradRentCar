package com.autostrad.rentcar.model;

import com.adoisstudio.helper.Json;

public class FieldModel {
    String filed;
    Json json;

    public FieldModel(String filed,Json json) {
        this.filed = filed;
        this.json = json;
    }

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }
}
