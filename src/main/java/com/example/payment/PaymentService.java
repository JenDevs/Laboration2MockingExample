package com.example.payment;

public interface PaymentService {
    PaymentApiResponse charge(String apiKey, double amount);
}
