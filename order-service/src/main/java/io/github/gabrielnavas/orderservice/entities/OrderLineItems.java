package io.github.gabrielnavas.orderservice.entities;


import io.github.gabrielnavas.orderservice.dtos.OrderRequestItemsDtos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "t_order_line_items")
@Table(name = "t_order_line_items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

    public OrderLineItems(String skuCode, BigDecimal price, Integer quantity) {
        this.skuCode = skuCode;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItems(OrderRequestItemsDtos item) {
        this(item.getSkuCode(), item.getPrice(), item.getQuantity());
    }
}
