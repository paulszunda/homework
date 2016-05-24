package io.fourfinanceit.exception;

public class LoanException extends Exception {
	private static final long serialVersionUID = 1L;

	public LoanException() {
		super();
	}

	public LoanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoanException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoanException(String message) {
		super(message);
	}

	public LoanException(Throwable cause) {
		super(cause);
	}

	
}
