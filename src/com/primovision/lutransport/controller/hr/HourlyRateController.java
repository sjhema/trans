package com.primovision.lutransport.controller.hr;




import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.HourlyRate;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.service.DateUpdateService;



@Controller
@RequestMapping("/hr/hourlyrate")
public class HourlyRateController extends CRUDController<HourlyRate> {

	public HourlyRateController() {
		setUrlContext("/hr/hourlyrate");
	}
	
	String hourlyPayRateAlertListPg = "hr/payrollratealert/list.do?type=hourlyPayRate";
	String fromAlertPageIndicator = "fromAlertPage";
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}

	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	@Override
	public void initBinder(WebDataBinder binder) {
		// SimpleDateFormat("yyyy-MM-dd")
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class, false));

		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(StaticData.class, new AbstractModelEditor(StaticData.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(LeaveType.class, new AbstractModelEditor(LeaveType.class));

	}


	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();

		criterias.put("type", 3);
		model.addAttribute("companyLocation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		
		criterias.clear();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		
		if (isRequestFromAlertPage(request)) {
			model.addAttribute(fromAlertPageIndicator, "true");
		}
	}

	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"driver.lastName asc,driver.firstName asc,validFrom desc,validTo",true,null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "ValidFrom", "validFrom");
		dateupdateService.updateDate(request, "ValidTo", "validTo");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"driver.lastName asc,driver.firstName asc,validFrom desc,validTo",true,null));
		//criteria.getSearchMap().put("ValidFrom", request.getParameter("ValidFrom"));
		//criteria.getSearchMap().put("ValidTo", request.getParameter("ValidTo"));
		return urlContext + "/list";
		
		
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		//setupCreate(model, request);
		Map criterias = new HashMap();

		criterias.put("type", 3);
		model.addAttribute("companyLocation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		/*criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Employee.class, criterias, "fullName", false));*/
		String query="select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		criterias.clear();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));

	}

	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") HourlyRate entity,
			BindingResult bindingResult, ModelMap model) {

		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}

		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option", null, null);
		}
		if(entity.getDriver()==null){
			System.out.println("\nentity.driver is null==>"+entity.getDriver()+"\n");
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}

		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}

		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();

		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){
			if(entity.getValidFrom().getTime()== validFromTemp.getTime() && entity.getValidTo().getTime()== validToTemp.getTime()){					
				///// do nothing			
			}
			else{		
				System.out.println("it is changed");
				String rateQuery = "select obj from HourlyRate obj where obj.company='"
					+ entity.getCompany().getId()
					+ "' and obj.driver='"					
					+ entity.getDriver().getId()
					+ "' and obj.catagory='"					
					+ entity.getCatagory().getId()
					+ "' and obj.terminal='"					
					+ entity.getTerminal().getId()
					+"' and obj.validFrom !='"
					+ validFromTemp
					+ "' and obj.validTo != '"
					+validToTemp+"'";	
				
				String result=common(request,model,entity,rateQuery);
				
				if(result.equalsIgnoreCase("error")){
					setupUpdate(model, request);	
					model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
					request.getSession().setAttribute("error", "Rate already exists for specified date range.");
					return urlContext + "/form"; 
				}  
			}		
		}
		else{	
	
			String rateQuery = "select obj from HourlyRate obj where obj.company='"
				+ entity.getCompany().getId()
				+ "' and obj.driver='"					
				+ entity.getDriver().getId()
				+ "' and obj.catagory='"					
				+ entity.getCatagory().getId()
				+ "' and obj.terminal='"					
				+ entity.getTerminal().getId()+ "' order by obj.validFrom desc";

			String result=common(request,model,entity,rateQuery);

			if(result.equalsIgnoreCase("error")){    
				setupUpdate(model, request);	
				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
				request.getSession().setAttribute("error", "Rate already exists for specified date range.");
				return urlContext + "/form"; 
			}
		}
		
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		addMsg(request, "Hourly rate details saved successfully");
		
		String redirectUrl = "redirect:/" + urlContext + "/list.do";
		if (isRequestFromAlertPage(request)) {
			redirectUrl = "redirect:/" + hourlyPayRateAlertListPg;
		}
		return redirectUrl;
		
		//return super.save(request, entity, bindingResult, model);
	}
	
	
	private String common(HttpServletRequest request,ModelMap model,HourlyRate entity,String rateQuery)
	{
		String result="";
		List<HourlyRate> hourlyRates = genericDAO.executeSimpleQuery(rateQuery);
		if (hourlyRates != null && hourlyRates.size() > 0) { 
			for(HourlyRate hourlyRate: hourlyRates){
				if((entity.getValidFrom().getTime() >= hourlyRate.getValidFrom().getTime() && entity.getValidTo().getTime()<= hourlyRate.getValidTo().getTime())
						||(entity.getValidFrom().getTime() <= hourlyRate.getValidFrom().getTime() && entity.getValidTo().getTime()>= hourlyRate.getValidTo().getTime())  
						|| ((entity.getValidFrom().getTime() >= hourlyRate.getValidFrom().getTime() && entity.getValidFrom().getTime()<=hourlyRate.getValidTo().getTime()) && entity.getValidTo().getTime()>= hourlyRate.getValidTo().getTime())
						|| (entity.getValidFrom().getTime() <= hourlyRate.getValidFrom().getTime() && (entity.getValidTo().getTime()>=hourlyRate.getValidFrom().getTime()   && entity.getValidTo().getTime()<= hourlyRate.getValidTo().getTime()))){       		
					result="error";      		   
					return result;
				}      	
			}   
		}   
		return "";
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") HourlyRate entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute("errors",
					"This" + entity.getClass().getSimpleName() + " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
			
		addMsg(request, "Hourly rate deleted successfully");
		
		String redirectUrl = "redirect:/" + urlContext + "/list.do";
		if (isRequestFromAlertPage(request)) {
			redirectUrl = "redirect:/" + hourlyPayRateAlertListPg;
		}
		return redirectUrl;
	}
	
	@RequestMapping("/changeAlertStatus.do")
	public String changeAlertStatus(HttpServletRequest request, ModelMap modMap) {
		HourlyRate hourlyRate = genericDAO.getById(HourlyRate.class, Long.valueOf(request.getParameter("id")));
		String newStatus = request.getParameter("status");
		if (!StringUtils.equals(newStatus, String.valueOf(hourlyRate.getAlertStatus()))) { 
			hourlyRate.setAlertStatus(Integer.valueOf(newStatus));
			genericDAO.save(hourlyRate);
			addMsg(request, "Hourly rate alert status changed successfully");
		}
		
		String redirectUrl = "redirect:/" + urlContext + "/list.do";
		if (isRequestFromAlertPage(request)) {
			redirectUrl = "redirect:/" + hourlyPayRateAlertListPg;
		}
		
		return redirectUrl;
	}
	
	private boolean isRequestFromAlertPage(HttpServletRequest request) {
		String fromAlertPage = request.getParameter(fromAlertPageIndicator);
		return (StringUtils.isNotEmpty(fromAlertPage) && BooleanUtils.toBoolean(fromAlertPage));
	}

	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
	{
		
		System.out.println("~~~~~~~~~~~~~~~AJAX MODE:");
		if("findDCompany".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> company=new ArrayList<Location>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				company.add(driver.getCompany());
				Gson gson = new Gson();
				return gson.toJson(company);
			}
		
		}
		if("findDTerminal".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> terminal=new ArrayList<Location>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				terminal.add(driver.getTerminal());
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
