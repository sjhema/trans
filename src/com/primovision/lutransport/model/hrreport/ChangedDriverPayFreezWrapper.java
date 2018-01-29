package com.primovision.lutransport.model.hrreport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;


@Entity
@Table(name="changed_driverpay_freezed")
public class ChangedDriverPayFreezWrapper extends AbstractBaseModel{
	@Transient
	public static Integer CHANGED_STATUS_IN_PROCESS = 0;
	
	@Column(name="base_id")
	protected Long baseId;
	
	@Column(name="changed_status")
	protected Integer changedStatus;
	
	@Column(name="origin")
	String origin = "";
	
	@Column(name="destination")
	String destination = "";
	
	@Column(name="driver")
	String drivername="";
	
	@Column(name="company")
	String companyname="";
	
	@Column(name="seq_num")
	String seqNum="";
	
	@Column(name="terminal_name")
	String terminalname="";
	
	@Column(name="is_main_row")
	String isMainRow="";
	
	@Column(name="no_of_load")
	int noOfLoad=0;
	
	@Column(name="no_of_loadtotal")
	int noOfLoadtotal=0;
	
	@Column(name="rate")
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
	
	@Column(name = "payroll_batchstring")
	protected String payRollBatchString;
	
	
	@Column(name = "billbatch_fromdatestring")
	protected String billBatchDateFromString;
	
	@Column(name = "billbatch_todatestring")
	protected String billBatchDateToString;
	
	@Transient
	Date billBatchDate;
	
	
	
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
	Double subTotalAmount = 0.0;
	
	@Column 
	Double probationDeductionAmount = 0.0;
	
	
	
	@Column
	Double reimburseAmount = 0.0;
	
	@Column
	Double quatarAmount = 0.0;
	
	
	Double deductionAmount = 0.0;
	
	
	Double sickParsonalAmount=0.00;
	
	Double sickPersonalAmount=0.0;
	
	Integer numberOfSickDays = 0;
	
	Integer numberOfVactionDays = 0;
	
	
	Double bonusAmount0=null;
	Double bonusAmount1=null;
	Double bonusAmount2=null;
	Double bonusAmount3=null;
	Double bonusAmount4=null;
	
	String bonusTypeName="";
	String bonusTypeName0="";
	String bonusTypeName1="";
	String bonusTypeName2="";
	String bonusTypeName3="";
	String bonusTypeName4="";
	
	String bonusNotes="";
	String bonusNotes0="";
	String bonusNotes1="";
	String bonusNotes2="";
	String bonusNotes3="";
	String bonusNotes4="";						
	
	
	String holidayname="";
	String holidaydateFrom="";
	String holidaydateTo="";
	
	
	String reimburseNotes="";
	Double reimburseAmt=0.0;
	
	
	String qutarNotes="";
	Double qutarAmt=0.0;
	
	Double miscamt=0.0;
	Double miscamt0=null;
	Double miscamt1=null;
	Double miscamt2=null;
	Double miscamt3=null;
	Double miscamt4=null;
	Double miscamt5=null;
	
	String miscnote=""; 
	String miscnote0="";
	String miscnote1="";
	String miscnote2="";
	String miscnote3="";
	String miscnote4="";
	String miscnote5="";
	
	Double sumTotal=0.0;
	
	
	Integer totalRowCount=0;	
	
	Double sumAmount=0.0;
	
	
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
	
	// Bereavement change - driver
	@Transient
	Double bereavementAmountSpc = 0.0;
	
	@Transient
	Double reimAmountSpc = 0.0;
	
	@Transient
	Double quarterAmountSpc = 0.0;
	
	
	// Bereavement change - driver
	@Transient
	public Double getBereavementAmountSpc() {
		return bereavementAmountSpc;
	}
	// Bereavement change - driver
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

	public Double getSickParsonalAmount() {
		return sickParsonalAmount;
	}

	public void setSickParsonalAmount(Double sickParsonalAmount) {
		this.sickParsonalAmount = sickParsonalAmount;
	}

	public Integer getNumberOfSickDays() {
		return numberOfSickDays;
	}

	public void setNumberOfSickDays(Integer numberOfSickDays) {
		this.numberOfSickDays = numberOfSickDays;
	}

	public Integer getNumberOfVactionDays() {
		return numberOfVactionDays;
	}

	public void setNumberOfVactionDays(Integer numberOfVactionDays) {
		this.numberOfVactionDays = numberOfVactionDays;
	}

	public Double getBonusAmount0() {
		return bonusAmount0;
	}

	public void setBonusAmount0(Double bonusAmount0) {
		this.bonusAmount0 = bonusAmount0;
	}

