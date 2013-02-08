package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.bancoamazonia.infrend.modelo.ArquivoProcessado;
import br.com.bancoamazonia.infrend.service.ArquivoProcessadoService;
import br.com.bancoamazonia.infrend.web.beans.tabs.ArquivoProcessadoTabBean;
import br.com.bancoamazonia.infrend.web.beans.tabs.TabBean;

@ManagedBean
public class ArquivoProcessadoBean implements Serializable
{
	private static final long serialVersionUID = -8496327183172897793L;
	private ArquivoProcessadoService arquivoProcessadoService;
	private ArquivoProcessadoTabBean arquivoProcessadoTabBean;
	private ArquivoProcessado selectedArquivoProcessado;
	public void setArquivoProcessadoService(ArquivoProcessadoService arquivoProcessadoService)
	{
		this.arquivoProcessadoService = arquivoProcessadoService;
	}
	public void setArquivoProcessadoTabBean(ArquivoProcessadoTabBean arquivoProcessadoTabBean)
	{
		this.arquivoProcessadoTabBean = arquivoProcessadoTabBean;
	}
	public ArquivoProcessado getSelectedArquivoProcessado()
	{
		return selectedArquivoProcessado;
	}
	public void setSelectedArquivoProcessado(ArquivoProcessado selectedArquivoProcessado)
	{
		this.selectedArquivoProcessado = selectedArquivoProcessado;
	}
	public List<ArquivoProcessado> getList()
	{
		return arquivoProcessadoService.list();
	}
	public void edit()
	{
		arquivoProcessadoTabBean.setTabIndex(TabBean.Tab.EDIT.ordinal());
	}
	public void update()
	{
		arquivoProcessadoService.update(selectedArquivoProcessado);
		arquivoProcessadoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("ArquivoProcessado de id " + selectedArquivoProcessado.getId() + " foi atualizado com sucesso!")
		);
	}
	public void delete()
	{
		arquivoProcessadoService.delete(selectedArquivoProcessado);
		arquivoProcessadoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("ArquivoProcessado de id " + selectedArquivoProcessado.getId() + " foi deletado com sucesso!")
		);
	}
}
