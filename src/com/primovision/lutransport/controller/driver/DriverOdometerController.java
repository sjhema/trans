package com.primovision.lutransport.controller.driver;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.Odometer;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/driver/odometer")
public class DriverOdometerController extends CRUDController<Odometer>{
	
	public DriverOdometerController() {
		setUrlContext("driver/odometer");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
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
		public void setupCreate(ModelMap model, HttpServletRequest request) {					
			
		}
	 
	 @Override
		public void setupList(ModelMap model, HttpServletRequest request) {
			populateSearchCriteria(request, request.getParameterMap());
			setupCreate(model, request);
		}
	 
	 
	 @Override
		public String create(ModelMap model, HttpServletRequest request) {
			setupCreate(model, request);
			
			// Driver mobile entry validation fix - 23rd Jun 2016
			/*User userObj = (User) request.getSession().getAttribute("userInfo");			
			Map criterias = new HashMap();		
			criterias.clear();
			criterias.put("status",1);
			criterias.put("firstName",userObj.getFirstName());
			criterias.put("lastName", userObj.getLastName());
			Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
			
			criterias.clear();
			criterias.put("driver.id",driver.getId());
			criterias.put("inComplete","Yes");
			Odometer obj = genericDAO.getByCriteria(Odometer.class, criterias);
			if(obj!=null){
				request.getSession().setAttribute("error",
				"Cannot add new entry until previous logs are complete!");
				return "redirect:list.do";
			}
			else{
				return urlContext + "/form";
			}*/
			
			return urlContext + "/form";
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
			
			/*LocalDate now = new LocalDate();			
			LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
			
			DateTime lastWeek = new DateTime().minusDays(7);*/
			
			LocalDate now = new LocalDate();	
			LocalDate prevWeek = now.minusWeeks(1);
			LocalDate prevFriday = prevWeek.withDayOfWeek(DateTimeConstants.FRIDAY);
			
			criterias.clear();
			criterias.put("status",1);
			criterias.put("firstName",userObj.getFirstName());
			criterias.put("lastName", userObj.getLastName());
			Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
			//criteria.getSearchMap().put("driver",driver.getId());
			//criteria.getSearchMap().put("batchDate",dateFormat1.format(sunday.toDate()));		
			model.addAttribute("list",genericDAO.executeSimpleQuery("Select obj from Odometer obj where obj.driver in ("+driver.getId()+") and obj.recordDate>='"+dateFormat1.format(prevFriday.toDate())+"'"));
			return urlContext + "/list";
		}
	 
	 @Override
		public String save(HttpServletRequest request,
				@ModelAttribute("modelObject") Odometer entity,
				BindingResult bindingResult, ModelMap model) {
		    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");		
			User userObj = (User) request.getSession().getAttribute("userInfo");
			
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
					//entity.setTruck(truck);
				}			
			}	
			
			if(entity.getStartReading()==null){
				bindingResult.rejectValue("startReading", "NotNull.java.lang.Integer",
						null, null);
			}
			else{
				// Driver odometer start reading invalid fix - 17th Jun 2016
				//String query = "select obj from Odometer obj where 1=1 and obj.tempTruck='"+entity.getTempTruck()+"' and obj.endReading >"+entity.getStartReading();
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
			
			String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())+" and obj.validFrom<='"+dateFormat.format(entity.getRecordDate())+"' and obj.validTo>='"+dateFormat.format(entity.getRecordDate())+"'";
			
			System.out.println("******* The vehicle query for driver odometer is "+vehicleQuery);
			List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
			
			if(vehicleList==null || vehicleList.size()==0)
			{
				
			}
			else{
				entity.setTruck(vehicleList.get(0));
			}
			
			
			
			criterias.clear();
			criterias.put("status",1);
			criterias.put("firstName",userObj.getFirstName());
			criterias.put("lastName", userObj.getLastName());
			Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
			entity.setDriver(driver);
			
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
	   	  	
	   	  	
	   	    entity.setDriverCompany(driver.getCompany());
	   	  	
	   	  	entity.setTerminal(driver.getTerminal());
	   	  	
	   	    entity.setTerminalName(driver.getTerminal().getName());
	   	  	
	   	  	entity.setCompanyName(driver.getCompany().getName());
	   	  	
	   	  	entity.setTempDriverName(driver.getFullName());
	   	  	
	   	  	entity.setTempRecordDate(sdf.format(entity.getRecordDate()));
			
			
	   	    if(entity.getId()==null){
	   	  		entity.setEnteredBy("driver");	   	  		
	   	  	}
	   	  	
	   	    
	   	    if(entity.getStartReading()!=null && entity.getEndReading()!=null){
	   	    	entity.setMiles(entity.getEndReading()-entity.getStartReading());
	   	    }
			beforeSave(request, entity, model);			
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
			
			String mobileEntryTableUpdateQuery = "update DriverMobileEntry d "
					+ "set d.odometer_flag='Y' "
					+ ", d.enteredBy='" + getUser(request).getFullName() + "'"
					+ ", d.enteredById=" + getUser(request).getId()
					+ ", d.enteredByType='DRIVER'"
					+ " where d.employeeName in ('"+driver.getFullName()+"') and d.entryDate='"+mysqldf.format(entity.getRecordDate())+"'";
			genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery.toString());
			
			
			if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
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
			
			return "";
		}
}
