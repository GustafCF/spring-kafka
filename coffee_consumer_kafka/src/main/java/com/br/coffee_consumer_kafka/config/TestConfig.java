package com.br.coffee_consumer_kafka.config;

import com.br.coffee_consumer_kafka.domain.OrderItemModel;
import com.br.coffee_consumer_kafka.domain.OrderModel;
import com.br.coffee_consumer_kafka.domain.ProductModel;
import com.br.coffee_consumer_kafka.domain.UserModel;
import com.br.coffee_consumer_kafka.repository.OrderItemRepository;
import com.br.coffee_consumer_kafka.repository.OrderRepository;
import com.br.coffee_consumer_kafka.repository.ProductRepository;
import com.br.coffee_consumer_kafka.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public TestConfig(UserRepository userRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        productRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();

        ProductModel p1 = new ProductModel( "coffee", "coffe description", 5.0);
        ProductModel p2 = new ProductModel( "Capuccino", "Capuccino description", 6.0 );

        productRepository.saveAll(Arrays.asList(p1, p2));

        UserModel u1 = new UserModel("Gustavo", "gustavo@email.com", "99999-9999");
        UserModel u2 = new UserModel("Mariana", "mariana@email.com", "99999-9999");

        userRepository.saveAll(Arrays.asList(u1, u2));

        OrderModel o1 = new OrderModel(Instant.now(), u1);
        OrderModel o2 = new OrderModel(Instant.now(), u2);

        orderRepository.saveAll(Arrays.asList(o1, o2));

        OrderItemModel oi1 = new OrderItemModel(o1, p1, 2, p1.getPrice());
        OrderItemModel oi2 = new OrderItemModel(o2, p2, 2, p2.getPrice());

        orderItemRepository.saveAll(Arrays.asList(oi1, oi2));

    }
}
