package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.SubcontractorInvoice;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.Billing_New;
import com.primovision.lutransport.model.report.SubcontractorBillingNew;
import com.primovision.lutransport.model.report.SubcontractorBillingWrapper;
import com.primovision.lutransport.model.report.SubcontractorReportInput;
import com.primovision.lutransport.model.report.SubcontractorSummary;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/subcontractorreport")
public class SubcontractorReportController extends BaseController {

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}

	@Autowired
	private ReportService reportService;

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
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
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,
				criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		model.addAttribute("destinations", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));

		criterias.clear();

		model.addAttribute("subcontractors", genericDAO.findByCriteria(
				SubContractor.class, criterias, "name", false));
		criterias.clear();

		model.addAttribute("voucherStatus", listStaticData("VOUCHER_STATUS"));
		 return "reportuser/report/subcontractorreport"; 
		
	}
	protected  Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request, SubcontractorReportInput input) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		// Subcontractor summary report - 16thMar2016
		SubcontractorBillingWrapper wrapper=generateSubcontractorReport(criteria, input, false);
		/*double mesceCharge=0.00;
		double mCharge=0.00;
		DecimalFormat df2 = new DecimalFormat("###.##");
		if(!StringUtils.isEmpty(input.getMiscelleneousCharges())){
			
			String query="select obj from SubcontractorInvoice obj where obj.miscelleneousCharges='"+input.getMiscelleneousCharges()+"'";
			List<SubcontractorInvoice> invoices=genericDAO.executeSimpleQuery(query);
			for(SubcontractorInvoice invoice:invoices){
			mCharge	=Double.parseDouble(invoice.getMiscelleneousCharges());
			System.out.println("\n d value-->"+Double.valueOf(invoice.getMiscelleneousCharges()));
			
			mesceCharge+=Double.valueOf(df2.format(mCharge));
			}
			params.put("misceCharges","$"+Double.toString(mesceCharge));
			
		}*/
		/*params.put("grandTotal", wrapper.getSumTotal()+mesceCharge);*/
		params.put("misceCharges",wrapper.getMiscelleneousCharges());
		params.put("grandTotal",wrapper.getGrandTotal());
		params.put("sumOtherCharges", wrapper.getSumOtherCharges());
		params.put("sumBillableTon", wrapper.getSumBillableTon());
		params.put("sumOriginTon", wrapper.getSumOriginTon());
		params.put("sumDestinationTon", wrapper.getSumDestinationTon());
		
		params.put("sumTotal", wrapper.getSumTotal());
		//params.put("sumNet", wrapper.getSumNet());
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
		data.put("data", wrapper.getSubcontractorBillingsNew());
		data.put("params",params);
		return data;
		//return null;
	}
	
	// Subcontractor summary report - 16thMar2016
	public SubcontractorBillingWrapper generateSubcontractorReport(SearchCriteria searchCriteria, SubcontractorReportInput input, boolean isSummary) {
		    return reportService.generateSubcontractorReportData(searchCriteria, input, isSummary);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") SubcontractorReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		// Subcontractor summary report - 16thMar2016
		String sum = request.getParameter("summary");
		if (StringUtils.contains(sum, "true")) {
			try {
				request.getSession().setAttribute("input", input);
				
				List<SubcontractorSummary> subcontractorSummaryList = generateSubcontractorSummaryReport(criteria, input); 
				if (subcontractorSummaryList.isEmpty()) {
					return "reportuser/report/subcontractorreport/nodata";
				}
				
				Map<String,Object> params= new HashMap<String,Object>();
				String batchDateFrom = StringUtils.isEmpty(input.getBatchDateFrom()) ? StringUtils.EMPTY : input.getBatchDateFrom();
				String batchDateTo = StringUtils.isEmpty(input.getBatchDateTo()) ? StringUtils.EMPTY : input.getBatchDateTo();
				params.put("batchDateFrom", batchDateFrom);
				params.put("batchDateTo", batchDateTo);
				
				if (StringUtils.isEmpty(type)) {
					type = "html";
				}
				response.setContentType(MimeUtil.getContentType(type));			
			   
				JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("subContractorSummaryReport",
						(List)subcontractorSummaryList, params, request);
				request.setAttribute("jasperPrint", jasperPrint);	
				return  "reportuser/report/subcontractorreport/summayHtml";
			} catch (Exception e) {
				e.printStackTrace();
				request.getSession().setAttribute("errors", e.getMessage());
				return "error";
			} 
		}
		
		String p=request.getParameter("p");
		if(p==null)
			request.getSession().setAttribute("input", input);
		
		SubcontractorReportInput input1=(SubcontractorReportInput)request.getSession().getAttribute("input");
		criteria.setPageSize(1000);
		try {
			Map<String, Object> datas;
			//if(criteria.getPage()==0)
			if(p==null)
			{
				 datas = generateData(criteria,request,input);
		    }else
			{
				 datas = generateData(criteria,request,input1);	
			}
			//Map<String, Object> datas = generateData(criteria, request,input);
			if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=subcontractorreport." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("subcontractorreport",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			/*return "reportuser/report/"+type;*/
			if(criteria.getRecordCount()>0)
			    return "reportuser/report/subcontractorreport/"+type;
		     return "reportuser/report/subcontractorreport/nodata";
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	
	// Subcontractor summary report - 16thMar2016
	private List<SubcontractorSummary> generateSubcontractorSummaryReport(SearchCriteria criteria, 
			SubcontractorReportInput input) {
		List<SubcontractorSummary> subcontractorSummaryList = reportService.generateSubcontractorSummaryReport(criteria, input);
		return subcontractorSummaryList;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml)  throws IOException {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		SubcontractorReportInput input = (SubcontractorReportInput)request.getSession().getAttribute("input");
		
		// Subcontractor summary report - 16thMar2016
		String sum=request.getParameter("typ");
		if (StringUtils.equalsIgnoreCase(sum, "summary")) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				List<SubcontractorSummary> subcontractorSummaryList = generateSubcontractorSummaryReport(criteria, input);           
				
				Map<String,Object> params= new HashMap<String,Object>();
				String batchDateFrom = StringUtils.isEmpty(input.getBatchDateFrom()) ? StringUtils.EMPTY : input.getBatchDateFrom();
				String batchDateTo = StringUtils.isEmpty(input.getBatchDateTo()) ? StringUtils.EMPTY : input.getBatchDateTo();
				params.put("batchDateFrom", batchDateFrom);
				params.put("batchDateTo", batchDateTo);
				
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename=subContractorSummaryReport." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				
				if (!type.equals("print")&&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("subContractorSummaryReport",
							(List)subcontractorSummaryList, params, type, request);
				}
				else if(type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("subContractorSummaryReport",
							(List)subcontractorSummaryList, params, type, request);
				} 			
				out.writeTo(response.getOutputStream());
				out.flush();
				out.close();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Unable to create file :" + e);
				request.getSession().setAttribute("errors", e.getMessage());
				out.flush();
				out.close();
				return "error";
			}			
		}
		
		criteria.setPageSize(25000);
		criteria.setPage(0);
		try {
			Map<String,Object> datas = generateData(criteria, request, input);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= subcontractorreport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("subcontractorreport",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("subcontractorreportpdf",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("subcontractorreport"+"print",
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

	@ModelAttribute("modelObject")
	public SubcontractorReportInput setupModel(HttpServletRequest request) {
		return new SubcontractorReportInput();
	}

}
