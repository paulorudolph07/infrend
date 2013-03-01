package br.com.bancoamazonia.infrend.web.listeners;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.bancoamazonia.infrend.web.services.SeguClientService;
import br.com.pdcase.pdsegu.ws.AutenticaServiceWrapper;

public class AuthorizationListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(true);
		Boolean authenticated = (Boolean)session.getAttribute("authenticated");
		if(authenticated == null) {
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
			
			String authKey = (String)session.getAttribute("authkey");
			authKey = authKey==null || authKey.equals("")?request.getParameter("authkey"):authKey;
			
			ApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(
					(ServletContext)context.getExternalContext().getContext());
			SeguClientService seguClientService = webContext.getBean(SeguClientService.class);
			
			try {
				if(authKey == null || authKey.equals("")) {seguClientService.login(response);return;}
				
				AutenticaServiceWrapper autentica = seguClientService.validateUserByAuthKey(authKey);
				
				if(!autentica.getResultado().equals("0")) {seguClientService.login(response);return;}
				
				session.setAttribute("authkey", authKey);
				session.setAttribute("username", autentica.getUsuario().getNome());
				session.setAttribute("authenticated", true);
			}
			catch (Exception e) {
				e.printStackTrace();
				NavigationHandler nh = context.getApplication().getNavigationHandler();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "msg_detail"));
				nh.handleNavigation(context, null, "/error.xhtml");
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
