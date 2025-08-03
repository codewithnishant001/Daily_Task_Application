package com.example.demo.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

public class SecureController {
    @GetMapping("/secure")
    public String secureEndpoint() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // this is the subject from the JWT (i.e. the email)

        return "🔐 Hello, " + email + "! You are authenticated.";
    }
}
