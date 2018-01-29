package com.primovision.lutransport.model.hrreport;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;

/**
 * @author kishor
 *
 */
@Entity
@Table(name="driverpay_detail")
public class DriverPay extends AbstractBaseModel{
	
	@Transient
	String origin = "";
	@Transient
	String destination = "";
	
	@Transient
	String updatedDriverPayNotes = StringUtils.EMPTY;
	@Transient
	Integer updatedDriverPayNoOfLoads = 0;
	@Transient
	Double updatedDriverPayAmount=0.0;
	
	@Column(name="driver")
	String drivername="";
	
	@Column(name="company")
	String companyname="";
	
	@Column(name="seq_num")
	String seqNum="";
	
	@Column(name="terminal_name")
	String terminalname="";
	
	@Column(name="no_of_load")
	int noOfLoad=0;
	
	// 17th Jan 2016 - Split paychex
	@Transient
	String forceNewPaychexSeqNum = StringUtils.EMPTY;
	
	@Transient
	Double rate = 0.0;
	
	@Column(name="amount")
	Double amount=0.0;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;
	
	@Column(name = "payroll_batch")
	protected Date payRollBatch;
	
	
	@Column(name = "billbatch_fromdate")
	protected Date billBatchDateFrom;
	
	@Column(name = "billbatch_todate")
	protected Date billBatchDateTo;
	
	@Transient
	Date billBatchDate;
	
	@Column(name="sick_personal")
	Double sickPersonalAmount=0.0;
	
	@Column(name="vacation_amount")
	Double vacationAmount=0.0;
	
	// Bereavement change - driver
	@Column(name="bereavementAmount")
	Double bereavementAmount=0.0;
	
	@Column(name="bonus_amount")
	Double bonusAmount=0.0;
	
	@Column(name="holiday_amount")
	Double holidayAmount=0.0;
	
	@Column(name="totalamount")
	Double totalAmount=0.0;
	
	@Column 
	Double miscAmount = 0.0;
	
	@Column 
	Double transportationAmount = 0.0;
	
	@Column 
	Double transportationAmountDiff = 0.0;
	
	@Column 
	Double probationDeductionAmount = 0.0;
	
	@Column
	String miscNote="";
	
	
	@Column
	Double reimburseAmount = 0.0;
	
	
	@Column
	Double quatarAmount = 0.0;
	
	
	
	
	@Transient
	Double transAmountDiffSpc = 0.0;
	
	@Transient
	Double transAmountSpc = 0.0;	
	
	@Transient
	Double probdeductionAmountSpc = 0.0;
	
	@Transient
	Double subTotalAmountSpc = 0.0;
	
	@Transient
	Double miscAmountSpc = 0.0;
	
	@Transient
	Double bonusAmountSpc = 0.0;
	
	@Transient
	Double ptodAmountSpc = 0.0;
	
	@Transient
	Double totalAmountSpc = 0.0;
	
	@Transient
	Double holidayAmountSpc = 0.0;
	
	@Transient
	Double vacationAmountSpc = 0.0;
	
	@Transient
	Double bereavementAmountSpc = 0.0;
	
	@Transient
	Double reimAmountSpc = 0.0;
	
	@Transient
	Double quarterAmountSpc = 0.0;
	
	@Transient
	public Double getBereavementAmountSpc() {
		return bereavementAmountSpc;
	}

	@Transient
	public void setBereavementAmountSpc(Double bereavementAmountSpc) {
		this.bereavementAmountSpc = bereavementAmountSpc;
	}

	public Double getTransportationAmount() {
		return transportationAmount;
	}
	
	public void setTransportationAmount(Double transportationAmount) {
		this.transportationAmount = transportationAmount;
	}
	
	
	// Bereavement change - driver
	public Double getBereavementAmount() {
		return bereavementAmount;
	}

	// Bereavement change - driver
	public void setBereavementAmount(Double bereavementAmount) {
		this.bereavementAmount = bereavementAmount;
	}

	public Date getBillBatchDateFrom() {
		return billBatchDateFrom;
	}
	
	public Date getBillBatchDateTo() {
		return billBatchDateTo;
	}
	
	
	public void setBillBatchDateFrom(Date billBatchDateFrom) {
		this.billBatchDateFrom = billBatchDateFrom;
	}
	
	
	public void setBillBatchDateTo(Date billBatchDateTo) {
		this.billBatchDateTo = billBatchDateTo;
	}
	 
	
	
	public String getTerminalname() {
		return terminalname;
	}
	
	public void setTerminalname(String terminalname) {
		this.terminalname = terminalname;
	}
	
	
	
	public Double getReimburseAmount() {
		return reimburseAmount;
	}
	
	public void setReimburseAmount(Double reimburseAmount) {
		this.reimburseAmount = reimburseAmount;
	}
	
	public void setMiscAmount(Double miscAmount) {
		this.miscAmount = miscAmount;
	}
	
	public void setMiscNote(String miscNote) {
		this.miscNote = miscNote;
	}
	
	public String getMiscNote() {
		return miscNote;
	}
	
	public Double getMiscAmount() {
		return miscAmount;
	}

