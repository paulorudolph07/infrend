package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bancoamazonia.infrend.web.services.SeguClientService;

@ManagedBean
public class LogoutBean implements Serializable {
	private static final long serialVersionUID = 4705316389468689231L;
	private SeguClientService seguClientService;
	public void setSeguClientService(SeguClientService seguClientService) {
		this.seguClientService = seguClientService;
	}
	
	public void logout() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession)context.getExternalContext().getSession(true);
			
			String authkey = (String)session.getAttribute("authkey");
			session.invalidate();
			
			seguClientService.logout((HttpServletResponse)context.getExternalContext().getResponse(), authkey);
		} catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), "msg_detail"));
		}
	}
}
