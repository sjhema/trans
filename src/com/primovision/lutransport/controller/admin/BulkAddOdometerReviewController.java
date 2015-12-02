package com.primovision.lutransport.controller.admin;

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
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.Odometer;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("admin/bulkodometerreview")
public class BulkAddOdometerReviewController extends CRUDController<Odometer>{
		
		public BulkAddOdometerReviewController() {
			setUrlContext("admin/bulkodometerreview");
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
			public String save(HttpServletRequest request,
					@ModelAttribute("modelObject") Odometer entity,
					BindingResult bindingResult, ModelMap model) {
			 
			 SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			    Map criterias = new HashMap();
			    
			    for(int i=1;i<=7;i++){			    	
			    	if(!StringUtils.isEmpty(request.getParameter("truck"+i)) && !StringUtils.isEmpty(request.getParameter("startReading"+i)) ){
			    		Odometer odometer = new Odometer();
			    		try {
							odometer.setBatchDate(dateFormat1.parse(dateFormat1.format(sdf.parse(request.getParameter("day7")))));
						} catch (ParseException e) {							
							e.printStackTrace();
						}						
						odometer.setTempTruck(request.getParameter("truck"+i));
						
						odometer.setTempRecordDate(request.getParameter("day"+i));
						
						odometer.setEnteredBy("system");
						
						try {
							odometer.setRecordDate(dateFormat1.parse(dateFormat1.format(sdf.parse(request.getParameter("day"+i)))));
						} catch (ParseException e) {							
							e.printStackTrace();
						}
						
						if(StringUtils.isEmpty(request.getParameter("endReading"+i))){
							odometer.setInComplete("Yes");
						} 
						else{
							odometer.setInComplete("No");
							odometer.setEndReading(Integer.parseInt(request.getParameter("endReading"+i)));
						}	
						
						odometer.setStartReading(Integer.parseInt(request.getParameter("startReading"+i)));
						
						criterias.clear();
						/*criterias.put("unit", Integer.parseInt(request.getParameter("truck"+i)));
						Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
						if(truck!=null){
							odometer.setTruck(truck);
						}	*/
						
						
						/***********/
						
						String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
								+ request.getParameter("truck"+i)
								+") order by obj.validFrom desc";
						
						List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
							
						if(vehicleLists!=null && vehicleLists.size() > 0){				
							for(Vehicle vehicleObj : vehicleLists) {	
								if(vehicleObj.getValidFrom() != null && vehicleObj.getValidTo() != null){
									String dateStr = request.getParameter("day"+i);
									Date dateObj;
									try {
										dateObj = (new SimpleDateFormat("MM-dd-yyyy")).parse(dateStr);
										
										if ((dateObj.getTime() >= vehicleObj.getValidFrom().getTime() && dateObj.getTime() <= vehicleObj.getValidTo().getTime())) {
											odometer.setTruck(vehicleObj);
											System.out.println("Vehicle Id being saved = " + vehicleObj.getId());
											break;
										}
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						
						/**********/
						
						criterias.clear();
				   	  	criterias.put("id", entity.getDriver().getId());
				   	  	Driver drviver = genericDAO.getByCriteria(Driver.class, criterias);
				   	  	
				   	  	odometer.setDriver(drviver);
				   	  	
				   	  	odometer.setDriverCompany(drviver.getCompany());
				   	  	
				   	  	odometer.setTerminal(drviver.getTerminal());
				   	  	
				   	  	odometer.setTerminalName(drviver.getTerminal().getName());
				   	  	
				   	  	odometer.setCompanyName(drviver.getCompany().getName());
				   	  	
				   	  	odometer.setTempDriverName(drviver.getFullName());
				   	  	
				   	 if(!StringUtils.isEmpty(request.getParameter("startReading"+i)) && !StringUtils.isEmpty(request.getParameter("endReading"+i))){
				   		 odometer.setMiles(odometer.getEndReading()-odometer.getStartReading());
				   	 }
				   	  		
						genericDAO.saveOrUpdate(odometer);
						
			    	}
			    }
				
				cleanUp(request);			
				request.getSession().setAttribute("msg","Odometer readings added successfully");
				return "redirect:create.do";
			}
		 
		 
		 @Override
			protected String processAjaxRequest(HttpServletRequest request,
					String action, Model model) {
			 
			 
			 if ("calcMiles".equalsIgnoreCase(action)) {	
				 
			 }
			 
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
				
				if ("verifytruckandreading".equalsIgnoreCase(action)) {	

					try {
						
						String query = "select obj from Odometer obj where 1=1 and obj.tempTruck='"+request.getParameter("truck")+"' and obj.startReading<="+Integer.parseInt(request.getParameter("startreading"))+" and obj.endReading >"+Integer.parseInt(request.getParameter("startreading"));
						
						List<Odometer> odolist = genericDAO.executeSimpleQuery(query);
						
						if(odolist!=null && odolist.size()>0){
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
				
				if("allDaysOfWeek".equalsIgnoreCase(action)){
					try {
						List<String> list = new ArrayList<String>();
						SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
						SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
					
					String batchdate=dateFormat1.format(sdf.parse(request.getParameter("batchDate")));				
					
				
					LocalDate now = new LocalDate(batchdate);
					LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
					list.add(sdf.format(monday.toDate()));			
					LocalDate tuesday = now.withDayOfWeek(DateTimeConstants.TUESDAY);
					list.add(sdf.format(tuesday.toDate()));	
					LocalDate wednesday = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
					list.add(sdf.format(wednesday.toDate()));	
					LocalDate thursday = now.withDayOfWeek(DateTimeConstants.THURSDAY);
					list.add(sdf.format(thursday.toDate()));	
					LocalDate friday = now.withDayOfWeek(DateTimeConstants.FRIDAY);
					list.add(sdf.format(friday.toDate()));	
					LocalDate saturday = now.withDayOfWeek(DateTimeConstants.SATURDAY);
					list.add(sdf.format(saturday.toDate()));
					LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
					list.add(sdf.format(sunday.toDate()));
					Gson gson = new Gson();
					return gson.toJson(list);
					} catch (ParseException e) {				
						return "";
					}
				}
				
				
				return "";
			}

}
