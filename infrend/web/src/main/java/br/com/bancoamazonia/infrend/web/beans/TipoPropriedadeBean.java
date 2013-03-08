package br.com.bancoamazonia.infrend.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;

import br.com.bancoamazonia.infrend.modelo.TipoPropriedade;
import br.com.bancoamazonia.infrend.service.TipoPropriedadeService;

@ManagedBean
public class TipoPropriedadeBean implements Serializable {
	private static final long serialVersionUID = -8029529785313332416L;
	
	private Logger log = Logger.getLogger(getClass());
	
	private TipoPropriedadeService tipoPropriedadeService;
	public void setTipoPropriedadeService(TipoPropriedadeService tipoPropriedadeService) {
		this.tipoPropriedadeService = tipoPropriedadeService;
	}
	public List<TipoPropriedade> getList() {
		try {
			return tipoPropriedadeService.list();
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}
}
