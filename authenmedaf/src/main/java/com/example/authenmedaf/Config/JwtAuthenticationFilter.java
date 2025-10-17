package com.example.authenmedaf.Config;

import com.example.authenmedaf.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1) Récupérer le JWT depuis le cookie "JWT-TOKEN"
        String jwt = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("JWT-TOKEN".equals(c.getName())) {
                    jwt = c.getValue();
                    break;
                }
            }
        }

        // 2) Si pas de token → continuer la chaîne
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Extraire le username depuis le token
        String username = null;
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // token invalide / expiré → continuer sans auth
            filterChain.doFilter(request, response);
            return;
        }

        // 4) Si pas déjà authentifié et username présent → valider le token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Remplace isTokenValid(...) par ta méthode réelle (validateToken /
            // isTokenValid)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5) Continuer la chaîne
        filterChain.doFilter(request, response);
    }
}
