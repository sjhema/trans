package com.primovision.lutransport.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

import java.util.Calendar;
import java.util.Date;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.model.report.Billing_New;
import com.primovision.lutransport.controller.hr.LeaveBalanceController;
import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.tags.StaticDataUtil;
import com.primovision.lutransport.core.util.DateUtil;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Terminal;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.Attendance;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.EmpBonusTypesList;
//import com.primovision.lutransport.model.hr.Driver;
import com.primovision.lutransport.model.hr.EmployeeBonus;
import com.primovision.lutransport.model.hr.HolidayType;
import com.primovision.lutransport.model.hr.HourlyPayrollInvoice;
import com.primovision.lutransport.model.hr.HourlyRate;
import com.primovision.lutransport.model.hr.LeaveCurrentBalance;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.Ptod;
import com.primovision.lutransport.model.hr.PtodRate;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hr.SalaryOverride;
import com.primovision.lutransport.model.hr.TimeSheet;
import com.primovision.lutransport.model.hr.WeeklySalary;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayHistoryInput;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayroll;
import com.primovision.lutransport.model.hrreport.EmployeeBonusInput;
import com.primovision.lutransport.model.hrreport.EmployeeBonusWrapper;
import com.primovision.lutransport.model.hrreport.EmployeeInput;
import com.primovision.lutransport.model.hrreport.EmployeePayrollInput;
import com.primovision.lutransport.model.hrreport.EmployeePayrollWrapper;
import com.primovision.lutransport.model.hrreport.EmployeeWrapper;
import com.primovision.lutransport.model.hrreport.HourlyPayrollInvoiceDetails;
import com.primovision.lutransport.model.hrreport.LeaveAccrualReport;
import com.primovision.lutransport.model.hrreport.PayChexDetail;
import com.primovision.lutransport.model.hrreport.ProbationReportInput;
import com.primovision.lutransport.model.hrreport.PtodApplicationInput;
import com.primovision.lutransport.model.hrreport.RemainingLeaveInput;
import com.primovision.lutransport.model.hrreport.SalaryDetail;
import com.primovision.lutransport.model.hrreport.TimeSheetInput;
import com.primovision.lutransport.model.hrreport.TimeSheetWrapper;
import com.primovision.lutransport.model.hrreport.TimeSheetWrapperDetail;
import com.primovision.lutransport.model.hrreport.WeeklyPay;
import com.primovision.lutransport.model.hrreport.WeeklyPayDetail;
import com.primovision.lutransport.model.hrreport.WeeklypayWrapper;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.model.report.BillingWrapper;

/**
 * @author kishan
 *
 */
@Transactional(readOnly = false)
public class HrReportServiceImpl implements HrReportService {

	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private GenericDAO genericDAO;

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	/* (non-Javadoc)
	 * @see com.primovision.lutransport.service.HrReportService#generateDriverPayReport(com.primovision.lutransport.model.SearchCriteria)
	 */
	@Override
	public DriverPayWrapper generateDriverPayReport(SearchCriteria criteria) {
		// TODO Auto-generated method stub
		Map criterias = new HashMap();
		List<String> str = new ArrayList<String>();
		int errorCount = 0;
		Map<String, Double> map=new HashMap<String, Double>();
		Map<String, Integer> map1=new HashMap<String, Integer>();
		String payrollDate=(String) criteria.getSearchMap().get("payrollDate");
		String driverid=(String) criteria.getSearchMap().get("driver");
		String frombatch=(String) criteria.getSearchMap().get("fromDate");
		String tobatch=(String) criteria.getSearchMap().get("toDate");
		String company=(String) criteria.getSearchMap().get("company");
		String terminal=(String) criteria.getSearchMap().get("terminal");
		 String expire= (String) criteria.getSearchMap().get("expire");
		 String status=(String)criteria.getSearchMap().get("pay");
		 String sta=(String)criteria.getSearchMap().get("stat");
		 String driversmul=(String)criteria.getSearchMap().get("driversmul");
		 
		 List<String> holidayExceptionDriverNameList = (List<String>)criteria.getSearchMap().get("holidayexpdriversmul");
			if(holidayExceptionDriverNameList==null){
				holidayExceptionDriverNameList=new  ArrayList<String>();
				holidayExceptionDriverNameList.add("Name");
			}
			
			List<String> doublePayDriverNameList = (List<String>)criteria.getSearchMap().get("doubleholpayriversmul");
			if(doublePayDriverNameList==null){
				doublePayDriverNameList=new  ArrayList<String>();
				doublePayDriverNameList.add("Name");			
			}
		 
		 
		 if(!StringUtils.isEmpty(status)){
			 status="1";
		 }else{
			 status="2";
		 }
		 status="1";
		frombatch=ReportDateUtil.getFromDate(frombatch);
		tobatch=ReportDateUtil.getFromDate(tobatch);		
		
		payrollDate=ReportDateUtil.getFromDate(payrollDate);
		 String sum= (String) criteria.getSearchMap().get("summary");
		 Location companylocation=null;
		 Location terminallocation=null;
		 String driverIds="";
		 if(!StringUtils.isEmpty(company)){
		    companylocation=genericDAO.getById(Location.class, Long.parseLong(company));
		 }
		 if(!StringUtils.isEmpty(terminal)){
			    terminallocation=genericDAO.getById(Location.class, Long.parseLong(terminal));
		 }
		 
		 if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
			 Map drivercriteria = new HashMap();
			 drivercriteria.clear();
			 drivercriteria.put("status",1);
			 drivercriteria.put("company.id",companylocation.getId());
			 List<Driver> drivers = genericDAO.findByCriteria(Driver.class, drivercriteria);
			 for(Driver driverObj:drivers){
				 if(driverIds.equals(""))
					 driverIds = driverObj.getId().toString();
				else
					driverIds = driverIds +","+driverObj.getId().toString();
			 }
		}
		 
		Driver driver=null;
		List<Driver> drivers=null;
		/*if(!StringUtils.isEmpty(driverid)){
		Driver employee= genericDAO.getById(Driver.class,Long.parseLong(driverid));
		criterias.put("fullName",employee.getFullName());
	    Driver driver=genericDAO.getByCriteria(Driver.class, criterias);
		}*/
	    StringBuffer query=new StringBuffer("");
	    if(StringUtils.isEmpty(sta)){
	    	query.append("select obj from Ticket obj where obj.payRollStatus=1 and obj.payRollBatch is null and  obj.billBatch>='"+frombatch+
		"' and obj.billBatch<='"+tobatch+"'");
	    }else{
	    	query.append("select obj from Ticket obj where obj.payRollStatus=1 and obj.payRollBatch is null and obj.billBatch>='"+frombatch+
	    			"' and obj.billBatch<='"+tobatch+"'");
	    }
        if(!StringUtils.isEmpty(driverid)){        	
        	query.append(" and obj.driver.fullName='"+driverid+"'");
        }
        
        if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
        	query.append(" and obj.driver.fullName='"+driverid+"'");
        }
        else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
        	query.append(" and obj.driver in (").append(driverIds).append(")");
        }
       
        if(!StringUtils.isEmpty(terminal)){
            query.append(" and obj.terminal="+terminal);
        }
        if(!StringUtils.isEmpty(driversmul)){
        	query.append(" and obj.driver not in ("+driversmul+")");
        }
        
       /* if(StringUtils.isEmpty(driverid))*/
        if(StringUtils.contains(sum, "true")){
        	StringBuffer driquery=new StringBuffer("");
        	driquery.append("select DISTINCT(obj.driver.fullName) from Ticket obj where obj.payRollStatus=1 and obj.payRollBatch is null and  obj.billBatch>='"+frombatch+
   		"' and obj.billBatch<='"+tobatch+"' ");
        	
        	 if(!StringUtils.isEmpty(terminal)){
        		 driquery.append(" and obj.terminal="+terminal);
             }             
             if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
            	 driquery.append(" and obj.driver.fullName='"+driverid+"'");
             }
             else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
            	 driquery.append(" and obj.driver in (").append(driverIds).append(")");
             }             
             if(!StringUtils.isEmpty(driverid)){        	
        		 driquery.append(" and obj.driver.fullName='"+driverid+"'");
             }             
        	 if(!StringUtils.isEmpty(driversmul)){
        		 driquery.append(" and obj.driver not in ("+driversmul+")");
             }
        	driquery.append(" order by obj.driver.fullName");
        	System.out.println("******** Driver query is "+ driquery.toString());
            drivers=genericDAO.executeSimpleQuery(driquery.toString());
            Map criti = new HashMap();
           
	        for(int i=0;i<drivers.size();i++){        	
	        	String driverFullName = String.valueOf(drivers.get(i));	
	        	criti.clear();
	        	criti.put("status",1);
	        	criti.put("fullName",driverFullName);
	        	Driver driver2 = genericDAO.getByCriteria(Driver.class, criti);
           	 map.put(driver2.getFullName(),0.0);
           	 map1.put(driver2.getFullName(),0);
            }
          }
        query.append(" group by obj.origin,obj.destination");
        if(StringUtils.isEmpty(driverid)){
        	query.append(",obj.driver.fullName");
        }
        query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
        System.out.println("\n query-->"+query);
		List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
		DriverPayWrapper wrapper=new DriverPayWrapper();
		List<DriverPay> summarys=new ArrayList<DriverPay>();
		Map<String,Double> sumAmounts = new HashMap<String,Double>();
		Map<String,Integer> totalCounts = new HashMap<String,Integer>();
		wrapper.setDriverPays(summarys);
		double sumAmount=0.0;
		int totalcount=0;
		boolean expiredRate = false;
		List<String> driverNames = new ArrayList<String>();
		Map<Long,Double> driverPayRateMap = new HashMap<Long, Double>();
		
		
		for(Ticket ticket:tickets){
			
			StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+ticket.getDriver().getCompany().getId()+" and obj.terminal="+ticket.getDriver().getTerminal().getId()+" and obj.catagory="+ticket.getDriver().getCatagory().getId()+" and obj.leaveType=3");
			if(!StringUtils.isEmpty(frombatch)){
				holidayquery.append(" and obj.batchdate>='"+frombatch+"'");
			}
			if(!StringUtils.isEmpty(tobatch)){
				holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
			}			
			
			
			List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());		
			
			
			driverNames.add(ticket.getDriver().getFullName());
			
			StringBuffer countquery=new StringBuffer("");
			/*String*/ countquery.append("select count(obj) from Ticket obj where obj.payRollStatus=1 and obj.payRollBatch is null and  obj.billBatch>='"+frombatch+
		    "' and obj.billBatch<='"+tobatch+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId());
			 if(!StringUtils.isEmpty(driverid)){
				 countquery.append("and obj.driver.fullName='"+driverid+"'");
			 }
			 else{
				 countquery.append("and obj.driver.fullName='"+ticket.getDriver().getFullName()+"'");
			 }
			 if(!StringUtils.isEmpty(driversmul)){
				 countquery.append(" and obj.driver not in ("+driversmul+")");
		     }
			 Long recordCount = (Long) genericDAO.getEntityManager()
			.createQuery(countquery.toString()).getSingleResult();
			 
			 
			 StringBuffer ticketquery=new StringBuffer("");
				/*String*/ ticketquery.append("select obj from Ticket obj where obj.payRollStatus=1 and obj.payRollBatch is null and  obj.billBatch>='"+frombatch+
			    "' and obj.billBatch<='"+tobatch+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId());
				 if(!StringUtils.isEmpty(driverid)){
					 ticketquery.append("and obj.driver.fullName='"+driverid+"'");
				 }
				 else{
					 ticketquery.append("and obj.driver.fullName='"+ticket.getDriver().getFullName()+"'");
				 }
				 if(!StringUtils.isEmpty(driversmul)){
					 ticketquery.append(" and obj.driver not in ("+driversmul+")");
			     }
			 List<Ticket> filtrdtickets = genericDAO.executeSimpleQuery(ticketquery.toString());  
			 
			 int sundaycount = 0;
			 int holidaycount = 0;
			 List<String> sundayRateList = new ArrayList<String>();
			 List<String> holidayRateList = new ArrayList<String>();
			 List<String> regularRateList = new ArrayList<String>();
			 
			 Map<String,String> sundayRateMap = new HashMap<String,String>();
			 Map<String,String> holidayRateMap = new HashMap<String,String>();
			 Map<String,String> regularRateMap = new HashMap<String,String>();
			 
			 for(Ticket ticketObj : filtrdtickets) {				 
				 
				 //Newly added
				 
				 Long destination_id;
					Location location = genericDAO.getById(Location.class, ticketObj
							.getDestination().getId());
					
					int payUsing = 0;
					
					String initRateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"						
						+ ticket.getDestination().getId() 
						+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
						+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()
						+ "' order by obj.validTo";
					List<DriverPayRate>	initfs = genericDAO.executeSimpleQuery(initRateQuery);
				    if (initfs != null && initfs.size() > 0) {
				    	payUsing = initfs.get(0).getRateUsing();
				    }
				    else{
				    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
				    		String initRateQuery1 = "select obj from DriverPayRate obj where obj.transferStation='"
								+ ticket.getOrigin().getId() + "' and obj.landfill='"						
								+ 91 
								+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
								+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()
								+ "' order by obj.validTo";
				    		List<DriverPayRate>	initfs2 = genericDAO.executeSimpleQuery(initRateQuery1);
				    		if (initfs2 != null && initfs2.size() > 0) {
				    			payUsing = initfs2.get(0).getRateUsing();
				    		}				    		
				       }			       
				  }
					
					StringBuffer rateQuery = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
							+ ticketObj.getOrigin().getId() + "' and obj.landfill='"
							+ ticketObj.getDestination().getId() 
							+ "' and obj.company='"+ticketObj.getDriver().getCompany().getId()
							+ "' and obj.terminal='"+ticketObj.getDriver().getTerminal().getId()+"'");
					if(payUsing==1){
							rateQuery.append(" and obj.validFrom <='"+ticketObj.getLoadDate()
							+"' and obj.validTo >='"+ticketObj.getLoadDate()+"'");
					}
					else{
							rateQuery.append(" and obj.validFrom <='"+ticketObj.getUnloadDate()
							+"' and obj.validTo >='"+ticketObj.getUnloadDate()+"'");
					}
					
					List<DriverPayRate>	fs = new ArrayList<DriverPayRate>();
					
					List<DriverPayRate>	fs1 = genericDAO.executeSimpleQuery(rateQuery.toString());
					 
					if (fs1 != null && fs1.size() > 0) {
				    	fs = fs1;
				    }
				    else{
				    	
				    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
							StringBuffer rateQuery1 = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
								+ ticketObj.getOrigin().getId() + "' and obj.landfill='"
								+ 91 
								+ "' and obj.company='"+ticketObj.getDriver().getCompany().getId()
								+ "' and obj.terminal='"+ticketObj.getDriver().getTerminal().getId()+"'");
							if(payUsing==1){	
								rateQuery1.append(" and obj.validFrom <='"+ticketObj.getLoadDate()
								+"' and obj.validTo >='"+ticketObj.getLoadDate()+"'");
							}
							else{
								rateQuery1.append(" and obj.validFrom <='"+ticketObj.getUnloadDate()
								+"' and obj.validTo >='"+ticketObj.getUnloadDate()+"'");
							}
							
							List<DriverPayRate>	fs2 = genericDAO.executeSimpleQuery(rateQuery1.toString());
							if (fs2 != null && fs2.size() > 0) {
								fs = fs2;
							}
				    	}
				    	
				    }
					Double payrate = 0.0;
					
					if(fs!=null && fs.size()>0){
						Map criti=new HashMap();
						criti.clear();
						criti.put("status",1);
						criti.put("fullName",ticketObj.getDriver().getFullName() );
						Driver empObj=genericDAO.getByCriteria(Driver.class, criti);
						
						LocalDate dt= null;								
						if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							if(frombatch.equalsIgnoreCase(tobatch)){
								try {											
									dt = new LocalDate(frombatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
							else{
								try {
									dt = new LocalDate(tobatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
						}
						else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						boolean wbDrivers = false;
						
						if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
							if(ticket.getDriver().getDateProbationEnd()!=null){
								if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
									wbDrivers = true;
								}
							}
						}
						if(wbDrivers){
							payrate = fs.get(0).getProbationRate() ;						
						}
						else{
							if(empObj.getId()!=null){
								if(empObj.getShift().equals("1")){								
									payrate = fs.get(0).getPayRate();
								}
								else{									
									payrate = fs.get(0).getNightPayRate();
								}
							}
							else{
								payrate = fs.get(0).getPayRate();
							}
						}					
					
				 
				 //Newly added end here
				 
						System.out.println("****** pay rate value is "+payrate);
				 boolean regularSetNo = true;		
				 boolean sundaySetNo = true;
				 LocalDate unloadDate = new LocalDate(ticketObj.getUnloadDate());
				 LocalDate loadDate = new LocalDate(ticketObj.getLoadDate());
				 if(unloadDate.getDayOfWeek() == DateTimeConstants.SUNDAY){
					 payrate = payrate * fs.get(0).getSundayRateFactor();
					 String sundayKeyString = fs.get(0).getId()+"-"+payrate;
					 sundayRateList.add(sundayKeyString);
					 sundayRateMap.put(sundayKeyString,sundayKeyString);
					 sundaySetNo = false;
					 //sundaycount++;
				 }
				 else{
					 regularSetNo = false;
					 String regularKeyString = fs.get(0).getId()+"-"+payrate;
					 regularRateList.add(regularKeyString);
					 regularRateMap.put(regularKeyString,regularKeyString);
				 }
				 
				if(doublePayDriverNameList.contains(ticketObj.getDriver().getFullName())){
				 
				 for(HolidayType type:holidayTypes){
					 
					 if((ticketObj.getLoadDate().getTime() >= type.getDateFrom().getTime() && ticketObj.getLoadDate().getTime() <= type.getDateTo().getTime())  ||
							 
					    (ticketObj.getUnloadDate().getTime() >= type.getDateFrom().getTime() && ticketObj.getUnloadDate().getTime() <= type.getDateTo().getTime())){
						 
						 // 18th Jan 2017 - Double holiday pay fix
						 //payrate = payrate * 2.0;
						 
						 String holidayKeyString = fs.get(0).getId()+"-"+payrate;
						 holidayRateList.add(holidayKeyString);
						 holidayRateMap.put(holidayKeyString,holidayKeyString);
						 //holidaycount++;
						 
					}
					 else if(regularSetNo && sundaySetNo){
						 String regularKeyString = fs.get(0).getId()+"-"+payrate;
						 regularRateList.add(regularKeyString);
						 regularRateMap.put(regularKeyString,regularKeyString);
					 }
				 }
				}
				else if(regularSetNo && sundaySetNo){
					 String regularKeyString = fs.get(0).getId()+"-"+payrate;
					 regularRateList.add(regularKeyString);
					 regularRateMap.put(regularKeyString,regularKeyString);
				 }		
			}
					
					driverPayRateMap.put(ticketObj.getId(), payrate);
		}
			 
			 
			 
			 double amount=0.0;
			 
			 System.out.println("****** length of regular list  is "+regularRateList.size());
			 
			 for(String key:regularRateMap.keySet()){
					int count = 0;
					for(int i=0;i<regularRateList.size();i++){
						if(key.equals(regularRateList.get(i))){
							count++;
						}				
					}
					String[] strs = key.split("-");
					
					Double rate = Double.valueOf(strs[1]);				

					DriverPay pay=new DriverPay();
					pay.setNoOfLoad(count);
					pay.setOrigin(ticket.getOrigin().getName());
					pay.setDestination(ticket.getDestination().getName());
					if(ticket.getDriver().getCompany()!=null)
						pay.setCompanyname(ticket.getDriver().getCompany().getName());
					else
						pay.setCompanyname("");
					
					if(ticket.getDriver().getTerminal()!=null)
						pay.setTerminalname(ticket.getDriver().getTerminal().getName());
					else
						pay.setTerminalname("");			
										
					pay.setRate(rate);
					amount= count*rate;					
					
					sumAmount+=amount;
					
					amount=MathUtil.roundUp(amount, 2);
					
					pay.setAmount(amount);								
					pay.setDrivername(ticket.getDriver().getFullName());		
					
					totalcount+=pay.getNoOfLoad();			
				
					if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
						double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
						driveramount = driveramount + amount;
						sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
					}else{
						sumAmounts.put(ticket.getDriver().getFullName(),amount);
					}
					
					summarys.add(pay);					
			}
			 
			 for(String key:sundayRateMap.keySet()){
					int count = 0;
					for(int i=0;i<sundayRateList.size();i++){
						if(key.equals(sundayRateList.get(i))){
							count++;
						}					
					}					

					String[] strs = key.split("-");					
					Double rate = Double.valueOf(strs[1]);
					

					DriverPay pay=new DriverPay();					
					pay.setNoOfLoad(count);					
					pay.setOrigin(ticket.getOrigin().getName());
					pay.setDestination(ticket.getDestination().getName());
					if(ticket.getDriver().getCompany()!=null)
						pay.setCompanyname(ticket.getDriver().getCompany().getName());
					else
						pay.setCompanyname("");
					
					if(ticket.getDriver().getTerminal()!=null)
						pay.setTerminalname(ticket.getDriver().getTerminal().getName());
					else
						pay.setTerminalname("");
					
					pay.setRate(rate);
					
					amount = count * rate ;
					
					sumAmount+=amount;
					amount=MathUtil.roundUp(amount, 2);
					
					pay.setAmount(amount);					
					pay.setDrivername(ticket.getDriver().getFullName());					
					totalcount+=pay.getNoOfLoad();
					
					if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
						double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
						driveramount = driveramount + amount;
						sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
					}else{
						sumAmounts.put(ticket.getDriver().getFullName(),amount);
					}					
					summarys.add(pay);			 
			}
			 
			 for(String key:holidayRateMap.keySet()){
					int count = 0;
					for(int i=0;i<holidayRateList.size();i++){
						if(key.equals(holidayRateList.get(i))){
							count++;
						}					
					}
					String[] strs = key.split("-");
					
					Double rate = Double.valueOf(strs[1]);
					

					 
					DriverPay pay=new DriverPay();
					
					// 18th Jan 2017 - Double holiday pay fix
					//pay.setNoOfLoad(count);
					pay.setNoOfLoad(0);
					
					pay.setOrigin(ticket.getOrigin().getName());
					pay.setDestination(ticket.getDestination().getName());
					if(ticket.getDriver().getCompany()!=null)
						pay.setCompanyname(ticket.getDriver().getCompany().getName());
					else
						pay.setCompanyname("");
					
					if(ticket.getDriver().getTerminal()!=null)
						pay.setTerminalname(ticket.getDriver().getTerminal().getName());
					else
						pay.setTerminalname("");
					
					
					pay.setRate(rate);
					
					amount =  count*rate;
					sumAmount+=amount;
					amount=MathUtil.roundUp(amount, 2);
					
					pay.setAmount(amount);					
					pay.setDrivername(ticket.getDriver().getFullName());
					
					totalcount+=pay.getNoOfLoad();
					
					if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
						double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
						driveramount = driveramount + amount;
						sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
					}else{
						sumAmounts.put(ticket.getDriver().getFullName(),amount);
					}	
					
					summarys.add(pay);					
			}		 
			
			
			/*if(sundaycount > 0){
				DriverPay pay=new DriverPay();
				int diffCount = Integer.parseInt(recordCount.toString()) - sundaycount;
				pay.setNoOfLoad(sundaycount);
				if(diffCount == 0){
					calculateOtherRow = false;
				}
				pay.setOrigin(ticket.getOrigin().getName());
				pay.setDestination(ticket.getDestination().getName());
				if(ticket.getCompanyLocation()!=null)
					pay.setCompanyname(ticket.getCompanyLocation().getName());
				else
					pay.setCompanyname("");
				
				if(ticket.getDriver().getTerminal()!=null)
					pay.setTerminalname(ticket.getDriver().getTerminal().getName());
				else
					pay.setTerminalname("");
				
				try{
					Long destination_id;
					Location location = genericDAO.getById(Location.class, ticket
							.getDestination().getId());
					
					String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
							+ ticket.getOrigin().getId() + "' and obj.landfill='"
							+ ticket.getDestination().getId() 
							+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
							+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()
							+"' and obj.validFrom <='"+ticket.getLoadDate()
							+"' and obj.validTo >='"+ticket.getLoadDate()
							+"' and obj.validFrom <='"+ticket.getUnloadDate()
							+"' and obj.validTo >='"+ticket.getUnloadDate()+"'";
					
					List<DriverPayRate>	fs = new ArrayList<DriverPayRate>();
					
					List<DriverPayRate>	fs1 = genericDAO.executeSimpleQuery(rateQuery);
					 
					if (fs1 != null && fs1.size() > 0) {
				    	fs = fs1;
				    }
				    else{
				    	
				    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
							String rateQuery1 = "select obj from DriverPayRate obj where obj.transferStation='"
								+ ticket.getOrigin().getId() + "' and obj.landfill='"
								+ 91 
								+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
								+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()
								+"' and obj.validFrom <='"+ticket.getLoadDate()
								+"' and obj.validTo >='"+ticket.getLoadDate()
								+"' and obj.validFrom <='"+ticket.getUnloadDate()
								+"' and obj.validTo >='"+ticket.getUnloadDate()+"'";
							
							List<DriverPayRate>	fs2 = genericDAO.executeSimpleQuery(rateQuery);
							
							fs = fs2;
				    	}
				    	
				    }
					
					
					
				//List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
				DriverPayRate payRate = null;
				if (fs != null && fs.size() > 0) {
					for (DriverPayRate rate : fs) {
						if (rate.getRateUsing() == null) {
							payRate = rate;
							break;
						} else if (rate.getRateUsing() == 1) {
							// calculation for a load date
							if ((ticket.getLoadDate().getTime() >= rate
									.getValidFrom().getTime())
									&& (ticket.getLoadDate().getTime() <= rate
											.getValidTo().getTime())) {
								payRate = rate;
								break;
							}
						} else if (rate.getRateUsing() == 2) {
							// calculation for a unload date
							if ((ticket.getUnloadDate().getTime() >= rate
									.getValidFrom().getTime())
									&& (ticket.getUnloadDate().getTime() <= rate
											.getValidTo().getTime())) {
								payRate = rate;
								break;
							}
						}
					}
					}
				if(payRate==null){
					pay.setAmount(0.0);
					sumAmount+=0.0;
					if(StringUtils.contains(expire, "1")){
					if (!expiredRate)
						str.add("<u>Rates Are Expired or not Available</u></br>");
					expiredRate = true;
					errorCount++;
					Location originName = genericDAO.getById(Location.class,
							ticket.getOrigin().getId());
					Location DestinationName = genericDAO.getById(
							Location.class, ticket.getDestination().getId());
					boolean cont = str.contains((originName.getName() + " - "
							+ DestinationName.getName() + "</br>"));
					String string = (originName.getName() + " - "
							+ DestinationName.getName() + "</br>");

					if (!cont)
						str.add(string);
					}
				
				}else{
					
					Map criti=new HashMap();
					criti.clear();
					criti.put("status",1);
					criti.put("fullName",ticket.getDriver().getFullName() );
					Driver empObj=genericDAO.getByCriteria(Driver.class, criti);					
					double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
					
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					boolean wbDrivers = false;
					
					if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
						if(ticket.getDriver().getDateProbationEnd()!=null){
							if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
								wbDrivers = true;
							}
						}
					}
					if(wbDrivers){
						if(sundaycount > 0){
							double sundayRate = payRate.getProbationRate() ;
							sundayAmount = sundaycount * sundayRate;
							pay.setRate(payRate.getProbationRate());
						}
					}
					else{
						if(empObj.getId()!=null){
							if(empObj.getShift().equals("1")){							
								if(sundaycount > 0){
									double sundayRate = payRate.getPayRate() * payRate.getSundayRateFactor();
									sundayAmount = sundaycount * sundayRate;
									pay.setRate(payRate.getPayRate() * payRate.getSundayRateFactor());
								}						
							}
							else{
								if(sundaycount > 0){
									double sundayRate = payRate.getNightPayRate() * payRate.getSundayRateFactor();
									sundayAmount = sundaycount * sundayRate;
									pay.setRate(payRate.getNightPayRate() * payRate.getSundayRateFactor());
								}						
							}
						}
						else{
							if(sundaycount > 0){
								double sundayRate = payRate.getPayRate() * payRate.getSundayRateFactor();
								sundayAmount = sundaycount * sundayRate;
								pay.setRate(payRate.getPayRate() * payRate.getSundayRateFactor());
							}					
						}
					}
					amount =  sundayAmount;
					sumAmount+=amount;
					amount=MathUtil.roundUp(amount, 2);
					pay.setAmount(amount);
					//pay.setRate(payRate.getPayRate());
				}
				pay.setNoOfLoad(sundaycount);
				//if(StringUtils.isEmpty(driverid))
				pay.setDrivername(ticket.getDriver().getFullName());
				//pay.setCompanyname(ticket.getDriver().getCompany().getName());
				totalcount+=pay.getNoOfLoad();
				
				if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
					double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
					driveramount = driveramount + amount;
					sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
				}else{
					sumAmounts.put(ticket.getDriver().getFullName(),amount);
				}
				
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				summarys.add(pay);
				
			 }*/
			
			
			
			/*double holidaydoublepayAmount = 0.0;
			
			if (holidaycount > 0){
				 
					DriverPay pay=new DriverPay();
					int diffCount = Integer.parseInt(recordCount.toString())-sundaycount-holidaycount;
					pay.setNoOfLoad(holidaycount);
					if(diffCount == 0){
						calculateOtherRow = false;
					}
					pay.setOrigin(ticket.getOrigin().getName());
					pay.setDestination(ticket.getDestination().getName());
					if(ticket.getCompanyLocation()!=null)
						pay.setCompanyname(ticket.getCompanyLocation().getName());
					else
						pay.setCompanyname("");
					
					if(ticket.getDriver().getTerminal()!=null)
						pay.setTerminalname(ticket.getDriver().getTerminal().getName());
					else
						pay.setTerminalname("");
					
					try{
						Long destination_id;
						Location location = genericDAO.getById(Location.class, ticket
								.getDestination().getId());
						if (location.getName().equalsIgnoreCase("grows")
								|| location.getName().equalsIgnoreCase("tullytown")) {
		                        destination_id = 91l;
		               } else {
							destination_id = ticket.getDestination().getId();
						}
						String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
								+ ticket.getOrigin().getId() + "' and obj.landfill='"
								 + ticket.getDestination().getId() + "'"; 
								+ destination_id 
								+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
								+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'"; 
						
						 
						 
					List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
					DriverPayRate payRate = null;
					if (fs != null && fs.size() > 0) {
						for (DriverPayRate rate : fs) {
							if (rate.getRateUsing() == null) {
								payRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
								// calculation for a load date
								if ((ticket.getLoadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getLoadDate().getTime() <= rate
												.getValidTo().getTime())) {
									payRate = rate;
									break;
								}
							} else if (rate.getRateUsing() == 2) {
								// calculation for a unload date
								if ((ticket.getUnloadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getUnloadDate().getTime() <= rate
												.getValidTo().getTime())) {
									payRate = rate;
									break;
								}
							}
						}
						}
					if(payRate==null){
						pay.setAmount(0.0);
						sumAmount+=0.0;
						if(StringUtils.contains(expire, "1")){
						if (!expiredRate)
							str.add("<u>Rates Are Expired or not Available</u></br>");
						expiredRate = true;
						errorCount++;
						Location originName = genericDAO.getById(Location.class,
								ticket.getOrigin().getId());
						Location DestinationName = genericDAO.getById(
								Location.class, ticket.getDestination().getId());
						boolean cont = str.contains((originName.getName() + " - "
								+ DestinationName.getName() + "</br>"));
						String string = (originName.getName() + " - "
								+ DestinationName.getName() + "</br>");

						if (!cont)
							str.add(string);
						}
					
					}else{
						
						Map criti=new HashMap();
						criti.clear();
						criti.put("status",1);
						criti.put("fullName",ticket.getDriver().getFullName() );
						Driver empObj=genericDAO.getByCriteria(Driver.class, criti);					
						double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
						
						
						LocalDate dt= null;								
						if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							if(frombatch.equalsIgnoreCase(tobatch)){
								try {											
									dt = new LocalDate(frombatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
							else{
								try {
									dt = new LocalDate(tobatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
						}
						else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						boolean wbDrivers = false;
						
						if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
							if(ticket.getDriver().getDateProbationEnd()!=null){
								if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
									wbDrivers = true;
								}
							}
						}
						
						
						
						
						if(wbDrivers){
							if(holidaycount > 0){
								double holidayDoublePayRate = payRate.getProbationRate();
								holidaydoublepayAmount = holidaycount * holidayDoublePayRate;
								pay.setRate(payRate.getProbationRate());
							}
						}
						else{
							if(empObj.getId()!=null){
								if(empObj.getShift().equals("1")){							
									if(holidaycount > 0){
										double holidayDoublePayRate = payRate.getPayRate() * 2.0;
										holidaydoublepayAmount = holidaycount * holidayDoublePayRate;
										pay.setRate(payRate.getPayRate() * 2.0);
									}						
								}
								else{
									if(holidaycount > 0){
										double holidayDoublePayRate = payRate.getNightPayRate() * 2.0;
										holidaydoublepayAmount = holidaycount * holidayDoublePayRate;
										pay.setRate(payRate.getNightPayRate() * 2.0);
									}						
								}
							}
							else{
								if(holidaycount > 0){
									double holidayDoublePayRate = payRate.getPayRate() * 2.0;
									holidaydoublepayAmount = holidaycount * holidayDoublePayRate;
									pay.setRate(payRate.getPayRate() * 2.0);
								}					
							}
						}
						amount =  holidaydoublepayAmount;
						sumAmount+=amount;
						amount=MathUtil.roundUp(amount, 2);
						pay.setAmount(amount);
						//pay.setRate(payRate.getPayRate());
					}
					pay.setNoOfLoad(holidaycount);
					//if(StringUtils.isEmpty(driverid))
					pay.setDrivername(ticket.getDriver().getFullName());
					//pay.setCompanyname(ticket.getDriver().getCompany().getName());
					totalcount+=pay.getNoOfLoad();
					
					if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
						double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
						driveramount = driveramount + amount;
						sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
					}else{
						sumAmounts.put(ticket.getDriver().getFullName(),amount);
					}
					
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
					summarys.add(pay);					
				 
			 }*/
			
			
			
				
			/*if(calculateOtherRow){
				DriverPay pay=new DriverPay();
				pay.setNoOfLoad(Integer.parseInt(recordCount.toString())-sundaycount-holidaycount);
				pay.setOrigin(ticket.getOrigin().getName());
				pay.setDestination(ticket.getDestination().getName());
				if(ticket.getCompanyLocation()!=null)
					pay.setCompanyname(ticket.getCompanyLocation().getName());
				else
					pay.setCompanyname("");
				
				if(ticket.getDriver().getTerminal()!=null)
					pay.setTerminalname(ticket.getDriver().getTerminal().getName());
				else
					pay.setTerminalname("");
				
				try{
					Long destination_id;
					Location location = genericDAO.getById(Location.class, ticket
						.getDestination().getId());
					if (location.getName().equalsIgnoreCase("grows")
						|| location.getName().equalsIgnoreCase("tullytown")) {
                        destination_id = 91l;
					} else {
						destination_id = ticket.getDestination().getId();
					}
					String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"
						 + ticket.getDestination().getId() + "'"; 
						+ destination_id 
						+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
						+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'";
					
					
					
					
					List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
					DriverPayRate payRate = null;
					if (fs != null && fs.size() > 0) {
						for (DriverPayRate rate : fs) {
							if (rate.getRateUsing() == null) {
								payRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
						// calculation for a load date
						if ((ticket.getLoadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getLoadDate().getTime() <= rate
										.getValidTo().getTime())) {
							payRate = rate;
							break;
						}
					} else if (rate.getRateUsing() == 2) {
						// calculation for a unload date
						if ((ticket.getUnloadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getUnloadDate().getTime() <= rate
										.getValidTo().getTime())) {
							payRate = rate;
							break;
						}
					}
				}
				}
			if(payRate==null){
				pay.setAmount(0.0);
				sumAmount+=0.0;
				if(StringUtils.contains(expire, "1")){
				if (!expiredRate)
					str.add("<u>Rates Are Expired or not Available</u></br>");
				expiredRate = true;
				errorCount++;
				Location originName = genericDAO.getById(Location.class,
						ticket.getOrigin().getId());
				Location DestinationName = genericDAO.getById(
						Location.class, ticket.getDestination().getId());
				boolean cont = str.contains((originName.getName() + " - "
						+ DestinationName.getName() + "</br>"));
				String string = (originName.getName() + " - "
						+ DestinationName.getName() + "</br>");

				if (!cont)
					str.add(string);
				}
			
			}else{
				
				Map criti=new HashMap();
				criti.clear();
				criti.put("status",1);
				criti.put("fullName",ticket.getDriver().getFullName() );
				Driver empObj=genericDAO.getByCriteria(Driver.class, criti);
				
				double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
				
				
				LocalDate dt= null;								
				if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
					if(frombatch.equalsIgnoreCase(tobatch)){
						try {											
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else{
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
				}
				else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
					try {
						dt = new LocalDate(frombatch);
					} catch (Exception e) {
						System.out.println("Error Parsing Date");
					}
				}
				else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
					try {
						dt = new LocalDate(tobatch);
					} catch (Exception e) {
						System.out.println("Error Parsing Date");
					}
				}
				boolean wbDrivers = false;
				
				if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
					if(ticket.getDriver().getDateProbationEnd()!=null){
						if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
							wbDrivers = true;
						}
					}
				}
				
				
				
				if(wbDrivers){					
						pay.setRate(payRate.getProbationRate());
						amount= nofload*payRate.getProbationRate();					
				}
				else{
					if(empObj.getId()!=null){
						if(empObj.getShift().equals("1")){						
							pay.setRate(payRate.getPayRate());						
							amount=nofload*payRate.getPayRate();
						}
						else{						
							pay.setRate(payRate.getNightPayRate());						
							amount= nofload*payRate.getNightPayRate();						
						}
					}
					else{
						pay.setRate(payRate.getPayRate());
						amount= nofload*payRate.getPayRate();					
					}	
				}
				sumAmount+=amount;
				amount=MathUtil.roundUp(amount, 2);
				pay.setAmount(amount);
				//pay.setRate(payRate.getPayRate());
			}
			pay.setNoOfLoad(pay.getNoOfLoad());
			//if(StringUtils.isEmpty(driverid))
			pay.setDrivername(ticket.getDriver().getFullName());
			//pay.setCompanyname(ticket.getDriver().getCompany().getName());
			totalcount+=pay.getNoOfLoad();			
			
			if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
				double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
				driveramount = driveramount + amount;
				sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
			}else{
				sumAmounts.put(ticket.getDriver().getFullName(),amount);
			}
			
			
		
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			summarys.add(pay);
		}*/
		}
		
		wrapper.setDriverNames(driverNames);
		wrapper.setSumAmountsMap(sumAmounts);
		sumAmount=MathUtil.roundUp(sumAmount, 2);
		wrapper.setTotalRowCount(totalcount);
		wrapper.setSumTotal(sumAmount);
		wrapper.setBatchDateFrom((String) criteria.getSearchMap().get("fromDate"));
		wrapper.setBatchDateTo((String) criteria.getSearchMap().get("toDate"));
		wrapper.setPayRollBatch((String) criteria.getSearchMap().get("payrollDate"));
		if(!StringUtils.isEmpty(company)){
		wrapper.setCompany(companylocation.getName());
		wrapper.setCompanylocation(companylocation);
		}
		if(!StringUtils.isEmpty(terminal)){
		wrapper.setTerminal(terminallocation);
		}
		if(driver!=null){
		wrapper.setDriver(driver.getFullName());
		wrapper.setCompany(driver.getCompany().getName());
		}
		/*if(StringUtils.isEmpty(driverid))*/
		
		String tempDriverName = ""; 
		Integer loadCount=0;
		int currentcount=1;
		int recordCount = wrapper.getDriverPays().size();
		if(!StringUtils.contains(sum, "true")){
			for(DriverPay driverPay:wrapper.getDriverPays()){				
				
				if(tempDriverName.equals("")){					
	        		tempDriverName = driverPay.getDrivername();
	        		loadCount=loadCount+driverPay.getNoOfLoad();
				}
				else{					
					if(tempDriverName.equals(driverPay.getDrivername())){
						loadCount=loadCount+driverPay.getNoOfLoad();
						if(currentcount==recordCount)
							totalCounts.put(driverPay.getDrivername(),loadCount);
					}
					else{						
						totalCounts.put(tempDriverName,loadCount);
						loadCount=0;
						tempDriverName = driverPay.getDrivername();
						loadCount=loadCount+driverPay.getNoOfLoad();
					}
				}
				
				currentcount++;
			}			
			wrapper.setTotolcounts(totalCounts);
		}
		
		
		
		
		if(StringUtils.contains(sum, "true")){
			for(DriverPay driverPay:wrapper.getDriverPays()){
				Double amount=map.get(driverPay.getDrivername());
				if(amount==null){
				amount=0.0;	
				}
				Double totamount=amount+driverPay.getAmount();
				Integer count=map1.get(driverPay.getDrivername());
				if(count==null){
					count=0;
				}	
				Integer totcount=count+driverPay.getNoOfLoad();
				map1.put(driverPay.getDrivername(), totcount);
				map.put(driverPay.getDrivername(), totamount);
			}
			wrapper.getDriverPays().clear();
		}
		/*if(StringUtils.isEmpty(driverid))*/
		if(StringUtils.contains(sum, "true")){
		List<DriverPay> fields=new ArrayList<DriverPay>();
		wrapper.setDriverPays(fields);
		/*if(StringUtils.isEmpty(driverid))*/
		
		Map empmap=new HashMap();
		double TotalAmount=0.0;
		
		
		Map criti = new HashMap();
		String drivernames = "";
        for(int i=0;i<drivers.size();i++){		        	
        	String driverFullName = String.valueOf(drivers.get(i));	
        	if(drivernames.equals("")){
        		drivernames ="'"+String.valueOf(drivers.get(i))+"'";	
        	}
        	else{
        		drivernames = drivernames+",'"+ String.valueOf(drivers.get(i))+"'";
        	}
        	criti.clear();
        	criti.put("status",1);
        	criti.put("fullName",driverFullName);
        	Driver driver3 = genericDAO.getByCriteria(Driver.class, criti);
       	
        		Double deductionAmount = 0.0;
				Double sickParsonalAmount=0.0;
				Double vacationAmount=0.0;
				// Bereavement change - driver
				Double bereavementAmount=0.0;
				Double bonusAmount=0.0;
				Double miscAmount=0.0;
				Double holidayAmount=0.0;
				DriverPay pay=new DriverPay();
				pay.setDrivername(driver3.getFullName());
				pay.setCompanyname(driver3.getCompany().getName());
				pay.setTerminalname(driver3.getTerminal().getName());
				pay.setTerminal(driver3.getTerminal());
				Double amount=0.0;
				amount=(Double)(map.get(driver3.getFullName()));				
				empmap.clear();
				empmap.put("fullName", driver3.getFullName());
				empmap.put("status",1);
				Driver employee=genericDAO.getByCriteria(Driver.class, empmap);				
				Double miscamt=0.0;	
				Double reimburseAmount=0.0;	
				Double quatarAmount=0.0;	
				if(employee!=null){
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and  obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes!='Reimbursement' and obj.miscType!='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}			
				List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
				for(MiscellaneousAmount miscamnt:miscamounts){
					miscamt+=miscamnt.getMisamount();					
				}	
				
				
				StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes='Reimbursement'");
				if(!StringUtils.isEmpty(frombatch)){
					reimburseamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					reimburseamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}			
				List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:reimburseamounts){
					reimburseAmount+=reimbursecamnt.getMisamount();					
				}
				
				///*********************** Newly Added
				StringBuffer qutaramountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					qutaramountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					qutaramountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}			
				List<MiscellaneousAmount> qutaramounts=genericDAO.executeSimpleQuery(qutaramountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:qutaramounts){
					quatarAmount+=reimbursecamnt.getMisamount();					
				}
				
				//*************************
				
				
			    }
				
				amount=MathUtil.roundUp(amount, 2);
				miscamt=MathUtil.roundUp(miscamt, 2);
				pay.setTransportationAmount(amount);
				//amount=amount+miscamt;				
				//amount=MathUtil.roundUp(amount, 2);
				//pay.setAmount(amount);
				pay.setMiscAmount(miscamt);
				pay.setReimburseAmount(reimburseAmount);
				pay.setQuatarAmount(quatarAmount);
				
				pay.setNoOfLoad(map1.get(driver3.getFullName()));
				/*empmap.clear();
				empmap.put("fullName", driver3.getFullName());
				empmap.put("status",1);
				Driver employee=genericDAO.getByCriteria(Driver.class, empmap);*/
				if(employee!=null){
					StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus!=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
					    ptodquery.append(" and obj.batchdate>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						ptodquery.append(" and obj.batchdate<='"+tobatch+"'");
					}
					List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
					for(Ptodapplication ptodapplication:ptodapplications){
						if(ptodapplication.getLeavetype().getId()==1){
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
						}
						// Jury duty fix - driver - 3rd Nov 2016
						if(ptodapplication.getLeavetype().getId()==9){
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
						}
						if(ptodapplication.getLeavetype().getId()==4){
							vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
							
						}
						// Bereavement change - driver
						if(ptodapplication.getLeavetype().getId() == 8) {
							bereavementAmount = bereavementAmount + ptodapplication.getSequenceAmt1();
						}
					}
					
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}				
					
					if(employee.getCompany().getId()!=4l || employee.getTerminal().getId()!=93l){
						if(employee.getDateProbationEnd()!=null){
							if(new LocalDate(employee.getDateProbationEnd()).isAfter(dt) || new LocalDate(employee.getDateProbationEnd()).isEqual(dt) ){
								double miscAndLoadAmt = pay.getTransportationAmount();											
								deductionAmount =  miscAndLoadAmt * 0.10;
								if(deductionAmount > 100.0){
									deductionAmount = 100.0;
								}
							}	
						}
					}
					deductionAmount=MathUtil.roundUp(deductionAmount, 2);
					
					StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
					}
						List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
						for(EmployeeBonus bonus:bonuses){
							for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
								bonusAmount+=list.getBonusamount();
								//miscAmount+=list.getMisamount();
							}
						}
						
					
						if(holidayExceptionDriverNameList.contains(employee.getFullName()))	{
							  holidayAmount=0.0;
						 }
						 else {		
						
							 StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus!=2 and obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
							 if(!StringUtils.isEmpty(frombatch)){
								 holidayquery.append(" and obj.batchdate>='"+frombatch+"'");
							 }
							 if(!StringUtils.isEmpty(tobatch)){
								 holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
							 }
							 List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
							 for(HolidayType type:holidayTypes){
								 holidayAmount=holidayAmount+type.getAmount();
							 }
						}
				}
				
				amount=pay.getTransportationAmount()-deductionAmount;				
				amount=MathUtil.roundUp(amount, 2);
				pay.setAmount(amount);
				
				pay.setProbationDeductionAmount(deductionAmount);
				pay.setBonusAmount(bonusAmount);
				pay.setSickPersonalAmount(sickParsonalAmount);
				pay.setVacationAmount(vacationAmount);
				// Bereavement change - driver
				pay.setBereavementAmount(bereavementAmount);
				pay.setHolidayAmount(holidayAmount);
				Double totalAmount=(pay.getTransportationAmount()-pay.getProbationDeductionAmount())+pay.getMiscAmount()+pay.getSickPersonalAmount()+pay.getBonusAmount()+pay.getHolidayAmount();
				totalAmount=MathUtil.roundUp(totalAmount, 2);
				TotalAmount+=totalAmount;
				pay.setTotalAmount(totalAmount);
				fields.add(pay);
			}
		
        
        
        //******************************************************************
        List<Driver> driverWithOutTickets = null;
        if(!drivernames.equals("") && StringUtils.isEmpty(driverid)){
        	StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and  obj.fullName not in ("+drivernames+")");
        	if(!StringUtils.isEmpty(company)){
        		drivernameauery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		drivernameauery.append(" and obj.terminal="+terminal);
             }
        	
        	 driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
        }
        else if(drivernames.equals("") && !StringUtils.isEmpty(driverid)){
        	StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and  obj.fullName in ('"+driverid+"')");
        	if(!StringUtils.isEmpty(company)){
        		drivernameauery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		drivernameauery.append(" and obj.terminal="+terminal);
             }       	
        	
        	 driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
        }
        else if(drivernames.equals("") && StringUtils.isEmpty(driverid)){
        	StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1'");
        	if(!StringUtils.isEmpty(company)){
        		drivernameauery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		drivernameauery.append(" and obj.terminal="+terminal);
             }       	
        	
        	 driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
        }
       
        if(driverWithOutTickets!=null){
        for(Driver driverWithOutTicket: driverWithOutTickets){        
       	
        		boolean setDriver = false;
        		Double deductionAmount = 0.0;
				Double sickParsonalAmount=0.0;
				Double vacationAmount=0.0;
				Double bonusAmount=0.0;
				Double miscAmount=0.0;
				Double holidayAmount=0.0;
				// Bereavement change - driver
				Double bereavementAmount=0.0;
				DriverPay pay=new DriverPay();
				pay.setDrivername(driverWithOutTicket.getFullName());
				if(driverWithOutTicket.getCompany()!=null)
					pay.setCompanyname(driverWithOutTicket.getCompany().getName());
				else
					pay.setCompanyname("");
				
				pay.setTerminalname(driverWithOutTicket.getTerminal().getName());
				pay.setTerminal(driverWithOutTicket.getTerminal());
				Double amount=0.0;
				//amount=(Double)(map.get(driverWithOutTicket.getFullName()));				
				
				Driver employee= driverWithOutTicket;				
				Double miscamt=0.0;	
				Double reimburseAmount=0.0;	
				Double quatarAmount=0.0;	
				if(employee!=null){
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes!='Reimbursement' and obj.miscType!='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}			
				List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
				for(MiscellaneousAmount miscamnt:miscamounts){
					setDriver = true;
					miscamt+=miscamnt.getMisamount();					
				}	
				
				
				///*************** Newly Added 
				StringBuffer quataramountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					quataramountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					quataramountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}			
				List<MiscellaneousAmount> quataramounts=genericDAO.executeSimpleQuery(quataramountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:quataramounts){
					setDriver = true;
					 quatarAmount+=reimbursecamnt.getMisamount();					
				}
				
				//******************
				
				
				StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes='Reimbursement'");
				if(!StringUtils.isEmpty(frombatch)){
					reimburseamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					reimburseamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}			
				List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:reimburseamounts){
					setDriver = true;
					reimburseAmount+=reimbursecamnt.getMisamount();					
				}
				
			    }
				amount=MathUtil.roundUp(amount, 2);
				pay.setTransportationAmount(amount);
				
				miscamt=MathUtil.roundUp(miscamt, 2);
				
				//amount=amount+miscamt;					
				//amount=MathUtil.roundUp(amount, 2);
				
				pay.setAmount(amount);
				
				pay.setMiscAmount(miscamt);
				pay.setReimburseAmount(reimburseAmount);
				pay.setQuatarAmount(quatarAmount);
				pay.setNoOfLoad(0);
				
				Driver employees=driverWithOutTicket;
				if(employees!=null){
					StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus!=2 and obj.approvestatus=1 and obj.driver.fullName='"+employees.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
					    ptodquery.append(" and obj.batchdate>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						ptodquery.append(" and obj.batchdate<='"+tobatch+"'");
					}
					List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
					for(Ptodapplication ptodapplication:ptodapplications){
						if(ptodapplication.getLeavetype().getId()==1){
							setDriver = true;
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
						}
						// Jury duty fix - driver - 3rd Nov 2016
						if(ptodapplication.getLeavetype().getId()==9){
							setDriver = true;
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
						}
						if(ptodapplication.getLeavetype().getId()==4){
							setDriver = true;
							vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
							
						}
						// Bereavement change - driver
						if(ptodapplication.getLeavetype().getId() == 8) {
							setDriver = true;
							bereavementAmount = bereavementAmount + ptodapplication.getSequenceAmt1();
						}
					}
					
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}				
					
					
					if(employee.getDateProbationEnd()!=null){
						if(new LocalDate(employee.getDateProbationEnd()).isAfter(dt)){
							
						}
						else{
							
						}	
					}
					
					
					StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus!=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
					}
						List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
						for(EmployeeBonus bonus:bonuses){
							for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
								setDriver = true;
								bonusAmount+=list.getBonusamount();
								//miscAmount+=list.getMisamount();
							}
						}
						
					if(holidayExceptionDriverNameList.contains(employee.getFullName()))	{
						  holidayAmount=0.0;
					}
					else {		
						
						StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus!=2 and obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
						if(!StringUtils.isEmpty(frombatch)){
							holidayquery.append(" and obj.batchdate>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
						}
						List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
						for(HolidayType type:holidayTypes){
							setDriver = true;
							holidayAmount=holidayAmount+type.getAmount();
						}
					}
				}
				
				pay.setProbationDeductionAmount(0.0);
				pay.setBonusAmount(bonusAmount);
				pay.setSickPersonalAmount(sickParsonalAmount);
				pay.setVacationAmount(vacationAmount);
				pay.setHolidayAmount(holidayAmount);
				// Bereavement change - driver
				pay.setBereavementAmount(bereavementAmount);
				Double totalAmount=(pay.getTransportationAmount()-pay.getProbationDeductionAmount())+pay.getMiscAmount()+pay.getSickPersonalAmount()+pay.getBonusAmount()+pay.getHolidayAmount();
				totalAmount=MathUtil.roundUp(totalAmount, 2);
				
				
				if(setDriver) {
					TotalAmount+=totalAmount;
					pay.setTotalAmount(totalAmount);
					fields.add(pay);			
				}
        	
        }
		}
        //************************************************************
		wrapper.setSumAmount(TotalAmount);
		
		
		
		}
		wrapper.setList(str);
		
		wrapper.setDriverPayRateDataMap(driverPayRateMap);
		
		
		return wrapper;
	}

	
	///***********************************************************////
	///***********************************************************////
	///***********************************************************////
	@Override
	public DriverPayWrapper generateDriverPayReport2(SearchCriteria criteria) {

		// TODO Auto-generated method stub
		Map criterias = new HashMap();
		List<String> str = new ArrayList<String>();
		int errorCount = 0;
		Map<String, Double> map=new HashMap<String, Double>();
		Map<String, Integer> map1=new HashMap<String, Integer>();
		String payrollDate=(String) criteria.getSearchMap().get("payrollDate");
		String driverid=(String) criteria.getSearchMap().get("driver");
		String frombatch=(String) criteria.getSearchMap().get("fromDate");
		String tobatch=(String) criteria.getSearchMap().get("toDate");
		String company=(String) criteria.getSearchMap().get("company");
		String terminal=(String) criteria.getSearchMap().get("terminal");
		 String expire= (String) criteria.getSearchMap().get("expire");
		 String status=(String)criteria.getSearchMap().get("pay");
		 String sta=(String)criteria.getSearchMap().get("stat");
		 String driversmul=(String)criteria.getSearchMap().get("driversmul");
		 if(!StringUtils.isEmpty(status)){
			 status="1";
		 }else{
			 status="2";
		 }
		 status="1";
		frombatch=ReportDateUtil.getFromDate(frombatch);
		tobatch=ReportDateUtil.getFromDate(tobatch);		
		
		payrollDate=ReportDateUtil.getFromDate(payrollDate);
		 String sum= (String) criteria.getSearchMap().get("summary");
		 Location companylocation=null;
		 Location terminallocation=null;
		 String driverIds="";
		 if(!StringUtils.isEmpty(company)){
		    companylocation=genericDAO.getById(Location.class, Long.parseLong(company));
		 }
		 if(!StringUtils.isEmpty(terminal)){
			    terminallocation=genericDAO.getById(Location.class, Long.parseLong(terminal));
			}
		 
		 if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
			 Map drivercriteria = new HashMap();
			 drivercriteria.clear();
			 drivercriteria.put("status",1);
			 drivercriteria.put("company.id",companylocation.getId());
			 List<Driver> drivers = genericDAO.findByCriteria(Driver.class, drivercriteria);
			 for(Driver driverObj:drivers){
				 if(driverIds.equals(""))
					 driverIds = driverObj.getId().toString();
				else
					driverIds = driverIds +","+driverObj.getId().toString();
			 }
		}
		 
		Driver driver=null;
		List<Driver> drivers=null;
		/*if(!StringUtils.isEmpty(driverid)){
		Driver employee= genericDAO.getById(Driver.class,Long.parseLong(driverid));
		criterias.put("fullName",employee.getFullName());
	    Driver driver=genericDAO.getByCriteria(Driver.class, criterias);
		}*/
	    StringBuffer query=new StringBuffer("");
	    if(StringUtils.isEmpty(sta)){
	    	query.append("select obj from Ticket obj where obj.payRollStatus=2 and obj.payRollBatch is not null and  obj.billBatch>='"+frombatch+
		"' and obj.billBatch<='"+tobatch+"' and obj.payRollBatch='"+payrollDate+"'");
	    }else{
	    	query.append("select obj from Ticket obj where obj.payRollStatus=2 and obj.payRollBatch is not null and obj.billBatch>='"+frombatch+
	    			"' and obj.billBatch<='"+tobatch+"' and obj.payRollBatch='"+payrollDate+"'");
	    }
        if(!StringUtils.isEmpty(driverid)){        	
        	query.append(" and obj.driver.fullName='"+driverid+"'");
        }
        
        if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
        	query.append(" and obj.driver.fullName='"+driverid+"'");
        }
        else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
        	query.append(" and obj.driver in (").append(driverIds).append(")");
        }
       
        if(!StringUtils.isEmpty(terminal)){
            query.append(" and obj.terminal="+terminal);
        }
        if(!StringUtils.isEmpty(driversmul)){
        	query.append(" and obj.driver not in ("+driversmul+")");
        }
        
       /* if(StringUtils.isEmpty(driverid))*/
        if(StringUtils.contains(sum, "true")){
        	StringBuffer driquery=new StringBuffer("");
        	driquery.append("select DISTINCT(obj.driver.fullName) from Ticket obj where obj.payRollStatus=2 and obj.payRollBatch is not null and  obj.billBatch>='"+frombatch+
   		"' and obj.billBatch<='"+tobatch+"' and obj.payRollBatch='"+payrollDate+"'");
        	
        	 if(!StringUtils.isEmpty(terminal)){
        		 driquery.append(" and obj.terminal="+terminal);
             }             
             if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
            	 driquery.append(" and obj.driver.fullName='"+driverid+"'");
             }
             else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
            	 driquery.append(" and obj.driver in (").append(driverIds).append(")");
             }             
             if(!StringUtils.isEmpty(driverid)){        	
        		 driquery.append(" and obj.driver.fullName='"+driverid+"'");
             }             
        	 if(!StringUtils.isEmpty(driversmul)){
        		 driquery.append(" and obj.driver not in ("+driversmul+")");
             }
        	driquery.append(" order by obj.driver.fullName");
        	System.out.println("******** Driver query is "+ driquery.toString());
            drivers=genericDAO.executeSimpleQuery(driquery.toString());
            Map criti = new HashMap();
           
	        for(int i=0;i<drivers.size();i++){        	
	        	String driverFullName = String.valueOf(drivers.get(i));	
	        	criti.clear();
	        	criti.put("status",1);
	        	criti.put("fullName",driverFullName);
	        	Driver driver2 = genericDAO.getByCriteria(Driver.class, criti);
           	 map.put(driver2.getFullName(),0.0);
           	 map1.put(driver2.getFullName(),0);
            }
          }
        query.append(" group by obj.origin,obj.destination");
        if(StringUtils.isEmpty(driverid)){
        	query.append(",obj.driver.fullName");
        }
        query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
        System.out.println("\n query-->"+query);
		List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
		DriverPayWrapper wrapper=new DriverPayWrapper();
		List<DriverPay> summarys=new ArrayList<DriverPay>();
		Map<String,Double> sumAmounts = new HashMap<String,Double>();
		wrapper.setDriverPays(summarys);
		double sumAmount=0.0;
		int totalcount=0;
		boolean expiredRate = false;
		List<String> driverNames = new ArrayList<String>();
		for(Ticket ticket:tickets){
			
			driverNames.add(ticket.getDriver().getFullName());
			
			StringBuffer countquery=new StringBuffer("");
			/*String*/ countquery.append("select count(obj) from Ticket obj where obj.payRollStatus=2 and obj.payRollBatch is not null and  obj.billBatch>='"+frombatch+
		    "' and obj.billBatch<='"+tobatch+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId()+" and obj.payRollBatch='"+payrollDate+"'");
			 if(!StringUtils.isEmpty(driverid)){
				 countquery.append("and obj.driver.fullName='"+driverid+"'");
			 }
			 else{
				 countquery.append("and obj.driver.fullName='"+ticket.getDriver().getFullName()+"'");
			 }
			 if(!StringUtils.isEmpty(driversmul)){
				 countquery.append(" and obj.driver not in ("+driversmul+")");
		     }
			 Long recordCount = (Long) genericDAO.getEntityManager()
			.createQuery(countquery.toString()).getSingleResult();
			 
			 
			 StringBuffer ticketquery=new StringBuffer("");
				/*String*/ ticketquery.append("select obj from Ticket obj where obj.payRollStatus=2 and obj.payRollBatch is not null and  obj.billBatch>='"+frombatch+
			    "' and obj.billBatch<='"+tobatch+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId()+" and obj.payRollBatch='"+payrollDate+"'");
				 if(!StringUtils.isEmpty(driverid)){
					 ticketquery.append("and obj.driver.fullName='"+driverid+"'");
				 }
				 else{
					 ticketquery.append("and obj.driver.fullName='"+ticket.getDriver().getFullName()+"'");
				 }
				 if(!StringUtils.isEmpty(driversmul)){
					 ticketquery.append(" and obj.driver not in ("+driversmul+")");
			     }
			 List<Ticket> filtrdtickets = genericDAO.executeSimpleQuery(ticketquery.toString());  
			 int sundaycount = 0;
			 for(Ticket ticketObj : filtrdtickets) {
				 LocalDate unloadDate = new LocalDate(ticketObj.getUnloadDate());
				 
				 if(unloadDate.getDayOfWeek() == DateTimeConstants.SUNDAY)
					 sundaycount++;
			 }
			 
			
			 
			double amount=0.0;
			double sundayAmount = 0.0;
			boolean calculateOtherRow =  true;
			
			if(sundaycount > 0){
				DriverPay pay=new DriverPay();
				int diffCount = Integer.parseInt(recordCount.toString()) - sundaycount;
				pay.setNoOfLoad(sundaycount);
				if(diffCount == 0.0){
					calculateOtherRow = false;
				}
				pay.setOrigin(ticket.getOrigin().getName());
				pay.setDestination(ticket.getDestination().getName());
				if(ticket.getCompanyLocation()!=null)
					pay.setCompanyname(ticket.getCompanyLocation().getName());
				else
					pay.setCompanyname("");
				
				if(ticket.getDriver().getTerminal()!=null)
					pay.setTerminalname(ticket.getDriver().getTerminal().getName());
				else
					pay.setTerminalname("");
				
				try{
					Long destination_id;
					Location location = genericDAO.getById(Location.class, ticket
							.getDestination().getId());
					if (location.getName().equalsIgnoreCase("grows")
							|| location.getName().equalsIgnoreCase("tullytown")) {
	                        destination_id = 91l;
	               } else {
						destination_id = ticket.getDestination().getId();
					}
					String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
							+ ticket.getOrigin().getId() + "' and obj.landfill='"
							/* + ticket.getDestination().getId() + "'"; */
							+ destination_id 
							+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
							+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'";
					
					
					
				List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
				DriverPayRate payRate = null;
				if (fs != null && fs.size() > 0) {
					for (DriverPayRate rate : fs) {
						if (rate.getRateUsing() == null) {
							payRate = rate;
							break;
						} else if (rate.getRateUsing() == 1) {
							// calculation for a load date
							if ((ticket.getLoadDate().getTime() >= rate
									.getValidFrom().getTime())
									&& (ticket.getLoadDate().getTime() <= rate
											.getValidTo().getTime())) {
								payRate = rate;
								break;
							}
						} else if (rate.getRateUsing() == 2) {
							// calculation for a unload date
							if ((ticket.getUnloadDate().getTime() >= rate
									.getValidFrom().getTime())
									&& (ticket.getUnloadDate().getTime() <= rate
											.getValidTo().getTime())) {
								payRate = rate;
								break;
							}
						}
					}
					}
				if(payRate==null){
					pay.setAmount(0.0);
					sumAmount+=0.0;
					if(StringUtils.contains(expire, "1")){
					if (!expiredRate)
						str.add("<u>Rates Are Expired or not Available</u></br>");
					expiredRate = true;
					errorCount++;
					Location originName = genericDAO.getById(Location.class,
							ticket.getOrigin().getId());
					Location DestinationName = genericDAO.getById(
							Location.class, ticket.getDestination().getId());
					boolean cont = str.contains((originName.getName() + " - "
							+ DestinationName.getName() + "</br>"));
					String string = (originName.getName() + " - "
							+ DestinationName.getName() + "</br>");

					if (!cont)
						str.add(string);
					}
				
				}else{
					
					Map criti=new HashMap();
					criti.clear();
					criti.put("status",1);
					criti.put("fullName",ticket.getDriver().getFullName() );
					Driver empObj=genericDAO.getByCriteria(Driver.class, criti);					
					double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
					
					
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					boolean wbDrivers = false;
					
					if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
						if(ticket.getDriver().getDateProbationEnd()!=null){
							if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
								wbDrivers = true;
							}
						}
					}
					
					
					
					if(wbDrivers){
						double sundayRate = payRate.getProbationRate();
						sundayAmount = sundaycount * sundayRate;
						pay.setRate(payRate.getProbationRate());
					}
					else{
						if(empObj.getId()!=null){
							if(empObj.getShift().equals("1")){							
								if(sundaycount > 0){
									double sundayRate = payRate.getPayRate() * payRate.getSundayRateFactor();
									sundayAmount = sundaycount * sundayRate;
									pay.setRate(payRate.getPayRate() * payRate.getSundayRateFactor());
								}						
							}
							else{
								if(sundaycount > 0){
									double sundayRate = payRate.getNightPayRate() * payRate.getSundayRateFactor();
									sundayAmount = sundaycount * sundayRate;
									pay.setRate(payRate.getNightPayRate() * payRate.getSundayRateFactor());
								}						
							}
						}
						else{
							if(sundaycount > 0){
								double sundayRate = payRate.getPayRate() * payRate.getSundayRateFactor();
								sundayAmount = sundaycount * sundayRate;
								pay.setRate(payRate.getPayRate() * payRate.getSundayRateFactor());
							}					
						}
					}
					amount =  sundayAmount;
					sumAmount+=amount;
					amount=MathUtil.roundUp(amount, 2);
					pay.setAmount(amount);
					//pay.setRate(payRate.getPayRate());
				}
				pay.setNoOfLoad(sundaycount);
				//if(StringUtils.isEmpty(driverid))
				pay.setDrivername(ticket.getDriver().getFullName());
				//pay.setCompanyname(ticket.getDriver().getCompany().getName());
				totalcount+=pay.getNoOfLoad();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				summarys.add(pay);
				
			 }
			
				
			if(calculateOtherRow){
				DriverPay pay=new DriverPay();
				pay.setNoOfLoad(Integer.parseInt(recordCount.toString())-sundaycount);
				pay.setOrigin(ticket.getOrigin().getName());
				pay.setDestination(ticket.getDestination().getName());
				if(ticket.getCompanyLocation()!=null)
					pay.setCompanyname(ticket.getCompanyLocation().getName());
				else
					pay.setCompanyname("");
				
				if(ticket.getDriver().getTerminal()!=null)
					pay.setTerminalname(ticket.getDriver().getTerminal().getName());
				else
					pay.setTerminalname("");
				
				try{
					Long destination_id;
					Location location = genericDAO.getById(Location.class, ticket
						.getDestination().getId());
					if (location.getName().equalsIgnoreCase("grows")
						|| location.getName().equalsIgnoreCase("tullytown")) {
                        destination_id = 91l;
					} else {
						destination_id = ticket.getDestination().getId();
					}
					String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"
						/* + ticket.getDestination().getId() + "'"; */
						+ destination_id 
						+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
						+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'";
					
					
					
					
					List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
					DriverPayRate payRate = null;
					if (fs != null && fs.size() > 0) {
						for (DriverPayRate rate : fs) {
							if (rate.getRateUsing() == null) {
								payRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
						// calculation for a load date
						if ((ticket.getLoadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getLoadDate().getTime() <= rate
										.getValidTo().getTime())) {
							payRate = rate;
							break;
						}
					} else if (rate.getRateUsing() == 2) {
						// calculation for a unload date
						if ((ticket.getUnloadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getUnloadDate().getTime() <= rate
										.getValidTo().getTime())) {
							payRate = rate;
							break;
						}
					}
				}
				}
			if(payRate==null){
				pay.setAmount(0.0);
				sumAmount+=0.0;
				if(StringUtils.contains(expire, "1")){
				if (!expiredRate)
					str.add("<u>Rates Are Expired or not Available</u></br>");
				expiredRate = true;
				errorCount++;
				Location originName = genericDAO.getById(Location.class,
						ticket.getOrigin().getId());
				Location DestinationName = genericDAO.getById(
						Location.class, ticket.getDestination().getId());
				boolean cont = str.contains((originName.getName() + " - "
						+ DestinationName.getName() + "</br>"));
				String string = (originName.getName() + " - "
						+ DestinationName.getName() + "</br>");

				if (!cont)
					str.add(string);
				}
			
			}else{
				
				Map criti=new HashMap();
				criti.clear();
				criti.put("status",1);
				criti.put("fullName",ticket.getDriver().getFullName() );
				Driver empObj=genericDAO.getByCriteria(Driver.class, criti);
				
				double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
				
				
				
				LocalDate dt= null;								
				if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
					if(frombatch.equalsIgnoreCase(tobatch)){
						try {											
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else{
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
				}
				else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
					try {
						dt = new LocalDate(frombatch);
					} catch (Exception e) {
						System.out.println("Error Parsing Date");
					}
				}
				else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
					try {
						dt = new LocalDate(tobatch);
					} catch (Exception e) {
						System.out.println("Error Parsing Date");
					}
				}
				boolean wbDrivers = false;
				
				if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
					if(ticket.getDriver().getDateProbationEnd()!=null){
						if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
							wbDrivers = true;
						}
					}
				}
				
				
				
				if(wbDrivers){
					pay.setRate(payRate.getProbationRate());
					amount= nofload*payRate.getProbationRate();
				}
				else{
					if(empObj.getId()!=null){
						if(empObj.getShift().equals("1")){						
							pay.setRate(payRate.getPayRate());						
							amount=nofload*payRate.getPayRate();
						}
						else{						
							pay.setRate(payRate.getNightPayRate());						
							amount= nofload*payRate.getNightPayRate();						
						}
					}
					else{
						pay.setRate(payRate.getPayRate());
						amount= nofload*payRate.getPayRate();					
					}
				}
				sumAmount+=amount;
				amount=MathUtil.roundUp(amount, 2);
				pay.setAmount(amount);
				//pay.setRate(payRate.getPayRate());
			}
			pay.setNoOfLoad(pay.getNoOfLoad());
			//if(StringUtils.isEmpty(driverid))
			pay.setDrivername(ticket.getDriver().getFullName());
			//pay.setCompanyname(ticket.getDriver().getCompany().getName());
			totalcount+=pay.getNoOfLoad();			
			
			if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
				double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
				driveramount = driveramount + amount;
				sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
			}else{
				sumAmounts.put(ticket.getDriver().getFullName(),amount);
			}
			
			
		
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			summarys.add(pay);
		}
		}
		
		wrapper.setDriverNames(driverNames);
		wrapper.setSumAmountsMap(sumAmounts);
		sumAmount=MathUtil.roundUp(sumAmount, 2);
		wrapper.setTotalRowCount(totalcount);
		wrapper.setSumTotal(sumAmount);
		wrapper.setBatchDateFrom((String) criteria.getSearchMap().get("fromDate"));
		wrapper.setBatchDateTo((String) criteria.getSearchMap().get("toDate"));
		wrapper.setPayRollBatch((String) criteria.getSearchMap().get("payrollDate"));
		if(!StringUtils.isEmpty(company)){
		wrapper.setCompany(companylocation.getName());
		wrapper.setCompanylocation(companylocation);
		}
		if(!StringUtils.isEmpty(terminal)){
		wrapper.setTerminal(terminallocation);
		}
		if(driver!=null){
		wrapper.setDriver(driver.getFullName());
		wrapper.setCompany(driver.getCompany().getName());
		}
		/*if(StringUtils.isEmpty(driverid))*/
		if(StringUtils.contains(sum, "true")){
			for(DriverPay driverPay:wrapper.getDriverPays()){
				Double amount=map.get(driverPay.getDrivername());
				if(amount==null){
				amount=0.0;	
				}
				Double totamount=amount+driverPay.getAmount();
				Integer count=map1.get(driverPay.getDrivername());
				if(count==null){
					count=0;
				}	
				Integer totcount=count+driverPay.getNoOfLoad();
				map1.put(driverPay.getDrivername(), totcount);
				map.put(driverPay.getDrivername(), totamount);
			}
			wrapper.getDriverPays().clear();
		}
		/*if(StringUtils.isEmpty(driverid))*/
		if(StringUtils.contains(sum, "true")){
		List<DriverPay> fields=new ArrayList<DriverPay>();
		wrapper.setDriverPays(fields);
		/*if(StringUtils.isEmpty(driverid))*/
		
		Map empmap=new HashMap();
		double TotalAmount=0.0;
		
		
		Map criti = new HashMap();
		String drivernames = "";
        for(int i=0;i<drivers.size();i++){		        	
        	String driverFullName = String.valueOf(drivers.get(i));	
        	if(drivernames.equals("")){
        		drivernames ="'"+String.valueOf(drivers.get(i))+"'";	
        	}
        	else{
        		drivernames = drivernames+",'"+ String.valueOf(drivers.get(i))+"'";
        	}
        	criti.clear();
        	criti.put("status",1);
        	criti.put("fullName",driverFullName);
        	Driver driver3 = genericDAO.getByCriteria(Driver.class, criti);
       	
        		Double deductionAmount = 0.0;
				Double sickParsonalAmount=0.0;
				Double vacationAmount=0.0;
				Double bonusAmount=0.0;
				Double miscAmount=0.0;
				Double holidayAmount=0.0;
				DriverPay pay=new DriverPay();
				pay.setDrivername(driver3.getFullName());
				pay.setCompanyname(driver3.getCompany().getName());
				pay.setTerminalname(driver3.getTerminal().getName());
				Double amount=0.0;
				amount=(Double)(map.get(driver3.getFullName()));				
				empmap.clear();
				empmap.put("fullName", driver3.getFullName());
				empmap.put("status",1);
				Driver employee=genericDAO.getByCriteria(Driver.class, empmap);				
				Double miscamt=0.0;	
				Double reimburseAmount=0.0;	
				Double quatarAmount=0.0;	
				if(employee!=null){
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and  obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes!='Reimbursement' and obj.miscType!='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}	
				if(!StringUtils.isEmpty(payrollDate)){
					miscamountquery.append(" and obj.payRollBatch='"+payrollDate+"'");
				}
				
				
				List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
				for(MiscellaneousAmount miscamnt:miscamounts){
					miscamt+=miscamnt.getMisamount();					
				}	
				
				
				StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes='Reimbursement'");
				if(!StringUtils.isEmpty(frombatch)){
					reimburseamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					reimburseamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}	
				if(!StringUtils.isEmpty(payrollDate)){
					reimburseamountquery.append(" and obj.payRollBatch='"+payrollDate+"'");
				}
				
				List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:reimburseamounts){
					reimburseAmount+=reimbursecamnt.getMisamount();					
				}
				
				///*********************** Newly Added
				StringBuffer qutaramountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					qutaramountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					qutaramountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}	
				if(!StringUtils.isEmpty(payrollDate)){
					qutaramountquery.append(" and obj.payRollBatch='"+payrollDate+"'");
				}
				
				List<MiscellaneousAmount> qutaramounts=genericDAO.executeSimpleQuery(qutaramountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:qutaramounts){
					quatarAmount+=reimbursecamnt.getMisamount();					
				}
				
				//*************************
				
				
			    }
				
				amount=MathUtil.roundUp(amount, 2);
				miscamt=MathUtil.roundUp(miscamt, 2);
				pay.setTransportationAmount(amount);
				//amount=amount+miscamt;				
				//amount=MathUtil.roundUp(amount, 2);
				//pay.setAmount(amount);
				pay.setMiscAmount(miscamt);
				pay.setReimburseAmount(reimburseAmount);
				pay.setQuatarAmount(quatarAmount);
				
				pay.setNoOfLoad(map1.get(driver3.getFullName()));
				/*empmap.clear();
				empmap.put("fullName", driver3.getFullName());
				empmap.put("status",1);
				Driver employee=genericDAO.getByCriteria(Driver.class, empmap);*/
				if(employee!=null){
					StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
					    ptodquery.append(" and obj.batchdate>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						ptodquery.append(" and obj.batchdate<='"+tobatch+"'");
					}
					if(!StringUtils.isEmpty(payrollDate)){
						ptodquery.append(" and obj.payRollBatch='"+payrollDate+"'");
					}
					
					List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
					for(Ptodapplication ptodapplication:ptodapplications){
						if(ptodapplication.getLeavetype().getId()==1){
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
						}
						if(ptodapplication.getLeavetype().getId()==4){
							vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
							
						}
					}
					
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}				
					
					if(employee.getCompany().getId()!=4l || employee.getTerminal().getId()!=93l){	
						if(employee.getDateProbationEnd()!=null){
							if(new LocalDate(employee.getDateProbationEnd()).isAfter(dt) || new LocalDate(employee.getDateProbationEnd()).isEqual(dt) ){
								double miscAndLoadAmt = pay.getTransportationAmount();											
								deductionAmount =  miscAndLoadAmt * 0.10;
								if(deductionAmount > 100.0){
									deductionAmount = 100.0;
								}
							}	
						}
					}
					deductionAmount=MathUtil.roundUp(deductionAmount, 2);
					
					StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
					}
					if(!StringUtils.isEmpty(payrollDate)){
						bonusquery.append(" and obj.payRollBatch='"+payrollDate+"'");
					}
					
					
						List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
						for(EmployeeBonus bonus:bonuses){
							for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
								bonusAmount+=list.getBonusamount();
								//miscAmount+=list.getMisamount();
							}
						}
					StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus=2 and obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
					if(!StringUtils.isEmpty(frombatch)){
						holidayquery.append(" and obj.batchdate='"+frombatch+"'");
						}
						/*if(!StringUtils.isEmpty(tobatch)){
							holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
						}*/
					if(!StringUtils.isEmpty(payrollDate)){
						holidayquery.append(" and obj.payRollBatch='"+payrollDate+"'");
					}
					
						List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
						for(HolidayType type:holidayTypes){
							holidayAmount=holidayAmount+type.getAmount();
						}
					
				}
				
				amount=pay.getTransportationAmount()-deductionAmount;				
				amount=MathUtil.roundUp(amount, 2);
				pay.setAmount(amount);
				
				pay.setProbationDeductionAmount(deductionAmount);
				pay.setBonusAmount(bonusAmount);
				pay.setSickPersonalAmount(sickParsonalAmount);
				pay.setVacationAmount(vacationAmount);
				pay.setHolidayAmount(holidayAmount);
				Double totalAmount=(pay.getTransportationAmount()-pay.getProbationDeductionAmount())+pay.getMiscAmount()+pay.getSickPersonalAmount()+pay.getBonusAmount()+pay.getHolidayAmount();
				totalAmount=MathUtil.roundUp(totalAmount, 2);
				TotalAmount+=totalAmount;
				pay.setTotalAmount(totalAmount);
				fields.add(pay);
			}
		
        
        
        //******************************************************************
        List<Driver> driverWithOutTickets = null;
        if(!drivernames.equals("") && StringUtils.isEmpty(driverid)){
        	StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and  obj.fullName not in ("+drivernames+")");
        	if(!StringUtils.isEmpty(company)){
        		drivernameauery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		drivernameauery.append(" and obj.terminal="+terminal);
             }
        	
        	 driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
        }
        else if(drivernames.equals("") && !StringUtils.isEmpty(driverid)){
        	StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and  obj.fullName in ('"+driverid+"')");
        	if(!StringUtils.isEmpty(company)){
        		drivernameauery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		drivernameauery.append(" and obj.terminal="+terminal);
             }       	
        	
        	 driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
        }
        else if(drivernames.equals("") && StringUtils.isEmpty(driverid)){
        	StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1'");
        	if(!StringUtils.isEmpty(company)){
        		drivernameauery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		drivernameauery.append(" and obj.terminal="+terminal);
             }       	
        	
        	 driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
        }
       
        if(driverWithOutTickets!=null){
        for(Driver driverWithOutTicket: driverWithOutTickets){        
       	
        		boolean setDriver = false;
        		Double deductionAmount = 0.0;
				Double sickParsonalAmount=0.0;
				Double vacationAmount=0.0;
				Double bonusAmount=0.0;
				Double miscAmount=0.0;
				Double holidayAmount=0.0;
				DriverPay pay=new DriverPay();
				pay.setDrivername(driverWithOutTicket.getFullName());
				if(driverWithOutTicket.getCompany()!=null)
					pay.setCompanyname(driverWithOutTicket.getCompany().getName());
				else
					pay.setCompanyname("");
				
				pay.setTerminalname(driverWithOutTicket.getTerminal().getName());
				Double amount=0.0;
				//amount=(Double)(map.get(driverWithOutTicket.getFullName()));				
				
				Driver employee= driverWithOutTicket;				
				Double miscamt=0.0;	
				Double reimburseAmount=0.0;	
				Double quatarAmount=0.0;	
				if(employee!=null){
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes!='Reimbursement' and obj.miscType!='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}	
				if(!StringUtils.isEmpty(payrollDate)){
					miscamountquery.append(" and obj.payRollBatch='"+payrollDate+"'");
				}
				
				
				List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
				for(MiscellaneousAmount miscamnt:miscamounts){
					setDriver = true;
					miscamt+=miscamnt.getMisamount();					
				}	
				
				
				///*************** Newly Added 
				StringBuffer quataramountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType='Quarter Bonus'");
				if(!StringUtils.isEmpty(frombatch)){
					quataramountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					quataramountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}	
				if(!StringUtils.isEmpty(payrollDate)){
					quataramountquery.append(" and obj.payRollBatch='"+payrollDate+"'");
				}
				
				List<MiscellaneousAmount> quataramounts=genericDAO.executeSimpleQuery(quataramountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:quataramounts){
					setDriver = true;
					 quatarAmount+=reimbursecamnt.getMisamount();					
				}
				
				//******************
				
				
				StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes='Reimbursement'");
				if(!StringUtils.isEmpty(frombatch)){
					reimburseamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
				}
				if(!StringUtils.isEmpty(tobatch)){
					reimburseamountquery.append(" and obj.batchTo<='"+tobatch+"'");
				}	
				
				if(!StringUtils.isEmpty(payrollDate)){
					reimburseamountquery.append(" and obj.payRollBatch='"+payrollDate+"'");
				}
				List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
				for(MiscellaneousAmount reimbursecamnt:reimburseamounts){
					setDriver = true;
					reimburseAmount+=reimbursecamnt.getMisamount();					
				}
				
			    }
				amount=MathUtil.roundUp(amount, 2);
				pay.setTransportationAmount(amount);
				
				miscamt=MathUtil.roundUp(miscamt, 2);
				
				//amount=amount+miscamt;					
				//amount=MathUtil.roundUp(amount, 2);
				
				pay.setAmount(amount);
				
				pay.setMiscAmount(miscamt);
				pay.setReimburseAmount(reimburseAmount);
				pay.setQuatarAmount(quatarAmount);
				pay.setNoOfLoad(0);
				
				Driver employees=driverWithOutTicket;
				if(employees!=null){
					StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employees.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
					    ptodquery.append(" and obj.batchdate>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						ptodquery.append(" and obj.batchdate<='"+tobatch+"'");
					}
					if(!StringUtils.isEmpty(payrollDate)){
						ptodquery.append(" and obj.payRollBatch='"+payrollDate+"'");
					}
					List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
					for(Ptodapplication ptodapplication:ptodapplications){
						if(ptodapplication.getLeavetype().getId()==1){
							setDriver = true;
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
						}
						if(ptodapplication.getLeavetype().getId()==4){
							setDriver = true;
							vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
							
						}
					}
					
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}				
					
					
					if(employee.getDateProbationEnd()!=null){
						if(new LocalDate(employee.getDateProbationEnd()).isAfter(dt)){
							
						}
						else{
							
						}	
					}
					
					
					StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
					}
					if(!StringUtils.isEmpty(payrollDate)){
						bonusquery.append(" and obj.payRollBatch='"+payrollDate+"'");
					}
					
						List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
						for(EmployeeBonus bonus:bonuses){
							for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
								setDriver = true;
								bonusAmount+=list.getBonusamount();
								//miscAmount+=list.getMisamount();
							}
						}
					StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus=2 and obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
					if(!StringUtils.isEmpty(frombatch)){
						holidayquery.append(" and obj.batchdate='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
						}
						if(!StringUtils.isEmpty(payrollDate)){
							holidayquery.append(" and obj.payRollBatch='"+payrollDate+"'");
						}
						List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
						for(HolidayType type:holidayTypes){
							setDriver = true;
							holidayAmount=holidayAmount+type.getAmount();
						}
					
				}
				
				pay.setProbationDeductionAmount(0.0);
				pay.setBonusAmount(bonusAmount);
				pay.setSickPersonalAmount(sickParsonalAmount);
				pay.setVacationAmount(vacationAmount);
				pay.setHolidayAmount(holidayAmount);
				Double totalAmount=(pay.getTransportationAmount()-pay.getProbationDeductionAmount())+pay.getMiscAmount()+pay.getSickPersonalAmount()+pay.getBonusAmount()+pay.getHolidayAmount();
				totalAmount=MathUtil.roundUp(totalAmount, 2);
				
				
				if(setDriver) {
					TotalAmount+=totalAmount;
					pay.setTotalAmount(totalAmount);
					fields.add(pay);			
				}
        	
        }
		}
        //************************************************************
		wrapper.setSumAmount(TotalAmount);
		
		
		
		}
		wrapper.setList(str);
		return wrapper;
	
	}
	///***********************************************************///
	///***********************************************************///
	///***********************************************************///
	
	
	
	public TimeSheetWrapper generateTimeSheetData(SearchCriteria criteria,TimeSheetInput input)
	{
		double totalrHours=0.0;
		double totalwHours=0.0;
		double totalot=0.0;
		double totaldt=0.0;
		Map<String, String> params = new HashMap<String, String>();
		
		String weekstartDate1 = (String) criteria.getSearchMap().get("batchDatefrom");
		String weekendDate1 = (String) criteria.getSearchMap().get("batchDateto");
		
		String weekstartDate = ReportDateUtil.getFromDate(input.getWeekstartDateFrom());
		String weekendDate = ReportDateUtil.getToDate(input.getWeekendDateTo());
		 String sum= (String) criteria.getSearchMap().get("summary");
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String employee = input.getDriver();
		String category = input.getCategory();
		
		
		/*String employeeno = input.getEmployeesNo();*/
		
		StringBuffer query = new StringBuffer("");
		//query.append("select obj from Attendance obj where 1=1 ");
		query.append("select obj from TimeSheet obj where 1=1 ");
		
		if (!StringUtils.isEmpty(company)) {
		query.append("and  obj.company in (" + company + ")");
		}
		
		if (!StringUtils.isEmpty(terminal)) {
			query.append("and  obj.terminal in (" + terminal + ")");
		}
		if (!StringUtils.isEmpty(employee)) {
			query.append("and  obj.driver in (" + employee + ")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.catagory in (" + category + ")");
		}
		
		if (!StringUtils.isEmpty(weekstartDate) && !StringUtils.isEmpty(weekendDate)) {
			query.append("and  obj.batchdate between '"
					+ weekstartDate + "' and '" + weekendDate
					+ "'");
		}
		
		
		
		//System.out.println("\nquery==>" + query + "\n");
		TimeSheetWrapper wrapper=new TimeSheetWrapper();
		List<TimeSheet> fs = genericDAO.executeSimpleQuery(query.toString());
		
		if(StringUtils.contains(sum, "true")){
			//System.out.println("\nTimeSheet Summary\n");
		List<TimeSheet> summarys = new ArrayList<TimeSheet>();
		//TimeSheetWrapper wrapper=new TimeSheetWrapper();
		wrapper.setTimesheets(summarys);
		for (TimeSheet timesheet : fs) {
			if(timesheet.getTotalothoursinweek()!=null){
				   totalot=totalot+timesheet.getTotalothoursinweek();
				   System.out.println("\not===>"+totalot+"\n");
				}
				if(timesheet.getTotaldthoursinweek()!=null){
				totaldt=totaldt+timesheet.getTotaldthoursinweek();
				//System.out.println("\ndt===>"+totaldt+"\n");
				}
				if(timesheet.getHoursworkedInweekRoundedValue() !=null){
					totalwHours=totalwHours+timesheet.getHoursworkedInweekRoundedValue();
				}
				
				if(timesheet.getRegularhours()!=null){
					totalrHours=totalrHours+timesheet.getRegularhours();
				}
			if (timesheet != null) {
				TimeSheet output = new TimeSheet();
				output.setBatchDates((timesheet.getBatchdate()!=null) ?sdf.format(timesheet.getBatchdate()):"");
				output.setCompanies((timesheet.getCompany() != null) ? timesheet.getCompany().getName() : "");
				output.setTerminals((timesheet.getTerminal() != null) ? timesheet.getTerminal().getName() : "");
				output.setEmployees((timesheet.getDriver() != null) ? timesheet.getDriver().getFullName() : "");
				output.setEmployeesNo((timesheet.getDriver() != null) ? timesheet.getDriver().getStaffId(): "");
				//output.setAttendanceDates((attendance.getAttendancedate()!=null) ?sdf.format(attendance.getAttendancedate()):"");
				/*output.setSignintime((attendance.getSignintime() != null) ? attendance.getSignintime(): "");
				output.setSignouttime((attendance.getSignouttime() != null) ? attendance.getSignouttime(): "");*/
				output.setrHours((timesheet.getRegularhours() !=null) ? timesheet.getRegularhours(): 0.00);
				output.setHoursworkeds((timesheet.getTotalhoursworkedInweek() !=null) ? timesheet.getTotalhoursworkedInweek(): 0.00);
				output.setOtHours((timesheet.getTotalothoursinweek()!=null)?timesheet.getTotalothoursinweek():0.00);
				output.setDtHours((timesheet.getTotaldthoursinweek()!=null)?timesheet.getTotaldthoursinweek():0.00);
				
				summarys.add(output);
				
			}
			
		}
		//wrapper.setTotalRowCount(fs.size());
		}
		else{
			List<TimeSheetWrapperDetail> summarys = new ArrayList<TimeSheetWrapperDetail>();
			///System.out.println("\nTimeSheet Details\n");
			wrapper.setTimesheetdetail(summarys);
			/*double totalot=0.0;
			double totaldt=0.0;*/
			for (TimeSheet timesheet : fs) {
				if (timesheet != null) {
					
					if(timesheet.getTotalothoursinweek()!=null){
					   totalot=totalot+timesheet.getTotalothoursinweek();
					   System.out.println("\not===>"+totalot+"\n");
					}
					if(timesheet.getTotaldthoursinweek()!=null){
					totaldt=totaldt+timesheet.getTotaldthoursinweek();
					//System.out.println("\ndt===>"+totaldt+"\n");
					}
					if(timesheet.getHoursworkedInweekRoundedValue() !=null){
						totalwHours=totalwHours+timesheet.getHoursworkedInweekRoundedValue();
					}
					
					if(timesheet.getDailyHours()!=null){
						totalrHours=totalrHours+timesheet.getDailyHours();
					}
					else{
						totalrHours=totalrHours+0.0;
					}
					
					for(int i=1;i<=7;i++){
					TimeSheetWrapperDetail output = new TimeSheetWrapperDetail();
					
					output.setEmpnumber(timesheet.getDriver().getStaffId());
					output.setEmpname(timesheet.getDriver().getFullName());
					output.setEmpcategory(timesheet.getCatagory().getName());
					output.setEmpcompany(timesheet.getCompany().getName());
					output.setEmpterminal(timesheet.getTerminal().getName());
					
					//output.setRegularhours((timesheet.getRegularhours()).toString());
					
					if(i==1){
						output.setDay(timesheet.getSundayname());
						output.setDate(timesheet.getSdate() !=null ? sdf.format(timesheet.getSdate()):"");
						output.setTimein(timesheet.getSsignintime() !=null ?timesheet.getSsignintime():"");
						output.setTimeout(timesheet.getSsignouttime() !=null?timesheet.getSsignouttime():"");
						output.setHoursworked(timesheet.getShoursworked()!=null?timesheet.getShoursworked():0.00);
						Double ot=0.0;
						Double dt=0.0;
						if(timesheet.getShoursworked() !=null){
							if(timesheet.getShoursworked()>0){								
								output.setHoursworked(timesheet.getShoursworked());
								
								if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
									output.setRegularhours((timesheet.getDailyHours()).toString());
									if(timesheet.getSdtflag().equalsIgnoreCase("Yes")){
										dt=timesheet.getShoursworked()-timesheet.getDailyHours();
									}
									else{
										if(timesheet.getSotflag().equalsIgnoreCase("Yes")){
											ot=timesheet.getShoursworked()-timesheet.getDailyHours();
										}
									}
								}
								else{
									output.setRegularhours("0.00");									
								}
							}
            				else{
            					output.setHoursworked(0.00);
    							output.setRegularhours("0.00");
    							dt = 0.00;
								ot = 0.00;
            				}
            	
						}
						else{
							output.setHoursworked(0.00);
							output.setRegularhours("0.00");
							dt = 0.00;
							ot = 0.00;
							System.out.println("\ngetShoursworked() != null----else\n");
						}
						output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
					}
					
					if(i==2){
						output.setDay(timesheet.getMondayname());
						output.setDate(timesheet.getMdate() !=null ?sdf.format(timesheet.getMdate()):"");
						output.setTimein(timesheet.getMsignintime()!=null ? timesheet.getMsignintime():"");
						output.setTimeout(timesheet.getMsignouttime()!=null ?timesheet.getMsignouttime():"");
						output.setHoursworked(timesheet.getMhoursworked()!=null ? timesheet.getMhoursworked():0.00);
						Double ot=0.0;
						Double dt=0.0;
						if(timesheet.getMhoursworked() != null){
							if(timesheet.getMhoursworked()>0){
            				output.setHoursworked(timesheet.getMhoursworked());
            				
            				if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
								output.setRegularhours((timesheet.getDailyHours()).toString());
								if(timesheet.getMdtflag().equalsIgnoreCase("Yes")){
									dt=timesheet.getMhoursworked()-timesheet.getDailyHours();
								}
								else{
									if(timesheet.getMtotflag().equalsIgnoreCase("Yes")){
										ot=timesheet.getMhoursworked()-timesheet.getDailyHours();
									}
								}
							}
							else{
								output.setRegularhours("0.00");									
							}
            				
            				
						}
							else{
								output.setHoursworked(0.00);
								output.setRegularhours("0.00");
							}
						}
						else{
							output.setHoursworked(0.00);
							output.setRegularhours("0.00");
						}
						output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
					}
					
                    if(i==3){
                    	output.setDay(timesheet.getTuesdayname());
                    	output.setDate(timesheet.getTdate() !=null ? sdf.format(timesheet.getTdate()):"");
                    	output.setTimein(timesheet.getTsignintime()!=null ? timesheet.getTsignintime():"");
                    	output.setTimeout(timesheet.getTsignouttime() !=null ? timesheet.getTsignouttime():"");
                    	output.setHoursworked(timesheet.getThoursworked() !=null ? timesheet.getThoursworked():0.00);
                    	Double ot=0.0;
						Double dt=0.0;
						if(timesheet.getThoursworked() != null){
							if(timesheet.getThoursworked()>0){
								output.setHoursworked(timesheet.getThoursworked());
								if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
									output.setRegularhours((timesheet.getDailyHours()).toString());
									if(timesheet.getTdtflag().equalsIgnoreCase("Yes")){
										dt=timesheet.getThoursworked()-timesheet.getDailyHours();
									}
									else{
										if(timesheet.getTotflag().equalsIgnoreCase("Yes")){
											ot=timesheet.getThoursworked()-timesheet.getDailyHours();
										}
									}
								}
								else{
									output.setRegularhours("0.00");									
								}
            				
							}
							else{
								output.setHoursworked(0.00);
								output.setRegularhours("0.00");
							}
						}
						else{
							output.setHoursworked(0.00);
							output.setRegularhours("0.00");
						}
						output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
					}
					
                    if(i==4){
                    	output.setDay(timesheet.getWednesdayname());
                    	output.setDate(timesheet.getWdate() !=null?sdf.format(timesheet.getWdate()):"");
                    	output.setTimein(timesheet.getWsignintime()!=null ? timesheet.getWsignintime():"");
                    	output.setTimeout(timesheet.getW_signouttime() !=null ? timesheet.getW_signouttime():"");
                    	output.setHoursworked(timesheet.getWhoursworked() !=null ?timesheet.getWhoursworked():0.00);
                    	Double ot=0.0;
						Double dt=0.0;
						if(timesheet.getWhoursworked() != null){
							if(timesheet.getWhoursworked()>0){
								output.setHoursworked(timesheet.getWhoursworked());
								if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
									output.setRegularhours((timesheet.getDailyHours()).toString());
									if(timesheet.getWdtflag().equalsIgnoreCase("Yes")){
										dt=timesheet.getWhoursworked()-timesheet.getDailyHours();
									}
									else{
										if(timesheet.getWotflag().equalsIgnoreCase("Yes")){
											ot=timesheet.getWhoursworked()-timesheet.getDailyHours();
										}
									}
								}
								else{
									output.setRegularhours("0.00");									
								}           				
							}
							else{
								output.setHoursworked(0.00);
								output.setRegularhours("0.00");
							}
            	
						}
						else{
							output.setHoursworked(0.00);
							output.setRegularhours("0.00");
						}
						output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
					 }
					
                    if(i==5){
                    	output.setDay(timesheet.getThrusdayname());
                    	output.setDate(timesheet.getThdate() !=null ? sdf.format(timesheet.getThdate()):"");
                    	output.setTimein(timesheet.getThsignintime()!=null ? timesheet.getThsignintime():"");
                    	output.setTimeout(timesheet.getThsignouttime()!=null ? timesheet.getThsignouttime():"");
                    	output.setHoursworked(timesheet.getThhoursworked()!=null ? timesheet.getThhoursworked():0.00);
                    	Double ot=0.0;
						Double dt=0.0;
						if(timesheet.getThhoursworked() != null){
						if(timesheet.getThhoursworked()>0){
							output.setHoursworked(timesheet.getThhoursworked());

							if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
								output.setRegularhours((timesheet.getDailyHours()).toString());
								if(timesheet.getThdtflag().equalsIgnoreCase("Yes")){
									dt=timesheet.getThhoursworked()-timesheet.getDailyHours();
								}
								else{
									if(timesheet.getThotflag().equalsIgnoreCase("Yes")){
										ot=timesheet.getThhoursworked()-timesheet.getDailyHours();
									}
								}
							}
							else{
								output.setRegularhours("0.00");									
							}
            				
						}
						else{
								output.setHoursworked(0.00);
								output.setRegularhours("0.00");
							}
						}
						else{
							output.setHoursworked(0.00);
							output.setRegularhours("0.00");
						}
						output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
					 }
                    
                    if(i==6){
                    	output.setDay(timesheet.getFridayname());
                    	output.setDate(timesheet.getFdate()!=null?sdf.format(timesheet.getFdate()):"");
                    	output.setTimein(timesheet.getFsignintime()!=null ? timesheet.getFsignintime():"");
                    	output.setTimeout(timesheet.getFsignouttime()!=null ? timesheet.getFsignouttime():"");
                    	output.setHoursworked(timesheet.getFhoursworked()!=null ? timesheet.getFhoursworked():0.00);
                    	Double ot=0.0;
						Double dt=0.0;
							if(timesheet.getFhoursworked() != null){
								if(timesheet.getFhoursworked()>0){
									output.setHoursworked(timesheet.getFhoursworked());
									if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
										output.setRegularhours((timesheet.getDailyHours()).toString());
										if(timesheet.getFdtflag().equalsIgnoreCase("Yes")){
											dt=timesheet.getFhoursworked()-timesheet.getDailyHours();
										}
										else{
											if(timesheet.getFotflag().equalsIgnoreCase("Yes")){
												ot=timesheet.getFhoursworked()-timesheet.getDailyHours();
											}
										}
									}
									else{
										output.setRegularhours("0.00");									
									}
								}
								else{
									output.setHoursworked(0.00);
									output.setRegularhours("0.00");
								}
							}
							else{
								output.setHoursworked(0.00);
								output.setRegularhours("0.00");
							}
						output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
                    	
					 }
                    
                    if(i==7){
                    	output.setDay(timesheet.getStdayname());
                    	output.setDate(timesheet.getStadate() !=null? sdf.format(timesheet.getStadate()):"");
                    	output.setTimein(timesheet.getStsignintime() !=null ? timesheet.getStsignintime():"");
                    	output.setTimeout(timesheet.getSt_signouttime() !=null ? timesheet.getSt_signouttime():"");
                    	Double ot=0.0;
						Double dt=0.0;
                    		if(timesheet.getSthoursworked() != null){
                    			if(timesheet.getSthoursworked() >0 ){                    			
                    				output.setHoursworked(timesheet.getSthoursworked());
                    				if(timesheet.getDailyHours()!=null && timesheet.getDailyHours() > 0.0){
        								output.setRegularhours((timesheet.getDailyHours()).toString());
        								if(timesheet.getStdtflag().equalsIgnoreCase("Yes")){
        									dt=timesheet.getSthoursworked()-timesheet.getDailyHours();
        								}
        								else{
        									if(timesheet.getStotflag().equalsIgnoreCase("Yes")){
        										ot=timesheet.getSthoursworked()-timesheet.getDailyHours();
        									}
        								}
        							}
        							else{
        								output.setRegularhours("0.00");									
        							}
                    			}
                    			else{
                        			output.setHoursworked(0.00);
                        			output.setRegularhours("0.00");
                        		}
                    	
                    		}
                    		else{
                    			output.setHoursworked(0.00);
                    			output.setRegularhours("0.00");
                    			System.out.println("\ngetSthoursworked() != null----else\n");
                    		}
                    	output.setOthours(MathUtil.roundUp(ot, 2));
						output.setDthours(MathUtil.roundUp(dt, 2));
                    }
                     
                    
					summarys.add(output);
					}
				}
			}
		
		}
		/*if(!StringUtils.contains(sum, "true")){*/
			wrapper.setTotalrHours(totalrHours);
			wrapper.setTotalwHours(totalwHours);
			wrapper.setTotalot(totalot);
			wrapper.setTotaldt(totaldt);
			System.out.println("\ntotalot==>"+totalot+"\n");
			System.out.println("\ntotaldt==>"+totaldt+"\n");
	/*	}*/
		return wrapper;
	}

	@Override
	public List<RemainingLeaveInput> generateRemainingLeaveReport(
			SearchCriteria criteria, RemainingLeaveInput input) {
		// TODO Auto-generated method stub
		String company=input.getCompany();
		String terminal=input.getTerminal();
		String category=input.getCategory();
		String leaveType=input.getLeaveType();
		String employees=input.getEmployees();
		StringBuffer query=new StringBuffer("");
		query.append("select obj from LeaveCurrentBalance obj where 1=1");
		query.append(" and ((obj.hourremain!='0.0' and obj.hourremain is not null) OR (obj.daysremain!='0.0' and obj.daysremain is not null)) ");
		if (!StringUtils.isEmpty(employees)) {
			query.append("and  obj.empname in ("+employees+")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.empcategory in ("+category+")");
		   }
		if (!StringUtils.isEmpty(leaveType)) {
			query.append("and  obj.leavetype in ("+leaveType+")");
		    }
		if (!StringUtils.isEmpty(company)) {
			query.append("and  obj.company in ("+company+")");
			}
		if (!StringUtils.isEmpty(terminal)) {
				query.append("and  obj.terminal in ("+terminal+")");
			}
		
		query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.empname.fullName asc");
		
			System.out.println("******* The query is "+query.toString());
			List<RemainingLeaveInput> out=new ArrayList<RemainingLeaveInput>();
			List<LeaveCurrentBalance> balances=genericDAO.executeSimpleQuery(query.toString()); 
			for(LeaveCurrentBalance balance:balances){
				
				Double ptodrate=0.0;
				Double hourlyptodrate=0.0;
				boolean cal=false;
				String ptodquery = "select obj from Ptod obj where obj.company="+balance.getCompany().getId()+" and obj.terminal="+balance.getTerminal().getId()+" and obj.category="+balance.getEmpcategory().getId()+"  and obj.leavetype='"+balance.getLeavetype().getId()+"'";
				List<Ptod> ptodob = genericDAO.executeSimpleQuery(ptodquery);
				if(!ptodob.isEmpty() && ptodob.size()>0 ){
					Ptod ptod=ptodob.get(0);
					if(ptod.getCalculateFlag()==1){
						cal=true;
					}else{
						ptodrate=ptod.getRate();	
					}
				}
				if(cal){
				String rquery="select obj from PtodRate obj where obj.driver="+balance.getEmpname().getId()+" and obj.catagory="+balance.getEmpcategory().getId()+" and obj.company="+balance.getCompany().getId()+" and obj.terminal="+balance.getTerminal().getId();
				List<PtodRate> ptodRates=genericDAO.executeSimpleQuery(rquery);
				if(!ptodRates.isEmpty()){
					PtodRate ptodsrate=ptodRates.get(0);
					ptodrate=ptodsrate.getPtodRate();
				}
				}			
				
				RemainingLeaveInput remainingleave=new RemainingLeaveInput();
				if(balance.getDaysremain()!=null && balance.getDaysremain()!=0.0){					
					remainingleave.setAmount(ptodrate*balance.getDaysremain());
				}else{					
					remainingleave.setAmount(ptodrate*0.0);
				}				
				remainingleave.setEmployeesId((balance.getEmpname()!=null)?balance.getEmpname().getStaffId():"");
				remainingleave.setEmployees((balance.getEmpname()!=null)?balance.getEmpname().getFullName():"");
				remainingleave.setCategory((balance.getEmpcategory()!=null)?balance.getEmpcategory().getName():"");
				remainingleave.setCompany((balance.getCompany()!=null)?balance.getCompany().getName():"");
				remainingleave.setTerminal((balance.getTerminal()!=null)?balance.getTerminal().getName():"");
				remainingleave.setDaysEarned((balance.getDayssbalance()!=null)?balance.getDayssbalance():0.0);
				remainingleave.setDaysAccrude((balance.getDaysaccrude()!=null)?balance.getDaysaccrude():0.0);
				remainingleave.setDaysAvailable((balance.getDaysavailable()!=null)?balance.getDaysavailable():0.0);
				remainingleave.setLeaveType(balance.getLeavetype().getName());
				remainingleave.setDaysused((balance.getDaysused()!=null)?balance.getDaysused():0.0);
				remainingleave.setDaysremain((balance.getDaysremain()!=null)?balance.getDaysremain():0.0);
				remainingleave.setHoursEarned((balance.getHoursbalance()!=null)?balance.getHoursbalance():0.0);
				remainingleave.setHoursAccrued((balance.getHoursaccrude()!=null)?balance.getHoursaccrude():0.0);
				remainingleave.setHoursAvailable((balance.getHoursavailable()!=null)?balance.getHoursavailable():0.0);
				remainingleave.setHoursused((balance.getHoursused()!=null)?balance.getHoursused():0.0);
				remainingleave.setHourremain((balance.getHourremain()!=null)?balance.getHourremain():0.0);
				remainingleave.setNote(balance.getNote()!=null?balance.getNote():"");
				out.add(remainingleave);
			
			}
			
		return out;
	}
	
	private List<Driver> retrieveEmployees(LeaveAccrualReport input, Date searchDateTo) {
		//Map<String, Object> criterias = new HashMap<String, Object>();
		
		StringBuffer query = new StringBuffer("select obj from Driver obj where 1=1");
		
		//criterias.put("status", 1);
		query.append(" and (");
		query.append("(obj.status = 1)");
		query.append(" OR (obj.status = 0 AND obj.dateTerminated > '"+mysqldf.format(searchDateTo)+"'" + ")");
		query.append(")");
		
		String company = input.getCompany();
		if (StringUtils.isNotEmpty(company)) {
			//criterias.put("company.id", Long.valueOf(company));
			query.append(" and obj.company = " + company);
		}
		
		String terminal = input.getTerminal();
		if (StringUtils.isNotEmpty(terminal)) {
			//criterias.put("terminal.id", Long.valueOf(terminal));
			query.append(" and obj.terminal = " + terminal);
		}
		
		String employee = input.getEmployee();
		if (StringUtils.isNotEmpty(employee)) {
			//criterias.put("id", Long.valueOf(employee));
			query.append(" and obj.id = " + employee);
		}
		
		String category = input.getCategory();
		if (StringUtils.isNotEmpty(category)) {
			//criterias.put("catagory.id", Long.valueOf(category));
			query.append(" and obj.catagory = " + category);
		}
		
		query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.fullName asc");
		
		//List<Driver> employees = genericDAO.findByCriteria(Driver.class, criterias, "fullName", false);
		List<Driver> employees = genericDAO.executeSimpleQuery(query.toString()); 
		return employees;
	}
	
	private LeaveCurrentBalance retrieveLeaveCurrentBalance(Long employee, String leaveType,
			Date searchDateFrom, Date searchDateTo) {
		StringBuffer query = new StringBuffer();
		query.append("select obj from LeaveCurrentBalance obj where 1=1");
		query.append(" and obj.empname = " + employee);
		query.append(" and obj.leavetype = " + leaveType);
		query.append(" and obj.dateEffectiveFrom >= '" + mysqldf.format(searchDateFrom)+"'");
		query.append(" and obj.dateEffectiveFrom <= '" + mysqldf.format(searchDateTo)+"'");
		
		List<LeaveCurrentBalance> leaveCurrentBalances = genericDAO.executeSimpleQuery(query.toString()); 
		return leaveCurrentBalances.isEmpty() ? null : leaveCurrentBalances.get(0);
	}
	
	private List<Ptodapplication> retrievePtodApplication(Long employee, String leaveType,
			Date searchDateFrom, Date searchDateTo) {
		StringBuffer query = new StringBuffer();
		query.append("select obj from Ptodapplication obj where 1=1");
		query.append(" and obj.driver = " + employee);
		query.append(" and obj.leavetype = " + leaveType);
		query.append(" and obj.batchdate >= '" + mysqldf.format(searchDateFrom)+"'");
		query.append(" and obj.batchdate <= '" + mysqldf.format(searchDateTo)+"'");
		
		List<Ptodapplication> ptodapplicationList = genericDAO.executeSimpleQuery(query.toString()); 
		return ptodapplicationList;
	}
	
	private Double retrievePtodRate(Driver anEmployee, String leaveType) {
		String ptodQuery = "select obj from Ptod obj where obj.company="+anEmployee.getCompany().getId()
			+" and obj.terminal="+anEmployee.getTerminal().getId()
			+" and obj.category="+anEmployee.getCatagory().getId()
			+" and obj.leavetype="+leaveType+" and obj.status=1";
		List<Ptod> ptodList = genericDAO.executeSimpleQuery(ptodQuery);
		
		Double ptodRate = 0.0;
		if (ptodList.isEmpty()){
			return ptodRate;
		}
		
		Ptod ptod = ptodList.get(0);
		if (ptod.getCalculateFlag() == 0) {
			ptodRate = ptod.getRate();	
		} else {
			String ptodRateQuery="select obj from PtodRate obj where obj.driver="+anEmployee.getId()
			+" and obj.catagory="+anEmployee.getCatagory().getId()+" and obj.company="+anEmployee.getCompany().getId()
			+" and obj.terminal="+anEmployee.getTerminal().getId();
			List<PtodRate> ptodRates = genericDAO.executeSimpleQuery(ptodRateQuery);
			if (!ptodRates.isEmpty()){
				ptodRate = ptodRates.get(0).getPtodRate();
			}
		}
		
		return ptodRate == null ? 0.0 : ptodRate;
	}
	
	private Double retrieveHourlyRate(Driver anEmployee, Date searchDateTo) {
		String query = "select obj from HourlyRate obj where obj.driver="+anEmployee.getId();
		query += " and obj.catagory="+anEmployee.getCatagory().getId();
		query += " and obj.company="+anEmployee.getCompany().getId();
		query += " and obj.terminal="+anEmployee.getTerminal().getId();
		
		query += " and '"+mysqldf.format(searchDateTo)+"' BETWEEN obj.validFrom and obj.validTo";
		
		List<HourlyRate> hourlyRates = genericDAO.executeSimpleQuery(query);
		Double hourlyRate = 0.0;
		if (!hourlyRates.isEmpty()) {
			hourlyRate = hourlyRates.get(0).getHourlyRegularRate();
		}
		
		return hourlyRate == null ? 0.0 : hourlyRate;
	}
	
	private Double retrieveWeeklyRate(Driver anEmployee, Date searchDateTo) {
		String query = "select obj from WeeklySalary obj where obj.driver="+anEmployee.getId();
		query += " and obj.catagory="+anEmployee.getCatagory().getId();
		query += " and obj.company="+anEmployee.getCompany().getId();
		query += " and obj.terminal="+anEmployee.getTerminal().getId();
		
		query += " and '"+mysqldf.format(searchDateTo)+"' BETWEEN obj.validFrom and obj.validTo";
		
		List<WeeklySalary> weeklySalaryRates = genericDAO.executeSimpleQuery(query);
		Double weeklySalaryRate = 0.0;
		if (!weeklySalaryRates.isEmpty()) {
			weeklySalaryRate = weeklySalaryRates.get(0).getDailySalary();
		}
		
		return weeklySalaryRate == null ? 0.0 : weeklySalaryRate;
	}
	
	private LeaveType retrieveLeaveType(String leaveType) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("id", Long.valueOf(leaveType));
		
		List<LeaveType> leaveTypes = genericDAO.findByCriteria(LeaveType.class, criterias, null, false);
		return leaveTypes.get(0);
	}
	
	@Override
	public List<LeaveAccrualReport> generateLeaveAccrualReport(SearchCriteria criteria, 
			LeaveAccrualReport input) {
		int accrualYear = input.getAccrualYear();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, accrualYear);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date searchDateFrom = cal.getTime();
		
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date searchDateTo = cal.getTime();
		
		List<LeaveAccrualReport> outLeaveAccrualReportList = new ArrayList<LeaveAccrualReport>();
		
		List<Driver> employees = retrieveEmployees(input, searchDateTo);
		if (employees.isEmpty()) {
			return outLeaveAccrualReportList;
		}
		
		String leaveType = input.getLeaveType();
		for (Driver anEmployee : employees) {
			Long employeeId = anEmployee.getId();
		
			LeaveCurrentBalance leaveCurrentBalance = retrieveLeaveCurrentBalance(employeeId, leaveType, 
					searchDateFrom, searchDateTo);
			if (leaveCurrentBalance == null) {
				continue;
			}
			
			Double daysAvailable = leaveCurrentBalance.getDaysavailable();
			if (daysAvailable == null) {
				daysAvailable = 0.0;
			}
			Double hoursAvailable = leaveCurrentBalance.getHoursavailable();
			if (hoursAvailable == null) {
				hoursAvailable = 0.0;
			}
			Date effectiveFromDate = leaveCurrentBalance.getDateEffectiveFrom();
			
			List<Ptodapplication> ptodapplicationList = retrievePtodApplication(employeeId, leaveType,
					effectiveFromDate, searchDateTo);
			Double daysPaid = 0.0;
			Double daysPaidOut = 0.0;
			Double hoursPaid = 0.0;
			Double hoursPaidOut = 0.0;
			for (Ptodapplication aPtodapplication : ptodapplicationList) {
				if (aPtodapplication.getDayspaid() != null) {
					daysPaid += aPtodapplication.getDayspaid();
				}
				if (aPtodapplication.getPaidoutdays() != null) {
					daysPaidOut += aPtodapplication.getPaidoutdays();
				}
				if (aPtodapplication.getHourspaid() != null) {
					hoursPaid += aPtodapplication.getHourspaid();
				}
				if (aPtodapplication.getPaidouthours() != null) {
					hoursPaidOut += aPtodapplication.getPaidouthours();
				}
				/*if (aPtodapplication.getPtodrates() != null) {
					dailyRate = aPtodapplication.getPtodrates();
				}
				if (aPtodapplication.getPtodhourlyrate() != null) {
					hourlyRate = aPtodapplication.getPtodhourlyrate();
				}*/
			}
			
			Double totalDaysPaid = daysPaid + daysPaidOut;
			Double totalHoursPaid = hoursPaid + hoursPaidOut;
			
			/*if (totalHoursPaid == 0.0 && hourlyRate != 0.0) {
				hourlyRate = 0.0;
			}*/
			
			Double daysRemaining = daysAvailable - totalDaysPaid;
			Double hoursRemaining = hoursAvailable - totalHoursPaid;
			
			Double dailyRate = 0.0;
			Double hourlyRate = 0.0;
			String payTerm = anEmployee.getPayTerm();
			if ("3".equals(payTerm)) {
				dailyRate = retrieveWeeklyRate(anEmployee, searchDateTo);
			} else if ("1".equals(payTerm)){
				dailyRate = retrievePtodRate(anEmployee, leaveType);
			} else if ("2".equals(payTerm)){
				hourlyRate = retrieveHourlyRate(anEmployee, searchDateTo);
			}
			
			Double dailyAmount = daysRemaining * dailyRate;
			Double hourlyAmount = hoursRemaining * hourlyRate;
			
			String leaveTypeName = retrieveLeaveType(leaveType).getName();
			
			LeaveAccrualReport outLeaveAccrualReport = new LeaveAccrualReport();
			outLeaveAccrualReport.setEmployeeStaffId(anEmployee.getStaffId());
			outLeaveAccrualReport.setEmployee(anEmployee.getFullName());
			outLeaveAccrualReport.setCategory(anEmployee.getCatagory().getName());
			outLeaveAccrualReport.setCompany(anEmployee.getCompany().getName());
			outLeaveAccrualReport.setTerminal(anEmployee.getTerminal().getName());
			outLeaveAccrualReport.setLeaveType(leaveTypeName);
			outLeaveAccrualReport.setDaysAvailable(daysAvailable);
			outLeaveAccrualReport.setDaysUsed(totalDaysPaid);
			outLeaveAccrualReport.setDaysRemaining(daysRemaining);
			outLeaveAccrualReport.setDailyRate(dailyRate);
			outLeaveAccrualReport.setDailyAmount(dailyAmount);
			outLeaveAccrualReport.setHoursAvailable(hoursAvailable);
			outLeaveAccrualReport.setHoursUsed(totalHoursPaid);
			outLeaveAccrualReport.setHoursRemaining(hoursRemaining);
			outLeaveAccrualReport.setHourlyRate(hourlyRate);
			outLeaveAccrualReport.setHourlyAmount(hourlyAmount);
			
			outLeaveAccrualReportList.add(outLeaveAccrualReport);
		}	
		
		return outLeaveAccrualReportList;
	}
	
	public EmployeePayrollWrapper  generateEmployeePayrollData(SearchCriteria criteria,EmployeePayrollInput input)
	{
		
		Map<String, String> params = new HashMap<String, String>();
		String batchDatefrom1 = (String) criteria.getSearchMap().get("DateFrom");
		String batchDateto1 = (String) criteria.getSearchMap().get("DateTo");
		
		
		String batchDatefrom = ReportDateUtil.getFromDate(input.getBatchDatefrom());
		String batchDateto = ReportDateUtil.getToDate(input.getBatchDateto());
		
		
		
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String employee = input.getDriver();
		String category = input.getCategory();
		String status=input.getHourlypayrollstatus();
		/*String employeeno = input.getEmployeesNo();*/
		
		StringBuffer query = new StringBuffer("");
		StringBuffer queryCount = new StringBuffer("");
		query.append("select obj from TimeSheet obj where 1=1");
		queryCount.append("select obj from TimeSheet obj where 1=1 ");
		if (!StringUtils.isEmpty(company)) {
		query.append("and  obj.company in (" + company + ")");
		queryCount.append(" and obj.company in (" + company + ")");
		}
		
		if (!StringUtils.isEmpty(terminal)) {
			query.append("and  obj.terminal in (" + terminal + ")");
			queryCount.append(" and obj.terminal in (" + terminal + ")");
		}
		
		
		if (!StringUtils.isEmpty(employee)) {
			
			String empNames="";
			String[]employees=employee.split(",");
			for(int i=0;i<employees.length;i++){
				if(empNames.equals("")){
					empNames="'"+employees[i]+"'";
				}
				else{
					empNames=empNames+",'"+employees[i]+"'";
				}					
			}					
			String empQury="select obj from Driver obj where 1=1 and obj.fullName in ("+empNames+")";
			List<Driver> empObj=genericDAO.executeSimpleQuery(empQury);
			String empids="";			
			if(empObj.size()>0 && empObj!=null){
				for(Driver obj:empObj){
					if(empids.equals("")){
						empids=String.valueOf(obj.getId());
					}
					else{
						empids=empids+","+obj.getId();
					}
				}
			}			
			query.append("and  obj.driver in (" + empids + ")");
			queryCount.append(" and obj.driver in (" + empids + ")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.catagory in (" + category + ")");
			queryCount.append(" and obj.catagory in (" + category + ")");
		}
		
		if (!StringUtils.isEmpty(batchDatefrom) && !StringUtils.isEmpty(batchDateto)) {
			query.append("and  obj.batchdate >= '"
					+ batchDatefrom + "' and obj.batchdate <='" + batchDateto
					+ "'");
			queryCount.append("and  obj.batchdate between '"
					+ batchDatefrom + "' and obj.batchdate <='" + batchDateto
					+ "'");
		}
		if (!StringUtils.isEmpty(status)) {
			System.out.println("\nobj.hourlypayrollstatus not null\n");
			query.append("and  obj.hourlypayrollstatus in (" + status + ")");
			queryCount.append(" and obj.hourlypayrollstatus in (" + status + ")");
		}		
		List<TimeSheet> ts = genericDAO.executeSimpleQuery(query.toString());
		
		List<TimeSheet> summarys = new ArrayList<TimeSheet>();
		EmployeePayrollWrapper wrapper=new EmployeePayrollWrapper();
		double sumOftotAmount=0.0;
		double sumdtAmount=0.0;
		double sumotAmount=0.0;
		double sumregularAmount=0.0;
		double sumHolidayAmount=0.0;
		
		//wrapper.setAttendance(summarys);
		wrapper.setTimesheets(summarys);
		for (TimeSheet timeSheet : ts) {
			Double rHours=0.0;
			Double otHours=0.0;
			Double dtHours=0.0;
			Double holidayHours=0.0;
			
			Double rRate=0.0;
			Double otRate=0.0;
			double dtRate=0.0;
			Double holidayRate=0.0;
			
			Double rAmount=0.0;
			Double otAmount=0.0;
			Double dtAmount=0.0;
			Double totAmount=0.0;
			Double holidayAmount=0.0;
			
			Double vacationAmount=0.0;
			Double sickParsanolAmount=0.0;
			Double bonusAmounts=0.0;
			
			Double sumOfTotVacSicBonus=0.0;
			
			if (timeSheet != null) {
				TimeSheet output = new TimeSheet();
				
				output.setBatchDates((timeSheet.getBatchdate()!=null) ?sdf.format(timeSheet.getBatchdate()):"");
				output.setEmployeesNo((timeSheet.getDriver() != null) ? timeSheet.getDriver().getStaffId(): "");
				output.setEmployees((timeSheet.getDriver() != null) ? timeSheet.getDriver().getFullName() : "");
				output.setCategories((timeSheet.getCatagory().getName() != null) ? timeSheet.getCatagory().getName() : "");
				output.setCompanies((timeSheet.getCompany() != null) ? timeSheet.getCompany().getName() : "");
				output.setTerminals((timeSheet.getTerminal() != null) ? timeSheet.getTerminal().getName() : "");
				String query1="select obj from HourlyRate obj where obj.driver="+timeSheet.getDriver().getId()+" and obj.catagory="+timeSheet.getCatagory().getId()+" and obj.company="+timeSheet.getCompany().getId()+" and obj.terminal="+timeSheet.getTerminal().getId()+"and '"+timeSheet.getBatchdate()+"' BETWEEN obj.validFrom and obj.validTo";
				List<HourlyRate> hourlyRates=genericDAO.executeSimpleQuery(query1);
				boolean cal=false;
				HourlyRate rate=null;
				if(!hourlyRates.isEmpty()){
					cal=true;
					 rate=hourlyRates.get(0);
				}
				
				Driver emp=genericDAO.getById(Driver.class, timeSheet.getDriver().getId());
				
				if(timeSheet.getRegularhours()!=null){
					if(timeSheet.getHolidayhours()!=null){
						rHours=timeSheet.getRegularhours()-timeSheet.getHolidayhours();
					}
					else{
						rHours=timeSheet.getRegularhours();
					}
					
					
					 if(cal&& rate.getHourlyRegularRate()!=null){
				         rRate=rate.getHourlyRegularRate();
			             rAmount=rHours*rate.getHourlyRegularRate();
			   }
					  /* if( emp.getHourlyRegularRate()!=null){
						         rRate=emp.getHourlyRegularRate();
					             rAmount=timeSheet.getRegularhours()*emp.getHourlyRegularRate();
					   }*/
			  }
				
				
				
				if(timeSheet.getHolidayhours()!=null){
					holidayHours=timeSheet.getHolidayhours();
					
					 if(cal&& rate.getHourlyRegularRate()!=null){
						 holidayRate=rate.getHourlyRegularRate();
						 holidayAmount=timeSheet.getHolidayhours()*rate.getHourlyRegularRate();
					 }
				}
				
				
				
				
				if(timeSheet.getTotalothoursinweek()!=null){
					otHours=timeSheet.getTotalothoursinweek();
					 if(cal&&rate.getHourlyOvertimeRate()!=null){
						 otRate=rate.getHourlyOvertimeRate()*rate.getHourlyRegularRate();
					     //otAmount=timeSheet.getTotalothoursinweek()*emp.getHourlyOvertimeRate();
						 //otRate=MathUtil.roundUp(otRate,2);
						 otAmount=timeSheet.getTotalothoursinweek()*otRate;
						 System.out.println("\notAmount=======>"+otAmount+"\n");
					}
				/*	 if(emp.getHourlyOvertimeRate()!=null){
						 otRate=emp.getHourlyOvertimeRate()*emp.getHourlyRegularRate();
					     //otAmount=timeSheet.getTotalothoursinweek()*emp.getHourlyOvertimeRate();
						 otRate=MathUtil.roundUp(otRate,2);
						 otAmount=timeSheet.getTotalothoursinweek()*otRate;
						 System.out.println("\notAmount=======>"+otAmount+"\n");
					}*/
			      }
				
				
				if(cal&&timeSheet.getTotaldthoursinweek()!=null){
					dtHours=timeSheet.getTotaldthoursinweek();
					  if(rate.getHourlyDoubleTimeRate()!=null){
						  dtRate=rate.getHourlyDoubleTimeRate()*rate.getHourlyRegularRate(); 
					    //dtAmount=timeSheet.getTotaldthoursinweek()*emp.getHourlyDoubleTimeRate();
						  //dtRate=MathUtil.roundUp(dtRate,2);
						  dtAmount=timeSheet.getTotaldthoursinweek()*dtRate;
						  System.out.println("\ndtAmount=======>"+dtAmount+"\n");
					}
					/*  if(emp.getHourlyDoubleTimeRate()!=null){
						  dtRate=emp.getHourlyDoubleTimeRate()*emp.getHourlyRegularRate(); 
					    //dtAmount=timeSheet.getTotaldthoursinweek()*emp.getHourlyDoubleTimeRate();
						  dtRate=MathUtil.roundUp(dtRate,2);
						  dtAmount=timeSheet.getTotaldthoursinweek()*dtRate;
						  System.out.println("\ndtAmount=======>"+dtAmount+"\n");
					}*/
			       }
				//calculating Vacation Amount and Sick/Vacation pay(amount)
				Long empid=timeSheet.getDriver().getId();
				Long empcategory=timeSheet.getCatagory().getId();
				Date ptodappBatchdate=timeSheet.getBatchdate();
				String ptodAppQuery = "select obj from Ptodapplication obj where obj.driver='"
					+ timeSheet.getDriver().getId() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"' and batchdate='" +mysqldf.format(timeSheet.getBatchdate())+"'";
		      
				List<Ptodapplication>	ptotAppl = genericDAO.executeSimpleQuery(ptodAppQuery);
				//System.out.println("\nptotAppl.size()=>" + ptotAppl.size() + "\n");
				for(Ptodapplication ptodap:ptotAppl){
					if(ptodap.getLeavetype().getId()==1){
						sickParsanolAmount=sickParsanolAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());;
					}
					if(ptodap.getLeavetype().getId()==4){
						vacationAmount=vacationAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());
						
					}
					
				}
				//
				
				//calculating Bonus Amount 
				/*String empBonus = "select obj from EmployeeBonus obj where obj.employee='"
					+ timeSheet.getDriver().getId() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"' and batchFrom<='" +mysqldf.format(timeSheet.getBatchdate())+"'and batchTo>='"+mysqldf.format(timeSheet.getBatchdate())+"'";
				*/
				String empBonus = "select obj from EmployeeBonus obj where obj.driver='"
					+ timeSheet.getDriver().getId() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"' and batchFrom='" +mysqldf.format(timeSheet.getBatchdate())+"'";
			    List<EmployeeBonus>	empsBonus = genericDAO.executeSimpleQuery(empBonus);
				//System.out.println("\nempBonusQuery====>"+empBonus+"\n");
				EmployeeBonus EmpBonus=null;
				for(EmployeeBonus enmBonusObj:empsBonus){
					//bonusAmounts=bonusAmounts+enmBonusObj.getBonusamount();   
					//System.out.println("\nbonusAmounts====>"+enmBonusObj.getId()+"\n");
					EmpBonus=enmBonusObj;
				}
				
				if(EmpBonus !=null){
				 List<EmpBonusTypesList> listEmpBonud=EmpBonus.getBonusTypesLists();
				for(EmpBonusTypesList enmBonusListObj:listEmpBonud){
						bonusAmounts=bonusAmounts+enmBonusListObj.getBonusamount();   
						//System.out.println("\nbonusAmounts====>"+bonusAmounts+"\n");
					
					}
				}
				 
				/*Double holidayAmount=0.00;
				StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+timeSheet.getCompany().getId()+" and obj.terminal="+timeSheet.getTerminal().getId()+" and obj.catagory="+timeSheet.getCatagory().getId()+" and obj.leaveType=3");
						holidayquery.append(" and obj.batchdate<='"+timeSheet.getBatchdate()+"'");
						holidayquery.append(" and obj.batchdate<='"+timeSheet.getBatchdate()+"'");
                        List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
					for(HolidayType type:holidayTypes){
						if(type.getAmount()!=null)
							holidayAmount=holidayAmount+type.getAmount();
					}
					*/
				 
				 
				
				totAmount=rAmount+otAmount+dtAmount+holidayAmount;
				
				sumOftotAmount=sumOftotAmount+totAmount;
				sumdtAmount=sumdtAmount+dtAmount;
				sumotAmount=sumotAmount+otAmount;
				sumHolidayAmount=sumHolidayAmount+holidayAmount;
				sumregularAmount=sumregularAmount+rAmount;
				
				output.setrHours(rHours);
				output.setOtHours(otHours);
				output.setDtHours(dtHours);
				
				output.setrRate(rRate);
				output.setOtRate(otRate);
				output.setDtRate(dtRate);
				
				output.setRegularamounts(rAmount);
				output.setOtamounts(otAmount);
				output.setDtamounts(dtAmount);
				output.setTotamounts(totAmount);
				
				output.setVacationAmount(vacationAmount);
				output.setSickParsanolAmount(sickParsanolAmount);
				output.setBonusAmounts(bonusAmounts);
				output.setHolidayAmount(holidayAmount);
				
				
				sumOfTotVacSicBonus = totAmount + vacationAmount + sickParsanolAmount + bonusAmounts;
				output.setSumOfTotVacSicBonus(sumOfTotVacSicBonus);
				summarys.add(output);
				
				
			}
		}
		
		sumOftotAmount = MathUtil.roundUp(sumOftotAmount, 2);
		sumdtAmount = MathUtil.roundUp(sumdtAmount, 2);
		sumotAmount = MathUtil.roundUp(sumotAmount, 2);
		sumregularAmount = MathUtil.roundUp(sumregularAmount, 2);
		
		wrapper.setSumtotalAmount(sumOftotAmount);
		wrapper.setSumdtAmount(sumdtAmount);
		wrapper.setSumotAmount(sumotAmount);
		wrapper.setSumregularAmount(sumregularAmount);
		//System.out.println("sumOftotAmount====="+wrapper.getSumtotalAmount()+"\n");
		return wrapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveDriverPayData(HttpServletRequest request,
			SearchCriteria criteria) throws Exception {
		// TODO Auto-generated method stub
		Map criterias = new HashMap();
		String driverid=(String) criteria.getSearchMap().get("driver");
		String frombatch=(String) criteria.getSearchMap().get("fromDate");
		String tobatch=(String) criteria.getSearchMap().get("toDate");
		String company=(String) criteria.getSearchMap().get("company");
		String terminal=(String) criteria.getSearchMap().get("terminal");
		String expire= (String) criteria.getSearchMap().get("expire");
		String status=(String)criteria.getSearchMap().get("pay");
		
		 
		 
		 
		 
		 if(StringUtils.isEmpty(status)){
			 status="1";
		 }else{
			 status="2";
		 }
		frombatch=ReportDateUtil.getFromDate(frombatch);
		tobatch=ReportDateUtil.getFromDate(tobatch);
		 String sum= (String) criteria.getSearchMap().get("summary");
			String payrollDate=(String) criteria.getSearchMap().get("payrollDate");
			String driversmul=(String)criteria.getSearchMap().get("driversmul");
			Driver driver=null;
			if(!StringUtils.isEmpty(driverid)){			
				criterias.put("fullName",driverid);
				criterias.put("status",1);
				driver=genericDAO.getByCriteria(Driver.class, criterias);
			}
		/* if(StringUtils.contains(expire, "0")){
			 request.getSession().setAttribute("error", "Please Validate Rate Expiration");
			 throw new Exception("Rate Validation not done");
		 }*/
		 if(StringUtils.isEmpty(company)){
			 request.getSession().setAttribute("error", "Please Select Company");
			 throw new Exception("Company is Null"); 
		 }
		 if(StringUtils.isEmpty(payrollDate)){
			 request.getSession().setAttribute("error", "Please Enter Payroll Date");
			 throw new Exception("payrollDate is Null"); 
		 }
		 
		 
		 
		 Location companylocation=null;
		 Location terminallocation=null;
		 String driverIds="-1";
		 String driverNames  = "";
		 
		 String driverIdsWithCompanySelected="-1";		 
		 if(!StringUtils.isEmpty(company)){
		    companylocation=genericDAO.getById(Location.class, Long.parseLong(company));
		 }
		 if(!StringUtils.isEmpty(terminal)){
			    terminallocation=genericDAO.getById(Location.class, Long.parseLong(terminal));
		 }
		 
		 
		 
		 
		 if(!StringUtils.isEmpty(driverid)){			
				criterias.put("fullName",driverid);
				//criterias.put("status",1);
				List<Driver> drivers = genericDAO.findByCriteria(Driver.class, criterias);
				for(Driver driverObj:drivers){
					 if(driverObj.getPayTerm().equals("1")){
						driverIdsWithCompanySelected = driverIdsWithCompanySelected +","+driverObj.getId().toString();
					 }
				 }
		}
		 
		 
		 DriverPayWrapper wrapper=generateDriverPayReport(criteria);
		 
		 Date batchFrom=null;
		 if(wrapper.getBatchDateFrom()!=null){
		    batchFrom = new SimpleDateFormat("MM-dd-yyyy")
			.parse(wrapper.getBatchDateFrom());
		 
		 }
		 
		 Date batchto=null;
		 if(wrapper.getBatchDateTo()!=null){
			 batchto = new SimpleDateFormat("MM-dd-yyyy")
				.parse(wrapper.getBatchDateTo()); 			 
		 }
		 
		 Date payrollbatch =null;
		 if(wrapper.getPayRollBatch()!=null){
			 payrollbatch = new SimpleDateFormat("MM-dd-yyyy")
				.parse(wrapper.getPayRollBatch()); 		 
		 }		 
		 
		 
		 
		 Map<Long,Long> terminalIds =  new HashMap<Long,Long>();
		 
		 if(wrapper.getDriverPays().size()>0){
			 
			 List<DriverPay> summary = wrapper.getDriverPays();
			
		     Comparator<DriverPay> comparator3=new Comparator<DriverPay>() {
					@Override
					public int compare(DriverPay o1, DriverPay o2) {
						return  o1.getDrivername().compareTo(o2.getDrivername());
					}
			 };
				
			 ComparatorChain chain = new ComparatorChain();  
			 chain.addComparator(comparator3);
			 Collections.sort(summary, chain);
			 wrapper.setDriverPays(summary);
			 
		 for(DriverPay pay:wrapper.getDriverPays()){	
			 if(driverNames.equals("")){
				 driverNames = "'"+pay.getDrivername()+"'";
			 }
			 else{
				 driverNames = driverNames +",'"+pay.getDrivername()+"'";
			 }
			  
			 terminalIds.put(pay.getTerminal().getId(),pay.getTerminal().getId());
			 pay.setCompany(wrapper.getCompanylocation());			
			 pay.setPayRollBatch(payrollbatch);
			 pay.setBillBatchDateFrom(batchFrom);
			 pay.setBillBatchDateTo(batchto);
			 genericDAO.save(pay);
		 }
		 
		 if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
			 String driverQuery = "Select obj from Driver obj where obj.fullName in ("+driverNames+")";
			 List<Driver> drivers = genericDAO.executeSimpleQuery(driverQuery);
			 
			 for(Driver driverObj:drivers){
				driverIds = driverIds +","+driverObj.getId().toString();			
			 }
		}
		 
		 
		 }
		 
		 
		 
		 
		 if(wrapper.getTerminal()==null && wrapper.getCompanylocation().getId()==5){
			
			 
			 for(Long key: terminalIds.keySet()){				 
				 			 
				List<Location> terminals = genericDAO.executeSimpleQuery("select obj from Location obj where obj.id in ("+terminalIds.get(key)+") order by obj.name");
				 
				DriverPayroll payroll=new DriverPayroll();
				 
				 payroll.setBillBatchFrom(batchFrom);
				 payroll.setBillBatchTo(batchto);
				 payroll.setPayRollBatch(payrollbatch);
			 
				 if(wrapper.getCompanylocation()!=null){
					 payroll.setCompany(wrapper.getCompanylocation());
				 }				
				 payroll.setTerminal(terminals.get(0));				 
				 payroll.setSumTotal(wrapper.getSumTotal());
				 payroll.setTotalRowCount(wrapper.getTotalRowCount());
			 	 payroll.setSumAmount(wrapper.getSumAmount());
			 	 if(wrapper.getDriverPays().size()>0)
			 		genericDAO.saveOrUpdate(payroll);
			}
		 }
		 else{
			 DriverPayroll payroll=new DriverPayroll();
		 
			 payroll.setBillBatchFrom(batchFrom);
			 payroll.setBillBatchTo(batchto);
			 payroll.setPayRollBatch(payrollbatch);
		 
			 if(wrapper.getCompanylocation()!=null){
				 payroll.setCompany(wrapper.getCompanylocation());
			 }
			 
			 if(wrapper.getTerminal()!=null){
				 payroll.setTerminal(wrapper.getTerminal());
			 }
			 
			 payroll.setSumTotal(wrapper.getSumTotal());
			 payroll.setTotalRowCount(wrapper.getTotalRowCount());
		 	 payroll.setSumAmount(wrapper.getSumAmount());
		 	 
		 	 if(wrapper.getDriverPays().size()>0)
		 		genericDAO.saveOrUpdate(payroll);		 
	    }
		 
		 
		 StringBuffer query=new StringBuffer("update Ticket t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1" );
		 StringBuffer ptodquery=new StringBuffer("update Ptodapplication t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1 and t.category=2");
		 StringBuffer holidayquery=new StringBuffer("update HolidayType t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1 and t.leaveType=3" );
		 StringBuffer empbonusquery=new StringBuffer("update EmployeeBonus t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1 and t.category=2");
		 StringBuffer miscAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1 and t.miscNotes!='Reimbursement' and t.miscType!='Quarter Bonus'");
		 StringBuffer reimAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1 and t.miscNotes='Reimbursement'" );
		 StringBuffer quarterBonusquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=2,t.payRollBatch='"+mysqldf.format(payrollbatch)+"' where 1=1 and  t.payRollStatus=1 and t.miscType='Quarter Bonus'" );
		 if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
			 query.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
			 ptodquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 holidayquery.append(" and t.company="+company);
        	 empbonusquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 miscAmountquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 reimAmountquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 quarterBonusquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
         }
         else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
        	 query.append(" and t.driver in (").append(driverIds).append(")");
        	 ptodquery.append(" and t.driver in (").append(driverIds).append(")");
        	 holidayquery.append(" and t.company in (").append(company).append(")");
        	 empbonusquery.append(" and t.driver in (").append(driverIds).append(")");
        	 miscAmountquery.append(" and t.driver in (").append(driverIds).append(")");
        	 reimAmountquery.append(" and t.driver in (").append(driverIds).append(")");
        	 quarterBonusquery.append(" and t.driver in (").append(driverIds).append(")");
         }
         
         if(!StringUtils.isEmpty(driverid)){        	
        	 query.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
			 ptodquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 //holidayquery.append(" and t.company="+company);
        	 empbonusquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 miscAmountquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 reimAmountquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
        	 quarterBonusquery.append(" and t.driver in ("+driverIdsWithCompanySelected+")");
         }
		
		if(!StringUtils.isEmpty(frombatch)){
			query.append("and t.billBatch>='"+frombatch+"'");
			ptodquery.append(" and t.batchdate>='"+frombatch+"'");
			holidayquery.append(" and t.batchdate>='"+frombatch+"'");
			empbonusquery.append(" and t.batchFrom>='"+frombatch+"'");
			miscAmountquery.append(" and t.batchFrom>='"+frombatch+"'");
			reimAmountquery.append(" and t.batchFrom>='"+frombatch+"'");
			quarterBonusquery.append(" and t.batchFrom>='"+frombatch+"'");
		}
		if(!StringUtils.isEmpty(tobatch)){
			query.append(" and t.billBatch<='"+tobatch+"'");
			ptodquery.append(" and t.batchdate<='"+tobatch+"'");
			holidayquery.append(" and t.batchdate<='"+tobatch+"'");
			empbonusquery.append(" and t.batchTo<='"+tobatch+"'");
			miscAmountquery.append(" and t.batchTo<='"+tobatch+"'");
			reimAmountquery.append(" and t.batchTo<='"+tobatch+"'");
			quarterBonusquery.append(" and t.batchTo<='"+tobatch+"'");
		}
		 if(!StringUtils.isEmpty(terminal)){
	            query.append(" and t.terminal="+terminal);
	            ptodquery.append(" and t.terminal="+terminal);
				holidayquery.append(" and t.terminal="+terminal);
				empbonusquery.append(" and t.terminal="+terminal);
				miscAmountquery.append(" and t.terminal="+terminal);
				reimAmountquery.append(" and t.terminal="+terminal);
				quarterBonusquery.append(" and t.terminal="+terminal);
	     }
		 if(!StringUtils.isEmpty(driversmul)){
			 query.append(" and t.driver not in ("+driversmul+")");
			 ptodquery.append(" and t.driver not in ("+driversmul+")");        	 
        	 empbonusquery.append(" and t.driver not in ("+driversmul+")");
        	 miscAmountquery.append(" and t.driver not in ("+driversmul+")");
        	 reimAmountquery.append(" and t.driver not in ("+driversmul+")");
        	 quarterBonusquery.append(" and t.driver not in ("+driversmul+")");
         }
		 
		  // Double pay fix - 30th Aug 2017
		  //genericDAO.getEntityManager().createQuery(query.toString()).executeUpdate();
		  List<DriverPay> driverPayList = wrapper.getDriverPays();
		  int totalLoads = 0;
		  for (DriverPay aDriverPay : driverPayList) {
			  totalLoads += aDriverPay.getNoOfLoad();
		  }
				 
		  if (totalLoads > 0) {
			  int recordsUpdated = 0;
			  int attempts = 0;
			  while (recordsUpdated <= 0) {
				  attempts++;
				  if (attempts > 1) {
					  System.out.println("Re-trying ticket status update as part of paystub save; attempt no.: " + attempts);
				  }
				  
				  try {
					  recordsUpdated = genericDAO.getEntityManager().createQuery(query.toString()).executeUpdate();
				  } catch (Throwable t) {
					  recordsUpdated = 0;
					  System.out.println("Exception occured while ticket status update as part of paystub save; attempt no.: " + attempts);
					  t.printStackTrace();
				  }
			  }
		  }
		  
		  genericDAO.getEntityManager().createQuery(ptodquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(holidayquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(empbonusquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(miscAmountquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(reimAmountquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(quarterBonusquery.toString()).executeUpdate();
		  
		  for (Map.Entry<Long, Double> entry : wrapper.getDriverPayRateDataMap().entrySet())
			{
			   String tickDrvRateUpdateQuery = "update Ticket t set t.driverPayRate="+entry.getValue()+" where t.id="+entry.getKey();
			   genericDAO.getEntityManager().createQuery(tickDrvRateUpdateQuery).executeUpdate();
			   
			   String billingHistoryDrvRateUpdateQuery = "update Billing_New b set b.driverPayRate="+entry.getValue()+" where b.ticket="+entry.getKey();
			   genericDAO.getEntityManager().createQuery(tickDrvRateUpdateQuery).executeUpdate();
			   
			   
			}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteDriverPayData(DriverPayroll payroll) throws Exception {
		// TODO Auto-generated method stub
		Map criteria = new HashMap();
		String driverNames="";
		criteria.put("company", payroll.getCompany());
		criteria.put("payRollBatch", payroll.getPayRollBatch());
		criteria.put("billBatchDateFrom",payroll.getBillBatchFrom());
		criteria.put("billBatchDateTo",payroll.getBillBatchTo());
		
		if(payroll.getTerminal()!=null)
		   criteria.put("terminal", payroll.getTerminal());
		
		List<DriverPay> datas=genericDAO.findByCriteria(DriverPay.class, criteria);
		if (datas!=null && datas.size()>0) {
			for(DriverPay pay:datas){
				if(driverNames.equals("")){
					 driverNames = "'"+pay.getDrivername()+"'";
				 }
				 else{
					 driverNames = driverNames +",'"+pay.getDrivername()+"'";
				 }
				genericDAO.delete(pay);
			}
		}		 
		 String driverIds="-1";	
		 
		 
		 if(payroll.getCompany()!=null){
			 
				 String driverQuery = "Select obj from Driver obj where obj.fullName in ("+driverNames+")";
				 List<Driver> drivers = genericDAO.executeSimpleQuery(driverQuery);
				 
				 for(Driver driverObj:drivers){
					driverIds = driverIds +","+driverObj.getId().toString();			
				 }
			
		}
		
		
		
		 StringBuffer query=new StringBuffer("update Ticket t set t.payRollStatus=1,t.payRollBatch=null,t.driverPayRate=null where 1=1 and  t.payRollStatus=2" );
		 StringBuffer ptodquery=new StringBuffer("update Ptodapplication t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.category=2");
		 StringBuffer holidayquery=new StringBuffer("update HolidayType t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.leaveType=3" );
		 StringBuffer empbonusquery=new StringBuffer("update EmployeeBonus t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.category=2");
		 StringBuffer miscAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.miscNotes!='Reimbursement' and t.miscType!='Quarter Bonus'");
		 StringBuffer reimAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.miscNotes='Reimbursement'" );	
		 StringBuffer quarterAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.miscType='Quarter Bonus'" );	
		 
		 
		 
		 if(payroll.getCompany()!=null){
			 query.append(" and t.driver in ("+driverIds+")");
			 ptodquery.append(" and t.driver in (").append(driverIds).append(")");
			 holidayquery.append(" and t.company="+payroll.getCompany().getId());
        	 empbonusquery.append(" and t.driver in (").append(driverIds).append(")");
        	 miscAmountquery.append(" and t.driver in (").append(driverIds).append(")");
        	 reimAmountquery.append(" and t.driver in (").append(driverIds).append(")");
        	 quarterAmountquery.append(" and t.driver in (").append(driverIds).append(")");
		 }
		if(payroll.getBillBatchFrom()!=null){
			query.append(" and t.billBatch>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
			ptodquery.append("and t.batchdate>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
			holidayquery.append("and t.batchdate>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
			empbonusquery.append("and t.batchFrom>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
			miscAmountquery.append("and t.batchFrom>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
			reimAmountquery.append("and t.batchFrom>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
			quarterAmountquery.append("and t.batchFrom>='"+mysqldf.format(payroll.getBillBatchFrom())+"'");
		}
		if(payroll.getBillBatchTo()!=null){
			query.append(" and t.billBatch<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
			ptodquery.append("and t.batchdate<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
			holidayquery.append("and t.batchdate<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
			empbonusquery.append("and t.batchTo<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
			miscAmountquery.append("and t.batchTo<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
			reimAmountquery.append("and t.batchTo<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
			quarterAmountquery.append("and t.batchTo<='"+mysqldf.format(payroll.getBillBatchTo())+"'");
		}
		 if(payroll.getTerminal()!=null){
	            query.append(" and t.terminal="+payroll.getTerminal().getId());
	            ptodquery.append(" and t.terminal="+payroll.getTerminal().getId());
				holidayquery.append(" and t.terminal="+payroll.getTerminal().getId());
				empbonusquery.append(" and t.terminal="+payroll.getTerminal().getId());
				miscAmountquery.append(" and t.terminal="+payroll.getTerminal().getId());
				reimAmountquery.append(" and t.terminal="+payroll.getTerminal().getId());
				quarterAmountquery.append(" and t.terminal="+payroll.getTerminal().getId());
	     }
		 if(payroll.getPayRollBatch()!=null){
			 query.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
			 ptodquery.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
			 holidayquery.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
			 empbonusquery.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
			 miscAmountquery.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
			 reimAmountquery.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
			 quarterAmountquery.append(" and t.payRollBatch='"+mysqldf.format(payroll.getPayRollBatch())+"'");
		 }
		 
		 
		genericDAO.getEntityManager().createQuery(query.toString()).executeUpdate();
		genericDAO.getEntityManager().createQuery(ptodquery.toString()).executeUpdate();
		genericDAO.getEntityManager().createQuery(holidayquery.toString()).executeUpdate();
		genericDAO.getEntityManager().createQuery(empbonusquery.toString()).executeUpdate();
		genericDAO.getEntityManager().createQuery(miscAmountquery.toString()).executeUpdate();
		genericDAO.getEntityManager().createQuery(reimAmountquery.toString()).executeUpdate();
		genericDAO.getEntityManager().createQuery(quarterAmountquery.toString()).executeUpdate();
		genericDAO.delete(payroll);
	}
	
	
	@Override
	public EmployeePayrollWrapper generateHourlyPayrollData(SearchCriteria searchCriteria) {
		
		String fromDateStr = (String) searchCriteria.getSearchMap().get("fromDate");
        String toDateStr = (String) searchCriteria.getSearchMap().get("toDate");
		String company = (String) searchCriteria.getSearchMap().get("company");
		String terminal = (String) searchCriteria.getSearchMap().get("terminal");
		String employee = (String) searchCriteria.getSearchMap().get("driver");
		String category = (String) searchCriteria.getSearchMap().get("category");
		
		fromDateStr = ReportDateUtil.getFromDate(fromDateStr);
		toDateStr = ReportDateUtil.getToDate(toDateStr);
		//System.out.println("\ngenerateHourlyPayrollData of HrReportServiceImpl\n");
		StringBuffer query = new StringBuffer("");
		query.append("select obj from TimeSheet obj where obj.status=1 and obj.hourlypayrollstatus=1");
		if (!StringUtils.isEmpty(company)) {
			query.append("and  obj.company=").append(company);
		}
		if (!StringUtils.isEmpty(terminal)) {
			query.append("and  obj.terminal=").append(terminal);
		}
		String driverdIds = "";
		if (!StringUtils.isEmpty(employee)) {						
			query.append("and  obj.driver.fullName='").append(employee).append("'");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.catagory=").append(category);
		}
		if (!StringUtils.isEmpty(fromDateStr)) {
			query.append(" and  obj.batchdate>='").append(fromDateStr + "'");
			System.out.println("\ngenerateHourlyPayrollData -->"+fromDateStr+"\n");
		}
		if (!StringUtils.isEmpty(toDateStr)) {
			query.append(" and  obj.batchdate<='").append(toDateStr + "'");
			System.out.println("\ngenerateHourlyPayrollData -->"+toDateStr+"\n");
		}
		
		
		query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.catagory.name asc, obj.driver.fullName asc, obj.batchdate asc");
		
		
		List<TimeSheet> ts = genericDAO.executeSimpleQuery(query.toString());
		
		/*EmployeePayrollWrapper wrapper=new EmployeePayrollWrapper();
		System.out.println("\ngenerateHourlyPayrollData of HrReportServiceImpl----end\n");
		List<TimeSheet> summarys = new ArrayList<TimeSheet>();*/
		
		List<HourlyPayrollInvoiceDetails> summarys = new ArrayList<HourlyPayrollInvoiceDetails>();
		EmployeePayrollWrapper wrapper=new EmployeePayrollWrapper();
		
		double sumOftotAmount=0.0;
		double sumdtAmount=0.0;
		double sumotAmount=0.0;
		double sumregularAmount=0.0;
		double sumHolidayAmount=0.0;
		List<String> driverList = new ArrayList<String>();
		//wrapper.setTimesheets(summarys);
		wrapper.setInvoicedetails(summarys);
		for (TimeSheet timeSheet : ts) {
			Double rHours=0.0;
			Double otHours=0.0;
			Double dtHours=0.0;			
			Double holidayHours=0.0;
			
			Double rRate=0.0;
			Double otRate=0.0;
			Double dtRate=0.0;
			Double holidayRate=0.0;
			
			Double rAmount=0.0;
			Double otAmount=0.0;
			Double dtAmount=0.0;
			Double totAmount=0.0;
			Double holidayAmount=0.00;
			
			Double vacationAmount=0.0;
			Double sickParsanolAmount=0.0;
			Double bonusAmounts=0.0;
			Double miscamt=0.0;
			Double reimburseamt=0.0;
			
			// Bereavement change
			Double bereavementAmount=0.0;
			
			Double sumOfTotVacSicBonus=0.0;
			
			
			if (timeSheet != null) {
				HourlyPayrollInvoiceDetails output = new HourlyPayrollInvoiceDetails();
				if(timeSheet.getDriver() != null){
					driverList.add(timeSheet.getDriver().getFullName());
				}
				output.setBatchdate((timeSheet.getBatchdate()!=null) ?sdf.format(timeSheet.getBatchdate()):"");
				output.setEmployeesNo((timeSheet.getDriver() != null) ? timeSheet.getDriver().getStaffId(): "");
				output.setDriver((timeSheet.getDriver() != null) ? timeSheet.getDriver().getFullName() : "");
				output.setCategory((timeSheet.getCatagory() != null) ? timeSheet.getCatagory().getName() : "");
				
				output.setCompany((timeSheet.getCompany() != null) ? timeSheet.getCompany().getName() : "");
				output.setTerminal((timeSheet.getTerminal() != null) ? timeSheet.getTerminal().getName() : "");
				output.setTerminalLoc(timeSheet.getTerminal());
				output.setCompanyLoc(timeSheet.getCompany());
				String query1="select obj from HourlyRate obj where obj.driver="+timeSheet.getDriver().getId()+" and obj.catagory="+timeSheet.getCatagory().getId()+" and obj.company="+timeSheet.getCompany().getId()+" and obj.terminal="+timeSheet.getTerminal().getId()+"and '"+timeSheet.getBatchdate()+"' BETWEEN obj.validFrom and obj.validTo";
				List<HourlyRate> hourlyRates=genericDAO.executeSimpleQuery(query1);
				boolean cal=false;
				HourlyRate rate=null;
				if(!hourlyRates.isEmpty()){
					cal=true;
					 rate=hourlyRates.get(0);	
				}
				Driver emp=genericDAO.getById(Driver.class, timeSheet.getDriver().getId());
				if(timeSheet.getRegularhours()!=null){
					if(timeSheet.getHolidayhours()!=null){
						rHours=timeSheet.getRegularhours();
					}
					else{
						rHours=timeSheet.getRegularhours();
					}
					
					
					 if(cal&& rate.getHourlyRegularRate()!=null){
				         rRate=rate.getHourlyRegularRate();
			             rAmount=rHours*rate.getHourlyRegularRate();
			   }
					  /* if( emp.getHourlyRegularRate()!=null){
						         rRate=emp.getHourlyRegularRate();
					             rAmount=timeSheet.getRegularhours()*emp.getHourlyRegularRate();
					   }*/
			  }
				
				if(timeSheet.getHolidayhours()!=null){
					holidayHours=timeSheet.getHolidayhours();
					
					 if(cal&& rate.getHourlyRegularRate()!=null){
						 holidayRate=rate.getHourlyRegularRate();
						 holidayAmount=timeSheet.getHolidayhours()*rate.getHourlyRegularRate();
					 }
				}
				
				
				
				if(timeSheet.getTotalothoursinweek()!=null){
					otHours=timeSheet.getTotalothoursinweek();
					 if(cal&&rate.getHourlyOvertimeRate()!=null){
						 otRate=rate.getHourlyOvertimeRate()*rate.getHourlyRegularRate();
					     //otAmount=timeSheet.getTotalothoursinweek()*emp.getHourlyOvertimeRate();
						 //otRate=MathUtil.roundUp(otRate,2);
						 otAmount=timeSheet.getTotalothoursinweek()*otRate;
						 System.out.println("\notAmount=======>"+otAmount+"\n");
					}
				/*	 if(emp.getHourlyOvertimeRate()!=null){
						 otRate=emp.getHourlyOvertimeRate()*emp.getHourlyRegularRate();
					     //otAmount=timeSheet.getTotalothoursinweek()*emp.getHourlyOvertimeRate();
						 otRate=MathUtil.roundUp(otRate,2);
						 otAmount=timeSheet.getTotalothoursinweek()*otRate;
						 System.out.println("\notAmount=======>"+otAmount+"\n");
					}*/
			      }
				
				
				if(cal&&timeSheet.getTotaldthoursinweek()!=null){
					dtHours=timeSheet.getTotaldthoursinweek();
					  if(rate.getHourlyDoubleTimeRate()!=null){
						  dtRate=rate.getHourlyDoubleTimeRate()*rate.getHourlyRegularRate(); 
					    //dtAmount=timeSheet.getTotaldthoursinweek()*emp.getHourlyDoubleTimeRate();
						 // dtRate=MathUtil.roundUp(dtRate,2);
						  dtAmount=timeSheet.getTotaldthoursinweek()*dtRate;
						  System.out.println("\ndtAmount=======>"+dtAmount+"\n");
					}
					/*  if(emp.getHourlyDoubleTimeRate()!=null){
						  dtRate=emp.getHourlyDoubleTimeRate()*emp.getHourlyRegularRate(); 
					    //dtAmount=timeSheet.getTotaldthoursinweek()*emp.getHourlyDoubleTimeRate();
						  dtRate=MathUtil.roundUp(dtRate,2);
						  dtAmount=timeSheet.getTotaldthoursinweek()*dtRate;
						  System.out.println("\ndtAmount=======>"+dtAmount+"\n");
					}*/
			       }
				
				//calculating Vacation Amount and Sick/Vacation pay(amount)
				if(timeSheet.getDriver().getPayTerm().equals("2")){
					
				String ptodAppQuery = "select obj from Ptodapplication obj where obj.payRollStatus!=2 and obj.driver.fullName='"
					+ timeSheet.getDriver().getFullName() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"'";
				
				if (!StringUtils.isEmpty(fromDateStr)) {
					ptodAppQuery = ptodAppQuery + " and  obj.batchdate>='"+fromDateStr+"'";
					
				}
				if (!StringUtils.isEmpty(toDateStr)) {
					ptodAppQuery = ptodAppQuery + " and  obj.batchdate<='"+toDateStr + "'";					
				}
		      
				List<Ptodapplication>	ptotAppl = genericDAO.executeSimpleQuery(ptodAppQuery);
				System.out.println("\nptotAppl.size()=>" + ptotAppl.size() + "\n");
				for(Ptodapplication ptodap:ptotAppl){
					if(ptodap.getLeavetype().getId()==1){
						sickParsanolAmount=sickParsanolAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());;
					}
					// Jury duty fix - 3rd Nov 2016
					if(ptodap.getLeavetype().getId()==9){
						sickParsanolAmount=sickParsanolAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());
					}
					if(ptodap.getLeavetype().getId()==4){
						vacationAmount=vacationAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());
						
					}
					// Bereavement change
					if(ptodap.getLeavetype().getId()==8){
						bereavementAmount=bereavementAmount+ptodap.getSequenceAmt1();
						
					}
				}
				
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+timeSheet.getDriver().getFullName()
						+"' and obj.miscNotes!='Reimbursement'");
				
					//miscamountquery.append(" and obj.batchFrom='"+mysqldf.format(timeSheet.getBatchdate())+"'")
				
				if (!StringUtils.isEmpty(fromDateStr)) {
					miscamountquery.append(" and obj.batchFrom>='"+fromDateStr+"'");						
				}
				if (!StringUtils.isEmpty(toDateStr)) {
					miscamountquery.append(" and obj.batchTo<='"+toDateStr +"'");					
				}	
				
				// Misc amt dup emp fix - 10th Nov 2016
				if (!StringUtils.isEmpty(company)) {
					miscamountquery.append(" and  obj.company=").append(company);
				}
				if (!StringUtils.isEmpty(terminal)) {
					miscamountquery.append(" and  obj.terminal=").append(terminal);
				}
				
					List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
					int count=0;
					for(MiscellaneousAmount miscamnt:miscamounts){
						miscamt+=miscamnt.getMisamount();
					}
					
					
					StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+timeSheet.getDriver().getFullName()
							+"' and obj.miscNotes ='Reimbursement'");
					
					//reimburseamountquery.append(" and obj.batchFrom='"+mysqldf.format(timeSheet.getBatchdate())+"'");
				
					if (!StringUtils.isEmpty(fromDateStr)) {
						reimburseamountquery.append(" and obj.batchFrom>='"+fromDateStr+"'");						
					}
					if (!StringUtils.isEmpty(toDateStr)) {
						reimburseamountquery.append(" and obj.batchTo<='"+toDateStr +"'");					
					}
				
					// Misc amt dup emp fix - 10th Nov 2016
					if (!StringUtils.isEmpty(company)) {
						reimburseamountquery.append(" and  obj.company=").append(company);
					}
					if (!StringUtils.isEmpty(terminal)) {
						reimburseamountquery.append(" and  obj.terminal=").append(terminal);
					}
				
					List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
					int reimbursecount=0;
					for(MiscellaneousAmount reimburseamount:reimburseamounts){
						reimburseamt+=reimburseamount.getMisamount();
					}
				//calculating Bonus Amount 
				/*String empBonus = "select obj from EmployeeBonus obj where obj.employee='"
					+ timeSheet.getDriver().getId() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"' and batchFrom<='" +mysqldf.format(timeSheet.getBatchdate())+"'and batchTo>='"+mysqldf.format(timeSheet.getBatchdate())+"'";
				
				List<EmployeeBonus>	empsBonus = genericDAO.executeSimpleQuery(empBonus);
				System.out.println("\nempBonusQuery====>"+empBonus+"\n");
			
				for(EmployeeBonus enmBonusObj:empsBonus){
					bonusAmounts=bonusAmounts+enmBonusObj.getBonusamount();   
					System.out.println("\nbonusAmounts====>"+bonusAmounts+"\n")
				}*/
				
				String empBonus = "select obj from EmployeeBonus obj where obj.payRollStatus!=2 and obj.driver.fullName='"
					+ timeSheet.getDriver().getFullName() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"'";
			    
				if (!StringUtils.isEmpty(fromDateStr)) {
					empBonus = empBonus + " and  obj.batchFrom>='"+fromDateStr+"'";
					
				}
				if (!StringUtils.isEmpty(toDateStr)) {
					empBonus = empBonus + " and  obj.batchTo<='"+toDateStr + "'";					
				}
				
				List<EmployeeBonus>	empsBonus = genericDAO.executeSimpleQuery(empBonus);
				//System.out.println("\nempBonusQuery====>"+empBonus+"\n");
				
				
				EmployeeBonus EmpBonus=null;
				for(EmployeeBonus enmBonusObj:empsBonus){
					//bonusAmounts=bonusAmounts+enmBonusObj.getBonusamount();   
					//System.out.println("\nbonusAmounts====>"+enmBonusObj.getId()+"\n");
					EmpBonus=enmBonusObj;
				}
				
				if(EmpBonus !=null){
				 List<EmpBonusTypesList> listEmpBonud=EmpBonus.getBonusTypesLists();
				for(EmpBonusTypesList enmBonusListObj:listEmpBonud){
						bonusAmounts=bonusAmounts+enmBonusListObj.getBonusamount();   
						//System.out.println("\nbonusAmounts====>"+bonusAmounts+"\n");
					
					}
				}
				
				
				/*Double holidayAmount=0.00;
				StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+timeSheet.getCompany().getId()+" and obj.terminal="+timeSheet.getTerminal().getId()+" and obj.catagory="+timeSheet.getCatagory().getId()+" and obj.leaveType=3");
				
					holidayquery.append(" and obj.batchdate<='"+timeSheet.getBatchdate()+"'");
					
					holidayquery.append(" and obj.batchdate>='"+timeSheet.getBatchdate()+"'");
				
					List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
					for(HolidayType type:holidayTypes){
						holidayAmount=holidayAmount+type.getAmount();
					}*/
				
			}
		
				
				output.setTimesheet(timeSheet);
				totAmount=rAmount+otAmount+dtAmount+holidayAmount;
				
				sumOftotAmount=sumOftotAmount+totAmount;
				sumdtAmount=sumdtAmount+dtAmount;
				sumotAmount=sumotAmount+otAmount;
				sumHolidayAmount=sumHolidayAmount+holidayAmount;
				sumregularAmount=sumregularAmount+rAmount;
				
				output.setRegularhours(rHours);
				output.setOthours(otHours);
				output.setDthours(dtHours);
				output.setHolidayhours(holidayHours);
				
				output.setRegularrate(rRate);
				output.setOtrate(otRate);
				output.setDtrate(dtRate);
				
				output.setRegularamount(rAmount);
				output.setOtamount(otAmount);
				output.setDtamount(dtAmount);
				output.setSumamount(totAmount);
				output.setMiscAmount(miscamt);
				output.setReimburseAmount(reimburseamt);
				output.setVacationAmount(vacationAmount);
				output.setSickParsanolAmount(sickParsanolAmount);
                output.setBonusAmounts(bonusAmounts);
                output.setHolidayAmount(holidayAmount);
                
            // Bereavement change    
            output.setBereavementAmount(bereavementAmount);   
				
				sumOfTotVacSicBonus = totAmount + miscamt + sickParsanolAmount + bonusAmounts;
				output.setSumOfTotVacSicBonus(sumOfTotVacSicBonus);
				summarys.add(output);
				
				
				
			}
		}
		
		StringBuffer  driverQuery = new StringBuffer("Select obj from Driver obj where obj.status=1 and obj.payTerm='2'");
		if (!StringUtils.isEmpty(company)) {
			driverQuery.append("and  obj.company=").append(company);
		}
		if (!StringUtils.isEmpty(terminal)) {
			driverQuery.append("and  obj.terminal=").append(terminal);
		}
		//String driverdIds = "";
		if (!StringUtils.isEmpty(employee)) {						
			driverQuery.append("and  obj.fullName='").append(employee).append("'");
		}
		
		if (!StringUtils.isEmpty(category)) {
			driverQuery.append("and  obj.catagory=").append(category);
		}		
		
		query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.catagory.name asc, obj.fullName asc");
		
		
		List<Driver> hourlyDrivers = genericDAO.executeSimpleQuery(driverQuery.toString());
		
		for(Driver drvObj : hourlyDrivers ){
			
			Double rHours=0.0;
			Double otHours=0.0;
			Double dtHours=0.0;			
			Double holidayHours=0.0;
			
			Double rRate=0.0;
			Double otRate=0.0;
			Double dtRate=0.0;
			Double holidayRate=0.0;
			
			Double rAmount=0.0;
			Double otAmount=0.0;
			Double dtAmount=0.0;
			Double totAmount=0.0;
			Double holidayAmount=0.00;
			
			Double vacationAmount=0.0;
			Double sickParsanolAmount=0.0;
			Double bonusAmounts=0.0;
			Double miscamt=0.0;
			Double reimburseamt=0.0;
			
			// Bereavement change
			Double bereavementAmount=0.0;
			
			
			Double sumOfTotVacSicBonus=0.0;
			
			
			if(driverList.contains(drvObj.getFullName())){
				//do nothing
			}
			else{

				HourlyPayrollInvoiceDetails output = new HourlyPayrollInvoiceDetails();
				driverList.add(drvObj.getFullName());				
				//output.setBatchdate((timeSheet.getBatchdate()!=null) ?sdf.format(timeSheet.getBatchdate()):"");
				
				output.setEmployeesNo(drvObj.getStaffId());
				output.setDriver(drvObj.getFullName());
				output.setCategory(drvObj.getCatagory().getName());				
				output.setCompany(drvObj.getCompany().getName());
				output.setTerminal(drvObj.getTerminal().getName());
				output.setTerminalLoc(drvObj.getTerminal());
				output.setCompanyLoc(drvObj.getCompany());							
				
								
				//calculating Vacation Amount and Sick/Vacation pay(amount)
				
				String ptodAppQuery = "select obj from Ptodapplication obj where obj.payRollStatus!=2 and obj.driver.fullName='"
					+ drvObj.getFullName() + "' and obj.category='"+ drvObj.getCatagory().getId()
				    +"'";
				if (!StringUtils.isEmpty(fromDateStr)) {
					ptodAppQuery = ptodAppQuery + " and  obj.batchdate>='"+fromDateStr+"'";
					
				}
				if (!StringUtils.isEmpty(toDateStr)) {
					ptodAppQuery = ptodAppQuery + " and  obj.batchdate<='"+toDateStr + "'";					
				}
		      
				List<Ptodapplication>	ptotAppl = genericDAO.executeSimpleQuery(ptodAppQuery);
				
				for(Ptodapplication ptodap:ptotAppl){
					if(ptodap.getLeavetype().getId()==1){
						output.setBatchdate(sdf.format(ptodap.getBatchdate()));
						sickParsanolAmount=sickParsanolAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());;
					}
					// Jury duty fix - 3rd Nov 2016
					if(ptodap.getLeavetype().getId()==9){
						output.setBatchdate(sdf.format(ptodap.getBatchdate()));
						sickParsanolAmount=sickParsanolAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());
					}
					if(ptodap.getLeavetype().getId()==4){
						output.setBatchdate(sdf.format(ptodap.getBatchdate()));
						vacationAmount=vacationAmount+(ptodap.getAmountpaid())+(ptodap.getHourlyamountpaid());
						
					}
					// Bereavement change
					if(ptodap.getLeavetype().getId()==8){
						output.setBatchdate(sdf.format(ptodap.getBatchdate()));
						bereavementAmount=bereavementAmount+ptodap.getSequenceAmt1();
					}
					
				}
				
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+drvObj.getFullName()
				+"' and obj.miscNotes!='Reimbursement'");
				
				if (!StringUtils.isEmpty(fromDateStr)) {
					miscamountquery.append(" and obj.batchFrom>='"+fromDateStr+"'");						
				}
				if (!StringUtils.isEmpty(toDateStr)) {
					miscamountquery.append(" and obj.batchTo<='"+toDateStr +"'");					
				}
				
				// Misc amt dup emp fix - 10th Nov 2016
				if (!StringUtils.isEmpty(company)) {
					miscamountquery.append(" and  obj.company=").append(company);
				}
				if (!StringUtils.isEmpty(terminal)) {
					miscamountquery.append(" and  obj.terminal=").append(terminal);
				}
				
					List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
					int count=0;
					for(MiscellaneousAmount miscamnt:miscamounts){
						miscamt+=miscamnt.getMisamount();
						output.setBatchdate(sdf.format(miscamnt.getBatchFrom()));
					}
					
					
					StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+drvObj.getFullName()
					+"' and obj.miscNotes ='Reimbursement'");
					
					//reimburseamountquery.append(" and obj.batchFrom='"+mysqldf.format(timeSheet.getBatchdate())+"'");
					if (!StringUtils.isEmpty(fromDateStr)) {
						reimburseamountquery.append(" and obj.batchFrom>='"+fromDateStr+"'");						
					}
					if (!StringUtils.isEmpty(toDateStr)) {
						reimburseamountquery.append(" and obj.batchTo<='"+toDateStr +"'");					
					}
				
					// Misc amt dup emp fix - 10th Nov 2016
					if (!StringUtils.isEmpty(company)) {
						reimburseamountquery.append(" and  obj.company=").append(company);
					}
					if (!StringUtils.isEmpty(terminal)) {
						reimburseamountquery.append(" and  obj.terminal=").append(terminal);
					}
				
					List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
					int reimbursecount=0;
					for(MiscellaneousAmount reimburseamount:reimburseamounts){
						reimburseamt+=reimburseamount.getMisamount();
						output.setBatchdate(sdf.format(reimburseamount.getBatchFrom()));
					}
				//calculating Bonus Amount 
				/*String empBonus = "select obj from EmployeeBonus obj where obj.employee='"
					+ timeSheet.getDriver().getId() + "' and obj.category='"+ timeSheet.getCatagory().getId()
				    +"' and batchFrom<='" +mysqldf.format(timeSheet.getBatchdate())+"'and batchTo>='"+mysqldf.format(timeSheet.getBatchdate())+"'";
				
				List<EmployeeBonus>	empsBonus = genericDAO.executeSimpleQuery(empBonus);
				System.out.println("\nempBonusQuery====>"+empBonus+"\n");
			
				for(EmployeeBonus enmBonusObj:empsBonus){
					bonusAmounts=bonusAmounts+enmBonusObj.getBonusamount();   
					System.out.println("\nbonusAmounts====>"+bonusAmounts+"\n")
				}*/
				
				String empBonus = "select obj from EmployeeBonus obj where obj.payRollStatus!=2 and obj.driver.fullName='"
					+ drvObj.getFullName() + "' and obj.category='"+ drvObj.getCatagory().getId()
				    +"'";
				if (!StringUtils.isEmpty(fromDateStr)) {
					empBonus = empBonus + " and  obj.batchFrom>='"+fromDateStr+"'";
					
				}
				if (!StringUtils.isEmpty(toDateStr)) {
					empBonus = empBonus + " and  obj.batchTo<='"+toDateStr + "'";					
				}
				
				
			    List<EmployeeBonus>	empsBonus = genericDAO.executeSimpleQuery(empBonus);
				//System.out.println("\nempBonusQuery====>"+empBonus+"\n");
				
				
				EmployeeBonus EmpBonus=null;
				for(EmployeeBonus enmBonusObj:empsBonus){
					//bonusAmounts=bonusAmounts+enmBonusObj.getBonusamount();   
					//System.out.println("\nbonusAmounts====>"+enmBonusObj.getId()+"\n");
					EmpBonus=enmBonusObj;
					output.setBatchdate(sdf.format(enmBonusObj.getBatchFrom()));
				}
				
				if(EmpBonus !=null){
				 List<EmpBonusTypesList> listEmpBonud=EmpBonus.getBonusTypesLists();
				for(EmpBonusTypesList enmBonusListObj:listEmpBonud){
						bonusAmounts=bonusAmounts+enmBonusListObj.getBonusamount();   
						//System.out.println("\nbonusAmounts====>"+bonusAmounts+"\n");					
					}
				}
				
				
				
				
				output.setTimesheet(null);
				totAmount=0.0;
				
				sumOftotAmount=sumOftotAmount+totAmount;
				sumdtAmount=sumdtAmount+dtAmount;
				sumotAmount=sumotAmount+otAmount;
				sumHolidayAmount=sumHolidayAmount+holidayAmount;
				sumregularAmount=sumregularAmount+rAmount;
				
				output.setRegularhours(rHours);
				output.setOthours(otHours);
				output.setDthours(dtHours);
				output.setHolidayhours(holidayHours);
				
				output.setRegularrate(rRate);
				output.setOtrate(otRate);
				output.setDtrate(dtRate);
				
				output.setRegularamount(rAmount);
				output.setOtamount(otAmount);
				output.setDtamount(dtAmount);
				output.setSumamount(totAmount);
				output.setMiscAmount(miscamt);
				output.setReimburseAmount(reimburseamt);
				output.setVacationAmount(vacationAmount);
				output.setSickParsanolAmount(sickParsanolAmount);
                output.setBonusAmounts(bonusAmounts);
                output.setHolidayAmount(holidayAmount);
            
            // Bereavement change
            output.setBereavementAmount(bereavementAmount);
            
				sumOfTotVacSicBonus = totAmount + miscamt + sickParsanolAmount + bonusAmounts;
				output.setSumOfTotVacSicBonus(sumOfTotVacSicBonus);
				// Bereavement change
				if(miscamt==0.0 && sickParsanolAmount==0.0 && reimburseamt==0.0 && vacationAmount==0.0 && bonusAmounts==0.0 && bereavementAmount == 0.0){
					// do nothing			
				}
				else{
					summarys.add(output);
				}
			
			}
		}
		
		
		sumOftotAmount = MathUtil.roundUp(sumOftotAmount, 2);
		sumdtAmount = MathUtil.roundUp(sumdtAmount, 2);
		sumotAmount = MathUtil.roundUp(sumotAmount, 2);
		sumregularAmount = MathUtil.roundUp(sumregularAmount, 2);
		
		wrapper.setSumtotalAmount(sumOftotAmount);
		wrapper.setSumdtAmount(sumdtAmount);
		wrapper.setSumotAmount(sumotAmount);
		wrapper.setSumregularAmount(sumregularAmount);
		//System.out.println("sumOftotAmount====="+wrapper.getSumtotalAmount()+"\n");
		
		
		
		////
	 return wrapper;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveHourlyPayrollData(HttpServletRequest request,SearchCriteria criteria) throws Exception 
	{
		String payrollinvoiceNumber = (String) criteria.getSearchMap().get("payrollinvoicedate");
		String company = (String) criteria.getSearchMap().get("company");
		String terminal = (String) criteria.getSearchMap().get("terminal");
		String payrollinvoicedate = null; 
		String batchdateFrom = null;
		String batchdateTo = null;
		Date payinvDate=null;
		Date batchDateFrom=null;
		Date batchDateTo=null;
		String driverNames = "-1";
		
		//System.out.println("\npayrollinvoiceNumber====>"+payrollinvoiceNumber+"\n");
		if (StringUtils.isEmpty(payrollinvoiceNumber)){
			request.getSession().setAttribute("error","Please Enter Check Date");
			throw new Exception("invoice number null");
		}
		
		StringBuilder querypayrollinvoice = new StringBuilder("select obj from HourlyPayrollInvoice obj where obj.payrollinvoicedate='"
			+ payrollinvoiceNumber+ "' and obj.companyLoc="+company);
		
		if (!StringUtils.isEmpty(terminal)){
			querypayrollinvoice.append(" and obj.terminal="+terminal);
		}
		
		List<HourlyPayrollInvoice> payrollinvoices = genericDAO.executeSimpleQuery(querypayrollinvoice.toString());
		EmployeePayrollWrapper wrapper = generateHourlyPayrollData(criteria);
		if (payrollinvoices.isEmpty()) {
			HourlyPayrollInvoice HPinvoice = new HourlyPayrollInvoice();
			
			payrollinvoicedate =  (String) criteria.getSearchMap().get("payrollinvoicedate");
			batchdateFrom =  (String) criteria.getSearchMap().get("fromDate");
			batchdateTo =  (String) criteria.getSearchMap().get("toDate");
			
			if (!payrollinvoicedate.isEmpty()) {
				payinvDate = new SimpleDateFormat("MM-dd-yyyy").parse(payrollinvoicedate);
				SimpleDateFormat mysqldf1 = new SimpleDateFormat("yyyy-MM-dd");
				HPinvoice.setPayrollinvoicedate(payinvDate);
				payrollinvoicedate=mysqldf1.format(payinvDate);
			} 
			else {
				HPinvoice.setPayrollinvoicedate(Calendar.getInstance().getTime());
				SimpleDateFormat mysqldf1 = new SimpleDateFormat("yyyy-MM-dd");
				payrollinvoicedate=mysqldf1.format(Calendar.getInstance().getTime());
				payinvDate=Calendar.getInstance().getTime();
			}
			
			if(!batchdateFrom.isEmpty()){
				batchDateFrom = new SimpleDateFormat("MM-dd-yyyy").parse(batchdateFrom);
				SimpleDateFormat mysqldf1 = new SimpleDateFormat("yyyy-MM-dd");
				HPinvoice.setBillBatchFrom(batchDateFrom);
				batchdateFrom=mysqldf1.format(batchDateFrom);
			}
			
			if(!batchdateTo.isEmpty()){
				batchDateTo = new SimpleDateFormat("MM-dd-yyyy").parse(batchdateTo);
				SimpleDateFormat mysqldf1 = new SimpleDateFormat("yyyy-MM-dd");
				HPinvoice.setBillBatchTo(batchDateTo);
				batchdateTo=mysqldf1.format(batchDateTo);
			}
			
			//HPinvoice.setPayrollinvoicenumber(payrollinvoiceNumber);
			HPinvoice.setSumregularamount(wrapper.getSumregularAmount());
			HPinvoice.setSumotamount(wrapper.getSumotAmount());
			HPinvoice.setSumdtamount(wrapper.getSumdtAmount());
			HPinvoice.setSumtotalamount(wrapper.getSumtotalAmount());
			HPinvoice.setCompany(company);
			
			Location compObj=genericDAO.getById(Location.class,Long.parseLong(company));
			HPinvoice.setCompanyLoc(compObj);
			
			if (!StringUtils.isEmpty(terminal)){
				Location terminalObj=genericDAO.getById(Location.class,Long.parseLong(terminal));	
				HPinvoice.setTerminal(terminalObj);
			}
			
			
			genericDAO.saveOrUpdate(HPinvoice);
			
			//System.out.println("\nwrapper.getInvoicedetails.size()====>"+wrapper.getInvoicedetails().size()+"\n");
			//System.out.println("\nwrapper.getInvoicedetails().size()====>"+wrapper.getInvoicedetails().get(0).getId()+"\n");
			/*wrapper.getTimesheets().*/
		
			
			Location compLocObj=genericDAO.getById(Location.class,Long.parseLong(company));
			Location terminalLocObj= null;
			
			if (!StringUtils.isEmpty(terminal)){
				terminalLocObj=genericDAO.getById(Location.class,Long.parseLong(terminal));				
			}
			
			Map criterias = new HashMap();
			for (HourlyPayrollInvoiceDetails billing : wrapper.getInvoicedetails()) {				
				
				criterias.clear();
				criterias.put("fullName",billing.getDriver() );
				//criterias.put("status",1);
				//Driver drvObj = genericDAO.getByCriteria(Driver.class, criterias);
				List<Driver> drivers = genericDAO.findByCriteria(Driver.class, criterias);
				for(Driver drvObj:drivers){
					if(drvObj.getPayTerm().equals("2")){
						driverNames = driverNames+","+drvObj.getId().toString();						
					}
				}
				
				/*HourlyPayrollInvoiceDetails HPinvoiceDetails = new HourlyPayrollInvoiceDetails();*/
				if(billing.getTimesheet()!=null){
					TimeSheet timesheet = genericDAO.getById(TimeSheet.class, billing.getTimesheet().getId());
				
					timesheet.setHourlypayrollinvoiceNumber(payrollinvoiceNumber);
					timesheet.setHourlypayrollinvoiceDate(payinvDate);
					timesheet.setHourlypayrollstatus(2);
				}
				billing.setDate(payrollinvoicedate);
				billing.setBatchdate(batchdateFrom);
				billing.setBatchdateTo(batchdateTo);
				billing.setPayRollBatchFrom(batchDateFrom);
				billing.setPayRollBatchTo(batchDateTo);
				billing.setPayRollCheckDate(payinvDate);
				billing.setPayrollinvoiceNo(payrollinvoiceNumber);
				billing.setCompanyLoc(compLocObj);				
				billing.setTerminalLoc(terminalLocObj);
				genericDAO.saveOrUpdate(billing);
			}
			
			
		}
		else {
			request.getSession().setAttribute("error","Hourly Payroll Record with same details exists already.");
			throw new Exception("Payroll Run number already exists. Please choose another number");
		}
		
		
		 StringBuffer ptodquery=new StringBuffer("update Ptodapplication t set t.payRollStatus=2,t.payRollBatch='"+payrollinvoicedate+"' where 1=1 and  t.payRollStatus=1 and t.driver in ("+driverNames+")");
		 //StringBuffer holidayquery=new StringBuffer("update HolidayType t set t.payRollStatus=2,t.payRollBatch='"+payrollinvoicedate+"' where 1=1 and  t.payRollStatus=1 and t.leaveType=3" );
		 StringBuffer empbonusquery=new StringBuffer("update EmployeeBonus t set t.payRollStatus=2,t.payRollBatch='"+payrollinvoicedate+"' where 1=1 and  t.payRollStatus=1  and t.driver in ("+driverNames+")");
		 StringBuffer miscAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=2,t.payRollBatch='"+payrollinvoicedate+"' where 1=1 and  t.payRollStatus=1 and t.miscNotes!='Reimbursement' and t.driver in ("+driverNames+")");
		 StringBuffer reimAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=2,t.payRollBatch='"+payrollinvoicedate+"' where 1=1 and  t.payRollStatus=1 and t.miscNotes='Reimbursement'  and t.driver in ("+driverNames+")");
		 //StringBuffer quarterBonusquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=2,t.payRollBatch='"+payrollinvoicedate+"' where 1=1 and  t.payRollStatus=1 and t.miscType='Quarter Bonus'" );
		      
		
		if(!StringUtils.isEmpty(batchdateFrom)){			
			ptodquery.append(" and t.batchdate>='"+batchdateFrom+" 00:00:00'");
			//holidayquery.append(" and t.batchdate>='"+batchdateFrom+" 00:00:00'");
			empbonusquery.append(" and t.batchFrom>='"+batchdateFrom+" 00:00:00'");
			miscAmountquery.append(" and t.batchFrom>='"+batchdateFrom+" 00:00:00'");
			reimAmountquery.append(" and t.batchFrom>='"+batchdateFrom+" 00:00:00'");
			//quarterBonusquery.append(" and t.batchFrom>='"+batchdateFrom+" 00:00:00'");
		}
		if(!StringUtils.isEmpty(batchdateTo)){			
			ptodquery.append(" and t.batchdate<='"+batchdateTo+" 00:00:00'");
			//holidayquery.append(" and t.batchdate<='"+batchdateTo+" 00:00:00'");
			empbonusquery.append(" and t.batchTo<='"+batchdateTo+" 00:00:00'");
			miscAmountquery.append(" and t.batchTo<='"+batchdateTo+" 00:00:00'");
			reimAmountquery.append(" and t.batchTo<='"+batchdateTo+" 00:00:00'");
			//quarterBonusquery.append(" and t.batchTo<='"+batchdateTo+" 00:00:00'");
		}
		 if(!StringUtils.isEmpty(terminal)){	            
	            ptodquery.append(" and t.terminal="+terminal);
				//holidayquery.append(" and t.terminal="+terminal);
				empbonusquery.append(" and t.terminal="+terminal);
				miscAmountquery.append(" and t.terminal="+terminal);
				reimAmountquery.append(" and t.terminal="+terminal);
				//quarterBonusquery.append(" and t.terminal="+terminal);
	     }	 
		 
		  genericDAO.getEntityManager().createQuery(ptodquery.toString()).executeUpdate();
		  //genericDAO.getEntityManager().createQuery(holidayquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(empbonusquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(miscAmountquery.toString()).executeUpdate();
		  genericDAO.getEntityManager().createQuery(reimAmountquery.toString()).executeUpdate();
		  //genericDAO.getEntityManager().createQuery(quarterBonusquery.toString()).executeUpdate();
	}

	@Override
	public List<PtodApplicationInput> generatePtodApplicationReport(
			SearchCriteria criteria, PtodApplicationInput input) {
		// TODO Auto-generated method stub
		String company=input.getCompany();
		String terminal=input.getTerminal();
		String category=input.getCategory();
		String leaveType=input.getLeaveType();
		String employees=input.getEmployees();
		String approveStatus=input.getApprovestatus();
		
		String payRollBatchFrom=input.getPayRollBatchFrom();
		String payRollBatchto=input.getPayRollBatchto();
		payRollBatchFrom=ReportDateUtil.getFromDate(payRollBatchFrom);
		payRollBatchto=ReportDateUtil.getToDate(payRollBatchto);
		StringBuffer query=new StringBuffer("");
		query.append("select obj from Ptodapplication obj where 1=1");
		if (!StringUtils.isEmpty(employees)) {
			query.append("and  obj.driver in ("+employees+")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.category in ("+category+")");
		   }
		if (!StringUtils.isEmpty(leaveType)) {
			query.append("and  obj.leavetype in ("+leaveType+")");
		    }
		if (!StringUtils.isEmpty(company)) {
			query.append("and  obj.company in ("+company+")");
			}
		if (!StringUtils.isEmpty(terminal)) {
				query.append("and  obj.terminal in ("+terminal+")");
			}
		if(!StringUtils.isEmpty(payRollBatchFrom)){
			query.append("and obj.batchdate>='"+payRollBatchFrom+"'");
		}
		if(!StringUtils.isEmpty(payRollBatchto)){
			query.append("and obj.batchdate<='"+payRollBatchto+"'");
		}
		if(!StringUtils.isEmpty(approveStatus)){
			query.append(" and obj.approvestatus in ("+approveStatus+")");
		}
		List<PtodApplicationInput> out=new ArrayList<PtodApplicationInput>();
		List<Ptodapplication> ptodapplications=genericDAO.executeSimpleQuery(query.toString()); 
		for(Ptodapplication ptodapplication:ptodapplications){
			PtodApplicationInput applicationInput=new PtodApplicationInput();
			applicationInput.setCompany(ptodapplication.getCompany()!=null?ptodapplication.getCompany().getName():"");
			applicationInput.setTerminal(ptodapplication.getTerminal()!=null?ptodapplication.getTerminal().getName():"");
			applicationInput.setCategory(ptodapplication.getCategory()!=null?ptodapplication.getCategory().getName():"");
			applicationInput.setEmployees(ptodapplication.getDriver().getFullName());
			applicationInput.setLeaveType(ptodapplication.getLeavetype()!=null?ptodapplication.getLeavetype().getName():"");
			applicationInput.setSubmitdate(ptodapplication.getSubmitdate()!=null? sdf.format(ptodapplication.getSubmitdate()):"");
			applicationInput.setDaysrequested(ptodapplication.getDaysrequested()!=null?ptodapplication.getDaysrequested():0);
			applicationInput.setLeavedatefrom(ptodapplication.getLeavedatefrom()!=null? sdf.format(ptodapplication.getLeavedatefrom()):"");
			applicationInput.setLeavedateto(ptodapplication.getLeavedateto()!=null? sdf.format(ptodapplication.getLeavedateto()):"");
			applicationInput.setDayspaid(ptodapplication.getDayspaid()!=null?ptodapplication.getDayspaid():0);
			applicationInput.setDaysunpaid(ptodapplication.getDaysunpaid()!=null?ptodapplication.getDaysunpaid():0);
			applicationInput.setPtodrates(ptodapplication.getPtodrates()!=null?ptodapplication.getPtodrates():0.0);
			applicationInput.setAmountpaid(ptodapplication.getAmountpaid()!=null?ptodapplication.getAmountpaid():0.0);
			applicationInput.setHoursrequested(ptodapplication.getHoursrequested()!=null?ptodapplication.getHoursrequested():0.0);
			applicationInput.setHourspaid(ptodapplication.getHourspaid()!=null?ptodapplication.getHourspaid():0.0);
			applicationInput.setHoursunpaid(ptodapplication.getHoursunpaid()!=null?ptodapplication.getHoursunpaid():0.0);
			applicationInput.setPtodhourlyrate(ptodapplication.getPtodhourlyrate()!=null?ptodapplication.getPtodhourlyrate():0.0);
			applicationInput.setHourlyamountpaid(ptodapplication.getHourlyamountpaid()!=null?ptodapplication.getHourlyamountpaid():0.0);
			applicationInput.setCheckdate(ptodapplication.getCheckdate()!=null? sdf.format(ptodapplication.getCheckdate()):"");
			applicationInput.setApproveby(ptodapplication.getApproveby()!=null?ptodapplication.getApproveby().getFullName():"");
			applicationInput.setApprovestatus(StaticDataUtil.getText(
						"APPROVE_STATUS", "" + ptodapplication.getApprovestatus()));
			applicationInput.setPayrollbatch(ptodapplication.getBatchdate()!=null? sdf.format(ptodapplication.getBatchdate()):"");
			
			out.add(applicationInput);
			
			
		}
		return out;
	}
	
	@Override
	public EmployeeBonusWrapper generateEmployeeBonusData(SearchCriteria criteria, EmployeeBonusInput input)
	{
		// TODO Auto-generated method stub
		//Map<String, String> params = new HashMap<String, String>();
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String employee = input.getDriver();
		String category = input.getCategory();
		String bonustype = input.getBonustype();
		String empnumber = input.getEmployeeNo();
		
		String batchDateFrom = input.getBatchFrom();
		String batchDateTo = input.getBatchTo();
		
		
		StringBuffer query = new StringBuffer("");
		StringBuffer queryCount = new StringBuffer("");
		query.append("select obj from EmployeeBonus obj where 1=1");
		queryCount.append("select obj from EmployeeBonus obj where 1=1 ");
		
		if (!StringUtils.isEmpty(company)) {
		query.append("and  obj.company in (" + company + ")");
		queryCount.append(" and obj.company in (" + company + ")");
		}
		
		if (!StringUtils.isEmpty(terminal)) {
			query.append("and  obj.terminal in (" + terminal + ")");
			queryCount.append(" and obj.terminal in (" + terminal + ")");
		}
		if (!StringUtils.isEmpty(employee)) {
			query.append("and  obj.driver in (" + employee + ")");
			queryCount.append(" and obj.driver in (" + employee + ")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.catagory in (" + category + ")");
			queryCount.append(" and obj.catagory in (" + category + ")");
		}
		if (!StringUtils.isEmpty(bonustype)) {
			query.append("and  obj.bonustype in (" + bonustype + ")");
			queryCount.append(" and obj.bonustype in (" + bonustype + ")");
		}
		
		if (!StringUtils.isEmpty(empnumber)) {
			query.append("and  obj.empnumber ="+  empnumber +"" );
			queryCount.append("and  obj.empnumber ="+  empnumber +"" );
			
		}
		
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer batchDateClause = new StringBuffer();
		
		
		  if (StringUtils.isNotEmpty(batchDateFrom)) {
	        	try {
	        		batchDateClause.append(" (obj.batchFrom>='"+sdf1.format(dateFormat1.parse(batchDateFrom))+"'");
	        		batchDateClause.append(" and obj.batchFrom<='"+sdf1.format(dateFormat1.parse(batchDateTo))+"')");
	        		
	        		batchDateClause.append(" OR ");
	        		
	        		batchDateClause.append(" (obj.batchTo>='"+sdf1.format(dateFormat1.parse(batchDateFrom))+"'");
	        		batchDateClause.append(" and obj.batchTo<='"+sdf1.format(dateFormat1.parse(batchDateTo))+"')");
	   	   } catch (ParseException e) {
					e.printStackTrace();
				}
	        	
			}
	      
	      if (StringUtils.isNotEmpty(batchDateClause.toString())) {
	      	query.append(" and (")
	      				  .append(batchDateClause)
	      				  .append(" )");
	      	queryCount.append(" and (")
				  .append(batchDateClause)
				  .append(" )");
	      }
	      
	      query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.driver.fullName asc, obj.batchFrom asc, obj.batchTo asc");
		
		
		
		System.out.println("\ntEmp Bonus Query=>" + query + "\n");
		List<EmployeeBonus> empBonus = genericDAO.executeSimpleQuery(query.toString());
		System.out.println("\ntts.size()=>" + empBonus.size() + "\n");
		List<EmployeeBonus> summarys = new ArrayList<EmployeeBonus>();
		EmployeeBonusWrapper wrapper=new EmployeeBonusWrapper();
		wrapper.setEmployeeBonus(summarys);
		double sumOftotAmount=0.0;
		for (EmployeeBonus empbonusObj : empBonus) {
			Double amuont=0.0;
			if (empbonusObj != null) {
				EmployeeBonus output = new EmployeeBonus();
				output.setEmployees((empbonusObj.getDriver() != null) ? empbonusObj.getDriver().getFullName() : "");
				System.out.println("\noutput.getEmployeeis"+output.getEmployees()+"\n");
				output.setCategories((empbonusObj.getCategory().getName() != null) ? empbonusObj.getCategory().getName() : "");
				output.setCompanies((empbonusObj.getCompany() != null) ? empbonusObj.getCompany().getName() : "");
				output.setTerminals((empbonusObj.getTerminal() != null) ? empbonusObj.getTerminal().getName() : "");
				output.setEmployeesNo((empbonusObj.getDriver() != null)? empbonusObj.getDriver().getStaffId() :"");
				//output.setBonustypes((empbonusObj.getBonustype() != null)? empbonusObj.getBonustype().getTypename() : "");
				output.setBatchFroms((empbonusObj.getBatchFrom() != null)?  sdf.format(empbonusObj.getBatchFrom()):"");
				output.setBatchTos((empbonusObj.getBatchTo() != null)?  sdf.format(empbonusObj.getBatchTo()):"");
				
				//output.setNotes((empbonusObj.getNote() != null)? (empbonusObj.getNote()):"");
				//output.setBatchDates((timesheet.getBatchdate()!=null) ?sdf.format(timesheet.getBatchdate()):"");
			    //amuont= empbonusObj.getBonusamount()!=null?empbonusObj.getBonusamount():0.0;
				StringBuffer buffer=new StringBuffer("");
				StringBuffer buffer1=new StringBuffer("");
				for(EmpBonusTypesList bonusTypesList:empbonusObj.getBonusTypesLists()){
					amuont+=bonusTypesList.getBonusamount();
					buffer.append(bonusTypesList.getBonusType().getTypename());
					buffer1.append(bonusTypesList.getNote());
					buffer1.append(",");
					buffer.append(",");
				}
				if(buffer.length()>0){
				int i=buffer.lastIndexOf(",");
				buffer.deleteCharAt(i);
				}
				if(buffer1.length()>0){
					int i1=buffer1.lastIndexOf(",");
					buffer1.deleteCharAt(i1);
					}
				output.setNotes(buffer1.toString());
				output.setBonustypes(buffer.toString());
				output.setAmounts(amuont);
				
				
				sumOftotAmount+=amuont;
				
				summarys.add(output);
			}
		}
		
		
		
	
	
		sumOftotAmount = MathUtil.roundUp(sumOftotAmount, 2);
		wrapper.setSumtotalAmount(sumOftotAmount);
		wrapper.setTotalRowCount(empBonus.size());
		System.out.println("\ngenerateEmployeeBonusData\n");
		return wrapper;
	}

	@Override
	public List<ProbationReportInput> generateProbationReport(
			SearchCriteria criteria, ProbationReportInput input) {
		// TODO Auto-generated method stub
		String company=input.getCompany();
		String terminal=input.getTerminal();
		String category=input.getCategory();
		String employee=input.getDriver();
		String dateProbationStart=input.getDateProbationStart();
		String dateProbationEnd=input.getDateProbationEnd();
		dateProbationStart=ReportDateUtil.getFromDate(dateProbationStart);
		dateProbationEnd=ReportDateUtil.getToDate(dateProbationEnd);
		
		StringBuffer query=new StringBuffer("");
		int q=0;
		query.append("select obj from Driver obj where 1=1");
		if (!StringUtils.isEmpty(employee)) {
			query.append("and  obj.id in ("+employee+")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.catagory in ("+category+")");
		   }
		
		if (!StringUtils.isEmpty(company)) {
			query.append("and  obj.company in ("+company+")");
			}
		if (!StringUtils.isEmpty(terminal)) {
				query.append("and  obj.terminal in ("+terminal+")");
			}
		if(!StringUtils.isEmpty(dateProbationStart)){
			query.append("and obj.dateProbationStart>='"+dateProbationStart+"'");
			q++;
		}
		if(!StringUtils.isEmpty(dateProbationEnd)){
			query.append("and obj.dateProbationEnd<='"+dateProbationEnd+"'");
			q++;
		}
		if(q==0){
			SimpleDateFormat mysqldf1 = new SimpleDateFormat("yyyy-MM-dd");
			Date d=DateUtil.get30day();
			query.append("and  obj.dateProbationEnd>=CURRENT_DATE and obj.dateProbationEnd<='"+mysqldf1.format(d)+"'");
		}
		query.append("order by obj.company.name asc,obj.terminal.name asc,obj.fullName asc,obj.dateHired asc,obj.dateReHired asc");
		List<Driver> employees =genericDAO.executeSimpleQuery(query.toString());
		List<ProbationReportInput> out =new ArrayList<ProbationReportInput>();
		
		for(Driver emp:employees){
			ProbationReportInput probationReportInput=new ProbationReportInput();
			probationReportInput.setCategory(emp.getCatagory()!=null?emp.getCatagory().getName():"");
			probationReportInput.setCompany(emp.getCompany()!=null?emp.getCompany().getName():"");
			probationReportInput.setDateHired(emp.getDateHired()!=null? sdf.format(emp.getDateHired()):"");
			probationReportInput.setDateReHired(emp.getDateReHired()!=null?sdf.format(emp.getDateReHired()):"");
			probationReportInput.setDateProbationStart(emp.getDateProbationStart()!=null? sdf.format(emp.getDateProbationStart()):"");
			probationReportInput.setDateProbationEnd(emp.getDateProbationEnd()!=null? sdf.format(emp.getDateProbationEnd()):"");
			probationReportInput.setDateTerminated(emp.getDateTerminated()!=null? sdf.format(emp.getDateTerminated()):"");
			probationReportInput.setDriver(emp.getFullName()!=null?emp.getFullName():"");
			probationReportInput.setProbationDays(emp.getProbationDays());
			probationReportInput.setStaffId(emp.getStaffId()!=null?emp.getStaffId():"");
			probationReportInput.setStatus(StaticDataUtil.getText("STATUS",""+emp.getStatus()));
			probationReportInput.setTerminal(emp.getTerminal()!=null?emp.getTerminal().getName():"");
			out.add(probationReportInput);
			
		}
		return out;
	}
	
	@Override
	public EmployeeWrapper generateEmployeeReportData(SearchCriteria criteria,EmployeeInput input)
	{
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String employee = input.getDriver();
		String category = input.getCategory();
		String empstatus = input.getStatus();
		
		//String hireddatefrom=input.getDateHiredfrom();
		//String hireddateto=input.getDateHiredto();
	    String hireddatefrom=ReportDateUtil.getFromDate(input.getDateHiredfrom());
		String hireddateto=	ReportDateUtil.getFromDate(input.getDateHiredto());
		
		StringBuffer query = new StringBuffer("");
		StringBuffer queryCount = new StringBuffer("");
		query.append("select obj from Driver obj where 1=1");
		queryCount.append("select obj from Driver obj where 1=1 ");
		
		if (!StringUtils.isEmpty(company)) {
		query.append(" and  obj.company in (" + company + ")");
		queryCount.append(" and obj.company in (" + company + ")");
		}
		
		if (!StringUtils.isEmpty(terminal)) {
			query.append(" and  obj.terminal in (" + terminal + ")");
			queryCount.append(" and obj.terminal in (" + terminal + ")");
		}
		if (!StringUtils.isEmpty(employee)) {
			query.append(" and  obj.id in (" + employee + ")");
			queryCount.append(" and obj.id in (" + employee + ")");
		}
		
		if (!StringUtils.isEmpty(category)) {
			query.append("and  obj.catagory in (" + category + ")");
			queryCount.append(" and obj.catagory in (" + category + ")");
		}
		
		if(!StringUtils.isEmpty(hireddatefrom)){
			query.append("and obj.dateHired>='"+hireddatefrom+"'");
			queryCount.append(" and obj.dateHired>='"+hireddatefrom+"'");
		}
		if(!StringUtils.isEmpty(hireddateto)){
			query.append("and obj.dateHired<='"+hireddateto+"'");
			queryCount.append(" and obj.dateHired<='"+hireddateto+"'");
		}
		if(!StringUtils.isEmpty(empstatus)){
			query.append("and  obj.status in (" + empstatus + ")");
			queryCount.append(" and obj.status in (" + empstatus + ")");
		}
		query.append(" order by obj.company.name asc,obj.terminal.name asc,obj.fullName asc,obj.dateHired asc,obj.dateReHired asc");
		queryCount.append(" order by obj.company.name asc,obj.terminal.name asc,obj.fullName asc,obj.dateHired asc,obj.dateReHired asc");
		System.out.println("\ntEmp Query=>" + query + "\n");
		List<Driver> emp = genericDAO.executeSimpleQuery(query.toString());
		System.out.println("\ntts.size()=>" + emp.size() + "\n");
		List<Driver> summarys = new ArrayList<Driver>();
		EmployeeWrapper wrapper=new EmployeeWrapper();
		wrapper.setDrivers(summarys);
		
		for (Driver empObj : emp) {
			
			Driver output = new Driver();
			
			output.setStaffId(empObj.getStaffId()!=null?empObj.getStaffId():"");
			output.setFullName(empObj.getFullName()!=null?empObj.getFullName():"");
			output.setCategories(empObj.getCatagory()!=null?empObj.getCatagory().getName():"");
			output.setCompanies(empObj.getCompany()!=null?empObj.getCompany().getName():"");
			output.setTerminals(empObj.getTerminal()!=null?empObj.getTerminal().getName():"");			
			output.setEmpshift(empObj.getShift().equals("1") ? "Day" : "Night");
			output.setDatesTerminated(empObj.getDateTerminated()!=null? sdf.format(empObj.getDateTerminated()):"");
			output.setFirstName(empObj.getFirstName()!=null?empObj.getFirstName():"");
			output.setLastName(empObj.getLastName()!=null?empObj.getLastName():"");
			output.setHiredDate(empObj.getDateHired()!=null? sdf.format(empObj.getDateHired()):"");
			output.setRehiredDate(empObj.getDateReHired()!=null? sdf.format(empObj.getDateReHired()):"");
			output.setProbationDays(empObj.getProbationDays());
			output.setProbationStartDate(empObj.getDateProbationStart()!=null? sdf.format(empObj.getDateProbationStart()):"");
			output.setProbationEndDate(empObj.getDateProbationEnd()!=null? sdf.format(empObj.getDateProbationEnd()):"");
			output.setEmpanniversaryDate(empObj.getAnniversaryDate()!=null? sdf.format(empObj.getAnniversaryDate()):"");			
			output.setEmpstatus(empObj.getStatus()==1 ? "Active": "Inactive");
			
			output.setSsn(StringUtils.isNotEmpty(empObj.getSsn()) ? empObj.getSsn() : StringUtils.EMPTY);
			
			output.setPayTerm(StringUtils.isNotEmpty(empObj.getPayTerm())
					? retrievePayTermText(empObj.getPayTerm()) : StringUtils.EMPTY);
			
			summarys.add(output);				
		}
		
		
		return wrapper;
	}
	
	private String retrievePayTermText(String dataValue) {
		String dataType = "Pay Term";
		return StaticDataUtil.getText(dataType, dataValue);
	}
	
	///***********************************************************
	///***********************************************************
	///***********************************************************
	
	@Override
	public DriverPayWrapper generateDriverPayHistory(SearchCriteria criteria,
			DriverPayHistoryInput input) {

		Map criterias = new HashMap();
		List<String> str = new ArrayList<String>();
		int errorCount = 0;
		Map<String, Double> map=new HashMap<String, Double>();
		Map<String, Integer> map1=new HashMap<String, Integer>();		

		String payrollDate=input.getPayrollBatchDate();
		String driverid=input.getDrivers();
		String frombatch=input.getBatchDateFrom();
		String tobatch=input.getBatchDateTo();
		String company=input.getCompanies();
		String terminal=input.getTerminals();
		String expire= (String) criteria.getSearchMap().get("expire");
		String status=input.getPayrollstatus();
		String sta=(String)criteria.getSearchMap().get("stat");
		String driversmul=(String)criteria.getSearchMap().get("driversmul");

		frombatch=ReportDateUtil.getFromDate(frombatch);
		tobatch=ReportDateUtil.getFromDate(tobatch);		

		payrollDate=ReportDateUtil.getFromDate(payrollDate);
		String sum= (String) criteria.getSearchMap().get("summary");
		Location companylocation=null;
		Location terminallocation=null;
		String driverIds="";
		if(!StringUtils.isEmpty(company)){
			companylocation=genericDAO.getById(Location.class, Long.parseLong(company));
		}
		if(!StringUtils.isEmpty(terminal)){
			terminallocation=genericDAO.getById(Location.class, Long.parseLong(terminal));
		}
		if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
			Map drivercriteria = new HashMap();
			drivercriteria.clear();
			//drivercriteria.put("status",1);
			drivercriteria.put("company.id",companylocation.getId());
			List<Driver> drivers = genericDAO.findByCriteria(Driver.class, drivercriteria);
			for(Driver driverObj:drivers){
				if(driverIds.equals(""))
					driverIds = driverObj.getId().toString();
				else
					driverIds = driverIds +","+driverObj.getId().toString();
			}
		}

		Driver driver=null;
		List<Driver> drivers=null;
		/*if(!StringUtils.isEmpty(driverid)){
		Driver employee= genericDAO.getById(Driver.class,Long.parseLong(driverid));
		criterias.put("fullName",employee.getFullName());
	    Driver driver=genericDAO.getByCriteria(Driver.class, criterias);
		}*/
		StringBuffer query=new StringBuffer("");

		query.append("select obj from Ticket obj where obj.payRollStatus=2 and  obj.billBatch>='"+frombatch+
				"' and obj.billBatch<='"+tobatch+"'");

		if(!StringUtils.isEmpty(driverid)){        	
			query.append(" and obj.driver.fullName='"+driverid+"'");
		}

		if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
			query.append(" and obj.driver.fullName='"+driverid+"'");
		}
		else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
			query.append(" and obj.driver in (").append(driverIds).append(")");
		}

		if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.terminal="+terminal);
		}
		if(!StringUtils.isEmpty(driversmul)){
			query.append(" and obj.driver not in ("+driversmul+")");
		}
		if(!StringUtils.isEmpty(status)){
			query.append("and  obj.payRollStatus in ("+status+")");
		}
		if(!StringUtils.isEmpty(payrollDate)){
			query.append("and obj.payRollBatch='"+payrollDate+"'");
		}

		/* if(StringUtils.isEmpty(driverid))*/
		if(StringUtils.contains(sum, "true")){

			StringBuffer driquery=new StringBuffer("");
			driquery.append("select DISTINCT(obj.driver.fullName) from Ticket obj where obj.payRollStatus=2 and obj.billBatch>='"+frombatch+
					"' and obj.billBatch<='"+tobatch+"' ");

			if(!StringUtils.isEmpty(terminal)){
				driquery.append(" and obj.terminal="+terminal);
			}

			if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid)){
				driquery.append(" and obj.driver.fullName='"+driverid+"'");
			}
			else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid)){
				driquery.append(" and obj.driver in (").append(driverIds).append(")");
			}

			if(!StringUtils.isEmpty(driverid)){        	
				driquery.append(" and obj.driver.fullName='"+driverid+"'");
			}

			if(!StringUtils.isEmpty(driversmul)){
				driquery.append(" and obj.driver not in ("+driversmul+")");
			}

			if(!StringUtils.isEmpty(status)){
				driquery.append("and  obj.payRollStatus in ("+status+")");
			}
			if(!StringUtils.isEmpty(payrollDate)){
				driquery.append("and obj.payRollBatch='"+payrollDate+"'");
			}
			driquery.append(" order by obj.driver.fullName");
			System.out.println("**************** test query is "+driquery.toString());
			drivers=genericDAO.executeSimpleQuery(driquery.toString());
			Map criti = new HashMap();
			for(int i=0;i<drivers.size();i++){	


				String driverFullName = String.valueOf(drivers.get(i));	
				criti.clear();
				//criti.put("status",1);
				criti.put("fullName",driverFullName);
				Driver driver2 = genericDAO.getByCriteria(Driver.class, criti);
				map.put(driver2.getFullName(),0.0);
				map1.put(driver2.getFullName(),0);
			}
		}        


		query.append(" group by obj.origin,obj.destination");
		if(StringUtils.isEmpty(driverid)){
			query.append(",obj.driver.fullName");
		}
		query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
		System.out.println("\n query-->"+query);
		List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
		DriverPayWrapper wrapper=new DriverPayWrapper();
		List<DriverPay> summarys=new ArrayList<DriverPay>();
		Map<String,Double> sumAmounts = new HashMap<String,Double>();
		wrapper.setDriverPays(summarys);
		double sumAmount=0.0;
		int totalcount=0;
		boolean expiredRate = false;
		List<String> driverNames = new ArrayList<String>();
		for(Ticket ticket:tickets){

			driverNames.add(ticket.getDriver().getFullName());
			StringBuffer countquery=new StringBuffer("");
			/*String*/ countquery.append("select count(obj) from Ticket obj where obj.payRollStatus=2 and obj.billBatch>='"+frombatch+
					"' and obj.billBatch<='"+tobatch+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId());
			if(!StringUtils.isEmpty(driverid)){
				countquery.append("and obj.driver.fullName='"+driverid+"'");
			}
			else{
				countquery.append("and obj.driver.fullName='"+ticket.getDriver().getFullName()+"'");
			}

			Long recordCount = (Long) genericDAO.getEntityManager()
			.createQuery(countquery.toString()).getSingleResult();

			StringBuffer ticketquery=new StringBuffer("");
			/*String*/ ticketquery.append("select obj from Ticket obj where obj.payRollStatus=2 and obj.billBatch>='"+frombatch+
					"' and obj.billBatch<='"+tobatch+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId());
			if(!StringUtils.isEmpty(driverid)){
				ticketquery.append("and obj.driver.fullName='"+driverid+"'");
			}
			else{
				ticketquery.append("and obj.driver.fullName='"+ticket.getDriver().getFullName()+"'");
			}

			List<Ticket> filtrdtickets = genericDAO.executeSimpleQuery(ticketquery.toString());  
			int sundaycount = 0;
			for(Ticket ticketObj : filtrdtickets) {
				LocalDate unloadDate = new LocalDate(ticketObj.getUnloadDate());

				if(unloadDate.getDayOfWeek() == DateTimeConstants.SUNDAY)
					sundaycount++;
			}
			double amount=0.0;
			double sundayAmount = 0.0;
			boolean calculateOtherRow =  true;

			if(sundaycount > 0){
				DriverPay pay=new DriverPay();
				int diffCount = Integer.parseInt(recordCount.toString()) - sundaycount;
				pay.setNoOfLoad(sundaycount);
				if(diffCount == 0.0){
					calculateOtherRow = false;
				}
				pay.setOrigin(ticket.getOrigin().getName());
				pay.setDestination(ticket.getDestination().getName());
				if(ticket.getCompanyLocation()!=null)
					pay.setCompanyname(ticket.getCompanyLocation().getName());
				else
					pay.setCompanyname("");

				if(ticket.getDriver().getTerminal()!=null)
					pay.setTerminalname(ticket.getDriver().getTerminal().getName());
				else
					pay.setTerminalname("");

				try{
					Long destination_id;
					Location location = genericDAO.getById(Location.class, ticket
							.getDestination().getId());
					if (location.getName().equalsIgnoreCase("grows")
							|| location.getName().equalsIgnoreCase("tullytown")) {
						destination_id = 91l;
					} else {
						destination_id = ticket.getDestination().getId();
					}
					String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"
						/* + ticket.getDestination().getId() + "'"; */
						+ destination_id 
						+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
						+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'";
					
					
					
					
					List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
					DriverPayRate payRate = null;
					if (fs != null && fs.size() > 0) {
						for (DriverPayRate rate : fs) {
							if (rate.getRateUsing() == null) {
								payRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
								// calculation for a load date
								if ((ticket.getLoadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getLoadDate().getTime() <= rate
												.getValidTo().getTime())) {
									payRate = rate;
									break;
								}
							} else if (rate.getRateUsing() == 2) {
								// calculation for a unload date
								if ((ticket.getUnloadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getUnloadDate().getTime() <= rate
												.getValidTo().getTime())) {
									payRate = rate;
									break;
								}
							}
						}
					}
					if(payRate==null){
						pay.setAmount(0.0);
						sumAmount+=0.0;
						if(StringUtils.contains(expire, "1")){
							if (!expiredRate)
								str.add("<u>Rates Are Expired or not Available</u></br>");
							expiredRate = true;
							errorCount++;
							Location originName = genericDAO.getById(Location.class,
									ticket.getOrigin().getId());
							Location DestinationName = genericDAO.getById(
									Location.class, ticket.getDestination().getId());
							boolean cont = str.contains((originName.getName() + " - "
									+ DestinationName.getName() + "</br>"));
							String string = (originName.getName() + " - "
									+ DestinationName.getName() + "</br>");

							if (!cont)
								str.add(string);
						}
					}else{
						Map criti=new HashMap();
						criti.clear();
						//criti.put("status",1);
						criti.put("fullName",ticket.getDriver().getFullName() );
						Driver empObj=genericDAO.getByCriteria(Driver.class, criti);					
						double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
						
						
						LocalDate dt= null;								
						if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							if(frombatch.equalsIgnoreCase(tobatch)){
								try {											
									dt = new LocalDate(frombatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
							else{
								try {
									dt = new LocalDate(tobatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
						}
						else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						boolean wbDrivers = false;
						
						if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
							if(ticket.getDriver().getDateProbationEnd()!=null){
								if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
									wbDrivers = true;
								}
							}
						}
						
						
						
						if(wbDrivers){
							double sundayRate = payRate.getProbationRate();
							sundayAmount = sundaycount * sundayRate;
							pay.setRate(payRate.getProbationRate());
						}
						else{
							if(empObj.getId()!=null){
								if(empObj.getShift().equals("1")){							
									if(sundaycount > 0){
										double sundayRate = payRate.getPayRate() * payRate.getSundayRateFactor();
										sundayAmount = sundaycount * sundayRate;
										pay.setRate(payRate.getPayRate() * payRate.getSundayRateFactor());
									}						
								}
								else{
									if(sundaycount > 0){
										double sundayRate = payRate.getNightPayRate() * payRate.getSundayRateFactor();
										sundayAmount = sundaycount * sundayRate;
										pay.setRate(payRate.getNightPayRate() * payRate.getSundayRateFactor());
									}						
								}
							}
							else{
								if(sundaycount > 0){
									double sundayRate = payRate.getPayRate() * payRate.getSundayRateFactor();
									sundayAmount = sundaycount * sundayRate;
									pay.setRate(payRate.getPayRate() * payRate.getSundayRateFactor());
								}					
							}
						}
						amount =  sundayAmount;
						sumAmount+=amount;
						amount=MathUtil.roundUp(amount, 2);
						pay.setAmount(amount);
						//pay.setRate(payRate.getPayRate());
					}
					pay.setNoOfLoad(sundaycount);
					//if(StringUtils.isEmpty(driverid))
					pay.setDrivername(ticket.getDriver().getFullName());
					//pay.setCompanyname(ticket.getDriver().getCompany().getName());
					totalcount+=pay.getNoOfLoad();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				summarys.add(pay);

			}

			if(calculateOtherRow){
				DriverPay pay=new DriverPay();
				pay.setNoOfLoad(Integer.parseInt(recordCount.toString())-sundaycount);
				pay.setOrigin(ticket.getOrigin().getName());
				pay.setDestination(ticket.getDestination().getName());
				if(ticket.getCompanyLocation()!=null)
					pay.setCompanyname(ticket.getCompanyLocation().getName());
				else
					pay.setCompanyname("");

				if(ticket.getDriver().getTerminal()!=null)
					pay.setTerminalname(ticket.getDriver().getTerminal().getName());
				else
					pay.setTerminalname("");

				try{
					Long destination_id;
					Location location = genericDAO.getById(Location.class, ticket
							.getDestination().getId());
					if (location.getName().equalsIgnoreCase("grows")
							|| location.getName().equalsIgnoreCase("tullytown")) {
						destination_id = 91l;
					} else {
						destination_id = ticket.getDestination().getId();
					}
					String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"
						/* + ticket.getDestination().getId() + "'"; */
						+ destination_id 
						+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
						+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'";
					
					
					
					List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
					DriverPayRate payRate = null;
					if (fs != null && fs.size() > 0) {
						for (DriverPayRate rate : fs) {
							if (rate.getRateUsing() == null) {
								payRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
								// calculation for a load date
								if ((ticket.getLoadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getLoadDate().getTime() <= rate
												.getValidTo().getTime())) {
									payRate = rate;
									break;
								}
							} else if (rate.getRateUsing() == 2) {
								// calculation for a unload date
								if ((ticket.getUnloadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getUnloadDate().getTime() <= rate
												.getValidTo().getTime())) {
									payRate = rate;
									break;
								}
							}
						}
					}
					if(payRate==null){
						pay.setAmount(0.0);
						sumAmount+=0.0;
						if(StringUtils.contains(expire, "1")){
							if (!expiredRate)
								str.add("<u>Rates Are Expired or not Available</u></br>");
							expiredRate = true;
							errorCount++;
							Location originName = genericDAO.getById(Location.class,
									ticket.getOrigin().getId());
							Location DestinationName = genericDAO.getById(
									Location.class, ticket.getDestination().getId());
							boolean cont = str.contains((originName.getName() + " - "
									+ DestinationName.getName() + "</br>"));
							String string = (originName.getName() + " - "
									+ DestinationName.getName() + "</br>");

							if (!cont)
								str.add(string);
						}
					}else{
						Map criti=new HashMap();
						criti.clear();
						//criti.put("status",1);
						criti.put("fullName",ticket.getDriver().getFullName() );
						Driver empObj=genericDAO.getByCriteria(Driver.class, criti);

						double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
						
						
						LocalDate dt= null;								
						if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							if(frombatch.equalsIgnoreCase(tobatch)){
								try {											
									dt = new LocalDate(frombatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
							else{
								try {
									dt = new LocalDate(tobatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
						}
						else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						boolean wbDrivers = false;
						
						if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
							if(ticket.getDriver().getDateProbationEnd()!=null){
								if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
									wbDrivers = true;
								}
							}
						}
						
						
						
						if(wbDrivers){
							pay.setRate(payRate.getProbationRate());
							amount= nofload*payRate.getProbationRate();
						}
						else{
							if(empObj.getId()!=null){
								if(empObj.getShift().equals("1")){						
									pay.setRate(payRate.getPayRate());						
									amount=nofload*payRate.getPayRate();
								}
								else{						
									pay.setRate(payRate.getNightPayRate());						
									amount= nofload*payRate.getNightPayRate();						
								}
							}
							else{
								pay.setRate(payRate.getPayRate());
								amount= nofload*payRate.getPayRate();					
							}		
						}
						sumAmount+=amount;
						amount=MathUtil.roundUp(amount, 2);
						pay.setAmount(amount);
						//pay.setRate(payRate.getPayRate());
					}
					pay.setNoOfLoad(pay.getNoOfLoad());
					//if(StringUtils.isEmpty(driverid))
					pay.setDrivername(ticket.getDriver().getFullName());
					//pay.setCompanyname(ticket.getDriver().getCompany().getName());
					totalcount+=pay.getNoOfLoad();			

					if(sumAmounts.get(ticket.getDriver().getFullName()) != null ){
						double driveramount= sumAmounts.get(ticket.getDriver().getFullName());
						driveramount = driveramount + amount;
						sumAmounts.put(ticket.getDriver().getFullName(),driveramount);
					}else{
						sumAmounts.put(ticket.getDriver().getFullName(),amount);
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				summarys.add(pay);
			}
		}

		wrapper.setDriverNames(driverNames);
		wrapper.setSumAmountsMap(sumAmounts);
		sumAmount=MathUtil.roundUp(sumAmount, 2);
		wrapper.setTotalRowCount(totalcount);
		wrapper.setSumTotal(sumAmount);		
		wrapper.setBatchDateFrom(input.getBatchDateFrom());
		wrapper.setBatchDateTo(input.getBatchDateTo());
		wrapper.setPayRollBatch(input.getPayrollBatchDate());
		if(!StringUtils.isEmpty(company)){
			wrapper.setCompany(companylocation.getName());
			wrapper.setCompanylocation(companylocation);
		}
		if(!StringUtils.isEmpty(terminal)){
			wrapper.setTerminal(terminallocation);
		}
		if(driver!=null){
			wrapper.setDriver(driver.getFullName());
			wrapper.setCompany(driver.getCompany().getName());
		}
		/*if(StringUtils.isEmpty(driverid))*/
		if(StringUtils.contains(sum, "true")){
			for(DriverPay driverPay:wrapper.getDriverPays()){
				Double amount=map.get(driverPay.getDrivername());
				if(amount==null){
					amount=0.0;	
				}
				Double totamount=amount+driverPay.getAmount();
				Integer count=map1.get(driverPay.getDrivername());
				if(count==null){
					count=0;
				}	
				Integer totcount=count+driverPay.getNoOfLoad();
				map1.put(driverPay.getDrivername(), totcount);
				map.put(driverPay.getDrivername(), totamount);
			}
			wrapper.getDriverPays().clear();
		}
		/*if(StringUtils.isEmpty(driverid))*/
		if(StringUtils.contains(sum, "true")){
			List<DriverPay> fields=new ArrayList<DriverPay>();
			wrapper.setDriverPays(fields);
			/*if(StringUtils.isEmpty(driverid))*/

			Map empmap=new HashMap();
			double TotalAmount=0.0;


			Map criti = new HashMap();
			String drivernames = "";
			for(int i=0;i<drivers.size();i++){
				System.out.println("***** The driver is "+drivers.get(i));		        	
				String driverFullName = String.valueOf(drivers.get(i));	
				if(drivernames.equals("")){
					drivernames ="'"+String.valueOf(drivers.get(i))+"'";	
				}
				else{
					drivernames = drivernames+",'"+ String.valueOf(drivers.get(i))+"'";
				}
				criti.clear();
				//criti.put("status",1);
				criti.put("fullName",driverFullName);
				Driver driver3 = genericDAO.getByCriteria(Driver.class, criti);

				Double deductionAmount = 0.0;
				Double sickParsonalAmount=0.0;
				Double vacationAmount=0.0;
				Double bonusAmount=0.0;
				Double miscAmount=0.0;
				Double holidayAmount=0.0;
				DriverPay pay=new DriverPay();
				pay.setDrivername(driver3.getFullName());
				pay.setCompanyname(driver3.getCompany().getName());
				pay.setTerminalname(driver3.getTerminal().getName());
				Double amount=0.0;
				amount=(Double)(map.get(driver3.getFullName()));				
				empmap.clear();
				empmap.put("fullName", driver3.getFullName());
				//empmap.put("status",1);
				Driver employee=genericDAO.getByCriteria(Driver.class, empmap);				
				Double miscamt=0.0;	
				Double reimburseAmount=0.0;	
				if(employee!=null){
					StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes!='Reimbursement'");
					if(!StringUtils.isEmpty(frombatch)){
						miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
					}			
					List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
					for(MiscellaneousAmount miscamnt:miscamounts){
						miscamt+=miscamnt.getMisamount();					
					}	

					StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes='Reimbursement'");
					if(!StringUtils.isEmpty(frombatch)){
						reimburseamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						reimburseamountquery.append(" and obj.batchTo<='"+tobatch+"'");
					}			
					List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
					for(MiscellaneousAmount reimbursecamnt:reimburseamounts){
						reimburseAmount+=reimbursecamnt.getMisamount();					
					}
				}

				amount=MathUtil.roundUp(amount, 2);
				miscamt=MathUtil.roundUp(miscamt, 2);
				pay.setTransportationAmount(amount);
				amount=amount+miscamt;				
				amount=MathUtil.roundUp(amount, 2);
				pay.setAmount(amount);
				pay.setMiscAmount(miscamt);
				pay.setReimburseAmount(reimburseAmount);
				pay.setNoOfLoad(map1.get(driver3.getFullName()));
				/*empmap.clear();
				empmap.put("fullName", driver3.getFullName());
				empmap.put("status",1);
				Driver employee=genericDAO.getByCriteria(Driver.class, empmap);*/
				if(employee!=null){
					StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						ptodquery.append(" and obj.batchdate>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						ptodquery.append(" and obj.batchdate<='"+tobatch+"'");
					}
					List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
					for(Ptodapplication ptodapplication:ptodapplications){
						if(ptodapplication.getLeavetype().getId()==1){
							sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
						}
						if(ptodapplication.getLeavetype().getId()==4){
							vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
						}
					}
					LocalDate dt= null;								
					if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						if(frombatch.equalsIgnoreCase(tobatch)){
							try {											
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else{
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
					}
					else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(frombatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}
					else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
						try {
							dt = new LocalDate(tobatch);
						} catch (Exception e) {
							System.out.println("Error Parsing Date");
						}
					}		
					if(employee.getCompany().getId()!=4l && employee.getTerminal().getId()!=93l){
						if(employee.getDateProbationEnd()!=null){
							if(new LocalDate(employee.getDateProbationEnd()).isAfter(dt)){
								double miscAndLoadAmt = pay.getTransportationAmount();											
								deductionAmount =  miscAndLoadAmt * 0.10;
								if(deductionAmount > 100.0){
									deductionAmount = 100.0;
								}
							}	
						}
					}
					deductionAmount=MathUtil.roundUp(deductionAmount, 2);

					StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
					}
					List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
					for(EmployeeBonus bonus:bonuses){
						for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
							bonusAmount+=list.getBonusamount();
							//miscAmount+=list.getMisamount();
						}
					}
					StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus=2 and obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
					if(!StringUtils.isEmpty(frombatch)){
						holidayquery.append(" and obj.batchdate='"+frombatch+"'");
					}
					/*if(!StringUtils.isEmpty(tobatch)){
							holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
						}*/
					List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
					for(HolidayType type:holidayTypes){
						holidayAmount=holidayAmount+type.getAmount();
					}
				}

				amount=pay.getTransportationAmount()-deductionAmount;				
				amount=MathUtil.roundUp(amount, 2);
				pay.setAmount(amount);

				pay.setProbationDeductionAmount(deductionAmount);
				pay.setBonusAmount(bonusAmount);
				pay.setSickPersonalAmount(sickParsonalAmount);
				pay.setVacationAmount(vacationAmount);
				pay.setHolidayAmount(holidayAmount);
				Double totalAmount=(pay.getTransportationAmount()-pay.getProbationDeductionAmount())+pay.getMiscAmount()+pay.getSickPersonalAmount()+pay.getBonusAmount()+pay.getHolidayAmount();
				totalAmount=MathUtil.roundUp(totalAmount, 2);
				TotalAmount+=totalAmount;
				pay.setTotalAmount(totalAmount);
				fields.add(pay);
			}

			//******************************************************************
			List<Driver> driverWithOutTickets = null;
			if(!drivernames.equals("") && StringUtils.isEmpty(driverid)){
				StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and  obj.fullName not in ("+drivernames+")");
				if(!StringUtils.isEmpty(company)){
					drivernameauery.append(" and obj.company="+company);
				}
				if(!StringUtils.isEmpty(terminal)){
					drivernameauery.append(" and obj.terminal="+terminal);
				}

				driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
			}
			else if(drivernames.equals("") && !StringUtils.isEmpty(driverid)){
				StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and  obj.fullName in ('"+driverid+"')");
				if(!StringUtils.isEmpty(company)){
					drivernameauery.append(" and obj.company="+company);
				}
				if(!StringUtils.isEmpty(terminal)){
					drivernameauery.append(" and obj.terminal="+terminal);
				}       	
				driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
			}
			else if(drivernames.equals("") && StringUtils.isEmpty(driverid)){
				StringBuffer drivernameauery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1'");
				if(!StringUtils.isEmpty(company)){
					drivernameauery.append(" and obj.company="+company);
				}
				if(!StringUtils.isEmpty(terminal)){
					drivernameauery.append(" and obj.terminal="+terminal);
				}       	
				driverWithOutTickets = genericDAO.executeSimpleQuery(drivernameauery.toString());
			}

			if(driverWithOutTickets!=null){
				for(Driver driverWithOutTicket: driverWithOutTickets){        

					boolean setDriver = false;
					Double deductionAmount = 0.0;
					Double sickParsonalAmount=0.0;
					Double vacationAmount=0.0;
					Double bonusAmount=0.0;
					Double miscAmount=0.0;
					Double holidayAmount=0.0;
					DriverPay pay=new DriverPay();
					pay.setDrivername(driverWithOutTicket.getFullName());
					if(driverWithOutTicket.getCompany()!=null)
						pay.setCompanyname(driverWithOutTicket.getCompany().getName());
					else
						pay.setCompanyname("");

					pay.setTerminalname(driverWithOutTicket.getTerminal().getName());
					Double amount=0.0;
					//amount=(Double)(map.get(driverWithOutTicket.getFullName()));				

					Driver employee= driverWithOutTicket;				
					Double miscamt=0.0;	
					Double reimburseAmount=0.0;	
					if(employee!=null){
						StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes!='Reimbursement'");
						if(!StringUtils.isEmpty(frombatch)){
							miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
						}			
						List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
						for(MiscellaneousAmount miscamnt:miscamounts){
							setDriver = true;
							miscamt+=miscamnt.getMisamount();					
						}	


						StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscNotes='Reimbursement'");
						if(!StringUtils.isEmpty(frombatch)){
							reimburseamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							reimburseamountquery.append(" and obj.batchTo<='"+tobatch+"'");
						}			
						List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
						for(MiscellaneousAmount reimbursecamnt:reimburseamounts){
							setDriver = true;
							reimburseAmount+=reimbursecamnt.getMisamount();					
						}
					}
					pay.setTransportationAmount(amount);
					amount=amount+miscamt;				
					amount=MathUtil.roundUp(amount, 2);
					pay.setAmount(amount);
					pay.setMiscAmount(miscamt);
					pay.setReimburseAmount(reimburseAmount);
					pay.setNoOfLoad(0);

					Driver employees=driverWithOutTicket;
					if(employees!=null){
						StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employees.getFullName()+"' and obj.category=2");
						if(!StringUtils.isEmpty(frombatch)){
							ptodquery.append(" and obj.batchdate>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							ptodquery.append(" and obj.batchdate<='"+tobatch+"'");
						}
						List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
						for(Ptodapplication ptodapplication:ptodapplications){
							if(ptodapplication.getLeavetype().getId()==1){
								setDriver = true;
								sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
							}
							if(ptodapplication.getLeavetype().getId()==4){
								setDriver = true;
								vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());

							}
						}

						LocalDate dt= null;								
						if(!StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							if(frombatch.equalsIgnoreCase(tobatch)){
								try {											
									dt = new LocalDate(frombatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
							else{
								try {
									dt = new LocalDate(tobatch);
								} catch (Exception e) {
									System.out.println("Error Parsing Date");
								}
							}
						}
						else if (!StringUtils.isEmpty(frombatch) && StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(frombatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}
						else if (StringUtils.isEmpty(frombatch) && !StringUtils.isEmpty(tobatch)){
							try {
								dt = new LocalDate(tobatch);
							} catch (Exception e) {
								System.out.println("Error Parsing Date");
							}
						}				


						if(employee.getDateProbationEnd()!=null){
							if(new LocalDate(employee.getDateProbationEnd()).isAfter(dt)){

							}
							else{

							}	
						}


						StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category=2");
						if(!StringUtils.isEmpty(frombatch)){
							bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
						}
						List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
						for(EmployeeBonus bonus:bonuses){
							for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
								setDriver = true;
								bonusAmount+=list.getBonusamount();
								//miscAmount+=list.getMisamount();
							}
						}
						StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus=2 and obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
						if(!StringUtils.isEmpty(frombatch)){
							holidayquery.append(" and obj.batchdate='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
						}
						List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
						for(HolidayType type:holidayTypes){
							setDriver = true;
							holidayAmount=holidayAmount+type.getAmount();
						}
					}
					pay.setProbationDeductionAmount(0.0);
					pay.setBonusAmount(bonusAmount);
					pay.setSickPersonalAmount(sickParsonalAmount);
					pay.setVacationAmount(vacationAmount);
					pay.setHolidayAmount(holidayAmount);
					Double totalAmount=pay.getAmount()+pay.getSickPersonalAmount()+pay.getBonusAmount()+pay.getHolidayAmount()-pay.getProbationDeductionAmount();
					totalAmount=MathUtil.roundUp(totalAmount, 2);

					if(setDriver) {
						TotalAmount+=totalAmount;
						pay.setTotalAmount(totalAmount);
						fields.add(pay);			
					}
				}
			}
			//************************************************************
			wrapper.setSumAmount(TotalAmount);
		}
		wrapper.setList(str);
		return wrapper;
	}
	
	///***********************************************************
	///***********************************************************
	///***********************************************************
	
	
	
	//@Override
	//public DriverPayWrapper generateDriverPayHistory(SearchCriteria criteria,
			//DriverPayHistoryInput input) {
		/*
	}
		// TODO Auto-generated method stub
		String driversinput=input.getDrivers();
		String companies=input.getCompanies();
		String terminals=input.getTerminals();
		String batchDateFrom=input.getBatchDateFrom();
		String batchDateTo=input.getBatchDateTo();
		String payrollBatchDate=input.getPayrollBatchDate();
		batchDateFrom=ReportDateUtil.getFromDate(batchDateFrom);
		batchDateTo=ReportDateUtil.getToDate(batchDateTo);
		payrollBatchDate=ReportDateUtil.getFromDate(payrollBatchDate);
		String payrollstatus=input.getPayrollstatus();
		Map<String, Double> map=new HashMap<String, Double>();
		Map<String, Integer> map1=new HashMap<String, Integer>();
		StringBuffer query=new StringBuffer("");
		StringBuffer driverids=new StringBuffer("");
		Map criterias=new HashMap();
		if(!StringUtils.isEmpty(driversinput)){
			String[] driverinput=driversinput.split(",");
			for(String driverstr:driverinput){
				Driver employee=genericDAO.getById(Driver.class,Long.parseLong(driverstr));
				criterias.put("fullName", employee.getFullName());
				Driver driver=genericDAO.getByCriteria(Driver.class,criterias);
				if(driver!=null){
					driverids.append(driver.getId());
					driverids.append(",");
				}
			}
			int d=driverids.lastIndexOf(",");
			if(d>0){
				driverids.deleteCharAt(d);
			}
		}
		query.append("select obj from Ticket obj where obj.status=1 and  obj.billBatch>='"+batchDateFrom+
    			"' and obj.billBatch<='"+batchDateTo+"'");
		if(!StringUtils.isEmpty(companies)){
			query.append(" and obj.companyLocation in ("+companies+")");
		}
		if(!StringUtils.isEmpty(terminals)){
			query.append(" and obj.terminal in ("+terminals+")");
		}
		if(!StringUtils.isEmpty(driversinput)){
			query.append(" and obj.driver in ("+driverids+")");
		}
		 if(!StringUtils.isEmpty(payrollstatus)){
    		 query.append("and  obj.payRollStatus in ("+payrollstatus+")");
    	 }
		 if(!StringUtils.isEmpty(payrollBatchDate)){
			 query.append("and obj.payRollBatch='"+payrollBatchDate+"'");
		 }
			StringBuffer query1=new StringBuffer("");
	    	query1.append("select DISTINCT(obj.driver) from Ticket obj where obj.status=1 and  obj.billBatch>='"+batchDateFrom+
	    	   		"' and obj.billBatch<='"+batchDateTo+"'");
    	        	if(!StringUtils.isEmpty(companies)){
    	        		  query1.append("and obj.companyLocation in ("+companies+")");
    	                }
    	        	 if(!StringUtils.isEmpty(terminals)){
    	        		  query1.append(" and obj.terminal in ("+terminals+")");
    	                 }
    	        	 if(!StringUtils.isEmpty(driversinput)){
    	        		  query1.append("and obj.driver in ("+driverids+")");
    	        	        }
    	        	 if(!StringUtils.isEmpty(payrollstatus)){
		        		 query1.append("and  obj.payRollStatus in ("+payrollstatus+")");
		        	 }
    	        	 if(!StringUtils.isEmpty(payrollBatchDate)){
    	    			 query1.append("and obj.payRollBatch='"+payrollBatchDate+"'");
    	    		 }
    	        	 	  query1.append(" order by obj.driver.fullName");
    	        	 	List<Driver>  drivers=genericDAO.executeSimpleQuery(query1.toString());
    	                  for(Driver driver2:drivers){
    	                 	 map.put(driver2.getFullName(),0.0);
    	                 	 map1.put(driver2.getFullName(),0);
    	                  }
    	                  query.append(" group by obj.origin,obj.destination");
    	                  query.append(",obj.driver");
    	                  query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
    	                  System.out.println("\n query-->"+query);
    	          		List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
    	          		DriverPayWrapper wrapper=new DriverPayWrapper();
    	        		List<DriverPay> summarys=new ArrayList<DriverPay>();
    	        		wrapper.setDriverPays(summarys);
    	        		double sumAmount=0.0;
    	        		int totalcount=0;
    	        		for(Ticket ticket:tickets){
    	        			StringBuffer countquery=new StringBuffer("");
    	        			countquery.append("select count(obj) from Ticket obj where obj.status=1 and  obj.billBatch>='"+batchDateFrom+
    	        		    "' and obj.billBatch<='"+batchDateTo+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId());
    	        			 countquery.append("and obj.driver="+ticket.getDriver().getId());
    	        			 if(!StringUtils.isEmpty(payrollstatus)){
    	        				 countquery.append("and  obj.payRollStatus in ("+payrollstatus+")");
    			        	 }
    	        			 if(!StringUtils.isEmpty(payrollBatchDate)){
    	        				 countquery.append("and obj.payRollBatch='"+payrollBatchDate+"'");
    	        			 }
    	        			Long recordCount = (Long) genericDAO.getEntityManager()
    	        			.createQuery(countquery.toString()).getSingleResult();
    	        			double amount=0.0;
    	        			DriverPay pay=new DriverPay();
    	        			pay.setNoOfLoad(Integer.parseInt(recordCount.toString()));
    	        			pay.setOrigin(ticket.getOrigin().getName());
    	        			pay.setDestination(ticket.getDestination().getName());
    	        			try{
    	        				Long destination_id;
    	        				Location location = genericDAO.getById(Location.class, ticket
    	        						.getDestination().getId());
    	        				if (location.getName().equalsIgnoreCase("grows")
    	        						|| location.getName().equalsIgnoreCase("tullytown")) {
    	                                destination_id = 91l;
    	                       } else {
    	        					destination_id = ticket.getDestination().getId();
    	        				}
    	        				String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
    	        						+ ticket.getOrigin().getId() + "' and obj.landfill='"
    	        						 + ticket.getDestination().getId() + "'"; 
    	        						+ destination_id + "'";
    	        			List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
    	        			DriverPayRate payRate = null;
    	        			if (fs != null && fs.size() > 0) {
    	        				for (DriverPayRate rate : fs) {
    	        					if (rate.getRateUsing() == null) {
    	        						payRate = rate;
    	        						break;
    	        					} else if (rate.getRateUsing() == 1) {
    	        						// calculation for a load date
    	        						if ((ticket.getLoadDate().getTime() >= rate
    	        								.getValidFrom().getTime())
    	        								&& (ticket.getLoadDate().getTime() <= rate
    	        										.getValidTo().getTime())) {
    	        							payRate = rate;
    	        							break;
    	        						}
    	        					} else if (rate.getRateUsing() == 2) {
    	        						// calculation for a unload date
    	        						if ((ticket.getUnloadDate().getTime() >= rate
    	        								.getValidFrom().getTime())
    	        								&& (ticket.getUnloadDate().getTime() <= rate
    	        										.getValidTo().getTime())) {
    	        							payRate = rate;
    	        							break;
    	        						}
    	        					}
    	        				}
    	        				}
    	        			if(payRate==null){
    	        				pay.setAmount(0.0);
    	        				sumAmount+=0.0;
    	        			}else{
    	        				double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
    	        				amount= nofload*payRate.getPayRate();
    	        				sumAmount+=amount;
    	        				amount=MathUtil.roundUp(amount, 2);
    	        				pay.setAmount(amount);
    	        				pay.setRate(payRate.getPayRate());
    	        			}
    	        			
    	        			
    	        			if(payRate==null){
    	        				pay.setAmount(0.0);
    	        				sumAmount+=0.0;
    	        			}else{ 	        				
    	        				Map criti=new HashMap();
    	        				criti.clear();
    	        				criti.put("fullName",ticket.getDriver().getFullName() );
    	        				Driver empObj=genericDAO.getByCriteria(Driver.class, criti);  	        				
    	        				double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
    	        				if(empObj.getId()!=null){
    	        					if(empObj.getShift().equals("1")){	    	        						
    	        						amount= nofload*payRate.getPayRate();
    	        						pay.setRate(payRate.getPayRate());
    	        					}
    	        					else{    	        						
    	        						amount= nofload*payRate.getNightPayRate();
    	        						pay.setRate(payRate.getNightPayRate());
    	        					}
    	        				}
    	        				else{					
    	        					amount= nofload*payRate.getPayRate();
    	        					pay.setRate(payRate.getPayRate());
    	        				}    	        				
    	        				//amount= nofload*payRate.getPayRate();
    	        				sumAmount+=amount;
    	        				amount=MathUtil.roundUp(amount, 2);
    	        				pay.setAmount(amount);
    	        				//pay.setRate(payRate.getPayRate())
    	        			}   	        			
    	        			
    	        			pay.setDrivername(ticket.getDriver().getFullName());
    	        			pay.setCompanyname(ticket.getDriver().getCompany().getName());
    	        			totalcount+=pay.getNoOfLoad();
    	        			}
    	        			catch (Exception ex) {
    	        				ex.printStackTrace();
    	        			}
    	        			summarys.add(pay);
    	        		}
    	        		sumAmount=MathUtil.roundUp(sumAmount, 2);
    	        		wrapper.setTotalRowCount(totalcount);
    	        		wrapper.setSumTotal(sumAmount);
    	        		wrapper.setBatchDateFrom(input.getBatchDateFrom());
    	        		wrapper.setBatchDateTo(input.getBatchDateTo());
    	        		wrapper.setPayRollBatch(input.getPayrollBatchDate());
    	        		for(DriverPay driverPay:wrapper.getDriverPays()){
    	    				Double amount=map.get(driverPay.getDrivername());
    	    				Double totamount=amount+driverPay.getAmount();
    	    				Integer count=map1.get(driverPay.getDrivername());
    	    				Integer totcount=count+driverPay.getNoOfLoad();
    	    				map1.put(driverPay.getDrivername(), totcount);
    	    				map.put(driverPay.getDrivername(), totamount);
    	    			}
    	    			wrapper.getDriverPays().clear();
    	    			List<DriverPay> fields=new ArrayList<DriverPay>();
    	    			wrapper.setDriverPays(fields);
    	    			Map empmap=new HashMap();
    	    			double TotalAmount=0.0;
    	    			{
    	    				for(Driver driver3:drivers){
    	    					Double sickParsonalAmount=0.0;
    	    					Double vacationAmount=0.0;
    	    					Double bonusAmount=0.0;
    	    					Double holidayAmount=0.0;
    	    					DriverPay pay=new DriverPay();
    	    					pay.setDrivername(driver3.getFullName());
    	    					pay.setCompanyname(driver3.getCompany().getName());
    	    					pay.setAmount(map.get(driver3.getFullName()));
    	    					pay.setNoOfLoad(map1.get(driver3.getFullName()));
    	    					empmap.clear();
    	    					empmap.put("fullName", driver3.getFullName());
    	    					Driver employee=genericDAO.getByCriteria(Driver.class, empmap);
    	    					if(employee!=null){
    	    						StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+employee.getId()+" and obj.category=2");
    	    						if(!StringUtils.isEmpty(batchDateFrom)){
    	    						ptodquery.append(" and obj.batchdate>='"+batchDateFrom+"'");
    	    						}
    	    						if(!StringUtils.isEmpty(batchDateTo)){
    	    							ptodquery.append(" and obj.batchdate<='"+batchDateTo+"'");
    	    						}
    	    						List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
    	    						for(Ptodapplication ptodapplication:ptodapplications){
    	    							if(ptodapplication.getLeavetype().getId()==1){
    	    								sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
    	    							}
    	    							if(ptodapplication.getLeavetype().getId()==4){
    	    								vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
    	    							}
    	    						}
    	    						StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver="+employee.getId()+" and obj.category=2");
    	    						if(!StringUtils.isEmpty(batchDateFrom)){
    	    							bonusquery.append(" and obj.batchFrom>='"+batchDateFrom+"'");
    	    							}
    	    							if(!StringUtils.isEmpty(batchDateTo)){
    	    								bonusquery.append(" and obj.batchTo<='"+batchDateTo+"'");
    	    							}
    	    							List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
    	    							for(EmployeeBonus bonus:bonuses){
    	    								for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
    	    									bonusAmount+=list.getBonusamount();
    	    								}
    	    							}
    	    							StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+employee.getCompany().getId()+" and obj.terminal="+employee.getTerminal().getId()+" and obj.catagory="+employee.getCatagory().getId()+" and obj.leaveType=3");
    	    							if(!StringUtils.isEmpty(batchDateFrom)){
    	    								holidayquery.append(" and obj.batchdate<='"+batchDateFrom+"'");
    	    								}
    	    								if(!StringUtils.isEmpty(batchDateTo)){
    	    									holidayquery.append(" and obj.batchdate<='"+batchDateTo+"'");
    	    								}
    	    								List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
    	    								for(HolidayType type:holidayTypes){
    	    									holidayAmount=holidayAmount+type.getAmount();
    	    								}		
    	    							
    	    					}
    	    					pay.setBonusAmount(bonusAmount);
    	    					pay.setSickPersonalAmount(sickParsonalAmount);
    	    					pay.setVacationAmount(vacationAmount);
    	    					pay.setHolidayAmount(holidayAmount);
    	    					Double totalAmount=pay.getAmount()+pay.getSickPersonalAmount()+pay.getVacationAmount()+pay.getBonusAmount()+pay.getHolidayAmount();
    	    					totalAmount=MathUtil.roundUp(totalAmount, 2);
    	    					TotalAmount+=totalAmount;
    	    					pay.setTotalAmount(totalAmount);
    	    					fields.add(pay);
    	    				}
    	    			}
    	    			wrapper.setSumAmount(TotalAmount);
    	    			return wrapper;
	*///}

	@Override
	public DriverPayWrapper generateDriverPayHistoryDetail(
			SearchCriteria criteria, DriverPayHistoryInput input) {
		// TODO Auto-generated method stub
		String driversinput=input.getDrivers();
		String companies=input.getCompanies();
		String terminals=input.getTerminals();
		String batchDateFrom=input.getBatchDateFrom();
		String batchDateTo=input.getBatchDateTo();
		String payrollBatchDate=input.getPayrollBatchDate();
		batchDateFrom=ReportDateUtil.getFromDate(batchDateFrom);
		batchDateTo=ReportDateUtil.getToDate(batchDateTo);
		payrollBatchDate=ReportDateUtil.getFromDate(payrollBatchDate);
		String payrollstatus=input.getPayrollstatus();
		Map<String, Double> map=new HashMap<String, Double>();
		Map<String, Integer> map1=new HashMap<String, Integer>();
		StringBuffer query=new StringBuffer("");
		StringBuffer driverids=new StringBuffer("");
		Map criterias=new HashMap();
		if(!StringUtils.isEmpty(driversinput)){
			String[] driverinput=driversinput.split(",");
			for(String driverstr:driverinput){
				Driver employee=genericDAO.getById(Driver.class,Long.parseLong(driverstr));
				criterias.put("status",1);
				criterias.put("fullName", employee.getFullName());
				Driver driver=genericDAO.getByCriteria(Driver.class,criterias);
				if(driver!=null){
					driverids.append(driver.getId());
					driverids.append(",");
				}
			}
			int d=driverids.lastIndexOf(",");
			if(d>0){
				driverids.deleteCharAt(d);
			}
		}
						query.append("select obj from Ticket obj where obj.status=1 and  obj.billBatch>='"+batchDateFrom+
				    			"' and obj.billBatch<='"+batchDateTo+"'");
						if(!StringUtils.isEmpty(companies)){
							query.append(" and obj.companyLocation in ("+companies+")");
						}
						if(!StringUtils.isEmpty(terminals)){
							query.append(" and obj.terminal in ("+terminals+")");
						}
						if(!StringUtils.isEmpty(driversinput)){
							query.append(" and obj.driver in ("+driverids+")");
						}
						 if(!StringUtils.isEmpty(payrollstatus)){
			        		 query.append("and  obj.payRollStatus in ("+payrollstatus+")");
			        	 }
						 if(!StringUtils.isEmpty(payrollBatchDate)){
							 query.append("and obj.payRollBatch='"+payrollBatchDate+"'");
	        			 }
						  query.append(" group by obj.origin,obj.destination");
    	                  query.append(",obj.driver");
    	                  query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
    	                  System.out.println("\n query-->"+query);
    	          		List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
    	          		DriverPayWrapper wrapper=new DriverPayWrapper();
    	        		List<DriverPay> summarys=new ArrayList<DriverPay>();
    	        		wrapper.setDriverPays(summarys);
    	        		double sumAmount=0.0;
    	        		int totalcount=0;
    	        		for(Ticket ticket:tickets){
    	        			StringBuffer countquery=new StringBuffer("");
    	        			countquery.append("select count(obj) from Ticket obj where obj.status=1 and  obj.billBatch>='"+batchDateFrom+
    	        		    "' and obj.billBatch<='"+batchDateTo+"'  and obj.origin="+ticket.getOrigin().getId()+" and obj.destination="+ticket.getDestination().getId());
    	        			 countquery.append("and obj.driver="+ticket.getDriver().getId());
    	        			 if(!StringUtils.isEmpty(payrollstatus)){
    	        				 countquery.append("and  obj.payRollStatus in ("+payrollstatus+")");
    	    	        	 }
    						 if(!StringUtils.isEmpty(payrollBatchDate)){
    							 countquery.append("and obj.payRollBatch='"+payrollBatchDate+"'");
    	        			 }
    	        			Long recordCount = (Long) genericDAO.getEntityManager()
    	        			.createQuery(countquery.toString()).getSingleResult();
    	        			double amount=0.0;
    	        			DriverPay pay=new DriverPay();
    	        			pay.setNoOfLoad(Integer.parseInt(recordCount.toString()));
    	        			pay.setOrigin(ticket.getOrigin().getName());
    	        			pay.setDestination(ticket.getDestination().getName());
    	        			try{
    	        				Long destination_id;
    	        				Location location = genericDAO.getById(Location.class, ticket
    	        						.getDestination().getId());
    	        				if (location.getName().equalsIgnoreCase("grows")
    	        						|| location.getName().equalsIgnoreCase("tullytown")) {
    	                                destination_id = 91l;
    	                       } else {
    	        					destination_id = ticket.getDestination().getId();
    	        				}
    	        				String rateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
    	        						+ ticket.getOrigin().getId() + "' and obj.landfill='"
    	        						/* + ticket.getDestination().getId() + "'"; */
    	        						+ destination_id 
    	    							+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
    	    							+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'";
    	        				
    	        				
    	        				
    	        			List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery);
    	        			DriverPayRate payRate = null;
    	        			if (fs != null && fs.size() > 0) {
    	        				for (DriverPayRate rate : fs) {
    	        					if (rate.getRateUsing() == null) {
    	        						payRate = rate;
    	        						break;
    	        					} else if (rate.getRateUsing() == 1) {
    	        						// calculation for a load date
    	        						if ((ticket.getLoadDate().getTime() >= rate
    	        								.getValidFrom().getTime())
    	        								&& (ticket.getLoadDate().getTime() <= rate
    	        										.getValidTo().getTime())) {
    	        							payRate = rate;
    	        							break;
    	        						}
    	        					} else if (rate.getRateUsing() == 2) {
    	        						// calculation for a unload date
    	        						if ((ticket.getUnloadDate().getTime() >= rate
    	        								.getValidFrom().getTime())
    	        								&& (ticket.getUnloadDate().getTime() <= rate
    	        										.getValidTo().getTime())) {
    	        							payRate = rate;
    	        							break;
    	        						}
    	        					}
    	        				}
    	        				}
    	        			if(payRate==null){
    	        				pay.setAmount(0.0);
    	        				sumAmount+=0.0;
    	        			}else{ 	        				
    	        				Map criti=new HashMap();
    	        				criti.clear();
    	        				criti.put("status",1);
    	        				criti.put("fullName",ticket.getDriver().getFullName() );
    	        				Driver empObj=genericDAO.getByCriteria(Driver.class, criti);  	        				
    	        				double nofload=Double.parseDouble(pay.getNoOfLoad()+"");
    	        				
    	        				
    	        				LocalDate dt= null;								
    	        				if(!StringUtils.isEmpty(batchDateFrom) && !StringUtils.isEmpty(batchDateTo)){
    	        					if(batchDateFrom.equalsIgnoreCase(batchDateTo)){
    	        						try {											
    	        							dt = new LocalDate(batchDateFrom);
    	        						} catch (Exception e) {
    	        							System.out.println("Error Parsing Date");
    	        						}
    	        					}
    	        					else{
    	        						try {
    	        							dt = new LocalDate(batchDateTo);
    	        						} catch (Exception e) {
    	        							System.out.println("Error Parsing Date");
    	        						}
    	        					}
    	        				}
    	        				else if (!StringUtils.isEmpty(batchDateFrom) && StringUtils.isEmpty(batchDateTo)){
    	        					try {
    	        						dt = new LocalDate(batchDateFrom);
    	        					} catch (Exception e) {
    	        						System.out.println("Error Parsing Date");
    	        					}
    	        				}
    	        				else if (StringUtils.isEmpty(batchDateFrom) && !StringUtils.isEmpty(batchDateTo)){
    	        					try {
    	        						dt = new LocalDate(batchDateTo);
    	        					} catch (Exception e) {
    	        						System.out.println("Error Parsing Date");
    	        					}
    	        				}
    	        				boolean wbDrivers = false;
    	        				
    	        				if(ticket.getDriver().getCompany().getId()==4l && ticket.getDriver().getTerminal().getId()==93l){
    	        					if(ticket.getDriver().getDateProbationEnd()!=null){
    	        						if(new LocalDate(ticket.getDriver().getDateProbationEnd()).isAfter(dt) || new LocalDate(ticket.getDriver().getDateProbationEnd()).isEqual(dt)){
    	        							wbDrivers = true;
    	        						}
    	        					}
    	        				}
    	        				
    	        				
    	        				
    	        				if(wbDrivers){
    	        					amount= nofload*payRate.getProbationRate();
									pay.setRate(payRate.getProbationRate());
    							}
    							else{
    								if(empObj.getId()!=null){
    									if(empObj.getShift().equals("1")){	    	        						
    										amount= nofload*payRate.getPayRate();
    										pay.setRate(payRate.getPayRate());
    									}
    									else{    	        						
    										amount= nofload*payRate.getNightPayRate();
    										pay.setRate(payRate.getNightPayRate());
    									}
    								}
    								else{					
    									amount= nofload*payRate.getPayRate();
    									pay.setRate(payRate.getPayRate());
    								}  
    							}
    	        				//amount= nofload*payRate.getPayRate();
    	        				sumAmount+=amount;
    	        				amount=MathUtil.roundUp(amount, 2);
    	        				pay.setAmount(amount);
    	        				//pay.setRate(payRate.getPayRate())
    	        			}
    	        			pay.setDrivername(ticket.getDriver().getFullName());
    	        			pay.setCompanyname(ticket.getDriver().getCompany().getName());
    	        			totalcount+=pay.getNoOfLoad();
    	        			}
    	        			catch (Exception ex) {
    	        				ex.printStackTrace();
    	        			}
    	        			summarys.add(pay);
    	        		}
    	        		sumAmount=MathUtil.roundUp(sumAmount, 2);
    	        		wrapper.setTotalRowCount(totalcount);
    	        		wrapper.setSumTotal(sumAmount);
    	        		wrapper.setBatchDateFrom(input.getBatchDateFrom());
    	        		wrapper.setBatchDateTo(input.getBatchDateTo());
    	        		wrapper.setPayRollBatch(input.getPayrollBatchDate());
    	        		return wrapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteEmployeePayrollData(HourlyPayrollInvoice invoice)
			throws Exception {
		Map params = new HashMap();
		Location companyObj = genericDAO.getById(Location.class,Long.valueOf(invoice.getCompany()));
		List<HourlyPayrollInvoiceDetails> datas=null;
		params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(invoice.getPayrollinvoicedate()));
		//params.put("company", companyObj.getName());
		params.put("batchdate",new SimpleDateFormat("yyyy-MM-dd").format(invoice.getBillBatchFrom()));
		params.put("batchdateTo",new SimpleDateFormat("yyyy-MM-dd").format(invoice.getBillBatchTo()));
		params.put("companyLoc",invoice.getCompanyLoc());
		params.put("terminalLoc",invoice.getTerminal());
		
		
		StringBuilder hourlyDeleteQuery= new StringBuilder("select obj from HourlyPayrollInvoiceDetails obj where  obj.date='").append( new SimpleDateFormat("yyyy-MM-dd").format(invoice.getPayrollinvoicedate())).append("' and obj.batchdate='");
		hourlyDeleteQuery.append(new SimpleDateFormat("yyyy-MM-dd").format(invoice.getBillBatchFrom())).append("' and obj.batchdateTo='").append(new SimpleDateFormat("yyyy-MM-dd").format(invoice.getBillBatchTo())).append("' and obj.companyLoc=").append(invoice.getCompanyLoc().getId());
		if(invoice.getTerminal()!=null)
			hourlyDeleteQuery.append(" and obj.terminalLoc="+invoice.getTerminal().getId());
		else
			hourlyDeleteQuery.append(" and obj.terminalLoc="+null);
		
		
		
		datas = genericDAO.executeSimpleQuery(hourlyDeleteQuery.toString());
		
		String driverNames = "-1";
		Map criterias = new HashMap();
		
		if (datas != null && datas.size() > 0) {			
			for(HourlyPayrollInvoiceDetails details:datas){
				if(details.getTimesheet()!=null){
					TimeSheet sheet=genericDAO.getById(TimeSheet.class, details.getTimesheet().getId());
					sheet.setHourlypayrollinvoiceNumber(null);
					sheet.setHourlypayrollinvoiceDate(null);
					sheet.setHourlypayrollstatus(1);
					genericDAO.saveOrUpdate(sheet);
				}
				
				criterias.clear();
				criterias.put("fullName",details.getDriver() );
				//criterias.put("status",1);
				//Driver drvObj = genericDAO.getByCriteria(Driver.class, criterias);
				List<Driver> drivers = genericDAO.findByCriteria(Driver.class, criterias);
				for(Driver drvObj:drivers){
					if(drvObj.getPayTerm().equals("2")){
						driverNames = driverNames+","+drvObj.getId().toString();
					}					
				}
				
				genericDAO.delete(details);			
			}
			
			 StringBuffer ptodquery=new StringBuffer("update Ptodapplication t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.driver in ("+driverNames+")");
			 //StringBuffer holidayquery=new StringBuffer("update HolidayType t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.leaveType=3" );
			 StringBuffer empbonusquery=new StringBuffer("update EmployeeBonus t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.driver in ("+driverNames+")");
			 StringBuffer miscAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.miscNotes!='Reimbursement' and t.driver in ("+driverNames+")");
			 StringBuffer reimAmountquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.miscNotes='Reimbursement' and t.driver in ("+driverNames+")");
			// StringBuffer quarterBonusquery=new StringBuffer("update MiscellaneousAmount t set t.payRollStatus=1,t.payRollBatch=null where 1=1 and  t.payRollStatus=2 and t.miscType='Quarter Bonus'" );
			      
			
			if(invoice.getBillBatchFrom()!=null){			
				ptodquery.append(" and t.batchdate>='"+mysqldf.format(invoice.getBillBatchFrom())+"'");
				//holidayquery.append(" and t.batchdate>='"+mysqldf.format(invoice.getBillBatchFrom())+"'");
				empbonusquery.append(" and t.batchFrom>='"+mysqldf.format(invoice.getBillBatchFrom())+"'");
				miscAmountquery.append(" and t.batchFrom>='"+mysqldf.format(invoice.getBillBatchFrom())+"'");
				reimAmountquery.append(" and t.batchFrom>='"+mysqldf.format(invoice.getBillBatchFrom())+"'");
				//quarterBonusquery.append(" and t.batchFrom>='"+mysqldf.format(invoice.getBillBatchFrom())+"'");
			}
			if(invoice.getBillBatchTo()!=null){			
				ptodquery.append(" and t.batchdate<='"+mysqldf.format(invoice.getBillBatchTo())+"'");
				//holidayquery.append(" and t.batchdate<='"+mysqldf.format(invoice.getBillBatchTo())+"'");
				empbonusquery.append(" and t.batchTo<='"+mysqldf.format(invoice.getBillBatchTo())+"'");
				miscAmountquery.append(" and t.batchTo<='"+mysqldf.format(invoice.getBillBatchTo())+"'");
				reimAmountquery.append(" and t.batchTo<='"+mysqldf.format(invoice.getBillBatchTo())+"'");
				//quarterBonusquery.append(" and t.batchTo<='"+mysqldf.format(invoice.getBillBatchTo())+"'");
			}
			 if(invoice.getTerminal()!=null){	            
		            ptodquery.append(" and t.terminal="+invoice.getTerminal().getId());
					//holidayquery.append(" and t.terminal="+invoice.getTerminal().getId());
					empbonusquery.append(" and t.terminal="+invoice.getTerminal().getId());
					miscAmountquery.append(" and t.terminal="+invoice.getTerminal().getId());
					reimAmountquery.append(" and t.terminal="+invoice.getTerminal().getId());
					//quarterBonusquery.append(" and t.terminal="+invoice.getTerminal().getId());
		     }	
			 
			 if(invoice.getPayrollinvoicedate()!=null){
				 ptodquery.append(" and t.payRollBatch='"+mysqldf.format(invoice.getPayrollinvoicedate())+"'");
				// holidayquery.append(" and t.payRollBatch='"+mysqldf.format(invoice.getPayrollinvoicedate())+"'");
				 empbonusquery.append(" and t.payRollBatch='"+mysqldf.format(invoice.getPayrollinvoicedate())+"'");
				 miscAmountquery.append(" and t.payRollBatch='"+mysqldf.format(invoice.getPayrollinvoicedate())+"'");
				 reimAmountquery.append(" and t.payRollBatch='"+mysqldf.format(invoice.getPayrollinvoicedate())+"'");
				 //quarterBonusquery.append(" and t.payRollBatch='"+mysqldf.format(invoice.getPayrollinvoicedate())+"'");
			 }
			  
			 
			 
			  genericDAO.getEntityManager().createQuery(ptodquery.toString()).executeUpdate();
			  //genericDAO.getEntityManager().createQuery(holidayquery.toString()).executeUpdate();
			  genericDAO.getEntityManager().createQuery(empbonusquery.toString()).executeUpdate();
			  genericDAO.getEntityManager().createQuery(miscAmountquery.toString()).executeUpdate();
			  genericDAO.getEntityManager().createQuery(reimAmountquery.toString()).executeUpdate();
			  //genericDAO.getEntityManager().createQuery(quarterBonusquery.toString()).executeUpdate();
			
			genericDAO.delete(invoice);
		}		
	}
	
	
	
	
	
	@Override
	public WeeklypayWrapper generateWeeklyPayrollData(SearchCriteria criteria,String from) {

		String employee=(String)criteria.getSearchMap().get("driver");
		String company=(String)criteria.getSearchMap().get("company");
		String terminal=(String)criteria.getSearchMap().get("terminal");
		String payrollDate=(String)criteria.getSearchMap().get("payrollDate");
		String payrollNumber=(String)criteria.getSearchMap().get("checkDate");
		String driversmul=(String)criteria.getSearchMap().get("driversmul");
		payrollNumber = ReportDateUtil.getFromDate(payrollNumber);
		payrollDate=ReportDateUtil.getFromDate(payrollDate);
		Date paydate = null;
		Date checkDate=null;
		Location companyid=null;
		Location terminalid=null;
		try {
			paydate = new SimpleDateFormat("MM-dd-yyyy")
			.parse((String)criteria.getSearchMap().get("payrollDate"));
			checkDate  = new SimpleDateFormat("MM-dd-yyyy")
			.parse((String)criteria.getSearchMap().get("checkDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		StringBuffer query=new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='3'");
		if(!StringUtils.isEmpty(company)){
			query.append(" and obj.company="+company);
			companyid=genericDAO.getById(Location.class, Long.parseLong(company));
		}

		if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.terminal="+terminal);
			terminalid=genericDAO.getById(Location.class, Long.parseLong(terminal));
		}
		
		if(!StringUtils.isEmpty(employee)){
			query.append(" and obj.fullName ='"+employee+"'");
		}

		if(!StringUtils.isEmpty(driversmul)){
			query.append(" and obj.id not in ("+driversmul+")");
		}

		if(!StringUtils.isEmpty(payrollDate)){

		}
		query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.catagory.name asc, obj.fullName asc");

		List<Driver> employees=genericDAO.executeSimpleQuery(query.toString());
		WeeklypayWrapper wrapper=new WeeklypayWrapper();
		List<WeeklyPayDetail> summary=new ArrayList<WeeklyPayDetail>();
		wrapper.setDetails(summary);
		double sumTotal=0.0;
		double sumAmount=0.0;
		for(Driver employee2:employees){
			
			String checkdate = ReportDateUtil.getFromDate((String)criteria.getSearchMap().get("checkDate"));
			String queryToFindRecInDPD = "SELECT w From WeeklyPayDetail w WHERE driver='"+employee2.getFullName()+"'AND w.company ='"+company+"' AND w.checkDate ='"+checkdate+"' AND w.payRollBatch='"+payrollDate+"'";
			List<WeeklyPayDetail> wpdList = genericDAO.executeSimpleQuery(queryToFindRecInDPD);
			
			if((wpdList.size() == 0 && StringUtils.equalsIgnoreCase(from, "payroll")) 
					|| (wpdList.size() > 0 && StringUtils.equalsIgnoreCase(from, "payrollreport"))){
				
				double sickParsonalAmount=0.0;
				double paidSickPersonalAmount=0.0;
				double paidVacationAmount=0.0;
				double vacationAmount=0.0;
				double bonusAmount=0.0;
				double holidayAmount=0.0;
				Double miscamt=0.0;
				Double reimburseamt=0.0;
				// Bereavement change - salary
				double bereavementAmount=0.0;
				String query1="select obj from WeeklySalary obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.catagory="+employee2.getCatagory().getId()+" and obj.company="+employee2.getCompany().getId()+" and obj.terminal="+employee2.getTerminal().getId()+"and '"+payrollDate+"' BETWEEN obj.validFrom and obj.validTo";
				
				List<WeeklySalary> salaries=genericDAO.executeSimpleQuery(query1);
				
				double weeklysalary=0.0;
				if(!salaries.isEmpty()){
					WeeklySalary salary=salaries.get(0);
					if(salary.getWeeklySalary()!=null){
						weeklysalary=salary.getWeeklySalary();
					}
				}
				
				// Salary override req - 29th May 2016
				Double salaryOverrideAmount = retrieveSalaryOverride(employee2.getFullName(), employee2.getCompany().getId(), 
						employee2.getTerminal().getId(), payrollDate);
				if (salaryOverrideAmount != null) {
					weeklysalary = salaryOverrideAmount;
				}
				
				WeeklyPayDetail detail=new WeeklyPayDetail();
				detail.setCheckDate(checkDate);
				detail.setDriver(employee2.getFullName());
				detail.setCompanyname(employee2.getCompany().getName());
				detail.setCategory(employee2.getCatagory().getName());
				detail.setTerminalName(employee2.getTerminal().getName());
				detail.setAmount(weeklysalary);
				StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.approvestatus=1 and obj.driver.fullName='"+employee2.getFullName()+"' and obj.category="+employee2.getCatagory().getId());
				if(!StringUtils.isEmpty(payrollDate)){
					ptodquery.append(" and obj.batchdate='"+payrollDate+"'");
				}

				List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
				for(Ptodapplication ptodapplication:ptodapplications){
					if(ptodapplication.getLeavetype().getId()==1){
						sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
						paidSickPersonalAmount = paidSickPersonalAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
					}
					// Jury duty fix - salary - 3rd Nov 2016
					if(ptodapplication.getLeavetype().getId()==9){
						sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
						paidSickPersonalAmount = paidSickPersonalAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
					}
					if(ptodapplication.getLeavetype().getId()==4){
						vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
						paidVacationAmount = paidVacationAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
					}
					
					// Bereavement change - salary
					if(ptodapplication.getLeavetype().getId() == 8) {
						bereavementAmount = bereavementAmount + ptodapplication.getSequenceAmt1();
					}
				}
				detail.setVacationAmount(vacationAmount);
				detail.setSickPersonalAmount(sickParsonalAmount);
				
				// Bereavement change - salary
				detail.setBereavementAmount(bereavementAmount);

				// Bereavement change - salary
				detail.setAmount(detail.getAmount()-(paidSickPersonalAmount+paidVacationAmount+bereavementAmount));

				StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.category="+employee2.getCatagory().getId());
				if(!StringUtils.isEmpty(payrollDate)){
					bonusquery.append(" and obj.batchFrom='"+payrollDate+"'");
				}

				List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
				for(EmployeeBonus bonus:bonuses){
					for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
						bonusAmount+=list.getBonusamount();
					}
				}
				detail.setBonusAmount(bonusAmount);

				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.miscNotes!='Reimbursement'");
				if(!StringUtils.isEmpty(payrollDate)){
					miscamountquery.append(" and obj.batchFrom='"+payrollDate+"'");
				}

				List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
				int count=0;

				for(MiscellaneousAmount miscamnt:miscamounts){
					miscamt+=miscamnt.getMisamount();
				}

				detail.setMiscAmount(miscamt);

				StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.miscNotes ='Reimbursement'");
				if(!StringUtils.isEmpty(payrollDate)){
					reimburseamountquery.append(" and obj.batchFrom='"+payrollDate+"'");
				}

				List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
				int reimbursecount=0;
				for(MiscellaneousAmount reimburseamount:reimburseamounts){
					reimburseamt+=reimburseamount.getMisamount();
				}

				detail.setReimburseAmount(reimburseamt);

				StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+employee2.getCompany().getId()+" and obj.terminal="+employee2.getTerminal().getId()+" and obj.catagory="+employee2.getCatagory().getId()+" and obj.leaveType=3");
				if(!StringUtils.isEmpty(payrollDate)){
					holidayquery.append(" and obj.batchdate='"+payrollDate+"'");
				}

				List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
				for(HolidayType type:holidayTypes){
					holidayAmount=holidayAmount+type.getAmount();
				}
				detail.setHolidayAmount(holidayAmount);
				sumTotal+=detail.getAmount();
				// Bereavement change - salary - Should bereavement be added below?
				double totalamount=detail.getAmount()+detail.getSickPersonalAmount()+detail.getMiscAmount()+detail.getBonusAmount()+detail.getHolidayAmount();
				totalamount=MathUtil.roundUp(totalamount, 2);
				sumAmount+=totalamount;
				detail.setTotalAmount(totalamount);
				detail.setCompany(employee2.getCompany());
				detail.setTerminal(employee2.getTerminal());
				detail.setPayRollBatch(paydate);
				summary.add(detail);
			}
			// 19th Jan 2017 - Salary payroll multiple run
			else if((wpdList.size() > 0 && StringUtils.equalsIgnoreCase(from, "payroll"))) {
				WeeklyPayDetail detail1 = processMultipleSalaryPayrollRun(employee2, payrollDate, 
						 paydate, checkDate);
				
				//check 
				if (detail1.getAmount() != null && detail1.getAmount() != 0.0) {
					sumTotal+=detail1.getAmount();
				}
				
				//check 
				//sumAmount+=totalamount;
				if (detail1.getTotalAmount() != null && detail1.getTotalAmount() != 0.0) {
					sumAmount+=detail1.getTotalAmount();
				}
				
				if ((detail1.getTotalAmount() != null && detail1.getTotalAmount() != 0.0) 
						|| (detail1.getReimburseAmount() != null && detail1.getReimburseAmount() != 0.0)) {
					summary.add(detail1);
				}
			}	
		}
		wrapper.setSumAmount(sumAmount);
		wrapper.setPayRollBatch((String)criteria.getSearchMap().get("payrollDate"));
		wrapper.setSumTotal(sumTotal);
		wrapper.setTotalRowCount(employees.size());
		wrapper.setPayrollNumber((String)criteria.getSearchMap().get("payrollNumber"));        
		if(companyid!=null){
			wrapper.setCompanylocation(companyid);
			wrapper.setCompany(companyid.getName());
		}
		if(terminalid!=null){
			wrapper.setTerminall(terminalid);
			wrapper.setTerminal(terminalid.getName());
		}
		return wrapper;
	}
	
	// 19th Jan 2017 - Salary payroll multiple run
	private WeeklyPayDetail processMultipleSalaryPayrollRun(Driver employee2, String payrollDate, Date paydate,
			Date checkDate) {
		double sickParsonalAmount=0.0;
		double paidSickPersonalAmount=0.0;
		double paidVacationAmount=0.0;
		double vacationAmount=0.0;
		double bonusAmount=0.0;
		double holidayAmount=0.0;
		Double miscamt=0.0;
		Double reimburseamt=0.0;
		// Bereavement change - salary
		double bereavementAmount=0.0;
		
		WeeklyPayDetail detail=new WeeklyPayDetail();
		detail.setCheckDate(checkDate);
		detail.setDriver(employee2.getFullName());
		detail.setCompanyname(employee2.getCompany().getName());
		detail.setCategory(employee2.getCatagory().getName());
		detail.setTerminalName(employee2.getTerminal().getName());
		
		//check
		//detail.setAmount(weeklysalary);
		detail.setAmount(0.0);
		
		StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.approvestatus=1 "
				+ " and obj.driver.fullName='"+employee2.getFullName()+"' and obj.category="+employee2.getCatagory().getId());
		if(!StringUtils.isEmpty(payrollDate)){
			ptodquery.append(" and obj.batchdate='"+payrollDate+"'");
		}
		ptodquery.append(" and obj.payRollStatus=1");

		List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
		for(Ptodapplication ptodapplication:ptodapplications){
			if(ptodapplication.getLeavetype().getId()==1){
				sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
				paidSickPersonalAmount = paidSickPersonalAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
			}
			// Jury duty fix - salary - 3rd Nov 2016
			if(ptodapplication.getLeavetype().getId()==9){
				sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
				paidSickPersonalAmount = paidSickPersonalAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
			}
			if(ptodapplication.getLeavetype().getId()==4){
				vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
				paidVacationAmount = paidVacationAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
			}
			
			// Bereavement change - salary
			if(ptodapplication.getLeavetype().getId() == 8) {
				bereavementAmount = bereavementAmount + ptodapplication.getSequenceAmt1();
			}
		}
		detail.setVacationAmount(vacationAmount);
		detail.setSickPersonalAmount(sickParsonalAmount);
		
		// Bereavement change - salary
		detail.setBereavementAmount(bereavementAmount);

		// Bereavement change - salary
		// Check 
		//detail.setAmount(detail.getAmount()-(paidSickPersonalAmount+paidVacationAmount+bereavementAmount));
		detail.setAmount((paidSickPersonalAmount+paidVacationAmount+bereavementAmount));

		StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver.fullName='"+employee2.getFullName()
				+"' and obj.category="+employee2.getCatagory().getId());
		if(!StringUtils.isEmpty(payrollDate)){
			bonusquery.append(" and obj.batchFrom='"+payrollDate+"'");
		}
		bonusquery.append(" and obj.payRollStatus=1");

		List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
		for(EmployeeBonus bonus:bonuses){
			for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
				bonusAmount+=list.getBonusamount();
			}
		}
		detail.setBonusAmount(bonusAmount);

		StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"
					+employee2.getFullName()+"' and obj.miscNotes!='Reimbursement'");
		if(!StringUtils.isEmpty(payrollDate)){
			miscamountquery.append(" and obj.batchFrom='"+payrollDate+"'");
		}
		miscamountquery.append(" and obj.payRollStatus=1");

		List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
		int count=0;

		for(MiscellaneousAmount miscamnt:miscamounts){
			miscamt+=miscamnt.getMisamount();
		}

		detail.setMiscAmount(miscamt);

		StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"
					+employee2.getFullName()+"' and obj.miscNotes ='Reimbursement'");
		if(!StringUtils.isEmpty(payrollDate)){
			reimburseamountquery.append(" and obj.batchFrom='"+payrollDate+"'");
		}
		reimburseamountquery.append(" and obj.payRollStatus=1");

		List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
		int reimbursecount=0;
		for(MiscellaneousAmount reimburseamount:reimburseamounts){
			reimburseamt+=reimburseamount.getMisamount();
		}

		detail.setReimburseAmount(reimburseamt);

		StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+employee2.getCompany().getId()
						+" and obj.terminal="+employee2.getTerminal().getId()+" and obj.catagory="+employee2.getCatagory().getId()+" and obj.leaveType=3");
		if(!StringUtils.isEmpty(payrollDate)){
			holidayquery.append(" and obj.batchdate='"+payrollDate+"'");
		}
		holidayquery.append(" and obj.payRollStatus=1");

		List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
		for(HolidayType type:holidayTypes){
			holidayAmount=holidayAmount+type.getAmount();
		}
		detail.setHolidayAmount(holidayAmount);
		
		//check 
		//sumTotal+=detail.getAmount();
		
		// Bereavement change - salary - Should bereavement be added below?
		double totalamount=detail.getAmount()+detail.getSickPersonalAmount()+detail.getMiscAmount()+detail.getBonusAmount()+detail.getHolidayAmount();
		totalamount=MathUtil.roundUp(totalamount, 2);
		
		//check 
		//sumAmount+=totalamount;
		
		detail.setTotalAmount(totalamount);
		detail.setCompany(employee2.getCompany());
		detail.setTerminal(employee2.getTerminal());
		detail.setPayRollBatch(paydate);
		
		// check 
		//summary.add(detail);
		
		return detail;
	}
	
	// Salary override req - 29th May 2016
	private Double retrieveSalaryOverride(String employeeFullName, Long companyId, Long terminalId, String payrollDate) {
		String query = "select obj from SalaryOverride obj where obj.driver.fullName='"+employeeFullName
				+"' and obj.company="+companyId+" and obj.terminal="+terminalId+" and obj.payrollBatch='"+payrollDate+"'";
		
		List<SalaryOverride> salaryOverrideList = genericDAO.executeSimpleQuery(query);
		if (!salaryOverrideList.isEmpty()) {
			return salaryOverrideList.get(0).getAmount();
		}
		
		return null;
	}

	//@Override
	//public WeeklypayWrapper generateWeeklyPayrollData(SearchCriteria criteria) {
		/*
	}
		// TODO Auto-generated method stub
		String employee=(String)criteria.getSearchMap().get("driver");
		String company=(String)criteria.getSearchMap().get("company");
		String terminal=(String)criteria.getSearchMap().get("terminal");
		String payrollDate=(String)criteria.getSearchMap().get("payrollDate");
		String payrollNumber=(String)criteria.getSearchMap().get("checkDate");
		String driversmul=(String)criteria.getSearchMap().get("driversmul");
		payrollNumber = ReportDateUtil.getFromDate(payrollNumber);
		payrollDate=ReportDateUtil.getFromDate(payrollDate);
		Date paydate = null;
		Date checkDate=null;
		Location companyid=null;
		Location terminalid=null;
		try {
			paydate = new SimpleDateFormat("MM-dd-yyyy")
			.parse((String)criteria.getSearchMap().get("payrollDate"));
			checkDate  = new SimpleDateFormat("MM-dd-yyyy")
			.parse((String)criteria.getSearchMap().get("checkDate"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer query=new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='3'");
		if(!StringUtils.isEmpty(company)){
			query.append(" and obj.company="+company);
			companyid=genericDAO.getById(Location.class, Long.parseLong(company));
		}
      
        if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.terminal="+terminal);
			terminalid=genericDAO.getById(Location.class, Long.parseLong(terminal));
			
		}
        if(!StringUtils.isEmpty(employee)){
        	query.append(" and obj.fullName ='"+employee+"'");
		}
        
       
        if(!StringUtils.isEmpty(driversmul)){
        	query.append(" and obj.id not in ("+driversmul+")");
        }
        
        if(!StringUtils.isEmpty(payrollDate)){
			
		}
        query.append(" order by obj.company.name asc, obj.terminal.name asc, obj.catagory.name asc, obj.fullName asc");
        
        List<Driver> employees=genericDAO.executeSimpleQuery(query.toString());
        
        WeeklypayWrapper wrapper=new WeeklypayWrapper();
        List<WeeklyPayDetail> summary=new ArrayList<WeeklyPayDetail>();
        wrapper.setDetails(summary);
        double sumTotal=0.0;
        double sumAmount=0.0;
        for(Driver employee2:employees){
        	double sickParsonalAmount=0.0;
        	double paidSickPersonalAmount=0.0;
        	double paidVacationAmount=0.0;
        	double vacationAmount=0.0;
        	double bonusAmount=0.0;
        	double holidayAmount=0.0;
        	Double miscamt=0.0;
			Double reimburseamt=0.0;
        	String query1="select obj from WeeklySalary obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.catagory="+employee2.getCatagory().getId()+" and obj.company="+employee2.getCompany().getId()+" and obj.terminal="+employee2.getTerminal().getId()+"and '"+payrollDate+"' BETWEEN obj.validFrom and obj.validTo";
			List<WeeklySalary> salaries=genericDAO.executeSimpleQuery(query1);
			double weeklysalary=0.0;
			if(!salaries.isEmpty()){
				WeeklySalary salary=salaries.get(0);
				if(salary.getWeeklySalary()!=null){
					weeklysalary=salary.getWeeklySalary();
				}
			}
			WeeklyPayDetail detail=new WeeklyPayDetail();
			detail.setCheckDate(checkDate);
			detail.setDriver(employee2.getFullName());
			detail.setCompanyname(employee2.getCompany().getName());
			detail.setCategory(employee2.getCatagory().getName());
			detail.setTerminalName(employee2.getTerminal().getName());
			detail.setAmount(weeklysalary);
			StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.approvestatus=1 and obj.driver.fullName='"+employee2.getFullName()+"' and obj.category="+employee2.getCatagory().getId());
			if(!StringUtils.isEmpty(payrollDate)){
			ptodquery.append(" and obj.batchdate='"+payrollDate+"'");
			}
			
			List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
			for(Ptodapplication ptodapplication:ptodapplications){
				if(ptodapplication.getLeavetype().getId()==1){
					sickParsonalAmount=sickParsonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
					paidSickPersonalAmount = paidSickPersonalAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
				}
				if(ptodapplication.getLeavetype().getId()==4){
					vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
					paidVacationAmount = paidVacationAmount + (ptodapplication.getDayspaid()*ptodapplication.getPtodrates())+(ptodapplication.getHourspaid()*ptodapplication.getPtodhourlyrate());
				}
			}
			detail.setVacationAmount(vacationAmount);
			detail.setSickPersonalAmount(sickParsonalAmount);
			
			detail.setAmount(detail.getAmount()-(paidSickPersonalAmount+paidVacationAmount));
			
			
			StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.category="+employee2.getCatagory().getId());
			if(!StringUtils.isEmpty(payrollDate)){
				ptodquery.append(" and obj.batchFrom='"+payrollDate+"'");
			}
				
				List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
				for(EmployeeBonus bonus:bonuses){
					for(EmpBonusTypesList list:bonus.getBonusTypesLists()){
						bonusAmount+=list.getBonusamount();
					}
				}
				detail.setBonusAmount(bonusAmount);
				
				
				
				StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.miscNotes!='Reimbursement'");
				if(!StringUtils.isEmpty(payrollDate)){
					miscamountquery.append(" and obj.batchFrom='"+payrollDate+"'");
				}
			
			
				List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
				int count=0;
				
				for(MiscellaneousAmount miscamnt:miscamounts){
					miscamt+=miscamnt.getMisamount();
				}
				
				detail.setMiscAmount(miscamt);
				
				
				
				StringBuffer reimburseamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+employee2.getFullName()+"' and obj.miscNotes ='Reimbursement'");
				if(!StringUtils.isEmpty(payrollDate)){
					reimburseamountquery.append(" and obj.batchFrom='"+payrollDate+"'");
				}
			
			
				List<MiscellaneousAmount> reimburseamounts=genericDAO.executeSimpleQuery(reimburseamountquery.toString());
				int reimbursecount=0;
				for(MiscellaneousAmount reimburseamount:reimburseamounts){
					reimburseamt+=reimburseamount.getMisamount();
				}
				
				detail.setReimburseAmount(reimburseamt);
				
				
				
				
				StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+employee2.getCompany().getId()+" and obj.terminal="+employee2.getTerminal().getId()+" and obj.catagory="+employee2.getCatagory().getId()+" and obj.leaveType=3");
				if(!StringUtils.isEmpty(payrollDate)){
					holidayquery.append(" and obj.batchdate='"+payrollDate+"'");
					}
					
					List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
					for(HolidayType type:holidayTypes){
						holidayAmount=holidayAmount+type.getAmount();
					}
					detail.setHolidayAmount(holidayAmount);
					sumTotal+=detail.getAmount();
					double totalamount=detail.getAmount()+detail.getSickPersonalAmount()+detail.getMiscAmount()+detail.getBonusAmount()+detail.getHolidayAmount();
					totalamount=MathUtil.roundUp(totalamount, 2);
					sumAmount+=totalamount;
					detail.setTotalAmount(totalamount);
					detail.setCompany(employee2.getCompany());
					detail.setTerminal(employee2.getTerminal());
					detail.setPayRollBatch(paydate);
					summary.add(detail);
        }
        wrapper.setSumAmount(sumAmount);
        wrapper.setPayRollBatch((String)criteria.getSearchMap().get("payrollDate"));
        wrapper.setSumTotal(sumTotal);
        wrapper.setTotalRowCount(employees.size());
        wrapper.setPayrollNumber((String)criteria.getSearchMap().get("payrollNumber"));        
        if(companyid!=null){
        wrapper.setCompanylocation(companyid);
        wrapper.setCompany(companyid.getName());
        }
        if(terminalid!=null){
        wrapper.setTerminall(terminalid);
        wrapper.setTerminal(terminalid.getName());
        }
		return wrapper;
	*///}

	
	@Override
    public void saveWeeklyPayData(HttpServletRequest request,
            SearchCriteria criteria,String from) throws Exception {

        WeeklypayWrapper wrapper=generateWeeklyPayrollData(criteria,from);
        WeeklyPay pay=new WeeklyPay();
        pay.setSumAmount(wrapper.getSumAmount());
        pay.setSumTotal(wrapper.getSumTotal());
        pay.setCompany(wrapper.getCompanylocation());
        Date checkdate = new SimpleDateFormat("MM-dd-yyyy")
        .parse((String)criteria.getSearchMap().get("checkDate"));
        pay.setCheckDate(checkdate);
        if(wrapper.getTerminall()!=null)
            pay.setTerminal(wrapper.getTerminall());
        pay.setTotalRowCount(wrapper.getTotalRowCount());
        Date paydate = new SimpleDateFormat("MM-dd-yyyy")
        .parse((String)criteria.getSearchMap().get("payrollDate"));
        pay.setPayRollBatch(paydate);
        if(wrapper.getDetails().size()>0){
            for(WeeklyPayDetail detail:wrapper.getDetails()){
                genericDAO.saveOrUpdate(detail);
                Map criti = new HashMap();
				String driverIds="";
				Driver drvObj=null;
                criti.clear();
                criti.put("fullName", detail.getDriver());
                List<Driver> drvObjList = genericDAO.findByCriteria(Driver.class, criti);
                for(Driver driver:drvObjList){
                	if(driverIds.equals("")){
                		driverIds=driver.getId().toString();
                	}
                	else{
                		driverIds=driverIds+","+driver.getId().toString();
                	}
                	drvObj=driver;
                }
                StringBuffer ptodUpdateQuery=new StringBuffer("Update Ptodapplication obj set obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"',obj.payRollStatus=2 where obj.approvestatus=1 and obj.payRollStatus=1 and obj.driver in ("+driverIds+") and obj.category="+drvObj.getCatagory().getId()+" and obj.batchdate='"+mysqldf.format(detail.getPayRollBatch())+"'");
                StringBuffer bonusUpdateQuery=new StringBuffer("Update EmployeeBonus obj set obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"',obj.payRollStatus=2 where obj.payRollStatus=1 and obj.driver in ("+driverIds+") and obj.category="+drvObj.getCatagory().getId()+" and obj.batchFrom='"+mysqldf.format(detail.getPayRollBatch())+"'");
                StringBuffer miscamountUpdateQuery=new StringBuffer("Update MiscellaneousAmount obj set obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"',obj.payRollStatus=2 where obj.payRollStatus=1 and obj.driver in ("+driverIds+") and obj.batchFrom='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.miscNotes!='Reimbursement'");
                StringBuffer reimburseUpdateQuery=new StringBuffer("Update MiscellaneousAmount obj set obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"',obj.payRollStatus=2 where obj.payRollStatus=1 and obj.driver in ("+driverIds+") and obj.batchFrom='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.miscNotes='Reimbursement'");
                StringBuffer holidayUpdateQuery=new StringBuffer("Update HolidayType obj set obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"' ,obj.payRollStatus=2 where obj.payRollStatus=1 and obj.paid=1 and obj.company="+drvObj.getCompany().getId()+" and obj.terminal="+drvObj.getTerminal().getId()+" and obj.catagory="+drvObj.getCatagory().getId()+" and obj.leaveType=3 and obj.batchdate='"+mysqldf.format(detail.getPayRollBatch())+"'");
				
                genericDAO.getEntityManager().createQuery(ptodUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(bonusUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(miscamountUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(reimburseUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(holidayUpdateQuery.toString()).executeUpdate();
                
                System.out.println("********** The update query is "+ptodUpdateQuery.toString());
                System.out.println("********** The update query is "+bonusUpdateQuery.toString());
                System.out.println("********** The update query is "+miscamountUpdateQuery.toString());
                System.out.println("********** The update query is "+reimburseUpdateQuery.toString());
                System.out.println("********** The update query is "+holidayUpdateQuery.toString());
				
                
            }
            genericDAO.saveOrUpdate(pay);

        }       
    }
	
	
	//@Override
	//public void saveWeeklyPayData(HttpServletRequest request,
			//SearchCriteria criteria) throws Exception {
		
		/*
	}
		// TODO Auto-generated method stub
		
		WeeklypayWrapper wrapper=generateWeeklyPayrollData(criteria);
		WeeklyPay pay=new WeeklyPay();
		pay.setSumAmount(wrapper.getSumAmount());
		pay.setSumTotal(wrapper.getSumTotal());
		pay.setCompany(wrapper.getCompanylocation());
		Date checkdate = new SimpleDateFormat("MM-dd-yyyy")
		.parse((String)criteria.getSearchMap().get("checkDate"));
		pay.setCheckDate(checkdate);
		if(wrapper.getTerminall()!=null)
		pay.setTerminal(wrapper.getTerminall());
		pay.setTotalRowCount(wrapper.getTotalRowCount());
		 Date paydate = new SimpleDateFormat("MM-dd-yyyy")
			.parse((String)criteria.getSearchMap().get("payrollDate"));
		pay.setPayRollBatch(paydate);
		if(wrapper.getDetails().size()>0){
			for(WeeklyPayDetail detail:wrapper.getDetails()){
				genericDAO.saveOrUpdate(detail);
			}
		genericDAO.saveOrUpdate(pay);
			
		}		
	*///}

	@Override
	public void deleteWeeklyPayrollData(WeeklyPay pay) throws Exception {
		// TODO Auto-generated method stub
		Map criteria = new HashMap();
		Map params = new HashMap();
		List<WeeklyPayDetail> datas=null;
		criteria.put("company", pay.getCompany());
		criteria.put("payRollBatch", pay.getPayRollBatch());
		criteria.put("checkDate", pay.getCheckDate());
		if(pay.getTerminal()!=null)
		criteria.put("terminal", pay.getTerminal());
		datas=genericDAO.findByCriteria(WeeklyPayDetail.class, criteria);
		if (datas != null && datas.size() > 0) {
			for(WeeklyPayDetail detail:datas){
				Map criti = new HashMap();
				String driverIds="";
				Driver drvObj=null;
                criti.clear();
                criti.put("fullName", detail.getDriver());
                List<Driver> drvObjList = genericDAO.findByCriteria(Driver.class, criti);
                for(Driver driver:drvObjList){
                	if(driverIds.equals("")){
                		driverIds=driver.getId().toString();
                	}
                	else{
                		driverIds=driverIds+","+driver.getId().toString();
                	}
                	drvObj=driver;
                }
                StringBuffer ptodUpdateQuery=new StringBuffer("Update Ptodapplication obj set obj.payRollBatch=null,obj.payRollStatus=1 where obj.approvestatus=1 and obj.payRollStatus=2 and obj.driver in ("+driverIds+") and obj.category="+drvObj.getCatagory().getId()+" and obj.batchdate='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"'");
                StringBuffer bonusUpdateQuery=new StringBuffer("Update EmployeeBonus obj set obj.payRollBatch=null,obj.payRollStatus=1 where obj.payRollStatus=2 and obj.driver in ("+driverIds+") and obj.category="+drvObj.getCatagory().getId()+" and obj.batchFrom='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"'");
                StringBuffer miscamountUpdateQuery=new StringBuffer("Update MiscellaneousAmount obj set obj.payRollBatch=null,obj.payRollStatus=1 where obj.payRollStatus=2 and obj.driver in ("+driverIds+") and obj.batchFrom='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.miscNotes!='Reimbursement' and obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"'");
                StringBuffer reimburseUpdateQuery=new StringBuffer("Update MiscellaneousAmount obj set obj.payRollBatch=null,obj.payRollStatus=1 where obj.payRollStatus=2 and obj.driver in ("+driverIds+") and obj.batchFrom='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.miscNotes='Reimbursement' and obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"'");
                StringBuffer holidayUpdateQuery=new StringBuffer("Update HolidayType obj set obj.payRollBatch=null,obj.payRollStatus=1 where obj.payRollStatus=2 and obj.paid=1 and obj.company="+drvObj.getCompany().getId()+" and obj.terminal="+drvObj.getTerminal().getId()+" and obj.catagory="+drvObj.getCatagory().getId()+" and obj.leaveType=3 and obj.batchdate='"+mysqldf.format(detail.getPayRollBatch())+"' and obj.payRollBatch='"+mysqldf.format(detail.getCheckDate())+"'");
				
                genericDAO.getEntityManager().createQuery(ptodUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(bonusUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(miscamountUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(reimburseUpdateQuery.toString()).executeUpdate();
                genericDAO.getEntityManager().createQuery(holidayUpdateQuery.toString()).executeUpdate();
                
                System.out.println("********** The update query is "+ptodUpdateQuery.toString());
                System.out.println("********** The update query is "+bonusUpdateQuery.toString());
                System.out.println("********** The update query is "+miscamountUpdateQuery.toString());
                System.out.println("********** The update query is "+reimburseUpdateQuery.toString());
                System.out.println("********** The update query is "+holidayUpdateQuery.toString());
				genericDAO.delete(detail);
			}
			genericDAO.delete(pay);
		}
	}
	
	// Bereavement change - driver
	private void addBereavementPayForDriver(DriverPay driverPay, List<PayChexDetail> summary)  {
		Driver driver = retrieveDriver(driverPay);
		if (driver == null) {
			return;
		}
		
		StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+ driver.getFullName()+"'");
		ptodquery.append(" and obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");
		
		List<Ptodapplication> ptodapplications = genericDAO.executeSimpleQuery(ptodquery.toString());
		for(Ptodapplication ptodapplication:ptodapplications){
			if(ptodapplication.getLeavetype().getId() != 8) {
				continue;
			}
			
			List<Integer> sequenceNumber = new ArrayList<Integer>();
			List<Double>  sequenceAmount = new ArrayList<Double>();
			
			sequenceNumber.add(ptodapplication.getSequenceNum1());
			sequenceNumber.add(ptodapplication.getSequenceNum2());
			sequenceNumber.add(ptodapplication.getSequenceNum3());
			sequenceNumber.add(ptodapplication.getSequenceNum4());
			
			sequenceAmount.add(ptodapplication.getSequenceAmt1());
			sequenceAmount.add(ptodapplication.getSequenceAmt2());
			sequenceAmount.add(ptodapplication.getSequenceAmt3());
			sequenceAmount.add(ptodapplication.getSequenceAmt4());
			
			sequenceNumber.add(ptodapplication.getSequenceNum5());
			sequenceNumber.add(ptodapplication.getSequenceNum6());
			sequenceNumber.add(ptodapplication.getSequenceNum7());
			sequenceNumber.add(ptodapplication.getSequenceNum8());
			sequenceAmount.add(ptodapplication.getSequenceAmt5());
			sequenceAmount.add(ptodapplication.getSequenceAmt6());
			sequenceAmount.add(ptodapplication.getSequenceAmt7());
			sequenceAmount.add(ptodapplication.getSequenceAmt8());
							
			for(int i = 0; i < 8; i++) {
				if(sequenceNumber.get(i) != 0 && sequenceAmount.get(i) != 0.0) {	
					PayChexDetail detail2 = new PayChexDetail();
					
					Terminal terminal = retrieveTerminal(driver);
					if(terminal != null && terminal.getHomeBranch() != null) {
						detail2.setHomeBr(terminal.getHomeBranch().toString());
					}
			
					detail2.setSeqNo(sequenceNumber.get(i));
					detail2.setHomeDpt(driver.getCatagory().getCode());
					detail2.setEeNo(driver.getStaffId());
					detail2.setLastName(driver.getLastName());
					detail2.setFirstName(driver.getFirstName());
					detail2.setVacationAmount(0.0);
					// Bereavement change - driver
					detail2.setBereavementAmount(sequenceAmount.get(i));
					detail2.setPersonalSickAmount(0.0);
					detail2.setMiscAmount(0.0);
					detail2.setReimburseAmount(0.0);
					detail2.setTransportDriverAmount(0.0);
					detail2.setBonusAmount(0.0);
					detail2.setHolidayAmount(0.0);
					
					summary.add(detail2);
				}
			}
		}	
	}
	
	// Bereavement change - driver
	private Driver retrieveDriver(DriverPay driverPay) {
		Map criterias=new HashMap();
		criterias.put("status",1);
		criterias.put("fullName", driverPay.getDrivername());
		criterias.put("company.id", driverPay.getCompany().getId());
		return genericDAO.getByCriteria(Driver.class, criterias);
	}
	
	// Bereavement change - driver
	private Terminal retrieveTerminal(Driver driver) {
		Map criterias=new HashMap();
		criterias.clear();
		criterias.put("terminal.id", driver.getTerminal().getId());
		criterias.put("company.id", driver.getCompany().getId());
		return genericDAO.getByCriteria(Terminal.class, criterias);
	}

	@Override
	public List<PayChexDetail> generatePaychexData(SearchCriteria criteria) {
		// TODO Auto-generated method stub
		String companyid=(String)criteria.getSearchMap().get("company");
		String terminalid=(String)criteria.getSearchMap().get("terminal");
		String payrollDate=(String)criteria.getSearchMap().get("payrollDate");
		String actualpayrollDate=(String)criteria.getSearchMap().get("payrollDate");
		payrollDate=ReportDateUtil.getFromDate(payrollDate);
		System.out.println("Date format for paychex "+payrollDate);
		Location company=null;
		Location terminal=null;
		if(!StringUtils.isEmpty(companyid)){
			company=genericDAO.getById(Location.class,Long.parseLong(companyid));
			
		}
		if(!StringUtils.isEmpty(terminalid)){
			terminal=genericDAO.getById(Location.class, Long.parseLong(terminalid));
		}
		StringBuffer driverquery=new StringBuffer("");
		StringBuffer timequery=new StringBuffer("");
		StringBuffer weeklyquery=new StringBuffer("");
		weeklyquery.append("select obj from WeeklyPayDetail obj where 1=1");
		driverquery.append("select obj from DriverPay obj where 1=1");
		timequery.append("select obj from HourlyPayrollInvoiceDetails obj where 1=1");
		if(!StringUtils.isEmpty(payrollDate)){
			driverquery.append(" and obj.payRollBatch='"+payrollDate+"'");
			timequery.append(" and obj.date='"+payrollDate+"'");
			weeklyquery.append(" and obj.checkDate='"+payrollDate+"'");			
		}
		if(!StringUtils.isEmpty(companyid)){
			driverquery.append(" and obj.company="+companyid);
			timequery.append(" and obj.company='"+company.getName()+"'");
			weeklyquery.append(" and obj.company="+companyid);
		}
		if(!StringUtils.isEmpty(terminalid)){
			driverquery.append(" and obj.terminal="+terminalid);
			timequery.append(" and obj.terminal='"+terminal.getName()+"'");
			weeklyquery.append(" and obj.terminal="+terminalid);
		}
		
		List<DriverPay> driverPays=genericDAO.executeSimpleQuery(driverquery.toString());
		List<WeeklyPayDetail> payDetails=genericDAO.executeSimpleQuery(weeklyquery.toString());
		List<HourlyPayrollInvoiceDetails> hourlyPayrollInvoiceDetails=genericDAO.executeSimpleQuery(timequery.toString());
		
		// 17th Jan 2016 - Split paychex
		determineDriverMultipleBatch(driverPays);
		
		List<PayChexDetail> summary= new ArrayList<PayChexDetail>();
		Map criterias=new HashMap();
		for(WeeklyPayDetail payDetail:payDetails){
			PayChexDetail detail=new PayChexDetail();
			criterias.clear();
		if((payDetail.getVacationAmount()==0.0 || payDetail.getVacationAmount()==null)
				// Bereavement change - salary
				&& (payDetail.getBereavementAmount() == null || payDetail.getBereavementAmount() == 0.0))
		{
			criterias.put("status",1);
			criterias.put("fullName",payDetail.getDriver());
			criterias.put("company.id", payDetail.getCompany().getId());
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				criterias.clear();
				criterias.put("terminal.id",employee.getTerminal().getId());
				criterias.put("company.id", employee.getCompany().getId());
				Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
						detail.setHomeBr(empterminal.getHomeBranch().toString());
				detail.setHomeDpt(employee.getCatagory().getCode());
				detail.setEeNo(employee.getStaffId());
				detail.setLastName(employee.getLastName());
				detail.setFirstName(employee.getFirstName());
				detail.setVacationAmount(payDetail.getVacationAmount());
				detail.setSalary(payDetail.getAmount());
				detail.setMiscAmount(payDetail.getMiscAmount());
				detail.setReimburseAmount(payDetail.getReimburseAmount());
				detail.setBonusAmount(payDetail.getBonusAmount());
				detail.setHolidayAmount(payDetail.getHolidayAmount());
				detail.setPersonalSickAmount(payDetail.getSickPersonalAmount());
				detail.setTransportDriverAmount(0.0);
				// Bereavement change - salary
				detail.setBereavementAmount(0.0);
				summary.add(detail);
			}else{
				continue;
			}
			
		}
		else if((payDetail.getVacationAmount()!=0.0 && payDetail.getVacationAmount()!=null)
					// Bereavement change - salary
					|| (payDetail.getBereavementAmount() != null && payDetail.getBereavementAmount() != 0.0)){
			criterias.put("status",1);
			criterias.put("fullName",payDetail.getDriver());
			criterias.put("company.id", payDetail.getCompany().getId());
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				criterias.clear();
				criterias.put("terminal.id",employee.getTerminal().getId());
				criterias.put("company.id", employee.getCompany().getId());
				Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
						detail.setHomeBr(empterminal.getHomeBranch().toString());
				detail.setHomeDpt(employee.getCatagory().getCode());
				detail.setEeNo(employee.getStaffId());
				detail.setLastName(employee.getLastName());
				detail.setFirstName(employee.getFirstName());
				detail.setVacationAmount(0.0);
				detail.setSalary(payDetail.getAmount());
				detail.setMiscAmount(payDetail.getMiscAmount());
				detail.setReimburseAmount(payDetail.getReimburseAmount());
				detail.setBonusAmount(payDetail.getBonusAmount());
				detail.setHolidayAmount(payDetail.getHolidayAmount());
				detail.setPersonalSickAmount(payDetail.getSickPersonalAmount());
				detail.setTransportDriverAmount(0.0);
				// Bereavement change - salary
				detail.setBereavementAmount(0.0);
				summary.add(detail);
				
				
				//************** Newly Added
				
				///// ***************** PTOD related
					
				StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category!=2");
				
				ptodquery.append(" and obj.payRollBatch='"+mysqldf.format(payDetail.getCheckDate())+"'");
				
				System.out.println("******* ptod query paychex "+ptodquery.toString());
				    
				List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
				
				for(Ptodapplication ptodapplication:ptodapplications){
					boolean noSequenceNumber=true;
					if(ptodapplication.getLeavetype().getId()==1){
						// do nothing
					}
					// Jury duty fix - salary - 3rd Nov 2016
					if(ptodapplication.getLeavetype().getId()==9){
						// do nothing
					}
					if(ptodapplication.getLeavetype().getId()==4 ||
							// Bereavement change - salary
							ptodapplication.getLeavetype().getId() == 8) {
						List<Integer> sequenceNumber = new ArrayList<Integer>();
						List<Double>  sequenceAmount = new ArrayList<Double>();
						
						sequenceNumber.add(ptodapplication.getSequenceNum1());
						sequenceNumber.add(ptodapplication.getSequenceNum2());
						sequenceNumber.add(ptodapplication.getSequenceNum3());
						sequenceNumber.add(ptodapplication.getSequenceNum4());
						
						sequenceAmount.add(ptodapplication.getSequenceAmt1());
						sequenceAmount.add(ptodapplication.getSequenceAmt2());
						sequenceAmount.add(ptodapplication.getSequenceAmt3());
						sequenceAmount.add(ptodapplication.getSequenceAmt4());
						
						sequenceNumber.add(ptodapplication.getSequenceNum5());
						sequenceNumber.add(ptodapplication.getSequenceNum6());
						sequenceNumber.add(ptodapplication.getSequenceNum7());
						sequenceNumber.add(ptodapplication.getSequenceNum8());
						sequenceAmount.add(ptodapplication.getSequenceAmt5());
						sequenceAmount.add(ptodapplication.getSequenceAmt6());
						sequenceAmount.add(ptodapplication.getSequenceAmt7());
						sequenceAmount.add(ptodapplication.getSequenceAmt8());
						
						for(int i=0;i<8;i++){
							if(sequenceNumber.get(i)!=0 && sequenceAmount.get(i)!=0.0){	
								noSequenceNumber = false;
								PayChexDetail detail2 =new PayChexDetail();
								if(empterminal!=null)
									if(empterminal.getHomeBranch()!=null)
										detail2.setHomeBr(empterminal.getHomeBranch().toString());
								detail2.setSeqNo(sequenceNumber.get(i));
								detail2.setHomeDpt(employee.getCatagory().getCode());
								detail2.setEeNo(employee.getStaffId());
								detail2.setLastName(employee.getLastName());
								detail2.setFirstName(employee.getFirstName());
								// Bereavement change - salary
								// Bereavement vacation fix - 7th Aug 2017
								//if (sequenceNumber.get(i) == 7) {
								if (ptodapplication.getLeavetype().getId() == 8) {
									detail2.setBereavementAmount(sequenceAmount.get(i));
									detail2.setVacationAmount(0.0);
								} else {
									detail2.setVacationAmount(sequenceAmount.get(i));
									detail2.setBereavementAmount(0.0);
								}
								
								//detail2.setSalary(payDetail.getAmount());
								detail2.setMiscAmount(0.0);
								detail2.setReimburseAmount(0.0);
								detail2.setBonusAmount(0.0);
								detail2.setHolidayAmount(0.0);
								detail2.setPersonalSickAmount(0.0);
								detail2.setTransportDriverAmount(0.0);
								summary.add(detail2);
							}
							
						}
						if(noSequenceNumber) {
							// Bereavement change - salary
							if(payDetail.getBereavementAmount() != null && payDetail.getBereavementAmount() != 0.0) {
								detail.setBereavementAmount(payDetail.getBereavementAmount());
							} else {
								detail.setVacationAmount(detail.getVacationAmount()+(ptodapplication.getAmountpaid()+ptodapplication.getHourlyamountpaid()));
							}
						}
					}
					
				}
				
				
			}else{
				continue;
			}
			
			
		}
		}
		
		
   List<DriverPayroll> driverPayrollObjlist = null;
   DriverPayroll driverPayrollObj = null;
   
 for(DriverPay driverPay:driverPays){
	 
	 Double diffMiscAmt = 0.0;
	 
	 if(driverPayrollObjlist==null){	    
		 driverPayrollObjlist =  genericDAO.executeSimpleQuery("select obj from DriverPayroll obj where obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");
		 for(DriverPayroll dpObj:driverPayrollObjlist){
		  driverPayrollObj = dpObj;
		 }
	 }
	 
	 // Bereavement change - driver
	 addBereavementPayForDriver(driverPay, summary);
	 
	 if(driverPay.getMiscAmount()!=0.0 && driverPay.getMiscAmount()!=null){	
		 
		 criterias.clear();
		 criterias.put("status",1);
		 criterias.put("fullName", driverPay.getDrivername());
		 criterias.put("company.id", driverPay.getCompany().getId());
		 Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
		 if(employee!=null){
			 
			StringBuffer miscquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType!='Quarter Bonus' and obj.miscNotes!='Reimbursement'");
			miscquery.append(" and obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");				
				
			
			
			List<MiscellaneousAmount> qutarbonuses=genericDAO.executeSimpleQuery(miscquery.toString()); 			 
			
			for(MiscellaneousAmount miscAmount:qutarbonuses){
				if(!StringUtils.isEmpty(miscAmount.getSequenceNumber())){
				
					PayChexDetail detail1 =new PayChexDetail();			
					criterias.clear();
					criterias.put("terminal.id",employee.getTerminal().getId());
					criterias.put("company.id", employee.getCompany().getId());
					Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
					if(empterminal!=null)
						if(empterminal.getHomeBranch()!=null)
							detail1.setHomeBr(empterminal.getHomeBranch().toString());
			
					detail1.setHomeDpt(employee.getCatagory().getCode());
					detail1.setEeNo(employee.getStaffId());
					detail1.setLastName(employee.getLastName());
					detail1.setFirstName(employee.getFirstName());			
				
					if(!StringUtils.isEmpty(miscAmount.getSequenceNumber()))
				       detail1.setSeqNo(Integer.parseInt(miscAmount.getSequenceNumber()));									
				
					detail1.setVacationAmount(0.0);
					// Bereavement change - driver
					detail1.setBereavementAmount(0.0);
					detail1.setPersonalSickAmount(0.0);
					detail1.setMiscAmount(miscAmount.getMisamount());
					detail1.setReimburseAmount(0.0);
					if(driverPay.getTransportationAmount()!=null){
						if(driverPay.getProbationDeductionAmount()!=null)
							detail1.setTransportDriverAmount(0.0);
						else
							detail1.setTransportDriverAmount(0.0);
					}else
						detail1.setTransportDriverAmount(0.0);
					detail1.setBonusAmount(0.0);
					detail1.setHolidayAmount(0.0);	
					summary.add(detail1);	
					diffMiscAmt = diffMiscAmt+miscAmount.getMisamount();					
					
				} 
			}
		 }else{			 
				continue;
		  }
		
	 }
	 
	 
		 if(driverPay.getQuatarAmount()==0.0 && driverPay.getVacationAmount()==0.0){
			PayChexDetail detail =new PayChexDetail();
			criterias.clear();
			criterias.put("status",1);
			criterias.put("fullName", driverPay.getDrivername());
			criterias.put("company.id", driverPay.getCompany().getId());
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				criterias.clear();
				criterias.put("terminal.id",employee.getTerminal().getId());
				criterias.put("company.id", employee.getCompany().getId());
				Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
						detail.setHomeBr(empterminal.getHomeBranch().toString());
				detail.setHomeDpt(employee.getCatagory().getCode());
				detail.setEeNo(employee.getStaffId());
				detail.setLastName(employee.getLastName());
				detail.setFirstName(employee.getFirstName());
				detail.setVacationAmount(0.0);
				// Bereavement change - driver
				detail.setBereavementAmount(0.0);
				detail.setPersonalSickAmount(driverPay.getSickPersonalAmount());				
				detail.setMiscAmount(driverPay.getMiscAmount()-diffMiscAmt);
				detail.setReimburseAmount(driverPay.getReimburseAmount());
				
				// 17th Jan 2016 - Split paychex
				if (StringUtils.isNotEmpty(driverPay.getForceNewPaychexSeqNum())) {
					detail.setSeqNo(Integer.parseInt(driverPay.getForceNewPaychexSeqNum()));
				}
				
				if(driverPay.getTransportationAmount()!=null){
					if(driverPay.getProbationDeductionAmount()!=null)
						detail.setTransportDriverAmount(driverPay.getTransportationAmount()-driverPay.getProbationDeductionAmount());
					else
						detail.setTransportDriverAmount(driverPay.getTransportationAmount());
				}else
					detail.setTransportDriverAmount(0.0);
				detail.setBonusAmount(driverPay.getBonusAmount());
				detail.setHolidayAmount(driverPay.getHolidayAmount());
				
			}else{
				continue;
			}
			summary.add(detail);
	}
	else {			
			
	       if(driverPay.getQuatarAmount()!=0.0 && driverPay.getVacationAmount()==0.0){
			
			PayChexDetail detail =new PayChexDetail();
			PayChexDetail detail1 =new PayChexDetail();
			criterias.clear();
			criterias.put("status",1);
			criterias.put("fullName", driverPay.getDrivername());
			criterias.put("company.id", driverPay.getCompany().getId());
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				criterias.clear();
				criterias.put("terminal.id",employee.getTerminal().getId());
				criterias.put("company.id", employee.getCompany().getId());
				Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
						detail.setHomeBr(empterminal.getHomeBranch().toString());
				detail.setHomeDpt(employee.getCatagory().getCode());
				detail.setEeNo(employee.getStaffId());
				detail.setLastName(employee.getLastName());
				detail.setFirstName(employee.getFirstName());
				detail.setVacationAmount(0.0);
				// Bereavement change - driver
				detail.setBereavementAmount(0.0);
				detail.setPersonalSickAmount(driverPay.getSickPersonalAmount());
				detail.setMiscAmount(driverPay.getMiscAmount()-diffMiscAmt);
				detail.setReimburseAmount(driverPay.getReimburseAmount());
				
				// 17th Jan 2016 - Split paychex
				if (StringUtils.isNotEmpty(driverPay.getForceNewPaychexSeqNum())) {
					detail.setSeqNo(Integer.parseInt(driverPay.getForceNewPaychexSeqNum()));
				}
				
				if(driverPay.getTransportationAmount()!=null){
					if(driverPay.getProbationDeductionAmount()!=null)
						detail.setTransportDriverAmount(driverPay.getTransportationAmount()-driverPay.getProbationDeductionAmount());
					else
						detail.setTransportDriverAmount(driverPay.getTransportationAmount());
				}else
					detail.setTransportDriverAmount(0.0);
				detail.setBonusAmount(driverPay.getBonusAmount());
				detail.setHolidayAmount(driverPay.getHolidayAmount());
				
				
				if(detail.getTransportDriverAmount()==0.0 && detail.getPersonalSickAmount()==0.0 && detail.getReimburseAmount()==0.0 && detail.getHolidayAmount()==0.0 && detail.getBonusAmount()==0.0 && detail.getMiscAmount()==0.0 ){
					// do nothing	
				}
				else{
					summary.add(detail);
				}
				
				
				//************** Newly Added				
				
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
					   detail1.setHomeBr(empterminal.getHomeBranch().toString());
				
					detail1.setHomeDpt(employee.getCatagory().getCode());
					detail1.setEeNo(employee.getStaffId());
					detail1.setLastName(employee.getLastName());
					detail1.setFirstName(employee.getFirstName());
					
					StringBuffer qutarBonusquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType='Quarter Bonus'");
					qutarBonusquery.append(" and obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");
					
					
					List<MiscellaneousAmount> qutarbonuses=genericDAO.executeSimpleQuery(qutarBonusquery.toString());
					
					for(MiscellaneousAmount miscAmount:qutarbonuses){
						if(!StringUtils.isEmpty(miscAmount.getSequenceNumber()))
					       detail1.setSeqNo(Integer.parseInt(miscAmount.getSequenceNumber()));									
					}
					//
					detail1.setVacationAmount(0.0);
					// Bereavement change - driver
					detail1.setBereavementAmount(0.0);
					detail1.setPersonalSickAmount(0.0);
					detail1.setMiscAmount(0.0);
					detail1.setReimburseAmount(0.0);
					if(driverPay.getTransportationAmount()!=null){
						if(driverPay.getProbationDeductionAmount()!=null)
							detail1.setTransportDriverAmount(0.0);
						else
							detail1.setTransportDriverAmount(0.0);
					}else
						detail1.setTransportDriverAmount(0.0);
					detail1.setBonusAmount(driverPay.getQuatarAmount());
					detail1.setHolidayAmount(0.0);				
					summary.add(detail1);			
				//************************************
			}else{
				continue;
			}					
		}
	    else if(driverPay.getQuatarAmount()==0.0 && driverPay.getVacationAmount()!=0.0){
			
			PayChexDetail detail =new PayChexDetail();
			PayChexDetail detail1 =new PayChexDetail();
			boolean addedToList = true;
			criterias.clear();
			criterias.put("status",1);
			criterias.put("fullName", driverPay.getDrivername());
			criterias.put("company.id", driverPay.getCompany().getId());
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				criterias.clear();
				criterias.put("terminal.id",employee.getTerminal().getId());
				criterias.put("company.id", employee.getCompany().getId());
				Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
						detail.setHomeBr(empterminal.getHomeBranch().toString());
				detail.setHomeDpt(employee.getCatagory().getCode());
				detail.setEeNo(employee.getStaffId());
				detail.setLastName(employee.getLastName());
				detail.setFirstName(employee.getFirstName());
				detail.setVacationAmount(0.0);
				// Bereavement change - driver
				detail.setBereavementAmount(0.0);
				detail.setPersonalSickAmount(driverPay.getSickPersonalAmount());
				detail.setMiscAmount(driverPay.getMiscAmount()-diffMiscAmt);
				detail.setReimburseAmount(driverPay.getReimburseAmount());
				
				// 17th Jan 2016 - Split paychex
				if (StringUtils.isNotEmpty(driverPay.getForceNewPaychexSeqNum())) {
					detail.setSeqNo(Integer.parseInt(driverPay.getForceNewPaychexSeqNum()));
				}
				
				if(driverPay.getTransportationAmount()!=null){
					if(driverPay.getProbationDeductionAmount()!=null)
						detail.setTransportDriverAmount(driverPay.getTransportationAmount()-driverPay.getProbationDeductionAmount());
					else
						detail.setTransportDriverAmount(driverPay.getTransportationAmount());
				}else
					detail.setTransportDriverAmount(0.0);
				detail.setBonusAmount(driverPay.getBonusAmount());
				detail.setHolidayAmount(driverPay.getHolidayAmount());
				
				
				if(detail.getTransportDriverAmount()==0.0 && detail.getPersonalSickAmount()==0.0 && detail.getReimburseAmount()==0.0 && detail.getHolidayAmount()==0.0 && detail.getBonusAmount()==0.0 && detail.getMiscAmount()==0.0 ){
					// do nothing	
					addedToList = false;
				}
				else{
					summary.add(detail);
				}
				
				
				//************** Newly Added
				
				///// ***************** PTOD related
					
				StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"'");
				ptodquery.append(" and obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");
				/*if(driverPayrollObj!=null){
				    ptodquery.append(" and obj.batchdate>='"+mysqldf.format(driverPayrollObj.getBillBatchFrom())+"'");
				}
				if(driverPayrollObj!=null){
					ptodquery.append(" and obj.batchdate<='"+mysqldf.format(driverPayrollObj.getBillBatchTo())+"'");
				}*/
				List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
				int count=9;
				for(Ptodapplication ptodapplication:ptodapplications){
					boolean noSequenceNumber=true;
					if(ptodapplication.getLeavetype().getId()==1){
						// do nothing
					}
					if(ptodapplication.getLeavetype().getId()==4){
						List<Integer> sequenceNumber = new ArrayList<Integer>();
						List<Double>  sequenceAmount = new ArrayList<Double>();
						
						sequenceNumber.add(ptodapplication.getSequenceNum1());
						sequenceNumber.add(ptodapplication.getSequenceNum2());
						sequenceNumber.add(ptodapplication.getSequenceNum3());
						sequenceNumber.add(ptodapplication.getSequenceNum4());
						
						sequenceAmount.add(ptodapplication.getSequenceAmt1());
						sequenceAmount.add(ptodapplication.getSequenceAmt2());
						sequenceAmount.add(ptodapplication.getSequenceAmt3());
						sequenceAmount.add(ptodapplication.getSequenceAmt4());
						
						sequenceNumber.add(ptodapplication.getSequenceNum5());
						sequenceNumber.add(ptodapplication.getSequenceNum6());
						sequenceNumber.add(ptodapplication.getSequenceNum7());
						sequenceNumber.add(ptodapplication.getSequenceNum8());
						sequenceAmount.add(ptodapplication.getSequenceAmt5());
						sequenceAmount.add(ptodapplication.getSequenceAmt6());
						sequenceAmount.add(ptodapplication.getSequenceAmt7());
						sequenceAmount.add(ptodapplication.getSequenceAmt8());
						
					for(int i=0;i<8;i++){
							
						if(sequenceNumber.get(i)!=0 && sequenceAmount.get(i)!=0.0){	
							noSequenceNumber = false;
							PayChexDetail detail2 =new PayChexDetail();
							if(empterminal!=null)
								if(empterminal.getHomeBranch()!=null)
									detail2.setHomeBr(empterminal.getHomeBranch().toString());
						
							//detail2.setSeqNo(count);
							// count--;
							detail2.setSeqNo(sequenceNumber.get(i));
							detail2.setHomeDpt(employee.getCatagory().getCode());
							detail2.setEeNo(employee.getStaffId());
							detail2.setLastName(employee.getLastName());
							detail2.setFirstName(employee.getFirstName());
							//detail2.setVacationAmount(ptodapplication.getAmountpaid());
							detail2.setVacationAmount(sequenceAmount.get(i));
							// Bereavement change - driver
							detail2.setBereavementAmount(0.0);
							detail2.setPersonalSickAmount(0.0);
							detail2.setMiscAmount(0.0);
							detail2.setReimburseAmount(0.0);
							detail2.setTransportDriverAmount(0.0);
							detail2.setBonusAmount(0.0);
							detail2.setHolidayAmount(0.0);
							summary.add(detail2);
						}
					}
					if(noSequenceNumber){
						detail.setVacationAmount(detail.getVacationAmount()+(ptodapplication.getAmountpaid()+ptodapplication.getHourlyamountpaid()));
						if(!addedToList){
							summary.add(detail);
							addedToList = true;
						}						
					}
				}
			}
				
				//*********************************************
				
							
				//************************************
			}else{
				continue;
			}					
		}	    
	    
       
        else if(driverPay.getQuatarAmount()!=0.0 && driverPay.getVacationAmount()!=0.0){

			
        	boolean addedToList=true;
			PayChexDetail detail =new PayChexDetail();
			PayChexDetail detail1 =new PayChexDetail();
			PayChexDetail detail3 =new PayChexDetail();
			criterias.clear();
			criterias.put("status",1);
			criterias.put("fullName", driverPay.getDrivername());
			criterias.put("company.id", driverPay.getCompany().getId());
			Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
			if(employee!=null){
				criterias.clear();
				criterias.put("terminal.id",employee.getTerminal().getId());
				criterias.put("company.id", employee.getCompany().getId());
				Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
						detail.setHomeBr(empterminal.getHomeBranch().toString());
				detail.setHomeDpt(employee.getCatagory().getCode());
				detail.setEeNo(employee.getStaffId());
				detail.setLastName(employee.getLastName());
				detail.setFirstName(employee.getFirstName());
				detail.setVacationAmount(0.0);
				// Bereavement change - driver
				detail.setBereavementAmount(0.0);
				detail.setPersonalSickAmount(driverPay.getSickPersonalAmount());
				detail.setMiscAmount(driverPay.getMiscAmount()-diffMiscAmt);
				detail.setReimburseAmount(driverPay.getReimburseAmount());
				
				// 17th Jan 2016 - Split paychex
				if (StringUtils.isNotEmpty(driverPay.getForceNewPaychexSeqNum())) {
					detail.setSeqNo(Integer.parseInt(driverPay.getForceNewPaychexSeqNum()));
				}
				
				if(driverPay.getTransportationAmount()!=null){
					if(driverPay.getProbationDeductionAmount()!=null)
						detail.setTransportDriverAmount(driverPay.getTransportationAmount()-driverPay.getProbationDeductionAmount());
					else
						detail.setTransportDriverAmount(driverPay.getTransportationAmount());
				}else
					detail.setTransportDriverAmount(0.0);
				detail.setBonusAmount(driverPay.getBonusAmount());
				detail.setHolidayAmount(driverPay.getHolidayAmount());
				
				
				if(detail.getTransportDriverAmount()==0.0 && detail.getPersonalSickAmount()==0.0 && detail.getReimburseAmount()==0.0 && detail.getHolidayAmount()==0.0 && detail.getBonusAmount()==0.0 && detail.getMiscAmount()==0.0 ){
					// do nothing	
					addedToList = false;
				}
				else{
					summary.add(detail);
				}
				
				
				//************** Newly Added
				
				///// ***************** PTOD related
					
				StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"'");
				ptodquery.append(" and obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");
				/*if(driverPayrollObj!=null){
				    ptodquery.append(" and obj.batchdate>='"+mysqldf.format(driverPayrollObj.getBillBatchFrom())+"'");
				}
				if(driverPayrollObj!=null){
					ptodquery.append(" and obj.batchdate<='"+mysqldf.format(driverPayrollObj.getBillBatchTo())+"'");
				}*/
				List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
				int count=9;
				for(Ptodapplication ptodapplication:ptodapplications){
					boolean noSequenceNumber = true;
					if(ptodapplication.getLeavetype().getId()==1){
						// do nothing
					}
					if(ptodapplication.getLeavetype().getId()==4){
						List<Integer> sequenceNumber = new ArrayList<Integer>();
						List<Double>  sequenceAmount = new ArrayList<Double>();
						
						sequenceNumber.add(ptodapplication.getSequenceNum1());
						sequenceNumber.add(ptodapplication.getSequenceNum2());
						sequenceNumber.add(ptodapplication.getSequenceNum3());
						sequenceNumber.add(ptodapplication.getSequenceNum4());
						
						sequenceAmount.add(ptodapplication.getSequenceAmt1());
						sequenceAmount.add(ptodapplication.getSequenceAmt2());
						sequenceAmount.add(ptodapplication.getSequenceAmt3());
						sequenceAmount.add(ptodapplication.getSequenceAmt4());
						
						sequenceNumber.add(ptodapplication.getSequenceNum5());
						sequenceNumber.add(ptodapplication.getSequenceNum6());
						sequenceNumber.add(ptodapplication.getSequenceNum7());
						sequenceNumber.add(ptodapplication.getSequenceNum8());
						sequenceAmount.add(ptodapplication.getSequenceAmt5());
						sequenceAmount.add(ptodapplication.getSequenceAmt6());
						sequenceAmount.add(ptodapplication.getSequenceAmt7());
						sequenceAmount.add(ptodapplication.getSequenceAmt8());
						
					for(int i=0;i<8;i++){
							
						if(sequenceNumber.get(i)!=0 && sequenceAmount.get(i)!=0.0){	
							noSequenceNumber = false;
							PayChexDetail detail2 =new PayChexDetail();
							if(empterminal!=null)
								if(empterminal.getHomeBranch()!=null)
									detail2.setHomeBr(empterminal.getHomeBranch().toString());
						
							//detail2.setSeqNo(count);
							// count--;
							detail2.setSeqNo(sequenceNumber.get(i));
							detail2.setHomeDpt(employee.getCatagory().getCode());
							detail2.setEeNo(employee.getStaffId());
							detail2.setLastName(employee.getLastName());
							detail2.setFirstName(employee.getFirstName());
							//detail2.setVacationAmount(ptodapplication.getAmountpaid());
							detail2.setVacationAmount(sequenceAmount.get(i));
							// Bereavement change - driver
							detail2.setBereavementAmount(0.0);
							detail2.setPersonalSickAmount(0.0);
							detail2.setMiscAmount(0.0);
							detail2.setReimburseAmount(0.0);
							detail2.setTransportDriverAmount(0.0);
							detail2.setBonusAmount(0.0);
							detail2.setHolidayAmount(0.0);
							summary.add(detail2);
						}
					}
					if(noSequenceNumber){
						detail.setVacationAmount(detail.getVacationAmount()+(ptodapplication.getAmountpaid()+ptodapplication.getHourlyamountpaid()));
						if(!addedToList){
							summary.add(detail);
							addedToList = true;
						}						
					}
					
					
					}
				}	
			
				
				//*********************************************				
				
				if(empterminal!=null)
					if(empterminal.getHomeBranch()!=null)
					   detail1.setHomeBr(empterminal.getHomeBranch().toString());
				
					detail1.setHomeDpt(employee.getCatagory().getCode());
					detail1.setEeNo(employee.getStaffId());
					detail1.setLastName(employee.getLastName());
					detail1.setFirstName(employee.getFirstName());
					
					StringBuffer qutarBonusquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus=2 and obj.driver.fullName='"+employee.getFullName()+"' and obj.miscType='Quarter Bonus'");
					qutarBonusquery.append(" and obj.payRollBatch='"+mysqldf.format(driverPay.getPayRollBatch())+"'");
					
					
					List<MiscellaneousAmount> qutarbonuses=genericDAO.executeSimpleQuery(qutarBonusquery.toString());
					
					for(MiscellaneousAmount miscAmount:qutarbonuses){
						if(!StringUtils.isEmpty(miscAmount.getSequenceNumber()))
					       detail1.setSeqNo(Integer.parseInt(miscAmount.getSequenceNumber()));									
					}
					//
					detail1.setVacationAmount(0.0);
					// Bereavement change - driver
					detail1.setBereavementAmount(0.0);
					detail1.setPersonalSickAmount(0.0);
					detail1.setMiscAmount(0.0);
					detail1.setReimburseAmount(0.0);
					if(driverPay.getTransportationAmount()!=null){
						if(driverPay.getProbationDeductionAmount()!=null)
							detail1.setTransportDriverAmount(0.0);
						else
							detail1.setTransportDriverAmount(0.0);
					}else
						detail1.setTransportDriverAmount(0.0);
					detail1.setBonusAmount(driverPay.getQuatarAmount());
					detail1.setHolidayAmount(0.0);				
					summary.add(detail1);
							
				//************************************
			}else{
				continue;
			}					
		
	    }
	}	
 }
		
		
		
		for(HourlyPayrollInvoiceDetails invoiceDetails:hourlyPayrollInvoiceDetails){			
			criterias.clear();		
			if(invoiceDetails.getDriver()!=null){
				if((invoiceDetails.getVacationAmount()==0.0 || invoiceDetails.getVacationAmount()==null)
						// Bereavement change
						&& (invoiceDetails.getBereavementAmount() == null || invoiceDetails.getBereavementAmount() == 0.0)) {
									
					criterias.put("status",1);
					criterias.put("fullName", invoiceDetails.getDriver());
					criterias.put("company.name", invoiceDetails.getCompany());
					Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
					if(employee!=null){		
						PayChexDetail detail=new PayChexDetail();
						criterias.clear();
						criterias.put("terminal.id",employee.getTerminal().getId());
						criterias.put("company.id", employee.getCompany().getId());
						Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
						if(empterminal!=null)
							if(empterminal.getHomeBranch()!=null)
								detail.setHomeBr(empterminal.getHomeBranch().toString());
						detail.setHomeDpt(employee.getCatagory().getCode());
						detail.setEeNo(employee.getStaffId());
						detail.setLastName(employee.getLastName());
						detail.setFirstName(employee.getFirstName());
						detail.setVacationAmount(0.0);
						detail.setPersonalSickAmount(invoiceDetails.getSickParsanolAmount());
						detail.setBonusAmount(invoiceDetails.getBonusAmounts());
						detail.setHolidayAmount(0.0);
						detail.setMiscAmount(invoiceDetails.getMiscAmount());
						detail.setReimburseAmount(invoiceDetails.getReimburseAmount());
						detail.setOverTimeHour(invoiceDetails.getOthours());				
						detail.setRegularHour(invoiceDetails.getRegularhours());
						detail.setHolidayHour(invoiceDetails.getHolidayhours());
						detail.setRate(invoiceDetails.getRegularrate());
						detail.setOvRate(invoiceDetails.getOtrate());
						detail.setTransportDriverAmount(0.0);
						// Bereavement change
						detail.setBereavementAmount(0.0);
						summary.add(detail);
					}else{
						continue;
					}
					
				}
				else if((invoiceDetails.getVacationAmount()!=0.0 && invoiceDetails.getVacationAmount()!=null)
						// Bereavement change
						|| (invoiceDetails.getBereavementAmount() != null && invoiceDetails.getBereavementAmount() != 0.0)) {									
					criterias.put("status",1);
					criterias.put("fullName", invoiceDetails.getDriver());
					criterias.put("company.name", invoiceDetails.getCompany());
					Driver employee=genericDAO.getByCriteria(Driver.class, criterias);
					if(employee!=null){
						PayChexDetail detail=new PayChexDetail();
						criterias.clear();
						criterias.put("terminal.id",employee.getTerminal().getId());
						criterias.put("company.id", employee.getCompany().getId());
						Terminal empterminal=genericDAO.getByCriteria(Terminal.class, criterias);
						if(empterminal!=null)
							if(empterminal.getHomeBranch()!=null)
								detail.setHomeBr(empterminal.getHomeBranch().toString());
						detail.setHomeDpt(employee.getCatagory().getCode());
						detail.setEeNo(employee.getStaffId());
						detail.setLastName(employee.getLastName());
						detail.setFirstName(employee.getFirstName());
						detail.setVacationAmount(0.0);
						detail.setPersonalSickAmount(invoiceDetails.getSickParsanolAmount());
						detail.setBonusAmount(invoiceDetails.getBonusAmounts());
						detail.setHolidayAmount(0.0);
						detail.setMiscAmount(invoiceDetails.getMiscAmount());
						detail.setReimburseAmount(invoiceDetails.getReimburseAmount());
						detail.setOverTimeHour(invoiceDetails.getOthours());				
						detail.setRegularHour(invoiceDetails.getRegularhours());
						detail.setHolidayHour(invoiceDetails.getHolidayhours());
						detail.setRate(invoiceDetails.getRegularrate());
						detail.setOvRate(invoiceDetails.getOtrate());
						detail.setTransportDriverAmount(0.0);
						// Bereavement change
						detail.setBereavementAmount(0.0);
						summary.add(detail);
						
						//************** Newly Added
						
						///// ***************** PTOD related
							
						
						StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus=2 and obj.approvestatus=1 and obj.driver.fullName='"+employee.getFullName()+"' and obj.category!=2");
						
						ptodquery.append(" and obj.payRollBatch='"+mysqldf.format(invoiceDetails.getPayRollCheckDate())+"'");
						
						System.out.println("******* ptod query paychex "+ptodquery.toString());
						    
						List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
						
						for(Ptodapplication ptodapplication:ptodapplications){
							boolean noSequenceNumber = true;
							if(ptodapplication.getLeavetype().getId()==1){
								// do nothing
							}
							if(ptodapplication.getLeavetype().getId()==4 ||
									// Bereavement change
									ptodapplication.getLeavetype().getId()==8){
								List<Integer> sequenceNumber = new ArrayList<Integer>();
								List<Double>  sequenceAmount = new ArrayList<Double>();
								
								sequenceNumber.add(ptodapplication.getSequenceNum1());
								sequenceNumber.add(ptodapplication.getSequenceNum2());
								sequenceNumber.add(ptodapplication.getSequenceNum3());
								sequenceNumber.add(ptodapplication.getSequenceNum4());
								
								sequenceAmount.add(ptodapplication.getSequenceAmt1());
								sequenceAmount.add(ptodapplication.getSequenceAmt2());
								sequenceAmount.add(ptodapplication.getSequenceAmt3());
								sequenceAmount.add(ptodapplication.getSequenceAmt4());
								
								sequenceNumber.add(ptodapplication.getSequenceNum5());
								sequenceNumber.add(ptodapplication.getSequenceNum6());
								sequenceNumber.add(ptodapplication.getSequenceNum7());
								sequenceNumber.add(ptodapplication.getSequenceNum8());
								sequenceAmount.add(ptodapplication.getSequenceAmt5());
								sequenceAmount.add(ptodapplication.getSequenceAmt6());
								sequenceAmount.add(ptodapplication.getSequenceAmt7());
								sequenceAmount.add(ptodapplication.getSequenceAmt8());
								
								for(int i=0;i<8;i++){
									
									if(sequenceNumber.get(i)!=0 && sequenceAmount.get(i)!=0.0){	
										noSequenceNumber=false;
										PayChexDetail detail2 =new PayChexDetail();
										if(empterminal!=null)
											if(empterminal.getHomeBranch()!=null)
												detail2.setHomeBr(empterminal.getHomeBranch().toString());
										detail2.setSeqNo(sequenceNumber.get(i));
										detail2.setHomeDpt(employee.getCatagory().getCode());
										detail2.setEeNo(employee.getStaffId());
										detail2.setLastName(employee.getLastName());
										detail2.setFirstName(employee.getFirstName());
										detail2.setPersonalSickAmount(0.0);
										detail2.setBonusAmount(0.0);
										detail2.setHolidayAmount(0.0);
										detail2.setMiscAmount(0.0);
										detail2.setReimburseAmount(0.0);
										detail2.setOverTimeHour(0.0);				
										detail2.setRegularHour(0.0);
										detail2.setHolidayHour(0.0);
										detail2.setRate(0.0);
										detail2.setOvRate(0.0);
										detail2.setTransportDriverAmount(0.0);
										// Bereavement change
										// Bereavement vacation fix - 7th Aug 2017
										//if (sequenceNumber.get(i) == 7) {
										if (ptodapplication.getLeavetype().getId()==8) {
											detail2.setBereavementAmount(sequenceAmount.get(i));
											detail2.setVacationAmount(0.0);
										} else {
											detail2.setVacationAmount(sequenceAmount.get(i));
											detail2.setBereavementAmount(0.0);
										}
										summary.add(detail2);
									}									
								}
								if(noSequenceNumber){
									// Bereavement change
									if(invoiceDetails.getBereavementAmount() != null && invoiceDetails.getBereavementAmount() != 0.0) {
										detail.setBereavementAmount(invoiceDetails.getBereavementAmount());
									} else {
										detail.setVacationAmount(detail.getVacationAmount()+(ptodapplication.getAmountpaid()+ptodapplication.getHourlyamountpaid()));
									}
								}
							}
						}
					}else{
						continue;
					}				
				}
			}
			else{
				continue;
			}
			
		}
		
		// Bereavement change - fixing null
		for (PayChexDetail aPayChexDetail : summary) {
			if (aPayChexDetail.getBereavementAmount() == null) {
				aPayChexDetail.setBereavementAmount(0.0);
			}
		}
		
		Comparator<PayChexDetail> comparator=new Comparator<PayChexDetail>() {
			@Override
			public int compare(PayChexDetail o1, PayChexDetail o2) {
				return  o1.getLastName().compareTo(o2.getLastName());
			}
		};
		Comparator<PayChexDetail> comparator2=new Comparator<PayChexDetail>() {
			@Override
			public int compare(PayChexDetail o1, PayChexDetail o2) {
				return  o1.getFirstName().compareTo(o2.getFirstName());
			}
		};
		ComparatorChain chain = new ComparatorChain();  
		chain.addComparator(comparator);
		chain.addComparator(comparator2);
		Collections.sort(summary, chain); 
		return summary;
	}
	
	// 17th Jan 2016 - Split paychex
	private void determineDriverMultipleBatch(List<DriverPay> driverPayList) {
		if (driverPayList == null || driverPayList.isEmpty()) {
			return;
		}
		
		Map<String, DriverPay> groupedDriverPay = new HashMap<String, DriverPay>();
		for (DriverPay aDriverPay : driverPayList) {
			DriverPay existingDriverPay = groupedDriverPay.get(aDriverPay.getDrivername());
			if (existingDriverPay == null) {
				groupedDriverPay.put(aDriverPay.getDrivername(), aDriverPay);
			} else {
				if (aDriverPay.getBillBatchDateTo().before(existingDriverPay.getBillBatchDateTo())) {
					aDriverPay.setForceNewPaychexSeqNum("8");
				} else {
					existingDriverPay.setForceNewPaychexSeqNum("8");
				}
			}
		}
	}

	@Override
	public List<SalaryDetail> generateSalaryData(SearchCriteria criteria) {
		// TODO Auto-generated method stub
		
		String companyid=(String)criteria.getSearchMap().get("company");
		String terminalid=(String)criteria.getSearchMap().get("terminal");
		String payrollDateFrom=(String)criteria.getSearchMap().get("payrollDateFrom");
		String payrollDateTo=(String)criteria.getSearchMap().get("payrollDateTo");
		payrollDateTo=ReportDateUtil.getToDate(payrollDateTo);
		payrollDateFrom=ReportDateUtil.getFromDate(payrollDateFrom);
		Location company=null;
		Location terminal=null;
		if(!StringUtils.isEmpty(companyid)){
			company=genericDAO.getById(Location.class,Long.parseLong(companyid));
			
		}
		if(!StringUtils.isEmpty(terminalid)){
			terminal=genericDAO.getById(Location.class, Long.parseLong(terminalid));
		}
		StringBuffer driverquery=new StringBuffer("");
		StringBuffer timequery=new StringBuffer("");
		StringBuffer weeklyquery=new StringBuffer("");
		weeklyquery.append("select obj from WeeklyPayDetail obj where 1=1");
		driverquery.append("select obj from DriverPay obj where 1=1");
		timequery.append("select obj from HourlyPayrollInvoiceDetails obj where 1=1");
		if(!StringUtils.isEmpty(payrollDateFrom)){
			driverquery.append(" and obj.payRollBatch>='"+payrollDateFrom+"'");
			timequery.append(" and obj.date>='"+payrollDateFrom+"'");
			weeklyquery.append(" and obj.checkDate>='"+payrollDateFrom+"'");
			
		}
		if(!StringUtils.isEmpty(payrollDateTo)){
			driverquery.append(" and obj.payRollBatch<='"+payrollDateTo+"'");
			timequery.append(" and obj.date<='"+payrollDateTo+"'");
			weeklyquery.append(" and obj.checkDate<='"+payrollDateTo+"'");			
		}
		if(!StringUtils.isEmpty(companyid)){
			driverquery.append(" and obj.company="+companyid);
			timequery.append(" and obj.company='"+company.getName()+"'");
			weeklyquery.append(" and obj.company="+companyid);
		}
		if(!StringUtils.isEmpty(terminalid)){
			driverquery.append(" and obj.terminal="+terminalid);
			timequery.append(" and obj.terminal='"+terminal.getName()+"'");
			weeklyquery.append(" and obj.terminal="+terminalid);
		}
			
		List<DriverPay> driverPays=genericDAO.executeSimpleQuery(driverquery.toString());
		List<WeeklyPayDetail> payDetails=genericDAO.executeSimpleQuery(weeklyquery.toString());
		List<HourlyPayrollInvoiceDetails> hourlyPayrollInvoiceDetails=genericDAO.executeSimpleQuery(timequery.toString());
		List<SalaryDetail> summary= new ArrayList<SalaryDetail>();
		Map criterias=new HashMap();
		for(DriverPay pay:driverPays){
			SalaryDetail detail=new SalaryDetail();
			detail.setDriver(pay.getDrivername());
			detail.setCategory("Driver");
			detail.setCompanyname(pay.getCompanyname());
			detail.setAmount(pay.getTransportationAmount());
			// Bereavement change - driver
			detail.setBereavementAmount(pay.getBereavementAmount());
			detail.setVacationAmount(pay.getVacationAmount());
			detail.setSickPersonalAmount(pay.getSickPersonalAmount());
			detail.setBonusAmount(pay.getBonusAmount());
			detail.setHolidayAmount(pay.getHolidayAmount());
			detail.setQuarterAmount(pay.getQuatarAmount());		
			detail.setTotalAmount(pay.getTotalAmount());
			detail.setMiscAmount(pay.getMiscAmount());
			detail.setReimburseAmount(pay.getReimburseAmount());
			detail.setProbationDetection(pay.getProbationDeductionAmount());
			Map driverCriteria = new HashMap();
			driverCriteria.clear();
			driverCriteria.put("fullName",pay.getDrivername());
			driverCriteria.put("status",1);
			Driver drvObj = genericDAO.getByCriteria(Driver.class, driverCriteria);
			if(drvObj!=null)
				detail.setTerminalName(drvObj.getTerminal().getName());
			else{
				driverCriteria.clear();
				driverCriteria.put("status",1);
				driverCriteria.put("fullName",pay.getDrivername());
				Driver drvsObj = genericDAO.getByCriteria(Driver.class, driverCriteria);
				if(drvsObj!=null)
					detail.setTerminalName(drvsObj.getTerminal().getName());
				else
					detail.setTerminalName("");
			}
			summary.add(detail);
		}
		for(WeeklyPayDetail payDetail:payDetails){
			SalaryDetail detail=new SalaryDetail();
			detail.setDriver(payDetail.getDriver());
			detail.setCategory(payDetail.getCategory());
			detail.setCompanyname(payDetail.getCompanyname());
			detail.setAmount(payDetail.getAmount());
			detail.setVacationAmount(payDetail.getVacationAmount());
			detail.setSickPersonalAmount(payDetail.getSickPersonalAmount());
			detail.setBonusAmount(payDetail.getBonusAmount());
			detail.setHolidayAmount(payDetail.getHolidayAmount());
			detail.setTotalAmount(payDetail.getTotalAmount());
			detail.setMiscAmount(payDetail.getMiscAmount());
			detail.setReimburseAmount(payDetail.getReimburseAmount());
			// Bereavement change - salary
			detail.setBereavementAmount(payDetail.getBereavementAmount());
			Map driverCriteria = new HashMap();
			driverCriteria.clear();
			driverCriteria.put("fullName",payDetail.getDriver());
			driverCriteria.put("status",1);
			Driver drvObj = genericDAO.getByCriteria(Driver.class, driverCriteria);
			if(drvObj!=null)
				detail.setTerminalName(drvObj.getTerminal().getName());
			else{
				driverCriteria.clear();
				driverCriteria.put("status",1);
				driverCriteria.put("fullName",payDetail.getDriver());
				Driver drvsObj = genericDAO.getByCriteria(Driver.class, driverCriteria);
				if(drvsObj!=null)
					detail.setTerminalName(drvsObj.getTerminal().getName());
				else
					detail.setTerminalName("");
			}
			summary.add(detail);
		}
		for(HourlyPayrollInvoiceDetails invoiceDetails:hourlyPayrollInvoiceDetails){
			SalaryDetail detail=new SalaryDetail();
			detail.setDriver(invoiceDetails.getDriver());
			detail.setCategory(invoiceDetails.getCategory());
			detail.setCompanyname(invoiceDetails.getCompany());
			detail.setAmount(invoiceDetails.getSumamount());
			detail.setVacationAmount(invoiceDetails.getVacationAmount());
			detail.setSickPersonalAmount(invoiceDetails.getSickParsanolAmount());
			detail.setBonusAmount(invoiceDetails.getBonusAmounts());
			detail.setHolidayAmount(invoiceDetails.getHolidayAmount());
			detail.setTotalAmount(invoiceDetails.getSumOfTotVacSicBonus());
			detail.setMiscAmount(invoiceDetails.getMiscAmount());
			detail.setReimburseAmount(invoiceDetails.getReimburseAmount());
			// Bereavement change
			detail.setBereavementAmount(invoiceDetails.getBereavementAmount());
			Map driverCriteria = new HashMap();
			driverCriteria.clear();
			driverCriteria.put("fullName",invoiceDetails.getDriver());
			driverCriteria.put("status",1);
			Driver drvObj = genericDAO.getByCriteria(Driver.class, driverCriteria);
			if(drvObj!=null)
				detail.setTerminalName(drvObj.getTerminal().getName());
			else{
				driverCriteria.clear();
				driverCriteria.put("status",1);
				driverCriteria.put("fullName",invoiceDetails.getDriver());
				Driver drvsObj = genericDAO.getByCriteria(Driver.class, driverCriteria);
				if(drvsObj!=null)
					detail.setTerminalName(drvsObj.getTerminal().getName());
				else
					detail.setTerminalName("");
			}
			summary.add(detail);
		}
		
		Comparator<SalaryDetail> comparator=new Comparator<SalaryDetail>() {
			@Override
			public int compare(SalaryDetail o1, SalaryDetail o2) {
				return  o1.getCompanyname().compareTo(o2.getCompanyname());
			}
		};
		Comparator<SalaryDetail> comparator1=new Comparator<SalaryDetail>() {
			@Override
			public int compare(SalaryDetail o1, SalaryDetail o2) {
				return  o1.getTerminalName().compareTo(o2.getTerminalName());
			}
		};
		
		
		Comparator<SalaryDetail> comparator2=new Comparator<SalaryDetail>() {
			@Override
			public int compare(SalaryDetail o1, SalaryDetail o2) {
				return  o1.getDriver().compareTo(o2.getDriver());
			}
		};
		ComparatorChain chain = new ComparatorChain();  
		chain.addComparator(comparator);
		chain.addComparator(comparator1);
		chain.addComparator(comparator2);
		Collections.sort(summary, chain); 
		return summary;
	}
}