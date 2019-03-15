package com.primovision.lutransport.controller.admin;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
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
	
	@RequestMapping("/bulkedit.do")
	public String bulkEdit(ModelMap model, HttpServletRequest request, @RequestParam("id") String[] ids) {
		if (ids.length <= 0) {
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error", "Please select fuel cards to bulk edit");
			return "redirect:list.do";
		}
		request.getSession().setAttribute("bulkeditids", ids);
		
		setupCreate(model, request);
		
		FuelCard fuelCard = getEntityInstance();
		model.addAttribute("modelObject", fuelCard);		
		return urlContext + "/massUpdateForm";
	}
	
	@RequestMapping("/cancelbulkedit.do")
	public String cancelBulkEdit(ModelMap model, HttpServletRequest request){
		request.getSession().removeAttribute("bulkeditids");				
		return "redirect:list.do";
	}
	
	@RequestMapping(value="/updatebulkeditdata.do", method=RequestMethod.POST)
	public String updateBulklyEditedFuelCards(HttpServletRequest request,
			@ModelAttribute("modelObject") FuelCard entity,
			BindingResult bindingResult, ModelMap model) {
		String[] ids = (String[])request.getSession().getAttribute("bulkeditids");
		if (ids.length <= 0) {
			cleanUp(request);
			
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error", "No fuel cards selected to bulk edit");
			return "redirect:list.do";
		}
		
		StringBuffer fuelCardUpdateQuery = new StringBuffer("update FuelCard set ");
		boolean doNotAddComma = true;
		
		Date validTo = entity.getValidTo();
		if (validTo != null){
			if (doNotAddComma){
				doNotAddComma = false;
			} else {
				fuelCardUpdateQuery.append(", ");
			}
			
			fuelCardUpdateQuery.append("validTo='").append(ReportDateUtil.oracleFormatter.format(validTo)).append("'");
		}
		
		Integer status = entity.getStatus();
		if (status != null && status != -1) {
			if (doNotAddComma){
				doNotAddComma = false;
			} else {
				fuelCardUpdateQuery.append(", ");
			}
			
			fuelCardUpdateQuery.append("status = " + status.intValue());
		}
		
		if (doNotAddComma) {
			cleanUp(request);
			
			request.getSession().removeAttribute("bulkeditids");
			request.getSession().setAttribute("error", "No data entered to do bulk edit");
			return "redirect:list.do";
		}
		
		Long userId = getUser(request).getId();
		fuelCardUpdateQuery.append(", modifiedBy = " + userId.intValue());
		
		Date currentDate = new Date();
		fuelCardUpdateQuery.append(", modifiedAt='").append(ReportDateUtil.oracleFormatter.format(currentDate)).append("'");
		
		String commaSeparatedIds = StringUtils.EMPTY;
		for (String aDriverFuelCardId : ids) {
			commaSeparatedIds += (aDriverFuelCardId + ",");
		}
		commaSeparatedIds = commaSeparatedIds.substring(0, commaSeparatedIds.length()-1);
		
		fuelCardUpdateQuery.append(" where id in (").append(commaSeparatedIds).append(")");
		
		genericDAO.executeSimpleUpdateQuery(fuelCardUpdateQuery.toString());
		
		cleanUp(request);
		
		request.getSession().removeAttribute("bulkeditids");
		String successMsg = "Fuel cards updated Successfully";
		request.getSession().setAttribute("msg", successMsg);
		
		return "redirect:list.do";		
	}
}
