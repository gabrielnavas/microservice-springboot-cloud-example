package io.github.gabrielnavas.inventoryservice.services;

import io.github.gabrielnavas.inventoryservice.models.Inventory;
import io.github.gabrielnavas.inventoryservice.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        Optional<Inventory> optionalInventory = inventoryRepository.findBySkuCode(skuCode);
        return optionalInventory.isPresent();
    }
}
