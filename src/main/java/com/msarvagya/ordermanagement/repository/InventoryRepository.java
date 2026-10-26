package com.msarvagya.ordermanagement.repository;


import com.msarvagya.ordermanagement.entity.Inventory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InventoryRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Optional<Inventory> get(UUID productId) {
        return Optional.ofNullable(entityManager.find(Inventory.class, productId));
    }

    public void create(Inventory inventory) {
        entityManager.persist(inventory);
    }

   public void update(Inventory updated) {
        Inventory existing = entityManager.find(Inventory.class, updated.getId());
        if(existing != null) {
            existing.update(updated);
        }
    }
}
