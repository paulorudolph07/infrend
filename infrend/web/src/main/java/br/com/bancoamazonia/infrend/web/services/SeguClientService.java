package br.com.bancoamazonia.infrend.web.services;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.pdcase.pdsegu.ws.AutenticaServiceWrapper;
import br.com.pdcase.pdsegu.ws.GrupoBean;
import br.com.pdcase.pdsegu.ws.Parametro;
import br.com.pdcase.pdsegu.ws.SeguServiceProxy;
import br.com.pdcase.pdsegu.ws.TransacaoBean;

public class SeguClientService {
	private SeguServiceProxy seguServiceProxy;
	private String sigla;
	private String timeout;
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public SeguServiceProxy getSeguServiceProxy() {
		return seguServiceProxy;
	}
	public void setSeguServiceProxy(SeguServiceProxy seguServiceProxy) {
		this.seguServiceProxy = seguServiceProxy;
	}
	
	public void login(HttpServletResponse response) throws IOException {
		Parametro parametro = seguServiceProxy.recuperarParametro("URL_LOGIN_PAGE");
		String url = parametro.getValor();
		response.sendRedirect(url+"?sigla="+sigla);
	}
	
	public void logout(HttpServletResponse response, String authkey) throws IOException {
		Parametro parametro = seguServiceProxy.recuperarParametro("URL_LOGIN_PAGE");
		String url = parametro.getValor();
		response.sendRedirect(url);
	}
	
	public void logout(HttpServletResponse response) throws IOException {
		Parametro parametro = seguServiceProxy.recuperarParametro("URL_LOGIN_PAGE");
		String url = parametro.getValor();
		response.sendRedirect(url);
	}
	
	public void setSessionTimeout(HttpSession session) throws RemoteException {
		if(timeout == null || timeout.equals("")) {
			Parametro parametro = seguServiceProxy.recuperarParametro("TEMPO_EXP_SESSAO");
			timeout = parametro.getValor();
		}
		session.setMaxInactiveInterval(Integer.parseInt(timeout));
	}
	
	public AutenticaServiceWrapper validateUserByAuthKey(String authKey) throws RemoteException {
		return seguServiceProxy.validarUsuarioByAuthKey(authKey);
	}
	
	public List<TransacaoBean> getTransactionsByAuthKey(String authKey) throws RemoteException {
		return Arrays.asList(seguServiceProxy.obterTransacoesByAuthKey(authKey, sigla).getTransacoes());
	}
	
	public String getTransactionsAsTextByAuthKey(String authKey) throws RemoteException {
		return seguServiceProxy.obterTransacoesAsTextByAuthKey(authKey, sigla);
	}
	
	public GrupoBean[] getUserRolesByAuthKey(String authKey) throws RemoteException {
		return seguServiceProxy.obterGruposUsuarioByAuthKey(authKey).getGrupos();
	}
	
	public String getUserRolesAsTextByAuthKey(String authKey) throws RemoteException {
		String roles = "";
		for(GrupoBean g : seguServiceProxy.obterGruposUsuarioByAuthKey(authKey).getGrupos())
			roles += g.getNome() + " ";
		
		return roles;
	}
	public AutenticaServiceWrapper validateUser(String username, String password) throws RemoteException {
		return seguServiceProxy.validarUsuario(username, password);
	}
	
	public boolean hasSystemAccess(String authkey) throws RemoteException {
		return seguServiceProxy.obterSistemasAsTextByAuthKey(authkey).contains(sigla);
	}
}
