package br.com.bancoamazonia.infrend.modelo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DadoBancario
{
	public DadoBancario()
	{
		super();
	}
	public DadoBancario(String numeroAgencia, String numeroConta) {
		this.numeroAgencia=numeroAgencia;
		this.numeroConta=numeroConta;
	}
	public DadoBancario(String numeroAgencia, String numeroConta, String digito) {
		this(numeroAgencia, numeroConta);
		this.digito=digito;
	}
	@Column(name="NU_AGENCIA", nullable=false, length=3)
	private String numeroAgencia;
	@Column(name="NU_CONTA", nullable=false, length=6)
	private String numeroConta;
	@Column(name="NU_DIGITO", nullable=false, length=1)
	private String digito;
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
	public String getDigito() {
		return digito;
	}
	public void setDigito(String digito) {
		this.digito = digito;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numeroAgencia == null) ? 0 : numeroAgencia.hashCode());
		result = prime * result
				+ ((numeroConta == null) ? 0 : numeroConta.hashCode());
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
		DadoBancario other = (DadoBancario) obj;
		if (numeroAgencia == null) {
			if (other.numeroAgencia != null)
				return false;
		} else if (!numeroAgencia.equals(other.numeroAgencia))
			return false;
		if (numeroConta == null) {
			if (other.numeroConta != null)
				return false;
		} else if (!numeroConta.equals(other.numeroConta))
			return false;
		return true;
	}
	
}
