package com.primovision.lutransport.controller.operator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.core.util.TicketUtils;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.ChangedTicket;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Terminal;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.WMTicket;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hrreport.ChangedDriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.UpdatedDriverPay;
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
		/*String driverdIds = "";
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
		
		checkAndRemoveSubContractorCriteria(criteria);
		
		dateupdateService.updateDate(request, "billBatchDate", "billBatch");
		dateupdateService.updateDate(request, "lDate","loadDate");
		dateupdateService.updateDate(request, "uDate","unloadDate");
		dateupdateService.updateDate(request, "pDate","payRollBatch");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria));*/
		
		List<Ticket> ticketList = performSearch(criteria, request);
		model.addAttribute("list", ticketList);
		return urlContext + "/list";
	}
	
	private List<Ticket> performSearch(SearchCriteria criteria, HttpServletRequest request) {
		String driverdIds = "";
		String driverId = (String) criteria.getSearchMap().get("driver.id");		
		if (!StringUtils.isEmpty(driverId)) {			
			Map criterias = new HashMap();
			criterias.put("fullName", driverId);
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
		
		checkAndRemoveSubContractorCriteria(criteria);
		
		dateupdateService.updateDate(request, "billBatchDate", "billBatch");
		dateupdateService.updateDate(request, "lDate","loadDate");
		dateupdateService.updateDate(request, "uDate","unloadDate");
		dateupdateService.updateDate(request, "pDate","payRollBatch");
		
		return genericDAO.search(getEntityClass(), criteria);
	}

	private List<Ticket> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(15000);
		
		List<Ticket> ticketList = performSearch(criteria, request);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return ticketList;
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
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName asc, id desc", false));
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
		Long userId = getUser(request).getId() ;
		if (userId == 20 || // Maria
				userId == 295) { // Ela Tylka
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
		
		model.addAttribute("subcontractors", genericDAO.executeSimpleQuery("Select obj from SubContractor obj order by obj.name "));
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
			
			// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
			Long userId = getUser(request).getId();
			if(entity.getOrigin() != null && entity.getDestination() != null && entity.getOriginTicket() !=null && entity.getDestinationTicket() !=null){
				/*String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.destination.id="+entity.getDestination().getId()+" and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;*/
				String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
				if (tickets!=null && tickets.size()>0){	
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (userId != 20 && // Maria
							userId != 295) { // Ela Tylka
						bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);
					}
				}
				/*String destination = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;*/
				String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
				if (tickets1!=null && tickets1.size()>0){	
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (userId != 20 && // Maria
							userId != 295) { // Ela Tylka
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
			
			entity.setAutoCreated(Ticket.AUTO_CREATED_NO);
			entity.setPaperVerifiedStatus(Ticket.PAPER_VERIFIED_STATUS_YES);
			
			// WB LU subcontractor change - 3rd June 2019
			TicketUtils.populateInternalSubcontractor(entity, genericDAO);
			
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
			
			// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
			Long userId = getUser(request).getId();
			if(entity.getOrigin() != null && entity.getDestination() != null && entity.getOriginTicket() !=null && entity.getDestinationTicket() !=null){
				String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
				
				if (tickets!=null && tickets.size()>0){	
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (userId != 20 && // Maria
							userId != 295) { // Ela Tylka
						bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);
					}
				}
				String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
				List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
				if (tickets1!=null && tickets1.size()>0){	
					// Allow duplicate tickets for Maria Navarro - 17th Oct 2016
					if (userId != 20 && // Maria
							userId != 295) { // Ela Tylka
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
			
			entity.setAutoCreated(Ticket.AUTO_CREATED_NO);
			entity.setPaperVerifiedStatus(Ticket.PAPER_VERIFIED_STATUS_YES);
			
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
	public String bulkEdit(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids) {
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
		
		String mode = request.getParameter("mode");
		if (StringUtils.equals(Ticket.REVERT_MODE, mode)) {
			String revertMsg = validateCanBeReverted(commaSperatedID, request);
			if (StringUtils.contains(revertMsg, "success")) {
				//request.getSession().setAttribute("msg", "Tickets can be Reverted - validation check successful");
			} else {
				request.getSession().removeAttribute("bulkeditids");
				request.getSession().setAttribute("error", revertMsg);
				return "redirect:list.do";
			}
		}
		
		String ticketQuery = "select obj from Ticket obj where obj.ticketStatus=2 and obj.id in ("+commaSperatedID+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		
		if(tickets.size() > 0 && !tickets.isEmpty() && !StringUtils.equals(Ticket.REVERT_MODE, mode)) {
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error",
			"Some tickets are invoiced. Select only Available tickets. Contact Admin for Invoiced ticket update.");
			return "redirect:list.do";
		}
		else{
			setupCreate(model, request);
			
			Ticket ticket = getEntityInstance();
			if (StringUtils.equals(Ticket.REVERT_MODE, mode)) {
				ticket.setMode(Ticket.REVERT_MODE);
			}
			
			model.addAttribute("modelObject", ticket);		
			return urlContext + "/massUpdateForm";
		}
	}
	
	private String validateCanBeReverted(String ticketIds, HttpServletRequest request) {
		String ticketQuery = "select obj from Ticket obj where obj.id in ("+ticketIds+")";
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		if (tickets == null || tickets.isEmpty()) {
			return "No Ticktes found for specified criteria";
		}
		
		for (Ticket aTicket : tickets) {
			if (aTicket.getPayRollBatch() == null) {
				return "Unable to revert - some Tickets are not in paid status";
			}
		}
		
		return "Tickets can be Reverted - validation check successful";
	} 
	
	private String processRevert(String commaSperatedTicketIds, Long userId, Ticket entity) {
		if (entity.getDriver() == null && entity.getOrigin() == null && entity.getDestination() == null) {
			return "Change either Driver or Origin/Destination";
		}
		if (entity.getDriver() != null && (entity.getOrigin() != null || entity.getDestination() != null)) {
			return "Change either Driver or Origin/Destination (not both)";
		}
		
		String ticketQuery = "select obj from Ticket obj where obj.id in ("+commaSperatedTicketIds+")";
		List<Ticket> ticketsList = genericDAO.executeSimpleQuery(ticketQuery);
		List<ChangedTicket> changedTicketsList = createChangedTickets(ticketsList, userId, entity);
		
		String result = createUpdatedDriverPay(changedTicketsList, userId);
		if (!StringUtils.contains(result, "Success")) {
			return result;
		}
		
		return "Successfully updated changed info";
	}
	
	private List<ChangedTicket> createChangedTickets(List<Ticket> ticketList, Long userId, Ticket entity) {
		List<ChangedTicket> changedTicketsList = new ArrayList<ChangedTicket>();
		for (Ticket aTicket : ticketList) {
			ChangedTicket changedTicket = new ChangedTicket();
			changedTicket.setCreatedAt(Calendar.getInstance().getTime());
			changedTicket.setCreatedBy(userId);

			User user = genericDAO.getById(User.class, userId);
			changedTicket.setEnteredBy(user.getName());
			
			map(changedTicket, aTicket);
			
			changedTicket.setNewDriver(entity.getDriver());
			changedTicket.setNewOrigin(entity.getOrigin());
			changedTicket.setNewDestination(entity.getDestination());
			changedTicket.setNewNotes(entity.getNotes());
			changedTicket.setNewBillBatch(entity.getBillBatch());
			
			if (changedTicket.getNewOrigin() != null || changedTicket.getNewDestination() != null) {
				Double newPayRate = deduceNewDriverPayRate(changedTicket);
				aTicket.setDriverPayRate(newPayRate);
				genericDAO.saveOrUpdate(aTicket);
				
				changedTicket.setNewDriverPayRate(newPayRate);
			}
			
			genericDAO.saveOrUpdate(changedTicket);
			changedTicketsList.add(changedTicket);
		}
		
		return changedTicketsList;
	}
	
	private Double deduceNewDriverPayRate(ChangedTicket aChangedTicket) {
		Double newPayRate = 0.0;
		List<DriverPayRate> oldRateList = retrieveDriverPayRate(aChangedTicket, 
				aChangedTicket.getOrigin().getId(),
				aChangedTicket.getDestination().getId());
		List<DriverPayRate> newRateList = retrieveDriverPayRate(aChangedTicket, 
				(aChangedTicket.getNewOrigin() != null ? aChangedTicket.getNewOrigin().getId()
						: aChangedTicket.getOrigin().getId()),
				(aChangedTicket.getNewDestination() != null ? aChangedTicket.getNewDestination().getId()
						: aChangedTicket.getDestination().getId()));
		if (oldRateList == null || oldRateList.isEmpty() || newRateList == null || newRateList.isEmpty()) {
			return newPayRate;
		}
		DriverPayRate oldRateObj = oldRateList.get(0);
		DriverPayRate newRateObj = newRateList.get(0);
		
		double oldTicketRate = aChangedTicket.getDriverPayRate().doubleValue();
		if (oldRateObj.getPayRate() != null && oldTicketRate == oldRateObj.getPayRate()) {
			newPayRate = newRateObj.getPayRate();
		} else if (oldRateObj.getNightPayRate() != null && oldTicketRate == oldRateObj.getNightPayRate()) {
			newPayRate = newRateObj.getNightPayRate();
		} else if (oldRateObj.getSundayRateFactor() != null && oldTicketRate == (oldRateObj.getPayRate() * oldRateObj.getSundayRateFactor())) {
			newPayRate = (newRateObj.getPayRate() * newRateObj.getSundayRateFactor());
		} else if (oldRateObj.getProbationRate() != null && oldTicketRate == oldRateObj.getProbationRate()
				&& newRateObj.getProbationRate() != null) {
			newPayRate = newRateObj.getProbationRate();
		}
		return newPayRate;
	}
	
	private List<DriverPayRate> retrieveDriverPayRate(ChangedTicket aChangedTicket, Long origin, Long destination) {
		String query = "Select obj from DriverPayRate obj where 1=1 "
				+ " and obj.catagory=2"
				+ " and obj.company="+aChangedTicket.getDriverCompany().getId()
				+ " and obj.terminal="+aChangedTicket.getTerminal().getId()
				+ " and obj.transferStation="+origin
				+ " and obj.landfill="+destination
				+ " and obj.validFrom <= '" + aChangedTicket.getUnloadDate() + "'"
				+ " and obj.validTo >= '" + aChangedTicket.getUnloadDate() + "'";
		List<DriverPayRate> rateList = genericDAO.executeSimpleQuery(query);
		return rateList;
	}
	
	private String createUpdatedDriverPay(List<ChangedTicket> changedTicketList, Long userId) {
		Map<Long, List<ChangedTicket>> groupedTicketMap = groupTicketsByDriver(changedTicketList);
		Set<Long> keys = groupedTicketMap.keySet();
		for (Long aKey : keys) {
			UpdatedDriverPay updatedDriverPay = new UpdatedDriverPay();
			updatedDriverPay.setCreatedBy(userId);
			updatedDriverPay.setCreatedAt(Calendar.getInstance().getTime());
			updatedDriverPay.setUpdatedStatus(UpdatedDriverPay.UPDATED_STATUS_IN_PROCESS);
			
			List<ChangedTicket> groupedTicketList = groupedTicketMap.get(aKey);
			ChangedTicket aChangedTicket = groupedTicketList.get(0);
			boolean revertPay = true;
			if (aChangedTicket.getNewOrigin() != null || aChangedTicket.getNewDestination() != null) {
				revertPay = false;
			} 
			
			Double amount = 0.0;
			for (ChangedTicket aLoopTicket : groupedTicketList) {
				Double loopAmount = 0.0;
				if (revertPay) {
					loopAmount = -(aLoopTicket.getDriverPayRate());
				} else {
					loopAmount = (aLoopTicket.getNewDriverPayRate() - aLoopTicket.getDriverPayRate());
				}
				
				amount += (loopAmount);
			}
			updatedDriverPay.setAmount(amount);
			updatedDriverPay.setNoOfLoad(groupedTicketList.size());
			
			map(updatedDriverPay, aChangedTicket);
			genericDAO.saveOrUpdate(updatedDriverPay);
		}
		
		return "Successfully saved UpdatedDriverPay";
	}
	
	private void map(UpdatedDriverPay updatedDriverPay, ChangedTicket aTicket) {
		updatedDriverPay.setDriverName(aTicket.getDriver().getFullName());
		updatedDriverPay.setCompany(aTicket.getDriverCompany());
		updatedDriverPay.setTerminal(aTicket.getTerminal());
		updatedDriverPay.setBillBatchDateFrom(aTicket.getBillBatch());
		updatedDriverPay.setBillBatchDateTo(aTicket.getBillBatch());
		updatedDriverPay.setNotes(aTicket.getNewNotes());
	}
	
	private Map<Long, List<ChangedTicket>> groupTicketsByDriver(List<ChangedTicket> changedTicketList) {
		Map<Long, List<ChangedTicket>> groupedTicketMap = new HashMap<Long, List<ChangedTicket>>();
		for (ChangedTicket aChangedTicket : changedTicketList) {
			Long driver = aChangedTicket.getDriver().getId();
			List<ChangedTicket> groupedTicketList = groupedTicketMap.get(driver);
			if (groupedTicketList == null) {
				groupedTicketList = new ArrayList<ChangedTicket>();
				groupedTicketMap.put(driver, groupedTicketList);
			}
			groupedTicketList.add(aChangedTicket);
		}
		return groupedTicketMap;
	}
	
	private String createChangedDriverPayFreezed(List<Ticket> ticketList, Long userId) {
		for (Ticket aTicket : ticketList) {
			String freezeQuery = "Select obj from DriverPayFreezWrapper obj where "
					+ " obj.origin = '" + aTicket.getOrigin().getName() + "'"
					+ " and obj.destination = '" + aTicket.getDestination().getName() + "'"
					+ " and obj.drivername = '" + aTicket.getDriver().getFullName() + "'"
					+ " and obj.company="+aTicket.getDriverCompany().getId()
					+ " and obj.terminal="+aTicket.getTerminal().getId()
					+ " and obj.payRollBatch = '" + aTicket.getPayRollBatch() + "'"
					+ " and obj.billBatchDateTo = '" + aTicket.getBillBatch() + "'";
			List<DriverPayFreezWrapper> freezeList = genericDAO.executeSimpleQuery(freezeQuery);
			if (freezeList == null || freezeList.size() == 0) {
				return "Driver Pay Freez Wrapper not found";
				
			}
			if (freezeList.size() > 1) {
				return "More than one Driver Pay Freez Wrapper not found";
			}
			DriverPayFreezWrapper aDriverPayFreezWrapper = freezeList.get(0);
			
			ChangedDriverPayFreezWrapper changedDriverPayFreezWrapper = new ChangedDriverPayFreezWrapper();
			map(changedDriverPayFreezWrapper, aDriverPayFreezWrapper, userId);
			genericDAO.saveOrUpdate(changedDriverPayFreezWrapper);
		}
		
		return "Successfully saved ChangedDriverPayFreezWrapper";
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
		Long userId = getUser(request).getId();
		
		boolean revertMode = (StringUtils.equals(Ticket.REVERT_MODE, entity.getMode()) ? true : false) ;
		
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
		
		if (StringUtils.isNotEmpty(entity.getNotes())) {
			if(doNotAddComma){
				doNotAddComma = false;
			}
			else{
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("notes='").append(entity.getNotes()).append("'");
		}
		
		if (entity.getPayRollStatus() != null && !revertMode){
			if (doNotAddComma) {
				doNotAddComma = false;
			} else {
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("payRollStatus=").append(entity.getPayRollStatus());
		}
		
		if (StringUtils.isNotEmpty(entity.getNotBillable())) {
			if (doNotAddComma) {
				doNotAddComma = false;
			} else {
				ticketupdatequery.append(",");
			}
			ticketupdatequery.append("notBillable='").append(entity.getNotBillable()).append("'");
		}
		
		if (revertMode) {
			if (entity.getOrigin() == null && entity.getDestination() == null) {
				if (doNotAddComma) {
					doNotAddComma = false;
				} else {
					ticketupdatequery.append(",");
				}
				ticketupdatequery.append(" payRollBatch=null,");
				ticketupdatequery.append(" payRollStatus=1,");
				ticketupdatequery.append(" driverPayRate=0.0");
			}
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
		
		String successMsg = "Tickets Updated Successfully";
		if(!doNotAddComma) {
			if (revertMode) {
				String result = processRevert(commaSperatedID, userId, entity);
				if (!StringUtils.contains(result, "Success")) {
					cleanUp(request);
					request.getSession().removeAttribute("bulkeditids");
					setupCreate(model, request);
					request.getSession().setAttribute("error", result);
					return "redirect:list.do";
				} else {
					successMsg = "Tickets Reverted Successfully";
				}
			}
			
			genericDAO.executeSimpleUpdateQuery(ticketupdatequery.toString());
		}
		
		cleanUp(request);
		// return to list
		request.getSession().removeAttribute("bulkeditids");
		setupCreate(model, request);
		request.getSession().setAttribute("msg", successMsg);
		
			return "redirect:list.do";		
	}
	
	private void map(ChangedDriverPayFreezWrapper changedDriverPayFreezWrapper, 
			DriverPayFreezWrapper driverPayFreezWrapper, Long userId) {
		changedDriverPayFreezWrapper.setAmount(driverPayFreezWrapper.getAmount());
		changedDriverPayFreezWrapper.setBaseId(driverPayFreezWrapper.getId());
		changedDriverPayFreezWrapper.setBereavementAmount(driverPayFreezWrapper.getBereavementAmount());
		changedDriverPayFreezWrapper.setBillBatchDate(driverPayFreezWrapper.getBillBatchDate());
		changedDriverPayFreezWrapper.setBillBatchDateFrom(driverPayFreezWrapper.getBillBatchDateFrom());
		changedDriverPayFreezWrapper.setBillBatchDateFromString(driverPayFreezWrapper.getBillBatchDateFromString());
		changedDriverPayFreezWrapper.setBillBatchDateTo(driverPayFreezWrapper.getBillBatchDateTo());
		changedDriverPayFreezWrapper.setBillBatchDateToString(driverPayFreezWrapper.getBillBatchDateToString());
		changedDriverPayFreezWrapper.setBonusAmount(driverPayFreezWrapper.getBonusAmount());
		changedDriverPayFreezWrapper.setBonusAmount0(driverPayFreezWrapper.getBonusAmount0());
		changedDriverPayFreezWrapper.setBonusAmount1(driverPayFreezWrapper.getBonusAmount1());
		changedDriverPayFreezWrapper.setBonusAmount2(driverPayFreezWrapper.getBonusAmount2());
		changedDriverPayFreezWrapper.setBonusAmount3(driverPayFreezWrapper.getBonusAmount3());
		changedDriverPayFreezWrapper.setBonusAmount4(driverPayFreezWrapper.getBonusAmount4());
		changedDriverPayFreezWrapper.setBonusNotes(driverPayFreezWrapper.getBonusNotes());
		changedDriverPayFreezWrapper.setBonusNotes0(driverPayFreezWrapper.getBonusNotes0());
		changedDriverPayFreezWrapper.setBonusNotes1(driverPayFreezWrapper.getBonusNotes1());
		changedDriverPayFreezWrapper.setBonusNotes2(driverPayFreezWrapper.getBonusNotes2());
		changedDriverPayFreezWrapper.setBonusNotes3(driverPayFreezWrapper.getBonusNotes3());
		changedDriverPayFreezWrapper.setBonusNotes4(driverPayFreezWrapper.getBonusNotes4());
		changedDriverPayFreezWrapper.setBonusTypeName(driverPayFreezWrapper.getBonusTypeName());
		changedDriverPayFreezWrapper.setBonusTypeName0(driverPayFreezWrapper.getBonusTypeName0());
		changedDriverPayFreezWrapper.setBonusTypeName1(driverPayFreezWrapper.getBonusTypeName1());
		changedDriverPayFreezWrapper.setBonusTypeName2(driverPayFreezWrapper.getBonusTypeName2());
		changedDriverPayFreezWrapper.setBonusTypeName3(driverPayFreezWrapper.getBonusTypeName3());
		changedDriverPayFreezWrapper.setBonusTypeName4(driverPayFreezWrapper.getBonusTypeName4());
		changedDriverPayFreezWrapper.setChangedStatus(ChangedDriverPayFreezWrapper.CHANGED_STATUS_IN_PROCESS);
		changedDriverPayFreezWrapper.setCompany(driverPayFreezWrapper.getCompany());
		changedDriverPayFreezWrapper.setCompanyname(driverPayFreezWrapper.getCompanyname());
		changedDriverPayFreezWrapper.setCreatedAt(Calendar.getInstance().getTime());
		changedDriverPayFreezWrapper.setCreatedBy(userId);
		changedDriverPayFreezWrapper.setDeductionAmount(driverPayFreezWrapper.getDeductionAmount());
		changedDriverPayFreezWrapper.setDestination(driverPayFreezWrapper.getDestination());
		changedDriverPayFreezWrapper.setDrivername(driverPayFreezWrapper.getDrivername());
		changedDriverPayFreezWrapper.setHolidayAmount(driverPayFreezWrapper.getHolidayAmount());
		changedDriverPayFreezWrapper.setHolidaydateFrom(driverPayFreezWrapper.getHolidaydateFrom());
		changedDriverPayFreezWrapper.setHolidaydateTo(driverPayFreezWrapper.getHolidaydateTo());
		changedDriverPayFreezWrapper.setHolidayname(driverPayFreezWrapper.getHolidayname());
		changedDriverPayFreezWrapper.setIsMainRow(driverPayFreezWrapper.getIsMainRow());
		changedDriverPayFreezWrapper.setMiscAmount(driverPayFreezWrapper.getMiscAmount());
		changedDriverPayFreezWrapper.setMiscamt(driverPayFreezWrapper.getMiscamt());
		changedDriverPayFreezWrapper.setMiscamt0(driverPayFreezWrapper.getMiscamt0());
		changedDriverPayFreezWrapper.setMiscamt1(driverPayFreezWrapper.getMiscamt1());
		changedDriverPayFreezWrapper.setMiscamt2(driverPayFreezWrapper.getMiscamt2());
		changedDriverPayFreezWrapper.setMiscamt3(driverPayFreezWrapper.getMiscamt3());
		changedDriverPayFreezWrapper.setMiscamt4(driverPayFreezWrapper.getMiscamt4());
		changedDriverPayFreezWrapper.setMiscamt5(driverPayFreezWrapper.getMiscamt5());
		changedDriverPayFreezWrapper.setMiscnote(driverPayFreezWrapper.getMiscnote());
		changedDriverPayFreezWrapper.setMiscnote0(driverPayFreezWrapper.getMiscnote0());
		changedDriverPayFreezWrapper.setMiscnote1(driverPayFreezWrapper.getMiscnote1());
		changedDriverPayFreezWrapper.setMiscnote2(driverPayFreezWrapper.getMiscnote2());
		changedDriverPayFreezWrapper.setMiscnote3(driverPayFreezWrapper.getMiscnote3());
		changedDriverPayFreezWrapper.setMiscnote4(driverPayFreezWrapper.getMiscnote4());
		changedDriverPayFreezWrapper.setMiscnote5(driverPayFreezWrapper.getMiscnote5());
		changedDriverPayFreezWrapper.setNoOfLoad(driverPayFreezWrapper.getNoOfLoad());
		changedDriverPayFreezWrapper.setNoOfLoadtotal(driverPayFreezWrapper.getNoOfLoadtotal());
		changedDriverPayFreezWrapper.setNumberOfSickDays(driverPayFreezWrapper.getNumberOfSickDays());
		changedDriverPayFreezWrapper.setNumberOfVactionDays(driverPayFreezWrapper.getNumberOfVactionDays());
		changedDriverPayFreezWrapper.setOrigin(driverPayFreezWrapper.getOrigin());
		changedDriverPayFreezWrapper.setPayRollBatch(driverPayFreezWrapper.getPayRollBatch());
		changedDriverPayFreezWrapper.setPayRollBatchString(driverPayFreezWrapper.getPayRollBatchString());
		changedDriverPayFreezWrapper.setProbationDeductionAmount(driverPayFreezWrapper.getProbationDeductionAmount());
		changedDriverPayFreezWrapper.setQuatarAmount(driverPayFreezWrapper.getQuatarAmount());
		changedDriverPayFreezWrapper.setQutarAmt(driverPayFreezWrapper.getQutarAmt());
		changedDriverPayFreezWrapper.setQutarNotes(driverPayFreezWrapper.getQutarNotes());
		changedDriverPayFreezWrapper.setRate(driverPayFreezWrapper.getRate());
		changedDriverPayFreezWrapper.setReimburseAmount(driverPayFreezWrapper.getReimburseAmount());
		changedDriverPayFreezWrapper.setReimburseAmt(driverPayFreezWrapper.getReimburseAmt());
		changedDriverPayFreezWrapper.setReimburseNotes(driverPayFreezWrapper.getReimburseNotes());
		changedDriverPayFreezWrapper.setSeqNum(driverPayFreezWrapper.getSeqNum());
		changedDriverPayFreezWrapper.setSickParsonalAmount(driverPayFreezWrapper.getSickParsonalAmount());
		changedDriverPayFreezWrapper.setSickPersonalAmount(driverPayFreezWrapper.getSickPersonalAmount());
		changedDriverPayFreezWrapper.setSubTotalAmount(driverPayFreezWrapper.getSubTotalAmount());
		changedDriverPayFreezWrapper.setStatus(driverPayFreezWrapper.getStatus());
		changedDriverPayFreezWrapper.setSubTotalAmount(driverPayFreezWrapper.getSubTotalAmount());
		changedDriverPayFreezWrapper.setSumAmount(driverPayFreezWrapper.getSumAmount());
		changedDriverPayFreezWrapper.setSumTotal(driverPayFreezWrapper.getSumTotal());
		changedDriverPayFreezWrapper.setTerminal(driverPayFreezWrapper.getTerminal());
		changedDriverPayFreezWrapper.setTerminalname(driverPayFreezWrapper.getTerminalname());
		changedDriverPayFreezWrapper.setTotalAmount(driverPayFreezWrapper.getTotalAmount());
		changedDriverPayFreezWrapper.setTotalRowCount(driverPayFreezWrapper.getTotalRowCount());
		changedDriverPayFreezWrapper.setTransportationAmount(driverPayFreezWrapper.getTransportationAmount());
		changedDriverPayFreezWrapper.setVacationAmount(driverPayFreezWrapper.getVacationAmount());
	}
	
	private void map(ChangedTicket changedTicket, Ticket ticket) {
		changedTicket.setAutoCreated(ticket.getAutoCreated());
		changedTicket.setBaseTicketId(ticket.getId());
		changedTicket.setBillBatch(ticket.getBillBatch());
		changedTicket.setChangedStatus(ChangedTicket.CHANGED_STATUS_IN_PROCESS);
		changedTicket.setCompanyLocation(ticket.getCompanyLocation());
		changedTicket.setCustomer(ticket.getCustomer());
		changedTicket.setDestination(ticket.getDestination());
		changedTicket.setDestinationTicket(ticket.getDestinationTicket());
		changedTicket.setDriver(ticket.getDriver());
		changedTicket.setDriverCompany(ticket.getDriverCompany());
		changedTicket.setDriverPayRate(ticket.getDriverPayRate());
		changedTicket.setGallons(ticket.getGallons());
		changedTicket.setInvoiceDate(ticket.getInvoiceDate());
		changedTicket.setInvoiceNumber(ticket.getInvoiceNumber());
		changedTicket.setLandfillGross(ticket.getLandfillGross());
		changedTicket.setLandfillNet(ticket.getLandfillNet());
		changedTicket.setLandfillTare(ticket.getLandfillTare());
		changedTicket.setLandfillTimeIn(ticket.getLandfillTimeIn());
		changedTicket.setLandfillTimeOut(ticket.getLandfillTimeOut());
		changedTicket.setLandfillTons(ticket.getLandfillTons());
		changedTicket.setLoadDate(ticket.getLoadDate());
		changedTicket.setNotes(ticket.getNotes());
		changedTicket.setOrigin(ticket.getOrigin());
		changedTicket.setOriginTicket(ticket.getOriginTicket());
		changedTicket.setPaperVerifiedStatus(ticket.getPaperVerifiedStatus());
		changedTicket.setPayRollBatch(ticket.getPayRollBatch());
		changedTicket.setPayRollStatus(ticket.getPayRollStatus());
		changedTicket.setStatus(ticket.getStatus());
		changedTicket.setSubcontractor(ticket.getSubcontractor());
		changedTicket.setTerminal(ticket.getTerminal());
		changedTicket.setTicketStatus(ticket.getTicketStatus());
		changedTicket.setTrailer(ticket.getTrailer());
		changedTicket.setTransferGross(ticket.getTransferGross());
		changedTicket.setTransferNet(ticket.getTransferNet());
		changedTicket.setTransferTare(ticket.getTransferTare());
		changedTicket.setTransferTimeIn(ticket.getTransferTimeIn());
		changedTicket.setTransferTimeOut(ticket.getTransferTimeOut());
		changedTicket.setTransferTons(ticket.getTransferTons());
		changedTicket.setUnloadDate(ticket.getUnloadDate());
		changedTicket.setVehicle(ticket.getVehicle());
		changedTicket.setVoucherDate(ticket.getVoucherDate());
		changedTicket.setVoucherNumber(ticket.getVoucherNumber());
		changedTicket.setVoucherStatus(ticket.getVoucherStatus());
	}
	
	private void checkAndRemoveSubContractorCriteria(SearchCriteria criteria) {
		String subcontractorId = (String) criteria.getSearchMap().get("subcontractor");
		if (StringUtils.isEmpty(subcontractorId)) {
			criteria.getSearchMap().remove("subcontractor");
		}
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
		
		checkAndRemoveSubContractorCriteria(criteria);
		
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
		// WM Ticket change - 23rd May 2017
		Long userId = getUser(request).getId();
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
				
				// WM Ticket change - 23rd May 2017
				TicketUtils.processWMTicketForTicketDelete(entity, WMTicket.PROCESSING_STATUS_PROCESSING, userId, genericDAO);
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
				String query="select obj from Driver obj where obj.terminal="+terminalId+" and obj.company in ("+companyId+",149) order by fullName ASC, id desc";
				dlst=genericDAO.executeSimpleQuery(query);
				}
				else{
					String query="select obj from Driver obj where obj.status=1 and obj.terminal="+terminalId+" and obj.company in ("+companyId+",149) order by fullName ASC, id desc";
					
					dlst=genericDAO.executeSimpleQuery(query);
				}
			}
			
			populateDriverNameWithStatus(dlst);
			
			Gson gson = new Gson();
			return gson.toJson(dlst);
		}

		if ("findAllDriver".equalsIgnoreCase(action)) {

			List<Driver> dlst=null;
				String query="select obj from Driver obj where obj.status=1 order by fullName ASC, id desc";
				dlst=genericDAO.executeSimpleQuery(query);
			
			populateDriverNameWithStatus(dlst);	
			
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
	
	private void populateDriverNameWithStatus(List<Driver> driverList) {
		if (driverList == null) {
			return;
		}
		
		for (Driver aDriver : driverList) {
			aDriver.populateDriverNameWithStatus();
		}
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
	
	@Override
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		if (StringUtils.equals("xls", type )) {
			exportForUpload(model, request, response);
			return;
		}
		
		String reportName = "ticketReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		List<Ticket> ticketList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), ticketList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					ticketList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			if (type.equals("html")) {
				response.getOutputStream().println(
								"<script language=\"javascript\">window.print()</script>");
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		/****
		List columnPropertyList = (List) request.getSession().getAttribute(
				"columnPropertyList");
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");

		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html"))
			response.setHeader("Content-Disposition", "attachment;filename="
					+ urlContext + "Report." + type);
		try {
			criteria.setPageSize(100000);
			String label = getCriteriaAsString(criteria);
			ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(),
					columnPropertyList, criteria, request);
			out.writeTo(response.getOutputStream());
			if (type.equals("html"))
				response.getOutputStream()
						.println(
								"<script language=\"javascript\">window.print()</script>");
			criteria.setPageSize(25);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		}
		*******/
	}
	
	private void exportForUpload(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		String reportName = "ticketsForUpload";
		String type = "csv";
		response.setContentType(MimeUtil.getContentType(type));
		response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		
		List<Ticket> ticketList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), injuryList,
						columnPropertyList, request);*/
			out = dynamicReportService.generateStaticReport(reportName,
					ticketList, params, type, request);
			out.writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
