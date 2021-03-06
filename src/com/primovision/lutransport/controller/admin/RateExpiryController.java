package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.util.logging.resources.logging;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.FuelSurchargeWeeklyRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/rate/expirationreport")
public class RateExpiryController extends BaseController {
	
	
	 Map criterias=new HashMap();
	 int count=-1;
	 String check="no";
	 Long transferidtemp =  -1l;
	 Long landfillidtemp= -1l;
	 
	@Autowired
	private GenericDAO genericDAO;
	
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/list.do")
	public String getExpriredRates(ModelMap model,HttpServletRequest request){
		 
		request.getSession().setAttribute("searchCriteria",null);
		if(request.getParameter("type").equals("all")){
			criterias.clear();
		    List<BillingRate> billingrate=genericDAO.findByCriteria(BillingRate.class, criterias, "transferStation.name asc,obj.landfill.name asc,obj.validFrom" ,true);
		    List<SubcontractorRate> subcontractorRates=genericDAO.findByCriteria(SubcontractorRate.class, criterias,"subcontractor.name asc,obj.transferStation.name asc,obj.landfill.name asc,obj.validFrom", true);
		    List<FuelSurchargePadd> fuelSurchargePadd=genericDAO.findByCriteria(FuelSurchargePadd.class, criterias,"code asc,obj.validFrom",true);
		    List<FuelSurchargeWeeklyRate> fuelSurchargeWeeklyRate=genericDAO.findByCriteria(FuelSurchargeWeeklyRate.class, criterias, "transferStations.name asc,obj.landfillStations.name asc,obj.fromDate",true);
			checkExpiredBillingRates(model,request,billingrate);
			checkExpiredSubcontractorRates(model,request,subcontractorRates);
			checkExpiredFuelSurchargeRates(model,request,fuelSurchargePadd);
			checkExpiredFuelSurchargeWeeklyRates(model,request,fuelSurchargeWeeklyRate);			
			return "admin/rateexpiration/home";
		}
		
		if(request.getParameter("type").equals("billingrate")){
		criterias.clear();		
		List<BillingRate> billingrate=genericDAO.findByCriteria(BillingRate.class, criterias, "transferStation.name asc,obj.landfill.name asc,obj.validFrom" ,true);
		checkExpiredBillingRates(model,request,billingrate);
		getTransferAndLandfill(model,request);	
		model.addAttribute("type","alert");
		return "admin/rateexpiration/billingrate";		
		}
		if(request.getParameter("type").equals("subcontractorrate")){
		criterias.clear();
		List<SubcontractorRate> subcontractorRates=genericDAO.findByCriteria(SubcontractorRate.class, criterias,"subcontractor.name asc,obj.transferStation.name asc,obj.landfill.name asc,obj.validFrom", true);
		checkExpiredSubcontractorRates(model,request,subcontractorRates);		
		getTransferAndLandfill(model,request);
		model.addAttribute("type","alert");
		return "admin/rateexpiration/subcontractorrate";
		}
		if(request.getParameter("type").equals("surchargepadd")){
			criterias.clear();
			List<FuelSurchargePadd> fuelSurchargePadd=genericDAO.findByCriteria(FuelSurchargePadd.class, criterias,"code asc,obj.validFrom",true);
			checkExpiredFuelSurchargeRates(model,request,fuelSurchargePadd);
			model.addAttribute("type","alert");
			return "admin/rateexpiration/fuelsurchargepadd";
		}
		if(request.getParameter("type").equals("weeklyrate")){
			criterias.clear();
			 List<FuelSurchargeWeeklyRate> fuelSurchargeWeeklyRate=genericDAO.findByCriteria(FuelSurchargeWeeklyRate.class, criterias, "transferStations.name asc,obj.landfillStations.name asc,obj.fromDate",true);
			checkExpiredFuelSurchargeWeeklyRates(model,request,fuelSurchargeWeeklyRate);
			getTransferAndLandfill(model,request);	
			model.addAttribute("type","alert");
			model.addAttribute("rateTypes", listStaticData("RATE_TYPE"));
			return "admin/rateexpiration/fuelsurchargweeklyrate";
		}
			
		return "blank/blank";
	}	
	
	
	@RequestMapping(method = RequestMethod.GET,value="/search.do")
	public String searchExpiredRates(ModelMap model, HttpServletRequest request){		
		populateSearchCriteria(request, request.getParameterMap());				
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(10000);
		criteria.getSearchMap().remove("type");
		getTransferAndLandfill(model,request);			
		if(request.getParameter("type").equals("billingrate")){
		List<BillingRate> billingrate=genericDAO.search(BillingRate.class, criteria,"transferStation.name asc,landfill.name asc,validFrom",true);				
		checkExpiredBillingRates(model,request,billingrate);	
		request.getSession().setAttribute("searchCriteria",null);
		model.addAttribute("type","alert");
		return "admin/rateexpiration/billingrate";
		}
		if(request.getParameter("type").equals("subcontractorrate")){			
			List<SubcontractorRate> subcontractorRates=genericDAO.search(SubcontractorRate.class, criteria,"subcontractor.name asc,transferStation.name asc,landfill.name asc,validFrom", true);
			checkExpiredSubcontractorRates(model,request,subcontractorRates);
			getTransferAndLandfill(model,request);
			request.getSession().setAttribute("searchCriteria",null);
			model.addAttribute("type","alert");
			return "admin/rateexpiration/subcontractorrate";
		}		
		if(request.getParameter("type").equals("surchargepadd")){
			dateupdateService.updateDate(request, "validFromDate", "validFrom");
			dateupdateService.updateDate(request, "validToDate", "validTo");
			List<FuelSurchargePadd> fuelSurchargePadd=genericDAO.search(FuelSurchargePadd.class, criteria,"code asc,validFrom",true);
			checkExpiredFuelSurchargeRates(model,request,fuelSurchargePadd);
			request.getSession().setAttribute("searchCriteria",null);
			model.addAttribute("type","alert");
			return "admin/rateexpiration/fuelsurchargepadd";
		}
		if(request.getParameter("type").equals("weeklyrate")){
		List<FuelSurchargeWeeklyRate> fuelSurchargeWeeklyRate=genericDAO.search(FuelSurchargeWeeklyRate.class, criteria, "transferStations.name asc,landfillStations.name asc,fromDate",true);
		model.addAttribute("rateTypes", listStaticData("RATE_TYPE"));
		checkExpiredFuelSurchargeWeeklyRates(model,request,fuelSurchargeWeeklyRate);
		request.getSession().setAttribute("searchCriteria",null);
		model.addAttribute("type","alert");
		return "admin/rateexpiration/fuelsurchargweeklyrate";
			
		}
		return "blank/blank";
	}	
	

