package com.gl.base_domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String orderId;
    private String customerName;
    private String phoneNumber;
    private double totalAmount;
    private String deliveryAddress;
    private List<OrderItem> items;

}
