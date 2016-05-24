package io.fourfinanceit.exception;

public class LoanNotFoundException extends LoanException {
	private static final long serialVersionUID = 1L;

	public LoanNotFoundException() {
		super();
	}

	public LoanNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoanNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoanNotFoundException(String message) {
		super(message);
	}

	public LoanNotFoundException(Throwable cause) {
		super(cause);
	}

	
}
