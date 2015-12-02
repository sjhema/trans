package com.primovision.lutransport.controller.admin;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Terminal;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.ReportService;

@Controller
@RequestMapping("/admin/tripsheetticketverification")
public class TripSheetTicketVerification extends CRUDController<Ticket> {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public TripSheetTicketVerification() {
		setUrlContext("admin/tripsheetticketverification");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(
				Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(
				Vehicle.class));
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(
				SubContractor.class));
	}
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		criterias.clear();
		if (request.getParameter("id") != null) {
			criterias.clear();
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			criterias.clear();
			model.addAttribute("subcontractors",
					genericDAO.findByCriteria(SubContractor.class, criterias, "name", false));
			model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		
			if (((Ticket)model.get("modelObject")).getLoadDate() == null && ((Ticket)model.get("modelObject")) != null) {
				model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit order by obj.unit"));
			} else {
				System.out.println("Loading Truck numbers valid for: Load date = " + ((Ticket)model.get("modelObject")).getLoadDate() + " , unload date = " + ((Ticket)model.get("modelObject")).getUnloadDate()); 
				System.out.println("Truck number = " + ((Ticket)model.get("modelObject")).getVehicle().getId());
				String truckQueryUsingLoadDate = queryForTrucksUsingLoadDate( ((Ticket)model.get("modelObject")));
				model.addAttribute("trucks",
						genericDAO.executeSimpleQuery(truckQueryUsingLoadDate));
			}
			
			model.addAttribute("trailers",
					genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 order by obj.unit"));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("dataType", "Payroll_Pending");
			map2.put("dataValue", "0,1,2");
			model.addAttribute("payrollPending", genericDAO.findByCriteria(StaticData.class, map2, "dataText", false));
		} else {
			criterias.clear();
			criterias.put("status", 1);
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			criterias.clear();
			criterias.put("status", 1);
			model.addAttribute("subcontractors",
					genericDAO.findByCriteria(SubContractor.class, criterias, "name", false));
			criterias.put("status", 1);
			model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
			
			/**
			 *  If id=null, load dates are not available, hence populate all trucks
			 */
			if (((Ticket)model.get("modelObject")).getLoadDate() == null && ((Ticket)model.get("modelObject")) != null) {
				model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit order by obj.unit"));
			} else {
				System.out.println("Loading Truck numbers valid for: Load date = " + ((Ticket)model.get("modelObject")).getLoadDate() + " , unload date = " + ((Ticket)model.get("modelObject")).getUnloadDate()); 
				System.out.println("Truck number = " + ((Ticket)model.get("modelObject")).getVehicle().getId());
				String truckQueryUsingLoadDate = queryForTrucksUsingLoadDate( ((Ticket)model.get("modelObject")));
				model.addAttribute("trucks",
						genericDAO.executeSimpleQuery(truckQueryUsingLoadDate));
			}
			
			model.addAttribute("trailers", genericDAO.executeSimpleQuery(
					"select obj from Vehicle obj where obj.type=2 group by obj.unit order by obj.unit"));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("dataType", "Payroll_Pending");
			map2.put("dataValue", "0,1,2");
			model.addAttribute("payrollPending", genericDAO.findByCriteria(StaticData.class, map2, "dataText", false));

		}
		
	if(request.getParameter("id")!=null){
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		criterias.put("id!",91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}else{
		criterias.clear();
		criterias.put("status", 1);
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("status", 1);
		criterias.put("type", 2);
		criterias.put("id!",91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}

		
		
		
		criterias.clear();
		
		criterias.put("type", 3);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		
		criterias.put("type", 4);
		model.addAttribute("terminalslist", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();		
		
		Map<String,Object> map1=new HashMap<String,Object>();
		map1.put("dataType", "TICKET_STATUS");
		map1.put("dataValue", "0,1");
		model.addAttribute("ticketStatus", genericDAO.findByCriteria(StaticData.class, map1,"dataText",false));
		
		System.out.println("Truck = " + ((Ticket)model.get("modelObject")).getVehicle());
	}
	
	private String queryForTrucksUsingLoadDate(Ticket entity) {
		String vehicleQuery = StringUtils.EMPTY;
		if(entity.getLoadDate()!=null){
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.validFrom<='"+ dateFormat.format(entity.getLoadDate())+"' and obj.validTo>='"+dateFormat.format(entity.getLoadDate())+"'";
		}
		else if(entity.getUnloadDate()!=null){
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.validFrom<='"+entity.getUnloadDate()+"' and obj.validTo>='"+entity.getUnloadDate()+"'";
		}
		System.out.println("******* The vehicle query for driver tripsheet is "+vehicleQuery);
		
		return vehicleQuery;
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Ticket entity,
			BindingResult bindingResult, ModelMap model) {
		Long ticketId = -1l;
		Map criteria = new HashMap();
		String type = request.getParameter("type");		
        request.getSession().setAttribute("createdBy",entity.getCreatedBy());
        //request.getSession().setAttribute("subcontractor", entity.getSubcontractor());
        request.getSession().setAttribute("truck", entity.getVehicle());
        request.getSession().setAttribute("trailer", entity.getTrailer());
        request.getSession().setAttribute("terminal", entity.getTerminal());
        request.getSession().setAttribute("batchDate", entity.getBillBatch());
        request.getSession().setAttribute("driver",entity.getDriver());
        request.getSession().setAttribute("driverCompany",entity.getDriverCompany());
		// Validate for Duplicate OriginTicket#
       
		if (type.equals("complete")) {	
			// validate entity
			  
			
			if (entity.getId()!=null){
				ticketId=entity.getId();				
			}
			
			if (entity.getDriverCompany() == null) {
				bindingResult.rejectValue("driverCompany", "error.select.option",
						null, null);
			}
			
			if (entity.getDriver() == null) {
				bindingResult.rejectValue("driver", "error.select.option",
						null, null);
			}
			if (entity.getVehicle()==null) {
				bindingResult.rejectValue("vehicle", "error.select.option",
						null, null);
			}
			if (entity.getOrigin() == null) {
				bindingResult.rejectValue("origin", "error.select.option",
						null, null);
			}
			if (entity.getDestination() == null) {
				bindingResult.rejectValue("destination", "error.select.option",
						null, null);
			}
			if (entity.getTrailer()==null) {
				bindingResult.rejectValue("trailer", "error.select.option",
						null, null);
			}
			if (entity.getCreatedBy() == null) {
				bindingResult.rejectValue("createdBy", "error.select.option",
						null, null);
			}
			if (entity.getUnloadDate()!=null && entity.getLoadDate()!=null) {
				if (entity.getUnloadDate().before(entity.getLoadDate())) {
					bindingResult.rejectValue("unloadDate", "error.textbox.unloadDate",
							null, null);
				}
			}
		/*	if(reportService.checkDuplicate(entity,"O")){
				bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);		
			}
			if(reportService.checkDuplicate(entity,"D")){
				bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);		
			}*/
			/*if (getUser(request).getBillBatchDate()!=null) {
				entity.setBillBatch(getUser(request).getBillBatchDate());
			}*/		
			
			
			if(entity.getOrigin() != null && entity.getDestination() != null && entity.getOriginTicket() !=null && entity.getDestinationTicket() !=null){
				/*String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.destination.id="+entity.getDestination().getId()+" and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;*/
				String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
				if (tickets!=null && tickets.size()>0){		
					bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);		
				}
				/*String destination = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;*/
				String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
				if (tickets1!=null && tickets1.size()>0){		
					bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);		
				}
				}
			
			
			
			try {
				getValidator().validate(entity, bindingResult);
			} catch (ValidationException e) {
				e.printStackTrace();
				log.warn("Error in validation :" + e);
			}
			
			
	if(entity.getDriverCompany()!=null){
			
		if(entity.getDriverCompany().getId()!=149l){
			
			if (entity.getUnloadDate()!=null && entity.getBillBatch()!=null) {
				
				
				if (entity.getUnloadDate().after(entity.getBillBatch()) || entity.getUnloadDate().before(entity.getBillBatch())) {
			
					LocalDate billBatchDate = new LocalDate(entity.getBillBatch());
					int billBatchDateWeek = billBatchDate.get(DateTimeFieldType.weekOfWeekyear());
					int billBatchDateYear = billBatchDate.get(DateTimeFieldType.weekyear());
				
					LocalDate unloadDate = new LocalDate(entity.getUnloadDate());
					int unloadDateWeek = unloadDate.get(DateTimeFieldType.weekOfWeekyear());
					int unloadDateYear = unloadDate.get(DateTimeFieldType.weekyear());			
					
					if((billBatchDateWeek != unloadDateWeek) || (billBatchDateYear != unloadDateYear)){
						bindingResult.rejectValue("unloadDate", "error.invalid.unloadDate",
								null, null);
					}
			
				}
			
			
		  }
		}
		else{
			// do nothing
		}
	}	
			
			
			// return to form if we had errors
			if (bindingResult.hasErrors()) {
				setupCreate(model, request);
				System.out.println("\nreturning to ticket form\n");
				return urlContext + "/form";
			}
			
			
			if (entity.getVehicle() != null) {
				boolean sendError=true;
				
				
				Vehicle vehob=genericDAO.getById(Vehicle.class,entity.getVehicle().getId());
				
				String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
					+vehob.getUnit()
					+") order by obj.validFrom desc";
				
				
				
				List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
				
				if(vehicleLists!=null && vehicleLists.size()>0){				
				for(Vehicle vehicleObj : vehicleLists) {	
					if(vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
					if ((entity.getLoadDate().getTime()>= vehicleObj.getValidFrom().getTime() && entity.getLoadDate().getTime()<= vehicleObj.getValidTo().getTime()) && (entity.getUnloadDate().getTime()>= vehicleObj.getValidFrom().getTime() && entity.getUnloadDate().getTime()<= vehicleObj.getValidTo().getTime())) {
						entity.setVehicle(vehicleObj);
						sendError=false;
					}
				}
				}
				}
				
				if(sendError){
					setupCreate(model, request);
					request.getSession().setAttribute("error",
					"No Matching Vehicle Entries Found for Selected Truck and Entered Date(s).");
					return urlContext + "/form";
				}
			}
			
			
			
			if(entity.getTrailer()!=null) {
				boolean sendError=true;
				
				Vehicle vehob=genericDAO.getById(Vehicle.class,entity.getTrailer().getId());
				String vehiclequery="select obj from Vehicle obj where obj.type=2 and obj.unit in ("
					+vehob.getUnit()
					+") order by obj.validFrom desc";
				
				
				
				List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
				
				if(vehicleLists!=null && vehicleLists.size()>0){				
				for(Vehicle vehicleObj : vehicleLists) {					
					if(vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
					if ((entity.getLoadDate().getTime()>= vehicleObj.getValidFrom().getTime() && entity.getLoadDate().getTime()<= vehicleObj.getValidTo().getTime()) && (entity.getUnloadDate().getTime()>= vehicleObj.getValidFrom().getTime() && entity.getUnloadDate().getTime()<= vehicleObj.getValidTo().getTime())) {
						entity.setTrailer(vehicleObj);
						sendError=false;
					}	
					}
				}
				}
				
				if(sendError){
					setupCreate(model, request);
					request.getSession().setAttribute("error",
					"No Matching Vehicle Entries Found for Selected Trailer and Entered Date(s).");
					return urlContext + "/form";
				}
				
			}
			
			
			
			entity.setStatus(1);
			if(entity.getTicketStatus()== null){				
				entity.setTicketStatus(1);
			}
			
			
			
			
			if(entity.getPayRollStatus()==null){
				if(entity.getPayRollBatch()==null)
					entity.setPayRollStatus(1);
				else
					entity.setPayRollStatus(2);
			}
			else{
				if(entity.getPayRollBatch()==null){
					//do nothing
				}
				else{
					entity.setPayRollStatus(2);
				}
			}
			
			
			
			/*// return to form if we had errors
			if (bindingResult.hasErrors()) {
				setupCreate(model, request);
				System.out.println("\nreturnibg to ticket form\n");
				return urlContext + "/form";
			}*/
			
			//FOR CUSTOMER AND COMPANY_LOCATION
			String  query;
			//for Growa or Tullytown
			if(entity.getDestination().getId()==253||entity.getDestination().getId()==254){
			query="select obj from BillingRate obj where obj.transferStation="+entity.getOrigin().getId()+" and obj.landfill=91";
			}else{
				query="select obj from BillingRate obj where obj.transferStation="+entity.getOrigin().getId()+" and obj.landfill="+entity.getDestination().getId();
			}
			List<BillingRate> rates=genericDAO.executeSimpleQuery(query);
			BillingRate billingRate=null;
			for(BillingRate rate:rates) 
			{
				if (rate.getRateUsing()==null)
				{
					billingRate = rate;
					break;
				}
				else if (rate.getRateUsing()==1) 
				{
					//calculation for a load date
					if ((entity.getLoadDate().getTime()>= rate.getValidFrom().getTime()) && (entity.getLoadDate().getTime()<= rate.getValidTo().getTime())) {
						billingRate = rate;
						break;
					}
				}
				else if (rate.getRateUsing()==2)
				{
					//calculation for a unload date
					if ((entity.getUnloadDate().getTime()>= rate.getValidFrom().getTime()) && (entity.getUnloadDate().getTime()<= rate.getValidTo().getTime())) {
						billingRate = rate;
						break;
					}
				}
			}
			
           if(billingRate != null)
			{
			  entity.setCustomer((billingRate.getCustomername()!=null)?billingRate.getCustomername():null);
			  entity.setCompanyLocation((billingRate.getCompanyLocation()!=null)?billingRate.getCompanyLocation():null);
			  //if(billingRate.getBilledby().equals("bygallon")){
        		  entity.setGallons(entity.getTransferNet()/8.34);
        	  // }
			}
			/*else
			{
				request.getSession().setAttribute("error","Rate is expaired,please contect to administrator");
				return "redirect:create.do";
			}*/
                      
			Driver driver = genericDAO.getById(Driver.class,Long.valueOf(entity.getDriver().getId()));
			entity.setTerminal(driver.getTerminal());			
			
			
			if(entity.getSubcontractor() != null){
				
				SubContractor subcontractor = genericDAO.getById(SubContractor.class, entity.getSubcontractor().getId());
				if(subcontractor.getName().equalsIgnoreCase("blank"))
					entity.setSubcontractor(null);				
			}
			
			
			
			
			beforeSave(request, entity, model);
			User user=genericDAO.getById(User.class,entity.getCreatedBy());
			entity.setEnteredBy(user.getName());
			// merge into datasource
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
			// return to list
			setupCreate(model, request);
			request.getSession().setAttribute("msg",
					"Ticket added successfully");
			
			String tripsheetQuery = "select obj from TripSheet obj  where (obj.origin in ("+entity.getOrigin().getId()+") and obj.originTicket in ("+entity.getOriginTicket()+")) OR (obj.destination in ("+entity.getDestination().getId()+") and obj.destinationTicket in ("+entity.getDestinationTicket()+"))";
			
			List<TripSheet> tripsheets  = genericDAO.executeSimpleQuery(tripsheetQuery);
			
			if(tripsheets!=null && tripsheets.size()>0){
				for(TripSheet tripsheet:tripsheets){
					tripsheet.setBatchDate(entity.getBillBatch());
					tripsheet.setDriverCompany(entity.getDriverCompany());
					criteria.clear();
					criteria.put("id", entity.getDriverCompany().getId());
					Location loc = genericDAO.getByCriteria(Location.class, criteria);
					tripsheet.setCompanyName(loc.getName());				
					tripsheet.setTerminal(entity.getTerminal());
					tripsheet.setTerminalName(entity.getTerminal().getName());				
					tripsheet.setDriver(entity.getDriver());				
					tripsheet.setTrailer(entity.getTrailer());
					tripsheet.setTempTrailer(entity.getTrailer().getUnitNum());				
					tripsheet.setTruck(entity.getVehicle());
					tripsheet.setTempTruck(entity.getVehicle().getUnitNum());
					tripsheet.setDestination(entity.getDestination());
					tripsheet.setDestinationTicket(entity.getDestinationTicket());					
					tripsheet.setIncomplete("No");
					tripsheet.setLoadDate(entity.getLoadDate());
					tripsheet.setOrigin(entity.getOrigin());
					tripsheet.setOriginTicket(entity.getOriginTicket());					
					tripsheet.setUnloadDate(entity.getUnloadDate());
					tripsheet.setVerificationStatus("Verified");
					genericDAO.saveOrUpdate(tripsheet);
				}
			}
			else{
				TripSheet tripsheet = new TripSheet();
				tripsheet.setBatchDate(entity.getBillBatch());
				tripsheet.setDriverCompany(entity.getDriverCompany());
				criteria.clear();
				criteria.put("id", entity.getDriverCompany().getId());
				Location loc = genericDAO.getByCriteria(Location.class, criteria);
				tripsheet.setCompanyName(loc.getName());				
				tripsheet.setTerminal(entity.getTerminal());
				tripsheet.setTerminalName(entity.getTerminal().getName());				
				tripsheet.setDriver(entity.getDriver());				
				tripsheet.setTrailer(entity.getTrailer());
				tripsheet.setTempTrailer(entity.getTrailer().getUnitNum());				
				tripsheet.setTruck(entity.getVehicle());
				tripsheet.setTempTruck(entity.getVehicle().getUnitNum());
				tripsheet.setDestination(entity.getDestination());
				tripsheet.setDestinationTicket(entity.getDestinationTicket());					
				tripsheet.setIncomplete("No");
				tripsheet.setLoadDate(entity.getLoadDate());
				tripsheet.setOrigin(entity.getOrigin());
				tripsheet.setOriginTicket(entity.getOriginTicket());					
				tripsheet.setUnloadDate(entity.getUnloadDate());
				tripsheet.setVerificationStatus("Verified");
				tripsheet.setEnteredBy("system");
				genericDAO.saveOrUpdate(tripsheet);			
			}
			
			
			//return "redirect:/admin/tripsheetreview/list.do";
			return "redirect:/admin/tripsheetticketverification/create.do";
			
		} 
		else {
			/*if(reportService.checkDuplicate(entity,"O")){
				bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);		
			}
			else if(reportService.checkDuplicate(entity,"D")){
				bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);		
			}*/
			
			
			if (entity.getId()!=null){
				ticketId=entity.getId();				
			}			
			
			if (entity.getOrigin() == null) {
				bindingResult.rejectValue("origin", "error.select.option",
						null, null);
			}
			if (entity.getDestination() == null) {
				bindingResult.rejectValue("destination", "error.select.option",
						null, null);
			}
			
			
			if(entity.getOrigin() != null && entity.getDestination() != null && entity.getOriginTicket() !=null && entity.getDestinationTicket() !=null){
				String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
				if (tickets!=null && tickets.size()>0){		
					bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);		
				}
				String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
				if (tickets1!=null && tickets1.size()>0){		
					bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);		
				}
				}
			
			try {
				getValidator().validate(entity, bindingResult);
			} catch (ValidationException e) {
				e.printStackTrace();
				log.warn("Error in validation :" + e);
			}			
			
			// return to form if we had errors
			if (bindingResult.hasErrors()) {
				setupCreate(model, request);
				return urlContext + "/form";
			}
			//entity.setBillBatch(getUser(request).getBillBatchDate());
			
			//entity.setTicketStatus(0);
			entity.setStatus(3);
			if(entity.getTicketStatus()!=null){
				if(entity.getTicketStatus()!=2){
					System.out.println("\nNEXT entity.getTicketStatus()!=2\n");
					entity.setTicketStatus(0);
				}
			}
			else{
				entity.setTicketStatus(0);
			}
			
			if(entity.getPayRollStatus()==null){
				if(entity.getPayRollBatch()==null)
					entity.setPayRollStatus(1);
				else
					entity.setPayRollStatus(2);
			}
			else{
				if(entity.getPayRollBatch()==null){
					//do nothing
				}
				else{
					entity.setPayRollStatus(2);
				}
			}
			
			if(entity.getSubcontractor() != null){
				
				SubContractor subcontractor = genericDAO.getById(SubContractor.class, entity.getSubcontractor().getId());
				if(subcontractor.getName().equalsIgnoreCase("blank"))
					entity.setSubcontractor(null);				
			}
			
			
			beforeSave(request, entity, model);
			User user=genericDAO.getById(User.class,entity.getCreatedBy());
			entity.setEnteredBy(user.getName());
			
			genericDAO.saveOrUpdate(entity);
			
			String tripsheetQuery = "select obj from TripSheet obj  where (obj.origin in ("+entity.getOrigin().getId()+") and obj.originTicket in ("+entity.getOriginTicket()+")) OR (obj.destination in ("+entity.getDestination().getId()+") and obj.destinationTicket in ("+entity.getDestinationTicket()+"))";
			
			List<TripSheet> tripsheets  = genericDAO.executeSimpleQuery(tripsheetQuery);
			
			if(tripsheets!=null && tripsheets.size()>0){
				for(TripSheet tripsheet:tripsheets){
					tripsheet.setBatchDate(entity.getBillBatch());
					if(entity.getDriverCompany()!=null){
						tripsheet.setDriverCompany(entity.getDriverCompany());
						criteria.clear();
						criteria.put("id", entity.getDriverCompany().getId());
						Location loc = genericDAO.getByCriteria(Location.class, criteria);
						tripsheet.setCompanyName(loc.getName());
					}
					if(entity.getTerminal()!=null){
						tripsheet.setTerminal(entity.getTerminal());
						criteria.clear();
						criteria.put("id", entity.getTerminal().getId());
						Location loc = genericDAO.getByCriteria(Location.class, criteria);
						tripsheet.setTerminalName(loc.getName());
					}
					if(entity.getDriver()!=null){
						tripsheet.setDriver(entity.getDriver());
					}
					if(entity.getTrailer()!=null){
						tripsheet.setTrailer(entity.getTrailer());
						criteria.clear();
						criteria.put("id", entity.getTrailer().getId());
						Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
						tripsheet.setTempTrailer(veh.getUnitNum());
					}
					if(entity.getVehicle()!=null){
						tripsheet.setTruck(entity.getVehicle());
						criteria.clear();
						criteria.put("id", entity.getVehicle().getId());
						Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
						tripsheet.setTempTruck(veh.getUnitNum());
					}
					tripsheet.setDestination(entity.getDestination());
					tripsheet.setDestinationTicket(entity.getDestinationTicket());					
					tripsheet.setIncomplete("No");
					tripsheet.setLoadDate(entity.getLoadDate());
					tripsheet.setOrigin(entity.getOrigin());
					tripsheet.setOriginTicket(entity.getOriginTicket());					
					tripsheet.setUnloadDate(entity.getUnloadDate());
					tripsheet.setVerificationStatus("Verified");
					genericDAO.saveOrUpdate(tripsheet);
				}
			}
			else{
				TripSheet tripsheet = new TripSheet();

				tripsheet.setBatchDate(entity.getBillBatch());
				if(entity.getDriverCompany()!=null){
					tripsheet.setDriverCompany(entity.getDriverCompany());
					criteria.clear();
					criteria.put("id", entity.getDriverCompany().getId());
					Location loc = genericDAO.getByCriteria(Location.class, criteria);
					tripsheet.setCompanyName(loc.getName());
				}
				if(entity.getTerminal()!=null){
					tripsheet.setTerminal(entity.getTerminal());
					criteria.clear();
					criteria.put("id", entity.getTerminal().getId());
					Location loc = genericDAO.getByCriteria(Location.class, criteria);
					tripsheet.setTerminalName(loc.getName());
				}
				if(entity.getDriver()!=null){
					tripsheet.setDriver(entity.getDriver());
				}
				if(entity.getTrailer()!=null){
					tripsheet.setTrailer(entity.getTrailer());
					criteria.clear();
					criteria.put("id", entity.getTrailer().getId());
					Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
					tripsheet.setTempTrailer(veh.getUnitNum());
				}
				if(entity.getVehicle()!=null){
					tripsheet.setTruck(entity.getVehicle());
					criteria.clear();
					criteria.put("id", entity.getVehicle().getId());
					Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
					tripsheet.setTempTruck(veh.getUnitNum());
				}
				tripsheet.setDestination(entity.getDestination());
				tripsheet.setDestinationTicket(entity.getDestinationTicket());					
				tripsheet.setIncomplete("No");
				tripsheet.setLoadDate(entity.getLoadDate());
				tripsheet.setOrigin(entity.getOrigin());
				tripsheet.setOriginTicket(entity.getOriginTicket());					
				tripsheet.setUnloadDate(entity.getUnloadDate());
				tripsheet.setVerificationStatus("Verified");
				tripsheet.setEnteredBy("system");
				genericDAO.saveOrUpdate(tripsheet);
			
			}
			
			return "redirect:/admin/tripsheetticketverification/create.do";
			
			//return "redirect:/admin/tripsheetreview/list.do";
		}
	}
	
	
	@ModelAttribute("modelObject")
	public Ticket setupModel(HttpServletRequest request) {
		String tsID = request.getParameter("tripsheetId");
		if (tsID == null || org.apache.commons.lang.StringUtils.isEmpty(tsID)) {		
			Ticket ticket = getEntityInstance();
			//TripSheet tripSheet = genericDAO.getById(TripSheet.class, Long.parseLong(tsID));
			ticket.setDriver((Driver)request.getSession().getAttribute("driver"));
			ticket.setBillBatch((Date)request.getSession().getAttribute("batchDate"));
			ticket.setDriverCompany((Location)request.getSession().getAttribute("driverCompany"));
			ticket.setTerminal((Location)request.getSession().getAttribute("terminal"));
			ticket.setVehicle((Vehicle)request.getSession().getAttribute("truck"));
			ticket.setTrailer((Vehicle)request.getSession().getAttribute("trailer"));
			//ticket.setOrigin((Location)request.getSession().getAttribute("origin"));
			//ticket.setDestination((Location)request.getSession().getAttribute("destination"));
			//ticket.setOriginTicket((Long)request.getSession().getAttribute("orgTicket"));
			//ticket.setDestinationTicket((Long)request.getSession().getAttribute("destTicket"));
			//ticket.setLoadDate((Date)request.getSession().getAttribute("loadDate"));
			ticket.setCreatedBy((Long)request.getSession().getAttribute("createdBy"));
			//ticket.setUnloadDate((Date)request.getSession().getAttribute("unloadDate"));			
			return ticket;
		} else {
			Ticket ticket = getEntityInstance();
			TripSheet tripSheet = genericDAO.getById(TripSheet.class, Long.parseLong(tsID));
			ticket.setDriver(tripSheet.getDriver());
			ticket.setBillBatch(tripSheet.getBatchDate());
			ticket.setDriverCompany(tripSheet.getDriverCompany());
			ticket.setTerminal(tripSheet.getTerminal());
			ticket.setVehicle(tripSheet.getTruck());
			ticket.setTrailer(tripSheet.getTrailer());
			ticket.setOrigin(tripSheet.getOrigin());
			ticket.setDestination(tripSheet.getDestination());
			ticket.setOriginTicket(tripSheet.getOriginTicket());
			ticket.setDestinationTicket(tripSheet.getDestinationTicket());
			ticket.setLoadDate(tripSheet.getLoadDate());
			ticket.setUnloadDate(tripSheet.getUnloadDate());			
			return ticket;
		}
	}
	
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if ("findTransferContents".equalsIgnoreCase(action)) {
			List<String> list = new ArrayList<String>();
			Double transferGross = Double.valueOf(request
					.getParameter("transferGross"));
			Double transferTare = Double.valueOf(request
					.getParameter("transferTare"));
			Double transferNet = transferGross - transferTare;
			Double transferTonns = transferNet / 2000.0;
			DecimalFormat df = new DecimalFormat("#0.00");
			String tNet = df.format(transferNet);
			String tTonn = df.format(transferTonns);
			list.add(tNet);
			list.add(tTonn);
			Gson gson = new Gson();
			return gson.toJson(list);
		}
		if ("findLandFillContenets".equalsIgnoreCase(action)) {
			List<String> list = new ArrayList<String>();
			Double landfillGross = Double.valueOf(request
					.getParameter("landfillGross"));
			Double landfillTare = Double.valueOf(request
					.getParameter("landfillTare"));
			Double landfillNet = landfillGross - landfillTare;
			Double landfillTonns = landfillNet / 2000.0;
			DecimalFormat df = new DecimalFormat("#0.00");
			String lNet = df.format(landfillNet);
			String lTonn = df.format(landfillTonns);
			list.add(lNet);
			list.add(lTonn);
			Gson gson = new Gson();
			return gson.toJson(list);
		}
		
		
		if ("findTerminal".equalsIgnoreCase(action)) {

			List<Terminal> dlst=null;
			List<Location> dlst1=null;
			String terminalids="";
			String companyId = request.getParameter("company");
			if (!StringUtils.isEmpty(companyId))
			{						
				String query="select obj from Terminal obj where obj.status=1 and obj.company="+companyId+"";
				dlst=genericDAO.executeSimpleQuery(query);						
				if(dlst!=null && dlst.size()>0){
					for(Terminal terminalObj : dlst ){
						if(terminalids.equalsIgnoreCase("")){
							terminalids=terminalObj.getTerminal().getId().toString();
						}
						else{
							terminalids=terminalids+","+terminalObj.getTerminal().getId().toString();
						}
					}
					
					String query1="select obj from Location obj where obj.type=4 and  obj.status=1 and obj.id in ("+terminalids+")order by name ASC";	
					dlst1=genericDAO.executeSimpleQuery(query1);
					
				}
				else{
					
					dlst1=Collections.emptyList();
				}
			}
			Gson gson = new Gson();
			return gson.toJson(dlst1);
		}
		
		
		if ("findAllTerminal".equalsIgnoreCase(action)) {

			List<Driver> dlst=null;
			String query="select obj from Location obj where obj.type=4 and  obj.status=1 order by name ASC";
				dlst=genericDAO.executeSimpleQuery(query);
			
			Gson gson = new Gson();
			return gson.toJson(dlst);
		}
		
		
		if ("findDriver".equalsIgnoreCase(action)) {

			List<Driver> dlst=null;
			String terminalId = request.getParameter("terminal");
			String companyId = request.getParameter("company");
			
			if (!StringUtils.isEmpty(terminalId))
			{		
				if(!StringUtils.isEmpty(request.getParameter("tickId"))){				
				String query="select obj from Driver obj where obj.terminal="+terminalId+" and obj.company in ("+companyId+",149) order by fullName ASC";
				dlst=genericDAO.executeSimpleQuery(query);
				}
				else{
					String query="select obj from Driver obj where obj.status=1 and obj.terminal="+terminalId+" and obj.company in ("+companyId+",149) order by fullName ASC";
					
					dlst=genericDAO.executeSimpleQuery(query);
				}
			}
			Gson gson = new Gson();
			return gson.toJson(dlst);
		}

		if ("findAllDriver".equalsIgnoreCase(action)) {

			List<Driver> dlst=null;
				String query="select obj from Driver obj where obj.status=1 order by fullName ASC";
				dlst=genericDAO.executeSimpleQuery(query);
			
			Gson gson = new Gson();
			return gson.toJson(dlst);
		}
		
		
		if ("checkBillBatch".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			
			String billdte=request.getParameter("billbatchdte");
		    String loaddte=request.getParameter("loaddte");
		    String unloaddte=request.getParameter("unloaddte");
			
		    try{
		    Date billdate=dateFormat.parse(billdte);
		    Date loaddate=dateFormat.parse(loaddte);
		    Date unloaddate=dateFormat.parse(unloaddte);
		    
		    DateMidnight billbatch_date=new DateMidnight(billdate);
		    DateMidnight load_date=new DateMidnight(loaddate);
		    DateMidnight unload_date=new DateMidnight(unloaddate);	
		    
		    int daysBtwnLb=Days.daysBetween(load_date,billbatch_date).getDays();
		    int daysBtwnULb=Days.daysBetween(unload_date,billbatch_date).getDays();
		    System.out.println("****** the date between are "+daysBtwnLb+" and "+daysBtwnULb);
		    String mssg="";
		    if(daysBtwnLb<0 && daysBtwnULb<0){
		    	mssg="Load and Unload Dates are Past the Billbatch Date.";
		    }
		    else if(daysBtwnLb<0){
		    	mssg="Load Date is Past the Billbatch Date.";
		    }
		    else if(daysBtwnULb<0){		    
		    	mssg="UnLoad Date is Past the Billbatch Date.";
		    }
		    
		    return mssg;
		    } catch (Exception e) {
		    	System.out.println("****** =====> error "+e);
				// TODO: handle exception
			}			
			
		}
		
		
		if("getRemainingTicketData".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String landfill=request.getParameter("landfil");
		    String landticket=request.getParameter("originTcket");
		    List tickList = new ArrayList();
		    String origin = "select obj from TripSheet obj where obj.origin.id="+landfill+" and obj.originTicket="+landticket;
		    List<TripSheet> tripsheets = genericDAO.executeSimpleQuery(origin);
			if (tripsheets!=null && tripsheets.size()>0){	
				for(TripSheet tripSheetObj:tripsheets){
					tickList.add(tripSheetObj.getDriverCompany().getId());
					tickList.add(tripSheetObj.getDriver().getId());
					tickList.add(tripSheetObj.getTerminal().getId());
					//tickList.add(tripSheetObj.getTruck().getId());
					tickList.add(tripSheetObj.getTruck().getUnitNum());
					tickList.add(tripSheetObj.getTrailer().getId());
					tickList.add(dateFormat.format(tripSheetObj.getBatchDate()));
					tickList.add(tripSheetObj.getOrigin().getId());
					tickList.add(tripSheetObj.getOriginTicket());
					tickList.add(dateFormat.format(tripSheetObj.getLoadDate()));
					tickList.add(tripSheetObj.getDestination().getId());
					tickList.add(tripSheetObj.getDestinationTicket());
					tickList.add(dateFormat.format(tripSheetObj.getUnloadDate()));
					System.out.println("***** Enetered here");
				}
				Gson gson = new Gson();
				return gson.toJson(tickList);
			}
			else{
				return "";
			}
		}
		
		
		if("getRemainingTicketDataDest".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String landfill=request.getParameter("transferstation");
		    String landticket=request.getParameter("destinationTcket");
		    List tickList = new ArrayList();
		    String origin = "select obj from TripSheet obj where obj.destination.id="+landfill+" and obj.destinationTicket="+landticket;
		    List<TripSheet> tripsheets = genericDAO.executeSimpleQuery(origin);
			if (tripsheets!=null && tripsheets.size()>0){	
				for(TripSheet tripSheetObj:tripsheets){
					tickList.add(tripSheetObj.getDriverCompany().getId());
					tickList.add(tripSheetObj.getDriver().getId());
					tickList.add(tripSheetObj.getTerminal().getId());
//					tickList.add(tripSheetObj.getTruck().getId());
					tickList.add(tripSheetObj.getTruck().getUnitNum());
					tickList.add(tripSheetObj.getTrailer().getId());
					tickList.add(dateFormat.format(tripSheetObj.getBatchDate()));
					tickList.add(tripSheetObj.getOrigin().getId());
					tickList.add(tripSheetObj.getOriginTicket());
					tickList.add(dateFormat.format(tripSheetObj.getLoadDate()));
					tickList.add(tripSheetObj.getDestination().getId());
					tickList.add(tripSheetObj.getDestinationTicket());
					tickList.add(dateFormat.format(tripSheetObj.getUnloadDate()));
					System.out.println("***** Enetered here 2");
				}
				Gson gson = new Gson();
				return gson.toJson(tickList);
			}
			else{
				return "";
			}
		}
		
		
		if ("checkOriginTicket".equalsIgnoreCase(action)) {
			String mssg="";
			
			
			String landfill=request.getParameter("landfil");
		    String landticket=request.getParameter("originTcket");
		    String tickId=request.getParameter("ticId");
		    
            if(StringUtils.isEmpty(tickId)){
            	tickId=null;
		    }
		    
		    
		    String origin = "select obj from Ticket obj where obj.origin.id="+landfill+" and obj.originTicket="+landticket+" and obj.id != "+tickId;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
			if (tickets!=null && tickets.size()>0){		
				mssg="Duplicate Origin Ticket";	
			}
		    
			 return mssg;
			
		}
		
		if ("checkDestinationTicket".equalsIgnoreCase(action)) {
			String mssg="";
			
			
			
			
			
			String transfersttn=request.getParameter("transferstation");
		    String transferticket=request.getParameter("destinationTcket");
		    String tickId=request.getParameter("ticId");
			
		    if(StringUtils.isEmpty(tickId)){
            	tickId=null;
		    }
		    
		    
		    String destination = "select obj from Ticket obj where obj.destination.id="+transfersttn+" and obj.destinationTicket="+transferticket+" and obj.id != "+tickId;
			List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
			if (tickets1!=null && tickets1.size()>0){		
				mssg="Duplicate Destination Ticket";			
			}
			 return mssg;
		}
		
		
		
		if ("checkVehicle".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			 boolean sendError=true;
			String truckId=request.getParameter("truckId");
		    String loaddte=request.getParameter("loaddte");
		    String unloaddte=request.getParameter("unloaddte");
			
		    try{		   
		    Date loaddate=dateFormat.parse(loaddte);
		    Date unloaddate=dateFormat.parse(unloaddte);		    		
			Vehicle vehob=genericDAO.getById(Vehicle.class,Long.parseLong(truckId));
			
			String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
				+vehob.getUnit()
				+") order by obj.validFrom desc";
			
			
			
			List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
			
			if(vehicleLists!=null && vehicleLists.size()>0){				
			for(Vehicle vehicleObj : vehicleLists) {	
				if(vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
				if ((loaddate.getTime()>= vehicleObj.getValidFrom().getTime() && loaddate.getTime()<= vehicleObj.getValidTo().getTime()) && (unloaddate.getTime()>= vehicleObj.getValidFrom().getTime() && unloaddate.getTime()<= vehicleObj.getValidTo().getTime())) {					
					sendError=false;
				}
			}
			}
			}		    
		    }
		    catch (Exception e) {
				// TODO: handle exception
			}	
		    if(sendError){
		    	return "No Valid Vehicle Entry Found for Selected Truck.";
		    } 
		}
		
		
		if ("checkTrailer".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			 boolean sendError=true;	
			String trailerId=request.getParameter("trailerId");
		    String loaddte=request.getParameter("loaddte");
		    String unloaddte=request.getParameter("unloaddte");
			
		    try{		   
		    Date loaddate=dateFormat.parse(loaddte);
		    Date unloaddate=dateFormat.parse(unloaddte);
		    
		    Vehicle vehob=genericDAO.getById(Vehicle.class,Long.parseLong(trailerId));
			String vehiclequery="select obj from Vehicle obj where obj.type=2 and obj.unit in ("
				+vehob.getUnit()
				+") order by obj.validFrom desc";
			
			
			
			List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
			
			if(vehicleLists!=null && vehicleLists.size()>0){				
			for(Vehicle vehicleObj : vehicleLists) {					
				if(vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
				if ((loaddate.getTime()>= vehicleObj.getValidFrom().getTime() && loaddate.getTime()<= vehicleObj.getValidTo().getTime()) && (unloaddate.getTime()>= vehicleObj.getValidFrom().getTime() && unloaddate.getTime()<= vehicleObj.getValidTo().getTime())) {
					
					sendError=false;
				}	
				}
			}
			}		    
		    }
		    catch (Exception e) {
				// TODO: handle exception
			}
		    
		    if(sendError){
		    	return "No Valid Vehicle Entry Found for Selected Trailer.";
		    }    
		    
		}
		
		
		
		return "";
	}

}
