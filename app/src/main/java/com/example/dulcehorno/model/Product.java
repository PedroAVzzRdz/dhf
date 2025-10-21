package com.example.dulcehorno.model;

import java.io.Serializable;

public class Product implements Serializable {
    private final String id;
    private final String name;
    private final double price;
    private final String drawableResId;
    private final String description;
    private final String category;
    private final int availableUnits; // <-- NUEVO

    public Product(String id, String name, double price, String drawableResId, String description, String category, int availableUnits) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.drawableResId = drawableResId;
        this.description = description;
        this.category = category;
        this.availableUnits = availableUnits;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDrawableResId() { return drawableResId; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getAvailableUnits() { return availableUnits; }
}

