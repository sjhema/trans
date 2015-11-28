package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.hr.Eligibility;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HolidayType;
import com.primovision.lutransport.model.hr.LeaveType;

/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/holiday")
public class HolidayTypeController extends CRUDController<HolidayType>{
	public HolidayTypeController() {
	setUrlContext("/hr/holiday");
	}
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		model.addAttribute("paidstatus",listStaticData("YES_NO"));
		
		criterias.clear();
		model.addAttribute("holidaytypes",genericDAO.findByCriteria(HolidayType.class, criterias, "name", false));
		model.addAttribute("leavetypes",genericDAO.findByCriteria(LeaveType.class, criterias, "name", false));
	}
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
	    binder.registerCustomEditor(LeaveType.class, new AbstractModelEditor(LeaveType.class));
	}
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") HolidayType entity,
			BindingResult bindingResult, ModelMap model) {
		boolean err=true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		if(entity.getName()==null){
			
			err=false;
		}
		if(entity.getLeaveType()==null){
			bindingResult.rejectValue("leaveType", "error.select.option",
					null, null);
			err=false;
		}
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option",
					null, null);
			err=false;
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
			err=false;
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
					null, null);
			err=false;
		}
		
		if(entity.getCatagory()!=null){			
			if(entity.getCatagory().getId() != 3){
				
				if(entity.getRate()==null){
					bindingResult.rejectValue("rate", "error.select.option",
							null, null);
					err=false;
				}
				
				if(entity.getAmount()==null){
					bindingResult.rejectValue("amount", "error.select.option",
							null, null);
					err=false;
				}				
			}			
		}
		
		
		
		if(err){
			StringBuffer query=new StringBuffer("");
		   query.append("select obj from HolidayType obj where obj.company.id="+entity.getCompany().getId()+" and obj.terminal.id="
			+entity.getTerminal().getId()+" and obj.name='"+entity.getName()+"' and obj.catagory.id="+entity.getCatagory().getId()+"and obj.leaveType.id="+entity.getLeaveType().getId());
			if(entity.getDateFrom()!=null && entity.getDateTo()!=null && entity.getBatchdate()!=null){
				query.append(" and obj.dateFrom='"+sdf.format(entity.getDateFrom())+"' and obj.dateTo='"+sdf.format(entity.getDateFrom())+"' and obj.batchdate='"+sdf.format(entity.getBatchdate())+"'");
		  
			}
			if(entity.getId()!=null){
				query.append("and obj.id!="+entity.getId());
			}
		   
			List<Eligibility> list=genericDAO.executeSimpleQuery(query.toString());
			;
		
			if(list.size()>0){
				request.getSession().setAttribute("error","duplicate entry");
				setupCreate(model, request);
				return urlContext + "/form";
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
		
		
		return super.save(request, entity, bindingResult, model);
	}
	
}
