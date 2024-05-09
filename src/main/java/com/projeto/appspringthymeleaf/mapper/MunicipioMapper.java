package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import com.projeto.appspringthymeleaf.dto.MunicipioDTO;
import com.projeto.appspringthymeleaf.model.MunicipioModel;

public class MunicipioMapper {
    public static MunicipioDTO converterParaDTO(MunicipioModel municipio) {
        MunicipioDTO dto = new MunicipioDTO();
        dto.setId(municipio.getId());
        dto.setNome(municipio.getNome());
        dto.setEstado(EstadoMapper.converterParaDTO(municipio.getEstado()));
        return dto;
    }

    public static MunicipioModel converterParaModel(MunicipioDTO dto) {
        MunicipioModel model = new MunicipioModel();
        model.setId(dto.getId());
        model.setNome(dto.getNome());
        model.setEstado(EstadoMapper.converterParaModel(dto.getEstado()));
        return model;
    }

    public static List<MunicipioDTO> converterParaDTOList(List<MunicipioModel> municipios) {
        return municipios.stream().map(MunicipioMapper::converterParaDTO).toList();
    }

    public static List<MunicipioModel> converterParaModelList(List<MunicipioDTO> municipios) {
        return municipios.stream().map(MunicipioMapper::converterParaModel).toList();
    }
}
