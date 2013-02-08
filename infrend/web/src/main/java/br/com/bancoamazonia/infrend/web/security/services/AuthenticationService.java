/*package br.com.bancoamazonia.infrend.web.security.services;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.bancoamazonia.infrend.web.services.SeguClientService;
import br.com.pdcase.pdsegu.ws.AutenticaServiceWrapper;

public class AuthenticationService
{
	private AuthenticationManager authenticationManager;
	private SeguClientService seguClientService;
	public void setAuthenticationManager(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}
	public void setSeguClientService(SeguClientService seguClientService)
	{
		this.seguClientService = seguClientService;
	}
	public boolean login(String username, String password) throws Exception
	{
		AutenticaServiceWrapper autentica = seguClientService.validateUser(username, password);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)context.getExternalContext().getSession(true);
		
		session.setAttribute("authkey", autentica.getUsuario().getAuthKey());
		session.setAttribute("username", autentica.getUsuario().getNome());
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		
		Authentication authentication = authenticationManager.authenticate(token);
		if(authentication.isAuthenticated())
		{
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return true;
		}
		return false;
	}
	
	public void logout()
	{  
        SecurityContextHolder.getContext().setAuthentication(null);  
        invalidateSession();  
    }
  
    private void invalidateSession()
    {  
        FacesContext fc = FacesContext.getCurrentInstance();  
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);  
        session.invalidate();
    }
}*/