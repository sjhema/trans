package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
//import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.model.report.NetReportInput;
import com.primovision.lutransport.model.report.NetReportWrapper;

import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

/**
 * @author kishor
 * 
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/netreport")
public class NetReportController extends BaseController{

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public NetReportController() {
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null)
		{
			
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		Map criterias = new HashMap();		
		model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where obj.id in (4,6,5) and obj.type=3 order by obj.name"));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals",genericDAO.findByCriteria(Location.class, criterias, "name",false));
		
		criterias.put("type", 1);
		model.addAttribute("trucks",genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "lastName", false));
		criterias.clear();
		criterias.put("type", 2);
		model.addAttribute("trailers",genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
		model.addAttribute("ticketStatuss", listStaticData("TICKET_STATUS"));
		return "reportuser/report/netReport";
	}
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,NetReportInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		System.out.println("\nNetReportController-----generateData()method\n");
		List<NetReportWrapper> wrapper = generateNetReport(searchCriteria,input,request);
		String range=null;
		String daterangeFrom=new String();
		String daterangeTo=new String();
	
		/*if ((searchCriteria.getSearchMap().get("batchDateFrom")!=null) || (searchCriteria.getSearchMap().get("batchDateTo")!=null)){*/
	/*	 if(!StringUtils.isEmpty(input.getBatchDateFrom())&& !StringUtils.isEmpty(input.getBatchDateTo())){
			daterangeFrom =(String)searchCriteria.getSearchMap().get("batchDateFrom");
			daterangeTo= (String)searchCriteria.getSearchMap().get("batchDateTo");
			System.out.println("\nBATCH-daterangeFrom===>"+daterangeFrom+"\n");
			System.out.println("\ndaterangeTo===>"+daterangeTo+"\n");
		}
		 if(!StringUtils.isEmpty(input.getLoadedFrom())&& !StringUtils.isEmpty(input.getLoadedTo())){
			daterangeFrom =(String)searchCriteria.getSearchMap().get("loadedFrom");
			daterangeTo= (String)searchCriteria.getSearchMap().get("loadedTo");
			System.out.println("\nLOAD-daterangeFrom===>"+daterangeFrom+"\n");
			System.out.println("\ndaterangeTo===>"+daterangeTo+"\n");
		}*/
		
		 if(!StringUtils.isEmpty(input.getUnloadedFrom())&& !StringUtils.isEmpty(input.getUnloadedTo())){	
			daterangeFrom =(String)searchCriteria.getSearchMap().get("unloadedFrom");
			daterangeTo= (String)searchCriteria.getSearchMap().get("unloadedTo");
			System.out.println("\nUNLOADdaterangeFrom===>"+daterangeFrom+"\n");
			System.out.println("\ndaterangeTo===>"+daterangeTo+"\n");
		}
		 /*if(!StringUtils.isEmpty(input.getInvoiceDateFrom())&& !StringUtils.isEmpty(input.getInvoiceDateTo())){	
				daterangeFrom =(String)searchCriteria.getSearchMap().get("invoiceDateFrom");
				daterangeTo= (String)searchCriteria.getSearchMap().get("invoiceDateTo");
				System.out.println("\nUNLOADdaterangeFrom===>"+daterangeFrom+"\n");
				System.out.println("\ndaterangeTo===>"+daterangeTo+"\n");
			}*/
		if(daterangeFrom!=null && daterangeTo!=null){
			range=daterangeFrom+" to "+daterangeTo;
			if(range.length()>5){
				params.put("date", range);
			}
			
			
			String unloadDateFrom = ReportDateUtil.getFromDate(daterangeFrom);
			
			String unloadDateTo = ReportDateUtil.getFromDate(daterangeTo);
			
			params.put("unloadDateFrom", unloadDateFrom);
			
			params.put("unloadDateTo",unloadDateTo);
			
			
		}
		String Driver=(String)request.getSession().getAttribute("DriverReport");
		String TruckReport = (String) request.getSession().getAttribute("TruckReport");
		String TerminalReport = (String) request.getSession().getAttribute(
		"TerminalReport");
		String TrailerReport=(String)request.getSession().getAttribute("TrailerReport");
		if(!StringUtils.isEmpty(Driver)||!StringUtils.isEmpty(TruckReport)||!StringUtils.isEmpty(TerminalReport)||!StringUtils.isEmpty(TrailerReport)){
			String query="select obj from Location obj where id in("+input.getCompany()+")";
			List<Location> locations=genericDAO.executeSimpleQuery(query);
			StringBuffer company= new StringBuffer("");
			for(Location location:locations){
				company.append(location.getName());
				company.append(",");
			}
			if(company.length()>0){
			int c=company.lastIndexOf(",");
			company.deleteCharAt(c);
			}
			params.put("companyName", company.toString());
			/*if(StringUtils.equalsIgnoreCase(TruckReport,"Truckca")||StringUtils.equalsIgnoreCase(TerminalReport,"Terminalca")||StringUtils.equalsIgnoreCase(Driver,"Driverca")||StringUtils.equalsIgnoreCase(TrailerReport,"Trailerca")){
			params.put("companyName","");
			}*/
			String ReportName = null;
			if(StringUtils.equalsIgnoreCase(Driver,"Driverac")){
				ReportName="All Driver,Company Billing ";
			}
			if(StringUtils.equalsIgnoreCase(Driver,"Drivercc")){
				ReportName="Company Driver,Company Billing";
			}
			if(StringUtils.equalsIgnoreCase(Driver,"Driverca")){
				ReportName="Company Driver,All Billing";
			}
			if(StringUtils.equalsIgnoreCase(TruckReport,"Truckac")){
				ReportName="All Truck,Company Billing ";
			}
			if(StringUtils.equalsIgnoreCase(TruckReport,"Truckcc")){
				ReportName="Company Truck,Company Billing";
			}
			if(StringUtils.equalsIgnoreCase(TruckReport,"Truckca")){
				ReportName="Company Truck,All Billing";
			}
			if(StringUtils.equalsIgnoreCase(TerminalReport,"Terminalac")){
				ReportName="All Terminal,Company Billing ";
			}
			if(StringUtils.equalsIgnoreCase(TerminalReport,"Terminalcc")){
				ReportName="Company Terminal,Company Billing";
			}
			if(StringUtils.equalsIgnoreCase(TerminalReport,"Terminalca")){
				ReportName="Company Terminal,All Billing";
			}
			if(StringUtils.equalsIgnoreCase(TrailerReport,"Trailerac")){
				ReportName="All Trailer,Company Billing ";
			}
			if(StringUtils.equalsIgnoreCase(TrailerReport,"Trailercc")){
				ReportName="Company Trailer,Company Billing";
			}
			if(StringUtils.equalsIgnoreCase(TrailerReport,"Trailerca")){
				ReportName="Company Trailer,All Billing";
			}

			params.put("ReportName", ReportName);
			System.out.println("\n Report Name--->"+ReportName);
		}
		System.out.println("\ndate===>"+range+"\n");
		data.put("data", wrapper);
		data.put("params", params);
		request.getSession().setAttribute("data", data);
		  return data;
	}
	
	
	public List<NetReportWrapper> generateNetReport(SearchCriteria searchCriteria,NetReportInput input,HttpServletRequest request) {
	return reportService.generateNetReportData(searchCriteria,input,request);
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") NetReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
        System.out.println("\nNetReportController==Search()\n"); 
        request.getSession().removeAttribute("data");
        String batchDateFrom = ReportDateUtil.getFromDate(input.getBatchDateFrom());
		String batchDateTo = ReportDateUtil.getToDate(input.getBatchDateTo());		
		String loadDateFrom = ReportDateUtil.getFromDate(input.getLoadedFrom());
		String loadDateTo = ReportDateUtil.getToDate(input.getLoadedTo());		
		String unloadDateFrom = ReportDateUtil.getFromDate(input.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());
		String invoiceDateFrom=ReportDateUtil.getFromDate(input.getInvoiceDateFrom());
		String invoiceDateTo=ReportDateUtil.getToDate(input.getInvoiceDateTo());
		int NoOFDateRange=0;
		
       /* if(!StringUtils.isEmpty(batchDateFrom) && !StringUtils.isEmpty(batchDateTo)){
        	NoOFDateRange=NoOFDateRange+1; 
        	 System.out.println("\nbatchDate\n"); 
        }*/
        /*if(!StringUtils.isEmpty(loadDateFrom) && !StringUtils.isEmpty(loadDateTo)){
        	NoOFDateRange=NoOFDateRange+1;
        	System.out.println("\nloadDate\n");
        }*/
        if(!StringUtils.isEmpty(unloadDateFrom)&& !StringUtils.isEmpty(unloadDateTo)){
        	NoOFDateRange=NoOFDateRange+1;
        	System.out.println("\nunloadDate\n");
        }
        /*if(!StringUtils.isEmpty(invoiceDateFrom)&& !StringUtils.isEmpty(invoiceDateTo)){
        	NoOFDateRange=NoOFDateRange+1;
        	System.out.println("\nunloadDate\n");
        }*/
        if(NoOFDateRange==0){
        	request.setAttribute("error","Please select a date range!");
        	Map criterias = new HashMap();
    		criterias.put("type", 3);
    		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
    		criterias.clear();
    		criterias.put("type", 4);
    		model.addAttribute("terminals",genericDAO.findByCriteria(Location.class, criterias, "name",false));
    		criterias.put("type", 1);
    		model.addAttribute("trucks",genericDAO.findByCriteria(Vehicle.class, criterias, "unit", false));
    		criterias.clear();
    		criterias.put("catagory", 2);
    		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "lastName", false));
    		criterias.clear();
    		model.addAttribute("ticketStatuss", listStaticData("TICKET_STATUS"));
        	return "reportuser/report/netReport";
        }
        if(NoOFDateRange>1){
        	request.setAttribute("error","Please select only one date range!");
        	Map criterias = new HashMap();
    		criterias.put("type", 3);
    		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
    		criterias.clear();
    		criterias.put("type", 4);
    		model.addAttribute("terminals",genericDAO.findByCriteria(Location.class, criterias, "name",false));
    		criterias.put("type", 1);
    		model.addAttribute("trucks",genericDAO.findByCriteria(Vehicle.class, criterias, "unit", false));
    		criterias.clear();
    		criterias.put("catagory", 2);
    		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "lastName", false));
    		criterias.clear();
        	return "reportuser/report/netReport";
        }
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		String driver=request.getParameter("Driver");
		String truck=request.getParameter("Truck");
		String terminal=request.getParameter("Terminal");
		String company=request.getParameter("Company");
		String trailer=request.getParameter("Trailer");
		
		if(!StringUtils.isEmpty(driver)&&(StringUtils.equalsIgnoreCase(driver, "Drivercc")||StringUtils.equalsIgnoreCase(driver, "Driverca"))){
			if(!StringUtils.isEmpty(input.getCompany())){
			String str[]=input.getCompany().split(",");
			if(str.length>1){
				request.getSession().setAttribute("error","Please select only one company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			}
			else{
				request.getSession().setAttribute("error","Please select company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			
		}
		if(!StringUtils.isEmpty(truck)&&(StringUtils.equalsIgnoreCase(truck, "Truckcc")||StringUtils.equalsIgnoreCase(truck, "Truckca"))){
			if(!StringUtils.isEmpty(input.getCompany())){
			String str[]=input.getCompany().split(",");
			if(str.length>1){
				request.getSession().setAttribute("error","Please select only one company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			}
			else{
				request.getSession().setAttribute("error","Please select company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			
		}
		if(!StringUtils.isEmpty(terminal)&&(StringUtils.equalsIgnoreCase(terminal, "Terminalcc")||StringUtils.equalsIgnoreCase(terminal, "Terminalca"))){
			if(!StringUtils.isEmpty(input.getCompany())){
			String str[]=input.getCompany().split(",");
			if(str.length>1){
				request.getSession().setAttribute("error","Please select only one company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			}
			else{
				request.getSession().setAttribute("error","Please select company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			
		}
		if(!StringUtils.isEmpty(trailer)&&(StringUtils.equalsIgnoreCase(trailer, "Trailercc")||StringUtils.equalsIgnoreCase(trailer, "Trailerca"))){
			if(!StringUtils.isEmpty(input.getCompany())){
			String str[]=input.getCompany().split(",");
			if(str.length>1){
				request.getSession().setAttribute("error","Please select only one company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			}
			else{
				request.getSession().setAttribute("error","Please select company");
				return "redirect:"+"/reportuser/report/netreport/start.do";
			}
			
		}
		
		if(driver!=null){
			request.getSession().setAttribute("DriverReport", driver); 
		}
		else{
			request.getSession().removeAttribute("DriverReport");
		}
		if(truck!=null){
			request.getSession().setAttribute("TruckReport", truck); 
		}
		else{
			request.getSession().removeAttribute("TruckReport");
		}
		if(terminal!=null){
			request.getSession().setAttribute("TerminalReport", terminal); 
		}else{
			request.getSession().removeAttribute("TerminalReport");
		}
		if(company!=null){
			request.getSession().setAttribute("CompanyReport", company); 
		}else{
			request.getSession().removeAttribute("CompanyReport");
		}
		if(trailer!=null){
			request.getSession().setAttribute("TrailerReport", trailer); 
		}else{
			request.getSession().removeAttribute("TrailerReport");
		}
		
		
		
		
		criteria.setPageSize(1000);
		try {
			Map<String, Object> datas = generateData(criteria, request,input);
		    if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=billinghistory." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint=null;
			if(driver!=null)
			 jasperPrint = dynamicReportService.getJasperPrintFromFile("netReportdriver",(List)datas.get("data"), params, request);
			if(truck!=null)
				 jasperPrint = dynamicReportService.getJasperPrintFromFile("netReporttruck",(List)datas.get("data"), params, request);
			if(terminal!=null)
				 jasperPrint = dynamicReportService.getJasperPrintFromFile("netReportterminal",(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			if(company!=null)
				 jasperPrint = dynamicReportService.getJasperPrintFromFile("netReportcompany",(List)datas.get("data"), params, request);
			if(trailer!=null)
				jasperPrint = dynamicReportService.getJasperPrintFromFile("netReporttrailer",(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			return "reportuser/report/netreport/"+type;
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
			System.out.println("\nNetReportController==export()==type===>"+type+"\n"); 
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
			criteria.setPageSize(10000);
			String DriverReport=(String)request.getSession().getAttribute("DriverReport");
			String TruckReport=(String)request.getSession().getAttribute("TruckReport");
			String TerminalReport=(String)request.getSession().getAttribute("TerminalReport");
			String CompanyReport=(String)request.getSession().getAttribute("CompanyReport");
			String TrailerReport=(String)request.getSession().getAttribute("TrailerReport");
			NetReportInput input = (NetReportInput)request.getSession().getAttribute("input");
			try {
				//Map<String,Object> datas = generateData(criteria, request, input);
				Map<String,Object> datas =null;
				if(request.getSession().getAttribute("data")==null){
				datas = generateData(criteria, request, input);
				}
				else{
					datas=(Map<String,Object>)request.getSession().getAttribute("data");
				}
				//List propertyList = (List<String>) request.getSession()
				//		.getAttribute("propertyList");
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= NetReport." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Map<String, Object> params = (Map<String,Object>)datas.get("params");
				/*if (!type.equals("print") &&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("fuellogReport",
							(List)datas.get("data"), params, type, request);
				}*/
				/*if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("netReportdriver"+"pdf",
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("csv")) {
					out = dynamicReportService.generateStaticReport("netReportdriver"+"csv",
							(List)datas.get("data"), params, type, request);
				}*/
				/*else {
					out = dynamicReportService.generateStaticReport("netReportdriver",
							(List)datas.get("data"), params, type, request);
				}*/
				if(DriverReport!=null){
					if (type.equals("pdf")) {
						out = dynamicReportService.generateStaticReport("netReportdriverpdf",
								(List)datas.get("data"), params, type, request);
					}
					else{
					out = dynamicReportService.generateStaticReport("netReportdriver",
							(List)datas.get("data"), params, type, request);
					}
					
				}
				if(TruckReport!=null){
					
					if (type.equals("pdf")) {
						out = dynamicReportService.generateStaticReport("netReporttruckpdf",
								(List)datas.get("data"), params, type, request);
					}else{
					out = dynamicReportService.generateStaticReport("netReporttruck",
							(List)datas.get("data"), params, type, request);
					}
				}
				if(TerminalReport!=null){
					if (type.equals("pdf")) {
						out = dynamicReportService.generateStaticReport("netReportterminalpdf",
								(List)datas.get("data"), params, type, request);
					}
					else{
					out = dynamicReportService.generateStaticReport("netReportterminal",
							(List)datas.get("data"), params, type, request);
					}
				}
				if(CompanyReport!=null){
					if (type.equals("pdf")) {
						out = dynamicReportService.generateStaticReport("netReportcompanypdf",
								(List)datas.get("data"), params, type, request);
					}
					else{
					out = dynamicReportService.generateStaticReport("netReportcompany",
							(List)datas.get("data"), params, type, request);
					}
				}
				if(TrailerReport!=null){
					if (type.equals("pdf")) {
						out = dynamicReportService.generateStaticReport("netReporttrailerpdf",
								(List)datas.get("data"), params, type, request);
					}
					else{
					out = dynamicReportService.generateStaticReport("netReporttrailer",
							(List)datas.get("data"), params, type, request);
					}
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
	public NetReportInput setupModel(HttpServletRequest request) {
		return new NetReportInput();
	}

}
