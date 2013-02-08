package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;

import br.com.bancoamazonia.infrend.modelo.Operacao;
import br.com.bancoamazonia.infrend.service.OperacaoService;

@ManagedBean
public class OperacaoBean implements Serializable {
	private static final long serialVersionUID = -1958294332948652399L;
	private OperacaoService operacaoService;
	public void setOperacaoService(OperacaoService operacaoService) {
		this.operacaoService = operacaoService;
	}
	public List<Operacao> getList() {
		return operacaoService.list();
	}
}
