package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.SubcontractorBilling;
import com.primovision.lutransport.model.report.SubcontractorBillingWrapper;
import com.primovision.lutransport.service.ReportService;


@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/subcontractorbilling")
public class SubcontractorBillingRateController extends ReportController<SubcontractorBilling>{
	
	public SubcontractorBillingRateController() {
		setReportName("subcontractorbilling");
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String report(ModelMap model, HttpServletRequest request) {
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
		criterias.put("type", 2);
		model.addAttribute("destinations", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("subContractor", genericDAO.findByCriteria(
				SubContractor.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companyLocation", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));

		return "/reportuser/report/subcontractorbilling";
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/save.do")
	public String save(ModelMap model, HttpServletRequest request,	HttpServletResponse response) {
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if(StringUtils.isEmpty((String)criteria.getSearchMap().get("company"))){
			request.getSession().setAttribute("error","Please select Company");
			return "blank/blank";
		}
		try {
			reportService.saveSubcontractorBillingData(request,criteria);
			request.getSession().setAttribute("msg", "Your voucher is saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
		return "blank/blank";
	}
	@Override
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request) {
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map params = new HashMap();
		String   subcontractorName   = (String) criteria.getSearchMap().get("subcontractorName");
		String company=(String)criteria.getSearchMap().get("company");
	/*	if(subcontractorName.isEmpty()||company.isEmpty())*/
		if(subcontractorName.isEmpty()){
			StringBuilder builder=new StringBuilder();
			if(subcontractorName.isEmpty())
				builder.append("Please select Subcontractor<br/>  ");
			/*if(company.isEmpty())
				builder.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Please select Company");*/
			
			request.getSession().setAttribute("error", builder.toString());
			
			try {
				throw new Exception("subcontractor Name null");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(criteria.getSearchMap().get("subcontractorName")!=null)
			params.put("subcontractorName", criteria.getSearchMap().get("subcontractorName"));
		if (criteria.getSearchMap().get("fromDate")!=null)
			params.put("fromDate", criteria.getSearchMap().get("fromDate"));
		if (criteria.getSearchMap().get("toDate")!=null)
			params.put("toDate", criteria.getSearchMap().get("toDate"));
		if (criteria.getSearchMap().get("fromloadDate")!=null)
			params.put("loadDate", criteria.getSearchMap().get("fromloadDate"));
		if (criteria.getSearchMap().get("toloadDate")!=null)
			params.put("loadDate", criteria.getSearchMap().get("toloadDate"));
		if (criteria.getSearchMap().get("fromunloadDate")!=null)
			params.put("unloadDate", criteria.getSearchMap().get("fromunloadDate"));
		if (criteria.getSearchMap().get("tounloadDate")!=null)
			params.put("unloadDate", criteria.getSearchMap().get("tounloadDate"));
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("origin"))) {
			Location origin = genericDAO.getById(Location.class,Long.parseLong((String)criteria.getSearchMap().get("origin")));
			params.put("origin", origin.getName());
		}
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("destination"))) {
			Location destination = genericDAO.getById(Location.class,Long.parseLong((String)criteria.getSearchMap().get("destination")));
			params.put("destination", destination.getName());
		}
		if (criteria.getSearchMap().get("invoiceDate")!=null) {
			params.put("date", (String)criteria.getSearchMap().get("invoiceDate"));
		}
		else {
			params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
		}
		params.put("invoiceNo", (String)criteria.getSearchMap().get("invoiceNumber"));
		
		if(!StringUtils.isEmpty((String)criteria.getSearchMap().get("miscelleneousNote"))){
			String[] miscellNotes=((String)criteria.getSearchMap().get("miscelleneousNote")).split(",");			
			for(int i=0;i<miscellNotes.length;i++){
			params.put("misceNote"+i,miscellNotes[i]);				
			}			
		}
		
		if(!StringUtils.isEmpty((String)criteria.getSearchMap().get("miscelleneousCharges"))){
			String[] miscellCharges=((String)criteria.getSearchMap().get("miscelleneousCharges")).split(",");
			for(int i=0;i<miscellCharges.length;i++){				
				params.put("misceCharges"+i,"$"+miscellCharges[i]);				
				}
		}
		
		
		
		
		SubcontractorBillingWrapper wrapper = generateBillingReport(criteria);
		setList(wrapper.getList());
		params.put("grandTotal", wrapper.getGrandTotal());
		params.put("subcontractorname", wrapper.getSubcontractorname());
		params.put("address1",wrapper.getAddress1() );
		params.put("address2", wrapper.getAddress2());
		params.put("city", wrapper.getCity());
		params.put("zipcode",wrapper.getZipcode() );
		params.put("phone",wrapper.getPhone() );
		params.put("fax",wrapper.getFax() );
		params.put("state",wrapper.getState());
		if(wrapper.getCompanyLocationId()!=null)
		params.put("companyname", wrapper.getCompanyLocationId().getName());
		
		params.put("sumOtherCharges", wrapper.getSumOtherCharges());
		params.put("sumBillableTon", wrapper.getSumBillableTon());
		params.put("sumOriginTon", wrapper.getSumOriginTon());
		params.put("sumDestinationTon", wrapper.getSumDestinationTon());
		params.put("sumTotal", wrapper.getSumTotal());
		params.put("sumNet", wrapper.getSumNet());
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
		params.put("totalRowCount", wrapper.getTotalRowCount());
		System.out.println("******* the total row count is "+wrapper.getTotalRowCount());
		data.put("data", wrapper.getSubcontractorBillings());
		data.put("params",params);
		
		return data;
	}

	public SubcontractorBillingWrapper generateBillingReport(SearchCriteria searchCriteria) {
		return reportService.generateSubcontractorBillingData(searchCriteria);
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {

		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			Map<String,Object> datas = generateData(criteria, request);
			//List propertyList = generatePropertyList(request);
			//request.getSession().setAttribute("propertyList", propertyList);
			if (StringUtils.isEmpty(type)){
				type = "html";
				request.getSession().setAttribute("errorlist",getList());
				
				if(getList().size()>0){
					return "blank/subcontractorexpiredratelist";
				}
				
			}
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
			/*JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			 return "reportuser/report/subcontractor/"+type;*/
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "blank/blank";
		}

	}
	
	//
	
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
			if (type.equals("print")) {
				out = dynamicReportService.generateStaticReport(reportName+"print",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(reportName+"pdf",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport(reportName+"csv",
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
	
	//
	
	
	
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if ("findDestinationLoad".equalsIgnoreCase(action)) {
			if (!StringUtils.isEmpty(request.getParameter("origin"))) {
				List<Location> destinatioLoad = new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("transferStation.id",
						Long.parseLong(request.getParameter("origin")));
				List<SubcontractorRate> billing = genericDAO.findByCriteria(
						SubcontractorRate.class, criterias, "transferStation",
						false);
				for (SubcontractorRate bill : billing) {
					destinatioLoad.add(bill.getLandfill());
				}
				Gson gson = new Gson();
				return gson.toJson(destinatioLoad);
			}
		}
		if ("findOriginLoad".equalsIgnoreCase(action)) {
			if (!StringUtils.isEmpty(request.getParameter("destination"))) {
				List<Location> originLoad = new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("landfill.id",
						Long.parseLong(request.getParameter("destination")));
				List<SubcontractorRate> billing = genericDAO.findByCriteria(
						SubcontractorRate.class, criterias, "landfill", false);
				for (SubcontractorRate bill : billing) {
					originLoad.add(bill.getTransferStation());
				}
				Gson gson = new Gson();
				return gson.toJson(originLoad);
			}
		}
		return "";
	}
	
	public List<String> list = new ArrayList<String>();

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
}
