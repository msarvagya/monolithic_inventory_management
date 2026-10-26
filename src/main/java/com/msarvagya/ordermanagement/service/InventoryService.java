package com.msarvagya.ordermanagement.service;

import java.util.UUID;

import com.msarvagya.ordermanagement.exception.InsufficientStockException;

public interface InventoryService {

    /**
     * Performs a dirty read to check if the requested quantity is available.
     * Aims to fail-fast before initiating heavy transaction logic.
     * Does NOT acquire database locks.
     *
     * @param productId The UUID of the product.
     * @param quantity  The number of units requested.
     * @return true if availableQuantity >= quantity, false otherwise.
     */
    Boolean verifyAvailability(UUID productId, Integer quantity);

    /**
     * Deducts from 'availableQuantity' and adds to 'reservedQuantity'.
     * Aims to secure stock during the checkout process before payment is confirmed.
     * MUST be transactional and MUST implement retry logic for optimistic lock collisions.
     *
     * @param productId The UUID of the product.
     * @param quantity  The number of units to reserve.
     * @throws InsufficientStockException if requested quantity exceeds available stock.
     */
    void reserveStock(UUID productId, Integer quantity);

    /**
     * Permanently deducts the quantity from 'reservedQuantity'.
     * Aims to finalize the inventory state upon successful payment capture.
     * 'availableQuantity' is left untouched.
     *
     * @param productId The UUID of the product.
     * @param quantity  The number of units previously reserved.
     */
    void commitStock(UUID productId, Integer quantity);

    /**
     * Deducts from 'reservedQuantity' and adds back to 'availableQuantity'.
     * Aims to reverse a reservation when a payment fails, an order times out, 
     * or a user explicitly cancels their checkout.
     *
     * @param productId The UUID of the product.
     * @param quantity  The number of units to release back into the available pool.
     */
    void releaseStock(UUID productId, Integer quantity);
}