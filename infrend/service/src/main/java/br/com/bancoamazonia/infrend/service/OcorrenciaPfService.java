package br.com.bancoamazonia.infrend.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

import br.com.bancoamazonia.infrend.callback.UpdateOcorrenciaPfCallback;
import br.com.bancoamazonia.infrend.dao.OcorrenciaPfDao;
import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaFisica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class OcorrenciaPfService
{
	
	public class OcorrenciaPfServiceException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		public OcorrenciaPfServiceException(String message)
		{
			super(message);
		}
		public OcorrenciaPfServiceException(Throwable e)
		{
			super(e);
		}
	}
	
	public OcorrenciaPfService()
	{
		super();
		// {ABCDEFGHI}JLMNOPQRS = 01234567890123456789
		char[] list = "{ABCDEFGHI".toCharArray();
		for(int i = 0; i < list.length; i++)
			caracters.put(list[i], Character.forDigit(i, 10));
		list = "}JKLMNOPQR".toCharArray();
		for(int i = 0; i < list.length; i++)
			caracters.put(list[i], Character.forDigit(i, 10));
	}

	private Map<Character, Character> caracters = new HashMap<Character, Character>();
	private BigDecimal negative = new BigDecimal("-1");
	private BigDecimal positive = new BigDecimal("1");
	private OcorrenciaPfDao ocorrenciaPfDao;
	private OperacaoService operacaoService;
	public OcorrenciaPfDao getOcorrenciaPfDao()
	{
		return ocorrenciaPfDao;
	}
	public void setOcorrenciaPfDao(OcorrenciaPfDao ocorrenciaPfDao)
	{
		this.ocorrenciaPfDao = ocorrenciaPfDao;
	}
	public void setOperacaoService(OperacaoService operacaoService)
	{
		this.operacaoService = operacaoService;
	}
	private BigDecimal setSignal(String value)
	{
		char c = value.charAt(value.length()-1);
		BigDecimal signal = positive;
		if("}JKLMNOPQR".contains(String.valueOf(c)))
			signal = negative;
		try
		{
			value = value.replace(c, caracters.get(c));
		} 
		catch(NullPointerException e) {/*para valores inteiros*/}
		return new BigDecimal(new StringBuffer(value).insert(value.length()-2, ".").toString()).multiply(signal);
	}
	
	public void insert(Cliente cliente, Integer ano, String operacao, String saldoAnterior, String saldoAtual, String rendimento)
	{
		OcorrenciaPessoaFisica ocorrencia = null;
		Operacao operacaoInstance;
		operacaoInstance = operacaoService.load(new BigInteger(operacao));
		try
		{
			ocorrencia = new OcorrenciaPessoaFisica();
			ocorrencia.setCliente(cliente);
			ocorrencia.setAno(ano);
			ocorrencia.setOperacao(operacaoInstance);
			ocorrencia.setSaldoAnterior(setSignal(saldoAnterior));
			ocorrencia.setSaldoAtual(setSignal(saldoAtual));
			ocorrencia.setRendimento(setSignal(rendimento));
			ocorrenciaPfDao.save(ocorrencia);
		}
		catch(DataIntegrityViolationException e)
		{
			ocorrenciaPfDao.clear();
			ocorrenciaPfDao.getHibernateTemplate().executeWithNewSession(
					new UpdateOcorrenciaPfCallback(cliente, operacaoInstance, 
							ano, setSignal(saldoAnterior), setSignal(saldoAtual), setSignal(rendimento)));
		}
	}
	
	public Map<String, Object> toMap(Cliente cliente, Integer ano)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		List<Operacao> operacoes = operacaoService.pfOperacaoList();
		DecimalFormat formatador = new DecimalFormat("###,###,###,##0.00");
		
		Field fields[] = OcorrenciaPessoaFisica.class.getDeclaredFields();
		BigDecimal rendimentoTotal = new BigDecimal(0);
		
		for(Operacao op : operacoes)
		{
			for(Field f : fields)
			{
				if(f.getType().equals(BigDecimal.class))
				{
					BigDecimal value = ocorrenciaPfDao.sumFieldByClienteAndOperacao(f.getName(), cliente, op, ano);
					params.put(op.getTipo().toLowerCase()+"_"+f.getName(), formatador.format(value));
					// para operacoes de fundos e prazo
					if((op.getId().equals(new BigInteger("2")) ||
							op.getId().equals(new BigInteger("3"))) &&
							f.getName().equals("rendimento"))
						{
							rendimentoTotal = rendimentoTotal.add(value);
						}
				}
			}
		}
		params.put("rendimento_total", formatador.format(rendimentoTotal));
		return params;
	}
}
