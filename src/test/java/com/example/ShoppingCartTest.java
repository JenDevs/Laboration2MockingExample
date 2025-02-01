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
        book = new Item(150);
        record = new Item(200);
    }

    @Test
    void addOneItemInShoppingCart() {
        shoppingCart.addItem(book, 1);
        assertEquals(1, book.getQuantity());
        assertEquals(1, shoppingCart.getTotalQuantity());

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

    @Test
    void addZeroItemsDoesNotChangeShoppingCart() {
        shoppingCart.addItem(record, 0);

        assertEquals(0, shoppingCart.getTotalQuantity());
        assertEquals(0, shoppingCart.getNumberOfItems());
    }

    @Test
    void cannotAddNegativeQuantity() {
        shoppingCart.addItem(book, 1);
        shoppingCart.addItem(book, -1);

        assertEquals(1, shoppingCart.getTotalQuantity());
        assertEquals(1, shoppingCart.getNumberOfItems());
    }

    @Test
    void addLargeQuantityOfItemWorks() {
        shoppingCart.addItem(book, 100);
        shoppingCart.addItem(record, 100);

        assertEquals(200, shoppingCart.getTotalQuantity());
        assertEquals(2, shoppingCart.getNumberOfItems());
    }

    @Test
    void removeNegativeQuantityDoesNotChangeShoppingCart() {
        shoppingCart.addItem(book, 2);
        shoppingCart.removeItem(book, -1);

        assertEquals(2, shoppingCart.getTotalQuantity());
        assertEquals(1, shoppingCart.getNumberOfItems());
    }

    @Test
    void removingZeroItemDoesNotChangeShoppingCart() {
        shoppingCart.addItem(book, 1);
        shoppingCart.removeItem(book, 0);

        assertEquals(1, shoppingCart.getTotalQuantity());
        assertEquals(1, shoppingCart.getNumberOfItems());
    }

    @Test
    void removingOneItemRemoveOneItemInShoppingCart () {
        shoppingCart.addItem(book, 3);
        shoppingCart.removeItem(book, 1);

        assertEquals(2, shoppingCart.getTotalQuantity());
        assertEquals(1, shoppingCart.getNumberOfItems());
    }

    @Test
    void removingAllOfAnItemRemovesItFromShoppingCart() {
        shoppingCart.addItem(book, 3);
        shoppingCart.removeItem(book, 3);

        assertEquals(0, shoppingCart.getTotalQuantity());
        assertEquals(0, shoppingCart.getNumberOfItems());
    }

    @Test
    void removingMoreItemsThenAvailableResultsInZeroItem () {
        shoppingCart.addItem(book, 3);
        shoppingCart.removeItem(book, 5);

        assertEquals(0, shoppingCart.getTotalQuantity());
        assertEquals(0, shoppingCart.getNumberOfItems());
    }

    @Test
    void removingItemThatDoesNotExistInShoppingCartDoesNotChangeShoppingCart() {
        shoppingCart.addItem(book, 3);
        shoppingCart.removeItem(book, 3);
        shoppingCart.removeItem(book, 1);
        assertEquals(0, shoppingCart.getTotalQuantity());
        assertEquals(0, shoppingCart.getNumberOfItems());
    }

    @Test
    void removeLargeQuantityOfItemDoesRemoveTheLargeAmountOfItem () {
        shoppingCart.addItem(book, 100);
        shoppingCart.addItem(record, 100);
        shoppingCart.removeItem(book, 100);
        assertEquals(100, shoppingCart.getTotalQuantity());
        assertEquals(1, shoppingCart.getNumberOfItems());
    }

    @Test
    void calculateTotalPriceReturnsCorrectSum () {
        shoppingCart.addItem(book, 3);
        shoppingCart.addItem(record, 5);

        double expectedTotalPrice = (3 * book.getPrice()) + (5 * record.getPrice());
        double actualTotalPrice = shoppingCart.calculateTotalPrice();
        assertEquals(expectedTotalPrice,actualTotalPrice);

    }






}
