package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.DateUtil;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.Ptod;


@Controller
@RequestMapping("/hr/updatevacationleavebalance")
public class UpdateVacationLeaveBalanceController extends BaseController {
	
	public UpdateVacationLeaveBalanceController() {
		setUrlContext("/hr/updatevacationleavebalance");
	}
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(method = RequestMethod.GET, value = "/start.do")
	public String start(ModelMap map,HttpServletRequest request){
		return urlContext+"/updatevacationleavebalance";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/update.do")
	public String update(ModelMap map,HttpServletRequest request){
		SearchCriteria criteria = (SearchCriteria) request.getSession()
		.getAttribute("searchCriteria");
		criteria=null;		
		Map frstcriterias=new HashMap();
		frstcriterias.put("leavetype.id",4l);
		List<Ptod> ptods=genericDAO.findByCriteria(Ptod.class,frstcriterias);
		List<String> str = new ArrayList<String>();
		
		for(Ptod ptod:ptods){
			StringBuffer buffer=new StringBuffer("");
			String acc="No";
			if(ptod.getAnnualoraccrual()==1)
				acc="Yes";
		/*	buffer.append("---------------------------------------------------------------------------------------------------------------------------------");
			buffer.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>PTOD</font></b></span>");
			buffer.append("<table class='datagrid' width='100%'>");
			buffer.append("<tr><th>Company</th><th>Terminal</th><th>Category</th><th>Leave Type</th><th>Days Earned</th><th>Hours Earned</th><th>Experiance Required in days</th><th>Experiance Required in Year From</th><th>Experiance Required in Year To</th><th>Annual Accrual</th></tr>");
			buffer.append("<tr>");
			buffer.append("<td>"+ptod.getCompany().getName()+"</td>");
			buffer.append("<td>"+ptod.getTerminal().getName()+"</td>");
			buffer.append("<td>"+ptod.getCategory().getName()+"</td>");
			buffer.append("<td>"+ptod.getLeavetype().getName()+"</td>");
			buffer.append("<td>"+ptod.getDayearned()+"</td>");
			buffer.append("<td>"+ptod.getHoursearned()+"</td>");
			buffer.append("<td>"+ptod.getExperienceindays()+"</td>");
			buffer.append("<td>"+ptod.getExperienceinyears()+"</td>");
			buffer.append("<td>"+ptod.getExperienceinyearsTo()+"</td>");
			buffer.append("<td>"+acc+"</td>");
			buffer.append("</tr>");
			buffer.append("</table>");*/
/*			buffer.append("<br/><b><font color='#003466'>PTOD</b></font><br/><font color='#5e90d5'><b>company-</font>"+ptod.getCompany().getName()+"<br/><font color='#5e90d5'> terminal-</font>"+ptod.getTerminal().getName()+
					"<br/><font color='#5e90d5'> category-</font>"+ptod.getCategory().getName()+"<br/><font color='#5e90d5'> leave type-</font>"+ptod.getLeavetype().getName() +
					"<br/><font color='#5e90d5'>Reqired Experince in Days-</font>"+ptod.getExperienceindays()+"<br/><font color='#5e90d5'> Reqired Experince in Year From to-</font>"+ptod.getExperienceinyears()+
				" - "+ptod.getExperienceinyearsTo()+"<br/><font color='#5e90d5'>days Earned </font>"+ptod.getDayearned()+"<br/><font color='#5e90d5'>Hours Earned </font>"+ptod.getHoursearned()+"<br/><font color='#5e90d5'> Annual Accrual-</font>"+acc+" </b>");*/
			//str.add(buffer.toString());
			boolean accheck=false;
			if(ptod.getAnnualoraccrual()==1)
				accheck=true;
			Map criterias=new HashMap();
			criterias.put("company.id", ptod.getCompany().getId());
			criterias.put("terminal.id", ptod.getTerminal().getId());
			criterias.put("empcategory.id", ptod.getCategory().getId());
			criterias.put("leavetype.id", ptod.getLeavetype().getId());
			List<LeaveCurrentBalance> balances=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
			StringBuffer buffer1=new StringBuffer();
			Calendar currentDate = Calendar.getInstance();
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
			for(LeaveCurrentBalance balance:balances){
				DateTime dt = new DateTime();
				boolean expelg=false;
				int totex=0;
				int orgintotex=0;
				if(balance.getEmpname().getDateReHired()!=null && balance.getEmpname().getDateHired()!=null){
					
					totex=Days.daysBetween(new DateMidnight(balance.getEmpname().getDateHired()), new DateMidnight(dt)).getDays();
				}
				else if (balance.getEmpname().getDateReHired()!=null){
					totex=Days.daysBetween(new DateMidnight(balance.getEmpname().getDateReHired()), new DateMidnight(dt)).getDays();
				}
				else{
					
					totex=Days.daysBetween(new DateMidnight(balance.getEmpname().getDateHired()), new DateMidnight(dt)).getDays();
				}
				
				orgintotex=totex;
				totex=totex/365;
				
				/*buffer1.append("<span style=' background-color:#5e90d5;'><b><font color='white'>Employee Current Balance</font></b></span>");
				buffer1.append("<table class='datagrid' width='100%'>");
				buffer1.append("<tr><th>Employee</th><th>Total Exp(Days)</th><th>Update Leave</th></tr>");
				buffer1.append("<tr><td>"+balance.getEmpname().getFullName()+"</td><td>"+totex);*/
				
				//buffer1.append("<br/><b><font color='#003466'>Current Balance</font></b><br/> <b><font color='#5e90d5'>Employee</font>"+balance.getEmpname().getFullName()+"<br/><font color='#5e90d5'> Total Exp</font>"+totex+"<br/><font color='#5e90d5'> Update Leave</font>");
				System.out.println("\n driver-->"+balance.getEmpname().getFullName());
				if(days>0&&orgintotex>0){
					if(orgintotex>=days)
						expelg=true;
				}
				if(fromdays>0&&totex>0&&todays>0){
					if(totex>=fromdays&&totex<todays)
						expelg=true;
				}
				if(fromdays>0&&todays<0&&totex>0){
					if(totex>=fromdays)
						expelg=true;
					
				}
				if(fromdays<=0&&todays>0&&totex>0){
					if(totex<todays)
						expelg=true;
				}
				/*if(fromdays>0&&totex>0){
					if(todays>0){
						if(totex>=fromdays&&totex<todays)
							expelg=true;
					}
					else{
						if(totex>=fromdays)
							expelg=true;
					}
				}*/
				/*buffer1.append("</td><td>"+expelg+"</td></tr>");
				buffer1.append("</table>");*/
				//buffer1.append(expelg+"</b>");
				if(expelg){
					//days
					if(ptod.getDayearned()!=null){
						if(accheck){
							if(balance.getDaysremain()!=null){
							Double earnedday=balance.getDaysremain()+ptod.getDayearned();
							balance.setDayssbalance(earnedday);
							balance.setDaysused(0.00);
							balance.setDaysremain(earnedday);
							}
						}else{
							if(balance.getDaysremain()!=null){
							Double earnedday=ptod.getDayearned();
							balance.setDayssbalance(earnedday);
							balance.setDaysused(0.00);
							balance.setDaysremain(earnedday);
							}
						}
					}
					//Hours
					if(ptod.getHoursearned()!=null){
						if(accheck){
							if(balance.getHourremain()!=null){
								Double earnehour=balance.getHourremain()+ptod.getHoursearned();
								balance.setHoursbalance(earnehour);
								balance.setHoursused(0.00);
								balance.setHourremain(earnehour);
							}
							genericDAO.saveOrUpdate(balance);
						}else{
							if(balance.getHourremain()!=null){
								Double earnehour=balance.getHourremain();
								balance.setHoursbalance(earnehour);
								balance.setHoursused(0.00);
								balance.setHourremain(earnehour);
								genericDAO.saveOrUpdate(balance);
							}
						}
					}
				/*	if(balance.getEmpcategory().getId()==2||balance.getEmpcategory().getId()==4){
						buffer1.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>Current</font></b></span>");
						buffer1.append("<table class='datagrid' width='100%'>");
						buffer1.append("<tr><th>Days Earned</th><th>Days used</th><th>Days Remaining</th></tr>");
						buffer1.append("<tr><td>"+balance.getDayssbalance()+"</td><td>"+balance.getDaysused()+"</td><td>"+balance.getDaysremain()+"</td></tr>");
						buffer1.append("</table>");
						//buffer1.append("<br/><b><font color='#003466'>Current</font></b><br/><b><font color='#5e90d5'>Days Earned </font>"+balance.getDayssbalance()+"</b><br/><b><font color='#5e90d5'>Days used</font>"+balance.getDaysused()+"</b><br/><b><font color='#5e90d5'>Days Remaining</font>"+balance.getDaysremain()+"</b><br/>");
						if(accheck){
							System.out.println("\n in accrual-->"+accheck);
							System.out.println("balance.getDaysremain()-->"+balance.getDaysremain());
							System.out.println("\n ptod.getDayearned()-->"+ptod.getDayearned());
						Double earnedday=balance.getDaysremain()+ptod.getDayearned();
						System.out.println("\n ernedday-->"+earnedday);
						buffer1.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>updated Current</font></b></span>");
						buffer1.append("<table class='datagrid' width='100%'>");
						buffer1.append("<tr><th>Days Earned</th><th>Days used</th><th>Days Remaining</th></tr>");
						buffer1.append("<tr><td>"+earnedday+"</td><td>"+0+"</td><td>"+0+"</td></tr>");
						buffer1.append("</table>");
						//buffer1.append("<br/><b><font color='#003466'>Updated Current</font><br/><font color='#5e90d5'>Days Earned </font>"+ernedday+"<br/><font color='#5e90d5'>Days used</font>"+0+"<br/><font color='#5e90d5'>Days Remaining</font>"+0);
						}
						else{
							Double earnedday=ptod.getDayearned();
							//buffer1.append("</br><b><font color='#003466'>updated Current</font><br/><font color='#5e90d5'>Days Earned </font>"+ernedday+"<br/><font color='#5e90d5'>Days used</font>"+0+"<br/><font color='#5e90d5'>Days Remaining</font>"+0);
							buffer1.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>updated Current</font></b></span>");
							buffer1.append("<table class='datagrid' width='100%'>");
							buffer1.append("<tr><th>Days Earned</th><th>Days used</th><th>Days Remaining</th></tr>");
							buffer1.append("<tr><td>"+earnedday+"</td><td>"+0+"</td><td>"+0+"</td></tr>");
							buffer1.append("</table>");
						}
						count++;
					}
					if(balance.getEmpcategory().getId()==3){
						buffer1.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>Current</font></b></span>");
						buffer1.append("<table class='datagrid' width='100%'>");
						buffer1.append("<tr><th>Hours Earned </th><th>Hours used</th><th>Hours Remaining</th></tr>");
						buffer1.append("<tr><td>"+balance.getHoursbalance()+"</td><td>"+balance.getHoursused()+"</td><td>"+balance.getHourremain()+"</td></tr>");
						buffer1.append("</table>");
						
						//buffer1.append("<br/><b><font color='#003466'>Current</font></b><br/><b><font color='#5e90d5'>Hours Earned </font>"+balance.getHoursbalance()+"</b><br/><b><font color='#5e90d5'>Hours used</font>"+balance.getHoursused()+"</b><br/><b><font color='#5e90d5'>Hours Remaining</font>"+balance.getHourremain()+"</b><br/>");
						if(accheck){
							System.out.println("\n in accrual-->"+accheck);
							
						Double earnehour=balance.getHourremain()+ptod.getHoursearned();
						System.out.println("\n ernedday-->"+earnehour);
						buffer1.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>updated Current</font></b></span>");
						buffer1.append("<table class='datagrid' width='100%'>");
						buffer1.append("<tr><th>Hours Earned </th><th>Hours used</th><th>Hours Remaining</th></tr>");
						buffer1.append("<tr><td>"+earnehour+"</td><td>"+0+"</td><td>"+0+"</td></tr>");
						buffer1.append("</table>");
						//buffer1.append("<br/><b><font color='#003466'>Updated Current</font><br/><font color='#5e90d5'>Hours  Earned </font>"+earnehour+"<br/><font color='#5e90d5'>Hours used</font>"+0+"<br/><font color='#5e90d5'>Hours Remaining</font>"+0);
						}
						else{
							Double earnehour=ptod.getHoursearned();
							//buffer1.append("</br><b><font color='#003466'>updated Current</font><br/><font color='#5e90d5'>Hours Earned </font>"+ernehour+"<br/><font color='#5e90d5'>Hours used</font>"+0+"<br/><font color='#5e90d5'>HoursRemaining</font>"+0);
							buffer1.append("<br/><span style=' background-color:#5e90d5;'><b><font color='white'>updated Current</font></b></span>");
							buffer1.append("<table class='datagrid' width='100%'>");
							buffer1.append("<tr><th>Hours Earned </th><th>Hours used</th><th>Hours Remaining</th></tr>");
							buffer1.append("<tr><td>"+earnehour+"</td><td>"+0+"</td><td>"+0+"</td></tr>");
							buffer1.append("</table>");
						}
						count++;
					}*/
					count++;
				}
			
				/*str.add(buffer1.toString());
				System.out.println("\n days-->"+"name"+balance.getEmpname().getFullName()+DateUtil.daysBetween(balance.getEmpname().getDateHired(),currentDate.getTime()));
				System.out.println("\n eligibility-->"+expelg);*/
			}
			ptod.setNoOfRecord(count);
		}
		request.getSession().setAttribute("ptodlists", str);
		
		map.addAttribute("list",ptods);
		request.getSession().setAttribute("msg","Process is successful");
		return "blank/updatevacationlist";
		//return urlContext + "/list";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/showlist.do")
	public String showLeaveList(ModelMap model,HttpServletRequest request){
		String id=request.getParameter("id");
		Ptod ptod=genericDAO.getById(Ptod.class, Long.parseLong(id));
		Map criterias=new HashMap();
		criterias.put("company.id", ptod.getCompany().getId());
		criterias.put("terminal.id", ptod.getTerminal().getId());
		criterias.put("empcategory.id", ptod.getCategory().getId());
		criterias.put("leavetype.id", ptod.getLeavetype().getId());
		List<LeaveCurrentBalance> balances=genericDAO.findByCriteria(LeaveCurrentBalance.class, criterias);
		List<LeaveCurrentBalance> currentBalances=new ArrayList<LeaveCurrentBalance>();
		//currentBalances=null;
		boolean accheck=false;
		if(ptod.getAnnualoraccrual()==1)
			accheck=true;
		Calendar currentDate = Calendar.getInstance();
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
		for(LeaveCurrentBalance balance:balances){
			DateTime dt = new DateTime();
			boolean expelg=false;
			/*int totex=	DateUtil.daysBetween(balance.getEmpname().getDateHired(),currentDate.getTime());*/
			int totex=0;
			
			int orgintotex=0;	
			
			
			if(balance.getEmpname().getDateReHired()!=null && balance.getEmpname().getDateHired()!=null){
				
				totex=Days.daysBetween(new DateMidnight(balance.getEmpname().getDateHired()), new DateMidnight(dt)).getDays();
			}
			else if (balance.getEmpname().getDateReHired()!=null){
				totex=Days.daysBetween(new DateMidnight(balance.getEmpname().getDateReHired()), new DateMidnight(dt)).getDays();
			}
			else{
				
				totex=Days.daysBetween(new DateMidnight(balance.getEmpname().getDateHired()), new DateMidnight(dt)).getDays();
			}
			
			orgintotex=totex;
			totex=totex/365;
			
			System.out.println("\n totalExp-->"+totex);
			if(days>0&&orgintotex>0){
				if(orgintotex>=days)
					expelg=true;
			}
			if(fromdays>0&&totex>0&&todays>0){
				if(totex>=fromdays&&totex<todays)
					expelg=true;
			}
			if(fromdays>0&&todays<0&&totex>0){
				if(totex>=fromdays)
					expelg=true;
				
			}
			if(fromdays<=0&&todays>0&&totex>0){
				if(totex<todays)
					expelg=true;
			}
			if(expelg){
			/*	if(balance.getEmpcategory().getId()==2||balance.getEmpcategory().getId()==4){
					currentBalances.add(balance);	
				}
				if(balance.getEmpcategory().getId()==3){
					currentBalances.add(balance);
					
				}*/
				currentBalances.add(balance);
			}
			
		}
		model.addAttribute("list",currentBalances);
		return "blank/vacationleavebalancelist";
		
	}
}