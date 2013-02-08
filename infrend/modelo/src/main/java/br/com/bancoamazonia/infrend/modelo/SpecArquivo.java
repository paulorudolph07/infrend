package br.com.bancoamazonia.infrend.modelo;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SPEC_ARQUIVO")
public class SpecArquivo {
	@Id
	@Column(name="ID_SPEC_ARQUIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@ManyToOne
	@JoinColumn(name="ID_OPERACAO", referencedColumnName="ID_OPERACAO")
	private Operacao operacao;
	@Column(name="TX_PATTERN", nullable=false, length=255)
	private String pattern;
	@Column(name="DIR_ORIGEM", nullable=false, length=255)
	private String dirOrigem;
	@Column(name="DIR_DESTINO", nullable=true, length=255)
	private String dirDestino;
	@ManyToOne
	@JoinColumn(name="ID_TIPO_ARQUIVO", referencedColumnName="ID_TIPO_ARQUIVO")
	private TipoArquivo tipo;
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinTable(
			name="SPEC_ARQUIVO_PROPRIEDADE",
			schema="dbo",
			joinColumns=@JoinColumn(name="ID_SPEC_ARQUIVO"),
			inverseJoinColumns=@JoinColumn(name="ID_PROPRIEDADE")
	)
	private List<Propriedade> propriedades;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Operacao getOperacao() {
		return operacao;
	}
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getDirOrigem() {
		return dirOrigem;
	}
	public void setDirOrigem(String dirOrigem) {
		this.dirOrigem = dirOrigem;
	}
	public String getDirDestino() {
		return dirDestino;
	}
	public void setDirDestino(String dirDestino) {
		this.dirDestino = dirDestino;
	}
	public TipoArquivo getTipo() {
		return tipo;
	}
	public void setTipo(TipoArquivo tipo) {
		this.tipo = tipo;
	}
	public List<Propriedade> getPropriedades() {
		return propriedades;
	}
	public void setPropriedades(List<Propriedade> propriedades) {
		this.propriedades = propriedades;
	}
	public String toString()
	{
		return id+":"+operacao.getTipo()+":"+tipo.getDescricao();
	}
}
