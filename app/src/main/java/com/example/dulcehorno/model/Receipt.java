package com.example.dulcehorno.model;

import java.util.List;

public class Receipt {
    private String id;
    private String date;
    private List<Product> products;
    private double total;

    public Receipt(String id, String date, List<Product> products, double total) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.total = total;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public List<Product> getProducts() { return products; }
    public double getTotal() { return total; }
}
