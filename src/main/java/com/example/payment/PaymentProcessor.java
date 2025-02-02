/*
 * Refaktoreringsbeslut:
 * 1. Skapat interfaces för att lättare testa och separera beroenden.
 * 2. Använder dependency injection så beroenden skickas in via konstruktorn.
 * 3. Vid testning används mock-objekt istället för riktiga implementationer.
 * 4. Enhetstesterna testar lyckat, misslyckat och när det är fel summa.
 * 5. API_KEY är en statisk variabel för autentisering mot betaltjänsten.
 * 6. Interfaces gör koden flexibel och enkel att ändra.
 * 7. Record gör att koden blir mindre och immutable, enklare underhåll och testning.
 */

package com.example.payment;

import java.sql.SQLException;

public class PaymentProcessor {
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final DatabaseConnection databaseConnection;
    private static final String API_KEY = "sk_test_123456";


    public PaymentProcessor(PaymentService paymentService, EmailService emailService, DatabaseConnection databaseConnection, String apiKey) {
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.databaseConnection = databaseConnection;
    }

    public boolean processPayment(double amount) throws SQLException {

        PaymentApiResponse response = paymentService.charge(API_KEY, amount);


        if (response.success()) {
            databaseConnection.executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
        }


        if (response.success()) {
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.success();
    }
}
