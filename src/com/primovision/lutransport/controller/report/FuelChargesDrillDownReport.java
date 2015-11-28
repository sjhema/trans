package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.model.report.NetReportMain;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;


@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/fueldrilldownreport")
public class FuelChargesDrillDownReport extends BaseController {

	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd ");
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");	
	public static SimpleDateFormat tdff = new SimpleDateFormat("yyyy-MM-dd");	
	
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
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/fueldrilldownexport.do")
	public String downloadFuelDrillDown(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
			value = "type") String typeKey,@RequestParam(required = false, value = "typeValue") String typeValue,@RequestParam(required = false, value = "unloadDateFromValue") String unloadDateFrom,
			@RequestParam(required = false, value = "unloadDateToValue") String unloadDateTo,
			@RequestParam(required = false, value = "companyValue") String companyValue) throws IOException {
		
		
		
		Map criti=new HashMap(); 
		Location locObj=null;
		Location terminalObj=null;
		Vehicle truckObj=null;
		Vehicle trailerObj=null;
		Driver driverObj=null;
		
		List<FuelLog> fsAll=new ArrayList<FuelLog>();
		
		String tcktquery=(String)request.getSession().getAttribute("netTicktQuery");
		
		String tckIds = "";
		
		
		
		String fuelLogquery="select obj from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";
		
	    
		
	    
	    if(!StringUtils.isEmpty(companyValue)){
	    	criti.clear();
			criti.put("name",companyValue);
			locObj=genericDAO.getByCriteria(Location.class, criti);
			if(locObj!=null)
				fuelLogquery = fuelLogquery+" and obj.company="+locObj.getId()+"";
	    }
		
		
	    if(typeKey.equalsIgnoreCase("company"))
		{				
			criti.clear();
			criti.put("name",typeValue);
			locObj=genericDAO.getByCriteria(Location.class, criti);
			if(locObj!=null)
				fuelLogquery = fuelLogquery+" and obj.company="+locObj.getId()+"";
			
		}
	    
	    
	    
	    
	    if(typeKey.equalsIgnoreCase("terminal"))
		{
	    	criti.clear();
			criti.put("name",typeValue);
			terminalObj=genericDAO.getByCriteria(Location.class, criti);
			if(terminalObj!=null)
				fuelLogquery = fuelLogquery+" and obj.terminal="+terminalObj.getId()+"";
	    	
		}
	    
	    if(typeKey.equalsIgnoreCase("truck"))
		{	    	
	    	truckObj=genericDAO.getById(Vehicle.class,Long.parseLong(typeValue));
			if(truckObj!=null)			
				fuelLogquery = fuelLogquery+" and obj.unit="+truckObj.getId()+"";
		}
	    
	    
	    
	    if(typeKey.equalsIgnoreCase("driver"))
		{	    	
	    	criti.clear();
			criti.put("fullName",typeValue);				
			driverObj=genericDAO.getByCriteria(Driver.class, criti);
			if(driverObj!=null)			
				fuelLogquery = fuelLogquery +" and obj.driversid="+driverObj.getId()+"";
		}      
	   
	    
	 
		
		
		List<FuelLog> fuelLogObjList = genericDAO.executeSimpleQuery(fuelLogquery);
			    
			    
			
			List<FuelLog> summarys = new ArrayList<FuelLog>();
			FuelLogReportWrapper wrapper = new FuelLogReportWrapper();
			wrapper.setFuellog(summarys);

			long totalColumn = 0;
			double totalGallons = 0.0;
			double totalFees = 0.0;
			double totaldiscounts = 0.0;
			double totalAmounts = 0.0;
			double totalGrossCost=0.0;

			Map<String, List<FuelLog>> fuellogMap = new HashMap<String, List<FuelLog>>();

			for (FuelLog fuelog : fuelLogObjList) {
				totalColumn = totalColumn + 1;
				if (fuelog != null) {
					FuelLog output = new FuelLog();
					output.setFuelVenders((fuelog.getFuelvendor() != null) ? fuelog
							.getFuelvendor().getName() : "");
					output.setCompanies((fuelog.getCompany() != null) ? fuelog
							.getCompany().getName() : "");
					output.setTerminals((fuelog.getTerminal() != null) ? fuelog
							.getTerminal().getName() : "");
					// output.setInvoicedDate(sdf.format(fuelog.getInvoiceDate()));
					output.setInvoicedDate((fuelog.getInvoiceDate() != null) ? sdf
							.format(fuelog.getInvoiceDate()) : "");
					output.setInvoiceNo((fuelog.getInvoiceNo() != null) ? fuelog
							.getInvoiceNo() : "");
					output.setUnits((fuelog.getUnit() != null) ? fuelog.getUnit()
							.getUnit().toString() : "");

					
					String fuelcardNum=null;
	                if(fuelog.getFuelcard()!=null){                                        
	                        String fuelcardnumber=fuelog.getFuelcard().getFuelcardNum();                                        
	                        
	                        if(fuelcardnumber.length()>8 && fuelcardnumber.length()<=12){
	                                String[] fuelcardnum = new String[10];
	                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
	                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
	                                        fuelcardnum[2]=fuelcardnumber.substring(8,fuelcardnumber.length());
	                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2];                                                
	                        }
	                        
	                        else if(fuelcardnumber.length()>12){
	                                String[] fuelcardnum = new String[10];
	                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
	                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
	                                        fuelcardnum[2]=fuelcardnumber.substring(8,12);
	                                        fuelcardnum[3]=fuelcardnumber.substring(12,fuelcardnumber.length());
	                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2]+"-"+fuelcardnum[3];                                                
	                        }
	                        else
	                        {
	                                fuelcardNum=fuelcardnumber;
	                        }  
	                }				
					
					// output.setDrivers((fuelog.getDriverFname()!=null)?fuelog.getDriverFname().getFullName():"");
					output.setDrivers((fuelog.getDriversid() != null) ? fuelog
							.getDriversid().getFullName() : "");
					// billing.setDestination((ticket.getDestination()!=null)?ticket.getDestination().getName():"");
					/*
					 * output.setFuelCardNumbers(fuelog.getFuelCardNumber().toString(
					 * ));
					 */
					output.setFuelCardNumbers((fuelog.getFuelcard() != null) ? fuelcardNum : "");
					/*
					 * output.setFuelCardNumbers((fuelog.getFuelCardNumber()!=null)?
					 * fuelog.getFuelCardNumber():"");
					 */
					/* output.setFuelCardNumber((fuelog.getFuelCardNumber())); */// added
																					// today

					// output.setFueltype(fuelog.getFueltype());

					output.setFueltype((fuelog.getFueltype() != null) ? fuelog
							.getFueltype() : "");

					output.setCity((fuelog.getCity() != null) ? fuelog.getCity()
							: "");
					output.setStates((fuelog.getState() != null) ? fuelog
							.getState().getName() : "");
					output.setTransactiontime((fuelog.getTransactiontime() != null) ? fuelog
							.getTransactiontime() : "");
					output.setTransactionsDate((fuelog.getTransactiondate() != null) ? sdf
							.format(fuelog.getTransactiondate()) : "");
					output.setGallons((fuelog.getGallons() != null) ? fuelog
							.getGallons() : 0.0);
					// output.setGallons(fuelog.getGallons());
					output.setUnitprice((fuelog.getUnitprice() != null) ? fuelog
							.getUnitprice() : 0.0);
					
					output.setGrosscost((fuelog.getGrosscost() !=null) ? fuelog.getGrosscost(): 0.0);
					output.setFees((fuelog.getFees() != null) ? fuelog.getFees()
							: 0.0);
					output.setDiscounts((fuelog.getDiscounts() != null) ? fuelog
							.getDiscounts() : 0.0);
					output.setAmount((fuelog.getAmount() != null) ? fuelog
							.getAmount() : 0.0);

					if (fuelog.getGallons() != null)
						totalGallons += fuelog.getGallons();
					if(fuelog.getGrosscost() != null)
						totalGrossCost+=fuelog.getGrosscost();
					if (fuelog.getFees() != null)
						totalFees += fuelog.getFees();
					if (fuelog.getDiscounts() != null)
						totaldiscounts += fuelog.getDiscounts();
					if (fuelog.getAmount() != null)
						totalAmounts += fuelog.getAmount();

					summarys.add(output);

				}

			}
	        
			totalAmounts = MathUtil.roundUp(totalAmounts, 2);
			totaldiscounts = MathUtil.roundUp(totaldiscounts, 2);
			totalFees = MathUtil.roundUp(totalFees, 2);
			totalGallons = MathUtil.roundUp(totalGallons, 3);
			totalGrossCost=MathUtil.roundUp(totalGrossCost, 2);
			wrapper.setTotalAmounts(totalAmounts);
			wrapper.setTotaldiscounts(totaldiscounts);
			wrapper.setTotalFees(totalFees);
			wrapper.setTotalGallons(totalGallons);
			wrapper.setTotalGrossCost(totalGrossCost);
			//wrapper.setTotalColumn(totalColumn);
			wrapper.setTotalColumn(fsAll.size());
			
			//Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> params = new HashMap<String,Object>();		 
			params.put("totalAmounts",wrapper.getTotalAmounts());
			params.put("totaldiscounts", wrapper.getTotaldiscounts());
			params.put("totalFees", wrapper.getTotalFees());
			params.put("totalGallons",wrapper.getTotalGallons());
			params.put("totalColumn",wrapper.getTotalColumn());
			params.put("totalGrossCost",wrapper.getTotalGrossCost());
			/*data.put("data", wrapper.getFuellog());
			data.put("params",params);*/
			try{				
				String	type = "csv";
				response.setHeader("Content-Disposition",
							"attachment;filename= fuellogReport." + type);
				
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				out = dynamicReportService.generateStaticReport("fuellogReport"+"csv",
							(List)wrapper.getFuellog(), params, type, request);
				
			
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
