package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;

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
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.report.MileageLogReportInput;
import com.primovision.lutransport.model.report.MileageLogReportWrapper;

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
	public String populate(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null) {
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		
		Map criterias = new HashMap();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 1);
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		return "reportuser/report/mileageLogReport";
	}
	
	private Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request, MileageLogReportInput input) {
		MileageLogReportWrapper wrapper = generateMileageLogReport(searchCriteria, input);
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		 
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
		if (!StringUtils.isEmpty(wrapper.getPeriodFrom()) && !StringUtils.isEmpty(wrapper.getPeriodTo())) {
			period = wrapper.getPeriodFrom() + " - " + wrapper.getPeriodTo();
		}
		params.put("period", period);
		  
		data.put("data", wrapper.getMileageLogList());
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
		if (p==null) {
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
			
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=mileageLogReport." + type);
			
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
	
	@ModelAttribute("modelObject")
	public MileageLogReportInput setupModel(HttpServletRequest request) {
		return new MileageLogReportInput();
	}
}
