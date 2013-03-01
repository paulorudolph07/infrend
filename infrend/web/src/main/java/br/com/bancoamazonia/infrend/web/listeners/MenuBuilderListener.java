package br.com.bancoamazonia.infrend.web.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.Tab;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.bancoamazonia.infrend.web.services.SeguClientService;
import br.com.pdcase.pdsegu.ws.TransacaoBean;

public class MenuBuilderListener implements PhaseListener {
	private static final long serialVersionUID = 1L;
	private Integer index = -1;

	@Override
	public void beforePhase(PhaseEvent event) {}

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		try {
			HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
			String authkey = (String)session.getAttribute("authkey");
			if(authkey != null && !authkey.equals("") && session.getAttribute("menu") == null) {
				HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
				ApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext((ServletContext)context.getExternalContext().getContext());
				SeguClientService seguClientService = webContext.getBean(SeguClientService.class);
				List<TransacaoBean> transacoes = new LinkedList<TransacaoBean>(seguClientService.getTransactionsByAuthKey(authkey));
				
				AccordionPanel panel = new AccordionPanel();
				panel.setActiveIndex("-1");
				
				TransacaoBean pai = null;
				for(Iterator<TransacaoBean> it = transacoes.iterator(); it.hasNext();) {
					TransacaoBean trans = it.next();
					if(trans.getIdTransacaoPai() == null || trans.getIdTransacaoPai().equals("")) {
						it.remove();
						pai = trans;
						List<TransacaoBean> filhos = new ArrayList<TransacaoBean>();
						for(Iterator<TransacaoBean> it2 = transacoes.iterator(); it2.hasNext();) {
							TransacaoBean filho = it2.next();
							if(filho.getIdTransacaoPai().equals(pai.getIdTransacao())) {
								it2.remove();
								filhos.add(filho);
							}
						}
						createTab(pai, filhos, request.getContextPath(), panel);
						// cria novo iterator para evitar excetpion Concurrent
						it = transacoes.iterator();
					}
				}
				session.setAttribute("menu", panel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			NavigationHandler nh = context.getApplication().getNavigationHandler();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "msg_detail"));
			nh.handleNavigation(context, null, "/error.xhtml");
		}
	}
	
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	private void createTab(TransacaoBean pai, List<TransacaoBean> filhos, String contextPaht, AccordionPanel panel) {
		Tab tab = new Tab();
		tab.setTitle(pai.getNome());
		tab.setRendered(pai.getMostrar().equals("1"));
		tab.getAttributes().put("index", (++index).toString());
		for(TransacaoBean filho : filhos) {
			HtmlOutputLink link = new HtmlOutputLink();
			// currentLink e setado na sessao pelo br.com.bancoamazonia.infrend.web.listeners.RequestToSessionListener
			link.setValue(contextPaht + filho.getPaginaTransacao() + "?currentLink=" + filho.getNome().replaceAll(" ", "+"));
			link.setStyle("margin-bottom: 5px;");
			link.setRendered(filho.getMostrar().equals("1"));
			link.setStyleClass("menu-item");
			
			HtmlOutputText text = new HtmlOutputText();
			text.setValue(filho.getNome());
			
			link.getChildren().add(text);
			
			tab.getChildren().add(link);
		}
		
		panel.getChildren().add(tab);
	}

}
