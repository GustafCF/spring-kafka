package com.br.coffee_consumer_kafka.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "TB_ORDER")
public class OrderModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private long id;
    @Schema(hidden = true)
    private Instant orderTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "Customer information", example = "{\"name\": \"Gustavo\"}")
    private UserModel client;

    @OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL)
    @Schema(description = "Lista de itens do pedido", example = "[{\"quantity\": 2}]")
    private Set<OrderItemModel> items = new HashSet<>();

    public OrderModel() {
    }

    public OrderModel(UserModel client) {
        this.client = client;
    }

    public OrderModel(Instant orderTime, UserModel client) {
        this.orderTime = orderTime;
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<OrderItemModel> getItems() {
        return items;
    }

    public Instant getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Instant orderTime) {
        this.orderTime = orderTime;
    }

    public UserModel getClient() {
        return client;
    }

    public void setClient(UserModel client) {
        this.client = client;
    }

    @Schema(hidden = true)
    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemModel x : items) {
            sum += x.getSubTotal();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderModel that = (OrderModel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
