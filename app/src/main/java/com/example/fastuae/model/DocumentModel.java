package com.example.fastuae.model;

public class DocumentModel {

    String documentName;
    String documentDetails;
    String path;

    public DocumentModel(String documentName,String path) {
        this.documentName = documentName;
        this.path = path;
    }

    public DocumentModel() {

    }


    public String getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(String documentDetails) {
        this.documentDetails = documentDetails;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
