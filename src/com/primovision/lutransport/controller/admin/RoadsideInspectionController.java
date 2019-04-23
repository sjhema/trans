package com.primovision.lutransport.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.RoadsideInspection;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.Violation;

@Controller
@RequestMapping("/admin/roadsideinspec/roadsideinspecmaint")
public class RoadsideInspectionController extends CRUDController<RoadsideInspection> {
	public RoadsideInspectionController() {
		setUrlContext("admin/roadsideinspec/roadsideinspecmaint");
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
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<Violation> violationList = performSearch(criteria);
		model.addAttribute("list", violationList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<Violation> violationList = performSearch(criteria);
		model.addAttribute("list", violationList);
		
		return urlContext + "/list";
	}
	
	private List<Violation> performSearch(SearchCriteria criteria) {
		String roadsideInspectionId = (String) criteria.getSearchMap().get("id");
		String company = (String) criteria.getSearchMap().get("company.id");
		String truck = (String) criteria.getSearchMap().get("truck.unit");
		String trailer = (String) criteria.getSearchMap().get("trailer.unit");	
		String driver = (String) criteria.getSearchMap().get("driver");
		String inspectionDateFrom = (String) criteria.getSearchMap().get("inspectionDateFrom");
		String inspectionDateTo = (String) criteria.getSearchMap().get("inspectionDateTo");
		String violation = (String) criteria.getSearchMap().get("violation");
		String citation = (String) criteria.getSearchMap().get("citation");
		
		StringBuffer query = new StringBuffer("select obj from Violation obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Violation obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.roadsideInspection is not null");
		
		if (StringUtils.isNotEmpty(roadsideInspectionId)) {
			whereClause.append(" and obj.roadsideInspection.id=" + roadsideInspectionId);
		}
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.roadsideInspection.company=" + company);
		}
		if (StringUtils.isNotEmpty(truck)) {
			whereClause.append(" and obj.roadsideInspection.truck.unit=" + truck);
		}
		if (StringUtils.isNotEmpty(trailer)) {
			whereClause.append(" and obj.roadsideInspection.trailer.unit=" + trailer);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.roadsideInspection.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(inspectionDateFrom)){
        	try {
        		whereClause.append(" and obj.roadsideInspection.inspectionDateFrom >='"+sdf.format(dateFormat.parse(inspectionDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(inspectionDateTo)){
	     	try {
	     		whereClause.append(" and obj.roadsideInspection.inspectionDateTo <='"+sdf.format(dateFormat.parse(inspectionDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(violation)) {
      	whereClause.append(" and obj.roadsideInspection.violation='" + violation + "'");
		}
      if (StringUtils.isNotEmpty(citation)) {
      	whereClause.append(" and obj.roadsideInspection.citation='" + citation + "'");
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by roadsideInspection.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Violation> violationList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return violationList;
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName asc, id desc", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCommon(model, request);
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
	}
	
	public void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		//model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
	}

	private void validateSave(RoadsideInspection entity, BindingResult bindingResult) {
		if (entity.getInspectionDate() == null) {
			bindingResult.rejectValue("inspectionDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getCompany() == null) {
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if (entity.getTruck() == null) {
			bindingResult.rejectValue("truck", "error.select.option", null, null);
		}
		if (entity.getTrailer() == null) {
			bindingResult.rejectValue("trailer", "error.select.option", null, null);
		}
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		if (StringUtils.isEmpty(entity.getInspectionLevel())) {
			bindingResult.rejectValue("inspectionLevel", "error.select.option", null, null);
		}
		
		validateSaveViolation(entity, bindingResult);
	}
	
	private void validateSaveViolation(RoadsideInspection entity, BindingResult bindingResult) {
		String violationType = StringUtils.trimToEmpty(entity.getViolationType());
		if (StringUtils.isEmpty(violationType)) {
			if (entity.getId() == null 
					|| StringUtils.isNotEmpty(entity.getViolationId())
					|| StringUtils.isNotEmpty(entity.getCitationNo())
					|| StringUtils.isNotEmpty(entity.getOutOfService())) {
				bindingResult.rejectValue("violationType", "NotNull.java.lang.String", null, null);
			}
		} else {
			if (StringUtils.isEmpty(entity.getOutOfService())) {
				bindingResult.rejectValue("outOfService", "error.select.option", null, null);
			}
			
			String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
			if (StringUtils.isNotEmpty(citationNo)
					&& StringUtils.isEmpty(entity.getViolationId())) {
				if(isDuplicate(citationNo)) {
					bindingResult.rejectValue("citationNo", "error.duplicate.entry", null, null);
				}
			}
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") RoadsideInspection entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if (bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	setupViolation(model, request, entity);
        	return getUrlContext() + "/form";
      }
		
		String violationType = StringUtils.trimToEmpty(entity.getViolationType());
		if (StringUtils.isNotEmpty(violationType)) {
			entity.setViolation("Y");
		} else {
			if (entity.getId() == null) {
				entity.setViolation("N");
			} else {
				if (!hasViolations(entity.getId())) {
					entity.setViolation("N");
				}
			}
		}
		
		String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
		if (StringUtils.isNotEmpty(citationNo)) {
			entity.setCitation("Y");
		} else {
			if (entity.getId() == null) {
				entity.setCitation("N");
			} else {
				if (!hasCitations(entity.getId())) {
					entity.setCitation("N");
				}
			}
		}
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		saveViolation(entity);
		
		request.getSession().setAttribute("msg", "Roadside Inspection saved successfully");
		
		setupCreate(model, request);
		setupViolation(model, request, entity);
		return getUrlContext() + "/form";
	}
	
	private boolean hasViolations(Long roadInspectionId) {
		List<Violation> violationList = retrieveViolationsForRoadInspec(roadInspectionId);
		return (violationList == null || violationList.isEmpty()) ? false : true;
	}
	
	private boolean hasCitations(Long roadInspectionId) {
		List<Violation> citationList = retrieveCitationsForRoadInspec(roadInspectionId);
		return (citationList == null || citationList.isEmpty()) ? false : true;
	}
	
	private List<Violation> retrieveViolationsForRoadInspec(Long roadInspectionId) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.roadsideInspection.id=" + roadInspectionId);
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private List<Violation> retrieveCitationsForRoadInspec(Long roadInspectionId) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.roadsideInspection.id=" + roadInspectionId);
		query.append(" and obj.citationNo is not null");
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private List<Violation> retrieveCitation(String citationNo) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.citationNo='" + citationNo + "'");
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private boolean isDuplicate(String citationNo) {
		List<Violation> citationList = retrieveCitation(citationNo);
		return (citationList == null || citationList.isEmpty()) ? false : true;
	}
	
	private void saveViolation(RoadsideInspection entity) {
		String violationType = StringUtils.trimToEmpty(entity.getViolationType());
		if (StringUtils.isEmpty(violationType)) {
			return;
		}
		
		Violation aViolation = null;
		String violationIdStr = entity.getViolationId();
		if (StringUtils.isNotEmpty(violationIdStr)) {
			Long violationId = Long.valueOf(violationIdStr);
			aViolation = genericDAO.getById(Violation.class, violationId);
			aViolation.setModifiedAt(entity.getModifiedAt());
			aViolation.setModifiedBy(entity.getModifiedBy());
		} else {
			aViolation = new Violation();
			aViolation.setCreatedAt(entity.getCreatedAt());
			aViolation.setCreatedBy(entity.getCreatedBy());
			aViolation.setStatus(1);
		}
		
		aViolation.setCompany(entity.getCompany());
		aViolation.setDriver(entity.getDriver());
		aViolation.setTruck(entity.getTruck());
		aViolation.setTrailer(entity.getTrailer());
		aViolation.setIncidentDate(entity.getInspectionDate());
		aViolation.setOutOfService(entity.getOutOfService());
		aViolation.setViolationType(violationType);
		
		String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
		if (StringUtils.isNotEmpty(citationNo)) {
			aViolation.setCitationNo(citationNo);
		} else {
			aViolation.setCitationNo(null);
		}
		
		aViolation.setRoadsideInspection(entity);
		
		genericDAO.saveOrUpdate(aViolation);
		
		emptyViolation(entity);
	}
	
	private void emptyViolation(RoadsideInspection entity) {
		entity.setViolationId(StringUtils.EMPTY);
		entity.setCitationNo(StringUtils.EMPTY);
		entity.setViolationType(StringUtils.EMPTY);
		entity.setOutOfService(StringUtils.EMPTY);
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") RoadsideInspection entity,
			BindingResult bindingResult, HttpServletRequest request) {
		String query = "select obj from Violation obj where obj.roadsideInspection.id=" + entity.getId();
		List<Violation> violationList = genericDAO.executeSimpleQuery(query);
		for (Violation aViolation : violationList) {
			genericDAO.delete(aViolation);
		}
		
		String errorMsg = StringUtils.EMPTY;
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Roadside Inspection with id: %d", entity.getId());
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Roadside Inspection deleted successfully");
		}
		
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		setupViolation(model, request, null);
		
		return urlContext + "/form";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteViolation.do")
	public String deleteViolation(HttpServletRequest request, ModelMap model,
			@RequestParam(value = "id") Long roadsideInspectionId,
			@RequestParam(value = "violationId") Long violationId) {
		String query = "select obj from Violation obj where obj.roadsideInspection.id=" + roadsideInspectionId;
		List<Violation> violationList = genericDAO.executeSimpleQuery(query);
		if (violationList.size() == 1) {
			request.getSession().setAttribute("error", "Roadside inspection should have at least one violation");
			
			RoadsideInspection aRoadsideInspection = genericDAO.getById(RoadsideInspection.class, roadsideInspectionId);
			model.addAttribute("modelObject", aRoadsideInspection);
			
			setupCreate(model, request);
			setupViolation(model, request, aRoadsideInspection);
			
			return getUrlContext() + "/form";
		}
		
		String errorMsg = StringUtils.EMPTY;
		Violation aViolation = genericDAO.getById(Violation.class, violationId);
		try {
			genericDAO.delete(aViolation);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Violation with id: %d", violationId);
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Violation deleted successfully");
		}
		
		RoadsideInspection aRoadsideInspection = genericDAO.getById(RoadsideInspection.class, roadsideInspectionId);
		model.addAttribute("modelObject", aRoadsideInspection);
		
		setupCreate(model, request);
		setupViolation(model, request, aRoadsideInspection);
		
		return getUrlContext() + "/form";
	}
	
	private void setupViolation(ModelMap model, HttpServletRequest request, RoadsideInspection entity) {
		String roadsideInspectionIdStr = request.getParameter("id");
		if (entity == null && StringUtils.isEmpty(roadsideInspectionIdStr)) {
			return;
		}
		
		Long roadsideInspectionId = entity != null ? entity.getId() : Long.valueOf(roadsideInspectionIdStr);
		if (roadsideInspectionId == null) {
			return;
		}
		
		String query = "select obj from Violation obj where obj.roadsideInspection.id=" + roadsideInspectionId
								+ " order by obj.id asc";
		List<Violation> violationList = genericDAO.executeSimpleQuery(query);
		model.addAttribute("violationList", violationList);
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveViolation", action)) {
			Violation aViolation = retrieveViolation(request);
			return gson.toJson(aViolation);
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
	
	private Violation retrieveViolation(HttpServletRequest request) {
		Long violationId = Long.valueOf(request.getParameter("violationId"));
		return genericDAO.getById(Violation.class, violationId);
	}
}
