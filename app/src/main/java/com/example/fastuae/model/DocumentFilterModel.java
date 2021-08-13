package com.example.fastuae.model;

import org.json.JSONArray;

public class DocumentFilterModel {

    JSONArray document_key;
    JSONArray field_for;
    String title;

    public JSONArray getDocument_key() {
        return document_key;
    }

    public void setDocument_key(JSONArray document_key) {
        this.document_key = document_key;
    }

    public JSONArray getField_for() {
        return field_for;
    }

    public void setField_for(JSONArray field_for) {
        this.field_for = field_for;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
