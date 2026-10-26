package com.msarvagya.ordermanagement.dto;

import com.msarvagya.ordermanagement.entity.Customer;
import com.msarvagya.ordermanagement.entity.Order;
import com.msarvagya.ordermanagement.entity.OrderItem;
import com.msarvagya.ordermanagement.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private UUID id;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private Currency currency;

    private Set<OrderItem> orderItems;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.currency = order.getCurrency();
        this.orderItems = order.getOrderItems();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }
}
