package com.dossantosh.usersmanagement.projections;

import java.io.Serializable;
import java.util.LinkedHashSet;

import com.dossantosh.usersmanagement.models.Modules;
import com.dossantosh.usersmanagement.models.Roles;
import com.dossantosh.usersmanagement.models.Submodules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullUserDTO implements Serializable {

    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private Boolean isAdmin;

    private LinkedHashSet<Roles> roles = new LinkedHashSet<>();
    private LinkedHashSet<Modules> modules = new LinkedHashSet<>();
    private LinkedHashSet<Submodules> submodules = new LinkedHashSet<>();

    public FullUserDTO(Long id, String username, String email, Boolean enabled, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.isAdmin = isAdmin;
    }
}