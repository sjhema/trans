package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmpBonusTypesList;
//import com.primovision.lutransport.model.hr.Driver;
import com.primovision.lutransport.model.hr.BonusType;
import com.primovision.lutransport.model.hr.EmployeeBonus;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HolidayType;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayHistoryInput;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

/**
 * @author kishor
 *
 */
/*@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/driverpay")*/
public class DriverPayHistoryController extends BaseController{
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	@Autowired
	protected HrReportService hrReportService;
	
	public void setHrReportService(
			HrReportService hrReportService) {
		this.hrReportService = hrReportService;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if (criteria != null) {
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		Map criterias = new HashMap();
		criterias.put("name","Driver");
		EmployeeCatagory catagory=genericDAO.getByCriteria(EmployeeCatagory.class, criterias);
		if(catagory==null){
			request.getSession().setAttribute("error","Driver category not available" );
			return "hr/report/driverpayhistory";
		}
		criterias.clear();
		criterias.put("catagory.id", catagory.getId());
		criterias.put("status",1);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		model.addAttribute("payrollrunStatus", listStaticData("PAYROLLRUN_STATUS"));
		return "hr/report/driverpayhistory";
	}
protected Map<String,Object> generateReportData(SearchCriteria criteria ,DriverPayHistoryInput input,HttpServletRequest request){

	Map<String,Object> data = new HashMap<String,Object>();
	Map<String,Object> params = new HashMap<String,Object>();
	DriverPayWrapper wrapper=generateDriverPayReportData(criteria,input);
	
	//
	
	 String sum= (String) criteria.getSearchMap().get("summary");
	
	String driverid1=input.getDrivers();
	String frombatch=input.getBatchDateFrom();
	String tobatch=input.getBatchDateTo();
	String company=input.getCompanies();
	String terminal=input.getTerminals();	 
	String status=input.getPayrollstatus();
	frombatch=ReportDateUtil.getFromDate(frombatch);
	tobatch=ReportDateUtil.getFromDate(tobatch);
	
	 
	 StringBuffer driverquery=new StringBuffer("");
	  driverquery.append("select obj from Driver obj where 1=1 ");
	 if(!StringUtils.isEmpty(company)){
 		driverquery.append(" and obj.company in ("+company+")");
     }
 	 if(!StringUtils.isEmpty(terminal)){
 		driverquery.append(" and obj.terminal in ("+terminal+")");
      }
 	if(!StringUtils.isEmpty(driverid1)){
 		driverquery.append(" and obj.fullName in ("+driverid1+")");
 	}
 	
 	
 	Map<String,Double> sumAmountsMap = wrapper.getSumAmountsMap();
 	
 	List<String> driverNames = wrapper.getDriverNames();
 	String drvName ="";
 	for(String driversName : driverNames){
 		if(drvName.equals("")){
 			drvName = "'"+driversName+"'";
 		}
 		else{
 			drvName = drvName+",'"+driversName+"'";
 		}
 	}
 	
 	
 	List<Driver> driverWithOutTickets = null;
    if(!drvName.equals("") && StringUtils.isEmpty(driverid1)){
    	StringBuffer driverQuery = new StringBuffer("select obj from Driver obj where obj.catagory=2  and obj.fullName not in (");
    	driverQuery.append(drvName).append(")");
    	if(!StringUtils.isEmpty(company)){
    		driverQuery.append(" and obj.company in ("+company+")");
    	}
    	if(!StringUtils.isEmpty(terminal)){
    		driverQuery.append(" and obj.terminal in ("+terminal+")");
         }
    	
    	driverWithOutTickets = genericDAO.executeSimpleQuery(driverQuery.toString());
    }
    else if(drvName.equals("") && !StringUtils.isEmpty(driverid1)){
    	StringBuffer driverQuery = new StringBuffer("select obj from Driver obj where   obj.catagory=2 and obj.fullName in ('");
    	driverQuery.append(driverid1).append("')");
    	if(!StringUtils.isEmpty(company)){
    		driverQuery.append(" and obj.company in ("+company+")");
    	}
    	if(!StringUtils.isEmpty(terminal)){
    		driverQuery.append(" and obj.terminal in ("+terminal+")");
         }
    	
    	driverWithOutTickets = genericDAO.executeSimpleQuery(driverQuery.toString());
    }
    
    
    //****************************************************************************
    if(!StringUtils.contains(sum, "true")){
    if(driverWithOutTickets!=null){
    	for(Driver drvObj : driverWithOutTickets){	
        	
        	Driver driver3 =  drvObj;
        	
        	boolean setDriver = false;				
			
					StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
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
							
						}
						if(ptodapplication.getLeavetype().getId()==4){
							setDriver = true;
							
						}
					}					
					
					StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
					if(!StringUtils.isEmpty(frombatch)){
						bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
					}
					if(!StringUtils.isEmpty(tobatch)){
						bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
					}					
					
						List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
						int count1=0;
						for(EmployeeBonus bonus:bonuses){									
							for(EmpBonusTypesList list:bonus.getBonusTypesLists()){										
								setDriver = true;
							}									
						}
						
						StringBuffer reimbursementquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes='Reimbursement'");
						
						if(!StringUtils.isEmpty(frombatch)){
							reimbursementquery.append(" and obj.batchFrom>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							reimbursementquery.append(" and obj.batchTo<='"+tobatch+"'");
						}		
						
						
						List<MiscellaneousAmount> reimbursebonuses=genericDAO.executeSimpleQuery(reimbursementquery.toString());
							
						for(MiscellaneousAmount miscAmount:reimbursebonuses){	
							setDriver = true;
															
						}														
						
						StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes!='Reimbursement'");
						if(!StringUtils.isEmpty(frombatch)){
							miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
						}		
							List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
							int count=0;
							for(MiscellaneousAmount miscamnt:miscamounts){
								setDriver = true;																		
							}							
							
						StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+driver3.getCompany().getId()+" and obj.terminal="+driver3.getTerminal().getId()+" and obj.catagory="+driver3.getCatagory().getId()+" and obj.leaveType=3");
						if(!StringUtils.isEmpty(frombatch)){
							holidayquery.append(" and obj.batchdate>='"+frombatch+"'");
							}
							if(!StringUtils.isEmpty(tobatch)){
								holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
							}						   
						   
							List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
							for(HolidayType type:holidayTypes){										
								setDriver = true;
								
							}
				//}   	
    	
				if(setDriver) {									
					DriverPay pay=new DriverPay();
					pay.setDrivername(driver3.getFullName());
					pay.setCompany(driver3.getCompany());
					pay.setTerminalname(driver3.getTerminal().getName());
					List<DriverPay> driverPays = wrapper.getDriverPays();
					driverPays.add(pay);
					wrapper.setTotalRowCount(wrapper.getTotalRowCount()+1);
					wrapper.setDriverPays(driverPays);															
				}						
    	}
	}  
    
}
    List<DriverPay> summary = wrapper.getDriverPays();
	
