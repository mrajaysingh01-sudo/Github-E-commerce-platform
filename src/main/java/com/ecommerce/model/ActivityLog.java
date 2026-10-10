package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivityLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Can be null for anonymous actions
    private User user;

    @Column(nullable = false)
    private String action; // e.g., "LOGIN", "PRODUCT_CREATED", "ORDER_PLACED"

    @Column(length = 1000)
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}