package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import org.springframework.data.domain.Page;

import com.projeto.appspringthymeleaf.dto.UsuarioDTO;
import com.projeto.appspringthymeleaf.model.UsuarioModel;

public class UsuarioMapper {

    public static UsuarioDTO converterParaDTO(UsuarioModel usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public static UsuarioModel converterParaModel(UsuarioDTO usuarioDTO) {
        UsuarioModel model = new UsuarioModel();
        model.setId(usuarioDTO.getId());
        model.setNome(usuarioDTO.getNome());
        model.setEmail(usuarioDTO.getEmail());
        return model;
    }

    public static List<UsuarioDTO> converterParaDTOList(List<UsuarioModel> usuarios) {
        return usuarios.stream().map(UsuarioMapper::converterParaDTO).toList();
    }

    public static List<UsuarioModel> converterParaModelList(List<UsuarioDTO> usuariosDTO) {
        return usuariosDTO.stream().map(UsuarioMapper::converterParaModel).toList();
    }

    public static Page<UsuarioDTO> converterParaDTOPage(Page<UsuarioModel> usuarios) {
        return usuarios.map(UsuarioMapper::converterParaDTO);
    }
}
