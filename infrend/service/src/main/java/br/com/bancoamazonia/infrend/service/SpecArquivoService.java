package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.SpecArquivoDao;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;

public class SpecArquivoService
{
	private SpecArquivoDao specArquivoDao;
	public void setSpecArquivoDao(SpecArquivoDao specArquivoDao)
	{
		this.specArquivoDao = specArquivoDao;
	}
	public SpecArquivo get(Serializable id)
	{
		return specArquivoDao.get(SpecArquivo.class, id);
	}
	public List<SpecArquivo> list()
	{
		return specArquivoDao.list(SpecArquivo.class);
	}
	public Serializable save(SpecArquivo specArquivo)
	{
		return specArquivoDao.save(specArquivo);
	}
	public void update(SpecArquivo specArquivo)
	{
		specArquivoDao.update(specArquivo);
	}
	public void delete(SpecArquivo specArquivo)
	{
		specArquivoDao.delete(specArquivo);
	}
}