	/////function to get expired BillingRate and its count
	public String checkExpiredBillingRates(ModelMap model, HttpServletRequest request,List<BillingRate> billingrates)
	{			    
       int billingrateExprng=0,billingrateExprd=0;          
       List<BillingRate> billingrate=new ArrayList<BillingRate>();	  
         
		    for(BillingRate billing: billingrates ){			    	
			int diffInDays=31;
			Long transferid=billing.getTransferStation().getId();
			Long landfillid=billing.getLandfill().getId();
			///code to check for new origin and destination. 
			if(transferidtemp!=transferid || landfillidtemp!=landfillid){
			if(billing.getStatus()==1)
			check="yes";				
			count=0;                                                     
			transferidtemp=transferid;			
			landfillidtemp=landfillid;
			}
			//code to check validity of rates.
			if(transferidtemp==transferid && landfillidtemp==landfillid && count==0 && check.equalsIgnoreCase("yes") ){				
			count++;
			check="no";
			if( billing.getValidFrom().getTime()<=new Date().getTime()) 
			diffInDays = (int) ((billing.getValidTo().getTime()- new Date().getTime()) / (1000 * 60 * 60 * 24));//Note: only one active rates for same origin and destination will be checked for validity.		
			else
			count=0;	
			
			if(diffInDays<=30 && diffInDays>=0){		
				billingrateExprng++;
			    billing.setRatestatus("current");
			    billingrate.add(billing);
			}		
			else if(diffInDays<0){
				billingrateExprd++;
				billing.setRatestatus("expired");
				billingrate.add(billing);				
			}			
		}				
		}	
		    
		    
			
	        Comparator<BillingRate> comparator=new Comparator<BillingRate>() {
				@Override
				public int compare(BillingRate o1, BillingRate o2) {
					return  o1.getCompanyLocation().getName().compareTo(o2.getCompanyLocation().getName());
				}
			};
			
			Comparator<BillingRate> comparator1=new Comparator<BillingRate>() {
				@Override
				public int compare(BillingRate o1, BillingRate o2) {
					return  o1.getValidTo().compareTo(o2.getValidTo());
				}
			};
	        
			Comparator<BillingRate> comparator2=new Comparator<BillingRate>() {
				@Override
				public int compare(BillingRate o1, BillingRate o2) {
					return  o1.getTransferStation().getName().compareTo(o2.getTransferStation().getName());
				}
			};  
			
			Comparator<BillingRate> comparator3=new Comparator<BillingRate>() {
				@Override
				public int compare(BillingRate o1, BillingRate o2) {
					return  o1.getLandfill().getName().compareTo(o2.getLandfill().getName());
				}
			};  
		    
			
			ComparatorChain chain = new ComparatorChain();  		
			
				chain.addComparator(comparator);
				chain.addComparator(comparator1);			
			    chain.addComparator(comparator2);
			    chain.addComparator(comparator3);
			    Collections.sort(billingrate, chain);
			
			
		model.addAttribute("billingrate",billingrate);		
		model.addAttribute("billingrateExprdCount",billingrateExprd);
		model.addAttribute("billingrateExprngCount",billingrateExprng);
		return "";				
	}	
	
	
	///function to get expired subcontractor rates and its count
	public String checkExpiredSubcontractorRates(ModelMap model, HttpServletRequest request,List<SubcontractorRate> subcontractorRates){
		
		    int subcontractorRateExprng=0,subcontractorRateExprd=0;		   
		    Long subcontractoridtemp=-1l;
			List<SubcontractorRate> subcontractorRate=new ArrayList<SubcontractorRate>();			
			
		    for(SubcontractorRate subcontractorrate: subcontractorRates){	
			int diffInDays=31;
			Long transferid=subcontractorrate.getTransferStation().getId();
			Long landfillid=subcontractorrate.getLandfill().getId();
			Long subcontractorid=subcontractorrate.getSubcontractor().getId();
			if(transferidtemp!=transferid || landfillidtemp!=landfillid || subcontractorid!=subcontractoridtemp){
				count=0;				
				if(subcontractorrate.getStatus()==1)
					check="yes";
				subcontractoridtemp=subcontractorid;
				transferidtemp=transferid;			
				landfillidtemp=landfillid;				
				}			
			if(transferidtemp==transferid && landfillidtemp==landfillid && subcontractoridtemp==subcontractorid && count==0 && check.equalsIgnoreCase("yes")){
				count++;
				check="no";
				if( subcontractorrate.getValidFrom().getTime()<=new Date().getTime()) 
			diffInDays = (int) ((subcontractorrate.getValidTo().getTime()- new Date().getTime()) / (1000 * 60 * 60 * 24));	
				else
				count=0;
			if(diffInDays<=30 && diffInDays>=0){			
				subcontractorRateExprng++;	
				subcontractorrate.setRatestatus("current");
				subcontractorRate.add(subcontractorrate);									
			}	
			else if(diffInDays<0){
				subcontractorRateExprd++;
				subcontractorrate.setRatestatus("expired");
				subcontractorRate.add(subcontractorrate);
			}
			}
		}		   
			model.addAttribute("subcontractorRate",subcontractorRate);	
			model.addAttribute("subcontractorRateExprdCount",subcontractorRateExprd);
			model.addAttribute("subcontractorRateExprngCount",subcontractorRateExprng);
			return "";		
	}
	
	
	///function to get expired Fuel surcharge padd and its count
	public String checkExpiredFuelSurchargeRates(ModelMap model, HttpServletRequest request,List<FuelSurchargePadd> fuelSurchargePadds){
		
		String codeTemp="";
		 int fuelsrchrgpaddExprng=0,fuelsrchrgpaddExprd=0;	
		List<FuelSurchargePadd> fuelSurchargePadd=new ArrayList<FuelSurchargePadd>();			
		
		for(FuelSurchargePadd fuelsurchargepadd: fuelSurchargePadds){
			int diffInDays=6;
			String code=fuelsurchargepadd.getCode();
			if(code!=null){
			if(!codeTemp.equalsIgnoreCase(code)){
				count=0;
				if(fuelsurchargepadd.getStatus()==1)
					check="yes";
				codeTemp=code;
			}
			if(count==0 && code.equalsIgnoreCase(codeTemp) && check.equalsIgnoreCase("yes")){
				count++;
				check="no";
				if(fuelsurchargepadd.getValidFrom().getTime()<=new Date().getTime())
			 diffInDays = (int) ((fuelsurchargepadd.getValidTo().getTime()- new Date().getTime()) / (1000 * 60 * 60 * 24));	
				else
					count=0;
			if(diffInDays<=5 && diffInDays>=0){			
				fuelsrchrgpaddExprng++;	
				fuelsurchargepadd.setRatestatus("current");
				fuelSurchargePadd.add(fuelsurchargepadd);
			}	
			else if(diffInDays<0){
				fuelsrchrgpaddExprd++;	
				fuelsurchargepadd.setRatestatus("expired");
				fuelSurchargePadd.add(fuelsurchargepadd);
			}
			}
			}
		}
		    model.addAttribute("fuelSurchargePadd",fuelSurchargePadd);			
			model.addAttribute("fuelSurchargePaddExprdCount",fuelsrchrgpaddExprd);
			model.addAttribute("fuelSurchargePaddExprngCount",fuelsrchrgpaddExprng);
			return "";
		}
		

