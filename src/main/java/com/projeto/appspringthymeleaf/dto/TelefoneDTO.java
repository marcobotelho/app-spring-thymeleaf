package com.projeto.appspringthymeleaf.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TelefoneDTO {
    private Long id;

    @NotBlank(message = "Número obrigatório")
    @Length(min = 15, max = 15, message = "Número inválido")
    private String numero;

    @NotNull(message = "Tipo obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoTelefoneEnum tipo;

    @JsonIgnoreProperties("telefones")
    private ClienteDTO cliente;

    public TelefoneDTO() {
    }

    public TelefoneDTO(Long id, String numero, TipoTelefoneEnum tipo, ClienteDTO cliente) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoTelefoneEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoTelefoneEnum tipo) {
        this.tipo = tipo;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

}
