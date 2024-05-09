package com.projeto.appspringthymeleaf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;

public class TelefoneDTO {
    private Long id;

    private String numero;

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
