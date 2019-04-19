package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.WorkerCompUtils;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.Violation;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.accident.AccidentCause;
import com.primovision.lutransport.model.accident.AccidentRoadCondition;
import com.primovision.lutransport.model.accident.AccidentType;
import com.primovision.lutransport.model.accident.AccidentWeather;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

@Controller
@RequestMapping("/admin/citation/citationmaint")
public class CitationController extends CRUDController<Violation> {
	public CitationController() {
		setUrlContext("admin/citation/citationmaint");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName asc, id desc", false));
		
		//model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
	}
	
	public void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCommon(model, request);
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
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
		String company = (String) criteria.getSearchMap().get("company");
		String driver = (String) criteria.getSearchMap().get("driver");
		
		String citationDateFrom = (String) criteria.getSearchMap().get("citationDateFrom");
		String citationDateTo = (String) criteria.getSearchMap().get("citationDateTo");
		
		StringBuffer query = new StringBuffer("select obj from Violation obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Violation obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.roadsideInspection is null");
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.company.id=" + company);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(citationDateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(citationDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(citationDateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(citationDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.incidentDate desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Violation> violationList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return violationList;
	}

	private void validateSave(Violation entity, BindingResult bindingResult, HttpServletRequest request) {
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		
		if (entity.getTruck() == null || StringUtils.isEmpty(entity.getTruck().getUnitNum())) {
			bindingResult.rejectValue("truck.unitNum", "error.select.option", null, null);
		}
		if (entity.getTruck() != null && StringUtils.isNotEmpty(entity.getTruck().getUnitNum())
				&& entity.getIncidentDate() != null) {
			Vehicle matchingTruck = WorkerCompUtils.retrieveVehicleForUnit(entity.getTruck().getUnitNum(), 1,  
					entity.getIncidentDate(), genericDAO);
			if (matchingTruck == null) {
				bindingResult.rejectValue("truck.unitNum", "error.select.option", null, null);
				request.getSession().setAttribute("error",
						"No Matching Truck Entries Found for Selected Truck and Citation Date.");
			} else {
				entity.setTruck(matchingTruck);
			}
		}
		
		if (entity.getTrailer() == null || StringUtils.isEmpty(entity.getTrailer().getUnitNum())) {
			bindingResult.rejectValue("trailer", "error.select.option", null, null);
		}
		if (entity.getTrailer() != null && StringUtils.isNotEmpty(entity.getTrailer().getUnitNum())
				&& entity.getIncidentDate() != null) {
			Vehicle matchingTrailer = WorkerCompUtils.retrieveVehicleForUnit(entity.getTrailer().getUnitNum(), 2,  
					entity.getIncidentDate(), genericDAO);
			if (matchingTrailer == null) {
				bindingResult.rejectValue("trailer", "error.select.option", null, null);
				request.getSession().setAttribute("error",
						"No Matching Trailer Entries Found for Selected Trailer and Citation Date.");
			} else {
				entity.setTrailer(matchingTrailer);
			}
		}
		
		if (entity.getCompany() == null) {
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if (entity.getIncidentDate() == null) {
			bindingResult.rejectValue("incidentDate", "NotNull.java.lang.String", null, null);
		}
		String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
		if (StringUtils.isEmpty(citationNo)) {
			bindingResult.rejectValue("citationNo", "NotNull.java.lang.String", null, null);
		}
		if (StringUtils.isEmpty(entity.getOutOfService())) {
			bindingResult.rejectValue("outOfService", "error.select.option", null, null);
		}
		if (StringUtils.isEmpty(entity.getViolationType())) {
			bindingResult.rejectValue("violationType", "NotNull.java.lang.String", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Violation entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult, request);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Citation saved successfully");
		
		setupCreate(model, request);
		
		return getUrlContext() + "/form";
	}
	
	private List<Violation> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(15000);
		
		List<Violation> violationList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return violationList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String reportName = "violationReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		List<Violation> violationList = searchForExport(model, request);
		//Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, getEntityClass(), violationList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					violationList, params, type, request);*/
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
		
		if (StringUtils.equals("retrieveDriverCompTerm", action)) {
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
}
