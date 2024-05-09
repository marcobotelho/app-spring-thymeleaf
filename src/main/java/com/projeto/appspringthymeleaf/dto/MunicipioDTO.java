package com.projeto.appspringthymeleaf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class MunicipioDTO {
    private Long id;

    private String nome;

    @JsonIgnoreProperties("municipios")
    private EstadoDTO estado;

    public MunicipioDTO() {
    }

    public MunicipioDTO(Long id, String nome, EstadoDTO estado) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoDTO getEstado() {
        return estado;
    }

    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }
}
