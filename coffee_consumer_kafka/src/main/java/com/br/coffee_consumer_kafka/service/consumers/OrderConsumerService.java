package com.br.coffee_consumer_kafka.service.consumers;

import com.br.coffee_consumer_kafka.domain.OrderItemModel;
import com.br.coffee_consumer_kafka.domain.OrderModel;
import com.br.coffee_consumer_kafka.domain.ProductModel;
import com.br.coffee_consumer_kafka.domain.UserModel;
import com.br.coffee_consumer_kafka.domain.dto.CancelOrderDTO;
import com.br.coffee_consumer_kafka.domain.dto.SendOrderDTO;
import com.br.coffee_consumer_kafka.repository.OrderRepository;
import com.br.coffee_consumer_kafka.repository.ProductRepository;
import com.br.coffee_consumer_kafka.repository.UserRepository;
import com.br.coffee_consumer_kafka.service.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderConsumerService {

    private static final Logger logger = Logger.getLogger(OrderConsumerService.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderConsumerService(OrderRepository orderRepository, ProductRepository productRepository
                                , UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "coffee-shop-topic", groupId = "coffee-shop-consumer")
    @Transactional
    public void processOrder(String message) {
        long startTime = System.currentTimeMillis();
        try {
            SendOrderDTO order = objectMapper.readValue(message, SendOrderDTO.class);

            UserModel user = userRepository.findByName(order.client())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + order.client()));

            ProductModel product = productRepository.findByName(order.nameProduct())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + order.nameProduct()));

            OrderModel orderModel = new OrderModel();
            orderModel.setOrderTime(Instant.now());
            orderModel.setClient(user);

            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setQuantity(order.quantity());
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(orderModel);

            orderModel.getItems().add(orderItem);

            orderRepository.save(orderModel);

            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "Pedido salvo no banco: " + orderModel.getId() +
                    " (tempo de processamento: " + (endTime - startTime) + "ms)");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao processar pedido", e);
        }
    }

    @KafkaListener(topics = "cancel-order-coffe-shop", groupId = "coffee-shop-consumer")
    public void delete(String message) {
        long startTime = System.currentTimeMillis();
        try {
            CancelOrderDTO cancelOrder = objectMapper.readValue(message, CancelOrderDTO.class);
            orderRepository.deleteById(Long.valueOf(cancelOrder.orderId()));
            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "Pedido deletado com sucesso: " + cancelOrder.orderId() +
                    " (tempo de processamento: " + (endTime - startTime) + "ms)");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Erro ao cancelar pedido", e);
        }
    }
}
