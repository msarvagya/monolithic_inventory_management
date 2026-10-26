package com.msarvagya.ordermanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Inventory {

    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Column(nullable = false)
    private Integer reorderThreshold;

    @Version
    private Long version;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void update(Inventory updated) {
        availableQuantity = updated.getAvailableQuantity();
        reservedQuantity = updated.getReservedQuantity();
        reorderThreshold = updated.getReorderThreshold();
    }
}