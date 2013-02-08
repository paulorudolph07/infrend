package br.com.bancoamazonia.infrend.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class Dao<T> extends HibernateDaoSupport {

	public Serializable save(T obj) throws DataAccessException {
		return getHibernateTemplate().save(obj);
	}
		
	public void delete(T obj) throws DataAccessException {
		getHibernateTemplate().delete(obj);
	}

	public void update(T obj) throws DataAccessException {
		getHibernateTemplate().update(obj);
	}
	
	public void saveOrUpdate(T obj) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public T merge(T obj) throws DataAccessException {
		return getHibernateTemplate().merge(obj);
	}
	
	public void flush() throws DataAccessException {
		getHibernateTemplate().flush();
	}
	
	public void clear() throws DataAccessException {
		getHibernateTemplate().clear();
	}
	
	public void evict(T obj) throws DataAccessException {
		getHibernateTemplate().evict(obj);
	}
	
	public void persit(T obj) throws DataAccessException {
		getHibernateTemplate().persist(obj);
	}

	@SuppressWarnings("unchecked")
	public List<T> list(Class<T> clazz) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	public T get(Class<T> clazz, Serializable id) throws DataAccessException {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	public T load(Class<T> clazz, Serializable id) throws DataAccessException {
		return (T) getHibernateTemplate().load(clazz, id);
	}
	
}
