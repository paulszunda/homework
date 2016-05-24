package io.fourfinanceit.controller;

public class ErrorResponse {
	private String error;
	private int status;
	private String message;

	public ErrorResponse(String error, int status, String message) {
		super();
		this.error = error;
		this.status = status;
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
