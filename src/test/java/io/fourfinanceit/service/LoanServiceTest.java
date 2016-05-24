package io.fourfinanceit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.fourfinanceit.HomeworkApplication;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanExtension;
import io.fourfinanceit.domain.Settings;
import io.fourfinanceit.exception.LoanException;
import io.fourfinanceit.repository.LoanRepository;
import io.fourfinanceit.repository.SettingsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HomeworkApplication.class)
public class LoanServiceTest {

	private final BigDecimal DEFAULT_AMOUNT = new BigDecimal(300);
	private final int DEFAULT_DAYS = 30;
	private final String DEFAULT_IP = "192.168.0.101";
	private final LocalDateTime DEFAULT_LOANDATE = LocalDateTime.of(2016, 01, 01, 12, 00);

	@Autowired
	private LoanService loanService;
	@Autowired
	private SettingsRepository settingsRepository;
	@Autowired
	private LoanRepository loanRepository;

	@Before
	public void setDefaultSettings() {
		Settings settings = settingsRepository.findDefaultSettings();
		settings.setDefaultSettings(true);
		settings.setInitialTermMin(10);
		settings.setInitialTermMax(30);
		settings.setInterestFactor(new BigDecimal("1.5"));
		settings.setMaxApplications(3);
		settings.setMinAmount(new BigDecimal(50));
		settings.setMaxAmount(new BigDecimal(300));
		settings.setRiskHoursStart(LocalTime.of(00, 00));
		settings.setRiskHoursEnd(LocalTime.of(06, 00));
		settings.setReturnBaseFactor(new BigDecimal("0.1"));
		settings.setReturnPerDayFactor(new BigDecimal("0.001"));
		settingsRepository.save(settings);
	}

	@Test
	public void validateCreateLoan() throws LoanException {
		Loan loan = loanService.validateCreateLoan(DEFAULT_AMOUNT, DEFAULT_DAYS, DEFAULT_IP, DEFAULT_LOANDATE);

		assertThat(loan).isNotNull();
		assertThat(loan.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
		assertThat(loan.getFinalPaybackDate()).isEqualTo(LocalDate.of(2016, 01, 31));
		assertThat(loan.getOriginalPaybackDate()).isEqualTo(LocalDate.of(2016, 01, 31));
		assertThat(loan.getIp()).isEqualTo(DEFAULT_IP);
		assertThat(loan.getLoanExtensions()).hasSize(0);
		assertThat(loan.getReturnTotal()).isEqualByComparingTo(new BigDecimal("339"));

		loanRepository.delete(loan);
	}

	@Test
	public void extendLoanOnce() throws LoanException {

		LocalDate extendedDate = LocalDate.of(2016, 02, 25);

		Loan loan = loanService.validateCreateLoan(DEFAULT_AMOUNT, DEFAULT_DAYS, DEFAULT_IP, DEFAULT_LOANDATE);

		LoanExtension loanExtension = loanService.extendLoan(loan, extendedDate);

		assertThat(loanExtension).isNotNull();
		assertThat(loanExtension.getLoan()).isEqualTo(loan);
		assertThat(loan.getLoanExtensions()).containsExactly(loanExtension);
		assertThat(loanExtension.getExtendedDate()).isEqualTo(extendedDate);
		assertThat(loan.getFinalPaybackDate()).isEqualTo(extendedDate);
		assertThat(loan.getOriginalPaybackDate()).isEqualTo(LocalDate.of(2016, 01, 31));
		assertThat(loanExtension.getExtensionFee()).isEqualByComparingTo(new BigDecimal("21.0375"));
		assertThat(loan.getReturnTotal()).isEqualByComparingTo(new BigDecimal("360.0375"));

		loanRepository.delete(loan);
	}

	@Test
	public void extendLoanThreeTimes() throws LoanException {
		LocalDate extendedDate1 = LocalDate.of(2016, 02, 10);
		LocalDate extendedDate2 = LocalDate.of(2016, 02, 19);
		LocalDate extendedDate3 = LocalDate.of(2016, 02, 25);

		Loan loan = loanService.validateCreateLoan(DEFAULT_AMOUNT, DEFAULT_DAYS, DEFAULT_IP, DEFAULT_LOANDATE);

		LoanExtension loanExtension1 = loanService.extendLoan(loan, extendedDate1);
		LoanExtension loanExtension2 = loanService.extendLoan(loan, extendedDate2);
		LoanExtension loanExtension3 = loanService.extendLoan(loan, extendedDate3);

		assertThat(loan.getLoanExtensions()).containsExactly(loanExtension1, loanExtension2, loanExtension3);
		assertThat(loan.getReturnTotal()).isEqualByComparingTo(new BigDecimal("360.0375"));

		loanRepository.delete(loan);
	}

	@Test
	public void createLoanWithBadTermShouldReturnValidationError() {
		int daysTooLow = 5;
		int daysTooHigh = 40;

		LocalDateTime loanDate = LocalDateTime.of(2016, 01, 01, 12, 00);

		assertThatThrownBy(() -> {
			loanService.validateCreateLoan(DEFAULT_AMOUNT, daysTooLow, DEFAULT_IP, loanDate);
		}).isInstanceOf(LoanException.class).hasMessageContaining("Loan term must be in range");

		assertThatThrownBy(() -> {
			loanService.validateCreateLoan(DEFAULT_AMOUNT, daysTooHigh, DEFAULT_IP, loanDate);
		}).isInstanceOf(LoanException.class).hasMessageContaining("Loan term must be in range");

	}

	@Test
	public void createLoanWithBadAmountShouldReturnValidationError() {
		BigDecimal amountTooLow = new BigDecimal(10);
		BigDecimal amountTooHigh = new BigDecimal(400);

		LocalDateTime loanDate = LocalDateTime.of(2016, 01, 01, 12, 00);

		assertThatThrownBy(() -> {
			loanService.validateCreateLoan(amountTooLow, DEFAULT_DAYS, DEFAULT_IP, loanDate);
		}).isInstanceOf(LoanException.class).hasMessageContaining("Loan amount must be in range");

		assertThatThrownBy(() -> {
			loanService.validateCreateLoan(amountTooHigh, DEFAULT_DAYS, DEFAULT_IP, loanDate);
		}).isInstanceOf(LoanException.class).hasMessageContaining("Loan amount must be in range");
	}

	/*
	 * Out of 20 loan request should deny at least 1 loan. Still 1% chance to
	 * approve all 20 loans.
	 */
	@Test
	public void createLoanFromSameIPShouldReturnRiskError() throws LoanException {
		assertThatThrownBy(() -> {
			for (int i = 0; i < 20; i++) {
				loanService.validateCreateLoan(DEFAULT_AMOUNT, DEFAULT_DAYS, DEFAULT_IP, DEFAULT_LOANDATE);
			}
		}).isInstanceOf(LoanException.class).hasMessageContaining("Max loans from same ip exceeded");
		loanRepository.deleteAll();
	}

	/*
	 * Out of 20 loan request should deny at least 1 loan.
	 */
	@Test
	public void createLoanInRiskHoursShouldReturnRiskError() {

		assertThatThrownBy(() -> {
			String ip = "192.168.0.10";
			for (int i = 0; i < 20; i++) {
				ip = ip + i;
				loanService.validateCreateLoan(DEFAULT_AMOUNT, DEFAULT_DAYS, ip, LocalDateTime.of(2016, 01, 01, 02, 00));
			}
		}).isInstanceOf(LoanException.class).hasMessageContaining("Loan taken during risk hours.");

		loanRepository.deleteAll();
	}

}
