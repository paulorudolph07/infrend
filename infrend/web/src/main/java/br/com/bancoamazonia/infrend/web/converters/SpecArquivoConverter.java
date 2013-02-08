package br.com.bancoamazonia.infrend.web.converters;

import java.math.BigInteger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import br.com.bancoamazonia.infrend.modelo.SpecArquivo;
import br.com.bancoamazonia.infrend.service.SpecArquivoService;

@FacesConverter(value="specArquivoConverter", forClass=SpecArquivo.class)
public class SpecArquivoConverter implements Converter
{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)
	{
		if(value != null)
			return FacesContextUtils.getWebApplicationContext(context).getBean(SpecArquivoService.class).get(new BigInteger(value));
		return null;
	}
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if(value != null && value instanceof SpecArquivo)
			return ((SpecArquivo)value).getId().toString();
		return null;
	}
}
