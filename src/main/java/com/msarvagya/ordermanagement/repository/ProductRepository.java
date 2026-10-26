package com.msarvagya.ordermanagement.repository;

import com.msarvagya.ordermanagement.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public void create(Product product) {
        entityManager.persist(product);
    }

    public void update(Product updated) {
        Product existing = entityManager.find(Product.class, updated.getId());
        if(existing != null) existing.set(updated);
        entityManager.flush();
    }


    public void deleted(UUID id) {
       Product product = entityManager.find(Product.class, id);
        product.setIsActive(false);
    }

    public Product get(UUID id) {
        return entityManager.find(Product.class, id);
    }

    public List<Product> getAll() {
        return entityManager
                .createQuery("SELECT p FROM Product p", Product.class)
                .getResultStream()
                .filter(Product::getIsActive)
                .toList();
    }

    public List<Product> getAll(Set<UUID> productIds) {
        return entityManager
                .createQuery("SELECT p FROM Product p " +
                        "WHERE p.id IN :prodcut_ids", Product.class)
                .setParameter("product_ids", productIds)
                .getResultStream()
                .filter(Product::getIsActive)
                .toList();
    }
}
