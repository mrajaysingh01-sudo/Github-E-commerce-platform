package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentMethod {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String type; // e.g., "CARD", "UPI"

    @Column(nullable = false)
    private String maskedNumber; // e.g., "**** **** **** 1234" or UPI ID

    private boolean isDefault;
}