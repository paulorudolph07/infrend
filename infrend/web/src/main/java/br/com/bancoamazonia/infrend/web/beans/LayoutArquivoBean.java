package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.bancoamazonia.infrend.modelo.LayoutArquivo;
import br.com.bancoamazonia.infrend.service.LayoutArquivoService;
import br.com.bancoamazonia.infrend.web.beans.tabs.LayoutArquivoTabBean;
import br.com.bancoamazonia.infrend.web.beans.tabs.TabBean;

@ManagedBean
public class LayoutArquivoBean implements Serializable
{
	private static final long serialVersionUID = 2907842740903095729L;
	private LayoutArquivoService layoutArquivoService;
	private LayoutArquivoTabBean layoutArquivoTabBean;
	private LayoutArquivo selectedLayoutArquivo, newLayoutArquivo;
	/**
	 * Inicializa propriedades no construtor, pelo fato do escopo ser do tipo 'view'
	 */
	public LayoutArquivoBean()
	{
		init();
	}
	private void init()
	{
		newLayoutArquivo = new LayoutArquivo();
	}
	public void setLayoutArquivoService(LayoutArquivoService layoutArquivoService)
	{
		this.layoutArquivoService = layoutArquivoService;
	}
	public void setLayoutArquivoTabBean(LayoutArquivoTabBean layoutArquivoTabBean)
	{
		this.layoutArquivoTabBean = layoutArquivoTabBean;
	}
	public LayoutArquivo getSelectedLayoutArquivo() {
		return selectedLayoutArquivo;
	}
	public void setSelectedLayoutArquivo(LayoutArquivo selectedLayoutArquivo) 
	{
		this.selectedLayoutArquivo = selectedLayoutArquivo;
	}
	public LayoutArquivo getNewLayoutArquivo()
	{
		return newLayoutArquivo;
	}
	public void setNewLayoutArquivo(LayoutArquivo newLayoutArquivo)
	{
		this.newLayoutArquivo = newLayoutArquivo;
	}
	public List<LayoutArquivo> getList()
	{
		layoutArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		return layoutArquivoService.list();
	}
	public void save()
	{
		layoutArquivoService.save(newLayoutArquivo);
		layoutArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("LayoutArquivo de id " + newLayoutArquivo.getId() + " foi criado com sucesso!")
		);
		
	}
	public void edit()
	{
		layoutArquivoTabBean.setTabIndex(TabBean.Tab.EDIT.ordinal());
	}
	public void update()
	{
		layoutArquivoService.update(selectedLayoutArquivo);
		layoutArquivoTabBean.setTabIndex(TabBean.Tab.EDIT.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("LayoutArquivo de id " + selectedLayoutArquivo.getId() + " foi atualizado com sucesso!")
		);
	}
	public void delete()
	{
		layoutArquivoService.delete(selectedLayoutArquivo);
		layoutArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("LayoutArquivo de id " + selectedLayoutArquivo.getId() + " foi deletado com sucesso!")
		);
		
	}
}
