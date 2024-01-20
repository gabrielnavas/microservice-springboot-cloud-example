package io.github.gabrielnavas.inventoryservice.commands;

import io.github.gabrielnavas.inventoryservice.models.Inventory;
import io.github.gabrielnavas.inventoryservice.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryLoadData implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;
    @Override
    public void run(String... args) throws Exception {
        if(inventoryRepository.findBySkuCode("!@123").isEmpty()) {
            inventoryRepository.save(Inventory.builder().skuCode("!@123").quantity(10).build());
        }
        if(inventoryRepository.findBySkuCode("!@321").isEmpty()) {
            inventoryRepository.save(Inventory.builder().skuCode("!@321").quantity(0).build());
        }
        if(inventoryRepository.findBySkuCode("!@213").isEmpty()) {
            inventoryRepository.save(Inventory.builder().skuCode("!@213").quantity(5).build());
        }
    }
}
