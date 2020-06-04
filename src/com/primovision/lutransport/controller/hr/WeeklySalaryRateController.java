package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.WeeklySalary;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/hr/weeklysalaryrate")
public class WeeklySalaryRateController extends CRUDController<WeeklySalary>
{

	public WeeklySalaryRateController() {
		setUrlContext("/hr/weeklysalaryrate");
	}
	
	String weeklySalaryRateAlertListPg = "hr/payrollratealert/list.do?type=weeklySalaryRate";
	String fromAlertPageIndicator = "fromAlertPage";
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	
	
	
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("employeestatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
		
		if (isRequestFromAlertPage(request)) {
			model.addAttribute(fromAlertPageIndicator, "true");
		}
	}
	
	
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
	}
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"driver.fullName",null,null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "ValidFrom", "validFrom");
		dateupdateService.updateDate(request, "ValidTo", "validTo");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"driver.fullName",null,null));
		 criteria.getSearchMap().put("ValidFrom", request.getParameter("ValidFrom"));
			criteria.getSearchMap().put("ValidTo", request.getParameter("ValidTo"));
		 return urlContext + "/list";
		
		
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		//setupCreate(model, request);
		Map criterias = new HashMap();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		/*criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Employee.class, criterias,"fullName",false));*/
		String query="select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("employeestatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));

	}
	
	
	

	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") WeeklySalary entity,
			BindingResult bindingResult, ModelMap model) {
		
		
		/*if(entity.getStaffId()==null)
		{
			bindingResult.rejectValue("staffId", "error.select.option",
					null, null);
		}*/
		if(entity.getDriver()==null){
			System.out.println("******** Driver Data is "+entity.getDriver());
			bindingResult.rejectValue("driver", "error.select.option",
					null, null);	
		}
		
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option",
					null, null);
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
					null, null);
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
		
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		addMsg(request, "Weekly salary rate details saved successfully");
		
		String redirectUrl = "redirect:/" + urlContext + "/list.do";
		if (isRequestFromAlertPage(request)) {
			redirectUrl = "redirect:/" + weeklySalaryRateAlertListPg;
		}
		return redirectUrl;
		
		//return super.save(request, entity, bindingResult, model);
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") WeeklySalary entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute("errors",
					"This" + entity.getClass().getSimpleName() + " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
			
		addMsg(request, "Weekly salary rate deleted successfully");
		
		String redirectUrl = "redirect:/" + urlContext + "/list.do";
		if (isRequestFromAlertPage(request)) {
			redirectUrl = "redirect:/" + weeklySalaryRateAlertListPg;
		}
		return redirectUrl;
	}
	
	@RequestMapping("/changeAlertStatus.do")
	public String changeAlertStatus(HttpServletRequest request, ModelMap modMap) {
		WeeklySalary weeklySalary = genericDAO.getById(WeeklySalary.class, Long.valueOf(request.getParameter("id")));
		String newStatus = request.getParameter("status");
		if (!StringUtils.equals(newStatus, String.valueOf(weeklySalary.getAlertStatus()))) { 
			weeklySalary.setAlertStatus(Integer.valueOf(newStatus));
			genericDAO.save(weeklySalary);
			addMsg(request, "Weekly salary rate alert status changed successfully");
		}
		
		String redirectUrl = "redirect:/" + urlContext + "/list.do";
		if (isRequestFromAlertPage(request)) {
			redirectUrl = "redirect:/" + weeklySalaryRateAlertListPg;
		}
		
		return redirectUrl;
	}
	
	private boolean isRequestFromAlertPage(HttpServletRequest request) {
		String fromAlertPage = request.getParameter(fromAlertPageIndicator);
		return (StringUtils.isNotEmpty(fromAlertPage) && BooleanUtils.toBoolean(fromAlertPage));
	}
	
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {	
		
		
		if("findDCompany".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> company=new ArrayList<Location>();
				
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				company.add(driver.getCompany());
				Gson gson = new Gson();
				return gson.toJson(company);
				
			}
			else{
				Map criterias = new HashMap();
				criterias.put("type", 3);
				List<Location> company=new ArrayList<Location>();
				company=genericDAO.findByCriteria(Location.class, criterias,"name",false);
				Gson gson = new Gson();
				return gson.toJson(company);
				
			}
			
		}
		
		if("findDterminal".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> terminal=new ArrayList<Location>();
				
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				terminal.add(driver.getTerminal());
				Gson gson = new Gson();
				return gson.toJson(terminal);
				
			}
			else{
				Map criterias = new HashMap();
				criterias.put("type", 4);
				List<Location> terminal=new ArrayList<Location>();
				terminal=genericDAO.findByCriteria(Location.class, criterias,"name",false);
				Gson gson = new Gson();
				return gson.toJson(terminal);
				
			}
		}
		if("findDCategory".equalsIgnoreCase(action)){
				if(!StringUtils.isEmpty(request.getParameter("driver"))){
					List<EmployeeCatagory> category=new ArrayList<EmployeeCatagory>();
					Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
					category.add(driver.getCatagory());
					Gson gson = new Gson();
					return gson.toJson(category);
				}
				else{
					Map criterias = new HashMap();
					List<EmployeeCatagory> category=new ArrayList<EmployeeCatagory>();
					category=genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false);
					Gson gson = new Gson();
					return gson.toJson(category);
				}
			
			}
		if("findDStaff".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<String> str=new ArrayList<String>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				if(driver!=null){
					str.add(driver.getStaffId());
				}else{
					str.add(".");
				}
				Gson gson = new Gson();
				return gson.toJson(str);
			}
		}
		
		return "";
	}
}
