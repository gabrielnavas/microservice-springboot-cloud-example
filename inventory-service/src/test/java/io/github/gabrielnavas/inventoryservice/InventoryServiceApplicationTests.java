package io.github.gabrielnavas.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
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
		Inventory inventory = Inventory.builder().skuCode("!@123").quantity(10).build();
		inventoryRepository.save(inventory);

		var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/" + inventory.getSkuCode()))
				.andExpect(status().isOk())
				.andReturn();

		String body = result.getResponse().getContentAsString();
		Boolean isInStock = objectMapper.readValue(body, Boolean.class);

		Assertions.assertEquals(true, isInStock);
	}

}
