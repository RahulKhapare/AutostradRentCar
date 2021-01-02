package com.example.fastuae.model;

public class CategoryModel {

    String categoryName;
    String categoryFlag;

    public void CategoryModel(String categoryFlag, String categoryName){
        this.categoryFlag = categoryFlag;
        this.categoryName = categoryName;
    }

    public String getCategoryFlag() {
        return categoryFlag;
    }

    public void setCategoryFlag(String categoryFlag) {
        this.categoryFlag = categoryFlag;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
