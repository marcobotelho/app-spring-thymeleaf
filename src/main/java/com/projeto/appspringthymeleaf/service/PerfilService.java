package com.projeto.appspringthymeleaf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.dto.PerfilDTO;
import com.projeto.appspringthymeleaf.mapper.PerfilMapper;
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

    @Value("${page.size}")
    private Integer pageSize;

    public void save(PerfilDTO dto) {
        if (dto.getId() != null && !perfilRepository.existsById(dto.getId())) {
            throw new RuntimeException("Perfil com id " + dto.getId() + " não encontrado!");
        }
        perfilRepository.save(PerfilMapper.converterParaModel(dto));
    }

    public void delete(PerfilDTO dto) {
        PerfilModel model = PerfilMapper.converterParaModel(dto);
        perfilRepository.delete(model);
    }

    public void deleteById(Long id) {
        getById(id);
        perfilRepository.deleteById(id);
    }

    public PerfilDTO getById(Long id) {
        PerfilModel model = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil com id " + id + " não encontrado!"));
        return PerfilMapper.converterParaDTO(model);
    }

    public List<PerfilDTO> getAll() {
        return PerfilMapper.converterParaDTOList(perfilRepository.findAll());
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

    public Page<PerfilDTO> getListaPaginada(int page) {
        Page<PerfilModel> listaPaginada = perfilRepository.findAll(PageRequest.of(page, pageSize));
        return PerfilMapper.converterParaDTOPage(listaPaginada);
    }
}
