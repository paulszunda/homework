package io.fourfinanceit.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.fourfinanceit.domain.Loan;

@Transactional
public interface LoanRepository extends JpaRepository<Loan, Long> {

	@Query("from Loan l where l.loanDate between :from and :to and l.ip=:ip")
	public List<Loan> findLoansByIpAndBetweenDates(@Param("ip") String ip, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
