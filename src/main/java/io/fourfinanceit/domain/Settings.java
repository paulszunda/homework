package io.fourfinanceit.domain;

import java.math.BigDecimal;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Settings {

	@Id
	@GeneratedValue
	private Long id;
	private boolean defaultSettings;
	private BigDecimal minAmount;
	private BigDecimal maxAmount;
	private Integer initialTermMin;
	private Integer initialTermMax;
	private Integer maxApplications;
	@Column(precision = 12, scale = 5)
	private BigDecimal IntegererestFactor;
	@Column(precision = 12, scale = 5)
	private BigDecimal returnBaseFactor;
	@Column(precision = 12, scale = 5)
	private BigDecimal returnPerDayFactor;
	private LocalTime riskHoursStart;
	private LocalTime riskHoursEnd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDefaultSettings() {
		return defaultSettings;
	}

	public void setDefaultSettings(boolean defaultSettings) {
		this.defaultSettings = defaultSettings;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Integer getInitialTermMin() {
		return initialTermMin;
	}

	public void setInitialTermMin(Integer initialTermMin) {
		this.initialTermMin = initialTermMin;
	}

	public Integer getInitialTermMax() {
		return initialTermMax;
	}

	public void setInitialTermMax(Integer initialTermMax) {
		this.initialTermMax = initialTermMax;
	}

	public Integer getMaxApplications() {
		return maxApplications;
	}

	public void setMaxApplications(Integer maxApplications) {
		this.maxApplications = maxApplications;
	}

	public BigDecimal getInterestFactor() {
		return IntegererestFactor;
	}

	public void setInterestFactor(BigDecimal IntegererestFactor) {
		this.IntegererestFactor = IntegererestFactor;
	}

	public BigDecimal getReturnBaseFactor() {
		return returnBaseFactor;
	}

	public void setReturnBaseFactor(BigDecimal returnBaseFactor) {
		this.returnBaseFactor = returnBaseFactor;
	}

	public BigDecimal getReturnPerDayFactor() {
		return returnPerDayFactor;
	}

	public void setReturnPerDayFactor(BigDecimal returnPerDayFactor) {
		this.returnPerDayFactor = returnPerDayFactor;
	}

	public LocalTime getRiskHoursStart() {
		return riskHoursStart;
	}

	public void setRiskHoursStart(LocalTime riskHoursStart) {
		this.riskHoursStart = riskHoursStart;
	}

	public LocalTime getRiskHoursEnd() {
		return riskHoursEnd;
	}

	public void setRiskHoursEnd(LocalTime riskHoursEnd) {
		this.riskHoursEnd = riskHoursEnd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IntegererestFactor == null) ? 0 : IntegererestFactor.hashCode());
		result = prime * result + (defaultSettings ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((initialTermMax == null) ? 0 : initialTermMax.hashCode());
		result = prime * result + ((initialTermMin == null) ? 0 : initialTermMin.hashCode());
		result = prime * result + ((maxAmount == null) ? 0 : maxAmount.hashCode());
		result = prime * result + ((maxApplications == null) ? 0 : maxApplications.hashCode());
		result = prime * result + ((minAmount == null) ? 0 : minAmount.hashCode());
		result = prime * result + ((returnBaseFactor == null) ? 0 : returnBaseFactor.hashCode());
		result = prime * result + ((returnPerDayFactor == null) ? 0 : returnPerDayFactor.hashCode());
		result = prime * result + ((riskHoursEnd == null) ? 0 : riskHoursEnd.hashCode());
		result = prime * result + ((riskHoursStart == null) ? 0 : riskHoursStart.hashCode());
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
		Settings other = (Settings) obj;
		if (IntegererestFactor == null) {
			if (other.IntegererestFactor != null)
				return false;
		} else if (!IntegererestFactor.equals(other.IntegererestFactor))
			return false;
		if (defaultSettings != other.defaultSettings)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (initialTermMax == null) {
			if (other.initialTermMax != null)
				return false;
		} else if (!initialTermMax.equals(other.initialTermMax))
			return false;
		if (initialTermMin == null) {
			if (other.initialTermMin != null)
				return false;
		} else if (!initialTermMin.equals(other.initialTermMin))
			return false;
		if (maxAmount == null) {
			if (other.maxAmount != null)
				return false;
		} else if (!maxAmount.equals(other.maxAmount))
			return false;
		if (maxApplications == null) {
			if (other.maxApplications != null)
				return false;
		} else if (!maxApplications.equals(other.maxApplications))
			return false;
		if (minAmount == null) {
			if (other.minAmount != null)
				return false;
		} else if (!minAmount.equals(other.minAmount))
			return false;
		if (returnBaseFactor == null) {
			if (other.returnBaseFactor != null)
				return false;
		} else if (!returnBaseFactor.equals(other.returnBaseFactor))
			return false;
		if (returnPerDayFactor == null) {
			if (other.returnPerDayFactor != null)
				return false;
		} else if (!returnPerDayFactor.equals(other.returnPerDayFactor))
			return false;
		if (riskHoursEnd == null) {
			if (other.riskHoursEnd != null)
				return false;
		} else if (!riskHoursEnd.equals(other.riskHoursEnd))
			return false;
		if (riskHoursStart == null) {
			if (other.riskHoursStart != null)
				return false;
		} else if (!riskHoursStart.equals(other.riskHoursStart))
			return false;
		return true;
	}
}
