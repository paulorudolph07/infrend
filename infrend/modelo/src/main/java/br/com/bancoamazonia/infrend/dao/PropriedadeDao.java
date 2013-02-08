package br.com.bancoamazonia.infrend.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.com.bancoamazonia.infrend.modelo.LayoutArquivo;
import br.com.bancoamazonia.infrend.modelo.Propriedade;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;

public class PropriedadeDao extends Dao<Propriedade>
{
	@SuppressWarnings("unchecked")
	public List<Propriedade> list()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Propriedade.class);
		criteria.addOrder(Order.asc("id"));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Propriedade> listOut(List<BigInteger> excludedIds)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Propriedade.class);
		criteria.add(Restrictions.not(Restrictions.in("id", excludedIds)));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Propriedade> listIn(List<BigInteger> includedIds)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Propriedade.class);
		criteria.add(Restrictions.in("id", includedIds));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Propriedade> findAllBySpecArquivo(SpecArquivo specArquivo)
	{
		DetachedCriteria specArqCriteria = DetachedCriteria.forClass(SpecArquivo.class, "specArquivo");
		specArqCriteria
		.setProjection(Projections.id())
		.add(Restrictions.idEq(specArquivo.getId()))
		.add(Property.forName("specArquivo.id").eqProperty("specArquivoProp.id"));
		
		DetachedCriteria propriedadeCriteria = DetachedCriteria.forClass(Propriedade.class);
		propriedadeCriteria
		.createAlias("specArquivos", "specArquivoProp")
		.add(Subqueries.exists(specArqCriteria))
		.addOrder(Order.asc("id"));
		
		return getHibernateTemplate().findByCriteria(propriedadeCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Propriedade> listBySpecArquivoAndLayoutArquivo(SpecArquivo spec, LayoutArquivo layout)
	{
		DetachedCriteria specArqCriteria = DetachedCriteria.forClass(SpecArquivo.class, "specArquivo");
		specArqCriteria
		.setProjection(Projections.id())
		.add(Restrictions.idEq(spec.getId()))
		.add(Property.forName("specArquivo.id").eqProperty("specArquivoProp.id"));
		
		DetachedCriteria propriedadeCriteria = DetachedCriteria.forClass(Propriedade.class);
		propriedadeCriteria
		.createAlias("specArquivos", "specArquivoProp")
		.add(Restrictions.eq("layoutArquivo", layout))
		.add(Subqueries.exists(specArqCriteria))
		.addOrder(Order.asc("ordem"));
		return getHibernateTemplate().findByCriteria(propriedadeCriteria);
	}
	
	/*public List<Propriedade> listPf() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Propriedade.class);
		criteria
		.add(Restrictions.between("id", new BigInteger("1"), new BigInteger("16")))
		.add(Restrictions.ne("id",  new BigInteger("12")));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List<Propriedade> listPj() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Propriedade.class);
		criteria
		.add(Restrictions.between("id", new BigInteger("1"), new BigInteger("12")));
		return getHibernateTemplate().findByCriteria(criteria);
	}*/

}
