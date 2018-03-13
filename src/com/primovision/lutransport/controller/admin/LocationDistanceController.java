package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

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

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.LocationDistance;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;


@Controller
@RequestMapping("/admin/locationdist")
public class LocationDistanceController extends CRUDController<LocationDistance> {
	public LocationDistanceController() {
		setUrlContext("admin/locationdist");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
	   
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		criterias.put("id!",91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		populateSearchCriteria(request, request.getParameterMap());
	}

	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<LocationDistance> locationDistanceList = performSearch(criteria);
		
		model.addAttribute("list", locationDistanceList);
		return urlContext + "/list";
	}
	
	private List<LocationDistance> performSearch(SearchCriteria criteria) {
		String origin = (String) criteria.getSearchMap().get("origin");
		String destination = (String) criteria.getSearchMap().get("destination");
		
		StringBuffer query = new StringBuffer("select obj from LocationDistance obj where 1=1");
		StringBuffer countquery = new StringBuffer("select count(obj) from LocationDistance obj where 1=1");
		
		if (StringUtils.isNotEmpty(origin)) {
			query.append(" and obj.origin.id=" + origin);
			countquery.append(" and obj.origin.id=" + origin);
		}
		
		if (StringUtils.isNotEmpty(destination)) {
			query.append(" and obj.destination.id=" + destination);
			countquery.append(" and obj.destination.id=" + destination);
		}

     query.append(" order by obj.origin asc, obj.destination asc");
     countquery.append(" order by obj.origin asc, obj.destination asc");
		
     Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
        		countquery.toString()).getSingleResult();        
     criteria.setRecordCount(recordCount.intValue());
		
     List<LocationDistance> locationDistanceList = genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList();
     return locationDistanceList;
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<LocationDistance> locationDistanceList = performSearch(criteria);
		model.addAttribute("list", locationDistanceList);
		return urlContext + "/list";
	}

	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		return urlContext + "/form";
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") LocationDistance entity,
			BindingResult bindingResult, ModelMap model) {
		if (entity.getOrigin() == null) {
			bindingResult.rejectValue("origin", "error.select.option", null, null);
		}
		
		if (entity.getDestination() == null){
			bindingResult.rejectValue("destination", "error.select.option", null, null);
		}
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation:" + e);
		}
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		if (entity.getId() == null) {
			if (checkDuplicate(entity)) {
				request.getSession().setAttribute("error", "Duplicate Load Miles");
				return "redirect:create.do";
			}
		}
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		if (StringUtils.isNotEmpty(request.getParameter("id"))){
			request.getSession().setAttribute("msg", "Load Miles updated successfully");
			return "redirect:list.do";
		}
		else {			
			request.getSession().setAttribute("msg", "Load Miles added successfully");
			return "redirect:create.do";
		}
	}
	
	private boolean checkDuplicate(LocationDistance entity) {
		Location origin = entity.getOrigin();
		Location destination = entity.getDestination();
		if (origin == null || destination == null) {
			return false;
		}
		
		String query = "select count(obj) from LocationDistance obj where 1=1"
				+ " and obj.origin.id=" + origin.getId()
				+ " and obj.destination.id=" + destination.getId();
		 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
				 query.toString()).getSingleResult(); 
		 return (recordCount > 0);
	}
	
	private List<LocationDistance> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(1500);
		
		List<LocationDistance> locationDistanceList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return locationDistanceList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String reportName = "locationDistanceReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + "loadMilesReport" + "." + type);
		}
		
		List<LocationDistance> locationDistanceList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(
					reportName, type, getEntityClass(), locationDistanceList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					locationDistanceList, params, type, request);*/
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
	protected String processAjaxRequest(HttpServletRequest request, String action, Model model) {
		return "";
	}
}
