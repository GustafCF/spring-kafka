package com.br.coffee_producer_kafka.controller;

import com.br.coffee_producer_kafka.model.CancelOrderModel;
import com.br.coffee_producer_kafka.model.SendOrderModel;
import com.br.coffee_producer_kafka.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class.getName());

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/send")
    public ResponseEntity<String> createOrder(@RequestBody SendOrderModel order) {
        try {
            orderService.sendOrder(order);
            return ResponseEntity.ok("Pedido enviado com sucesso!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao processar o pedido", e);
            return ResponseEntity.status(500).body("Erro ao processar o pedido.");
        }
    }

    @PostMapping(value = "/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody CancelOrderModel order) {
        try {
            orderService.cancelOrder(order);
            return ResponseEntity.ok("Pedido cancelado com sucesso!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao cancelar o pedido", e);
            return ResponseEntity.status(500).body("Erro ao cancelar o pedido.");
        }

    }
}
