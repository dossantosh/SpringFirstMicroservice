package com.dossantosh.springfirstmicroservise.dtos;

public interface UserProjection {
    Long getId();
    String getUsername();
    String getEmail();
    Boolean getEnabled();
    Boolean getIsAdmin();
}