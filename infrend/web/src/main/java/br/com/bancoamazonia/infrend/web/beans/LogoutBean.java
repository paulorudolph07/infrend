package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.bancoamazonia.infrend.web.services.SeguClientService;

@ManagedBean
public class LogoutBean implements Serializable {
	private static final long serialVersionUID = 4705316389468689231L;
	
	public void logout() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession)context.getExternalContext().getSession(true);
					
			ApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext((ServletContext)context.getExternalContext().getContext());
			SeguClientService seguClientService = webContext.getBean(SeguClientService.class);
			HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
			seguClientService.logout(response, (String)session.getAttribute("authkey"));
			session.invalidate();
		} catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), "msg_detail"));
		}
	}
}
