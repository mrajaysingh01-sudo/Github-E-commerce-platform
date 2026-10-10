package com.ecommerce.controller;

import com.ecommerce.dto.UserDto;
import com.ecommerce.model.ActivityLog;
import com.ecommerce.model.User;
import com.ecommerce.repository.ActivityLogRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, ProductRepository productRepository,
                           OrderRepository orderRepository, ActivityLogRepository activityLogRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.activityLogRepository = activityLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalOrders", orderRepository.count());
        return "admin/dashboard";
    }

    // --- USER MANAGEMENT ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/users";
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        model.addAttribute("userDto", dto);
        return "admin/user-edit";
    }

    @PostMapping("/users/update")
    public String updateUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/user-edit";
        }
        User user = userRepository.findById(userDto.getId()).orElse(null);
        if (user != null) {
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());
            user.setEnabled(userDto.isEnabled());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            userRepository.save(user);

            activityLogRepository.save(ActivityLog.builder()
                    .action("USER_UPDATED")
                    .details("Admin updated user ID: " + user.getId())
                    .build());

            redirectAttributes.addFlashAttribute("success", "User updated successfully");
        }
        return "redirect:/admin/users";
    }

    // --- PRODUCT MANAGEMENT ---
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/products";
    }

    @GetMapping("/products/toggle/{id}")
    public String toggleProductStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productRepository.findById(id).ifPresent(product -> {
            product.setActive(!product.isActive());
            productRepository.save(product);

            activityLogRepository.save(ActivityLog.builder()
                    .action("PRODUCT_STATUS_CHANGED")
                    .details("Admin changed status of product ID: " + id + " to " + product.isActive())
                    .build());
        });
        redirectAttributes.addFlashAttribute("success", "Product status updated");
        return "redirect:/admin/products";
    }

    // --- ORDER MANAGEMENT ---
    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/orders";
    }
}