package com.primovision.lutransport.controller.admin;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.service.DateUpdateService;



@Controller
@RequestMapping("/admin/fuelcard")
public class FuelCardController extends CRUDController<FuelCard>{
	
	
	public FuelCardController(){
		setUrlContext("admin/fuelcard");
	}
	
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
		binder.registerCustomEditor(FuelVendor.class, new AbstractModelEditor(
				FuelVendor.class));
	}
	
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("fuelvendor", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		String query="select obj from FuelCard obj order by ABS(fuelcardNum)";
        model.addAttribute("fuelcard",genericDAO.executeSimpleQuery(query));
		//model.addAttribute("fuelcard",genericDAO.findByCriteria(FuelCard.class,criterias,"fuelcardNum",false));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("fuelcardstatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}

	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"ABS(fuelcardNum)",false));
		return urlContext + "/list";
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"ABS(fuelcardNum)",false));
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") FuelCard entity,
			BindingResult bindingResult, ModelMap model) {	
		if (entity.getFuelvendor() == null) {
			bindingResult.rejectValue("fuelvendor", "error.select.option",
					null, null);
		}
		else{
		Map properties=new HashMap();
		//System.out.println("************* the fuekl vendor os"+entity.getFuelvendor());
		properties.put("fuelvendor",entity.getFuelvendor().getId());
		properties.put("fuelcardNum",entity.getFuelcardNum());
			if(!genericDAO.isUnique(FuelCard.class, entity ,properties)){
				bindingResult.rejectValue("fuelcardNum", "error.already.exist", null, null);
			}
		}
		return super.save(request, entity, bindingResult, model);
	}
	

}
