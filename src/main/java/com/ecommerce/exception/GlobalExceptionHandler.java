package com.ecommerce.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("error", "404 - Page Not Found");
        model.addAttribute("message", "The page you are looking for does not exist.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalError(Exception ex, Model model) {
        model.addAttribute("error", "500 - Internal Server Error");
        model.addAttribute("message", "An unexpected error occurred. Our team has been notified.");
        ex.printStackTrace(); // Prints to your IntelliJ terminal for debugging
        return "error";
    }
}