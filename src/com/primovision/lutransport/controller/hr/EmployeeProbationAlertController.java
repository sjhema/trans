package com.primovision.lutransport.controller.hr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.controller.JobCronTrigger;
import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.util.DateUtil;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.UpdateLeaveBalanceHistory;
/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/employee/alert")
public class EmployeeProbationAlertController extends BaseController{
	private static SimpleDateFormat logDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
	
	@Autowired
	private GenericDAO genericDAO;
	
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	@RequestMapping(method = RequestMethod.GET,value="/list.do")
	public String listEmployee(ModelMap model , HttpServletRequest request) throws ParseException{
		
		
		request.getSession().removeAttribute("searchCriteria");
		Calendar c=new GregorianCalendar();
		Date currentDate = c.getTime();
		c.add(Calendar.DATE, 30);
		Date d=c.getTime();
		//Date d=DateUtil.get30day();
		SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
		//mysqldf.format(d);
		String query="select obj from Driver obj where obj.status=1 and obj.dateProbationEnd >='"+ mysqldf.format(currentDate) +"' and obj.dateProbationEnd <='"+mysqldf.format(d)+"' order by obj.company.name asc,obj.terminal.name asc,obj.fullName asc,obj.dateProbationEnd asc";
		
		List<Driver> employees=genericDAO.executeSimpleQuery(query);
		
		//checkEmployeeProbation();
		//checkEmployeeAnniversary();
		//checkAnniversaryToUpdateVactaionLeave();
		model.addAttribute("list", employees);
		
		return"/hr/employee/alert";
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET,value="/leaveprobationlist.do")
	public String listLeaveProbationEmployee(ModelMap model , HttpServletRequest request) throws ParseException{
		
		
		request.getSession().removeAttribute("searchCriteria");
		Calendar c=new GregorianCalendar();
		Date currentDate = c.getTime();
		c.add(Calendar.DATE, 30);
		Date d=c.getTime();
		//Date d=DateUtil.get30day();
		SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
		//mysqldf.format(d);
		String query="select obj from Driver obj where obj.status=1 and obj.dateLeaveProbationEnd >='"+ mysqldf.format(currentDate) +"' and obj.dateLeaveProbationEnd <='"+mysqldf.format(d)+"' order by obj.company.name asc,obj.terminal.name asc,obj.fullName asc,obj.dateLeaveProbationEnd asc";
		
		List<Driver> employees=genericDAO.executeSimpleQuery(query);
		
		//checkEmployeeProbation();
		//checkEmployeeAnniversary();
		//checkAnniversaryToUpdateVactaionLeave();
		model.addAttribute("list", employees);
		
		return"/hr/employee/leaveprobationalert";
	}
	
	
	
	
	//@Scheduled(cron="0 0 5 * * ?")
	@Scheduled(cron="0 10 2 * * ?")
	public void checkEmployeeProbation() throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat databasedf = new SimpleDateFormat("yyyy-MM-dd");
		Date curr_date=new Date();	
		
		long startTime = System.currentTimeMillis();
		String formatDateNow = logDateFormat.format(new Date(startTime));
		System.out.println(formatDateNow + " -> Now starting employee probation job");
		
