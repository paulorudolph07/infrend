package br.com.bancoamazonia.infrend.callback;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaJuridica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class InsertOcorrenciaPjCallback implements HibernateCallback<OcorrenciaPessoaJuridica>
{
	
	private Cliente cliente;
	private Operacao operacao;
	private Integer ano, mes;
	private BigDecimal rendimento, imposto;
	
	
	public InsertOcorrenciaPjCallback(Cliente cliente, Operacao operacao, Integer ano, 
			Integer mes, BigDecimal rendimento, BigDecimal imposto)
	{
		this.cliente=cliente;
		this.operacao=operacao;
		this.ano=ano;
		this.mes=mes;
		this.rendimento=rendimento;
		this.imposto=imposto;
	}

	@Override
	public OcorrenciaPessoaJuridica doInHibernate(Session session) throws HibernateException, SQLException
	{
		OcorrenciaPessoaJuridica ocorrencia = new OcorrenciaPessoaJuridica();
		ocorrencia.setCliente(cliente);
		ocorrencia.setAno(ano);
		ocorrencia.setMes(mes);
		ocorrencia.setOperacao(operacao);
		ocorrencia.setRendimento(rendimento);
		ocorrencia.setImpostoRenda(imposto);
		session.save(ocorrencia);
		return ocorrencia;
	}

}
