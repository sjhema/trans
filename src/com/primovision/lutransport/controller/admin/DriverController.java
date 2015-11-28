package com.primovision.lutransport.controller.admin;

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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.ProbationType;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.service.DateUpdateService;
/**
 * @author sudhir
 */

@Controller
@RequestMapping("/hr/employee")
public class DriverController extends CRUDController<Driver>{
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
	public DriverController() {
		setUrlContext("/hr/employee");
	}
	
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
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("employeestatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
		
		Map<String,Object> map1=new HashMap<String,Object>();
		map1.put("dataType", "SHIFT");
		map1.put("dataValue", "1,2");
		model.addAttribute("employeeshift", genericDAO.findByCriteria(StaticData.class, map1,"dataText",false));
		
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("dataType", "Pay Term");
		map2.put("dataValue", "1,2,3");
		model.addAttribute("payterm", genericDAO.findByCriteria(StaticData.class, map2,"dataValue",false));
		
		criterias.clear();
		criterias.put("probationCategory", "PayRoll");
		model.addAttribute("payrollProbationTypes", genericDAO.findByCriteria(ProbationType.class, criterias,"probationName",false));
		
		criterias.clear();
		criterias.put("probationCategory", "Leave");
		model.addAttribute("leaveProbationTypes", genericDAO.findByCriteria(ProbationType.class, criterias,"probationName",false));
		
		criterias.clear();
		criterias.put("probationCategory", "General");
		model.addAttribute("generalProbationTypes", genericDAO.findByCriteria(ProbationType.class, criterias,"probationName",false));
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("id")));	
		Date joinedDate = null;
		
		if(driver.getDateHired()!=null && driver.getDateReHired()==null){
			joinedDate = driver.getDateHired();
		}
		else if(driver.getDateHired()==null && driver.getDateReHired()!=null){
			joinedDate = driver.getDateReHired();
		}
		else if(driver.getDateHired()!=null && driver.getDateReHired()!=null){
			joinedDate = driver.getDateReHired();
		}
		
		String payrollProbationQuery = "select obj from ProbationType obj where 1=1 and obj.company='"+driver.getCompany().getId()+"' and obj.terminal='"+driver.getTerminal().getId()
		+"' and obj.empCategory='"+driver.getCatagory().getId()+"' and obj.dateFrom<='"+joinedDate+"' and obj.dateTo>='"+joinedDate+"' and obj.probationCategory='PayRoll'";
		
		String leaveProbationQuery = "select obj from ProbationType obj where 1=1 and obj.company='"+driver.getCompany().getId()+"' and obj.terminal='"+driver.getTerminal().getId()
		+"' and obj.empCategory='"+driver.getCatagory().getId()+"' and obj.dateFrom<='"+joinedDate+"' and obj.dateTo>='"+joinedDate+"' and obj.probationCategory='Leave'";
		
		model.addAttribute("payrollProbationTypes",genericDAO.executeSimpleQuery(payrollProbationQuery));
		
		model.addAttribute("leaveProbationTypes",genericDAO.executeSimpleQuery(leaveProbationQuery) );
		return urlContext + "/form";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		//setupCreate(model, request);
		String query="select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		Map criterias = new HashMap();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("employeestatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
		Map<String,Object> map1=new HashMap<String,Object>();
		map1.put("dataType", "SHIFT");
		map1.put("dataValue", "1,2");
		model.addAttribute("employeeshift", genericDAO.findByCriteria(StaticData.class, map1,"dataText",false));
	
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("dataType", "Pay Term");
		map2.put("dataValue", "1,2,3");
		model.addAttribute("payterm", genericDAO.findByCriteria(StaticData.class, map2,"dataValue",false));
	
	}
	
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
		binder.registerCustomEditor(ProbationType.class, new AbstractModelEditor(ProbationType.class));
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"lastName asc,firstName asc,status desc",null,null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "Hireddate", "dateHired");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"lastName asc,firstName asc,status desc",null,null));
		return urlContext + "/list";
		
		
	}

	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Driver entity,
			BindingResult bindingResult, ModelMap model) {
		boolean err=true;
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option",
					null, null);
			err=false;
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
			err=false;
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
					null, null);
			err=false;
		}		
		
		
		if(StringUtils.isEmpty(entity.getLastName())){
			bindingResult.rejectValue("lastName", "NotEmpty.java.lang.String",
					null, null);
			err=false;
		}
		else{
			 entity.setLastName(entity.getLastName().trim());
		}
		
		if(StringUtils.isEmpty(entity.getFirstName())){
			bindingResult.rejectValue("firstName", "NotEmpty.java.lang.String",
					null, null);
			err=false;
		}
		else{
			entity.setFirstName(entity.getFirstName().trim());
		}
		
		if(entity.getDateHired()!=null&&entity.getDateTerminated()!=null){
			if(entity.getDateTerminated().before(entity.getDateHired())){
				bindingResult.rejectValue("dateTerminated", "error.textbox.terminatedDate",
						null, null);
				err=false;
			}
		}
		
		if(entity.getDateHired()==null && entity.getDateReHired()==null){
			 setupCreate(model, request);
			 request.getSession().setAttribute("error","Either Hired date or Re-Hired date is required!");
			return urlContext + "/form";
		}
		 if(entity.getDateTerminated()!=null&&entity.getStatus()==1){
			 bindingResult.rejectValue("status", "error.textbox.status",
						null, null);
			}
		
		 
		entity.setFullName(entity.getLastName()+" "+entity.getFirstName());
		
		/*Calendar calObj1 = Calendar.getInstance();
		if(entity.getDateProbationStart()!=null)
			calObj1.setTime(entity.getDateProbationStart());
		
		Calendar calObj2 = Calendar.getInstance();
		if(entity.getDateProbationEnd()!=null)
			calObj2.setTime(entity.getDateProbationEnd());
		
		Calendar cal1 = new GregorianCalendar();
		 Calendar cal2 = new GregorianCalendar();
		 if(entity.getDateProbationStart()!=null&&entity.getDateProbationEnd()!=null){
		 cal1.set(calObj1.get(Calendar.YEAR),calObj1.get(Calendar.MONTH),calObj1.get(Calendar.DAY_OF_MONTH));
		 cal2.set(calObj2.get(Calendar.YEAR),calObj2.get(Calendar.MONTH),calObj2.get(Calendar.DAY_OF_MONTH));
		long d=(cal2.getTime().getTime()-cal1.getTime().getTime())/(1000 * 60 * 60 * 24);
		//int d=entity.getDateProbationStart().compareTo(entity.getDateProbationEnd());
		entity.setProbationDays((int)d+1);
		 }*/
		
		
		 if(entity.getCompany()!=null&&entity.getStaffId()!=null&&err){
			 StringBuffer query=new StringBuffer("");
			 query.append("select obj from Driver obj where obj.status=1 and obj.company.id="+entity.getCompany().getId()+" and obj.staffId='"+entity.getStaffId()+"'");
			 if(entity.getId()!=null){
					query.append("and obj.id!="+entity.getId());
				}
			List<Driver> list=genericDAO.executeSimpleQuery(query.toString());
			if(list.size()>=1&&entity.getStatus()==1){
				setupCreate(model, request);
				request.getSession().setAttribute("error","Duplicate Employee Id");
				return urlContext + "/form";
			}
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
			
			beforeSave(request, entity, model);	
	
			try{
				
				
				/*if((entity.getDateReHired()!=null && entity.getDateHired()!=null) || entity.getDateHired()!=null ){
					String dateJoined=dateFormat.format(entity.getDateHired());		    	    
					LocalDate jodaJoinedDate=new LocalDate(dateJoined);				
					LocalDate currentDate = new LocalDate();					
					int days = Days.daysBetween(jodaJoinedDate, currentDate).getDays();
					if(days >= 365){
						entity.setLeaveQualifier("R");
					}
					else if(days >= entity.getProbationDays() && days < 365){
						entity.setLeaveQualifier("P");
					}
				
				}				
				else if (entity.getDateReHired()!=null && entity.getDateHired()==null  ){
					String dateJoined=dateFormat.format(entity.getDateReHired());		    	    
					LocalDate jodaJoinedDate=new LocalDate(dateJoined);				
					LocalDate currentDate = new LocalDate();
					
					int days = Days.daysBetween(jodaJoinedDate, currentDate).getDays();
					
					if(days >= 365){
						entity.setLeaveQualifier("R");
					}
					else if(days >= entity.getProbationDays() && days < 365){
						entity.setLeaveQualifier("P");
					}
				}*/
				
				if(entity.getCompany().getId() == 4l){
					Map criti = new HashMap();
					criti.clear();
					criti.put("company.id", entity.getCompany().getId());
					criti.put("terminal.id", entity.getTerminal().getId());
					criti.put("category.id", entity.getCatagory().getId());
					criti.put("leavetype.id", 1l);
					criti.put("leaveQualifier","4");
					criti.put("status",1);
					List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criti);
					if(ptods.size()>0 && ptods!=null){				
						Ptod ptodObj = ptods.get(0);
					DateTime dt = new DateTime();
					if((entity.getDateReHired()!=null && entity.getDateHired()!=null) || entity.getDateHired()!=null ){
						int totex=Days.daysBetween(new DateMidnight(entity.getDateHired()), new DateMidnight(dt)).getDays();
						if(totex>ptodObj.getExperienceindays()){
							entity.setLeaveQualifier("WBR");
						}
						else{
							entity.setLeaveQualifier("WBP");
						}
					}
					else if(entity.getDateReHired()!=null){
						int totex=Days.daysBetween(new DateMidnight(entity.getDateReHired()), new DateMidnight(dt)).getDays();
						if(totex>ptodObj.getExperienceindays()){
							entity.setLeaveQualifier("WBR");
						}
						else{
							entity.setLeaveQualifier("WBP");
						}
					}
					}
					else{
						entity.setLeaveQualifier("WBR");
					}
				}
				
				
				if((entity.getDateReHired()!=null && entity.getDateHired()!=null) || entity.getDateHired()!=null ){
					String dateJoined=dateFormat.format(entity.getDateHired());		    	    
					LocalDate jodaJoinedDate=new LocalDate(dateJoined);	
					LocalDate annviDate= jodaJoinedDate.plusYears(1);	
					entity.setAnniversaryDate(annviDate.toDate());	
				}
				else if(entity.getDateReHired()!=null){
					String dateJoined=dateFormat.format(entity.getDateReHired());		    	    
					LocalDate jodaJoinedDate=new LocalDate(dateJoined);	
					LocalDate annviDate= jodaJoinedDate.plusYears(1);	
					entity.setAnniversaryDate(annviDate.toDate());
				}
				
				if((entity.getDateReHired()!=null && entity.getDateHired()!=null) || entity.getDateHired()!=null ){
					String dateJoined=dateFormat.format(entity.getDateHired());		    	    
					LocalDate jodaJoinedDate=new LocalDate(dateJoined);				
					LocalDate currentDate = new LocalDate();				
					LocalDate checkForAnnviDate=jodaJoinedDate.withYear(currentDate.year().get());
					if(checkForAnnviDate.isAfter(currentDate))
						entity.setNextAnniversaryDate(checkForAnnviDate.toDate());	
					else {
						LocalDate AnnviDate=checkForAnnviDate.plusYears(1);
						entity.setNextAnniversaryDate(AnnviDate.toDate());
					}
						
				}
				else if(entity.getDateReHired()!=null){
					String dateJoined=dateFormat.format(entity.getDateReHired());						
					LocalDate jodaJoinedDate=new LocalDate(dateJoined);
					LocalDate currentDate = new LocalDate();					
					LocalDate checkForAnnviDate=jodaJoinedDate.withYear(currentDate.year().get());
					if(checkForAnnviDate.isAfter(currentDate))
						entity.setNextAnniversaryDate(checkForAnnviDate.toDate());	
					else {
						LocalDate AnnviDate=checkForAnnviDate.plusYears(1);
						entity.setNextAnniversaryDate(AnnviDate.toDate());
					}					
				}
			}
			catch(Exception e){
				System.out.println("An EXception Occured while parsing date in Driver controller");
			}
			
			genericDAO.saveOrUpdate(entity);
		
		  // merge into datasource
				
				cleanUp(request);
				// return to list
		     
		     return "redirect:/" + urlContext + "/list.do";
	}
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") Driver entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			 genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
			request.getSession().setAttribute("error","Cannot delete a parent row");
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
	
	
	
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if ("search".equalsIgnoreCase(action)) {
			String notes="";
			if (!StringUtils.isEmpty(request.getParameter("OldStaffId"))){
				Map criterias = new HashMap();
				criterias.put("staffId",request.getParameter("OldStaffId"));
				/*Employee employee=genericDAO.getByCriteria(Employee.class, criterias);*/
				List<Driver> employee=genericDAO.findByCriteria(Driver.class, criterias);
				if(employee.isEmpty()){
					notes="staff id is not available";
				}
				else{
				notes="staff id is available";
				}
			}
			Gson gson = new Gson();
			return gson.toJson(notes);
		}
		if("findDCompany".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("employee"))){
				List<Location> company=new ArrayList<Location>();
				Driver employee=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("employee")));
				company.add(employee.getCompany());
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
		if("findDTerminal".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("company"))&&!StringUtils.isEmpty(request.getParameter("staffId"))){
				List<Location> terminal=new ArrayList<Location>();
				Map criterias=new HashMap();
				Long cmpid=Long.parseLong(request.getParameter("company"));
				criterias.put("status", 1);
				criterias.put("company.id", cmpid);
				criterias.put("staffId", (String)request.getParameter("staffId"));
				
				Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
				if(employee!=null){
				terminal.add(employee.getTerminal());
				}
				
				Gson gson = new Gson();
				if(employee==null){
					return gson.toJson(".");
				}else{
					
					return gson.toJson(terminal);
				}
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
			if(!StringUtils.isEmpty(request.getParameter("company"))){
				List<EmployeeCatagory> category=new ArrayList<EmployeeCatagory>();
				Map criterias=new HashMap();
				Long cmpid=Long.parseLong(request.getParameter("company"));
				criterias.put("status", 1);
				criterias.put("company.id", cmpid);
				criterias.put("staffId", (String)request.getParameter("staffId"));
				
				Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
				if(employee!=null){
				category.add(employee.getCatagory());
				}
				Gson gson = new Gson();
				if(employee==null){
					return gson.toJson(".");
				}else{
					
					return gson.toJson(category);
				}
			}
			else{
				Map criterias = new HashMap();
				List<EmployeeCatagory> category=new ArrayList<EmployeeCatagory>();
				category=genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false);
				Gson gson = new Gson();
				return gson.toJson(category);
			}

		}
		if("findDFLnames".equalsIgnoreCase(action)){
			List<String> str=new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			Map criterias=new HashMap();
			Long cmpid=Long.parseLong(request.getParameter("company"));
			criterias.put("status", 1);
			criterias.put("company.id", cmpid);
			criterias.put("staffId", (String)request.getParameter("staffId"));
			
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				str.add(employee.getLastName());
				str.add(employee.getFirstName());				
				if(employee.getDateHired()!=null)
				str.add(sdf.format(employee.getDateHired()));
				else
				str.add(null);	
				if(employee.getDateReHired()!=null)
				str.add(sdf.format(employee.getDateReHired()));	
				else
				str.add(null);	
			}
			Gson gson = new Gson();
			if(employee!=null){
				return gson.toJson(str);
			}else{
				return gson.toJson(".");
			}
				
		}
		
		if("getProbationDates".equalsIgnoreCase(action)){			
			List str=new ArrayList();
			try{
			int numberOfDays = 0;
			if(!StringUtils.isEmpty(request.getParameter("probationTypeId"))){
				ProbationType obj = genericDAO.getById(ProbationType.class,Long.parseLong(request.getParameter("probationTypeId")));
				numberOfDays= obj.getNoOfDays();
			}
			
			String datejoined=dateFormat.format(sdf.parse(request.getParameter("joinedDate")));	    
		    	
			LocalDate jodaJoinedDate=new LocalDate(datejoined);
			LocalDate probEndDate=jodaJoinedDate.plusDays(numberOfDays);
			String probationEndDate=sdf.format(probEndDate.toDate());
			str.add(probationEndDate);
			str.add(numberOfDays);
			Gson gson = new Gson();
			return gson.toJson(str);
			}
			catch (Exception e) {				
				e.printStackTrace();
				return "";
			}
			
		}
		
		
		if("getPayrollProbationTypes".equalsIgnoreCase(action)){			
			try{				
			String datejoined=dateFormat.format(sdf.parse(request.getParameter("joinedDate")));			
			LocalDate jodaJoinedDate=new LocalDate(datejoined);			
			String query = "select obj from ProbationType obj where 1=1 and obj.company='"+request.getParameter("company")+"' and obj.terminal='"+request.getParameter("terminal")
			+"' and obj.empCategory='"+request.getParameter("category")+"' and obj.dateFrom<='"+dateFormat.format(jodaJoinedDate.toDate())+" 00:00:00' and obj.dateTo>='"+dateFormat.format(jodaJoinedDate.toDate())+" 00:00:00' and obj.probationCategory='PayRoll'";
			List<ProbationType> list = genericDAO.executeSimpleQuery(query);
			
			Gson gson = new Gson();
			return gson.toJson(list);
			}
			catch (Exception e) {				
				e.printStackTrace();
				return "";
			}
			
		}
		
		if("getLeaveProbationTypes".equalsIgnoreCase(action)){			
			try{	
			String datejoined=dateFormat.format(sdf.parse(request.getParameter("joinedDate")));
			String query = "select obj from ProbationType obj where 1=1 and obj.company='"+request.getParameter("company")+"' and obj.terminal='"+request.getParameter("terminal")
			+"' and obj.empCategory='"+request.getParameter("category")+"' and obj.dateFrom<='"+datejoined+" 00:00:00' and obj.dateTo>='"+datejoined+" 00:00:00' and obj.probationCategory='Leave'";
			List<ProbationType> list = genericDAO.executeSimpleQuery(query);
			
			Gson gson = new Gson();
			return gson.toJson(list);
			}
			catch (Exception e) {				
				e.printStackTrace();
				return "";
			}
			
		}
		

		return "";
	}
}
