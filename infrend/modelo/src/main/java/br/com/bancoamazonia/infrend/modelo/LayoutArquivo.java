package br.com.bancoamazonia.infrend.modelo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * NOME_ARQUIVO, HEADER, REGISTRO, TRAILER
 * 
 * @author 7485
 *
 */

@Entity
@Table(name="LAYOUT_ARQUIVO")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LayoutArquivo
{
	@Id
	@Column(name="ID_LAYOUT_ARQUIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="DS_LAYOUT_ARQUIVO", nullable=false, length=50, unique=true)
	private String descricao;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		LayoutArquivo other = (LayoutArquivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
