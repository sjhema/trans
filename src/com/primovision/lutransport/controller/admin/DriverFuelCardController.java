package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.ReportDateUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.Vehicle;

@Controller
@RequestMapping("/admin/driverfuelcard")
public class DriverFuelCardController extends CRUDController<DriverFuelCard>{
	
	
	public DriverFuelCardController(){
		setUrlContext("/admin/driverfuelcard");
	}
	
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(
				Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(
				Vehicle.class));
		binder.registerCustomEditor(FuelCard.class, new AbstractModelEditor(
				FuelCard.class));
		binder.registerCustomEditor(FuelVendor.class, new AbstractModelEditor(
				FuelVendor.class));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
	}

	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();	
		model.addAttribute("fuelvendor", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		//model.addAttribute("fuelcard",genericDAO.findByCriteria(FuelCard.class,criterias,"fuelcardNum",false));
		model.addAttribute("fuelcard",genericDAO.executeSimpleQuery("select fc from FuelCard fc order by ABS(fc.fuelcardNum)"));
		//criterias.put("status", 1);
		criterias.clear();
		//EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		//criterias.put("catagory", empobj);
		if(request.getParameter("id")!=null){
			criterias.clear();
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		}
		else{
			criterias.put("status", 1);
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		}
		
		// select obj from Vehicle obj where obj.type=1 group by obj.unit
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
		
		criterias.clear();
		model.addAttribute("searchdriver", genericDAO.executeSimpleQuery("select obj from Driver obj group by obj.fullName"));

		criterias.clear();
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
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		DriverFuelCard driverfuelcard=genericDAO.getById(DriverFuelCard.class,Long.parseLong(request.getParameter("id")));
		Map criteria = new HashMap();
		criteria.put("fuelvendor.id",driverfuelcard.getFuelvendor().getId());
		model.addAttribute("fuelcard",genericDAO.findByCriteria(FuelCard.class,criteria,"fuelcardNum",false));
		return urlContext + "/form";
	}
	
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<DriverFuelCard> driverFuelCardList = performSearch(criteria); 
		model.addAttribute("list", driverFuelCardList);
		
		return urlContext + "/list";
	}
	
	private List<DriverFuelCard> performSearch(SearchCriteria criteria) {
		/*String order = "driver.lastName";
		if (!StringUtils.isEmpty(request.getParameter("vehicle.unit"))) {
			order = "vehicle.unit";
		}
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria,order,false));*/
		
		List<DriverFuelCard> driverFuelCardList = genericDAO.search(getEntityClass(), criteria, null, false); 
		if (driverFuelCardList == null || driverFuelCardList.isEmpty()) {
			return driverFuelCardList;
		}
		
		Collections.sort(driverFuelCardList, new Comparator<DriverFuelCard>() {
			@Override
			public int compare(final DriverFuelCard record1, final DriverFuelCard record2) {
				String driverLastName1 = (record1.getDriver() == null || StringUtils.isEmpty(record1.getDriver().getLastName()))
						? StringUtils.EMPTY : record1.getDriver().getLastName();
				String driverLastName2 =(record2.getDriver() == null || StringUtils.isEmpty(record2.getDriver().getLastName()))
						? StringUtils.EMPTY : record2.getDriver().getLastName();
				Integer unit1 = (record1.getVehicle() == null) ? null : record1.getVehicle().getUnit();
				Integer unit2 = (record2.getVehicle() == null) ? null : record2.getVehicle().getUnit();
				
				int c = driverLastName1.compareTo(driverLastName2);
				if (c == 0) {
					c = ((unit1 == null) ?
					         (unit2 == null ? 0 : -1):
					         (unit2 == null ? 1 : unit1.compareTo(unit2)));
				}
				
				return c;
			}
		});
		
		return driverFuelCardList;
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		
		List<DriverFuelCard> driverFuelCardList = performSearch(criteria); 
		model.addAttribute("list", driverFuelCardList);
		
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") DriverFuelCard entity,
			BindingResult bindingResult, ModelMap model) {	
		/*if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option",
					null, null);
		}*/
		if (entity.getFuelvendor() == null) {
			bindingResult.rejectValue("fuelvendor", "error.select.option",
					null, null);
		}		
		if (entity.getFuelcard() == null) {
			bindingResult.rejectValue("fuelcard", "error.select.option",
					null, null);			
		}
		/*else{
			if (entity.getFuelvendor() == null) {
				
			}
			else
			{				
				Map criterias = new HashMap();
				criterias.clear();
				criterias.put("fuelvendor.id",entity.getFuelvendor().getId());
				criterias.put("id",entity.getFuelcard().getId());
				List<FuelCard> fuelcards=genericDAO.findByCriteria(FuelCard.class, criterias, "fuelvendor", false);
				if(fuelcards.isEmpty() || fuelcards.size()==0){
					bindingResult.rejectValue("fuelcard", "error.select.mismatch",
							null, null);
				}				
			}
			
		}*/
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			if (entity.getFuelcard() == null) {			
				if (entity.getFuelvendor() != null) {
				Map criteria = new HashMap();
				criteria.put("fuelvendor.id",Long.parseLong(request.getParameter("fuelvendor")));
				model.addAttribute("fuelcard",genericDAO.findByCriteria(FuelCard.class,criteria,"fuelcardNum",false));
				}
			}
			
			return urlContext + "/form";
		}
		
		 Map prop=new HashMap();
    	 prop.put("fuelvendor",entity.getFuelvendor().getId() );
    	 
    	 if (entity.getDriver() != null) {
    		 prop.put("driver",entity.getDriver().getId() );
    	 } else {
   		 prop.put("vehicle",entity.getVehicle().getId() );
   	 }
    	 
    	 
    	 prop.put("fuelcard",entity.getFuelcard().getId() );
    	  boolean rst=genericDAO.isUnique(DriverFuelCard.class,entity, prop);      	  
    	  if(!rst){
    		  setupCreate(model, request);    
    		  Map criteria = new HashMap();
				criteria.put("fuelvendor.id",Long.parseLong(request.getParameter("fuelvendor")));
				model.addAttribute("fuelcard",genericDAO.findByCriteria(FuelCard.class,criteria,"fuelcardNum",false));
				
				String errorMsg = (entity.getDriver() != null) ? "Driver": "Vehicle";
    		  request.getSession().setAttribute("error","Duplicate Entry! Same Card has already been assigned to this " + errorMsg);
    		  return urlContext + "/form";
    	  }
    	  
    
    	String driverFuelCardQuery = "select obj from DriverFuelCard obj where status=1";
    	driverFuelCardQuery += " and fuelcard.id=" + entity.getFuelcard().getId();
    	driverFuelCardQuery += " and fuelvendor.id=" + entity.getFuelvendor().getId();
   	if (entity.getDriver() != null) {
   		 driverFuelCardQuery += " and vehicle is not null";
   	} else {
   		 driverFuelCardQuery += " and driver is not null";
   	}
  		List<DriverFuelCard> driverFuelCardList = genericDAO.executeSimpleQuery(driverFuelCardQuery);
  		if (driverFuelCardList != null && !driverFuelCardList.isEmpty()) {
  			setupCreate(model, request);    
  			Map criteria = new HashMap();
			criteria.put("fuelvendor.id",Long.parseLong(request.getParameter("fuelvendor")));
			model.addAttribute("fuelcard",genericDAO.findByCriteria(FuelCard.class,criteria,"fuelcardNum",false));
			
			String errorMsg = (entity.getDriver() != null) ? "Vehicle": "Driver";
			request.getSession().setAttribute("error", "Duplicate Entry! Same Card has already been assigned to a " + errorMsg);
		  return urlContext + "/form";
  		}
  		
		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";
		
		//return super.save(request, entity, bindingResult, model);
	}
	
	@RequestMapping("/bulkedit.do")
	public String bulkEdit(ModelMap model, HttpServletRequest request,@RequestParam("id") String[] ids) {
		if (ids.length <= 0) {
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error", "Please select driver fuel cards to bulk edit");
			return "redirect:list.do";
		}
		request.getSession().setAttribute("bulkeditids", ids);
		
		setupCreate(model, request);
		
		DriverFuelCard driverFuelCard = getEntityInstance();
		model.addAttribute("modelObject", driverFuelCard);		
		return urlContext + "/massUpdateForm";
	}
	
	@RequestMapping("/cancelbulkedit.do")
	public String cancelBulkEdit(ModelMap model, HttpServletRequest request){
		request.getSession().removeAttribute("bulkeditids");				
		return "redirect:list.do";
	}
	
	@RequestMapping(value="/updatebulkeditdata.do", method=RequestMethod.POST)
	public String updateBulklyEditedFuelCards(HttpServletRequest request,
			@ModelAttribute("modelObject") DriverFuelCard entity,
			BindingResult bindingResult, ModelMap model) {
		String[] ids = (String[])request.getSession().getAttribute("bulkeditids");
		if (ids.length <= 0) {
			cleanUp(request);
			
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error", "No driver fuel cards selected to bulk edit");
			return "redirect:list.do";
		}
		
		StringBuffer driverFuelCardUpdateQuery = new StringBuffer("update DriverFuelCard set ");
		boolean doNotAddComma = true;
		
		Date validTo = entity.getValidTo();
		if (validTo != null){
			if (doNotAddComma){
				doNotAddComma = false;
			} else {
				driverFuelCardUpdateQuery.append(", ");
			}
			
			driverFuelCardUpdateQuery.append("validTo='").append(ReportDateUtil.oracleFormatter.format(validTo)).append("'");
		}
		
		Integer status = entity.getStatus();
		if (status != null && status != -1) {
			if (doNotAddComma){
				doNotAddComma = false;
			} else {
				driverFuelCardUpdateQuery.append(", ");
			}
			
			driverFuelCardUpdateQuery.append("status = " + status.intValue());
		}
		
		if (doNotAddComma) {
			cleanUp(request);
			
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error", "No data entered to do bulk edit");
			return "redirect:list.do";
		}
		
		Long userId = getUser(request).getId();
		driverFuelCardUpdateQuery.append(", modifiedBy = " + userId.intValue());
		
		Date currentDate = new Date();
		driverFuelCardUpdateQuery.append(", modifiedAt='").append(ReportDateUtil.oracleFormatter.format(currentDate)).append("'");
		
		String commaSeparatedIds = StringUtils.EMPTY;
		for (String aDriverFuelCardId : ids) {
			commaSeparatedIds += (aDriverFuelCardId + ",");
		}
		commaSeparatedIds = commaSeparatedIds.substring(0, commaSeparatedIds.length()-1);
		
		driverFuelCardUpdateQuery.append(" where id in (").append(commaSeparatedIds).append(")");
		
		genericDAO.executeSimpleUpdateQuery(driverFuelCardUpdateQuery.toString());
		
		cleanUp(request);
		
		request.getSession().removeAttribute("bulkeditids");
		String successMsg = "Driver fuel cards updated Successfully";
		request.getSession().setAttribute("msg", successMsg);
		
		return "redirect:list.do";		
	}
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if("findFuelCard".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("fuelvendor"))){					
				Map criterias = new HashMap();
				//criterias.put("fuelvendor.id", Long.parseLong(request.getParameter("fuelvendor")));
				//List<FuelCard> fuelcards=genericDAO.findByCriteria(FuelCard.class, criterias, "fuelvendor", false);
				long fid=Long.parseLong(request.getParameter("fuelvendor"));
				List<FuelCard> fuelcards=genericDAO.executeSimpleQuery("select fc from FuelCard fc where fc.fuelvendor.id="+fid+" order by ABS(fc.fuelcardNum)");
				Gson gson = new Gson();
				return gson.toJson(fuelcards);
			}
		}
		
		return "";
	}
}
