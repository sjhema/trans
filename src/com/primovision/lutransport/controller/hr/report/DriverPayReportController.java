package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.hr.BonusType;
import com.primovision.lutransport.model.hr.EmpBonusTypesList;
//import com.primovision.lutransport.model.hr.Driver;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.EmployeeBonus;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HolidayType;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.model.hrreport.PayChexDetail;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
/**
 * @author kishor
 *
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/driver")
public class DriverPayReportController extends BaseController{

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
			return "hr/report/driverpay";
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
		return "hr/report/driverpay";
	}
	
	
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request) throws ParseException {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		DriverPayWrapper wrapper=generateDriverPayRoll(criteria);
		
		//
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
		
		String driversmul=(String)criteria.getSearchMap().get("driversmul");
		 String sum= (String) criteria.getSearchMap().get("summary");
		 String specialDetails= (String) criteria.getSearchMap().get("specdetail");
		String payrollDate=(String) criteria.getSearchMap().get("payrollDate");
		String driverid1=(String) criteria.getSearchMap().get("driver");
		String frombatch=(String) criteria.getSearchMap().get("fromDate");
		String tobatch=(String) criteria.getSearchMap().get("toDate");
		String company=(String) criteria.getSearchMap().get("company");
		String terminal=(String) criteria.getSearchMap().get("terminal");
		 String expire1= (String) criteria.getSearchMap().get("expire");
		 String status=(String)criteria.getSearchMap().get("pay");
		 String sta=(String)criteria.getSearchMap().get("stat");
			frombatch=ReportDateUtil.getFromDate(frombatch);
			tobatch=ReportDateUtil.getFromDate(tobatch);
		 if(!StringUtils.isEmpty(status)){
			 status="1";
		 }else{
			 status="2";
		 }
		 
		 StringBuffer driverquery=new StringBuffer("");
		  driverquery.append("select obj from Driver obj where 1=1 ");
		 if(!StringUtils.isEmpty(company)){
     		driverquery.append(" and obj.company="+company);
         }
     	 if(!StringUtils.isEmpty(terminal)){
     		driverquery.append(" and obj.terminal="+terminal);
          }
     	if(!StringUtils.isEmpty(driverid1)){
     		driverquery.append(" and obj.fullName='"+driverid1+"'");
     	}
     	
     	
     	Map<String,Double> sumAmountsMap = wrapper.getSumAmountsMap();
     	Map<String,Integer> totolCounts = wrapper.getTotolcounts();
     	
     	
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
        	StringBuffer driverQuery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1'  and obj.fullName not in (");
        	driverQuery.append(drvName).append(")");
        	if(!StringUtils.isEmpty(company)){
        		driverQuery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		driverQuery.append(" and obj.terminal="+terminal);
             }
        	
        	driverWithOutTickets = genericDAO.executeSimpleQuery(driverQuery.toString());
        }
        else if(drvName.equals("") && !StringUtils.isEmpty(driverid1)){
        	StringBuffer driverQuery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1' and obj.fullName in ('");
        	driverQuery.append(driverid1).append("')");
        	if(!StringUtils.isEmpty(company)){
        		driverQuery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		driverQuery.append(" and obj.terminal="+terminal);
             }
        	
        	driverWithOutTickets = genericDAO.executeSimpleQuery(driverQuery.toString());
        }
        else if(drvName.equals("") && StringUtils.isEmpty(driverid1)){
        	StringBuffer driverQuery = new StringBuffer("select obj from Driver obj where obj.status=1 and obj.payTerm='1'");
        	
        	if(!StringUtils.isEmpty(company)){
        		driverQuery.append(" and obj.company="+company);
        	}
        	if(!StringUtils.isEmpty(terminal)){
        		driverQuery.append(" and obj.terminal="+terminal);
             }
        	
        	driverWithOutTickets = genericDAO.executeSimpleQuery(driverQuery.toString());
        }
        
        
        //****************************************************************************
        if(!StringUtils.contains(sum, "true")){
        if(driverWithOutTickets!=null){
        	for(Driver drvObj : driverWithOutTickets){	
	        	
	        	Driver driver3 =  drvObj;
	        	
	        	boolean setDriver = false;				
				
						StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
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
						
						StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
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
							
							
							//************************* Newly Added
							
							StringBuffer qutarBonusquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscType='Quarter Bonus'");
							
							if(!StringUtils.isEmpty(frombatch)){
								qutarBonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
							}
							if(!StringUtils.isEmpty(tobatch)){
								qutarBonusquery.append(" and obj.batchTo<='"+tobatch+"'");
							}		
							
							
							List<MiscellaneousAmount> qutarbonuses=genericDAO.executeSimpleQuery(qutarBonusquery.toString());
								
							for(MiscellaneousAmount miscAmount:qutarbonuses){	
								setDriver = true;																
							}
							
							/////******************************
							
							
							StringBuffer reimbursementquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes='Reimbursement'");
							
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
							
							StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes!='Reimbursement' and obj.miscType!='Quarter Bonus'");
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
							
								
							if(holidayExceptionDriverNameList.contains(driver3.getFullName()))	{
								//do nothing
							}
							else {
							StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus!=2 and  obj.paid=1 and obj.company="+driver3.getCompany().getId()+" and obj.terminal="+driver3.getTerminal().getId()+" and obj.catagory="+driver3.getCatagory().getId()+" and obj.leaveType=3");
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
							}
					//}   	
        	
					if(setDriver) {									
						DriverPay pay=new DriverPay();
						pay.setDrivername(driver3.getFullName());
						pay.setCompany(driver3.getCompany());
						pay.setTerminalname(driver3.getTerminal().getName());
						pay.setTerminal(driver3.getTerminal());
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
        
        
     	/*String driverIDs="";
		if((!StringUtils.isEmpty(company)) || (!StringUtils.isEmpty(terminal)) || (!StringUtils.isEmpty(driverid1))){
			List<Driver> drivers = genericDAO.executeSimpleQuery(driverquery.toString());			
			if(drivers.size()>0 && drivers!=null){
				for(Driver driver:drivers){
					if(driverIDs.equals(""))
						driverIDs = driver.getId().toString();
					else
						driverIDs = driverIDs+","+driver.getId().toString();
				}
			}
		}*/		
     
     /*	Location companylocation=null;     	
     	String driverIds="";
     	
     	if(!StringUtils.isEmpty(company)){
		    companylocation=genericDAO.getById(Location.class, Long.parseLong(company));
		}
     	
     	if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid1)){
			 Map drivercriteria = new HashMap();
			 drivercriteria.clear();
			 drivercriteria.put("company.id",companylocation.getId());
			 List<Driver> drivers = genericDAO.findByCriteria(Driver.class, drivercriteria);
			 for(Driver driverObj:drivers){
				 if(driverIds.equals(""))
					 driverIds = driverObj.getId().toString();
				else
					driverIds = driverIds +","+driverObj.getId().toString();
			 }
		}*/
		
	 /*StringBuffer driquery=new StringBuffer("");
	 driquery.append("select DISTINCT(obj.driver.fullName) from Ticket obj where obj.status=1 and obj.payRollStatus="+status+" and  obj.billBatch>='"+frombatch+
		   		"' and obj.billBatch<='"+tobatch+"' ");
		        	
		        	 if(!StringUtils.isEmpty(driverid1)){
		        		 driquery.append("and obj.driver.fullName ='"+driverid1+"'");
		        	  }
		        	 if(!StringUtils.isEmpty(driversmul)){
		        		 driquery.append(" and obj.driver not in ("+driversmul+")");
		             }
		        	 
		        	 if(!StringUtils.isEmpty(company) && !StringUtils.isEmpty(driverid1)){
		        		 driquery.append(" and obj.driver.fullName='"+driverid1+"'");
		             }
		             else if(!StringUtils.isEmpty(company) && StringUtils.isEmpty(driverid1)){
		            	 driquery.append(" and obj.driver in (").append(driverIds).append(")");
		             }
		        	 
		          	 if(!StringUtils.isEmpty(terminal)){
		          		driquery.append(" and obj.terminal="+terminal);
		               }
		        	 
		        	 
		        	driquery.append(" order by obj.driver.fullName");
		        	
		  	        	
		           drivers=genericDAO.executeSimpleQuery(driquery.toString());*/
		 
		 			List driverpays=wrapper.getDriverPays();
		 			
		 			List<DriverPay> driverPayNew=new ArrayList<DriverPay>();
		 			
		 			List<DriverPayFreezWrapper> driverPayFreezObjList=new ArrayList<DriverPayFreezWrapper>();
		 			
		 
		           Map empmap=new HashMap();
		           List<Double> parameter1=new ArrayList<Double>();
		           List<Double> parameter2=new ArrayList<Double>();
		           List<Double> parameter3=new ArrayList<Double>();
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
		           
		           List<String> quatarNote=new ArrayList<String>();
		           List<Double> quatarAmount=new ArrayList<Double>();
		           
		           
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
		        	DriverPayFreezWrapper driverPayFreezObj = new DriverPayFreezWrapper();
		        	if(tempDriverName.equals(""))
		        		tempDriverName = driverPay.getDrivername();
		        	else{
		        		if(tempDriverName.equals(driverPay.getDrivername())){
		        			 if(StringUtils.contains(specialDetails, "true")){
		        				 driverPayNew.add(driverPay);
		        			 }
		        			 else{
		        				driverPayFreezObj.setAmount(driverPay.getAmount());						
		 						driverPayFreezObj.setBonusAmount(0.00);		 												
		 						driverPayFreezObj.setDeductionAmount(0.00);
		 						driverPayFreezObj.setDestination(driverPay.getDestination());
		 						driverPayFreezObj.setDrivername(driverPay.getDrivername());
		 						driverPayFreezObj.setHolidayAmount(0.00);		 						
		 						driverPayFreezObj.setMiscAmount(0.00);
		 						driverPayFreezObj.setMiscamt(0.0);		 						
		 						driverPayFreezObj.setMiscnote("");		 						
		 						driverPayFreezObj.setNoOfLoad(driverPay.getNoOfLoad());
		 						driverPayFreezObj.setNumberOfSickDays(0);
		 						driverPayFreezObj.setNumberOfVactionDays(0);
		 						driverPayFreezObj.setOrigin(driverPay.getOrigin());						
		 						driverPayFreezObj.setProbationDeductionAmount(0.00);
		 						driverPayFreezObj.setQuatarAmount(0.00);
		 						driverPayFreezObj.setQutarNotes("");
		 						driverPayFreezObj.setRate(driverPay.getRate());
		 						driverPayFreezObj.setReimburseAmount(0.00);
		 						driverPayFreezObj.setReimburseAmt(0.00);
		 						driverPayFreezObj.setIsMainRow("zzz");
		 						driverPayFreezObj.setReimburseNotes("");
		 						driverPayFreezObj.setSeqNum(driverPay.getSeqNum());
		 						driverPayFreezObj.setSickParsonalAmount(0.00);
		 						driverPayFreezObj.setSickPersonalAmount(0.00);
		 						driverPayFreezObj.setSumTotal(MathUtil.roundUp(wrapper.getSumTotal(),2));						
		 						driverPayFreezObj.setTotalAmount(MathUtil.roundUp(driverPay.getTotalAmount(),2));
		 						driverPayFreezObj.setTransportationAmount(0.00);
		 						driverPayFreezObj.setSubTotalAmount(0.00);
		 						driverPayFreezObj.setVacationAmount(0.00);
		 						driverPayFreezObj.setTotalRowCount(wrapper.getTotalRowCount());
		 						driverPayFreezObj.setSumAmount(MathUtil.roundUp(wrapper.getSumAmount(),2));
		 						driverPayFreezObj.setPayRollBatchString(wrapper.getPayRollBatch());
		 						driverPayFreezObj.setBillBatchDateFromString(wrapper.getBatchDateFrom());
		 						driverPayFreezObj.setBillBatchDateToString(wrapper.getBatchDateTo());
		 						
		 						Date batchFrom=null;
								 if(!StringUtils.isEmpty(wrapper.getBatchDateFrom())){
								    batchFrom = new SimpleDateFormat("MM-dd-yyyy")
									.parse(wrapper.getBatchDateFrom());
								    driverPayFreezObj.setBillBatchDateFrom(batchFrom);
								 }
								 
								 Date batchto=null;
								 if(!StringUtils.isEmpty(wrapper.getBatchDateTo())){
									 batchto = new SimpleDateFormat("MM-dd-yyyy")
										.parse(wrapper.getBatchDateTo()); 
									 driverPayFreezObj.setBillBatchDateTo(batchto);
								 }
								 Date payrollbatch =null;
								 if(!StringUtils.isEmpty(wrapper.getPayRollBatch())){
									 payrollbatch = new SimpleDateFormat("MM-dd-yyyy")
										.parse(wrapper.getPayRollBatch()); 
									 
									 driverPayFreezObj.setPayRollBatch(payrollbatch);
								 }
								 if(wrapper.getCompanylocation()!=null){
									 driverPayFreezObj.setCompany(wrapper.getCompanylocation());
									 driverPayFreezObj.setCompanyname(wrapper.getCompanylocation().getName());
								}
								 //if(wrapper.getTerminal()!=null){
								    driverPayFreezObj.setTerminalname(driverPay.getTerminalname());
								    
								    criti.clear();
								    criti.put("name",driverPay.getTerminalname());
								    Location terminalObj = genericDAO.getByCriteria(Location.class,criti);
									
								    if(terminalObj!=null) 
								     driverPayFreezObj.setTerminal(terminalObj);
									 
								// }  
		 						
		 						
		 						
		 						driverPayFreezObjList.add(driverPayFreezObj);
		        			 }
		        			 continue;
		        		}
		        		else{
		        			tempDriverName = driverPay.getDrivername();
		        		}
		        	}
		        	criti.clear();
		        	criti.put("status",1);
		        	criti.put("fullName",driverPay.getDrivername());
		        	Driver driver3 = genericDAO.getByCriteria(Driver.class, criti); 
		        	
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
						
						
						String qutarNotes="";
						Double qutarAmt=0.0;
						
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
							StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
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
							
							
							
							StringBuffer bonusquery=new StringBuffer("select obj from EmployeeBonus obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.category=2");
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
								
								/////////*********** Newly Added
								
								StringBuffer qutarBonusquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscType='Quarter Bonus'");
								
								if(!StringUtils.isEmpty(frombatch)){
									qutarBonusquery.append(" and obj.batchFrom>='"+frombatch+"'");
								}
								if(!StringUtils.isEmpty(tobatch)){
									qutarBonusquery.append(" and obj.batchTo<='"+tobatch+"'");
								}		
								
								
								List<MiscellaneousAmount> qutarbonuses=genericDAO.executeSimpleQuery(qutarBonusquery.toString());
									
								for(MiscellaneousAmount miscAmount:qutarbonuses){	
									qutarNotes = miscAmount.getMiscNotes();
									qutarAmt = miscAmount.getMisamount();									
								}
								
								///*************************
								
								
								
								StringBuffer reimbursementquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes='Reimbursement'");
								
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
								
																
								
								StringBuffer miscamountquery=new StringBuffer("select obj from MiscellaneousAmount obj where obj.payRollStatus!=2 and obj.driver.fullName='"+driver3.getFullName()+"' and obj.miscNotes!='Reimbursement' and obj.miscType!='Quarter Bonus'");
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
									
						 if(holidayExceptionDriverNameList.contains(driver3.getFullName()))	{
							  holidayAmount=0.0;
						 }
						 else {	
									
								StringBuffer holidayquery=new StringBuffer("select obj from HolidayType obj where obj.payRollStatus!=2 and obj.paid=1 and obj.company="+driver3.getCompany().getId()+" and obj.terminal="+driver3.getTerminal().getId()+" and obj.catagory="+driver3.getCatagory().getId()+" and obj.leaveType=3");
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
									
									double deductionAmount = 0.0;
									Double sumAmount = 0.0;
									double miscAndLoadAmt = 0.0;
									System.out.println("************ the driver name is "+driver3.getFullName()+" and id is "+driver3.getId());
									if(driver3.getCompany().getId()!=4l || driver3.getTerminal().getId()!=93l){
										if(driver3.getDateProbationEnd()!=null){
										
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
									}
									else{
										sumAmount = sumAmountsMap.get(driver3.getFullName());
								        if (sumAmount == null)
								        	sumAmount = 0.0;									        
								        miscAndLoadAmt = sumAmount;
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
						
						quatarAmount.add(qutarAmt);
						quatarNote.add(qutarNotes);
						
						numberofsickdays.add(String.valueOf(numberOfSickDays));
						numberofvactiondays.add(String.valueOf(numberOfVactionDays));
						
						parameter1.add(MathUtil.roundUp(sickParsonalAmount,2));
						parameter2.add(MathUtil.roundUp(vacationAmount,2));
						deductionAmounts.add(MathUtil.roundUp(deductionAmount,2));
						
						Double transportationamount = 0.0;						
						transportationamount = sumAmountsMap.get(driver3.getFullName());
				        if (transportationamount == null)
				        	transportationamount = 0.0;
						
						parameter3.add(MathUtil.roundUp(transportationamount,2));
						
						
						
						if(StringUtils.contains(specialDetails, "true")){
							driverPay.setTransAmountSpc(MathUtil.roundUp(transportationamount,2));
							driverPay.setProbdeductionAmountSpc(MathUtil.roundUp(deductionAmount,2));
							driverPay.setSubTotalAmountSpc(MathUtil.roundUp((driverPay.getTransAmountSpc()-driverPay.getProbdeductionAmountSpc()),2));
							driverPay.setMiscAmountSpc(MathUtil.roundUp(miscamt,2));
							driverPay.setBonusAmountSpc(MathUtil.roundUp(bonusAmount,2));
							driverPay.setPtodAmountSpc(MathUtil.roundUp(sickParsonalAmount,2));
							driverPay.setHolidayAmountSpc(MathUtil.roundUp(holidayAmount,2));
							driverPay.setTotalAmountSpc(MathUtil.roundUp(((driverPay.getTransAmountSpc()+driverPay.getPtodAmountSpc()+driverPay.getBonusAmountSpc()+driverPay.getHolidayAmountSpc()+driverPay.getMiscAmountSpc())-driverPay.getProbdeductionAmountSpc()),2));
							driverPay.setVacationAmountSpc(MathUtil.roundUp(vacationAmount,2));
							driverPay.setReimAmountSpc(reimburseAmt);
							driverPay.setQuarterAmountSpc(qutarAmt);							
							driverPayNew.add(driverPay);							
						}
						
						
						
						driverPayFreezObj.setAmount(driverPay.getAmount());						
						driverPayFreezObj.setBonusAmount(MathUtil.roundUp(bonusAmount,2));
						driverPayFreezObj.setBonusAmount0(bonusAmount0);						
						driverPayFreezObj.setBonusAmount1(bonusAmount1);
						driverPayFreezObj.setBonusAmount2(bonusAmount2);
						driverPayFreezObj.setBonusAmount3(bonusAmount3);
						driverPayFreezObj.setBonusAmount4(bonusAmount4);						
						driverPayFreezObj.setBonusNotes(bonusNotes4);
						driverPayFreezObj.setBonusNotes0(bonusNotes0);
						driverPayFreezObj.setBonusNotes2(bonusNotes2);
						driverPayFreezObj.setBonusNotes3(bonusNotes3);
						driverPayFreezObj.setBonusNotes4(bonusNotes4);
						driverPayFreezObj.setBonusTypeName(bonusTypeName4);
						driverPayFreezObj.setBonusTypeName0(bonusTypeName0);
						driverPayFreezObj.setBonusTypeName1(bonusTypeName1);
						driverPayFreezObj.setBonusTypeName2(bonusTypeName2);
						driverPayFreezObj.setBonusTypeName3(bonusTypeName3);
						driverPayFreezObj.setBonusTypeName4(bonusTypeName4);						
						driverPayFreezObj.setDeductionAmount(MathUtil.roundUp(deductionAmount,2));
						driverPayFreezObj.setDestination(driverPay.getDestination());
						driverPayFreezObj.setDrivername(driver3.getFullName());
						driverPayFreezObj.setHolidayAmount(holidayAmount);
						driverPayFreezObj.setHolidaydateFrom(holidaydateFrom);
						driverPayFreezObj.setHolidaydateTo(holidaydateTo);
						driverPayFreezObj.setHolidayname(holidayname);
						driverPayFreezObj.setMiscAmount(MathUtil.roundUp(miscamt,2));
						driverPayFreezObj.setMiscamt(MathUtil.roundUp(miscamt,2));
						driverPayFreezObj.setMiscamt0(miscamt0);
						driverPayFreezObj.setMiscamt1(miscamt1);
						driverPayFreezObj.setMiscamt2(miscamt2);
						driverPayFreezObj.setMiscamt3(miscamt3);
						driverPayFreezObj.setMiscamt4(miscamt4);
						driverPayFreezObj.setMiscamt5(miscamt5);
						driverPayFreezObj.setMiscnote(miscnote);						
						driverPayFreezObj.setMiscnote0(miscnote0);
						driverPayFreezObj.setMiscnote1(miscnote1);
						driverPayFreezObj.setMiscnote2(miscnote2);
						driverPayFreezObj.setMiscnote3(miscnote3);
						driverPayFreezObj.setMiscnote4(miscnote4);
						driverPayFreezObj.setMiscnote5(miscnote5);
						driverPayFreezObj.setNoOfLoad(driverPay.getNoOfLoad());
						if(totolCounts.get(driverPay.getDrivername())!=null){
							driverPayFreezObj.setNoOfLoadtotal(totolCounts.get(driverPay.getDrivername()));
						}
						else{
							driverPayFreezObj.setNoOfLoadtotal(0);
						}
						driverPayFreezObj.setIsMainRow("yes");
						driverPayFreezObj.setNumberOfSickDays(numberOfSickDays);
						driverPayFreezObj.setNumberOfVactionDays(numberOfVactionDays);
						driverPayFreezObj.setOrigin(driverPay.getOrigin());						
						driverPayFreezObj.setProbationDeductionAmount(MathUtil.roundUp(deductionAmount,2));
						driverPayFreezObj.setQuatarAmount(MathUtil.roundUp(qutarAmt,2));
						driverPayFreezObj.setQutarNotes(qutarNotes);
						driverPayFreezObj.setRate(driverPay.getRate());
						driverPayFreezObj.setReimburseAmount(MathUtil.roundUp(reimburseAmt,2));
						driverPayFreezObj.setReimburseAmt(MathUtil.roundUp(reimburseAmt,2));
						driverPayFreezObj.setReimburseNotes(reimburseNotes);
						driverPayFreezObj.setSeqNum(driverPay.getSeqNum());
						driverPayFreezObj.setSickParsonalAmount(MathUtil.roundUp(sickParsonalAmount,2));						
						driverPayFreezObj.setSickPersonalAmount(MathUtil.roundUp(sickParsonalAmount,2));											
						driverPayFreezObj.setTransportationAmount(MathUtil.roundUp(transportationamount,2));						
						driverPayFreezObj.setSubTotalAmount(MathUtil.roundUp((driverPayFreezObj.getTransportationAmount()-driverPayFreezObj.getProbationDeductionAmount()),2));
						driverPayFreezObj.setVacationAmount(MathUtil.roundUp(vacationAmount,2));
						driverPayFreezObj.setTotalRowCount(wrapper.getTotalRowCount());
						driverPayFreezObj.setSumAmount(MathUtil.roundUp(wrapper.getSumAmount(),2));						
						driverPayFreezObj.setTotalAmount(MathUtil.roundUp((driverPayFreezObj.getTransportationAmount()-driverPayFreezObj.getProbationDeductionAmount())+driverPayFreezObj.getMiscAmount()+driverPayFreezObj.getSickParsonalAmount()+driverPayFreezObj.getHolidayAmount()+driverPayFreezObj.getBonusAmount(),2));
						driverPayFreezObj.setSumTotal(MathUtil.roundUp(wrapper.getSumTotal(),2));	
						driverPayFreezObj.setPayRollBatchString(wrapper.getPayRollBatch());
 						driverPayFreezObj.setBillBatchDateFromString(wrapper.getBatchDateFrom());
 						driverPayFreezObj.setBillBatchDateToString(wrapper.getBatchDateTo());
						
						Date batchFrom=null;
						 if(!StringUtils.isEmpty(wrapper.getBatchDateFrom())){
						    batchFrom = new SimpleDateFormat("MM-dd-yyyy")
							.parse(wrapper.getBatchDateFrom());
						    driverPayFreezObj.setBillBatchDateFrom(batchFrom);
						 }
						 
						 Date batchto=null;
						 if(!StringUtils.isEmpty(wrapper.getBatchDateTo())){
							 batchto = new SimpleDateFormat("MM-dd-yyyy")
								.parse(wrapper.getBatchDateTo()); 
							 driverPayFreezObj.setBillBatchDateTo(batchto);
						 }
						 Date payrollbatch =null;
						 if(!StringUtils.isEmpty(wrapper.getPayRollBatch())){
							 payrollbatch = new SimpleDateFormat("MM-dd-yyyy")
								.parse(wrapper.getPayRollBatch()); 
							 
							 driverPayFreezObj.setPayRollBatch(payrollbatch);
						 }
						 if(wrapper.getCompanylocation()!=null){
							 driverPayFreezObj.setCompany(wrapper.getCompanylocation());
							 driverPayFreezObj.setCompanyname(wrapper.getCompanylocation().getName());
						}
						// if(wrapper.getTerminal()!=null){
							 driverPayFreezObj.setTerminal(driver3.getTerminal());
							 driverPayFreezObj.setTerminalname(driver3.getTerminal().getName());
						 //}  
						 
						 driverPayFreezObjList.add(driverPayFreezObj);
		        }		        
		        
		        if(StringUtils.contains(specialDetails, "true")){
		        	wrapper.setDriverPays(driverPayNew);
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
		           
		           params.put("quatarAmount",quatarAmount);
		           params.put("quatarNote", quatarNote);
		           
		           params.put("parameter1", parameter1);
		           params.put("parameter2", parameter2);
		           params.put("parameter3", parameter3);
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
		data.put("driverfreez",driverPayFreezObjList);
		
		setList(wrapper.getList());
		return data;
	}
	public DriverPayWrapper generateDriverPayRoll(SearchCriteria criteria){
		return hrReportService.generateDriverPayReport(criteria);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) throws ParseException {
		 Map imagesMap = new HashMap();
		   request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		   populateSearchCriteria(request, request.getParameterMap());
		   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		   String[] driversmul=(String[])request.getParameterValues("driversmul");
		   
		   String[] holidayExpdriversmul=(String[])request.getParameterValues("holidayexpdriversmul");
		   
		   String[] doublePaydriversmul=(String[])request.getParameterValues("doubleholpayriversmul");
		   
		   if(holidayExpdriversmul!=null){
			   List<String> holidayExceptionDriverNames = new ArrayList<String>();
			   for(int i=0;i<holidayExpdriversmul.length;i++){
				   holidayExceptionDriverNames.add(holidayExpdriversmul[i]);
			   }
			   searchCriteria.getSearchMap().put("holidayexpdriversmul",holidayExceptionDriverNames);
		   }
		   
		   
		   if(doublePaydriversmul!=null){
			   List<String> doublePayDriverNames = new ArrayList<String>();
			   for(int i=0;i<doublePaydriversmul.length;i++){
				   doublePayDriverNames.add(doublePaydriversmul[i]);
			   }
			   searchCriteria.getSearchMap().put("doubleholpayriversmul",doublePayDriverNames);
		   }
		   
		   
		   StringBuffer drivids=new StringBuffer("");
		   String driv = null;
		   if(driversmul!=null){
		   driv=StringUtils.join(driversmul, ",");
		   List<Driver> employees=genericDAO.findByCommaSeparatedIds(Driver.class,driv);
		   for(Driver driver1:employees){
		   imagesMap.clear();
		   imagesMap.put("status",1);
		   imagesMap.put("fullName",driver1.getFullName());
		   Driver driver=genericDAO.getByCriteria(Driver.class, imagesMap);
		   if(driver!=null){
			   drivids.append(driver.getId());
			   drivids.append(",");
		   }
		}
		   if(drivids!=null&&drivids.length()>0){
				int d=drivids.lastIndexOf(",");
				drivids.deleteCharAt(d);
			}
		   
		   }
		   searchCriteria.getSearchMap().put("driversmul",drivids.toString());
		   String expire= (String) searchCriteria.getSearchMap().get("expire");
		   
		   String driverdIds = "";
		   String drivername= (String) searchCriteria.getSearchMap().get("driver");
		   
		   String sum= (String) searchCriteria.getSearchMap().get("summary");
		   String specialDetails= (String) searchCriteria.getSearchMap().get("specdetail");
		   
		   searchCriteria.getSearchMap().put("pay","pay");
		   
		  /*if(!StringUtils.isEmpty(drivername)){			  
			  Map criterias = new HashMap();
			  criterias.put("fullName",drivername);
			  List<Driver> drivers = genericDAO.findByCriteria(Driver.class, criterias);
			  if(drivers!=null && drivers.size()>0){
				  for(Driver driver:drivers){
					if(driverdIds.equals(""))
						driverdIds = driver.getId().toString();
					else
						driverdIds = driverdIds+","+driver.getId();					
				  }
				  searchCriteria.getSearchMap().put("driver",driverdIds);
			  }*/			  
			  
			  
			 /*Driver driver1= genericDAO.getById(Driver.class,Long.parseLong(driverid));
			 imagesMap.clear();
			 imagesMap.put("fullName",driver1.getFullName());
			 Driver driver=genericDAO.getByCriteria(Driver.class, imagesMap);
			 
			 if(driver==null){
				 request.getSession().setAttribute("error","driver not matched");
				 return "blank/blank";
			 }*/
		  //}
		   
		    boolean noRatesFound = false;
			String payrollDate=(String) searchCriteria.getSearchMap().get("payrollDate");
			String driverid=(String) searchCriteria.getSearchMap().get("driver");
			String frombatch=(String)searchCriteria.getSearchMap().get("fromDate");
			String tobatch=(String) searchCriteria.getSearchMap().get("toDate");
			String company=(String) searchCriteria.getSearchMap().get("company");
			String terminal=(String) searchCriteria.getSearchMap().get("terminal");
			String expire1= (String) searchCriteria.getSearchMap().get("expire");
			String status=(String)searchCriteria.getSearchMap().get("pay");
			String sta=(String)searchCriteria.getSearchMap().get("stat");
			String driversmul1=(String)searchCriteria.getSearchMap().get("driversmul");
			
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			
			if(StringUtils.isEmpty(frombatch)){				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -1);				
				Date previousYearDate = cal.getTime();
				
				String frombatchtemp=dateFormat1.format(previousYearDate);
				LocalDate now = new LocalDate(frombatchtemp);
				LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
				searchCriteria.getSearchMap().put("fromDate",sdf.format(sunday.toDate()));
				frombatch=sdf.format(sunday.toDate());
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
	        if(!StringUtils.isEmpty(driversmul1)){
	        	query.append(" and obj.driver not in ("+driversmul1+")");
	        }
	        
	        
	        query.append(" group by obj.origin,obj.destination");
	        if(StringUtils.isEmpty(driverid)){
	        	query.append(",obj.driver.fullName");
	        }
	        query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
	        System.out.println("\n query-->"+query);
			
	        List<String> listOfRecordWithoutRate = new ArrayList<String>();
	        
	        List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
		   
	        listOfRecordWithoutRate.add("<u>Rates are not available between following Driver: Origin - Destination :</u> <br/>");
	        
			for(Ticket ticket:tickets){
				
				Long destination_id;
				Location location = genericDAO.getById(Location.class, ticket
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
			    		else{
			    			noRatesFound = true;
					    	listOfRecordWithoutRate.add(ticket.getDriver().getFullName()+": "+ticket.getOrigin().getName()+" - "+ticket.getDestination().getName() );
			    		}
			       }			    
			       else{
			    	   noRatesFound = true;
			    	   listOfRecordWithoutRate.add(ticket.getDriver().getFullName()+": "+ticket.getOrigin().getName()+" - "+ticket.getDestination().getName() );
			       }
			  }
				
			  if(payUsing!=0){	
				
				
				StringBuffer rateQuery = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"						
						+ ticket.getDestination().getId() 
						+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
						+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'");
				        if(payUsing==1){
				        	rateQuery.append(" and obj.validFrom <='"+ticket.getLoadDate()
				        			+"' and obj.validTo >='"+ticket.getLoadDate()+"'");
				        }
				        else{
				        	rateQuery.append(" and obj.validFrom <='"+ticket.getUnloadDate()
				        			+"' and obj.validTo >='"+ticket.getUnloadDate()+"'");
				        }
				
				
				
				
			    List<DriverPayRate>	fs = genericDAO.executeSimpleQuery(rateQuery.toString());
			    if (fs != null && fs.size() > 0) {
			    	// do nothing
			    }
			    else{			    	
			    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
			    		StringBuffer rateQuery1 = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
							+ ticket.getOrigin().getId() + "' and obj.landfill='"						
							+ 91 
							+ "' and obj.company='"+ticket.getDriver().getCompany().getId()
							+ "' and obj.terminal='"+ticket.getDriver().getTerminal().getId()+"'");
							if(payUsing==1){
					        	rateQuery1.append(" and obj.validFrom <='"+ticket.getLoadDate()
					        			+"' and obj.validTo >='"+ticket.getLoadDate()+"'");
					        }
					        else{
					        	rateQuery1.append(" and obj.validFrom <='"+ticket.getUnloadDate()
					        			+"' and obj.validTo >='"+ticket.getUnloadDate()+"'");
					        }
			    		
			    		List<DriverPayRate>	fs1 = genericDAO.executeSimpleQuery(rateQuery1.toString());
					    if (fs1 != null && fs1.size() > 0) {
					    	// do nothing
					    }
					    else{
					    	noRatesFound = true;
					    	listOfRecordWithoutRate.add(ticket.getDriver().getFullName()+": "+ticket.getOrigin().getName()+" - "+ticket.getDestination().getName() );
					    }
			    	}			    	
			    	else{
			    		noRatesFound = true;
			    		listOfRecordWithoutRate.add(ticket.getDriver().getFullName()+": "+ticket.getOrigin().getName()+" - "+ticket.getDestination().getName() );
			    	}
			    }
			  }
			}
			
			if(noRatesFound){	
				//request.getSession().setAttribute("error", "Rates not avialble for following Origin - Destination");
				request.getSession().setAttribute("errorlist", listOfRecordWithoutRate);
				return "blank/subcontractorexpiredratelist";
			}
			
		 
		   Map<String,Object> datas = generateData(searchCriteria, request);
		
			if (StringUtils.isEmpty(type))
				type = "html";
			
			if(getList().size()>0){
				request.getSession().setAttribute("errorlist",getList());
				return "blank/subcontractorexpiredratelist";
			}
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "driverpay" + "." + type);
			}
	 try{ 
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			String report;
			
			
			System.out.println("****** The special details is 3 "+specialDetails);
			if(StringUtils.contains(specialDetails,"true")){				
				report="driverpayspecial";
			}
			else{
			if(!StringUtils.contains(sum, "true")){
				report="driverpay";
			}
			else{
				report="driverpayall";
			}
			}
			/*if(!StringUtils.isEmpty(driverid)){
				report="driverpay";
			}
			else{
				report="driverpayall";
			}*/
			out = dynamicReportService.generateStaticReport(report,
					(List)datas.get("data"), params, type, request);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		//String sum=request.getParameter("typ");	
		
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		 String sum= (String) criteria.getSearchMap().get("summary");
		 String specialDetails = (String) criteria.getSearchMap().get("specdetail");
		 String driverid= (String) criteria.getSearchMap().get("driver");
		criteria.setPageSize(25000);
		criteria.setPage(0);
		try {
			String report;
			
			
			
			if(StringUtils.contains(specialDetails, "true")){
				report="driverpayspecial";
			}
			else{
				if(!StringUtils.contains(sum, "true")){
					if(type.equalsIgnoreCase("pdf")){
						report="driverpaypdf";
					}else{
						report="driverpay";
					}
				}
				else{
					report="driverpayall";
				}
			}
			/*if(!StringUtils.isEmpty(driverid)){
				report="driverpay";
			}
			else{
				report="driverpayall";
			}*/
			Map<String,Object> datas = generateData(criteria, request);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= driverpay." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			criteria.setPageSize(25);
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/save.do")
	public String save(ModelMap model, HttpServletRequest request,	HttpServletResponse response) {
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			
			
			boolean noRatesFound = false;
			String payrollDate=(String) criteria.getSearchMap().get("payrollDate");
			String driverid=(String) criteria.getSearchMap().get("driver");
			String frombatch=(String)criteria.getSearchMap().get("fromDate");
			String tobatch=(String) criteria.getSearchMap().get("toDate");
			String company=(String) criteria.getSearchMap().get("company");
			String terminal=(String) criteria.getSearchMap().get("terminal");
			String expire1= (String) criteria.getSearchMap().get("expire");
			String status=(String)criteria.getSearchMap().get("pay");
			String sta=(String)criteria.getSearchMap().get("stat");
			String driversmul1=(String)criteria.getSearchMap().get("driversmul");
			 if(!StringUtils.isEmpty(status)){
				 status="1";
			 }else{
				 status="2";
			 }
			 status="1";
			frombatch=ReportDateUtil.getFromDate(frombatch);
			tobatch=ReportDateUtil.getFromDate(tobatch);		
			
			payrollDate=ReportDateUtil.getFromDate(payrollDate);
			 
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
				// drivercriteria.put("status",1);
				 drivercriteria.put("company.id",companylocation.getId());
				 List<Driver> drivers = genericDAO.findByCriteria(Driver.class, drivercriteria);
				 for(Driver driverObj:drivers){
					 if(driverIds.equals(""))
						 driverIds = driverObj.getId().toString();
					else
						driverIds = driverIds +","+driverObj.getId().toString();
				 }
			}
		   
		   
		   
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
	        if(!StringUtils.isEmpty(driversmul1)){
	        	query.append(" and obj.driver not in ("+driversmul1+")");
	        }
	        
	        
	        query.append(" group by obj.origin,obj.destination");
	        if(StringUtils.isEmpty(driverid)){
	        	query.append(",obj.driver.fullName");
	        }
	        query.append(" order by obj.driver.fullName asc, obj.origin.name ,obj.destination.name asc");
	        System.out.println("\n query-->"+query);
			
	        List<String> listOfRecordWithoutRate = new ArrayList<String>();
	        
	        //List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString());
		   
	        //listOfRecordWithoutRate.add("<u>Rates are not available between following Driver: Origin - Destination :</u> <br/>");
	        
			//for(Ticket ticket:tickets){}
			
			
			
			criteria.getSearchMap().put("summary","false");
			Map<String,Object> datas = generateData(criteria, request);
			List<DriverPayFreezWrapper> driverPayFreezObjList = (List<DriverPayFreezWrapper>)datas.get("driverfreez");
			for(DriverPayFreezWrapper driverPayFreezWrapperObj:driverPayFreezObjList){
				genericDAO.saveOrUpdate(driverPayFreezWrapperObj);
			}
			
			criteria.getSearchMap().put("summary","true");
			hrReportService.saveDriverPayData(request, criteria);
			request.getSession().setAttribute("msg", "Driver Pay is saved successfully");
		    
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
		return "blank/blank";
	}
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
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
		
		if("findDterminal".equalsIgnoreCase(action)){
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
		return "";
	}
	public List<String> list = new ArrayList<String>();

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
}
