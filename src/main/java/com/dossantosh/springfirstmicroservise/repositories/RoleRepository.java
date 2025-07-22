package com.dossantosh.springfirstmicroservise.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstmicroservise.models.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}