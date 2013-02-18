package br.com.bancoamazonia.infrend.callback;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.bancoamazonia.infrend.modelo.DadoBancario;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaFisica;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class UpdateOcorrenciaPfCallback implements HibernateCallback<OcorrenciaPessoaFisica> {
	
	private DadoBancario dadoBancario;
	private Operacao operacao;
	private Integer ano;
	private BigDecimal saldoAnterior, saldoAtual,rendimento;
	
	
	public UpdateOcorrenciaPfCallback(DadoBancario dadoBancario, Operacao operacao, Integer ano, 
			BigDecimal saldoAnterior, BigDecimal saldoAtual, BigDecimal rendimento) {
		this.dadoBancario=dadoBancario;
		this.operacao=operacao;
		this.ano=ano;
		this.saldoAnterior=saldoAnterior;
		this.saldoAtual=saldoAtual;
		this.rendimento=rendimento;
	}
	
	@Override
	public OcorrenciaPessoaFisica doInHibernate(Session session) throws HibernateException, SQLException {
		Criteria criteria = session.createCriteria(OcorrenciaPessoaFisica.class);
		criteria
		.add(Restrictions.eq("dadoBancario", dadoBancario))
		.add(Restrictions.eq("operacao", operacao))
		.add(Restrictions.eq("ano", ano));
		OcorrenciaPessoaFisica ocorrencia = (OcorrenciaPessoaFisica)criteria.uniqueResult();
		ocorrencia.setSaldoAnterior(ocorrencia.getSaldoAnterior().add(saldoAnterior));
		ocorrencia.setSaldoAtual(ocorrencia.getSaldoAtual().add(saldoAtual));
		ocorrencia.setRendimento(ocorrencia.getRendimento().add(rendimento));
		session.update(ocorrencia);
		return ocorrencia;
	}
	
}
