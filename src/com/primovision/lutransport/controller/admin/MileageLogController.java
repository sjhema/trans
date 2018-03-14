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
import com.primovision.lutransport.model.MileageLog;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehiclePermit;
import com.primovision.lutransport.model.accident.Accident;

@Controller
@RequestMapping("/operator/mileagelog")
public class MileageLogController extends CRUDController<MileageLog> {
	private static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat mileageSearchDateFormat = new SimpleDateFormat("MMMMM yyyy");
	
	public MileageLogController() {
		setUrlContext("operator/mileagelog");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy");
	   dateFormat.setLenient(false);
	   
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 1);
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		populateSearchCriteria(request, request.getParameterMap());
	}
	
	private List<MileageLog> performSearch(SearchCriteria criteria) {
		String company = (String) criteria.getSearchMap().get("company.id");
		String state = (String) criteria.getSearchMap().get("state.id");
		String truck = (String) criteria.getSearchMap().get("unit.id");
		String periodFrom = (String) criteria.getSearchMap().get("periodFrom");
		String periodTo = (String) criteria.getSearchMap().get("periodTo");
		
		StringBuffer query = new StringBuffer("select obj from MileageLog obj  where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from MileageLog obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (!StringUtils.isEmpty(company)) {
			whereClause.append(" and obj.company=" + company);
		}
		
		if (!StringUtils.isEmpty(state)) {
			whereClause.append(" and obj.state=" + state);
		}
		
		if (!StringUtils.isEmpty(truck)) {
			whereClause.append(" and obj.unitNum=" + truck);
		}
        
      if (!StringUtils.isEmpty(periodFrom)) {
        	try {
        		whereClause.append(" and obj.period >='"+dbDateFormat.format(mileageSearchDateFormat.parse(periodFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        
      if (!StringUtils.isEmpty(periodTo)) {
        	try {
        		whereClause.append(" and obj.period <='"+dbDateFormat.format(mileageSearchDateFormat.parse(periodTo))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
      }

      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.period desc");
		
     Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
     criteria.setRecordCount(recordCount.intValue());
		
     List<MileageLog> mileageLogList = 
   		  genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList();
		return mileageLogList;
	}

	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<MileageLog> mileageLogList = performSearch(criteria);
		model.addAttribute("list", mileageLogList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<MileageLog> mileageLogList = performSearch(criteria);
		model.addAttribute("list", mileageLogList);
		
		return urlContext + "/list";
	}
	
	private List<MileageLog> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(15000);
		
		List<MileageLog> mileageLogList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return mileageLogList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String reportName = "mileageLogReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		List<MileageLog> mileageLogList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List)request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, getEntityClass(), mileageLogList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					mileageLogList, params, type, request);*/
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
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		return urlContext + "/form";
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") MileageLog entity,
			BindingResult bindingResult, ModelMap model) {
		if (entity.getState() == null) {
			bindingResult.rejectValue("state", "error.select.option", null, null);
		}
		
		if (entity.getCompany() == null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		
		if (entity.getUnit() == null) {
			bindingResult.rejectValue("unit", "error.select.option", null, null);
		} else {
			Map crieiria = new HashMap();
			crieiria.put("id", entity.getUnit().getId());
		   Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, crieiria);
		   if (vehicle != null) {
		   	entity.setUnitNum(vehicle.getUnitNum());
		   } 
		}
		
		if (StringUtils.isEmpty(entity.getVehiclePermitNumber())) {
			entity.setVehiclePermitNumber(StringUtils.EMPTY);
			entity.setVehiclePermit(null);
		} else {
			VehiclePermit vehiclePermit = retrieveVehiclePermit(entity.getVehiclePermitNumber(), entity.getUnitNum());
			if (vehiclePermit != null) {
				entity.setVehiclePermit(vehiclePermit);
			} else {
				bindingResult.rejectValue("vehiclePermitNumber", "error.textbox.vehiclePermitNumber", null, null);
			}
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
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		if (!StringUtils.isEmpty(request.getParameter("id"))){
			request.getSession().setAttribute("msg", "Mileage updated successfully");
			return "redirect:list.do";
		}
		else {			
			request.getSession().setAttribute("msg", "Mileage added successfully");
			return "redirect:create.do";
		}
	}
	
	private VehiclePermit retrieveVehiclePermit(String vehiclePermitNumber, String unitNum) {
		if (StringUtils.isEmpty(vehiclePermitNumber) || StringUtils.isEmpty(unitNum)) {
			return null;
		}
		
		String query = "select obj from VehiclePermit obj where obj.permitNumber=" + vehiclePermitNumber
						+ " and obj.vehicle.unitNum = " + unitNum;
		List<VehiclePermit> permits = genericDAO.executeSimpleQuery(query);
		if (permits != null && !permits.isEmpty()) {
			return permits.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request, String action, Model model) {
		return "";
	}
}
