package com.projeto.appspringthymeleaf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "municipio")
public class MunicipioModel {

	@Id
	private Long id;

	@NotBlank(message = "Nome obrigatório")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "estado_id", nullable = false, foreignKey = @ForeignKey(name = "fk_municipio_estado_id"))
	private EstadoModel estado;

	@OneToMany(mappedBy = "municipio")
	@JsonIgnore
	private List<ClienteModel> clientes;

	public MunicipioModel() {

	}

	public MunicipioModel(Long id, String nome, EstadoModel estado) {
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

	public EstadoModel getEstado() {
		return estado;
	}

	public void setEstado(EstadoModel estado) {
		this.estado = estado;
	}

	public List<ClienteModel> getClientes() {
		return clientes;
	}

	public void setClientes(List<ClienteModel> clientes) {
		this.clientes = clientes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MunicipioModel other = (MunicipioModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MunicipioModel [id=" + id + ", nome=" + nome + ", estado=" + estado + "]";
	}

}