    Comparator<DriverPay> comparator=new Comparator<DriverPay>() {
		@Override
		public int compare(DriverPay o1, DriverPay o2) {
			return  o1.getCompanyname().compareTo(o2.getCompanyname());
		}
	};
	
	Comparator<DriverPay> comparator1=new Comparator<DriverPay>() {
		@Override
		public int compare(DriverPay o1, DriverPay o2) {
			return  o1.getTerminalname().compareTo(o2.getTerminalname());
		}
	};
    
	Comparator<DriverPay> comparator2=new Comparator<DriverPay>() {
		@Override
		public int compare(DriverPay o1, DriverPay o2) {
			return  o1.getDrivername().compareTo(o2.getDrivername());
		}
	};
	
	ComparatorChain chain = new ComparatorChain();  
	
	/*if(!StringUtils.isEmpty(company)){
		chain.addComparator(comparator);
		chain.addComparator(comparator1);
	}*/
	chain.addComparator(comparator2);
	Collections.sort(summary, chain);
	
	wrapper.setDriverPays(summary);
    
    
    
    
    
    //*************************************************************************
    
  
	 
	 			List driverpays=wrapper.getDriverPays();
	 
	           Map empmap=new HashMap();
	           List<Double> parameter1=new ArrayList<Double>();
	           List<Double> parameter2=new ArrayList<Double>();
	           List<String> numberofsickdays=new ArrayList<String>();
	           List<String> numberofvactiondays=new ArrayList<String>();
	           
	           List<String> calculateDedcution=new ArrayList<String>();
	           
	           List<Double> bonusamount=new ArrayList<Double>();
	           List<Double> bonusamount0=new ArrayList<Double>();
	           List<Double> bonusamount1=new ArrayList<Double>();
	           List<Double> bonusamount2=new ArrayList<Double>();
	           List<Double> bonusamount3=new ArrayList<Double>();
	           List<Double> bonusamount4=new ArrayList<Double>();
	           
