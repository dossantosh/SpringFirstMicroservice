package com.dossantosh.springfirstmicroservise.common.security.custom.auth.bus;

import java.util.LinkedHashSet;

import com.dossantosh.springfirstmicroservise.common.security.custom.auth.UserAuth;

/**
 * Service interface to provide contextual information about the currently authenticated user.
 */
public interface UserContextService {
    Long getId();

    String getUsername();

    Boolean getEnabled();

    Boolean isAdmin();

    LinkedHashSet<Long> getRoles();

    LinkedHashSet<Long> getModules();

    LinkedHashSet<Long> getSubmodules();

    UserAuth getCurrentUserAuth();
}