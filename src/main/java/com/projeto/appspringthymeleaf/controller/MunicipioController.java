package com.projeto.appspringthymeleaf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projeto.appspringthymeleaf.dto.MunicipioDTO;
import com.projeto.appspringthymeleaf.service.MunicipioService;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping("/{estadoId}")
    @ResponseBody
    public List<MunicipioDTO> getMunicipiosByEstado(@PathVariable("estadoId") Long estadoId) {
        List<MunicipioDTO> municipios = municipioService.getMunicipiosByEstadoId(estadoId);
        return municipios;
        // return municipioService.getMunicipiosByEstadoId(estadoId);
    }

}
