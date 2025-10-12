package com.example.dulcehorno.model;

public class Product {
    private final String id;
    private final String name;
    private final double price;
    private final int drawableResId;

    public Product(String id, String name, double price, int drawableResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.drawableResId = drawableResId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getDrawableResId() { return drawableResId; }
}