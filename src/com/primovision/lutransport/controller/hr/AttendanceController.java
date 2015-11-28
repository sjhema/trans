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
import org.apache.poi.util.IntegerField;
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


import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.Attendance;
import com.primovision.lutransport.model.hr.Eligibility;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hr.ShiftCalendar;
import com.primovision.lutransport.model.SearchCriteria;
/*import com.primovision.lutransport.model.State;*/
import com.primovision.lutransport.model.StaticData;
/*import com.primovision.lutransport.model.Vehicle;*/

/**
 * @author Subodh
 */

@Controller
@RequestMapping("/hr/attendance")
public class AttendanceController extends CRUDController<Attendance> {

	public AttendanceController() {
		setUrlContext("/hr/attendance");
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
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("employees",genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		//criterias.clear();
		/*criterias.put("dataType", "STATUS");
		criterias.put("dataValue", "0,1");
		model.addAttribute("status", genericDAO.findByCriteria(StaticData.class, criterias,"dataText",false));
		criterias.clear();*/
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		model.addAttribute("shiftnames",genericDAO.findByCriteria(ShiftCalendar.class, criterias, "name", false));
		}

	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		//System.out.println("\nlist1\n");
		 SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// System.out.println("\nlist2\n");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"driver.fullName",null,null));
		 //System.out.println("\nlist3\n");
		return urlContext + "/list";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
	    populateSearchCriteria(request, request.getParameterMap());
	    setupCreate(model, request);
		
	}

	
	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") Attendance entity,
			BindingResult bindingResult, ModelMap model) 
	    {
		
		if(entity.getDriver()==null){
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		
		if(entity.getShift()==null){
			bindingResult.rejectValue("shift", "error.select.option", null, null);
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
		
		//calculating hours worked and OT
		if(entity.getTimestarthours()!=null && !entity.getTimestarthours().isEmpty()  && entity.getTimestartminuts()!=null  &&!entity.getTimestartminuts().isEmpty() && entity.getTimeendinhours()!=null && entity.getTimeendinminuts()!=null ){
			/*double timeinhours=Double.parseDouble(entity.getTimestarthours());
			double timeinminutes=Double.parseDouble(entity.getTimestartminuts());
		    double timeouthours=Double.parseDouble(entity.getTimeendinhours());
			double timeoutminutes=Double.parseDouble(entity.getTimeendinminuts());*/
			
			
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
			
			if(entity.getShift()!=null){
				ShiftCalendar shift=genericDAO.getById(ShiftCalendar.class,entity.getShift().getId());
				
				ot=totalworkedtime-(shift.getDailyregularhours());
				 if(totalworkedtime>0){
					 entity.setOvertimehours(ot);
					 System.out.println("\nover time===>"+ot+"\n");
				 }
			}
			
			
			
			
		}
		
		
		
		//checking  for duplicate	  
		  
		if(entity.getId()!=null){
			boolean duplicat=false;
			Attendance currentAttendance=genericDAO.getById(Attendance.class,entity.getId());
			long curshift=currentAttendance.getShift().getId();
			long curemp=currentAttendance.getDriver().getId();
			String curattendancedate=dateFormat.format(currentAttendance.getAttendancedate());
			
			String query = "select obj from Attendance obj where 1=1 and obj.id not in("+entity.getId()+")";
			
			
			
			List<Attendance> Attends = genericDAO.executeSimpleQuery(query);
			if (Attends!=null && Attends.size()>0){	
				for(Attendance Attend:Attends){
					long shift=Attend.getShift().getId();
					long emp=Attend.getDriver().getId();
					String attendancedate=dateFormat.format(Attend.getAttendancedate());
					
					if(shift==curshift){
						//System.out.println("\ncomany same\n");
						if(emp==curemp){
						//System.out.println("\nterminal same\n");
						
							if(attendancedate.equals(curattendancedate)){
								//System.out.println("\nleave same\n");
								System.out.println("\nduplicat while editting\n");
					            duplicat=true;
						        break;
							
						}
						}
					}
				}
			}
			if(duplicat==true){
                   request.setAttribute("error","duplicate Attendance!");
				   setupUpdate(model, request);
				  return urlContext + "/form";
			}
			
			
		}
		
		
		
		if(entity.getShift()!=null && entity.getDriver()!=null && entity.getId()==null)
		{
		   String query = "select obj from Attendance obj where obj.shift="+entity.getShift().getId()+" and obj.driver="+entity.getDriver().getId()+"  and obj.attendancedate='"+dateFormat.format(entity.getAttendancedate())+"'";
		   //System.out.println("\nquery==>"+query+"\n");
		   List<Attendance> attendance = genericDAO.executeSimpleQuery(query);
		   if (attendance!=null && attendance.size()>0){	
			      System.out.println("\nDuplicate attendance while adding new\n");
		          request.setAttribute("error","duplicate attendance!");
			      setupUpdate(model, request);
			     return urlContext + "/form";
         	}
		}
		
		
		
		
		  /* String attendancedate=dateFormat.format(attendance.getAttendancedate());
		  
		   String attendanceQuery="select obj from Attendance obj where obj.shift='"+attendance.getShift().getId()+"' and obj.attendancedate='"+attendancedate+"' and obj.driver='"+attendance.getDriver().getId()+"'";
		   List<Attendance> dupAttendance = genericDAO.executeSimpleQuery(attendanceQuery);
		   //int duplicate=duplicateFuel.size();
		   System.out.println("\nattendanceQuery==>"+attendanceQuery+"\n");
		   System.out.println("\ndupAttendance.size()==>"+dupAttendance.size()+"\n");
		   if(dupAttendance.size()>0){
		  
		      
		   }*/
		
		System.out.println("\nbefor try...Attendance controller\n");
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
		// merge into datasource
		//System.out.println("\nsave\n");
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		
		return "redirect:/" + urlContext + "/list.do";
		
		
		//
		
      // return super.save(request, entity, bindingResult, model);
	}
        
	
	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
	{
		if("findDCompany".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> company=new ArrayList<Location>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				company.add(driver.getCompany());
				company.add(driver.getTerminal());
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
		
		
		return "";
	}
	
	
}
