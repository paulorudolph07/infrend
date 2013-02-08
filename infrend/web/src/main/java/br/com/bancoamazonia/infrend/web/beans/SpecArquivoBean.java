package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.bancoamazonia.infrend.modelo.Propriedade;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;
import br.com.bancoamazonia.infrend.service.PropriedadeService;
import br.com.bancoamazonia.infrend.service.SpecArquivoService;
import br.com.bancoamazonia.infrend.web.beans.tabs.SpecArquivoTabBean;
import br.com.bancoamazonia.infrend.web.beans.tabs.TabBean;

@ManagedBean
public class SpecArquivoBean implements Serializable
{
	private static final long serialVersionUID = 2561286800292527148L;
	private SpecArquivoService specArquivoService;
	private SpecArquivoTabBean specArquivoTabBean;
	private SpecArquivo selectedSpecArquivo, newSpecArquivo;
	//
	private PropriedadeService propriedadeService;
	private List<BigInteger> selectedPropriedades;
	private boolean checked;
	/**
	 * Metodo executado por views (ex.: create.xhtml ou edit.xhtml)
	 */
	public void init()
	{
		newSpecArquivo = new SpecArquivo();
		selectedPropriedades = new ArrayList<BigInteger>();
	}
	public void setSpecArquivoService(SpecArquivoService specArquivoService)
	{
		this.specArquivoService = specArquivoService;
	}
	public void setSpecArquivoTabBean(SpecArquivoTabBean specArquivoTabBean)
	{
		this.specArquivoTabBean = specArquivoTabBean;
	}
	public SpecArquivo getSelectedSpecArquivo()
	{
		return selectedSpecArquivo;
	}
	public void setSelectedSpecArquivo(SpecArquivo selectedSpecArquivo)
	{
		this.selectedSpecArquivo = selectedSpecArquivo;
	}
	public SpecArquivo getNewSpecArquivo() {
		return newSpecArquivo;
	}
	public void setNewSpecArquivo(SpecArquivo newSpecArquivo)
	{
		this.newSpecArquivo = newSpecArquivo;
	}
	public void setPropriedadeService(PropriedadeService propriedadeService)
	{
		this.propriedadeService = propriedadeService;
	}
	public List<BigInteger> getSelectedPropriedades()
	{
		if(specArquivoTabBean.getTabIndex() == TabBean.Tab.EDIT.ordinal())
		{
			//caso tenha sido instanciado pelo metodo init()
			if(selectedPropriedades == null || selectedPropriedades.isEmpty())
				for(Propriedade p : propriedadeService.findAllBySpecArquivo(selectedSpecArquivo))
					selectedPropriedades.add(p.getId());
		}
		return selectedPropriedades;
	}
	public boolean isChecked()
	{
		return checked;
	}
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}
	public List<SpecArquivo> getList()
	{
		specArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		return specArquivoService.list();
	}
	public void save()
	{
		if(selectedPropriedades != null && !selectedPropriedades.isEmpty())
			newSpecArquivo.setPropriedades(propriedadeService.listIn(selectedPropriedades));
		specArquivoService.save(newSpecArquivo);
		specArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("SpecArquivo de id " + newSpecArquivo.getId() + " foi criado com sucesso!")
		);
	}
	public void edit()
	{
		specArquivoTabBean.setTabIndex(TabBean.Tab.EDIT.ordinal());
	}
	public void update()
	{
		if(selectedPropriedades != null && !selectedPropriedades.isEmpty())
			selectedSpecArquivo.setPropriedades(propriedadeService.listIn(selectedPropriedades));
		specArquivoService.update(selectedSpecArquivo);
		specArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("SpecArquivo de id " + selectedSpecArquivo.getId() + " foi atualizado com sucesso!")
		);
	}
	public void delete()
	{
		specArquivoService.delete(selectedSpecArquivo);
		specArquivoTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("SpecArquivo de id " + selectedSpecArquivo.getId() + " foi deletado com sucesso!")
		);
	}
	public void onCheckboxChange(BigInteger id)
	{
		if(checked)
			selectedPropriedades.add(id);
		else selectedPropriedades.remove(id);
	}
}
