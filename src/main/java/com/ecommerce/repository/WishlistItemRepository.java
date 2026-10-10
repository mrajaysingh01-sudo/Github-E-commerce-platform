package com.ecommerce.repository;

import com.ecommerce.model.WishlistItem;
import com.ecommerce.model.User;
import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByBuyerOrderByAddedAtDesc(User buyer);
    Optional<WishlistItem> findByBuyerAndProduct(User buyer, Product product);
}