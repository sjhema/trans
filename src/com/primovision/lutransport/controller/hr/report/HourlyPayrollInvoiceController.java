package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.report.ReportController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.EmployeePayrollInput;
import com.primovision.lutransport.model.hrreport.EmployeePayrollWrapper;
import com.primovision.lutransport.model.hrreport.HourlyPayrollInvoiceDetails;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
/**
 * @author subodh
 *
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/payrollinvoice")
public class HourlyPayrollInvoiceController extends ReportController<HourlyPayrollInvoiceDetails>{

	public HourlyPayrollInvoiceController() {
		setReportName("hourlyPayrollinvoice");
	}
	
	@Autowired
	protected HrReportService hrReportService;
	public void setHrReportService(HrReportService hrReportService) {
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
		
		return "hr/report/payrollinvoice";
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		  // System.out.println("\nHourlyPayrollInvoiceController--search.do----type=="+type+"\n");
        System.out.println("\n inside search() of HourlyPayrollInvoiceController");
		   Map imagesMap = new HashMap();
		   request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		   populateSearchCriteria(request, request.getParameterMap());
		   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
	     try{
		   Map<String,Object> datas = generateData(searchCriteria, request);
			//List propertyList = generatePropertyList(request);
			//request.getSession().setAttribute("propertyList", propertyList);
			if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + reportName + "." + type);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			out = dynamicReportService.generateStaticReport(reportName,
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
	
	 
		
		
		
		
		@Override
		protected Map<String,Object> generateData(SearchCriteria criteria,
				HttpServletRequest request) {
			Map<String,Object> data = new HashMap<String,Object>();
			Map params = new HashMap();
			//System.out.println("\nHourlyPayrollInvoiceController---generateData() method-");
			
			if (criteria.getSearchMap().get("payrollinvoicedate")!=null) {
				params.put("date", (String)criteria.getSearchMap().get("payrollinvoicedate"));
			}
			else {
				params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
			}
			params.put("payrollinvoiceNo", (String)criteria.getSearchMap().get("payrollinvoicenumber"));
			EmployeePayrollWrapper wrapper = generateHourlyPayrollReport(criteria);
			
			
			params.put("totalRowCount", wrapper.getTotalRowCount());
			params.put("sumtotalAmount", wrapper.getSumtotalAmount());
			params.put("sumdtAmount", wrapper.getSumdtAmount());
			params.put("sumotAmount", wrapper.getSumotAmount());
			params.put("sumregularAmount", wrapper.getSumregularAmount());
			
			
			List<HourlyPayrollInvoiceDetails> summary = wrapper.getInvoicedetails();
			
	        /*Comparator<HourlyPayrollInvoiceDetails> comparator=new Comparator<HourlyPayrollInvoiceDetails>() {
				@Override
				public int compare(HourlyPayrollInvoiceDetails o1, HourlyPayrollInvoiceDetails o2) {
					return  o1.getCompany().compareTo(o2.getCompany());
				}
			};*/
			
			Comparator<HourlyPayrollInvoiceDetails> comparator1=new Comparator<HourlyPayrollInvoiceDetails>() {
				@Override
				public int compare(HourlyPayrollInvoiceDetails o1, HourlyPayrollInvoiceDetails o2) {
					return  o1.getTerminal().compareTo(o2.getTerminal());
				}
			};
	        
			Comparator<HourlyPayrollInvoiceDetails> comparator2=new Comparator<HourlyPayrollInvoiceDetails>() {
				@Override
				public int compare(HourlyPayrollInvoiceDetails o1, HourlyPayrollInvoiceDetails o2) {
					return  o1.getDriver().compareTo(o2.getDriver());
				}
			};
			
			ComparatorChain chain = new ComparatorChain();  
			
			
				//chain.addComparator(comparator);
				
			chain.addComparator(comparator1);			
			chain.addComparator(comparator2);
			Collections.sort(summary, chain);
			
			wrapper.setInvoicedetails(summary);
			
			
			
			
			   
			data.put("data", wrapper.getInvoicedetails());
			data.put("params",params);
			return data;
		}
		
		public EmployeePayrollWrapper generateHourlyPayrollReport(SearchCriteria searchCriteria) {
			return hrReportService.generateHourlyPayrollData(searchCriteria);
		}
		
		
		@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/save.do")
		public String save(ModelMap model, HttpServletRequest request,	HttpServletResponse response) {
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
			try {
				hrReportService.saveHourlyPayrollData(request,criteria);
				request.getSession().setAttribute("msg", "Payroll Run is saved successfully");
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Unable to create file :" + e);
				request.getSession().setAttribute("errors", e.getMessage());
			}
			return "blank/blank";
		}
		
		
		
		@Override
		public String display(ModelMap model, HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(required = false, value = "type") String type,
				@RequestParam(required = false, value = "jrxml") String jrxml) {
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
			try {
				Map<String,Object> datas = generateData(criteria, request);
				//List propertyList = (List<String>) request.getSession()
				//		.getAttribute("propertyList");
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename=" + reportName + "." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Map<String, Object> params = (Map<String,Object>)datas.get("params");
				if (!type.equals("print")) {
					out = dynamicReportService.generateStaticReport(reportName,
							(List)datas.get("data"), params, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport(reportName,
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
	
}
