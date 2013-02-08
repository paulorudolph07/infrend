package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import br.com.bancoamazonia.infrend.dao.ClienteDao;
import br.com.bancoamazonia.infrend.dao.TipoClienteDao;
import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.DadoBancario;
import br.com.bancoamazonia.infrend.modelo.TipoCliente;

public class ClienteService
{

	public class ClienteServiceException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		public ClienteServiceException(String message)
		{
			super(message);
		}
		public ClienteServiceException(Throwable e)
		{
			super(e);
		}
	}
	
	private ClienteDao clienteDao;
	private TipoClienteDao tipoClienteDao;
	public ClienteDao getClienteDao()
	{
		return clienteDao;
	}
	public void setClienteDao(ClienteDao clienteDao)
	{
		this.clienteDao = clienteDao;
	}
	public TipoClienteDao getTipoClienteDao()
	{
		return tipoClienteDao;
	}
	public void setTipoClienteDao(TipoClienteDao tipoClienteDao)
	{
		this.tipoClienteDao = tipoClienteDao;
	}
	public Cliente get(Serializable id)
	{
		return clienteDao.get(Cliente.class, id);
	}
	public Cliente find(Map<String, String> params)
	{
		return clienteDao.findByCodigoAndDadoBancario(params.get("codigo"),
					new DadoBancario(params.get("agencia"), params.get("conta")));
	}
	public Cliente findByCodigo(String codigo)
	{
		return clienteDao.findByCodigo(codigo);
	}
	public Cliente insert(Map<String, String> params)
	{
		if(Long.valueOf(params.get("codigo")).equals(0l))
			throw new StringIndexOutOfBoundsException("Codigo do cliente invalido: " + params.get("codigo"));
		Cliente cliente = new Cliente();
		cliente.setCodigo(params.get("codigo"));
		// 1 = pessoa fisica, 2 = pessoa juridica
		String tipoCliente = params.get("tipoCliente")!=null?params.get("tipoCliente"):"2";
		cliente.setTipo(tipoClienteDao.load(TipoCliente.class, new BigInteger(tipoCliente)));
		// sempre atualiza os dados
		cliente.setBairro(params.get("bairro"));
		cliente.setCep(params.get("cep"));
		cliente.setCidade(params.get("cidade"));
		cliente.setLogradouro(params.get("logradouro"));
		cliente.setNome(params.get("nome"));
		cliente.setUf(params.get("uf"));
		// dado bancario
		//cliente.setDadoBancario(new DadoBancario(params.get("agencia"), params.get("conta"), params.get("digito")));
		clienteDao.save(cliente);
		return cliente;
	}
	
	public void update(Cliente cliente, Map<String, String> params)
	{
		cliente.setBairro(params.get("bairro"));
		cliente.setCep(params.get("cep"));
		cliente.setCidade(params.get("cidade"));
		cliente.setLogradouro(params.get("logradouro"));
		cliente.setNome(params.get("nome"));
		cliente.setUf(params.get("uf"));
		clienteDao.update(cliente);
	}
	
	public Map<String, Object> toMap(Cliente cliente)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nome", cliente.getNome());
		return params;
	}
}
