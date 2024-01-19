package io.github.gabrielnavas.orderservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestItemsDtos {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
