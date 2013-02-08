package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.LayoutArquivoDao;
import br.com.bancoamazonia.infrend.modelo.LayoutArquivo;

public class LayoutArquivoService {

	private LayoutArquivoDao layoutArquivoDao;
	public void setLayoutArquivoDao(LayoutArquivoDao layoutArquivoDao)
	{
		this.layoutArquivoDao = layoutArquivoDao;
	}
	public LayoutArquivo load(Serializable id)
	{
		return layoutArquivoDao.load(LayoutArquivo.class, id);
	}
	public LayoutArquivo get(Serializable id)
	{
		return layoutArquivoDao.get(LayoutArquivo.class, id);
	}
	public List<LayoutArquivo> list()
	{
		return layoutArquivoDao.list(LayoutArquivo.class);
	}
	public void save(LayoutArquivo layoutArquivo)
	{
		layoutArquivoDao.save(layoutArquivo);
	}
	public void update(LayoutArquivo layoutArquivo)
	{
		layoutArquivoDao.update(layoutArquivo);
	}
	public void delete(LayoutArquivo layoutArquivo)
	{
		layoutArquivoDao.delete(layoutArquivo);
	}
}
