package io.github.gabrielnavas.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielnavas.inventoryservice.dtos.InventoryResponse;
import io.github.gabrielnavas.inventoryservice.models.Inventory;
import io.github.gabrielnavas.inventoryservice.repositories.InventoryRepository;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class InventoryServiceApplicationTests {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private InventoryRepository inventoryRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@Test
	void shouldVerifyIsInStock() throws Exception {
		Inventory inventory1 = Inventory.builder().skuCode("!@123").quantity(10).build();
		Inventory inventory2 = Inventory.builder().skuCode("!@321").quantity(0).build();
		inventoryRepository.save(inventory1);
		inventoryRepository.save(inventory2);

		var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
						.param("sku-code", inventory1.getSkuCode(), inventory2.getSkuCode())
				).andExpect(status().isOk())
				.andReturn();

		String body = result.getResponse().getContentAsString();
		InventoryResponse[] inventoryResponses = objectMapper.readValue(body, InventoryResponse[].class);

		Assertions.assertTrue(inventoryResponses.length >= 2);
		Assertions.assertEquals(inventory1.getSkuCode(), inventoryResponses[0].getSkuCode());
		Assertions.assertTrue(inventoryResponses[0].getIsInStock());

		Assertions.assertEquals(inventory2.getSkuCode(), inventoryResponses[1].getSkuCode());
		Assertions.assertFalse(inventoryResponses[1].getIsInStock());
	}

}
