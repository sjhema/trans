package com.primovision.lutransport.controller.driver;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.TripSheetLocation;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.ReportService;


@Controller
@RequestMapping("/driver/tripsheet")
public class DriverTripSheetController extends CRUDController<TripSheet>{
	
	
	public DriverTripSheetController() {
		setUrlContext("driver/tripsheet");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
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

	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		User userObj = (User) request.getSession().getAttribute("userInfo");
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("status",1);
		criterias.put("firstName",userObj.getFirstName());
		criterias.put("lastName", userObj.getLastName());
		Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		
		criterias.clear();
		criterias.put("driverCompany", driver.getCompany());
		criterias.put("terminal",driver.getTerminal());
		criterias.put("type",1);
		List<TripSheetLocation> locList = genericDAO.findByCriteria(TripSheetLocation.class, criterias, "name", false);	
		String locNames = "";
		if(locList!=null  && locList.size()>0){
			
			for(TripSheetLocation obj : locList){
				if(locNames.equals("")){
					locNames = obj.getName();
				}
				else{
					locNames =  locNames + ","+obj.getName();
				}
			}
		}
		
		criterias.clear();
		criterias.put("driverCompany", driver.getCompany());
		criterias.put("terminal",driver.getTerminal());
		criterias.put("type",2);
		List<TripSheetLocation> destLocList = genericDAO.findByCriteria(TripSheetLocation.class, criterias, "name", false);	
		String destLocNames = "";
		if(destLocList!=null  && destLocList.size()>0){
			
			for(TripSheetLocation destObj : destLocList){
				if(destLocNames.equals("")){
					destLocNames = destObj.getName();
				}
				else{
					destLocNames =  destLocNames + ","+destObj.getName();
				}
			}	
		}
		
		if(request.getParameter("id")!=null){
			criterias.clear();
			//criterias.put("status",1);
			criterias.put("name", locNames);
			criterias.put("type", 1);
			model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			//criterias.put("status",1);
			criterias.put("name",destLocNames);
			criterias.put("type", 2);
			criterias.put("id!",91l);
			model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));	
		}
		else{
			criterias.clear();
			criterias.put("status",1);
			criterias.put("name", locNames);
			criterias.put("type", 1);
			model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("status",1);
			criterias.put("name",destLocNames);
			criterias.put("type", 2);
			criterias.put("id!",91l);
			model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}
		criterias.clear();
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
		
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	

	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");	
		
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);	
		
		User userObj = (User) request.getSession().getAttribute("userInfo");		
		Map criterias = new HashMap();
		
		LocalDate now = new LocalDate();	
		LocalDate currSunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		LocalDate prevWeek = now.minusWeeks(1);
		LocalDate prevFriday = prevWeek.withDayOfWeek(DateTimeConstants.FRIDAY);
		LocalDate prevSunday = prevWeek.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		//LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		//DateTime lastWeek = new DateTime().minusDays(7);
		
		criterias.clear();
		criterias.put("status",1);
		criterias.put("firstName",userObj.getFirstName());
		criterias.put("lastName", userObj.getLastName());
		Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		//criteria.getSearchMap().put("driver",driver.getId());
		//criteria.getSearchMap().put("batchDate",dateFormat1.format(sunday.toDate()));		
		List<TripSheet> tripSheetList = new ArrayList<TripSheet>();
		
		List<TripSheet> tripSheetsCurrWeek = genericDAO.executeSimpleQuery("Select obj from TripSheet obj where obj.driver in ("+driver.getId()+") and obj.batchDate='"+dateFormat1.format(currSunday.toDate())+"' order by obj.loadDate desc");
		
		for(TripSheet tripSheet:tripSheetsCurrWeek){			
							
			StringBuilder ticketQuery = new StringBuilder("Select obj from Ticket obj where obj.driver in ("+tripSheet.getDriver().getId()+") and obj.origin in ("+tripSheet.getOrigin().getId()+") and obj.destination in ("+tripSheet.getDestination().getId()+")");
			if(tripSheet.getOriginTicket()!=null){
				ticketQuery.append(" and obj.originTicket in ("+tripSheet.getOriginTicket()+")");
			}
			if(tripSheet.getDestinationTicket()!=null){
				ticketQuery.append(" and obj.destinationTicket in ("+tripSheet.getDestinationTicket()+")");
			}
		
			List<Ticket> ticketList = genericDAO.executeSimpleQuery(ticketQuery.toString());
			
			if(ticketList!=null && !ticketList.isEmpty()){
				for(Ticket ticket:ticketList){
					if(ticket.getDriverPayRate()==null || ticket.getDriverPayRate()==0.0){
						tripSheetList.add(tripSheet);
					}
				}
			}
			else{
				tripSheetList.add(tripSheet);
			}		
		}
		
		List<TripSheet> tripSheetsPrevWeek = genericDAO.executeSimpleQuery("Select obj from TripSheet obj where obj.driver in ("+driver.getId()+") and obj.loadDate>='"+dateFormat1.format(prevFriday.toDate())+"' and obj.batchDate='"+dateFormat1.format(prevSunday.toDate())+"' order by obj.loadDate desc");
		
		for(TripSheet tripSheet:tripSheetsPrevWeek){					
				if(StringUtils.isEmpty(tripSheet.getVerificationStatus())){
					tripSheetList.add(tripSheet);					
				}
				else{					
					//do nothing
				}			
		}
		
		model.addAttribute("list",tripSheetList);
		
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") TripSheet entity,
			BindingResult bindingResult, ModelMap model) {
		
		User userObj = (User) request.getSession().getAttribute("userInfo");
		
		Map criterias = new HashMap();
		
		if(!StringUtils.isEmpty(entity.getVerificationStatus())){
			setupCreate(model, request);
			request.getSession().setAttribute("error",
			"Cannot save this entry!");
			return urlContext + "/form";
		}
		
		
		if(entity.getOriginTicket()==null && entity.getDestinationTicket()==null){
			setupCreate(model, request);
			request.getSession().setAttribute("error",
			"Either origin or destination ticket is required");
			return urlContext + "/form";
		}
		
		if (StringUtils.isEmpty(entity.getTempTruck())) {
			bindingResult.rejectValue("tempTruck", "NotNull.java.lang.String",
					null, null);
		}
		else{
			criterias.clear();
			criterias.put("unit", Integer.parseInt(entity.getTempTruck()));
			Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
			if(truck==null){
				bindingResult.rejectValue("tempTruck", "error.invalid.truck",
						null, null);
			}
			else{
				entity.setTruck(truck);
			}			
		}
		
		
		if (StringUtils.isEmpty(entity.getTempTrailer())) {
			bindingResult.rejectValue("tempTrailer", "NotNull.java.lang.String",
					null, null);
		}
		else{
			criterias.clear();
			criterias.put("unit", Integer.parseInt(entity.getTempTrailer()));
			Vehicle trailer = genericDAO.getByCriteria(Vehicle.class, criterias);
			if(trailer==null){
				bindingResult.rejectValue("tempTrailer", "error.invalid.trailer",
						null, null);
			}
			else{
				entity.setTrailer(trailer);
			}
		}
		
		
		if (entity.getOrigin() == null) {
			bindingResult.rejectValue("origin", "error.select.option",
					null, null);
		}
		if (entity.getDestination() == null) {
			bindingResult.rejectValue("destination", "error.select.option",
					null, null);
		}		
		
		if(entity.getOrigin() != null &&  entity.getOriginTicket() !=null){
			String originTSQuery = "select obj from TripSheet obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="+entity.getOriginTicket();
			if(entity.getId()!=null){
				originTSQuery = originTSQuery +" and obj.id != "+entity.getId();
			}
			List<TripSheet> tripsheets = genericDAO.executeSimpleQuery(originTSQuery);
			
			String originTicketQuery = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="+entity.getOriginTicket();
			
			List<Ticket> tickets = genericDAO.executeSimpleQuery(originTicketQuery);
			
			
			if ((tripsheets!=null && tripsheets.size()>0) || (tickets!=null && tickets.size()>0)){		
				bindingResult.rejectValue("originTicket", "error.duplicate.entry",	null, null);		
			}		
		}		
		
		if(entity.getDestination() != null && entity.getDestinationTicket() !=null){
			String destinationTSQuery = "select obj from TripSheet obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket();
			if(entity.getId()!=null){
				destinationTSQuery = destinationTSQuery +" and obj.id != "+entity.getId();
			}
			List<TripSheet> tripsheets1 = genericDAO.executeSimpleQuery(destinationTSQuery);
			
			String destinationTicketQuery = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()+" and obj.destinationTicket="+entity.getDestinationTicket();
			
			List<Ticket> tickets = genericDAO.executeSimpleQuery(destinationTicketQuery);
			
			
			if ((tripsheets1!=null && tripsheets1.size()>0) || (tickets!=null && tickets.size()>0)){		
				bindingResult.rejectValue("destinationTicket", "error.duplicate.entry",	null, null);		
			}
		}		
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}		
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);			
			return urlContext + "/form";
		}	
		
		String vehicleQuery = "";
		if(entity.getLoadDate()!=null){
		
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())+" and obj.validFrom<='"+dateFormat.format(entity.getLoadDate())+"' and obj.validTo>='"+dateFormat.format(entity.getLoadDate())+"'";
		}
		else if(entity.getUnloadDate()!=null){
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())+" and obj.validFrom<='"+entity.getUnloadDate()+"' and obj.validTo>='"+entity.getUnloadDate()+"'";
		}
		System.out.println("******* The vehicle query for driver tripsheet is "+vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if(vehicleList==null || vehicleList.size()==0)
		{
			
		}
		else{
			entity.setTruck(vehicleList.get(0));
		}
		System.out.println("Entity truck set as " + entity.getTruck().getId());
		
		LocalDate now = new LocalDate();			
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		/*if(!sunday.toDate().equals(entity.getBatchDate())){
			setupCreate(model, request);
			request.getSession().setAttribute("error",
			"Cannot save this entry. Invalid week of date!");
			return urlContext + "/form";
		}*/
		
		
		criterias.clear();
		criterias.put("firstName",userObj.getFirstName());
		criterias.put("lastName", userObj.getLastName());
		criterias.put("status",1);
		Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		entity.setDriver(driver);
		
		
		if(entity.getOriginTicket()==null || entity.getDestinationTicket()==null){
			entity.setIncomplete("Yes");
		}
		else{
			entity.setIncomplete("No");
		}
		
		entity.setDriverCompany(driver.getCompany());
   	  	
   	  	entity.setTerminal(driver.getTerminal());
   	  	
   	     entity.setTerminalName(driver.getTerminal().getName());
	  	
	  	entity.setCompanyName(driver.getCompany().getName());
	  	
	  	////////////////// NEW Code //////////////////////////////////////
	  
		Location location = genericDAO.getById(Location.class, entity
				.getDestination().getId());
		
		int payUsing = 0;
		
		String initRateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
			+ entity.getOrigin().getId() + "' and obj.landfill='"						
			+ entity.getDestination().getId() 
			+ "' and obj.company='"+driver.getCompany().getId()
			+ "' and obj.terminal='"+driver.getTerminal().getId()
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
					+ "' and obj.company='"+driver.getCompany().getId()
					+ "' and obj.terminal='"+driver.getTerminal().getId()
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
				+ "' and obj.company='"+driver.getCompany().getId()
				+ "' and obj.terminal='"+driver.getTerminal().getId()+"'");
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
					+ "' and obj.company='"+driver.getCompany().getId()
					+ "' and obj.terminal='"+driver.getTerminal().getId()+"'");
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
			
			if(driver.getCompany().getId()==4l && driver.getTerminal().getId()==93l){
				if(driver.getDateProbationEnd()!=null){
					if(new LocalDate(driver.getDateProbationEnd()).isAfter(new LocalDate(entity.getBatchDate())) || new LocalDate(driver.getDateProbationEnd()).isEqual(new LocalDate(entity.getBatchDate()))){
						wbDrivers = true;
					}
				}
			}
			if(wbDrivers){
				payrate = fs.get(0).getProbationRate() ;						
			}
			else{
				
					if(driver.getShift().equals("1")){								
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
	  	
	 	
	  	entity.setPayRate(payrate);
	  	
	  		//////////////////NEW Code End  //////////////////////////////////////
	  	if(entity.getId()==null){
   	  		entity.setEnteredBy("driver");   	  		
   	  	}
	  	
	  	
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		String mobileEntryTableUpdateQuery = "update DriverMobileEntry d set d.tripsheet_flag='Y' where d.employeeName in ('"+driver.getFullName()+"') and d.entryDate='"+mysqldf.format(entity.getLoadDate())+"'";
		genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery.toString());
		

		if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
			request.getSession().setAttribute("msg","Trip sheet updated successfully");
			return "redirect:list.do";
		}
		else{			
			request.getSession().setAttribute("msg","Trip sheet added successfully");
			return "redirect:create.do";
		}
	}
	
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if ("weekOfDate".equalsIgnoreCase(action)) {	

			try {
				List<String> list = new ArrayList<String>();
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			
			String unloadDate=dateFormat1.format(sdf.parse(request.getParameter("unloadDate")));			
			LocalDate now = new LocalDate(unloadDate);			
			LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
			list.add(sdf.format(sunday.toDate()));
			Gson gson = new Gson();
			return gson.toJson(list);
			} catch (ParseException e) {				
				return "";
			}			
		}
		
		if ("verifytruck".equalsIgnoreCase(action)) {	

			try {
				Map criterias = new HashMap();
				criterias.put("unit", Integer.parseInt(request.getParameter("truck")));
				Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
				if(truck==null){
					return "Invalid truck number";
				}
				else{
					return "";
				}
			
			} catch (Exception e) {				
				return "";
			}		
		}
		
		if ("verifytrailer".equalsIgnoreCase(action)) {	

			try {
				Map criterias = new HashMap();
				criterias.put("unit", Integer.parseInt(request.getParameter("trailer")));
				Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
				if(truck==null){
					return "Invalid trailer number";
				}
				else{
					return "";
				}
			} catch (Exception e) {				
				return "";
			}		
		}
		
		
		if ("checkOriginTicket".equalsIgnoreCase(action)) {
			String mssg="";			
			
			String landfill=request.getParameter("landfil");
		    String landticket=request.getParameter("originTcket");
		    String tripsheetId=request.getParameter("ticId");
		    
            if(StringUtils.isEmpty(tripsheetId)){
            	tripsheetId=null;
		    }
		    
		    
		    String originTickQuery = "select obj from TripSheet obj where obj.origin.id="+landfill+" and obj.originTicket="+landticket+" and obj.id != "+tripsheetId;
			
		    String originTicQuery = "select obj from Ticket obj where obj.origin.id="+landfill+" and obj.originTicket="+landticket;
			
		    
		    List<TripSheet> tripsheet = genericDAO.executeSimpleQuery(originTickQuery);
		    
		    List<Ticket> tickets = genericDAO.executeSimpleQuery(originTicQuery);
			
			if ((tickets!=null && tickets.size()>0) || (tripsheet!=null && tripsheet.size()>0)){		
				mssg="Duplicate Origin Ticket";	
			}
		    
			 return mssg;
			
		}
		
		if ("checkDestinationTicket".equalsIgnoreCase(action)) {
			String mssg="";			
			
			String transfersttn=request.getParameter("transferstation");
		    String transferticket=request.getParameter("destinationTcket");
		    String tripsheetId=request.getParameter("ticId");
		    
            if(StringUtils.isEmpty(tripsheetId)){
            	tripsheetId=null;
		    }
		    
		    
		    String destinationTickQuery = "select obj from TripSheet obj where obj.destination.id="+transfersttn+" and obj.destinationTicket="+transferticket+" and obj.id != "+tripsheetId;
		    String destinationTicQuery = "select obj from Ticket obj where obj.destination.id="+transfersttn+" and obj.destinationTicket="+transferticket;
			
		    List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destinationTicQuery);
		    
		    List<TripSheet> tripsheets1 = genericDAO.executeSimpleQuery(destinationTickQuery);
		    
			if ((tickets1!=null && tickets1.size()>0) || (tripsheets1!=null && tripsheets1.size()>0)){		
				mssg="Duplicate Destination Ticket";			
			}
			 return mssg;
		}
		
		
		
		
		return "";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/exportdata.do")
	public void display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		
		Map<String,Object> datas = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");	
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);	
		
		User userObj = (User) request.getSession().getAttribute("userInfo");		
		Map criterias = new HashMap();
		
		
		LocalDate now = new LocalDate();	
		LocalDate currSunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		LocalDate prevWeek = now.minusWeeks(1);
		LocalDate prevFriday = prevWeek.withDayOfWeek(DateTimeConstants.FRIDAY);
		LocalDate prevSunday = prevWeek.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		criterias.clear();
		criterias.put("status",1);
		criterias.put("firstName",userObj.getFirstName());
		criterias.put("lastName", userObj.getLastName());
		Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
				
		List<TripSheet> tripSheetList = new ArrayList<TripSheet>();
		
		List<TripSheet> tripSheetsCurrWeek = genericDAO.executeSimpleQuery("Select obj from TripSheet obj where obj.driver in ("+driver.getId()+") and obj.batchDate='"+dateFormat1.format(currSunday.toDate())+"' order by obj.loadDate desc");
		
		for(TripSheet obj:tripSheetsCurrWeek){	
			
			StringBuilder ticketQuery = new StringBuilder("Select obj from Ticket obj where obj.driver in ("+obj.getDriver().getId()+") and obj.origin in ("+obj.getOrigin().getId()+") and obj.destination in ("+obj.getDestination().getId()+")");
			if(obj.getOriginTicket()!=null){
				ticketQuery.append(" and obj.originTicket in ("+obj.getOriginTicket()+")");
			}
			if(obj.getDestinationTicket()!=null){
				ticketQuery.append(" and obj.destinationTicket in ("+obj.getDestinationTicket()+")");
			}
		
			List<Ticket> ticketList = genericDAO.executeSimpleQuery(ticketQuery.toString());
			
			if(ticketList!=null && !ticketList.isEmpty()){
				for(Ticket ticket:ticketList){
					if(ticket.getDriverPayRate()==null || ticket.getDriverPayRate()==0.0){
						TripSheet output = new TripSheet();
						output.setTempBatchDate(sdf.format(obj.getBatchDate()));
						output.setCompanyName(obj.getCompanyName());
						output.setTerminalName(obj.getTerminalName());
						output.setTempDriverName(obj.getDriver().getFullName());
						output.setTempTruck(obj.getTempTruck());
						output.setTempTrailer(obj.getTempTrailer());
						output.setTemploadDate(sdf.format(obj.getLoadDate()));
						output.setTempOriginName(obj.getOrigin().getName());
						output.setOriginTicket(obj.getOriginTicket());
						output.setTempunloadDate(sdf.format(obj.getUnloadDate()));
						output.setTempDestName(obj.getDestination().getName());
						output.setDestinationTicket(obj.getDestinationTicket());
						output.setVerificationStatus(obj.getVerificationStatus());
						output.setPayRate(obj.getPayRate());
						tripSheetList.add(output);
					}
				}
			}
			else{
				TripSheet output = new TripSheet();
				output.setTempBatchDate(sdf.format(obj.getBatchDate()));
				output.setCompanyName(obj.getCompanyName());
				output.setTerminalName(obj.getTerminalName());
				output.setTempDriverName(obj.getDriver().getFullName());
				output.setTempTruck(obj.getTempTruck());
				output.setTempTrailer(obj.getTempTrailer());
				output.setTemploadDate(sdf.format(obj.getLoadDate()));
				output.setTempOriginName(obj.getOrigin().getName());
				output.setOriginTicket(obj.getOriginTicket());
				output.setTempunloadDate(sdf.format(obj.getUnloadDate()));
				output.setTempDestName(obj.getDestination().getName());
				output.setDestinationTicket(obj.getDestinationTicket());
				output.setVerificationStatus(obj.getVerificationStatus());
				output.setPayRate(obj.getPayRate());
				tripSheetList.add(output);
			}				
		}
		
		List<TripSheet> tripSheetsPrevWeek = genericDAO.executeSimpleQuery("Select obj from TripSheet obj where obj.driver in ("+driver.getId()+") and obj.loadDate>='"+dateFormat1.format(prevFriday.toDate())+"' and obj.batchDate='"+dateFormat1.format(prevSunday.toDate())+"' order by obj.loadDate desc");
		
		for(TripSheet obj:tripSheetsPrevWeek){					
				if(StringUtils.isEmpty(obj.getVerificationStatus())){
					TripSheet output = new TripSheet();
					output.setTempBatchDate(sdf.format(obj.getBatchDate()));
					output.setCompanyName(obj.getCompanyName());
					output.setTerminalName(obj.getTerminalName());
					output.setTempDriverName(obj.getDriver().getFullName());
					output.setTempTruck(obj.getTempTruck());
					output.setTempTrailer(obj.getTempTrailer());
					output.setTemploadDate(sdf.format(obj.getLoadDate()));
					output.setTempOriginName(obj.getOrigin().getName());
					output.setOriginTicket(obj.getOriginTicket());
					output.setTempunloadDate(sdf.format(obj.getUnloadDate()));
					output.setTempDestName(obj.getDestination().getName());
					output.setDestinationTicket(obj.getDestinationTicket());
					output.setVerificationStatus(obj.getVerificationStatus());
					output.setPayRate(obj.getPayRate());
					tripSheetList.add(output);					
				}
				else{					
					//do nothing
				}			
		}


				datas.put("data", tripSheetList);
				datas.put("params",params);
				
				try {		
					
					
					if (StringUtils.isEmpty(type))
						type = "xlsx";
					if (!type.equals("html") && !(type.equals("print"))) {
						response.setHeader("Content-Disposition",
								"attachment;filename=Driver Activity - " +sdf.format(new Date())+"."+type);
					}
					response.setContentType(MimeUtil.getContentType(type));
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					
					
					if (type.equals("pdf")) {
						out = dynamicReportService.generateStaticReport("drivertripsheet",
								(List)datas.get("data"), params, type, request);
					}
					else if (type.equals("csv")) {
						out = dynamicReportService.generateStaticReport("drivertripsheet",
								(List)datas.get("data"), params, type, request);
					}
					else {
						out = dynamicReportService.generateStaticReport("drivertripsheet",
								(List)datas.get("data"), params, type, request);
					}
				
					out.writeTo(response.getOutputStream());
					out.close();
					
				} catch (Exception e) {
					e.printStackTrace();
					log.warn("Unable to create file :" + e);
					request.getSession().setAttribute("errors", e.getMessage());
					
				}
			}
	
}
