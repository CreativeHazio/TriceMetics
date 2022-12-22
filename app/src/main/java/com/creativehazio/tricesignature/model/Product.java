package com.creativehazio.tricesignature.model;

public class Product {
    private String category;
//    private String currentAdmin;
    private String description;
    private String image;
    private String name;
    private double price;
    private int stockUnit;
    private String id;

    public Product() {
    }

    public Product(String id, String image, String name, String description, String category, double price, int stockUnit) {
//        this.currentAdmin = currentAdmin;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stockUnit = stockUnit;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStockUnit() {
        return stockUnit;
    }

    public String getId() {
        return id;
    }

    //    public String getCurrentAdmin() {
//        return currentAdmin;
//    }
}
