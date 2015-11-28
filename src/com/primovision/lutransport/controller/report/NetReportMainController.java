package com.primovision.lutransport.controller.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.apache.commons.lang.*;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.report.Billing_New;
import com.primovision.lutransport.model.report.NetReportAutoUpdateTriggerTable;
import com.primovision.lutransport.model.report.NetReportAutoUpdateTriggerTable2;
import com.primovision.lutransport.model.report.NetReportMain;
import com.primovision.lutransport.model.report.SubcontractorBilling;
import com.primovision.lutransport.model.report.SubcontractorBillingNew;

@Controller
@SuppressWarnings("unchecked")
public class NetReportMainController extends BaseController{
	
	@Scheduled(cron="0 0 1 * * ?")	
	public void generateMainNetReportData(){
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		
		
		DateMidnight now = new DateMidnight();
		DateMidnight beginningOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
		DateMidnight beginningOfNextMonth = now.plusMonths(1).withDayOfMonth(1);
		DateMidnight endOfCurrentMonth = beginningOfNextMonth.minusDays(1);
		
		
		String formatDate1 = sdf.format(beginningOfLastMonth.toDate());
		String formatDate2 = sdf.format(beginningOfNextMonth.toDate());
		String formatDate3 = sdf.format(endOfCurrentMonth.toDate());
		
		formatDate1 = "'"+formatDate1+"'";
		formatDate2 = "'"+formatDate2+"'";
		formatDate3 = "'"+formatDate3+"'";
		
		NetReportAutoUpdateTriggerTable netReportAutoUpdateObj = new NetReportAutoUpdateTriggerTable();
		
		netReportAutoUpdateObj.setFromDate(formatDate1);
		netReportAutoUpdateObj.setToDate(formatDate3);
		
		genericDAO.saveOrUpdate(netReportAutoUpdateObj);	
		
		/*
		
		String tickeQuery = "select obj from Ticket obj where obj.billBatch >='2014-01-01 00:00:00' and billBatch <='2014-02-28 00:00:00'";	
		
		List<Ticket> ticketObjList = genericDAO.executeSimpleQuery(tickeQuery);
	
		for(Ticket tcktobj: ticketObjList){			
			Double tollAmount  = 0.0;
			Double fuelAmount = 0.0;
			Double subcontractorAmount = 0.0;
			Double billlingNewAmount = 0.0;
			String miscAmount = null;
			
			String tollQuery = "select obj from EzToll obj where obj.transactiondate >='"+tcktobj.getLoadDate()+"' and obj.transactiondate <='"+tcktobj.getUnloadDate()+"' and obj.plateNumber="+tcktobj.getVehicle().getId();
			
			List<EzToll> eztollObjList = genericDAO.executeSimpleQuery(tollQuery);
			
		    for(EzToll tollObj:eztollObjList ){
		    	if(tollObj.getAmount()!=null)
		    		tollAmount = tollAmount+tollObj.getAmount();
		    	else
		    		tollAmount = tollAmount+0.0;
		    }
		    
		   
		    String fuelQuery = "select obj from FuelLog obj where obj.transactiondate >='"+tcktobj.getLoadDate()+"' and obj.transactiondate <='"+tcktobj.getUnloadDate()+"' and obj.unit="+tcktobj.getVehicle().getId();
			
		    List<FuelLog> fuelLogObjList = genericDAO.executeSimpleQuery(fuelQuery);
		    
		    for(FuelLog fuelObj:fuelLogObjList){
		    	if(fuelObj.getAmount()!=null)
		    		fuelAmount = fuelAmount + fuelObj.getAmount();
		    	else
		    		fuelAmount = fuelAmount + 0.0;		    	
		    }
		    
		    
		    String subcontractorNewQuery = "select obj from SubcontractorBillingNew obj where obj.ticket="+tcktobj.getId();
		    
		    List<SubcontractorBillingNew> subcontractorObjList =  genericDAO.executeSimpleQuery(subcontractorNewQuery);
		    
		    for(SubcontractorBillingNew subcontractorObj: subcontractorObjList ){
		    	if(subcontractorObj.getTotAmt()!=null)
		    		subcontractorAmount = subcontractorAmount + subcontractorObj.getTotAmt();
		    	else
		    		subcontractorAmount = subcontractorAmount + 0.0;
		    } 
		    
		    
		    String billingHistory = "select obj from Billing_New obj where obj.ticket="+tcktobj.getId();
		    
		    List<Billing_New> billingNewObjList = genericDAO.executeSimpleQuery(billingHistory);
		    
		    for(Billing_New billingNewObj : billingNewObjList){
		    	if(billingNewObj.getTotAmt()!=null)
		    		billlingNewAmount = billlingNewAmount + billingNewObj.getTotAmt();
		    	else
		    		billlingNewAmount = billlingNewAmount + 0.0;		    	
		    }
		    
		   
		    String subcontractorQuery = "select obj from SubcontractorBilling obj where obj.ticket="+tcktobj.getId();
		    
		    List<SubcontractorBilling> subcontractorbillingList =  genericDAO.executeSimpleQuery(subcontractorQuery);
		    
		    
		    for(SubcontractorBilling subcontractorbilling : subcontractorbillingList){
		    	if(!StringUtils.isEmpty(subcontractorbilling.getMiscelleneousCharges()))
		    		miscAmount = subcontractorbilling.getMiscelleneousCharges();
		    	
		    	
		    }
		    
		    Map map = new HashMap();
		    map.clear();
		    map.put("ticketID", tcktobj.getId());
		    
		    NetReportMain netReportObj = genericDAO.getByCriteria(NetReportMain.class,map);
		    
		    if(netReportObj==null){		    
		   
		    	System.out.println("******** Enetered in New");
		    	
		    	NetReportMain netReportMainObj = new NetReportMain();
		    
		    	netReportMainObj.setFuelamount(fuelAmount);
		    	netReportMainObj.setTollamount(tollAmount);
		    	netReportMainObj.setTicketID(tcktobj.getId());
		    	netReportMainObj.setTicketTotAmt(billlingNewAmount);
		    	netReportMainObj.setSubcontractorTotAmt(subcontractorAmount);
		    	netReportMainObj.setSubMiscAmt(miscAmount);	
		    	netReportMainObj.setBillBatch(tcktobj.getBillBatch());
		    	if(tcktobj.getCompanyLocation()!=null)
		    		netReportMainObj.setCompany(tcktobj.getCompanyLocation().getId());
		    
		    	if(tcktobj.getDriverCompany()!=null)
		    		netReportMainObj.setDriverCompany(tcktobj.getDriverCompany().getId());
		    
		    	netReportMainObj.setDriver(tcktobj.getDriver().getId());
		    	netReportMainObj.setTrailer(tcktobj.getTrailer().getId());
		    	netReportMainObj.setVehicle(tcktobj.getVehicle().getId());
		    	netReportMainObj.setTerminal(tcktobj.getTerminal().getId());
		    	if(tcktobj.getSubcontractor()!=null)
		    		netReportMainObj.setSubcontractor(tcktobj.getSubcontractor().getId());	    
		    
		    
		    	genericDAO.saveOrUpdate(netReportMainObj);	
		    
		    }
		    else{
		    	
		    	System.out.println("******** Enetered in old ");
		    	netReportObj.setFuelamount(fuelAmount);
		    	netReportObj.setTollamount(tollAmount);
		    	netReportObj.setTicketID(tcktobj.getId());
		    	netReportObj.setTicketTotAmt(billlingNewAmount);
		    	netReportObj.setSubcontractorTotAmt(subcontractorAmount);
		    	netReportObj.setSubMiscAmt(miscAmount);	
		    	netReportObj.setBillBatch(tcktobj.getBillBatch());
		    	if(tcktobj.getCompanyLocation()!=null)
		    		netReportObj.setCompany(tcktobj.getCompanyLocation().getId());
		    
		    	if(tcktobj.getDriverCompany()!=null)
		    		netReportObj.setDriverCompany(tcktobj.getDriverCompany().getId());
		    
		    	netReportObj.setDriver(tcktobj.getDriver().getId());
		    	netReportObj.setTrailer(tcktobj.getTrailer().getId());
		    	netReportObj.setVehicle(tcktobj.getVehicle().getId());
		    	netReportObj.setTerminal(tcktobj.getTerminal().getId());
		    	if(tcktobj.getSubcontractor()!=null)
		    		netReportObj.setSubcontractor(tcktobj.getSubcontractor().getId());	    
		    
		    
		    	genericDAO.saveOrUpdate(netReportObj);
		    }
		    
		}
	
	*/
		
	}
	
	
	
	
	
	@Scheduled(cron="0 35 1 * * ?")	
	public void generateMainNetReportData2(){
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		
		
		DateMidnight now = new DateMidnight();
		DateMidnight beginningOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
		DateMidnight beginningOfNextMonth = now.plusMonths(1).withDayOfMonth(1);
		DateMidnight endOfCurrentMonth = beginningOfNextMonth.minusDays(1);
		
		
		String formatDate1 = sdf.format(beginningOfLastMonth.toDate());
		String formatDate2 = sdf.format(beginningOfNextMonth.toDate());
		String formatDate3 = sdf.format(endOfCurrentMonth.toDate());
		
		formatDate1 = "'"+formatDate1+"'";
		formatDate2 = "'"+formatDate2+"'";
		formatDate3 = "'"+formatDate3+"'";
		
		NetReportAutoUpdateTriggerTable2 netReportAutoUpdateObj = new NetReportAutoUpdateTriggerTable2();
		
		netReportAutoUpdateObj.setFromDate(formatDate1);
		netReportAutoUpdateObj.setToDate(formatDate3);
		
		genericDAO.saveOrUpdate(netReportAutoUpdateObj);		
		
	}
	
	
}
