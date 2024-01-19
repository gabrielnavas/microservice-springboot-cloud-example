package io.github.gabrielnavas.orderservice.repositories;

import io.github.gabrielnavas.orderservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
