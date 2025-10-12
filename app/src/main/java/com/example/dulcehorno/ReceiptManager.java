// ReceiptManager.java
package com.example.dulcehorno;

import java.util.ArrayList;
import java.util.List;

import com.example.dulcehorno.model.Receipt;

public class ReceiptManager {
    private static ReceiptManager instance;
    private final List<Receipt> receipts = new ArrayList<>();

    private ReceiptManager() {}

    public static ReceiptManager getInstance() {
        if (instance == null) instance = new ReceiptManager();
        return instance;
    }

    public void addReceipt(Receipt r) {
        receipts.add(0, r); // agrega al frente (más reciente primero)
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }
}
