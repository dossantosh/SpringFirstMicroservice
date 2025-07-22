package com.dossantosh.springfirstmicroservise.services;


import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstmicroservise.models.Roles;
import com.dossantosh.springfirstmicroservise.repositories.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public Roles findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol con ID " + id + " no encontrado"));
    }

    public List<Roles> findAllById(List<Long> listaId) {
        return new ArrayList<>(roleRepository.findAllById(listaId));

    }

    public List<Roles> findAll() {
        return new ArrayList<>(roleRepository.findAll());

    }

    public boolean existById(Long id) {
        return roleRepository.existsById(id);
    }

}