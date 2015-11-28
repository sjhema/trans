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
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Driver;


/**
 * @author subodh
 * 
 */

@Entity
@Table(name ="attendance")
public class Attendance extends AbstractBaseModel {
	

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="shift")
	protected ShiftCalendar shift;
	
	@ManyToOne
	@JoinColumn(name="employee")
	protected Driver driver;
	
	@ManyToOne
	@JoinColumn(name="catagory")
	private EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	
	@NotNull
	@Column(name="attendance_date")
	protected Date attendancedate;
	
	
	@Column(name="import_date")
	protected Date importdate;

	
	//@NotEmpty
	@Column(name ="signin_time")
	protected String signintime;
	
	//@NotEmpty
	@Column(name ="signout_time")
	protected String signouttime;

	@NotEmpty
	@Column(name ="timestart_hours")
	protected String timestarthours;
	
	@NotEmpty
	@Column(name ="timestart_minuts")
	protected String timestartminuts;
	
	@NotEmpty
	@Column(name ="timeendin_hours")
	protected String timeendinhours;
	
	@NotEmpty
	@Column(name ="timeendin_minuts")
	protected String timeendinminuts;
	
	
	@Column(name ="hours_worked")
	protected Double hoursworked;
	
	@Column(name ="over_time_hours")
	protected Double overtimehours;
	
	
	@Transient
	private String companies;
	
	@Transient
	private String employees;
	@Transient
	private String employeesNo;
	
	@Transient
	private String attendanceDates;
	
	
	@Transient
	private String terminals;
	
	
	
	
	
	
	
	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}

	public String getEmployees() {
		return employees;
	}

	public void setEmployees(String employees) {
		this.employees = employees;
	}

	public String getEmployeesNo() {
		return employeesNo;
	}

	public void setEmployeesNo(String employeesNo) {
		this.employeesNo = employeesNo;
	}

	public String getAttendanceDates() {
		return attendanceDates;
	}

	public void setAttendanceDates(String attendanceDates) {
		this.attendanceDates = attendanceDates;
	}

	public String getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}

	public EmployeeCatagory getCatagory() {
		return catagory;
	}

	public void setCatagory(EmployeeCatagory catagory) {
		this.catagory = catagory;
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

	public ShiftCalendar getShift() {
		return shift;
	}

	public void setShift(ShiftCalendar shift) {
		this.shift = shift;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Date getAttendancedate() {
		return attendancedate;
	}

	public void setAttendancedate(Date attendancedate) {
		this.attendancedate = attendancedate;
	}

	
	
	
	
	
	public Date getImportdate() {
		return importdate;
	}

	public void setImportdate(Date importdate) {
		this.importdate = importdate;
	}

	public String getSignintime() {
		return signintime;
	}

	public void setSignintime(String signintime) {
		this.signintime = signintime;
	}

	public String getSignouttime() {
		return signouttime;
	}

	public void setSignouttime(String signouttime) {
		this.signouttime = signouttime;
	}

	public String getTimestarthours() {
		return timestarthours;
	}

	public void setTimestarthours(String timestarthours) {
		this.timestarthours = timestarthours;
	}

	public String getTimestartminuts() {
		return timestartminuts;
	}

	public void setTimestartminuts(String timestartminuts) {
		this.timestartminuts = timestartminuts;
	}

	public String getTimeendinhours() {
		return timeendinhours;
	}

	public void setTimeendinhours(String timeendinhours) {
		this.timeendinhours = timeendinhours;
	}

	public String getTimeendinminuts() {
		return timeendinminuts;
	}

	public void setTimeendinminuts(String timeendinminuts) {
		this.timeendinminuts = timeendinminuts;
	}

	public Double getHoursworked() {
		return hoursworked;
	}

	public void setHoursworked(Double hoursworked) {
		this.hoursworked = hoursworked;
	}

	public Double getOvertimehours() {
		return overtimehours;
	}

	public void setOvertimehours(Double overtimehours) {
		this.overtimehours = overtimehours;
	}
	
    
	
	
}
