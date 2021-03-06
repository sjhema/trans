package com.primovision.lutransport.model.hrreport;
/**
 * @author kishor
 *
 */
public class SalaryDetail {
	private String driver="";
	
	private String category="";
	
	private String companyname="";
	
	private String terminalName="";
	
	private Double amount=0.0;
	
	private Double transportationAmountDiff=0.0;
	
	private Double sickPersonalAmount=0.0;
	
	private Double vacationAmount=0.0;
	
	private Double bonusAmount=0.0;
	
	private Double holidayAmount=0.0;
	
	private Double totalAmount=0.0;
	
	private Double miscAmount=0.0;
	
	private Double reimburseAmount=0.0;
	
	private Double quarterAmount=0.0;
	
	private Double probationDetection=0.0;
	
	// Bereavement change
	private Double bereavementAmount=0.0;
	
	// Worker comp change
	private Double workerCompAmount=0.0;
	
	public Double getWorkerCompAmount() {
		return workerCompAmount;
	}

	public void setWorkerCompAmount(Double workerCompAmount) {
		this.workerCompAmount = workerCompAmount;
	}

	public Double getProbationDetection() {
		return probationDetection;
	}
	
	public void setProbationDetection(Double probationDetection) {
		this.probationDetection = probationDetection;
	}
	
	
	
	public void setMiscAmount(Double miscAmount) {
		this.miscAmount = miscAmount;
	}
	
	public void setReimburseAmount(Double reimburseAmount) {
		this.reimburseAmount = reimburseAmount;
	}
	
	public Double getMiscAmount() {
		return miscAmount;
	}
	
	public Double getReimburseAmount() {
		return reimburseAmount;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getSickPersonalAmount() {
		return sickPersonalAmount;
	}

	public void setSickPersonalAmount(Double sickPersonalAmount) {
		this.sickPersonalAmount = sickPersonalAmount;
	}

	public Double getVacationAmount() {
		return vacationAmount;
	}

	public void setVacationAmount(Double vacationAmount) {
		this.vacationAmount = vacationAmount;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getQuarterAmount() {
		return quarterAmount;
	}
	public void setQuarterAmount(Double quarterAmount) {
		this.quarterAmount = quarterAmount;
	}

	public Double getBereavementAmount() {
		return bereavementAmount;
	}

	public void setBereavementAmount(Double bereavementAmount) {
		this.bereavementAmount = bereavementAmount;
	}

	public Double getTransportationAmountDiff() {
		return transportationAmountDiff;
	}

	public void setTransportationAmountDiff(Double transportationAmountDiff) {
		this.transportationAmountDiff = transportationAmountDiff;
	}

}
