package com.thiruacademy.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.gl.base_domains.dto.Order;
import com.gl.base_domains.dto.OrderEvent;
import com.gl.base_domains.dto.OrderItem;
import com.gl.base_domains.dto.Payment;
import com.thiruacademy.dto.SMSStatus;
import com.thiruacademy.dto.SmsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.thiruacademy.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

	@Autowired
	private TwilioConfig twilioConfig;

	@KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
	public SmsResponseDto sendOrderSMS(OrderEvent event) {
		SmsResponseDto smsResponseDto = null;
		Order order = event.getOrder();
		try {
			PhoneNumber to = new PhoneNumber(order.getPhoneNumber());//to
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from

			String orderMessage = String.format(
					"Dear %s, thank you for your order! Your order for %s has been successfully placed. " +
							"Order Id: %s, Quantity: %d, Total Amount: %.2f. " +
							"We will notify you once your order is delivered to your Address: %s. Thank you for shopping with us!",
					order.getCustomerName(),
					order.getItems().stream().map(OrderItem::getProductName).collect(Collectors.joining(", ")),
					order.getOrderId(),
					order.getItems().stream().mapToInt(OrderItem::getQuantity).sum(), // Summing the quantities
					order.getTotalAmount(),
					order.getDeliveryAddress()
			);
			Message message = Message
			        .creator(to, from,
			                orderMessage)
			        .create();
			smsResponseDto = new SmsResponseDto(SMSStatus.DELIVERED, orderMessage);
		} catch (Exception e) {
			e.printStackTrace();
			smsResponseDto = new SmsResponseDto(SMSStatus.FAILED, e.getMessage());
		}
		return smsResponseDto;
	}

	@KafkaListener(topics = "${spring.kafka.payment.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
	public SmsResponseDto sendPaymentSMS(Payment payment) {
		SmsResponseDto smsResponseDto = null;
		try {
			PhoneNumber to = new PhoneNumber(payment.getPhoneNumber());//to
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from

			String paymentMessage = String.format(
					"Dear Customer, thank you for your payment! Your payment of %.2f %s has been successfully processed. " +
							"Payment Id: %s, Payment Method: %s. " +
							"Thank you for your business!",
					payment.getAmount(),
					"\u20B9", // Rupee symbol Unicode character
					payment.getPaymentId(),
					payment.getPaymentMethod()
			);
			Message message = Message
					.creator(to, from,
							paymentMessage)
					.create();
			smsResponseDto = new SmsResponseDto(SMSStatus.DELIVERED, paymentMessage);
		} catch (Exception e) {
			e.printStackTrace();
			smsResponseDto = new SmsResponseDto(SMSStatus.FAILED, e.getMessage());
		}
		return smsResponseDto;
	}

}
