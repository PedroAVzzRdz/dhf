package com.example.dulcehorno.model;

import java.util.List;

public class Receipt {
    private String id;
    private String date;
    private List<CartItem> items;
    private double total;

    public Receipt(String id, String date, List<CartItem> items, double total) {
        this.id = id;
        this.date = date;
        this.items = items;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
}
