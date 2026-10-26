package com.msarvagya.ordermanagement.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.msarvagya.ordermanagement.dto.ProductDto;

public interface ProductService {

    /**
     * Retrieves a paginated list of all active products.
     * Aims to populate the storefront UI. Should be heavily optimized or cached.
     *
     * @param pageable Pagination and sorting configuration.
     * @return A Page of ProductDto objects containing standard catalog metadata.
     */
    Page<ProductDto> fetchActiveCatalog(Pageable pageable);

    /**
     * Mutates the active selling price of a product.
     * Aims to allow business administrators to updateStatus catalog pricing.
     * MUST NOT alter the locked prices of historical OrderItems.
     *
     * @param productId The UUID of the product to updateStatus.
     * @param newPrice  The updated BigDecimal price.
     */
    void updateProductPrice(UUID productId, BigDecimal newPrice);
}
