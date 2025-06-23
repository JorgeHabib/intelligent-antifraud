package com.jorgehabib.intelligent_antifraud.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;

@Service
public class TransactionEventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishTransactionEvent(String tenantId, Transaction transactionDTO) {
        rabbitTemplate.convertAndSend("transaction.events", tenantId, transactionDTO);
    }
}
