package io.fourfinanceit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanExtension;
import io.fourfinanceit.exception.LoanException;
import io.fourfinanceit.util.ValidationResult;

public interface LoanService {

	public List<Loan> getUserLoans();
	public Loan findLoan(Long id);
	public Loan createLoan( BigDecimal amount, Integer days, String ip, LocalDateTime loanDate);
	public ValidationResult validateLoan( BigDecimal amount, Integer days, String ip, LocalDateTime loanDate);
	public Loan validateCreateLoan(BigDecimal amount, Integer days, String ip, LocalDateTime loanDate) throws LoanException;
	public LoanExtension extendLoan(Loan loan, LocalDate date) throws LoanException;
}
