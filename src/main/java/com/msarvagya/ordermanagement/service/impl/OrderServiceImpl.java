package com.msarvagya.ordermanagement.service.impl;

import com.msarvagya.ordermanagement.dto.CheckoutItemDto;
import com.msarvagya.ordermanagement.dto.OrderResponseDto;
import com.msarvagya.ordermanagement.entity.Customer;
import com.msarvagya.ordermanagement.entity.Order;
import com.msarvagya.ordermanagement.entity.OrderItem;
import com.msarvagya.ordermanagement.entity.Product;
import com.msarvagya.ordermanagement.exception.CustomerNotFoundException;
import com.msarvagya.ordermanagement.exception.ProductNotFoundException;
import com.msarvagya.ordermanagement.repository.CustomerRepository;
import com.msarvagya.ordermanagement.repository.OrderRepository;
import com.msarvagya.ordermanagement.repository.ProductRepository;
import com.msarvagya.ordermanagement.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.util.*;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;

    private InventoryServiceImpl inventoryService;

    @Override
    @Transactional
    public OrderResponseDto initiateCheckout(UUID customerId, List<CheckoutItemDto> items) {

        for(CheckoutItemDto itemDto: items) {
            if(itemDto.getProductId() == null)  throw new IllegalArgumentException("Product ID cannot be null");
            if(itemDto.getQuantity() == null || itemDto.getQuantity() < 1) throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Customer customer = customerRepository.get(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer Not Found With This ID"));
        Map<UUID, Integer> quantities = items.stream()
                .collect(Collectors.toMap(CheckoutItemDto::getProductId, CheckoutItemDto::getQuantity));
        Set<UUID> itemIds = quantities.keySet();

        List<Product> products = productRepository.getAll(itemIds);

        if(products.size() != items.size()) {
            throw new ProductNotFoundException("One or more product is not found");
        }

        Set<OrderItem> orderItems = products.stream()
                .map(a ->
                        new OrderItem(
                                null,
                                null,
                                quantities.get(a.getId()),
                                a.getCurrentPrice()))
                .collect(Collectors.toSet());

        //Order Creation
        Order order = new Order(customer);

        //Inventory reservation
        for(Product product: products) {
            inventoryService.reserveStock(product.getId(), quantities.get(product.getId()));
        }

        for(OrderItem orderItem: orderItems) {
            order.addOrderItem(orderItem);
        }
        // Order persistence
        orderRepository.create(order);
        return new OrderResponseDto(order);
    }

    @Override
    public void confirmOrderPayment(UUID orderId, String transactionReference) {

    }

    @Override
    public void cancelOrder(UUID orderId, String reason) {

    }

    @Override
    public OrderResponseDto fetchOrderState(UUID orderId) {
        return null;
    }

}
