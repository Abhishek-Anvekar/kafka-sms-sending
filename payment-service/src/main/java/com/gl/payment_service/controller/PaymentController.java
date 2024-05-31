package com.gl.payment_service.controller;

import com.gl.base_domains.dto.Payment;
import com.gl.payment_service.kafka.PaymentProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    private PaymentProducer paymentProducer;

    public PaymentController(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }

    @PostMapping("/payment")
    public String makePayment(@RequestBody Payment payment){
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setCustomerId(UUID.randomUUID().toString());
        paymentProducer.sendMessage(payment);
        return "Payment successful!!";
    }
}
