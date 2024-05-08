package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.EstadoModel;
import com.projeto.appspringthymeleaf.repository.EstadoRepository;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<EstadoModel> getAll() {
        return estadoRepository.findAllByOrderBySiglaAsc();
    }
}
