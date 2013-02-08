package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.StatusArquivoDao;
import br.com.bancoamazonia.infrend.modelo.StatusArquivo;

public class StatusArquivoService
{
	private StatusArquivoDao statusArquivoDao;
	public void setStatusArquivoDao(StatusArquivoDao statusArquivoDao)
	{
		this.statusArquivoDao = statusArquivoDao;
	}
	public StatusArquivo get(Serializable id)
	{
		return statusArquivoDao.get(StatusArquivo.class, id);
	}
	public List<StatusArquivo> list()
	{
		return statusArquivoDao.list(StatusArquivo.class);
	}
}
