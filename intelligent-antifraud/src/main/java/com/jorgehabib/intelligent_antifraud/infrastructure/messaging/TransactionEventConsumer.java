package com.jorgehabib.intelligent_antifraud.infrastructure.messaging;

import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {

    @RabbitListener(queues = "transaction.events")
    public void consume(Transaction transactionDTO) {
        persistTransaction(transactionDTO);
        updateFeedbackLoop(transactionDTO);
        updateTransactionsCache(transactionDTO);
    }

    private void persistTransaction(Transaction transaction) {
        // persistir em PostgreSQL ou ElasticSearch
    }

    private void updateFeedbackLoop(Transaction transaction) {
        // atualizar modelos ou regras
    }

    private void updateTransactionsCache(Transaction transaction) {
        // atualizar parâmetros de cache
    }
}
