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

import com.primovision.lutransport.core.util.WorkerCompUtils;
import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.accident.AccidentCause;
import com.primovision.lutransport.model.accident.AccidentRoadCondition;
import com.primovision.lutransport.model.accident.AccidentType;
import com.primovision.lutransport.model.accident.AccidentWeather;
import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

@Controller
@RequestMapping("/admin/accident/accidentmaint")
public class AccidentController extends CRUDController<Accident> {
	public AccidentController() {
		setUrlContext("admin/accident/accidentmaint");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(SubContractor.class));
		binder.registerCustomEditor(InsuranceCompany.class, new AbstractModelEditor(InsuranceCompany.class));
		binder.registerCustomEditor(InsuranceCompanyRep.class, new AbstractModelEditor(InsuranceCompanyRep.class));
		binder.registerCustomEditor(AccidentType.class, new AbstractModelEditor(AccidentType.class));
		binder.registerCustomEditor(AccidentCause.class, new AbstractModelEditor(AccidentCause.class));
		binder.registerCustomEditor(AccidentWeather.class, new AbstractModelEditor(AccidentWeather.class));
		binder.registerCustomEditor(AccidentRoadCondition.class, new AbstractModelEditor(AccidentRoadCondition.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<Accident> accidentList = performSearch(criteria);
		model.addAttribute("list", accidentList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<Accident> accidentList = performSearch(criteria);
		model.addAttribute("list", accidentList);
		
		return urlContext + "/list";
	}
	
	private List<Accident> performSearch(SearchCriteria criteria) {
		String driverCompany = (String) criteria.getSearchMap().get("driverCompany");
		String insuranceCompany = (String) criteria.getSearchMap().get("insuranceCompany");
		String driver = (String) criteria.getSearchMap().get("driver");
		String subcontractor = (String) criteria.getSearchMap().get("subcontractor");
		
		String claimNumber = (String) criteria.getSearchMap().get("claimNumber");
		
		String recordable = (String) criteria.getSearchMap().get("recordable");
		
		String accidentType = (String) criteria.getSearchMap().get("accidentType");
		String accidentStatus = (String) criteria.getSearchMap().get("accidentStatus");
		
		String incidentDateFrom = (String) criteria.getSearchMap().get("incidentDateFrom");
		String incidentDateTo = (String) criteria.getSearchMap().get("incidentDateTo");
		
		StringBuffer query = new StringBuffer("select obj from Accident obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Accident obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(driverCompany)) {
			whereClause.append(" and obj.driverCompany.id=" + driverCompany);
		}
		if (StringUtils.isNotEmpty(insuranceCompany)) {
			whereClause.append(" and obj.insuranceCompany.id=" + insuranceCompany);
		}
		if (StringUtils.isNotEmpty(claimNumber)) {
			whereClause.append(" and obj.claimNumber='" + claimNumber + "'");
		}
		if (StringUtils.isNotEmpty(recordable)) {
			whereClause.append(" and obj.recordable='" + recordable + "'");
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
		if (StringUtils.isNotEmpty(subcontractor)) {
			whereClause.append(" and obj.subcontractor.id=" + subcontractor);
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
      if (StringUtils.isNotEmpty(accidentType)) {
			whereClause.append(" and obj.accidentType.id=" + accidentType);
		}
      if (StringUtils.isNotEmpty(accidentStatus)) {
			whereClause.append(" and obj.accidentStatus=" + accidentStatus);
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.incidentDate desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Accident> accidentList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return accidentList;
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		List<InsuranceCompanyRep> claimReps = null;
		if (request.getParameter("id") == null) {
			claimReps = genericDAO.findByCriteria(InsuranceCompanyRep.class, criterias, "name", false);
			//criterias.put("status", 1);
		} else {
			Accident entity = (Accident) model.get("modelObject");
			if (entity != null && entity.getInsuranceCompany() != null) {
				claimReps = retrieveClaimReps(entity.getInsuranceCompany().getId());
			}
		}
		model.addAttribute("claimReps", claimReps);
		
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName asc, id desc", false));
		
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
	}
	
	public void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("insuranceCompanies", genericDAO.findByCriteria(InsuranceCompany.class, criterias, "name", false));
		model.addAttribute("accidentTypes", genericDAO.findByCriteria(AccidentType.class, criterias, "accidentType asc", false));
		model.addAttribute("accidentCauses", genericDAO.findByCriteria(AccidentCause.class, criterias, "cause asc", false));
		model.addAttribute("roadConditions", genericDAO.findByCriteria(AccidentRoadCondition.class, criterias, "roadCondition asc", false));
		model.addAttribute("allWeather", genericDAO.findByCriteria(AccidentWeather.class, criterias, "weather asc", false));
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class,	criterias, "name", false));
		
		criterias.clear();
		criterias.put("dataType", "ACCIDENT_STATUS");
		model.addAttribute("statuses", genericDAO.findByCriteria(StaticData.class, criterias, "dataText", false));
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		// select obj from Vehicle obj where obj.type=1 group by obj.unit
		String vehicleQuery = "select obj from Vehicle obj group by obj.unit, obj.type";
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery(vehicleQuery));
	}


	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("accidents", genericDAO.findByCriteria(Accident.class, criterias, "claimNumber asc", false));
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Accident entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult, request);
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
		
