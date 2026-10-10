package com.ecommerce.repository;

import com.ecommerce.model.PaymentMethod;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByUser(User user);
}