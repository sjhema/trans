package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

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

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.MileageLog;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.IFTAReportInput;
import com.primovision.lutransport.model.report.IFTAReportWrapper;
import com.primovision.lutransport.model.report.MileageLogReportInput;
import com.primovision.lutransport.model.report.MileageLogReportWrapper;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/mileagelog")
public class MileageLogReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public MileageLogReportController() {
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null && criteria.getSearchMap() != null) {
			criteria.getSearchMap().clear();
		}
		
		Map criterias = new HashMap();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 1);
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		List<SubContractor> subConList = retrieveOwnerOpSubContractors();
		model.addAttribute("subcontractors", subConList);
		
		return "reportuser/report/mileageLogReport";
	}
	
	private List<SubContractor> retrieveOwnerOpSubContractors() {
		String query = "select obj from SubContractor obj where obj.ownerOp='Yes'";
		query += " order by obj.name asc";
		
		List<SubContractor> subConList = genericDAO.executeSimpleQuery(query);
		return subConList;
	}
	
	private void populateParams(Map<String,Object> params, HttpServletRequest request, 
			MileageLogReportWrapper wrapper) {
		params.put("totalRows", wrapper.getTotalRows());
		params.put("totalMiles", wrapper.getTotalMiles());
		
		String companies = "-";
		if (!StringUtils.isEmpty(wrapper.getCompanies())) {
			companies = retrieveCompanyNames(wrapper.getCompanies());
		}
		params.put("companies", companies);
		
		String states = "-";
		if (!StringUtils.isEmpty(wrapper.getStates())) {
			states = retrieveStateNames(wrapper.getStates());
		}
		params.put("states", states);
		
		String period = "-";
		if (StringUtils.isNotEmpty(wrapper.getPeriodFrom()) && StringUtils.isNotEmpty(wrapper.getPeriodTo())) {
			period = wrapper.getPeriodFrom() + " - " + wrapper.getPeriodTo();
		} else if (StringUtils.isNotEmpty(wrapper.getLastInStateFrom()) && StringUtils.isNotEmpty(wrapper.getLastInStateTo())) {
			period = wrapper.getLastInStateFrom() + " - " + wrapper.getLastInStateTo();
		}
		params.put("period", period);
		
		populateDrillDownReportParams(params, request);
	}
	
	private void populateDrillDownReportParams(Map<String,Object> params, HttpServletRequest request) {
		String requestUrl = request.getRequestURL().toString();
		String searchAction = StringUtils.substringAfterLast(requestUrl, "/");
		
		String mileageLogDetailsDrillDownReportUrl = StringUtils.replace(requestUrl, searchAction, "mileageLogDetailsDrillDownReport.do");
		params.put("mileageLogDetailsDrillDownReportUrl", mileageLogDetailsDrillDownReportUrl);
	}
	
	private Map<String,Object> generateData(SearchCriteria searchCriteria, HttpServletRequest request, MileageLogReportInput input) {
		MileageLogReportWrapper wrapper = generateMileageLogReport(searchCriteria, input);
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		 
		populateParams(params, request, wrapper);
		  
		data.put("data", wrapper.getMileageLogList());
		data.put("params", params);
		  
		return data;
	}
	
	private Map<String, Object> generateOwnerOpSubConMileageData(SearchCriteria searchCriteria, HttpServletRequest request, MileageLogReportInput input) {
		MileageLogReportWrapper wrapper = generateOwnerOpSubconMileageLogReport(searchCriteria, input);
		if (wrapper == null) {
			return null;
		}
		
		Map<String, Object> data = new HashMap<String,Object>();
		Map<String, Object> params = new HashMap<String,Object>();
		 
		populateParams(params, request, wrapper);
		  
		data.put("data", wrapper.getMileageLogList());
		data.put("params", params);
		  
		return data;
	}
	
	private Map<String,Object> generateIFTAData(SearchCriteria searchCriteria, HttpServletRequest request, IFTAReportInput input) {
		IFTAReportWrapper wrapper = generateIFTAReport(searchCriteria, input);
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		 
		params.put("totalRows", wrapper.getTotalRows());
		params.put("totalMiles", wrapper.getTotalMiles());
		params.put("totalGallons", wrapper.getTotalGallons());
		
		String companies = "-";
		if (!StringUtils.isEmpty(wrapper.getCompanies())) {
			companies = retrieveCompanyNames(wrapper.getCompanies());
		}
		params.put("companies", companies);
		
		String states = "-";
		if (!StringUtils.isEmpty(wrapper.getStates())) {
			states = retrieveStateNames(wrapper.getStates());
		}
		params.put("states", states);
		
		String period = "-";
		if (StringUtils.isNotEmpty(wrapper.getPeriodFrom()) && StringUtils.isNotEmpty(wrapper.getPeriodTo())) {
			period = wrapper.getPeriodFrom() + " - " + wrapper.getPeriodTo();
		} else if (StringUtils.isNotEmpty(wrapper.getLastInStateFrom()) && StringUtils.isNotEmpty(wrapper.getLastInStateTo())) {
			period = wrapper.getLastInStateFrom() + " - " + wrapper.getLastInStateTo();
		}
		params.put("period", period);
		
		populateDrillDownReportParams(params, request);
		  
		data.put("data", wrapper.getIftaReportList());
		data.put("params", params);
		  
		return data;
	}
	
	private Map<String,Object> generateMPGData(SearchCriteria searchCriteria, HttpServletRequest request, IFTAReportInput input) {
		IFTAReportWrapper wrapper = generateMPGReport(searchCriteria, input);
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		 
		params.put("totalRows", wrapper.getTotalRows());
		params.put("totalMiles", wrapper.getTotalMiles());
		params.put("totalGallons", wrapper.getTotalGallons());
		
		String companies = "-";
		if (!StringUtils.isEmpty(wrapper.getCompanies())) {
			companies = retrieveCompanyNames(wrapper.getCompanies());
		}
		params.put("companies", companies);
		
		String states = "-";
		if (!StringUtils.isEmpty(wrapper.getStates())) {
			states = retrieveStateNames(wrapper.getStates());
		}
		params.put("states", states);
		
		String period = "-";
		if (StringUtils.isNotEmpty(wrapper.getPeriodFrom()) && StringUtils.isNotEmpty(wrapper.getPeriodTo())) {
			period = wrapper.getPeriodFrom() + " - " + wrapper.getPeriodTo();
		} else if (StringUtils.isNotEmpty(wrapper.getLastInStateFrom()) && StringUtils.isNotEmpty(wrapper.getLastInStateTo())) {
			period = wrapper.getLastInStateFrom() + " - " + wrapper.getLastInStateTo();
		}
		params.put("period", period);
		
		populateDrillDownReportParams(params, request);
		  
		data.put("data", wrapper.getIftaReportList());
		data.put("params", params);
		  
		return data;
	}
	
	private String retrieveCompanyNames(String companyIds) {
		if (StringUtils.isEmpty(companyIds)) {
			return StringUtils.EMPTY;
		}
		
		String companyQuery = "select obj from Location obj where obj.id in (" 
	   		+ companyIds
	   		+")";
		
		List<Location> locationList = genericDAO.executeSimpleQuery(companyQuery);
		if (locationList == null || locationList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String comanyNames = StringUtils.EMPTY;
		for (Location aLocation : locationList) {
			comanyNames = comanyNames + ", " + aLocation.getName();
		}
		
		return comanyNames.substring(2);
	}
	
	private String retrieveStateNames(String stateIds) {
		if (StringUtils.isEmpty(stateIds)) {
			return StringUtils.EMPTY;
		}
		
		String companyQuery = "select obj from State obj where obj.id in (" 
	   		+ stateIds
	   		+")";
		
		List<State> stateList = genericDAO.executeSimpleQuery(companyQuery);
		if (stateList == null || stateList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String stateNames = StringUtils.EMPTY;
		for (State aState : stateList) {
			stateNames = stateNames + ", " + aState.getName();
		}
		
		return stateNames.substring(2);
	}
	
	public MileageLogReportWrapper generateMileageLogReport(SearchCriteria searchCriteria, MileageLogReportInput input) {
		return reportService.generateMileageLogData(searchCriteria, input);
	}
	
	public MileageLogReportWrapper generateOwnerOpSubconMileageLogReport(SearchCriteria searchCriteria, MileageLogReportInput input) {
		return reportService.generateOwnerOpSubConMileageLogData(searchCriteria, input);
	}
	
	public IFTAReportWrapper generateIFTAReport(SearchCriteria searchCriteria, IFTAReportInput input) {
		return reportService.generateIFTAData(searchCriteria, input);
	}
	
	public IFTAReportWrapper generateMPGReport(SearchCriteria searchCriteria, IFTAReportInput input) {
		return reportService.generateMPGData(searchCriteria, input);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") MileageLogReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
      System.out.println("\nMileageLogReportController==Search()==type===>"+type+"\n"); 
		
      populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
      
      Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		String p=request.getParameter("p");
		if (p == null) {
			request.getSession().setAttribute("input", input);
		}
		
		MileageLogReportInput input1 = (MileageLogReportInput)request.getSession().getAttribute("input");
		try {
			Map<String, Object> datas; 
			if (p == null) {
				 datas = generateData(criteria, request, input);
		   } else {
				 datas = generateData(criteria, request, input1);	
			}
			
		   if (StringUtils.isEmpty(type)) {
				type = "html";
		   }
			response.setContentType(MimeUtil.getContentType(type));
			
			if (!type.equals("html")) {
				response.setHeader("Content-Disposition",
						"attachment;filename=mileageLogReport." + type);
			}
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			
			String reportType = "mileageLogTotalsReport";
			if (MileageLogReportInput.REPORT_TYPE_DETAILS.equals(input.getReportType())) {
				reportType = "mileageLogDetailsReport";
			} 
			
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportType,
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/mileagelogreport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/ownerOpSubConSearch.do")
	public String ownerOpSubConSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") MileageLogReportInput input, 
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
      System.out.println("\nMileageLogReportController==ownerOpSubConSearch()==type===>"+type+"\n"); 
		
      populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
      
      Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		String p = request.getParameter("p");
		if (p == null) {
			request.getSession().setAttribute("input", input);
		}
		
		MileageLogReportInput input1 = (MileageLogReportInput)request.getSession().getAttribute("input");
		try {
			Map<String, Object> datas; 
			if (p == null) {
				 datas = generateOwnerOpSubConMileageData(criteria, request, input);
		   } else {
				 datas = generateOwnerOpSubConMileageData(criteria, request, input1);	
			}
			
			if (datas == null) {
				request.getSession().setAttribute("error", "No onwer operator subcontrators found!!");
				return "reportuser/report/mileageLogReport";
			}
			
		   if (StringUtils.isEmpty(type)) {
				type = "html";
		   }
			response.setContentType(MimeUtil.getContentType(type));
			
			String reportType = "ownerOpSubConMileageReport";
			if (!type.equals("html")) {
				response.setHeader("Content-Disposition",
						"attachment;filename="+reportType+"." + type);
			}
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			List<MileageLog> mielageLogList = (List<MileageLog>) datas.get("data");
			
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportType,
					mielageLogList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/mileagelogreport/"+type+reportType;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET}, value = "/mileageLogDetailsDrillDownReport.do")
	public String processMileageLogDetailsDrilldownReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false) String company,
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String unit) throws IOException {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPageSize = criteria.getPageSize();
		int origPage = criteria.getPage();
		
		criteria.setPageSize(1000);
		criteria.setPage(0);
		
		MileageLogReportInput input = (MileageLogReportInput) request.getSession().getAttribute("input");
		String origReportType = input.getReportType();
		input.setReportType(MileageLogReportInput.REPORT_TYPE_DETAILS);
		
		input.setDrillDownCompany(company);
		input.setDrillDownState(state);
		input.setDrillDownUnit(unit);
		
		if (StringUtils.isEmpty(type)) {
			type = "csv";
		}
     	
		String reportName = "mileageLogDetailsDrilldownReport";
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> datas = generateData(criteria, request, input);
			List<MileageLog> mileageLogList = (List<MileageLog>) datas.get("data");
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + reportName + "." + type);
			}
			
			if (type.equals("pdf")) {
				reportName += "pdf";
			}
			
			out = dynamicReportService.generateStaticReport(reportName,
					mileageLogList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create mileage log drilldown report :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		} finally {
			criteria.setPageSize(origPageSize);
			criteria.setPage(origPage);
			
			resetDrillDownCriteria(input, origReportType);
			
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
	}
	
	private void resetDrillDownCriteria(MileageLogReportInput input, String origReportType) {
		input.setReportType(origReportType);
		input.setDrillDownCompany(StringUtils.EMPTY);
		input.setDrillDownState(StringUtils.EMPTY);
		input.setDrillDownUnit(StringUtils.EMPTY);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/iftaSearch.do")
	public String iftaSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") MileageLogReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nMileageLogReportController==iftaSearch()==type===>"+type+"\n"); 
		
      populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
      
      Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		String p = request.getParameter("p");
		if (p == null) {
			request.getSession().setAttribute("input", input);
		}
		
		MileageLogReportInput input1 = (MileageLogReportInput) request.getSession().getAttribute("input");
		
		try {
			IFTAReportInput iftaReportInput = new IFTAReportInput();
			if (p == null) {
				map(iftaReportInput, input);
		   } else {
				map(iftaReportInput, input1);
			}
			
			Map<String, Object> datas = generateIFTAData(criteria, request, iftaReportInput);
			
		   if (StringUtils.isEmpty(type)) {
				type = "html";
		   }
			response.setContentType(MimeUtil.getContentType(type));
			
			String reportType = "iftaReport";
			
			if (!type.equals("html")) {
				response.setHeader("Content-Disposition", "attachment;filename="+reportType+"." + type);
			}
			
			Map params = (Map)datas.get("params");
			
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportType,
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/mileagelogreport/"+type+reportType;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/mpgSearch.do")
	public String mpgSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") MileageLogReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nMileageLogReportController==mpgSearch()==type===>"+type+"\n"); 
		
      populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
      
      Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		String p = request.getParameter("p");
		if (p == null) {
			request.getSession().setAttribute("input", input);
		}
		
		MileageLogReportInput input1 = (MileageLogReportInput) request.getSession().getAttribute("input");
		
		try {
			IFTAReportInput iftaReportInput = new IFTAReportInput();
			if (p == null) {
				map(iftaReportInput, input);
		   } else {
				map(iftaReportInput, input1);
			}
			
			Map<String, Object> datas = generateMPGData(criteria, request, iftaReportInput);
			
		   if (StringUtils.isEmpty(type)) {
				type = "html";
		   }
			response.setContentType(MimeUtil.getContentType(type));
			
			String reportType = "mpgReport";
			
			if (!type.equals("html")) {
				response.setHeader("Content-Disposition", "attachment;filename="+reportType+"." + type);
			}
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportType,
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/mileagelogreport/"+type+reportType;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	 
	private void map(IFTAReportInput iftaReportInput, MileageLogReportInput mileageLogReportInput) {
		iftaReportInput.setCompany(mileageLogReportInput.getCompany());
		iftaReportInput.setState(mileageLogReportInput.getState());
		iftaReportInput.setUnit(mileageLogReportInput.getUnit());
		
		iftaReportInput.setPeriodFrom(mileageLogReportInput.getPeriodFrom());
		iftaReportInput.setPeriodTo(mileageLogReportInput.getPeriodTo());
		
		iftaReportInput.setReportType(mileageLogReportInput.getReportType());
		
		iftaReportInput.setFirstInStateFrom(mileageLogReportInput.getFirstInStateFrom());
		iftaReportInput.setFirstInStateTo(mileageLogReportInput.getFirstInStateTo());
		iftaReportInput.setLastInStateFrom(mileageLogReportInput.getLastInStateFrom());
		iftaReportInput.setLastInStateTo(mileageLogReportInput.getLastInStateTo());
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nmileageLogBillingController==export()==type===>"+type+"\n");
		
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		MileageLogReportInput input = (MileageLogReportInput)request.getSession().getAttribute("input");
		try {
			Map<String,Object> datas = generateData(criteria, request, input);
			
			String reportType = "mileageLogTotalsReport";
			if (MileageLogReportInput.REPORT_TYPE_DETAILS.equals(input.getReportType())) {
				reportType = "mileageLogDetailsReport";
			}
			
			if (StringUtils.isEmpty(type)) {
				type = "xlsx";
			}
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=mileageLogReport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(reportType+"pdf",
						(List)datas.get("data"), params, type, request);
			} else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport(reportType+"csv",
						(List)datas.get("data"), params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport(reportType,
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/ownerOpSubConExport.do")
	public String ownerOpSubConExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nmileageLogBillingController==ownerOpSubConSearchExport()==type===>"+type+"\n");
		
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		MileageLogReportInput input = (MileageLogReportInput)request.getSession().getAttribute("input");
		try {
			Map<String,Object> datas = generateOwnerOpSubConMileageData(criteria, request, input);
			
			String reportType = "ownerOpSubConMileageReport";
			
			if (StringUtils.isEmpty(type)) {
				type = "xlsx";
			}
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename="+reportType+"." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			List<MileageLog> mielageLogList = (List<MileageLog>) datas.get("data");
			
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(reportType+"pdf",
						mielageLogList, params, type, request);
			} else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport(reportType+"csv",
						mielageLogList, params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport(reportType,
						mielageLogList, params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/iftaExport.do")
	public String iftaExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nmileageLogBillingController==iftaExport()==type===>"+type+"\n");
		
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		MileageLogReportInput input = (MileageLogReportInput)request.getSession().getAttribute("input");
		IFTAReportInput iftaReportInput = new IFTAReportInput();
		map(iftaReportInput, input);
		try {
			Map<String, Object> datas = generateIFTAData(criteria, request, iftaReportInput);
			
			String reportType = "iftaReport";
			
			if (StringUtils.isEmpty(type)) {
				type = "xlsx";
			}
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename="+reportType+"." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String, Object>)datas.get("params");
			
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(reportType+"pdf",
						(List)datas.get("data"), params, type, request);
			} else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport(reportType+"csv",
						(List)datas.get("data"), params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport(reportType,
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/mpgExport.do")
	public String mpgExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nmileageLogBillingController==mpgExport()==type===>"+type+"\n");
		
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		MileageLogReportInput input = (MileageLogReportInput)request.getSession().getAttribute("input");
		IFTAReportInput iftaReportInput = new IFTAReportInput();
		map(iftaReportInput, input);
		try {
			Map<String, Object> datas = generateMPGData(criteria, request, iftaReportInput);
			
			String reportType = "mpgReport";
			
			if (StringUtils.isEmpty(type)) {
				type = "xlsx";
			}
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename="+reportType+"." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String, Object>)datas.get("params");
			
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(reportType+"pdf",
						(List)datas.get("data"), params, type, request);
			} else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport(reportType+"csv",
						(List)datas.get("data"), params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport(reportType,
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	@ModelAttribute("modelObject")
	public MileageLogReportInput setupModel(HttpServletRequest request) {
		return new MileageLogReportInput();
	}
}
