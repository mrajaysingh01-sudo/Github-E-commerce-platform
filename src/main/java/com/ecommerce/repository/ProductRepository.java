package com.ecommerce.repository;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByActiveTrueAndCategory(String category, Pageable pageable);
    Page<Product> findByActiveTrueAndNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findBySeller(User seller, Pageable pageable);
    long countBySeller(User seller);
    long countBySellerAndStockQuantityLessThanEqual(User seller, int threshold);
}