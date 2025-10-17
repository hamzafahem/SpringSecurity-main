package com.example.authenmedaf.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomePage {
    @GetMapping("/")
    public String home(Model model) {
        // جلب معلومات المستخدم المصادق
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("username", username);
        return "home"; // home.html
    }

}
