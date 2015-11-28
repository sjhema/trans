package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.tags.StaticDataUtil;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SubcontractorInvoice;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.report.SubcontractorBilling;
import com.primovision.lutransport.model.report.SubcontractorBillingNew;
import com.primovision.lutransport.model.report.SubcontractorBillingWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;


@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/subcontractordrilldownreport")
public class SubContractorDrillDownReport extends BaseController {

	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
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
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/subcontractordrilldownexport.do")
	public String downloadSubcontractorDrillDown(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
			value = "type") String typeKey,@RequestParam(required = false, value = "typeValue") String typeValue
			,@RequestParam(required = false, value = "companyValue") String companyValue) throws IOException {
		
		
		Map criti=new HashMap(); 
		Location locObj=null;
		Location terminalObj=null;
		Vehicle truckObj=null;
		Vehicle trailerObj=null;
		Driver driverObj=null;
		
       String tcktquery=(String)request.getSession().getAttribute("netTicktQuery");
		
		String tckIds = "";
		
		List<Ticket> alltickets = genericDAO.executeSimpleQuery(tcktquery);
		
		
		for(Ticket tcktObj : alltickets){
			if(tckIds.equals("")){
				tckIds = tcktObj.getId().toString();
			}
			else{
				tckIds = tckIds +","+tcktObj.getId().toString();
			}
		}
		
		String ticktquery="select obj from Ticket obj where obj.id in ("+tckIds+")";
		
		
		
		
	    
	    if(!StringUtils.isEmpty(companyValue)){
	    	criti.clear();
			criti.put("name",companyValue);
			locObj=genericDAO.getByCriteria(Location.class, criti);
			if(locObj!=null)
				ticktquery = ticktquery+" and obj.companyLocation="+locObj.getId()+"";
	    }
		
	    
	    if(typeKey.equalsIgnoreCase("company"))
		{				
			criti.clear();
			criti.put("name",typeValue);
			locObj=genericDAO.getByCriteria(Location.class, criti);
			if(locObj!=null)
				ticktquery = ticktquery+" and obj.companyLocation="+locObj.getId()+"";
			
		}
	    
	    if(typeKey.equalsIgnoreCase("terminal"))
		{
	    	criti.clear();
			criti.put("name",typeValue);
			terminalObj=genericDAO.getByCriteria(Location.class, criti);
			if(terminalObj!=null)
				ticktquery = ticktquery+" and obj.terminal="+terminalObj.getId()+"";
	    	
		}
	    
	    if(typeKey.equalsIgnoreCase("truck"))
		{	    	
	    	truckObj=genericDAO.getById(Vehicle.class,Long.parseLong(typeValue));
			if(truckObj!=null)			
				ticktquery = ticktquery+" and obj.vehicle="+truckObj.getId()+"";
		}
	    
	    if(typeKey.equalsIgnoreCase("trailer"))
		{	    	
	    	trailerObj=genericDAO.getById(Vehicle.class,Long.parseLong(typeValue));
			if(trailerObj!=null)
				ticktquery = ticktquery+" and obj.trailer="+trailerObj.getId()+"";
	    	
			
		}
	    
	    if(typeKey.equalsIgnoreCase("driver"))
		{	    	
	    	criti.clear();
			criti.put("fullName",typeValue);				
			driverObj=genericDAO.getByCriteria(Driver.class, criti);
			if(driverObj!=null)			
				ticktquery = ticktquery+" and obj.driver="+driverObj.getId()+"";
		}    
	    
	    
	    System.out.println("**** the drill ticket query is "+ticktquery);
	    
	    List<Ticket> tickets = genericDAO.executeSimpleQuery(ticktquery);
			
		String ticktIds="";
		if(tickets!=null && tickets.size()>0 ){
			for(Ticket tcktobj:tickets){
				if(ticktIds.equals("")){
					ticktIds = tcktobj.getId().toString();
				}else{
					ticktIds = ticktIds +","+tcktobj.getId().toString();
				}				
			}
		}
		
		
		List<SubcontractorBillingNew> summarys = new ArrayList<SubcontractorBillingNew>();
		SubcontractorBillingWrapper wrapper = new SubcontractorBillingWrapper();
		
		double sumNet = 0.0;
		double sumBillableTon = 0.0;
		double sumOriginTon = 0.0;
		double sumDestinationTon = 0.0;
		double sumAmount = 0.0;
		double sumFuelSurcharge = 0.0;
		double sumTotal = 0.0;
		double sumOtherCharges = 0.0;
		double grandTotal = 0.0;
		double amount = 0.0;
		double miscelleneousCharge = 0.0;
		List<String> str = new ArrayList<String>();
		
		
		String subinvquery="select obj from SubcontractorBillingNew obj where obj.ticket in ("
			+ticktIds+")";		
		
		System.out.println(" ****** subquery is "+subinvquery);
		
		summarys=genericDAO.executeSimpleQuery(subinvquery);	
		
		
		System.out.println("****** summary size is "+summarys.size());
		
		wrapper.setSubcontractorBillingsNew(summarys);
		
		System.out.println("****** summary size is wrapper "+wrapper.getSubcontractorBillingsNew().size());
		 
		String sum_query="select sum(obj.effectiveNetWt),sum(obj.effectiveTonsWt),sum(obj.originTonsWt),sum(obj.destinationTonsWt),sum(obj.amount),sum(obj.fuelSurcharge),sum(obj.otherCharges),sum(obj.totAmt) from SubcontractorBillingNew obj where obj.ticket in ("
			+ticktIds+")";
		 
		 List<SubcontractorBillingNew> sum_list=genericDAO.executeSimpleQuery(sum_query);
			
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
			 sumOtherCharges=Double.parseDouble(objarry[6].toString());
			if(objarry[7] !=null)
			 sumTotal=Double.parseDouble(objarry[7].toString());
			}
		 }
		 }	
		
		 
		 
		 String subMiscAmount = "select miscelleneousCharges from SubcontractorBillingNew obj where obj.ticket in ("
				+ ticktIds
				+ ")  group by obj.invoiceNo,subcontractorId";
				
				System.out.println("********* ubMiscAmount query is "+subMiscAmount);
				
				
				List<Ticket> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
				
				if(submiscamountList!=null && submiscamountList.size()>0){
					for(Object submiscobj:submiscamountList){
						//Object[] submiscobjarry=(Object[])submiscobj;
						if(submiscobj!=null){
							
							String subMiscAmt = submiscobj.toString();
							
							
							String[]  subMiscAmts = subMiscAmt.split(",");
							for(int i=0;i< subMiscAmts.length;i++){									
								miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
							}
							
						}
					}						
				}	 
			

		sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);
		sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
		sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);
		sumNet = MathUtil.roundUp(sumNet, 2);
		sumAmount = MathUtil.roundUp(sumAmount, 2);

		
		
		sumTotal = MathUtil.roundUp(sumTotal, 2);
		wrapper.setSumBillableTon(sumBillableTon);
		wrapper.setSumOriginTon(sumOriginTon);
		wrapper.setSumDestinationTon(sumDestinationTon);
		wrapper.setSumNet(sumNet);
		wrapper.setSumAmount(sumAmount);
		wrapper.setSumFuelSurcharge(sumFuelSurcharge);
		wrapper.setSumTotal(sumTotal);
		wrapper.setSumOtherCharges(sumOtherCharges);
		wrapper.setMiscelleneousCharges(Double.toString(miscelleneousCharge));
		wrapper.setGrandTotal(sumTotal + miscelleneousCharge);
		
		
		
			
			////***************
			Map<String,Object> params = new HashMap<String,Object>();
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
		
			try{			
				String type="";
				
				
				type = "csv";
				response.setHeader("Content-Disposition",
							"attachment;filename= subcontractorreport." + type);
				
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				out = dynamicReportService.generateStaticReport("subcontractorreport",
							(List)wrapper.getSubcontractorBillingsNew(), params, type, request);
							
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
