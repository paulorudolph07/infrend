package br.com.bancoamazonia.infrend.callback;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaJuridica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class UpdateOcorrenciaPjCallback implements HibernateCallback<OcorrenciaPessoaJuridica>
{
	
	private Cliente cliente;
	private Operacao operacao;
	private Integer ano, mes;
	private BigDecimal rendimento, imposto;
	
	
	public UpdateOcorrenciaPjCallback(Cliente cliente, Operacao operacao, Integer ano, 
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
		Criteria criteria = session.createCriteria(OcorrenciaPessoaJuridica.class);
		criteria
		.add(Restrictions.eq("cliente", cliente))
		.add(Restrictions.eq("operacao", operacao))
		.add(Restrictions.eq("ano", ano))
		.add(Restrictions.eq("mes", mes));
		OcorrenciaPessoaJuridica ocorrencia = (OcorrenciaPessoaJuridica)criteria.uniqueResult();
		ocorrencia.setRendimento(ocorrencia.getRendimento().add(rendimento));
		ocorrencia.setImpostoRenda(ocorrencia.getImpostoRenda().add(imposto));
		session.update(ocorrencia);
		return ocorrencia;
	}
	
}
