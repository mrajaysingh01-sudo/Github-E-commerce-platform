package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository; // Simulating logged-in user for now

    @GetMapping("/products")
    public ResponseEntity<?> browseProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam Long buyerId,
                                      @RequestParam String address,
                                      @RequestParam String payment,
                                      @RequestBody List<OrderItem> items) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Order order = orderService.placeOrder(buyer, items, payment, address);
        return ResponseEntity.ok(order);
    }
}
