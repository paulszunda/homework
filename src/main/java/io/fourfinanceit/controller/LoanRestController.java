package io.fourfinanceit.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.fourfinanceit.controller.bean.LoanExtensionBean;
import io.fourfinanceit.controller.bean.LoanRequestBean;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanExtension;
import io.fourfinanceit.exception.LoanException;
import io.fourfinanceit.exception.LoanNotFoundException;
import io.fourfinanceit.service.LoanService;

@RestController
@RequestMapping("/rest/")
public class LoanRestController {

	@Autowired
	private LoanService loanService;

	@RequestMapping("/loans")
	private List<Loan> listUserLoans() {
		return loanService.getUserLoans();
	}

	@RequestMapping("/loans/apply")
	private Loan applyForLoan(@Valid @RequestBody LoanRequestBean loanRequestBean, HttpServletRequest request) throws LoanException {
		String ip = request.getRemoteAddr();
		LocalDateTime loanDate = LocalDateTime.now();
		Loan loan = loanService.validateCreateLoan(loanRequestBean.getAmount(), loanRequestBean.getDays(), ip, loanDate);
		return loan;
	}

	@RequestMapping("/loan/{id}/extend")
	private LoanExtension extendLoan(@Valid @PathVariable("id") Long id, @Valid @RequestBody LoanExtensionBean loanExtensionBean, BindingResult results) throws LoanException {
		Loan loan = loanService.findLoan(id);
		if (loan == null)
			throw new LoanNotFoundException("Loan with id " + id + " not found");
		LoanExtension loanExtension = loanService.extendLoan(loan, loanExtensionBean.getDate());
		return loanExtension;
	}
	
	

}
