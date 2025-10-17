package com.example.authenmedaf.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.authenmedaf.Entity.Users;
import com.example.authenmedaf.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.authenmedaf.Entity.UserPrincipal;
import com.example.authenmedaf.Entity.Users;
import com.example.authenmedaf.Repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username); // ✅ استخدم findByEmail

        if (user == null) {
            System.out.println("User Not Found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new UserPrincipal(user); // ✅ new UserPrincipal
    }
}