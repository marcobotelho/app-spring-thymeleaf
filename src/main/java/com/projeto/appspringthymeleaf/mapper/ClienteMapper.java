package com.projeto.appspringthymeleaf.mapper;

import java.util.List;

import org.springframework.data.domain.Page;

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
        if (cliente.getMunicipio() != null && cliente.getMunicipio().getId() != null) {
            dto.setMunicipio(MunicipioMapper.converterParaDTO(cliente.getMunicipio()));
        } else {
            dto.setMunicipio(null);
        }
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
        if (dto.getMunicipio() != null && dto.getMunicipio().getId() != null) {
            model.setMunicipio(MunicipioMapper.converterParaModel(dto.getMunicipio()));
        } else {
            model.setMunicipio(null);
        }
        return model;
    }

    public static List<ClienteDTO> converterParaDTOList(List<ClienteModel> clientes) {
        return clientes.stream().map(ClienteMapper::converterParaDTO).toList();
    }

    public static List<ClienteModel> converterParaModelList(List<ClienteDTO> clientes) {
        return clientes.stream().map(ClienteMapper::converterParaModel).toList();
    }

    public static Page<ClienteDTO> converterParaDTOPage(Page<ClienteModel> clientes) {
        return clientes.map(ClienteMapper::converterParaDTO);
    }
}