		request.getSession().setAttribute("msg", "Accident details saved successfully");
		
		if (entity.getModifiedAt() == null) {
			model.addAttribute("modelObject", new Accident());
		}
		setupCreate(model, request);
		
		return getUrlContext() + "/form";
	}
	
	private Driver retrieveDriver(Long driverId) {
		return genericDAO.getById(Driver.class, driverId);
	}
	
	private void setMoreDetails(Accident entity) {
		setDriverDetails(entity);
		
		setDayOfWeek(entity);
	}
	
	private void setDriverDetails(Accident entity) {
		if (entity.getDriver() == null) {
			return;
		}
		
		Long driverId = entity.getDriver().getId();
		Driver driver = retrieveDriver(driverId);
		
		/*entity.setDriverCompany(driver.getCompany());
		entity.setDriverTerminal(driver.getTerminal());*/
		entity.setDriverHiredDate(driver.getDateHired());
	}
	
	private void setDayOfWeek(Accident entity) {
		String dayOfWeek = WorkerCompUtils.deriveDayOfWeek(entity.getIncidentDate());
		entity.setIncidentDayOfWeek(dayOfWeek);
	}
	
	private void validateSave(Accident entity, BindingResult bindingResult, HttpServletRequest request) {
		if (entity.getIncidentDate() == null) {
			bindingResult.rejectValue("incidentDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getDriver() == null && entity.getSubcontractor() == null) {
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		if (entity.getDriver() != null && entity.getSubcontractor() != null) {
			bindingResult.rejectValue("subcontractor", "error.select.option", null, null);
		}
		if (entity.getDriverCompany() == null) {
			bindingResult.rejectValue("driverCompany", "error.select.option", null, null);
		}
		if (entity.getDriverTerminal() == null) {
			bindingResult.rejectValue("driverTerminal", "error.select.option", null, null);
		}
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
		
		if (entity.getVehicle() != null && entity.getIncidentDate() != null) {
			Vehicle matchingVehicle = WorkerCompUtils.retrieveVehicleForUnit(entity.getVehicle().getUnitNum(), 
					entity.getIncidentDate(), genericDAO);
			if (matchingVehicle == null) {
				bindingResult.rejectValue("vehicle", "error.select.option", null, null);
				request.getSession().setAttribute("error",
						"No Matching Vehicle Entries Found for Selected Vehicle and Incident Date.");
			} else {
				entity.setVehicle(matchingVehicle);
			}
		}
		
		Integer accidentStatus = entity.getAccidentStatus();
		if (accidentStatus == null) {
			bindingResult.rejectValue("accidentStatus", "error.select.option", null, null);
		} else {
			if (accidentStatus.intValue() != Accident.ACCIDENT_STATUS_NOT_REPORTED.intValue()) {
				if (entity.getInsuranceCompany() == null) {
					bindingResult.rejectValue("insuranceCompany", "error.select.option", null, null);
				}
			}
		}
		
		if (entity.getId() == null) {
			String claimNo = entity.getClaimNumber();
			if (StringUtils.isNotEmpty(claimNo)) {
				Accident existingAccident = WorkerCompUtils.retrieveAccidentByClaimNo(claimNo, genericDAO);
				if (existingAccident != null) {
					bindingResult.rejectValue("claimNumber", "error.duplicate.claimNumber", null, null);
				}
			}
		}
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") Accident entity,
			BindingResult bindingResult, HttpServletRequest request) {
		String errorMsg = StringUtils.EMPTY;
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Accident Details with id: %d", entity.getId());
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Accident details deleted successfully");
		}
		
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		return urlContext + "/form";
	}
	
	private List<Accident> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(15000);
		
		List<Accident> accidentList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return accidentList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String reportName = "accidentsReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		List<Accident> accidentList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), injuryList,
						columnPropertyList, request);*/
			out = dynamicReportService.generateStaticReport(reportName,
					accidentList, params, type, request);
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
		} else if (StringUtils.equalsIgnoreCase("saveAccidentCause", action)) {
			AccidentCause accidentCause = saveAccidentCause(request);
			return "Accident Cause saved successfully:" + accidentCause.getId();
		} else if (StringUtils.equalsIgnoreCase("saveAccidentType", action)) {
			AccidentType accidentType = saveAccidentType(request);
			return "Accident Type saved successfully:" + accidentType.getId();
		} else if (StringUtils.equalsIgnoreCase("saveAccidentWeather", action)) {
			AccidentWeather accidentWeather = saveAccidentWeather(request);
			return "Accident Weather details saved successfully:" + accidentWeather.getId();
		} else if (StringUtils.equalsIgnoreCase("saveAccidentRoadCondition", action)) {
			AccidentRoadCondition accidentRoadCondition = saveAccidentRoadCondition(request);
			return "Accident Road Condition details saved successfully:" + accidentRoadCondition.getId();
		} else if (StringUtils.equals("retrieveDriverCompTerm", action)) {
			String driverIdStr = request.getParameter("driverId");
			if (StringUtils.isEmpty(driverIdStr)) {
				return StringUtils.EMPTY;
			}
			
			Long driverId = Long.valueOf(driverIdStr);
			Driver driver = genericDAO.getById(Driver.class, driverId);
			if (driver == null) {
				return StringUtils.EMPTY;
			} 
			
			return driver.getCompany().getId().longValue() + "|" + driver.getTerminal().getId().longValue();
		}
		
		return StringUtils.EMPTY;
	}
	
	private AccidentType saveAccidentType(HttpServletRequest request) {
		String accidentType = request.getParameter("accidentType");
		
		AccidentType entity = new AccidentType();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setAccidentType(accidentType);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private AccidentRoadCondition saveAccidentRoadCondition(HttpServletRequest request) {
		String accidentRoadCondition = request.getParameter("accidentRoadCondition");
		
		AccidentRoadCondition entity = new AccidentRoadCondition();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setRoadCondition(accidentRoadCondition);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private AccidentWeather saveAccidentWeather(HttpServletRequest request) {
		String accidentWeather = request.getParameter("accidentWeather");
		
		AccidentWeather entity = new AccidentWeather();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setWeather(accidentWeather);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	private AccidentCause saveAccidentCause(HttpServletRequest request) {
		String accidentCause = request.getParameter("accidentCause");
		
		AccidentCause entity = new AccidentCause();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		
		entity.setCause(accidentCause);
		
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
