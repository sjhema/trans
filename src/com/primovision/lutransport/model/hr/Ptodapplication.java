package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.ReportModel;
import com.primovision.lutransport.model.Ticket;

/**
 * @author subodh
 * 
 */

@Entity
@Table(name = "ptodapplication")
public class Ptodapplication extends AbstractBaseModel /*implements ReportModel*/{

	/*private static final long serialVersionUID = -7960731852472127475L;*/
	
	
	@ManyToOne
	@JoinColumn(name="company")
	protected Location company;
	
	@ManyToOne
	@JoinColumn(name = "terminal")
	protected Location terminal;
	
	@ManyToOne
	@JoinColumn(name = "category")
	protected EmployeeCatagory category;
	
	@ManyToOne
	@JoinColumn(name="employee")
	protected Driver driver;
	
	@ManyToOne
	@JoinColumn(name="approve_by")
	protected Driver approveby;
	
	@ManyToOne
	@JoinColumn(name="leave_type")
	protected LeaveType leavetype;
	
	/*@NotEmpty
	@Column(name = "leave_type")
	protected String leavetype;*/
    
	
	@Column(name="hired_date")
	protected Date hireddate;
	
	
	@Column(name="re_hired_date")
	protected Date rehireddate;
	
	@NotNull
	@Column(name="batch_date")
	protected Date batchdate;
	
	@NotNull
	@Column(name = "earned_days")
	protected Integer earneddays;
	
	@Column(name = "earned_hours")
	protected Double earnedhours;
	
	@NotNull
	@Column(name = "used_days")
	protected Integer useddays;
	
	@Column(name = "used_hours")
	protected Double usedhours;
	
	@NotNull
	@Column(name = "remaining_days")
	protected Integer remainingdays;
	
	@Column(name = "remaining_hours")
	protected Double remaininghours;
	
	
	@Column(name="leave_date_from")
	protected Date leavedatefrom;
	
	@Column(name="leave_date_to")
	protected Date leavedateto;
	
	@Column(name="days_requested")
	protected  Integer daysrequested;
	
	@Column(name="hours_requested")
	protected  Double hoursrequested;
	
	@NotNull
	@Column(name="submit_date")
	protected Date submitdate;

	//@NotNull
	@Column(name="paid_out_days")
	protected  Integer paidoutdays=0;
	
	@Column(name="paid_out_hours")
	protected  Double paidouthours=0.0;
	
	@Transient
	protected Integer temppaidoutdays=0;
	
	@Transient
	protected Double temppaidouthours=0.0;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	protected Integer payRollStatus=1;
	
	
	//@NotNull
	@Column(name="days_paid")
	protected  Integer dayspaid=0;
	
	@Transient
	protected Integer tempdayspaid=0;
	
	//@NotNull
	@Column(name="days_unpaid")
	protected  Integer daysunpaid;
	
	//@NotNull
	@Column(name="hours_paid")
	protected  Double hourspaid=0.0;
	
	@Transient
	protected Double temphourspaid=0.0;
	
	//@NotNull
	@Column(name="hours_unpaid")
	protected  Double hoursunpaid;
	
	@NotNull
	@Column(name="approve_status")
	protected  Integer approvestatus;
	
	@NotNull
	@Column(name="date_approved")
	protected Date dateapproved;
	
	
	@Column(name="check_date")
	protected Date checkdate;
	
	@Column(name="nextanniversary_date")
	protected Date nextAnniversaryDate;
	
	@Column(name = "nextallotment_days")
	protected Integer nextAllotmentDays;
	
	@Column(name = "nextallotment_hours")
	protected Double nextAllotmenthours;
	
	
	@NotNull
	@Column(name="ptod_rates")
	protected  Double ptodrates;
	
	@NotNull
	@Column(name="ptod_hourly_rate")
	protected  Double ptodhourlyrate;
	
	@NotNull
	@Column(name="hourly_amount_paid")
	protected  Double hourlyamountpaid;		
		
	@NotNull
	@Column(name="amount_paid")
	protected  Double amountpaid;

	@Column(name = "notes")
	private String notes;
	
	
	
	@Column(name = "seq_num1")
	private Integer sequenceNum1=0;
	
	@Column(name = "seq_num2")
	private Integer sequenceNum2=0;
	
	@Column(name = "seq_num3")
	private Integer sequenceNum3=0;
	
