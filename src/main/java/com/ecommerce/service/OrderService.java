package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ActivityLogRepository activityLogRepository;

    @Transactional
    public Order placeOrder(User buyer, List<OrderItem> items, String paymentMethod, String address) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
            item.setPrice(product.getPrice());
        }

        Order order = Order.builder()
                .buyer(buyer)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PLACED)
                .totalAmount(total)
                .paymentMethod(paymentMethod)
                .shippingAddress(address)
                .build();

        // Link items to order
        items.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        activityLogRepository.save(ActivityLog.builder()
                .user(buyer)
                .action("ORDER_PLACED")
                .details("Order ID " + savedOrder.getId() + " placed for $" + total)
                .build());

        return savedOrder;
    }
}