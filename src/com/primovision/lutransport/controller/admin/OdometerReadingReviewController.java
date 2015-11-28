package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.Odometer;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/admin/odometerreview")
public class OdometerReadingReviewController extends CRUDController<Odometer>{
	
	public OdometerReadingReviewController() {
		setUrlContext("admin/odometerreview");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	 private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
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
			String truck = (String) criteria.getSearchMap().get("truck.unit");	// modified from truck.id		
			String dateFrom = (String) criteria.getSearchMap().get("dateFrom");
			String dateTo = (String) criteria.getSearchMap().get("dateTo");
			String startMilesFrom = (String) criteria.getSearchMap().get("startMilesFrom");
			String startMilesTo = (String) criteria.getSearchMap().get("startMilesTo");
			String endMilesFrom = (String) criteria.getSearchMap().get("endMilesFrom");
			String endMilesTo = (String) criteria.getSearchMap().get("endMilesTo");
			String milesFrom = (String) criteria.getSearchMap().get("milesFrom");
			String milesTo = (String) criteria.getSearchMap().get("milesTo");
			
			
			StringBuffer query = new StringBuffer("select obj from Odometer obj  where 1=1");
			StringBuffer countquery = new StringBuffer("select count(obj) from Odometer obj  where 1=1");
			
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
	        	query.append(" and obj.truck.unit in ("+truck+")"); // modified from obj.truck.id
	        	countquery.append(" and obj.truck.unit in ("+truck+")"); // modified from obj.truck.id
			}        
	        
	        
	        if(!StringUtils.isEmpty(dateFrom)){
	        	try {
					query.append(" and obj.recordDate  >='"+dateFormat.format(sdf.parse(dateFrom))+"'");
					countquery.append(" and obj.recordDate  >='"+dateFormat.format(sdf.parse(dateFrom))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
			}
	        
	        if(!StringUtils.isEmpty(dateTo)){
	        	try {
					query.append(" and obj.recordDate  <='"+dateFormat.format(sdf.parse(dateTo))+"'");
					countquery.append(" and obj.recordDate  <='"+dateFormat.format(sdf.parse(dateTo))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	        
	        if(!StringUtils.isEmpty(startMilesFrom)){
	        	query.append(" and obj.startReading  >="+startMilesFrom);
	        	countquery.append(" and obj.startReading  >="+startMilesFrom);
			}
	        
	        if(!StringUtils.isEmpty(startMilesTo)){
	        	query.append(" and obj.startReading <="+startMilesTo);
	        	countquery.append(" and obj.startReading  <="+startMilesTo);
			}
	        
	        if(!StringUtils.isEmpty(endMilesFrom)){
	        	query.append(" and obj.endReading  >="+endMilesFrom);
	        	countquery.append(" and obj.endReading  >="+endMilesFrom);
			}
	        
	        if(!StringUtils.isEmpty(endMilesTo)){
	        	query.append(" and obj.endReading  <="+endMilesTo);
	        	countquery.append(" and obj.endReading  <="+endMilesTo);
			}
	        
	        if(!StringUtils.isEmpty(milesFrom)){
	        	query.append(" and obj.miles  >="+milesFrom);
	        	countquery.append(" and obj.miles  >="+milesFrom);
			}
	        
	        if(!StringUtils.isEmpty(milesTo)){
	        	query.append(" and obj.miles  <="+milesTo);
	        	countquery.append(" and obj.miles  <="+milesTo);
			}
	        
	        query.append(" order by obj.recordDate desc");
	        countquery.append(" order by obj.recordDate desc");
			
	        Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
	        		countquery.toString()).getSingleResult();        
			criteria.setRecordCount(recordCount.intValue());	
			
			
			model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
					.setMaxResults(criteria.getPageSize())
					.setFirstResult(criteria.getPage() * criteria.getPageSize())
					.getResultList());
			return urlContext + "/list";
		}
	 
	 
	 @Override
		public void setupCreate(ModelMap model, HttpServletRequest request) {					
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
		}
	 
	 @Override
		public void setupList(ModelMap model, HttpServletRequest request) {
			populateSearchCriteria(request, request.getParameterMap());
			setupCreate(model, request);
			Map criterias = new HashMap();
			criterias.clear();
			
			criterias.put("type", 3);
			model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			
			criterias.put("type", 4);
			model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			
			model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
			
		}
	 
	 @Override
		public String save(HttpServletRequest request,
				@ModelAttribute("modelObject") Odometer entity,
				BindingResult bindingResult, ModelMap model) {
		 
		    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");			
			
			Map criterias = new HashMap();
			
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
					setTruckForOdometer(entity);
					System.out.println("Truck ID (After date check) = " + entity.getTruck().getId());
//					entity.setTruck(truck);
				}			
			}	
			
			if(entity.getDriver()==null){
				bindingResult.rejectValue("driver", "error.select.option", null, null);
			}
			
			if(entity.getStartReading()==null){
				bindingResult.rejectValue("startReading", "NotNull.java.lang.Integer",
						null, null);
			}
			else{
				String query = "select obj from Odometer obj where 1=1 and obj.tempTruck='"+entity.getTempTruck()+"' and obj.startReading<="+entity.getStartReading()+" and obj.endReading >"+entity.getStartReading();
				if(entity.getId()!=null){
					query = query + " and id!="+entity.getId();
				}
				List<Odometer> odolist = genericDAO.executeSimpleQuery(query);
				
				if(odolist!=null && odolist.size()>0){
					bindingResult.rejectValue("startReading", "error.invalid.startReading",
							null, null);
				}
			}
			
			if(entity.getEndReading()!=null){
				if(entity.getEndReading()<entity.getStartReading()){
					bindingResult.rejectValue("endReading", "error.invalid.endReading",
							null, null);
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
			
			
			
			
			if(entity.getEndReading()==null){
				entity.setInComplete("Yes");
			} 
			else{
				entity.setInComplete("No");
			}
			
			String bdate=dateFormat1.format(entity.getRecordDate());			
			LocalDate now = new LocalDate(bdate);			
			LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
	   	  	entity.setBatchDate(sunday.toDate());
	   	  	
	   	  	
	   	  	criterias.clear();
	   	  	criterias.put("id", entity.getDriver().getId());
	   	  	Driver drviver = genericDAO.getByCriteria(Driver.class, criterias);
	   	    entity.setDriverCompany(drviver.getCompany());
	   	  	
	   	  	entity.setTerminal(drviver.getTerminal());
	   	  	
	   	    entity.setTerminalName(drviver.getTerminal().getName());
	   	  	
	   	  	entity.setCompanyName(drviver.getCompany().getName());
	   	  	
	   	  	entity.setTempDriverName(drviver.getFullName());
	   	  	
	   	  	entity.setTempRecordDate(sdf.format(entity.getRecordDate()));
			
	   	  	if(entity.getId()==null){
	   	  		entity.setEnteredBy("system");   	  		
	   	  	}
	   	  	
			beforeSave(request, entity, model);			
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
			
			
			/*if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
				request.getSession().setAttribute("msg","Odometer reading updated successfully");
				return "redirect:list.do";
			}
			else{			
				request.getSession().setAttribute("msg","Odometer reading added successfully");
				if(entity.getInComplete().equals("Yes")){
					return "redirect:list.do";
				}
				else{
					return "redirect:create.do";
				}
			}*/
			
			return "redirect:list.do";
		}
	 
	private void setTruckForOdometer(Odometer entity) {
		String vehicleQuery = StringUtils.EMPTY;
		if (entity.getRecordDate() != null) {
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
					+ Integer.parseInt(entity.getTempTruck()) + " and obj.validFrom<='"
					+ dateFormat.format(entity.getRecordDate()) + "' and obj.validTo>='"
					+ dateFormat.format(entity.getRecordDate()) + "'";
		}

		System.out.println("******* The vehicle query for driver tripsheet is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);

		if (vehicleList != null && vehicleList.size() > 0) {
			entity.setTruck(vehicleList.get(0));
		}
	}
	 
	 @Override
		protected String processAjaxRequest(HttpServletRequest request,
				String action, Model model) {
			if ("calcMiles".equalsIgnoreCase(action)) {	

				try 
				{
					List<String> list = new ArrayList<String>();
					Double miles = 0.0;
					Double startReading=Double.parseDouble(request.getParameter("startReading"));			
					Double endReading=Double.parseDouble(request.getParameter("endReading"));		
					miles = endReading - startReading;
					DecimalFormat df = new DecimalFormat("#");
					df.setMaximumFractionDigits(3);					
					list.add(df.format(miles));
					Gson gson = new Gson();
					return gson.toJson(list);
				} catch (Exception e) 
				{				
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
	 
	 
	 @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/exportdata.do")
		public void display(ModelMap model, HttpServletRequest request,
				HttpServletResponse response,@RequestParam(required = false, value = "type") String type,
				@RequestParam(required = false, value = "jrxml") String jrxml) {
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
	String dateFrom = (String) criteria.getSearchMap().get("dateFrom");
	String dateTo = (String) criteria.getSearchMap().get("dateTo");
	String startMilesFrom = (String) criteria.getSearchMap().get("startMilesFrom");
	String startMilesTo = (String) criteria.getSearchMap().get("startMilesTo");
	String endMilesFrom = (String) criteria.getSearchMap().get("endMilesFrom");
	String endMilesTo = (String) criteria.getSearchMap().get("endMilesTo");
	String milesFrom = (String) criteria.getSearchMap().get("milesFrom");
	String milesTo = (String) criteria.getSearchMap().get("milesTo");
	
	
	StringBuffer query = new StringBuffer("select obj from Odometer obj  where 1=1");
	
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
    
    
    if(!StringUtils.isEmpty(dateFrom)){
    	try {
			query.append(" and obj.recordDate  >='"+dateFormat.format(sdf.parse(dateFrom))+"'");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
    
    if(!StringUtils.isEmpty(dateTo)){
    	try {
			query.append(" and obj.recordDate  <='"+dateFormat.format(sdf.parse(dateTo))+"'");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    
    if(!StringUtils.isEmpty(startMilesFrom)){
    	query.append(" and obj.startReading  >="+startMilesFrom);
    	
	}
    
    if(!StringUtils.isEmpty(startMilesTo)){
    	query.append(" and obj.startReading <="+startMilesTo);
    	
	}
    
    if(!StringUtils.isEmpty(endMilesFrom)){
    	query.append(" and obj.endReading  >="+endMilesFrom);
    	
	}
    
    if(!StringUtils.isEmpty(endMilesTo)){
    	query.append(" and obj.endReading  <="+endMilesTo);
    	
	}
    
    if(!StringUtils.isEmpty(milesFrom)){
    	query.append(" and obj.miles  >="+milesFrom);
    
	}
    
    if(!StringUtils.isEmpty(milesTo)){
    	query.append(" and obj.miles  <="+milesTo);
 
	}
    
    query.append(" order by obj.recordDate desc");
    
	List<Odometer>	OdometerList = genericDAO.executeSimpleQuery(query.toString());
			
			
			datas.put("data", OdometerList);
			datas.put("params",params);
			
			try {		
				
				
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= OdometerReadingReview." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				
				if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("driverodometerreview"+"pdf",
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("csv")) {
					out = dynamicReportService.generateStaticReport("driverodometerreview",
							(List)datas.get("data"), params, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport("driverodometerreview",
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
