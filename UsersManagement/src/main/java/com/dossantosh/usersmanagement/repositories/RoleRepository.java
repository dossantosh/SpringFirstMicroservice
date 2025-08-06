package com.dossantosh.usersmanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.usersmanagement.models.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}