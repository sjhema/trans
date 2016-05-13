package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/admin/fuellogreview")
public class FuelLogReviewController extends CRUDController<DriverFuelLog>{

	public FuelLogReviewController() {
		setUrlContext("admin/fuellogreview");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	 private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	 public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
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
		binder.registerCustomEditor(DriverFuelCard.class, new AbstractModelEditor(DriverFuelCard.class));
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");			
		
		String company = (String) criteria.getSearchMap().get("driverCompany.name");
		String terminal = (String) criteria.getSearchMap().get("terminal.name");
		String driver = (String) criteria.getSearchMap().get("driver.fullName");
		String truck = (String) criteria.getSearchMap().get("truck.unit");
		String fuelcard = (String) criteria.getSearchMap().get("driverFuelCard.fuelcard");
		String transDateFrom = (String) criteria.getSearchMap().get("transDateFrom");
		String transDateTo = (String) criteria.getSearchMap().get("transDateTo");
		String gallonFrom = (String) criteria.getSearchMap().get("gallonsFrom");
		String gallonTo = (String) criteria.getSearchMap().get("gallonsTo");
		
		
		StringBuffer query = new StringBuffer("select obj from DriverFuelLog obj  where 1=1");
		StringBuffer countquery = new StringBuffer("select count(obj) from DriverFuelLog obj  where 1=1");
		
		if(!StringUtils.isEmpty(company)){
			query.append(" and obj.driverCompany.name in ('"+company+"')");
			countquery.append(" and obj.driverCompany.name in ('"+company+"')");
		}
		
		if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.terminal.name in ('"+terminal+"')");
			countquery.append(" and obj.terminal.name in ('"+terminal+"')");
		}
		
        if(!StringUtils.isEmpty(driver)){
        	query.append(" and obj.driver.fullName in ('"+driver+"')");
        	countquery.append(" and obj.driver.fullName in ('"+driver+"')");
		}
        
        if(!StringUtils.isEmpty(truck)){
        	query.append(" and obj.truck.unit in ("+truck+")");
        	countquery.append(" and obj.truck.unit in ("+truck+")");
		}
        
        if(!StringUtils.isEmpty(gallonFrom)){
        	query.append(" and obj.gallons  >="+gallonFrom);
        	countquery.append(" and obj.gallons  >="+gallonFrom);
		}
        
        if(!StringUtils.isEmpty(gallonTo)){
        	query.append(" and obj.gallons  <="+gallonTo);
        	countquery.append(" and obj.gallons  <="+gallonTo);
		}
        
        if(!StringUtils.isEmpty(fuelcard)){
        	query.append(" and obj.driverFuelCard.fuelcard in ("+fuelcard+")");
        	countquery.append(" and obj.driverFuelCard.fuelcard in ("+fuelcard+")");
		}
        
        if(!StringUtils.isEmpty(transDateFrom)){
        	try {
				query.append(" and obj.transactionDate  >='"+dateFormat.format(sdf.parse(transDateFrom))+"'");
				countquery.append(" and obj.transactionDate  >='"+dateFormat.format(sdf.parse(transDateFrom))+"'");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
		}
        
        if(!StringUtils.isEmpty(transDateTo)){
        	try {
				query.append(" and obj.transactionDate  <='"+dateFormat.format(sdf.parse(transDateTo))+"'");
				countquery.append(" and obj.transactionDate  <='"+dateFormat.format(sdf.parse(transDateTo))+"'");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

        query.append(" order by obj.transactionDate desc");
        countquery.append(" order by obj.transactionDate desc");
		
        Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
        		countquery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		
		model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());
		return urlContext + "/list";
	}
	
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {					
	 	Map criterias = new HashMap();
	 	criterias.clear();
	 	if(request.getParameter("id")!=null){
	 		criterias.clear();
	 		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			
	 	}
	 	else{
	 		criterias.clear();
			criterias.put("status", 1);
	 		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		}
	}
	
	
	
	 @Override
		public void setupList(ModelMap model, HttpServletRequest request) {
			populateSearchCriteria(request, request.getParameterMap());
			setupCreate(model, request);
			Map criterias = new HashMap();
			criterias.clear();
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class,criterias, "fullName", false));
			
			
			criterias.clear();
			
