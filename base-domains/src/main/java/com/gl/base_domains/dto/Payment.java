package com.gl.base_domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String paymentId;
    private String customerId;
    private String customerName;
    private String phoneNumber;
    private double amount;
    private String paymentMethod;
}
