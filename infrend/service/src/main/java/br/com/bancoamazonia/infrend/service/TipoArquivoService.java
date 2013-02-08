package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.TipoArquivoDao;
import br.com.bancoamazonia.infrend.modelo.TipoArquivo;

public class TipoArquivoService
{
	private TipoArquivoDao tipoArquivoDao;
	public TipoArquivoDao getTipoArquivoDao()
	{
		return tipoArquivoDao;
	}
	public void setTipoArquivoDao(TipoArquivoDao tipoArquivoDao)
	{
		this.tipoArquivoDao = tipoArquivoDao;
	}
	public TipoArquivo get(Serializable id)
	{
		return tipoArquivoDao.get(TipoArquivo.class, id);
	}
	public List<TipoArquivo> list()
	{
		return tipoArquivoDao.list(TipoArquivo.class);
	}
}
