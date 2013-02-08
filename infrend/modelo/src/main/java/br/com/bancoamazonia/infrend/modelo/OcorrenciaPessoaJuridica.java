package br.com.bancoamazonia.infrend.modelo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name="OCORRENCIA_PJ", 
	uniqueConstraints={
			@UniqueConstraint(
					columnNames={"an_ocorrencia", "me_ocorrencia", "id_operacao", "id_cliente"}
			)
	}
)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class OcorrenciaPessoaJuridica extends Ocorrencia
{	
	@Id
	@Column(name="ID_OCORRENCIA_PJ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="ME_OCORRENCIA")
	private Integer mes;
	@Column(name="VL_RENDIMENTO", nullable=false)
	private BigDecimal rendimento;
	@Column(name="VL_IMPOSTO_RENDA", nullable=false)
	private BigDecimal impostoRenda;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public BigDecimal getRendimento() {
		return rendimento;
	}
	public void setRendimento(BigDecimal rendimento) {
		this.rendimento = rendimento;
	}
	public BigDecimal getImpostoRenda() {
		return impostoRenda;
	}
	public void setImpostoRenda(BigDecimal impostoRenda) {
		this.impostoRenda = impostoRenda;
	}
}
