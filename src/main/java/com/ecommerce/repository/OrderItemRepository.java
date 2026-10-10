package com.ecommerce.repository;

import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Changing this to countByProductSeller allows Spring to map OrderItem -> Product -> Seller
    long countByProductSeller(User seller);
}