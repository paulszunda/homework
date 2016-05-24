package io.fourfinanceit.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.fourfinanceit.exception.LoanException;
import io.fourfinanceit.exception.LoanNotFoundException;

@ControllerAdvice
public class LoanRestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> loanNotFoundError(HttpServletRequest req, LoanNotFoundException e) {
		return handleError(HttpStatus.NOT_FOUND, e);

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> loanError(HttpServletRequest req, LoanException e) {
		return handleError(HttpStatus.BAD_REQUEST, e);

	}
	
	private ResponseEntity<ErrorResponse> handleError(HttpStatus status, Exception e) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(status.getReasonPhrase(), status.value(), e.getMessage()), status);
	}
	
}
