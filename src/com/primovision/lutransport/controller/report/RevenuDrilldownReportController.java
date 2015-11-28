package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ListUtils;
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
import com.primovision.lutransport.model.report.SubcontractorBillingNew;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/revenudrilldownreport")
public class RevenuDrilldownReportController extends BaseController {

	
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
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/revenudrilldownexport.do")
	public String downloadRevenuDrillDown(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
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
		List<Billing_New> subBillsummarys= new ArrayList<Billing_New>();
		BillingWrapper wrapper = new BillingWrapper();
		double sumGallon = 0.0;
		double sumNet = 0.0;
		double sumBillableTon = 0.0;
		double sumOriginTon = 0.0;
		double sumDestinationTon = 0.0;
		double sumAmount = 0.0;
		double sumFuelSurcharge = 0.0;
		double sumTonnage = 0.0;
		double sumDemmurage = 0.0;
		double sumTotal = 0.0;	
		
		
		
			StringBuffer query=new StringBuffer("select obj from Billing_New obj where obj.ticket in ("
				+ticktIds.toString()+")");
			
			
			
			 if(!StringUtils.isEmpty(companyValue)){			    	
				 query.append(" and obj.driverCompanyName='").append(companyValue).append("'");
			 }
			
			if(typeKey.equalsIgnoreCase("company"))
			{
				query.append(" and obj.driverCompanyName='").append(typeValue).append("'");
			}			
			if(typeKey.equalsIgnoreCase("terminal"))
			{
				query.append(" and obj.terminal='").append(typeValue).append("'");
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
				query.append(" and obj.driver='").append(typeValue).append("'");
			}
			
			
			 summarys=genericDAO.executeSimpleQuery(query.toString());			
			
			 StringBuffer sum_query=new StringBuffer("select sum(obj.effectiveNetWt),sum(obj.effectiveTonsWt),sum(obj.originTonsWt),sum(obj.destinationTonsWt),sum(obj.amount),sum(obj.fuelSurcharge),sum(obj.tonnagePremium),sum(obj.demurrageCharge) from Billing_New obj where obj.ticket in ("
				+ticktIds.toString()+")");
			 
			 
			 if(!StringUtils.isEmpty(companyValue)){			    	
				 sum_query.append(" and obj.driverCompanyName='").append(companyValue).append("'");
			 }
			 
			 if(typeKey.equalsIgnoreCase("company"))
			 {
				 sum_query.append(" and obj.driverCompanyName='").append(typeValue).append("'");
			 }
			 if(typeKey.equalsIgnoreCase("terminal"))
			 {
				 sum_query.append(" and obj.terminal='").append(typeValue).append("'");
			 }
			 if(typeKey.equalsIgnoreCase("truck"))
			 {
				 sum_query.append(" and obj.t_unit='").append(typeValue).append("'");
			 }
			 if(typeKey.equalsIgnoreCase("trailer"))
			 {
				 sum_query.append(" and obj.trailer='").append(typeValue).append("'");
			 }
			 if(typeKey.equalsIgnoreCase("driver"))
			 {
				sum_query.append(" and obj.driver='").append(typeValue).append("'");
			 }
			 List<Billing_New> sum_list=genericDAO.executeSimpleQuery(sum_query.toString());
			
			 if(sum_list!=null && sum_list.size()>0){
			 for(Object obj:sum_list){
				 Object[] objarry=(Object[])obj;
				if(objarry!=null){
				if(objarry[0] !=null)	
				 sumNet=Double.parseDouble(objarry[0].toString());
				if(objarry[1] !=null)
				 sumBillableTon=Double.parseDouble(objarry[1].toString());
				if(objarry[2] !=null)
				 sumOriginTon=Double.parseDouble(objarry[2].toString());
				if(objarry[3] !=null)
				 sumDestinationTon=Double.parseDouble(objarry[3].toString());
				if(objarry[4] !=null)
				 sumAmount=Double.parseDouble(objarry[4].toString());
				if(objarry[5] !=null)
				 sumFuelSurcharge=Double.parseDouble(objarry[5].toString());
				if(objarry[6] !=null)
				 sumTonnage=Double.parseDouble(objarry[6].toString());
				if(objarry[7] !=null)
				 sumDemmurage=Double.parseDouble(objarry[7].toString());
				}
			 }
			 }		
		
			 if(typeKey.equalsIgnoreCase("company"))
			 {
				 String ticks ="-1";
				 String subQuery = "Select obj from SubcontractorBillingNew obj where obj.ticket in ("
				 +ticktIds.toString()+") and obj.company in ('"+typeValue+"')";
				 
				 List<SubcontractorBillingNew> subList = genericDAO.executeSimpleQuery(subQuery);
				 
				 for(SubcontractorBillingNew subObj:subList){
					ticks = ticks +","+subObj.getTicket();
				 }
					
				 
				 StringBuffer subBillquery=new StringBuffer("select obj from Billing_New obj where obj.ticket in ("
							+ticks+")");
				 
				 subBillsummarys=genericDAO.executeSimpleQuery(subBillquery.toString());			
					
				
				 
				 
				 StringBuffer subBill_sum_query=new StringBuffer("select sum(obj.effectiveNetWt),sum(obj.effectiveTonsWt),sum(obj.originTonsWt),sum(obj.destinationTonsWt),sum(obj.amount),sum(obj.fuelSurcharge),sum(obj.tonnagePremium),sum(obj.demurrageCharge) from Billing_New obj where obj.ticket in ("
					+ticks+")");
				 
				 List<Billing_New> subBill_sum_list=genericDAO.executeSimpleQuery(subBill_sum_query.toString());
					
				 if(subBill_sum_list!=null && subBill_sum_list.size()>0){
				 for(Object obj:subBill_sum_list){
					 Object[] objarry=(Object[])obj;
					if(objarry!=null){
					if(objarry[0] !=null)	
					 sumNet=sumNet+Double.parseDouble(objarry[0].toString());
					if(objarry[1] !=null)
					 sumBillableTon=sumBillableTon+Double.parseDouble(objarry[1].toString());
					if(objarry[2] !=null)
					 sumOriginTon=sumOriginTon+Double.parseDouble(objarry[2].toString());
					if(objarry[3] !=null)
					 sumDestinationTon=sumDestinationTon+Double.parseDouble(objarry[3].toString());
					if(objarry[4] !=null)
					 sumAmount=sumAmount+Double.parseDouble(objarry[4].toString());
					if(objarry[5] !=null)
					 sumFuelSurcharge=sumFuelSurcharge+Double.parseDouble(objarry[5].toString());
					if(objarry[6] !=null)
					 sumTonnage=sumTonnage+Double.parseDouble(objarry[6].toString());
					if(objarry[7] !=null)
					 sumDemmurage=sumDemmurage+Double.parseDouble(objarry[7].toString());
					}
				 }
				 }
				 
			 }
	    if(subBillsummarys!=null && subBillsummarys.size()>0){
	    	wrapper.setBillings(ListUtils.union(summarys,subBillsummarys));	
	    	
	    }
	    else{
	    	wrapper.setBillings(summarys);
	    }
	    	     
		sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);
		sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
		sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);
		sumNet = MathUtil.roundUp(sumNet, 2);
		sumAmount = MathUtil.roundUp(sumAmount, 2);
		sumFuelSurcharge = MathUtil.roundUp(sumFuelSurcharge, 2);
		sumDemmurage = MathUtil.roundUp(sumDemmurage, 2);
		sumTonnage = MathUtil.roundUp(sumTonnage, 2);
		sumGallon = MathUtil.roundUp(sumGallon, 2);		

		sumTotal = sumAmount + sumFuelSurcharge + sumDemmurage + sumTonnage;
		wrapper.setSumBillableTon(sumBillableTon);
		wrapper.setSumOriginTon(sumOriginTon);
		wrapper.setSumDestinationTon(sumDestinationTon);
		wrapper.setSumTonnage(sumTonnage);
		wrapper.setSumNet(sumNet);
		wrapper.setSumAmount(sumAmount);
		wrapper.setSumFuelSurcharge(sumFuelSurcharge);
		wrapper.setSumTotal(sumTotal);
		wrapper.setSumGallon(sumGallon);
		wrapper.setTotalRowCount(tickets.size());	
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sumDestinationTon", wrapper.getSumDestinationTon());
		params.put("sumOriginTon", wrapper.getSumOriginTon());
		params.put("sumBillableTon", wrapper.getSumBillableTon());
		params.put("sumTonnage", wrapper.getSumTonnage());
		params.put("sumTotal", wrapper.getSumTotal());
		params.put("sumDemurrage", wrapper.getSumDemmurage());
		params.put("sumNet", wrapper.getSumNet());
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
		data.put("data", wrapper.getBillings());			
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
							"attachment;filename= billinghistory." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));			
				Map<String, Object> param = (Map<String,Object>)data.get("params");
				if (!type.equals("print")&&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("billinghistory",
							(List)data.get("data"), param, type, request);
				}
				else if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("billinghistorypdf",
							(List)data.get("data"), param, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport("billinghistory"+"print",
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
