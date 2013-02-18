package br.com.bancoamazonia.infrend.modelo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name="DADO_BANCARIO",
		uniqueConstraints={@UniqueConstraint(columnNames={"nu_agencia", "nu_conta"})}
		)
public class DadoBancario {
	
	public DadoBancario() {
		super();
	}
	public DadoBancario(String numeroAgencia, String numeroConta) {
		this.numeroAgencia=numeroAgencia;
		this.numeroConta=numeroConta;
	}
	@Id
	@Column(name="ID_DADO_BANCARIO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="NU_AGENCIA", nullable=false, length=3)
	private String numeroAgencia;
	@Column(name="NU_CONTA", nullable=false, length=7)
	private String numeroConta;
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE", referencedColumnName="ID_CLIENTE")
	private Cliente cliente;
	public String getNumeroAgencia() {
		return numeroAgencia;
	}
	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}
	public String getNumeroConta() {
		return numeroConta;
	}
	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}
}
