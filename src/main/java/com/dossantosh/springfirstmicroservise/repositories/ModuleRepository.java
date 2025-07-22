package com.dossantosh.springfirstmicroservise.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstmicroservise.models.Modules;

@Repository
public interface ModuleRepository extends JpaRepository<Modules, Long> {
    
}
