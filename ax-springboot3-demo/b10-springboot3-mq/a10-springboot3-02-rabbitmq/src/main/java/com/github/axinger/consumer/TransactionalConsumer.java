package com.github.axinger.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@EnableTransactionManagement
public class TransactionalConsumer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void sendTransactionalMessage(String message) {

        transactionTemplate.execute(status -> {
            try {
                rabbitTemplate.convertAndSend("myExchange", "myRoutingKey", message);
                String receivedMessage = (String) rabbitTemplate.receiveAndConvert("myQueue");
                // process received message
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
    }


    @RabbitListener(queues = "myQueue")
    @Transactional
    public void receiveTransactionalMessage(String message) {
        try {
            // process received message
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
