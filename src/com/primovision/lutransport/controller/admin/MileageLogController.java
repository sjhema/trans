package com.primovision.lutransport.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.MileageLog;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;

@Controller
@RequestMapping("/operator/mileagelog")
public class MileageLogController extends CRUDController<MileageLog> {
	private static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat mileageSearchDateFormat = new SimpleDateFormat("MMMMM yyyy");
	
	public MileageLogController() {
		setUrlContext("operator/mileagelog");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy");
	   dateFormat.setLenient(false);
	   
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 1);
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		populateSearchCriteria(request, request.getParameterMap());
	}

	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");			
		
		String company = (String) criteria.getSearchMap().get("company.id");
		String state = (String) criteria.getSearchMap().get("state.id");
		String truck = (String) criteria.getSearchMap().get("unit.id");
		String periodFrom = (String) criteria.getSearchMap().get("periodFrom");
		String periodTo = (String) criteria.getSearchMap().get("periodTo");
		
		StringBuffer query = new StringBuffer("select obj from MileageLog obj  where 1=1");
		StringBuffer countquery = new StringBuffer("select count(obj) from MileageLog obj where 1=1");
		
		if (!StringUtils.isEmpty(company)) {
			query.append(" and obj.company=" + company);
			countquery.append(" and obj.company=" + company);
		}
		
		if (!StringUtils.isEmpty(state)) {
			query.append(" and obj.state=" + state);
			countquery.append(" and obj.state=" + state);
		}
		
		if (!StringUtils.isEmpty(truck)) {
			query.append(" and obj.unit=" + truck);
			countquery.append(" and obj.unit=" + truck);
		}
        
      if (!StringUtils.isEmpty(periodFrom)) {
        	try {
				query.append(" and obj.period  >='"+dbDateFormat.format(mileageSearchDateFormat.parse(periodFrom))+"'");
				countquery.append(" and obj.period  >='"+dbDateFormat.format(mileageSearchDateFormat.parse(periodFrom))+"'");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
      if (!StringUtils.isEmpty(periodTo)) {
        	try {
				query.append(" and obj.period  <='"+dbDateFormat.format(mileageSearchDateFormat.parse(periodTo))+"'");
				countquery.append(" and obj.period  <='"+dbDateFormat.format(mileageSearchDateFormat.parse(periodTo))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
      }

     query.append(" order by obj.period desc");
     countquery.append(" order by obj.period desc");
		
     Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
        		countquery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());
		
		model.addAttribute("list", genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		/*SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria));
		
		return urlContext + "/list";*/
		
		return search2(model, request);
	}

	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		return urlContext + "/form";
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") MileageLog entity,
			BindingResult bindingResult, ModelMap model) {
		if (entity.getState() == null) {
			bindingResult.rejectValue("state", "error.select.option", null, null);
		}
		
		if (entity.getCompany() == null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		
		if (entity.getUnit() == null) {
			bindingResult.rejectValue("unit", "error.select.option", null, null);
		} else {
			Map crieiria = new HashMap();
			crieiria.put("id", entity.getUnit().getId());
		   Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, crieiria);
		   if (vehicle != null) {
		   	entity.setUnitNum(vehicle.getUnitNum());
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
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		if(!StringUtils.isEmpty(request.getParameter("id"))){
			request.getSession().setAttribute("msg", "Mileage updated successfully");
			return "redirect:list.do";
		}
		else{			
			request.getSession().setAttribute("msg", "Mileage added successfully");
			return "redirect:create.do";
		}
	}
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request, String action, Model model) {
		return "";
	}
}
