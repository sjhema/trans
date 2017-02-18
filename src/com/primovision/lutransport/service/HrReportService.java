package com.primovision.lutransport.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.HourlyPayrollInvoice;
import com.primovision.lutransport.model.hrreport.DriverPayHistoryInput;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayroll;
import com.primovision.lutransport.model.hrreport.EmployeeBonusInput;
import com.primovision.lutransport.model.hrreport.EmployeeBonusWrapper;
import com.primovision.lutransport.model.hrreport.EmployeeInput;
import com.primovision.lutransport.model.hrreport.EmployeePayrollInput;
import com.primovision.lutransport.model.hrreport.EmployeePayrollWrapper;
import com.primovision.lutransport.model.hrreport.EmployeeWrapper;
import com.primovision.lutransport.model.hrreport.LeaveAccrualReport;
import com.primovision.lutransport.model.hrreport.PayChexDetail;
import com.primovision.lutransport.model.hrreport.ProbationReportInput;
import com.primovision.lutransport.model.hrreport.PtodApplicationInput;
import com.primovision.lutransport.model.hrreport.RemainingLeaveInput;
import com.primovision.lutransport.model.hrreport.SalaryDetail;
import com.primovision.lutransport.model.hrreport.TimeSheetInput;
import com.primovision.lutransport.model.hrreport.TimeSheetWrapper;
import com.primovision.lutransport.model.hrreport.WeeklyPay;
import com.primovision.lutransport.model.hrreport.WeeklypayWrapper;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.EztollReportInput;
import com.primovision.lutransport.model.report.EztollReportWrapper;

/**
 * @author kishor
 *
 */
public interface HrReportService {
	
	public DriverPayWrapper generateDriverPayReport(SearchCriteria criteria);
	public DriverPayWrapper generateDriverPayReport2(SearchCriteria criteria);
	public EmployeeWrapper generateEmployeeReportData(SearchCriteria criteria,EmployeeInput input);
	public TimeSheetWrapper generateTimeSheetData(SearchCriteria criteria,TimeSheetInput input);
	public  EmployeePayrollWrapper  generateHourlyPayrollData(SearchCriteria criteria);
	
	public void saveHourlyPayrollData(HttpServletRequest request,SearchCriteria criteria) throws Exception;
	public EmployeePayrollWrapper generateEmployeePayrollData(SearchCriteria criteria,EmployeePayrollInput input);
	public List<RemainingLeaveInput> generateRemainingLeaveReport(SearchCriteria criteria,RemainingLeaveInput input);
	public List<LeaveAccrualReport> generateLeaveAccrualReport(SearchCriteria criteria, LeaveAccrualReport input);
	public void saveDriverPayData(HttpServletRequest request,SearchCriteria criteria) throws Exception;
	void deleteDriverPayData(DriverPayroll payroll) throws Exception;
	public List<PtodApplicationInput> generatePtodApplicationReport(SearchCriteria criteria,PtodApplicationInput input);
	public EmployeeBonusWrapper generateEmployeeBonusData(SearchCriteria criteria,EmployeeBonusInput input);
	public List<ProbationReportInput> generateProbationReport(SearchCriteria criteria,ProbationReportInput input);
	public DriverPayWrapper generateDriverPayHistory(SearchCriteria criteria,DriverPayHistoryInput input);
	public DriverPayWrapper generateDriverPayHistoryDetail(SearchCriteria criteria,DriverPayHistoryInput input);
	void deleteEmployeePayrollData(HourlyPayrollInvoice invoice) throws Exception;
	//public WeeklypayWrapper generateWeeklyPayrollData(SearchCriteria criteria);
	//public void saveWeeklyPayData(HttpServletRequest request,SearchCriteria criteria) throws Exception;
	void deleteWeeklyPayrollData(WeeklyPay pay) throws Exception;
	public List<PayChexDetail> generatePaychexData(SearchCriteria criteria);
	public List<SalaryDetail> generateSalaryData(SearchCriteria criteria);

	public WeeklypayWrapper generateWeeklyPayrollData(SearchCriteria criteria,String from);
	public void saveWeeklyPayData(HttpServletRequest request,SearchCriteria criteria,String from) throws Exception;
	
}
