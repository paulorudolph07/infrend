package br.com.bancoamazonia.infrend.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.bancoamazonia.infrend.dao.OperacaoDao;
import br.com.bancoamazonia.infrend.modelo.Operacao;

public class OperacaoService {

	private OperacaoDao operacaoDao;
	public void setOperacaoDao(OperacaoDao operacaoDao) {
		this.operacaoDao = operacaoDao;
	}
	public Operacao load(Serializable id) {
		return operacaoDao.load(Operacao.class, id);
	}
	public Operacao get(Serializable id) {
		return operacaoDao.get(Operacao.class, id);
	}
	public List<Operacao> list() {
		return operacaoDao.list(Operacao.class);
	}
	/**
	 * 
	 * lista operacoes de conta corrente, prazo, fundos e poupanca, relativas a pessoa fisica
	 * @return
	 */
	public List<Operacao> pfOperacaoList() {
		return operacaoDao.listIn(new BigInteger[]{new BigInteger("1"), new BigInteger("2"), new BigInteger("3"), new BigInteger("5")});
	}
	/**
	 * lista operacoes de prazo, fundos e poupanca, relativas a pessoa juridica
	 * @return
	 */
	public List<Operacao> pjOperacaoList() {
		return operacaoDao.listIn(new BigInteger[]{new BigInteger("2"), new BigInteger("3"), new BigInteger("5")});
	}
}
