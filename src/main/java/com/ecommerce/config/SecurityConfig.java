package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ADDED CART AND ORDER ROUTES TO PERMIT_ALL
                        .requestMatchers("/", "/product/**", "/checkout/**", "/cart/**", "/checkout-confirm", "/my-orders", "/process-order", "/order-success", "/login", "/signup", "/register").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/seller/**").hasAuthority("ROLE_SELLER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            if (role.equals("ROLE_ADMIN")) { response.sendRedirect("/admin/dashboard"); }
                            else if (role.equals("ROLE_SELLER")) { response.sendRedirect("/seller/dashboard"); }
                            else { response.sendRedirect("/"); }
                        })
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}