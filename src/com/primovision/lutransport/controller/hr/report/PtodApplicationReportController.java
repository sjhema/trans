package com.primovision.lutransport.controller.hr.report;

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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hrreport.PtodApplicationInput;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

@Controller
@RequestMapping("/hr/report/ptodapplication")
public class PtodApplicationReportController extends BaseController{
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	@Autowired
	protected HrReportService hrReportService;
	
	public void setHrReportService(
			HrReportService hrReportService) {
		this.hrReportService = hrReportService;
	}
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		SearchCriteria criteria = (SearchCriteria) request.getSession()
		.getAttribute("searchCriteria");
			if (criteria != null) {
				if (criteria.getSearchMap() != null)
					criteria.getSearchMap().clear();
			}
			Map criterias = new HashMap();
			
			criterias.clear();
			criterias.put("type", 3);
			model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
			criterias.clear();
			criterias.put("type", 4);
			model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
			criterias.clear();
			model.addAttribute("categories",genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
			criterias.clear();
			criterias.put("status", 1);
			model.addAttribute("employees",genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
			
			criterias.clear();
			model.addAttribute("leavetypes",genericDAO.findByCriteria(LeaveType.class, criterias, "name", false));
			criterias.clear();
			model.addAttribute("approvestatuss", listStaticData("APPROVE_STATUS"));
			
			return "hr/report/ptodapplication/enquiry";
	}
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject")PtodApplicationInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {

		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			Map<String, Object> datas = generateData(criteria, request,input);
		    if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=ptodapplication." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("ptodapplication",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "hr/report/ptodapplication/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	
	
	
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25000);
		criteria.setPage(0);
		PtodApplicationInput input = (PtodApplicationInput)request.getSession().getAttribute("input");
		try {
			Map<String,Object> datas = generateData(criteria, request, input);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= ptodapplication." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("ptodapplication",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("ptodapplicationpdf",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("ptodapplication"+"print",
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			criteria.setPageSize(25);
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,PtodApplicationInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		List<PtodApplicationInput> list=hrReportService.generatePtodApplicationReport(searchCriteria, input);
		 data.put("data",list);
		  return data;
	}
	@ModelAttribute("modelObject")
	public PtodApplicationInput  setupModel(HttpServletRequest request) {
		return new PtodApplicationInput();
	}

}
