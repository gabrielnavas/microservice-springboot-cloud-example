package io.github.gabrielnavas.orderservice.services;

import io.github.gabrielnavas.orderservice.dtos.InventoryResponse;
import io.github.gabrielnavas.orderservice.dtos.OrderRequest;
import io.github.gabrielnavas.orderservice.entities.Order;
import io.github.gabrielnavas.orderservice.entities.OrderLineItems;
import io.github.gabrielnavas.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Value("${endpoints.http-rest.inventory}")
    private String inventoryHttpRestEndpoint;

    @Transactional
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderRequest.getOrderRequestItemsDtosList().stream().map(OrderLineItems::new).toList());

        // call Inventory Service checking quantity of each item
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        InventoryResponse[] inventoryResponses = webClient.get()
                .uri(inventoryHttpRestEndpoint, uriBuilder -> uriBuilder.queryParam("sku-code", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        // verify quantity is equals
        assert inventoryResponses != null;
        if(inventoryResponses.length != order.getOrderLineItemsList().size()) {
            throw new IllegalArgumentException("product is not in stock, please try again later");
        }

        // verify quantity of each item
        Boolean isAllInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);
        if(Boolean.FALSE.equals(isAllInStock)) {
            throw new IllegalArgumentException("product is not in stock, please try again later");
        }
        orderRepository.save(order);
    }
}
