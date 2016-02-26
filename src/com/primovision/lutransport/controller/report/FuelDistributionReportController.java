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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.FuelDistributionReportInput;
import com.primovision.lutransport.model.report.FuelDistributionReportWrapper;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;

import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/fueldistribution")
public class FuelDistributionReportController extends BaseController {
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public FuelDistributionReportController() {
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
		model.addAttribute("fuelvendor",genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		criterias.put("type", 1);
		model.addAttribute("origins",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 2);
		model.addAttribute("destinations",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 1);
		model.addAttribute("trucks",genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		criterias.clear();
		/*EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);*/
		model.addAttribute("drivers", genericDAO.executeSimpleQuery("Select obj from Driver obj group by obj.fullName order by obj.fullName "));
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("fuelTypes", listStaticData("FUEL_TYPE"));
		return "reportuser/report/fuelDistributionReport";
	}
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria, HttpServletRequest request, FuelDistributionReportInput input) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		FuelDistributionReportWrapper wrapper = generateFuelDistributionReport(searchCriteria, input);
		 
		params.put("totalAmounts", wrapper.getTotalAmounts());
		params.put("totaldiscounts", wrapper.getTotaldiscounts());
		params.put("totalFees", wrapper.getTotalFees());
		params.put("totalGallons", wrapper.getTotalGallons());
		params.put("totalColumn", wrapper.getTotalColumn());
		params.put("totalGrossCost", wrapper.getTotalGrossCost());
		params.put("invoiceDateFrom", wrapper.getInvoiceDateFrom());
		params.put("invoiceDateTo", wrapper.getInvoiceDateTo());
		params.put("transactionDateFrom", wrapper.getTransactionDateFrom());
		params.put("transactionDateTo", wrapper.getTransactionDateTo());
		params.put("invoiceNumberFrom", wrapper.getInvoiceNumberFrom());
		params.put("invoiceNumberTo", wrapper.getInvoiceNumberTo());
		  
		data.put("data", wrapper.getFuellog());
		data.put("params",params);
		  
		return data;
	}
	
	public FuelDistributionReportWrapper generateFuelDistributionReport(SearchCriteria searchCriteria, FuelDistributionReportInput input) {
		return reportService.generateFuelDistributionData(searchCriteria, input);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") FuelDistributionReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
        System.out.println("\nFuelDistributionController==Search()==type===>"+type+"\n"); 
		Map imagesMap = new HashMap();
		String p = request.getParameter("p");
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		//request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		
		if (p == null)
			request.getSession().setAttribute("input", input);
		
		FuelDistributionReportInput input1 = (FuelDistributionReportInput) request.getSession().getAttribute("input");
		
		try {
			Map<String, Object> datas;
			if (p == null) {
				 datas = generateData(criteria, request, input);
		   } else {
				 datas = generateData(criteria, request, input1);	
			}
			
			if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=fuelDistributionReport." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map) datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("fuelDistributionReport",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/fueldistributionreport/"+type;
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
		System.out.println("\nFuelDistributionReportController==export()==type===>"+type+"\n"); 
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		FuelDistributionReportInput input = (FuelDistributionReportInput) request.getSession().getAttribute("input");
		try {
			Map<String, Object> datas = generateData(criteria, request, input);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= fuelDistributionReport." + type);
			}
			
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String, Object>)datas.get("params");
			/*if (!type.equals("print") &&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("fuelDistributionReport",
						(List)datas.get("data"), params, type, request);
			}*/
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("fuelDistributionReport",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport("fuelDistributionReport",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("fuelDistributionReport",
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
	public FuelDistributionReportInput setupModel(HttpServletRequest request) {
		return new FuelDistributionReportInput();
	}

}
