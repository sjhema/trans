package com.primovision.lutransport.controller.hr;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;


import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.Eligibility;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.SetupData;
import com.primovision.lutransport.model.SearchCriteria;
/*import com.primovision.lutransport.model.State;*/
import com.primovision.lutransport.model.StaticData;
/*import com.primovision.lutransport.model.Vehicle;*/

/**
 * @author Subodh
 */

@Controller
@RequestMapping("/hr/ptod")
public class PtodController extends CRUDController<Ptod> {

	public PtodController() {
		setUrlContext("/hr/ptod");
	}

	
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
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		//model.addAttribute("LeaveTypes", listStaticData("LEAVE_TYPE"));
	    model.addAttribute("ptodstatus", listStaticData("POTD_STATUS"));
		
		model.addAttribute("annualaccrual", listStaticData("Annual_ Accrual"));
		/*map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");*/
		criterias.clear();
		Map<String,Object> map=new HashMap<String,Object>();/*Annual_ Accrual*/
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("ptodstatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
		model.addAttribute("calculate", listStaticData("YES_NO"));


		
		Map<String,Object> map1=new HashMap<String,Object>();
		map1.put("dataType", "LeaveQualifier");
		map1.put("dataValue", "1,2,3");
		model.addAttribute("leavequalifier", genericDAO.findByCriteria(StaticData.class, map1,"dataText",false));
		
		
		
		model.addAttribute("experienceyears", genericDAO.executeSimpleQuery("select obj from SetupData obj where dataType='EXPERIENCE'  order by ABS(obj.dataValue)"));
		
		
		criterias.clear();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		model.addAttribute("leavetypes", genericDAO.findByCriteria(LeaveType.class, criterias,"name",false));
	}



	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
	    populateSearchCriteria(request, request.getParameterMap());
	    setupCreate(model, request);
		
	}

	
	@Override	
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"company asc,terminal asc,category asc,leavetype asc,experienceinyears asc,experienceinyearsTo",false,null));
		return urlContext + "/list";
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"company asc,terminal asc,category asc,leavetype asc,experienceinyears asc,experienceinyearsTo",false,null));
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") Ptod entity,
			BindingResult bindingResult, ModelMap model) 
	{
		
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}
		
		if(entity.getCategory()==null){
			bindingResult.rejectValue("category", "error.select.option", null, null);
		}
		
		
		if(entity.getLeavetype()==null){
			bindingResult.rejectValue("leavetype", "error.select.option", null, null);
		}
		
		if(StringUtils.isEmpty(entity.getLeaveQualifier())){
			bindingResult.rejectValue("leaveQualifier", "error.select.option", null, null);
		}
		
		
		if(entity.getRate()==null&&entity.getCategory()!=null){
			if(entity.getCategory().getId()!=3||entity.getCategory().getId()!=6){
				/*bindingResult.rejectValue("rate", "error.NotNull.field", null, null);*/
			}
		}
					
			  if(entity.getExperienceindays()==null){
				  entity.setExperienceindays(0);
			  }
			  if(entity.getExperienceinyears()==null){
				  entity.setExperienceinyears(0);	
			  }
			  if(entity.getExperienceinyearsTo()==null){
				  entity.setExperienceinyearsTo(0);
			  }
			  if(entity.getDayearned()==null){
				  entity.setDayearned(0.0);
			  }
			  if(entity.getHoursearned()==null){
				  entity.setHoursearned(0.0);	
			  }						
			  if(entity.getRate()==null){
				   entity.setRate(0.0);
			  }
			  if(entity.getHourlyrate()==null){
				  entity.setHourlyrate(0.0);	
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
			/*System.out.println("\nsave--bindingResult.hasErrors()\n");*/
			System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			return urlContext + "/form";
		}
		
		
		if(entity.getId()!=null){
			
			StringBuffer query=new StringBuffer("");
			query.append("select obj from Ptod obj where obj.id not in("+entity.getId()+") and obj.company="+entity.getCompany().getId()+" and obj.terminal="+entity.getTerminal().getId()+" and obj.category="+entity.getCategory().getId()+"  and obj.leavetype='"+entity.getLeavetype().getId()+"'");
		    query.append(" and obj.experienceindays="+entity.getExperienceindays());				
			query.append(" and obj.experienceinyears="+entity.getExperienceinyears());
			query.append(" and obj.experienceinyearsTo="+entity.getExperienceinyearsTo());
			
		    List<Ptod> ptod = genericDAO.executeSimpleQuery(query.toString());
		    if (ptod!=null && ptod.size()>0){	
			      System.out.println("\nDuplicate ptod\n");
		          request.setAttribute("error","duplicate Ptod!");
			      setupUpdate(model, request);
			     return urlContext + "/form";
         	}		
			
			/*boolean duplicat=false;
			Ptod currentPto=genericDAO.getById(Ptod.class,entity.getId());
			long curcompany=currentPto.getCompany().getId();
			long curterminal=currentPto.getTerminal().getId();
			long curcategory=currentPto.getCategory().getId();
			long curleavetype=currentPto.getLeavetype().getId();
			String query = "select obj from Ptod obj where 1=1 and obj.id not in("+entity.getId()+")";
			
			
			
			List<Ptod> ptods = genericDAO.executeSimpleQuery(query);
			if (ptods!=null && ptods.size()>0){	
				for(Ptod ptod:ptods){
					long company=ptod.getCompany().getId();
					long terminal=ptod.getTerminal().getId();
					long category=ptod.getCategory().getId();
					long leavtype=ptod.getLeavetype().getId();
					
					if(company==curcompany){
						//System.out.println("\ncomany same\n");
						if(terminal==curterminal){
						//System.out.println("\nterminal same\n");
							if(category==curcategory){
							//System.out.println("\ncategory same\n");
							//if(ptod.getLeavetype().equals(currentPto.getLeavetype())){
								if(leavtype==curleavetype){
								//System.out.println("\nleave same\n");
									System.out.println("\nduplicat\n");
									duplicat=true;
									break;
								}
							}
						}
					}
				}
			}
			if(duplicat==true){
                   request.setAttribute("error","duplicate Ptod!");
				   setupUpdate(model, request);
				  return urlContext + "/form";
			}*/			
		}
		
		
		
		if(entity.getCompany()!=null && entity.getTerminal()!=null && entity.getCategory()!=null && entity.getLeavetype()!=null && entity.getId()==null){
		  StringBuffer query=new StringBuffer("");
			/* String*/ query.append("select obj from Ptod obj where obj.company="+entity.getCompany().getId()+" and obj.terminal="+entity.getTerminal().getId()+" and obj.category="+entity.getCategory().getId()+"  and obj.leavetype='"+entity.getLeavetype().getId()+"'"+"  and obj.leaveQualifier='"+entity.getLeaveQualifier()+"'");
		   //System.out.println("\nquery==>"+query+"\n");
			if(entity.getExperienceindays()!=null){
				query.append("and obj.experienceindays="+entity.getExperienceindays());				
			}
			if(entity.getExperienceinyears()!=null){
				query.append(" and obj.experienceinyears="+entity.getExperienceinyears());
			}
			if(entity.getExperienceinyearsTo()!=null){
				query.append("and obj.experienceinyearsTo="+entity.getExperienceinyearsTo());
			}
		   List<Ptod> ptod = genericDAO.executeSimpleQuery(query.toString());
		   if (ptod!=null && ptod.size()>0){	
			      System.out.println("\nDuplicate ptod\n");
		          request.setAttribute("error","duplicate Ptod!");
			      setupUpdate(model, request);
			     return urlContext + "/form";
         	}
		}
		
		/*System.out.println("\nbefore save\n");
		System.out.println("\nexperienceindays==>"+entity.getExperienceindays()+"\n");*/
		////
		
		beforeSave(request, entity, model);
		// merge into datasource
		System.out.println("\nCRUD1\n");
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list		
		return "redirect:/" + urlContext + "/list.do";	
	}
        
		
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		int totaldays=0;
		if ("findExp".equalsIgnoreCase(action)) {
			//System.out.println("\nprocessAjax--PTOD\n");
			List<Double> list = new ArrayList<Double>();
			//String terminalName = "";
			Date dateHired=null;
			String employeeId = request.getParameter("driver");
			if (!StringUtils.isEmpty(employeeId)) {
				Driver driver = genericDAO.getById(Driver.class,Long.valueOf(employeeId));
				request.getSession().setAttribute("driver", driver);
				dateHired = driver.getDateHired();
				
				int days = (int) ((new Date().getTime()-dateHired.getTime()) / (1000 * 60 * 60 * 24));		
				System.out.println("\ndiffInDays===>"+days+"\n");
				
				int expYears=0,earneddays=0;
				
				if(days<=364){
					expYears=0;
					if(days>=0 && days<=44){
						earneddays=0;
					}
				}
				else{
					expYears=days/365;
					days=days%365;
					earneddays=expYears*4;
				}
				/*
				System.out.println("\nfirst=="+(789/365)+"\n");//first==2
				System.out.println("\nsecond=="+(789%365)+"\n");//second==59
				System.out.println("\nfirst=="+(203/365)+"\n");//first==0
				System.out.println("\nsecond=="+(203%365)+"\n");//second==203*/				/* DecimalFormat df = new DecimalFormat("#0.00");
				String days = df.format(totaldays);
				String years = df.format(ageYears);*/
				double expday=days;
				double expyear=expYears;
				double earnedday=earneddays;
				list.add(expday);
				list.add(expyear);
				list.add(earnedday);
			    /*System.out.println("\ndateHired=="+dateHired+"\n");
			    System.out.println("\nyear===>"+ageYears+"\n");
			    System.out.println("\nmonth===>"+ageMonths+"\n");
			    System.out.println("\nday===>"+ageDays+"\n");
			    System.out.println("\ntotaldays===>"+totaldays+"\n");*/
				
				
			}
			Gson gson = new Gson();
			return gson.toJson(list);
		}
		if ("checkelgi".equalsIgnoreCase(action)) {
			List<String> responce=new ArrayList<String>();
			String companyId = request.getParameter("company");
			String terminalId = request.getParameter("terminal");
			String categoryId = request.getParameter("category");
			String leavetypeId = request.getParameter("leavetype");
			Map criterias=new HashMap();
			criterias.put("company.id", Long.valueOf(companyId));
			criterias.put("terminal.id", Long.valueOf(terminalId));
			criterias.put("catagory.id", Long.valueOf(categoryId));
			criterias.put("leaveType.id", Long.valueOf(leavetypeId));
			Eligibility eligibility=genericDAO.getByCriteria(Eligibility.class, criterias);
			if(eligibility!=null){
				responce.add((eligibility.getProbationDays() != null) ? eligibility.getProbationDays()+"" : ".");
				responce.add((eligibility.getProbationYear() != null) ? eligibility.getProbationYear()+"" : ".");
				responce.add((eligibility.getBeforeDays() != null) ? eligibility.getBeforeDays()+"" : ".");
				responce.add((eligibility.getAfterDays() != null) ? eligibility.getAfterDays()+"" : ".");
				responce.add((eligibility.getAfterDays() != null) ? eligibility.getAfterDays()+"" : ".");
				responce.add((eligibility.getWorkForWeek() != null) ? eligibility.getWorkForWeek()+"" : ".");
				responce.add((eligibility.getPriorNoticeDays() != null) ? eligibility.getPriorNoticeDays()+"" : ".");
				responce.add((eligibility.getPriorNoticeWeeks() != null) ? eligibility.getPriorNoticeWeeks()+"" : ".");
			}
				
			responce.add("null");
			Gson gson = new Gson();
			return gson.toJson(responce);
		}
		return "";
		
	}
	
	
}
