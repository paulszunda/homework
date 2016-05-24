package io.fourfinanceit.util;

public class ValidationResult {
	private String message = "";
	private boolean valid;
	
	public static ValidationResult invalid(String message) {
		return new ValidationResult(message, false);
	}
	
	public static ValidationResult valid() {
		return new ValidationResult(null, true);
	}
	
	private ValidationResult(String message, boolean valid) {
		this.valid = valid;
		if (message != null)
			this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public boolean isValid() {
		return valid;
	}
}
