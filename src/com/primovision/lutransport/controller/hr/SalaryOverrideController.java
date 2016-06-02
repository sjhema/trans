package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ValidationException;

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
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.core.util.ReportDateUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.SalaryOverride;
import com.primovision.lutransport.model.hrreport.WeeklyPay;

@Controller
@RequestMapping("/hr/salaryoverride")
public class SalaryOverrideController extends CRUDController<SalaryOverride> {
	public SalaryOverrideController() {
		setUrlContext("/hr/salaryoverride");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));			
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		String query="select obj from Driver obj where obj.status=1 and obj.payTerm=3 order by fullName ASC";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("payrollBatch"))) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			try {
				Date billBatch = dateFormat.parse((String)criteria.getSearchMap().get("payrollBatch"));
				criteria.getSearchMap().put("payrollBatch", ReportDateUtil.oracleFormatter.format(billBatch));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "payrollBatch", true));
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "payrollBatch", true));
		return urlContext + "/list";
	}
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") SalaryOverride entity,
			BindingResult bindingResult, ModelMap model) {
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option",null, null);
		}
		
		String query = "select obj from Driver obj where obj.id=" + entity.getDriver().getId();
		List<Driver> driverList = genericDAO.executeSimpleQuery(query);
		entity.setCompany(driverList.get(0).getCompany());
		entity.setTerminal(driverList.get(0).getTerminal());
		
		if (entity.getPayrollBatch() == null) {
			bindingResult.rejectValue("payrollBatch", "NotNull.java.util.Date",null, null);
		}
		
		if (entity.getAmount() == null) {
			bindingResult.rejectValue("amount", "NotNull.java.lang.Double",null, null);
		}
			
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		if (isDuplicate(entity)) {
			setupCreate(model, request);
			request.getSession().setAttribute("error", "Salary override already exists for the batch.");
			return urlContext + "/form";
		}
			
		beforeSave(request, entity, model);
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		setupCreate(model, request);
		request.getSession().setAttribute("msg", "Record saved successfully");
		
		if (!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id") != null) {
			return "redirect:list.do";
		} else {
			return "redirect:create.do";
		}
	}

	private boolean isDuplicate(SalaryOverride entity) {
		if(entity.getId() != null){
			return false;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String payrollBatch = dateFormat.format(entity.getPayrollBatch());
		
		String checkForDuplicateQuery = "SELECT obj FROM SalaryOverride obj WHERE"
				+ " obj.driver=" + entity.getDriver().getId()
				+ " AND obj.payrollBatch='" + payrollBatch + "'";
		
		System.out.println(checkForDuplicateQuery);
		List<SalaryOverride> salaryOverrideList = genericDAO.executeSimpleQuery(checkForDuplicateQuery);
		
		if (salaryOverrideList != null && !salaryOverrideList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}

