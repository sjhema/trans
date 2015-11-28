package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehiclePermit;
import com.primovision.lutransport.model.hr.HourlyRate;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.SetupData;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/admin/vehiclepermit")
public class VehiclePermitController extends CRUDController<VehiclePermit> {
	
	public VehiclePermitController(){
		setUrlContext("admin/vehiclepermit");
	}
	
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.BaseController#initBinder(org.springframework.web.bind.WebDataBinder)
	 */
	@Override
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(
				Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(SetupData.class, new AbstractModelEditor(SetupData.class));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("vehicle", genericDAO.findByCriteria(Vehicle.class, criterias));
		Map<String,Object> map=new HashMap<String,Object>();
		criterias.put("dataType", "PERMIT_TYPE");
		model.addAttribute("permitType", genericDAO.findByCriteria(SetupData.class, criterias, "dataLabel", false));		
		criterias.clear();
		criterias.put("name", "LU,WB,DREW");
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/copy.do")
	public String copyVehiclePermit(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		return urlContext + "/form";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#save(javax.servlet.http.HttpServletRequest, com.primovision.lutransport.model.BaseModel, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)
	 */
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") VehiclePermit entity,
			BindingResult bindingResult, ModelMap model) {
	
		Date issueDateTemp=entity.getValidFromTemp();
		Date expirationDateTemp=entity.getValidToTemp();
		
		if(entity.getVehicle() == null)
			bindingResult.rejectValue("vehicle", "error.select.option", null,	null);	
		if(StringUtils.isEmpty(request.getParameter("permitType")))
			bindingResult.rejectValue("permitType", "error.select.option", null,	null);	
		if(entity.getCompanyLocation() == null){
			bindingResult.rejectValue("companyLocation", "error.select.option", null, null);
		}
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
		
		if(!StringUtils.isEmpty(request.getParameter("permitType")) && entity.getVehicle()!=null && entity.getCompanyLocation()!=null && entity.getIssueDate()!=null && entity.getExpirationDate()!=null){	
			String issuedate=dateFormat1.format(entity.getIssueDate());
			String expirationdate=dateFormat1.format(entity.getExpirationDate());
			StringBuilder checkForDuplicateQuery = new StringBuilder("SELECT v FROM VehiclePermit v WHERE v.vehicle ="+entity.getVehicle().getId()+" "
				/*  + " and v.permitNumber='"+entity.getPermitNumber()+"'"*/
				    + " and v.issueDate='"+issuedate+"' and v.expirationDate='"+expirationdate+"' "
					+ " and v.companyLocation="+entity.getCompanyLocation().getId()+" "
					+ " and v.permitType="+request.getParameter("permitType"));
			if(entity.getId()!=null){
				checkForDuplicateQuery.append(" and v.id !="+entity.getId());
			}
			List<VehiclePermit> list = genericDAO.executeSimpleQuery(checkForDuplicateQuery.toString());

			if(list.size()>0 && list!=null){
				setupCreate(model, request);
				model.addAttribute("errorss","errorss");
				request.getSession().setAttribute("error",
				"Vehicle Permit Already Exists");
				return urlContext + "/form";
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("permitType"))){
			SetupData setupData = genericDAO.getById(SetupData.class, Long.parseLong(request.getParameter("permitType")));
			entity.setPermitType(setupData);
		}
		
		if(issueDateTemp!=null && expirationDateTemp !=null ){
			if(entity.getIssueDate().getTime()== issueDateTemp.getTime() && entity.getExpirationDate().getTime()== expirationDateTemp.getTime()){					
				///// do nothing			
			}
			else{						
				String query = "select obj from VehiclePermit obj where obj.vehicle='"
					+ entity.getVehicle().getId()
					+ "' and obj.permitType='"					
					+ request.getParameter("permitType")
					+"' and obj.issueDate !='"
					+ issueDateTemp
					+ "' and obj.expirationDate != '"
					+expirationDateTemp+"'";
						
				String result=common(request,model,entity,query);

				if(result.equalsIgnoreCase("error")){
					setupUpdate(model, request);	
					model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
					request.getSession().setAttribute("error", "Permit already exists for specified date range.");
					return urlContext + "/form"; 
				}  
			}		
		}
		else{	
			String query = "select obj from VehiclePermit obj where obj.vehicle='"
				+ entity.getVehicle().getId()
				+ "' and obj.permitType='"					
				+ request.getParameter("permitType")
				+ "' order by obj.issueDate desc";
			String result=common(request,model,entity,query);

			if(result.equalsIgnoreCase("error")){            	
				setupUpdate(model, request);	
				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
				request.getSession().setAttribute("error", "Permit already exists for specified date range.");
				return urlContext + "/form"; 
			}
		}
		
		beforeSave(request, entity, model);	
		genericDAO.saveOrUpdate(entity);
		return "redirect:/" + urlContext + "/list.do";
		//return super.save(request, entity, bindingResult, model);
	}
	
	private String common(HttpServletRequest request,ModelMap model,VehiclePermit entity,String permitQuery)
	{
		String result="";
		List<VehiclePermit> vehiclePermits = genericDAO.executeSimpleQuery(permitQuery);
		
		if (vehiclePermits != null && vehiclePermits.size() > 0) { 

			for(VehiclePermit vePermit: vehiclePermits){

				if((entity.getIssueDate().getTime() >= vePermit.getIssueDate().getTime() && entity.getExpirationDate().getTime()<= vePermit.getExpirationDate().getTime())
						||(entity.getIssueDate().getTime() <= vePermit.getIssueDate().getTime() && entity.getExpirationDate().getTime()>= vePermit.getExpirationDate().getTime())  
						|| ((entity.getIssueDate().getTime() >= vePermit.getIssueDate().getTime() && entity.getIssueDate().getTime()<=vePermit.getExpirationDate().getTime()) && entity.getExpirationDate().getTime()>= vePermit.getExpirationDate().getTime())
						|| (entity.getIssueDate().getTime() <= vePermit.getIssueDate().getTime() && (entity.getExpirationDate().getTime()>=vePermit.getIssueDate().getTime()   && entity.getExpirationDate().getTime()<= vePermit.getExpirationDate().getTime()))){       		
					result="error";      		   
					return result;
				}   	
			}   
		}   
		return "";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "issuedate", "issueDate");
		dateupdateService.updateDate(request, "expirydate", "expirationDate");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}
	
	@ModelAttribute("modelObject")
	public VehiclePermit setupModel(HttpServletRequest request) {
				
		String copymodepk = request.getParameter("vpid");
		if (copymodepk != null || !org.apache.commons.lang.StringUtils.isEmpty(copymodepk)) {
			VehiclePermit vehiclePermit = genericDAO.getById(getEntityClass(), Long.parseLong(copymodepk));
			vehiclePermit.setId(null);
			return vehiclePermit;
		} 
		
		String pk = request.getParameter(getPkParam());
		if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
			return getEntityInstance();
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}
	
}
