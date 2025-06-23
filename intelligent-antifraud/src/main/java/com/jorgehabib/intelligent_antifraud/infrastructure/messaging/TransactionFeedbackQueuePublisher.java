package com.jorgehabib.intelligent_antifraud.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.domain.model.TransactionResponse;


@Service
public class TransactionFeedbackQueuePublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(TransactionResponse feedback) {
        rabbitTemplate.convertAndSend("transaction.feedback.queue", feedback);
    }
}