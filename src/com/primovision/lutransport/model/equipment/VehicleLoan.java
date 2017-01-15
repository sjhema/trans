package com.primovision.lutransport.model.equipment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="vehicle_loan")
public class VehicleLoan extends AbstractBaseModel {
	@Column(name="loan_no")
	private String loanNo;
	
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name="lender")
	private EquipmentLender lender;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="interest_rate")
	private Double interestRate;
	
	@Column(name="payment_due_dom")
	private String paymentDueDom;
	
	@Column(name="payment_amount")
	private Double paymentAmount;
	
	@Column(name="no_of_payments")
	private Integer noOfPayments;
	
	@Column(name="description")
	private String description;
	
	@Transient
	private String lenderName;
	
	@Transient
	private Integer paymentsLeft;

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public EquipmentLender getLender() {
		return lender;
	}

	public void setLender(EquipmentLender lender) {
		this.lender = lender;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public String getPaymentDueDom() {
		return paymentDueDom;
	}

	public void setPaymentDueDom(String paymentDueDom) {
		this.paymentDueDom = paymentDueDom;
	}

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoOfPayments() {
		return noOfPayments;
	}

	public void setNoOfPayments(Integer noOfPayments) {
		this.noOfPayments = noOfPayments;
	}
	
	@Transient
	public String getLenderName() {
		return lenderName;
	}

	@Transient
	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}

	@Transient
	public Integer getPaymentsLeft() {
		return paymentsLeft;
	}

	@Transient
	public void setPaymentsLeft(Integer paymentsLeft) {
		this.paymentsLeft = paymentsLeft;
	}
}
