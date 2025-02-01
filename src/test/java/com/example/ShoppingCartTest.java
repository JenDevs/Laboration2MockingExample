package com.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {
    private ShoppingCart shoppingCart;
    private Item book;
    private Item record;


    @BeforeEach
    public void setUp() {
        shoppingCart = new ShoppingCart();
        book = new Item();
        record = new Item();
    }

    @Test
    void addOneItemInShoppingCart() {
        shoppingCart.addItem(book, 1);
        assertEquals(1, book.getQuantity());
        assertEquals(1, shoppingCart.getTotalQuantity());

    }

    @Test
    void totalQuantityInShoppingCartIsCorrect() {
        shoppingCart.addItem(book, 2);
        assertEquals(2, shoppingCart.getTotalQuantity());
    }

    @Test
    void addDifferentItemsToShoppingCart() {
        shoppingCart.addItem(book, 1);
        shoppingCart.addItem(record, 2);

        assertEquals(3, shoppingCart.getTotalQuantity());
        assertEquals(2, shoppingCart.getNumberOfItems());
    }

    @Test
    void addMoreThanOneOfTheSameItemInShoppingCart() {
        shoppingCart.addItem(book, 1);
        shoppingCart.addItem(book, 2);

        assertEquals(3, book.getQuantity());
        assertEquals(3, shoppingCart.getTotalQuantity());
        assertEquals(1, shoppingCart.getNumberOfItems());

    }


}
