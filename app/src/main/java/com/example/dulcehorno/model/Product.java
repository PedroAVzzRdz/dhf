package com.example.dulcehorno.model;

public class Product {
    private String name;
    private double price;
    private int drawableResId; // Recurso drawable

    public Product(String name, double price, int drawableResId) {
        this.name = name;
        this.price = price;
        this.drawableResId = drawableResId;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getDrawableResId() { return drawableResId; }
}
