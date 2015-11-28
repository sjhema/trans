package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="demurrage_charges")
public class DemurrageCharges extends AbstractBaseModel{
	
		@NotEmpty
		@Column(name="demurrage_code")
		private String demurragecode;
		
		@NotEmpty
		@Column(name="times_used")
		private String timesUsed;
		
		@NotNull
		@Column(name="charge_after")
		private Integer chargeAfter;
		
		@NotNull
		@Column(name="time_increments")
		private Integer timeIncrements;		
		
		
		@NotNull
		private Double demurragerate;
		
		@NotNull
		@Column(name="valid_from")
		private Date validFrom;
		
		@NotNull
		@Column(name="valid_to")
		private Date validTo;

		
		@Transient
		private Date validFromTemp;

		@Transient
		private Date validToTemp;

		public String getDemurragecode() {
			return demurragecode;
		}

		public void setDemurragecode(String demurragecode) {
			this.demurragecode = demurragecode;
		}

		public String getTimesUsed() {
			return timesUsed;
		}

		public void setTimesUsed(String timesUsed) {
			this.timesUsed = timesUsed;
		}

		public Integer getChargeAfter() {
			return chargeAfter;
		}

		public void setChargeAfter(Integer chargeAfter) {
			this.chargeAfter = chargeAfter;
		}

		public Integer getTimeIncrements() {
			return timeIncrements;
		}

		public void setTimeIncrements(Integer timeIncrements) {
			this.timeIncrements = timeIncrements;
		}

		public Double getDemurragerate() {
			return demurragerate;
		}

		public void setDemurragerate(Double demurragerate) {
			this.demurragerate = demurragerate;
		}

		public Date getValidFrom() {
			setValidFromTemp(validFrom);
			return validFrom;
		}

		public void setValidFrom(Date validFrom) {
			this.validFrom = validFrom;
		}

		public Date getValidTo() {
			setValidToTemp(validTo);
			return validTo;
		}

		public void setValidTo(Date validTo) {
			
			this.validTo = validTo;
		}

		public Date getValidFromTemp() {
			return validFromTemp;
		}

		public void setValidFromTemp(Date validFromTemp) {
			this.validFromTemp = validFromTemp;
		}

		public Date getValidToTemp() {
			return validToTemp;
		}

		public void setValidToTemp(Date validToTemp) {
			this.validToTemp = validToTemp;
		}
		
		
		
}
