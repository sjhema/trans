package com.primovision.lutransport.model.hrreport;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;

/**
 * @author kishor
 *
 */

@Entity
@Table(name="paychex_detail")
public class PayChexDetail extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location companyLocation;
	
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminalLocation;
	
	
	private Date checkDate;
	
	private String homeBr;
	
	private String homeDpt;
	
	private String eeNo;
	
	private String lastName;
	
	private String firstName;
	
	private Double salary;
	
	private Integer seqNo;
	
	private Double rate;
	
	private Double ovRate;
	
	private String ovBr;
	
	private String ovDpt;
	
	private Double regularHour;
	
	private Double overTimeHour;
	
	private Double holidayHour;
	
	private Double vacationAmount;
	
	private Double vacationHour;
	
	private Double perSickHours;
	
	private Double personalSickAmount;
	
	private Double transportDriverAmount;
	
	private Double bonusAmount;
	
	private Double holidayAmount;
	
	private Double bereavementAmount;
	
	private Double workerCompAmount;
	
	private Double reimburseAmount;
	
	private Double miscAmount;
	
	private Double advanceAmount;

	public String getHomeBr() {
		return homeBr;
	}

	public void setHomeBr(String homeBr) {
		this.homeBr = homeBr;
	}

	public String getHomeDpt() {
		return homeDpt;
	}

	public void setHomeDpt(String homeDpt) {
		this.homeDpt = homeDpt;
	}

	public String getEeNo() {
		return eeNo;
	}

	public void setEeNo(String eeNo) {
		this.eeNo = eeNo;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Integer getSeqNo() {
		return seqNo;
	}
	
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getOvRate() {
		return ovRate;
	}

	public void setOvRate(Double ovRate) {
		this.ovRate = ovRate;
	}

	public String getOvBr() {
		return ovBr;
	}

	public void setOvBr(String ovBr) {
		this.ovBr = ovBr;
	}

	public String getOvDpt() {
		return ovDpt;
	}

	public void setOvDpt(String ovDpt) {
		this.ovDpt = ovDpt;
	}

	public Double getRegularHour() {
		return regularHour;
	}

	public void setRegularHour(Double regularHour) {
		this.regularHour = regularHour;
	}

	public Double getOverTimeHour() {
		return overTimeHour;
	}

	public void setOverTimeHour(Double overTimeHour) {
		this.overTimeHour = overTimeHour;
	}

	public Double getHolidayHour() {
		return holidayHour;
	}

	public void setHolidayHour(Double holidayHour) {
		this.holidayHour = holidayHour;
	}

	public Double getVacationAmount() {
		return vacationAmount;
	}

	public void setVacationAmount(Double vacationAmount) {
		this.vacationAmount = vacationAmount;
	}

	public Double getVacationHour() {
		return vacationHour;
	}

	public void setVacationHour(Double vacationHour) {
		this.vacationHour = vacationHour;
	}

	public Double getPersonalSickAmount() {
		return personalSickAmount;
	}

	public void setPersonalSickAmount(Double personalSickAmount) {
		this.personalSickAmount = personalSickAmount;
	}

	public Double getTransportDriverAmount() {
		return transportDriverAmount;
	}

	public void setTransportDriverAmount(Double transportDriverAmount) {
		this.transportDriverAmount = transportDriverAmount;
	}

	public Double getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(Double bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public Double getHolidayAmount() {
		return holidayAmount;
	}

	public void setHolidayAmount(Double holidayAmount) {
		this.holidayAmount = holidayAmount;
	}

	public Double getBereavementAmount() {
		return bereavementAmount;
	}

	public void setBereavementAmount(Double bereavementAmount) {
		this.bereavementAmount = bereavementAmount;
	}

	public Double getWorkerCompAmount() {
		return workerCompAmount;
	}

	public void setWorkerCompAmount(Double workerCompAmount) {
		this.workerCompAmount = workerCompAmount;
	}

	public Double getReimburseAmount() {
		return reimburseAmount;
	}

	public void setReimburseAmount(Double reimburseAmount) {
		this.reimburseAmount = reimburseAmount;
	}

	public Double getMiscAmount() {
		return miscAmount;
	}

	public void setMiscAmount(Double miscAmount) {
		this.miscAmount = miscAmount;
	}

	public Double getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}
	
	public Double getPerSickHours() {
		return perSickHours;
	}
	
	public void setPerSickHours(Double perSickHours) {
		this.perSickHours = perSickHours;
	}

	public Location getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(Location companyLocation) {
		this.companyLocation = companyLocation;
	}

	public Location getTerminalLocation() {
		return terminalLocation;
	}

	public void setTerminalLocation(Location terminalLocation) {
		this.terminalLocation = terminalLocation;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	

	
	
	
}
