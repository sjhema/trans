package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.DateUtil;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Terminal;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hr.BonusType;
import com.primovision.lutransport.model.hr.EmpBonusTypesList;
//import com.primovision.lutransport.model.hr.Employee;
import com.primovision.lutransport.model.hr.EmployeeBonus;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.report.Summary;


@Controller
@RequestMapping("/hr/miscellaneousamount")
public class MiscellaneousAmountController extends CRUDController<MiscellaneousAmount> {	
	
	public MiscellaneousAmountController() {		
		setUrlContext("/hr/miscellaneousamount");
	}
		
		@Autowired
		protected HrReportService hrReportService;
		
		public void setHrReportService(
				HrReportService hrReportService) {
			this.hrReportService = hrReportService;
		}
		
		@Override
		 public void initBinder(WebDataBinder binder) { 
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			dateFormat.setLenient(false);
			binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
			binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));			
			binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
			
		}
		
		
		@Override
		public String search2(ModelMap model, HttpServletRequest request) {
			
			setupList(model, request);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
		
			
			if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("batchFrom"))) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				try {
					Date billBatch = dateFormat.parse((String)criteria.getSearchMap().get("batchFrom"));
					criteria.getSearchMap().put("batchFrom",ReportDateUtil.oracleFormatter.format(billBatch));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//criteria.getSearchMap().remove("batchdate");
			 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
			return urlContext + "/list";
		}

		
		
		public void setupCreate(ModelMap model, HttpServletRequest request) {
			SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");			
			Map criterias = new HashMap();
			if(request.getParameter("id")!=null){
				criterias.clear();
				model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
			}
			else{
				criterias.clear();
				criterias.put("status", 1);
				model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
			}
			criterias.clear();	
			criterias.put("type", 3);
			model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
			criterias.put("type", 4);
			model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
			criterias.clear();		
			model.addAttribute("miscellaneousDesc", genericDAO.executeSimpleQuery("select obj from SetupData obj where dataType='Miscellaneous_Desc'  order by obj.dataLabel asc"));
			criterias.clear();		
			model.addAttribute("miscellaneousType", genericDAO.executeSimpleQuery("select obj from SetupData obj where dataType='Miscellaneous_Category'  order by obj.dataValue desc"));
		}
		
		@Override
		public void setupList(ModelMap model, HttpServletRequest request) {
			populateSearchCriteria(request, request.getParameterMap());
			setupCreate(model, request);
		}
		
		
		@Override
		public String list(ModelMap model, HttpServletRequest request) {
			setupList(model, request);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
			 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
			return urlContext + "/list";
		}
		
		@Override
		public String save(HttpServletRequest request,@ModelAttribute("modelObject") MiscellaneousAmount entity,
				BindingResult bindingResult, ModelMap model) {
			
			request.getSession().setAttribute("company", entity.getCompany());
			request.getSession().setAttribute("terminal", entity.getTerminal());			
			request.getSession().setAttribute("batchFrom", entity.getBatchFrom());
			request.getSession().setAttribute("batchTo", entity.getBatchTo());
			request.getSession().setAttribute("miscNotes", entity.getMiscNotes());		
			request.getSession().setAttribute("miscType", entity.getMiscType());
			
			
			
			
			if(entity.getCompany()==null){				
				bindingResult.rejectValue("company", "error.select.option",null, null);
			}
			
			if(entity.getTerminal()==null){
				bindingResult.rejectValue("terminal", "error.select.option",null, null);
			}
			
			if(entity.getDriver()==null){
				bindingResult.rejectValue("driver", "error.select.option",null, null);
			}		
			
			if(entity.getMisamount()==null){
				bindingResult.rejectValue("misamount", "NotNull.java.lang.Double",null, null);
			}
			 
		    if(entity.getMiscNotes().equals("")){
				bindingResult.rejectValue("miscNotes", "error.select.option",null, null);
			}
		    else{
		    	if(entity.getMiscType().equalsIgnoreCase("Quarter Bonus")){
		    		if(StringUtils.isEmpty(entity.getSequenceNumber())){
		    			bindingResult.rejectValue("sequenceNumber", "NotNull.java.lang.String",null, null);
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
					return urlContext + "/form";
				}
				
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

				
				if(entity.getDriver()!=null && entity.getCompany()!=null && entity.getBatchFrom()!=null && entity.getBatchTo()!=null && entity.getTerminal()!=null && !StringUtils.isEmpty(entity.getMiscNotes())){
					String batchdateFrom=dateFormat1.format(entity.getBatchFrom());
					String batchdateTo=dateFormat1.format(entity.getBatchTo());
					StringBuilder checkForDuplicateQuery = new StringBuilder("SELECT m FROM MiscellaneousAmount m WHERE m.company ="+entity.getCompany().getId()+" AND m.terminal="+entity.getTerminal().getId()+" AND m.batchFrom='"+batchdateFrom+"' AND m.batchTo='"+batchdateTo+"' AND m.driver="+entity.getDriver().getId()+" AND m.miscNotes='"+entity.getMiscNotes()+"'");
					if(entity.getId()!=null){
						checkForDuplicateQuery.append(" and m.id !="+entity.getId());
					}
					
					System.out.println("???"+checkForDuplicateQuery.toString());
					List<MiscellaneousAmount> list = genericDAO.executeSimpleQuery(checkForDuplicateQuery.toString());
					
					
					if(list.size()>0 && list!=null){
						setupCreate(model, request);
						request.getSession().setAttribute("error",
						"Duplicate Entry.");
						return urlContext + "/form";
					}
				}
				
				
				
				beforeSave(request, entity, model);
				// merge into datasource
				genericDAO.saveOrUpdate(entity);
				cleanUp(request);
				
				setupCreate(model, request);
				request.getSession().setAttribute("msg",
						"Record added successfully");
				
				if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
					
					return "redirect:list.do";
				}
				else{
					
					return "redirect:create.do";
				}
			
		}
		
		
		@Override
		protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
		{
			
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
			
			
			if ("findAllTerminal".equalsIgnoreCase(action)) {

				List<Driver> dlst=null;
				String query="select obj from Location obj where obj.type=4 and  obj.status=1 order by name ASC";
					dlst=genericDAO.executeSimpleQuery(query);
				
				Gson gson = new Gson();
				return gson.toJson(dlst);
			}
			
			
			if ("findDriver".equalsIgnoreCase(action)) {

				List<Driver> dlst=null;
				String terminalId = request.getParameter("terminal");
				String companyId = request.getParameter("company");
				
				if (!StringUtils.isEmpty(terminalId))
				{		
					if(!StringUtils.isEmpty(request.getParameter("tickId"))){				
					String query="select obj from Driver obj where obj.terminal="+terminalId+" and obj.company="+companyId+" order by fullName ASC";
					dlst=genericDAO.executeSimpleQuery(query);
					}
					else{
						String query="select obj from Driver obj where obj.status=1 and obj.terminal="+terminalId+" and obj.company="+companyId+" order by fullName ASC";
						
						dlst=genericDAO.executeSimpleQuery(query);
					}
				}
				Gson gson = new Gson();
				return gson.toJson(dlst);
			}

			if ("findAllDriver".equalsIgnoreCase(action)) {

				List<Driver> dlst=null;
					String query="select obj from Driver obj where obj.status=1 order by fullName ASC";
					dlst=genericDAO.executeSimpleQuery(query);
				
				Gson gson = new Gson();
				return gson.toJson(dlst);
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
	
		
		
		@ModelAttribute("modelObject")
		public MiscellaneousAmount setupModel(HttpServletRequest request) {
			String pk = request.getParameter(getPkParam());
			if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
				MiscellaneousAmount miscAmount = getEntityInstance();
				miscAmount.setCompany((Location)request.getSession().getAttribute("company") );				
				miscAmount.setTerminal((Location)request.getSession().getAttribute("terminal"));
				miscAmount.setMiscNotes((String)request.getSession().getAttribute("miscNotes"));
				miscAmount.setMiscType((String)request.getSession().getAttribute("miscType"));
				miscAmount.setBatchFrom((Date)request.getSession().getAttribute("batchFrom"));
				miscAmount.setBatchTo((Date)request.getSession().getAttribute("batchTo"));				
				return miscAmount;
			} else {
				return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
			}
		}
}
