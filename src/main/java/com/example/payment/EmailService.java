package com.example.payment;

public interface EmailService {
    void sendPaymentConfirmation(String emailAddress, double amount);
}
