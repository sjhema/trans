package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;

import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.MileageLog;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Violation;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.WeeklyPayDetail;
import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.report.BonusQualificationReport;
import com.primovision.lutransport.model.report.BonusQualificationReportInput;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrder;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItem;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderComponent;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItemType;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderReportInput;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@RequestMapping("/reportuser/report/bonusqualifn")
public class BonusQualificationReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public BonusQualificationReportController() {
		setUrlContext("reportuser/report/bonusqualifn");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null && criteria.getSearchMap() != null) {
				criteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		return getUrlContext() + "/start";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/bonusNotQualifiedSearch.do")
	public String bonusNotQualifiedSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") BonusQualificationReportInput input) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		
		Map<String, Object> datas = generateBonusNotQualifiedData(criteria, request, input);
		List<BonusQualificationReport> bonusQualificationReportList = (List<BonusQualificationReport>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("bonusNotQualifiedReport",
					bonusQualificationReportList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/bonusqualifn/"+type+"BonusNotQualifiedReport";
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/bonusQualifiedSearch.do")
	public String bonusQualifiedSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") BonusQualificationReportInput input) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		
		Map<String, Object> datas = generateBonusQualifiedData(criteria, request, input);
		List<BonusQualificationReport> bonusQualificationReportList = (List<BonusQualificationReport>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("bonusQualifiedReport",
					bonusQualificationReportList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/bonusqualifn/"+type+"BonusQualifiedReport";
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/bonusNotQualifiedExport.do")
	public String bonusNotQualifiedExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		BonusQualificationReportInput input = (BonusQualificationReportInput) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateBonusNotQualifiedData(criteria, request, input);
		List<BonusQualificationReport> bonusQualificationReportList = (List<BonusQualificationReport>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "bonusNotQualifiedReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename="+reportName+"." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
							bonusQualificationReportList, params, type, request);
			
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/bonusQualifiedExport.do")
	public String bonusQualifiedExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		BonusQualificationReportInput input = (BonusQualificationReportInput) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateBonusQualifiedData(criteria, request, input);
		List<BonusQualificationReport> bonusQualificationReportList = (List<BonusQualificationReport>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "bonusQualifiedReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename="+reportName+"." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
							bonusQualificationReportList, params, type, request);
			
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Map<String,Object> generateBonusNotQualifiedData(SearchCriteria searchCriteria, 
			HttpServletRequest request, BonusQualificationReportInput input) {
		List<BonusQualificationReport> bonusQualificationReportList = performBonusNotQualifiedSearch(searchCriteria, input);  
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		Location company = retrieveCompany(input.getCompany());
		String companyName = company == null ? StringUtils.EMPTY : company.getName();
		params.put("company", companyName);
		
		String dateRange = StringUtils.isEmpty(input.getDateFrom()) ? StringUtils.EMPTY : input.getDateFrom();
		dateRange += " - ";
		dateRange += StringUtils.isEmpty(input.getDateTo()) ? StringUtils.EMPTY : input.getDateTo();
		params.put("dateRange", dateRange);
		
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", bonusQualificationReportList);
		data.put("params", params);
		     
		return data;
	}
	
	private Map<String,Object> generateBonusQualifiedData(SearchCriteria searchCriteria, 
			HttpServletRequest request, BonusQualificationReportInput input) {
		List<BonusQualificationReport> bonusQualificationReportList = performBonusQualifiedSearch(searchCriteria, input);  
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		Location company = retrieveCompany(input.getCompany());
		String companyName = company == null ? StringUtils.EMPTY : company.getName();
		params.put("company", companyName);
		
		String dateRange = StringUtils.isEmpty(input.getDateFrom()) ? StringUtils.EMPTY : input.getDateFrom();
		dateRange += " - ";
		dateRange += StringUtils.isEmpty(input.getDateTo()) ? StringUtils.EMPTY : input.getDateTo();
		params.put("dateRange", dateRange);
		
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", bonusQualificationReportList);
		data.put("params", params);
		     
		return data;
	}
	
	private Location retrieveCompany(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(Location.class, Long.valueOf(id));
	}
	
	private List<BonusQualificationReport> performBonusNotQualifiedSearch(SearchCriteria criteria, BonusQualificationReportInput input) {
		String company = input.getCompany();
		String driver = input.getDriver();
		String dateFrom = input.getDateFrom();
		String dateTo = input.getDateTo();
		
		List<BonusQualificationReport> bonusQualificationReportList = new ArrayList<BonusQualificationReport>();
		
		List<Accident> accidentList = retrieveAccidents(company, driver, dateFrom, dateTo, "6");
		for (Accident accident : accidentList) {
			BonusQualificationReport bonusQualificationReport = new BonusQualificationReport();
			map(bonusQualificationReport, accident);
			bonusQualificationReportList.add(bonusQualificationReport);
		}
		
		List<Injury> injuryList = retrieveInjuries(company, driver, dateFrom, dateTo);
		for (Injury injury : injuryList) {
			BonusQualificationReport bonusQualificationReport = new BonusQualificationReport();
			map(bonusQualificationReport, injury);
			bonusQualificationReportList.add(bonusQualificationReport);
		}
		
		List<Violation> violationList = retrieveViolations(company, driver, dateFrom, dateTo);
		for (Violation violation : violationList) {
			BonusQualificationReport bonusQualificationReport = new BonusQualificationReport();
			map(bonusQualificationReport, violation);
			bonusQualificationReportList.add(bonusQualificationReport);
		}
		
		sort(bonusQualificationReportList);
		return bonusQualificationReportList;
	}
	
	private List<BonusQualificationReport> performBonusQualifiedSearch(SearchCriteria criteria, BonusQualificationReportInput input) {
		String company = input.getCompany();
		String driverName = input.getDriver();
		
		List<BonusQualificationReport> bonusQualifiedReportList = new ArrayList<BonusQualificationReport>();
		
		List<Driver> driverList = retrieveDrivers(company, driverName);
		if (driverList == null || driverList.isEmpty()) {
			return bonusQualifiedReportList;
		}
		
		List<BonusQualificationReport> bonusNotQualifiedReportList = performBonusNotQualifiedSearch(criteria, input);
		List<String> driverNamesNotQualifiedList = new ArrayList<String>();
		for (BonusQualificationReport aBonusNotQualifiedReport : bonusNotQualifiedReportList) {
			driverNamesNotQualifiedList.add(aBonusNotQualifiedReport.getDriverName());
		}
		
		for (Driver aDriver : driverList) {
			if (StringUtils.contains(aDriver.getFullName(), "Unknown")) {
				continue;
			}
			if (driverNamesNotQualifiedList.contains(aDriver.getFullName())) {
				continue;
			}
			
			BonusQualificationReport aBonusQualifiedReport = new BonusQualificationReport();
			
			List<WeeklyPayDetail> payrollList = retrievePayroll(input, aDriver);
			aBonusQualifiedReport.setNoOfPayChecks(payrollList.size());
			aBonusQualifiedReport.setHiredDate(aDriver.getDateHired());
				
			map(aBonusQualifiedReport, aDriver);
			bonusQualifiedReportList.add(aBonusQualifiedReport);
		}
		
		sort(bonusQualifiedReportList);
		return bonusQualifiedReportList;
	}
	
	private List<WeeklyPayDetail> retrievePayroll(BonusQualificationReportInput input, Driver driver) {
		StringBuffer driverPayQueryBuff = new StringBuffer("select obj from DriverPay obj where 1=1");
		driverPayQueryBuff.append(" and drivername='").append(driver.getFullName()).append("'");
		driverPayQueryBuff.append(" and company=").append(driver.getCompany().getId());
		
		String fromDate = ReportDateUtil.getFromDate(input.getDateFrom());
		driverPayQueryBuff.append(" and payRollBatch >='").append(fromDate).append("'");
		
		String toDate = ReportDateUtil.getFromDate(input.getDateTo());
		driverPayQueryBuff.append(" and payRollBatch <='").append(toDate).append("'");
		
		driverPayQueryBuff.append(" order by obj.payRollBatch desc"); 
	
		List<WeeklyPayDetail> payrollList = genericDAO.executeSimpleQuery(driverPayQueryBuff.toString());
		return payrollList;
	}
	
	
	
	private void map(BonusQualificationReport aBonusQualifiedReport, Driver aDriver) {
		aBonusQualifiedReport.setCompanyName(aDriver.getCompany().getName());
		aBonusQualifiedReport.setDriverName(aDriver.getFullName());
	}
	
	private List<Accident> retrieveAccidents(String company, String driver, String dateFrom, String dateTo,
			String accidentCause) {
		StringBuffer query = new StringBuffer("select obj from Accident obj where 1=1 and obj.driver is not null");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			//whereClause.append(" and obj.driverCompany.id=" + company);
			whereClause.append(" and obj.driver.company.id=" + company);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(dateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(dateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(dateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(dateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(accidentCause)) {
			whereClause.append(" and obj.accidentCause.id !=" + accidentCause);
		}

      query.append(whereClause);
      query.append(" order by obj.incidentDate desc");
      
      List<Accident> accidentList = genericDAO.getEntityManager().createQuery(query.toString()).getResultList();
      
      return accidentList;
	}
	
	private List<Injury> retrieveInjuries(String company, String driver, String dateFrom, String dateTo) {
		StringBuffer query = new StringBuffer("select obj from Injury obj where 1=1 and obj.driver is not null");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.driverCompany.id=" + company);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(dateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(dateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(dateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(dateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
    
      query.append(whereClause);
      query.append(" order by obj.incidentDate desc");
      
      List<Injury> injuryList = genericDAO.getEntityManager().createQuery(query.toString()).getResultList();
      
      return injuryList;
	}
	
	private List<Violation> retrieveViolations(String company, String driver, String dateFrom, String dateTo) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.company.id=" + company);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(dateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(dateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(dateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(dateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
    
      query.append(whereClause);
      query.append(" order by obj.incidentDate desc");
      
      List<Violation> violationList = genericDAO.getEntityManager().createQuery(query.toString()).getResultList();
      return violationList;
	}
	
	private List<Driver> retrieveDrivers(String company, String driver) {
		StringBuffer query = new StringBuffer("select obj from Driver obj where 1=1 and obj.catagory.id=2");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.company.id=" + company);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.fullName='" + driver + "'");
		} else {
			whereClause.append(" and obj.status=1");
		}
    
      query.append(whereClause);
      query.append(" order by obj.fullName asc");
      
      List<Driver> driverList = genericDAO.getEntityManager().createQuery(query.toString()).getResultList();
      
      return driverList;
	}
	
	private void sort(List<BonusQualificationReport> bonusQualificationReportList) {
		if (bonusQualificationReportList == null || bonusQualificationReportList.isEmpty()) {
			return;
		}
		
		Comparator<BonusQualificationReport> companyComparator = new Comparator<BonusQualificationReport>() {
			@Override
			public int compare(BonusQualificationReport o1, BonusQualificationReport o2) {
				if (o1.getCompanyName() == null || o2.getCompanyName() == null) {
					return 0;
				}
				return o1.getCompanyName().compareTo(o2.getCompanyName());
			}
		};
		
		Comparator<BonusQualificationReport> driverComparator = new Comparator<BonusQualificationReport>() {
			@Override
			public int compare(BonusQualificationReport o1, BonusQualificationReport o2) {
				if (o1.getDriverName() == null || o2.getDriverName() == null) {
					return 0;
				}
				return o1.getDriverName().compareTo(o2.getDriverName());
			}
		};
		
		Comparator<BonusQualificationReport> dateComparator = new Comparator<BonusQualificationReport>() {
			@Override
			public int compare(BonusQualificationReport o1, BonusQualificationReport o2) {
				if (o1.getDate() == null || o2.getDate() == null) {
					return 0;
				}
				return o2.getDate().compareTo(o1.getDate());
			}
		};  
		
		ComparatorChain chain = new ComparatorChain(); 
		chain.addComparator(companyComparator);
		chain.addComparator(driverComparator);			
		chain.addComparator(dateComparator);
		
		Collections.sort(bonusQualificationReportList, chain);
	}
	
	private void map(BonusQualificationReport bonusQualificationReport, Accident accident) {
		bonusQualificationReport.setCompanyName(accident.getDriverCompany().getName());
		bonusQualificationReport.setDriverName(accident.getDriverName());
		
		if (accident.getVehicle() != null) {
			bonusQualificationReport.setUnitNum(accident.getVehicle().getUnitNum());
		}
		
		bonusQualificationReport.setDate(accident.getIncidentDate());
		bonusQualificationReport.setAccidentCause(accident.getAccidentCauseStr());
	}
	
	private void map(BonusQualificationReport bonusQualificationReport, Injury injury) {
		bonusQualificationReport.setCompanyName(injury.getDriverCompany().getName());
		bonusQualificationReport.setDriverName(injury.getDriver().getFullName());
		bonusQualificationReport.setDate(injury.getIncidentDate());
		
		if (injury.getIncidentType() != null) {
			bonusQualificationReport.setInjuryType(injury.getIncidentType().getIncidentType());
		}
	}
	
	private void map(BonusQualificationReport bonusQualificationReport, Violation violation) {
		bonusQualificationReport.setCompanyName(violation.getCompany().getName());
		bonusQualificationReport.setDriverName(violation.getDriver().getFullName());
		bonusQualificationReport.setUnitNum(violation.getTruck().getUnitNum());
		bonusQualificationReport.setDate(violation.getIncidentDate());
		bonusQualificationReport.setViolationType(violation.getViolationType());
		
		bonusQualificationReport.setViolation("Y");
		
		String citationNo = violation.getCitationNo();
		if (StringUtils.isNotEmpty(citationNo)) {
			bonusQualificationReport.setCitationNo(citationNo);
			bonusQualificationReport.setCitation("Y");
		} else {
			bonusQualificationReport.setCitation("N");
		}
	}

	public void setupList(ModelMap model, HttpServletRequest request) {
		String query = "select distinct(obj.fullName) from Driver obj where obj.catagory.id=2 order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}
	
	@ModelAttribute("modelObject")
	public BonusQualificationReportInput setupModel(HttpServletRequest request) {
		return new BonusQualificationReportInput();
	}
}
