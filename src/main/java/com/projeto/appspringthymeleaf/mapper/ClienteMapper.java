package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import com.projeto.appspringthymeleaf.dto.ClienteDTO;
import com.projeto.appspringthymeleaf.model.ClienteModel;

public class ClienteMapper {
    public static ClienteDTO converterParaDTO(ClienteModel cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setIdade(cliente.getIdade());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setCep(cliente.getCep());
        dto.setEndereco(cliente.getEndereco());
        dto.setBairro(cliente.getBairro());
        dto.setMunicipio(MunicipioMapper.converterParaDTO(cliente.getMunicipio()));
        return dto;
    }

    public static ClienteModel converterParaModel(ClienteDTO dto) {
        ClienteModel model = new ClienteModel();
        model.setId(dto.getId());
        model.setNome(dto.getNome());
        model.setEmail(dto.getEmail());
        model.setIdade(dto.getIdade());
        model.setDataNascimento(dto.getDataNascimento());
        model.setCep(dto.getCep());
        model.setEndereco(dto.getEndereco());
        model.setBairro(dto.getBairro());
        model.setMunicipio(MunicipioMapper.converterParaModel(dto.getMunicipio()));
        return model;
    }

    public static List<ClienteDTO> converterParaDTOList(List<ClienteModel> clientes) {
        return clientes.stream().map(ClienteMapper::converterParaDTO).toList();
    }

    public static List<ClienteModel> converterParaModelList(List<ClienteDTO> clientes) {
        return clientes.stream().map(ClienteMapper::converterParaModel).toList();
    }
}