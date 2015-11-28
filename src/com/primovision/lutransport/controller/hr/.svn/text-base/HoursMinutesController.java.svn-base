package com.primovision.lutransport.controller.hr;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IntegerField;
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
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.Attendance;
import com.primovision.lutransport.model.hr.Eligibility;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HoursMinutes;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hr.ShiftCalendar;
import com.primovision.lutransport.model.SearchCriteria;
/*import com.primovision.lutransport.model.State;*/
import com.primovision.lutransport.model.StaticData;
/*import com.primovision.lutransport.model.Vehicle;*/

/**
 * @author Subodh
 */

@Controller
@RequestMapping("/hr/hoursminutes")
public class HoursMinutesController extends CRUDController<HoursMinutes> {

	public HoursMinutesController() {
		setUrlContext("/hr/hoursminutes");
	}
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void initBinder(WebDataBinder binder) {
		// SimpleDateFormat("yyyy-MM-dd")
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class, false));
	}

	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		}

	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"hoursformat",null,null));
		return urlContext + "/list";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
	    populateSearchCriteria(request, request.getParameterMap());
	    setupCreate(model, request);
		
	}

	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") HoursMinutes entity,
			BindingResult bindingResult, ModelMap model) 
	    {
		
		
		System.out.println("\nbefor try...Attendance controller\n");
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			bindingResult.getFieldError();
		System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			return urlContext + "/form";
		}
		beforeSave(request, entity, model);
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
	
		
		return "redirect:/" + urlContext + "/list.do";
		
		
		
	}
        
	
	
	
}
