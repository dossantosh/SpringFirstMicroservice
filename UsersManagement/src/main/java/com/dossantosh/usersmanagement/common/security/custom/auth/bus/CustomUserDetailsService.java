package com.dossantosh.usersmanagement.common.security.custom.auth.bus;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuthProjection;
import com.dossantosh.usersmanagement.services.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of {@link UserDetailsService} that loads user-specific data
 * for authentication purposes using a user service.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Locates the user based on the username.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated {@link UserDetails} object (never null).
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAuthProjection userAuthProjection = userService.findUserAuthByUsername(username);
        return userService.mapToUserAuth(userAuthProjection);
    }
}
