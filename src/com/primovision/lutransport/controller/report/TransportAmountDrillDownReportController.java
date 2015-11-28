package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.Billing_New;
import com.primovision.lutransport.model.report.NetReportTransportationAmountDrillDownWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/transamountdrilldownreport")
 class TransportAmountDrillDownReportController extends BaseController {


	
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
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/transportamountdrilldownexport.do")
	public String downloadTransportAmountDrillDown(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
			value = "type") String typeKey,@RequestParam(required = false, value = "typeValue") String typeValue
			,@RequestParam(required = false, value = "companyValue") String companyValue) throws IOException {
		
		
		
		
	   String ticktquery=(String)request.getSession().getAttribute("netTicktQuery");
       System.out.println("**** the drill ticket query is "+ticktquery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticktquery);
		//StringBuffer ticketIds = new StringBuffer("");
		String ticktIds="";
		if(tickets!=null && tickets.size()>0 ){
			for(Ticket ticObj:tickets){
			  if(ticktIds.equals("")){
				  ticktIds=String.valueOf(ticObj.getId());
			  }
			  else
			  {
				  ticktIds=ticktIds+","+String.valueOf(ticObj.getId());				  
			  }
			}
		}
		
		List<Billing_New> summarys = new ArrayList<Billing_New>();
		BillingWrapper wrapper = new BillingWrapper();
			
		Map criti = new HashMap();
		
		
			StringBuffer query=new StringBuffer("select t.driver.fullName,t.driverCompany.name,t.origin.name,t.destination.name,count(t),sum(t.driverPayRate),t.driverPayRate  from Ticket t where t.id in ("
				+ticktIds.toString()+")");
			
			
			
			 if(!StringUtils.isEmpty(companyValue)){	
				 criti.clear();
				 criti.put("name",companyValue);
				 Location locObj = genericDAO.getByCriteria(Location.class,criti);
				 query.append(" and t.driverCompany='").append(locObj.getId()).append("'");
			 }
			
			if(typeKey.equalsIgnoreCase("company"))
			{
				 criti.clear();
				 criti.put("name",typeValue);
				 Location locObj = genericDAO.getByCriteria(Location.class,criti);
				query.append(" and t.driverCompany='").append(locObj.getId()).append("'");
			}			
			if(typeKey.equalsIgnoreCase("terminal"))
			{
				 criti.clear();
				 criti.put("name",typeValue);
				 Location locObj = genericDAO.getByCriteria(Location.class,criti);
				query.append(" and t.terminal='").append(locObj.getId()).append("'");
			}
			if(typeKey.equalsIgnoreCase("truck"))
			{
				query.append(" and obj.t_unit='").append(typeValue).append("'");
			}
			if(typeKey.equalsIgnoreCase("trailer"))
			{
				query.append(" and obj.trailer='").append(typeValue).append("'");
			}
			if(typeKey.equalsIgnoreCase("driver"))
			{
				query.append(" and t.driver.fullName='").append(typeValue).append("'");
			}
			
			query.append(" group by t.driver,t.origin,t.destination");
			
			 List<Ticket> sum_list=genericDAO.executeSimpleQuery(query.toString());			
			
			
			 List<NetReportTransportationAmountDrillDownWrapper> reportList = new ArrayList<NetReportTransportationAmountDrillDownWrapper>();
			
			 if(sum_list!=null && sum_list.size()>0){
			 for(Object obj:sum_list){
				 NetReportTransportationAmountDrillDownWrapper transAmountWrapperObj = new NetReportTransportationAmountDrillDownWrapper();
				 Object[] objarry=(Object[])obj;
				if(objarry!=null){
				if(objarry[0] !=null)	
					transAmountWrapperObj.setDriver(objarry[0].toString());
				if(objarry[1] !=null)
					transAmountWrapperObj.setDriverCompany(objarry[1].toString());
				if(objarry[2] !=null)
					transAmountWrapperObj.setOrigin(objarry[2].toString());
				if(objarry[3] !=null)
					transAmountWrapperObj.setDestination(objarry[3].toString());
				if(objarry[4] !=null)
					transAmountWrapperObj.setLoadCount(Integer.parseInt(objarry[4].toString()));
				if(objarry[5] !=null)
					transAmountWrapperObj.setTransAmount(Double.parseDouble(objarry[5].toString()));
				if(objarry[6] !=null)
					transAmountWrapperObj.setPayRate(Double.parseDouble(objarry[6].toString()));
				
				reportList.add(transAmountWrapperObj);
				}
			 }
			 }		
		
		
	    	
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();		
		data.put("data", reportList);			
		data.put("params", params);
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			String type="csv";
			if (StringUtils.isEmpty(type))
				type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= netReportTransAmount." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));			
				Map<String, Object> param = (Map<String,Object>)data.get("params");
				if (!type.equals("print")&&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("netReportTransAmount",
							(List)data.get("data"), param, type, request);
				}
				else if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("netReportTransAmountpdf",
							(List)data.get("data"), param, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport("netReportTransAmount"+"print",
							(List)data.get("data"), params, type, request);
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

}
