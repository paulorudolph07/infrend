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
@Table
(
	name="CLIENTE",
	uniqueConstraints={@UniqueConstraint(columnNames={"cd_cliente"})}
)
public class Cliente {
	@Id
	@Column(name="ID_CLIENTE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="CD_CLIENTE", nullable=false, length=15)
	private String codigo;
	@Column(name="NO_CLIENTE", nullable=false, length=50)
	private String nome;
	@Column(name="NO_LOGRADOURO", nullable=false, length=60)
	private String logradouro;
	@Column(name="NO_BAIRRO", nullable=false, length=20)
	private String bairro;
	@Column(name="NO_CIDADE", nullable=false, length=20)
	private String cidade;
	@Column(name="SG_ESTADO", nullable=false, length=2)
	private String uf;
	@Column(name="CD_CEP", nullable=false, length=8)
	private String cep;
	@ManyToOne
	@JoinColumn(name="ID_TIPO_CLIENTE", referencedColumnName="ID_TIPO_CLIENTE")
	private TipoCliente tipo;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public TipoCliente getTipo() {
		return tipo;
	}
	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo;
	}
}
