package com.dossantosh.springfirstmicroservise.services;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstmicroservise.models.Submodules;
import com.dossantosh.springfirstmicroservise.repositories.SubmoduleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubmoduleService {

    private final SubmoduleRepository submoduleRepository;

    public Submodules findById(Long id) {
        return submoduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Módulo con ID " + id + " no encontrado"));
    }

    public List<Submodules> findAllById(List<Long> listaId) {
        return new ArrayList<>(submoduleRepository.findAllById(listaId));
    }

    public List<Submodules> findAll() {
        return new ArrayList<>(submoduleRepository.findAll());
    }

    public Submodules save(Submodules submodule) {
        return submoduleRepository.save(submodule);
    }

    public boolean existById(Long id) {
        return submoduleRepository.existsById(id);
    }

}
