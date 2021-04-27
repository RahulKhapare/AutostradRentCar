package com.example.fastuae.model;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DocumentModel {

    String title;
    String key;
    JSONArray field;
    String checkValue;
    List<FieldModel> fieldList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JSONArray getField() {
        return field;
    }

    public void setField(JSONArray field) {
        this.field = field;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public List<FieldModel> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldModel> fieldList) {
        this.fieldList = fieldList;
    }
}
