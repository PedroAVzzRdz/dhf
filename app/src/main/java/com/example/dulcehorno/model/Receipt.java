package com.example.dulcehorno.model;

import java.util.List;

public class Receipt {
    private String id;
    private String requestDate;
    private String estimatedArrivalDate;
    private String deliveryLocation;
    private List<CartItem> items;
    private double total;

    public Receipt(String id, String requestDate, String estimatedArrivalDate,
                   String deliveryLocation, List<CartItem> items, double total) {
        this.id = id;
        this.requestDate = requestDate;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.deliveryLocation = deliveryLocation;
        this.items = items;
        this.total = total;
    }

    public String getId() { return id; }
    public String getRequestDate() { return requestDate; }
    public String getEstimatedArrivalDate() { return estimatedArrivalDate; }
    public String getDeliveryLocation() { return deliveryLocation; }
    public List<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
}

