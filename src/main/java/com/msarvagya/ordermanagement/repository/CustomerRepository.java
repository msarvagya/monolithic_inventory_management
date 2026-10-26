package com.msarvagya.ordermanagement.repository;

import com.msarvagya.ordermanagement.entity.Customer;
import com.msarvagya.ordermanagement.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepository {

    @PersistenceContext
    EntityManager entityManager;

    public void create(Customer customer) {
        entityManager.persist(customer);
    }

    public Optional<Customer> get(UUID customerId) {
        return Optional.ofNullable(entityManager.find(Customer.class, customerId));
    }
}
