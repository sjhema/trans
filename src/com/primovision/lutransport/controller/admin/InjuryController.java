package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.core.tags.StaticDataUtil;
import com.primovision.lutransport.core.util.InjuryUtils;
import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.injury.InjuryIncidentType;
import com.primovision.lutransport.model.injury.InjuryToType;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

@Controller
@RequestMapping("/admin/injury/injurymaint")
public class InjuryController extends CRUDController<Injury> {
	public InjuryController() {
		setUrlContext("admin/injury/injurymaint");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(InsuranceCompany.class, new AbstractModelEditor(InsuranceCompany.class));
		binder.registerCustomEditor(InsuranceCompanyRep.class, new AbstractModelEditor(InsuranceCompanyRep.class));
		binder.registerCustomEditor(InjuryIncidentType.class, new AbstractModelEditor(InjuryIncidentType.class));
		binder.registerCustomEditor(InjuryToType.class, new AbstractModelEditor(InjuryToType.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<Injury> injuryList = performSearch(criteria);
		model.addAttribute("list", injuryList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<Injury> injuryList = performSearch(criteria);
		model.addAttribute("list", injuryList);
		
		return urlContext + "/list";
	}
	
	private List<Injury> performSearch(SearchCriteria criteria) {
		String insuranceCompany = (String) criteria.getSearchMap().get("insuranceCompany");
		String driver = (String) criteria.getSearchMap().get("driver");
		
		String claimNumber = (String) criteria.getSearchMap().get("claimNumber");
		
		String incidentType = (String) criteria.getSearchMap().get("incidentType");
		String injuryStatus = (String) criteria.getSearchMap().get("injuryStatus");
		
		String incidentDateFrom = (String) criteria.getSearchMap().get("incidentDateFrom");
		String incidentDateTo = (String) criteria.getSearchMap().get("incidentDateTo");
		
		StringBuffer query = new StringBuffer("select obj from Injury obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Injury obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(insuranceCompany)) {
			whereClause.append(" and obj.insuranceCompany.id=" + insuranceCompany);
		}
		if (StringUtils.isNotEmpty(claimNumber)) {
			whereClause.append(" and obj.claimNumber='" + claimNumber + "'");
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(incidentDateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(incidentDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(incidentDateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(incidentDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(incidentType)) {
			whereClause.append(" and obj.incidentType.id=" + incidentType);
		}
      if (StringUtils.isNotEmpty(injuryStatus)) {
			whereClause.append(" and obj.injuryStatus=" + injuryStatus);
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.incidentDate desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Injury> injuryList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		//populateDriverDetails(injuryList);
		
		return injuryList;
	}
	
	private void populateDriverDetails(List<Injury> injuryList) {
		if (injuryList == null || injuryList.isEmpty()) {
			return;
		}
		
		for (Injury anInjury : injuryList) {
			if (anInjury.getReturnToWorkDate() != null) {
				anInjury.setWorking("Yes");
			} else {
				anInjury.setWorking("No");
			}
			
			Driver driver = anInjury.getDriver();
			if (driver.getStatus() == 1) {
				anInjury.setEmployed("Yes");
			} else {
				anInjury.setEmployed("No");
			}
		}
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("injuryToTypes", genericDAO.findByCriteria(InjuryToType.class, criterias, "injuryTo asc", false));

		List<InsuranceCompanyRep> claimReps = null;
		criterias.clear();
		if (request.getParameter("id") == null) {
			claimReps = genericDAO.findByCriteria(InsuranceCompanyRep.class, criterias, "name", false);
			criterias.put("status", 1);
		} else {
			Injury entity = (Injury) model.get("modelObject");
			if (entity != null && entity.getInsuranceCompany() != null) {
				claimReps = retrieveClaimReps(entity.getInsuranceCompany().getId());
			}
		}
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		model.addAttribute("claimReps", claimReps);
	
		/*String query = "select obj from Location obj "
				+ " where type in (1, 2)"
				+ " order by obj.name asc, obj.type asc";
		List<Location> locations = genericDAO.executeSimpleQuery(query);
		for (Location aLocation : locations) {
			String typeDescription = aLocation.getName() + " - ";
			typeDescription += StaticDataUtil.getText("LOCATION_TYPE", String.valueOf(aLocation.getType()));
			aLocation.setTypeDescription(typeDescription);
		}
		model.addAttribute("locations", locations);*/
	}
	
	public void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("insuranceCompanies", genericDAO.findByCriteria(InsuranceCompany.class, criterias, "name", false));
		model.addAttribute("incidentTypes", genericDAO.findByCriteria(InjuryIncidentType.class, criterias, "incidentType asc", false));
		
		criterias.clear();
		criterias.put("dataType", "INJURY_STATUS");
		model.addAttribute("statuses", genericDAO.findByCriteria(StaticData.class, criterias, "dataText", false));
		
	}


	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("injuries", genericDAO.findByCriteria(Injury.class, criterias, "claimNumber asc", false));
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Injury entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
	
		if (StringUtils.isEmpty(entity.getClaimNumber())) {
			entity.setClaimNumber(null);
		}
		setMoreDetails(entity);
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Injury details saved successfully");
		
		setupCreate(model, request);
		
		return getUrlContext() + "/form";
	}
	
	private Driver retrieveDriver(Long driverId) {
		return genericDAO.getById(Driver.class, driverId);
	}
	
	private void setMoreDetails(Injury entity) {
		setDriverDetails(entity);
		
		setDayOfWeek(entity);
		setIncidentTimeAMPM(entity);
	}
	
	private void setDriverDetails(Injury entity) {
		Long driverId = entity.getDriver().getId();
		Driver driver = retrieveDriver(driverId);
		
		entity.setDriverCompany(driver.getCompany());
		entity.setDriverTerminal(driver.getTerminal());
		entity.setDriverCategory(driver.getCatagory());
	}
	
	private void setDayOfWeek(Injury entity) {
		String dayOfWeek = InjuryUtils.deriveDayOfWeek(entity.getIncidentDate());
		entity.setIncidentDayOfWeek(dayOfWeek);
	}
	
	private void setIncidentTimeAMPM(Injury entity) {
		String incidentTime = entity.getIncidentTime();
		if (StringUtils.isEmpty(incidentTime)) {
			return;
		}
		
		String amPm = StringUtils.EMPTY;
		String hourStr = StringUtils.substringBefore(incidentTime, ":");
		int hour = Integer.valueOf(hourStr).intValue();
		if (hour >= 12) {
			amPm = "PM";
		} else {
			amPm = "AM";
		}
		entity.setIncidentTimeAMPM(amPm);
	}
	
	private void validateSave(Injury entity, BindingResult bindingResult) {
		if (entity.getIncidentDate() == null) {
			bindingResult.rejectValue("incidentDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		if (entity.getIncidentType() == null) {
			bindingResult.rejectValue("incidentType", "error.select.option", null, null);
		}
		
		Integer injuryStatus = entity.getInjuryStatus();
		if (injuryStatus == null) {
			bindingResult.rejectValue("injuryStatus", "error.select.option", null, null);
		} else {
			if (injuryStatus.intValue() != Injury.INJURY_STATUS_NOT_REPORTED.intValue()) {
				if (entity.getInsuranceCompany() == null) {
					bindingResult.rejectValue("insuranceCompany", "error.select.option", null, null);
				}
				if (StringUtils.isEmpty(entity.getClaimNumber())) {
					bindingResult.rejectValue("claimNumber", "NotNull.java.lang.String", null, null);
				}
			}
		}
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") Injury entity,
			BindingResult bindingResult, HttpServletRequest request) {
		String errorMsg = StringUtils.EMPTY;
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Injury Details with id: %d", entity.getId());
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Injury details deleted successfully");
		}
		
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		return urlContext + "/form";
	}
	
	private List<Injury> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(15000);
		
		List<Injury> injuryList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return injuryList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String reportName = "injuriesReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		List<Injury> injuryList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), injuryList,
						columnPropertyList, request);*/
			out = dynamicReportService.generateStaticReport(reportName,
					injuryList, params, type, request);
			out.writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveClaimReps", action)) {
			List<InsuranceCompanyRep> claimReps = retrieveClaimReps(request);
			return gson.toJson(claimReps);
		} else if (StringUtils.equalsIgnoreCase("saveInsuranceCompany", action)) {
			InsuranceCompany insuranceCompany = saveInsuranceCompany(request);
			return "Insurance Company saved successfully:" + insuranceCompany.getId();
		} else if (StringUtils.equalsIgnoreCase("saveInsuranceCompanyRep", action)) {
			InsuranceCompanyRep insuranceCompanyRep = saveInsuranceCompanyRep(request);
			return "Insurance Company Rep saved successfully:" + insuranceCompanyRep.getId();
		} else if (StringUtils.equalsIgnoreCase("saveIncidentType", action)) {
			InjuryIncidentType injuryIncidentType = saveIncidentType(request);
			return "Incident Type saved successfully:" + injuryIncidentType.getId();
		} else if (StringUtils.equalsIgnoreCase("saveInjuryTo", action)) {
			InjuryToType injuryToType = saveInjuryTo(request);
			return "Injury To details saved successfully:" + injuryToType.getId();
		} 
		
		return StringUtils.EMPTY;
	}
	
	private InjuryIncidentType saveIncidentType(HttpServletRequest request) {
		String incidentType = request.getParameter("incidentType");
		
		InjuryIncidentType entity = new InjuryIncidentType();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setIncidentType(incidentType);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private InjuryToType saveInjuryTo(HttpServletRequest request) {
		String injuryTo = request.getParameter("injuryTo");
		
		InjuryToType entity = new InjuryToType();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setInjuryTo(injuryTo);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private InsuranceCompany saveInsuranceCompany(HttpServletRequest request) {
		String name = request.getParameter("name");
		
		InsuranceCompany entity = new InsuranceCompany();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setName(name);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private InsuranceCompany retrieveInsuranceCompany(String inusranceCompanyIdStr) {
		if (StringUtils.isEmpty(inusranceCompanyIdStr)) {
			return null;
		}
		
		return genericDAO.getById(InsuranceCompany.class, Long.valueOf(inusranceCompanyIdStr));
	}
	
	private InsuranceCompanyRep saveInsuranceCompanyRep(HttpServletRequest request) {
		String inusranceCompanyId = request.getParameter("inusranceCompanyId");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		
		InsuranceCompanyRep entity = new InsuranceCompanyRep();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		InsuranceCompany insuranceCompany = retrieveInsuranceCompany(inusranceCompanyId);
		entity.setInsuranceCompany(insuranceCompany);
		
		entity.setName(name);
		entity.setPhone(phone);
		entity.setEmail(email);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private List<InsuranceCompanyRep> retrieveClaimReps(HttpServletRequest request) {
		Long insuranceCompanyId = Long.valueOf(request.getParameter("insuranceCompanyId"));
		return retrieveClaimReps(insuranceCompanyId);
	}
	
	private List<InsuranceCompanyRep> retrieveClaimReps(Long insuranceCompanyId) {
		String query = "select obj from InsuranceCompanyRep obj "
				+ " where insuranceCompany.id=" + insuranceCompanyId
				+ " order by obj.name";
		List<InsuranceCompanyRep> claimReps = genericDAO.executeSimpleQuery(query);
		return claimReps;
	}
}
