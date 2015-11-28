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
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.Eligibility;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
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
@RequestMapping("/hr/shiftcalendar")
public class ShiftCalendarController extends CRUDController<ShiftCalendar> {

	public ShiftCalendarController() {
		setUrlContext("/hr/shiftcalendar");
	}

	
	@Override
	public void initBinder(WebDataBinder binder) {
		// SimpleDateFormat("yyyy-MM-dd")
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class, false));
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		
	}

	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("dataType", "STATUS");
		criterias.put("dataValue", "0,1");
		model.addAttribute("status", genericDAO.findByCriteria(StaticData.class, criterias,"dataText",false));
		criterias.clear();
		model.addAttribute("shiftnames",genericDAO.findByCriteria(ShiftCalendar.class, criterias, "name", false));
		}

	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		//System.out.println("\nlist1\n");
		 SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// System.out.println("\nlist2\n");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"name",null,null));
		 //System.out.println("\nlist3\n");
		return urlContext + "/list";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
	    populateSearchCriteria(request, request.getParameterMap());
	    setupCreate(model, request);
		
	}

	
	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") ShiftCalendar entity,
			BindingResult bindingResult, ModelMap model) 
	    {
		
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		
		
		if (entity.getEffectivestartdate()!=null && entity.getEffectiveenddate()!=null) {
			if (entity.getEffectiveenddate().before(entity.getEffectivestartdate())) {
				bindingResult.rejectValue("effectivestartdate", "error.textbox.StartEndDate",null, null);
			}
		}
		
		
		if(entity.getStarttimehours()!=null && entity.getStarttimeminutes()!=null){
			String s1=entity.getStarttimehours();
			String s2=entity.getStarttimeminutes();
			String s3=s1+":"+s2;
			entity.setStarttime(s3);
		}
		if(entity.getEndtimehours()!=null && entity.getEndtimeminutes()!=null){
			String s1=entity.getEndtimehours();
			String s2=entity.getEndtimeminutes();
			String s3=s1+":"+s2;
			entity.setEndtime(s3);
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
			bindingResult.getFieldError();
			//System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			return urlContext + "/form";
		}
		beforeSave(request, entity, model);
		// merge into datasource
		//System.out.println("\nsave\n");
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		
		return "redirect:/" + urlContext + "/list.do";
		
		
		//
		
      // return super.save(request, entity, bindingResult, model);
	}
        
		
	
	
	
}
