package io.github.gabrielnavas.productservice.repositories;

import io.github.gabrielnavas.productservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends MongoRepository<Product, String> { }
