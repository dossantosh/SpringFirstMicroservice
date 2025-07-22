package com.dossantosh.springfirstmicroservise.common.security.custom.auth.models;

import java.util.List;

public interface UserAuthProjection {
    Long getId();

    String getUsername();

    String getPassword();

    Boolean getEnabled();

    List<String> getRoles();

    List<Long> getModules();

    List<Long> getSubmodules();
}