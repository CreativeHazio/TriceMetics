package com.creativehazio.tricesignature.model;

public class ProductCategory {
    private String category;
    private int imageResourceId;

    public ProductCategory(String category, int imageResourceId) {
        this.category = category;
        this.imageResourceId = imageResourceId;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
