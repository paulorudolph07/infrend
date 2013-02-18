package br.com.bancoamazonia.infrend.dao;

import java.math.BigDecimal;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaFisica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class OcorrenciaPfDao extends Dao<OcorrenciaPessoaFisica> {
	
	@SuppressWarnings("unchecked")
	public OcorrenciaPessoaFisica getByContaAndOperacaoAndAno(Operacao operacao, Integer ano) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OcorrenciaPessoaFisica.class);
		criteria
		.add(Restrictions.eq("operacao", operacao))
		.add(Restrictions.eq("ano", ano));
		return (OcorrenciaPessoaFisica)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}
	@SuppressWarnings("unchecked")
	public BigDecimal sumFieldByClienteAndOperacao(String field, Cliente cliente, Operacao op, Integer ano) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OcorrenciaPessoaFisica.class, "o");
		criteria
		.createAlias("o.dadoBancario", "db")
		.setProjection(Projections.projectionList()
				.add(Projections.sum("o."+field)))
		.add(Restrictions.eq("o.operacao", op))
		.add(Restrictions.eq("o.ano", ano))
		.add(Restrictions.eq("db.cliente.id", cliente.getId()));
		BigDecimal sum = (BigDecimal)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
		return sum!=null?sum:new BigDecimal(0);
	}
	
}
