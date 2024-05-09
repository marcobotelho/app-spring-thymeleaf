package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.dto.MunicipioDTO;
import com.projeto.appspringthymeleaf.mapper.MunicipioMapper;
import com.projeto.appspringthymeleaf.repository.MunicipioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<MunicipioDTO> getMunicipiosByEstadoId(Long estadoId) {
        return MunicipioMapper.converterParaDTOList(municipioRepository.findByEstadoId(estadoId));
    }
}
