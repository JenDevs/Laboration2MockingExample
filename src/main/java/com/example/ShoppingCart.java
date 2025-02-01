package com.example;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private List<Item> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }
    public void addItem(Item item, int quantity) {
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
}
