package com.dossantosh.springfirstmicroservise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstmicroservise.models.Submodules;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodules, Long> {

}