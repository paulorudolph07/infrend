package br.com.bancoamazonia.infrend.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "CPFValidator")
public class CPFValidator implements Validator {

	public void validate(FacesContext arg0, UIComponent arg1, Object value) throws ValidatorException {

		String strCpf = (String) value;

		strCpf = strCpf.replace(".", "");
		strCpf = strCpf.replace("-", "");

		int d1, d2;
		int digito1, digito2, resto;
		int digitoCPF;
		String nDigResult;

		d1 = d2 = 0;
		digito1 = digito2 = resto = 0;

		for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
			digitoCPF = Integer.valueOf(strCpf.substring(nCount - 1, nCount))
					.intValue();

			d1 = d1 + (11 - nCount) * digitoCPF;

			d2 = d2 + (12 - nCount) * digitoCPF;
		}

		resto = (d1 % 11);

		if (resto < 2) {
			digito1 = 0;
		} else {
			digito1 = 11 - resto;
		}

		d2 += 2 * digito1;

		resto = (d2 % 11);

		if (resto < 2) {
			digito2 = 0;
		} else {
			digito2 = 11 - resto;
		}

		String nDigVerific = strCpf.substring(strCpf.length() - 2,
				strCpf.length());

		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

		if (!nDigVerific.equals(nDigResult)) {

			FacesMessage msg = new FacesMessage();
			msg.setDetail("CPF INVALIDO");
			msg.setSummary("CPF INVALIDO");
			msg.setSeverity(FacesMessage.SEVERITY_WARN);

			throw new ValidatorException(msg);

		}
	}
}
