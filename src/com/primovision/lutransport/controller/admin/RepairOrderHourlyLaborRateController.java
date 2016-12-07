package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.core.util.ReportDateUtil;

import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderHourlyLaborRate;

@Controller
@RequestMapping("/admin/vehiclemaint/repairorders/hourlylaborrate")
public class RepairOrderHourlyLaborRateController extends CRUDController<RepairOrderHourlyLaborRate> {
	public RepairOrderHourlyLaborRateController() {
		setUrlContext("admin/vehiclemaint/repairorders/hourlylaborrate");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<RepairOrderHourlyLaborRate> repairOrderHourlyLaborRateList = retrieveData(criteria);
		model.addAttribute("list", repairOrderHourlyLaborRateList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<RepairOrderHourlyLaborRate> repairOrderHourlyLaborRateList = retrieveData(criteria);
		model.addAttribute("list", repairOrderHourlyLaborRateList);
		
		return urlContext + "/list";
	}
	
	private List<RepairOrderHourlyLaborRate> retrieveData(SearchCriteria criteria) {
		String effectiveStartDate = StringUtils.EMPTY;
		String effectiveEndDate = StringUtils.EMPTY;
		
		Map searchMap = criteria.getSearchMap();
		if (searchMap.get("effectiveStartDate") != null) {
			effectiveStartDate = (String) searchMap.get("effectiveStartDate");
		}
		if (searchMap.get("effectiveEndDate") != null) {
			effectiveEndDate = (String) searchMap.get("effectiveEndDate");
		}
		
		effectiveStartDate = ReportDateUtil.getFromDate(effectiveStartDate);
		effectiveEndDate = ReportDateUtil.getToDate(effectiveEndDate);
		
		StringBuffer query = new StringBuffer("select obj from RepairOrderHourlyLaborRate obj ");
		if (StringUtils.isNotEmpty(effectiveStartDate) && StringUtils.isNotEmpty(effectiveEndDate)) {
			query.append(" where obj.effectiveStartDate >= '" + effectiveStartDate+ "'" 
					+ " and obj.effectiveEndDate <='" + effectiveEndDate + "'");
		} else if (StringUtils.isNotEmpty(effectiveStartDate)) {
			query.append(" where obj.effectiveStartDate = '" + effectiveStartDate + "'");
		} else if (StringUtils.isNotEmpty(effectiveEndDate)) {
			query.append(" where obj.effectiveEndDate = '" + effectiveEndDate + "'");
		}
		query.append(" order by obj.effectiveEndDate desc");
		
		List<RepairOrderHourlyLaborRate> repairOrderHourlyLaborRateList = genericDAO.executeSimpleQuery(query.toString());
		return repairOrderHourlyLaborRateList;
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
	}

	private void validateSave(RepairOrderHourlyLaborRate entity, BindingResult bindingResult) {
		if (entity.getLaborRate() == null) {
			bindingResult.rejectValue("laborRate", "NotNull.java.lang.Double", null, null);
		}
		if (entity.getEffectiveStartDate() == null) {
			bindingResult.rejectValue("effectiveStartDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getEffectiveEndDate() == null) {
			bindingResult.rejectValue("effectiveEndDate", "NotNull.java.util.Date", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") RepairOrderHourlyLaborRate entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext()+"/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Repair Order Hourly labor rate saved successfully");
		
		setupCreate(model, request);
		return getUrlContext() + "/form";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		return urlContext + "/form";
	}
}
