package com.br.coffee_consumer_kafka.service;

import com.br.coffee_consumer_kafka.domain.OrderItemModel;
import com.br.coffee_consumer_kafka.domain.OrderModel;
import com.br.coffee_consumer_kafka.domain.ProductModel;
import com.br.coffee_consumer_kafka.domain.UserModel;
import com.br.coffee_consumer_kafka.repository.OrderItemRepository;
import com.br.coffee_consumer_kafka.repository.OrderRepository;
import com.br.coffee_consumer_kafka.repository.ProductRepository;
import com.br.coffee_consumer_kafka.repository.UserRepository;
import com.br.coffee_consumer_kafka.service.exceptions.DatabaseException;
import com.br.coffee_consumer_kafka.service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderModel> findAll() {
        return orderRepository.findAll();
    }

    public OrderModel findById(Long id) {
        Optional<OrderModel> order = orderRepository.findById(id);
        return order.orElseThrow(()-> new ResourceNotFoundException(id));
    }

    @Transactional
    public OrderModel sendOrder(OrderModel order, String name) {
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item.");
        }

        UserModel user = userRepository.findByName(order.getClient().getName()).orElseThrow(()-> new ResourceNotFoundException("Cliente não encontrado: " + order.getClient().getName()));

        order.setOrderTime(Instant.now());
        order.setClient(user);

        OrderModel savedOrder = orderRepository.save(order);

        for (OrderItemModel oItem : order.getItems()) {
            oItem.setOrder(savedOrder);

            Optional<ProductModel> product = productRepository.findByName(name);


            oItem.setProduct(product.get());
            oItem.setPrice(product.get().getPrice());
            orderItemRepository.save(oItem);
        }

        return savedOrder;
    }

    public void delete(Long id) {
        try {
            orderRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
