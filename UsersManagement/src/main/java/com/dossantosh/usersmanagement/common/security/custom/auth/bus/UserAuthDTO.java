package com.dossantosh.usersmanagement.common.security.custom.auth.bus;

import java.io.Serializable;
import java.util.LinkedHashSet;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing authenticated user details,
 * including user identity, status, and assigned roles, modules, and submodules.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO implements Serializable {

    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private Boolean isAdmin;

    private LinkedHashSet<Long> roles = new LinkedHashSet<>();
    private LinkedHashSet<Long> modules = new LinkedHashSet<>();
    private LinkedHashSet<Long> submodules = new LinkedHashSet<>();

}
