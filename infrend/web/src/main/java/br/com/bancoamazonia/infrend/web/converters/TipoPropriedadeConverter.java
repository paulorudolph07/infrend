package br.com.bancoamazonia.infrend.web.converters;

import java.math.BigInteger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.web.jsf.FacesContextUtils;

import br.com.bancoamazonia.infrend.modelo.TipoPropriedade;
import br.com.bancoamazonia.infrend.service.TipoPropriedadeService;

@FacesConverter(value="tipoPropriedadeConverter", forClass=TipoPropriedade.class)
public class TipoPropriedadeConverter implements Converter
{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)
	{
		if(value != null)
			return FacesContextUtils.getWebApplicationContext(context).getBean(TipoPropriedadeService.class).get(new BigInteger(value));
		return null;
	}
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if(value != null && value instanceof TipoPropriedade)
			return ((TipoPropriedade)value).getId().toString();
		return null;
	}
	
}
