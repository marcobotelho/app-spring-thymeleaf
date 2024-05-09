package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.dto.EstadoDTO;
import com.projeto.appspringthymeleaf.mapper.EstadoMapper;
import com.projeto.appspringthymeleaf.repository.EstadoRepository;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<EstadoDTO> getAll() {
        return EstadoMapper.converterParaDTOList(estadoRepository.findAllByOrderBySiglaAsc());
    }
}
