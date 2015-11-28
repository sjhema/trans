package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.TripSheetLocation;
import com.primovision.lutransport.model.Vehicle;


@Controller
@RequestMapping("/admin/tripsheetlocation")
public class TripSheetLocationController extends CRUDController<TripSheetLocation>{

	public TripSheetLocationController(){
		setUrlContext("admin/tripsheetlocation");
	}
	
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {		
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));		
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
	}
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("dataType", "LOCATION_TYPE");
		map.put("dataValue", "1,2");
		model.addAttribute("locationTypes", genericDAO.findByCriteria(StaticData.class, map, "dataText", false));
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();		
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("locations", genericDAO.findByCriteria(TripSheetLocation.class, criterias,"name",false));
		if(request.getParameter("id")!=null){
			criterias.clear();		
			model.addAttribute("locationNames", genericDAO.executeSimpleQuery("select obj from Location obj where 1=1 and obj.type in (1,2) order by obj.name"));
		}
		else{
			criterias.clear();		
			model.addAttribute("locationNames", genericDAO.executeSimpleQuery("select obj from Location obj where 1=1 and obj.status=1 and obj.type in (1,2) order by obj.name"));
		}
		
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		model.addAttribute("locationNames", genericDAO.executeSimpleQuery("select obj from Location obj where 1=1 and obj.type in (1,2) order by obj.name"));
		
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") TripSheetLocation entity,
			BindingResult bindingResult, ModelMap model) {
			
		String ret = checkforduplicacy(model, request, entity);
			if (ret != "") {
				return ret;
			}
			if(entity.getType()==null){
				bindingResult.rejectValue("type", "error.select.option", null, null);
			}
			
			if(entity.getDriverCompany()==null){
				bindingResult.rejectValue("driverCompany", "error.select.option", null, null);
			}
			
			if(entity.getTerminal()==null){
				bindingResult.rejectValue("terminal", "error.select.option", null, null);
			}
			
			
		return super.save(request, entity, bindingResult, model);
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,null,null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"name",null,null));
		return urlContext + "/list";
	}

	
	public String checkforduplicacy(ModelMap model, HttpServletRequest request,
			TripSheetLocation entity) {
		if (!entity.getName().equals(""))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", entity.getName());
			map.put("type", entity.getType());
			map.put("driverCompany",entity.getDriverCompany().getId());
			map.put("terminal", entity.getTerminal().getId());
			
			if (!(genericDAO.isUnique(TripSheetLocation.class, entity, map))) 
			{
				request.getSession().setAttribute("errors","Cannot create duplicate entry ");
				model.addAttribute("error","Cannot create duplicate entry with same name and same type");
				setupCreate(model, request);
				return urlContext + "/form";
			}
		}
		return "";
	}

	
	
}
