package br.com.bancoamazonia.infrend.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.ArquivoProcessadoDao;
import br.com.bancoamazonia.infrend.dao.StatusArquivoDao;
import br.com.bancoamazonia.infrend.modelo.ArquivoProcessado;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;
import br.com.bancoamazonia.infrend.modelo.StatusArquivo;

public class ArquivoProcessadoService
{
	private ArquivoProcessadoDao arquivoProcessadoDao;
	private StatusArquivoDao statusArquivoDao; 
	public void setArquivoProcessadoDao(ArquivoProcessadoDao arquivoProcessadoDao)
	{
		this.arquivoProcessadoDao = arquivoProcessadoDao;
	}
	public void setStatusArquivoDao(StatusArquivoDao statusArquivoDao)
	{
		this.statusArquivoDao = statusArquivoDao;
	}
	public ArquivoProcessado findByNomeAndSpecArquivo(String fileName, SpecArquivo specArquivo)
	{
		return arquivoProcessadoDao.findByNomeAndSpecArquivo(fileName, specArquivo);
	}
	public ArquivoProcessado findByNomeAndSpecArquivo(String fileName, SpecArquivo specArquivo, Date dataCalendario) throws Exception
	{
		return arquivoProcessadoDao.findByNomeAndSpecArquivo(fileName, specArquivo, dataCalendario);
	}
	public ArquivoProcessado insert(String fileName, SpecArquivo specArquivo, Date dataCalendario)
	{
		ArquivoProcessado arqProc = new ArquivoProcessado();
		arqProc.setDataCalendario(dataCalendario);
		arqProc.setNome(fileName);
		arqProc.setSpecArquivo(specArquivo);
		// seta status EM_PROCESSAMENTO
		arqProc.setStatus(statusArquivoDao.get(StatusArquivo.class, new BigInteger("1")));
		arquivoProcessadoDao.save(arqProc);
		return arqProc;
	}
	public void update(ArquivoProcessado arquivoProcessado, BigInteger statusId)
	{
		arquivoProcessado.setStatus(statusArquivoDao.get(StatusArquivo.class, statusId));
		arquivoProcessadoDao.update(arquivoProcessado);
	}
	public void update(ArquivoProcessado arquivoProcessado)
	{
		arquivoProcessadoDao.update(arquivoProcessado);
	}
	public List<ArquivoProcessado> list()
	{
		return arquivoProcessadoDao.list(ArquivoProcessado.class);
	}
	public void delete(ArquivoProcessado arquivoProcessado)
	{
		arquivoProcessadoDao.delete(arquivoProcessado);
	}
}
