package com.example.dotechuser.Model;

public class Category {
    private String strCategory;
    private String categoryId;

    public Category() {
    }


    public Category(String strCategory) {
        this.strCategory = strCategory;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }
}
