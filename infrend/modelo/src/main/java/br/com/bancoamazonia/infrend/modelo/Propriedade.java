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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(
	name="PROPRIEDADE",
	uniqueConstraints={@UniqueConstraint(
			columnNames={"no_propriedade", "vl_tamanho", "nu_ordem", "id_tipo_propriedade", "id_layout_arquivo"}
	)}
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Propriedade
{
	@Id
	@Column(name="ID_PROPRIEDADE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="NO_PROPRIEDADE", nullable=false, length=50)
	private String nome;
	@Column(name="DS_PROPRIEDADE", length=255)
	private String descricao;
	@Column(name="VL_TAMANHO", nullable=false)
	private Integer tamanho;
	@Column(name="NU_ORDEM", nullable=false)
	private Integer ordem;
	@ManyToOne
	@JoinColumn(name="ID_TIPO_PROPRIEDADE", referencedColumnName="ID_TIPO_PROPRIEDADE")
	private TipoPropriedade tipo;
	@ManyToOne
	@JoinColumn(name="ID_LAYOUT_ARQUIVO", referencedColumnName="ID_LAYOUT_ARQUIVO")
	private LayoutArquivo layoutArquivo;
	@ManyToMany(
	        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
	        fetch=FetchType.LAZY,
	        mappedBy = "propriedades"
	    )
	private List<SpecArquivo> specArquivos;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getTamanho() {
		return tamanho;
	}
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public TipoPropriedade getTipo() {
		return tipo;
	}
	public void setTipo(TipoPropriedade tipo) {
		this.tipo = tipo;
	}
	public LayoutArquivo getLayoutArquivo() {
		return layoutArquivo;
	}
	public void setLayoutArquivo(LayoutArquivo layoutArquivo) {
		this.layoutArquivo = layoutArquivo;
	}
	public List<SpecArquivo> getSpecArquivos() {
		return specArquivos;
	}
	public void setSpecArquivos(List<SpecArquivo> specArquivos) {
		this.specArquivos = specArquivos;
	}
}
