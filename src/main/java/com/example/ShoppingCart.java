package com.example;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private List<Item> items;

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
}
