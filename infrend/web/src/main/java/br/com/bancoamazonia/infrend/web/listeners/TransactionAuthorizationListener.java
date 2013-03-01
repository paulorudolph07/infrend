package br.com.bancoamazonia.infrend.web.listeners;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.bancoamazonia.infrend.web.services.SeguClientService;
/**
 * Valida se a pagina (link) requisitada pertence as transacoes do usuario
 * @author 7485
 *
 */
public class TransactionAuthorizationListener implements PhaseListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(true);
				
		ApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext((ServletContext)context.getExternalContext().getContext());
		SeguClientService seguClientService = webContext.getBean(SeguClientService.class);
		String authKey = (String)session.getAttribute("authkey");
		if(authKey != null && !authKey.equals("")) {
			try {
				String transactions = seguClientService.getTransactionsAsTextByAuthKey(authKey);
				if(!transactions.contains(context.getViewRoot().getViewId())) {
					NavigationHandler nh = context.getApplication().getNavigationHandler();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Voce nao tem permissao de acesso a esta funcionalidade!", "msg_detail"));
					nh.handleNavigation(context, null, "/error.xhtml");
				}
				if(session.getAttribute("user_roles") == null)
					session.setAttribute("user_roles", seguClientService.getUserRolesAsTextByAuthKey(authKey));
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
	public void beforePhase(PhaseEvent arg0) {}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
