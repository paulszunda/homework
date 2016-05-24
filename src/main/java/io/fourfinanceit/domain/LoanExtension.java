package io.fourfinanceit.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class LoanExtension {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Loan loan;
	private LocalDate extendedDate;
	private BigDecimal extensionFee;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
		if (loan != null)
			loan.getLoanExtensions().add(this);
	}

	public LocalDate getExtendedDate() {
		return extendedDate;
	}

	public void setExtendedDate(LocalDate extendedDate) {
		this.extendedDate = extendedDate;
	}

	public BigDecimal getExtensionFee() {
		return extensionFee;
	}

	public void setExtensionFee(BigDecimal extensionFee) {
		this.extensionFee = extensionFee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extendedDate == null) ? 0 : extendedDate.hashCode());
		result = prime * result + ((extensionFee == null) ? 0 : extensionFee.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((loan == null) ? 0 : loan.hashCode());
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
		LoanExtension other = (LoanExtension) obj;

		if (extendedDate == null) {
			if (other.extendedDate != null)
				return false;
		} else if (!extendedDate.equals(other.extendedDate))
			return false;
		if (extensionFee == null) {
			if (other.extensionFee != null)
				return false;
		} else if (extensionFee.compareTo(other.extensionFee) != 0)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (loan == null) {
			if (other.loan != null)
				return false;
		} else if (!loan.equals(other.loan))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoanExtension [id=" + id + ", loan=" + (loan == null ? "null" : loan.getId()) + ", extendedDate=" + extendedDate + ", extensionFee=" + extensionFee + "]";
	}

}
