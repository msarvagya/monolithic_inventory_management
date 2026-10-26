package com.msarvagya.ordermanagement.service;

import java.util.List;
import java.util.UUID;


import com.msarvagya.ordermanagement.dto.OrderResponseDto;
import com.msarvagya.ordermanagement.dto.CheckoutItemDto;

public interface OrderService {

    /**
     * Orchestrates the creation of a new order.
     * Aims to iterate through requested items, call InventoryService to reserve stock,
     * snapshot the current catalog prices, and save the Order as PENDING_PAYMENT.
     * MUST be wrapped in a strict transaction boundary to ensure atomicity.
     *
     * @param customerId The UUID of the purchasing user.
     * @param items      A list of requested products and their quantities.
     * @return OrderResponseDto containing the generated Order ID and calculated total.
     */
    OrderResponseDto initiateCheckout(UUID customerId, List<CheckoutItemDto> items);

    /**
     * Transitions an order from PENDING_PAYMENT to COMPLETED.
     * Aims to respond to asynchronous payment gateway webhooks. It must call
     * InventoryService.commitStock() for every item in the order.
     * MUST enforce idempotency to safely ignore duplicate webhook deliveries.
     *
     * @param orderId              The UUID of the order.
     * @param transactionReference The tracking ID provided by the payment gateway.
     */
    void confirmOrderPayment(UUID orderId, String transactionReference);

    /**
     * Transitions an order from PENDING_PAYMENT to CANCELLED.
     * Aims to abort the checkout flow. It must call InventoryService.releaseStock()
     * for every item in the order.
     *
     * @param orderId The UUID of the order to cancel.
     * @param reason  The justification for cancellation (e.g., "PAYMENT_FAILED", "USER_ABORTED").
     */
    void cancelOrder(UUID orderId, String reason);

    /**
     * Retrieves the current state and line items of an order.
     * Aims to provide the frontend with polling data to updateStatus the UI post-checkout.
     * MUST utilize JOIN FETCH or EntityGraphs to prevent N+1 queries.
     *
     * @param orderId The UUID of the order.
     * @return OrderResponseDto detailing status, total, and items.
     */
    OrderResponseDto fetchOrderState(UUID orderId);
}
