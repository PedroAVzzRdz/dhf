package com.example.dulcehorno.model;

import java.io.Serializable;

public class Product implements Serializable {
    private final String id;
    private final String name;
    private final double price;
    private final String drawableResId;
    private final String description;
    private final String category;

    public Product(String id, String name, double price, String drawableResId, String description, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.drawableResId = drawableResId;
        this.description = description;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDrawableResId() { return drawableResId; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
}
