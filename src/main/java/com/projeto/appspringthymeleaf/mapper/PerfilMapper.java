package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import com.projeto.appspringthymeleaf.dto.PerfilDTO;
import com.projeto.appspringthymeleaf.model.PerfilModel;

public class PerfilMapper {

    public static PerfilDTO converterParaDTO(PerfilModel perfil) {
        return new PerfilDTO(perfil.getId(), perfil.getNome(), perfil.getDescricao());
    }

    public static PerfilModel converterParaModel(PerfilDTO perfilDTO) {
        return new PerfilModel(perfilDTO.getId(), perfilDTO.getNome(), perfilDTO.getDescricao());
    }

    public static List<PerfilDTO> converterParaDTOList(List<PerfilModel> perfis) {
        return perfis.stream().map(PerfilMapper::converterParaDTO).toList();
    }

    public static List<PerfilModel> converterParaModelList(List<PerfilDTO> perfisDTO) {
        return perfisDTO.stream().map(PerfilMapper::converterParaModel).toList();
    }
}
