package br.com.bancoamazonia.infrend.job.core.exception;

public class LostConnectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public LostConnectionException() {
		super();
	}
	
	public LostConnectionException(String message) {
		super(message);
	}
	
	public LostConnectionException(Throwable e) {
		super(e);
	}

}
