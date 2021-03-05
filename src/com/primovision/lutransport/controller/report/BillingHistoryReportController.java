package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

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
import com.primovision.lutransport.model.Customer;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

/**
 * @author Amit
 * 
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/billinghistory")
public class BillingHistoryReportController extends BaseController {

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public BillingHistoryReportController() {
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
		criterias.put("type", 2);
		model.addAttribute("trailers",genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
		/*String query = "select distinct(obj.customername) from BillingRate obj";
		List<Object[]> results = genericDAO.getEntityManager().createQuery(query).getResultList();
		model.addAttribute("customers", results);*/
		criterias.clear();
		model.addAttribute("customers",genericDAO.findByCriteria(Customer.class, criterias, "name",false));
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class, criterias, "name", false));
		criterias.clear();
		//criterias.put("role.id", "2,l");
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "username", false));
		model.addAttribute("ticketStatuss", listStaticData("TICKET_STATUS"));
		
		List<String> notBillableOptions = new ArrayList<String>();
		notBillableOptions.add(BillingHistoryInput.EXCLUDE_NOT_BILLABLE);
		notBillableOptions.add(BillingHistoryInput.INCLUDE_NOT_BILLABLE);
		model.addAttribute("notBillableOptions", notBillableOptions);
		
		// Peak rate 2nd Feb 2021
		List<String> peakRateOptions = new ArrayList<String>();
		peakRateOptions.add("Y");
		peakRateOptions.add("N");
		model.addAttribute("peakRateOptions", peakRateOptions);
		
		return "reportuser/report/billinghistory";
	}

	protected  Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request, BillingHistoryInput input) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		/*if (criteria.getSearchMap().get("fromDate")!=null)
			params.put("fromDate", criteria.getSearchMap().get("fromDate"));
		if (criteria.getSearchMap().get("toDate")!=null)
			params.put("toDate", criteria.getSearchMap().get("toDate"));
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("origin"))) {
			Location origin = genericDAO.getById(Location.class,Long.parseLong((String)criteria.getSearchMap().get("origin")));
			params.put("origin", origin.getName());
		}
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("destination"))) {
			Location destination = genericDAO.getById(Location.class,Long.parseLong((String)criteria.getSearchMap().get("destination")));
			params.put("destination", destination.getName());
		}
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("origin")) && !StringUtils.isEmpty((String)criteria.getSearchMap().get("destination"))) {
			Map criterias = new HashMap();
			criterias.put("transferStation.id", Long.parseLong((String)criteria.getSearchMap().get("origin")));
			criterias.put("landfill.id", Long.parseLong((String)criteria.getSearchMap().get("destination")));
			BillingRate rate = genericDAO.getByCriteria(BillingRate.class, criterias);
			if (rate!=null) {
				params.put("notes", rate.getNotes());
			}
		}
		params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
		params.put("invoiceNo", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));*/
		BillingWrapper wrapper = generateBillingHistoryReport(criteria, input);
		if(!StringUtils.isEmpty(input.getTotAmtFrom())&&!StringUtils.isEmpty(input.getTotAmtTo())){
			if(input.getTotAmtFrom().equalsIgnoreCase(input.getTotAmtTo())){
				wrapper.setSumTotal(Double.parseDouble(input.getTotAmtFrom()));
				String query="select obj from Invoice obj where obj.sumTotal="+input.getTotAmtFrom();
				List<Invoice> invoices=genericDAO.executeSimpleQuery(query);
				if(invoices.size()>0){
				wrapper.setSumAmount(invoices.get(0).getSumAmount());
				wrapper.setSumDemmurage(invoices.get(0).getSumDemmurage());
				wrapper.setSumTonnage(invoices.get(0).getSumTonnage());
				wrapper.setSumFuelSurcharge(invoices.get(0).getSumFuelSurcharge());
				}
			}
		}			
		params.put("sumDestinationTon", wrapper.getSumDestinationTon());
		params.put("sumOriginTon", wrapper.getSumOriginTon());
		params.put("sumBillableTon", wrapper.getSumBillableTon());
		params.put("sumTonnage", wrapper.getSumTonnage());
		params.put("sumTotal", wrapper.getSumTotal());
		params.put("sumDemurrage", wrapper.getSumDemmurage());
		params.put("sumNet", wrapper.getSumNet());
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
		populateParams(params, input, false);
		
		data.put("data", wrapper.getBillings());		
		data.put("params", params);
		return data;
	}

	public BillingWrapper generateBillingHistoryReport(SearchCriteria searchCriteria, BillingHistoryInput input) {
		return reportService.generateBillingHistoryData(searchCriteria,input);
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") BillingHistoryInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		String p=request.getParameter("p");
		String sum=request.getParameter("summary");
		
	/*	if(StringUtils.contains(sum, "true")){
			model.addAttribute("list",reportService.generateSummary(criteria, input));
			return  "reportuser/report/summary";
		}*/
		
		// Truck driver report
		resetDrillDownCriteria(input);
		
		if(StringUtils.contains(sum, "true")){
			Map<String,Object> params= new HashMap<String,Object>();
			//List<Summary> summarylist=new ArrayList<Summary>();			           
			//List<Summary> list=reportService.generateSummaryNew(criteria, input);
			List<Summary> list = generateSummaryNew(criteria, params, input);
			/*if (!StringUtils.isEmpty(input.getBatchDateFrom())) {
				params.put("batchDateFrom",input.getBatchDateFrom());
			}
			if (!StringUtils.isEmpty(input.getBatchDateTo())) {
				params.put("batchDateTo",input.getBatchDateTo());
			}*/
			
			// Truck driver report
			StringBuffer requestUrl = request.getRequestURL();
			String truckDriverReportUrl = StringUtils.replace(requestUrl.toString(), "search.do", "truckDriverReport.do");
			params.put("truckDriverReportUrl", truckDriverReportUrl);
			
			if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			
		    //ByteArrayOutputStream out = new ByteArrayOutputStream();
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("summaryReport",(List)list,params,request);
			
			// Peak rate 2nd Feb 2021
			//request.setAttribute("jasperPrint", jasperPrint);
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			
			request.getSession().setAttribute("input", input);
			return  "reportuser/report/summary/html";
	
			/*model.addAttribute("list",list);
			return "reportuser/report/summary";*/
		}
		
		if(p==null)
		request.getSession().setAttribute("input", input);
		BillingHistoryInput input1=(BillingHistoryInput)request.getSession().getAttribute("input");
		
		
		//request.getSession().setAttribute("input", input);
		//populateSearchCriteria(request, request.getParameterMap());
		//SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
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
						"attachment;filename=billinghistory." + type);
			//ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("billinghistory",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			/*return "reportuser/report/"+type;*/
			if(criteria.getRecordCount()>0)
			    return "reportuser/report/"+type;
		     return "reportuser/report/nodata";
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

	}
	
	private List<Summary> generateSummaryNew(SearchCriteria criteria, Map<String, Object> params, 
			BillingHistoryInput input) {
		List<Summary> list = reportService.generateSummaryNew(criteria, input, params);
		populateParams(params, input, true);
		return list;
	}
	
	private void populateParams(Map<String,Object> params, BillingHistoryInput input, boolean isSummary) {
		if (!StringUtils.isEmpty(input.getBatchDateFrom())) {
			params.put("batchDateFrom",input.getBatchDateFrom());
		}
		if (!StringUtils.isEmpty(input.getBatchDateTo())) {
			params.put("batchDateTo", input.getBatchDateTo());
		}
		if (!StringUtils.isEmpty(input.getLoadedFrom())) {
			params.put("loadedFrom", input.getLoadedFrom());
		}
		if (!StringUtils.isEmpty(input.getLoadedTo())) {
			params.put("loadedTo", input.getLoadedTo());
		}
		if (!StringUtils.isEmpty(input.getUnloadedFrom())) {
			params.put("unloadedFrom", input.getUnloadedFrom());
		}
		if (!StringUtils.isEmpty(input.getUnloadedTo())) {
			params.put("unloadedTo", input.getUnloadedTo());
		}
		String notBillable = input.getNotBillable();
		if (StringUtils.isEmpty(notBillable)) {
			if (isSummary) {
				notBillable = BillingHistoryInput.EXCLUDE_NOT_BILLABLE;
			} else {
				notBillable = BillingHistoryInput.INCLUDE_NOT_BILLABLE;
			}
		}
		params.put("notBillable", notBillable);
	}
	
	/**
	 * Generic method to export report based on the filter criteria.
	 * 
	 * @param type
	 *            File type to which the report has to be exported
	 * @param jrxml
	 *            To decide whether jrxml or dynamicreport's method is to be
	 *            used
	 * @throws IOException 
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) throws IOException {
		Map imagesMap = new HashMap();
		String sum=request.getParameter("typ");	
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(500000);
		criteria.setPage(0);
		BillingHistoryInput input = (BillingHistoryInput)request.getSession().getAttribute("input");
		
		// Truck driver report
		resetDrillDownCriteria(input);
		
		  if(StringUtils.equalsIgnoreCase(sum,"summary")){
				
	        	 Map<String,Object> params= new HashMap<String,Object>();
	 			//List<Summary> summarylist=new ArrayList<Summary>();
	 			ByteArrayOutputStream out = new ByteArrayOutputStream();
	 			try{
	 				//List<Summary> list=reportService.generateSummaryNew(criteria, input);
	 				List<Summary> list = generateSummaryNew(criteria, params, input);
	 			/*if (!StringUtils.isEmpty(input.getBatchDateFrom())) {
					params.put("batchDateFrom",input.getBatchDateFrom());
				}
				if (!StringUtils.isEmpty(input.getBatchDateTo())) {
					params.put("batchDateTo",input.getBatchDateTo());
				}*/
	 			/*for(Object obj:list){						
					Object[] objArry=(Object[]) obj;
					Summary summary=new Summary();				
					summary.setOrigin(objArry[0].toString());
					summary.setDestination(objArry[1].toString());				
					summary.setCount(Integer.parseInt(objArry[2].toString()));
					summary.setAmount(Double.parseDouble(objArry[3].toString()));
				    if(objArry[4] != null)
				    	summary.setCompany(objArry[4].toString());
				    else
				    	summary.setCompany("");
				    if (objArry[5] != null) {
							Double billableTon = Double.parseDouble(objArry[5].toString());
							billableTon = MathUtil.roundUp(billableTon, 2);
							summary.setBillableTons(billableTon);
					 }
				    if (objArry[6] != null) {
							summary.setCountTrucks(Integer.parseInt(objArry[6].toString()));
					 }
				    if (objArry[7] != null) {
							summary.setNetAmount(Double.parseDouble(objArry[7].toString()));
					 }
					 if (objArry[8] != null) {
							summary.setFuelSurcharge(Double.parseDouble(objArry[8].toString()));
					 }
					 if (objArry[9] != null) {
							summary.setDriverPay(Double.parseDouble(objArry[9].toString()));
					 }
					 summarylist.add(summary);
					}*/
	 				
	 			if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename=summaryReport." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				//ByteArrayOutputStream out = new ByteArrayOutputStream();
	 			
				if (!type.equals("print")&&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("summaryReport",
							(List)list, params, type, request);
				}
				else if(type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("summaryReportpdf",
							(List)list, params, type, request);
				} 			
				out.writeTo(response.getOutputStream());
				criteria.setPageSize(500);				
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
		
		  ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Map<String,Object> datas = generateData(criteria, request, input);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= billinghistory." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));			
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("billinghistory",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("billinghistorypdf",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("billinghistory"+"print",
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			criteria.setPageSize(500);
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
	
	// Truck driver report
	private void resetDrillDownCriteria(BillingHistoryInput input) {
		input.setDrillDownCompany(StringUtils.EMPTY);
		input.setDrillDownOrigin(StringUtils.EMPTY);
		input.setDrillDownDestination(StringUtils.EMPTY);
	}
	
	// Truck driver report
	@RequestMapping(method = { RequestMethod.GET}, value = "/truckDriverReport.do")
	public String processTruckDriverReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = true) String company,
			@RequestParam(required = true) String origin,
			@RequestParam(required = true) String destination) throws IOException {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPageSize = criteria.getPageSize();
		int origPage = criteria.getPage();
		
		criteria.setPageSize(500000);
		criteria.setPage(0);
		
		BillingHistoryInput input = (BillingHistoryInput)request.getSession().getAttribute("input");
		input.setDrillDownCompany(company);
		input.setDrillDownOrigin(origin);
		input.setDrillDownDestination(destination);
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		/*if (!StringUtils.isEmpty(input.getBatchDateFrom())) {
			params.put("batchDateFrom", input.getBatchDateFrom());
		}
		if (!StringUtils.isEmpty(input.getBatchDateTo())) {
			params.put("batchDateTo", input.getBatchDateTo());
		}*/
		
		params.put("company", company);
		params.put("origin", origin);
		params.put("destination", destination);
		
		if (StringUtils.isEmpty(type)) {
			type = "csv";
		}
     	
		ByteArrayOutputStream out = null;
		try {
			//List<Summary> summaryList = reportService.generateSummaryNew(criteria, input);
			List<Summary> summaryList = generateSummaryNew(criteria, params, input);
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=billingHistoryTruckDriverReport." + type);
			}
			
			String reportName = "billingHistoryTruckDriverReport";
			if (type.equals("pdf")) {
				reportName = "billingHistoryTruckDriverReportpdf";
			}
			
			out = dynamicReportService.generateStaticReport(reportName,
						(List)summaryList, params, type, request);
			
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		} finally {
			criteria.setPageSize(origPageSize);
			criteria.setPage(origPage);
			
			// Truck driver report
			resetDrillDownCriteria(input);
			
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
	}
	
	@ModelAttribute("modelObject")
	public BillingHistoryInput setupModel(HttpServletRequest request) {
		return new BillingHistoryInput();
	}

}
