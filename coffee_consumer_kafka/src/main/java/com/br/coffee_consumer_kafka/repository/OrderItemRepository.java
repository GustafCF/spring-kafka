package com.br.coffee_consumer_kafka.repository;

import com.br.coffee_consumer_kafka.domain.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}
