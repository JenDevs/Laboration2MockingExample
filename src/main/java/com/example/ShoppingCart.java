package com.example;

public class ShoppingCart {

    private int totalQuantity = 0;

    public void addItem(Item item, int quantity) {
        item.setQuantity(quantity);
        totalQuantity += quantity;

    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
