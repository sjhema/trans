package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.report.DriverMobileEntry;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/admin/drivermobileentry")
public class DriverMobileEntryController extends CRUDController<DriverMobileEntry> {
	
	
	public DriverMobileEntryController() {
		setUrlContext("admin/drivermobileentry");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	

	
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		criterias.clear();
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class,criterias, "fullName", false));
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.clear();		
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		
	}
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		
		// Driver mobile entry sort order fix - 29th Jun 2016
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"employeeName asc, entryDate asc",false));
		//model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"entryDate desc,employeeName",false));
		
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		String company = (String) criteria.getSearchMap().get("employeeCompanyName");
		String terminal = (String) criteria.getSearchMap().get("employeeTerminalName");
		String driver = (String) criteria.getSearchMap().get("employeeName");		
		String entryDateFrom = (String) criteria.getSearchMap().get("entryDateFrom");
		String entryDateTo = (String) criteria.getSearchMap().get("entryDateTo");		
		String entryStatus = (String) criteria.getSearchMap().get("entryStatus");
		
		
		
		StringBuffer query = new StringBuffer("select obj from DriverMobileEntry obj  where 1=1");
		StringBuffer countquery = new StringBuffer("select count(obj) from DriverMobileEntry obj  where 1=1");
		
		if(!StringUtils.isEmpty(company)){
			query.append(" and obj.employeeCompanyName in ('"+company+"')");
			countquery.append(" and obj.employeeCompanyName in ('"+company+"')");
		}
		
		if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.employeeTerminalName in ('"+terminal+"')");
			countquery.append(" and obj.employeeTerminalName in ('"+terminal+"')");
		}
		
        if(!StringUtils.isEmpty(driver)){
        	query.append(" and obj.employeeName in ('"+driver+"')");
        	countquery.append(" and obj.employeeName in ('"+driver+"')");
		}      
        
        boolean entryDateSearch = false;
        if(!StringUtils.isEmpty(entryDateFrom)){
      	  entryDateSearch = true;
        	try {
				query.append(" and obj.entryDate  >='"+dateFormat.format(sdf.parse(entryDateFrom))+"'");
				countquery.append(" and obj.entryDate  >='"+dateFormat.format(sdf.parse(entryDateFrom))+"'");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
		}
        
        if(!StringUtils.isEmpty(entryDateTo)){
      	  entryDateSearch = true;
        	try {
				query.append(" and obj.entryDate  <='"+dateFormat.format(sdf.parse(entryDateTo))+"'");
				countquery.append(" and obj.entryDate <='"+dateFormat.format(sdf.parse(entryDateTo))+"'");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}        
        
        if(!StringUtils.isEmpty(entryStatus)){
        	if(entryStatus.equals("entered")){
        		
        		query.append(" and (obj.tripsheet_flag='Y' OR obj.fuellog_flag='Y' OR obj.odometer_flag='Y')");
				countquery.append(" and (obj.tripsheet_flag='Y' OR obj.fuellog_flag='Y' OR obj.odometer_flag='Y')");
        		
        	}
        	else{
        		query.append(" and obj.tripsheet_flag is NULL and obj.fuellog_flag is NULL and obj.odometer_flag is NULL");
				countquery.append(" and obj.tripsheet_flag is NULL and obj.fuellog_flag is NULL and obj.odometer_flag is NULL");
        	}
		}
       
        // Driver mobile entry order fix
        //String entryDateOrderDir =  entryDateSearch ? "asc" : "desc";
        //query.append(" order by obj.entryDate " + entryDateOrderDir + " ,obj.employeeName asc");
        // Driver mobile entry sort order fix - 29th Jun 2016
        query.append(" order by obj.employeeName asc, obj.entryDate asc");
       
        
		
        Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
        		countquery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		
		model.addAttribute("list",genericDAO.getEntityManager().createQuery(query.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList());
		return urlContext + "/list";
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


		String company = (String) criteria.getSearchMap().get("employeeCompanyName");
		String terminal = (String) criteria.getSearchMap().get("employeeTerminalName");
		String driver = (String) criteria.getSearchMap().get("employeeName");		
		String entryDateFrom = (String) criteria.getSearchMap().get("entryDateFrom");
		String entryDateTo = (String) criteria.getSearchMap().get("entryDateTo");		
		String entryStatus = (String) criteria.getSearchMap().get("entryStatus");
		
		
		
		StringBuffer query = new StringBuffer("select obj from DriverMobileEntry obj  where 1=1");
		
		if(!StringUtils.isEmpty(company)){
			query.append(" and obj.employeeCompanyName in ('"+company+"')");			
		}
		
		if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.employeeTerminalName in ('"+terminal+"')");			
		}
		
        if(!StringUtils.isEmpty(driver)){
        	query.append(" and obj.employeeName in ('"+driver+"')");        	
		}      
        
        boolean entryDateSearch = false;
        if(!StringUtils.isEmpty(entryDateFrom)){
      	  entryDateSearch = true;
        	try {
				query.append(" and obj.entryDate  >='"+dateFormat.format(sdf.parse(entryDateFrom))+"'");				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
		}
        
        if(!StringUtils.isEmpty(entryDateTo)){
      	  entryDateSearch = true;
        	try {
				query.append(" and obj.entryDate  <='"+dateFormat.format(sdf.parse(entryDateTo))+"'");				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}        
        
        if(!StringUtils.isEmpty(entryStatus)){
        	if(entryStatus.equals("entered")){        		
        		query.append(" and (obj.tripsheet_flag='Y' OR obj.fuellog_flag='Y' OR obj.odometer_flag='Y')");        		
        	}
        	else{
        		query.append(" and obj.tripsheet_flag is NULL and obj.fuellog_flag is NULL and obj.odometer_flag is NULL");				
        	}
		}
       
        // Driver mobile entry order fix
        //String entryDateOrderDir =  entryDateSearch ? "asc" : "desc";
        //query.append(" order by obj.entryDate " + entryDateOrderDir + " ,obj.employeeName asc");
        // Driver mobile entry sort order fix - 29th Jun 2016
        query.append(" order by obj.employeeName asc, obj.entryDate asc");
        
       

List<DriverMobileEntry>	driverMobileEntry = genericDAO.executeSimpleQuery(query.toString());
List<DriverMobileEntry>	driverMobileEntryList = new ArrayList<DriverMobileEntry>();		
		
for(DriverMobileEntry obj:driverMobileEntry){
	DriverMobileEntry output = new DriverMobileEntry();
	output.setEmployeeCompanyName(obj.getEmployeeCompanyName());
	output.setEmployeeTerminalName(obj.getEmployeeTerminalName());
	output.setEmployeeName(obj.getEmployeeName());
	output.setTempEntryDate(sdf.format(obj.getEntryDate()));
	output.setTripsheet_flag(obj.getTripsheet_flag());
	output.setFuellog_flag(obj.getFuellog_flag());
	output.setOdometer_flag(obj.getOdometer_flag());
	output.setEnteredBy(obj.getEnteredBy());
	driverMobileEntryList.add(output);
}


		datas.put("data", driverMobileEntryList);
		datas.put("params",params);
		
		try {		
			
			
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= Driver Mobile Entry." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("drivermobileentry"+"pdf",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport("drivermobileentry",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("drivermobileentry",
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
