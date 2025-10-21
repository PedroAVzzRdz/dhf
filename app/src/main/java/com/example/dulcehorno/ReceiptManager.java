package com.example.dulcehorno;

import com.example.dulcehorno.model.Receipt;
import java.util.ArrayList;
import java.util.List;

public class ReceiptManager {
    private static ReceiptManager instance;
    private final List<Receipt> receipts = new ArrayList<>();

    private ReceiptManager() {}

    public static ReceiptManager getInstance() {
        if (instance == null) instance = new ReceiptManager();
        return instance;
    }

    // Agrega un recibo (al frente para mostrar el más reciente primero)
    public void addReceipt(Receipt r) {
        receipts.add(0, r);
    }

    // Obtiene todos los recibos
    public List<Receipt> getReceipts() {
        return receipts;
    }

    // Limpia todos los recibos almacenados
    public void clearReceipts() {
        receipts.clear();
    }
}
