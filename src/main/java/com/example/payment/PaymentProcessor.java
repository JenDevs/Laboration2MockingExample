package com.example.payment;


import java.sql.SQLException;

public class PaymentProcessor {
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final DatabaseConnection databaseConnection;


    public PaymentProcessor(PaymentService paymentService, EmailService emailService, DatabaseConnection databaseConnection) {
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.databaseConnection = databaseConnection;
    }

    private static final String API_KEY = "sk_test_123456";

    public boolean processPayment(double amount) throws SQLException {

        PaymentApiResponse response = paymentService.charge(API_KEY, amount);


        if (response.isSuccess()) {
            databaseConnection.executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
        }


        if (response.isSuccess()) {
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
