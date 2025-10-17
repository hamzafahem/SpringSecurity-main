package com.example.authenmedaf.Controller;

import com.example.authenmedaf.Service.JwtService;
import com.example.authenmedaf.Service.MyUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    // صفحة Login
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Returns login.html template
    }

    // معالجة Login
    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            Model model) {
        try {
            // 1. مصادقة المستخدم
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // 2. توليد JWT Token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String jwt = jwtService.generateToken(userDetails);

            // 3. إنشاء HttpOnly Cookie
            Cookie jwtCookie = new Cookie("JWT-TOKEN", jwt);
            jwtCookie.setHttpOnly(true); // JavaScript ماقدرش يقراه
            jwtCookie.setSecure(false); // true f production avec HTTPS
            jwtCookie.setPath("/"); // متاح فجميع المسارات
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 ساعة
            // jwtCookie.setSameSite("Strict"); // حماية CSRF - يتطلب Servlet 6.0+

            response.addCookie(jwtCookie);

            // 4. Redirect للصفحة الرئيسية
            return "redirect:/";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // حذف Cookie
        Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // حذف فوري
        response.addCookie(jwtCookie);

        return "redirect:/login";
    }
}
