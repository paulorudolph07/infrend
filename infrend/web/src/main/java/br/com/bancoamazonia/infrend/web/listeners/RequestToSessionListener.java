package br.com.bancoamazonia.infrend.web.listeners;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestToSessionListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		
		try {
			String link = request.getParameter("currentLink");
			if(link != null && !link.equals(""))
				session.setAttribute("currentLink", request.getParameter("currentLink"));
		}
		catch (Exception e) {
			e.printStackTrace();
			NavigationHandler nh = context.getApplication().getNavigationHandler();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "msg_detail"));
			nh.handleNavigation(context, null, "/error.xhtml");
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
