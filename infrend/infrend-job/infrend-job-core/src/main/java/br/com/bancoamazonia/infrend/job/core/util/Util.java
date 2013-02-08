package br.com.bancoamazonia.infrend.job.core.util;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;

public class Util
{
	public static boolean isLostConnectionException(Throwable e)
	{
		return 	e instanceof JDBCConnectionException ||
				e instanceof TransactionException ||
				e instanceof TransactionSystemException ||
				e instanceof CannotCreateTransactionException;
	}
	
	public static boolean isDataBaseException(Throwable e)
	{
		return e instanceof IncorrectResultSizeDataAccessException || 
				e instanceof DataAccessException;
	}
	
	public static boolean isRegisterException(Throwable e)
	{
		return	e instanceof StringIndexOutOfBoundsException;
	}
	
	public static boolean isConfigurationException(Throwable e)
	{
		return	e instanceof NumberFormatException ||
				e instanceof NullPointerException;
	}
}
