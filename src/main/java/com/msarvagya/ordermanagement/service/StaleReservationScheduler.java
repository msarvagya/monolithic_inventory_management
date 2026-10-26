package com.msarvagya.ordermanagement.service;

public interface StaleReservationScheduler {

    /**
     * Scans the database for PENDING_PAYMENT orders that have exceeded their TTL (Time To Live).
     * Aims to find abandoned checkouts and call OrderService.cancelOrder() on them
     * so that the trapped inventory is released back into the available pool.
     * MUST process in batches to prevent OutOfMemory errors on large data sets.
     */
    void reclaimAbandonedInventory();
}
