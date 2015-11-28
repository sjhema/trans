package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.DriverInspection;
import com.primovision.lutransport.model.DriverInspectionTemp;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Terminal;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hr.TimeSheet;
import com.primovision.lutransport.model.report.DriverInspectionWrapper;
import com.primovision.lutransport.model.report.EztollReportInput;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/admin/driverinspection")
public class DriverInspectionController extends CRUDController<DriverInspection>{

	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
	SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd"); 
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	public DriverInspectionController() {
		setUrlContext("admin/driverinspection");
	}
	
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(
				Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(
				Vehicle.class));
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
//		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(
//				Customer.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(
				SubContractor.class));
	}
	
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		DriverInspectionTemp driverInspectionTempObj = new DriverInspectionTemp();
		if(request.getSession().getAttribute("inspcCompany")!=null){
			Location companyObj = genericDAO.getById(Location.class,Long.parseLong((String)request.getSession().getAttribute("inspcCompany")));
			driverInspectionTempObj.setCompany(companyObj);
		}
		if(request.getSession().getAttribute("inspcTerminal")!=null){
			Location terminalObj = genericDAO.getById(Location.class,Long.parseLong((String)request.getSession().getAttribute("inspcTerminal")));
			driverInspectionTempObj.setTerminal(terminalObj);
		}
		if(request.getSession().getAttribute("inspcDriver")!=null)	{
			Driver driverObj = genericDAO.getById(Driver.class,Long.parseLong((String)request.getSession().getAttribute("inspcDriver")));
			driverInspectionTempObj.setDriver(driverObj);
		}
		if(request.getSession().getAttribute("inspcEntered")!=null){
			driverInspectionTempObj.setEnteredBy((String)request.getSession().getAttribute("inspcEntered"));
		}
		if(request.getSession().getAttribute("inspcweekOfDate")!=null)	{	
			try{
			LocalDate localdate = new LocalDate((sdf.parse((String)request.getSession().getAttribute("inspcweekOfDate"))));
			driverInspectionTempObj.setWeekOfDate(localdate.toDate());
			}
			catch(Exception e){
				
			}
		}
		
		model.addAttribute("modelObject",driverInspectionTempObj);
		return urlContext + "/form";
	}
	
	@Override	
	public String edit2(ModelMap model, HttpServletRequest request) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		setupCreate(model, request);
		List dayLists = new ArrayList();
		List driverInspectionIdList = new ArrayList();
		
		DriverInspectionTemp driverInspectionTempObj = new DriverInspectionTemp();		
		DriverInspection inspectionObj = genericDAO.getById(DriverInspection.class,Long.parseLong(request.getParameter("id")));
		driverInspectionTempObj.setId(inspectionObj.getId());
		driverInspectionTempObj.setCompany(inspectionObj.getCompany());
		driverInspectionTempObj.setTerminal(inspectionObj.getTerminal());
		Map criteria = new HashMap();
		criteria.clear();
		criteria.put("name",inspectionObj.getEnteredBy());
		User userObj = genericDAO.getByCriteria(User.class, criteria);
		if(userObj!=null)
			driverInspectionTempObj.setEnteredBy(String.valueOf(userObj.getId()));
		driverInspectionTempObj.setDriver(inspectionObj.getDriver());
		driverInspectionTempObj.setWeekOfDate(inspectionObj.getWeekOfDate());
		criteria.clear();		
		criteria.put("weekOfDate",inspectionObj.getWeekOfDate());
		criteria.put("driver.id",inspectionObj.getDriver().getId());
		criteria.put("company.id",inspectionObj.getCompany().getId());
		criteria.put("terminal.id",inspectionObj.getTerminal().getId());
		List<DriverInspection> driverInspectionList = genericDAO.findByCriteria(DriverInspection.class, criteria,"weekDate",false);
		
		if(driverInspectionList!=null && driverInspectionList.size()>0){			
			for(DriverInspection drvinspectionObj : driverInspectionList){
				if(drvinspectionObj.getInspectionStatus().equalsIgnoreCase("Inspected")){
					dayLists.add(drvinspectionObj.getDayRefColumn());
				}
				driverInspectionIdList.add(drvinspectionObj.getId());
			}			
		}
		
		request.getSession().setAttribute("inspectionDriverIdlist",driverInspectionIdList);
		driverInspectionTempObj.setDaysList(dayLists);
		model.addAttribute("modelObject",driverInspectionTempObj);
		return urlContext + "/form";
	}
	
	
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();		
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		criterias.put("status",1);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		
		
		
	}
	
	
	@RequestMapping(value="/start.do", method=RequestMethod.GET)
	public String showReportForm(ModelMap model, HttpServletRequest request){
		Map criterias = new HashMap();		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		return urlContext + "/updateform";
	}
	
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		//setupCreate(model, request);
		Map criterias = new HashMap();		
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		criterias.put("status",1);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
	
		List<String> inspectionStatus = new ArrayList<String>(Arrays.asList("Inspected","No Loads","Violated","On Leave"));
		model.addAttribute("inspectionStatuss",inspectionStatus);
	}
	
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		List diverinspectionList = genericDAO.search(getEntityClass(), criteria,"company asc,terminal asc,driver.fullName asc,weekOfDate asc,weekDate",false);
		model.addAttribute("list",diverinspectionList);
		request.getSession().setAttribute("driverInspectionList",diverinspectionList);
		request.getSession().setAttribute("driverInspectionCriteria",criteria);
		return urlContext + "/list";
	}
	
	/*@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		try{
			
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");	
			
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		
		String weekofDateTemp = (String) criteria.getSearchMap().get("weekofDate");
	    String weekofDateToTemp = (String) criteria.getSearchMap().get("weekofDateTo");
	    String weekDateTemp = (String) criteria.getSearchMap().get("weekDate");
	    String weekDateToTemp = (String) criteria.getSearchMap().get("weekDateTo");
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekofDate"))) 
	    	criteria.getSearchMap().put("weekofDate",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekofDate"))));
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekofDateTo"))) 
		     criteria.getSearchMap().put("weekofDateTo",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekofDateTo"))));
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekDate"))) 
		     criteria.getSearchMap().put("weekDate",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekDate"))));
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekDateTo"))) 
		     criteria.getSearchMap().put("weekDateTo",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekDateTo"))));
	    
	    
		
		//dateupdateService.updateDate(request, "weekofDate", "weekOfDate");
		//dateupdateService.updateDate(request, "weekofDateTo", "weekOfDateTo");
		
		//dateupdateService.updateDate(request, "inspDateFrom", "weekDate");
		//dateupdateService.updateDate(request, "inspDateTo", "weekDateTo");
		
		List<DriverInspection> diverinspectionList = genericDAO.search(getEntityClass(), criteria,"company asc,terminal asc,driver.fullName asc,weekOfDate asc,weekDate asc",null,null);
		model.addAttribute("list",diverinspectionList);
		
		criteria.getSearchMap().put("weekofDate",weekofDateTemp);
		criteria.getSearchMap().put("weekofDateTo",weekofDateToTemp);
		criteria.getSearchMap().put("weekDate",weekDateTemp);
		criteria.getSearchMap().put("weekDateTo",weekDateToTemp);
		
		
		
		request.getSession().setAttribute("driverInspectionList",diverinspectionList);
		request.getSession().setAttribute("driverInspectionCriteria",criteria);		
		
			
				
		
			
		
	    String date1=dateFormat1.format(sdf.parse(request.getParameter("weekDate")));
	    String date2=dateFormat1.format(sdf.parse(request.getParameter("weekDateTo")));
	    
	    List<String> allDates = new ArrayList<String>();
	    LocalDate localDate1 = new LocalDate(date1);
	    LocalDate localDate2 = new LocalDate(date2);
	    
	    LocalDate monday = localDate1.withDayOfWeek(DateTimeConstants.MONDAY);
	    LocalDate sunday = localDate2.withDayOfWeek(DateTimeConstants.SUNDAY);
	    
	    while(monday.compareTo(sunday)!=1){
	    	allDates.add(sdf.format(monday.toDate()));
	    	monday = monday.plusDays(1);
	    }	    	
	    
		 return urlContext + "/list";
		}
		catch (Exception e) {
			 return urlContext + "/list";
		}
	}*/
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		try{
			
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");	
			
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		
		/*String weekofDateTemp = (String) criteria.getSearchMap().get("weekofDate");
	    String weekofDateToTemp = (String) criteria.getSearchMap().get("weekofDateTo");
	    String weekDateTemp = (String) criteria.getSearchMap().get("weekDate");
	    String weekDateToTemp = (String) criteria.getSearchMap().get("weekDateTo");*/
	    
	    /*if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekofDate"))) 
	    	criteria.getSearchMap().put("weekofDate",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekofDate"))));
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekofDateTo"))) 
		     criteria.getSearchMap().put("weekofDateTo",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekofDateTo"))));
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekDate"))) 
		     criteria.getSearchMap().put("weekDate",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekDate"))));
	    
	    if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("weekDateTo"))) 
		     criteria.getSearchMap().put("weekDateTo",dateFormat1.format(sdf.parse((String)criteria.getSearchMap().get("weekDateTo"))));
	    */
	    
		
		//dateupdateService.updateDate(request, "weekofDate", "weekOfDate");
		//dateupdateService.updateDate(request, "weekofDateTo", "weekOfDateTo");
		
		//dateupdateService.updateDate(request, "inspDateFrom", "weekDate");
		//dateupdateService.updateDate(request, "inspDateTo", "weekDateTo");
		
		List<DriverInspection> diverinspectionList = genericDAO.search(getEntityClass(), criteria,"company asc,terminal asc,driver.fullName asc,weekOfDate asc,weekDate asc",null,null);
		model.addAttribute("list",diverinspectionList);
		
	/*	criteria.getSearchMap().put("weekofDate",weekofDateTemp);
		criteria.getSearchMap().put("weekofDateTo",weekofDateToTemp);
		criteria.getSearchMap().put("weekDate",weekDateTemp);
		criteria.getSearchMap().put("weekDateTo",weekDateToTemp);*/
		
		
		
		request.getSession().setAttribute("driverInspectionList",diverinspectionList);
		request.getSession().setAttribute("driverInspectionCriteria",criteria);		
		
			
				
		
			
		
	    String date1=dateFormat1.format(sdf.parse(request.getParameter("weekDate")));
	    String date2=dateFormat1.format(sdf.parse(request.getParameter("weekDateTo")));
	    
	    List<String> allDates = new ArrayList<String>();
	    LocalDate localDate1 = new LocalDate(date1);
	    LocalDate localDate2 = new LocalDate(date2);
	    
	    LocalDate monday = localDate1.withDayOfWeek(DateTimeConstants.MONDAY);
	    LocalDate sunday = localDate2.withDayOfWeek(DateTimeConstants.SUNDAY);
	    
	    while(monday.compareTo(sunday)!=1){
	    	allDates.add(sdf.format(monday.toDate()));
	    	monday = monday.plusDays(1);
	    }	    	
	    
		 return urlContext + "/list";
		}
		catch (Exception e) {
			 return urlContext + "/list";
		}
	}
	
	
	
	
	@RequestMapping(value="/autoupdatedriverinspection.do", method=RequestMethod.POST)
	public String UpdateDriverInspection(ModelMap model, HttpServletRequest request) throws ParseException{
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");	
		
		String dateFrom = dateFormat1.format(sdf.parse(request.getParameter("fromDate")));
		String dateTo = dateFormat1.format(sdf.parse(request.getParameter("toDate")));		
		
		LocalDate now = new LocalDate(dateFrom);
		
		LocalDate firstMondayDate = now.withDayOfWeek(DateTimeConstants.MONDAY);		
		LocalDate monthEndDate = new LocalDate(dateTo);	
		
		
		Calendar c1 = Calendar.getInstance();  
        c1.setTime(firstMondayDate.toDate());  
   
        Calendar c2 = Calendar.getInstance();  
        c2.setTime(monthEndDate.toDate());  
   
        int mondays = 0;  
        List<Date> mondaydates =new ArrayList<Date>();
        while(c1.before(c2)) {  
            if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)  {
            	mondaydates.add(c1.getTime());           	
            	mondays++;  
            }
                
            c1.add(Calendar.DATE,1);  
        }      
        
        StringBuilder drvQuery = new StringBuilder("select obj from Driver obj where obj.status=1 and obj.catagory=2 and obj.id not in (463,596,597,634)");
		if(!StringUtils.isEmpty(request.getParameter("company.id"))){
			drvQuery.append(" and obj.company=").append(request.getParameter("company.id"));
		}
		if(!StringUtils.isEmpty(request.getParameter("terminal.id"))){
			drvQuery.append(" and obj.terminal=").append(request.getParameter("terminal.id"));
		}
		
		List<Driver> drivers = genericDAO.executeSimpleQuery(drvQuery.toString());	
		
		if(!drivers.isEmpty()){			
			for(Driver driver:drivers){				
				for(int i=0;i<mondays;i++){
					if(driver.getDateHired()!=null && driver.getDateReHired()!=null){
						if(driver.getDateReHired().getTime()>mondaydates.get(i).getTime()){
							// do nothing 
						}
						else{
							autoUpdateDriverInspection1(mondaydates.get(i),driver);
						}
					}
					else if (driver.getDateHired()!=null && driver.getDateReHired()==null){
						if(driver.getDateHired().getTime()>mondaydates.get(i).getTime()){
							// do nothing 
						}
						else{
							autoUpdateDriverInspection1(mondaydates.get(i),driver);
						}
					}
					else if (driver.getDateHired()==null && driver.getDateReHired()!=null){
						if(driver.getDateReHired().getTime()>mondaydates.get(i).getTime()){
							// do nothing 
						}
						else{
							autoUpdateDriverInspection1(mondaydates.get(i),driver);
						}
						
					}
				}
			}
		}
		
		
		
		// Logic for inactive drivers
		
		
		StringBuilder inactivedrvQuery = new StringBuilder("select obj from Driver obj where obj.status=0 and obj.catagory=2 and obj.id not in (463,596,597,634)");
		if(!StringUtils.isEmpty(request.getParameter("company.id"))){
			inactivedrvQuery.append(" and obj.company=").append(request.getParameter("company.id"));
		}
		if(!StringUtils.isEmpty(request.getParameter("terminal.id"))){
			inactivedrvQuery.append(" and obj.terminal=").append(request.getParameter("terminal.id"));
		}
		
		List<Driver> inactivedrivers = genericDAO.executeSimpleQuery(inactivedrvQuery.toString());	
		
		if(!drivers.isEmpty()){			
			for(Driver driver:inactivedrivers){				
				for(int i=0;i<mondays;i++){
					if(driver.getDateHired()!=null && driver.getDateReHired()!=null){
						if(driver.getDateReHired().getTime()>mondaydates.get(i).getTime()){
							// do nothing 
						}
						else{
							if(driver.getDateTerminated()!=null){
								if(driver.getDateReHired().getTime()<=mondaydates.get(i).getTime() && driver.getDateTerminated().getTime()>=mondaydates.get(i).getTime()){
									autoUpdateDriverInspection1(mondaydates.get(i),driver);
								}								
							}
							
						}
					}
					else if (driver.getDateHired()!=null && driver.getDateReHired()==null){
						if(driver.getDateHired().getTime()>mondaydates.get(i).getTime()){
							// do nothing 
						}
						else{
							if(driver.getDateTerminated()!=null){
								if(driver.getDateHired().getTime()<=mondaydates.get(i).getTime() && driver.getDateTerminated().getTime()>=mondaydates.get(i).getTime()){
									autoUpdateDriverInspection1(mondaydates.get(i),driver);
								}								
							}
						}
					}
					else if (driver.getDateHired()==null && driver.getDateReHired()!=null){
						if(driver.getDateReHired().getTime()>mondaydates.get(i).getTime()){
							// do nothing 
						}
						else{
							if(driver.getDateTerminated()!=null){
								if(driver.getDateReHired().getTime()<=mondaydates.get(i).getTime() && driver.getDateTerminated().getTime()>=mondaydates.get(i).getTime()){
									autoUpdateDriverInspection1(mondaydates.get(i),driver);
								}								
							}
						}						
					}
				}
			}
		}		
		//***** Logic for inactive drivers ends        
        
		request.getSession().setAttribute("msg","Successfully Updated Driver Inspection Records.");
		return "redirect:/" + urlContext + "/start.do";
	}
	
	
	
	public void autoUpdateDriverInspection1(Date weekOfDate,Driver driver){

		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		StringBuilder sb= new StringBuilder();
		List<Date> weekDateList = new ArrayList<Date>();
		List<String> weekDateDaysList = new ArrayList<String>();
		List<String> daysList =  new ArrayList<String>(Arrays.asList("Mon", "Tue", "Wed","Thu","Fri","Sat","Sun"));
		
		String weekofdate=dateFormat1.format(weekOfDate);		
		LocalDate now = new LocalDate(weekofdate);
		
		LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
		sb.append("'"+dateFormat1.format(monday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(monday.toDate()));	
		weekDateList.add(monday.toDate());
		
		LocalDate tuesday = now.withDayOfWeek(DateTimeConstants.TUESDAY);
		sb.append("'"+dateFormat1.format(tuesday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(tuesday.toDate()));
		weekDateList.add(tuesday.toDate());
		
		LocalDate wednesday = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
		sb.append("'"+dateFormat1.format(wednesday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(wednesday.toDate()));
		weekDateList.add(wednesday.toDate());
		
		LocalDate thursday = now.withDayOfWeek(DateTimeConstants.THURSDAY);
		sb.append("'"+dateFormat1.format(thursday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(thursday.toDate()));
		weekDateList.add(thursday.toDate());
		
		LocalDate friday = now.withDayOfWeek(DateTimeConstants.FRIDAY);
		sb.append("'"+dateFormat1.format(friday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(friday.toDate()));
		weekDateList.add(friday.toDate());
		
		LocalDate saturday = now.withDayOfWeek(DateTimeConstants.SATURDAY);
		sb.append("'"+dateFormat1.format(saturday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(saturday.toDate()));
		weekDateList.add(saturday.toDate());
		
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		sb.append("'"+dateFormat1.format(sunday.toDate())+" 00:00:00'");
		weekDateDaysList.add(sdf.format(sunday.toDate()));
		weekDateList.add(sunday.toDate());		
		
		String[] weekDateForTcktQuery = sb.toString().split(",");
		
		
        StringBuffer duplicateCheckQuery = new StringBuffer("Select obj from DriverInspection obj where obj.company="+driver.getCompany().getId()+" and obj.terminal="+driver.getTerminal().getId()+" and obj.driver="+driver.getId()+"  and obj.weekDate IN ("+sb.toString()+")");
		List<DriverInspection> driverInspection = genericDAO.executeSimpleQuery(duplicateCheckQuery.toString());
		if(driverInspection!=null && driverInspection.size()>0){
					// do nothing
		}
		else{					
			for(int i=0;i<7;i++){
				DriverInspection driverInspectionObj = new DriverInspection();
				driverInspectionObj.setCompany(driver.getCompany());
				driverInspectionObj.setTerminal(driver.getTerminal());
		      	driverInspectionObj.setDriver(driver);
				driverInspectionObj.setEnteredBy("Automatic");
				driverInspectionObj.setWeekOfDate(monday.toDate());
				driverInspectionObj.setWeekOfDateTo(monday.toDate());
				driverInspectionObj.setWeekDate(weekDateList.get(i));
				driverInspectionObj.setWeekDateDay(weekDateDaysList.get(i)+" - "+daysList.get(i));
				driverInspectionObj.setDayRefColumn("day"+(i+1));
				
			if(driver.getCompany().getId()==299){
				String timesheetquery = "";
				if(i==0) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.mdate="+weekDateForTcktQuery[i]+" and obj.mhoursworked is not null";
				else if(i==1) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.tdate="+weekDateForTcktQuery[i]+" and obj.thoursworked is not null";
				else if(i==2) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.wdate="+weekDateForTcktQuery[i]+" and obj.whoursworked is not null";
				else if(i==3) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.thdate="+weekDateForTcktQuery[i]+" and obj.thhoursworked is not null";
				else if(i==4) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.fdate="+weekDateForTcktQuery[i]+" and obj.fhoursworked is not null";
				else if(i==5) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.stadate="+weekDateForTcktQuery[i]+" and obj.sthoursworked is not null";
				else if(i==6) 
					timesheetquery = "select obj from TimeSheet obj where obj.driver="+driver.getId()+" and obj.sdate="+weekDateForTcktQuery[i]+" and obj.shoursworked is not null";
				
			 
			 
			 System.out.println("***** The timesheet is "+timesheetquery);
			 List<TimeSheet> timesheetList = genericDAO.executeSimpleQuery(timesheetquery);
			 if(timesheetList!=null && timesheetList.size()>0){
				 driverInspectionObj.setInspectionStatus("Violated"); 
			 }
			 else{
				 driverInspectionObj.setInspectionStatus("No Loads");
			 }
			}else{
				String query = "select obj from Ticket obj where obj.terminal="+driver.getTerminal().getId()+" and obj.driverCompany="+driver.getCompany().getId()+" and obj.driver="+driver.getId()+" and (obj.loadDate="+weekDateForTcktQuery[i]+" OR obj.unloadDate="+weekDateForTcktQuery[i]+")";
				//System.out.println("***** The ticket query is "+query);
				List<Ticket> tickets = genericDAO.executeSimpleQuery(query);
				if(tickets.size()>0 && !tickets.isEmpty()){
					String ptodapplication = "select obj from Ptodapplication obj where obj.company="+driver.getCompany().getId()+" and obj.terminal="+driver.getTerminal().getId()+" and obj.driver="+driver.getId()+" and obj.leavedatefrom<="+weekDateForTcktQuery[i]+" and leavedateto>="+weekDateForTcktQuery[i];
					List<Ptodapplication> ptodApplication = genericDAO.executeSimpleQuery(ptodapplication);
					if(ptodApplication.size()>0 && !ptodApplication.isEmpty()){	
						driverInspectionObj.setInspectionStatus("On Leave");
					}
					else{
					 driverInspectionObj.setInspectionStatus("Violated");
					}
				}
				else{
					driverInspectionObj.setInspectionStatus("No Loads");
				}
			}
				genericDAO.saveOrUpdate(driverInspectionObj);
			}
		}
	}

	
	
	//@RequestMapping(value="/autoupdatedriverinspection.do", method=RequestMethod.POST)
	public String autoUpdateDriverInspection(ModelMap model, HttpServletRequest request){
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		StringBuilder sb= new StringBuilder();
		List<Date> weekDateList = new ArrayList<Date>();
		List<String> weekDateDaysList = new ArrayList<String>();
		List<String> daysList =  new ArrayList<String>(Arrays.asList("Mon", "Tue", "Wed","Thu","Fri","Sat","Sun"));
		
		String batchdate=dateFormat1.format(new Date());		
		LocalDate now = new LocalDate(batchdate);
		
		LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
		sb.append("'"+dateFormat1.format(monday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(monday.toDate()));	
		weekDateList.add(monday.toDate());
		
		LocalDate tuesday = now.withDayOfWeek(DateTimeConstants.TUESDAY);
		sb.append("'"+dateFormat1.format(tuesday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(tuesday.toDate()));
		weekDateList.add(tuesday.toDate());
		
		LocalDate wednesday = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
		sb.append("'"+dateFormat1.format(wednesday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(wednesday.toDate()));
		weekDateList.add(wednesday.toDate());
		
		LocalDate thursday = now.withDayOfWeek(DateTimeConstants.THURSDAY);
		sb.append("'"+dateFormat1.format(thursday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(thursday.toDate()));
		weekDateList.add(thursday.toDate());
		
		LocalDate friday = now.withDayOfWeek(DateTimeConstants.FRIDAY);
		sb.append("'"+dateFormat1.format(friday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(friday.toDate()));
		weekDateList.add(friday.toDate());
		
		LocalDate saturday = now.withDayOfWeek(DateTimeConstants.SATURDAY);
		sb.append("'"+dateFormat1.format(saturday.toDate())+" 00:00:00',");
		weekDateDaysList.add(sdf.format(saturday.toDate()));
		weekDateList.add(saturday.toDate());
		
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		sb.append("'"+dateFormat1.format(sunday.toDate())+" 00:00:00'");
		weekDateDaysList.add(sdf.format(sunday.toDate()));
		weekDateList.add(sunday.toDate());
		
		
		String[] weekDateForTcktQuery = sb.toString().split(",");
		
		
		String drvQuery = "select obj from Driver obj where obj.status=1";
		
		List<Driver> drivers = genericDAO.executeSimpleQuery(drvQuery);
		
		if(!drivers.isEmpty()){			
			for(Driver driver:drivers){
				StringBuffer duplicateCheckQuery = new StringBuffer("Select obj from DriverInspection obj where obj.company="+driver.getCompany().getId()+" and obj.terminal="+driver.getTerminal().getId()+" and obj.driver="+driver.getId()+"  and obj.weekDate IN ("+sb.toString()+")");
				List<DriverInspection> driverInspection = genericDAO.executeSimpleQuery(duplicateCheckQuery.toString());
				if(driverInspection!=null && driverInspection.size()>0){
					// do nothing
				}
				else{					
					for(int i=0;i<7;i++){
						DriverInspection driverInspectionObj = new DriverInspection();
						driverInspectionObj.setCompany(driver.getCompany());
						driverInspectionObj.setTerminal(driver.getTerminal());
						driverInspectionObj.setDriver(driver);
						driverInspectionObj.setEnteredBy("Automatic");
						driverInspectionObj.setWeekOfDate(saturday.toDate());
						driverInspectionObj.setWeekOfDateTo(saturday.toDate());
						driverInspectionObj.setWeekDate(weekDateList.get(i));
						driverInspectionObj.setWeekDateDay(weekDateDaysList.get(i)+" - "+daysList.get(i));
						driverInspectionObj.setDayRefColumn("day"+(i+1));
						String query = "select obj from Ticket obj where obj.terminal="+driver.getTerminal().getId()+" and obj.driverCompany="+driver.getCompany().getId()+" and obj.driver="+driver.getId()+" and (obj.loadDate="+weekDateForTcktQuery[i]+" OR obj.unloadDate="+weekDateForTcktQuery[i]+")";
						System.out.println("***** The ticket query is "+query);
						List<Ticket> tickets = genericDAO.executeSimpleQuery(query);
						if(tickets.size()>0 && !tickets.isEmpty()){
							driverInspectionObj.setInspectionStatus("Violated");
						}
						else{
							driverInspectionObj.setInspectionStatus("No Loads");
						}
						
						genericDAO.saveOrUpdate(driverInspectionObj);
					}
				}
			}			
		}
		request.getSession().setAttribute("msg","Successfully Updated Driver Inspection Information.");
		return urlContext + "/updateform";
	}
	
	
	
	@RequestMapping(value="/savedriverinspection.do", method=RequestMethod.POST)
	public String saveDriverInspection(@RequestParam( value="daysList",required=false) String daysSelected,HttpServletRequest request,ModelMap model) throws ParseException  {
		User userObjToCheck=null;
		if(request.getParameter("enteredBy")!=null){
			userObjToCheck = genericDAO.getById(User.class,Long.parseLong(request.getParameter("enteredBy")));			
		}		
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		StringBuilder sb= new StringBuilder();
		
		String batchdate=dateFormat1.format(sdf.parse(request.getParameter("weekOfDate")));
		
		LocalDate now = new LocalDate(batchdate);
		
		LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
		sb.append("'"+dateFormat1.format(monday.toDate())+" 00:00:00',");		
		
		//String mondayDate = sdf.format(monday.toDate());		
		
		/*if(mondayDate.equals(request.getParameter("weekOfDate"))){
			//do nothing 
		}
		else{
			request.getSession().setAttribute("inspcCompany", request.getParameter("company"));
			request.getSession().setAttribute("inspcTerminal", request.getParameter("terminal"));
			request.getSession().setAttribute("inspcDriver", request.getParameter("driver"));
			request.getSession().setAttribute("inspcEntered", request.getParameter("enteredBy"));
			request.getSession().setAttribute("inspcweekOfDate", request.getParameter("weekOfDate"));
			
			request.getSession().setAttribute("error",
			"Week of Date must be Monday Date.");
			
			return "redirect:/" + urlContext + "/create.do";
		}*/
		
		
		LocalDate tuesday = now.withDayOfWeek(DateTimeConstants.TUESDAY);
		sb.append("'"+dateFormat1.format(tuesday.toDate())+" 00:00:00',");
		LocalDate wednesday = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
		sb.append("'"+dateFormat1.format(wednesday.toDate())+" 00:00:00',");
		LocalDate thursday = now.withDayOfWeek(DateTimeConstants.THURSDAY);
		sb.append("'"+dateFormat1.format(thursday.toDate())+" 00:00:00',");
		LocalDate friday = now.withDayOfWeek(DateTimeConstants.FRIDAY);
		sb.append("'"+dateFormat1.format(friday.toDate())+" 00:00:00',");
		LocalDate saturday = now.withDayOfWeek(DateTimeConstants.SATURDAY);
		sb.append("'"+dateFormat1.format(saturday.toDate())+" 00:00:00',");
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		sb.append("'"+dateFormat1.format(sunday.toDate())+" 00:00:00'");
	
		StringBuffer duplicateCheckQuery = new StringBuffer("Select obj from DriverInspection obj where obj.company="+request.getParameter("company")+" and obj.terminal="+request.getParameter("terminal")+" and obj.driver="+request.getParameter("driver")+"  and obj.weekDate IN ("+sb.toString()+")");
		
		
		
		if(!StringUtils.isEmpty(request.getParameter("id"))){
			List idList = (List)request.getSession().getAttribute("inspectionDriverIdlist");
			String driverIds ="";
			for(int i=0; i < idList.size(); i++ ){
				if(driverIds.equalsIgnoreCase(""))
					driverIds=idList.get(i).toString();
				else
					driverIds=driverIds+","+idList.get(i).toString();
			}
			duplicateCheckQuery.append(" and obj.id not in (").append(driverIds).append(")");
		}
		
		
		List<DriverInspection> driverInspection = genericDAO.executeSimpleQuery(duplicateCheckQuery.toString());
		
		
		request.getSession().setAttribute("inspcCompany", request.getParameter("company"));
		request.getSession().setAttribute("inspcTerminal", request.getParameter("terminal"));
		request.getSession().setAttribute("inspcDriver", request.getParameter("driver"));
		request.getSession().setAttribute("inspcEntered", request.getParameter("enteredBy"));
		request.getSession().setAttribute("inspcweekOfDate", request.getParameter("weekOfDate"));
		
		if(driverInspection!=null && driverInspection.size()>0){
			request.getSession().setAttribute("error",
			"Duplicate Entry!. Inspection Record with same data exist already.");
		}
		else{
		if(!StringUtils.isEmpty(request.getParameter("id"))){
			List idList = (List)request.getSession().getAttribute("inspectionDriverIdlist");
			String driverIds ="";
			for(int i=0; i < idList.size(); i++ ){
				if(driverIds.equalsIgnoreCase(""))
					driverIds=idList.get(i).toString();
				else
					driverIds=driverIds+","+idList.get(i).toString();
			}
			
			String deleteQuery = "delete from DriverInspection where id in ("+driverIds+")";
			
			genericDAO.executeSimpleUpdateQuery(deleteQuery);
		}
		
		List<String> daysList =  new ArrayList<String>(Arrays.asList("day1", "day2", "day3","day4","day5","day6","day7"));
		Map<String,String> map = new HashMap<String,String>();
		map.put(request.getParameter("day1"), "Mon");
		map.put(request.getParameter("day2"), "Tue");
		map.put(request.getParameter("day3"), "Wed");
		map.put(request.getParameter("day4"), "Thu");
		map.put(request.getParameter("day5"), "Fri");
		map.put(request.getParameter("day6"), "Sat");
		map.put(request.getParameter("day7"), "Sun");
		String[] dayslected={};
		if(!StringUtils.isEmpty(daysSelected))		
			dayslected = daysSelected.split(",");		
		
		for(int i=0; i < dayslected.length;i++ ){
			
			String inspectiondate = request.getParameter(dayslected[i]);			
			DriverInspection driverInspectionObj = new DriverInspection();
			
			
			if(request.getParameter("company")!=null){
				Location companyObj = genericDAO.getById(Location.class,Long.parseLong(request.getParameter("company")));
				driverInspectionObj.setCompany(companyObj);
			}
			
			if(request.getParameter("terminal")!=null){
				Location terminalObj = genericDAO.getById(Location.class,Long.parseLong(request.getParameter("terminal")));
				driverInspectionObj.setTerminal(terminalObj);
			}
			
			if(request.getParameter("driver")!=null){
				Driver driverObj = genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				driverInspectionObj.setDriver(driverObj);
			}
			
			if(request.getParameter("enteredBy")!=null){
				User userObj = genericDAO.getById(User.class,Long.parseLong(request.getParameter("enteredBy")));
				driverInspectionObj.setEnteredBy(userObj.getName());
			}
			
			
			if(request.getParameter("weekOfDate")!=null){	
				try{
					LocalDate localdate = new LocalDate(mysqldf.format(sdf.parse(request.getParameter("weekOfDate"))));					
					driverInspectionObj.setWeekOfDate(localdate.toDate());
					driverInspectionObj.setWeekOfDateTo(localdate.toDate());
				}
				catch (Exception e) {
					System.out.println("Error in parsing date - Driver Inspection"+e);
				}
			}
			
			
			
			
			try {
				LocalDate localdate = new LocalDate(mysqldf.format(sdf.parse(inspectiondate)));				
				driverInspectionObj.setWeekDate(localdate.toDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			driverInspectionObj.setWeekDateDay(String.valueOf(inspectiondate+" - "+map.get(inspectiondate)));
			
			driverInspectionObj.setInspectionStatus("Inspected");
			
			driverInspectionObj.setDayRefColumn(dayslected[i]);
			
			beforeSave(request, driverInspectionObj, model);
			genericDAO.saveOrUpdate(driverInspectionObj);
			
			daysList.remove(dayslected[i]);	
			
		}
		
		Location companyObj = null;
		Location terminalObj = null;
		Driver driverObj = null;
		User userObj = null;
		
		for(int i=0; i < daysList.size();i++ ){	
			
			
			String inspectiondate = request.getParameter(daysList.get(i));			
			DriverInspection driverInspectionObj = new DriverInspection();
			
			if(request.getParameter("company")!=null){
				companyObj = genericDAO.getById(Location.class,Long.parseLong(request.getParameter("company")));
				driverInspectionObj.setCompany(companyObj);
			}
			
			if(request.getParameter("terminal")!=null){
				terminalObj = genericDAO.getById(Location.class,Long.parseLong(request.getParameter("terminal")));
				driverInspectionObj.setTerminal(terminalObj);
			}
			
			if(request.getParameter("driver")!=null){
				driverObj = genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				driverInspectionObj.setDriver(driverObj);
			}
			
			if(request.getParameter("enteredBy")!=null){
				userObj = genericDAO.getById(User.class,Long.parseLong(request.getParameter("enteredBy")));
				driverInspectionObj.setEnteredBy(userObj.getName());
			}
			
			
			if(request.getParameter("weekOfDate")!=null){	
				try{
					LocalDate localdate = new LocalDate(mysqldf.format(sdf.parse(request.getParameter("weekOfDate"))));					
					driverInspectionObj.setWeekOfDate(localdate.toDate());
					driverInspectionObj.setWeekOfDateTo(localdate.toDate());
				}
				catch (Exception e) {
					System.out.println("Error in parsing date - Driver Inspection"+e);
				}
			}
			
			
			try {
				LocalDate localdate = new LocalDate(mysqldf.format(sdf.parse(inspectiondate)));				
				driverInspectionObj.setWeekDate(localdate.toDate());
			
			if(driverObj.getCompany().getId()==299){
					String timesheetquery = "";
					if(i==0) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.mdate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.mhoursworked is not null";
					else if(i==1) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.tdate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.thoursworked is not null";
					else if(i==2) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.wdate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.whoursworked is not null";
					else if(i==3) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.thdate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.thhoursworked is not null";
					else if(i==4) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.fdate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.fhoursworked is not null";
					else if(i==5) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.stadate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.sthoursworked is not null";
					else if(i==6) 
						timesheetquery = "select obj from TimeSheet obj where obj.driver="+driverObj.getId()+" and obj.sdate='"+dateFormat1.format(localdate.toDate())+" 00:00:00' and obj.shoursworked is not null";
					
				 
				 
				 System.out.println("***** The timesheet is "+timesheetquery);
				 List<TimeSheet> timesheetList = genericDAO.executeSimpleQuery(timesheetquery);
				 if(timesheetList!=null && timesheetList.size()>0){
					 driverInspectionObj.setInspectionStatus("Violated"); 
				 }
				 else{
					 driverInspectionObj.setInspectionStatus("No Loads");
				 }
			}			
			else{
				String query = "select obj from Ticket obj where obj.terminal="+terminalObj.getId()+" and obj.driverCompany="+companyObj.getId()+" and obj.driver="+driverObj.getId()+" and (obj.loadDate='"+mysqldf.format(localdate.toDate())+" 00:00:00' OR obj.unloadDate='"+mysqldf.format(localdate.toDate())+" 00:00:00')";
				
				List<Ticket> tickets = genericDAO.executeSimpleQuery(query);
				if(tickets.size()>0 && !tickets.isEmpty()){					
					String ptodapplication = "select obj from Ptodapplication obj where obj.company="+companyObj.getId()+" and obj.terminal="+terminalObj.getId()+" and obj.driver="+driverObj.getId()+" and obj.leavedatefrom<='"+mysqldf.format(localdate.toDate())+" 00:00:00' and leavedateto>='"+mysqldf.format(localdate.toDate())+" 00:00:00'";
					List<Ptodapplication> ptodApplication = genericDAO.executeSimpleQuery(ptodapplication);
					if(ptodApplication.size()>0 && !ptodApplication.isEmpty()){	
						driverInspectionObj.setInspectionStatus("On Leave");
					}
					else{
					 driverInspectionObj.setInspectionStatus("Violated");
					}					
				}
				else{
					driverInspectionObj.setInspectionStatus("No Loads");
				}
			}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			driverInspectionObj.setWeekDateDay(String.valueOf(inspectiondate+" - "+map.get(inspectiondate)));
			
			
			beforeSave(request, driverInspectionObj, model);
			genericDAO.saveOrUpdate(driverInspectionObj);
			
		}	
		
		request.getSession().setAttribute("msg",
		"Driver Inspection Record added successfully");
		}
		
		return "redirect:/" + urlContext + "/create.do";
	}
	
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		
			if ("findDriver".equalsIgnoreCase(action)) {

				List<Driver> dlst=null;
				String terminalId = request.getParameter("terminal");
				String companyId = request.getParameter("company");
				if (!StringUtils.isEmpty(terminalId))
				{				
						String query="select obj from Driver obj where obj.status=1  and obj.terminal="+terminalId+" and obj.company="+companyId+" order by fullName ASC";
						dlst=genericDAO.executeSimpleQuery(query);
					
				}
				Gson gson = new Gson();
				return gson.toJson(dlst);
			}
			
			if ("findTerminal".equalsIgnoreCase(action)) {

				List<Terminal> dlst=null;
				List<Location> dlst1=null;
				String terminalids="";
				String companyId = request.getParameter("company");
				if (!StringUtils.isEmpty(companyId))
				{						
					String query="select obj from Terminal obj where obj.status=1 and obj.company="+companyId+"";
					dlst=genericDAO.executeSimpleQuery(query);						
					if(dlst!=null && dlst.size()>0){
						for(Terminal terminalObj : dlst ){
							if(terminalids.equalsIgnoreCase("")){
								terminalids=terminalObj.getTerminal().getId().toString();
							}
							else{
								terminalids=terminalids+","+terminalObj.getTerminal().getId().toString();
							}
						}
						
						String query1="select obj from Location obj where obj.type=4 and  obj.status=1 and obj.id in ("+terminalids+")order by name ASC";	
						dlst1=genericDAO.executeSimpleQuery(query1);
						
					}
					else{
						
						dlst1=Collections.emptyList();
					}
				}
				Gson gson = new Gson();
				return gson.toJson(dlst1);
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
			
			if ("findAllDriver".equalsIgnoreCase(action)) {

				List<Driver> dlst=null;
					String query="select obj from Driver obj where  obj.status=1 order by fullName ASC";
					dlst=genericDAO.executeSimpleQuery(query);
				
				Gson gson = new Gson();
				return gson.toJson(dlst);
			}
			
			if ("findAllTerminal".equalsIgnoreCase(action)) {

				List<Driver> dlst=null;
				String query="select obj from Location obj where obj.type=4 and  obj.status=1 order by name ASC";
					dlst=genericDAO.executeSimpleQuery(query);
				
				Gson gson = new Gson();
				return gson.toJson(dlst);
			}
			
		
		return "";
	}
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") DriverInspection entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			List driverInspectionIdList = new ArrayList();
			Map criteria = new HashMap();
			DriverInspection inspectionObj = genericDAO.getById(DriverInspection.class,Long.parseLong(request.getParameter("id")));			
			criteria.clear();
			criteria.put("weekOfDate",inspectionObj.getWeekOfDate());
			criteria.put("driver.id",inspectionObj.getDriver().getId());
			criteria.put("company.id",inspectionObj.getCompany().getId());
			criteria.put("terminal.id",inspectionObj.getTerminal().getId());
			List<DriverInspection> driverInspectionList = genericDAO.findByCriteria(DriverInspection.class, criteria,"weekDate",false);
			String driverIds ="";
			if(driverInspectionList!=null && driverInspectionList.size()>0){				
			for(DriverInspection drvinspectionObj : driverInspectionList){
					
				if(driverIds.equalsIgnoreCase(""))
					driverIds=drvinspectionObj.getId().toString();
				else
					driverIds=driverIds+","+drvinspectionObj.getId().toString();
			}
		   }
			
			String deleteQuery = "delete from DriverInspection where id in ("+driverIds+")";
			
			genericDAO.executeSimpleUpdateQuery(deleteQuery);
			
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
	
	
	
	
	@ModelAttribute("modelObject")
	public DriverInspection setupModel(HttpServletRequest request) {
		return new DriverInspection();
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		try {
			SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("driverInspectionCriteria");
			String company = (String)criteria.getSearchMap().get("company.id");
			String terminal = (String)criteria.getSearchMap().get("terminal.id");
			String driver = (String)criteria.getSearchMap().get("driver.id");
			String wodFrom = (String)criteria.getSearchMap().get("weekofDate");
			String wodTo = (String)criteria.getSearchMap().get("weekofDateTo");
			String insDateFrom = (String)criteria.getSearchMap().get("weekDate");
			String insDateTo = (String)criteria.getSearchMap().get("weekDateTo");			
			String inspectionStatus = (String)criteria.getSearchMap().get("inspectionStatus");
			String enteredBy = (String) criteria.getSearchMap().get("inspectionStatus");
			
		
			
			Location compObj = null;
			Location terminalObj = null;
			Driver drvObj=null;
			
			StringBuffer query= new StringBuffer("select obj from DriverInspection obj where 1=1");
			
			if(!StringUtils.isEmpty(company)){
			 List<Location>	comp = genericDAO.executeSimpleQuery("select obj from Location obj where obj.id="+company);
			 compObj = comp.get(0);	
			 query.append(" and obj.company=").append(company);
			}
			if(!StringUtils.isEmpty(terminal)){
				List<Location> term =  genericDAO.executeSimpleQuery("select obj from Location obj where obj.id="+terminal);
				terminalObj =  term.get(0);
				query.append(" and obj.terminal=").append(terminal);
			}
			if(!StringUtils.isEmpty(driver)){
				List<Driver> drv = genericDAO.executeSimpleQuery("select obj from Driver obj where obj.id="+driver);
				drvObj = drv.get(0);
				query.append(" and obj.driver=").append(driver);
			}
			if(!StringUtils.isEmpty(wodFrom)){
				query.append(" and obj.weekOfDate>='").append(dateFormat1.format(sdf.parse(wodFrom))).append("'");
			}
			if(!StringUtils.isEmpty(wodTo)){
				query.append(" and obj.weekOfDate<='").append(dateFormat1.format(sdf.parse(wodTo))).append("'");
			}
			
			if(!StringUtils.isEmpty(insDateFrom)){
				query.append(" and obj.weekDate>='").append(dateFormat1.format(sdf.parse(insDateFrom))).append("'");
			}
			if(!StringUtils.isEmpty(insDateTo)){
				query.append(" and obj.weekDate<='").append(dateFormat1.format(sdf.parse(insDateTo))).append("'");
			}
			
			if(!StringUtils.isEmpty(inspectionStatus)){
				query.append(" and obj.inspectionStatus='").append(inspectionStatus).append("'");
			}
			
			if(!StringUtils.isEmpty(enteredBy)){
				query.append(" and obj.enteredBy='").append(enteredBy).append("'");
			}
			
			query.append(" order by obj.company asc,obj.terminal asc,obj.driver.fullName asc,obj.weekOfDate asc,obj.weekDate asc");
			
			List driverInspectionWrapperlist = new ArrayList();
			System.out.println("******* The query is "+query);
			List<DriverInspection> list = genericDAO.executeSimpleQuery(query.toString());
			System.out.println("The len of list is "+list.size());
			DriverInspectionWrapper driverinspectionwrap = null;
			for(DriverInspection inspectionObj : list){
				driverinspectionwrap = new DriverInspectionWrapper();
				driverinspectionwrap.setCompany(inspectionObj.getCompany().getName());
				driverinspectionwrap.setDriver(inspectionObj.getDriver().getFullName());
				driverinspectionwrap.setTerminal(inspectionObj.getTerminal().getName());
				driverinspectionwrap.setEnteredBy(inspectionObj.getEnteredBy());
				driverinspectionwrap.setInspectionStatus(inspectionObj.getInspectionStatus());
				driverinspectionwrap.setWeekDateDay(inspectionObj.getWeekDateDay());
				driverinspectionwrap.setWeekOfDate(sdf.format(inspectionObj.getWeekOfDate()));
				driverInspectionWrapperlist.add(driverinspectionwrap);
			}		
			
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= driverInspectionReport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = new HashMap<String,Object>();
			
			if(compObj!=null)
				params.put("companyLoc",compObj.getName());
			else
				params.put("companyLoc","");
			
			if(terminalObj!=null)
				params.put("terminalLoc",terminalObj.getName());
			else
				params.put("terminalLoc","");
			
			if(drvObj!=null)
				params.put("driverName",drvObj.getFullName());
			else
				params.put("driverName","");
				
			params.put("enteredByname",enteredBy);
			params.put("wodFrom",wodFrom);
			params.put("wodTo",wodTo);
			params.put("dateFrom",insDateFrom);
			params.put("dateTo",insDateTo);
						
			
			if (!type.equals("print") && !type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("driverInspectionReport",
						driverInspectionWrapperlist, params, type, request);
			}
			else if(type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("driverInspectionReportPdf",
						driverInspectionWrapperlist, params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("driverInspectionReport"+"print",
						driverInspectionWrapperlist, params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			
		}
	}
	
	
}
