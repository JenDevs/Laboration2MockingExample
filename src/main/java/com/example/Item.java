package com.example;

public class Item {

    private int quantity;
    private final double price;
    private final String name;

    public Item(String name, double price) {
        this.price = price;
        this.name = name;
        this.quantity = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

}
