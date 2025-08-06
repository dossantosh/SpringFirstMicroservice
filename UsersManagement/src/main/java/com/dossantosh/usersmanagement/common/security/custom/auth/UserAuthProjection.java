package com.dossantosh.usersmanagement.common.security.custom.auth;

import java.util.List;


/**
 * Projection interface to fetch essential user authentication data.
 */
public interface UserAuthProjection {
    Long getId();

    String getUsername();

    String getEmail();

    String getPassword();

    Boolean getEnabled();

    Boolean getIsAdmin();

    List<Long> getRoles();

    List<Long> getModules();

    List<Long> getSubmodules();
}