	           List<String> bonusnotes=new ArrayList<String>();
	           List<String> bonusnotes0=new ArrayList<String>();
	           List<String> bonusnotes1=new ArrayList<String>();
	           List<String> bonusnotes2=new ArrayList<String>();
	           List<String> bonusnotes3=new ArrayList<String>();
	           List<String> bonusnotes4=new ArrayList<String>();
	           
	           List<String> bonustype=new ArrayList<String>();
	           List<String> bonustype0=new ArrayList<String>();
	           List<String> bonustype1=new ArrayList<String>();
	           List<String> bonustype2=new ArrayList<String>();
	           List<String> bonustype3=new ArrayList<String>();
	           List<String> bonustype4=new ArrayList<String>();
	           
	           
	           List<Double> miscamount=new ArrayList<Double>();
	           List<Double> miscamount0=new ArrayList<Double>();
	           List<Double> miscamount1=new ArrayList<Double>();
	           List<Double> miscamount2=new ArrayList<Double>();
	           List<Double> miscamount3=new ArrayList<Double>();
	           List<Double> miscamount4=new ArrayList<Double>();
	           List<Double> miscamount5=new ArrayList<Double>();
	           
	           List<String> miscnotes=new ArrayList<String>();
	           List<String> miscnotes0=new ArrayList<String>();
	           List<String> miscnotes1=new ArrayList<String>();
	           List<String> miscnotes2=new ArrayList<String>();
	           List<String> miscnotes3=new ArrayList<String>();
	           List<String> miscnotes4=new ArrayList<String>();
	           List<String> miscnotes5=new ArrayList<String>();
	           
	           List<String> holidayName=new ArrayList<String>();
	           List<String> holidayDateFrom=new ArrayList<String>();
	           List<String> holidayDateTo=new ArrayList<String>();
	           List<Double> holidayamount=new ArrayList<Double>();
	           
	           List<String> reimburseNote=new ArrayList<String>();
	           List<Double> reimburseAmount=new ArrayList<Double>();
	           
	           List<Double> deductionAmounts=new ArrayList<Double>();
	          
	           Double vacationSum=0.0;
	           Double sickSum=0.0;
				Double bonusSum=0.0;					
				Double miscamtSum=0.0;
				Double holidaySum=0.0;
				
	           //for(String driverFullname:drivers){
				Map criti = new HashMap();
				String tempDriverName = ""; 
		if(!StringUtils.contains(sum, "true")){
	        for(int i=0;i<driverpays.size();i++){		        	
	        	DriverPay driverPay = (DriverPay) driverpays.get(i);
	        	if(tempDriverName.equals(""))
	        		tempDriverName = driverPay.getDrivername();
	        	else{
	        		if(tempDriverName == driverPay.getDrivername())
	        			continue;
	        		else
	        			tempDriverName = driverPay.getDrivername();
	        	}
	        	criti.clear();
	        	criti.put("fullName",driverPay.getDrivername());
	        	Driver driver3 = genericDAO.getByCriteria(Driver.class, criti); 
	        	System.out.println("************ the driver name is "+driver3.getFullName());
	        	    Double sickParsonalAmount=0.00;
					Double vacationAmount=0.00;
					Integer numberOfSickDays = 0;
					Integer numberOfVactionDays = 0;
					
					Double bonusAmount=0.0;
					Double bonusAmount0=null;
					Double bonusAmount1=null;
					Double bonusAmount2=null;
					Double bonusAmount3=null;
					Double bonusAmount4=null;
					
					String bonusTypeName="";
					String bonusTypeName0="";
					String bonusTypeName1="";
					String bonusTypeName2="";
					String bonusTypeName3="";
					String bonusTypeName4="";
					
					String bonusNotes="";
					String bonusNotes0="";
					String bonusNotes1="";
					String bonusNotes2="";
					String bonusNotes3="";
					String bonusNotes4="";						
					
					Double holidayAmount=0.0;
					String holidayname="";
					String holidaydateFrom="";
					String holidaydateTo="";
					
					
					String reimburseNotes="";
					Double reimburseAmt=0.0;
					
					Double miscamt=0.0;
					Double miscamt0=null;
					Double miscamt1=null;
					Double miscamt2=null;
					Double miscamt3=null;
					Double miscamt4=null;
					Double miscamt5=null;
					
					String miscnote=""; 
					String miscnote0="";
					String miscnote1="";
					String miscnote2="";
					String miscnote3="";
					String miscnote4="";
					String miscnote5="";
					
					empmap.clear();
					//empmap.put("fullName", driver3.getFullName());
					//empmap.put("status",1);
					//Driver driver=genericDAO.getByCriteria(Driver.class, empmap);
					//if(driver!=null){
						StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
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
							    sickSum+=sickParsonalAmount;
							    numberOfSickDays=numberOfSickDays+(ptodapplication.getDayspaid()+ptodapplication.getPaidoutdays());
							}
							if(ptodapplication.getLeavetype().getId()==4){
								vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
								vacationSum+=vacationAmount;
								numberOfVactionDays=numberOfVactionDays+(ptodapplication.getDayspaid()+ptodapplication.getPaidoutdays());
							}
						}
						
						
						
						StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
						if(!StringUtils.isEmpty(frombatch)){
							bonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
						}
						if(!StringUtils.isEmpty(tobatch)){
							bonusquery.append(" and obj.batchTo<='"+tobatch+"'");
						}
						
						
						
							List<EmployeeBonus> bonuses=genericDAO.executeSimpleQuery(bonusquery.toString());
							int count1=0;
							for(EmployeeBonus bonus:bonuses){									
								for(EmpBonusTypesList list:bonus.getBonusTypesLists()){										
									bonusAmount+=list.getBonusamount();
									bonusSum+=bonusAmount;	
									BonusType bonusType=genericDAO.getById(BonusType.class,list.getBonusType().getId());
									
									if(count1 == 0){
										bonusTypeName0 = bonusType.getTypename();
										bonusNotes0 = list.getNote();											
										bonusAmount0 = (list.getBonusamount()!=null)?list.getBonusamount():0.00;
										if(!StringUtils.isEmpty(bonusTypeName0)){
											if(StringUtils.isEmpty(bonusNotes0)){
												bonusNotes0 =".";
											}		
										}
									}										
									else if(count1 == 1){
										bonusTypeName1 = bonusType.getTypename();
										bonusNotes1 = list.getNote();
										bonusAmount1 = (list.getBonusamount()!=null)?list.getBonusamount():0.00;
										if(!StringUtils.isEmpty(bonusTypeName1)){
											if(StringUtils.isEmpty(bonusNotes1)){
												bonusNotes1 =".";
											}		
										}
									}										
									else if(count1 == 2){
										bonusTypeName2 = bonusType.getTypename();
										bonusNotes2 = list.getNote();
										bonusAmount2 = (list.getBonusamount()!=null)?list.getBonusamount():0.00;
										if(!StringUtils.isEmpty(bonusTypeName2)){
											if(StringUtils.isEmpty(bonusNotes2)){
												bonusNotes2 =".";
											}		
										}
									}																				
									else if(count1 == 3){
										bonusTypeName3 = bonusType.getTypename();
										bonusNotes3 = list.getNote();
										bonusAmount3 = (list.getBonusamount()!=null)?list.getBonusamount():0.00;
										if(!StringUtils.isEmpty(bonusTypeName3)){
											if(StringUtils.isEmpty(bonusNotes3)){
												bonusNotes3 =".";
											}		
										}
									}										
									else if(count1 == 4){
										bonusTypeName4 = bonusType.getTypename();
										bonusNotes4 = list.getNote();
										bonusAmount4 = (list.getBonusamount()!=null)?list.getBonusamount():0.00;
										if(!StringUtils.isEmpty(bonusTypeName4)){
											if(StringUtils.isEmpty(bonusNotes4)){
												bonusNotes4 =".";
											}		
										}
									}
									count1++;
								}									
							}
							
							StringBuffer reimbursementquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes='Reimbursement'");
							
							if(!StringUtils.isEmpty(frombatch)){
								reimbursementquery.append(" and obj.batchFrom>='"+frombatch+"'");
							}
							if(!StringUtils.isEmpty(tobatch)){
								reimbursementquery.append(" and obj.batchTo<='"+tobatch+"'");
							}		
							
							
							List<MiscellaneousAmount> reimbursebonuses=genericDAO.executeSimpleQuery(reimbursementquery.toString());
								
