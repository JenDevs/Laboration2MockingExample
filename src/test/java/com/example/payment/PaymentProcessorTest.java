package com.example.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class PaymentProcessorTest {

    private PaymentProcessor paymentProcessor;
    private DatabaseConnection databaseConnection;
    private EmailService emailService;
    private PaymentService paymentService;
    private static final String API_KEY_TEST = "sk_test_123456";

    @BeforeEach
    void setUp() {
        databaseConnection = mock(DatabaseConnection.class);
        emailService = mock(EmailService.class);
        paymentService = mock(PaymentService.class);
        paymentProcessor = new PaymentProcessor(paymentService, emailService, databaseConnection, API_KEY_TEST);

    }

    @Test
    void  processPaymentThrowsIllegalArgumentExceptionWhenAmountIsInvalid() {
        double invalidAmount = 0.0;

        assertThrows(NullPointerException.class, () -> paymentProcessor.processPayment(invalidAmount));

    }

    @Test
    void processPaymentReturnsFalseWhenChargeFails() throws Exception {
        double amount = 100.0;

        when(paymentService.charge(eq(API_KEY_TEST),eq(amount))).thenReturn(new PaymentApiResponseImplementation(false));

        boolean result = paymentProcessor.processPayment(amount);

        assertFalse(result);
        verify(paymentService).charge(eq(API_KEY_TEST),eq(amount));
        verify(databaseConnection, never()).executeUpdate(anyString());
        verify(emailService, never()).sendPaymentConfirmation(anyString(), anyDouble());

    }

    @Test
    void processPaymentReturnsTrueAndProcessActionsWhenChargeSucceeds() throws Exception {
        double amount = 100.0;
        when(paymentService.charge(eq(API_KEY_TEST),eq(amount))).thenReturn(new PaymentApiResponseImplementation(true));

        boolean result = paymentProcessor.processPayment(amount);

        assertTrue(result);
        verify(paymentService).charge(eq(API_KEY_TEST),eq(amount));
        verify(databaseConnection).executeUpdate(anyString());
        verify(emailService).sendPaymentConfirmation(anyString(), anyDouble());

    }

}
