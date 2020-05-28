package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.util.logging.resources.logging;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.BusinessObject;
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.FuelSurchargeWeeklyRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.service.AuthenticationService;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/hr/payrollratealert")
public class PayrollRateAlertController extends BaseController {
	@Autowired
	private AuthenticationService authenticationService;

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	 
	@Autowired
	private GenericDAO genericDAO;
	
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	private boolean hasDriverPayRateAlertPriv(HttpServletRequest request) {
		String objectName = "Manage Driver Pay Rate Alert";
		StringBuffer query = new StringBuffer("select bo from BusinessObject bo where bo.status=1");
		query.append(" and bo.objectName='").append(objectName).append("'");
		List<BusinessObject> businessObjects = genericDAO.executeSimpleQuery(query.toString());
		
		BusinessObject bo = businessObjects.get(0);
		String url = bo.getUrl();
		//String url = "/hr/payrollratealert/driverpayrate";
		User user = getUser(request);
		return authenticationService.hasUserPermission(user, url);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		request.getSession().setAttribute("searchCriteria", null);
		initList(model, request);
		
		String type = request.getParameter("type");
		if (StringUtils.equals("all", type)) {
			if (!hasDriverPayRateAlertPriv(request)) {
				model.addAttribute("driverPayRateExprdCount", null);
				model.addAttribute("driverPayRateExprngCount", null);
				return "hr/payrollratealert/home";
			}
		
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.clear();
		   List<DriverPayRate> driverPayRateList = genericDAO.findByCriteria(DriverPayRate.class, criterias, "validFrom", true);
		   checkExpiredDriverPayRates(model, request, driverPayRateList);
			 
			return "hr/payrollratealert/home";
		} else if (StringUtils.equals("driverPayRate", type)) {
			if (!hasDriverPayRateAlertPriv(request)) {
				model.addAttribute("driverPayRateExprdCount", null);
				model.addAttribute("driverPayRateExprngCount", null);
				return "hr/payrollratealert/home";
			}
		
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.clear();
		   List<DriverPayRate> driverPayRateList = genericDAO.findByCriteria(DriverPayRate.class, criterias, "validFrom", true);
		   checkExpiredDriverPayRates(model, request, driverPayRateList);
			 
			return "hr/payrollratealert/driverPayRateAlert";
		}
			
		return "blank/blank";
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/search.do")
	public String search(ModelMap model, HttpServletRequest request) {		
		populateSearchCriteria(request, request.getParameterMap());				
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPageSize(10000);
		searchCriteria.getSearchMap().remove("type");
		initList(model,request);
		
		request.getSession().setAttribute("searchCriteria", null);
		String returnUrl = "blank/blank";
		if (request.getParameter("type").equals("driverPayRate")) {
			searchDriverPayRateAlert(model, request, searchCriteria);
			returnUrl = "hr/payrollratealert/driverPayRateAlert";
		}
		
		return returnUrl;
	}
	
	public void searchDriverPayRateAlert(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {		
		List<DriverPayRate> driverPayRateList = null;
		if (searchCriteria == null) {
			Map<String, Object> criterias = new HashMap<String, Object>();
			driverPayRateList = genericDAO.findByCriteria(DriverPayRate.class, criterias, "transferStation.name asc,landfill.name asc, validFrom", true);
		} else {
			driverPayRateList = genericDAO.search(DriverPayRate.class, searchCriteria, "transferStation.name asc,landfill.name asc,validFrom", true);				
			request.getSession().setAttribute("searchCriteria", null);
		}
		
	   checkExpiredDriverPayRates(model, request, driverPayRateList);
	}
	
	public void checkExpiredDriverPayRates(ModelMap model, HttpServletRequest request, List<DriverPayRate> driverPayRateList) {		
		int driverPayRateExprngCount = 0, driverPayRateExprdCount = 0;          
      List<DriverPayRate> expiredDriverPayRateList = new ArrayList<DriverPayRate>();
      for (DriverPayRate aDriverPayRate : driverPayRateList) {
			if(aDriverPayRate.getAlertStatus() == 0) {
				continue;
			}
			if (aDriverPayRate.getValidFrom().getTime() > new Date().getTime()) {
				continue;
			}
			
			int diffInDays = (int) ((aDriverPayRate.getValidTo().getTime()- new Date().getTime()) / (1000 * 60 * 60 * 24));		
			if (diffInDays <= 30 && diffInDays >= 0) {		
				driverPayRateExprngCount++;
				aDriverPayRate.setRateStatus("CURRENT");
				expiredDriverPayRateList.add(aDriverPayRate);
			} else if (diffInDays<0){
				driverPayRateExprdCount++;
				aDriverPayRate.setRateStatus("EXPIRED");
				expiredDriverPayRateList.add(aDriverPayRate);				
			}			
		}		
      
      Comparator<DriverPayRate> comparator= new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getCompany().getName().compareTo(o2.getCompany().getName());
			}
		};
			
		Comparator<DriverPayRate> comparator1 = new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getValidTo().compareTo(o2.getValidTo());
			}
		};
	        
		Comparator<DriverPayRate> comparator2=new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getTransferStation().getName().compareTo(o2.getTransferStation().getName());
			}
		};  
		
		Comparator<DriverPayRate> comparator3 = new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getLandfill().getName().compareTo(o2.getLandfill().getName());
			}
		};
		
		ComparatorChain chain = new ComparatorChain();  		
		chain.addComparator(comparator);
		chain.addComparator(comparator1);			
		chain.addComparator(comparator2);
		chain.addComparator(comparator3);
		Collections.sort(expiredDriverPayRateList, chain);
			
		model.addAttribute("expiredDriverPayRateList", expiredDriverPayRateList);		
		model.addAttribute("driverPayRateExprdCount", driverPayRateExprdCount);
		model.addAttribute("driverPayRateExprngCount", driverPayRateExprngCount);
	}
	
	public void initList(ModelMap model,HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 1);
		model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
	}
}




