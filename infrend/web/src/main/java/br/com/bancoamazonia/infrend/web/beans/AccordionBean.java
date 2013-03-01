package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
public class AccordionBean implements Serializable {
	private static final long serialVersionUID = 3173773566268991233L;
	private AccordionPanel panel;
	
	public AccordionPanel getPanel() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
		Boolean authenticated = (Boolean)session.getAttribute("authenticated");
		if(authenticated == null)
			panel = new AccordionPanel();
		else panel = (AccordionPanel)session.getAttribute("menu");
		return this.panel;
	}
	public void setPanel(AccordionPanel panel) {
		this.panel = panel;
	}
	public void onTabChange(TabChangeEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
		session.setAttribute("accordionTitle", event.getTab().getTitle());
		panel.setActiveIndex((String)event.getTab().getAttributes().get("index"));
	}
}
