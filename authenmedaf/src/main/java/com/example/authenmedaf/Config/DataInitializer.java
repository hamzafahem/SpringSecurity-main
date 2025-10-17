package com.example.authenmedaf.Config;

import com.example.authenmedaf.Entity.Users;
import com.example.authenmedaf.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDefaultUsers(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            // مسح المستخدمين القدامى (اختياري - حذفه في Production!)
            // userRepo.deleteAll();

            // ✅ Profile 1: Admin
            createUserIfNotExists(userRepo, encoder,
                    "admin@medafrica.com",
                    "admin123",
                    "👑 Admin");

            // ✅ Profile 2: Manager
            createUserIfNotExists(userRepo, encoder,
                    "manager@medafrica.com",
                    "manager123",
                    "📊 Manager");

            // ✅ Profile 3: User
            createUserIfNotExists(userRepo, encoder,
                    "user@medafrica.com",
                    "user123",
                    "👤 User");

            // ✅ Profile 4: Guest
            createUserIfNotExists(userRepo, encoder,
                    "guest@medafrica.com",
                    "guest123",
                    "🎭 Guest");

            System.out.println("\n" + "=".repeat(60));
            System.out.println("✅ 4 DEFAULT PROFILES CREATED:");
            System.out.println("=".repeat(60));
            System.out.println("1️⃣  admin@medafrica.com    | password: admin123");
            System.out.println("2️⃣  manager@medafrica.com  | password: manager123");
            System.out.println("3️⃣  user@medafrica.com     | password: user123");
            System.out.println("4️⃣  guest@medafrica.com    | password: guest123");
            System.out.println("=".repeat(60) + "\n");
        };
    }

    private void createUserIfNotExists(UserRepository repo,
            PasswordEncoder encoder,
            String email,
            String password,
            String displayName) {
        if (repo.findByEmail(email) == null) {
            Users user = new Users();
            user.setEmail(email);
            user.setPassword(encoder.encode(password)); // ✅ BCrypt Encoding
            repo.save(user);
            System.out.println("✅ Created: " + displayName + " (" + email + ")");
        } else {
            System.out.println("ℹ️  Already exists: " + email);
        }
    }
}
