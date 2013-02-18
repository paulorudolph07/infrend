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
	name="OCORRENCIA_PF", 
	uniqueConstraints={
			@UniqueConstraint(
					columnNames={"an_ocorrencia", "id_operacao", "id_dado_bancario"}
			)
	}
)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class OcorrenciaPessoaFisica extends Ocorrencia
{
	@Id
	@Column(name="ID_OCORRENCIA_PF")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	@Column(name="VL_SALDO_ANTERIOR", nullable=false)
	private BigDecimal saldoAnterior;
	@Column(name="VL_SALDO_ATUAL", nullable=false)
	private BigDecimal saldoAtual;
	@Column(name="VL_RENDIMENTO", nullable=false)
	private BigDecimal rendimento;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}
	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}
	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	public BigDecimal getRendimento() {
		return rendimento;
	}
	public void setRendimento(BigDecimal rendimento) {
		this.rendimento = rendimento;
	}
}
