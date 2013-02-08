package br.com.bancoamazonia.infrend.web.converters;

import java.math.BigInteger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import br.com.bancoamazonia.infrend.modelo.Propriedade;
import br.com.bancoamazonia.infrend.service.PropriedadeService;

@FacesConverter(value="propriedadeConverter", forClass=Propriedade.class)
public class PropriedadeConverter implements Converter
{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)
	{
		if(value != null)
			return FacesContextUtils.getWebApplicationContext(context).getBean(PropriedadeService.class).get(new BigInteger(value));
		return null;
	}
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if(value != null && value instanceof Propriedade)
			return ((Propriedade)value).getId().toString();
		return null;
	}
	
}
