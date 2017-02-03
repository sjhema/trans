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

import javax.servlet.http.HttpServletRequest;
import javax.swing.border.EmptyBorder;
import javax.validation.ValidationException;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IntegerField;
import org.apache.poi.util.StringUtil;
import org.hibernate.Session;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
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


import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.Eligibility;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HourlyRate;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.PtodRate;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hr.WeeklySalary;
import com.primovision.lutransport.model.SearchCriteria;
/*import com.primovision.lutransport.model.State;*/
import com.primovision.lutransport.model.StaticData;
/*import com.primovision.lutransport.model.Vehicle;*/
import com.primovision.lutransport.service.DateUpdateService;

/**
 * @author Subodh
 */

@Controller
@RequestMapping("/hr/ptodapplication")
public class PtodapplicationController extends CRUDController<Ptodapplication> {

	public PtodapplicationController() {
		setUrlContext("/hr/ptodapplication");
	}

	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
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
		if(request.getParameter("id")!=null){
			//criterias.put("status", 1);
			model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		}
		else{
			criterias.put("status", 1);
			model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		}
		
		//model.addAttribute("LeaveTypes", listStaticData("LEAVE_TYPE"));
		//model.addAttribute("ptodstatus", listStaticData("POTD_STATUS"));
		model.addAttribute("approvestatuss", listStaticData("APPROVE_STATUS"));
		//model.addAttribute("annualaccrual", listStaticData("Annual_ Accrual"));
		/*map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");*/
		/*criterias.clear();
		Map<String,Object> map=new HashMap<String,Object>();Annual_ Accrual
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("ptodstatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
		 */
		criterias.clear();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		model.addAttribute("leavetypes", genericDAO.findByCriteria(LeaveType.class, criterias,"name",false));
	}

	/*@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		//setupCreate(model, request);
		//System.out.println("\nlist1\n");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// System.out.println("\nlist2\n");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"company.name asc ,terminal.name asc,driver.fullName asc,hireddate asc,rehireddate asc,leavetype asc,leavedatefrom desc,leavedateto desc,createdAt",true,null));
		//System.out.println("\nlist3\n");
		return urlContext + "/list";
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("batchdate"))) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			try {
				Date billBatch = dateFormat.parse((String)criteria.getSearchMap().get("batchdate"));
				criteria.getSearchMap().put("batchdate",ReportDateUtil.oracleFormatter.format(billBatch));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"company.name asc ,terminal.name asc,driver.fullName asc,hireddate asc,rehireddate asc,leavetype asc,leavedatefrom desc,leavedateto desc,createdAt",true,null));
		return urlContext + "/list";
	}*/
	
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		//setupCreate(model, request);
		//System.out.println("\nlist1\n");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// System.out.println("\nlist2\n");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"batchdate desc,company.name asc ,terminal.name asc,driver.fullName asc,hireddate asc,rehireddate asc,leavetype asc,leavedatefrom desc,leavedateto desc,createdAt",true,null));
		//System.out.println("\nlist3\n");
		return urlContext + "/list";
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {	
		
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
	
		
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("batchdate"))) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			try {
				Date billBatch = dateFormat.parse((String)criteria.getSearchMap().get("batchdate"));
				criteria.getSearchMap().put("batchdate",ReportDateUtil.oracleFormatter.format(billBatch));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//criteria.getSearchMap().remove("batchdate");
		
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"batchdate desc,company.name asc ,terminal.name asc,driver.fullName asc,hireddate asc,rehireddate asc,leavetype asc,leavedatefrom desc,leavedateto desc,createdAt",true,null));
		return urlContext + "/list";
	}
	
	
	
	

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		//setupCreate(model, request);
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
		//model.addAttribute("LeaveTypes", listStaticData("LEAVE_TYPE"));
		//model.addAttribute("ptodstatus", listStaticData("POTD_STATUS"));
		model.addAttribute("approvestatuss", listStaticData("APPROVE_STATUS"));
		//model.addAttribute("annualaccrual", listStaticData("Annual_ Accrual"));
		/*map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");*/
		/*criterias.clear();
		Map<String,Object> map=new HashMap<String,Object>();Annual_ Accrual
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("ptodstatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
		 */
		criterias.clear();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		model.addAttribute("leavetypes", genericDAO.findByCriteria(LeaveType.class, criterias,"name",false));

	}

	@RequestMapping(method = RequestMethod.GET, value = "/history.do")
	public String  history(ModelMap model,HttpServletRequest request,Ptodapplication entity){
		Ptodapplication newentiry=(Ptodapplication)request.getSession().getAttribute("modelobj");
		if(newentiry!=null){
			Map criterias=new HashMap();
			
			criterias.put("empname.id", newentiry.getDriver().getId());
			criterias.put("empcategory.id", newentiry.getCategory().getId());
			criterias.put("company.id", newentiry.getCompany().getId());
			criterias.put("terminal.id", newentiry.getTerminal().getId());
			criterias.put("leavetype.id", newentiry.getLeavetype().getId());
			criterias.put("status",1);
			List<LeaveCurrentBalance> balances=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
			criterias.clear();
			criterias.put("company.id", newentiry.getCompany().getId());
			criterias.put("terminal.id", newentiry.getTerminal().getId());
			criterias.put("category.id", newentiry.getCategory().getId());
			criterias.put("driver.id", newentiry.getDriver().getId());
			criterias.put("leavetype.id",newentiry.getLeavetype().getId());
			criterias.put("status",1);
			List<Ptodapplication> ptodapplications=genericDAO.findByCriteria(Ptodapplication.class, criterias,"leavedatefrom",true);
			model.addAttribute("list1", ptodapplications);
			model.addAttribute("list", balances);
		}
		return urlContext+"/ptodhistory";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/redirect.do")
	public String redirect(ModelMap model,HttpServletRequest request){
		Ptodapplication newentiry=(Ptodapplication)request.getSession().getAttribute("modelobj");
		setupCreate(model, request);
		model.addAttribute("modelObject",newentiry);
		return urlContext+"/form";
	}
	/*@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") Ptodapplication entity,
			BindingResult bindingResult, ModelMap model) 
	{	

		
		if(entity.getSequenceAmt1()==null){
			entity.setSequenceAmt1(0.0);
		}
			
		if(entity.getSequenceAmt2()==null){
			entity.setSequenceAmt2(0.0);
		}
		
		if(entity.getSequenceAmt3()==null){
			entity.setSequenceAmt3(0.0);
		}
		
		if(entity.getSequenceAmt4()==null){
			entity.setSequenceAmt4(0.0);
		}
		
		if(entity.getSequenceNum1()==null){
			entity.setSequenceNum1(0);
		}
		
		if(entity.getSequenceNum2()==null){
			entity.setSequenceNum2(0);
		}
		
		if(entity.getSequenceNum3()==null){
			entity.setSequenceNum3(0);
		}
		
		if(entity.getSequenceNum4()==null){
			entity.setSequenceNum4(0);
		}
		
		
		Integer tempdayspaid=entity.getTempdayspaid();
		Double temphourspaid=entity.getTemphourspaid();	
		boolean preventity=false;

		Integer temppaidoutdays=entity.getTemppaidoutdays();
		Double temppaidouthours=entity.getTemppaidouthours();	

		String bh=(String)request.getParameter("bh");
		if(StringUtils.equalsIgnoreCase("true", bh)){
			request.getSession().setAttribute("modelobj", entity);
			return "redirect:"+urlContext+"/history.do";
		}
		request.getSession().removeAttribute("modelobj");

		if(entity.getPaidoutdays()==null)
			entity.setPaidoutdays(0);
		if(entity.getPaidouthours()==null)
			entity.setPaidouthours(0.0);
		if(entity.getDayspaid()==null)
			entity.setDayspaid(0);
		if(entity.getHourspaid()==null)
			entity.setHourspaid(0.0);
		if(entity.getHourlyamountpaid()==null)
			entity.setHourlyamountpaid(0.0);
		
		
		
		if(entity.getNextAnniversaryDate()==null)
			entity.setNextAllotmentDays(null);
		
		if(entity.getNextAllotmenthours()==null){
			entity.setNextAllotmenthours(0.0);
		}
		
		if(entity.getNextAllotmentDays()==null){
			entity.setNextAllotmentDays(0);
		}
		

		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}

		if(entity.getCategory()==null){
			bindingResult.rejectValue("category", "error.select.option", null, null);
		}
		if(entity.getDriver()==null){
			System.out.println("\nentity.driver is null==>"+entity.getDriver()+"\n");
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}	
		if(entity.getHireddate()==null && entity.getRehireddate()==null){
			bindingResult.rejectValue("hireddate", null, null, "Either Hired Date or Re-Hired Date is Required");
		}
		if(entity.getLeavetype()==null){
			bindingResult.rejectValue("leavetype", "error.select.option", null, null);
		}
		else
		{
			if(entity.getLeavetype().getId()==5 || entity.getLeavetype().getId()==6 || entity.getLeavetype().getId()==7 || entity.getLeavetype().getId()==8){
				
				if (bindingResult.hasErrors()) {					
					setupCreate(model, request);
					bindingResult.getFieldError();					
					return urlContext + "/form";
				}
				if(entity.getApprovestatus()==null){
					entity.setApprovestatus(0);
				}
				if(entity.getAmountpaid()==null){
					entity.setAmountpaid(0.0);
				}
				if(entity.getHourlyamountpaid()==null){
					entity.setHourlyamountpaid(0.0);
				}
				entity.setBatchdate(null);
				entity.setCheckdate(null);
				entity.setDateapproved(null);
				entity.setDayspaid(0);
				entity.setDaysrequested(0);
				entity.setDaysunpaid(0);
				entity.setEarneddays(0);
				entity.setEarnedhours(0.0);
				entity.setHourspaid(0.0);
				entity.setHourlyamountpaid(0.0);
				entity.setHoursrequested(0.0);
				entity.setHoursunpaid(0.0);
				entity.setPaidoutdays(0);
				entity.setPaidouthours(0.0);
				entity.setPtodhourlyrate(0.0);
				entity.setPtodrates(0.0);
				entity.setRemainingdays(0);
				entity.setRemaininghours(0.0);
				entity.setUseddays(0);
				entity.setUsedhours(0.0);
				entity.setSubmitdate(null);				
				
				beforeSave(request, entity, model);				
				genericDAO.saveOrUpdate(entity);
				cleanUp(request);

				setupCreate(model, request);
				request.getSession().setAttribute("msg","added successfully");
				return "redirect:create.do";				
				
			}			
		}
		
		
		if(entity.getApproveby()==null){
			bindingResult.rejectValue("approveby", "error.select.option", null, null);
		}

		if (entity.getLeavedatefrom()!=null && entity.getLeavedateto()!=null) {
			if (entity.getLeavedateto().before(entity.getLeavedatefrom())) {
				bindingResult.rejectValue("leavedateto", "error.textbox.leaveDateto",null, null);
			}
		}

		if (entity.getPaidoutdays()!=null){
			if(entity.getPaidoutdays()!=0 && entity.getPaidoutdays()>0){
				if(entity.getLeavedatefrom()==null){
				bindingResult.rejectValue("leavedatefrom", "error.textbox.ptod",null, null);
			}
			if(entity.getLeavedateto()==null){
				bindingResult.rejectValue("leavedateto", "error.textbox.ptod",null, null);
			}
				if(entity.getDaysrequested()==null){
					bindingResult.rejectValue("daysrequested", "error.textbox.ptod",null, null);
				}
			}
		}
		Ptodapplication prentity=null;
		if(entity.getId()!=null){
			preventity=true;
			prentity=genericDAO.getById(Ptodapplication.class,entity.getId());
			preventity=true;
			Session session = ((Session) genericDAO.getEntityManager().getDelegate())
			.getSessionFactory().openSession();
			prentity = (Ptodapplication) session.get(entity.getClass(), entity.getId());
		}
		
		//for checking Eligibility
		if(entity.getSubmitdate()!=null && entity.getLeavedatefrom()!=null && entity.getLeavedateto()!=null&&entity.getApprovestatus()!=null)
		{
			System.out.println("\nchecking Eligibility\n");
			Eligibility eligibility=null;
			if(entity.getApprovestatus()==1){
				Date leaveStartdate=entity.getLeavedatefrom();
				Date submitdate=entity.getSubmitdate();


				String eligibilityQuery = "select obj from Eligibility obj where obj.company="+entity.getCompany().getId()+" and obj.terminal="+entity.getTerminal().getId()+"  and obj.catagory="+entity.getCategory().getId()+" and obj.leaveType='"+entity.getLeavetype().getId()+"'";
				List<Eligibility> obs = genericDAO.executeSimpleQuery(eligibilityQuery);
				if(obs!=null && obs.size()>0){
					request.getSession().setAttribute("error","Eligibility  criteria not found");
				 setupUpdate(model, request);
				 return urlContext + "/form";
			}
					for(Eligibility obj:obs){
						eligibility=obj;
					}

					//for Leave type VACATION
					if((eligibility.getLeaveType().getName()).equalsIgnoreCase("VACATION"))
					{
						System.out.println("Leave type is VACATION===>"+eligibility.getLeaveType().getName()+"\n");
						if(eligibility.getPriorNoticeWeeks()!=null){
							System.out.println("\nPriorNoticeWeeks()!=null\n");
							int days = (int) ((leaveStartdate.getTime()-submitdate.getTime()) / (1000 * 60 * 60 * 24));
							int week=days/7;
							if(week<eligibility.getPriorNoticeWeeks()){
								System.out.println("\nWeeks="+week+"\n");
								//request.getSession().setAttribute("error","Submit day should be befor "+eligibility.getPriorNoticeWeeks()+"  weeks from leave start day");
								request.getSession().setAttribute("error","Vacation need "+eligibility.getPriorNoticeWeeks()+"  weeks prior notice");
								setupUpdate(model, request);
								return urlContext + "/form";
							}

						}
						if(eligibility.getPriorNoticeDays()!=null){
							System.out.println("\nPriorNoticeDays()!=null\n");
							int days = (int) ((leaveStartdate.getTime()-submitdate.getTime()) / (1000 * 60 * 60 * 24));
							if(days<eligibility.getPriorNoticeDays()){
								System.out.println("\nWeeks="+days+"\n");
								request.getSession().setAttribute("error","Vacation need "+eligibility.getPriorNoticeDays()+"  days prior notice");
								setupUpdate(model, request);
								return urlContext + "/form";
							}
						}
						if(eligibility.getNoVactionDateFrom()!=null&&eligibility.getNoVactionDateTo()!=null){
							if((entity.getLeavedatefrom()).after(eligibility.getNoVactionDateFrom())&&(entity.getLeavedateto()).before(eligibility.getNoVactionDateTo())){
								request.getSession().setAttribute("error","Vacation is Not Allowed during "+sdf.format(eligibility.getNoVactionDateFrom())+"--"+ sdf.format(eligibility.getNoVactionDateTo()));
								setupUpdate(model, request);
								return urlContext + "/form";
							}
						}
					}

					//for Leave type VACATION
					
				}
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
			bindingResult.getFieldError();
			//System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			return urlContext + "/form";
		}

		//UPDATING LeaveBalance Table 
		Map criterias=new HashMap();
		criterias.put("company.id",entity.getCompany().getId());
		criterias.put("terminal.id", entity.getTerminal().getId());
		criterias.put("empcategory.id", entity.getCategory().getId());
		criterias.put("empname.id", entity.getDriver().getId());
		criterias.put("leavetype.id", entity.getLeavetype().getId());
		criterias.put("status",1);
		List<LeaveCurrentBalance> leaveBalobs=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
		if(leaveBalobs.isEmpty()){
			if(entity.getApprovestatus()!=null && entity.getApprovestatus()==1){
				setupCreate(model, request);
				request.getSession().setAttribute("error", "Cannot find suitable record in Leave Balance Table for selected employee.");
				return urlContext + "/form";
			}
			
		}
		
		if(entity.getDriver()!=null && entity.getCategory()!=null && entity.getLeavetype()!=null && entity.getEarneddays()!=null
				&& entity.getUseddays()!=null && entity.getRemainingdays()!=null )
		{
			
	
		if(entity.getApprovestatus()!=null && entity.getApprovestatus()==1 ){
			
				
				if(leaveBalobs!=null && leaveBalobs.size()>0){
					for(LeaveCurrentBalance obj:leaveBalobs){

					 =============================================
				         Code  For Days Paid
			           =============================================	  
						if(entity.getDayspaid()!=null || entity.getHourspaid()!=null){			   	   
							if(obj.getDaysused()!=null){
								if(entity.getId()!=null){
									if(entity.getDayspaid()!=null && prentity.getDayspaid()!=null && entity.getDayspaid()!=prentity.getDayspaid() && entity.getDayspaid()>prentity.getDayspaid()){
										Integer daysdiff=entity.getDayspaid()-prentity.getDayspaid();
										obj.setDaysused(obj.getDaysused()+daysdiff);
									}
									else if(entity.getDayspaid()!=null && prentity.getDayspaid()!=null && entity.getDayspaid()!=prentity.getDayspaid() && entity.getDayspaid()<prentity.getDayspaid()){
										Integer daysdiff=prentity.getDayspaid()-entity.getDayspaid();
										obj.setDaysused(obj.getDaysused()-daysdiff);
									}
									else if(entity.getDayspaid()==prentity.getDayspaid()){
										//do nothing
									}
								}
								else if(entity.getDayspaid()!=null){
									obj.setDaysused(obj.getDaysused()+entity.getDayspaid());
								}
							}
							else if(entity.getDayspaid()!=null){
								obj.setDaysused((double)entity.getDayspaid());
							}		
							if(obj.getDaysavailable()!=null && obj.getDaysused()!=null){
								obj.setDaysremain(obj.getDaysavailable()-obj.getDaysused());
							}

							if(obj.getHoursused()!=null){				
								if(entity.getId()!=null){
									if(entity.getHourspaid()!=null && prentity.getHourspaid()!=null && entity.getHourspaid()!=prentity.getHourspaid() && entity.getHourspaid()> prentity.getHourspaid()){
										Double hoursdiff=entity.getHourspaid()-prentity.getHourspaid();
										obj.setHoursused(obj.getHoursused()+hoursdiff);
									}
									else if(entity.getHourspaid()!=null && prentity.getHourspaid()!=null && entity.getHourspaid()!=prentity.getHourspaid() && entity.getHourspaid()<prentity.getHourspaid()){
										Double hoursdiff=prentity.getHourspaid()-entity.getHourspaid();
										obj.setHoursused(obj.getHoursused()-hoursdiff);
									}
									else if(entity.getHourspaid()==prentity.getHourspaid()){
										//do nothing
									}
								}
								else if(entity.getHourspaid()!=null){
									obj.setHoursused(obj.getHoursused()+entity.getHourspaid());
								}			   
							}
							else if(entity.getHourspaid()!=null){			    	
								obj.setHoursused((double)entity.getHourspaid());			    
							}			  
							if(obj.getHoursavailable()!=null && obj.getHoursused()!=null){
								obj.setHourremain(obj.getHoursavailable()-obj.getHoursused());
							}
						}
						
					// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
				 =============================================
			         Code  For Days Paid Ends
		           =============================================	  	


					 =============================================
		                Code  For Paid Out Days
		              =============================================	  				  
						if(entity.getPaidoutdays()!=null || entity.getPaidouthours()!=null){		   	   
							if(obj.getDaysused()!=null){
								if(entity.getId()!=null){
									if(entity.getPaidoutdays()!=null && prentity.getPaidoutdays()!=null && entity.getPaidoutdays()!=prentity.getPaidoutdays() && entity.getPaidoutdays()>prentity.getPaidoutdays()){
										Integer daysdiff=entity.getPaidoutdays()-prentity.getPaidoutdays();
										obj.setDaysused(obj.getDaysused()+daysdiff);
									}
									else if(entity.getPaidoutdays()!=null && prentity.getPaidoutdays()!=null && entity.getPaidoutdays()!=prentity.getPaidoutdays() && entity.getPaidoutdays()<prentity.getPaidoutdays()){
										Integer daysdiff=prentity.getPaidoutdays()-entity.getPaidoutdays();
										obj.setDaysused(obj.getDaysused()-daysdiff);
									}
									else if(entity.getPaidoutdays()==prentity.getPaidoutdays()){
										//do nothing
									}
								}
								else if(entity.getPaidoutdays()!=null){
									obj.setDaysused(obj.getDaysused()+entity.getPaidoutdays());
								}
							}
							else if(entity.getPaidoutdays()!=null){
								obj.setDaysused((double)entity.getPaidoutdays());
							}		
							if(obj.getDaysavailable()!=null && obj.getDaysused()!=null){
								obj.setDaysremain(obj.getDaysavailable()-obj.getDaysused());
							}

							if(obj.getHoursused()!=null){

								if(entity.getId()!=null){
									if(entity.getPaidouthours()!=null && prentity.getPaidouthours()!=null && entity.getPaidouthours()!=prentity.getPaidouthours() && entity.getPaidouthours()> prentity.getPaidouthours()){
										Double hoursdiff=entity.getPaidouthours()-prentity.getPaidouthours();
										obj.setHoursused(obj.getHoursused()+hoursdiff);
									}
									else if(entity.getPaidouthours()!=null && prentity.getPaidouthours()!=null && entity.getPaidouthours()!=prentity.getPaidouthours() && entity.getPaidouthours()<prentity.getPaidouthours()){
										Double hoursdiff=prentity.getPaidouthours()-entity.getPaidouthours();
										obj.setHoursused(obj.getHoursused()-hoursdiff);
									}
									else if(entity.getPaidouthours()==prentity.getPaidouthours()){
										//do nothing
									}
								}
								else if(entity.getPaidouthours()!=null){
									obj.setHoursused(obj.getHoursused()+entity.getPaidouthours());
								}			   
							}
							else if(entity.getPaidouthours()!=null){			    	
								obj.setHoursused((double)entity.getPaidouthours());			    
							}			  
							if(obj.getHoursavailable()!=null && obj.getHoursused()!=null){
								obj.setHourremain(obj.getHoursavailable()-obj.getHoursused());
							}		
						}
						 =============================================
	                       Code  For Paid Out Days Ends
	                      =============================================	  	 

						genericDAO.saveOrUpdate(obj);			   
					}
				}
			}
		
		else{			
			
			if(leaveBalobs!=null && leaveBalobs.size()>0){				
			for(LeaveCurrentBalance balance:leaveBalobs){
				if(preventity){
					if(balance.getDaysused()!=null && prentity.getDayspaid()!=null){ 
						balance.setDaysused(balance.getDaysused()-prentity.getDayspaid());
						}
					
						if(balance.getDaysused()!=null && prentity.getPaidoutdays()!=null){ 
						balance.setDaysused(balance.getDaysused()-prentity.getPaidoutdays());
						}
					
						if(balance.getDaysavailable()!=null && balance.getDaysused()!=null){ 
						balance.setDaysremain(balance.getDaysavailable()-balance.getDaysused());
						}


						if(balance.getHoursused()!=null && prentity.getHourspaid()!=null){
						balance.setHoursused(balance.getHoursused()-prentity.getHourspaid());
						}
					
						if(balance.getHoursused()!=null && prentity.getPaidouthours()!=null){
						balance.setHoursused(balance.getHoursused()-prentity.getPaidouthours());
						}

						if(balance.getHoursavailable()!=null && balance.getHoursused()!=null){
						balance.setHourremain(balance.getHoursavailable()-balance.getHoursused());
						}
					// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
						genericDAO.saveOrUpdate(balance);
				}
				else{
					if(balance.getDaysused()!=null && entity.getDayspaid()!=null){ 
						balance.setDaysused(balance.getDaysused()-entity.getDayspaid());
						}
					
						if(balance.getDaysused()!=null && entity.getPaidoutdays()!=null){ 
						balance.setDaysused(balance.getDaysused()-entity.getPaidoutdays());
						}
					
						if(balance.getDaysavailable()!=null && balance.getDaysused()!=null){ 
						balance.setDaysremain(balance.getDaysavailable()-balance.getDaysused());
						}


						if(balance.getHoursused()!=null && entity.getHourspaid()!=null){
						balance.setHoursused(balance.getHoursused()-entity.getHourspaid());
						}
					
						if(balance.getHoursused()!=null && entity.getPaidouthours()!=null){
						balance.setHoursused(balance.getHoursused()-entity.getPaidouthours());
						}

						if(balance.getHoursavailable()!=null && balance.getHoursused()!=null){
						balance.setHourremain(balance.getHoursavailable()-balance.getHoursused());
						}
					// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
						genericDAO.saveOrUpdate(balance);
				}			 
			}
		}
			entity.setAmountpaid(0.00);
			entity.setHourlyamountpaid(0.00);
			entity.setPaidoutdays(0);
			entity.setDayspaid(0);
			entity.setHourspaid(0.0);
			entity.setPaidouthours(0.0);
		
		}
		}
		beforeSave(request, entity, model);
		// merge into datasource
		System.out.println("\nCRUD1\n");
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);

		setupCreate(model, request);
		request.getSession().setAttribute("msg","added successfully");
		return "redirect:create.do";		
	}*/

	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") Ptodapplication entity,
			BindingResult bindingResult, ModelMap model) 
	{	
		
		if(entity.getSequenceAmt1()==null){
			entity.setSequenceAmt1(0.0);
		}
			
		if(entity.getSequenceAmt2()==null){
			entity.setSequenceAmt2(0.0);
		}
		
		if(entity.getSequenceAmt3()==null){
			entity.setSequenceAmt3(0.0);
		}
		
		if(entity.getSequenceAmt4()==null){
			entity.setSequenceAmt4(0.0);
		}
		
		if(entity.getSequenceNum1()==null){
			entity.setSequenceNum1(0);
		}
		
		if(entity.getSequenceNum2()==null){
			entity.setSequenceNum2(0);
		}
		
		if(entity.getSequenceNum3()==null){
			entity.setSequenceNum3(0);
		}
		
		if(entity.getSequenceNum4()==null){
			entity.setSequenceNum4(0);
		}
		
		if(entity.getSequenceAmt5()==null){
			entity.setSequenceAmt5(0.0);
		}
		if(entity.getSequenceAmt6()==null){
			entity.setSequenceAmt6(0.0);
		}
		if(entity.getSequenceAmt7()==null){
			entity.setSequenceAmt7(0.0);
		}
		if(entity.getSequenceAmt8()==null){
			entity.setSequenceAmt8(0.0);
		}
		if(entity.getSequenceNum5()==null){
			entity.setSequenceNum5(0);
		}
		if(entity.getSequenceNum6()==null){
			entity.setSequenceNum6(0);
		}
		if(entity.getSequenceNum7()==null){
			entity.setSequenceNum7(0);
		}
		if(entity.getSequenceNum8()==null){
			entity.setSequenceNum8(0);
		}
		
		Integer tempdayspaid=entity.getTempdayspaid();
		Double temphourspaid=entity.getTemphourspaid();	
		boolean preventity=false;

		Integer temppaidoutdays=entity.getTemppaidoutdays();
		Double temppaidouthours=entity.getTemppaidouthours();	

		String bh=(String)request.getParameter("bh");
		if(StringUtils.equalsIgnoreCase("true", bh)){
			request.getSession().setAttribute("modelobj", entity);
			return "redirect:"+urlContext+"/history.do";
		}
		request.getSession().removeAttribute("modelobj");

		if(entity.getPaidoutdays()==null)
			entity.setPaidoutdays(0);
		if(entity.getPaidouthours()==null)
			entity.setPaidouthours(0.0);
		if(entity.getDayspaid()==null)
			entity.setDayspaid(0);
		if(entity.getHourspaid()==null)
			entity.setHourspaid(0.0);
		if(entity.getHourlyamountpaid()==null)
			entity.setHourlyamountpaid(0.0);
		
		if(entity.getNextAnniversaryDate()==null)
			entity.setNextAnniversaryDate(null);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>"+entity.getNextAnniversaryDate());
		if(entity.getNextAnniversaryDate()==null)
			entity.setNextAllotmentDays(null);
		
		if(entity.getNextAllotmenthours()==null){
			entity.setNextAllotmenthours(0.0);
		}
		
		if(entity.getNextAllotmentDays()==null){
			entity.setNextAllotmentDays(0);
		}
		

		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}

		if(entity.getCategory()==null){
			bindingResult.rejectValue("category", "error.select.option", null, null);
		}
		if(entity.getDriver()==null){
			System.out.println("\nentity.driver is null==>"+entity.getDriver()+"\n");
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}	
		if(entity.getHireddate()==null && entity.getRehireddate()==null){
			bindingResult.rejectValue("hireddate", null, null, "Either Hired Date or Re-Hired Date is Required");
		}
		if(entity.getLeavetype()==null){
			bindingResult.rejectValue("leavetype", "error.select.option", null, null);
		}
		else
		{
			// Jury duty fix - 3rd Nov 2016 (leave type 9)
			if(entity.getLeavetype().getId()==9 || entity.getLeavetype().getId()==5 || entity.getLeavetype().getId()==6 || entity.getLeavetype().getId()==7 || entity.getLeavetype().getId()==8){
				
				if(entity.getSubmitdate()==null){
					bindingResult.rejectValue("submitdate", null, null, "Submit Date required");
				}
				
				if(entity.getBatchdate()==null){
					bindingResult.rejectValue("batchdate", null, null, "Batch Date is required");
				}
				
				if(entity.getDateapproved()==null){
					bindingResult.rejectValue("dateapproved", null, null, "Approved date required");
				}
				
				if(entity.getApprovestatus()==null){
					bindingResult.rejectValue("approvestatus", "error.select.option", null, null);
				}
				
				if(entity.getApproveby()==null){
					bindingResult.rejectValue("approveby", "error.select.option", null, null);
				}
				
				if (bindingResult.hasErrors()) {					
					setupCreate(model, request);
					bindingResult.getFieldError();					
					return urlContext + "/form";
				}
				if(entity.getApprovestatus()==null){
					entity.setApprovestatus(0);
				}
				if(entity.getAmountpaid()==null){
					entity.setAmountpaid(0.0);
				}
				if(entity.getHourlyamountpaid()==null){
					entity.setHourlyamountpaid(0.0);
				}
				
				// Jury duty fix - 3rd Nov 2016 (leave type 9)
				if(entity.getDaysrequested()==null){
					entity.setDaysrequested(0);
				}
				if(entity.getHoursrequested()==null){
					entity.setHoursrequested(0.0);
				}
				
				//entity.setBatchdate(null);
				//entity.setCheckdate(null);
				
				entity.setDateapproved(entity.getDateapproved());
				
				// Jury duty fix - 3rd Nov 2016 (leave type 9)
				if(entity.getLeavetype().getId()!=9) {
					entity.setDaysrequested(0);
					entity.setDayspaid(0);
					entity.setPtodrates(0.0);
					
					entity.setHoursrequested(0.0);
					entity.setHourspaid(0.0);
					entity.setPtodhourlyrate(0.0);
					entity.setHourlyamountpaid(0.0);
				}
				
				entity.setDaysunpaid(0);
				entity.setEarneddays(0);
				entity.setEarnedhours(0.0);
				
				entity.setHoursunpaid(0.0);
				entity.setPaidoutdays(0);
				entity.setPaidouthours(0.0);
				
				entity.setRemainingdays(0);
				entity.setRemaininghours(0.0);
				entity.setUseddays(0);
				entity.setUsedhours(0.0);
				//entity.setSubmitdate(null);				
				
				beforeSave(request, entity, model);				
				genericDAO.saveOrUpdate(entity);
				cleanUp(request);

				setupCreate(model, request);
				request.getSession().setAttribute("msg","added successfully");
				return "redirect:create.do";				
				
			}			
		}
		
		
		if(entity.getApproveby()==null){
			bindingResult.rejectValue("approveby", "error.select.option", null, null);
		}

		if (entity.getLeavedatefrom()!=null && entity.getLeavedateto()!=null) {
			if (entity.getLeavedateto().before(entity.getLeavedatefrom())) {
				bindingResult.rejectValue("leavedateto", "error.textbox.leaveDateto",null, null);
			}
		}

		if (entity.getPaidoutdays()!=null){
			if(entity.getPaidoutdays()!=0 && entity.getPaidoutdays()>0){
				/*if(entity.getLeavedatefrom()==null){
				bindingResult.rejectValue("leavedatefrom", "error.textbox.ptod",null, null);
			}
			if(entity.getLeavedateto()==null){
				bindingResult.rejectValue("leavedateto", "error.textbox.ptod",null, null);
			}*/
				if(entity.getDaysrequested()==null){
					bindingResult.rejectValue("daysrequested", "error.textbox.ptod",null, null);
				}
			}
		}
		Ptodapplication prentity=null;
		if(entity.getId()!=null){
			/*preventity=true;
			prentity=genericDAO.getById(Ptodapplication.class,entity.getId());*/
			preventity=true;
			Session session = ((Session) genericDAO.getEntityManager().getDelegate())
			.getSessionFactory().openSession();
			prentity = (Ptodapplication) session.get(entity.getClass(), entity.getId());
		}
		
		//for checking Eligibility
		if(entity.getSubmitdate()!=null && entity.getLeavedatefrom()!=null && entity.getLeavedateto()!=null&&entity.getApprovestatus()!=null)
		{
			System.out.println("\nchecking Eligibility\n");
			Eligibility eligibility=null;
			if(entity.getApprovestatus()==1){
				Date leaveStartdate=entity.getLeavedatefrom();
				Date submitdate=entity.getSubmitdate();


				String eligibilityQuery = "select obj from Eligibility obj where obj.company="+entity.getCompany().getId()+" and obj.terminal="+entity.getTerminal().getId()+"  and obj.catagory="+entity.getCategory().getId()+" and obj.leaveType='"+entity.getLeavetype().getId()+"'";
				List<Eligibility> obs = genericDAO.executeSimpleQuery(eligibilityQuery);
				if(obs!=null && obs.size()>0){
					/*request.getSession().setAttribute("error","Eligibility  criteria not found");
				 setupUpdate(model, request);
				 return urlContext + "/form";
			}*/
					for(Eligibility obj:obs){
						eligibility=obj;
					}

					//for Leave type VACATION
					if((eligibility.getLeaveType().getName()).equalsIgnoreCase("VACATION"))
					{
						System.out.println("Leave type is VACATION===>"+eligibility.getLeaveType().getName()+"\n");
						if(eligibility.getPriorNoticeWeeks()!=null){
							System.out.println("\nPriorNoticeWeeks()!=null\n");
							int days = (int) ((leaveStartdate.getTime()-submitdate.getTime()) / (1000 * 60 * 60 * 24));
							int week=days/7;
							if(week<eligibility.getPriorNoticeWeeks()){
								/*System.out.println("\nWeeks="+week+"\n");*/
								//request.getSession().setAttribute("error","Submit day should be befor "+eligibility.getPriorNoticeWeeks()+"  weeks from leave start day");
								request.getSession().setAttribute("error","Vacation need "+eligibility.getPriorNoticeWeeks()+"  weeks prior notice");
								setupUpdate(model, request);
								return urlContext + "/form";
							}

						}
						if(eligibility.getPriorNoticeDays()!=null){
							System.out.println("\nPriorNoticeDays()!=null\n");
							int days = (int) ((leaveStartdate.getTime()-submitdate.getTime()) / (1000 * 60 * 60 * 24));
							if(days<eligibility.getPriorNoticeDays()){
								/*System.out.println("\nWeeks="+days+"\n");*/
								request.getSession().setAttribute("error","Vacation need "+eligibility.getPriorNoticeDays()+"  days prior notice");
								setupUpdate(model, request);
								return urlContext + "/form";
							}
						}
						if(eligibility.getNoVactionDateFrom()!=null&&eligibility.getNoVactionDateTo()!=null){
							if((entity.getLeavedatefrom()).after(eligibility.getNoVactionDateFrom())&&(entity.getLeavedateto()).before(eligibility.getNoVactionDateTo())){
								request.getSession().setAttribute("error","Vacation is Not Allowed during "+sdf.format(eligibility.getNoVactionDateFrom())+"--"+ sdf.format(eligibility.getNoVactionDateTo()));
								setupUpdate(model, request);
								return urlContext + "/form";
							}
						}
					}

					//for Leave type VACATION
					
				}
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
			bindingResult.getFieldError();
			//System.out.println("\nsave--bindingResult.hasErrors()=="+bindingResult.getFieldError()+"\n");
			
			System.out.println("ERROROOROROR");
			return urlContext + "/form";
		}

		//UPDATING LeaveBalance Table 
		Map criterias=new HashMap();
		criterias.put("company.id",entity.getCompany().getId());
		criterias.put("terminal.id", entity.getTerminal().getId());
		criterias.put("empcategory.id", entity.getCategory().getId());
		criterias.put("empname.id", entity.getDriver().getId());
		criterias.put("leavetype.id", entity.getLeavetype().getId());
		criterias.put("status",1);
		List<LeaveCurrentBalance> leaveBalobs=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
		if(leaveBalobs.isEmpty()){
			if(entity.getApprovestatus()!=null && entity.getApprovestatus()==1){
				// Jury duty fix - 3rd Nov 2016
				if (entity.getLeavetype().getId() != 9) {
					setupCreate(model, request);
					request.getSession().setAttribute("error", "Cannot find suitable record in Leave Balance Table for selected employee.");
					return urlContext + "/form";
				}
			}
			
		}
		
		if(entity.getDriver()!=null && entity.getCategory()!=null && entity.getLeavetype()!=null && entity.getEarneddays()!=null
				&& entity.getUseddays()!=null && entity.getRemainingdays()!=null )
		{
			
	
		if(entity.getApprovestatus()!=null && entity.getApprovestatus()==1 ){
			
				
				if(leaveBalobs!=null && leaveBalobs.size()>0){
					for(LeaveCurrentBalance obj:leaveBalobs){

					/* =============================================
				         Code  For Days Paid
			           =============================================	  */
						if(entity.getDayspaid()!=null || entity.getHourspaid()!=null){			   	   
							if(obj.getDaysused()!=null){
								if(entity.getId()!=null){
									if(entity.getDayspaid()!=null && prentity.getDayspaid()!=null && entity.getDayspaid()!=prentity.getDayspaid() && entity.getDayspaid()>prentity.getDayspaid()){
										Integer daysdiff=entity.getDayspaid()-prentity.getDayspaid();
										obj.setDaysused(obj.getDaysused()+daysdiff);
									}
									else if(entity.getDayspaid()!=null && prentity.getDayspaid()!=null && entity.getDayspaid()!=prentity.getDayspaid() && entity.getDayspaid()<prentity.getDayspaid()){
										Integer daysdiff=prentity.getDayspaid()-entity.getDayspaid();
										obj.setDaysused(obj.getDaysused()-daysdiff);
									}
									else if(entity.getDayspaid()==prentity.getDayspaid()){
										//do nothing
									}
								}
								else if(entity.getDayspaid()!=null){
									obj.setDaysused(obj.getDaysused()+entity.getDayspaid());
								}
							}
							else if(entity.getDayspaid()!=null){
								obj.setDaysused((double)entity.getDayspaid());
							}		
							if(obj.getDaysavailable()!=null && obj.getDaysused()!=null){
								obj.setDaysremain(obj.getDaysavailable()-obj.getDaysused());
							}

							if(obj.getHoursused()!=null){				
								if(entity.getId()!=null){
									if(entity.getHourspaid()!=null && prentity.getHourspaid()!=null && entity.getHourspaid()!=prentity.getHourspaid() && entity.getHourspaid()> prentity.getHourspaid()){
										Double hoursdiff=entity.getHourspaid()-prentity.getHourspaid();
										obj.setHoursused(obj.getHoursused()+hoursdiff);
									}
									else if(entity.getHourspaid()!=null && prentity.getHourspaid()!=null && entity.getHourspaid()!=prentity.getHourspaid() && entity.getHourspaid()<prentity.getHourspaid()){
										Double hoursdiff=prentity.getHourspaid()-entity.getHourspaid();
										obj.setHoursused(obj.getHoursused()-hoursdiff);
									}
									else if(entity.getHourspaid()==prentity.getHourspaid()){
										//do nothing
									}
								}
								else if(entity.getHourspaid()!=null){
									obj.setHoursused(obj.getHoursused()+entity.getHourspaid());
								}			   
							}
							else if(entity.getHourspaid()!=null){			    	
								obj.setHoursused((double)entity.getHourspaid());			    
							}			  
							if(obj.getHoursavailable()!=null && obj.getHoursused()!=null){
								obj.setHourremain(obj.getHoursavailable()-obj.getHoursused());
							}
						}
						
					// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
				/* =============================================
			         Code  For Days Paid Ends
		           =============================================	  */	


					/* =============================================
		                Code  For Paid Out Days
		              =============================================	  */				  
						if(entity.getPaidoutdays()!=null || entity.getPaidouthours()!=null){		   	   
							if(obj.getDaysused()!=null){
								if(entity.getId()!=null){
									if(entity.getPaidoutdays()!=null && prentity.getPaidoutdays()!=null && entity.getPaidoutdays()!=prentity.getPaidoutdays() && entity.getPaidoutdays()>prentity.getPaidoutdays()){
										Integer daysdiff=entity.getPaidoutdays()-prentity.getPaidoutdays();
										obj.setDaysused(obj.getDaysused()+daysdiff);
									}
									else if(entity.getPaidoutdays()!=null && prentity.getPaidoutdays()!=null && entity.getPaidoutdays()!=prentity.getPaidoutdays() && entity.getPaidoutdays()<prentity.getPaidoutdays()){
										Integer daysdiff=prentity.getPaidoutdays()-entity.getPaidoutdays();
										obj.setDaysused(obj.getDaysused()-daysdiff);
									}
									else if(entity.getPaidoutdays()==prentity.getPaidoutdays()){
										//do nothing
									}
								}
								else if(entity.getPaidoutdays()!=null){
									obj.setDaysused(obj.getDaysused()+entity.getPaidoutdays());
								}
							}
							else if(entity.getPaidoutdays()!=null){
								obj.setDaysused((double)entity.getPaidoutdays());
							}		
							if(obj.getDaysavailable()!=null && obj.getDaysused()!=null){
								obj.setDaysremain(obj.getDaysavailable()-obj.getDaysused());
							}

							if(obj.getHoursused()!=null){

								if(entity.getId()!=null){
									if(entity.getPaidouthours()!=null && prentity.getPaidouthours()!=null && entity.getPaidouthours()!=prentity.getPaidouthours() && entity.getPaidouthours()> prentity.getPaidouthours()){
										Double hoursdiff=entity.getPaidouthours()-prentity.getPaidouthours();
										obj.setHoursused(obj.getHoursused()+hoursdiff);
									}
									else if(entity.getPaidouthours()!=null && prentity.getPaidouthours()!=null && entity.getPaidouthours()!=prentity.getPaidouthours() && entity.getPaidouthours()<prentity.getPaidouthours()){
										Double hoursdiff=prentity.getPaidouthours()-entity.getPaidouthours();
										obj.setHoursused(obj.getHoursused()-hoursdiff);
									}
									else if(entity.getPaidouthours()==prentity.getPaidouthours()){
										//do nothing
									}
								}
								else if(entity.getPaidouthours()!=null){
									obj.setHoursused(obj.getHoursused()+entity.getPaidouthours());
								}			   
							}
							else if(entity.getPaidouthours()!=null){			    	
								obj.setHoursused((double)entity.getPaidouthours());			    
							}			  
							if(obj.getHoursavailable()!=null && obj.getHoursused()!=null){
								obj.setHourremain(obj.getHoursavailable()-obj.getHoursused());
							}		
						}
						/* =============================================
	                       Code  For Paid Out Days Ends
	                      =============================================	  */	 

						genericDAO.saveOrUpdate(obj);			   
					}
				}
			}
		
		else{			
			
			if(leaveBalobs!=null && leaveBalobs.size()>0){				
			for(LeaveCurrentBalance balance:leaveBalobs){
				if(preventity){
					if(balance.getDaysused()!=null && prentity.getDayspaid()!=null){ 
						balance.setDaysused(balance.getDaysused()-prentity.getDayspaid());
						}
					
						if(balance.getDaysused()!=null && prentity.getPaidoutdays()!=null){ 
						balance.setDaysused(balance.getDaysused()-prentity.getPaidoutdays());
						}
					
						if(balance.getDaysavailable()!=null && balance.getDaysused()!=null){ 
						balance.setDaysremain(balance.getDaysavailable()-balance.getDaysused());
						}


						if(balance.getHoursused()!=null && prentity.getHourspaid()!=null){
						balance.setHoursused(balance.getHoursused()-prentity.getHourspaid());
						}
					
						if(balance.getHoursused()!=null && prentity.getPaidouthours()!=null){
						balance.setHoursused(balance.getHoursused()-prentity.getPaidouthours());
						}

						if(balance.getHoursavailable()!=null && balance.getHoursused()!=null){
						balance.setHourremain(balance.getHoursavailable()-balance.getHoursused());
						}
					// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
						genericDAO.saveOrUpdate(balance);
				}
				else{
					if(balance.getDaysused()!=null && entity.getDayspaid()!=null){ 
						balance.setDaysused(balance.getDaysused()-entity.getDayspaid());
						}
					
						if(balance.getDaysused()!=null && entity.getPaidoutdays()!=null){ 
						balance.setDaysused(balance.getDaysused()-entity.getPaidoutdays());
						}
					
						if(balance.getDaysavailable()!=null && balance.getDaysused()!=null){ 
						balance.setDaysremain(balance.getDaysavailable()-balance.getDaysused());
						}


						if(balance.getHoursused()!=null && entity.getHourspaid()!=null){
						balance.setHoursused(balance.getHoursused()-entity.getHourspaid());
						}
					
						if(balance.getHoursused()!=null && entity.getPaidouthours()!=null){
						balance.setHoursused(balance.getHoursused()-entity.getPaidouthours());
						}

						if(balance.getHoursavailable()!=null && balance.getHoursused()!=null){
						balance.setHourremain(balance.getHoursavailable()-balance.getHoursused());
						}
					// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
						genericDAO.saveOrUpdate(balance);
				}			 
			}
		}
			entity.setAmountpaid(0.00);
			entity.setHourlyamountpaid(0.00);
			entity.setDayspaid(0);
			entity.setHourspaid(0.0);
			entity.setPaidouthours(0.0);
		
		}
		}
		beforeSave(request, entity, model);
		// merge into datasource
		System.out.println("\nCRUD1\n");
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);

		setupCreate(model, request);
		request.getSession().setAttribute("msg","added successfully");
		return "redirect:create.do";		
	}

	
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") Ptodapplication entity, BindingResult bindingResult,
			HttpServletRequest request) {
		Map criterias=new HashMap();
		if(entity.getLeavetype().getId()!=5 && entity.getLeavetype().getId()!=6 && entity.getLeavetype().getId()!=7 && entity.getLeavetype().getId()!=8){		
		criterias.put("company.id",entity.getCompany().getId());
		criterias.put("terminal.id", entity.getTerminal().getId());
		criterias.put("empcategory.id", entity.getCategory().getId());
		criterias.put("empname.id", entity.getDriver().getId());
		criterias.put("leavetype.id", entity.getLeavetype().getId());
		criterias.put("status",1);
		List<LeaveCurrentBalance> balances=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
		for(LeaveCurrentBalance balance:balances){
			if(balance.getDaysused()!=null && entity.getDayspaid()!=null){ 
				balance.setDaysused(balance.getDaysused()-entity.getDayspaid());
			}
			
			if(balance.getDaysused()!=null && entity.getPaidoutdays()!=null){ 
				balance.setDaysused(balance.getDaysused()-entity.getPaidoutdays());
			}
			
			if(balance.getDaysavailable()!=null && balance.getDaysused()!=null){ 
				balance.setDaysremain(balance.getDaysavailable()-balance.getDaysused());
			}


			if(balance.getHoursused()!=null && entity.getHourspaid()!=null){
				balance.setHoursused(balance.getHoursused()-entity.getHourspaid());
			}
			
			if(balance.getHoursused()!=null && entity.getPaidouthours()!=null){
				balance.setHoursused(balance.getHoursused()-entity.getPaidouthours());
			}

			if(balance.getHoursavailable()!=null && balance.getHoursused()!=null){
				balance.setHourremain(balance.getHoursavailable()-balance.getHoursused());
			}
			// obj.setDayssbalance((double)entity.getEarneddays()-entity.getUseddays());
			genericDAO.saveOrUpdate(balance);
		}		
		
	 }
		return super.delete(entity, bindingResult, request);
	}    

	@Override
	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
	{

		if ("findExp".equalsIgnoreCase(action)) {
			
			List<String> list = new ArrayList<String>();

			Date dateHired=null;
			Date dateReHired=null;
			Date nextAnniversaryDate=null;
			Double hoursAlloted = 0.0;
			Double daysAlloted = 0.0;
			boolean setdays=false;
			boolean sethours=false;
			String comId = request.getParameter("company");
			String terminalId = request.getParameter("terminal");
			String employeeId = request.getParameter("driver");
			String leavetype = request.getParameter("leavetype");
			String category = request.getParameter("category");

			if (!StringUtils.isEmpty(employeeId)) {

				double remainingdays=0,earneddays=0,availabledays=0,usedday=0,earnedhours=0,availablehours=0,usedhours=0,remaininghours=0,ptodRate=0,ptodHourlyRate=0;
                  
				
				Map criteria=new HashMap();
				criteria.clear();
				criteria.put("id",Long.valueOf(employeeId));
				criteria.put("status",1);
				Driver driver = genericDAO.getByCriteria(Driver.class,criteria);
				dateHired = driver.getDateHired();
				dateReHired=driver.getDateReHired();
				nextAnniversaryDate=driver.getNextAnniversaryDate();
				if (!StringUtils.isEmpty(employeeId) &&!StringUtils.isEmpty(category) && !StringUtils.isEmpty(leavetype)) 
				{				
					String Leavequery = "select obj from LeaveCurrentBalance obj where obj.status=1 and obj.empname="+employeeId+" and obj.empcategory="+category+"  and obj.leavetype="+leavetype+"";
					
					List<LeaveCurrentBalance> leaveob = genericDAO.executeSimpleQuery(Leavequery);
					
					for(LeaveCurrentBalance leaveobs:leaveob){
						
						availabledays=(leaveobs.getDaysavailable()!=null?leaveobs.getDaysavailable():0);
						usedday=(leaveobs.getDaysused()!=null?leaveobs.getDaysused():0);
						remainingdays=(leaveobs.getDaysremain()!=null?leaveobs.getDaysremain():0);

						
						availablehours=(leaveobs.getHoursavailable()!=null?leaveobs.getHoursavailable():0);
						usedhours=(leaveobs.getHoursused()!=null?leaveobs.getHoursused():0);
						remaininghours=(leaveobs.getHourremain()!=null?leaveobs.getHourremain():0);
						
					}
				}


				if (!StringUtils.isEmpty(comId)&& !StringUtils.isEmpty(terminalId)&& !StringUtils.isEmpty(employeeId) &&!StringUtils.isEmpty(category) && !StringUtils.isEmpty(leavetype)) 
				{

					String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category+"  and obj.leavetype='"+leavetype+"'";
					List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);				

				}


				request.getSession().setAttribute("driver", driver);
				
				DecimalFormat df = new DecimalFormat("#0");
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

				//String earneddayss= df.format(earneddays);
				String availabledayss= df.format(availabledays);
				String used= df.format(usedday);
				String remain= df.format(remainingdays);
				String pRate = df.format(ptodRate);
				String phRate = df.format(ptodHourlyRate);


				//String earnedHour= df.format(earnedhours);
				String availablehourss= df.format(availablehours);
				String usedHour= df.format(usedhours);
				String remainHour= df.format(remaininghours);


				list.add(availabledayss);
				if(dateHired!=null){
					list.add(dateFormat.format(dateHired));
				}else{
				list.add(null);
				}
				list.add(used);
				list.add(remain);
				list.add(pRate);
				list.add(phRate);
				list.add(availablehourss);
				list.add(usedHour);
				list.add(remainHour);
				if(dateReHired!=null)
					list.add(dateFormat.format(dateReHired));
				else
					list.add(null);
				
				
				if(leavetype.equals("4")){				
					if(nextAnniversaryDate!=null)
						list.add(dateFormat.format(nextAnniversaryDate));
					else
						list.add(null);				
				}
				else if(leavetype.equals("1")){
					if(driver.getDateProbationEnd()!=null){
					 try{
						Calendar now= Calendar.getInstance() ;
						Date probationEndDate = driver.getDateProbationEnd();
						if(probationEndDate.after(now.getTime())){
							nextAnniversaryDate=probationEndDate;
							String formattedDate = sdf.format(probationEndDate);
							list.add(formattedDate);	
						}
						else{
							int currentdate = now.get(Calendar.YEAR);
							currentdate = currentdate + 1;
							String nxtallotDate="01-01-"+currentdate;							
							nextAnniversaryDate= sdf.parse(nxtallotDate);
							list.add("01-01-"+currentdate);
						}
					 }
					 catch(Exception e){
						 list.add(null);
					    System.out.println("Error in parsing Date "+e);
					 }
					}
					else{
						try{
						Calendar now2= Calendar.getInstance() ;
						int currentdate = now2.get(Calendar.YEAR);
						currentdate = currentdate + 1;
						String nxtallotDate="01-01-"+currentdate;						
						nextAnniversaryDate= sdf.parse(nxtallotDate);
						list.add("01-01-"+currentdate);
						}
						catch (Exception e) {
							list.add(null);
						    System.out.println("Error in parsing Date "+e);
						}
					}
				}
				else{
					list.add(null);	
				}
				
				
				System.out.println("******* The next allotment date is "+nextAnniversaryDate);
				
				Map criterias=new HashMap();
				criterias.clear();
				criterias.put("company.id", driver.getCompany().getId());
				criterias.put("terminal.id", driver.getTerminal().getId());
				criterias.put("category.id", driver.getCatagory().getId());
				criterias.put("leavetype.id", Long.parseLong(leavetype) );				
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
						
						if(leavetype.equals("4")){
							if(ptod.getExperienceinyears()!=null)
								fromdays=ptod.getExperienceinyears();
							if(ptod.getExperienceinyearsTo()!=null)
								todays=ptod.getExperienceinyearsTo();
							if(ptod.getExperienceindays()!=null)
								days=ptod.getExperienceindays();					
						}
						else if(leavetype.equals("1")){
							if(ptod.getExperienceinyears()!=null)
								fromdays=ptod.getExperienceinyears()*365;
							if(ptod.getExperienceinyearsTo()!=null)
								todays=ptod.getExperienceinyearsTo()*365;
							if(ptod.getExperienceindays()!=null)
								days=ptod.getExperienceindays();
						}
						
						
						int count=0;
						
					    DateTime t = new DateTime();
									    	
					    	
							boolean expelg=false;
							boolean expfromDays=false;
							int totex=0;				
							int orgintotex=0;
							
							
							if(driver.getDateReHired()!=null && driver.getDateHired()!=null){
								
								totex=Days.daysBetween(new DateMidnight(driver.getDateHired()), new DateMidnight(nextAnniversaryDate)).getDays();
							}
							else if (driver.getDateReHired()!=null){
								totex=Days.daysBetween(new DateMidnight(driver.getDateReHired()), new DateMidnight(nextAnniversaryDate)).getDays();
							}
							else{
								
								totex=Days.daysBetween(new DateMidnight(driver.getDateHired()), new DateMidnight(nextAnniversaryDate)).getDays();
							}
							
							if(leavetype.equals("4")){
								orgintotex=totex;
								totex = totex/365;
							}
							else if(leavetype.equals("1")){
								orgintotex=totex;
								totex = totex;
							}
							
							System.out.println("Total exp "+totex);
							
							System.out.println("******** From Days is "+fromdays);
							
							System.out.println("*********** To days is "+todays);
							
							
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
				
				System.out.println("****** eligebal");
				if(ptod.getDayearned()!=null){				
					daysAlloted=ptod.getDayearned();					
				}				
				list.add(daysAlloted.toString());
				setdays = true;
				
				if(ptod.getHoursearned()!=null){					
					hoursAlloted=ptod.getHoursearned();									
				}	
				list.add(hoursAlloted.toString());
				sethours = true;
			}			
		}		
		
		if(!setdays){
			list.add("0");
		}
		if(!sethours){
			list.add("0.0");
		}
				
	}
}
			
			
			System.out.println("********** list size is "+list.size());
			Gson gson = new Gson();
			return gson.toJson(list);
}


		//FOR AmountPaid
		if ("amountPaid".equalsIgnoreCase(action)){

			List<String> list = new ArrayList<String>();
			String comId = request.getParameter("company");
			String terminalId = request.getParameter("terminal");
			String leavetype = request.getParameter("leavetype");
			String category = request.getParameter("category");
			String employeeId = request.getParameter("driver");
			String batchDate=request.getParameter("batch");
			batchDate=ReportDateUtil.getFromDate(batchDate);
			
			// Jury duty fix - 3rd Nov 2016
			String leaveTypeToBeUsed = StringUtils.equals(leavetype, "9") ? "1" : leavetype;

			Integer dayspaid = Integer.valueOf(request.getParameter("dayspaid"));
			Integer dayspaidout = Integer.valueOf(request.getParameter("dayspaidout"));

			//Integer.valueOf(request.getParameter("dayspaid"));



			double ptodRate=0.0;
			double amountPaid=0.0;


			if (!StringUtils.isEmpty(comId)&& !StringUtils.isEmpty(terminalId)&&!StringUtils.isEmpty(leavetype) &&!StringUtils.isEmpty(comId)) 
			{
			/*	String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category+"  and obj.leavetype='"+leavetype+"'";
				List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
				for(Ptod ptods:ptodob){
					ptodRate=ptods.getRate();

				}*/
				boolean cal=false;
				
				if(!category.equals("2")){						
					Driver driver = genericDAO.getById(Driver.class,Long.parseLong(employeeId));
					
					if(driver.getPayTerm().equals("3")){	
						String query="select obj from WeeklySalary obj where obj.driver.fullName='"+driver.getFullName()+"'"
										  // PTOD payrate fix - weekly salary - 25thMar2016
										  + " and '" + batchDate + "' BETWEEN obj.validFrom and obj.validTo";
						List<WeeklySalary> weeklySalaryRates=genericDAO.executeSimpleQuery(query);
						if(!weeklySalaryRates.isEmpty()){								
							WeeklySalary weeklySalaryRate=weeklySalaryRates.get(0);								
							ptodRate=weeklySalaryRate.getDailySalary();
						}
					}
					else{
						// Jury duty fix - 3rd Nov 2016
						String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category+" "
								+ " and obj.leavetype='"+leaveTypeToBeUsed+"'"; // instead of leavetype
						List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
						if(!ptodob.isEmpty()){
							Ptod ptod=ptodob.get(0);
							if(ptod.getCalculateFlag()==1){
								cal=true;
							}else{
								ptodRate=ptod.getRate();	
							}
						}
						
						if(cal){											
								String query="select obj from PtodRate obj where obj.driver="+employeeId+" and obj.catagory="+category+" and obj.company="+comId+" and obj.terminal="+terminalId+"and '"+batchDate+"' BETWEEN obj.validFrom and obj.validTo";
								List<PtodRate> ptodRates=genericDAO.executeSimpleQuery(query);
								if(!ptodRates.isEmpty()){
									PtodRate ptodsrate=ptodRates.get(0);
									ptodRate=ptodsrate.getPtodRate();
								}
						}
					}
				}
				else{
					// Jury duty fix - 3rd Nov 2016
					String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category
							+"  and obj.leavetype='"+leaveTypeToBeUsed+"'";  // instead of leavetype
					List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
					if(!ptodob.isEmpty()){
						Ptod ptod=ptodob.get(0);
						if(ptod.getCalculateFlag()==1){
							cal=true;
						}else{
							ptodRate=ptod.getRate();	
						}
					}
					
					if(cal){											
							String query="select obj from PtodRate obj where obj.driver="+employeeId+" and obj.catagory="+category+" and obj.company="+comId+" and obj.terminal="+terminalId+"and '"+batchDate+"' BETWEEN obj.validFrom and obj.validTo";
							List<PtodRate> ptodRates=genericDAO.executeSimpleQuery(query);
							if(!ptodRates.isEmpty()){
								PtodRate ptodsrate=ptodRates.get(0);
								ptodRate=ptodsrate.getPtodRate();
							}
					}
				}
			}
			amountPaid=(dayspaid+dayspaidout)*ptodRate;
			//System.out.println("\ncalculated amountPaid\n");
			DecimalFormat df = new DecimalFormat("#0.00");
			String APaid = df.format(amountPaid);

			list.add(APaid);


			Gson gson = new Gson();
			return gson.toJson(list);
		}


		// For hourlyamountPaid
		if ("hourlyamountPaid".equalsIgnoreCase(action)){

			List<String> list = new ArrayList<String>();
			String comId = request.getParameter("company");
			String terminalId = request.getParameter("terminal");
			String leavetype = request.getParameter("leavetype");
			String category = request.getParameter("category");
			String employeeId = request.getParameter("driver");
			Double hpaid = Double.valueOf(request.getParameter("hourspaid"));
			Double hourpaidout = Double.valueOf(request.getParameter("hourspaidout"));
			String batchDate=request.getParameter("batch");
			batchDate=ReportDateUtil.getFromDate(batchDate);
			double ptodhourlyRate=0.0;
			double hourlyamountPaid=0.0;

			Driver employeeRate=genericDAO.getById(Driver.class, Long.parseLong(employeeId));
			if (!StringUtils.isEmpty(comId)&& !StringUtils.isEmpty(terminalId)&&!StringUtils.isEmpty(leavetype) &&!StringUtils.isEmpty(comId))
			{
				String query1="select obj from HourlyRate obj where obj.driver="+employeeId+" and obj.catagory="+category+" and obj.company="+comId+" and obj.terminal="+terminalId+"and '"+batchDate+"' BETWEEN obj.validFrom and obj.validTo";
				List<HourlyRate> hourlyRates=genericDAO.executeSimpleQuery(query1);
				if(!hourlyRates.isEmpty()){
					HourlyRate hourlyRate=hourlyRates.get(0);
					ptodhourlyRate=hourlyRate.getHourlyRegularRate();
					
				}
			/*	String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category+"  and obj.leavetype='"+leavetype+"'";
				List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
				for(Ptod ptods:ptodob){
					//ptodhourlyRate=ptods.getHourlyrate();
					if(employeeRate.getHourlyRegularRate()!=null ){
						if(employeeRate.getHourlyRegularRate()!=0){
							//System.out.println("\nEmp hourly regular rate for calculating hourly amount paid\n");
							ptodhourlyRate=employeeRate.getHourlyRegularRate();
						}
						else{
							ptodhourlyRate=ptods.getHourlyrate();
						}
					}else{
						ptodhourlyRate=ptods.getHourlyrate();	
					}

				}
*/

			}	
			hourlyamountPaid=(hpaid+hourpaidout)*ptodhourlyRate;
			//System.out.println("\nhourlyamountPaid\n");
			//System.out.println("\nemployeeId--->"+employeeId+"\n");
			DecimalFormat df = new DecimalFormat("#0.00");
			String HAPaid = df.format(hourlyamountPaid);

			list.add(HAPaid);




			Gson gson = new Gson();
			return gson.toJson(list);
		}

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
		if("findDTerminal".equalsIgnoreCase(action)){
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
		
		if ("recalculateAmount".equalsIgnoreCase(action)) {
			List<String> list = new ArrayList<String>();
			Integer dayspaid = Integer.valueOf(request.getParameter("ndayspaid"));
			Integer dayspaidout = Integer.valueOf(request.getParameter("ndayspaidout"));
			
			Double ptodrate=Double.parseDouble(request.getParameter("ptodrate"));
			Double amountPaid=(dayspaid+dayspaidout)*ptodrate;
			DecimalFormat df = new DecimalFormat("#0.00");
			String APaid = df.format(amountPaid);
			list.add(APaid);
			Gson gson = new Gson();
			return gson.toJson(list);
		}
		
       
		if ("findAmount".equalsIgnoreCase(action)) {
			List<Double> str=new ArrayList<Double>();
			String comId = request.getParameter("company");
			String terminalId = request.getParameter("terminal");
			String employeeId = request.getParameter("driver");
			String leavetype = request.getParameter("leavetype");
			String category = request.getParameter("category");
			String batchDate=request.getParameter("batch");
			batchDate=ReportDateUtil.getFromDate(batchDate);

			// Jury duty fix - 3rd Nov 2016
			String leaveTypeToBeUsed = StringUtils.equals(leavetype, "9") ? "1" : leavetype ;
			
			Double ptodrate=0.0;
			Double hourlyptodrate=0.0;
			boolean cal=false;
			
			if(!category.equals("2")){						
				Driver driver = genericDAO.getById(Driver.class,Long.parseLong(employeeId));
				
				if(driver.getPayTerm().equals("3")){
					String query="select obj from WeeklySalary obj where obj.driver.fullName='"+driver.getFullName()+"'"
									  // PTOD payrate fix - weekly salary - 25thMar2016
									  + " and '" + batchDate + "' BETWEEN obj.validFrom and obj.validTo";
					
					List<WeeklySalary> weeklySalaryRates=genericDAO.executeSimpleQuery(query);
					if(!weeklySalaryRates.isEmpty()){							
						WeeklySalary weeklySalaryRate=weeklySalaryRates.get(0);							
						ptodrate=weeklySalaryRate.getDailySalary();
					}
				}
				else{
					String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category+"  and obj.leavetype='"+leavetype+"'";
					List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
					if(!ptodob.isEmpty()){
						Ptod ptod=ptodob.get(0);
						if(ptod.getCalculateFlag()==1){
							cal=true;
						}else{
							ptodrate=ptod.getRate();	
						}
					}
					if(cal){								
							String query="select obj from PtodRate obj where obj.driver="+employeeId+" and obj.catagory="+category+" and obj.company="+comId+" and obj.terminal="+terminalId+"and '"+batchDate+"' BETWEEN obj.validFrom and obj.validTo";
							List<PtodRate> ptodRates=genericDAO.executeSimpleQuery(query);
							if(!ptodRates.isEmpty()){
								PtodRate ptodsrate=ptodRates.get(0);
								ptodrate=ptodsrate.getPtodRate();
							}			
					}
				}
			}
			else{
				// Jury duty fix - 3rd Nov 2016
				String ptodquery = "select obj from Ptod obj where obj.company="+comId+" and obj.terminal="+terminalId+" and obj.category="+category
						+"  and obj.leavetype='"+leaveTypeToBeUsed+"'";  // instead of leavetype
				List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
				if(!ptodob.isEmpty()){
					Ptod ptod=ptodob.get(0);
					if(ptod.getCalculateFlag()==1){
						cal=true;
					}else{
						ptodrate=ptod.getRate();	
					}
				}
				if(cal){								
						String query="select obj from PtodRate obj where obj.driver="+employeeId+" and obj.catagory="+category+" and obj.company="+comId+" and obj.terminal="+terminalId+"and '"+batchDate+"' BETWEEN obj.validFrom and obj.validTo";
						List<PtodRate> ptodRates=genericDAO.executeSimpleQuery(query);
						if(!ptodRates.isEmpty()){
							PtodRate ptodsrate=ptodRates.get(0);
							ptodrate=ptodsrate.getPtodRate();
						}			
				}				
			}
			
			
			String query1="select obj from HourlyRate obj where obj.driver="+employeeId+" and obj.catagory="+category+" and obj.company="+comId+" and obj.terminal="+terminalId+"and '"+batchDate+"' BETWEEN obj.validFrom and obj.validTo";
			List<HourlyRate> hourlyRates=genericDAO.executeSimpleQuery(query1);
			if(!hourlyRates.isEmpty()){
				HourlyRate hourlyRate=hourlyRates.get(0);
				hourlyptodrate=hourlyRate.getHourlyRegularRate();
				
			}
			str.add(ptodrate);
			str.add(hourlyptodrate);
			Gson gson = new Gson();
			return gson.toJson(str);
		}

		return "";
	}


}
