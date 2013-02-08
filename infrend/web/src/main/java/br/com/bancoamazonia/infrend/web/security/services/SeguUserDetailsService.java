/*package br.com.bancoamazonia.infrend.web.security.services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.bancoamazonia.infrend.web.security.model.SeguGrantedAuthority;
import br.com.bancoamazonia.infrend.web.security.model.SeguUserDetails;
import br.com.bancoamazonia.infrend.web.services.SeguClientService;
import br.com.pdcase.pdsegu.ws.GrupoBean;

public class SeguUserDetailsService implements UserDetailsService
{
	private SeguClientService seguClientService;
	public void setSeguClientService(SeguClientService seguClientService)
	{
		this.seguClientService = seguClientService;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		SeguUserDetails user = null;
		try {
			HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			String authKey = (String)session.getAttribute("authkey");
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			
			for(GrupoBean g : seguClientService.getUserRolesByAuthKey(authKey))
				authorities.add(new SeguGrantedAuthority("ROLE_"+g.getNome()));
			
			authorities.add(new SeguGrantedAuthority("IS_AUTHENTICATED_FULLY"));
			
			user = new SeguUserDetails();
			user.setAuthorities(authorities);
			user.setMatricula(username);
			user.setPassword(new Md5PasswordEncoder().encodePassword((String)request.getParameter("j_password"), null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return user;
	}
}*/