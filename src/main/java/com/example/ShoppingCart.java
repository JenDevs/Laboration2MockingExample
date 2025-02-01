package com.example;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private List<Item> items;
    private double discount = 0.0;

    public ShoppingCart() {
        items = new ArrayList<>();
    }
    public void addItem(Item item, int quantity) {
        if(quantity <= 0)
            return;

        for (Item existingItem : items) {
            if(existingItem.getName().equals(item.getName())) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                return;
            }
         }

        item.setQuantity(quantity);
        items.add(item);

    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public int getNumberOfItems() {
        return items.size();
    }

    public void removeItem(Item item, int quantity) {
        if(quantity <= 0)
            return;

        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getName().equals(item.getName())) {
                int newQuantity = items.get(i).getQuantity() - quantity;
                if(newQuantity > 0) {
                    items.get(i).setQuantity(newQuantity);
                } else {
                    items.remove(i);
                }
                return;
            }
        }

    }

    public double calculateTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

    }


    public void applyDiscount(double discountInPercentage) {
        if(discountInPercentage < 0 || discountInPercentage > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        this.discount = discountInPercentage;
    }

    public double priceAfterDiscount() {
        double totalPrice = calculateTotalPrice();
        return totalPrice * (1 - discount / 100);
    }
}
