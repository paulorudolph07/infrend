package br.com.bancoamazonia.infrend.modelo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author Paulo Rudolph (7485)
 *
 */
@Entity
@Table(
	name="ARQUIVO_PROCESSADO",
	uniqueConstraints={@UniqueConstraint(columnNames={"no_arquivo_processado", "id_spec_arquivo"})})
public class ArquivoProcessado
{
	@Id
	@Column(name="ID_ARQUIVO_PROCESSADO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="NO_ARQUIVO_PROCESSADO", nullable=false, length=255)
	private String nome;
	@Temporal(TemporalType.DATE)
	@Column(name="DT_CALENDARIO")
	private Date dataCalendario;
	@ManyToOne
	@JoinColumn(name="ID_STATUS_ARQUIVO", referencedColumnName="ID_STATUS_ARQUIVO")
	private StatusArquivo status;
	@ManyToOne
	@JoinColumn(name="ID_SPEC_ARQUIVO", referencedColumnName="ID_SPEC_ARQUIVO")
	private SpecArquivo specArquivo;
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
	public Date getDataCalendario() {
		return dataCalendario;
	}
	public void setDataCalendario(Date dataCalendario) {
		this.dataCalendario = dataCalendario;
	}
	public StatusArquivo getStatus() {
		return status;
	}
	public void setStatus(StatusArquivo status) {
		this.status = status;
	}
	public SpecArquivo getSpecArquivo() {
		return specArquivo;
	}
	public void setSpecArquivo(SpecArquivo specArquivo) {
		this.specArquivo = specArquivo;
	}
}
