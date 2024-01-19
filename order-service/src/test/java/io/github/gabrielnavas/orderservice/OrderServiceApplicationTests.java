package io.github.gabrielnavas.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielnavas.orderservice.dtos.OrderRequest;
import io.github.gabrielnavas.orderservice.dtos.OrderRequestItemsDtos;
import io.github.gabrielnavas.orderservice.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    void shouldCreateOrder() throws Exception {
        OrderRequest product = createProductRequest();

        String payloadJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN)
                        .content(payloadJson))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, orderRepository.findAll().size());
    }

    private OrderRequest createProductRequest() {
        List<OrderRequestItemsDtos> list = new ArrayList<>();
        list.add(OrderRequestItemsDtos.builder().skuCode("!@100").price(BigDecimal.valueOf(100.10)).quantity(10).build());
        list.add(OrderRequestItemsDtos.builder().skuCode("!@200").price(BigDecimal.valueOf(200.20)).quantity(20).build());
        list.add(OrderRequestItemsDtos.builder().skuCode("!@300").price(BigDecimal.valueOf(300.30)).quantity(30).build());
        return OrderRequest.builder()
                .orderRequestItemsDtosList(list)
                .build();
    }

}
