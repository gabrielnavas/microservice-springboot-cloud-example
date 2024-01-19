package io.github.gabrielnavas.orderservice.services;

import io.github.gabrielnavas.orderservice.dtos.OrderRequest;
import io.github.gabrielnavas.orderservice.entities.Order;
import io.github.gabrielnavas.orderservice.entities.OrderLineItems;
import io.github.gabrielnavas.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderRequest.getOrderRequestItemsDtosList().stream().map(OrderLineItems::new).toList());

        orderRepository.save(order);
    }
}
