package com.br.coffee_consumer_kafka.domain.dto;

public record SendOrderDTO(String nameProduct, int quantity, String client) {
}
