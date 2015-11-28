package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hrreport.SalaryDetail;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/salaryreport")
public class SalaryReportController extends BaseController{
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
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/start.do")
	public String populate(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		Map criterias=new HashMap();
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		return "hr/report/salary";
	}
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		List<SalaryDetail> details=generateSalaryData(criteria,request);
		String payrollDateFrom=(String)criteria.getSearchMap().get("payrollDateFrom");
		String payrollDateTo=(String)criteria.getSearchMap().get("payrollDateTo");
		Location company=null;
		String companyid=(String)criteria.getSearchMap().get("company");
		if(!StringUtils.isEmpty(companyid)){
			company=genericDAO.getById(Location.class,Long.parseLong(companyid));
			
		}
		SalaryDetail detail=null;
		if(!details.isEmpty()){
			detail=details.get(0);
		}
		if(detail!=null){
			if(company!=null)
			params.put("company", company.getName());
			params.put("payRollBatch", payrollDateFrom);
			params.put("payRollBatchTo", payrollDateTo);
		}else{
			params.put("company", "");
			params.put("payRollBatch", "");
			params.put("payRollBatchTo", "");
		}
		data.put("params", params);
		data.put("data", details);
	    return data; 
		
	}
	public List<SalaryDetail> generateSalaryData(SearchCriteria criteria,HttpServletRequest request){
		return hrReportService.generateSalaryData(criteria);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		 Map imagesMap = new HashMap();
		   request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		   populateSearchCriteria(request, request.getParameterMap());
		   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		   Map<String,Object> datas = generateData(searchCriteria, request);
		   if (StringUtils.isEmpty(type))
				type = "html";
			
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "salary" + "." + type);
			}
		   try{
			   ByteArrayOutputStream out = new ByteArrayOutputStream();
			   Map params = (Map)datas.get("params");
			   out = dynamicReportService.generateStaticReport("salary",
						(List)datas.get("data"), params, type, request);
				out.writeTo(response.getOutputStream());
				out.close();
				return null;
			   
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
		//String sum=request.getParameter("typ");	
		
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			String report="salary";
			
			
			Map<String,Object> datas = generateData(criteria, request);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= salary." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport(report,
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

}
