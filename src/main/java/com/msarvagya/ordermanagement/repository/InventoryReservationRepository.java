package com.msarvagya.ordermanagement.repository;

import com.msarvagya.ordermanagement.entity.Inventory;
import com.msarvagya.ordermanagement.entity.InventoryReservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class InventoryReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(InventoryReservation inventoryReservation) { entityManager.persist(inventoryReservation);}

    public void update(InventoryReservation updated) {
        InventoryReservation existing  = entityManager.find(InventoryReservation.class, updated.getId());
        if(existing != null) {
            existing.setStatus(updated.getStatus());
        }
    }

    public int delete(LocalDateTime timeLimit) {
        return entityManager
                .createQuery(
                        "DELETE FROM InventoryReservation ir " +
                                "WHERE ir.expiresAt < :limit"
                )
                .setParameter("limit", timeLimit)
                .executeUpdate();
    }

}
