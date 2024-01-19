package io.github.gabrielnavas.productservice.dtos;

import io.github.gabrielnavas.productservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }
}
