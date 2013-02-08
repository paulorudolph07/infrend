package br.com.bancoamazonia.infrend.web.util;

import java.math.BigInteger;

public class Util
{
	public static String rawCode(String codigo)
	{
		return String.format("%015d", new BigInteger(codigo.replaceAll("\\-|\\.|/", "")));
	}
	
}
