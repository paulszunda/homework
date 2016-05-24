package io.fourfinanceit.controller.bean;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class LoanRequestBean {
	@NotNull
	private BigDecimal amount;
	@NotNull
	private Integer days;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public Integer getDays() {
		return days;
	}
	
	
}
