package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import com.projeto.appspringthymeleaf.dto.UsuarioDTO;
import com.projeto.appspringthymeleaf.model.UsuarioModel;

public class UsuarioMapper {

    public static UsuarioDTO converterParaDTO(UsuarioModel usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public static UsuarioModel converterParaModel(UsuarioDTO usuarioDTO) {
        return new UsuarioModel(null, usuarioDTO.getNome(), usuarioDTO.getEmail());
    }

    public static List<UsuarioDTO> converterParaDTOList(List<UsuarioModel> usuarios) {
        return usuarios.stream().map(UsuarioMapper::converterParaDTO).toList();
    }

    public static List<UsuarioModel> converterParaModelList(List<UsuarioDTO> usuariosDTO) {
        return usuariosDTO.stream().map(UsuarioMapper::converterParaModel).toList();
    }
}
