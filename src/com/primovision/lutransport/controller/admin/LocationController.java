package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.Ticket;

@Controller
@RequestMapping("/admin/location")
public class LocationController extends CRUDController<Location>{
		
	public LocationController(){
		setUrlContext("admin/location");
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("dataType", "LOCATION_TYPE");
		model.addAttribute("locationTypes", genericDAO.findByCriteria(StaticData.class, map, "dataText", false));
		Map criterias = new HashMap();
		model.addAttribute("locations", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("dataType", "STATUS");
		map1.put("dataValue", "0,1");
		model.addAttribute("locationstatus", genericDAO.findByCriteria(StaticData.class, map1,"dataText",false));

	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Location entity,
			BindingResult bindingResult, ModelMap model) {
			System.out.println("\nsave()1---of ...locationController\n");
		String ret = checkforduplicacy(model, request, entity);
			if (ret != "") {
				return ret;
			}
			if(entity.getType()==null){
				bindingResult.rejectValue("type", "error.select.option", null, null);
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
			Location entity) {
		if (!entity.getName().equals(""))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", entity.getName());
			map.put("type", entity.getType());
			
			if (!(genericDAO.isUnique(Location.class, entity, map))) 
			{
				request.getSession().setAttribute("errors","Cannot create duplicate entry ");
				model.addAttribute("msg","Cannot create duplicate entry with same name and same Station type");
				setupCreate(model, request);
				return urlContext + "/form";
			}
		}
		return "";
	}

}
