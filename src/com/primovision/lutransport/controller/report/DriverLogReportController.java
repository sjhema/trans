package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;

import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

import com.primovision.lutransport.model.report.Billing_New;
import com.primovision.lutransport.model.report.DriverLogReport;
import com.primovision.lutransport.model.report.DriverLogReportInput;
import com.primovision.lutransport.model.report.DriverLogReportWrapper;
import com.primovision.lutransport.model.report.EztollReportInput;
import com.primovision.lutransport.model.report.EztollReportWrapper;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;

import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/driverlogreport")
public class DriverLogReportController extends BaseController {
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public DriverLogReportController() {
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null) {
			if (searchCriteria.getSearchMap() != null) {
				searchCriteria.getSearchMap().clear();
			}
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		
		
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class, 2l);
		criterias.put("catagory", empobj);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		
		return "reportuser/report/driverlogreportForm";
	}
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria, HttpServletRequest request,
			DriverLogReportInput input) {
		DriverLogReportWrapper wrapper = generateDriverLogReport(searchCriteria, input);
		 
		Map<String, Object> params = new HashMap<String, Object>();
		String transactionDateRange = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(input.getTransactionDateFrom())) {
			transactionDateRange = input.getTransactionDateFrom() + " - " + input.getTransactionDateTo();
		}
		params.put("transactionDateRange", transactionDateRange);
		
		String company = StringUtils.isEmpty(input.getCompany()) ? StringUtils.EMPTY 
					: constructLocationNames(input.getCompany(), false);
		params.put("company", company);
		
		String terminal = StringUtils.isEmpty(input.getTerminal()) ? StringUtils.EMPTY 
				: constructLocationNames(input.getTerminal(), false);
		params.put("terminal", terminal);
		
		params.put("driver", StringUtils.isEmpty(input.getDriver()) ? StringUtils.EMPTY : input.getDriver());
		  
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", wrapper.getDriverLogs());
		data.put("params", params);
		     
		return data;
	}
	
	public DriverLogReportWrapper generateDriverLogReport(SearchCriteria searchCriteria, DriverLogReportInput input) {
		List<DriverLogReport> driverLogs = new ArrayList<DriverLogReport>();
		DriverLogReportWrapper driverLogReportWrapper = new DriverLogReportWrapper();
		driverLogReportWrapper.setDriverLogs(driverLogs);
		
		EztollReportInput eztollReportInput = new EztollReportInput();
		map(eztollReportInput, input);
		EztollReportWrapper eztollReportWrapper = reportService.generateEztollData(searchCriteria, eztollReportInput, true);
		map(driverLogReportWrapper, eztollReportWrapper);
		
		FuelLogReportInput fuelLogReportInput = new FuelLogReportInput();
		map(fuelLogReportInput, input);
		FuelLogReportWrapper fuelLogReportWrapper = reportService.generateFuellogData(searchCriteria, fuelLogReportInput, true);
		map(driverLogReportWrapper, fuelLogReportWrapper);
		
		List<Billing_New> invoiceDetailsList = retrieveInvoiceDetails(input);
		map(driverLogReportWrapper, invoiceDetailsList);
		
		sort(driverLogReportWrapper.getDriverLogs());
		
		return driverLogReportWrapper;
	}
	
	private void sort(List<DriverLogReport> driverLogList) {
		if (driverLogList == null || driverLogList.isEmpty()) {
			return;
		}
		
		Comparator<DriverLogReport> driverComparator = new Comparator<DriverLogReport>() {
			@Override
			public int compare(DriverLogReport o1, DriverLogReport o2) {
				if (StringUtils.isEmpty(o1.getDriver()) || StringUtils.isEmpty(o2.getDriver())) {
					return 0;
				}
				return o1.getDriver().compareTo(o2.getDriver());
			}
		};
		
		Comparator<DriverLogReport> transactionDateComparator = new Comparator<DriverLogReport>() {
			@Override
			public int compare(DriverLogReport o1, DriverLogReport o2) {
				if (o1.getTransactionDate() == null || o2.getTransactionDate() == null) {
					return 0;
				}
				return o1.getTransactionDate().compareTo(o2.getTransactionDate());
			}
		};  
		
		Comparator<DriverLogReport> transactionTimeComparator = new Comparator<DriverLogReport>() {
			@Override
			public int compare(DriverLogReport o1, DriverLogReport o2) {
				if (StringUtils.isEmpty(o1.getTransactionTime()) || StringUtils.isEmpty(o2.getTransactionTime())) {
					return 0;
				}
				return o1.getTransactionTime().compareTo(o2.getTransactionTime());
			}
		};  
		
		ComparatorChain chain = new ComparatorChain(); 
		chain.addComparator(driverComparator);
		chain.addComparator(transactionDateComparator);
		chain.addComparator(transactionTimeComparator);
		
		Collections.sort(driverLogList, chain);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") DriverLogReportInput input, 
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPageSize(1000);
		
		String p = request.getParameter("p");
		if (p == null) {
			request.getSession().setAttribute("input", input);
		}
		
		String reportName = "driverLogReport";
		DriverLogReportInput input1 = (DriverLogReportInput) request.getSession().getAttribute("input");
		try {
			Map<String, Object> datas; 
			if (p == null) {
				 datas = generateData(searchCriteria, request, input);
		   } else {
				 datas = generateData(searchCriteria, request, input1);	
			}
			
		   if (StringUtils.isEmpty(type)) {
				type = "html";
		   }
			response.setContentType(MimeUtil.getContentType(type));
			
			if (!type.equals("html")) {
				response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
			}
			
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			List dataList = (List) datas.get("data");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					dataList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/driverlogreport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPageSize(15000);
		searchCriteria.setPage(0);
		
		DriverLogReportInput input = (DriverLogReportInput)request.getSession().getAttribute("input");
		ByteArrayOutputStream out = null;
		String reportName = "driverLogReport";
		try {
			Map<String, Object> reportData = generateData(searchCriteria, request, input);
			
			if (StringUtils.isEmpty(type)) {
				type = "xlsx";
			}
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			
			if (StringUtils.equals("print", type)) {
				reportName += "print";
			} 
			
			Map<String, Object> params = (Map<String, Object>) reportData.get("params");
			List dataList = (List) reportData.get("data");
			
			out = dynamicReportService.generateStaticReport(reportName, dataList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file: " + e);
			
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@ModelAttribute("modelObject")
	public DriverLogReportInput setupModel(HttpServletRequest request) {
		return new DriverLogReportInput();
	}
	
	private void map(EztollReportInput eztollReportInput, DriverLogReportInput driverLogReportInput) {
		eztollReportInput.setCompany(driverLogReportInput.getCompany());
		eztollReportInput.setTerminal(driverLogReportInput.getTerminal());
		
		eztollReportInput.setDriver(retrieveDriverIds(driverLogReportInput.getDriver()));
		
		eztollReportInput.setTransferDateFrom(driverLogReportInput.getTransactionDateFrom());
		eztollReportInput.setTransferDateTo(driverLogReportInput.getTransactionDateTo());
	}
	
	private void map(FuelLogReportInput fuelLogReportInput, DriverLogReportInput driverLogReportInput) {
		fuelLogReportInput.setCompany(driverLogReportInput.getCompany());
		fuelLogReportInput.setTerminal(fuelLogReportInput.getTerminal());
		
		fuelLogReportInput.setDriver(driverLogReportInput.getDriver());
		
		fuelLogReportInput.setTransactionDateFrom(driverLogReportInput.getTransactionDateFrom());
		fuelLogReportInput.setTransactionDateTo(driverLogReportInput.getTransactionDateTo());
	}
	
	private void map(DriverLogReportWrapper driverLogReportWrapper, EztollReportWrapper eztollReportWrapper) {
		List<EzToll> ezTollList = eztollReportWrapper.getEztolls();
		if (ezTollList == null || ezTollList.isEmpty()) {
			return;
		}
		
		List<DriverLogReport> driverLogs = driverLogReportWrapper.getDriverLogs();
		for (EzToll anEZToll : ezTollList) {
			DriverLogReport aDriverLogReport = new DriverLogReport();
			map(aDriverLogReport, anEZToll);
			driverLogs.add(aDriverLogReport);
		}
	}
	
	private void map(DriverLogReport aDriverLogReport, EzToll anEZToll) {
		aDriverLogReport.setCompany(anEZToll.getCompanies());
		aDriverLogReport.setTerminal(anEZToll.getTerminals());
		
		aDriverLogReport.setVendor(anEZToll.getTollcompanies());
		aDriverLogReport.setAgency(anEZToll.getAgency());
		
		aDriverLogReport.setDriver(anEZToll.getDrivername());
		
		aDriverLogReport.setUnit(anEZToll.getUnits());
		aDriverLogReport.setTollTagNumber(anEZToll.getTollTagNumbers());
		
		aDriverLogReport.setTransactionDateStr(anEZToll.getTransfersDate());
		aDriverLogReport.setTransactionTime(anEZToll.getTransactiontime());
		
		aDriverLogReport.setDriverPayAmount(anEZToll.getAmount());
	}
	
	private void map(DriverLogReport aDriverLogReport, FuelLog aFuelLog) {
		aDriverLogReport.setCompany(aFuelLog.getCompanies());
		aDriverLogReport.setTerminal(aFuelLog.getTerminals());
		
		aDriverLogReport.setVendor(aFuelLog.getFuelVenders());
		
		aDriverLogReport.setDriver(aFuelLog.getDrivers());
		
		aDriverLogReport.setUnit(aFuelLog.getUnits());
		
		aDriverLogReport.setTransactionDateStr(aFuelLog.getTransactionsDate());
		aDriverLogReport.setTransactionTime(aFuelLog.getTransactiontime());
		
		aDriverLogReport.setGrossCost(aFuelLog.getGrosscost());
		aDriverLogReport.setFees(aFuelLog.getFees());
		aDriverLogReport.setDriverPayAmount(aFuelLog.getAmount());
		
		aDriverLogReport.setRateUnitPrice(aFuelLog.getUnitprice());
		aDriverLogReport.setTonsGallons(aFuelLog.getGallons());
		
		aDriverLogReport.setDiscount(aFuelLog.getDiscounts());
		
		aDriverLogReport.setCustomerCity(aFuelLog.getCity());
		aDriverLogReport.setState(aFuelLog.getStates());
	}
	
	private void map(DriverLogReportWrapper driverLogReportWrapper, FuelLogReportWrapper fuelLogReportWrapper) {
		List<FuelLog> fuelLogList = fuelLogReportWrapper.getFuellog();
		if (fuelLogList == null || fuelLogList.isEmpty()) {
			return;
		}
		
		List<DriverLogReport> driverLogs = driverLogReportWrapper.getDriverLogs();
		for (FuelLog aFuelLog : fuelLogList) {
			DriverLogReport aDriverLogReport = new DriverLogReport();
			map(aDriverLogReport, aFuelLog);
			driverLogs.add(aDriverLogReport);
		}
	}
	
	private void map(DriverLogReport aDriverLogReport, Billing_New anInvoice) {
		aDriverLogReport.setCompany(anInvoice.getCompany());
		aDriverLogReport.setTerminal(anInvoice.getTerminal());
		
		aDriverLogReport.setDriver(anInvoice.getDriver());
		
		aDriverLogReport.setUnit(anInvoice.getT_unit());
		aDriverLogReport.setTrailer(anInvoice.getTrailer());
		
		aDriverLogReport.setOrigin(anInvoice.getT_origin());
		aDriverLogReport.setDestination(anInvoice.getT_destination());
		
		aDriverLogReport.setTransferDate(anInvoice.getLoaded());
		aDriverLogReport.setTransactionDate(anInvoice.getUnloadDate());
		aDriverLogReport.setTransactionDateStr(anInvoice.getUnloaded());
		
		aDriverLogReport.setTransferTimeIn(anInvoice.getTransferTimeIn());
		aDriverLogReport.setTransferTimeOut(anInvoice.getTransferTimeOut());
		aDriverLogReport.setLandfillTimeIn(anInvoice.getLandfillTimeIn());
		aDriverLogReport.setTransactionTime(anInvoice.getLandfillTimeOut());
		
		aDriverLogReport.setGrossCost(anInvoice.getAmount());
		aDriverLogReport.setFees(anInvoice.getFuelSurcharge());
		aDriverLogReport.setDriverPayAmount(anInvoice.getDriverPayRate());
		aDriverLogReport.setTotalAmount(anInvoice.getTotAmt());
		
		aDriverLogReport.setRateUnitPrice(anInvoice.getRate());
		aDriverLogReport.setTonsGallons(anInvoice.getEffectiveTonsWt());
		
		aDriverLogReport.setCustomerCity(anInvoice.getCustomer());
	}
	
	private void map(DriverLogReportWrapper driverLogReportWrapper, List<Billing_New> invoiceList) {
		if (invoiceList == null || invoiceList.isEmpty()) {
			return;
		}
		
		List<DriverLogReport> driverLogs = driverLogReportWrapper.getDriverLogs();
		for (Billing_New anInvoice : invoiceList) {
			DriverLogReport aDriverLogReport = new DriverLogReport();
			map(aDriverLogReport, anInvoice);
			driverLogs.add(aDriverLogReport);
		}
	}
	
	private List<Billing_New> retrieveInvoiceDetails(DriverLogReportInput driverLogReportInput) {
		String company = driverLogReportInput.getCompany();
		String terminal = driverLogReportInput.getTerminal();
		String driver = driverLogReportInput.getDriver();
		
		String transactionDateFrom = driverLogReportInput.getTransactionDateFrom();
		String transactionDateTo = driverLogReportInput.getTransactionDateTo();
		
		StringBuffer queryBuff = new StringBuffer("select obj from Billing_New obj where 1=1 ");
		if (StringUtils.isNotEmpty(company)) {
			queryBuff.append(" and obj.company in (" + constructLocationNames(company, true) + ")");
		}
		if (StringUtils.isNotEmpty(terminal)) {
			queryBuff.append(" and obj.terminal in (" + constructLocationNames(terminal, true) + ")");
		}
		
		if (StringUtils.isNotEmpty(driver)) {
			queryBuff.append(" and obj.driver in (" + constructDriverNamesForQuery(driver) + ")");
		}
		
		if (StringUtils.isNotEmpty(transactionDateFrom)
				&& StringUtils.isNotEmpty(transactionDateTo)) {
			transactionDateFrom = ReportDateUtil.getFromDate(transactionDateFrom);
			transactionDateTo = ReportDateUtil.getToDate(transactionDateTo);
			queryBuff.append(" and obj.unloadDate >= '" + transactionDateFrom 
			+ "' and obj.unloadDate <='" + transactionDateTo + "'");
		}
		
		List<Billing_New> invoiceDetailsList = genericDAO.executeSimpleQuery(queryBuff.toString());
		return invoiceDetailsList;
	}
	
	private String retrieveDriverIds(String driverNames) {
		if (StringUtils.isEmpty(driverNames)) {
			return StringUtils.EMPTY;
		}
		
		String driverNamesForQuery = constructDriverNamesForQuery(driverNames);
		
		List<Driver> driverList = genericDAO.executeSimpleQuery(
				"select obj from Driver obj where obj.fullName in ("+driverNamesForQuery+")");
		if (driverList == null || driverList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String driverIds = StringUtils.EMPTY;
		for (Driver aDriver : driverList) {
			driverIds += (aDriver.getId().toString() + ",");
		}
		driverIds = driverIds.substring(0, driverIds.length()-1);
			
		return driverIds;
	}
	
	private String constructDriverNamesForQuery(String driverNames) {
		if (StringUtils.isEmpty(driverNames)) {
			return StringUtils.EMPTY;
		}
		
		String driverNamesForQuery = StringUtils.EMPTY;
		String[] driverNameTokens = driverNames.split(",");
		for (int i = 0; i < driverNameTokens.length; i++) {
			driverNamesForQuery += "'"+driverNameTokens[i]+"',";
		}
		driverNamesForQuery = driverNamesForQuery.substring(0, driverNamesForQuery.length()-1);
		return driverNamesForQuery;
	}
	
	private String constructLocationNames(String locationIds, boolean forQuery) {
		if (StringUtils.isEmpty(locationIds)) {
			return StringUtils.EMPTY;
		}
		
		List<Location> locationList = retrieveLocations(locationIds);
		if (locationList == null || locationList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String locationNames = StringUtils.EMPTY;
		String stringMarker = forQuery ? "'" : StringUtils.EMPTY;
		for (Location aLocation : locationList) {
			locationNames += (stringMarker + aLocation.getName() + stringMarker + ", ");
		}
		locationNames = locationNames.substring(0, locationNames.length()-2);
		
		return locationNames;
	}
	
	private List<Location> retrieveLocations(String locationIds) {
		if (StringUtils.isEmpty(locationIds)) {
			return null;
		}
		
		List<Location> locationList = genericDAO.findByCommaSeparatedIds(Location.class, locationIds);
		return locationList;
	}
}
