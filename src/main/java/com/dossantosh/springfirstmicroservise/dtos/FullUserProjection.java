package com.dossantosh.springfirstmicroservise.dtos;
import java.util.List;

public interface FullUserProjection {

    Long getId();

    String getUsername();

    String getEmail();

    Boolean getEnabled();

    List<String> getRolesIds();

    List<String> getModulesIds();

    List<String> getSubmodulesIds();
}
