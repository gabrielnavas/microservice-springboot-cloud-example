package io.github.gabrielnavas.productservice.services;

import io.github.gabrielnavas.productservice.dtos.ProductRequest;
import io.github.gabrielnavas.productservice.dtos.ProductResponse;
import io.github.gabrielnavas.productservice.models.Product;
import io.github.gabrielnavas.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .build();

        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::new)
                .toList();
    }
}
