package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DataSeeder(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Generate Fresh Users
        User admin = new User(); admin.setName("Admin"); admin.setEmail("admin@shop.com"); admin.setPassword("admin"); admin.setRole(Role.ADMIN); admin.setEnabled(true);
        User seller = new User(); seller.setName("Seller"); seller.setEmail("seller@shop.com"); seller.setPassword("seller"); seller.setRole(Role.SELLER); seller.setEnabled(true);
        User buyer = new User(); buyer.setName("Test Buyer"); buyer.setEmail("buyer@shop.com"); buyer.setPassword("buyer"); buyer.setRole(Role.BUYER); buyer.setEnabled(true);

        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(admin, buyer));
            userRepository.save(seller);
        }

        User currentSeller = userRepository.findByEmail("seller@shop.com").orElse(seller);
        User currentBuyer = userRepository.findByEmail("buyer@shop.com").orElse(buyer);

        // 2. Generate Real Products
        if (productRepository.count() < 70) {
            String[][] productsData = {
                    {"Electronics", "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=600&h=400&fit=crop", "Sony PlayStation 5", "Xbox Series X", "Nintendo Switch OLED", "GoPro HERO12 Black", "DJI Mini 4 Pro", "Kindle Paperwhite", "Apple iPad Pro 12.9", "Samsung Galaxy Tab S9", "Bose Smart Soundbar 900", "LG C3 OLED TV"},
                    {"Earbuds", "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&h=400&fit=crop", "Apple AirPods Pro 3", "Sony WF-1000XM6", "Samsung Galaxy Buds4 Pro", "Sennheiser Momentum True Wireless 5", "Beats Powerbeats Pro 2", "Nothing CMF Buds 2 Plus", "EarFun Air Pro 4+", "Bowers & Wilkins Pi8", "Anker Soundcore Liberty 5 Pro", "Google Pixel Buds 2a"},
                    {"Laptop", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&h=400&fit=crop", "Apple MacBook Air (M5)", "Asus Zenbook S14", "Dell XPS 15", "Lenovo ThinkPad X1 Carbon", "HP OmniBook 7", "Asus ROG Zephyrus G14", "Razer Blade 15", "Acer Swift Edge 16", "Microsoft Surface Laptop 5", "LG Gram 17"},
                    {"Accessories", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&h=400&fit=crop", "Logitech MX Master 3S", "Keychron Q1 Pro Keyboard", "Anker 737 Power Bank", "Apple AirTag (4-Pack)", "Satechi USB-C Hub", "Belkin MagSafe Charger", "Nomad Leather iPhone Case", "SanDisk 2TB Portable SSD", "Secretlab Titan Evo Chair", "Elgato Stream Deck MK.2"},
                    {"Clothes", "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=600&h=400&fit=crop", "Nike Tech Fleece Hoodie", "Levi's 501 Original Jeans", "Adidas Ultraboost 1.0", "The North Face Nuptse Jacket", "Patagonia Better Sweater", "Carhartt WIP Detroit Jacket", "Lululemon Align Leggings", "Ralph Lauren Oxford Shirt", "Champion Reverse Weave", "Zara Minimalist Overcoat"},
                    {"Toys", "https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?w=600&h=400&fit=crop", "LEGO Star Wars Falcon", "Hot Wheels 50-Car Pack", "Nerf Pro Gelfire Mythic", "Barbie Dreamhouse", "Play-Doh Ultimate Set", "Magna-Tiles 100-Piece", "Transformers Optimus Prime", "Hasbro Monopoly Classic", "Squishmallows 16-Inch Plush", "Fisher-Price Little People"},
                    {"Stationery", "https://images.unsplash.com/photo-1517842645767-c639042777db?w=600&h=400&fit=crop", "Moleskine Classic Notebook", "Lamy Safari Fountain Pen", "Faber-Castell Polychromos", "Zebra Mildliner Set", "Pilot G2 Premium Gel Pens", "Tombow Dual Brush Pens", "Rhodia DotPad", "Pentel GraphGear 1000", "Midori MD Notebook", "Twsbi Eco Fountain Pen"}
            };

            for (String[] categoryData : productsData) {
                String category = categoryData[0];
                String imageUrl = categoryData[1];
                for (int i = 2; i <= 11; i++) {
                    Product p = new Product();
                    String name = categoryData[i];
                    p.setName(name);
                    p.setCategory(category);
                    p.setPrice(BigDecimal.valueOf(49.99 + (i * 12.50)));
                    p.setStockQuantity(50);
                    p.setActive(true);
                    p.setImageUrl(imageUrl);
                    p.setDescription("Experience premium quality with the " + name + ". Highly rated by professionals, this item offers unparalleled performance, durability, and top-tier reliability for your everyday needs.");
                    p.setSeller(currentSeller);
                    productRepository.save(p);
                }
            }
        }

        // 3. Generate Dashboard Orders
        if (orderRepository.count() == 0) {
            List<Product> products = productRepository.findAll();
            if (!products.isEmpty() && currentBuyer != null) {
                for (int i = 1; i <= 100; i++) {
                    Order order = new Order();
                    order.setStatus(OrderStatus.DELIVERED);
                    order.setTotalAmount(products.get(i % products.size()).getPrice());
                    order.setBuyer(currentBuyer);

                    // THE FIX: Provide the required missing details to stop the MySQL crash
                    order.setPaymentMethod("Credit Card");
                    order.setShippingAddress("123 Hackathon Way, Tech City");

                    orderRepository.save(order);
                }
            }
        }
    }
}