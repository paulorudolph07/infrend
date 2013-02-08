package br.com.bancoamazonia.infrend.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "CNPJValidator")
public class CNPJValidator implements Validator {

	public void validate(FacesContext arg0, UIComponent arg1, Object value) throws ValidatorException {

		String CNPJ = (String) value;
		boolean validado;

		CNPJ = CNPJ.replace(".", "");
		CNPJ = CNPJ.replace("/", "");
		CNPJ = CNPJ.replace("-", "");

		if (CNPJ.length() != 14)
			validado = false;

		char dig13, dig14;
		int sm, i, r, num, peso;

		sm = 0;
		peso = 2;
		for (i = 11; i >= 0; i--) {

			num = (int) (CNPJ.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso + 1;
			if (peso == 10)
				peso = 2;
		}

		r = sm % 11;
		if ((r == 0) || (r == 1))
			dig13 = '0';
		else
			dig13 = (char) ((11 - r) + 48);

		sm = 0;
		peso = 2;
		for (i = 12; i >= 0; i--) {
			num = (int) (CNPJ.charAt(i) - 48);
			sm = sm + (num * peso);
			peso = peso + 1;
			if (peso == 10)
				peso = 2;
		}

		r = sm % 11;
		if ((r == 0) || (r == 1))
			dig14 = '0';
		else
			dig14 = (char) ((11 - r) + 48);

		if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
			validado = true;
		else
			validado = false;

		if (!validado) {

			FacesMessage msg = new FacesMessage();
			msg.setDetail("CNPJ INVALIDO");
			msg.setSummary("CNPJ INVALIDO");
			msg.setSeverity(FacesMessage.SEVERITY_WARN);

			throw new ValidatorException(msg);
		}
	}
}
