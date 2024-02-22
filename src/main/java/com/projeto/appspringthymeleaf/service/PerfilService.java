package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.repository.PerfilRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PerfilService {
    @Autowired
    private PerfilRepository perfilRepository;

    public void save(PerfilModel model) {
        if (model.getId() != null) {
            perfilRepository.findById(model.getId())
                    .orElseThrow(() -> new RuntimeException("Role com id " + model.getId() + " não encontrado!"));
        }
        perfilRepository.save(model);
    }

    public void delete(PerfilModel model) {
        perfilRepository.delete(model);
    }

    public void deleteById(Long id) {
        getById(id);
        perfilRepository.deleteById(id);
    }

    public PerfilModel getById(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role com id " + id + " não encontrado!"));
    }

    public List<PerfilModel> getAll(Long usuarioId) {
        return perfilRepository.findAll();
    }
}
