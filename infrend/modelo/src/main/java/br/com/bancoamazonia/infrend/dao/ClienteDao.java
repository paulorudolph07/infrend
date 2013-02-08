package br.com.bancoamazonia.infrend.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.DadoBancario;

public class ClienteDao extends Dao<Cliente> {
	
	@SuppressWarnings("unchecked")
	public Cliente findByCodigo(String codigo)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Cliente.class);
		criteria.add(Restrictions.eq("codigo", codigo));
		return (Cliente)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}
	@SuppressWarnings("unchecked")
	public Cliente findByCodigoAndDadoBancario(String codigo, DadoBancario db) 
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Cliente.class);
		criteria
		.add(Restrictions.eq("codigo", codigo))
		.add(Restrictions.eq("dadoBancario.numeroAgencia", db.getNumeroAgencia()))
		.add(Restrictions.eq("dadoBancario.numeroConta", db.getNumeroConta()));
		return (Cliente)DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

}
