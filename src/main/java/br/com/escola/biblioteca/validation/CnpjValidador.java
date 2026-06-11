package br.com.escola.biblioteca.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidador implements ConstraintValidator<CnpjValido, String> {

	@Override
	public boolean isValid(String cnpj, ConstraintValidatorContext context) {
		if (cnpj == null || cnpj.isBlank())
			return false;

		String apenasDigitos = cnpj.replaceAll("[^0-9]", "");
		if (apenasDigitos.length() != 14)
			return false;

		if (apenasDigitos.matches("(\\d)\\1{13}"))
			return false;

		int[] pesos1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		int[] pesos2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

		int soma1 = 0;
		for (int i = 0; i < 12; i++) {
			soma1 += Character.getNumericValue(apenasDigitos.charAt(i)) * pesos1[i];
		}
		int dig1 = soma1 % 11 < 2 ? 0 : 11 - (soma1 % 11);

		int soma2 = 0;
		for (int i = 0; i < 13; i++) {
			soma2 += Character.getNumericValue(apenasDigitos.charAt(i)) * pesos2[i];
		}
		int dig2 = soma2 % 11 < 2 ? 0 : 11 - (soma2 % 11);

		return Character.getNumericValue(apenasDigitos.charAt(12)) == dig1
				&& Character.getNumericValue(apenasDigitos.charAt(13)) == dig2;
	}
}
