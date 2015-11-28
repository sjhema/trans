package com.primovision.lutransport.controller.driver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.DriverReportLeave;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/driver/reportleave")
public class DriverReportLeaveController extends CRUDController<DriverReportLeave>{

public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
	public DriverReportLeaveController() {
		setUrlContext("driver/reportleave");
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
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") DriverReportLeave entity,
			BindingResult bindingResult, ModelMap model) {
		
		User userObj = (User) request.getSession().getAttribute("userInfo");
		
		Map criterias = new HashMap();		
				
		criterias.clear();
		criterias.put("firstName",userObj.getFirstName());
		criterias.put("lastName", userObj.getLastName());
		criterias.put("status",1);
		Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		
		
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
		
		
		String mobileEntryTableUpdateQuery = "update DriverMobileEntry d set d.tripsheet_flag='UL', d.fuellog_flag='UL', d.odometer_flag='UL' where d.employeeName in ('"+driver.getFullName()+"') and d.entryDate>='"+mysqldf.format(entity.getFromDate())+"' and d.entryDate<='"+mysqldf.format(entity.getToDate())+"'";
		genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery);
		
		
		request.getSession().setAttribute("msg","Reported Leave Successfully");
		return "redirect:/driver/tripsheet/list.do?rst=1";
	}
	
	
}
