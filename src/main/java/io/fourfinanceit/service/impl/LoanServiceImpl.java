package io.fourfinanceit.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanExtension;
import io.fourfinanceit.domain.Settings;
import io.fourfinanceit.exception.LoanException;
import io.fourfinanceit.repository.LoanExtensionRepository;
import io.fourfinanceit.repository.LoanRepository;
import io.fourfinanceit.repository.SettingsRepository;
import io.fourfinanceit.service.LoanService;
import io.fourfinanceit.util.ValidationResult;

@Transactional
@Service("loanService")
public class LoanServiceImpl implements LoanService {

	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private SettingsRepository settingsRepository;
	@Autowired
	private LoanExtensionRepository loanExtensionRepository;

	@Override
	public List<Loan> getUserLoans() {
		return loanRepository.findAll();
	}

	@Override
	public Loan findLoan(Long id) {
		return loanRepository.findOne(id);
	}

	@Override
	public Loan createLoan(BigDecimal amount, Integer days, String ip, LocalDateTime loanDate) {
		Loan loan = new Loan();
		loan.setAmount(amount);
		loan.setIp(ip);

		LocalDate paybackDate = loanDate.toLocalDate().plusDays(days);
		loan.setLoanDate(loanDate);
		loan.setOriginalPaybackDate(paybackDate);
		loan.setFinalPaybackDate(paybackDate);

		loan.setReturnTotal(calculateReturnMoney(amount, days));

		loanRepository.save(loan);
		return loan;
	}

	@Override
	public ValidationResult validateLoan(BigDecimal amount, Integer days, String ip, LocalDateTime loanDate) {
		Settings settings = settingsRepository.findDefaultSettings();
		ValidationResult inputValidation = validateLoanInput(amount, settings.getMinAmount(), settings.getMaxAmount(), days, settings.getInitialTermMin(), settings.getInitialTermMax());
		if (!inputValidation.isValid())
			return inputValidation;

		ValidationResult riskHourValidation = validateLoanInRiskHours(amount, settings.getMaxAmount(), loanDate.toLocalTime(), settings.getRiskHoursStart(), settings.getRiskHoursEnd());
		if (!riskHourValidation.isValid())
			return riskHourValidation;

		ValidationResult sameIpValidation = validateLoanFromIp(ip, settings.getMaxApplications(), loanDate);
		if (!sameIpValidation.isValid())
			return sameIpValidation;

		return ValidationResult.valid();
	}

	@Override
	public Loan validateCreateLoan(BigDecimal amount, Integer days, String ip, LocalDateTime loanDate) throws LoanException {
		ValidationResult validationResult = validateLoan(amount, days, ip, loanDate);
		if (!validationResult.isValid())
			throw new LoanException(validationResult.getMessage());
		return createLoan(amount, days, ip, loanDate);
	}

	@Override
	public LoanExtension extendLoan(Loan loan, LocalDate date) throws LoanException {
		if (loan == null)
			throw new LoanException("Cannot extend null loan");
		if (!date.isAfter(loan.getFinalPaybackDate()))
			throw new LoanException("New extended date must be greater than current loan payback date");
		Settings settings = settingsRepository.findDefaultSettings();
		BigDecimal extensionFee = calculateExtensionFee(loan, date, settings.getInterestFactor(), settings.getReturnPerDayFactor());

		LoanExtension extension = new LoanExtension();
		extension.setExtendedDate(date);
		extension.setLoan(loan);
		extension.setExtensionFee(extensionFee);
		loanExtensionRepository.save(extension);

		loan.setFinalPaybackDate(date);
		loan.setReturnTotal(loan.getReturnTotal().add(extensionFee));
		loanRepository.save(loan);
		return extension;
	}

