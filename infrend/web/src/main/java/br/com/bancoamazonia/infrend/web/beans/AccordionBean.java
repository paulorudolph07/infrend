package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.TabChangeEvent;

@ManagedBean
public class AccordionBean implements Serializable
{
	private static final long serialVersionUID = 3173773566268991233L;
	/**
	 * tabIndex = -1, accordion desativado inicialmente 
	 */
	private int tabIndex = -1;
	public int getTabIndex()
	{
		return tabIndex;
	}
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}
	public void onTabChange(TabChangeEvent event)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
		session.setAttribute("accordionTitle", event.getTab().getTitle());
		tabIndex = Integer.parseInt((String)event.getTab().getAttributes().get("index"));
	}
}
