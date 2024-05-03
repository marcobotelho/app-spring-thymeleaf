package com.projeto.appspringthymeleaf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.PerfilRepository;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PerfilService {
    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    public List<PerfilModel> getAll() {
        return perfilRepository.findAll();
    }

    public Long[] getSelectedUsuarioIds(Long perfilId) {
        PerfilModel perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado!"));
        List<UsuarioModel> usuarios = perfil.getUsuarios();
        Long[] ids = new Long[usuarios.size()];
        for (int i = 0; i < usuarios.size(); i++) {
            ids[i] = usuarios.get(i).getId();
        }
        return ids;
    }

    public void salvarPerfilUsuarios(Long perfilId, Long[] selectedUsuarioIds) {
        PerfilModel perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado!"));

        List<UsuarioModel> todosUsuarios = usuarioRepository.findAll();

        List<UsuarioModel> listaUsuarios = new ArrayList<>();

        for (UsuarioModel usuario : todosUsuarios) {
            usuario.getPerfis().remove(perfil);
            if (selectedUsuarioIds != null) {
                for (Long usuarioId : selectedUsuarioIds) {
                    if (usuario.getId().equals(usuarioId)) {
                        usuario.getPerfis().add(perfil);
                        break;
                    }
                }
            }
            listaUsuarios.add(usuario);
        }

        usuarioRepository.saveAll(listaUsuarios);
    }
}
