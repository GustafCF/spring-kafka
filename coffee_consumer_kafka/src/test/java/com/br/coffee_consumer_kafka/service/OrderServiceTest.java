package com.br.coffee_consumer_kafka.service;

import com.br.coffee_consumer_kafka.domain.OrderModel;
import com.br.coffee_consumer_kafka.repository.OrderRepository;
import com.br.coffee_consumer_kafka.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private final Long validId = 1L;
    private final Long invalidId = 99L;
    private OrderModel order;

    @BeforeEach
    void setUp() {
        order = new OrderModel(Instant.now(), null);
        order.setId(validId);

        when(orderRepository.findById(validId)).thenReturn(Optional.of(order));
        when(orderRepository.findById(invalidId)).thenReturn(Optional.empty());

        doNothing().when(orderRepository).deleteById(validId);
        doThrow(new EmptyResultDataAccessException(1)).when(orderRepository).deleteById(invalidId);
    }

    @Test
    void delete_WithValidId_ShouldNotThrowException() {
        assertDoesNotThrow(() -> orderService.delete(validId));
        verify(orderRepository, times(1)).deleteById(validId);
    }

    @Test
    void delete_WithInvalidId_ShouldThrowResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.delete(invalidId));
        verify(orderRepository, times(1)).deleteById(invalidId);
    }


    @Test
    void findById_WithValidId_ShouldReturnOrder() {
        OrderModel foundOrder = orderService.findById(validId);
        assertNotNull(foundOrder);
        assertEquals(validId, foundOrder.getId());
        verify(orderRepository, times(1)).findById(validId);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.findById(invalidId));
        verify(orderRepository, times(1)).findById(invalidId);
    }


    @Test
    void findAll_ShouldReturnListOfOrders() {
        OrderModel order2 = new OrderModel(Instant.now(), null);
        order2.setId(2L);


        when(orderRepository.findAll()).thenReturn(Arrays.asList(order, order2));

        List<OrderModel> orders = orderService.findAll();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order));
        assertTrue(orders.contains(order2));

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void findAll_WhenNoOrdersExist_ShouldReturnEmptyList() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        List<OrderModel> orders = orderService.findAll();

        assertNotNull(orders);
        assertTrue(orders.isEmpty());

        verify(orderRepository, times(1)).findAll();
    }
}
