package com.br.coffee_producer_kafka.service;

import com.br.coffee_producer_kafka.model.CancelOrderModel;
import com.br.coffee_producer_kafka.model.SendOrderModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    @Transactional
    public void sendOrder(SendOrderModel order) {
        try {
            String orderMessage = convertOrderToJson(order);
            kafkaTemplate.send("coffee-shop-topic", String.valueOf(order.getNameProduct()), orderMessage);
            logger.log(Level.INFO, "Pedido enviado com sucesso: " + order.getNameProduct());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao enviar pedido para o Kafka", e);
        }
    }

    private String convertOrderToJson(SendOrderModel order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Erro ao converter objeto para JSON", e);
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    private String convertCanselOrderToJson(CancelOrderModel order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Erro ao converter objeto para JSON", e);
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    @Transactional
    public void cancelOrder(CancelOrderModel order) {
        try {
            String orderMessage = convertCanselOrderToJson(order);
            kafkaTemplate.send("cancel-order-coffe-shop", String.valueOf(order.getOrderId()), orderMessage);
            logger.log(Level.INFO, "Pedido cancelado com sucesso: " + order.getOrderId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao cancelar pedido para o Kafka", e);
        }
    }
}
