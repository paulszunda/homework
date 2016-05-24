package io.fourfinanceit.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Loan {

	@Id
	@GeneratedValue
	private Long id;
	private BigDecimal amount;

	private LocalDateTime loanDate;
	private LocalDate originalPaybackDate;
	private LocalDate finalPaybackDate;
	@OneToMany(mappedBy = "loan", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<LoanExtension> loanExtensions = new ArrayList<LoanExtension>();
	private BigDecimal returnTotal;
	private String ip;

	public Long getId() {
		return id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDateTime loanDate) {
		this.loanDate = loanDate;
	}

	public LocalDate getOriginalPaybackDate() {
		return originalPaybackDate;
	}

	public void setOriginalPaybackDate(LocalDate originalPaybackDate) {
		this.originalPaybackDate = originalPaybackDate;
	}

	public LocalDate getFinalPaybackDate() {
		return finalPaybackDate;
	}

	public void setFinalPaybackDate(LocalDate finalPaybackDate) {
		this.finalPaybackDate = finalPaybackDate;
	}

	public List<LoanExtension> getLoanExtensions() {
		return loanExtensions;
	}

	public BigDecimal getReturnTotal() {
		return returnTotal;
	}

	public void setReturnTotal(BigDecimal returnTotal) {
		this.returnTotal = returnTotal;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((finalPaybackDate == null) ? 0 : finalPaybackDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((loanDate == null) ? 0 : loanDate.hashCode());
		result = prime * result + ((loanExtensions == null) ? 0 : loanExtensions.hashCode());
		result = prime * result + ((originalPaybackDate == null) ? 0 : originalPaybackDate.hashCode());
		result = prime * result + ((returnTotal == null) ? 0 : returnTotal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Loan other = (Loan) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (amount.compareTo(other.amount) != 0)
			return false;
		if (finalPaybackDate == null) {
			if (other.finalPaybackDate != null)
				return false;
		} else if (!finalPaybackDate.equals(other.finalPaybackDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (loanDate == null) {
			if (other.loanDate != null)
				return false;
		} else if (!loanDate.equals(other.loanDate))
			return false;
		if (loanExtensions == null) {
			if (other.loanExtensions != null)
				return false;
		} else if (!loanExtensions.equals(other.loanExtensions))
			return false;
		if (originalPaybackDate == null) {
			if (other.originalPaybackDate != null)
				return false;
		} else if (!originalPaybackDate.equals(other.originalPaybackDate))
			return false;
		if (returnTotal == null) {
			if (other.returnTotal != null)
				return false;
		} else if (returnTotal.compareTo(other.returnTotal) != 0)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Loan [id=" + id + ", amount=" + amount + ", loanDate=" + loanDate + ", originalPaybackDate=" + originalPaybackDate + ", finalPaybackDate=" + finalPaybackDate + ", loanExtensions="
				+ loanExtensions + ", returnTotal=" + returnTotal + ", ip=" + ip + "]";
	}

}
