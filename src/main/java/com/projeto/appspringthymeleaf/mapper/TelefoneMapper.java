package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import com.projeto.appspringthymeleaf.dto.TelefoneDTO;
import com.projeto.appspringthymeleaf.model.TelefoneModel;

public class TelefoneMapper {
    public static TelefoneDTO converterParaDTO(TelefoneModel telefone) {
        TelefoneDTO dto = new TelefoneDTO();
        dto.setId(telefone.getId());
        dto.setNumero(telefone.getNumero());
        dto.setTipo(telefone.getTipo());
        dto.setCliente(ClienteMapper.converterParaDTO(telefone.getCliente()));
        return dto;
    }

    public static TelefoneModel converterParaModel(TelefoneDTO dto) {
        TelefoneModel model = new TelefoneModel();
        model.setId(dto.getId());
        model.setNumero(dto.getNumero());
        model.setTipo(dto.getTipo());
        model.setCliente(ClienteMapper.converterParaModel(dto.getCliente()));
        return model;
    }

    public static List<TelefoneDTO> converterParaDTOList(List<TelefoneModel> telefones) {
        return telefones.stream().map(TelefoneMapper::converterParaDTO).toList();
    }

    public static List<TelefoneModel> converterParaModelList(List<TelefoneDTO> telefones) {
        return telefones.stream().map(TelefoneMapper::converterParaModel).toList();
    }
}
