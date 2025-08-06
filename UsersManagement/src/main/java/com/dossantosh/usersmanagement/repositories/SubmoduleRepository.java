package com.dossantosh.usersmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.usersmanagement.models.Submodules;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodules, Long> {

}