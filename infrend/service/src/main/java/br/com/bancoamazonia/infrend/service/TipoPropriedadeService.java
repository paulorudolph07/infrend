package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.TipoPropriedadeDao;
import br.com.bancoamazonia.infrend.modelo.TipoPropriedade;

public class TipoPropriedadeService {

	private TipoPropriedadeDao tipoPropriedadeDao;
	public TipoPropriedadeDao getTipoPropriedadeDao() {
		return tipoPropriedadeDao;
	}
	public void setTipoPropriedadeDao(TipoPropriedadeDao tipoPropriedadeDao) {
		this.tipoPropriedadeDao = tipoPropriedadeDao;
	}
	public TipoPropriedade load(Serializable id)
	{
		return tipoPropriedadeDao.load(TipoPropriedade.class, id);
	}
	public TipoPropriedade get(Serializable id)
	{
		return tipoPropriedadeDao.get(TipoPropriedade.class, id);
	}
	public List<TipoPropriedade> list() {
		return tipoPropriedadeDao.list(TipoPropriedade.class);
	}
}
