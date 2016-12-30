package com.primovision.lutransport.controller.operator;

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
import org.joda.time.DateTimeConstants;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Terminal;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.ReportService;

@Controller
@RequestMapping("/operator/ticket")
public class TicketController extends CRUDController<Ticket> {

	public TicketController() {
		setUrlContext("operator/ticket");
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

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
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
//		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(
//				Customer.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(
				SubContractor.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		String driverdIds = "";
		String driverId = (String) criteria.getSearchMap().get(
		"driver.id");		
		if (!StringUtils.isEmpty(driverId)) {			
			Map criterias = new HashMap();
			criterias.put("fullName",driverId);
			List<Driver> drivers = genericDAO.findByCriteria(Driver.class, criterias);
			if(drivers!=null && drivers.size()>0){
				for(Driver driver:drivers){
					if(driverdIds.equals(""))
						driverdIds = driver.getId().toString();
					else
						driverdIds = driverdIds+","+driver.getId();					
				}
				criteria.getSearchMap().put("driver.id",driverdIds);
			}
		}
		
		dateupdateService.updateDate(request, "billBatchDate", "billBatch");
		dateupdateService.updateDate(request, "lDate","loadDate");
		dateupdateService.updateDate(request, "uDate","unloadDate");
		dateupdateService.updateDate(request, "pDate","payRollBatch");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
//		model.addAttribute("customers", genericDAO.findByCriteria(
//				Customer.class, criterias, "firstName", false));
		criterias.clear();
/*		criterias.put("status", 1);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "lastName", false));*/
		if(request.getParameter("id")!=null){
		criterias.clear();
		/*EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);*/
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class,	criterias, "name", false));
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 order by obj.unit"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 order by obj.unit"));
		Map<String,Object> map2=new HashMap<String,Object>();		
		map2.put("dataType", "Payroll_Pending");
		map2.put("dataValue", "0,1,2");
		model.addAttribute("payrollPending", genericDAO.findByCriteria(StaticData.class, map2,"dataText",false));
		}
		else
		{
		criterias.clear();
		criterias.put("status", 1);
		/*EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);*/
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class,criterias, "fullName", false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class,	criterias, "name", false));
		criterias.put("status",1);
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1  group by obj.unit order by obj.unit"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit order by obj.unit"));
		Map<String,Object> map2=new HashMap<String,Object>();		
		map2.put("dataType", "Payroll_Pending");
		map2.put("dataValue", "0,1,2");
		model.addAttribute("payrollPending", genericDAO.findByCriteria(StaticData.class, map2,"dataText",false));
		
		
		}
		
		/*criterias.put("status", 1);
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class,	criterias, "name", false));*/
		criterias.clear();
		/*criterias.put("type", 1);
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		criterias.clear();
		criterias.put("type", 2);
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));*/
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
		
		// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
		String showDuplicateTicketOverrideMsg = "false";
		if (getUser(request).getId() == 20) {
			showDuplicateTicketOverrideMsg = "true";
		}
		model.addAttribute("showDuplicateTicketOverrideMsg", showDuplicateTicketOverrideMsg);
		
		//criterias.put("role.id", 2l);
		
		// model.addAttribute("ticketstatus", listStaticData("TICKET_STATUS"));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		criterias.clear();
		//criterias.put("status", 1);
		/*EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);		
		model.addAttribute("drivers", genericDAO.executeSimpleQuery("select obj from Driver obj where obj.catagory="+empobj.getId()+"  group by obj.fullName order by obj.fullName"));*/
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class,criterias, "fullName", false));
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,
				criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("destinations", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		
		criterias.clear();
		
		criterias.put("type", 3);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		
		criterias.put("type", 4);
		model.addAttribute("terminalslist", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		
		
		
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
		

	}

	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Ticket entity,
			BindingResult bindingResult, ModelMap model) {
		Long ticketId = -1l;
		Map criteria = new HashMap();
		String type = request.getParameter("type");		
		request.getSession().setAttribute("subcontractor", entity.getSubcontractor());
		request.getSession().setAttribute("truck", entity.getVehicle());
		request.getSession().setAttribute("trailer", entity.getTrailer());
		request.getSession().setAttribute("terminal", entity.getTerminal());
		request.getSession().setAttribute("createdBy", entity.getCreatedBy());
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
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (getUser(request).getId() != 20) {
						bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);
					}
				}
				/*String destination = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;*/
				String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
				if (tickets1!=null && tickets1.size()>0){	
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (getUser(request).getId() != 20) {
						bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);	
					}
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
			  // Change to 8.35 - 28th Dec 2016
        		  entity.setGallons(entity.getTransferNet()/8.35);
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
					
					/////// Newly Added
					Location location = genericDAO.getById(Location.class, entity
							.getDestination().getId());
					
					Driver driverObj = genericDAO.getById(Driver.class,entity.getDriver().getId());
					
					int payUsing = 0;
					
					String initRateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
						+ entity.getOrigin().getId() + "' and obj.landfill='"						
						+ entity.getDestination().getId() 
						+ "' and obj.company='"+entity.getDriverCompany().getId()
						+ "' and obj.terminal='"+entity.getTerminal().getId()
						+ "' order by obj.validTo";
					List<DriverPayRate>	initfs = genericDAO.executeSimpleQuery(initRateQuery);
				    if (initfs != null && initfs.size() > 0) {
				    	payUsing = initfs.get(0).getRateUsing();
				    }
				    else{
				    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
				    		String initRateQuery1 = "select obj from DriverPayRate obj where obj.transferStation='"
								+ entity.getOrigin().getId() + "' and obj.landfill='"						
								+ 91 
								+ "' and obj.company='"+entity.getDriverCompany().getId()
								+ "' and obj.terminal='"+entity.getTerminal().getId()
								+ "' order by obj.validTo";
				    		List<DriverPayRate>	initfs2 = genericDAO.executeSimpleQuery(initRateQuery1);
				    		if (initfs2 != null && initfs2.size() > 0) {
				    			payUsing = initfs2.get(0).getRateUsing();
				    		}				    		
				       }			       
				  }
					
					StringBuffer rateQuery = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
							+ entity.getOrigin().getId() + "' and obj.landfill='"
							+ entity.getDestination().getId() 
							+ "' and obj.company='"+entity.getDriverCompany().getId()
							+ "' and obj.terminal='"+entity.getTerminal().getId()+"'");
					if(payUsing==1){
							rateQuery.append(" and obj.validFrom <='"+dateFormat.format(entity.getLoadDate())
							+"' and obj.validTo >='"+dateFormat.format(entity.getLoadDate())+"'");
					}
					else{
							rateQuery.append(" and obj.validFrom <='"+dateFormat.format(entity.getUnloadDate())
							+"' and obj.validTo >='"+dateFormat.format(entity.getUnloadDate())+"'");
					}
					
					List<DriverPayRate>	fs = new ArrayList<DriverPayRate>();
					
					System.out.println("***** RTE qUERY IS "+rateQuery.toString());
					
					List<DriverPayRate>	fs1 = genericDAO.executeSimpleQuery(rateQuery.toString());
					 
					if (fs1 != null && fs1.size() > 0) {
				    	fs = fs1;
				    }
				    else{
				    	
				    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
							StringBuffer rateQuery1 = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
								+ entity.getOrigin().getId() + "' and obj.landfill='"
								+ 91 
								+ "' and obj.company='"+entity.getDriverCompany().getId()
								+ "' and obj.terminal='"+entity.getTerminal().getId()+"'");
							if(payUsing==1){	
								rateQuery1.append(" and obj.validFrom <='"+dateFormat.format(entity.getLoadDate())
								+"' and obj.validTo >='"+dateFormat.format(entity.getLoadDate())+"'");
							}
							else{
								rateQuery1.append(" and obj.validFrom <='"+dateFormat.format(entity.getUnloadDate())
								+"' and obj.validTo >='"+dateFormat.format(entity.getUnloadDate())+"'");
							}
							
							List<DriverPayRate>	fs2 = genericDAO.executeSimpleQuery(rateQuery1.toString());
							if (fs2 != null && fs2.size() > 0) {
								fs = fs2;
							}
				    	}
				    	
				    }
					Double payrate = 0.0;
					
					if(fs!=null && fs.size()>0){		
						
						boolean wbDrivers = false;
						
						if(entity.getDriverCompany().getId()==4l && entity.getTerminal().getId()==93l){
							if(driver.getDateProbationEnd()!=null){
								if(new LocalDate(driverObj.getDateProbationEnd()).isAfter(new LocalDate(entity.getBillBatch())) || new LocalDate(driverObj.getDateProbationEnd()).isEqual(new LocalDate(entity.getBillBatch()))){
									wbDrivers = true;
								}
							}
						}
						if(wbDrivers){
							payrate = fs.get(0).getProbationRate() ;						
						}
						else{
							
								if(driverObj.getShift().equals("1")){								
									payrate = fs.get(0).getPayRate();
								}
								else{									
									payrate = fs.get(0).getNightPayRate();
								}
							
							
						}					

				 LocalDate unloadDate = new LocalDate(entity.getUnloadDate());	
				 if(unloadDate.getDayOfWeek() == DateTimeConstants.SUNDAY){
					 payrate = payrate * fs.get(0).getSundayRateFactor();
				 }
				  	
				  	
				}
				  	
				 	
				  	tripsheet.setPayRate(payrate);
					
					//// New Code End Here
					
					
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
				tripsheet.setEnteredBy("system");
				tripsheet.setVerificationStatus("Verified");
			/////// Newly Added
				Location location = genericDAO.getById(Location.class, entity
						.getDestination().getId());
				
				Driver driverObj = genericDAO.getById(Driver.class,entity.getDriver().getId());
				
				int payUsing = 0;
				
				String initRateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
					+ entity.getOrigin().getId() + "' and obj.landfill='"						
					+ entity.getDestination().getId() 
					+ "' and obj.company='"+entity.getDriverCompany().getId()
					+ "' and obj.terminal='"+entity.getTerminal().getId()
					+ "' order by obj.validTo";
				List<DriverPayRate>	initfs = genericDAO.executeSimpleQuery(initRateQuery);
			    if (initfs != null && initfs.size() > 0) {
			    	payUsing = initfs.get(0).getRateUsing();
			    }
			    else{
			    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
			    		String initRateQuery1 = "select obj from DriverPayRate obj where obj.transferStation='"
							+ entity.getOrigin().getId() + "' and obj.landfill='"						
							+ 91 
							+ "' and obj.company='"+entity.getDriverCompany().getId()
							+ "' and obj.terminal='"+entity.getTerminal().getId()
							+ "' order by obj.validTo";
			    		List<DriverPayRate>	initfs2 = genericDAO.executeSimpleQuery(initRateQuery1);
			    		if (initfs2 != null && initfs2.size() > 0) {
			    			payUsing = initfs2.get(0).getRateUsing();
			    		}				    		
			       }			       
			  }
				
				StringBuffer rateQuery = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
						+ entity.getOrigin().getId() + "' and obj.landfill='"
						+ entity.getDestination().getId() 
						+ "' and obj.company='"+entity.getDriverCompany().getId()
						+ "' and obj.terminal='"+entity.getTerminal().getId()+"'");
				if(payUsing==1){
						rateQuery.append(" and obj.validFrom <='"+dateFormat.format(entity.getLoadDate())
						+"' and obj.validTo >='"+dateFormat.format(entity.getLoadDate())+"'");
				}
				else{
						rateQuery.append(" and obj.validFrom <='"+dateFormat.format(entity.getUnloadDate())
						+"' and obj.validTo >='"+dateFormat.format(entity.getUnloadDate())+"'");
				}
				
				List<DriverPayRate>	fs = new ArrayList<DriverPayRate>();
				
				System.out.println("***** RTE qUERY IS "+rateQuery.toString());
				
				List<DriverPayRate>	fs1 = genericDAO.executeSimpleQuery(rateQuery.toString());
				 
				if (fs1 != null && fs1.size() > 0) {
			    	fs = fs1;
			    }
			    else{
			    	
			    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
						StringBuffer rateQuery1 = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
							+ entity.getOrigin().getId() + "' and obj.landfill='"
							+ 91 
							+ "' and obj.company='"+entity.getDriverCompany().getId()
							+ "' and obj.terminal='"+entity.getTerminal().getId()+"'");
						if(payUsing==1){	
							rateQuery1.append(" and obj.validFrom <='"+dateFormat.format(entity.getLoadDate())
							+"' and obj.validTo >='"+dateFormat.format(entity.getLoadDate())+"'");
						}
						else{
							rateQuery1.append(" and obj.validFrom <='"+dateFormat.format(entity.getUnloadDate())
							+"' and obj.validTo >='"+dateFormat.format(entity.getUnloadDate())+"'");
						}
						
						List<DriverPayRate>	fs2 = genericDAO.executeSimpleQuery(rateQuery1.toString());
						if (fs2 != null && fs2.size() > 0) {
							fs = fs2;
						}
			    	}
			    	
			    }
				Double payrate = 0.0;
				
				if(fs!=null && fs.size()>0){		
					
					boolean wbDrivers = false;
					
					if(entity.getDriverCompany().getId()==4l && entity.getTerminal().getId()==93l){
						if(driver.getDateProbationEnd()!=null){
							if(new LocalDate(driverObj.getDateProbationEnd()).isAfter(new LocalDate(entity.getBillBatch())) || new LocalDate(driverObj.getDateProbationEnd()).isEqual(new LocalDate(entity.getBillBatch()))){
								wbDrivers = true;
							}
						}
					}
					if(wbDrivers){
						payrate = fs.get(0).getProbationRate() ;						
					}
					else{
						
							if(driverObj.getShift().equals("1")){								
								payrate = fs.get(0).getPayRate();
							}
							else{									
								payrate = fs.get(0).getNightPayRate();
							}
						
						
					}					

			 LocalDate unloadDate = new LocalDate(entity.getUnloadDate());	
			 if(unloadDate.getDayOfWeek() == DateTimeConstants.SUNDAY){
				 payrate = payrate * fs.get(0).getSundayRateFactor();
			 }
			  	
			  	
			}
			  	
			 	
			  	tripsheet.setPayRate(payrate);
				
				//// New Code End Here
				genericDAO.saveOrUpdate(tripsheet);			
			}
			
			if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
				
				return "redirect:list.do";
			}
			else{
				
				return "redirect:create.do";
			}
			
			
			//return "redirect:create.do";
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
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (getUser(request).getId() != 20) {
						bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);
					}
				}
				String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
				if (tickets1!=null && tickets1.size()>0){	
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (getUser(request).getId() != 20) {
						bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);	
					}
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
			if(entity.getTicketStatus()==null){
				entity.setTicketStatus(0);
			}
			else{
				if(entity.getTicketStatus()!=2){					
					entity.setTicketStatus(0);
				}
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
			
			if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
				
				return "redirect:list.do";
			}
			else{
				
				return "redirect:create.do";
			}
		}
	}
	
	
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		String ticketID = request.getParameter("id");
		
		Ticket ticket = genericDAO.getById(Ticket.class,Long.parseLong(ticketID));
		
		
		if(ticket.getTicketStatus()==2 && ticket.getVoucherStatus()==2) {
			setupUpdate(model, request);
			return urlContext + "/form2";
		}
		else if(ticket.getTicketStatus()==2 && ticket.getVoucherStatus()==1){
			setupUpdate(model, request);
			return urlContext + "/form1";
		}
		else{
			setupUpdate(model, request);
			return urlContext + "/form";
		}
	}
	
	
	
	
	@RequestMapping("/bulkedit.do")
	public String bulkEdit(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids){
		request.getSession().setAttribute("bulkeditids", ids);		
		String[] ticketIDs = (String[])request.getSession().getAttribute("bulkeditids");
		
		String commaSperatedID = "";
		for(String tickid : ticketIDs){			
			if(commaSperatedID.equals("")){
				commaSperatedID=tickid;
			}
			else{
				commaSperatedID=commaSperatedID+","+tickid;
			}
		}
		
		String ticketQuery = "select obj from Ticket obj where obj.ticketStatus=2 and obj.id in ("+commaSperatedID+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		
		if(tickets.size() > 0 && !tickets.isEmpty()) {
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error",
			"Some tickets are invoiced. Select only Available tickets. Contact Admin for Invoiced ticket update.");
			return "redirect:list.do";
		}
		else{
			setupCreate(model, request);
			Ticket ticket = getEntityInstance();
			model.addAttribute("modelObject", ticket);		
			return urlContext + "/massUpdateForm";
		}
	}
	
	
	
	@RequestMapping("/prPendingYes.do")
	public String prPendingYes(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids){		
		String[] ticketIDs = ids;		
		String commaSperatedID = "";
		for(String tickid : ticketIDs){			
			if(commaSperatedID.equals("")){
				commaSperatedID=tickid;
			}
			else{
				commaSperatedID=commaSperatedID+","+tickid;
			}
		}
		
		/*String ticketQuery = "select obj from Ticket obj where obj.ticketStatus=2 and obj.id in ("+commaSperatedID+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		
		if(tickets.size() > 0 && !tickets.isEmpty()) {			
			request.getSession().setAttribute("error",
			"Some tickets are invoiced. Select only Available tickets. Contact Admin for Invoice ticket update.");
			return "redirect:list.do";
		}
		else{*/	
			StringBuffer ticketupdatequery = new StringBuffer("update Ticket set payRollStatus=0 where payRollStatus!=2 and payRollBatch is null and id in ("+commaSperatedID+")");
			genericDAO.executeSimpleUpdateQuery(ticketupdatequery.toString());
			return "redirect:list.do";
		//}
	}
	
	
	@RequestMapping("/prPendingNo.do")
	public String prPendingNo(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids){		
		String[] ticketIDs = ids;		
		String commaSperatedID = "";
		for(String tickid : ticketIDs){			
			if(commaSperatedID.equals("")){
				commaSperatedID=tickid;
			}
			else{
				commaSperatedID=commaSperatedID+","+tickid;
			}
		}
		
		/*String ticketQuery = "select obj from Ticket obj where obj.ticketStatus=2 and obj.id in ("+commaSperatedID+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		
		if(tickets.size() > 0 && !tickets.isEmpty()) {			
			request.getSession().setAttribute("error",
			"Some tickets are invoiced. Select only Available tickets. Contact Admin for Invoice ticket update.");
			return "redirect:list.do";
		}
		else{	*/
		StringBuffer ticketupdatequery = new StringBuffer("update Ticket set payRollStatus=1 where payRollStatus!=2 and payRollBatch is null and id in ("+commaSperatedID+")");
		genericDAO.executeSimpleUpdateQuery(ticketupdatequery.toString());
		return "redirect:list.do";
		//}
	}
	
	
	
	
	
	@RequestMapping("/holdAllTickets.do")
	public String holdAllTickets(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids){		
		String[] ticketIDs = ids;		
		String commaSperatedID = "";
		for(String tickid : ticketIDs){			
			if(commaSperatedID.equals("")){
				commaSperatedID=tickid;
			}
			else{
				commaSperatedID=commaSperatedID+","+tickid;
			}
		}
		
		String ticketQuery = "select obj from Ticket obj where obj.ticketStatus=2 and obj.id in ("+commaSperatedID+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		
		if(tickets.size() > 0 && !tickets.isEmpty()) {			
			request.getSession().setAttribute("error",
			"Some tickets are invoiced. Select only Available tickets. Contact Admin for Invoice ticket update.");
			return "redirect:list.do";
		}
		else{	
			StringBuffer ticketupdatequery = new StringBuffer("update Ticket set ticketStatus=0 where id in ("+commaSperatedID+")");
			genericDAO.executeSimpleUpdateQuery(ticketupdatequery.toString());
			return "redirect:list.do";
		}
	}
	
	@RequestMapping("/unholdAllTickets.do")
	public String unholdAllTickets(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids){
		String[] ticketIDs = ids;		
		String commaSperatedID = "";
		for(String tickid : ticketIDs){			
			if(commaSperatedID.equals("")){
				commaSperatedID=tickid;
			}
			else{
				commaSperatedID=commaSperatedID+","+tickid;
			}
		}
		
		String ticketQuery = "select obj from Ticket obj where obj.ticketStatus=2 and obj.id in ("+commaSperatedID+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		
		if(tickets.size() > 0 && !tickets.isEmpty()) {			
			request.getSession().setAttribute("error",
			"Some tickets are invoiced. Select only Available tickets. Contact Admin for Invoice ticket update.");
			return "redirect:list.do";
		}
		else{		
			StringBuffer ticketupdatequery = new StringBuffer("update Ticket set ticketStatus=1 where id in ("+commaSperatedID+")");
			genericDAO.executeSimpleUpdateQuery(ticketupdatequery.toString());
			return "redirect:list.do";
		}
	}
	
	@RequestMapping("/cancelbulkedit.do")
	public String cancelBulkEdit(ModelMap model, HttpServletRequest request){
		request.getSession().removeAttribute("bulkeditids");				
		return "redirect:list.do";
	}
	
	
	
	@RequestMapping(value="/updatebulkeditdata.do", method=RequestMethod.POST)
	public String updateBulklyEditedTikets(HttpServletRequest request,
			@ModelAttribute("modelObject") Ticket entity,
			BindingResult bindingResult, ModelMap model) {
		
		StringBuffer ticketupdatequery = new StringBuffer("update Ticket set ");
		boolean doNotAddComma = true;
		
		if(entity.getSubcontractor() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			SubContractor subcontractor = genericDAO.getById(SubContractor.class, entity.getSubcontractor().getId());
			if(subcontractor.getName().equalsIgnoreCase("blank"))
				ticketupdatequery.append("subcontractor = null");
			else	
				ticketupdatequery.append("subcontractor=").append(entity.getSubcontractor().getId());			
		}
		
		if(entity.getBillBatch()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("billBatch='").append(ReportDateUtil.oracleFormatter.format(entity.getBillBatch())).append("'");
		}
		
		
		
		if(entity.getLoadDate() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("loadDate='").append(ReportDateUtil.oracleFormatter.format(entity.getLoadDate())).append("'");
		}
		
		
		if(entity.getCreatedBy() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			User userObj = genericDAO.getById(User.class,entity.getCreatedBy());
			if(userObj !=null)
				ticketupdatequery.append("enteredBy='").append(userObj.getName()).append("'");
		}
		
		
		if(entity.getUnloadDate() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("unloadDate='").append(ReportDateUtil.oracleFormatter.format(entity.getUnloadDate())).append("'");
		}
		
		if(!StringUtils.isEmpty(entity.getTransferTimeIn())){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("transferTimeIn='").append(entity.getTransferTimeIn()).append("'");
		}
		
		if(!StringUtils.isEmpty(entity.getTransferTimeOut())){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("transferTimeOut='").append(entity.getTransferTimeOut()).append("'");
		}
		
		if(entity.getTransferGross() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("transferGross=").append(entity.getTransferGross());
		}
		
		if(entity.getTransferTare() != null ){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("transferTare=").append(entity.getTransferTare());
		}
		
		if(entity.getTransferNet() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("transferNet=").append(entity.getTransferNet());
		}
		
		if(entity.getTransferTons()!= null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("transferTons=").append(entity.getTransferTons());
		}
		
		if(entity.getDestination()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("destination=").append(entity.getDestination().getId());
		}
		
		if(!StringUtils.isEmpty(entity.getLandfillTimeIn())){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillTimeIn='").append(entity.getLandfillTimeIn()).append("'");
		}
		
		if(!StringUtils.isEmpty(entity.getLandfillTimeOut())){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillTimeOut='").append(entity.getLandfillTimeOut()).append("'");
		}
		
		
		if(entity.getLandfillGross() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillGross=").append(entity.getLandfillGross());
		}
		
		if(entity.getLandfillTare() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillTare=").append(entity.getLandfillTare());
		}
		
		if(entity.getLandfillNet() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillNet=").append(entity.getLandfillNet());
		}
		
		if(entity.getLandfillTons() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillTons=").append(entity.getLandfillTons());
		}
		
		if (entity.getOrigin() != null) {
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("origin=").append(entity.getOrigin().getId());
		}
		
		if(entity.getLandfillTare() != null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("landfillTare=").append(entity.getLandfillTare());
		}
		
		if (entity.getDriver() != null) {
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append(" driver=").append(entity.getDriver().getId());
		}
		
		
		/*if (entity.getVehicle() != null) {
			
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
		}	*/	
		
		
		
		//FOR CUSTOMER AND COMPANY_LOCATION
		String  query;
		
		if(entity.getDestination()!=null && entity.getOrigin()!=null){
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
		// Change to 8.35 - 28th Dec 2016
    		  entity.setGallons(entity.getTransferNet()/8.35);
    	  // }
		}
		}
		
		
		
        if(entity.getDriver()!=null) {
        	Driver driver = genericDAO.getById(Driver.class,Long.valueOf(entity.getDriver().getId()));
        	entity.setTerminal(driver.getTerminal());			
        }
		
		if(entity.getTerminal()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("terminal=").append(entity.getTerminal().getId());
		}
		
		
		if(entity.getDriverCompany()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("driverCompany=").append(entity.getDriverCompany().getId());
		}
		
		
		
		if(entity.getCustomer()!=null){		
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("customer=").append(entity.getCustomer().getId());
		}
		
		if(entity.getCompanyLocation()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("companyLocation=").append(entity.getCompanyLocation().getId());
		}
		
		if(entity.getTicketStatus()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("ticketStatus=").append(entity.getTicketStatus());
		}
		
		if(entity.getTrailer()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("trailer=").append(entity.getTrailer().getId());
		}
		
		if(entity.getVehicle()!=null){
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("vehicle=").append(entity.getVehicle().getId());
		}
		
		String[] ticketIDs = (String[])request.getSession().getAttribute("bulkeditids");
		
		String commaSperatedID = "";
		for(String tickid : ticketIDs){			
			if(commaSperatedID.equals("")){
				commaSperatedID=tickid;
			}
			else{
				commaSperatedID=commaSperatedID+","+tickid;
			}
		}
		
		
		ticketupdatequery.append(" where id in (").append(commaSperatedID).append(")");
		
		if(!doNotAddComma)
			genericDAO.executeSimpleUpdateQuery(ticketupdatequery.toString());
		cleanUp(request);
		// return to list
		request.getSession().removeAttribute("bulkeditids");
		setupCreate(model, request);
		request.getSession().setAttribute("msg",
				"Tickets Updated Successfully");
		
			return "redirect:list.do";		
	}
	
	
	
	
	
	
	@Override	
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		if(criteria.getSearchMap().get("contnd")!=null){
			criteria.getSearchMap().remove("contnd");
		}
		
		// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
		criteria.getSearchMap().remove("showDuplicateTicketOverrideMsg");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
		
		if(!StringUtils.isEmpty(request.getParameter("contnd"))){
			
			if(request.getParameter("contnd").equalsIgnoreCase("yes"))
			 request.getSession().setAttribute("error","Process Incomplete!. Unable to Delete Invoiced Tickets.");
		}	
		
		return urlContext + "/list";
	}
	
	

	@RequestMapping("/changestatus.do")
	public String change(HttpServletRequest request, ModelMap modMap) {
		Ticket tkt = genericDAO.getById(Ticket.class,
				Long.valueOf(request.getParameter("id")));
		if (tkt.getTicketStatus() == 1) {
			tkt.setTicketStatus(0);
			genericDAO.save(tkt);
		} else if(tkt.getTicketStatus() == 0) {
			tkt.setTicketStatus(1);
			genericDAO.save(tkt);
		}
		
		return "redirect:list.do";
	}
	
	@Override
	public String bulkdelete(@RequestParam("id") String[] id) {
		boolean error=false;
		if (id != null) {			
			for (int i = 0; i < id.length; i++) {
				Ticket ticket=genericDAO.getById(Ticket.class, Long.parseLong(id[i]));
				if(ticket.getTicketStatus()!=2){
				try {
					genericDAO.deleteById(getEntityClass(),
							Long.parseLong(id[i]));
				} catch (Exception ex) {
					ex.printStackTrace();
					log.warn("Error deleting record " + id, ex);
				}
				}
				else
				{
					error=true;
				}				
				
			}
		}
		
		if(error){
			return "redirect:/" + urlContext + "/list.do?contnd=yes";
		}
		else{	
		    return "redirect:/" + urlContext + "/list.do";
		}
	}

	@Override
	public String delete(@ModelAttribute("modelObject") Ticket entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
		
			if(entity.getTicketStatus()!=2){
				String tripsheetQuery = "select obj from TripSheet obj  where obj.origin in ("+entity.getOrigin().getId()+") and obj.originTicket in ("+entity.getOriginTicket()+") and obj.destination in ("+entity.getDestination().getId()+") and obj.destinationTicket in ("+entity.getDestinationTicket()+")";
				
				List<TripSheet> tripsheets  = genericDAO.executeSimpleQuery(tripsheetQuery);
				
				if(tripsheets!=null && tripsheets.size()>0){
					for(TripSheet tripsheet:tripsheets){
						tripsheet.setVerificationStatus(null);
						genericDAO.saveOrUpdate(tripsheet);
					}
				}
				genericDAO.delete(entity);
			}
			else
			{
				 request.getSession().setAttribute("error","Process Failed!. Cannot Delete Invoiced Tickets.");
			}
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
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

	/*public String checkforduplicacy(ModelMap model, HttpServletRequest request,
			Ticket entity) {
		if (!entity.getOriginTicket().equals("")
				&& !entity.getDestinationTicket().equals("")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("originTicket", entity.getOriginTicket());
			map.put("destinationTicket", entity.getDestinationTicket());
			if (!(genericDAO.isUnique(Ticket.class, entity, map))) {
				request.getSession().setAttribute("errors",
						"Cannot create Duplicate entry ");
				setupCreate(model, request);
				return urlContext + "/form";
			}
		}
		return "";
	}*/
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteAll.do")
	public String deleteAll(HttpServletRequest request) {
		try {
			List<Ticket> tickets = genericDAO.findAll(Ticket.class);
			for(Ticket ticket:tickets) {
				genericDAO.delete(ticket);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

	@ModelAttribute("modelObject")
	public Ticket setupModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
			Ticket ticket = getEntityInstance();
			ticket.setBillBatch(new Date());
			ticket.setDriver((Driver)request.getSession().getAttribute("driver"));
			ticket.setDriverCompany((Location)request.getSession().getAttribute("driverCompany"));
			ticket.setSubcontractor((SubContractor)request.getSession().getAttribute("subcontractor"));
			ticket.setTerminal((Location)request.getSession().getAttribute("terminal"));
			ticket.setVehicle((Vehicle)request.getSession().getAttribute("truck"));
			ticket.setTrailer((Vehicle)request.getSession().getAttribute("trailer"));
			ticket.setCreatedBy((Long)request.getSession().getAttribute("createdBy"));
			ticket.setBillBatch((Date)request.getSession().getAttribute("batchDate"));
			return ticket;
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}
	
}
