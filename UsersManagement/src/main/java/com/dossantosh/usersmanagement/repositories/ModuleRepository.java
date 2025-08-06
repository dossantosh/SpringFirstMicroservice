package com.dossantosh.usersmanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.usersmanagement.models.Modules;

@Repository
public interface ModuleRepository extends JpaRepository<Modules, Long> {
    
}
