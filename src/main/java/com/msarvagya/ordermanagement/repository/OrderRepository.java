package com.msarvagya.ordermanagement.repository;

import com.msarvagya.ordermanagement.entity.Order;
import com.msarvagya.ordermanagement.entity.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(Order order) {
        entityManager.persist(order);
    }

    public void update(Order updated) {
        Order existing = entityManager.find(Order.class, updated.getId());
        if(existing != null) existing.updateStatus(updated);
    }

    public List<Order> getAll(UUID customerId, Set<OrderStatus> statuses) {
        if(statuses.isEmpty()) return List.of();
        return entityManager
                .createQuery(
                        "SELECT o FROM Order o " +
                                "WHERE o.customer.id = :customerId " +
                                "AND o.status IN :statuses",
                        Order.class
                )
                .setParameter("customerId", customerId)
                .setParameter("statuses", statuses)
                .getResultList();
    }

    public List<Order> getAll(Set<OrderStatus> statuses) {
        return entityManager
                .createQuery(
                        "SELECT o FROM Order o " +
                                "WHERE o.status IN :statuses",
                        Order.class
                )
                .setParameter("statuses", statuses)
                .getResultList();
    }
}
