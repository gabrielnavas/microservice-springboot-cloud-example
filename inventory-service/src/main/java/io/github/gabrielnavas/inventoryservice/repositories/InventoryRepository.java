package io.github.gabrielnavas.inventoryservice.repositories;

import io.github.gabrielnavas.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findBySkuCode(String SkuCode);
}
