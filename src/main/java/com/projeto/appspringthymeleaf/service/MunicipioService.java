package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.MunicipioModel;
import com.projeto.appspringthymeleaf.repository.MunicipioRepository;

import jakarta.transaction.Transactional;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<MunicipioModel> getAll() {
        return municipioRepository.findAllByOrderByNomeAsc();
    }

    @Transactional
    public List<MunicipioModel> getMunicipiosByEstadoId(Long estadoId) {
        return municipioRepository.findByEstadoId(estadoId);
    }
}
