package com.dossantosh.springfirstmicroservise.common.security.custom.auth.models;

import java.util.LinkedHashSet;

import com.dossantosh.springfirstmicroservise.common.security.custom.auth.UserAuth;


public interface UserContextService {
    Long getId();

    String getUsername();

    boolean getEnabled();

    LinkedHashSet<String> getRoles();

    LinkedHashSet<Long> getModules();

    LinkedHashSet<Long> getSubmodules();

    boolean isAdmin();

    UserAuth getCurrentUserAuth();
}