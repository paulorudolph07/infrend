package br.com.bancoamazonia.infrend.dao;

import java.math.BigDecimal;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaJuridica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class OcorrenciaPjDao extends Dao<OcorrenciaPessoaJuridica> {
	
	@SuppressWarnings("unchecked")
	public OcorrenciaPessoaJuridica getByContaAndOperacaoAndAnoAndMes(Operacao operacao, Integer ano, Integer mes)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(OcorrenciaPessoaJuridica.class);
		criteria
		.add(Restrictions.eq("operacao", operacao))
		.add(Restrictions.eq("ano", ano))
		.add(Restrictions.eq("mes", mes));
		return (OcorrenciaPessoaJuridica)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal sumRendimentoByClienteAndOperacao(Cliente cliente, Operacao op, Integer ano, Integer mes)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(OcorrenciaPessoaJuridica.class, "o");
		criteria
		.setProjection(Projections.projectionList()
				.add(Projections.sum("o.rendimento")))
		.add(Restrictions.eq("o.operacao", op))
		.add(Restrictions.eq("o.ano", ano))
		.add(Restrictions.eq("o.mes", mes))
		.add(Restrictions.eq("o.cliente", cliente));
		BigDecimal sum = (BigDecimal)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
		return sum!=null?sum:new BigDecimal(0);
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal sumImpostoRendaByClienteAndOperacao(Cliente cliente, Operacao op, Integer ano, Integer mes)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(OcorrenciaPessoaJuridica.class, "o");
		criteria
		.setProjection(Projections.projectionList()
				.add(Projections.sum("o.impostoRenda")))
		.add(Restrictions.eq("o.operacao", op))
		.add(Restrictions.eq("o.ano", ano))
		.add(Restrictions.eq("o.mes", mes))
		.add(Restrictions.eq("o.cliente", cliente));
		BigDecimal sum = (BigDecimal)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
		return sum!=null?sum:new BigDecimal(0);
	}

}
