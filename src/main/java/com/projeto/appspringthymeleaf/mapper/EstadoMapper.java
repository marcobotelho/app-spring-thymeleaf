package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import com.projeto.appspringthymeleaf.dto.EstadoDTO;
import com.projeto.appspringthymeleaf.model.EstadoModel;

public class EstadoMapper {
    public static EstadoDTO converterParaDTO(EstadoModel estado) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(estado.getId());
        dto.setNome(estado.getNome());
        dto.setSigla(estado.getSigla());
        return dto;
    }

    public static EstadoModel converterParaModel(EstadoDTO dto) {
        EstadoModel model = new EstadoModel();
        model.setId(dto.getId());
        model.setNome(dto.getNome());
        model.setSigla(dto.getSigla());
        return model;
    }

    public static List<EstadoDTO> converterParaDTOList(List<EstadoModel> estados) {
        return estados.stream().map(EstadoMapper::converterParaDTO).toList();
    }

    public static List<EstadoModel> converterParaModelList(List<EstadoDTO> estados) {
        return estados.stream().map(EstadoMapper::converterParaModel).toList();
    }
}