package io.github.gabrielnavas.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielnavas.productservice.dtos.ProductRequest;
import io.github.gabrielnavas.productservice.dtos.ProductResponse;
import io.github.gabrielnavas.productservice.services.ProductService;
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
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest product = createProductRequest();

        String payloadJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andExpect(status().isCreated());

        Assertions.assertTrue(productService.getAllProducts().size() > 0);
    }

    @Test
    void shouldFindAllProducts() throws Exception {
        ProductRequest productRequest = createProductRequest();
        productService.createProduct(productRequest);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        ProductResponse[] productResponses = objectMapper.readValue(body, ProductResponse[].class);

        Assertions.assertEquals(1, productResponses.length);
    }

    private ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .name("any_name")
                .description("any_description")
                .price(BigDecimal.valueOf(5000.00))
                .build();
    }

}
