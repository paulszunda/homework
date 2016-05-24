package io.fourfinanceit.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import io.fourfinanceit.HomeworkApplication;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.Settings;
import io.fourfinanceit.repository.LoanRepository;
import io.fourfinanceit.repository.SettingsRepository;

import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HomeworkApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class HomeworkApplicationTests {
	@Value("${local.server.port}")
	int port;

	@Autowired
	private SettingsRepository settingsRepository;
	@Autowired
	private LoanRepository loanRepository;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

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
	public void getEmptyLoanList() throws Exception {
		get("/rest/loans").then().statusCode(200).contentType(ContentType.JSON).body("size()", is(0));
	}

	@Test
	public void requestingNewLoan() throws Exception {
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("amount", 200);
		request.put("days", 30);

		Loan loan = given().body(request).contentType(ContentType.JSON).post("/rest/loans/apply").as(Loan.class);
		Loan dbLoan = loanRepository.findOne(loan.getId());

		assertThat(loan).isEqualTo(dbLoan);

		loanRepository.deleteAll();
	}

	@Test
	public void getMultipleLoans() throws Exception {
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("amount", 200);
		request.put("days", 30);

		given().body(request).contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(200);
		given().body(request).contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(200);
		given().body(request).contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(200);

		get("/rest/loans").then().statusCode(200).contentType(ContentType.JSON).body("size()", is(3));

		loanRepository.deleteAll();
	}

	@Test
	public void extendLoan() {
		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("amount", 200);
		loanRequest.put("days", 30);

		Loan loan = given().body(loanRequest).contentType(ContentType.JSON).post("/rest/loans/apply").as(Loan.class);

		LocalDate extendedDate = loan.getFinalPaybackDate().plusDays(20);
		Map<String, Object> extensionRequest = new HashMap<String, Object>();
		extensionRequest.put("date", extendedDate);

		given().body(extensionRequest).contentType(ContentType.JSON).post("/rest/loan/" + loan.getId() + "/extend").then().contentType(ContentType.JSON).statusCode(200).body("id", is(1))
				.body("extensionFee", greaterThan(0f)).body("extendedDate", is(DateTimeFormatter.ISO_DATE.format(extendedDate)));

		loanRepository.deleteAll();
	}

	@Test
	public void extendLoanThreeTimes() {
		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("amount", 200);
		loanRequest.put("days", 30);

		Loan loan = given().body(loanRequest).contentType(ContentType.JSON).post("/rest/loans/apply").as(Loan.class);

		LocalDate extendedDate1 = loan.getFinalPaybackDate().plusDays(10);
		LocalDate extendedDate2 = loan.getFinalPaybackDate().plusDays(15);
		LocalDate extendedDate3 = loan.getFinalPaybackDate().plusDays(20);

		Map<String, Object> extensionRequest1 = new HashMap<String, Object>();
		extensionRequest1.put("date", extendedDate1);
		Map<String, Object> extensionRequest2 = new HashMap<String, Object>();
		extensionRequest2.put("date", extendedDate2);
		Map<String, Object> extensionRequest3 = new HashMap<String, Object>();
		extensionRequest3.put("date", extendedDate3);

		given().body(extensionRequest1).contentType(ContentType.JSON).post("/rest/loan/" + loan.getId() + "/extend").then().contentType(ContentType.JSON).statusCode(200);
		given().body(extensionRequest2).contentType(ContentType.JSON).post("/rest/loan/" + loan.getId() + "/extend").then().contentType(ContentType.JSON).statusCode(200);
		given().body(extensionRequest3).contentType(ContentType.JSON).post("/rest/loan/" + loan.getId() + "/extend").then().contentType(ContentType.JSON).statusCode(200);

		get("/rest/loans").then().statusCode(200).contentType(ContentType.JSON).body("size()", is(1)).body("[0].loanExtensions.size()", is(3));

		loanRepository.deleteAll();
	}

	@Test
	public void loanRequestWithBadAmounts() {
		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("amount", "sss");
		loanRequest.put("days", 30);

		given().body(loanRequest).contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(400).body("message", containsString("not a valid"));
	}

	@Test
	public void loanRequestWithBadDays() {
		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("amount", 200);
		loanRequest.put("days", "sss");

		given().body(loanRequest).contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(400).body("message", containsString("not a valid"));
	}

	@Test
	public void extendNonExistingLoan() {

		Map<String, Object> extensionRequest = new HashMap<String, Object>();
		extensionRequest.put("date", LocalDate.now());

		given().body(extensionRequest).contentType(ContentType.JSON).post("/rest/loan/2/extend").then().contentType(ContentType.JSON).statusCode(404);
	}

	@Test
	public void extendLoanWithDateTooSmall() {

		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("amount", 200);
		loanRequest.put("days", 30);

		Loan loan = given().body(loanRequest).contentType(ContentType.JSON).post("/rest/loans/apply").as(Loan.class);

		LocalDate extendedDate = loan.getFinalPaybackDate().minusDays(10);
		Map<String, Object> extensionRequest = new HashMap<String, Object>();
		extensionRequest.put("date", extendedDate);

		given().body(extensionRequest).contentType(ContentType.JSON).post("/rest/loan/" + loan.getId() + "/extend").then().contentType(ContentType.JSON).statusCode(400)
				.body("message", containsString("extended date must be greater"));

		loanRepository.deleteAll();
	}

	@Test
	public void extendLoanWithIdAsString() {
		Map<String, Object> extensionRequest = new HashMap<String, Object>();
		extensionRequest.put("date", LocalDate.now());

		given().body(extensionRequest).contentType(ContentType.JSON).post("/rest/loan/x/extend").then().contentType(ContentType.JSON).statusCode(400).body("message",
				containsString("Failed to convert"));
	}

	@Test
	public void requestLoanWithEmptyBody() {
		given().contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(400).body("message", containsString("body is missing"));
	}

	@Test
	public void requestLoanAsXML() {
		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("amount", 200);
		loanRequest.put("days", 30);

		given().body(loanRequest).contentType(ContentType.XML).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(415);
	}

	@Test
	public void requestLoanWithoutAmount() {
		Map<String, Object> loanRequest = new HashMap<String, Object>();
		loanRequest.put("days", 30);

		given().body(loanRequest).contentType(ContentType.JSON).post("/rest/loans/apply").then().contentType(ContentType.JSON).statusCode(400).body("message", containsString("Validation failed"));
	}
	
	@Test
	public void accessBadURL() {
		get("/invalid/url").then().contentType(ContentType.JSON).statusCode(404);
	}

}
