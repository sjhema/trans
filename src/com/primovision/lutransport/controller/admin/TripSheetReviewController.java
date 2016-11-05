package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
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
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.Odometer;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/admin/tripsheetreview")
public class TripSheetReviewController extends CRUDController<TripSheet>{

	public TripSheetReviewController() {
		setUrlContext("admin/tripsheetreview");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
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
		public String search2(ModelMap model, HttpServletRequest request) {
			setupList(model, request);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");				

			String company = (String) criteria.getSearchMap().get("driverCompany.name");
			String terminal = (String) criteria.getSearchMap().get("terminal.name");
			String driver = (String) criteria.getSearchMap().get("driver.fullName");
			String truck = (String) criteria.getSearchMap().get("truck.unit");	
			String trailer = (String) criteria.getSearchMap().get("trailer.id");
			String batchDateFrom = (String) criteria.getSearchMap().get("batchDateFrom");
			String batchDateTo = (String) criteria.getSearchMap().get("batchDateTo");
			String loadDateFrom = (String) criteria.getSearchMap().get("lDateFrom");
			String loadDateTo = (String) criteria.getSearchMap().get("lDateTo");
			String unloadDateFrom = (String) criteria.getSearchMap().get("ulDateFrom");
			String unloadDateTo = (String) criteria.getSearchMap().get("ulDateTo");
			String origin = (String) criteria.getSearchMap().get("origin.id");
			String desination = (String) criteria.getSearchMap().get("destination.id");
			String originTicket = (String) criteria.getSearchMap().get("originTicket");
			String destinationTicket = (String) criteria.getSearchMap().get("destinationTicket");
			String verificationStatus = (String) criteria.getSearchMap().get("verificationStatus");
			
			
			
			StringBuffer query = new StringBuffer("select obj from TripSheet obj  where 1=1");
			StringBuffer countquery = new StringBuffer("select count(obj) from TripSheet obj  where 1=1");
			
			if(!StringUtils.isEmpty(company)){
				query.append(" and obj.driverCompany.name in ('"+company+"')");
				countquery.append(" and obj.driverCompany.name in ('"+company+"')");
			}
			
			if(!StringUtils.isEmpty(terminal)){
				query.append(" and obj.terminal.name in ('"+terminal+"')");
				countquery.append(" and obj.terminal.name in ('"+terminal+"')");
			}
			
	        if(!StringUtils.isEmpty(driver)){
	        	query.append(" and obj.driver.fullName in ('"+driver+"')");
	        	countquery.append(" and obj.driver.fullName in ('"+driver+"')");
			}
	        
	        if(!StringUtils.isEmpty(truck)){
	        	query.append(" and obj.truck.unit in ("+truck+")");
	        	countquery.append(" and obj.truck.unit in ("+truck+")");
			}    
	        
	        if(!StringUtils.isEmpty(trailer)){
	        	query.append(" and obj.trailer in ("+trailer+")");
	        	countquery.append(" and obj.trailer in ("+trailer+")");
			} 
	        
	        
	        if(!StringUtils.isEmpty(batchDateFrom)){
	        	try {
					query.append(" and obj.batchDate  >='"+dateFormat.format(sdf.parse(batchDateFrom))+"'");
					countquery.append(" and obj.batchDate  >='"+dateFormat.format(sdf.parse(batchDateFrom))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
			}
	        
	        if(!StringUtils.isEmpty(batchDateTo)){
	        	try {
					query.append(" and obj.batchDate  <='"+dateFormat.format(sdf.parse(batchDateTo))+"'");
					countquery.append(" and obj.batchDate <='"+dateFormat.format(sdf.parse(batchDateTo))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	        
	        if(!StringUtils.isEmpty(loadDateFrom)){
	        	try {
					query.append(" and obj.loadDate  >='"+dateFormat.format(sdf.parse(loadDateFrom))+"'");
					countquery.append(" and obj.loadDate  >='"+dateFormat.format(sdf.parse(loadDateFrom))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
			}
	        
	        if(!StringUtils.isEmpty(loadDateTo)){
	        	try {
					query.append(" and obj.loadDate  <='"+dateFormat.format(sdf.parse(loadDateTo))+"'");
					countquery.append(" and obj.loadDate <='"+dateFormat.format(sdf.parse(loadDateTo))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        
	        
	        if(!StringUtils.isEmpty(unloadDateFrom)){
	        	try {
					query.append(" and obj.unloadDate  >='"+dateFormat.format(sdf.parse(unloadDateFrom))+"'");
					countquery.append(" and obj.unloadDate >='"+dateFormat.format(sdf.parse(unloadDateFrom))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
			}
	        
	        if(!StringUtils.isEmpty(unloadDateTo)){
	        	try {
					query.append(" and obj.unloadDate  <='"+dateFormat.format(sdf.parse(unloadDateTo))+"'");
					countquery.append(" and obj.unloadDate  <='"+dateFormat.format(sdf.parse(unloadDateTo))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        
	        if(!StringUtils.isEmpty(origin)){
				query.append(" and obj.origin in ("+origin+")");
				countquery.append(" and obj.origin in ("+origin+")");
			}
	        
	        if(!StringUtils.isEmpty(desination)){
				query.append(" and obj.destination in ("+desination+")");
				countquery.append(" and obj.destination in ("+desination+")");
			}
	        
	        if(!StringUtils.isEmpty(originTicket)){
				query.append(" and obj.originTicket in ("+originTicket+")");
				countquery.append(" and obj.originTicket in ("+originTicket+")");
			}
	        
	        if(!StringUtils.isEmpty(destinationTicket)){
				query.append(" and obj.destinationTicket in ("+destinationTicket+")");
				countquery.append(" and obj.destinationTicket in ("+destinationTicket+")");
			}
	        
	        if(!StringUtils.isEmpty(verificationStatus)){
	        	if(verificationStatus.equals("null")){
	        		
	        		query.append(" and obj.verificationStatus is NULL");
					countquery.append(" and obj.verificationStatus is NULL");
	        		
	        	}
	        	else{
	        		query.append(" and obj.verificationStatus in ('"+verificationStatus+"')");
					countquery.append(" and obj.verificationStatus in ('"+verificationStatus+"')");
	        	}
			}
	       
	        query.append(" order by obj.loadDate desc,obj.origin asc,obj.originTicket asc");
	        countquery.append(" order by obj.loadDate desc,obj.origin asc,obj.originTicket asc");
	       
			
	        Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
	        		countquery.toString()).getSingleResult();        
			criteria.setRecordCount(recordCount.intValue());	
				
			System.out.println("Query here = " + query.toString());
			
			model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
					.setMaxResults(criteria.getPageSize())
					.setFirstResult(criteria.getPage() * criteria.getPageSize())
					.getResultList());
			return urlContext + "/list";
		}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		User userObj = (User) request.getSession().getAttribute("userInfo");
		Map criterias = new HashMap();
		criterias.clear();
		if(request.getParameter("id")!=null){
	 		criterias.clear();
	 		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			
	 	}
	 	else{
	 		criterias.clear();
			criterias.put("status", 1);
	 		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
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
	}
	
	
	
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		Map criterias = new HashMap();
		criterias.clear();
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class,criterias, "fullName", false));
		
		criterias.clear();
		
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		criterias.put("id!",91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		
		//model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1"));
		
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
		
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") TripSheet entity,
			BindingResult bindingResult, ModelMap model) {
		
		
		Map criterias = new HashMap();
		
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
				
				/**
				 * Added: Bug Fix - Save the truck ID corresponding to the latest date range 
				 */
				System.out.println("Truck ID (Incoming) = " + truck.getId());
				setTruckForTripSheet(entity);
				System.out.println("Truck ID (After date check) = " + entity.getTruck().getId());
//				entity.setTruck(truck);
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
		
		if(entity.getDriver()==null){
			bindingResult.rejectValue("driver", "error.select.option",
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
		
		

		
		/*LocalDate now = new LocalDate();			
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		if(!sunday.toDate().equals(entity.getBatchDate())){
			setupCreate(model, request);
			request.getSession().setAttribute("error",
			"Cannot save this entry. Invalid week of date!");
			return urlContext + "/form";
		}*/
		
		
		
		
		
		if(entity.getOriginTicket()==null || entity.getDestinationTicket()==null){
			entity.setIncomplete("Yes");
		}
		else{
			entity.setIncomplete("No");
		}
		
		criterias.clear();
   	  	criterias.put("id", entity.getDriver().getId());
   	  	Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		
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
   	  		entity.setEnteredBy("system");   	  		
   	  	}
	  	
	  	
		
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		String mobileEntryTableUpdateQuery = "update DriverMobileEntry d set "
				+ "d.tripsheet_flag='Y' "
				+ ", d.enteredBy='" + getUser(request).getFullName() + "'"
				+ ", d.enteredById=" + getUser(request).getId()
				+ ", d.enteredByType='OPS'"
				+ " where "
				+ "d.employeeName in ('"+driver.getFullName()+"') and d.entryDate='"+mysqldf.format(entity.getLoadDate())+"'";
		genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery.toString());
		
		
		

		/*if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
			request.getSession().setAttribute("msg","Trip sheet updated successfully");
			return "redirect:list.do";
		}
		else{			
			request.getSession().setAttribute("msg","Trip sheet added successfully");
			return "redirect:create.do";
		}*/
		
		return "redirect:list.do";
	}
	
	private void setTruckForTripSheet(TripSheet entity) {
		String vehicleQuery = StringUtils.EMPTY;
		if(entity.getLoadDate()!=null){
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())+" and obj.validFrom<='"+dateFormat.format(entity.getLoadDate())+"' and obj.validTo>='"+dateFormat.format(entity.getLoadDate())+"'";
		}
		else if(entity.getUnloadDate()!=null){
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())+" and obj.validFrom<='"+entity.getUnloadDate()+"' and obj.validTo>='"+entity.getUnloadDate()+"'";
		}
		System.out.println("******* The vehicle query for driver tripsheet is "+vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList != null && vehicleList.size() > 0) {
			entity.setTruck(vehicleList.get(0));
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
		
		
		if ("fetchcomterm".equalsIgnoreCase(action)) {	
			try {
				List<String> list = new ArrayList<String>();
				Map criterias = new HashMap();
				criterias.put("id",Long.parseLong(request.getParameter("driver")));
				Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
				list.add(driver.getCompany().getName());
				list.add(driver.getTerminal().getName());					
				Gson gson = new Gson();
				return gson.toJson(list);
			
			} catch (Exception e) {				
				return "";
			}
		}
		
		return "";
	}
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") TripSheet entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			String ticketQuery = "Select obj from Ticket obj where obj.originTicket="+entity.getOriginTicket()+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.driver in ("+entity.getDriver().getId()+") and obj.origin in ("+entity.getOrigin().getId()+") and obj.destination in ("+entity.getDestination().getId()+")";
			
			List<Ticket> ticketList = genericDAO.executeSimpleQuery(ticketQuery);
			
			if(ticketList!=null && !ticketList.isEmpty()){
				for(Ticket ticket:ticketList){
					if(ticket.getTicketStatus()==2 || ticket.getPayRollStatus()==2){
						request.getSession().setAttribute("error","Cannot delete this entry.");
					}
					else{
						genericDAO.delete(entity);
						genericDAO.delete(ticket);
					}
				}
			}
			else{
				genericDAO.delete(entity);
			}
			
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
			request.getSession().setAttribute("error","Cannot delete a parent row");
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/exportdata.do")
	public void display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml,
			@RequestParam(required = false, value = "audit") boolean audit) {
		Map<String,Object> datas = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
		.getAttribute("searchCriteria");				

String company = (String) criteria.getSearchMap().get("driverCompany.name");
String terminal = (String) criteria.getSearchMap().get("terminal.name");
String driver = (String) criteria.getSearchMap().get("driver.fullName");
String truck = (String) criteria.getSearchMap().get("truck.unit");	
String trailer = (String) criteria.getSearchMap().get("trailer.id");
String batchDateFrom = (String) criteria.getSearchMap().get("batchDateFrom");
String batchDateTo = (String) criteria.getSearchMap().get("batchDateTo");
String loadDateFrom = (String) criteria.getSearchMap().get("lDateFrom");
String loadDateTo = (String) criteria.getSearchMap().get("lDateTo");
String unloadDateFrom = (String) criteria.getSearchMap().get("ulDateFrom");
String unloadDateTo = (String) criteria.getSearchMap().get("ulDateTo");
String origin = (String) criteria.getSearchMap().get("origin.id");
String desination = (String) criteria.getSearchMap().get("destination.id");
String originTicket = (String) criteria.getSearchMap().get("originTicket");
String destinationTicket = (String) criteria.getSearchMap().get("destinationTicket");
String verificationStatus = (String) criteria.getSearchMap().get("verificationStatus");



StringBuffer query = new StringBuffer("select obj from TripSheet obj  where 1=1");

if(!StringUtils.isEmpty(company)){
	query.append(" and obj.driverCompany.name in ('"+company+"')");
	
}

if(!StringUtils.isEmpty(terminal)){
	query.append(" and obj.terminal.name in ('"+terminal+"')");

}

if(!StringUtils.isEmpty(driver)){
	query.append(" and obj.driver.fullName in ('"+driver+"')");

}

if(!StringUtils.isEmpty(truck)){
	query.append(" and obj.truck.unit in ("+truck+")");
	
}    

if(!StringUtils.isEmpty(trailer)){
	query.append(" and obj.trailer in ("+trailer+")");
	
} 


if(!StringUtils.isEmpty(batchDateFrom)){
	try {
		query.append(" and obj.batchDate  >='"+dateFormat.format(sdf.parse(batchDateFrom))+"'");
		
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

if(!StringUtils.isEmpty(batchDateTo)){
	try {
		query.append(" and obj.batchDate  <='"+dateFormat.format(sdf.parse(batchDateTo))+"'");
	
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


if(!StringUtils.isEmpty(loadDateFrom)){
	try {
		query.append(" and obj.loadDate  >='"+dateFormat.format(sdf.parse(loadDateFrom))+"'");
	
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

if(!StringUtils.isEmpty(loadDateTo)){
	try {
		query.append(" and obj.loadDate  <='"+dateFormat.format(sdf.parse(loadDateTo))+"'");
	
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


if(!StringUtils.isEmpty(unloadDateFrom)){
	try {
		query.append(" and obj.unloadDate  >='"+dateFormat.format(sdf.parse(unloadDateFrom))+"'");
	
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

if(!StringUtils.isEmpty(unloadDateTo)){
	try {
		query.append(" and obj.unloadDate  <='"+dateFormat.format(sdf.parse(unloadDateTo))+"'");
	
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

if(!StringUtils.isEmpty(origin)){
	query.append(" and obj.origin in ("+origin+")");
	
}

if(!StringUtils.isEmpty(desination)){
	query.append(" and obj.destination in ("+desination+")");
	
}

if(!StringUtils.isEmpty(originTicket)){
	query.append(" and obj.originTicket in ("+originTicket+")");
	
}

if(!StringUtils.isEmpty(destinationTicket)){
	query.append(" and obj.destinationTicket in ("+destinationTicket+")");

}

if(!StringUtils.isEmpty(verificationStatus)){
	if(verificationStatus.equals("null")){
		
		query.append(" and obj.verificationStatus is NULL");
		
		
	}
	else{
		query.append(" and obj.verificationStatus in ('"+verificationStatus+"')");
		
	}
}

// Trip sheet export order fix - 31st Mar 2016
query.append(" order by obj.loadDate asc,obj.origin asc,obj.originTicket asc");
//query.append(" order by obj.loadDate desc,obj.origin asc,obj.originTicket asc");



List<TripSheet>	tripsheettempList = genericDAO.executeSimpleQuery(query.toString());
List<TripSheet>	tripsheetList = new ArrayList<TripSheet>();		
		
for(TripSheet obj:tripsheettempList){
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
	tripsheetList.add(output);
}


		datas.put("data", tripsheetList);
		datas.put("params",params);
		
		try {		
			
			
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= Daily_Driver_Activity_Schedule." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			
			if (type.equals("pdf")) {
				String reportFile = audit ? "drivertripsheetreviewpdfAudit" : "drivertripsheetreviewpdf";
				out = dynamicReportService.generateStaticReport(reportFile,
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport("drivertripsheetreview",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("drivertripsheetreview",
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
