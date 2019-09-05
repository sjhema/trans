package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayroll;
import com.primovision.lutransport.model.hrreport.UpdatedDriverPay;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;



@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/driverpay")
public class PayRollReport extends BaseController{
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
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
		
		criterias.put("name","Driver");
		EmployeeCatagory catagory=genericDAO.getByCriteria(EmployeeCatagory.class, criterias);
		if(catagory==null){
			request.getSession().setAttribute("error","Driver category not available" );
			return "hr/report/driverpayhistory";
		}
		criterias.clear();
		//criterias.put("catagory.id", catagory.getId());
		//criterias.put("status",1);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		
		return "hr/report/driverpayhistory";
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String displayDriverPayroll(HttpServletRequest request,Model model,HttpServletResponse response){
		String frombatch=request.getParameter("fromDate");
		String tobatch=request.getParameter("toDate");
		//String payrollDate=request.getParameter("payrollDate");
		String type=request.getParameter("type");
		String reportType =  request.getParameter("reportType");
		//DriverPayroll payroll=genericDAO.getById(DriverPayroll.class, Long.valueOf(invoiceId));
		try{
			
			Map params = new HashMap();
			
			StringBuilder driverPayQuery = new StringBuilder("select obj from DriverPay obj where 1=1");
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				driverPayQuery.append(" and drivername='").append(request.getParameter("driver")).append("'");
			}
			if(!StringUtils.isEmpty(request.getParameter("company"))){
				driverPayQuery.append(" and company='").append(request.getParameter("company")).append("'");
				Location companyObj = genericDAO.getById(Location.class, Long.valueOf(request.getParameter("company")));
				params.put("company", companyObj.getName()); 
			}
			if(!StringUtils.isEmpty(request.getParameter("terminal"))){
				driverPayQuery.append(" and terminal='").append(request.getParameter("terminal")).append("'");
			}
			if(!StringUtils.isEmpty(frombatch)){				
				frombatch=ReportDateUtil.getFromDate(frombatch);
				driverPayQuery.append(" and payRollBatch >='").append(frombatch).append("'");
			}
			if(!StringUtils.isEmpty(tobatch)){				
				tobatch=ReportDateUtil.getFromDate(tobatch);
				driverPayQuery.append(" and payRollBatch <='").append(tobatch).append("'");
			}
			
			
			driverPayQuery.append(" order by obj.drivername asc"); 
			
			System.out.println("****** The query is "+driverPayQuery.toString());
			
			request.getSession().setAttribute("payhistoryQuery",driverPayQuery.toString() );	
			request.getSession().setAttribute("reportType",reportType);
			
			List<DriverPay> datas=genericDAO.executeSimpleQuery(driverPayQuery.toString());
			
			if (datas!=null && datas.size()>0) {						
			params.put("payRollBatchFrom",request.getParameter("fromDate"));	
			params.put("payRollBatchTo",request.getParameter("toDate"));	
			request.getSession().setAttribute("payhistoryParam",params);
			
			if (StringUtils.isEmpty(type))
				type = "html";
			
			
				
				
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "driverpayhistory" + "." + type);
			}
	 
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			String report;
			
			report="driverpayallhistory";			
			
