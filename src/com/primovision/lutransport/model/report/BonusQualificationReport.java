package com.primovision.lutransport.model.report;

import java.util.Date;

public class BonusQualificationReport {
	private String companyName;
	private String driverName;
	private String unitNum;
	
	private Date date;
	
	private String accidentCause;
	private String injuryType;
	private String violationType;
	private String violation;
	private String citation;
	private String citationNo;
	
	private Integer noOfPayChecks;
	private Date hiredDate;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getUnitNum() {
		return unitNum;
	}
	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAccidentCause() {
		return accidentCause;
	}
	public void setAccidentCause(String accidentCause) {
		this.accidentCause = accidentCause;
	}
	public String getInjuryType() {
		return injuryType;
	}
	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}
	public String getViolationType() {
		return violationType;
	}
	public void setViolationType(String violationType) {
		this.violationType = violationType;
	}
	public String getViolation() {
		return violation;
	}
	public void setViolation(String violation) {
		this.violation = violation;
	}
	public String getCitation() {
		return citation;
	}
	public void setCitation(String citation) {
		this.citation = citation;
	}
	public String getCitationNo() {
		return citationNo;
	}
	public void setCitationNo(String citationNo) {
		this.citationNo = citationNo;
	}
	public Integer getNoOfPayChecks() {
		return noOfPayChecks;
	}
	public void setNoOfPayChecks(Integer noOfPayChecks) {
		this.noOfPayChecks = noOfPayChecks;
	}
	public Date getHiredDate() {
		return hiredDate;
	}
	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}
}
