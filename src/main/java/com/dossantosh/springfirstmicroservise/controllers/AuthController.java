package com.dossantosh.springfirstmicroservise.controllers;

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

import com.dossantosh.springfirstmicroservise.common.angular.AuthRequest;
import com.dossantosh.springfirstmicroservise.common.angular.AuthResponse;
import com.dossantosh.springfirstmicroservise.common.security.custom.auth.UserAuth;

import com.dossantosh.springfirstmicroservise.common.security.custom.auth.bus.CustomUserDetailsService;
import com.dossantosh.springfirstmicroservise.common.security.custom.auth.bus.UserAuthDTO;
import com.dossantosh.springfirstmicroservise.common.security.jwt.JwtUtil;

import com.dossantosh.springfirstmicroservise.services.UserService;

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
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
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
