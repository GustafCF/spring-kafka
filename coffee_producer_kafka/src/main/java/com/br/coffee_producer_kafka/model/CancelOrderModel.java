package com.br.coffee_producer_kafka.model;

public class CancelOrderModel {

    private String orderId;

    public CancelOrderModel(){}

    public CancelOrderModel(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
