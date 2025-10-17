package com.example.authenmedaf.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.authenmedaf.Entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // Recherche par email
    Users findByEmail(String email);
}
