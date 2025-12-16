package com.dossantosh.usersmanagement.common.security.custom.auth.bus;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuth;

import java.util.LinkedHashSet;
import java.util.Optional;

/**
 * Implementation of {@link UserContextService} that retrieves
 * user authentication details from the Spring Security context.
 */
@Service
public final class UserContextServiceImpl implements UserContextService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return getUserAuth().map(UserAuth::getId).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return getUserAuth().map(UserAuth::getUsername).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getEnabled() {
        return getUserAuth().map(UserAuth::getEnabled).orElse(Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedHashSet<Long> getRoles() {
        return new LinkedHashSet<>(
                getUserAuth().map(UserAuth::getRoles).orElse(new LinkedHashSet<>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedHashSet<Long> getModules() {
        return new LinkedHashSet<>(
                getUserAuth().map(UserAuth::getModules).orElse(new LinkedHashSet<>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedHashSet<Long> getSubmodules() {
        return new LinkedHashSet<>(
                getUserAuth().map(UserAuth::getSubmodules).orElse(new LinkedHashSet<>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isAdmin() {
        return getUserAuth()
                .map(UserAuth::getIsAdmin)
                .orElse(Boolean.FALSE);
    }
    
    /**
     * Helper method to retrieve the {@link UserAuth} object from
     * the Spring Security context.
     *
     * @return an Optional containing the UserAuth if present and authenticated, otherwise empty
     */
    private static Optional<UserAuth> getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserAuth userAuth) {
            return Optional.of(userAuth);
        }
        return Optional.empty();
    }
}
