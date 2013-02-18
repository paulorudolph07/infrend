package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.com.bancoamazonia.infrend.modelo.Propriedade;
import br.com.bancoamazonia.infrend.service.PropriedadeService;
import br.com.bancoamazonia.infrend.web.beans.tabs.PropriedadeTabBean;
import br.com.bancoamazonia.infrend.web.beans.tabs.TabBean;

@ManagedBean
public class PropriedadeBean implements Serializable {
	private Logger log = Logger.getLogger(getClass());
	private static final long serialVersionUID = -4135687848313181509L;
	private PropriedadeService propriedadeService;
	private PropriedadeTabBean propriedadeTabBean;
	private Propriedade selectedPropriedade, newPropriedade;
	/**
	 * Inicializa propriedades no construtor, pelo fato do escopo ser do tipo 'view'
	 */
	public PropriedadeBean() {
		init();
	}
	private void init() {
		newPropriedade = new Propriedade();
	}
	public void setPropriedadeService(PropriedadeService propriedadeService) {
		this.propriedadeService = propriedadeService;
	}
	public void setPropriedadeTabBean(PropriedadeTabBean propriedadeTabBean) {
		this.propriedadeTabBean = propriedadeTabBean;
	}
	public Propriedade getSelectedPropriedade() {
		return selectedPropriedade;
	}
	public void setSelectedPropriedade(Propriedade selectedPropriedade) {
		this.selectedPropriedade = selectedPropriedade;
	}
	public Propriedade getNewPropriedade() {
		return newPropriedade;
	}
	public void setNewPropriedade(Propriedade newPropriedade) {
		this.newPropriedade = newPropriedade;
	}
	public List<Propriedade> getList() {
		propriedadeTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		return propriedadeService.list();
	}
	public void save() {
		propriedadeService.save(newPropriedade);
		propriedadeTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("Propriedade de id " + newPropriedade.getId() + " foi criada com sucesso!")
		);
	}
	public void edit() {
		propriedadeTabBean.setTabIndex(TabBean.Tab.EDIT.ordinal());
	}
	public void update() {
		propriedadeService.update(selectedPropriedade);
		propriedadeTabBean.setTabIndex(TabBean.Tab.EDIT.ordinal());
		FacesContext.getCurrentInstance().addMessage(
				null, 
				new FacesMessage("Propriedade de id " + selectedPropriedade.getId() + " foi atualizada com sucesso!")
		);
	}
	public void delete() {
		try {
			propriedadeService.delete(selectedPropriedade);
			propriedadeTabBean.setTabIndex(TabBean.Tab.LIST.ordinal());
			FacesContext.getCurrentInstance().addMessage(
					null, 
					new FacesMessage("Propriedade de id " + selectedPropriedade.getId() + " foi deletada com sucesso!")
			);
		} catch(Exception e) {
			log.error(e);
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), "msg_detail"));
		}
	}
}
