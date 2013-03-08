package br.com.bancoamazonia.infrend.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.bancoamazonia.infrend.modelo.Cliente;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaFisica;
import br.com.bancoamazonia.infrend.modelo.OcorrenciaPessoaJuridica;
import br.com.bancoamazonia.infrend.service.ClienteService;
import br.com.bancoamazonia.infrend.service.OcorrenciaPfService;
import br.com.bancoamazonia.infrend.service.OcorrenciaPjService;
import br.com.bancoamazonia.infrend.web.services.ReportService;
import br.com.bancoamazonia.infrend.web.util.Util;

@ManagedBean
public class ReportBean implements Serializable {
	private Logger log = Logger.getLogger(getClass());
	
	private static final long serialVersionUID = -1302453001656185278L;
	private ClienteService clienteService;
	private OcorrenciaPfService ocorrenciaPfService;
	private OcorrenciaPjService ocorrenciaPjService;
	private ReportService reportService; 
	
	private Cliente clientePf;
	private Cliente clientePj;
	private String tipoCliente = "pf";
	private String cpf;
	private String cnpj;
	private int ano;
	private int trimestre;
	private StreamedContent report;
	private List<OcorrenciaPessoaFisica> ocorrenciaPfList;
	private List<OcorrenciaPessoaJuridica> ocorrenciaPjList;
	private List<OcorrenciaPessoaJuridica> contaCorrenteList;
	private List<String> meses = Arrays.asList("NONE", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto",
			"Setembro", "Outubro", "Novembro", "Dezembro");
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setOcorrenciaPfService(OcorrenciaPfService ocorrenciaPfService) {
		this.ocorrenciaPfService = ocorrenciaPfService;
	}
	public void setOcorrenciaPjService(OcorrenciaPjService ocorrenciaPjService)	{
		this.ocorrenciaPjService = ocorrenciaPjService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public Cliente getClientePf() {
		return clientePf;
	}
	public void setClientePf(Cliente clientePf) {
		this.clientePf = clientePf;
	}
	public Cliente getClientePj() {
		return clientePj;
	}
	public void setClientePj(Cliente clientePj) {
		this.clientePj = clientePj;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getTrimestre() {
		return trimestre;
	}
	public void setTrimestre(int trimestre) {
		this.trimestre = trimestre;
	}
	public StreamedContent getReport() {
		return report;
	}
	public void setReport(StreamedContent report) {
		this.report = report;
	}
	public void setOcorrenciaList() {
		if(tipoCliente.equalsIgnoreCase("pf") && (clientePf = (clienteService.findByCodigo(Util.rawCode(cpf)))) != null) 
			ocorrenciaPfList = ocorrenciaPfService.listByCodigoAndAno(clientePf.getCodigo(), ano);
		else if (tipoCliente.equalsIgnoreCase("pj") && (clientePj = (clienteService.findByCodigo(Util.rawCode(cnpj)))) != null) {
			ocorrenciaPjList = ocorrenciaPjService.listByCodigoAndAnoAndOperacao(clientePj.getCodigo(), ano,
					new BigInteger[]{new BigInteger("2"), new BigInteger("3"), new BigInteger("5")});
			contaCorrenteList = ocorrenciaPjService.listByCodigoAndAnoAndOperacao(clientePj.getCodigo(), ano,
					new BigInteger[]{new BigInteger("1")});
		}
		else
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN, "A consulta nao retornou resultado!", "msg_detail"));
	}
	public List<OcorrenciaPessoaFisica> getOcorrenciaPfList() {
		return ocorrenciaPfList;
	}
	public List<OcorrenciaPessoaJuridica> getOcorrenciaPjList() {
		return ocorrenciaPjList;
	}
	public List<OcorrenciaPessoaJuridica> getContaCorrenteList() {
		return contaCorrenteList;
	}
	public String getMonthName(Integer index) {
		return meses.get(index);
	}
	
	public void loadReport(ActionEvent event) throws AbortProcessingException {
		try	{
			if(tipoCliente.equalsIgnoreCase("pf")) {
				clientePf = clienteService.findByCodigo(Util.rawCode(cpf));
				loadBy(clientePf);
			}
			else {
				clientePj = clienteService.findByCodigo(Util.rawCode(cnpj));
				loadBy(clientePj);
			}
			
		}
		catch(Exception e) {
			log.error(e);
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), "msg_detail"));
			throw new AbortProcessingException();
		}
	}
	
	private void loadBy(Cliente cliente) {
		if(cliente != null) {
			Map<String, Object> params = clienteService.toMap(cliente);
			params.put("codigo", cliente.getCodigo());
			params.put("ano", ano);
			params.put("trimestre", trimestre);
			params.put("tipoCliente", tipoCliente);
			if(tipoCliente.toLowerCase().equals("pf"))
				params.putAll(ocorrenciaPfService.toMap(cliente, ano));
			else if(tipoCliente.toLowerCase().equals("pj"))
				params.putAll(ocorrenciaPjService.toMap(cliente, ano, trimestre));
			InputStream conteudoRel = reportService.create(params);
			report = new DefaultStreamedContent(conteudoRel, "application/pdf;charset=ISO-8859-1", "ir"
					+Util.rawCode(cliente.getCodigo())
					+".pdf");
		}
		else throw new RuntimeException("A consulta nao retornou resultado!");
	}
	
}