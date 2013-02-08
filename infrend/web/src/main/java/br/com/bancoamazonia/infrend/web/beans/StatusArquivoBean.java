package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;

import br.com.bancoamazonia.infrend.modelo.StatusArquivo;
import br.com.bancoamazonia.infrend.service.StatusArquivoService;

@ManagedBean
public class StatusArquivoBean implements Serializable
{
	private static final long serialVersionUID = -8865039404795852877L;
	private StatusArquivoService statusArquivoService;
	public void setStatusArquivoService(StatusArquivoService statusArquivoService)
	{
		this.statusArquivoService = statusArquivoService;
	}
	public List<StatusArquivo> getList()
	{
		return statusArquivoService.list();
	}
}
