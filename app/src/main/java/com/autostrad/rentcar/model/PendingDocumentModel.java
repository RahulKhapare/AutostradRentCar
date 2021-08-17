package com.autostrad.rentcar.model;

import com.adoisstudio.helper.Json;

import org.json.JSONArray;

import java.util.List;

public class PendingDocumentModel {

    String key;
    String title;
    JSONArray field;
    JSONArray field_for;
    String checkValue;
    List<FieldModel> fieldList;
    Json save_data;

    public List<FieldModel> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldModel> fieldList) {
        this.fieldList = fieldList;
    }

    public Json getSave_data() {
        return save_data;
    }

    public void setSave_data(Json save_data) {
        this.save_data = save_data;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONArray getField() {
        return field;
    }

    public void setField(JSONArray field) {
        this.field = field;
    }

    public JSONArray getField_for() {
        return field_for;
    }

    public void setField_for(JSONArray field_for) {
        this.field_for = field_for;
    }
}
