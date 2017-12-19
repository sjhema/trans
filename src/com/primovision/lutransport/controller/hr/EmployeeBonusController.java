package com.primovision.lutransport.controller.hr;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.DateUtil;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.hr.BonusType;
import com.primovision.lutransport.model.hr.Eligibility;
import com.primovision.lutransport.model.hr.EmpBonusTypesList;
//import com.primovision.lutransport.model.hr.Driver;
import com.primovision.lutransport.model.hr.EmployeeBonus;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.Ptodapplication;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayroll;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/empbonus")
public class EmployeeBonusController extends CRUDController<EmployeeBonus> {
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
	public EmployeeBonusController() {
	
	setUrlContext("/hr/empbonus");
	}
	
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
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession()
		.getAttribute("searchCriteria");
		
		Map criterias = new HashMap();
		criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
		criterias.clear();
		model.addAttribute("categories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		model.addAttribute("bonustypes", genericDAO.findByCriteria(BonusType.class, criterias,"typename",false));
		model.addAttribute("reffrals", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
		
	}
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		setupList(model, request);
		String rst=(String)request.getParameter("rst");
		if(!StringUtils.isEmpty(rst)){
			 request.getSession().removeAttribute("nextbonuses");
		}
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		List<EmployeeBonus> list=genericDAO.search(getEntityClass(), criteria);
		for(EmployeeBonus bonus:list){
			StringBuffer buffer=new StringBuffer("");
			double amount=0.0;
			for(EmpBonusTypesList bonusTypesList:bonus.getBonusTypesLists()){
				buffer.append(bonusTypesList.getBonusType().getTypename());
				buffer.append(",");
				amount+=bonusTypesList.getBonusamount();
			}
			if(buffer.length()>0){
				int i=buffer.lastIndexOf(",");
				buffer.deleteCharAt(i);
			}
			bonus.setAmounts(amount);
			bonus.setBonustypes(buffer.toString());
		}
		model.addAttribute("list",list);
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		List<EmployeeBonus> list=genericDAO.search(getEntityClass(), criteria);
		for(EmployeeBonus bonus:list){
			StringBuffer buffer=new StringBuffer("");
			double amount=0.0;
			for(EmpBonusTypesList bonusTypesList:bonus.getBonusTypesLists()){
				buffer.append(bonusTypesList.getBonusType().getTypename());
				buffer.append(",");
				amount+=bonusTypesList.getBonusamount();
			}
			if(buffer.length()>0){
				int i=buffer.lastIndexOf(",");
				buffer.deleteCharAt(i);
			}
			bonus.setAmounts(amount);
			bonus.setBonustypes(buffer.toString());
		}
		model.addAttribute("list",list);
		return urlContext + "/list";
	}
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		 return "redirect:start.do";
		//	return super.create(model, request);
	}
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		String mode = request.getParameter("mode");
		if (StringUtils.equals("REVERT", mode)) {
			String revertMsg = processRevert(model, request);
		}
		
		String id=request.getParameter("id");
		 return "redirect:edit1.do?id="+id;
	}
	
	private String processRevert(ModelMap model, HttpServletRequest request) {
		EmployeeBonus empBonus = (EmployeeBonus) model.get("modelObject");
		if (empBonus.getPayRollBatch() == null || empBonus.getPayRollStatus() == 1) {
			return StringUtils.EMPTY;
		}
		
		Driver driver = empBonus.getDriver();
		String payTerm = driver.getPayTerm();
		if (StringUtils.equals("1", payTerm)) {
			return processRevertForDriver(request, empBonus);
		} else if (StringUtils.equals("2", payTerm)) {
			return processRevertForHourly(request, empBonus);
		} else if (StringUtils.equals("3", payTerm)) {
			return processRevertForSalary(request, empBonus);
		} else {
			return "Payroll Revert not supported for specified employee type";
		}
	}
	
	private String processRevertForHourly(HttpServletRequest request, EmployeeBonus empBonus) {
		return "Payroll Reverted successfully";
	}
	
	private String processRevertForSalary(HttpServletRequest request, EmployeeBonus empBonus) {
		return "Payroll Reverted successfully";
	}
	
	private String processRevertForDriver(HttpServletRequest request, EmployeeBonus empBonusObj) {
		Driver driver = empBonusObj.getDriver();
		String driverFullName = driver.getFullName();

		String checkDate = sdf.format(empBonusObj.getPayRollBatch());
		//String batchDateFrom = sdf.format(miscAmtObj.getBatchFrom());
		String batchDateTo = sdf.format(empBonusObj.getBatchTo());
		
		String company = String.valueOf(empBonusObj.getCompany().getId());
		String terminal = String.valueOf(empBonusObj.getTerminal().getId());
		
		StringBuilder driverPayQuery = new StringBuilder("select obj from DriverPay obj where 1=1");
		if (StringUtils.isNotEmpty(driverFullName)){
			driverPayQuery.append(" and drivername='").append(driverFullName).append("'");
		}
		if (StringUtils.isNotEmpty(company)){
			driverPayQuery.append(" and company='").append(company).append("'");
		}
		if (StringUtils.isNotEmpty(terminal)){
			driverPayQuery.append(" and terminal='").append(terminal).append("'");
		}
		/*if (StringUtils.isNotEmpty(batchDateFrom)){				
			batchDateFrom = ReportDateUtil.getFromDate(batchDateFrom);
			driverPayQuery.append(" and billBatchDateFrom >='").append(batchDateFrom).append("'");
		}*/
		if (StringUtils.isNotEmpty(batchDateTo)){				
			batchDateTo = ReportDateUtil.getFromDate(batchDateTo);
			driverPayQuery.append(" and billBatchDateTo ='").append(batchDateTo).append("'");
		}
		if (StringUtils.isNotEmpty(checkDate)){				
			checkDate = ReportDateUtil.getFromDate(checkDate);
			driverPayQuery.append(" and payRollBatch ='").append(checkDate).append("'");
		}
		
		List<DriverPay> driverPayList = genericDAO.executeSimpleQuery(driverPayQuery.toString());
		if (driverPayList == null || driverPayList.isEmpty()) {
			return "Driver pay not found for selected criteria";
		}
		DriverPay driverPay = driverPayList.get(0);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("company", driverPay.getCompany());
		criterias.put("payRollBatch", driverPay.getPayRollBatch());
		criterias.put("billBatchFrom", driverPay.getBillBatchDateFrom());
		criterias.put("billBatchTo", driverPay.getBillBatchDateTo());
		if (driverPay.getTerminal() != null) {
			criterias.put("terminal", driverPay.getTerminal());
		}
		
		List<DriverPayroll> driverPayrollList = genericDAO.findByCriteria(DriverPayroll.class, criterias);
		if (driverPayrollList == null || driverPayrollList.isEmpty()) {
			return "Driver pay not found for selected criteria";
		}
		DriverPayroll driverPayroll = driverPayrollList.get(0);
		
		criterias.clear();
		criterias.put("drivername", driverPay.getDrivername());
		criterias.put("company", driverPay.getCompany());
		criterias.put("payRollBatch", driverPay.getPayRollBatch());
		criterias.put("billBatchDateFrom", driverPay.getBillBatchDateFrom());
		criterias.put("billBatchDateTo", driverPay.getBillBatchDateTo());
		if (driverPay.getTerminal() != null) {
			criterias.put("terminal", driverPay.getTerminal());
		}
		List<DriverPayFreezWrapper> driverPayFreezeWrapperList = genericDAO.findByCriteria(DriverPayFreezWrapper.class, criterias);
		if (driverPayFreezeWrapperList == null || driverPayFreezeWrapperList.isEmpty()) {
			return "Driver pay not found for selected criteria";
		}
		DriverPayFreezWrapper driverPayFreezWrapper = driverPayFreezeWrapperList.get(0);
		
		revert(empBonusObj, request);
		
		Double bonusAmt = empBonusObj.getBonustype().getAmount();
		driverPayroll.setSumAmount(driverPayroll.getSumAmount() - bonusAmt);
		
		driverPay.setBonusAmount(driverPay.getBonusAmount() - bonusAmt);
		driverPay.setTotalAmount(driverPay.getTotalAmount() - bonusAmt);
			
		if (driverPayFreezWrapper.getBonusAmount() != null
				&& driverPayFreezWrapper.getBonusAmount() != 0.0) {
			driverPayFreezWrapper.setBonusAmount(driverPayFreezWrapper.getBonusAmount() - bonusAmt);
		}
		if (driverPayFreezWrapper.getTotalAmount() != null
				&& driverPayFreezWrapper.getTotalAmount() != 0.0) {
			driverPayFreezWrapper.setTotalAmount(driverPayFreezWrapper.getTotalAmount() - bonusAmt);
		}
		
		driverPay.setModifiedBy(getUser(request).getId());
		driverPay.setModifiedAt(Calendar.getInstance().getTime());
		genericDAO.saveOrUpdate(driverPay);
		
		driverPayroll.setModifiedBy(getUser(request).getId());
		driverPayroll.setModifiedAt(Calendar.getInstance().getTime());
		genericDAO.saveOrUpdate(driverPayroll);
		
		driverPayFreezWrapper.setModifiedBy(getUser(request).getId());
		driverPayFreezWrapper.setModifiedAt(Calendar.getInstance().getTime());
		genericDAO.saveOrUpdate(driverPayFreezWrapper);
		
		return "Payroll Reverted successfully";
	}
	
	private void revert(EmployeeBonus empBonus, HttpServletRequest request) {
		empBonus.setPayRollStatus(1);
		empBonus.setPayRollBatch(null);
		
		empBonus.setModifiedBy(getUser(request).getId());
		empBonus.setModifiedAt(Calendar.getInstance().getTime());
		genericDAO.saveOrUpdate(empBonus);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/edit1.do")
	public String edit3(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		setupCreate(model, request);
		EmployeeBonus bonus=genericDAO.getById(EmployeeBonus.class, Long.parseLong(id));
		 SearchCriteria searchCriteria =new SearchCriteria();
		  DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		 String dfrom = formatter.format(bonus.getBatchFrom());
		 String dto = formatter.format(bonus.getBatchTo());
		 searchCriteria.getSearchMap().put("fromDate",dfrom);
		 searchCriteria.getSearchMap().put("toDate",dto);	
		 searchCriteria.getSearchMap().put("stat","1");
		 searchCriteria.getSearchMap().put("driver",bonus.getDriver().getFullName()+"");
		 searchCriteria.getSearchMap().put("summary","false");
		 model.addAttribute("modelObject", bonus);
		 model.addAttribute("fromEditMethod","yes");
		 model.addAttribute("bonustypese",bonus.getBonusTypesLists());
		 model.addAttribute("bon", true);
		 
		 DriverPayWrapper wrapper = null;
		 try {
			wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		} catch (Exception e) {
			// TODO: handle exception
			request.getSession().setAttribute("error", bonus.getDriver().getFullName()+" driver not found");
			
		}
		
		dfrom=ReportDateUtil.getFromDate(dfrom);
		dto=ReportDateUtil.getFromDate(dto);
		 Double sickParsonalAmount=0.00;
			Double vacationAmount=0.00;
			List<Double> parameter1=new ArrayList<Double>();
	           List<Double> parameter2=new ArrayList<Double>();
			StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+bonus.getDriver().getId()+" and obj.category=2");
			if(!StringUtils.isEmpty(dfrom)){
			ptodquery.append(" and obj.batchdate>='"+dfrom+"'");
			}
			if(!StringUtils.isEmpty(dto)){
				ptodquery.append(" and obj.batchdate<='"+dto+"'");
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
			parameter1.add(sickParsonalAmount);
			parameter2.add(vacationAmount);
			sickParsonalAmount=0.00;
				vacationAmount=0.00;
		 Map<String,Object> datas = new HashMap<String,Object>();
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("totalRowCount", wrapper.getTotalRowCount());
			params.put("driver", wrapper.getDriver());
			params.put("company", wrapper.getCompany());
			params.put("batchDateFrom", wrapper.getBatchDateFrom());
			params.put("batchDateTo",wrapper.getBatchDateTo());
			params.put("sumTotal",wrapper.getSumTotal());
			params.put("payRollBatch", wrapper.getPayRollBatch());
			datas.put("data", wrapper.getDriverPays());
			params.put("sumAmount", wrapper.getSumAmount());
			 params.put("parameter1", parameter1);
	           params.put("parameter2", parameter2);
			datas.put("params", params);
			String type = "html";
			String  report="driverpaybonus";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "driverpay" + "." + type);
			}
			try{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(report,
						(List)datas.get("data"), params, request);
				request.setAttribute("jasperPrint", jasperPrint);
				
			} catch (Exception e) {
				e.printStackTrace();
				request.getSession().setAttribute("errors", e.getMessage());
				return "error";
			}
			
		
			return urlContext+"/bonusformedit";
		//
		 
		 
	}
	
	/*@RequestMapping(method = RequestMethod.POST, value = "/updatebonus.do")
	public String updateBonusList(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		
		String[] bonusListsIDs = request.getParameterValues("bonuslistId");
		String[] bonusAmts =  request.getParameterValues("bonusAmt");
		String[] bonusNtes =  request.getParameterValues("bonusNtes");
		
		for(int i=0;i<bonusListsIDs.length;i++){
			EmpBonusTypesList empBonusListObj = genericDAO.getById(EmpBonusTypesList.class, Long.parseLong(bonusListsIDs[i]));
			empBonusListObj.setBonusamount(Double.parseDouble(bonusAmts[i]));
			empBonusListObj.setNote(bonusNtes[i]);			
			genericDAO.saveOrUpdate(empBonusListObj);
		}
		
		return "redirect:list.do";
	}*/
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/updatebonus.do")
	public String updateBonusList(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] bonusListsIDs = request.getParameterValues("bonuslistId");
		String[] bonusAmts =  request.getParameterValues("bonusAmt");
		String[] bonusNtes =  request.getParameterValues("bonusNtes");

		double amt = 0.0;
		String bType = "";
	
		
		if(bonusAmts.length == bonusListsIDs.length){
			for(int i=0;i<bonusAmts.length;i++){
				EmpBonusTypesList empBonusListObj = genericDAO.getById(EmpBonusTypesList.class, Long.parseLong(bonusListsIDs[i]));
				amt+=Double.parseDouble(bonusAmts[i]);
				bType+=empBonusListObj.getBonusType();
			}
			
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			
			for (int i = 0; i < bonusListsIDs.length; i++) {
				EmpBonusTypesList empBonusListObj = genericDAO.getById(EmpBonusTypesList.class, Long.parseLong(bonusListsIDs[i]));
				String batchdateFrom=dateFormat1.format(empBonusListObj.getBonus().getBatchFrom());
				String batchdateTo=dateFormat1.format(empBonusListObj.getBonus().getBatchTo());
				StringBuilder checkForDuplicateQuery = new StringBuilder("SELECT obj FROM EmployeeBonus obj WHERE obj.company ="+empBonusListObj.getBonus().getCompany().getId()+" AND obj.terminal="+empBonusListObj.getBonus().getTerminal().getId()+" AND obj.batchFrom='"+batchdateFrom+"' AND obj.batchTo='"+batchdateTo+"' AND obj.driver="+empBonusListObj.getBonus().getDriver().getId());	
					checkForDuplicateQuery.append(" and obj.id !="+empBonusListObj.getBonus().getId());

				System.out.println("???"+checkForDuplicateQuery.toString());
				List<EmployeeBonus> list = genericDAO.executeSimpleQuery(checkForDuplicateQuery.toString());
									
				if(list.size()>0 && list!=null){
					for (EmployeeBonus employeeBonus : list) {
						List<EmpBonusTypesList> ebTypeList = genericDAO.executeSimpleQuery("select obj from EmpBonusTypesList obj where obj.bonus="+employeeBonus.getId());
						if (ebTypeList.size()>0 && ebTypeList!=null) {
							double empBTLamt=0.0;
							String empBTLType = "";
							for (EmpBonusTypesList empBonusTypesList : ebTypeList) {
								empBTLamt+=empBonusTypesList.getBonusamount();
								empBTLType+=empBonusTypesList.getBonusType();
							}				
							System.out.println("\n\n\n->"+empBTLamt+" ==== ");
							System.out.println(amt+" ==== ");
							if(empBTLamt == amt && StringUtils.equalsIgnoreCase(empBTLType,bType)){
								setupCreate(model, request);
								request.getSession().setAttribute("error",
								"Duplicate Entry.");
								return urlContext + "/bonusformedit";
							}
						}	
					}
				}
			}
		}
		
		for(int i=0;i<bonusListsIDs.length;i++){
			EmpBonusTypesList empBonusListObj = genericDAO.getById(EmpBonusTypesList.class, Long.parseLong(bonusListsIDs[i]));
			empBonusListObj.setBonusamount(Double.parseDouble(bonusAmts[i]));
			empBonusListObj.setNote(bonusNtes[i]);			
			genericDAO.saveOrUpdate(empBonusListObj);
		}

		return "redirect:list.do";
	}
	
	
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		//setupCreate(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
		.getAttribute("searchCriteria");
		
		Map criterias = new HashMap();
		/*criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));*/
		String query="select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		criterias.clear();
		model.addAttribute("categories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		model.addAttribute("bonustypes", genericDAO.findByCriteria(BonusType.class, criterias,"typename",false));
		model.addAttribute("reffrals", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
		if(criteria!=null){  
			criteria.getSearchMap().remove("summary");
		criteria.getSearchMap().remove("fromDate");
		criteria.getSearchMap().remove("driver");
		criteria.getSearchMap().remove("toDate");
		criteria.getSearchMap().remove("stat");
			request.getSession().setAttribute("searchCriteria", criteria);
			}
	}
	@RequestMapping(method = RequestMethod.GET, value = "/start.do")
	public String Start(ModelMap model,HttpServletRequest request){
		Map criterias = new HashMap();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class,criterias,"fullName",false));

		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class,criterias,"name",false));
		return urlContext+"/start";
	}
	@RequestMapping(method=RequestMethod.POST, value="/next.do")
	public String next(ModelMap model,HttpServletRequest request){
		  populateSearchCriteria(request, request.getParameterMap());
		   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		   request.getSession().setAttribute("searchCriteria", searchCriteria);
		  String company=(String) searchCriteria.getSearchMap().get("company");
		  String batchFrom=(String)searchCriteria.getSearchMap().get("fromDate");
		  String batchTo=(String)searchCriteria.getSearchMap().get("toDate");
		  String employeeid=(String)searchCriteria.getSearchMap().get("driver");
		  String terminal=(String)searchCriteria.getSearchMap().get("terminal");
		  
		  Map criterias = new HashMap();
		  criterias.clear();
		  
			model.addAttribute("bonustypes", genericDAO.findByCriteria(BonusType.class, criterias,"typename",false));
		  
		  
		  int i=0;
		 /* if(StringUtils.isEmpty(company)){
			  request.getSession().setAttribute("error","Please select company");
			  return "redirect:start.do";
		  }*/
		  if(StringUtils.isEmpty(batchFrom)){
			  request.getSession().setAttribute("error","Please Enter Batch Date from");
			  return "redirect:start.do";
		  }
		  if(StringUtils.isEmpty(batchTo)){
			  request.getSession().setAttribute("error","Please Enter Batch Date to");
			  return "redirect:start.do";
		  }
		 /* String batchFrom=(String)searchCriteria.getSearchMap().get("fromDate");
		  String batchTo=(String)searchCriteria.getSearchMap().get("toDate");*/
		  StringBuffer query=new StringBuffer("");
		  query.append("select obj from Driver obj where obj.status=1");
		  if(!StringUtils.isEmpty(company)){
			  query.append(" and  obj.company="+company);	 
			  criterias.put("company",company);
		  }else{
			  i++;
		  }
		 if(!StringUtils.isEmpty(employeeid)){
				query.append("and  obj.fullName='"+employeeid+"'");	  
		  }else{
			  i++;
		  }
		 if(!StringUtils.isEmpty(terminal)){
			 query.append("and  obj.terminal="+terminal);
		 }else{
			  i++;
		  }
		 if(i==3){
			 request.getSession().setAttribute("error","Please Select any one option");
			  return "redirect:start.do";
		 }
		 query.append("order by obj.fullName asc");
		  //String query="select obj from Driver obj where obj.company="+company+" and obj.catagory=2 order by obj.fullName asc";
		 
		 List<Driver>employees= genericDAO.executeSimpleQuery(query.toString());
		 if(employees.isEmpty()||employees.size()<0){
			 request.getSession().setAttribute("error","Record Not Found");
			  return "redirect:start.do"; 
		 }
		 List<String> id=new ArrayList<String>();
		 List<String> id1=new ArrayList<String>();
		 
		 for(Driver driver:employees){
			 id.add(driver.getFullName()+"");
		 }
		 request.getSession().setAttribute("empid1", id1);
		   request.getSession().setAttribute("empid", id);
		   return "redirect:bonusnext.do";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/bonuscontinue.do")
	public String bonusContinue(HttpServletRequest request,HttpServletResponse response,
			 ModelMap model){

		
		EmployeeBonus entity = null;
		
		List<String> id=(List<String>) request.getSession().getAttribute("empid");
		List<String> id1=(List<String>) request.getSession().getAttribute("empid1");
		
		String idd=(String)request.getParameter("id");
		
		
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
        if(!StringUtils.isEmpty(idd)){
        	
        	Map map =new HashMap();
			map=(Map)request.getSession().getAttribute("bonusmap");			
			searchCriteria.getSearchMap().put("fromDate", map.get("batchfrom"));
			searchCriteria.getSearchMap().put("toDate", map.get("batchto"));
			
		}
		/*}else{
			System.out.println("\n inside else--");
			searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria11");
		}*/
		 String batchFrom=(String)searchCriteria.getSearchMap().get("fromDate");
		 String batchTo=(String)searchCriteria.getSearchMap().get("toDate");
		  try {
			  
			  id.get(0);
		} catch (Exception e) {
			id.add(0, id1.get(0));
			id1.remove(0);
			System.out.println("\n list over-->"+id.get(0));
			model.addAttribute("listEnd", true);
			//request.getSession().setAttribute("error","List Is Over1");
		}
		System.out.println("\n here near id-->"+id.get(0));
		Map criti =new HashMap();
		criti.put("fullName",id.get(0));
		Driver driver=genericDAO.getByCriteria(Driver.class, criti);
		EmployeeBonus bonus=(EmployeeBonus) request.getSession().getAttribute("ebonus");
		// EmployeeBonus modelObject;
		 if(bonus==null){
			 System.out.println("\n inside if");
			 entity = new EmployeeBonus();
			 entity.setCategory(driver.getCatagory());
			 entity.setDriver(driver);
			 if(driver.getDateHired()!=null){
			 entity.setDateHired(driver.getDateHired());
			 }else{
				 entity.setDateHired(driver.getDateReHired());
			 }
			 entity.setCompany(driver.getCompany());
			 entity.setTerminal(driver.getTerminal());
		 Date dateHired;
		 if(driver.getDateHired()!=null){
			 
			 dateHired=driver.getDateHired();
		 }else{
			 dateHired=driver.getDateReHired();
		 }
			Calendar currentDate = Calendar.getInstance();
				int d=DateUtil.daysBetween(dateHired, currentDate.getTime());
			  int year	=(d /365);
			  double days=(d %365);
			  
				 double month=(days/30);
				int finmonths=(int)MathUtil.roundUp(month, 0);
				StringBuffer exp=new StringBuffer("");
				if(finmonths>11){
					entity.setExperience((year+1)+".0");
				}else{
					entity.setExperience(year+"."+finmonths);
				}
				
				DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
				try {
					Date dfrom = (Date)formatter.parse(batchFrom);
					entity.setBatchFrom(dfrom);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Date dto = (Date)formatter.parse(batchTo);
					entity.setBatchTo(dto);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 }else{
			 System.out.println("\n inside save");
			 request.getSession().removeAttribute("ebonus");
			 entity= bonus;
			 
			/*	if(entity.getRefferal()==null){
					bindingResult.rejectValue("refferal", "error.select.option",null, null);
				}*/
				if(entity.getDriver()!=null){
					entity.setEmpnumber(entity.getDriver().getStaffId());
				}
				
				
				
				
				//return super.save(request, entity, bindingResult, model);
				try {
					
				} catch (ValidationException e) {
					e.printStackTrace();
					log.warn("Error in validation :" + e);
				}
		 }
		 model.addAttribute("modelObject", entity);
		 setupCreate(model, request);
		 searchCriteria.getSearchMap().put("stat","1");
		 searchCriteria.getSearchMap().put("driver",id.get(0));
		 searchCriteria.getSearchMap().put("summary","false");
		 DriverPayWrapper wrapper = null;
		 try {
			wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		} catch (Exception e) {
			// TODO: handle exception
			request.getSession().setAttribute("error", entity.getDriver().getFullName()+" driver not found");
			 id1.add(0, id.get(0));
			 id.remove(0);
			 request.getSession().setAttribute("empid", id);
			 request.getSession().setAttribute("empid1", id1);
			return "redirect:bonusnext.do";
		}
		// DriverPayWrapper wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		 id1.add(0, id.get(0));
		 id.remove(0);
		 request.getSession().setAttribute("empid", id);
		 request.getSession().setAttribute("empid1", id1);
		 System.out.println("\n sumamount-->"+wrapper.getSumAmount());
		 batchFrom=ReportDateUtil.getFromDate(batchFrom);
		 batchTo=ReportDateUtil.getFromDate(batchTo);
		 Double sickParsonalAmount=0.00;
			Double vacationAmount=0.00;
			List<Double> parameter1=new ArrayList<Double>();
	           List<Double> parameter2=new ArrayList<Double>();
			StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+driver.getId()+" and obj.category=2");
			if(!StringUtils.isEmpty(batchFrom)){
			ptodquery.append(" and obj.batchdate>='"+batchFrom+"'");
			}
			if(!StringUtils.isEmpty(batchTo)){
				ptodquery.append(" and obj.batchdate<='"+batchTo+"'");
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
			parameter1.add(sickParsonalAmount);
			parameter2.add(vacationAmount);
			sickParsonalAmount=0.00;
				vacationAmount=0.00;
		 Map<String,Object> datas = new HashMap<String,Object>();
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("totalRowCount", wrapper.getTotalRowCount());
			params.put("driver", wrapper.getDriver());
			params.put("company", wrapper.getCompany());
			params.put("batchDateFrom", wrapper.getBatchDateFrom());
			params.put("batchDateTo",wrapper.getBatchDateTo());
			params.put("sumTotal",wrapper.getSumTotal());
			params.put("payRollBatch", wrapper.getPayRollBatch());
			datas.put("data", wrapper.getDriverPays());
			params.put("sumAmount", wrapper.getSumAmount());
			 params.put("parameter1", parameter1);
	           params.put("parameter2", parameter2);
			datas.put("params", params);
			String type = "html";
			String  report="driverpaybonus";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "driverpay" + "." + type);
			}
			try{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(report,
						(List)datas.get("data"), params, request);
				request.setAttribute("jasperPrint", jasperPrint);
				
			} catch (Exception e) {
				e.printStackTrace();
				request.getSession().setAttribute("errors", e.getMessage());
				return "error";
			}
			
		
			return urlContext+"/bonusform";
	
		
		
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/addEmpBonus.do")
	public String addEmpBonus(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
	    SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		
		if(StringUtils.isEmpty(request.getParameter("fromDate"))){
			  request.getSession().setAttribute("error","Please Enter Batch Date From");
			  return "redirect:start.do";
		  }
		  if(StringUtils.isEmpty(request.getParameter("toDate"))){
			  request.getSession().setAttribute("error","Please Enter Batch Date To");
			  return "redirect:start.do";
		  }
		  
		  if(StringUtils.isEmpty(request.getParameter("company"))){
			  request.getSession().setAttribute("error","Please Select Company");
			  return "redirect:start.do";
		  }
		
		
		System.out.println("***** "+request.getParameter("company"));
		System.out.println("***** "+request.getParameter("terminal"));
		System.out.println("***** "+request.getParameter("driver"));
		System.out.println("***** "+request.getParameter("fromDate"));
		System.out.println("***** "+request.getParameter("toDate"));
		
		
		
		StringBuffer empQuery = new StringBuffer("Select obj from Driver obj where obj.status=1 and obj.company="+request.getParameter("company"));
		if(request.getParameter("terminal")!=null){
			if(!StringUtils.isEmpty(request.getParameter("terminal")))
				empQuery.append(" and obj.terminal=").append(request.getParameter("terminal"));
		}
		
		empQuery.append(" order by obj.company,obj.fullName");
		
		List<Driver> drivers = genericDAO.executeSimpleQuery(empQuery.toString());
		
		if(drivers!=null && drivers.size()>0){
		
		if(!StringUtils.isEmpty(request.getParameter("driver"))){
				boolean found = false;		
				String driverName =  request.getParameter("driver");
				
				for (Iterator<Driver> iterator = drivers.iterator(); iterator.hasNext(); ) {
					  Driver drvObj = iterator.next();
					  if(!driverName.equalsIgnoreCase(drvObj.getFullName())){
					    iterator.remove();
					  }
					  else{
							found = true;
							break;
						}
				}

				if(!found){
					request.getSession().setAttribute("error","No Records found for given input");
					return "redirect:start.do";
				}
		}			
		
		
		request.getSession().setAttribute("driverList",drivers);
		request.getSession().setAttribute("remainingDriverscount",drivers.size());
		request.getSession().setAttribute("driverListIndexCount",0);
		request.getSession().setAttribute("loadbatchFom",request.getParameter("fromDate"));		
		request.getSession().setAttribute("loadbatchT",request.getParameter("toDate"));
		
		
		EmployeeBonus entity = new EmployeeBonus();

		 entity.setCategory(drivers.get(0).getCatagory());
		 entity.setDriver(drivers.get(0));
		 if(drivers.get(0).getDateHired()!=null){
			 entity.setDateHired(drivers.get(0).getDateHired());
		 }else{
			 entity.setDateHired(drivers.get(0).getDateReHired());
		 }
		 entity.setCompany(drivers.get(0).getCompany());
		 entity.setTerminal(drivers.get(0).getTerminal());
		 Date dateHired;
		 if(drivers.get(0).getDateHired()!=null){		 
			 dateHired=drivers.get(0).getDateHired();
		 }else{
			 dateHired=drivers.get(0).getDateReHired();
		 }
		 Calendar currentDate = Calendar.getInstance();
		 int d=DateUtil.daysBetween(dateHired, currentDate.getTime());
		 int year	=(d /365);
		 double days=(d %365);		  
		 double month=(days/30);
		 int finmonths=(int)MathUtil.roundUp(month, 0);
		 StringBuffer exp=new StringBuffer("");
		 if(finmonths>11){
			entity.setExperience((year+1)+".0");
		 }else{
			entity.setExperience(year+"."+finmonths);
		 }
		 DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		 try {
			Date dfrom = (Date)formatter.parse(request.getParameter("fromDate"));
			entity.setBatchFrom(dfrom);
		 } catch (ParseException e1) {				
			e1.printStackTrace();
		 }
		 try {
			Date dto = (Date)formatter.parse(request.getParameter("toDate"));
			entity.setBatchTo(dto);
		 } catch (ParseException e1) {				
			e1.printStackTrace();
		 }
	 
		 model.addAttribute("modelObject", entity);
		 setupCreate(model, request);
		 searchCriteria.getSearchMap().put("stat","1");
		 searchCriteria.getSearchMap().put("driver",drivers.get(0).getFullName());
		 searchCriteria.getSearchMap().put("summary","false");
		 searchCriteria.getSearchMap().put("company",request.getParameter("company"));
		 searchCriteria.getSearchMap().put("terminal",request.getParameter("terminal"));
		// searchCriteria.getSearchMap().put("driver",request.getParameter("driver"));
		 searchCriteria.getSearchMap().put("fromDate",request.getParameter("fromDate"));
		 searchCriteria.getSearchMap().put("toDate",request.getParameter("toDate"));
		 
		 DriverPayWrapper wrapper = null;
		 
		 try {
			wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		} catch (Exception e) {
			
			
		}
		// DriverPayWrapper wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		 
		 System.out.println("\n sumamount-->"+wrapper.getSumAmount());
		 String batchFrom=ReportDateUtil.getFromDate(request.getParameter("fromDate"));
		 String batchTo=ReportDateUtil.getFromDate(request.getParameter("toDate"));
		 Double sickParsonalAmount=0.00;
			Double vacationAmount=0.00;
			List<Double> parameter1=new ArrayList<Double>();
	           List<Double> parameter2=new ArrayList<Double>();
			StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+drivers.get(0).getId()+" and obj.category=2");
			if(!StringUtils.isEmpty(batchFrom)){
			ptodquery.append(" and obj.batchdate>='"+batchFrom+"'");
			}
			if(!StringUtils.isEmpty(batchTo)){
				ptodquery.append(" and obj.batchdate<='"+batchTo+"'");
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
			parameter1.add(sickParsonalAmount);
			parameter2.add(vacationAmount);
			sickParsonalAmount=0.00;
				vacationAmount=0.00;
		 Map<String,Object> datas = new HashMap<String,Object>();
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("totalRowCount", wrapper.getTotalRowCount());
			params.put("driver", wrapper.getDriver());
			params.put("company", wrapper.getCompany());
			params.put("batchDateFrom", wrapper.getBatchDateFrom());
			params.put("batchDateTo",wrapper.getBatchDateTo());
			params.put("sumTotal",wrapper.getSumTotal());
			params.put("payRollBatch", wrapper.getPayRollBatch());
			datas.put("data", wrapper.getDriverPays());
			params.put("sumAmount", wrapper.getSumAmount());
			 params.put("parameter1", parameter1);
	           params.put("parameter2", parameter2);
			datas.put("params", params);
			String type = "html";
			String  report="driverpaybonus";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "driverpay" + "." + type);
			}
			try{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(report,
						(List)datas.get("data"), params, request);
				request.setAttribute("jasperPrint", jasperPrint);
				
			} catch (Exception e) {
				e.printStackTrace();
				request.getSession().setAttribute("errors", e.getMessage());
				return "error";
			}
			
		
			return urlContext+"/bonusform";	
		}
		else{
			request.getSession().setAttribute("error","No Records found for given input");
			return "redirect:start.do";
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/addEmpBonusNext.do")
	public String moveToNextEmployee(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
		
		Integer remainingDrivercount = (Integer) request.getSession().getAttribute("remainingDriverscount");
		
		List<Driver> drivers = (List<Driver>) request.getSession().getAttribute("driverList");
		
		//int driverListIndex = Integer.parseInt(listIndex);
		
		int currentListIndex = driverListIndex+1;
		
		
		if(currentListIndex == remainingDrivercount){
			currentListIndex=currentListIndex-1;
			request.getSession().setAttribute("error","End of the List.");
		}
		
		if(drivers!=null && drivers.size()>0){		
			
			request.getSession().setAttribute("driverListIndexCount",currentListIndex);
			
			
			
			EmployeeBonus entity = new EmployeeBonus();

			 entity.setCategory(drivers.get(currentListIndex).getCatagory());
			 entity.setDriver(drivers.get(currentListIndex));
			 if(drivers.get(currentListIndex).getDateHired()!=null){
				 entity.setDateHired(drivers.get(currentListIndex).getDateHired());
			 }else{
				 entity.setDateHired(drivers.get(currentListIndex).getDateReHired());
			 }
			 entity.setCompany(drivers.get(currentListIndex).getCompany());
			 entity.setTerminal(drivers.get(currentListIndex).getTerminal());
			 Date dateHired;
			 if(drivers.get(currentListIndex).getDateHired()!=null){		 
				 dateHired=drivers.get(currentListIndex).getDateHired();
			 }else{
				 dateHired=drivers.get(currentListIndex).getDateReHired();
			 }
			 Calendar currentDate = Calendar.getInstance();
			 int d=DateUtil.daysBetween(dateHired, currentDate.getTime());
			 int year	=(d /365);
			 double days=(d %365);		  
			 double month=(days/30);
			 int finmonths=(int)MathUtil.roundUp(month, 0);
			 StringBuffer exp=new StringBuffer("");
			 if(finmonths>11){
				entity.setExperience((year+1)+".0");
			 }else{
				entity.setExperience(year+"."+finmonths);
			 }
			 DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			 try {
				Date dfrom = (Date)formatter.parse((String)request.getSession().getAttribute("loadbatchFom"));
				entity.setBatchFrom(dfrom);
			 } catch (ParseException e1) {				
				e1.printStackTrace();
			 }
			 try {
				Date dto = (Date)formatter.parse((String)request.getSession().getAttribute("loadbatchT"));
				entity.setBatchTo(dto);
			 } catch (ParseException e1) {				
				e1.printStackTrace();
			 }
		 
			 model.addAttribute("modelObject", entity);
			 setupCreate(model, request);
			 searchCriteria.getSearchMap().put("stat","1");
			// searchCriteria.getSearchMap().put("driver",drivers.get(currentListIndex));
			 searchCriteria.getSearchMap().put("summary","false");
			 searchCriteria.getSearchMap().put("company",drivers.get(currentListIndex).getCompany().getId().toString());
			 searchCriteria.getSearchMap().put("terminal",drivers.get(currentListIndex).getTerminal().getId().toString());
			 searchCriteria.getSearchMap().put("driver",drivers.get(currentListIndex).getFullName());
			 searchCriteria.getSearchMap().put("fromDate",(String)request.getSession().getAttribute("loadbatchFom"));
			 searchCriteria.getSearchMap().put("toDate",(String)request.getSession().getAttribute("loadbatchT"));
			 
			 DriverPayWrapper wrapper = null;
			 
			 try {
				wrapper=hrReportService.generateDriverPayReport(searchCriteria);
				System.out.println("**********wrapper value is "+wrapper);
				if(wrapper==null){
					System.out.println(" wrapper value is "+wrapper);
					return urlContext+"/bonusform";
				}
			} catch (Exception e) {				
				System.out.println("******* Exception is "+e);
			}
			// DriverPayWrapper wrapper=hrReportService.generateDriverPayReport(searchCriteria);
			 
			 System.out.println("\n sumamount-->"+wrapper.getSumAmount());
			 String batchFrom=ReportDateUtil.getFromDate((String)request.getSession().getAttribute("loadbatchFom"));
			 String batchTo=ReportDateUtil.getFromDate((String)request.getSession().getAttribute("loadbatchT"));
			 Double sickParsonalAmount=0.00;
				Double vacationAmount=0.00;
				List<Double> parameter1=new ArrayList<Double>();
		           List<Double> parameter2=new ArrayList<Double>();
				StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+drivers.get(currentListIndex).getId()+" and obj.category=2");
				if(!StringUtils.isEmpty(batchFrom)){
				ptodquery.append(" and obj.batchdate>='"+batchFrom+"'");
				}
				if(!StringUtils.isEmpty(batchTo)){
					ptodquery.append(" and obj.batchdate<='"+batchTo+"'");
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
				parameter1.add(sickParsonalAmount);
				parameter2.add(vacationAmount);
				sickParsonalAmount=0.00;
					vacationAmount=0.00;
			 Map<String,Object> datas = new HashMap<String,Object>();
			 Map<String,Object> params = new HashMap<String,Object>();
			 params.put("totalRowCount", wrapper.getTotalRowCount());
				params.put("driver", wrapper.getDriver());
				params.put("company", wrapper.getCompany());
				params.put("batchDateFrom", wrapper.getBatchDateFrom());
				params.put("batchDateTo",wrapper.getBatchDateTo());
				params.put("sumTotal",wrapper.getSumTotal());
				params.put("payRollBatch", wrapper.getPayRollBatch());
				datas.put("data", wrapper.getDriverPays());
				params.put("sumAmount", wrapper.getSumAmount());
				 params.put("parameter1", parameter1);
		           params.put("parameter2", parameter2);
				datas.put("params", params);
				String type = "html";
				String  report="driverpaybonus";
				response.setContentType(MimeUtil.getContentType(type));
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename=" + "driverpay" + "." + type);
				}
				try{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(report,
							(List)datas.get("data"), params, request);
					request.setAttribute("jasperPrint", jasperPrint);
					
				} catch (Exception e) {
					e.printStackTrace();
					request.getSession().setAttribute("errors", e.getMessage());
					return "error";
				}
				
			
				return urlContext+"/bonusform";			
		}	
		else{
			return urlContext+"/bonusform";	
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/addEmpBonusPrevious.do")
	public String moveToPreviousEmployee(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
		
		List<Driver> drivers = (List<Driver>) request.getSession().getAttribute("driverList");
		
		//int driverListIndex = Integer.parseInt(listIndex);
		
		int currentListIndex = driverListIndex-1;
		
		if(currentListIndex < 0){
			currentListIndex=0;
			request.getSession().setAttribute("error","Cannot go to Previous.");
		}
		
		
		if(drivers!=null && drivers.size()>0){		
			
			request.getSession().setAttribute("driverListIndexCount",currentListIndex);
			
			
			
			EmployeeBonus entity = new EmployeeBonus();

			 entity.setCategory(drivers.get(currentListIndex).getCatagory());
			 entity.setDriver(drivers.get(currentListIndex));
			 if(drivers.get(currentListIndex).getDateHired()!=null){
				 entity.setDateHired(drivers.get(currentListIndex).getDateHired());
			 }else{
				 entity.setDateHired(drivers.get(currentListIndex).getDateReHired());
			 }
			 entity.setCompany(drivers.get(currentListIndex).getCompany());
			 entity.setTerminal(drivers.get(currentListIndex).getTerminal());
			 Date dateHired;
			 if(drivers.get(currentListIndex).getDateHired()!=null){		 
				 dateHired=drivers.get(currentListIndex).getDateHired();
			 }else{
				 dateHired=drivers.get(currentListIndex).getDateReHired();
			 }
			 Calendar currentDate = Calendar.getInstance();
			 int d=DateUtil.daysBetween(dateHired, currentDate.getTime());
			 int year	=(d /365);
			 double days=(d %365);		  
			 double month=(days/30);
			 int finmonths=(int)MathUtil.roundUp(month, 0);
			 StringBuffer exp=new StringBuffer("");
			 if(finmonths>11){
				entity.setExperience((year+1)+".0");
			 }else{
				entity.setExperience(year+"."+finmonths);
			 }
			 DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			 try {
				Date dfrom = (Date)formatter.parse((String)request.getSession().getAttribute("loadbatchFom"));
				entity.setBatchFrom(dfrom);
			 } catch (ParseException e1) {				
				e1.printStackTrace();
			 }
			 try {
				Date dto = (Date)formatter.parse((String)request.getSession().getAttribute("loadbatchT"));
				entity.setBatchTo(dto);
			 } catch (ParseException e1) {				
				e1.printStackTrace();
			 }
		 
			 model.addAttribute("modelObject", entity);
			 setupCreate(model, request);
			 searchCriteria.getSearchMap().put("stat","1");
			// searchCriteria.getSearchMap().put("driver",drivers.get(currentListIndex));
			 searchCriteria.getSearchMap().put("summary","false");
			 searchCriteria.getSearchMap().put("company",drivers.get(currentListIndex).getCompany().getId().toString());
			 searchCriteria.getSearchMap().put("terminal",drivers.get(currentListIndex).getTerminal().getId().toString());
			 searchCriteria.getSearchMap().put("driver",drivers.get(currentListIndex).getFullName());
			 searchCriteria.getSearchMap().put("fromDate",(String)request.getSession().getAttribute("loadbatchFom"));
			 searchCriteria.getSearchMap().put("toDate",(String)request.getSession().getAttribute("loadbatchT"));
			 
			 DriverPayWrapper wrapper = null;
			 
			 try {
				wrapper=hrReportService.generateDriverPayReport(searchCriteria);
				System.out.println("**********wrapper value is "+wrapper);
				if(wrapper==null){
					System.out.println(" wrapper value is "+wrapper);
					return urlContext+"/bonusform";
				}
			} catch (Exception e) {
				
				System.out.println("******* Exception is "+e);
			}
			// DriverPayWrapper wrapper=hrReportService.generateDriverPayReport(searchCriteria);
			 
			 System.out.println("\n sumamount-->"+wrapper.getSumAmount());
			 String batchFrom=ReportDateUtil.getFromDate((String)request.getSession().getAttribute("loadbatchFom"));
			 String batchTo=ReportDateUtil.getFromDate((String)request.getSession().getAttribute("loadbatchT"));
			 Double sickParsonalAmount=0.00;
				Double vacationAmount=0.00;
				List<Double> parameter1=new ArrayList<Double>();
		           List<Double> parameter2=new ArrayList<Double>();
				StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+drivers.get(currentListIndex).getId()+" and obj.category=2");
				if(!StringUtils.isEmpty(batchFrom)){
				ptodquery.append(" and obj.batchdate>='"+batchFrom+"'");
				}
				if(!StringUtils.isEmpty(batchTo)){
					ptodquery.append(" and obj.batchdate<='"+batchTo+"'");
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
				parameter1.add(sickParsonalAmount);
				parameter2.add(vacationAmount);
				sickParsonalAmount=0.00;
					vacationAmount=0.00;
			 Map<String,Object> datas = new HashMap<String,Object>();
			 Map<String,Object> params = new HashMap<String,Object>();
			 params.put("totalRowCount", wrapper.getTotalRowCount());
				params.put("driver", wrapper.getDriver());
				params.put("company", wrapper.getCompany());
				params.put("batchDateFrom", wrapper.getBatchDateFrom());
				params.put("batchDateTo",wrapper.getBatchDateTo());
				params.put("sumTotal",wrapper.getSumTotal());
				params.put("payRollBatch", wrapper.getPayRollBatch());
				datas.put("data", wrapper.getDriverPays());
				params.put("sumAmount", wrapper.getSumAmount());
				 params.put("parameter1", parameter1);
		           params.put("parameter2", parameter2);
				datas.put("params", params);
				String type = "html";
				String  report="driverpaybonus";
				response.setContentType(MimeUtil.getContentType(type));
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename=" + "driverpay" + "." + type);
				}
				try{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(report,
							(List)datas.get("data"), params, request);
					request.setAttribute("jasperPrint", jasperPrint);
					
				} catch (Exception e) {
					e.printStackTrace();
					request.getSession().setAttribute("errors", e.getMessage());
					return "error";
				}
				
			
				return urlContext+"/bonusform";			
		}	
		else{
			return urlContext+"/bonusform";	
		}
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/saveBonus.do")
	public String saveBonusData(HttpServletRequest request,@ModelAttribute("modelObject") EmployeeBonus entity,
			BindingResult bindingResult, ModelMap model){	
		try{		

			String[] bonustype=(String[])request.getParameterValues("bonustype");
			String[] bonusamount=(String[])request.getParameterValues("bonusAmount");
			String[] note=(String[])request.getParameterValues("note");
			List<BonusType> bonusTypes=new ArrayList<BonusType>();
			if(bonustype==null||bonusamount==null){	
				Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
				int currentListIndex = driverListIndex-1;
				request.getSession().setAttribute("driverListIndexCount",currentListIndex);
				request.getSession().setAttribute("error", "Please add Bonus Type and Amount");
				return "redirect:addEmpBonusNext.do";

			}
			for(String b:bonustype){
				if(!StringUtils.isEmpty(b)){
					BonusType type=genericDAO.getById(BonusType.class, Long.parseLong(b));
					bonusTypes.add(type);
				}

				System.out.println("\n b-->"+b);
			}
			List<Double> amount=new ArrayList<Double>();
			for(String a:bonusamount){
				if(!StringUtils.isEmpty(a)){
					amount.add(Double.parseDouble(a));				
				}
			}
			List<String> notes= new ArrayList<String>();
			for(String n:note){
				notes.add(n);
			}
			
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

			double duplicateAmt = 0.0;
			for (Double string :amount) {
				duplicateAmt+=string;
			}
			
			String duplicateType ="";
			for (BonusType type :bonusTypes) {
				duplicateType+=type.getTypename();
			}
			
			if(entity.getDriver()!=null && entity.getCompany()!=null && entity.getBatchFrom()!=null && entity.getBatchTo()!=null && entity.getTerminal()!=null){
				String batchdateFrom=dateFormat1.format(entity.getBatchFrom());
				String batchdateTo=dateFormat1.format(entity.getBatchTo());
				StringBuilder checkForDuplicateQuery = new StringBuilder("SELECT obj FROM EmployeeBonus obj WHERE obj.company ="+entity.getCompany().getId()+" AND obj.terminal="+entity.getTerminal().getId()+" AND obj.batchFrom='"+batchdateFrom+"' AND obj.batchTo='"+batchdateTo+"' AND obj.driver="+entity.getDriver().getId());
				if(entity.getId()!=null){
					checkForDuplicateQuery.append(" and obj.id !="+entity.getId());
				}
				
				List<EmployeeBonus> list = genericDAO.executeSimpleQuery(checkForDuplicateQuery.toString());
									
				if(list.size()>0 && list!=null){
					for (EmployeeBonus employeeBonus : list) {
						List<EmpBonusTypesList> ebTypeList = genericDAO.executeSimpleQuery("select obj from EmpBonusTypesList obj where obj.bonus="+employeeBonus.getId());
						if (ebTypeList.size()>0 && ebTypeList!=null) {
							double empBTLamt=0.0;
							String empBTLType = "";
							for (EmpBonusTypesList empBonusTypesList : ebTypeList) {
								empBTLamt+=empBonusTypesList.getBonusamount();
								empBTLType+=empBonusTypesList.getBonusType().getTypename();
							}				
							if(empBTLamt == duplicateAmt && StringUtils.equalsIgnoreCase(empBTLType, duplicateType)){
								setupCreate(model, request);
								request.getSession().setAttribute("error",
								"Duplicate Entry.");
								return urlContext + "/bonusform";
							}else{
								System.out.println("Not Equals");
							}
						}	
					}
				}
			}

			beforeSave(request, entity, model);

			genericDAO.saveOrUpdate(entity);

			if(amount.size()== bonusTypes.size()){			
				for(int i=0;i<bonusTypes.size();i++){
					EmpBonusTypesList bonusTypesList=new EmpBonusTypesList();
					bonusTypesList.setBonusType(bonusTypes.get(i));
					bonusTypesList.setBonusamount(amount.get(i));
					bonusTypesList.setNote(notes.get(i));
					bonusTypesList.setBonus(entity);
					genericDAO.save(bonusTypesList);				
				}			
			}
			else{
				request.getSession().setAttribute("error", "DataBase Error!. Data partially stored. ");			
			} 
			Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
			int currentListIndex = driverListIndex-1;
			request.getSession().setAttribute("driverListIndexCount",currentListIndex);
			request.getSession().setAttribute("msg", "Driver Bonus Added successful");
			return "redirect:addEmpBonusNext.do";	
		}
		catch (Exception e) {
			e.printStackTrace();
			Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
			int currentListIndex = driverListIndex-1;
			request.getSession().setAttribute("driverListIndexCount",currentListIndex);
			request.getSession().setAttribute("error", "Incorrect Data.");
			return "redirect:addEmpBonusNext.do";	
		}
	}
	
	
	/*@RequestMapping(method=RequestMethod.POST, value="/saveBonus.do")
	public String saveBonusData(HttpServletRequest request,@ModelAttribute("modelObject") EmployeeBonus entity,
			BindingResult bindingResult, ModelMap model){	
		try{		
		String[] bonustype=(String[])request.getParameterValues("bonustype");
		String[] bonusamount=(String[])request.getParameterValues("bonusAmount");
		String[] note=(String[])request.getParameterValues("note");
		List<BonusType> bonusTypes=new ArrayList<BonusType>();
		if(bonustype==null||bonusamount==null){	
			Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
			int currentListIndex = driverListIndex-1;
			request.getSession().setAttribute("driverListIndexCount",currentListIndex);
			request.getSession().setAttribute("error", "Please add Bonus Type and Amount");
			return "redirect:addEmpBonusNext.do";
		 
		}
		for(String b:bonustype){
			if(!StringUtils.isEmpty(b)){
				BonusType type=genericDAO.getById(BonusType.class, Long.parseLong(b));
				bonusTypes.add(type);
			}
				
			System.out.println("\n b-->"+b);
		}
		List<Double> amount=new ArrayList<Double>();
		for(String a:bonusamount){
			if(!StringUtils.isEmpty(a)){
				amount.add(Double.parseDouble(a));				
			}
		}
	List<String> notes= new ArrayList<String>();
	for(String n:note){
		notes.add(n);
	}
		beforeSave(request, entity, model);
		
		genericDAO.saveOrUpdate(entity);
		
		
		
		if(amount.size()== bonusTypes.size()){			
				for(int i=0;i<bonusTypes.size();i++){
					EmpBonusTypesList bonusTypesList=new EmpBonusTypesList();
					bonusTypesList.setBonusType(bonusTypes.get(i));
					bonusTypesList.setBonusamount(amount.get(i));
					bonusTypesList.setNote(notes.get(i));
					bonusTypesList.setBonus(entity);
					genericDAO.save(bonusTypesList);				
				}			
		}
		else{
			request.getSession().setAttribute("error", "DataBase Error!. Data partially stored. ");			
		} 
		Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
		int currentListIndex = driverListIndex-1;
		request.getSession().setAttribute("driverListIndexCount",currentListIndex);
		request.getSession().setAttribute("msg", "Driver Bonus Added successful");
		return "redirect:addEmpBonusNext.do";	
		}
		catch (Exception e) {
			Integer driverListIndex = (Integer)  request.getSession().getAttribute("driverListIndexCount");
			int currentListIndex = driverListIndex-1;
			request.getSession().setAttribute("driverListIndexCount",currentListIndex);
			request.getSession().setAttribute("error", "Incorrect Data.");
			return "redirect:addEmpBonusNext.do";	
		}
	}*/
	
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/bonusnext.do")
	public String bonusNext(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("modelObject") EmployeeBonus entity,
			BindingResult bindingResult, ModelMap model){
		
		
		
		List<String> id=(List<String>) request.getSession().getAttribute("empid");
		List<String> id1=(List<String>) request.getSession().getAttribute("empid1");
		
		String idd=(String)request.getParameter("id");
		
		
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
        if(!StringUtils.isEmpty(idd)){        	
        	Map map =new HashMap();
			map=(Map)request.getSession().getAttribute("bonusmap");			
			searchCriteria.getSearchMap().put("fromDate", map.get("batchfrom"));
			searchCriteria.getSearchMap().put("toDate", map.get("batchto"));			
		}
		/*}else{
			System.out.println("\n inside else--");
			searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria11");
		}*/
		 String batchFrom=(String)searchCriteria.getSearchMap().get("fromDate");
		 String batchTo=(String)searchCriteria.getSearchMap().get("toDate");
		  try {
			  
			  id.get(0);
		} catch (Exception e) {
			id.add(0, id1.get(0));
			id1.remove(0);
			System.out.println("\n list over-->"+id.get(0));
			model.addAttribute("listEnd", true);
			//request.getSession().setAttribute("error","List Is Over1");
		}
		System.out.println("\n here near id-->"+id.get(0));
		Map criti =new HashMap();
		criti.put("fullName",id.get(0));
		Driver driver=genericDAO.getByCriteria(Driver.class, criti);
		EmployeeBonus bonus=(EmployeeBonus) request.getSession().getAttribute("ebonus");
		// EmployeeBonus modelObject;
		 if(bonus==null){
			 System.out.println("\n inside if");
			 entity = new EmployeeBonus();
			 entity.setCategory(driver.getCatagory());
			 entity.setDriver(driver);
			 if(driver.getDateHired()!=null){
				 entity.setDateHired(driver.getDateHired());
			 }else{
				 entity.setDateHired(driver.getDateReHired());
			 }
			 entity.setCompany(driver.getCompany());
			 entity.setTerminal(driver.getTerminal());
		 Date dateHired;
		 if(driver.getDateHired()!=null){
			 
			 dateHired=driver.getDateHired();
		 }else{
			 dateHired=driver.getDateReHired();
		 }
			Calendar currentDate = Calendar.getInstance();
				int d=DateUtil.daysBetween(dateHired, currentDate.getTime());
			  int year	=(d /365);
			  double days=(d %365);
			  
				 double month=(days/30);
				int finmonths=(int)MathUtil.roundUp(month, 0);
				StringBuffer exp=new StringBuffer("");
				if(finmonths>11){
					entity.setExperience((year+1)+".0");
				}else{
					entity.setExperience(year+"."+finmonths);
				}
				
				DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
				try {
					Date dfrom = (Date)formatter.parse(batchFrom);
					entity.setBatchFrom(dfrom);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Date dto = (Date)formatter.parse(batchTo);
					entity.setBatchTo(dto);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 }else{
			 System.out.println("\n inside save");
			 request.getSession().removeAttribute("ebonus");
			 entity= bonus;
			 if(entity.getDriver()==null){
					bindingResult.rejectValue("driver", "error.select.option",null, null);
				}
				
				if(entity.getCategory()==null){
					bindingResult.rejectValue("category", "error.select.option",null, null);
				}
				if(entity.getCompany()==null){
					bindingResult.rejectValue("company", "error.select.option",null, null);
				}
				if(entity.getTerminal()==null){
					bindingResult.rejectValue("terminal", "error.select.option",null, null);
				}
				
				if(entity.getBonustype()==null){
					bindingResult.rejectValue("bonustype", "error.select.option",null, null);
					//bindingResult.rejectValue("bonustype", "please select option");
					request.getSession().setAttribute("error", "please select Bonus Type");
				}
			/*	if(entity.getRefferal()==null){
					bindingResult.rejectValue("refferal", "error.select.option",null, null);
				}*/
				if(entity.getDriver()!=null){
					entity.setEmpnumber(entity.getDriver().getStaffId());
				}
				
				
				
				
				//return super.save(request, entity, bindingResult, model);
				try {
					System.out.println("\n error-->"+bindingResult.getFieldError());
					getValidator().validate(entity, bindingResult);
				} catch (ValidationException e) {
					e.printStackTrace();
					log.warn("Error in validation :" + e);
				}
		 }
		 model.addAttribute("modelObject", entity);
		 setupCreate(model, request);
		 searchCriteria.getSearchMap().put("stat","1");
		 searchCriteria.getSearchMap().put("driver",id.get(0));
		 searchCriteria.getSearchMap().put("summary","false");
		 DriverPayWrapper wrapper = null;
		 try {
			wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		} catch (Exception e) {
			// TODO: handle exception
			request.getSession().setAttribute("error", entity.getDriver().getFullName()+" driver not found");
			 id1.add(0, id.get(0));
			 id.remove(0);
			 request.getSession().setAttribute("empid", id);
			 request.getSession().setAttribute("empid1", id1);
			return "redirect:bonusnext.do";
		}
		// DriverPayWrapper wrapper=hrReportService.generateDriverPayReport(searchCriteria);
		 id1.add(0, id.get(0));
		 id.remove(0);
		 request.getSession().setAttribute("empid", id);
		 request.getSession().setAttribute("empid1", id1);
		 System.out.println("\n sumamount-->"+wrapper.getSumAmount());
		 batchFrom=ReportDateUtil.getFromDate(batchFrom);
		 batchTo=ReportDateUtil.getFromDate(batchTo);
		 Double sickParsonalAmount=0.00;
			Double vacationAmount=0.00;
			List<Double> parameter1=new ArrayList<Double>();
	           List<Double> parameter2=new ArrayList<Double>();
			StringBuffer ptodquery=new StringBuffer("select obj from Ptodapplication obj where obj.driver="+driver.getId()+" and obj.category=2");
			if(!StringUtils.isEmpty(batchFrom)){
			ptodquery.append(" and obj.batchdate>='"+batchFrom+"'");
			}
			if(!StringUtils.isEmpty(batchTo)){
				ptodquery.append(" and obj.batchdate<='"+batchTo+"'");
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
			parameter1.add(sickParsonalAmount);
			parameter2.add(vacationAmount);
			sickParsonalAmount=0.00;
				vacationAmount=0.00;
		 Map<String,Object> datas = new HashMap<String,Object>();
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("totalRowCount", wrapper.getTotalRowCount());
			params.put("driver", wrapper.getDriver());
			params.put("company", wrapper.getCompany());
			params.put("batchDateFrom", wrapper.getBatchDateFrom());
			params.put("batchDateTo",wrapper.getBatchDateTo());
			params.put("sumTotal",wrapper.getSumTotal());
			params.put("payRollBatch", wrapper.getPayRollBatch());
			datas.put("data", wrapper.getDriverPays());
			params.put("sumAmount", wrapper.getSumAmount());
			 params.put("parameter1", parameter1);
	           params.put("parameter2", parameter2);
			datas.put("params", params);
			String type = "html";
			String  report="driverpaybonus";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "driverpay" + "." + type);
			}
			try{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(report,
						(List)datas.get("data"), params, request);
				request.setAttribute("jasperPrint", jasperPrint);
				
			} catch (Exception e) {
				e.printStackTrace();
				request.getSession().setAttribute("errors", e.getMessage());
				return "error";
			}
			if (bindingResult.hasErrors()) {
				return urlContext+"/bonusform";
			}
		
			return urlContext+"/bonusform";
	}
	@RequestMapping("previous.do")
	String previous(HttpServletRequest request){
		List<String> id=(List<String>) request.getSession().getAttribute("empid");
		List<String> id1=(List<String>) request.getSession().getAttribute("empid1");
		
		try {
			id.add(0, id1.get(0));
			id.add(0, id1.get(1));
		} catch (Exception e) {
		System.out.println("Error Saving Employee Bonus "+e.getMessage());
		}
		
		try {
			//id1.remove(0);
			id1.remove(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		 request.getSession().setAttribute("empid1", id1);
		   request.getSession().setAttribute("empid", id);
		 return "redirect:bonusnext.do";
	}
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(BonusType.class, new AbstractModelEditor(BonusType.class));
	}
	
	
	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") EmployeeBonus entity,
			BindingResult bindingResult, ModelMap model) {
		boolean check=false;
		if(entity.getId()!=null){
			check=true;
		}	
		
		if(entity.getDriver()==null){
			bindingResult.rejectValue("driver", "error.select.option",null, null);
		}
		
		if(entity.getCategory()==null){
			bindingResult.rejectValue("category", "error.select.option",null, null);
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",null, null);
		}
		
		/*if(entity.getBonustype()==null){
			bindingResult.rejectValue("bonustype", "error.select.option",null, null);
		}*/
	/*	if(entity.getRefferal()==null){
			bindingResult.rejectValue("refferal", "error.select.option",null, null);
		}*/
		if(entity.getDriver()!=null){
			entity.setEmpnumber(entity.getDriver().getStaffId());
		}
		
		String[] bonustype=(String[])request.getParameterValues("bonustype");
		String[] bonusamount=(String[])request.getParameterValues("bonusAmount");
		String[] note=(String[])request.getParameterValues("note");
		List<BonusType> bonusTypes=new ArrayList<BonusType>();
		if(bonustype==null||bonusamount==null){
			request.getSession().setAttribute("error", "Please add Bonus Type and Amount");
		return "redirect:previous.do";
		}
		for(String b:bonustype){
			if(!StringUtils.isEmpty(b)){
				BonusType type=genericDAO.getById(BonusType.class, Long.parseLong(b));
				bonusTypes.add(type);
			}
				
			System.out.println("\n b-->"+b);
		}
		List<Double> amount=new ArrayList<Double>();
		for(String a:bonusamount){
			if(!StringUtils.isEmpty(a)){
				amount.add(Double.parseDouble(a));
				
			}
		}
	List<String> notes= new ArrayList<String>();
	for(String n:note){
		notes.add(n);
	}
		//return super.save(request, entity, bindingResult, model);
	/*	try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		if (bindingResult.hasErrors()) {
			List<String> id=(List<String>) request.getSession().getAttribute("empid");
			id.add(0,entity.getEmployee().getId()+"" );
			 request.getSession().setAttribute("empid", id);
			 request.getSession().setAttribute("ebonus", entity);
			//setupCreate(model, request);
			 return "redirect:/" + urlContext + "/bonusnext.do";
		}*/
		beforeSave(request, entity, model);
		
		genericDAO.saveOrUpdate(entity);
		System.out.println("\n amount.size()-->"+amount.size());
		System.out.println("\n bonusTypes.size()-->"+bonusTypes.size());
		if(amount.size()== bonusTypes.size()){
			if(!check){
			for(int i=0;i<bonusTypes.size();i++){
				EmpBonusTypesList bonusTypesList=new EmpBonusTypesList();
				bonusTypesList.setBonusType(bonusTypes.get(i));
				bonusTypesList.setBonusamount(amount.get(i));
				bonusTypesList.setNote(notes.get(i));
				bonusTypesList.setBonus(entity);
				genericDAO.save(bonusTypesList);
				
			}
			}
		}
		else{
			request.getSession().setAttribute("error", "DataBase Error");
			
		}
		if(check){
			return "redirect:list.do";
		}
		request.getSession().setAttribute("msg", "Driver Bonus Added successful");
		 SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		 Map map=new HashMap();
		 map.put("batchfrom", searchCriteria.getSearchMap().get("fromDate"));
		 map.put("batchto", searchCriteria.getSearchMap().get("toDate"));
		 request.getSession().setAttribute("bonusmap",map);
		/* SearchCriteria criteria=new SearchCriteria();
		 criteria=searchCriteria;
		 request.getSession().setAttribute("searchCriteria11",criteria );*/
		// searchCriteria.getSearchMap().clear();
		 searchCriteria.getSearchMap().put("id", entity.getId());
		 request.getSession().setAttribute("searchCriteria", searchCriteria);
		 request.getSession().setAttribute("nextbonuses", true);
		return "redirect:list.do";
		//return "redirect:/" + urlContext + "/bonusnext.do";
	}
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
	{
		 String driverQuery = "select obj from Driver obj where obj.status=1 and obj.fullName='";
		
		if("findDCompany".equalsIgnoreCase(action)){
 			if(!StringUtils.isEmpty(request.getParameter("driver"))){
 				List<Location> company=new ArrayList<Location>();
 				
 				//Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
 				driverQuery += (request.getParameter("driver") + "'");
 				List<Driver> driverList = genericDAO.executeSimpleQuery(driverQuery);
 				Driver driver = driverList.get(0);
 				
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
 				
 				//Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
 				driverQuery += (request.getParameter("driver") + "'");
 				List<Driver> driverList = genericDAO.executeSimpleQuery(driverQuery);
 				Driver driver = driverList.get(0);
 				
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
 		
 		if("findhireex".equalsIgnoreCase(action)){
 			List<String> list = new ArrayList<String>();
 			if(!StringUtils.isEmpty(request.getParameter("driver"))){
 				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
 				 SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
 				Date dateHired = driver.getDateHired();
 				list.add(dateFormat.format(dateHired));
 				Calendar currentDate = Calendar.getInstance();
 				int d=DateUtil.daysBetween(dateHired, currentDate.getTime());
 			  int year	=(d /365);
 			  double days=(d %365);
 			  
 			  if(days>30){
 				 double month=(days/30);
 				int finmonths=(int)MathUtil.roundUp(month, 0);
 				StringBuffer exp=new StringBuffer("");
 				if(finmonths>11){
 					exp.append((year+1)+".0");
 				}else{
 					exp.append(year+"."+finmonths);
 				}
 				 list.add(exp.toString());
 			  }
 				
 			}
 			else{
 				list.add("");
 				list.add("");
 			}
 			Gson gson = new Gson();
				return gson.toJson(list);
 		}
		if("findref".equalsIgnoreCase(action)){
			List<String> list = new ArrayList<String>();
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
 				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
 				 SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
 				Date dateHired = driver.getDateHired();
 				list.add(dateFormat.format(dateHired));
 				Gson gson = new Gson();
				return gson.toJson(list);
			}
		}
		
		if("findamount".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("bid"))){
				String idin=(String)request.getParameter("bid");
				String ids[]=idin.split(",");
				List<BonusType> bonusTypes=new ArrayList<BonusType>();
				for(String id:ids){
					System.out.println("\n id-->"+id);
					if(!StringUtils.isEmpty(id)){
					BonusType bonusType=genericDAO.getById(BonusType.class,Long.parseLong(id));
					bonusTypes.add(bonusType);
					}
				}
				//List<String> list = new ArrayList<String>();
				/*BonusType bonusType=genericDAO.getById(BonusType.class,Long.parseLong(request.getParameter("bid")));*/
			//	DecimalFormat df = new DecimalFormat("#0");
			//	Double amount=bonusType.getAmount();
				/*if(amount!=null){
					list.add(df.format(amount));
				}*/
				Gson gson = new Gson();
				return gson.toJson(bonusTypes);
			}
		}
if("findticket".equalsIgnoreCase(action)){
	String batchfrom=request.getParameter("p1");
	String batchto=request.getParameter("p2");
	String empid=request.getParameter("empid");
	batchfrom=ReportDateUtil.getFromDate(batchfrom);
	batchto=ReportDateUtil.getToDate(batchto);
	Driver driver=null;
	if(!StringUtils.isEmpty(empid)){
	 Driver driver1=genericDAO.getById(Driver.class, Long.parseLong(empid));
	 Map criterias=new HashMap();
	 criterias.put("fullName", driver1.getFullName());
	 driver =genericDAO.getByCriteria(Driver.class, criterias);
	}
			StringBuffer query=new StringBuffer("");
			query.append("select  obj.origin.name,obj.destination.name ,count(obj)from Ticket obj where obj.status=1 ");
			if(driver!=null){
				query.append(" and obj.driver="+driver.getId());
			}
			if(!StringUtils.isEmpty(batchfrom)){
				query.append(" and  obj.billBatch>='").append(batchfrom+"'");
			}
			if(!StringUtils.isEmpty(batchto)){
				query.append(" and  obj.billBatch<='").append(batchto+"'");
			}
			query.append(" group by origin,destination order by obj.origin.name asc,obj.destination.name asc");
			List<Summary> list = genericDAO.executeSimpleQuery(query.toString());
			List<Summary> summarylist=new ArrayList<Summary>();		
			for(Object obj:list){						
				Object[] objArry=(Object[]) obj;
				Summary summary=new Summary();				
				summary.setOrigin(objArry[0].toString());
				summary.setDestination(objArry[1].toString());				
				summary.setCount(Integer.parseInt(objArry[2].toString()));		
				summarylist.add(summary);
				}
			Gson gson = new Gson();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return gson.toJson(summarylist);
		}
		
		return "";
	}
}
