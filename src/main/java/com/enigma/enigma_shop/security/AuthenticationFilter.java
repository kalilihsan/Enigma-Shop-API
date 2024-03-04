package com.enigma.enigma_shop.security;

import com.enigma.enigma_shop.dto.response.JwtClaims;
import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.service.JwtService;
import com.enigma.enigma_shop.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader("Authorization");

            if (bearerToken != null && jwtService.verifyJwtToken(bearerToken)) {
                // server akan menyimpan informasi user yg terverifikasi
                // ke storage security context selama permintaan http selesai
                JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken);
                UserAccount userAccount = userService.getByUserId(jwtClaims.getUserAccountId());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAccount.getUsername(),
                        null,
                        userAccount.getAuthorities()
                );
                // menyimpan informasi tambahan berupa ip address dll
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // meneruskan request ke controller
        filterChain.doFilter(request, response);
    }
}