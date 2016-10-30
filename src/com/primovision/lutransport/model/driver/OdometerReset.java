package com.primovision.lutransport.model.driver;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="odometer_reset")
public class OdometerReset extends AbstractBaseModel{
	@ManyToOne
	@JoinColumn(name="truck")
	private Vehicle truck;
	
	@NotNull
	@Column(name="reset_date")
	private Date resetDate;

	@NotNull
	@Column(name="reset_reading")
	private Integer resetReading;	
	
	@NotNull
	@Column(name="entered_by")
	private String enteredBy;
	
	@NotNull
	@Column(name="entered_by_id")
	private Long enteredById;
	
	@NotNull
	@Column(name="tempTruck")
	private String tempTruck;

	public Vehicle getTruck() {
		return truck;
	}

	public void setTruck(Vehicle truck) {
		this.truck = truck;
	}

	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}

	public Long getEnteredById() {
		return enteredById;
	}

	public void setEnteredById(Long enteredById) {
		this.enteredById = enteredById;
	}

	public Integer getResetReading() {
		return resetReading;
	}
	
	public void setResetReading(Integer resetReading) {
		this.resetReading = resetReading;
	}

	public String getEnteredBy() {
		return enteredBy;
	}
	
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public String getTempTruck() {
		return tempTruck;
	}

	public void setTempTruck(String tempTruck) {
		this.tempTruck = tempTruck;
	}
}
