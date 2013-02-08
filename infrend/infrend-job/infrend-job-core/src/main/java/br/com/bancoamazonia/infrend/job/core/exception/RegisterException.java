package br.com.bancoamazonia.infrend.job.core.exception;

public class RegisterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegisterException() {
		super();
	}
	
	public RegisterException(String message) {
		super(message);
	}
	
	public RegisterException(Throwable e) {
		super(e);
	}
	
	public RegisterException(String message, Throwable e) {
		super(message, e);
	}
}
