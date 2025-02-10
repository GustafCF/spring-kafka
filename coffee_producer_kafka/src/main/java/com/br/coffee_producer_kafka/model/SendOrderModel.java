package com.br.coffee_producer_kafka.model;

public class SendOrderModel {

    private String nameProduct;
    private int quantity;
    private  String client;

    public SendOrderModel() {
    }

    public SendOrderModel(String nameProduct, int quantity, String client) {
      this.nameProduct = nameProduct;
      this.quantity = quantity;
      this.client = client;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