			criterias.put("type", 3);
			model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			
			criterias.put("type", 4);
			model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			
			model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
			
			model.addAttribute("fuelcards",genericDAO.executeSimpleQuery("select obj from FuelCard obj order by ABS(fuelcardNum)"));
		}
	 
	 
	 @Override	
		public String edit2(ModelMap model, HttpServletRequest request) {
			setupUpdate(model, request);
			DriverFuelLog driverfuellog=genericDAO.getById(DriverFuelLog.class,Long.parseLong(request.getParameter("id")));	
			
			Map criteria = new HashMap();		
			criteria.put("driver.id",driverfuellog.getDriver().getId());			
			model.addAttribute("fuelcards",genericDAO.findByCriteria(DriverFuelCard.class, criteria, "fuelvendor", false));
			model.addAttribute("driverFuelCard",driverfuellog.getDriverFuelCard());
			return urlContext + "/form";
			
	 }
	 
	 
	 @Override
		public String save(HttpServletRequest request,
				@ModelAttribute("modelObject") DriverFuelLog entity,
				BindingResult bindingResult, ModelMap model) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");	
			
			
			Map criterias = new HashMap();
			
			if (StringUtils.isEmpty(entity.getTempTruck())) {
				bindingResult.rejectValue("tempTruck", "NotNull.java.lang.String",
						null, null);
			}
			else{
				criterias.clear();
				criterias.put("unit", Integer.parseInt(entity.getTempTruck()));
				Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
				if(truck==null){
					bindingResult.rejectValue("tempTruck", "error.invalid.truck",
							null, null);
				}
				else{
					
					/**
					 * Added: Bug Fix - Save the truck ID corresponding to the latest date range 
					 */
					System.out.println("Truck ID (Incoming) = " + truck.getId());
					setTruckForFuelLog(entity);
					System.out.println("Truck ID (After date check) = " + entity.getTruck().getId());
//					entity.setTruck(truck);
				}			
			}	
			
			
			if(entity.getDriver()==null){
				bindingResult.rejectValue("driver", "error.select.option", null, null);
			}		
			
			/*if (entity.getDriverFuelCard()==null) {
				bindingResult.rejectValue("driverFuelCard", "error.select.option",
						null, null);
			}*/
			
			try {
				getValidator().validate(entity, bindingResult);
			} catch (ValidationException e) {
				e.printStackTrace();
				log.warn("Error in validation :" + e);
			}
			
			if (bindingResult.hasErrors()) {
				setupCreate(model, request);
				Map criteria = new HashMap();
				if(entity.getDriver()!=null){
					criteria.put("driver.id",entity.getDriver().getId());			
					model.addAttribute("fuelcards",genericDAO.findByCriteria(DriverFuelCard.class, criteria, "fuelvendor", false));
				}
				if(entity.getDriverFuelCard()!=null){
					model.addAttribute("driverFuelCard",entity.getDriverFuelCard());
				}
				return urlContext + "/form";
			}		
			
			
			Map prop=new HashMap();
			prop.clear();
			prop.put("transactionDate",dateFormat.format(entity.getTransactionDate()) );  
			prop.put("gallons",entity.getGallons() );
			if(entity.getDriverFuelCard()!=null)
				prop.put("driverFuelCard",entity.getDriverFuelCard().getId() );
			prop.put("truck",entity.getTruck().getId() );
			prop.put("driver",entity.getDriver().getId() );
			
			boolean rst=genericDAO.isUnique(DriverFuelLog.class,entity, prop);      	  
	   	  	if(!rst){
	   	  		setupCreate(model, request);   			
	   			request.getSession().setAttribute("error","Duplicate Fuel Log Entry");
	   			return urlContext + "/form";
	   	  	}
			
	   	    String bdate=dateFormat1.format(entity.getTransactionDate());			
			LocalDate now = new LocalDate(bdate);			
			LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
	   	  	entity.setBatchDate(sunday.toDate());
	   	  	
	   	  	criterias.clear();
	   	  	criterias.put("id", entity.getDriver().getId());
	   	  	Driver drviver = genericDAO.getByCriteria(Driver.class, criterias);
	   	    entity.setDriverCompany(drviver.getCompany());
	   	  	
	   	  	entity.setTerminal(drviver.getTerminal());
	   	  	
	   	  	entity.setTerminalName(drviver.getTerminal().getName());
	   	  	
	   	  	entity.setCompanyName(drviver.getCompany().getName());
	   	  	
	   	  	entity.setDriverName(drviver.getFullName());
	   	  	
	   	  	entity.setTempTransactionDate(sdf.format(entity.getTransactionDate()));
	   	  	
	   	  	criterias.clear();
	   	 if(entity.getDriverFuelCard()!=null){
	   	    criterias.put("id",entity.getDriverFuelCard().getId());
	   	    DriverFuelCard drvfcObj =genericDAO.getByCriteria(DriverFuelCard.class, criterias);
	   	  	entity.setFuelCard(drvfcObj.getFuelcard());
	   	  	entity.setTempFuelCardNum(drvfcObj.getFuelcard().getFuelcardNum());
	   	 }
	   	  	
	   	    if(entity.getId()==null){
	   	    	entity.setEnteredBy("system");   	  		
	   	    }
	   	  	
			beforeSave(request, entity, model);			
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
			
			String mobileEntryTableUpdateQuery = "update DriverMobileEntry d "
					+ "set d.fuellog_flag='Y' "
					+ ", d.enteredBy='" + getUser(request).getFullName() + "'"
					+ " where "
					+ "d.employeeName in ('"+drviver.getFullName()+"') and d.entryDate='"+mysqldf.format(entity.getTransactionDate())+"'";
			genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery.toString());
			
			
			/*if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
				request.getSession().setAttribute("msg","Fuel log updated successfully");
				return "redirect:list.do";
			}
			else{			
				request.getSession().setAttribute("msg","Fuel log added successfully");
				return "redirect:create.do";
			}*/
			
			return "redirect:list.do";
		}
		
	private void setTruckForFuelLog(DriverFuelLog entity) {
		String vehicleQuery = StringUtils.EMPTY;
		if (entity.getTransactionDate() != null) {

			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
					+ Integer.parseInt(entity.getTempTruck()) + " and obj.validFrom<='"
					+ dateFormat.format(entity.getTransactionDate()) + "' and obj.validTo>='"
					+ dateFormat.format(entity.getTransactionDate()) + "'";
		} 
		
		System.out.println("******* The vehicle query for driver tripsheet is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);

		if (vehicleList != null && vehicleList.size() > 0) {
			entity.setTruck(vehicleList.get(0));
		}
	}
		
		@Override
		protected String processAjaxRequest(HttpServletRequest request,
				String action, Model model) {		
			
			if ("verifytruck".equalsIgnoreCase(action)) {	

				try {
					Map criterias = new HashMap();
					criterias.put("unit", Integer.parseInt(request.getParameter("truck")));
					Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
					if(truck==null){
						return "Invalid truck number";
					}
					else{
						return "";
					}
				
				} catch (Exception e) {				
					return "";
				}		
			}
			
			if("findFuelCard".equalsIgnoreCase(action)){
				if(!StringUtils.isEmpty(request.getParameter("driver"))){					
					Map criterias = new HashMap();			
					criterias.put("driver.id",Long.parseLong(request.getParameter("driver")));
					List<DriverFuelCard> fuelcards=genericDAO.findByCriteria(DriverFuelCard.class, criterias, "fuelvendor", false);		
					Gson gson = new Gson();
					return gson.toJson(fuelcards);
				}
			}
			
			if ("fetchcomterm".equalsIgnoreCase(action)) {	
				try {
					List<String> list = new ArrayList<String>();
					Map criterias = new HashMap();
					criterias.put("id",Long.parseLong(request.getParameter("driver")));
					Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
					list.add(driver.getCompany().getName());
					list.add(driver.getTerminal().getName());					
					Gson gson = new Gson();
					return gson.toJson(list);
				
				} catch (Exception e) {				
					return "";
				}
			}
			
			return "";
		}
	 
	 
		
		@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/exportdata.do")
		public void display(ModelMap model, HttpServletRequest request,
				HttpServletResponse response,@RequestParam(required = false, value = "type") String type,
				@RequestParam(required = false, value = "jrxml") String jrxml) {
			Map<String,Object> datas = new HashMap<String,Object>();
			Map<String,Object> params = new HashMap<String,Object>();
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			
			SearchCriteria criteria = (SearchCriteria) request.getSession()
			.getAttribute("searchCriteria");			
	
			String company = (String) criteria.getSearchMap().get("driverCompany.name");
			String terminal = (String) criteria.getSearchMap().get("terminal.name");
			String driver = (String) criteria.getSearchMap().get("driver.fullName");
			String truck = (String) criteria.getSearchMap().get("truck.unit");
			String fuelcard = (String) criteria.getSearchMap().get("driverFuelCard.fuelcard");
			String transDateFrom = (String) criteria.getSearchMap().get("transDateFrom");
			String transDateTo = (String) criteria.getSearchMap().get("transDateTo");
			String gallonFrom = (String) criteria.getSearchMap().get("gallonsFrom");
			String gallonTo = (String) criteria.getSearchMap().get("gallonsTo");
	
	
			StringBuffer query = new StringBuffer("select obj from DriverFuelLog obj  where 1=1");
	
			if(!StringUtils.isEmpty(company)){
				query.append(" and obj.driverCompany.name in ('"+company+"')");
			}
	
			if(!StringUtils.isEmpty(terminal)){
				query.append(" and obj.terminal.name in ('"+terminal+"')");		
			}
	
			if(!StringUtils.isEmpty(driver)){
				query.append(" and obj.driver.fullName in ('"+driver+"')");    	
			}
    
			if(!StringUtils.isEmpty(truck)){
				query.append(" and obj.truck.unit in ("+truck+")");    	
			}
    
			if(!StringUtils.isEmpty(gallonFrom)){
				query.append(" and obj.gallons  >="+gallonFrom);    	
			}
    
			if(!StringUtils.isEmpty(gallonTo)){
				query.append(" and obj.gallons  <="+gallonTo);    	
			}
    
			if(!StringUtils.isEmpty(fuelcard)){
				query.append(" and obj.driverFuelCard.fuelcard in ("+fuelcard+")");    	
			}
    
			if(!StringUtils.isEmpty(transDateFrom)){
				try {
					query.append(" and obj.transactionDate  >='"+dateFormat.format(sdf.parse(transDateFrom))+"'");			
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	
			}
    
			if(!StringUtils.isEmpty(transDateTo)){
				try {
					query.append(" and obj.transactionDate  <='"+dateFormat.format(sdf.parse(transDateTo))+"'");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			query.append(" order by obj.transactionDate desc");
   
			List<DriverFuelLog>	driverFuellogList = genericDAO.executeSimpleQuery(query.toString());
			
			datas.put("data", driverFuellogList);
			datas.put("params",params);
			
			try {		
				
				
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= DriverFuelLogReview." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				
				if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("driverfuellogreview"+"pdf",
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("csv")) {
					out = dynamicReportService.generateStaticReport("driverfuellogreview",
							(List)datas.get("data"), params, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport("driverfuellogreview",
							(List)datas.get("data"), params, type, request);
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
