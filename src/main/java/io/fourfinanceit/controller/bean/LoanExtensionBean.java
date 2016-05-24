package io.fourfinanceit.controller.bean;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;


public class LoanExtensionBean {
	@NotNull
	private LocalDate date;
	
	public LocalDate getDate() {
		return date;
	}
}
