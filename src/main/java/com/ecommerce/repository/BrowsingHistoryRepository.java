package com.ecommerce.repository;

import com.ecommerce.model.BrowsingHistory;
import com.ecommerce.model.User;
import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BrowsingHistoryRepository extends JpaRepository<BrowsingHistory, Long> {
    List<BrowsingHistory> findTop10ByBuyerOrderByViewedAtDesc(User buyer);
    Optional<BrowsingHistory> findByBuyerAndProduct(User buyer, Product product);
}