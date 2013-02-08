package br.com.bancoamazonia.infrend.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.com.bancoamazonia.infrend.modelo.Operacao;

public class OperacaoDao extends Dao<Operacao> {

	@SuppressWarnings("unchecked")
	public List<Operacao> listIn(BigInteger[] ids) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Operacao.class);
		criteria
		.add(Restrictions.in("id", ids));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
}
