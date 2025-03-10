package com.br.coffee_consumer_kafka.repository;

import com.br.coffee_consumer_kafka.domain.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
}
