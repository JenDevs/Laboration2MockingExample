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

        for(Item itemExist : items) {
            if(itemExist == item) {
                itemExist.setQuantity(itemExist.getQuantity() + quantity);
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

        for(Item itemExist : items) {
            if(itemExist == item) {
                int newQuantity = itemExist.getQuantity() - quantity;
                if(newQuantity > 0) {
                    itemExist.setQuantity(newQuantity);
                } else {
                    items.remove(itemExist);
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