			out = dynamicReportService.generateStaticReport(report,
					datas, params, type, request);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
			}
			else{
				return "blank/subcontractorexpiredratelist";
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

			
	}
	
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/searchWeeklyPayroll.do")
	public String displayWeeklyPayroll(HttpServletRequest request,Model model,HttpServletResponse response){
		String frombatch=request.getParameter("fromDate");
		String tobatch=request.getParameter("toDate");		
		String type=request.getParameter("type");
		String reportType =  request.getParameter("reportType");
		//DriverPayroll payroll=genericDAO.getById(DriverPayroll.class, Long.valueOf(invoiceId));
		try{
			
			Map params = new HashMap();
			
			StringBuilder driverPayQuery = new StringBuilder("select obj from WeeklyPayDetail obj where 1=1");
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				driverPayQuery.append(" and driver='").append(request.getParameter("driver")).append("'");
			}
			if(!StringUtils.isEmpty(request.getParameter("company"))){
				driverPayQuery.append(" and company='").append(request.getParameter("company")).append("'");
				Location companyObj = genericDAO.getById(Location.class, Long.valueOf(request.getParameter("company")));
				params.put("company", companyObj.getName());
			}
			String category =  request.getParameter("category");
			if(!StringUtils.isEmpty(category)) {
				String categoryQuery = "select obj from EmployeeCatagory obj where obj.id=" + category;
				List<EmployeeCatagory> categoryList = genericDAO.executeSimpleQuery(categoryQuery);
				String categoryName = categoryList.get(0).getName();
				driverPayQuery.append(" and category='").append(categoryName).append("'");
			}
			if(!StringUtils.isEmpty(request.getParameter("terminal"))){
				driverPayQuery.append(" and terminal='").append(request.getParameter("terminal")).append("'");
				Location terminalObj = genericDAO.getById(Location.class, Long.valueOf(request.getParameter("terminal")));
				params.put("terminal", terminalObj.getName());
			}
			if(!StringUtils.isEmpty(frombatch)){				
				frombatch=ReportDateUtil.getFromDate(frombatch);
				driverPayQuery.append(" and checkDate >='").append(frombatch).append("'");
			}
			if(!StringUtils.isEmpty(tobatch)){				
				tobatch=ReportDateUtil.getFromDate(tobatch);
				driverPayQuery.append(" and checkDate <='").append(tobatch).append("'");
			}
			
			
			driverPayQuery.append(" order by obj.driver asc"); 
			
			System.out.println("****** The query is "+driverPayQuery.toString());		
			
			List<DriverPay> datas=genericDAO.executeSimpleQuery(driverPayQuery.toString());
			
			request.getSession().setAttribute("payhistoryQuery",driverPayQuery.toString() );
			request.getSession().setAttribute("reportType",reportType);
			
			if (datas!=null && datas.size()>0) {			
			
			params.put("payrollNumber",request.getParameter("fromDate")+" to "+ request.getParameter("toDate"));	
			
			
			
			request.getSession().setAttribute("payhistoryParam",params);
			if (StringUtils.isEmpty(type))
				type = "html";
			
			
				
				
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "weeklypayroll" + "." + type);
			}
	 
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			String report;
			
			report="weeklypay";			
			
			out = dynamicReportService.generateStaticReport(report,
					datas, params, type, request);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
			}
			else{
				return "blank/subcontractorexpiredratelist";
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}			
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/searchHourlyPayroll.do")
	public String displayHourlyPayroll(HttpServletRequest request,Model model,HttpServletResponse response){
		String frombatch=request.getParameter("fromDate");
		String tobatch=request.getParameter("toDate");
		
		String type=request.getParameter("type");
		String reportType =  request.getParameter("reportType");
		//DriverPayroll payroll=genericDAO.getById(DriverPayroll.class, Long.valueOf(invoiceId));
		try{
			
			Map params = new HashMap();
			
			StringBuilder driverPayQuery = new StringBuilder("select obj from HourlyPayrollInvoiceDetails obj where 1=1");
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				driverPayQuery.append(" and driver='").append(request.getParameter("driver")).append("'");
			}
			if(!StringUtils.isEmpty(request.getParameter("company"))){
				driverPayQuery.append(" and companyLoc='").append(request.getParameter("company")).append("'");
				Location companyObj = genericDAO.getById(Location.class, Long.valueOf(request.getParameter("company")));
				params.put("company", companyObj.getName()); 
			}
			String category =  request.getParameter("category");
			if(!StringUtils.isEmpty(category)) {
				String categoryQuery = "select obj from EmployeeCatagory obj where obj.id=" + category;
				List<EmployeeCatagory> categoryList = genericDAO.executeSimpleQuery(categoryQuery);
				String categoryName = categoryList.get(0).getName();
				driverPayQuery.append(" and category='").append(categoryName).append("'");
			}
			if(!StringUtils.isEmpty(request.getParameter("terminal"))){
				driverPayQuery.append(" and terminalLoc='").append(request.getParameter("terminal")).append("'");
				
			}
			if(!StringUtils.isEmpty(frombatch)){				
				frombatch=ReportDateUtil.getFromDate(frombatch);
				driverPayQuery.append(" and payRollCheckDate >='").append(frombatch).append("'");
			}
			if(!StringUtils.isEmpty(tobatch)){				
				tobatch=ReportDateUtil.getFromDate(tobatch);
				driverPayQuery.append(" and payRollCheckDate <='").append(tobatch).append("'");
			}
			
			
			driverPayQuery.append(" order by obj.driver asc"); 
			
			System.out.println("****** The query is "+driverPayQuery.toString());		
			
			List<DriverPay> datas=genericDAO.executeSimpleQuery(driverPayQuery.toString());
			
			request.getSession().setAttribute("payhistoryQuery",driverPayQuery.toString() );
			request.getSession().setAttribute("reportType",reportType);
			
			if (datas!=null && datas.size()>0) {			
			
				params.put("payRollBatchFrom",request.getParameter("fromDate"));	
				params.put("payRollBatchTo",request.getParameter("toDate"));	
				
			request.getSession().setAttribute("payhistoryParam",params);
			if (StringUtils.isEmpty(type))
				type = "html";
			
			
				
				
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "hourlypayroll" + "." + type);
			}
	 
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			String report;
			
			report="employeepayrollReport";			
			
			out = dynamicReportService.generateStaticReport(report,
					datas, params, type, request);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
			}
			else{
				return "blank/subcontractorexpiredratelist";
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

			
	}
	
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String downloadPayrollReort(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		
		
		String type=request.getParameter("type");	
		String driverPayQuery = (String)request.getSession().getAttribute("payhistoryQuery");
		String reportType = (String) request.getSession().getAttribute("reportType");
		
		
		try {
			
			Map params = new HashMap();
			params= (Map)request.getSession().getAttribute("payhistoryParam");
			
			
			System.out.println("****** The query is "+driverPayQuery);
			
			List<DriverPay> datas=genericDAO.executeSimpleQuery(driverPayQuery);
			
			if (datas!=null && datas.size()>0) {			
				String report;
			if(reportType.equals("1")){		
				report="driverpayallhistory";
			}
			else if(reportType.equals("2")){
				report="employeepayrollReport";
			}
			else{
				report="weeklypay";
			}
			
			
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				if(reportType.equals("1")){	
					response.setHeader("Content-Disposition",
						"attachment;filename= driverpayhistory." + type);
				}
				else if(reportType.equals("2")){
					response.setHeader("Content-Disposition",
							"attachment;filename= hourlypayhistory." + type);
				}
				else{
					response.setHeader("Content-Disposition",
							"attachment;filename= weeklypayhistory." + type);
				}
				
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						datas, params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						datas, params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport(report,
						datas, params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			
			out.close();
			return null;
		}
		else{
			return null;
		}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		System.out.println("***** enetered here");
		if("findDrivers".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("category"))){
				List<Driver> drivers=new ArrayList<Driver>();
				Map criterias = new HashMap();
				EmployeeCatagory empCategory = genericDAO.getById(EmployeeCatagory.class, Long.parseLong(request.getParameter("category")));
				criterias.put("catagory",empCategory);
				criterias.put("status",1);
				drivers=genericDAO.findByCriteria(Driver.class,criterias,"fullName",false);				
				Gson gson = new Gson();
				return gson.toJson(drivers);				
			}
			else{
				Map criterias = new HashMap();				
				List<Driver> drivers=new ArrayList<Driver>();
				criterias.put("status",1);
				drivers=genericDAO.findByCriteria(Driver.class,criterias,"fullName",false);
				Gson gson = new Gson();
				return gson.toJson(drivers);				
			}
			
		}
		
		if("findReportType".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<String> returnDataList=new ArrayList<String>();
				List<Driver> drvObjList = genericDAO.executeSimpleQuery("Select obj from Driver obj where  obj.status=1 and obj.fullName='"+request.getParameter("driver")+"'");
				for(Driver drvObj:drvObjList){
					returnDataList.add(drvObj.getPayTerm());
				}
				Gson gson = new Gson();
				return gson.toJson(returnDataList);
			}
		}
		return "";
	}

	
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request) {
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		String driversmul=(String)criteria.getSearchMap().get("driversmul");
		 String sum= (String) criteria.getSearchMap().get("summary");
		 String specialDetails= (String) criteria.getSearchMap().get("specdetail");
		String payrollDate=(String) criteria.getSearchMap().get("payrollDate");
		String driver=(String) criteria.getSearchMap().get("driver");
		String frombatch=(String) criteria.getSearchMap().get("fromDate");
		String tobatch=(String) criteria.getSearchMap().get("toDate");
		String company=(String) criteria.getSearchMap().get("company");
		String terminal=(String) criteria.getSearchMap().get("terminal");
		 String expire1= (String) criteria.getSearchMap().get("expire");
		 String status=(String)criteria.getSearchMap().get("pay");
		 String sta=(String)criteria.getSearchMap().get("stat");
			frombatch=ReportDateUtil.getFromDate(frombatch);
			tobatch=ReportDateUtil.getFromDate(tobatch);
			payrollDate=ReportDateUtil.getFromDate(payrollDate);
		 if(!StringUtils.isEmpty(status)){
			 status="1";
		 }else{
			 status="2";
		 }
		 
		
       /* List<DriverPay> summary = wrapper.getDriverPays();
		
        Comparator<DriverPay> comparator=new Comparator<DriverPay>() {
			@Override
			public int compare(DriverPay o1, DriverPay o2) {
				return  o1.getCompanyname().compareTo(o2.getCompanyname());
			}
		};
		
		Comparator<DriverPay> comparator1=new Comparator<DriverPay>() {
			@Override
			public int compare(DriverPay o1, DriverPay o2) {
				return  o1.getTerminalname().compareTo(o2.getTerminalname());
			}
		};
        
		Comparator<DriverPay> comparator2=new Comparator<DriverPay>() {
			@Override
			public int compare(DriverPay o1, DriverPay o2) {
				return  o1.getDrivername().compareTo(o2.getDrivername());
			}
		};*/
		
		/*ComparatorChain chain = new ComparatorChain();  
		
		if(!StringUtils.isEmpty(company)){
			chain.addComparator(comparator);
			chain.addComparator(comparator1);
		}
		chain.addComparator(comparator2);
		Collections.sort(summary, chain);
		
		wrapper.setDriverPays(summary);
        */
        
        
       // 16th Jan 2016 - pay stub multiple fix
		 //StringBuilder driverpayfreezquery = new StringBuilder("select  obj from DriverPayFreezWrapper obj where 1=1 and obj.payRollBatch='"+payrollDate+"' and obj.billBatchDateFrom>='
		 //"+frombatch+"' and obj.billBatchDateTo<='"+tobatch+"'");
		 StringBuilder driverpayfreezquery = new StringBuilder("select  obj from DriverPayFreezWrapper obj where 1=1 and obj.payRollBatch='"+payrollDate+"' and obj.billBatchDateFrom='"+frombatch+"' and obj.billBatchDateTo='"+tobatch+"'");
		 if(!StringUtils.isEmpty(company)){
			 driverpayfreezquery.append(" and obj.company="+company);
		 }
		 if(!StringUtils.isEmpty(terminal)){
			 driverpayfreezquery.append(" and obj.terminal="+terminal);
		 }
		 if(!StringUtils.isEmpty(driver)){
			 driverpayfreezquery.append(" and obj.drivername='"+driver+"'");
		 }
		 if(!StringUtils.contains(sum, "true")){			
			 driverpayfreezquery.append(" order by obj.drivername,obj.isMainRow");
		 }
		 else {
			 driverpayfreezquery.append(" and obj.isMainRow='yes' order by obj.drivername");
		 }
		 
		 System.out.println("********** the freeze query is "+driverpayfreezquery.toString());
		 
		 		  List<DriverPayFreezWrapper> driverpays= genericDAO.executeSimpleQuery(driverpayfreezquery.toString());
		 			
		 		  List<DriverPayFreezWrapper> driverPayNew=new ArrayList<DriverPayFreezWrapper>();
		 
		           Map empmap=new HashMap();
		           List<Double> parameter1=new ArrayList<Double>();
		           List<Double> parameter2=new ArrayList<Double>();
		           List<String> numberofsickdays=new ArrayList<String>();
		           List<String> numberofvactiondays=new ArrayList<String>();
		           
		           List<String> calculateDedcution=new ArrayList<String>();
		           
		           List<Double> bonusamount=new ArrayList<Double>();
		           List<Double> bonusamount0=new ArrayList<Double>();
		           List<Double> bonusamount1=new ArrayList<Double>();
		           List<Double> bonusamount2=new ArrayList<Double>();
		           List<Double> bonusamount3=new ArrayList<Double>();
		           List<Double> bonusamount4=new ArrayList<Double>();
		           
		           List<String> bonusnotes=new ArrayList<String>();
		           List<String> bonusnotes0=new ArrayList<String>();
		           List<String> bonusnotes1=new ArrayList<String>();
		           List<String> bonusnotes2=new ArrayList<String>();
		           List<String> bonusnotes3=new ArrayList<String>();
		           List<String> bonusnotes4=new ArrayList<String>();
		           
		           List<String> bonustype=new ArrayList<String>();
		           List<String> bonustype0=new ArrayList<String>();
		           List<String> bonustype1=new ArrayList<String>();
		           List<String> bonustype2=new ArrayList<String>();
		           List<String> bonustype3=new ArrayList<String>();
		           List<String> bonustype4=new ArrayList<String>();
		           
		           
		           List<Double> miscamount=new ArrayList<Double>();
		           List<Double> miscamount0=new ArrayList<Double>();
		           List<Double> miscamount1=new ArrayList<Double>();
		           List<Double> miscamount2=new ArrayList<Double>();
		           List<Double> miscamount3=new ArrayList<Double>();
		           List<Double> miscamount4=new ArrayList<Double>();
		           List<Double> miscamount5=new ArrayList<Double>();
		           
		           List<String> miscnotes=new ArrayList<String>();
		           List<String> miscnotes0=new ArrayList<String>();
		           List<String> miscnotes1=new ArrayList<String>();
		           List<String> miscnotes2=new ArrayList<String>();
		           List<String> miscnotes3=new ArrayList<String>();
		           List<String> miscnotes4=new ArrayList<String>();
		           List<String> miscnotes5=new ArrayList<String>();
		           
		           List<String> holidayName=new ArrayList<String>();
		           List<String> holidayDateFrom=new ArrayList<String>();
		           List<String> holidayDateTo=new ArrayList<String>();
		           List<Double> holidayamount=new ArrayList<Double>();
		           
		           List<String> reimburseNote=new ArrayList<String>();
		           List<Double> reimburseAmount=new ArrayList<Double>();
		           
		           List<String> quatarNote=new ArrayList<String>();
		           List<Double> quatarAmount=new ArrayList<Double>();
		           
		           
		           List<Double> deductionAmounts=new ArrayList<Double>();
		          
		           Double vacationSum=0.0;
		           Double sickSum=0.0;
					Double bonusSum=0.0;					
					Double miscamtSum=0.0;
					Double holidaySum=0.0;
					
		           //for(String driverFullname:drivers){
					Map criti = new HashMap();
					String tempDriverName = ""; 
			if(!StringUtils.contains(sum, "true")){
		        for(int i=0;i<driverpays.size();i++){		        	
		        	DriverPayFreezWrapper driverPay = (DriverPayFreezWrapper) driverpays.get(i);
		        	if(tempDriverName.equals(""))
		        		tempDriverName = driverPay.getDrivername();
		        	else{
		        		if(tempDriverName == driverPay.getDrivername()){
		        			if(StringUtils.contains(specialDetails, "true")){
		        				 driverPayNew.add(driverPay);
		        			 }
		        			continue;
		        		}else
		        			tempDriverName = driverPay.getDrivername();
		        	}
		        	criti.clear();
		        	criti.put("status",1);
		        	criti.put("fullName",driverPay.getDrivername());
		        //Driver driver3 = genericDAO.getByCriteria(Driver.class, criti); 
		        	//System.out.println("************ the driver name is "+driver3.getFullName());
		        	    Double sickParsonalAmount=0.00;
						Double vacationAmount=0.00;
						// Bereavement change - driver
						Double bereavementAmount=0.00;
					   // Worker comp change - driver
						Double workerCompAmount=0.00;
						Integer numberOfSickDays = 0;
						Integer numberOfVactionDays = 0;
						
						Double bonusAmount=0.0;
						Double bonusAmount0=null;
						Double bonusAmount1=null;
						Double bonusAmount2=null;
						Double bonusAmount3=null;
						Double bonusAmount4=null;
						
						String bonusTypeName="";
						String bonusTypeName0="";
						String bonusTypeName1="";
						String bonusTypeName2="";
						String bonusTypeName3="";
						String bonusTypeName4="";
						
						String bonusNotes="";
						String bonusNotes0="";
						String bonusNotes1="";
						String bonusNotes2="";
						String bonusNotes3="";
						String bonusNotes4="";						
						
						Double holidayAmount=0.0;
						String holidayname="";
						String holidaydateFrom="";
						String holidaydateTo="";
						
						
						String reimburseNotes="";
						Double reimburseAmt=0.0;
						
						
						String qutarNotes="";
						Double qutarAmt=0.0;
						
						Double miscamt=0.0;
						Double miscamt0=null;
						Double miscamt1=null;
						Double miscamt2=null;
						Double miscamt3=null;
						Double miscamt4=null;
						Double miscamt5=null;
						
						String miscnote=""; 
						String miscnote0="";
						String miscnote1="";
						String miscnote2="";
						String miscnote3="";
						String miscnote4="";
						String miscnote5="";
						
						empmap.clear();
						
							
							
							sickParsonalAmount= driverPay.getSickParsonalAmount();
								    
							numberOfSickDays=driverPay.getNumberOfSickDays();
							
							vacationAmount=driverPay.getVacationAmount();
							// Bereavement change - driver
							bereavementAmount=driverPay.getBereavementAmount();
							// Worker comp change - driver
							workerCompAmount=driverPay.getWorkerCompAmount();
									
							numberOfVactionDays=driverPay.getNumberOfVactionDays();
							
							bonusAmount=driverPay.getBonusAmount();
							bonusAmount0=driverPay.getBonusAmount0();
							bonusAmount1=driverPay.getBonusAmount1();
							bonusAmount2=driverPay.getBonusAmount2();
							bonusAmount3=driverPay.getBonusAmount3();
							bonusAmount4=driverPay.getBonusAmount4();
							
							bonusTypeName=driverPay.getBonusTypeName();
							bonusTypeName0=driverPay.getBonusTypeName0();
							bonusTypeName1=driverPay.getBonusTypeName1();
							bonusTypeName2=driverPay.getBonusTypeName2();
							bonusTypeName3=driverPay.getBonusTypeName3();
							bonusTypeName4=driverPay.getBonusTypeName4();
							
							bonusNotes=driverPay.getBonusNotes();
							bonusNotes0=driverPay.getBonusNotes0();
							bonusNotes1=driverPay.getBonusNotes1();
							bonusNotes2=driverPay.getBonusNotes2();
							bonusNotes3=driverPay.getBonusNotes3();
							bonusNotes4=driverPay.getBonusNotes4();							
							
							qutarNotes = driverPay.getQutarNotes();
							qutarAmt = driverPay.getQuatarAmount();									
							
								
						    reimburseNotes = driverPay.getReimburseNotes();
							reimburseAmt = driverPay.getReimburseAmt();									
																	
							
							miscamt=driverPay.getMiscamt();
							miscamt0=driverPay.getMiscamt0();
							miscamt1=driverPay.getMiscamt1();
							miscamt2=driverPay.getMiscamt2();
							miscamt3=driverPay.getMiscamt3();
							miscamt4=driverPay.getMiscamt4();
							miscamt5=driverPay.getMiscamt5();
							
							miscnote=driverPay.getMiscnote(); 
							miscnote0=driverPay.getMiscnote0();
							miscnote1=driverPay.getMiscnote1();
							miscnote2=driverPay.getMiscnote2();
							miscnote3=driverPay.getMiscnote3();
							miscnote4=driverPay.getMiscnote4();
							miscnote5=driverPay.getMiscnote5();		
									
							holidayAmount=driverPay.getHolidayAmount();																	
							holidayname = driverPay.getHolidayname();
							holidaydateFrom = driverPay.getHolidaydateFrom();
							holidaydateTo = driverPay.getHolidaydateTo();
										
							double deductionAmount = 0.0;
							deductionAmount = driverPay.getProbationDeductionAmount();
						//}		
									
					
						miscamount.add(MathUtil.roundUp(miscamt,2));					
						miscamount0.add(miscamt0);			
						miscamount1.add(miscamt1);
						miscamount2.add(miscamt2);
						miscamount3.add(miscamt3);
						miscamount4.add(miscamt4);
						miscamount5.add(miscamt5);
						
						miscnotes0.add(miscnote0);
						miscnotes1.add(miscnote1);
						miscnotes2.add(miscnote2);
						miscnotes3.add(miscnote3);
						miscnotes4.add(miscnote4);
						miscnotes5.add(miscnote5);
						
						bonusamount.add(MathUtil.roundUp(bonusAmount,2));
						bonusamount0.add(bonusAmount0);
						bonusamount1.add(bonusAmount1);
						bonusamount2.add(bonusAmount2);
						bonusamount3.add(bonusAmount3);
						bonusamount4.add(bonusAmount4);
						
						bonusnotes0.add(bonusNotes0);
						bonusnotes1.add(bonusNotes1);
						bonusnotes2.add(bonusNotes2);
						bonusnotes3.add(bonusNotes3);
						bonusnotes4.add(bonusNotes4);
						
						bonustype0.add(bonusTypeName0);
						bonustype1.add(bonusTypeName1);
						bonustype2.add(bonusTypeName2);
						bonustype3.add(bonusTypeName3);
						bonustype4.add(bonusTypeName4);
						
						holidayamount.add(MathUtil.roundUp(holidayAmount,2));
						holidayDateTo.add(holidaydateTo);
						holidayDateFrom.add(holidaydateFrom);
						holidayName.add(holidayname);
						
						reimburseAmount.add(reimburseAmt);
						reimburseNote.add(reimburseNotes);
						
						quatarAmount.add(qutarAmt);
						quatarNote.add(qutarNotes);
						
						numberofsickdays.add(String.valueOf(numberOfSickDays));
						numberofvactiondays.add(String.valueOf(numberOfVactionDays));
						
						parameter1.add(MathUtil.roundUp(sickParsonalAmount,2));
						parameter2.add(MathUtil.roundUp(vacationAmount,2));
						deductionAmounts.add(MathUtil.roundUp(deductionAmount,2));	
						
						Double transportationamount = 0.0;						
						
						transportationamount = 	driverPay.getTransportationAmount();
						
						Double transportationAmountDiff = (driverPay.getTransportationAmountDiff() == null ? 0.0 : driverPay.getTransportationAmountDiff());
						if(StringUtils.contains(specialDetails, "true")){
							driverPay.setTransAmountSpc(MathUtil.roundUp(transportationamount,2));
							driverPay.setTransAmountDiffSpc(MathUtil.roundUp(transportationAmountDiff,2));
							
							driverPay.setProbdeductionAmountSpc(MathUtil.roundUp(deductionAmount,2));
							driverPay.setSubTotalAmountSpc(MathUtil.roundUp((driverPay.getTransAmountSpc()
									-driverPay.getProbdeductionAmountSpc())
									+driverPay.getTransAmountDiffSpc(),2));
							driverPay.setMiscAmountSpc(MathUtil.roundUp(miscamt,2));
							driverPay.setBonusAmountSpc(MathUtil.roundUp(bonusAmount,2));
							driverPay.setPtodAmountSpc(MathUtil.roundUp(sickParsonalAmount,2));
							driverPay.setHolidayAmountSpc(MathUtil.roundUp(holidayAmount,2));
							driverPay.setTotalAmountSpc(MathUtil.roundUp(((driverPay.getTransAmountSpc()
									+driverPay.getPtodAmountSpc()+driverPay.getBonusAmountSpc()
									+driverPay.getHolidayAmountSpc()+driverPay.getMiscAmountSpc())
									-driverPay.getProbdeductionAmountSpc())
									+driverPay.getTransAmountDiffSpc(),2));
							driverPay.setVacationAmountSpc(MathUtil.roundUp(vacationAmount,2));
							// Bereavement change - driver
							driverPay.setBereavementAmountSpc(MathUtil.roundUp(bereavementAmount,2));
							// Worker comp change - driver
							driverPay.setWorkerCompSpc(MathUtil.roundUp(workerCompAmount,2));
							driverPay.setReimAmountSpc(reimburseAmt);
							driverPay.setQuarterAmountSpc(qutarAmt);	
							
							populateChangedAmount(driverPay);
							
							driverPayNew.add(driverPay);							
						}
						
		           }      
		        
			}
		           
		           
		           
		           params.put("numberOfSickdays",numberofsickdays);
		           params.put("numberOfVacation",numberofvactiondays);
		           
		         
		           if(driverpays!=null && driverpays.size()>0){
		            params.put("totalRowCount",driverpays.get(0).getTotalRowCount());
		            params.put("payRollBatch",driverpays.get(0).getPayRollBatchString());
		            params.put("batchDateFrom",driverpays.get(0).getBillBatchDateFromString());
		            params.put("batchDateTo",driverpays.get(0).getBillBatchDateToString());
		            params.put("company",driverpays.get(0).getCompanyname());
		           }
		           
		           params.put("miscNotes0",miscnotes0);
		           params.put("miscNotes1",miscnotes1);
		           params.put("miscNotes2",miscnotes2);
		           params.put("miscNotes3",miscnotes3);
		           params.put("miscNotes4",miscnotes4);
		           params.put("miscNotes5",miscnotes5);
		           
		           params.put("miscAmount", miscamount);
		           params.put("miscAmount0",miscamount0);
		           params.put("miscAmount1",miscamount1);
		           params.put("miscAmount2",miscamount2);
		           params.put("miscAmount3",miscamount3);
		           params.put("miscAmount4",miscamount4);
		           params.put("miscAmount5",miscamount5);
		           	           
		           params.put("bonusAmount", bonusamount);
		           params.put("bonusAmount0", bonusamount0);
		           params.put("bonusAmount1", bonusamount1);
		           params.put("bonusAmount2", bonusamount2);
		           params.put("bonusAmount3", bonusamount3);
		           params.put("bonusAmount4", bonusamount4);
		           
		           params.put("bonusNotes0", bonusnotes0);
		           params.put("bonusNotes1", bonusnotes1);
		           params.put("bonusNotes2", bonusnotes2);
		           params.put("bonusNotes3", bonusnotes3);
		           params.put("bonusNotes4", bonusnotes4);
		           
		           params.put("bonusType0", bonustype0);
		           params.put("bonusType1", bonustype1);
		           params.put("bonusType2", bonustype2);
		           params.put("bonusType3", bonustype3);
		           params.put("bonusType4", bonustype4);
		           
		           params.put("holidayamount", holidayamount);
		           params.put("holidayName", holidayName);
		           params.put("holidayDateFrom", holidayDateFrom);
		           params.put("holidayDateTo", holidayDateTo);
		           
		           params.put("reimburseAmount", reimburseAmount);
		           params.put("reimburseNote", reimburseNote);
		           
		           params.put("quatarAmount",quatarAmount);
		           params.put("quatarNote", quatarNote);
		           
		           params.put("parameter1", parameter1);
		           params.put("parameter2", parameter2);
		           params.put("deductionAmounts",deductionAmounts);
		           
		           if(StringUtils.contains(specialDetails, "true")){
		        	   data.put("data",driverPayNew );
		           }
		           else{
		        	   data.put("data",driverpays );
		           }
		   		
		           
		data.put("params", params);
		//setList(wrapper.getList());
		return data;
	}
	
	private void populateChangedAmount(DriverPayFreezWrapper aDriverPayFreezWrapper) {
		if (aDriverPayFreezWrapper.getTransportationAmountDiff() == null 
				|| aDriverPayFreezWrapper.getTransportationAmountDiff() == 0.0) {
			return;
		}
		
		List<UpdatedDriverPay> updatedDriverPayList = retrieveUpdatedDriverPay(aDriverPayFreezWrapper);
		if (updatedDriverPayList == null || updatedDriverPayList.isEmpty()) {
			return;
		}
		
		UpdatedDriverPay updatedDriverPay = updatedDriverPayList.get(0);
		Double changedAmount = updatedDriverPay.getAmount();
		
		aDriverPayFreezWrapper.setUpdatedDriverPayAmount(changedAmount);
		aDriverPayFreezWrapper.setUpdatedDriverPayNoOfLoads(updatedDriverPay.getNoOfLoad());
		aDriverPayFreezWrapper.setUpdatedDriverPayNotes(updatedDriverPay.getNotes());
	}
	
	private List<UpdatedDriverPay> retrieveUpdatedDriverPay(DriverPayFreezWrapper aDriverPayFreezWrapper) {
		String query = "select obj from UpdatedDriverPay obj where obj.updatedStatus="+UpdatedDriverPay.UPDATED_STATUS_PROCESSED
				+ " and obj.driverName='"+aDriverPayFreezWrapper.getDrivername()+"'"
				+ " and obj.company.name='"+aDriverPayFreezWrapper.getCompanyname()+"'"
				+ " and obj.payRollBatch = '" + aDriverPayFreezWrapper.getPayRollBatch() + "'";
		if (StringUtils.isNotEmpty(aDriverPayFreezWrapper.getTerminalname())) {
			query += " and obj.terminal.name='"+aDriverPayFreezWrapper.getTerminalname()+"'";
		}
		List<UpdatedDriverPay> updatedDriverPayList = genericDAO.executeSimpleQuery(query);
		return updatedDriverPayList;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/downloadpaystub.do")
	public String downloadPayStub(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		
		Map imagesMap = new HashMap();		
		String invoiceId=request.getParameter("id");
		String type=request.getParameter("type");
		DriverPayroll payroll=genericDAO.getById(DriverPayroll.class, Long.valueOf(invoiceId));
		
		
		SearchCriteria criteria = new SearchCriteria();
		
		String checkDate =  sdf.format(payroll.getPayRollBatch());
		String billBatchFrom =  sdf.format(payroll.getBillBatchFrom());
		String billBatchTo = sdf.format(payroll.getBillBatchTo());	
		
		
		criteria.getSearchMap().put("payrollDate",checkDate); 
		criteria.getSearchMap().put("fromDate",billBatchFrom); 
		criteria.getSearchMap().put("toDate",billBatchTo); 
		criteria.getSearchMap().put("company",payroll.getCompany().getId().toString()); 
		
		if(payroll.getTerminal()!=null)
		 criteria.getSearchMap().put("terminal",payroll.getTerminal().getId().toString()); 
		
		criteria.getSearchMap().put("specdetail","true"); 
		criteria.getSearchMap().put("sum","false"); 
		
		
		criteria.setPageSize(25000);
		criteria.setPage(0);
		try {
			String report="driverpayspecial";;
		
			
			
			Map<String,Object> datas = generateData(criteria, request);
			
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= driverpay." + type);
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
