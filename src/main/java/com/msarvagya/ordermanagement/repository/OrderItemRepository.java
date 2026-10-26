package com.msarvagya.ordermanagement.repository;


import com.msarvagya.ordermanagement.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderItemRepository {

    @PersistenceContext
    EntityManager entityManager;

    void create(OrderItem orderItem) { entityManager.persist(orderItem); }

    Optional<OrderItem> get(UUID orderId, UUID productId) {
        List<OrderItem> results = entityManager
                .createQuery(
                        "SELECT oi FROM OrderItem oi " +
                                "WHERE oi.order.id = :orderId " +
                                "AND oi.product.id = :productId",
                        OrderItem.class
                )
                .setParameter("orderId", orderId)
                .setParameter("productId", productId)
                .setMaxResults(1)
                .getResultList();

        return results.stream().findFirst();
    }

    List<OrderItem> getAll(UUID orderId) {
        return entityManager
                .createQuery(
                        "SELECT oi FROM OrderItem oi " +
                                "WHERE oi.order.id = :orderId",
                        OrderItem.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
