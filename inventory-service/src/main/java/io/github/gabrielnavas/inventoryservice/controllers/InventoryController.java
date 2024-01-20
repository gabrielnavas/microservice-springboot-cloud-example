package io.github.gabrielnavas.inventoryservice.controllers;

import io.github.gabrielnavas.inventoryservice.dtos.InventoryResponse;
import io.github.gabrielnavas.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // /api/inventory?sku-cod=product1&sku-cod=product2&sku-cod=product3
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam("sku-code") List<String> skuCodes) {
        List<InventoryResponse> inventoryResponses = inventoryService.isInStock(skuCodes);
        return ResponseEntity.ok(inventoryResponses);
    }
}
