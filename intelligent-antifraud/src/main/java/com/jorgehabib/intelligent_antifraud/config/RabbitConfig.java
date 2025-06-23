package com.jorgehabib.intelligent_antifraud.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;


@Configuration
public class RabbitConfig {
    @Bean
    public Queue transactionEventsQueue() {
        return new Queue("transaction.events", true);
    }

    @Bean
    public Queue transactionFeedbackQueue() {
        return new Queue("transaction.feedback.queue", true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange transactionEventsExchange() {
        return ExchangeBuilder.directExchange("transaction.events").durable(true).build();
    }

    @Bean
    public Binding bindingTransactionEvents(Queue transactionEventsQueue, Exchange transactionEventsExchange) {
        // Routing key igual ao nome da fila, se for direct exchange
        return BindingBuilder.bind(transactionEventsQueue).to(transactionEventsExchange).with("123123").noargs();
    }
}
