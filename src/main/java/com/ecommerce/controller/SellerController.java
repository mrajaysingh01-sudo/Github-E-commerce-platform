package com.ecommerce.controller;

import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public SellerController(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(Model model) {
        long listed = productRepository.count();
        long shipped = orderRepository.count(); // Using global orders for the dummy demo
        double totalProfit = shipped * 42.50; // Dummy profit calculation for hackathon demo

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("listed", listed);
        model.addAttribute("shipped", shipped);
        model.addAttribute("profit", String.format("%.2f", totalProfit));

        return "seller-dashboard";
    }
}