package com.msarvagya.ordermanagement.service.impl;

import com.msarvagya.ordermanagement.entity.Inventory;
import com.msarvagya.ordermanagement.exception.InsufficientStockException;
import com.msarvagya.ordermanagement.exception.ProductNotFoundException;
import com.msarvagya.ordermanagement.repository.InventoryRepository;
import com.msarvagya.ordermanagement.service.InventoryService;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean verifyAvailability(UUID productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .get(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        return inventory.getAvailableQuantity() - inventory.getReservedQuantity() >= quantity;
    }

    @Override
    @Transactional
    @Retryable(
            includes = ObjectOptimisticLockingFailureException.class,
            maxRetries = 3,
            delay = 150,
            multiplier = 2.0
    )
    public void reserveStock(UUID productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .get(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        if(inventory.getAvailableQuantity() - inventory.getReservedQuantity() < quantity) {
            throw new InsufficientStockException("No Enough Stock Available");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
    }

    @Override
    @Transactional
    public void commitStock(UUID productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .get(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
    }

    @Override
    public void releaseStock(UUID productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .get(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
    }
}
