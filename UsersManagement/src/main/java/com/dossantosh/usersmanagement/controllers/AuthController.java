package com.dossantosh.usersmanagement.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dossantosh.usersmanagement.common.angular.AuthRequest;
import com.dossantosh.usersmanagement.common.angular.AuthResponse;
import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuth;
import com.dossantosh.usersmanagement.common.security.custom.auth.bus.CustomUserDetailsService;
import com.dossantosh.usersmanagement.common.security.custom.auth.bus.UserAuthDTO;
import com.dossantosh.usersmanagement.common.security.jwt.JwtUtil;
import com.dossantosh.usersmanagement.services.UserService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling authentication and retrieving information
 * about the currently authenticated user.
 *
 * <p>
 * This controller exposes endpoints to:
 * <ul>
 * <li>Authenticate a user and generate a JWT token</li>
 * <li>Retrieve the full user details of the currently logged-in user</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user using a username and password.
     * If successful, returns a JWT token.
     *
     * @param authRequest the login request containing username and password
     * @return {@link ResponseEntity} with a {@link AuthResponse} containing the JWT
     *         token,
     *         or 401 UNAUTHORIZED if authentication fails
     */


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));

            System.out.println("✅ Autenticación correcta para: " + authRequest.username());

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());
            System.out.println("👤 UserDetails cargado: " + userDetails.getUsername());

            final String jwt = jwtUtil.generateToken(userDetails);
            System.out.println("🔐 JWT generado: " + jwt);

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (BadCredentialsException e) {
            System.out.println("Credenciales inválidas para: " + authRequest.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            System.out.println("Error inesperado en login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Retrieves the full user details of the currently authenticated user.
     *
     * @return {@link ResponseEntity} with a {@link UserAuthDTO} containing user
     *         details,
     *         or 401 UNAUTHORIZED if no user is authenticated
     */
    @GetMapping("/me")
    public ResponseEntity<UserAuthDTO> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserAuth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        UserAuthDTO userAuthDTO = userService.mapToUserAuthDTO(userAuth);

        return ResponseEntity.ok(userAuthDTO);
    }
}
