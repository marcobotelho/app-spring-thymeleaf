package com.projeto.appspringthymeleaf.model;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "telefone")
public class TelefoneModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Número obrigatório")
	@Length(min = 15, max = 15, message = "Número inválido")
	private String numero;

	@Enumerated(EnumType.STRING)
	private TipoTelefoneEnum tipo;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_telefone_id_usuario"))
	private UsuarioModel usuario;

	public TelefoneModel() {

	}

	public TelefoneModel(Long id, String numero, TipoTelefoneEnum tipo, UsuarioModel usuario) {
		this.id = id;
		this.numero = numero;
		this.tipo = tipo;
		this.usuario = usuario;
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

	public UsuarioModel getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioModel usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "TelefoneModel [id=" + id + ", numero=" + numero + ", tipo=" + tipo + ", usuario=" + usuario + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TelefoneModel other = (TelefoneModel) obj;
		return Objects.equals(id, other.id);
	}

}
