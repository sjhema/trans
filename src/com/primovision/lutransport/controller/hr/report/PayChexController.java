package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.startup.SetContextPropertiesRule;
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
import com.primovision.lutransport.model.hr.PayChex;
import com.primovision.lutransport.model.hrreport.PayChexDetail;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/paychex")
public class PayChexController extends BaseController{
	
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
		Map criterias=new HashMap();
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		return "hr/report/paychex";
	}
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request) {
		
		String companyid=(String)criteria.getSearchMap().get("company");
		String terminalid=(String)criteria.getSearchMap().get("terminal");
		String payrollDate=(String)criteria.getSearchMap().get("payrollDate");
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		List<PayChexDetail> details=generatePayData(criteria);
		
		request.getSession().setAttribute("paychexfreezedlist",details);
		request.getSession().setAttribute("paychexfreezedcompanyname",companyid);
		request.getSession().setAttribute("paychexfreezedterminalname",terminalid);
		request.getSession().setAttribute("paychexfreezedcheckDate",payrollDate);
		
		params.put("totalRowCount",details.size());
		data.put("params", params);
		data.put("data", details);
	    return data;     
		
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/savedetails.do")	
    public String savePayChexFreezedData(ModelMap model,HttpServletRequest request) throws ParseException{
 	 
	 Location companyLocation=null;
	 Location terminalLocation=null;
	 
	 String company = (String)request.getSession().getAttribute("paychexfreezedcompanyname");
	 String terminal = (String)request.getSession().getAttribute("paychexfreezedterminalname");
	 String cehckDate = (String) request.getSession().getAttribute("paychexfreezedcheckDate");
	 Map criti = new HashMap(); 
	 if(!StringUtils.isEmpty(company)){
		 criti.clear();
		 criti.put("id",Long.parseLong(company));
		 companyLocation = genericDAO.getByCriteria(Location.class,criti);
	 }
	 
	 if(!StringUtils.isEmpty(terminal)){
		 criti.clear();
		 criti.put("id",Long.parseLong(terminal));
		 terminalLocation = genericDAO.getByCriteria(Location.class,criti);
	 }
	 
	 Date payrollbatch =null;
	 if(!StringUtils.isEmpty(cehckDate)){
		 payrollbatch = new SimpleDateFormat("MM-dd-yyyy")
			.parse(cehckDate); 		 
	 }
	 
	 List<PayChexDetail> paychexList = (List<PayChexDetail>) request.getSession().getAttribute("paychexfreezedlist");
	 
	 if(paychexList!=null && paychexList.size()>0){		 
		 for(PayChexDetail paychexObj:paychexList){
			 paychexObj.setCompanyLocation(companyLocation);
			 paychexObj.setTerminalLocation(terminalLocation);
			 paychexObj.setCheckDate(payrollbatch);
			 genericDAO.saveOrUpdate(paychexObj);
		 }	
		 
		 PayChex paychex = new PayChex();
		 paychex.setTerminalLocation(terminalLocation);
		 paychex.setCompanyLocation(companyLocation);
		 paychex.setCheckDate(payrollbatch);
		 genericDAO.saveOrUpdate(paychex);
	 }
	 
	 request.getSession().removeAttribute("paychexfreezedcompanyname");
	 request.getSession().removeAttribute("paychexfreezedterminalname");
	 request.getSession().removeAttribute("paychexfreezedcheckDate");
	 request.getSession().removeAttribute("paychexfreezedlist");
	 request.getSession().setAttribute("msg","Pay Chex Details Stored Successfully.");
	 return "/blank/blank";
 }
	
	
public List<PayChexDetail> generatePayData(SearchCriteria criteria){
	return hrReportService.generatePaychexData(criteria);
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
						"attachment;filename=" + "paychex" + "." + type);
			}
		   try{
			   ByteArrayOutputStream out = new ByteArrayOutputStream();
			   Map params = (Map)datas.get("params");
			   out = dynamicReportService.generateStaticReport("paychex",
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
		
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			String report="paychex";
			Map<String,Object> datas = generateData(criteria, request);
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= paychex." + type);
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
