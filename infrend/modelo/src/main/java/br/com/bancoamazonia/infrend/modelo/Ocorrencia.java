package br.com.bancoamazonia.infrend.modelo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Ocorrencia
{
	@Column(name="AN_OCORRENCIA")
	private Integer ano;
	@ManyToOne
	@JoinColumn(name="ID_OPERACAO", referencedColumnName="ID_OPERACAO")
	private Operacao operacao;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ID_CLIENTE", referencedColumnName="ID_CLIENTE")
	private Cliente cliente;
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Operacao getOperacao() {
		return operacao;
	}
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