	@Column(name = "seq_num4")
	private Integer sequenceNum4=0;
	
	
	@Column(name = "seq_amt1")
	private Double sequenceAmt1=0.0;
	
	@Column(name = "seq_amt2")
	private Double sequenceAmt2=0.0;
	
	@Column(name = "seq_amt3")
	private Double sequenceAmt3=0.0;
	
	@Column(name = "seq_amt4")
	private Double sequenceAmt4=0.0;
	
	
	
	public Double getPaidouthours() {
		setTemppaidouthours(paidouthours);
		return paidouthours;
	}

	public void setPaidouthours(Double paidouthours) {
		this.paidouthours = paidouthours;
	}

	public Double getHoursrequested() {
		return hoursrequested;
	}

	public void setHoursrequested(Double hoursrequested) {
		this.hoursrequested = hoursrequested;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}

	public EmployeeCatagory getCategory() {
		return category;
	}

	public void setCategory(EmployeeCatagory category) {
		this.category = category;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Driver getApproveby() {
		return approveby;
	}

	public void setApproveby(Driver approveby) {
		this.approveby = approveby;
	}

	
	
	public LeaveType getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(LeaveType leavetype) {
		this.leavetype = leavetype;
	}

	/*public String getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}
*/
	
	
	
	
	
	public Date getHireddate() {
		return hireddate;
	}

	public Double getEarnedhours() {
		return earnedhours;
	}

	public void setEarnedhours(Double earnedhours) {
		this.earnedhours = earnedhours;
	}

	public Double getUsedhours() {
		return usedhours;
	}

	public void setUsedhours(Double usedhours) {
		this.usedhours = usedhours;
	}

	public Double getRemaininghours() {
		return remaininghours;
	}

	public void setRemaininghours(Double remaininghours) {
		this.remaininghours = remaininghours;
	}

	public void setHireddate(Date hireddate) {
		this.hireddate = hireddate;
	}

	public Integer getEarneddays() {
		return earneddays;
	}

	public void setEarneddays(Integer earneddays) {
		this.earneddays = earneddays;
	}

	public Integer getUseddays() {
		return useddays;
	}

	public void setUseddays(Integer useddays) {
		this.useddays = useddays;
	}

	public Integer getRemainingdays() {
		return remainingdays;
	}

	public void setRemainingdays(Integer remainingdays) {
		this.remainingdays = remainingdays;
	}

	public Date getLeavedatefrom() {
		return leavedatefrom;
	}

	public void setLeavedatefrom(Date leavedatefrom) {
		this.leavedatefrom = leavedatefrom;
	}

	public Date getLeavedateto() {
		return leavedateto;
	}

	public void setLeavedateto(Date leavedateto) {
		this.leavedateto = leavedateto;
	}

	public Integer getDaysrequested() {
		return daysrequested;
	}

	public void setDaysrequested(Integer daysrequested) {
		this.daysrequested = daysrequested;
	}

	public Date getSubmitdate() {
		return submitdate;
	}

	public void setSubmitdate(Date submitdate) {
		this.submitdate = submitdate;
	}

	public Integer getPaidoutdays() {
		setTemppaidoutdays(paidoutdays);
		return paidoutdays;
	}

	public void setPaidoutdays(Integer paidoutdays) {
		this.paidoutdays = paidoutdays;
	}

	public Integer getDayspaid() {
		setTempdayspaid(dayspaid);
		return dayspaid;
	}

	public void setDayspaid(Integer dayspaid) {
		this.dayspaid = dayspaid;
	}

	public Integer getDaysunpaid() {
		return daysunpaid;
	}

	public void setDaysunpaid(Integer daysunpaid) {
		this.daysunpaid = daysunpaid;
	}

	public Double getHourspaid() {
		setTemphourspaid(hourspaid);
		return hourspaid;
	}

	public void setHourspaid(Double hourspaid) {		
		this.hourspaid = hourspaid;
	}

	public Double getHoursunpaid() {		
		return hoursunpaid;
	}

	public void setHoursunpaid(Double hoursunpaid) {
		this.hoursunpaid = hoursunpaid;
	}

	public Integer getApprovestatus() {
		return approvestatus;
	}

	public void setApprovestatus(Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	public Date getDateapproved() {
		return dateapproved;
	}

	public void setDateapproved(Date dateapproved) {
		this.dateapproved = dateapproved;
	}

	public Date getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(Date checkdate) {
		this.checkdate = checkdate;
	}

	public Double getPtodrates() {
		return ptodrates;
	}

	public void setPtodrates(Double ptodrates) {
		this.ptodrates = ptodrates;
	}

	public Double getPtodhourlyrate() {
		return ptodhourlyrate;
	}

	public void setPtodhourlyrate(Double ptodhourlyrate) {
		this.ptodhourlyrate = ptodhourlyrate;
	}

	public Double getHourlyamountpaid() {
		return hourlyamountpaid;
	}

	public void setHourlyamountpaid(Double hourlyamountpaid) {
		this.hourlyamountpaid = hourlyamountpaid;
	}

	public Double getAmountpaid() {
		return amountpaid;
	}

	public void setAmountpaid(Double amountpaid) {
		this.amountpaid = amountpaid;
	}

	public Date getBatchdate() {
		return batchdate;
	}

	public void setBatchdate(Date batchdate) {
		this.batchdate = batchdate;
	}

   public Integer getTempdayspaid() {
	return tempdayspaid;
}
   
   
   public Double getTemphourspaid() {
	return temphourspaid;
}
   
   public void setTempdayspaid(Integer tempdayspaid) {
	this.tempdayspaid = tempdayspaid;
}
   
   
   public void setTemphourspaid(Double temphourspaid) {
	this.temphourspaid = temphourspaid;
}
   
public void setTemppaidoutdays(Integer temppaidoutdays) {
	this.temppaidoutdays = temppaidoutdays;
}


public void setTemppaidouthours(Double temppaidouthours) {
	this.temppaidouthours = temppaidouthours;
}

public Integer getTemppaidoutdays() {
	return temppaidoutdays;
}


public Double getTemppaidouthours() {
	return temppaidouthours;
}	

public Date getRehireddate() {
	return rehireddate;
}


public void setRehireddate(Date rehireddate) {
	this.rehireddate = rehireddate;
}

public Date getPayRollBatch() {
	return payRollBatch;
}

public void setPayRollBatch(Date payRollBatch) {
	this.payRollBatch = payRollBatch;
}

public Integer getPayRollStatus() {
	return payRollStatus;
}

public void setPayRollStatus(Integer payRollStatus) {
	this.payRollStatus = payRollStatus;
}

public Date getNextAnniversaryDate() {
	return nextAnniversaryDate;
}

public void setNextAnniversaryDate(Date nextAnniversaryDate) {
	this.nextAnniversaryDate = nextAnniversaryDate;
}

public Integer getNextAllotmentDays() {
	return nextAllotmentDays;
}

public void setNextAllotmentDays(Integer nextAllotmentDays) {
	this.nextAllotmentDays = nextAllotmentDays;
}

public Double getNextAllotmenthours() {
	return nextAllotmenthours;
}

public void setNextAllotmenthours(Double nextAllotmenthours) {
	this.nextAllotmenthours = nextAllotmenthours;
}

public Integer getSequenceNum1() {
	return sequenceNum1;
}

public void setSequenceNum1(Integer sequenceNum1) {
	this.sequenceNum1 = sequenceNum1;
}

public Integer getSequenceNum2() {
	return sequenceNum2;
}

public void setSequenceNum2(Integer sequenceNum2) {
	this.sequenceNum2 = sequenceNum2;
}

public Integer getSequenceNum3() {
	return sequenceNum3;
}

public void setSequenceNum3(Integer sequenceNum3) {
	this.sequenceNum3 = sequenceNum3;
}

public Integer getSequenceNum4() {
	return sequenceNum4;
}

public void setSequenceNum4(Integer sequenceNum4) {
	this.sequenceNum4 = sequenceNum4;
}

public Double getSequenceAmt1() {
	return sequenceAmt1;
}

public void setSequenceAmt1(Double sequenceAmt1) {
	this.sequenceAmt1 = sequenceAmt1;
}

public Double getSequenceAmt2() {
	return sequenceAmt2;
}

public void setSequenceAmt2(Double sequenceAmt2) {
	this.sequenceAmt2 = sequenceAmt2;
}

public Double getSequenceAmt3() {
	return sequenceAmt3;
}

public void setSequenceAmt3(Double sequenceAmt3) {
	this.sequenceAmt3 = sequenceAmt3;
}

public Double getSequenceAmt4() {
	return sequenceAmt4;
}

public void setSequenceAmt4(Double sequenceAmt4) {
	this.sequenceAmt4 = sequenceAmt4;
}

}
