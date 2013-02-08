package br.com.bancoamazonia.infrend.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

import br.com.bancoamazonia.infrend.callback.UpdateOcorrenciaPjCallback;
import br.com.bancoamazonia.infrend.dao.OcorrenciaPjDao;
import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaJuridica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class OcorrenciaPjService
{
	
	public class OcorrenciaPjServiceException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		public OcorrenciaPjServiceException(String message)
		{
			super(message);
		}
		public OcorrenciaPjServiceException(Throwable e)
		{
			super(e);
		}
	}
	
	private OcorrenciaPjDao ocorrenciaPjDao;
	private OperacaoService operacaoService;
	public void setOcorrenciaPjDao(OcorrenciaPjDao ocorrenciaPjDao)
	{
		this.ocorrenciaPjDao = ocorrenciaPjDao;
	}
	public void setOperacaoService(OperacaoService operacaoService)
	{
		this.operacaoService = operacaoService;
	}
	public void insert(Cliente cliente, Integer ano, Integer mesFinal, String operacao, String rendimentos)
	{
		StringBuffer rendText, impostoText;
		BigDecimal rendValue = new BigDecimal(0), 
					impostoValue = new BigDecimal(0);
		int inc = 12, 
			posicao = (mesFinal/3-1)*3*(2*inc), 
			mes = mesFinal-2;
			Operacao operacaoInstance = operacaoService.load(new BigInteger(operacao));
			OcorrenciaPessoaJuridica ocorrencia = null;
		for(; mes <= mesFinal; mes++)
		{
			rendText = new StringBuffer(rendimentos.substring(posicao, posicao+=inc));
			rendValue = new BigDecimal(rendText.insert(rendText.length()-2, ".").toString());
			
			impostoText = new StringBuffer(rendimentos.substring(posicao, posicao+=inc));
			impostoValue = new BigDecimal(impostoText.insert(impostoText.length()-2, ".").toString());
			try
			{
				ocorrencia = new OcorrenciaPessoaJuridica();
				ocorrencia.setCliente(cliente);
				ocorrencia.setAno(ano);
				ocorrencia.setMes(mes);
				ocorrencia.setOperacao(operacaoInstance);
				ocorrencia.setRendimento(rendValue);
				ocorrencia.setImpostoRenda(impostoValue);
				ocorrenciaPjDao.save(ocorrencia);
			}
			catch(DataIntegrityViolationException e)
			{
				ocorrenciaPjDao.clear();
				ocorrenciaPjDao.getHibernateTemplate().executeWithNewSession(
						new UpdateOcorrenciaPjCallback(cliente, operacaoInstance, ano, mes, rendValue, impostoValue));
			}
		}
	}
	
	public Map<String, Object> toMap(Cliente cliente, Integer ano, Integer trimestre)
	{
		Map<String, Object> params = new HashMap<String, Object>(); 
		Integer mesFinal = 3*trimestre;
		Integer mesInicial = mesFinal-2;
		
		List<Operacao> operacoes = operacaoService.pjOperacaoList();
		
		DecimalFormat formatador = new DecimalFormat("###,###,###,##0.00");
		for(Operacao op : operacoes)
		{
			for(int mes = mesInicial; mes <= mesFinal; mes++)
			{
				// criamos as chaves referentes aos nomes dos parametros no relatorio
				String rendKey = op.getTipo().toLowerCase()+"_rendimento_"+mes;
				String impostoKey = op.getTipo().toLowerCase()+"_imposto_"+mes;
				params.put(rendKey, formatador.format(ocorrenciaPjDao.sumRendimentoByClienteAndOperacao(cliente, op, ano, mes)));
				params.put(impostoKey, formatador.format(ocorrenciaPjDao.sumImpostoRendaByClienteAndOperacao(cliente, op, ano, mes)));
			}
		}
		// setamos os parametros de conta corrente separadamente
		Operacao contaCorrenteOp = operacaoService.get(new BigInteger("1"));
		params.put("conta_corrente_1", formatador.format(ocorrenciaPjDao.sumImpostoRendaByClienteAndOperacao(cliente, contaCorrenteOp, ano-1, 1)));
		params.put("conta_corrente_2", formatador.format(ocorrenciaPjDao.sumImpostoRendaByClienteAndOperacao(cliente, contaCorrenteOp, ano, 1)));
		return params;
	}
}
