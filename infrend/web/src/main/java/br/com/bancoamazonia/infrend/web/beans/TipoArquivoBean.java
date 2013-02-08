package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;

import br.com.bancoamazonia.infrend.modelo.TipoArquivo;
import br.com.bancoamazonia.infrend.service.TipoArquivoService;

@ManagedBean
public class TipoArquivoBean implements Serializable {
	private static final long serialVersionUID = -2978046274858114195L;
	private TipoArquivoService tipoArquivoService;
	public void setTipoArquivoService(TipoArquivoService tipoArquivoService) {
		this.tipoArquivoService = tipoArquivoService;
	}
	public List<TipoArquivo> getList() {
		return tipoArquivoService.list();
	}
}
