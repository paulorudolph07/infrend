package br.com.bancoamazonia.infrend.dao;

import java.util.Date;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;

import br.com.bancoamazonia.infrend.modelo.ArquivoProcessado;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;

public class ArquivoProcessadoDao extends Dao<ArquivoProcessado> {

	@SuppressWarnings("unchecked")
	public ArquivoProcessado findByNomeAndSpecArquivo(String nome, SpecArquivo specArquivo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArquivoProcessado.class);
		criteria
		.add(Restrictions.eq("nome", nome))
		.add(Restrictions.eq("specArquivo", specArquivo));
		return (ArquivoProcessado) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}
	@SuppressWarnings("unchecked")
	public ArquivoProcessado findByNomeAndSpecArquivo(String nome, SpecArquivo specArquivo, Date dataCalendario) 
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArquivoProcessado.class);
		criteria
		.add(Restrictions.eq("nome", nome))
		.add(Restrictions.eq("specArquivo", specArquivo))
		.add(Restrictions.eq("dataCalendario", dataCalendario));
		return (ArquivoProcessado) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}
}