	public Double getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(Double bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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

	public void setVacationAmount(Double vactionAmount) {
		this.vacationAmount = vactionAmount;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getNoOfLoad() {
		return noOfLoad;
	}

	public void setNoOfLoad(int noOfLoad) {
		this.noOfLoad = noOfLoad;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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

	public Date getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(Date payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public Double getHolidayAmount() {
		return holidayAmount;
	}

	public void setHolidayAmount(Double holidayAmount) {
		this.holidayAmount = holidayAmount;
	}
	
	
	public Double getProbationDeductionAmount() {
		return probationDeductionAmount;
	}
	
	public void setProbationDeductionAmount(Double probationDeductionAmount) {
		this.probationDeductionAmount = probationDeductionAmount;
	}
	
	public Date getBillBatchDate() {
		return billBatchDate;
	}
	
	
	public void setBillBatchDate(Date billBatchDate) {
		this.billBatchDate = billBatchDate;
	}
	
	
	public Double getQuatarAmount() {
		return quatarAmount;
	}
	
	public void setQuatarAmount(Double quatarAmount) {
		this.quatarAmount = quatarAmount;
	}
	
	public String getSeqNum() {
		return seqNum;
	}
	
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public Double getTransAmountSpc() {
		return transAmountSpc;
	}

	public void setTransAmountSpc(Double transAmountSpc) {
		this.transAmountSpc = transAmountSpc;
	}

	public Double getProbdeductionAmountSpc() {
		return probdeductionAmountSpc;
	}

	public void setProbdeductionAmountSpc(Double probdeductionAmountSpc) {
		this.probdeductionAmountSpc = probdeductionAmountSpc;
	}

	public Double getMiscAmountSpc() {
		return miscAmountSpc;
	}

	public void setMiscAmountSpc(Double miscAmountSpc) {
		this.miscAmountSpc = miscAmountSpc;
	}

	public Double getBonusAmountSpc() {
		return bonusAmountSpc;
	}

	public void setBonusAmountSpc(Double bonusAmountSpc) {
		this.bonusAmountSpc = bonusAmountSpc;
	}

	public Double getPtodAmountSpc() {
		return ptodAmountSpc;
	}

	public void setPtodAmountSpc(Double ptodAmountSpc) {
		this.ptodAmountSpc = ptodAmountSpc;
	}

	public Double getHolidayAmountSpc() {
		return holidayAmountSpc;
	}

	public void setHolidayAmountSpc(Double holidayAmountSpc) {
		this.holidayAmountSpc = holidayAmountSpc;
	}

	public Double getVacationAmountSpc() {
		return vacationAmountSpc;
	}

	public void setVacationAmountSpc(Double vacationAmountSpc) {
		this.vacationAmountSpc = vacationAmountSpc;
	}

	public Double getReimAmountSpc() {
		return reimAmountSpc;
	}

	public void setReimAmountSpc(Double reimAmountSpc) {
		this.reimAmountSpc = reimAmountSpc;
	}

	public Double getQuarterAmountSpc() {
		return quarterAmountSpc;
	}

	public void setQuarterAmountSpc(Double quarterAmountSpc) {
		this.quarterAmountSpc = quarterAmountSpc;
	}

	
	public Double getSubTotalAmountSpc() {
		return subTotalAmountSpc;
	}
	
	public Double getTotalAmountSpc() {
		return totalAmountSpc;
	}
	
	public void setSubTotalAmountSpc(Double subTotalAmountSpc) {
		this.subTotalAmountSpc = subTotalAmountSpc;
	}
	
	public void setTotalAmountSpc(Double totalAmountSpc) {
		this.totalAmountSpc = totalAmountSpc;
	}

	// 17th Jan 2016 - Split paychex
	@Transient
	public String getForceNewPaychexSeqNum() {
		return forceNewPaychexSeqNum;
	}

	@Transient
	public void setForceNewPaychexSeqNum(String forceNewPaychexSeqNum) {
		this.forceNewPaychexSeqNum = forceNewPaychexSeqNum;
	}

	@Transient
	public String getUpdatedDriverPayNotes() {
		return updatedDriverPayNotes;
	}

	@Transient
	public void setUpdatedDriverPayNotes(String updatedDriverPayNotes) {
		this.updatedDriverPayNotes = updatedDriverPayNotes;
	}
	
	@Transient
	public Double getUpdatedDriverPayAmount() {
		return updatedDriverPayAmount;
	}

	@Transient
	public void setUpdatedDriverPayAmount(Double updatedDriverPayAmount) {
		this.updatedDriverPayAmount = updatedDriverPayAmount;
	}

	@Transient
	public Integer getUpdatedDriverPayNoOfLoads() {
		return updatedDriverPayNoOfLoads;
	}

	@Transient
	public void setUpdatedDriverPayNoOfLoads(Integer updatedDriverPayNoOfLoads) {
		this.updatedDriverPayNoOfLoads = updatedDriverPayNoOfLoads;
	}
	
	@Transient
	public Double getTransAmountDiffSpc() {
		return transAmountDiffSpc;
	}
	@Transient
	public void setTransAmountDiffSpc(Double transAmountDiffSpc) {
		this.transAmountDiffSpc = transAmountDiffSpc;
	}

	public Double getTransportationAmountDiff() {
		return transportationAmountDiff;
	}

	public void setTransportationAmountDiff(Double transportationAmountDiff) {
		this.transportationAmountDiff = transportationAmountDiff;
	}
}