	///function to get expired FuelSurchargeWeekly Rate and its count
	public String checkExpiredFuelSurchargeWeeklyRates(ModelMap model, HttpServletRequest request,List<FuelSurchargeWeeklyRate> fuelSurchargeWeeklyRate){
		      
		     int fuelschrgweeklyExprng=0,fuelschrgweeklyExprd=0;			
			 List<FuelSurchargeWeeklyRate> surchargeWeeklyRate=new ArrayList<FuelSurchargeWeeklyRate>();					
			
			for(FuelSurchargeWeeklyRate fuelsurchargeweeklyrate: fuelSurchargeWeeklyRate){
				int diffInDays=6;
				Long transferid=fuelsurchargeweeklyrate.getTransferStations().getId();
				Long landfillid=fuelsurchargeweeklyrate.getLandfillStations().getId();
				
				if(transferidtemp!=transferid || landfillidtemp!=landfillid ){ ///code to check for new origin and destination. 
				count=0;     
				if(fuelsurchargeweeklyrate.getStatus()==1)
					check="yes";
				transferidtemp=transferid;			
				landfillidtemp=landfillid;
				}
				if(transferidtemp==transferid && landfillidtemp==landfillid && count==0 && check.equalsIgnoreCase("yes")){
					count++;
					check="no";
				if( fuelsurchargeweeklyrate.getFromDate().getTime()<=new Date().getTime()) 
			        diffInDays = (int) ((fuelsurchargeweeklyrate.getToDate().getTime()- new Date().getTime()) / (1000 * 60 * 60 * 24));		
				else
					count=0;
				
				if(diffInDays<=5 && diffInDays>=0){			
					fuelschrgweeklyExprng++;
					fuelsurchargeweeklyrate.setRatestatus("current");
					surchargeWeeklyRate.add(fuelsurchargeweeklyrate);
				}	
				else if(diffInDays<0){
				fuelschrgweeklyExprd++;
				fuelsurchargeweeklyrate.setRatestatus("expired");
				surchargeWeeklyRate.add(fuelsurchargeweeklyrate);
				}
			}
			}
			    model.addAttribute("surchargeWeeklyRate",surchargeWeeklyRate);					
				model.addAttribute("weeklyRateExprdCount",fuelschrgweeklyExprd);
				model.addAttribute("weeklyRateExprngCount",fuelschrgweeklyExprng);
				return "";
			}	
	
	
	public void getTransferAndLandfill(ModelMap model,HttpServletRequest request)
	{
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("transferStation",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfill",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
	}
	
	}




