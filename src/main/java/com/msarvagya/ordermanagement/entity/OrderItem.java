package com.msarvagya.ordermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal lockedPrice;

    @Column(nullable = false)
    private BigDecimal lineTotal;

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal lockedPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.lockedPrice = lockedPrice;
        this.lineTotal = lockedPrice.multiply(BigDecimal.valueOf(quantity));
    }

}
