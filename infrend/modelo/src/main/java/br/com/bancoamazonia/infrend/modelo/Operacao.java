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
 * CONTA_CORRENTE, PRAZO, FUNDOS, POUPANCA
 * @author rudolph (7485)
 *
 */
@Entity
@Table(name="OPERACAO")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Operacao {
	
	@Id
	@Column(name="ID_OPERACAO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="DS_OPERACAO")
	private String tipo;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
