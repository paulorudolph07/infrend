package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
public class IdleMonitorController implements Serializable {
	private static final long serialVersionUID = -7982167537268515059L;
	
	public void idleListener() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,   
	                                        "Sua sessao foi fechada", "Você estava inativo por mais de 5 minutos"));
		//invalidate session 
		((HttpSession)context.getExternalContext().getSession(false)).invalidate();
    }  
  
    public void activeListener() {
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,  
                                        "Bem vindo", "Nova sessao criada"));
    	// a sessao sera criada em br.com.bancoamazonia.infrend.web.listeners.AuthorizationListener
    }
}
