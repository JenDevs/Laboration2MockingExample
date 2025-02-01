package com.example;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {

    @Test
    void addOneItemInShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Item item = new Item();
        shoppingCart.addItem(item, 1);
        assertEquals(1, item.getQuantity());


    }



}
