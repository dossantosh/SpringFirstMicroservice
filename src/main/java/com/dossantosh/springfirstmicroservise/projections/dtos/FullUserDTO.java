package com.dossantosh.springfirstmicroservise.projections.dtos;

import java.io.Serializable;
import java.util.LinkedHashSet;

import com.dossantosh.springfirstmicroservise.models.Modules;
import com.dossantosh.springfirstmicroservise.models.Roles;
import com.dossantosh.springfirstmicroservise.models.Submodules;

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

    private LinkedHashSet<Roles> roles = new LinkedHashSet<>();
    private LinkedHashSet<Modules> modules = new LinkedHashSet<>();
    private LinkedHashSet<Submodules> submodules = new LinkedHashSet<>();

}