							for(MiscellaneousAmount miscAmount:reimbursebonuses){	
								reimburseNotes = miscAmount.getMiscNotes();
								reimburseAmt = miscAmount.getMisamount();									
							}									
							
															
							
							StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes!='Reimbursement'");
							if(!StringUtils.isEmpty(frombatch)){
								miscamountquery.append(" and obj.batchFrom>='"+frombatch+"'");
							}
							if(!StringUtils.isEmpty(tobatch)){
								miscamountquery.append(" and obj.batchTo<='"+tobatch+"'");
							}
							
							
								List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
								int count=0;
								for(MiscellaneousAmount miscamnt:miscamounts){
									miscamt+=miscamnt.getMisamount();
									
									if(count==0){
										miscnote0 = miscamnt.getMiscNotes();
										miscamt0 = (miscamnt.getMisamount()!=null)?miscamnt.getMisamount():0.00;
									}
									
									if(count==1){
										miscnote1 = miscamnt.getMiscNotes();
										miscamt1 = (miscamnt.getMisamount()!=null)?miscamnt.getMisamount():0.00;
									}
									
									if(count==2){
										miscnote2 = miscamnt.getMiscNotes();
										miscamt2 = (miscamnt.getMisamount()!=null)?miscamnt.getMisamount():0.00;
									}
									
									if(count==3){
										miscnote3 = miscamnt.getMiscNotes();
										miscamt3 = (miscamnt.getMisamount()!=null)?miscamnt.getMisamount():0.00;
									}
									
									if(count==4){
										miscnote4 = miscamnt.getMiscNotes();
										miscamt4 = (miscamnt.getMisamount()!=null)?miscamnt.getMisamount():0.00;
									}
									
									if(count==5){
										miscnote5 = miscamnt.getMiscNotes();
										miscamt5 = (miscamnt.getMisamount()!=null)?miscamnt.getMisamount():0.00;
									}
									
									count++;										
								}
								
								
								
							StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+driver3.getCompany().getId()+" and obj.terminal="+driver3.getTerminal().getId()+" and obj.catagory="+driver3.getCatagory().getId()+" and obj.leaveType=3");
							if(!StringUtils.isEmpty(frombatch)){
								holidayquery.append(" and obj.batchdate>='"+frombatch+"'");
								}
								if(!StringUtils.isEmpty(tobatch)){
									holidayquery.append(" and obj.batchdate<='"+tobatch+"'");
								}
							
							   holidayAmount=0.0;
							   
							   
								List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
								for(HolidayType type:holidayTypes){										
									holidayAmount=holidayAmount+type.getAmount();
									holidaySum+=holidayAmount;										
									holidayname = type.getName();
									holidaydateFrom = sdf.format(type.getDateFrom());
									holidaydateTo = sdf.format(type.getDateTo());
									
								}
					//}
								
								
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
								
								double deductionAmount = 0.0;
								Double sumAmount = 0.0;
								double miscAndLoadAmt = 0.0;
								if(driver3.getDateProbationEnd()!=null){
									System.out.println("********* Driver and date is "+driver3.getFullName()+" and "+driver3.getDateProbationEnd());
									if(new LocalDate(driver3.getDateProbationEnd()).isAfter(dt) || new LocalDate(driver3.getDateProbationEnd()).isEqual(dt)){
										sumAmount = sumAmountsMap.get(driver3.getFullName());
								        if (sumAmount == null)
								        	sumAmount = 0.0;									        
								        miscAndLoadAmt = sumAmount;
								        deductionAmount = miscAndLoadAmt * 0.1;
								        if (deductionAmount > 100.0) {
								        	deductionAmount = 100.0;
								        }
									}										
								}
								
					
					//miscnotes.add(miscnote);
					miscamount.add(MathUtil.roundUp(miscamt,2));	
					System.out.println("*********** misc. amount "+miscamt0);
					miscamount0.add(miscamt0);			
					miscamount1.add(miscamt1);
					miscamount2.add(miscamt2);
					miscamount3.add(miscamt3);
					miscamount4.add(miscamt4);
					miscamount5.add(miscamt5);
					System.out.println("*********** misc. Note "+miscnote0);
					miscnotes0.add(miscnote0);
					miscnotes1.add(miscnote1);
					miscnotes2.add(miscnote2);
					miscnotes3.add(miscnote3);
					miscnotes4.add(miscnote4);
					miscnotes5.add(miscnote5);
					
					bonusamount.add(MathUtil.roundUp(bonusAmount,2));
					bonusamount0.add(bonusAmount0);
					bonusamount1.add(bonusAmount1);
					bonusamount2.add(bonusAmount2);
					bonusamount3.add(bonusAmount3);
					bonusamount4.add(bonusAmount4);
					
					bonusnotes0.add(bonusNotes0);
					bonusnotes1.add(bonusNotes1);
					bonusnotes2.add(bonusNotes2);
					bonusnotes3.add(bonusNotes3);
					bonusnotes4.add(bonusNotes4);
					
					bonustype0.add(bonusTypeName0);
					bonustype1.add(bonusTypeName1);
					bonustype2.add(bonusTypeName2);
					bonustype3.add(bonusTypeName3);
					bonustype4.add(bonusTypeName4);
					
					holidayamount.add(MathUtil.roundUp(holidayAmount,2));
					holidayDateTo.add(holidaydateTo);
					holidayDateFrom.add(holidaydateFrom);
					holidayName.add(holidayname);
					
					reimburseAmount.add(reimburseAmt);
					reimburseNote.add(reimburseNotes);
					
					
					numberofsickdays.add(String.valueOf(numberOfSickDays));
					numberofvactiondays.add(String.valueOf(numberOfVactionDays));
					
					parameter1.add(MathUtil.roundUp(sickParsonalAmount,2));
					parameter2.add(MathUtil.roundUp(vacationAmount,2));
					deductionAmounts.add(MathUtil.roundUp(deductionAmount,2));						
	           }		        
		}
	           params.put("sumTotal",MathUtil.roundUp(wrapper.getSumTotal(),2));
	           
	           
	           params.put("numberOfSickdays",numberofsickdays);
	           params.put("numberOfVacation",numberofvactiondays);
	           
	           params.put("calculateDeduction",calculateDedcution);
	           
	           
	           params.put("miscNotes0",miscnotes0);
	           params.put("miscNotes1",miscnotes1);
	           params.put("miscNotes2",miscnotes2);
	           params.put("miscNotes3",miscnotes3);
	           params.put("miscNotes4",miscnotes4);
	           params.put("miscNotes5",miscnotes5);
	           
	           params.put("miscAmount", miscamount);
	           params.put("miscAmount0",miscamount0);
	           params.put("miscAmount1",miscamount1);
	           params.put("miscAmount2",miscamount2);
	           params.put("miscAmount3",miscamount3);
	           params.put("miscAmount4",miscamount4);
	           params.put("miscAmount5",miscamount5);
	           	           
	           params.put("bonusAmount", bonusamount);
	           params.put("bonusAmount0", bonusamount0);
	           params.put("bonusAmount1", bonusamount1);
	           params.put("bonusAmount2", bonusamount2);
	           params.put("bonusAmount3", bonusamount3);
	           params.put("bonusAmount4", bonusamount4);
	           
	           params.put("bonusNotes0", bonusnotes0);
	           params.put("bonusNotes1", bonusnotes1);
	           params.put("bonusNotes2", bonusnotes2);
	           params.put("bonusNotes3", bonusnotes3);
	           params.put("bonusNotes4", bonusnotes4);
	           
	           params.put("bonusType0", bonustype0);
	           params.put("bonusType1", bonustype1);
	           params.put("bonusType2", bonustype2);
	           params.put("bonusType3", bonustype3);
	           params.put("bonusType4", bonustype4);
	           
	           params.put("holidayamount", holidayamount);
	           params.put("holidayName", holidayName);
	           params.put("holidayDateFrom", holidayDateFrom);
	           params.put("holidayDateTo", holidayDateTo);
	           
	           params.put("reimburseAmount", reimburseAmount);
	           params.put("reimburseNote", reimburseNote);
	           
	           params.put("parameter1", parameter1);
	           params.put("parameter2", parameter2);
	           params.put("deductionAmounts",deductionAmounts);
	           
	        
	         
	        params.put("totalRowCount", wrapper.getTotalRowCount());
	   		params.put("driver", wrapper.getDriver());
	   		params.put("company", wrapper.getCompany());
	   		params.put("batchDateFrom", wrapper.getBatchDateFrom());
	   		params.put("batchDateTo",wrapper.getBatchDateTo());
	   		//params.put("sumTotal",wrapper.getSumTotal());
	   		params.put("payRollBatch", wrapper.getPayRollBatch());
	   		data.put("data", wrapper.getDriverPays());
	   		params.put("sumAmount", wrapper.getSumAmount());
	           
	data.put("params", params);
	setList(wrapper.getList());
	return data;

}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") DriverPayHistoryInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		
				Map imagesMap = new HashMap();
				request.getSession().setAttribute("IMAGES_MAP", imagesMap);
				request.getSession().setAttribute("input", input);
				populateSearchCriteria(request, request.getParameterMap());
				SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
				
		try{    Map<String, Object> datas = null;
			    String sum= (String) criteria.getSearchMap().get("summary");
			    
			    Map criterias = new HashMap();
			    List<DriverPayFreezWrapper> listObj = (List<DriverPayFreezWrapper>) genericDAO.getByCriteria(DriverPayFreezWrapper.class, criterias);
			    
			    	//datas=generateReportData(criteria, input, request);
			   
				
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				    String report ;
				    if(StringUtils.contains(sum, "true")){
					report="driverpayhistoryNew";
				    }else{
				    	report="driverpayhistoryNew";
				    }
			
					if (StringUtils.isEmpty(type))
						type = "html";
					response.setContentType(MimeUtil.getContentType(type));
					
					if (!type.equals("html") && !(type.equals("print"))) {
						response.setHeader("Content-Disposition",
								"attachment;filename=" + "driverpay" + "." + type);
					}
					//ByteArrayOutputStream out = new ByteArrayOutputStream();
					Map params = new HashMap();
					out = dynamicReportService.generateStaticReport(report,
							listObj, params, type, request);
					out.writeTo(response.getOutputStream());
					out.close();
					return null;
			} catch (Exception e) {
				e.printStackTrace();
				request.getSession().setAttribute("errors", e.getMessage());
				return "error";
			}
	}
	protected Map<String,Object> generateReportDataDetail(SearchCriteria criteria ,DriverPayHistoryInput input,HttpServletRequest request){
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		DriverPayWrapper wrapper=generateDriverPayReportDataDetail(criteria, input);
		String driversinput=input.getDrivers();
		String companies=input.getCompanies();
		String terminals=input.getTerminals();
		String batchDateFrom=input.getBatchDateFrom();
		String batchDateTo=input.getBatchDateTo();
		String payrollBatchDate=input.getPayrollBatchDate();
		String payrollstatus=input.getPayrollstatus();
		batchDateFrom=ReportDateUtil.getFromDate(batchDateFrom);
		batchDateTo=ReportDateUtil.getToDate(batchDateTo);
		payrollBatchDate=ReportDateUtil.getFromDate(payrollBatchDate);
		Map criterias=new HashMap();
		StringBuffer driverids=new StringBuffer("");
		if(!StringUtils.isEmpty(driversinput)){
			String[] driverinput=driversinput.split(",");
			for(String driverstr:driverinput){
				Driver driver1=genericDAO.getById(Driver.class,Long.parseLong(driverstr));
				criterias.put("fullName", driver1.getFullName());
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
	        	 	 Map empmap=new HashMap();
			           List<Double> parameter1=new ArrayList<Double>();
			           List<Double> parameter2=new ArrayList<Double>();
			           List<Double> bonusamount=new ArrayList<Double>();
			           List<Double> holidayamount=new ArrayList<Double>();
			           List<Double> miscamount=new ArrayList<Double>();
			           List<String> miscnotes=new ArrayList<String>();
			           Double vacationSum=0.0;
						Double sickSum=0.0;
						Double bonusSum=0.0;
						Double miscamtSum=0.0;
						Double holidaySum=0.0;
			           for(Driver driver3:drivers){
			        	   Double sickPersonalAmount=0.00;
							Double vacationAmount=0.00;
							Double bonusAmount=0.00;
							Double holidayAmount=0.0;
							Double miscamt=0.0;
							String miscnote=""; 
							empmap.clear();
							empmap.put("fullName", driver3.getFullName());
						//	empmap.put("status",1);
							Driver driver=genericDAO.getByCriteria(Driver.class, empmap);
							if(driver!=null){
								StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+driver.getId()+" and obj.category=2");
								if(!StringUtils.isEmpty(batchDateFrom)){
								ptodquery.append(" and obj.batchdate>='"+batchDateFrom+"'");
								}
								if(!StringUtils.isEmpty(batchDateTo)){
									ptodquery.append(" and obj.batchdate<='"+batchDateTo+"'");
								}
								List<Ptodapplication> ptodapplications= genericDAO.executeSimpleQuery(ptodquery.toString());
								for(Ptodapplication ptodapplication:ptodapplications){
									if(ptodapplication.getLeavetype().getId()==1){
										sickPersonalAmount=sickPersonalAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());;
									    sickSum+=sickPersonalAmount;
									}
									if(ptodapplication.getLeavetype().getId()==4){
										vacationAmount=vacationAmount+(ptodapplication.getAmountpaid())+(ptodapplication.getHourlyamountpaid());
										vacationSum+=vacationAmount;
									}
								}
								StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.driver="+driver.getId()+" and obj.category=2");
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
											bonusSum+=bonusAmount;											
										}
									}
									
									StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.driver="+driver.getId());
									if(!StringUtils.isEmpty(batchDateFrom)){
										miscamountquery.append(" and obj.batchFrom>='"+batchDateFrom+"'");
									}
									if(!StringUtils.isEmpty(batchDateTo)){
										miscamountquery.append(" and obj.batchTo<='"+batchDateTo+"'");
									}
									List<MiscellaneousAmount> miscamounts=genericDAO.executeSimpleQuery(miscamountquery.toString());
									for(MiscellaneousAmount miscamnt:miscamounts){
										miscamt+=miscamnt.getMisamount();
										if(miscnote.equals(""))
											miscnote=miscamnt.getMiscNotes();
										else
											miscnote=miscnote+","+miscamnt.getMiscNotes();
									}
									
									
									
									StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.paid=1 and obj.company="+driver.getCompany().getId()+" and obj.terminal="+driver.getTerminal().getId()+" and obj.catagory="+driver.getCatagory().getId()+" and obj.leaveType=3");
									if(!StringUtils.isEmpty(batchDateFrom)){
										holidayquery.append(" and obj.batchdate>='"+batchDateFrom+"'");
										}
										if(!StringUtils.isEmpty(batchDateTo)){
											holidayquery.append(" and obj.batchdate<='"+batchDateTo+"'");
										}
										List<HolidayType> holidayTypes=genericDAO.executeSimpleQuery(holidayquery.toString());
										for(HolidayType type:holidayTypes){
											holidayAmount=holidayAmount+type.getAmount();
											holidaySum+=holidayAmount;
										}		
							}
							miscnotes.add(miscnote);
							bonusamount.add(bonusAmount);
							miscamount.add(miscamt);
							parameter1.add(sickPersonalAmount);
							parameter2.add(vacationAmount);
							holidayamount.add(holidayAmount);
			           }
	    params.put("miscNotes",miscnotes);
		params.put("bonusamount", bonusamount);
		params.put("miscAmount", miscamount);
	    params.put("parameter1", parameter1);
	    params.put("parameter2", parameter2);
	    params.put("holidayamount", holidayamount);
		params.put("totalRowCount", wrapper.getTotalRowCount());
		params.put("sumTotal",wrapper.getSumTotal()+sickSum+vacationSum+bonusSum);
		//params.put("sumAmount", wrapper.getSumAmount());
		data.put("data", wrapper.getDriverPays());
		data.put("params", params);
		return data;
	}
	
	public DriverPayWrapper generateDriverPayReportDataDetail(SearchCriteria criteria,DriverPayHistoryInput input){
		return hrReportService.generateDriverPayHistoryDetail(criteria, input);
	}
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25000);
		DriverPayHistoryInput input = (DriverPayHistoryInput)request.getSession().getAttribute("input");
		 String sum= (String) criteria.getSearchMap().get("summary");
		try {
			Map<String,Object> datas ;
			String report;
			 if(StringUtils.contains(sum, "true")){
				 report="driverpayallhistory";
			datas= generateReportData(criteria,input, request);
			 }else{
				 if(type.equalsIgnoreCase("pdf")){
					 report="driverpayhistorypdf"; 
				 }else{
					 report="driverpayhistory";
				 }
				 datas=generateReportData(criteria, input, request);
			 }
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= driverpayhistory."+type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	public DriverPayWrapper generateDriverPayReportData(SearchCriteria criteria,DriverPayHistoryInput input){
		return hrReportService.generateDriverPayHistory(criteria, input);
	}
	
	@ModelAttribute("modelObject")
	public DriverPayHistoryInput setupModel(HttpServletRequest request) {
		return new DriverPayHistoryInput();
	}
	
	
	public List<String> list = new ArrayList<String>();

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
}
