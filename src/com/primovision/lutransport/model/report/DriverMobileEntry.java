package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.ReportModel;


@Entity
@Table(name="driver_mobile_entry")
public class DriverMobileEntry extends AbstractBaseModel implements ReportModel {

	
	@Column(name="employee_company_id")
	protected Long employeeCompanyId;
	
	@Column(name="employee_company_name")
	protected String employeeCompanyName;
	
	@Column(name="employee_terminal_name")
	protected String employeeTerminalName;
	
	@Column(name="employee_terminal_id")
	protected String employeeTerminalId;
	
	@Column(name = "entry_date")
	protected Date entryDate;
	
	@Transient
	protected String tempEntryDate;
	
	@Column(name="employee_name")
	protected String employeeName;
	
	@Column(name="employee_id")
	protected String employeeId;
	
	@Column(name="tripsheet_flag")
	protected String tripsheet_flag;
	
	@Column(name="fuellog_flag")
	protected String fuellog_flag;
	
	@Column(name="odometer_flag")
	protected String odometer_flag;
	
	@Column(name="entered_by")
	protected String enteredBy;

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public Long getEmployeeCompanyId() {
		return employeeCompanyId;
	}

	public void setEmployeeCompanyId(Long employeeCompanyId) {
		this.employeeCompanyId = employeeCompanyId;
	}

	public String getEmployeeCompanyName() {
		return employeeCompanyName;
	}

	public void setEmployeeCompanyName(String employeeCompanyName) {
		this.employeeCompanyName = employeeCompanyName;
	}

	public String getEmployeeTerminalName() {
		return employeeTerminalName;
	}

	public void setEmployeeTerminalName(String employeeTerminalName) {
		this.employeeTerminalName = employeeTerminalName;
	}

	public String getEmployeeTerminalId() {
		return employeeTerminalId;
	}

	public void setEmployeeTerminalId(String employeeTerminalId) {
		this.employeeTerminalId = employeeTerminalId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getTripsheet_flag() {
		return tripsheet_flag;
	}

	public void setTripsheet_flag(String tripsheet_flag) {
		this.tripsheet_flag = tripsheet_flag;
	}

	public String getFuellog_flag() {
		return fuellog_flag;
	}

	public void setFuellog_flag(String fuellog_flag) {
		this.fuellog_flag = fuellog_flag;
	}

	public String getOdometer_flag() {
		return odometer_flag;
	}

	public void setOdometer_flag(String odometer_flag) {
		this.odometer_flag = odometer_flag;
	}

	public String getTempEntryDate() {
		return tempEntryDate;
	}
	public void setTempEntryDate(String tempEntryDate) {
		this.tempEntryDate = tempEntryDate;
	}
	
	
	
}
