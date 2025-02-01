package com.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {
    private ShoppingCart shoppingCart;
    private Item book;



    @BeforeEach
    public void setUp() {
        shoppingCart = new ShoppingCart();
        book = new Item();
    }

    @Test
    void addOneItemInShoppingCart() {
        shoppingCart.addItem(book, 1);
        assertEquals(1, book.getQuantity());
        assertEquals(1, shoppingCart.getTotalQuantity());

    }



}
