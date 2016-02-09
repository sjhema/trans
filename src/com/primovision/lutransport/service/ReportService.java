package com.primovision.lutransport.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubcontractorInvoice;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.EztollReportInput;
import com.primovision.lutransport.model.report.EztollReportWrapper;
import com.primovision.lutransport.model.report.FuelDistributionReportInput;
import com.primovision.lutransport.model.report.FuelDistributionReportWrapper;
import com.primovision.lutransport.model.report.FuelLogAverageReportWrapper;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.model.report.FuelLogVerificationReportInput;
import com.primovision.lutransport.model.report.FuelLogVerificationReportWrapper;
import com.primovision.lutransport.model.report.FuelOverLimitInput;
import com.primovision.lutransport.model.report.FuelOverLimitReportWrapper;
import com.primovision.lutransport.model.report.FuelViolationInput;
import com.primovision.lutransport.model.report.FuelViolationReportWrapper;
import com.primovision.lutransport.model.report.NetReportInput;
import com.primovision.lutransport.model.report.NetReportWrapper;
import com.primovision.lutransport.model.report.SubcontractorBillingWrapper;
import com.primovision.lutransport.model.report.SubcontractorReportInput;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.model.report.TollDistributionReportInput;
import com.primovision.lutransport.model.report.TollDistributionReportWrapper;

public interface ReportService {
	public  BillingWrapper  generateBillingData(SearchCriteria criteria);
	public BillingWrapper generateBillingHistoryData(SearchCriteria searchCriteria, BillingHistoryInput input);
	public  SubcontractorBillingWrapper generateSubcontractorBillingData(SearchCriteria criteria);
	public void saveBillingData(HttpServletRequest request,SearchCriteria criteria) throws Exception;
	void deleteBillingData(Invoice invoice) throws Exception;
	public boolean checkDuplicate(Ticket ticket, String ticketType);
	public FuelLogReportWrapper generateFuellogData(SearchCriteria criteria,FuelLogReportInput input, boolean sort);
	public List<FuelLogAverageReportWrapper> generateFuellogAverageData(SearchCriteria criteria,FuelLogReportInput input);
	
	public FuelLogVerificationReportWrapper generateFuelLogVerificationData(SearchCriteria searchCriteria, FuelLogVerificationReportInput input);
	public FuelViolationReportWrapper generateFuelViolationData(SearchCriteria criteria,FuelViolationInput input);
	public FuelOverLimitReportWrapper generateFuelOverLimitData(SearchCriteria criteria,FuelOverLimitInput input);
	public EztollReportWrapper generateEztollData(SearchCriteria criteria, EztollReportInput input);
	public void saveSubcontractorBillingData(HttpServletRequest request,SearchCriteria criteria) throws Exception;
	void deleteSubcontractorBillingData(SubcontractorInvoice invoice) throws Exception;	
	public List<Summary> generateSummary(SearchCriteria criteria, BillingHistoryInput input) ;
	public SubcontractorBillingWrapper generateSubcontractorReportData(SearchCriteria searchCriteria,SubcontractorReportInput input);
	public List<NetReportWrapper> generateNetReportData(SearchCriteria searchCriteria, NetReportInput input,HttpServletRequest request);
	public List<Summary> generateSummaryNew(SearchCriteria criteria,
			BillingHistoryInput input);
	public FuelDistributionReportWrapper generateFuelDistributionData(SearchCriteria searchCriteria, FuelDistributionReportInput input);
	public TollDistributionReportWrapper generateTollDistributionData(SearchCriteria criteria, TollDistributionReportInput input);
}