	public Double getBonusAmount1() {
		return bonusAmount1;
	}

	public void setBonusAmount1(Double bonusAmount1) {
		this.bonusAmount1 = bonusAmount1;
	}

	public Double getBonusAmount2() {
		return bonusAmount2;
	}

	public void setBonusAmount2(Double bonusAmount2) {
		this.bonusAmount2 = bonusAmount2;
	}

	public Double getBonusAmount3() {
		return bonusAmount3;
	}

	public void setBonusAmount3(Double bonusAmount3) {
		this.bonusAmount3 = bonusAmount3;
	}

	public Double getBonusAmount4() {
		return bonusAmount4;
	}

	public void setBonusAmount4(Double bonusAmount4) {
		this.bonusAmount4 = bonusAmount4;
	}

	public String getBonusTypeName() {
		return bonusTypeName;
	}

	public void setBonusTypeName(String bonusTypeName) {
		this.bonusTypeName = bonusTypeName;
	}

	public String getBonusTypeName0() {
		return bonusTypeName0;
	}

	public void setBonusTypeName0(String bonusTypeName0) {
		this.bonusTypeName0 = bonusTypeName0;
	}

	public String getBonusTypeName1() {
		return bonusTypeName1;
	}

	public void setBonusTypeName1(String bonusTypeName1) {
		this.bonusTypeName1 = bonusTypeName1;
	}

	public String getBonusTypeName2() {
		return bonusTypeName2;
	}

	public void setBonusTypeName2(String bonusTypeName2) {
		this.bonusTypeName2 = bonusTypeName2;
	}

	public String getBonusTypeName3() {
		return bonusTypeName3;
	}

	public void setBonusTypeName3(String bonusTypeName3) {
		this.bonusTypeName3 = bonusTypeName3;
	}

	public String getBonusTypeName4() {
		return bonusTypeName4;
	}

	public void setBonusTypeName4(String bonusTypeName4) {
		this.bonusTypeName4 = bonusTypeName4;
	}

	public String getBonusNotes() {
		return bonusNotes;
	}

	public void setBonusNotes(String bonusNotes) {
		this.bonusNotes = bonusNotes;
	}

	public String getBonusNotes0() {
		return bonusNotes0;
	}

	public void setBonusNotes0(String bonusNotes0) {
		this.bonusNotes0 = bonusNotes0;
	}

	public String getBonusNotes1() {
		return bonusNotes1;
	}

	public void setBonusNotes1(String bonusNotes1) {
		this.bonusNotes1 = bonusNotes1;
	}

	public String getBonusNotes2() {
		return bonusNotes2;
	}

	public void setBonusNotes2(String bonusNotes2) {
		this.bonusNotes2 = bonusNotes2;
	}

	public String getBonusNotes3() {
		return bonusNotes3;
	}

	public void setBonusNotes3(String bonusNotes3) {
		this.bonusNotes3 = bonusNotes3;
	}

	public String getBonusNotes4() {
		return bonusNotes4;
	}

	public void setBonusNotes4(String bonusNotes4) {
		this.bonusNotes4 = bonusNotes4;
	}

	public String getHolidayname() {
		return holidayname;
	}

	public void setHolidayname(String holidayname) {
		this.holidayname = holidayname;
	}

	public String getHolidaydateFrom() {
		return holidaydateFrom;
	}

	public void setHolidaydateFrom(String holidaydateFrom) {
		this.holidaydateFrom = holidaydateFrom;
	}

	public String getHolidaydateTo() {
		return holidaydateTo;
	}

	public void setHolidaydateTo(String holidaydateTo) {
		this.holidaydateTo = holidaydateTo;
	}

	public String getReimburseNotes() {
		return reimburseNotes;
	}

	public void setReimburseNotes(String reimburseNotes) {
		this.reimburseNotes = reimburseNotes;
	}

	public Double getReimburseAmt() {
		return reimburseAmt;
	}

	public void setReimburseAmt(Double reimburseAmt) {
		this.reimburseAmt = reimburseAmt;
	}

	public String getQutarNotes() {
		return qutarNotes;
	}

	public void setQutarNotes(String qutarNotes) {
		this.qutarNotes = qutarNotes;
	}

	public Double getQutarAmt() {
		return qutarAmt;
	}

	public void setQutarAmt(Double qutarAmt) {
		this.qutarAmt = qutarAmt;
	}

	public Double getMiscamt() {
		return miscamt;
	}

	public void setMiscamt(Double miscamt) {
		this.miscamt = miscamt;
	}

	public Double getMiscamt0() {
		return miscamt0;
	}

	public void setMiscamt0(Double miscamt0) {
		this.miscamt0 = miscamt0;
	}

