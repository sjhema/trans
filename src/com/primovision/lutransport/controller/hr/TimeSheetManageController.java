package com.primovision.lutransport.controller.hr;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IntegerField;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestParam;


import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.hr.Attendance;
import com.primovision.lutransport.model.hr.Eligibility;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HoursMinutes;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.Minutes;
import com.primovision.lutransport.model.hr.HourlyRate;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hr.ShiftCalendar;
import com.primovision.lutransport.model.hr.TimeSheet;
import com.primovision.lutransport.model.SearchCriteria;
/*import com.primovision.lutransport.model.State;*/
import com.primovision.lutransport.model.StaticData;
/*import com.primovision.lutransport.model.Vehicle;*/

/**
 * @author Subodh
 */

@Controller
@RequestMapping("/hr/timesheetmanage")
public class TimeSheetManageController extends CRUDController<TimeSheet> {

	public TimeSheetManageController() {
		setUrlContext("/hr/timesheetmanage");
	}
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void initBinder(WebDataBinder binder) {
		// SimpleDateFormat("yyyy-MM-dd")
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class, false));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(ShiftCalendar.class, new AbstractModelEditor(ShiftCalendar.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
		
	}

	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		String mode = request.getParameter("mode");
		if (StringUtils.equals(TimeSheet.ADD_TO_PREV_MODE, mode)) {
			TimeSheet origEntity = (TimeSheet) model.get("modelObject");
			if (origEntity.getHourlypayrollinvoiceDate() == null) {
				request.getSession().setAttribute("error", "Unable to add - Timesheet not in paid status");
				return "redirect:list.do";
			}
			
			TimeSheet newEntity = new TimeSheet();
			newEntity.setDriver(origEntity.getDriver());
			newEntity.setCompany(origEntity.getCompany());
			newEntity.setTerminal(origEntity.getTerminal());
			newEntity.setCatagory(origEntity.getCatagory());
			//newEntity.setBatchdate(origEntity.getBatchdate());
			newEntity.setWeeklyHours(origEntity.getWeeklyHours());
			newEntity.setDailyHours(origEntity.getDailyHours());
			newEntity.setMode(TimeSheet.ADD_TO_PREV_MODE);
			
			model.addAttribute("modelObject", newEntity);
		} 
		
		setupUpdate(model, request);
		return urlContext + "/form";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		User userObj = (User) request.getSession().getAttribute("userInfo");
		if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Przemyslaw Szozda")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where obj.id in (6,297) and obj.type in (3) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where obj.id in (95,99) and obj.type in (4) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select obj from Driver obj where obj.status=1 and obj.company in (6,297) and obj.terminal in (95,99) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Bozena Szozda")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (5,297) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (100) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select obj from Driver obj where obj.status=1 and obj.company in (5,297) and obj.terminal in (100) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Iwona Bulawa")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (6,297) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (99) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select obj from Driver obj where obj.status=1 and obj.company in (6,297) and obj.terminal in (99) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Mohamed Diawara")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (4) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (93) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select obj from Driver obj where obj.status=1 and obj.company in (4) and obj.terminal in (93) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Maricela Camaraza")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (5) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (92,101) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select obj from Driver obj where obj.status=1 and obj.company in (5) and obj.terminal in (92,101) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else{
			criterias.put("type", 3);
			model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			criterias.put("type", 4);
			model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			criterias.put("status", 1);
			model.addAttribute("employees",genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		
		    model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		}
		model.addAttribute("hours", genericDAO.findByCriteria(HoursMinutes.class, criterias,"hoursvalues",false));
		model.addAttribute("minutes", genericDAO.findByCriteria(Minutes.class, criterias,"id",false));
		model.addAttribute("otstatuss", listStaticData("YES_NO"));
		criterias.put("dataType","YES_NO");
		model.addAttribute("dtstatuss", genericDAO.findByCriteria(StaticData.class, criterias,"dataValue",false));
		model.addAttribute("holidaystatuss", genericDAO.findByCriteria(StaticData.class, criterias,"dataValue",false));
		criterias.clear();
		model.addAttribute("ampm", listStaticData("AM_PM"));
		
		model.addAttribute("timesheetid",request.getParameter(getPkParam()));
		
		Map criteria = new HashMap();
		criteria.clear();
		criteria.put("dataType", "AM_PM");
		model.addAttribute("pmam", genericDAO.findByCriteria(StaticData.class, criteria,"dataValue",true));
		}

	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		User userObj = (User) request.getSession().getAttribute("userInfo");
		setupList(model, request);
		//setupCreate(model, request);
		//System.out.println("\nlist1\n");
		 SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// System.out.println("\nlist2\n");
		 if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Przemyslaw Szozda")){		
			 String countquery = "Select count(obj) from TimeSheet obj where 1=1 and obj.company in (6,297) and obj.terminal in (95,99) order by batchdate desc,driver.fullName asc";
			 String query = "Select obj from TimeSheet obj where 1=1 and obj.company in (6,297) and obj.terminal in (95,99) order by batchdate desc,driver.fullName asc";
			 
			 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
		        		         countquery.toString()).getSingleResult();        
				                 criteria.setRecordCount(recordCount.intValue());			 
			 model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList());
				return urlContext + "/list";
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Bozena Szozda")){
			 
			 String countquery = "Select count(obj) from TimeSheet obj where 1=1 and obj.company in (5,297) and obj.terminal in (100) order by batchdate desc,driver.fullName asc";
			 String query = "Select obj from TimeSheet obj where 1=1 and obj.company in (5,297) and obj.terminal in (100) order by batchdate desc,driver.fullName asc";
			 
			 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
		        		         countquery.toString()).getSingleResult();        
				                 criteria.setRecordCount(recordCount.intValue());			 
			 model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList());
				return urlContext + "/list";
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Iwona Bulawa")){			 
			 String countquery = "Select count(obj) from TimeSheet obj where 1=1 and obj.company in (6,297) and obj.terminal in (99) order by batchdate desc,driver.fullName asc";
			 String query = "Select obj from TimeSheet obj where 1=1 and obj.company in (6,297) and obj.terminal in (99) order by batchdate desc,driver.fullName asc";
			 
			 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
		        		         countquery.toString()).getSingleResult();        
				                 criteria.setRecordCount(recordCount.intValue());			 
			 model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList());
				return urlContext + "/list";
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Mohamed Diawara")){
			 
			 String countquery = "Select count(obj) from TimeSheet obj where 1=1 and obj.company in (4) and obj.terminal in (93) order by batchdate desc,driver.fullName asc";
			 String query = "Select obj from TimeSheet obj where 1=1 and obj.company in (4) and obj.terminal in (93) order by batchdate desc,driver.fullName asc";
			 
			 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
		        		         countquery.toString()).getSingleResult();        
				                 criteria.setRecordCount(recordCount.intValue());			 
			 model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList());
				return urlContext + "/list";
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Maricela Camaraza")){
			 
			 String countquery = "Select count(obj) from TimeSheet obj where 1=1 and obj.company in (5) and obj.terminal in (92,101) order by batchdate desc,driver.fullName asc";
			 String query = "Select obj from TimeSheet obj where 1=1 and obj.company in (5) and obj.terminal in (92,101) order by batchdate desc,driver.fullName asc";
			 
			 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
		        		         countquery.toString()).getSingleResult();        
				                 criteria.setRecordCount(recordCount.intValue());			 
			 model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList());
				return urlContext + "/list";
		 }
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"batchdate desc,driver.fullName asc",null,null));
		 //System.out.println("\nlist3\n");
		return urlContext + "/list";
	}
	
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		User userObj = (User) request.getSession().getAttribute("userInfo");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Przemyslaw Szozda")){			
			
			 StringBuilder countquery = new StringBuilder("Select count(obj) from TimeSheet obj where 1=1 ");
			 StringBuilder query = new StringBuilder("Select obj from TimeSheet obj where 1=1 ");
			 
			 
			
			if(criteria.getSearchMap().get("company.id")==null || criteria.getSearchMap().get("company.id").equals("")){ 
				countquery.append(" and obj.company in (6,297)");
			    query.append(" and obj.company in (6,297)");
		    } 
			else{
				countquery.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
				query.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("terminal.id")==null || criteria.getSearchMap().get("terminal.id").equals("")){
				countquery.append(" and terminal in (95,99)");
			    query.append(" and terminal in (95,99)");
		    }
			else{
				countquery.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			    query.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("driver.fullName")==null || criteria.getSearchMap().get("driver.fullName").equals("")){
			
			}
			else{
				countquery.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			    query.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			}
			
			if(criteria.getSearchMap().get("catagory.id")==null || criteria.getSearchMap().get("catagory.id").equals("")){
				
			}
			else{
				countquery.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			    query.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			}
			
			countquery.append(" order by batchdate desc,driver.fullName asc ");
			query.append(" order by batchdate desc,driver.fullName asc ");		
			
			
			Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
       		                    countquery.toString()).getSingleResult();        
		                        criteria.setRecordCount(recordCount.intValue());			 
	         model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());	
	         
	         return urlContext + "/list";
			 
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Bozena Szozda")){			 
			 
			 StringBuilder countquery = new StringBuilder("Select count(obj) from TimeSheet obj where 1=1");
			 StringBuilder query = new StringBuilder("Select obj from TimeSheet obj where 1=1");
			 
			 
			
			if(criteria.getSearchMap().get("company.id")==null || criteria.getSearchMap().get("company.id").equals("")){ 
				countquery.append(" and obj.company in (5,297)");
			    query.append(" and obj.company in (5,297)");
		    } 
			else{
				countquery.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
				query.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("terminal.id")==null || criteria.getSearchMap().get("terminal.id").equals("")){
				countquery.append(" and terminal in (100)");
			    query.append(" and terminal in (100)");
		    }
			else{
				countquery.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			    query.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("driver.fullName")==null || criteria.getSearchMap().get("driver.fullName").equals("")){
			
			}
			else{
				countquery.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			    query.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			}
			
			if(criteria.getSearchMap().get("catagory.id")==null || criteria.getSearchMap().get("catagory.id").equals("")){
				
			}
			else{
				countquery.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			    query.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			}
			
			countquery.append(" order by batchdate desc,driver.fullName asc ");
			query.append(" order by batchdate desc,driver.fullName asc ");		
			
			
			Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
       		                    countquery.toString()).getSingleResult();        
		                        criteria.setRecordCount(recordCount.intValue());			 
	         model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());	
	         
	         return urlContext + "/list";
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Iwona Bulawa")){			 
			 
			 StringBuilder countquery = new StringBuilder("Select count(obj) from TimeSheet obj where 1=1");
			 StringBuilder query = new StringBuilder("Select obj from TimeSheet obj where 1=1");
			 
			 
			
			if(criteria.getSearchMap().get("company.id")==null || criteria.getSearchMap().get("company.id").equals("")){ 
				countquery.append(" and obj.company in (6,297)");
			    query.append(" and obj.company in (6,297)");
		    } 
			else{
				countquery.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
				query.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("terminal.id")==null || criteria.getSearchMap().get("terminal.id").equals("")){
				countquery.append(" and terminal in (99)");
			    query.append(" and terminal in (99)");
		    }
			else{
				countquery.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			    query.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("driver.fullName")==null || criteria.getSearchMap().get("driver.fullName").equals("")){
			
			}
			else{
				countquery.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			    query.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			}
			
			if(criteria.getSearchMap().get("catagory.id")==null || criteria.getSearchMap().get("catagory.id").equals("")){
				
			}
			else{
				countquery.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			    query.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			}
			countquery.append(" order by batchdate desc,driver.fullName asc ");
			query.append(" order by batchdate desc,driver.fullName asc ");		
			
			
			Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
       		                    countquery.toString()).getSingleResult();        
		                        criteria.setRecordCount(recordCount.intValue());			 
	         model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());	
	         
	         return urlContext + "/list";
			 
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Mohamed Diawara")){
			 
			 
			 StringBuilder countquery = new StringBuilder("Select count(obj) from TimeSheet obj where 1=1");
			 StringBuilder query = new StringBuilder("Select obj from TimeSheet obj where 1=1");
			 
			 
			
			if(criteria.getSearchMap().get("company.id")==null || criteria.getSearchMap().get("company.id").equals("")){ 
				countquery.append(" and obj.company in (4)");
			    query.append(" and obj.company in (4)");
		    } 
			else{
				countquery.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
				query.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("terminal.id")==null || criteria.getSearchMap().get("terminal.id").equals("")){
				countquery.append(" and terminal in (93)");
			    query.append(" and terminal in (93)");
		    }
			else{
				countquery.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			    query.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("driver.fullName")==null || criteria.getSearchMap().get("driver.fullName").equals("")){
			
			}
			else{
				countquery.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			    query.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			}
			
			if(criteria.getSearchMap().get("catagory.id")==null || criteria.getSearchMap().get("catagory.id").equals("")){
				
			}
			else{
				countquery.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			    query.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			}
			countquery.append(" order by batchdate desc,driver.fullName asc ");
			query.append(" order by batchdate desc,driver.fullName asc ");		
			
			
			Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
       		                    countquery.toString()).getSingleResult();        
		                        criteria.setRecordCount(recordCount.intValue());			 
	         model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());	
	         
	         return urlContext + "/list";
		 }
		 else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Maricela Camaraza")){
			 			 
			 StringBuilder countquery = new StringBuilder("Select count(obj) from TimeSheet obj where 1=1");
			 StringBuilder query = new StringBuilder("Select obj from TimeSheet obj where 1=1");
			 
			 
			
			if(criteria.getSearchMap().get("company.id")==null || criteria.getSearchMap().get("company.id").equals("")){ 
				countquery.append(" and obj.company in (5)");
			    query.append(" and obj.company in (5)");
		    } 
			else{
				countquery.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
				query.append(" and obj.company in ("+criteria.getSearchMap().get("company.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("terminal.id")==null || criteria.getSearchMap().get("terminal.id").equals("")){
				countquery.append(" and terminal in (92,101)");
			    query.append(" and terminal in (92,101)");
		    }
			else{
				countquery.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			    query.append(" and terminal in ("+criteria.getSearchMap().get("terminal.id").toString()+")");
			}
				
			if(criteria.getSearchMap().get("driver.fullName")==null || criteria.getSearchMap().get("driver.fullName").equals("")){
			
			}
			else{
				countquery.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			    query.append(" and driver.fullName in ('"+criteria.getSearchMap().get("driver.fullName").toString()+"')");
			}
			if(criteria.getSearchMap().get("catagory.id")==null || criteria.getSearchMap().get("catagory.id").equals("")){
				
			}
			else{
				countquery.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			    query.append(" and catagory in ('"+criteria.getSearchMap().get("catagory.id").toString()+"')");
			}
			countquery.append(" order by batchdate desc,driver.fullName asc ");
			query.append(" order by batchdate desc,driver.fullName asc ");		
			
			
			Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
       		                    countquery.toString()).getSingleResult();        
		                        criteria.setRecordCount(recordCount.intValue());			 
	         model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());	
	         
	         return urlContext + "/list";
		 }
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"batchdate desc,driver.fullName asc",null,null));
		return urlContext + "/list";
	}
	

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
	    populateSearchCriteria(request, request.getParameterMap());
	  //  setupCreate(model, request);
	    Map criterias = new HashMap();
	    User userObj = (User) request.getSession().getAttribute("userInfo");
		if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Przemyslaw Szozda")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (6,297) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (95,99) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select distinct(obj.fullName) from Driver obj where obj.status=1 and obj.company in (6,297) and obj.terminal in (95,99) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Bozena Szozda")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (5,297) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (100) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select distinct(obj.fullName) from Driver obj where obj.status=1 and obj.company in (5,297) and obj.terminal in (100) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Iwona Bulawa")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (6,297) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (99) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select distinct(obj.fullName) from Driver obj where obj.status=1 and obj.company in (6,297) and obj.terminal in (99) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Mohamed Diawara")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (4) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (93) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select distinct(obj.fullName) from Driver obj where obj.status=1 and obj.company in (4) and obj.terminal in (93) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else if((userObj.getFirstName()+" "+userObj.getLastName()).equalsIgnoreCase("Maricela Camaraza")){
			model.addAttribute("companies",genericDAO.executeSimpleQuery("select obj from Location obj where id in (5) order by name asc"));
			model.addAttribute("terminals", genericDAO.executeSimpleQuery("select obj from Location obj where id in (92,101) order by name asc"));
			model.addAttribute("employees",genericDAO.executeSimpleQuery("select distinct(obj.fullName) from Driver obj where obj.status=1 and obj.company in (5) and obj.terminal in (92,101) order by fullName asc"));
			model.addAttribute("catagories", genericDAO.executeSimpleQuery("select obj from EmployeeCatagory obj"));
		}
		else{
			criterias.put("type", 3);
			model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			criterias.put("type", 4);
			model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			/*criterias.put("status", 1);
			model.addAttribute("employees",genericDAO.findByCriteria(Employee.class, criterias, "fullName", false));*/
			String query="select distinct(obj.fullName) from Driver obj order by obj.fullName";
			model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		
			model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		}
		model.addAttribute("hours", genericDAO.findByCriteria(HoursMinutes.class, criterias,"hoursvalues",false));
		model.addAttribute("minutes", genericDAO.findByCriteria(Minutes.class, criterias,"id",false));
		model.addAttribute("otstatuss", listStaticData("YES_NO"));
		criterias.put("dataType","YES_NO");
		model.addAttribute("dtstatuss", genericDAO.findByCriteria(StaticData.class, criterias,"dataValue",false));
		criterias.clear();
		model.addAttribute("ampm", listStaticData("AM_PM"));
		
	}
	
	//@Override
	/*@RequestMapping(method = RequestMethod.POST, value = "/savet.do")
	
	public String savet(HttpServletRequest request,@ModelAttribute("modelObject") TimeSheet entity,BindingResult bindingResult)
	{
		List<TimeSheet> timeSheets = new ArrayList<TimeSheet>();
		String driver=request.getParameter("driver");
		String catagory=request.getParameter("catagory");
		String company=request.getParameter("company");
		String terminal=request.getParameter("terminal");
		String weeklyHours=request.getParameter("weeklyHours");
		String dailyHours=request.getParameter("dailyHours");
		String batchdate=request.getParameter("batchdate");
		
		
		String[]  dname=request.getParameterValues("day_name");
		String[]  dates=request.getParameterValues("date");
		String[]  tstarthours=request.getParameterValues("timestarthours");
		String[]  tstartminuts=request.getParameterValues("timestartminuts");
		String[]  tendinhours=request.getParameterValues("timeendinhours");
		String[]  tendinminuts=request.getParameterValues("timeendinminuts");
		String[]  hoursworked=request.getParameterValues("hoursworked");
		String[]  otflag=request.getParameterValues("otflag");
		String[]  dtflag=request.getParameterValues("dtflag");
		
	    if(dname.length<2 ||dates.length<2 || tstarthours.length<2 ||tstartminuts.length<2 ||tendinhours.length<2 ||tendinminuts.length<2||hoursworked.length<2)
	    {
	    	request.setAttribute("error","Mendatary Fields Required !");
	    	// setupUpdate(model, request);
		     return urlContext + "/form";
	    }
	    Employee empl=genericDAO.getById(Employee.class, Long.parseLong(driver));
	    EmployeeCatagory empCate=genericDAO.getById(EmployeeCatagory.class, Long.parseLong(catagory));
	    Location com=genericDAO.getById(Location.class, Long.parseLong(company));
	    Location ter=genericDAO.getById(Location.class, Long.parseLong(terminal));
		TimeSheet timesheet =null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    for(int i=0;i<2;i++)
	    {
	    	timesheet=new TimeSheet();
	    	
	    	timesheet.setEmployee(empl);
	    	timesheet.setCatagory(empCate);
	    	timesheet.setCompany(com);
	    	timesheet.setTerminal(ter);
	    	timesheet.setWeeklyHours(Double.parseDouble(weeklyHours));
	    	timesheet.setDailyHours(Double.parseDouble(dailyHours));
	    	
	    	//Date d=new Date(batchdate);
	    	//timesheet.setBatchdate(d);
	    	
	    	timesheet.setDay_name(dname[i]);
	    	
	    	//Date d1=new Date(dates[i]);
	    	//timesheet.setDate(d1);
	    	
	    	timesheet.setTimestarthours(tstarthours[i]);
	    	timesheet.setTimestartminuts(tstartminuts[i]);
	    	timesheet.setTimeendinhours(tendinhours[i]);
	    	timesheet.setTimestartminuts(tendinhours[i]);
	    	
	    	timesheet.setHoursworked(Double.parseDouble(hoursworked[i]));
	    	if(!otflag[i].isEmpty()){
	    		timesheet.setOtflag(otflag[i]);
	    	}
	    	else{
	    		timesheet.setOtflag("N");
	    	}
	    	if(!dtflag[i].isEmpty()){
	    		timesheet.setDtflag(dtflag[i]);
	    	}
	    	else{
	    		timesheet.setDtflag("N");
	    	}
	    	
	    	
			System.out.println(otflag[i]);
			
			
			timeSheets.add(timesheet);	
			
		}
	    
	    for(TimeSheet tsheet:timeSheets) {
			
			genericDAO.saveOrUpdate(tsheet);
		}
		if(entity.getEmployee()==null){
			System.out.println("\nentity.getEmployee()==null\n");
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		
		
		
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}
		
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option", null, null);
		}
		
		if(entity.getTimestarthours()!=null && entity.getTimestartminuts()!=null){
			entity.setSignintime(entity.getTimestarthours()+":"+entity.getTimestartminuts());
		}
		if(entity.getTimeendinhours()!=null && entity.getTimeendinminuts()!=null){
			entity.setSignouttime(entity.getTimeendinhours()+":"+entity.getTimeendinminuts());
		}
		if(entity.getOtflag()==null){
			entity.setOtflag("N");
		}
		if(entity.getDtflag()==null){
			entity.setDtflag("N");
		}
		
		
		//calculating hours worked and OT
		if(entity.getTimestarthours()!=null && !entity.getTimestarthours().isEmpty()  && entity.getTimestartminuts()!=null  &&!entity.getTimestartminuts().isEmpty() && entity.getTimeendinhours()!=null && entity.getTimeendinminuts()!=null ){
			double timeinhours=Double.parseDouble(entity.getTimestarthours());
			double timeinminutes=Double.parseDouble(entity.getTimestartminuts());
		    double timeouthours=Double.parseDouble(entity.getTimeendinhours());
			double timeoutminutes=Double.parseDouble(entity.getTimeendinminuts());
			int timeinhours=Integer.parseInt(entity.getTimestarthours());
			int timeinminutes=Integer.parseInt(entity.getTimestartminuts());
			int timeouthours=Integer.parseInt(entity.getTimeendinhours());
			int timeoutminutes=Integer.parseInt(entity.getTimeendinminuts());
			double ot=0.0;
			DecimalFormat frmt = new DecimalFormat("#.00");
			
			
			
			int workedHours=timeouthours-timeinhours;
			int workedMinutes=timeoutminutes-timeinminutes;
			
			double totalworkedtime=workedHours+(workedMinutes/100.0);
			//totalworkedtime = MathUtil.roundUp(totalworkedtime, 2);
			//frmt.format(totalworkedtime);
			entity.setHoursworked(totalworkedtime);
			
			System.out.println("\nworkedHours===>"+workedHours+"\n");
			System.out.println("\nworkedMinutes===>"+workedMinutes+"\n");
			System.out.println("\ntotalworkedtime===>"+totalworkedtime+"\n");
		}
		
		
	
		
		System.out.println("\nbefor try...Timesheet controller\n");
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			bindingResult.getFieldError();
		System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			return urlContext + "/form";
		}
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		// merge into datasource
		//System.out.println("\nsave\n");
		for(TimeSheet ts:entity){
			beforeSave(request, ts, model);
		genericDAO.saveOrUpdate(ts);
		}
		cleanUp(request);
		// return to list
		
		return "redirect:/" + urlContext + "/list.do";
		
	}*/
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") TimeSheet entity,
			BindingResult bindingResult, ModelMap model) 
	    {
		
		if(entity.getDriver()==null){
			System.out.println("\nentity.getEmployee()==null\n");
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}
		
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option", null, null);
		}
		

		
		
		
		
		
		
		/*if(entity.getTimestarthours()!=null && entity.getTimestartminuts()!=null){
			entity.setSignintime(entity.getTimestarthours()+":"+entity.getTimestartminuts());
		}
		if(entity.getTimeendinhours()!=null && entity.getTimeendinminuts()!=null){
			entity.setSignouttime(entity.getTimeendinhours()+":"+entity.getTimeendinminuts());
		}*/
		
		/*if(entity.getSotflag()==null){
			System.out.println("\nsunOtFlag=====>"+entity.getSotflag()+"\n");
			entity.setSotflag("N");
		}
		if(entity.getSdtflag()==null){
			entity.setSdtflag("N");
		}
		
		if(entity.getMtotflag()==null){
			entity.setMtotflag("N");
		}
		if(entity.getMdtflag()==null){
			entity.setMdtflag("N");
		}
		
		if(entity.getTotflag()==null){
			entity.setTotflag("N");
		}
		if(entity.getTdtflag()==null){
			entity.setTdtflag("N");
		}
		
		if(entity.getWotflag()==null){
			entity.setWotflag("N");
		}
		if(entity.getWdtflag()==null){
			entity.setWdtflag("N");
		}
		
		if(entity.getThotflag()==null){
			entity.setThotflag("N");
		}
		if(entity.getThdtflag()==null){
			entity.setThdtflag("N");
		}
		
		if(entity.getFotflag()==null){
			entity.setFotflag("N");
		}
		if(entity.getFdtflag()==null){
			entity.setFdtflag("N");
		}
		
		if(entity.getStotflag()==null){
			entity.setFotflag("N");
		}
		
		if(entity.getStdtflag()==null){
			entity.setFdtflag("N");
		}*/
		//calculating hours worked and OT
		/*if(entity.getTimestarthours()!=null && !entity.getTimestarthours().isEmpty()  && entity.getTimestartminuts()!=null  &&!entity.getTimestartminuts().isEmpty() && entity.getTimeendinhours()!=null && entity.getTimeendinminuts()!=null ){
			Double timeinhours=Double.parseDouble(entity.getTimestarthours());
			Double timeinminutes=Double.parseDouble(entity.getTimestartminuts());
			Double timeouthours=Double.parseDouble(entity.getTimeendinhours());
			Double timeoutminutes=Double.parseDouble(entity.getTimeendinminuts());
			int timeinhours=Integer.parseInt(entity.getTimestarthours());
			int timeinminutes=Integer.parseInt(entity.getTimestartminuts());
			int timeouthours=Integer.parseInt(entity.getTimeendinhours());
			int timeoutminutes=Integer.parseInt(entity.getTimeendinminuts());
			Double ot=0.0;
			DecimalFormat frmt = new DecimalFormat("#.00");
			
			
			
			Double workedHours=timeouthours-timeinhours;
			Double workedMinutes=timeoutminutes-timeinminutes;
			
			Double totalworkedtime=workedHours+(workedMinutes/100.0);
			//totalworkedtime = MathUtil.roundUp(totalworkedtime, 2);
			//frmt.format(totalworkedtime);
			entity.setHoursworked(totalworkedtime);
			
			System.out.println("\nworkedHours===>"+workedHours+"\n");
			System.out.println("\nworkedMinutes===>"+workedMinutes+"\n");
			System.out.println("\ntotalworkedtime===>"+totalworkedtime+"\n");
		}*/
		
		
	
		
		System.out.println("\nbefor try...TimeSheet controller\n");
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			bindingResult.getFieldError();
		   System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			return urlContext + "/form";
		}
		
		StringBuffer duplicateCheckQuery = new StringBuffer("Select obj from TimeSheet obj where obj.driver=");
		duplicateCheckQuery.append(entity.getDriver().getId()).append(" and obj.company=").append(entity.getCompany().getId());
		duplicateCheckQuery.append(" and obj.terminal=").append(entity.getTerminal().getId()).append(" and obj.catagory=");
		duplicateCheckQuery.append(entity.getCatagory().getId()).append(" and obj.batchdate='").append(dateFormat.format(entity.getBatchdate())).append(" 00:00:00'");
		if(entity.getId()!=null){
			duplicateCheckQuery.append(" and obj.id not in (").append(entity.getId()).append(")");
		}
		
		
		
		List<TimeSheet> timeSheetList=genericDAO.executeSimpleQuery(duplicateCheckQuery.toString());
		
		if (!StringUtils.equals(TimeSheet.ADD_TO_PREV_MODE, entity.getMode())) {
			if(timeSheetList.size()>0 && timeSheetList!=null){
				request.getSession().setAttribute("error","Record already exist with same details.");
				setupCreate(model, request);
				return urlContext + "/form";
			}
		}
		
		
		
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
	    cleanUp(request);
		
		
		return "redirect:/" + urlContext + "/list.do";
		
		
	}
        
	
	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
	{
		
		
		if("getWrDrHrsOnBdt".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				
				String bdate = request.getParameter("batchdate"); 
				
				SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy"); 
				try {
					Date date = dt.parse(bdate);
					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
					String query = "select obj from HourlyRate obj where obj.driver="+driver.getId()+" and '"+dt1.format(date)+" 00:00:00' BETWEEN obj.validFrom AND obj.validTo";
					List<HourlyRate> hourlyrate = genericDAO.executeSimpleQuery(query);
					
					List jsonres=new ArrayList();
					if(hourlyrate!=null && hourlyrate.size()>0){
						jsonres.add(hourlyrate.get(0).getWeeklyHours());
						jsonres.add(hourlyrate.get(0).getDailyHours());
					}
					else{
						jsonres.add(0.0);
						jsonres.add(0.0);
					}
					Gson gson = new Gson();
					return gson.toJson(jsonres);
					
					
					
				} catch (ParseException e) {
					e.printStackTrace();
				} 
			}
		}
		
		
		
		
		
		if("findDCompany".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> company=new ArrayList<Location>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				company.add(driver.getCompany());				
				Gson gson = new Gson();
				return gson.toJson(company);
			}
		
		}
		
		if("findhours".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				Map criterias = new HashMap();
				criterias.put("driver",driver);
				List<HourlyRate> hourlyrate = genericDAO.executeSimpleQuery("select obj from HourlyRate obj where obj.driver="+driver.getId()+" and obj.validFrom =(select max(o.validFrom) from HourlyRate o where o.driver="+driver.getId()+")");
				//HourlyRate hourlyrate = genericDAO.getByCriteria(HourlyRate.class, criterias);
				List jsonres=new ArrayList();
				if(hourlyrate!=null && hourlyrate.size()>0){
				jsonres.add(hourlyrate.get(0).getWeeklyHours());
				jsonres.add(hourlyrate.get(0).getDailyHours());
				}
				else{
					jsonres.add(0.0);
					jsonres.add(0.0);
				}
				Gson gson = new Gson();
				return gson.toJson(jsonres);
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
		
		if("allDaysOfWeek".equalsIgnoreCase(action)){
			try {
				List<String> list = new ArrayList<String>();
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			
			String batchdate=dateFormat1.format(sdf.parse(request.getParameter("batchDate")));				
			
		
			LocalDate now = new LocalDate(batchdate);
			LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
			list.add(sdf.format(monday.toDate()));			
			LocalDate tuesday = now.withDayOfWeek(DateTimeConstants.TUESDAY);
			list.add(sdf.format(tuesday.toDate()));	
			LocalDate wednesday = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
			list.add(sdf.format(wednesday.toDate()));	
			LocalDate thursday = now.withDayOfWeek(DateTimeConstants.THURSDAY);
			list.add(sdf.format(thursday.toDate()));	
			LocalDate friday = now.withDayOfWeek(DateTimeConstants.FRIDAY);
			list.add(sdf.format(friday.toDate()));	
			LocalDate saturday = now.withDayOfWeek(DateTimeConstants.SATURDAY);
			list.add(sdf.format(saturday.toDate()));
			LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
			list.add(sdf.format(sunday.toDate()));
			Gson gson = new Gson();
			return gson.toJson(list);
			} catch (ParseException e) {				
				return "";
			}
		}
		
		
		if("fidworkedHours".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("signIn")) && !StringUtils.isEmpty(request.getParameter("signInamOrpm")) &&  !StringUtils.isEmpty(request.getParameter("signOut")) && !StringUtils.isEmpty(request.getParameter("signOutamOrpm")))
			{
				//System.out.println("\nfidworkedHours\n");
				List<String> list = new ArrayList<String>();
				String singIn=request.getParameter("signIn");
				String signInamOrpm=request.getParameter("signInamOrpm");
				String signOut=request.getParameter("signOut");
				String signOutamOrpm=request.getParameter("signOutamOrpm");
				
				//System.out.println("\nsignInamOrpm===>"+signInamOrpm+"\n");
				//System.out.println("\ninminutes===>"+signOutamOrpm+"\n");
				
				String inhours=singIn.substring(0,2);
				String inminutes=singIn.substring(3,5);
				String outhours=signOut.substring(0,2);
				String outminutes=signOut.substring(3,5);
				//System.out.println("\nin hours===>"+inhours+"\n");
				//System.out.println("\nin minutes===>"+inminutes+"\n");
				//System.out.println("\nout hours===>"+outhours+"\n");
				//System.out.println("\nout minutes===>"+outminutes+"\n");
				
				
				Double timeinhours = Double.valueOf(inhours);
				if(timeinhours==12){
					timeinhours=0.0;
				}
				Double timeinminutes= Double.valueOf(inminutes);
				Double timeouthours= Double.valueOf(outhours);
				if(timeouthours==12){
					timeouthours=0.0;
				}
				Double timeoutminutes= Double.valueOf(outminutes);
				
				if(signInamOrpm.equals("AM") && signOutamOrpm.equals("PM")){
					//System.out.println("\nAM-PM\n");
					timeouthours=timeouthours+12;
					if(timeoutminutes< timeinminutes){
						timeouthours=timeouthours-1;
						timeoutminutes=timeoutminutes+60;
					}
					
				}
				
				if(signInamOrpm.equals("AM") && signOutamOrpm.equals("AM")){
					//System.out.println("\nAM-AM\n");
					if(timeoutminutes< timeinminutes){
						timeouthours=timeouthours-1;
						timeoutminutes=timeoutminutes+60;
					}
				}
				
				if(signInamOrpm.equals("PM") && signOutamOrpm.equals("PM")){
					//System.out.println("\nPM-PM\n");
					if(timeoutminutes< timeinminutes){
						timeouthours=timeouthours-1;
						timeoutminutes=timeoutminutes+60;
					}
				}
				
				if(signInamOrpm.equals("PM") && signOutamOrpm.equals("AM")){
					//System.out.println("\nAM-PM\n");
					timeouthours=timeouthours+12;
					if(timeoutminutes< timeinminutes){
						timeouthours=timeouthours-1;
						timeoutminutes=timeoutminutes+60;
					}
					
				}
				
				/*if(timeoutminutes< timeinminutes){
					timeouthours=timeouthours-1;
					timeoutminutes=timeoutminutes+60;
				}*/
				
				
				Double workedHours=timeouthours-timeinhours;
				Double workedMinutes=timeoutminutes-timeinminutes;
				
				
				Double totalworkedtime=workedHours+(workedMinutes/100.0);
				DecimalFormat df = new DecimalFormat("#0.00");
				String wHours = df.format(totalworkedtime);
				
				//System.out.println("\nwHours===>"+wHours+"\n");
				
				list.add(wHours);
				Gson gson = new Gson();
				return gson.toJson(list);
			}
		
		}
		
		
		if("totalworkedHours".equalsIgnoreCase(action)){
			/*if(!StringUtils.isEmpty(request.getParameter("tstarthours")) && !StringUtils.isEmpty(request.getParameter("tsminuts")) &&  !StringUtils.isEmpty(request.getParameter("tendinhours")) && !StringUtils.isEmpty(request.getParameter("tendinminuts")))
			{*/
				System.out.println("\nfind TOTAL workedHours\n");
				List<String> list = new ArrayList<String>();
				
				
				String Sholiday=request.getParameter("Sholiday");
				String Mholiday=request.getParameter("Mholiday");
				String Tholiday=request.getParameter("Tholiday");
				String Wholiday=request.getParameter("Wholiday");
				String THholiday=request.getParameter("THholiday");
				String Fholiday=request.getParameter("Fholiday");
				String STholiday=request.getParameter("STholiday");
				
				
				
				String SSWhours= request.getParameter("SWhours");
				Double h1=0.0;
			    int m1=0;
			    
			    Double holh1 = 0.0;
			    int holm1=0;
			    
			    if(SSWhours !=null && SSWhours.length()>0){
			    	Double SSDWhours = Double.parseDouble(SSWhours);
			    	StringTokenizer sst = new StringTokenizer(SSDWhours.toString(), ".");
			    	String hh1=null;
			    	String mm1=null;
			    	if(sst.countTokens()>1){
			    		hh1=sst.nextElement().toString();
			    		mm1=sst.nextElement().toString();
			    		if(mm1.length()==1){
			    			mm1=mm1+"0";
			    		}
			    		//System.out.println("\nmm1===>"+mm1+"\n");
			    	}
			    	/*Double h1 = Double.valueOf(hh1);
			    	int m1 = Integer.valueOf(mm1);*/
			        h1 = Double.valueOf(hh1);
			        m1 = Integer.valueOf(mm1);
			        
			        if(Sholiday.equalsIgnoreCase("Yes")){
			        	System.out.println("Eneter 1");
			        	holh1=Double.valueOf(hh1);
			        	holm1=Integer.valueOf(mm1);	
			        }
			    }
			    
			    String SMWhours= request.getParameter("MWhours");
			    Double h2=0.0;
			    int m2=0;
			    
			    Double holh2 = 0.0;
			    int holm2=0;
			    
			    if(SMWhours !=null && SMWhours.length()>0){
			    	Double SMDWhours = Double.parseDouble(SMWhours);
			    	
			    	StringTokenizer mst = new StringTokenizer(SMDWhours.toString(), ".");
			    	String hh2=null;
			    	String mm2=null;
			    	if(mst.countTokens()>1){
			    		hh2=mst.nextElement().toString();
			    		mm2=mst.nextElement().toString();
			    		if(mm2.length()==1){
			    			mm2=mm2+"0";
			    		}
			    		//System.out.println("\nmm2===>"+mm2+"\n");
			    	}
			    	/*Double h2 = Double.valueOf(hh2);
			    	int m2 = Integer.valueOf(mm2);*/
			    	h2 = Double.valueOf(hh2);
			    	m2 = Integer.valueOf(mm2);
			    	
			    	 if(Mholiday.equalsIgnoreCase("Yes")){
			    		 System.out.println("Eneter 2");
				        	holh2=Double.valueOf(hh2);
				        	holm2=Integer.valueOf(mm2);	
				     }
			    }
			    
			    
			    String STWhours= request.getParameter("TWhours");
			    Double h3=0.0;
			    int m3=0;
			    
			    Double holh3 = 0.0;
			    int holm3=0;
			    
			    if(STWhours !=null && STWhours.length()>0){
			    	
			    	Double STDWhours = Double.parseDouble(STWhours);
			    	
			    	StringTokenizer tst = new StringTokenizer(STDWhours.toString(), ".");
			    	String hh3=null;
			    	String mm3=null;
			    	if(tst.countTokens()>1){
			    		hh3=tst.nextElement().toString();
			    		mm3=tst.nextElement().toString();
			    		if(mm3.length()==1){
			    			mm3=mm3+"0";
			    		}
			    		//System.out.println("\nmm3===>"+mm3+"\n");
			    	}
			    	/*Double h3 = Double.valueOf(hh3);
			    	int m3 = Integer.valueOf(mm3);*/
			    	h3 = Double.valueOf(hh3);
			    	m3 = Integer.valueOf(mm3);
			    	
			    	 if(Tholiday.equalsIgnoreCase("Yes")){
			    		 System.out.println("Eneter 3");
				        	holh3=Double.valueOf(hh3);
				        	holm3=Integer.valueOf(mm3);	
				     }
			    }
			    
			    
			    String SWWhours= request.getParameter("WWhours");
			    Double h4=0.0;
			    int m4=0;
			    
			    Double holh4 = 0.0;
			    int holm4=0;
			    
			    if(SWWhours !=null && SWWhours.length()>0){
			    	
			    	Double SWDWhours = Double.parseDouble(SWWhours);
			    	
			    	StringTokenizer wst = new StringTokenizer(SWDWhours.toString(), ".");
			    	String hh4=null;
			    	String mm4=null;
			    	if(wst.countTokens()>1){
			    		hh4=wst.nextElement().toString();
			    		mm4=wst.nextElement().toString();
			    		if(mm4.length()==1){
			    			mm4=mm4+"0";
			    		}
			    		//System.out.println("\nmm4===>"+mm4+"\n");
			    	}
			    	/*Double h4 = Double.valueOf(hh4);
			    	int m4 = Integer.valueOf(mm4);*/
			    	h4 = Double.valueOf(hh4);
			    	m4 = Integer.valueOf(mm4);
			    	
			    	 if(Wholiday.equalsIgnoreCase("Yes")){
			    		 System.out.println("Eneter 4");
				        	holh4=Double.valueOf(hh4);
				        	holm4=Integer.valueOf(mm4);	
				     }
			    }
	
			    
			    String STHWhours= request.getParameter("THWhours");
			    Double h5=0.0;
			    int m5=0;
			    
			    Double holh5 = 0.0;
			    int holm5=0;
			    
			    if(STHWhours !=null && STHWhours.length()>0){
			    	
			    	Double STHDWhours = Double.parseDouble(STHWhours);
			    	
			    	StringTokenizer rhst = new StringTokenizer(STHDWhours.toString(), ".");
			    	String hh5=null;
			    	String mm5=null;
			    	if(rhst.countTokens()>1){
			    		hh5=rhst.nextElement().toString();
			    		mm5=rhst.nextElement().toString();
			    		if(mm5.length()==1){
			    			mm5=mm5+"0";
			    		}
			    		//System.out.println("\nmm5===>"+mm5+"\n");
			    	}
			    	/*Double h5 = Double.valueOf(hh5);
			    	int m5 = Integer.valueOf(mm5);*/
			    	h5 = Double.valueOf(hh5);
			    	m5 = Integer.valueOf(mm5);
			    	
			    	
			    	 if(THholiday.equalsIgnoreCase("Yes")){
			    		 System.out.println("Eneter 5");
				        	holh5=Double.valueOf(hh5);
				        	holm5=Integer.valueOf(mm5);	
				     }
			    }
			    
			    String SFWhours= request.getParameter("FWhours");
			    Double h6=0.0;
			    int m6=0;
			    
			    Double holh6 = 0.0;
			    int holm6=0;
			    
			    if(SFWhours !=null && SFWhours.length()>0){
			    	
			    	Double SFDWhours = Double.parseDouble(SFWhours);
			    	
			    	StringTokenizer fst = new StringTokenizer(SFDWhours.toString(), ".");
			    	String hh6=null;
			    	String mm6=null;
			    	if(fst.countTokens()>1){
			    		hh6=fst.nextElement().toString();
			    		mm6=fst.nextElement().toString();
			    		if(mm6.length()==1){
			    			mm6=mm6+"0";
			    		}
			    		//System.out.println("\nmm6===>"+mm6+"\n");
			    	}
			    	/*Double h6 = Double.valueOf(hh6);
			    	int m6 = Integer.valueOf(mm6);*/
			    	h6 = Double.valueOf(hh6);
			    	m6 = Integer.valueOf(mm6);
			    	
			    	 if(Fholiday.equalsIgnoreCase("Yes")){
			    		 System.out.println("Eneter 6");
				        	holh6=Double.valueOf(hh6);
				        	holm6=Integer.valueOf(mm6);	
				     }
			    }
			    
			    String SSTWhours= request.getParameter("STWhours");
			    Double h7 = 0.0;
			    int m7=0;
			    
			    Double holh7 = 0.0;
			    int holm7=0;
			    
			    if(SSTWhours !=null && SSTWhours.length()>0){
			    	
			    	Double SSTDWhours = Double.parseDouble(SSTWhours);
			    	
			    	StringTokenizer stst = new StringTokenizer(SSTDWhours.toString(), ".");
			    	String hh7=null;
			    	String mm7=null;
			    	if(stst.countTokens()>1){
			    		hh7=stst.nextElement().toString();
			    		mm7=stst.nextElement().toString();
			    		if(mm7.length()==1){
			    			mm7=mm7+"0";
			    		}
					//System.out.println("\nmm7===>"+mm7+"\n");
			    	}
			 
			    	/*Double h7 = Double.valueOf(hh7);
			    	int m7 = Integer.valueOf(mm7);*/
			    	h7 = Double.valueOf(hh7);
			    	m7 = Integer.valueOf(mm7);
			    	
			    	 if(STholiday.equalsIgnoreCase("Yes")){
			    		 System.out.println("Eneter 7");
				        	holh7=Double.valueOf(hh7);
				        	holm7=Integer.valueOf(mm7);	
				     }
			    	
			    }
			    Double totalHH=h1+h2+h3+h4+h5+h6+h7;
			   // Double roundUpHH=
			    int totalMM=m1+m2+m3+m4+m5+m6+m7;
			    //System.out.println("\nHours===>"+totalHH+"\n");
				//System.out.println("\nMinutes===>"+totalMM+"\n");
			    
			    Double totalHolidayHH = holh1+holh2+holh3+holh4+holh5+holh6+holh7;
			    int totalHolidayMM=holm1+holm2+holm3+holm4+holm5+holm6+holm7;
			    
			   
				if(totalMM>59){
					int extHours=totalMM/60;
					int newminuts=totalMM%60;
					 totalHH=totalHH+extHours+(newminuts/100.00);					
				}
				else{
					totalHH=totalHH+(totalMM/100.00);
				}
				
				
				
				
				if(totalHolidayMM>59){
					int extHours=totalHolidayMM/60;
					int newminuts=totalHolidayMM%60;
					totalHolidayHH=totalHolidayHH+extHours+(newminuts/100.00);					
				}
				else{
					totalHolidayHH=totalHolidayHH+(totalHolidayMM/100.00);
				}
				
			    
			    //For round Up and dound Up value
				Double roundUptotalHH=0.00;
				Double roundUptotalMM=0.00;
				
				Double roundUptotalHolidayHH=0.00;
				Double roundUptotalHolidayMM=0.00;
				
				
				Double roundUptotalvalueHH=0.00;
				Double roundUptotalvalueMM=0.00;
				
				Double roundUptotalvalueHolidayHH=0.00;
				Double roundUptotalvalueHolidayMM=0.00;
				
				/*DecimalFormat df1 = new DecimalFormat();
				  String roundUpHHMM=df1.format(totalHH);*/
				String roundUpHHMM=totalHH+"";
				
				String roundUpHolidayHHMM=totalHolidayHH+"";
				
				
				
				//System.out.println("\nroundUpHHMM====>"+roundUpHHMM+"\n");
				  
				StringTokenizer strroundUpHHMM = new StringTokenizer(roundUpHHMM, ".");
				
				StringTokenizer strroundUpHolidayHHMM = new StringTokenizer(roundUpHolidayHHMM, ".");
				
				//System.out.println("\nstrroundUpHHMM.countTokens()====>"+strroundUpHHMM.countTokens()+"\n");
				String roundhh=null;
				String roundmm=null;
				if(strroundUpHHMM.countTokens()>1){
				   roundhh=strroundUpHHMM.nextElement().toString();
				   roundmm=strroundUpHHMM.nextElement().toString();
				   
				   if(roundmm.length()==1){
					   roundmm=roundmm+"0";
					}
				   /*System.out.println("\nroundhh===>"+roundhh+"\n");
				  System.out.println("\nroundmm===>"+roundmm+"\n");*/
				}
				
				
				
				String roundHolidayhh=null;
				String roundHolidaymm=null;
				if(strroundUpHolidayHHMM.countTokens()>1){
					roundHolidayhh=strroundUpHolidayHHMM.nextElement().toString();
					roundHolidaymm=strroundUpHolidayHHMM.nextElement().toString();
				   
				   if(roundHolidaymm.length()==1){
					   roundHolidaymm=roundHolidaymm+"0";
					}
				   /*System.out.println("\nroundhh===>"+roundhh+"\n");
				  System.out.println("\nroundmm===>"+roundmm+"\n");*/
				}
				
				
				 Double doubleroundhh=Double.valueOf(roundhh);
				 Double doubleroundmm=Double.valueOf(roundmm);
				 
				 Double doubleroundholidayhh=Double.valueOf(roundHolidayhh);
				 Double doubleroundholidaymm=Double.valueOf(roundHolidaymm);
				 
				 
				 roundUptotalHH=roundUptotalHH+doubleroundhh;
				 roundUptotalvalueHH=roundUptotalvalueHH+doubleroundhh;
				 
				 roundUptotalHolidayHH=roundUptotalHolidayHH+doubleroundholidayhh;
				 roundUptotalvalueHolidayHH=roundUptotalvalueHolidayHH+doubleroundholidayhh;
				 
				 
				 
				 if(doubleroundmm>0 && doubleroundmm<=15){
					
					 roundUptotalMM=0.15;
					 if(doubleroundmm>=7)
					  roundUptotalvalueMM=0.25;
					 else
						 roundUptotalvalueMM=0.00;
				 }
				 if(doubleroundmm>15 && doubleroundmm<=30){
					 roundUptotalMM=0.30;
					 if(doubleroundmm>=22)
					 roundUptotalvalueMM=0.50;
					 else
						roundUptotalvalueMM=0.25;
				 }
				 if(doubleroundmm>30 && doubleroundmm<=45){
					 roundUptotalMM=0.45;
					 if(doubleroundmm>=37)
					  roundUptotalvalueMM=0.75;
					 else
						 roundUptotalvalueMM=0.50; 
				 }
				 if(doubleroundmm>45){
					 roundUptotalHH=roundUptotalHH+1;
					 if(doubleroundmm>=52)
					  roundUptotalvalueHH=roundUptotalvalueHH+1;
					 else
						 roundUptotalvalueMM=0.75;
				 }
				 
				 
				 
				 
				 
				 if(doubleroundholidaymm>0 && doubleroundholidaymm<=15){
						
					 roundUptotalHolidayMM=0.15;
					 if(doubleroundholidaymm>=7)
						 roundUptotalvalueHolidayMM=0.25;
					 else
						 roundUptotalvalueHolidayMM=0.00;
				 }
				 if(doubleroundholidaymm>15 && doubleroundholidaymm<=30){
					 roundUptotalHolidayMM=0.30;
					 if(doubleroundholidaymm>=22)
						 roundUptotalvalueHolidayMM=0.50;
					 else
						 roundUptotalvalueHolidayMM=0.25;
				 }
				 if(doubleroundholidaymm>30 && doubleroundholidaymm<=45){
					 roundUptotalHolidayMM=0.45;
					 if(doubleroundholidaymm>=37)
						 roundUptotalvalueHolidayMM=0.75;
					 else
						 roundUptotalvalueHolidayMM=0.50; 
				 }
				 if(doubleroundholidaymm>45){
					 roundUptotalHolidayHH=roundUptotalHolidayHH+1;
					 if(doubleroundholidaymm>=52)
						 roundUptotalvalueHolidayHH=roundUptotalvalueHolidayHH+1;
					 else
						 roundUptotalvalueHolidayMM=0.75;
				 }
				 
				 
				 
				 
				 
				 
				 
				 Double roundUptotalHHMM=roundUptotalHH+roundUptotalMM;
				 Double roundUptotalvalueHHMM=roundUptotalvalueHH+roundUptotalvalueMM;
				 
				 
				 Double roundUptotalHolidayHHMM= roundUptotalHolidayHH+roundUptotalHolidayMM;
				 Double roundUptotalvalueHolidayHHMM=roundUptotalvalueHolidayHH+roundUptotalvalueHolidayMM;
				 
				/* System.out.println("\nroundUptotalHHMM===>"+roundUptotalHHMM+"\n");
				 System.out.println("\nroundUptotalvalueHHMM===>"+roundUptotalvalueHHMM+"\n");*/
				 
				/*Double SWhours = Double.valueOf(request.getParameter("SWhours"));
				Double MWhours= Double.valueOf(request.getParameter("MWhours"));
				Double TWhours= Double.valueOf(request.getParameter("TWhours"));
				Double WWhours= Double.valueOf(request.getParameter("WWhours"));
				Double THWhours= Double.valueOf(request.getParameter("THWhours"));
				Double FWhours= Double.valueOf(request.getParameter("FWhours"));
				Double STWhours= Double.valueOf(request.getParameter("STWhours"));*/
				
				
				//OT FLAGS
				String Sot=request.getParameter("Sot");
				String Mot=request.getParameter("Mot");
				String Tot=request.getParameter("Tot");
				String Wot=request.getParameter("Wot");
				String THot=request.getParameter("THot");
				String Fot=request.getParameter("Fot");
				String STot=request.getParameter("STot");
			    //System.out.println("\not===>"+ Sot +" "+ Mot +" "+ Tot +" "+ Wot +" "+ THot +" "+ Fot +" "+ STot +"\n");
				//DT FLAGS
				String Sdt=request.getParameter("Sdt");
				String Mdt=request.getParameter("Mdt");
				String Tdt=request.getParameter("Tdt");
				String Wdt=request.getParameter("Wdt");
				String THdt=request.getParameter("THdt");
				String Fdt=request.getParameter("Fdt");
				String STdt=request.getParameter("STdt");
				//System.out.println("\ndt===>"+ Sdt +" "+ Mdt +" "+ Tdt +" "+ Wdt +" "+ THdt +" "+ Fdt +" "+ STdt +"\n");
				
				Double reguHours=0.0;
				Double otHours=0.0;
				Double otHoursRounded=0.0;
				int otMinuts=0;
				Double dtHours=0.0;
				int dtMinuts=0;
				if(!StringUtils.isEmpty(request.getParameter("weeklyHours")) && !request.getParameter("weeklyHours").equalsIgnoreCase("0.0") ){
					Double weeklyHours= Double.valueOf(request.getParameter("weeklyHours"));
					if(weeklyHours==0.0){
						reguHours=roundUptotalvalueHHMM;
					}
					else{
					reguHours=weeklyHours;
					//otHours=totalHH-weeklyHours;
					}
					if(weeklyHours > 0.0){
						otHours=roundUptotalvalueHHMM-weeklyHours;
						if(otHours>0.0){
						//do nothing
						}
						else{
						otHours=0.0;
						}
					}
					else if (weeklyHours <= 0.0){
						otHours=0.0;
					}
					System.out.println("\notHours====>"+otHours+"\n");
				}
				else
				{
					if((StringUtils.isEmpty(request.getParameter("weeklyHours")) || request.getParameter("weeklyHours").equalsIgnoreCase("0.0")) && (! StringUtils.isEmpty(request.getParameter("dailyregularHours")) && !request.getParameter("dailyregularHours").equalsIgnoreCase("0.0")))
					{	
						Double dailyregularHours= Double.valueOf(request.getParameter("dailyregularHours"));
						if(SSWhours !=null && SSWhours.length()>0){
							reguHours=dailyregularHours+reguHours;
						
							//reguHours=dailyregularHours*7;
							if(Sdt.equalsIgnoreCase("yes")){
								dtHours=dtHours+(h1-dailyregularHours);
								dtMinuts=dtMinuts+(m1);
							}
							else{
								if(Sot.equalsIgnoreCase("Yes")){
									otHours=otHours+(h1-dailyregularHours);
									otMinuts=otMinuts+m1;
								}
							}
						}
						
						if(SMWhours !=null && SMWhours.length()>0){
							reguHours=dailyregularHours+reguHours;
						
							if(Mdt.equalsIgnoreCase("yes")){
								dtHours=dtHours+(h2-dailyregularHours);
								dtMinuts=dtMinuts+(m2);
							}
							else{
			   		         	if(Mot.equalsIgnoreCase("Yes")){
			   		        	 otHours=otHours+(h2-dailyregularHours);
						    	 otMinuts=otMinuts+m2;
			   		         	}
							}
						}
						
						if(STWhours !=null && STWhours.length()>0)
						{
							reguHours=dailyregularHours+reguHours;
							if(Tdt.equalsIgnoreCase("yes")){
								dtHours=dtHours+(h3-dailyregularHours);
								dtMinuts=dtMinuts+m3;
							}
							else{
			   		         	if(Tot.equalsIgnoreCase("Yes")){
			   		         		otHours=otHours+(h3-dailyregularHours);
			   		         		otMinuts=otMinuts+m3;
			   		         	}
							}
						}
						
						if(SWWhours !=null && SWWhours.length()>0){
							reguHours=dailyregularHours+reguHours;
							if(Wdt.equalsIgnoreCase("yes")){
								dtHours=dtHours+(h4-dailyregularHours);
								dtMinuts=dtMinuts+(m4);
							}
							else{
								if(Wot.equalsIgnoreCase("Yes")){
									otHours=otHours+(h4-dailyregularHours);
									otMinuts=otMinuts+m4;
								}
							}
						}	
							
						 if(STHWhours !=null && STHWhours.length()>0){
							 reguHours=dailyregularHours+reguHours; 
							 if(THdt.equalsIgnoreCase("yes")){
									 dtHours=dtHours+(h5-dailyregularHours);
									 dtMinuts=dtMinuts+m5;
							 }
							 else{
								 if(THot.equalsIgnoreCase("Yes")){
									 otHours=otHours+(h5-dailyregularHours);
									 otMinuts=otMinuts+m5;
								 	}
							 }
						 }
						
						 if(SFWhours !=null && SFWhours.length()>0){
							 reguHours=dailyregularHours+reguHours; 
							 if(Fdt.equalsIgnoreCase("yes")){
									 dtHours=dtHours+(h6-dailyregularHours);
									 dtMinuts=dtMinuts+m6;
							 }
							 else{
								 if(Fot.equalsIgnoreCase("Yes")){
									 otHours=otHours+(h6-dailyregularHours);
									 otMinuts=otMinuts+m6;
								 }
							 }
						 }
						
						if(SSTWhours !=null && SSTWhours.length()>0){
							reguHours=dailyregularHours+reguHours; 
							if(STdt.equalsIgnoreCase("yes")){
								dtHours=dtHours+(h7-dailyregularHours);
								dtMinuts=dtMinuts+m7;
							}
							else{
			   		         	if(STot.equalsIgnoreCase("Yes")){
			   		         		otHours=otHours+(h7-dailyregularHours);
			   		         		otMinuts=otMinuts+m7;
			   		         	}
							}
						}
					}
					else{
						reguHours=roundUptotalvalueHHMM;
						otHours=0.0;
					}
					
					double otMinutsForRound=0.0;
				    if(otMinuts>59){
						  int extOtHours=otMinuts/60;
						  int newOtminuts=otMinuts%60;
						  otMinutsForRound=newOtminuts;
						  //otHours=otHours+extOtHours+(newOtminuts/100.00);
						  otHours=otHours+extOtHours;
					}
				    else{
				    	//otHours=otHours+(otMinuts/100.00);
				    	otMinutsForRound=otMinuts;
				    }
				    
				     double RoundminutsOT= getRoundUpHours(otMinutsForRound );
				     if(otMinutsForRound>=52){
				    	 otHours=1+otHours+RoundminutsOT;
				     }
				     else{
				    	 otHours=otHours+RoundminutsOT;
				     }
				     System.out.println("OT hours rounded==>"+otHours+"\n");
				     
				     
				     double dtMinutsForRound=0.0;
				    if(dtMinuts>59){
						  int extdtHours=dtMinuts/60;
						  int newdtminuts=dtMinuts%60;
						  dtMinutsForRound=newdtminuts;
						  //dtHours=dtHours+extdtHours+(newdtminuts/100.00);
						  dtHours=dtHours+extdtHours;
				    }
				    else{
					   //dtHours=dtHours+(dtMinuts/100.00);
				    	dtMinutsForRound=dtMinuts;
				    }
				    
				    double RoundminutsDT= getRoundUpHours(dtMinutsForRound );
				    if(dtMinutsForRound>=52){
				    	dtHours=1+dtHours+RoundminutsDT;
				    }
				    else{
				    	dtHours=dtHours+RoundminutsDT;
				    }
				    System.out.println("dT hours rounded==>"+dtHours+"\n");
				 
				}
				
			
				
				if(roundUptotalvalueHolidayHHMM > 0.0 && otHours > 0.0){
					otHours = otHours - roundUptotalvalueHolidayHHMM;
				}
				
				
				
				Double roundUptotalvalueHHMMNew = roundUptotalvalueHHMM;
				if(totalHH <= reguHours)
				{
					if(roundUptotalvalueHolidayHHMM > 0.0 && roundUptotalvalueHHMM > 0.0){
						roundUptotalvalueHHMM = roundUptotalvalueHHMM - roundUptotalvalueHolidayHHMM ;
					}
				}
				else{
					/*if(roundUptotalvalueHolidayHHMM > 0.0 && otHours > 0.0){
						reguHours = reguHours - roundUptotalvalueHolidayHHMM;
					}*/
				}
				
				
				DecimalFormat df = new DecimalFormat("#0.00");
				String twHours = df.format(totalHH);
				String regularHours = df.format(reguHours);
				String totalotHours = df.format(otHours);
				String totaldtHours = df.format(dtHours);
				
				
				String roundHHMM = df.format(roundUptotalHHMM);
				String roundvalueHHMM = df.format(roundUptotalvalueHHMM);
				String roundvalueHHMMNew = df.format(roundUptotalvalueHHMMNew);
				
				String roundHolidayHHMM = df.format(roundUptotalHolidayHHMM);
				String roundvalueHolidayHHMM = df.format(roundUptotalvalueHolidayHHMM);
				
				
				/*System.out.println("\ntotalweekly worked Hours===>"+twHours+"\n");
				System.out.println("\ntotalweekly regular hours===>"+regularHours+"\n");
				System.out.println("\ntotal ot Hours===>"+totalotHours+"\n");
				System.out.println("\ntotal dt Hours===>"+dtHours+"\n");*/
				
				if(totalHH <= reguHours)
				{
					list.add(twHours);
					list.add(roundvalueHHMM);
				}
				else{
					list.add(twHours);
					list.add(regularHours);
				}
					
				
				list.add(totalotHours);
				list.add(totaldtHours);
				
				list.add(roundHHMM);
				list.add(roundvalueHHMMNew);
				
				
				list.add(roundHolidayHHMM);
				list.add(roundvalueHolidayHHMM);
				
				
				System.out.println("***** Holiday hours "+roundHolidayHHMM);
				
				System.out.println("***** Holiday hours total value "+roundvalueHolidayHHMM);
				
				Gson gson = new Gson();
				return gson.toJson(list);		
		}
	return "";
   }
	
	 double getRoundUpHours(double doubleroundmm){
		 double roundUptotalvalueMM=0.0;
		 if(doubleroundmm>0 && doubleroundmm<=15){
				
			
			 if(doubleroundmm>=7)
			  roundUptotalvalueMM=0.25;
			 else
				 roundUptotalvalueMM=0.00;
		 }
		 if(doubleroundmm>15 && doubleroundmm<=30){
			 
			 if(doubleroundmm>=22)
			 roundUptotalvalueMM=0.50;
			 else
				roundUptotalvalueMM=0.25;
		 }
		 if(doubleroundmm>30 && doubleroundmm<=45){
			
			 if(doubleroundmm>=37)
			  roundUptotalvalueMM=0.75;
			 else
				 roundUptotalvalueMM=0.50; 
		 }
		 if(doubleroundmm>45){
			
			 if(doubleroundmm>=52)
				 roundUptotalvalueMM=0.00;
			 else
				 roundUptotalvalueMM=0.75;
		 }
		 
		 
		 return roundUptotalvalueMM; 
	 }
	
	 
	 @RequestMapping(method = RequestMethod.GET, value = "/copy.do")
		public String copy(ModelMap model, HttpServletRequest request, @RequestParam("id")Long id) {

			setupUpdate(model, request);		
			TimeSheet sheet = genericDAO.getById(getEntityClass(), id);
			sheet.setBatchdate(null);
			sheet.setId(null);
			sheet.setMdate(null);
			sheet.setTdate(null);
			sheet.setWdate(null);
			sheet.setThdate(null);
			sheet.setFdate(null);
			sheet.setStadate(null);
			sheet.setSdate(null);
			
			model.addAttribute("modelObject", sheet);
			return urlContext + "/form";
		}
	 
	 
	
}
