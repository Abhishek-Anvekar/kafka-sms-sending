package com.gl.payment_service.kafka;

import com.gl.base_domains.dto.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    @Value("${spring.kafka.payment.topic.name}")
    private String topicName;
    private KafkaTemplate<String, Payment> kafkaTemplate;


    public PaymentProducer(KafkaTemplate<String, Payment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Payment payment){
        Message<Payment> message = MessageBuilder
                .withPayload(payment)
                .setHeader(KafkaHeaders.TOPIC,topicName)
                .build();
        kafkaTemplate.send(message);
    }
}