	public Double getMiscamt1() {
		return miscamt1;
	}

	public void setMiscamt1(Double miscamt1) {
		this.miscamt1 = miscamt1;
	}

	public Double getMiscamt2() {
		return miscamt2;
	}

	public void setMiscamt2(Double miscamt2) {
		this.miscamt2 = miscamt2;
	}

	public Double getMiscamt3() {
		return miscamt3;
	}

	public void setMiscamt3(Double miscamt3) {
		this.miscamt3 = miscamt3;
	}

	public Double getMiscamt4() {
		return miscamt4;
	}

	public void setMiscamt4(Double miscamt4) {
		this.miscamt4 = miscamt4;
	}

	public Double getMiscamt5() {
		return miscamt5;
	}

	public void setMiscamt5(Double miscamt5) {
		this.miscamt5 = miscamt5;
	}

	public String getMiscnote() {
		return miscnote;
	}

	public void setMiscnote(String miscnote) {
		this.miscnote = miscnote;
	}

	public String getMiscnote0() {
		return miscnote0;
	}

	public void setMiscnote0(String miscnote0) {
		this.miscnote0 = miscnote0;
	}

	public String getMiscnote1() {
		return miscnote1;
	}

	public void setMiscnote1(String miscnote1) {
		this.miscnote1 = miscnote1;
	}

	public String getMiscnote2() {
		return miscnote2;
	}

	public void setMiscnote2(String miscnote2) {
		this.miscnote2 = miscnote2;
	}

	public String getMiscnote3() {
		return miscnote3;
	}

	public void setMiscnote3(String miscnote3) {
		this.miscnote3 = miscnote3;
	}

	public String getMiscnote4() {
		return miscnote4;
	}

	public void setMiscnote4(String miscnote4) {
		this.miscnote4 = miscnote4;
	}

	public String getMiscnote5() {
		return miscnote5;
	}

	public void setMiscnote5(String miscnote5) {
		this.miscnote5 = miscnote5;
	}
	

	public Double getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(Double deductionAmount) {
		this.deductionAmount = deductionAmount;
	}  
	
	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}
	
	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
	public Double getSumAmount() {
		return sumAmount;
	}
	
	public Integer getTotalRowCount() {
		return totalRowCount;
	}

	public void setSumTotal(Double sumTotal) {
		this.sumTotal = sumTotal;
	}
	
	 
	public Double getSumTotal() {
		return sumTotal;
	}
	
	public Double getSubTotalAmount() {
		return subTotalAmount;
	}
	
	public void setSubTotalAmount(Double subTotalAmount) {
		this.subTotalAmount = subTotalAmount;
	}

	public String getPayRollBatchString() {
		return payRollBatchString;
	}

	public void setPayRollBatchString(String payRollBatchString) {
		this.payRollBatchString = payRollBatchString;
	}

	public String getBillBatchDateFromString() {
		return billBatchDateFromString;
	}

	public void setBillBatchDateFromString(String billBatchDateFromString) {
		this.billBatchDateFromString = billBatchDateFromString;
	}

	public String getBillBatchDateToString() {
		return billBatchDateToString;
	}

	public void setBillBatchDateToString(String billBatchDateToString) {
		this.billBatchDateToString = billBatchDateToString;
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

	public Double getSubTotalAmountSpc() {
		return subTotalAmountSpc;
	}

	public void setSubTotalAmountSpc(Double subTotalAmountSpc) {
		this.subTotalAmountSpc = subTotalAmountSpc;
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

	public Double getTotalAmountSpc() {
		return totalAmountSpc;
	}

	public void setTotalAmountSpc(Double totalAmountSpc) {
		this.totalAmountSpc = totalAmountSpc;
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
	
	public Double getSickPersonalAmount() {
		return sickPersonalAmount;
	}
	
	public void setSickPersonalAmount(Double sickPersonalAmount) {
		this.sickPersonalAmount = sickPersonalAmount;
	}
	
	public int getNoOfLoadtotal() {
		return noOfLoadtotal;
	}
	
	public void setNoOfLoadtotal(int noOfLoadtotal) {
		this.noOfLoadtotal = noOfLoadtotal;
	}
	
	public void setIsMainRow(String isMainRow) {
		this.isMainRow = isMainRow;
	}
	
	public String getIsMainRow() {
		return isMainRow;
	}
	public Long getBaseId() {
		return baseId;
	}
	public void setBaseId(Long baseId) {
		this.baseId = baseId;
	}
	public Integer getChangedStatus() {
		return changedStatus;
	}
	public void setChangedStatus(Integer changedStatus) {
		this.changedStatus = changedStatus;
	}
}
