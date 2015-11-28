package com.primovision.lutransport.controller.driver;

import java.text.DecimalFormat;
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

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/driver/fuellog")
public class DriverFuleLogController extends CRUDController<DriverFuelLog>{
	
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
	public DriverFuleLogController() {
		setUrlContext("driver/fuellog");
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
		binder.registerCustomEditor(DriverFuelCard.class, new AbstractModelEditor(DriverFuelCard.class));
		binder.registerCustomEditor(FuelCard.class, new AbstractModelEditor(FuelCard.class));
		
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
		criterias.put("status",1);
		criterias.put("driver.id",driver.getId());
		model.addAttribute("fuelcards", genericDAO.findByCriteria(DriverFuelCard.class, criterias, "fuelvendor", false));		
		
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
		model.addAttribute("list",genericDAO.executeSimpleQuery("Select obj from DriverFuelLog obj where obj.driver in ("+driver.getId()+") and obj.transactionDate>='"+dateFormat1.format(prevFriday.toDate())+"'"));
		return urlContext + "/list";
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") DriverFuelLog entity,
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
		
		/*if (entity.getDriverFuelCard()==null) {
			bindingResult.rejectValue("driverFuelCard", "error.select.option",
					null, null);
		}*/
		
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
		
		
		
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())+" and obj.validFrom<='"+dateFormat.format(entity.getTransactionDate())+"' and obj.validTo>='"+dateFormat.format(entity.getTransactionDate())+"'";
		
		System.out.println("******* The vehicle query for driver fuel log is "+vehicleQuery);
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
		
		
		Map prop=new HashMap();
		prop.clear();
		prop.put("transactionDate",dateFormat.format(entity.getTransactionDate()) );  
		prop.put("gallons",entity.getGallons() );
		if(entity.getDriverFuelCard()!=null)
			prop.put("driverFuelCard",entity.getDriverFuelCard().getId() );
		prop.put("truck",entity.getTruck().getId() );
		prop.put("driver",entity.getDriver().getId() );
		
		boolean rst=genericDAO.isUnique(DriverFuelLog.class,entity, prop);      	  
   	  	if(!rst){
   	  		setupCreate(model, request);   			
   			request.getSession().setAttribute("error","Duplicate Fuel Log Entry");
   			return urlContext + "/form";
   	  	}
		
   	    String bdate=dateFormat1.format(entity.getTransactionDate());			
		LocalDate now = new LocalDate(bdate);			
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
   	  	entity.setBatchDate(sunday.toDate());
   	  	
   	  	entity.setDriverCompany(driver.getCompany());
   	  	
   	  	entity.setTerminal(driver.getTerminal());
   	  	
   	  	entity.setTerminalName(driver.getTerminal().getName());
   	  	
   	  	entity.setCompanyName(driver.getCompany().getName());
   	  	
   	  	entity.setDriverName(driver.getFullName());
   	  	
   	  	entity.setTempTransactionDate(sdf.format(entity.getTransactionDate()));
   	  	
   	  	criterias.clear();
   	  	if(entity.getDriverFuelCard()!=null){
   	  		criterias.put("id",entity.getDriverFuelCard().getId());
   	  		DriverFuelCard drvfcObj =genericDAO.getByCriteria(DriverFuelCard.class, criterias);
   	  		entity.setFuelCard(drvfcObj.getFuelcard());
   	  		entity.setTempFuelCardNum(drvfcObj.getFuelcard().getFuelcardNum());
   	  	}
   	  	
   	  	
   	  	if(entity.getId()==null){
   	  		entity.setEnteredBy("driver");   	  		
   	  	}
   	  	
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		String mobileEntryTableUpdateQuery = "update DriverMobileEntry d set d.fuellog_flag='Y' where d.employeeName in ('"+driver.getFullName()+"') and d.entryDate='"+mysqldf.format(entity.getTransactionDate())+"'";
		genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery.toString());
		
		
		
		if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
			request.getSession().setAttribute("msg","Fuel log updated successfully");
			return "redirect:list.do";
		}
		else{			
			request.getSession().setAttribute("msg","Fuel log added successfully");
			return "redirect:create.do";
		}
	}
	
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {		
		
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
