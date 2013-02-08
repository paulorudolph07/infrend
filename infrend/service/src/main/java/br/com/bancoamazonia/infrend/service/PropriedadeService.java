package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bancoamazonia.infrend.dao.LayoutArquivoDao;
import br.com.bancoamazonia.infrend.dao.PropriedadeDao;
import br.com.bancoamazonia.infrend.modelo.LayoutArquivo;
import br.com.bancoamazonia.infrend.modelo.Propriedade;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;

public class PropriedadeService
{
	private LayoutArquivoDao layoutArquivoDao;
	private PropriedadeDao propriedadeDao;
	public LayoutArquivoDao getLayoutArquivoDao() 
	{
		return layoutArquivoDao;
	}
	public void setLayoutArquivoDao(LayoutArquivoDao layoutArquivoDao)
	{
		this.layoutArquivoDao = layoutArquivoDao;
	}
	public PropriedadeDao getPropriedadeDao()
	{
		return propriedadeDao;
	}
	public void setPropriedadeDao(PropriedadeDao propriedadeDao)
	{
		this.propriedadeDao = propriedadeDao;
	}
	public Propriedade get(Serializable id)
	{
		return propriedadeDao.get(Propriedade.class, id);
	}
	public List<Propriedade> list()
	{
		return propriedadeDao.list();
	}
	public Serializable save(Propriedade propriedade)
	{
		return propriedadeDao.save(propriedade);
	}
	public void update(Propriedade propriedade)
	{
		propriedadeDao.update(propriedade);
	}
	public void delete(Propriedade propriedade)
	{
		propriedadeDao.delete(propriedade);
	}
	public List<Propriedade> listOut(List<Propriedade> excludedPropriedades)
	{
		List<BigInteger> ids = new ArrayList<BigInteger>();
		for(Propriedade p : excludedPropriedades)
			ids.add(p.getId());
		return propriedadeDao.listOut(ids);
	}
	public List<Propriedade> listIn(List<BigInteger> includedIds)
	{
		return propriedadeDao.listIn(includedIds);
	}
	public List<Propriedade> findAllBySpecArquivo(SpecArquivo specArquivo)
	{
		return propriedadeDao.findAllBySpecArquivo(specArquivo);
	}
	/**
	 * Monta Map, no qual as chaves sao compostas pela descricao dos layouts 
	 * e os valores uma lista de propriedades associada ao respectivo layout
	 * @return
	 */
	public Map<String, List<Propriedade>> toLayoutMap(SpecArquivo specArq)
	{
		Map<String, List<Propriedade>> props = new HashMap<String, List<Propriedade>>();
		for(LayoutArquivo layout: layoutArquivoDao.list(LayoutArquivo.class))
		{
			List<Propriedade> propList = propriedadeDao.listBySpecArquivoAndLayoutArquivo(specArq, layout);
			if(propList != null && !propList.isEmpty())
				props.put(layout.getDescricao().toLowerCase(), propList);
		}
		return props;
	}
	
	public Map<String, String> toMap(String texto, List<Propriedade> propriedades) 
	{
		Map<String, String> params = new HashMap<String, String>();
		int posicaoInicial = 0, posicaoFinal = 0;
		for(Propriedade p : propriedades) 
		{
			posicaoFinal = posicaoInicial + p.getTamanho();
			if(p.getTipo().getId().equals(new BigInteger("2")))
				params.put(p.getNome(), texto.substring(posicaoInicial, posicaoFinal));
			else if(p.getTipo().getId().equals(new BigInteger("3")))
				params.put(p.getNome(), texto.substring(posicaoInicial));
			posicaoInicial += p.getTamanho();
		}
		return params;
	}

}
