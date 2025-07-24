package com.dossantosh.springfirstmicroservise.projections;

public interface UserProjection {
    Long getId();
    String getUsername();
    String getEmail();
    Boolean getEnabled();
    Boolean getIsAdmin();
}