	private BigDecimal calculateExtensionFee(Loan loan, LocalDate newPaybackDate, BigDecimal interestFactorPerWeek, BigDecimal returnPerDayFactor) {
		LocalDate originalPaybackDate = loan.getOriginalPaybackDate();
		LocalDate finalPaybackDate = loan.getFinalPaybackDate();

		BigDecimal returnPerDay = returnPerDayFactor.multiply(loan.getAmount());
		long previousExtendedDays = ChronoUnit.DAYS.between(originalPaybackDate, finalPaybackDate);
		long billableDays = ChronoUnit.DAYS.between(finalPaybackDate, newPaybackDate);

		int currentWeek = (int) ((previousExtendedDays / 7) + 1);
		int currentDay = (int) (previousExtendedDays % 7);

		BigDecimal currentModifier = interestFactorPerWeek.pow(currentWeek);
		double totalReturn = 0;
		while (billableDays > 7 - currentDay) {
			int calcDays = 7 - currentDay;
			double weekReturn = calcDays * currentModifier.doubleValue() * returnPerDay.doubleValue();
			currentModifier = currentModifier.multiply(interestFactorPerWeek);
			currentDay = 0;
			billableDays -= calcDays;
			totalReturn += weekReturn;
		}
		totalReturn += currentModifier.doubleValue() * billableDays * returnPerDay.doubleValue();

		return new BigDecimal(totalReturn).setScale(5, BigDecimal.ROUND_HALF_EVEN);
	}

	private BigDecimal calculateReturnMoney(BigDecimal amount, Integer days) {
		Settings settings = settingsRepository.findDefaultSettings();
		BigDecimal returnBaseFactor = settings.getReturnBaseFactor();
		BigDecimal returnPerDayFactor = settings.getReturnPerDayFactor();

		BigDecimal baseReturn = amount.multiply(returnBaseFactor);
		BigDecimal termReturn = new BigDecimal(days).multiply(returnPerDayFactor).multiply(amount);
		BigDecimal totalReturnMoney = amount.add(baseReturn).add(termReturn);

		return totalReturnMoney;
	}

	private ValidationResult validateLoanInput(BigDecimal amount, BigDecimal minAmount, BigDecimal maxAmount, Integer days, Integer minDays, Integer maxDays) {
		if (amount.compareTo(minAmount) < 0 || amount.compareTo(maxAmount) > 0)
			return ValidationResult.invalid("Loan amount must be in range of " + minAmount + " and " + maxAmount);

		if (days < minDays || days > maxDays)
			return ValidationResult.invalid("Loan term must be in range of " + minDays + " and " + maxDays);

		return ValidationResult.valid();
	}

	private ValidationResult validateLoanInRiskHours(BigDecimal amount, BigDecimal maxAmount, LocalTime loanTime, LocalTime riskHoursMin, LocalTime riskHoursMax) {
		if (amount.compareTo(maxAmount) == 0 && (loanTime.isAfter(riskHoursMin) || loanTime.equals(riskHoursMin)) && loanTime.isBefore(riskHoursMax)) {
			boolean canGrantLoan = grantRiskHourLoan();
			if (canGrantLoan == false)
				return ValidationResult.invalid("Loan taken during risk hours. Loan rejected. Try again after " + riskHoursMax);
		}
		return ValidationResult.valid();
	}

	private ValidationResult validateLoanFromIp(String ip, Integer maxApplications, LocalDateTime date) {
		LocalDateTime dayStart = date.withHour(0).withMinute(0).withSecond(0);
		LocalDateTime dayEnd = date.withHour(23).withMinute(59).withSecond(59);
		List<Loan> loans = loanRepository.findLoansByIpAndBetweenDates(ip, dayStart, dayEnd);
		if (loans.size() >= maxApplications) {
			boolean canGrantLoan = grantIpRiskLoan();
			if (canGrantLoan == false)
				return ValidationResult.invalid("Max loans from same ip exceeded! Try again tomorrow.");
		}
		return ValidationResult.valid();
	}

	// Dummy function for addition Risk evaluation. There is 40% chance to grant
	// a loan using this function.
	private boolean grantRiskHourLoan() {
		return Math.random() <= 0.4;
	}

	// Dummy function for addition Risk evaluation. There is 70% chance to grant
	// a loan using this function.
	private boolean grantIpRiskLoan() {
		return Math.random() <= 0.7;
	}

}