		String query="select obj from Driver obj where obj.status=1 and obj.dateLeaveProbationEnd='"+ databasedf.format(curr_date)+" 00:00:00'";
		List<Driver> employees=genericDAO.executeSimpleQuery(query);
		System.out.println("Employee probation job - No. of employees: " + employees.size());
		if(employees!=null && employees.size()>0){
			for(Driver empObj:employees){
				if(empObj.getCompany().getId() == 4l){
					Map criterias=new HashMap();
					criterias.put("company.id",empObj.getCompany().getId());
					criterias.put("terminal.id", empObj.getTerminal().getId());
					criterias.put("empcategory.id", empObj.getCatagory().getId());
					criterias.put("empname.id", empObj.getId());
					criterias.put("leavetype.id", 1l);
					criterias.put("status",1);
					List<LeaveCurrentBalance> leavebalobj=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
					if(leavebalobj!=null && leavebalobj.size()>0){
						empObj.setLeaveQualifier("WBR");	
						genericDAO.saveOrUpdate(empObj);
					}
					else{
						criterias.clear();
						criterias.put("company.id", empObj.getCompany().getId());
						criterias.put("terminal.id", empObj.getTerminal().getId());
						criterias.put("category.id", empObj.getCatagory().getId());
						criterias.put("leavetype.id", 1l);
						criterias.put("leaveQualifier","4");
						criterias.put("status",1);
						List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criterias);
						if(ptods.size()>0 && ptods!=null){
					
							Ptod ptodObj = ptods.get(0);	
				
							LeaveType sickpersonalobj=genericDAO.getById(LeaveType.class,1l);
							LeaveCurrentBalance leavebalanceObj=new LeaveCurrentBalance();
							leavebalanceObj.setEmpname(empObj);
							leavebalanceObj.setCompany(empObj.getCompany());
							leavebalanceObj.setEmpcategory(empObj.getCatagory());				
							leavebalanceObj.setTerminal(empObj.getTerminal());
							leavebalanceObj.setLeavetype(sickpersonalobj);
							leavebalanceObj.setDaysaccrude(0.0);
							leavebalanceObj.setDayssbalance(ptodObj.getDayearned());
							leavebalanceObj.setDaysavailable(ptodObj.getDayearned());
							leavebalanceObj.setDaysused(0.0);
							leavebalanceObj.setDaysremain(ptodObj.getDayearned());				
							leavebalanceObj.setHoursaccrude(0.0);
							leavebalanceObj.setHoursbalance(ptodObj.getHoursearned());
							leavebalanceObj.setHoursavailable(ptodObj.getHoursearned());
							leavebalanceObj.setHoursused(0.0);
							leavebalanceObj.setHourremain(ptodObj.getHoursearned());						
							Calendar now= Calendar.getInstance() ;						
				
							leavebalanceObj.setDateEffectiveFrom(now.getTime());
						
							int currentYear = now.get(Calendar.YEAR);
				
							now.set(Calendar.YEAR,currentYear);
							now.set(Calendar.MONTH, 11);
							now.set(Calendar.DAY_OF_MONTH, 31);
							leavebalanceObj.setDateEffectiveTo(now.getTime());
				
							genericDAO.saveOrUpdate(leavebalanceObj);
							empObj.setLeaveQualifier("WBR");
							genericDAO.saveOrUpdate(empObj);
							leavebalanceObj=null;
						}	
					}			
					leavebalobj=null;	
			
			}
			else{

				Map criterias=new HashMap();
				criterias.put("company.id",empObj.getCompany().getId());
				criterias.put("terminal.id", empObj.getTerminal().getId());
				criterias.put("empcategory.id", empObj.getCatagory().getId());
				criterias.put("empname.id", empObj.getId());
				criterias.put("leavetype.id", 1l);
				criterias.put("status",1);
				List<LeaveCurrentBalance> leavebalobj=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
				if(leavebalobj!=null && leavebalobj.size()>0){
					empObj.setLeaveQualifier("P");	
					genericDAO.saveOrUpdate(empObj);
				}
				else{
					criterias.clear();
					criterias.put("company.id", empObj.getCompany().getId());
					criterias.put("terminal.id", empObj.getTerminal().getId());
					criterias.put("category.id", empObj.getCatagory().getId());
					criterias.put("leavetype.id", 1l);
					criterias.put("leaveQualifier","1");
					criterias.put("status",1);
					List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criterias);
					if(ptods.size()>0 && ptods!=null){
						
					Ptod ptodObj = ptods.get(0);	
					
					LeaveType sickpersonalobj=genericDAO.getById(LeaveType.class,1l);
					LeaveCurrentBalance leavebalanceObj=new LeaveCurrentBalance();
					leavebalanceObj.setEmpname(empObj);
					leavebalanceObj.setCompany(empObj.getCompany());
					leavebalanceObj.setEmpcategory(empObj.getCatagory());				
					leavebalanceObj.setTerminal(empObj.getTerminal());
					leavebalanceObj.setLeavetype(sickpersonalobj);
					leavebalanceObj.setDaysaccrude(0.0);
					leavebalanceObj.setDayssbalance(ptodObj.getDayearned());
					leavebalanceObj.setDaysavailable(ptodObj.getDayearned());
					leavebalanceObj.setDaysused(0.0);
					leavebalanceObj.setDaysremain(ptodObj.getDayearned());				
					leavebalanceObj.setHoursaccrude(0.0);
					leavebalanceObj.setHoursbalance(ptodObj.getHoursearned());
					leavebalanceObj.setHoursavailable(ptodObj.getHoursearned());
					leavebalanceObj.setHoursused(0.0);
					leavebalanceObj.setHourremain(ptodObj.getHoursearned());						
					DateTime today_date=new DateTime();						
					Date nextDayDate=today_date.toDate();			
					leavebalanceObj.setDateEffectiveFrom(nextDayDate);
					if(empObj.getDateReHired()!=null){
					String actualhired=databasedf.format(empObj.getDateReHired());
					DateTime localHiredDate=new DateTime(actualhired);				
					DateTime newYear = localHiredDate.plusYears(1);				
					leavebalanceObj.setDateEffectiveTo(newYear.toDate());
					}
					else{
						String actualhired=databasedf.format(empObj.getDateHired());
						DateTime localHiredDate=new DateTime(actualhired);				
						DateTime newYear = localHiredDate.plusYears(1);				
						leavebalanceObj.setDateEffectiveTo(newYear.toDate());
					}
					genericDAO.saveOrUpdate(leavebalanceObj);
					empObj.setLeaveQualifier("P");
					genericDAO.saveOrUpdate(empObj);
					leavebalanceObj=null;
					}	
				}			
				leavebalobj=null;	
				
				
			}
		}
	}	
		
		long endTime = System.currentTimeMillis();
		formatDateNow = logDateFormat.format(new Date(endTime));
		System.out.println(formatDateNow + " -> Finished employee probation job. Time taken: "
				+ (endTime-startTime)/1000);
	}
	
	//@Scheduled(cron="0 0 3 * * ?")
	@Scheduled(cron="0 45 1 * * ?")
	public void checkEmployeeAnniversary() throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat databasedf = new SimpleDateFormat("yyyy-MM-dd");
		Date curr_date=new Date();	
		
		long startTime = System.currentTimeMillis();
		String formatDateNow = logDateFormat.format(new Date(startTime));
		System.out.println(formatDateNow + " -> Now starting employee anniversary job");
		
		String query="select obj from Driver obj where obj.status=1 and obj.anniversaryDate='"+ databasedf.format(curr_date)+" 00:00:00'";
		List<Driver> employees=genericDAO.executeSimpleQuery(query);
		System.out.println("Employee anniversary job - No. of employees: " + employees.size());
		if(employees!=null && employees.size()>0){
			for(Driver empObj:employees){		
			Map criterias=new HashMap();
			criterias.put("company.id",empObj.getCompany().getId());
			criterias.put("terminal.id", empObj.getTerminal().getId());
			criterias.put("empcategory.id", empObj.getCatagory().getId());
			criterias.put("empname.id", empObj.getId());
			criterias.put("leavetype.id", 1l);
			criterias.put("status",1);
			List<LeaveCurrentBalance> leavebalobj=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
			if(leavebalobj!=null && leavebalobj.size()>0){				
				
				LeaveCurrentBalance balance = leavebalobj.get(0);
				if(StringUtils.isEmpty(balance.getLeaveQualifier())){
				criterias.clear();
				criterias.put("company.id", empObj.getCompany().getId());
				criterias.put("terminal.id", empObj.getTerminal().getId());
				criterias.put("category.id", empObj.getCatagory().getId());
				criterias.put("leavetype.id", 1l);
				criterias.put("leaveQualifier","2");
				criterias.put("status",1);
				List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criterias);
				if(ptods.size()>0 && ptods!=null){
					for(Ptod ptod:ptods){	
					String acc="No";
					if(ptod.getAnnualoraccrual()==1)
						acc="Yes";
				
					boolean accheck=false;
					if(ptod.getAnnualoraccrual()==1)
						accheck=true;
					
					LeaveCurrentBalance newLeaveBalance = new LeaveCurrentBalance();
					
					newLeaveBalance.setEmpname(balance.getEmpname());
					newLeaveBalance.setCompany(balance.getCompany());
					newLeaveBalance.setEmpcategory(balance.getEmpcategory());				
					newLeaveBalance.setTerminal(balance.getTerminal());
					newLeaveBalance.setLeavetype(balance.getLeavetype());				
					newLeaveBalance.setLeaveQualifier("R");
					if(ptod.getDayearned()!=null){
						if(!empObj.getPayTerm().equals("2")){
						if(accheck){
							Double earnedday=0.0;
							if(balance.getDaysremain()!=null){	
								newLeaveBalance.setDaysaccrude(balance.getDaysremain());
								earnedday=balance.getDaysremain()+ptod.getDayearned();
							}else{
								earnedday=0+ptod.getDayearned();
								newLeaveBalance.setDaysaccrude(0.0);
							}
							newLeaveBalance.setDaysavailable(earnedday);
							newLeaveBalance.setDayssbalance(ptod.getDayearned());
							newLeaveBalance.setDaysused(0.00);
							newLeaveBalance.setDaysremain(earnedday);							
							
						}else{	
							newLeaveBalance.setDaysaccrude(0.0);
							Double earnedday=ptod.getDayearned();
							newLeaveBalance.setDaysavailable(earnedday);
							newLeaveBalance.setDayssbalance(earnedday);
							newLeaveBalance.setDaysused(0.00);
							newLeaveBalance.setDaysremain(earnedday);							
						}
					}
					else{
						newLeaveBalance.setDaysaccrude(0.0);
						Double earnedday=0.0;
						newLeaveBalance.setDaysavailable(earnedday);
						newLeaveBalance.setDayssbalance(earnedday);
						newLeaveBalance.setDaysused(0.00);
						newLeaveBalance.setDaysremain(earnedday);
					}
					}
					
					
					//Hours
					if(ptod.getHoursearned()!=null){
						if(empObj.getPayTerm().equals("2")){
						if(accheck){
							Double earnehour=0.0;
							if(balance.getHourremain()!=null){
								newLeaveBalance.setHoursaccrude(balance.getHourremain());
								earnehour=balance.getHourremain()+ptod.getHoursearned();
							}else{
								newLeaveBalance.setHoursaccrude(0.0);
								earnehour=0+ptod.getHoursearned();						
							}
							newLeaveBalance.setHoursavailable(earnehour);
							newLeaveBalance.setHoursbalance(earnehour);								
							newLeaveBalance.setHoursused(0.00);
							newLeaveBalance.setHourremain(earnehour);							
							
						}else{	
							newLeaveBalance.setHoursaccrude(0.0);
							Double earnehour=balance.getHourremain();
							newLeaveBalance.setHoursavailable(earnehour);
							newLeaveBalance.setHoursbalance(earnehour);
							newLeaveBalance.setHoursused(0.00);
							newLeaveBalance.setHourremain(earnehour);
							
						}
					}
					else{
						newLeaveBalance.setHoursaccrude(0.0);
						Double earnehour=0.0;
						newLeaveBalance.setHoursavailable(earnehour);
						newLeaveBalance.setHoursbalance(earnehour);
						newLeaveBalance.setHoursused(0.00);
						newLeaveBalance.setHourremain(earnehour);	
					}
					}
					
					Calendar now = Calendar.getInstance();
					int currentYear = now.get(Calendar.YEAR);
					
					newLeaveBalance.setDateEffectiveFrom(now.getTime());
					
					now.set(Calendar.YEAR,currentYear);
					now.set(Calendar.MONTH, 11); 
					now.set(Calendar.DAY_OF_MONTH, 31);		
					
					newLeaveBalance.setDateEffectiveTo(now.getTime());

					
					
					genericDAO.saveOrUpdate(newLeaveBalance);
					balance.setStatus(0);
					genericDAO.saveOrUpdate(balance);
					newLeaveBalance =null;
									
				
				
					
				}
				}
			}
			}
			else{
				criterias.clear();
				criterias.put("company.id", empObj.getCompany().getId());
				criterias.put("terminal.id", empObj.getTerminal().getId());
				criterias.put("category.id", empObj.getCatagory().getId());
				criterias.put("leavetype.id", 1l);
				criterias.put("leaveQualifier","2");
				criterias.put("status",1);
				List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criterias);
				if(ptods.size()>0 && ptods!=null){
					
				Ptod ptodObj = ptods.get(0);	
				
				LeaveType sickpersonalobj=genericDAO.getById(LeaveType.class,1l);
				LeaveCurrentBalance leavebalanceObj=new LeaveCurrentBalance();
				leavebalanceObj.setEmpname(empObj);
				leavebalanceObj.setLeaveQualifier("R");
				leavebalanceObj.setCompany(empObj.getCompany());
				leavebalanceObj.setEmpcategory(empObj.getCatagory());				
				leavebalanceObj.setTerminal(empObj.getTerminal());
				leavebalanceObj.setLeavetype(sickpersonalobj);
				
				if(!empObj.getPayTerm().equals("2")){
					leavebalanceObj.setDaysaccrude(0.0);
					leavebalanceObj.setDayssbalance(ptodObj.getDayearned());
					leavebalanceObj.setDaysavailable(ptodObj.getDayearned());
					leavebalanceObj.setDaysused(0.0);
					leavebalanceObj.setDaysremain(ptodObj.getDayearned());		
				}
				else{
					leavebalanceObj.setDaysaccrude(0.0);
					leavebalanceObj.setDayssbalance(0.0);
					leavebalanceObj.setDaysavailable(0.0);
					leavebalanceObj.setDaysused(0.0);
					leavebalanceObj.setDaysremain(0.0);
				}
					
				if(empObj.getPayTerm().equals("2")){
					leavebalanceObj.setHoursaccrude(0.0);
					leavebalanceObj.setHoursbalance(ptodObj.getHoursearned());
					leavebalanceObj.setHoursavailable(ptodObj.getHoursearned());
					leavebalanceObj.setHoursused(0.0);
					leavebalanceObj.setHourremain(ptodObj.getHoursearned());	
				}
				else{
					leavebalanceObj.setHoursaccrude(0.0);
					leavebalanceObj.setHoursbalance(0.0);
					leavebalanceObj.setHoursavailable(0.0);
					leavebalanceObj.setHoursused(0.0);
					leavebalanceObj.setHourremain(0.0);
				}
				
				Calendar now= Calendar.getInstance() ;						
							
				leavebalanceObj.setDateEffectiveFrom(now.getTime());
						
				int currentYear = now.get(Calendar.YEAR);
				
				now.set(Calendar.YEAR,currentYear);
				now.set(Calendar.MONTH, 11);
				now.set(Calendar.DAY_OF_MONTH, 31);
				leavebalanceObj.setDateEffectiveTo(now.getTime());
				
				genericDAO.saveOrUpdate(leavebalanceObj);
				leavebalanceObj=null;
				}	
			}			
			leavebalobj=null;			
		}
	}	
		
		long endTime = System.currentTimeMillis();
		formatDateNow = logDateFormat.format(new Date(endTime));
		System.out.println(formatDateNow + " -> Finished employee anniversary job. Time taken: "
				+ (endTime-startTime)/1000);
	}
	
	@Scheduled(cron="0 20 1 * * ?")
	public void checkAnniversaryToUpdateVactaionLeaveBalance() throws ParseException{		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat databasedf = new SimpleDateFormat("yyyy-MM-dd");
		Date curr_date=new Date();		
		
		long startTime = System.currentTimeMillis();
		String formatDateNow = logDateFormat.format(new Date(startTime));
		System.out.println(formatDateNow + " -> Now starting employee anniversary vacation leave balance job");
		
		String query="select obj from Driver obj where obj.status=1 and obj.nextAnniversaryDate='"+ databasedf.format(curr_date)+" 00:00:00'";
		
		List<Driver> employees=genericDAO.executeSimpleQuery(query);
		System.out.println("Employee anniversary vacation leave balance job - No. of employees: " + employees.size());
		if(employees!=null && employees.size()>0){			
			for(Driver empObj:employees){	
				System.out.println("******* Entered employee"+empObj.getFullName());
			Map criterias=new HashMap();
			criterias.put("company.id",empObj.getCompany().getId());
			criterias.put("terminal.id", empObj.getTerminal().getId());
			criterias.put("empcategory.id", empObj.getCatagory().getId());
			criterias.put("empname.id", empObj.getId());
			criterias.put("leavetype.id", 4l);
			criterias.put("status",1);
			List<LeaveCurrentBalance> leavebalobj=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
			if(leavebalobj!=null && leavebalobj.size()>0){
				System.out.println("******* Entered 007");
				criterias.clear();
				criterias.put("company.id", empObj.getCompany().getId());
				criterias.put("terminal.id", empObj.getTerminal().getId());
				criterias.put("category.id", empObj.getCatagory().getId());
				criterias.put("leavetype.id", 4l);
				//criterias.put("leaveQualifier","2");
				criterias.put("status",1);
				List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criterias);
				if(ptods.size()>0 && ptods!=null){
					for(Ptod ptod:ptods){					
						StringBuffer buffer=new StringBuffer("");
						String acc="No";
						if(ptod.getAnnualoraccrual()==1)
							acc="Yes";
					
						boolean accheck=false;
						if(ptod.getAnnualoraccrual()==1)
							accheck=true;
						
						int fromdays=0;
						int todays=0;
						int days=0;
						if(ptod.getExperienceinyears()!=null)
							fromdays=ptod.getExperienceinyears();
						if(ptod.getExperienceinyearsTo()!=null)
							todays=ptod.getExperienceinyearsTo();
						if(ptod.getExperienceindays()!=null)
							days=ptod.getExperienceindays();
						int count=0;
						
					    DateTime dt = new DateTime();
									    	
					    	
							boolean expelg=false;
							int totex=0;				
							int orgintotex=0;
							if(empObj.getDateReHired()!=null && empObj.getDateHired()!=null){
								
								totex=Days.daysBetween(new DateMidnight(empObj.getDateHired()), new DateMidnight(dt)).getDays();
							}
							else if (empObj.getDateReHired()!=null){
								totex=Days.daysBetween(new DateMidnight(empObj.getDateReHired()), new DateMidnight(dt)).getDays();
							}
							else{
								
								totex=Days.daysBetween(new DateMidnight(empObj.getDateHired()), new DateMidnight(dt)).getDays();
							}
							
							orgintotex=totex;
							totex= totex/365;
							
							System.out.println("Total exp "+totex);
							System.out.println("\n driver-->"+empObj.getFullName());
							if(days>0&&orgintotex>0){
								if(orgintotex>=days)
									expelg=true;
							}
							if(fromdays>0&&totex>0&&todays>0){
								
							if(ptod.getLeaveQualifier().equals("2")){
								if(totex>=fromdays&&totex<todays)
									expelg=true;
							}					
							else{
								if(totex>=fromdays&&totex<=todays)
									expelg=true;
							}
					    }
							if(fromdays>0&&todays<=0&&totex>0){
								if(totex>=fromdays)
									expelg=true;
								
							}
							if(fromdays<=0&&todays>0&&totex>0){
								if(ptod.getLeaveQualifier().equals("5")){
									if(totex<todays)
										expelg=true;
								}
								else{
									if(totex<=todays)
										expelg=true;
								}
							}
							if(expelg){				
								
								LeaveCurrentBalance balance = leavebalobj.get(0);									
								LeaveCurrentBalance newLeaveBalance = new LeaveCurrentBalance();
								
								newLeaveBalance.setEmpname(balance.getEmpname());
								newLeaveBalance.setCompany(balance.getCompany());
								newLeaveBalance.setEmpcategory(balance.getEmpcategory());				
								newLeaveBalance.setTerminal(balance.getTerminal());
								newLeaveBalance.setLeavetype(balance.getLeavetype());				
								
								
								
								if(ptod.getDayearned()!=null){		
									if(!empObj.getPayTerm().equals("2")){
										if(accheck){
											Double earnedday=0.0;
											if(balance.getDaysremain()!=null){	
												newLeaveBalance.setDaysaccrude(balance.getDaysremain());
												earnedday=balance.getDaysremain()+ptod.getDayearned();
											}else{
												earnedday=0+ptod.getDayearned();
												newLeaveBalance.setDaysaccrude(0.0);
											}
											newLeaveBalance.setDaysavailable(earnedday);
											newLeaveBalance.setDayssbalance(ptod.getDayearned());
											newLeaveBalance.setDaysused(0.00);
											newLeaveBalance.setDaysremain(earnedday);							
										
										}else{	
											newLeaveBalance.setDaysaccrude(0.0);
											Double earnedday=ptod.getDayearned();
											newLeaveBalance.setDaysavailable(earnedday);
											newLeaveBalance.setDayssbalance(earnedday);
											newLeaveBalance.setDaysused(0.00);
											newLeaveBalance.setDaysremain(earnedday);							
										}						
								}
								else{
									newLeaveBalance.setDaysaccrude(0.0);
									Double earnedday=0.0;
									newLeaveBalance.setDaysavailable(earnedday);
									newLeaveBalance.setDayssbalance(earnedday);
									newLeaveBalance.setDaysused(0.00);
									newLeaveBalance.setDaysremain(earnedday);
								}
							}
							
								//Hours
								if(ptod.getHoursearned()!=null){
									if(empObj.getPayTerm().equals("2")){
										if(accheck){
											Double earnehour=0.0;
											if(balance.getHourremain()!=null){
												newLeaveBalance.setHoursaccrude(balance.getHourremain());
												earnehour=balance.getHourremain()+ptod.getHoursearned();
											}else{
												newLeaveBalance.setHoursaccrude(0.0);
												earnehour=0+ptod.getHoursearned();						
											}
											newLeaveBalance.setHoursavailable(earnehour);
											newLeaveBalance.setHoursbalance(earnehour);								
											newLeaveBalance.setHoursused(0.00);
											newLeaveBalance.setHourremain(earnehour);							
										}else{	
											newLeaveBalance.setHoursaccrude(0.0);
											Double earnehour=balance.getHourremain();
											newLeaveBalance.setHoursavailable(earnehour);
											newLeaveBalance.setHoursbalance(earnehour);
											newLeaveBalance.setHoursused(0.00);
											newLeaveBalance.setHourremain(earnehour);										
										}				
									}
									else{
										newLeaveBalance.setHoursaccrude(0.0);
										Double earnehour=0.0;
										newLeaveBalance.setHoursavailable(earnehour);
										newLeaveBalance.setHoursbalance(earnehour);
										newLeaveBalance.setHoursused(0.00);
										newLeaveBalance.setHourremain(earnehour);
									}
							}
								
								Calendar now = Calendar.getInstance();
								int currentYear = now.get(Calendar.YEAR);
							    int nextyear =  currentYear + 1;
							    
							    int month = now.get(Calendar.MONTH);
							    int day = now.get(Calendar.DAY_OF_MONTH);												
											
							    newLeaveBalance.setDateEffectiveFrom(now.getTime());
								
								now.set(Calendar.YEAR,nextyear);
								now.set(Calendar.MONTH, month);
								now.set(Calendar.DAY_OF_MONTH, day);
								
								newLeaveBalance.setDateEffectiveTo(now.getTime());						
								
								genericDAO.saveOrUpdate(newLeaveBalance);
								balance.setStatus(0);
								genericDAO.saveOrUpdate(balance);
								newLeaveBalance =null;									    
								
							}			
					}		
				}
			}
			else{	
				System.out.println("******* Entered 009");
				criterias.clear();
				criterias.put("company.id", empObj.getCompany().getId());
				criterias.put("terminal.id", empObj.getTerminal().getId());
				criterias.put("category.id", empObj.getCatagory().getId());
				criterias.put("leavetype.id", 4l);
				//criterias.put("leaveQualifier","2");
				criterias.put("status",1);
				List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,criterias);
				if(ptods.size()>0 && ptods!=null){						
					for(Ptod ptod:ptods){						
						StringBuffer buffer=new StringBuffer("");
						String acc="No";
						if(ptod.getAnnualoraccrual()==1)
							acc="Yes";
					
						boolean accheck=false;
						if(ptod.getAnnualoraccrual()==1)
							accheck=true;
						
						int fromdays=0;
						int todays=0;
						int days=0;
						if(ptod.getExperienceinyears()!=null)
							fromdays=ptod.getExperienceinyears();
						if(ptod.getExperienceinyearsTo()!=null)
							todays=ptod.getExperienceinyearsTo();
						if(ptod.getExperienceindays()!=null)
							days=ptod.getExperienceindays();
						int count=0;
						
					    DateTime dt = new DateTime();
									    	
					    	
							boolean expelg=false;
							int totex=0;				
							int orgintotex=0;
							if(empObj.getDateReHired()!=null && empObj.getDateHired()!=null){
								
								totex=Days.daysBetween(new DateMidnight(empObj.getDateHired()), new DateMidnight(dt)).getDays();
							}
							else if (empObj.getDateReHired()!=null){
								totex=Days.daysBetween(new DateMidnight(empObj.getDateReHired()), new DateMidnight(dt)).getDays();
							}
							else{
								
								totex=Days.daysBetween(new DateMidnight(empObj.getDateHired()), new DateMidnight(dt)).getDays();
							}
							orgintotex=totex;
							totex = totex/365;
							
							
							System.out.println("\n driver-->"+empObj.getFullName());
							if(days>0&&orgintotex>0){
								if(orgintotex>=days)
									expelg=true;
							}
							if(fromdays>0&&totex>0&&todays>0){
								
							if(ptod.getLeaveQualifier().equals("2")){
								if(totex>=fromdays&&totex<todays)
									expelg=true;
							}					
							else{
								if(totex>=fromdays&&totex<=todays)
									expelg=true;
							}
					    }
							if(fromdays>0&&todays<=0&&totex>0){
								if(totex>=fromdays)
									expelg=true;
								
							}
							if(fromdays<=0&&todays>0&&totex>0){
								if(ptod.getLeaveQualifier().equals("5")){
									if(totex<todays)
										expelg=true;
								}
								else{
									if(totex<=todays)
										expelg=true;
								}
							}
							if(expelg){								
							LeaveCurrentBalance leavebalanceObj = new LeaveCurrentBalance();
							leavebalanceObj.setEmpname(empObj);
							leavebalanceObj.setCompany(empObj.getCompany());
							leavebalanceObj.setEmpcategory(empObj.getCatagory());				
							leavebalanceObj.setTerminal(empObj.getTerminal());
							leavebalanceObj.setLeavetype(ptod.getLeavetype());
							if(!empObj.getPayTerm().equals("2")){
								leavebalanceObj.setDaysaccrude(0.0);
								leavebalanceObj.setDayssbalance(ptod.getDayearned());
								leavebalanceObj.setDaysavailable(ptod.getDayearned());
								leavebalanceObj.setDaysused(0.0);
								leavebalanceObj.setDaysremain(ptod.getDayearned());				
							}
							else{
								leavebalanceObj.setDaysaccrude(0.0);
								leavebalanceObj.setDayssbalance(0.0);
								leavebalanceObj.setDaysavailable(0.0);
								leavebalanceObj.setDaysused(0.0);
								leavebalanceObj.setDaysremain(0.0);
							}
							if(empObj.getPayTerm().equals("2")){
								leavebalanceObj.setHoursaccrude(0.0);
								leavebalanceObj.setHoursbalance(ptod.getHoursearned());
								leavebalanceObj.setHoursavailable(ptod.getHoursearned());
								leavebalanceObj.setHoursused(0.0);
								leavebalanceObj.setHourremain(ptod.getHoursearned());
							}
							else{
								leavebalanceObj.setHoursaccrude(0.0);
								leavebalanceObj.setHoursbalance(0.0);
								leavebalanceObj.setHoursavailable(0.0);
								leavebalanceObj.setHoursused(0.0);
								leavebalanceObj.setHourremain(0.0);
							}
							Calendar now = Calendar.getInstance();
							int currentYear = now.get(Calendar.YEAR);
						    int nextyear =  currentYear + 1;
						    
						    int month = now.get(Calendar.MONTH);
						    int day = now.get(Calendar.DAY_OF_MONTH);												
										
							leavebalanceObj.setDateEffectiveFrom(now.getTime());
							
							now.set(Calendar.YEAR,nextyear);
							now.set(Calendar.MONTH, month);
							now.set(Calendar.DAY_OF_MONTH, day);
							
							leavebalanceObj.setDateEffectiveTo(now.getTime());
							
							genericDAO.saveOrUpdate(leavebalanceObj);
							leavebalanceObj =null;
						}				    
					    
					}					
				}	
			}	
			Calendar now = Calendar.getInstance();
			int currentYear = now.get(Calendar.YEAR);
		    int nextyear =  currentYear + 1;
		    int month = now.get(Calendar.MONTH);
		    int day = now.get(Calendar.DAY_OF_MONTH);		
			
			now.set(Calendar.YEAR,nextyear);
			now.set(Calendar.MONTH, month);
			now.set(Calendar.DAY_OF_MONTH, day);
			
			// Update vacation on anniversary fix - 1st Feb 2017
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			
			empObj.setNextAnniversaryDate(now.getTime());
			
			genericDAO.saveOrUpdate(empObj);
		}
	}
		
		long endTime = System.currentTimeMillis();
		formatDateNow = logDateFormat.format(new Date(endTime));
		System.out.println(formatDateNow + " -> Finished employee anniversary vacation leave balance job. Time taken: "
				+ (endTime-startTime)/1000);
	
}
	

}
