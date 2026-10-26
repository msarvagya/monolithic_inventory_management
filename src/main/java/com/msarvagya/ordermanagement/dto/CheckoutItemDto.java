package com.msarvagya.ordermanagement.dto;

import com.msarvagya.ordermanagement.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemDto {

    @NotBlank
    private UUID productId;

    @Positive
    private Integer quantity;
}
