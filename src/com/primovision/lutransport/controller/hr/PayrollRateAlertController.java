package com.primovision.lutransport.controller.hr;

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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.BaseController;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;

import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HourlyRate;

@Controller
@RequestMapping("/hr/payrollratealert")
public class PayrollRateAlertController extends BaseController {
	public PayrollRateAlertController() {
		setUrlContext("hr/payrollratealert");
	}
	
	String payrollRateAlertHomePg = "/home";
	String driverPayRateAlertPg = "/driverPayRateAlert";
	String hourlyPayRateAlertPg = "/hourlyPayRateAlert";
	
	private boolean hasDriverPayRateAlertPriv(HttpServletRequest request) {
		String objectName = "Manage Driver Pay Rate Alert";
		return hasPrivByBOName(request, objectName);
	}
	
	private boolean hasHourlyPayRateAlertPriv(HttpServletRequest request) {
		String objectName = "Manage Hourly Pay Rate Alert";
		return hasPrivByBOName(request, objectName);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		request.getSession().setAttribute("searchCriteria", null);
		initList(model, request);
		
		String returnUrl = getUrlContext() + payrollRateAlertHomePg;
		String type = getRequestType(request);
		if (StringUtils.equals("all", type)) {
			if (hasDriverPayRateAlertPriv(request)) {
				searchDriverPayRateAlert(model, request, null);
			}
			if (hasHourlyPayRateAlertPriv(request)) {
				searchHourlyPayRateAlert(model, request, null);
			}
		} else if (isDriverPayRateRequestAndHasPriv(type, request)) {
			searchDriverPayRateAlert(model, request, null);
			returnUrl = getUrlContext() + driverPayRateAlertPg;
		} else if (isHourlyPayRateRequestAndHasPriv(type, request)) {
			searchHourlyPayRateAlert(model, request, null);
			returnUrl = getUrlContext() + hourlyPayRateAlertPg;
		}
			
		return returnUrl;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/search.do")
	public String search(ModelMap model, HttpServletRequest request) {
		initList(model, request);
		
		populateSearchCriteria(request, request.getParameterMap());				
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPageSize(10000);
		searchCriteria.setPage(0);
		searchCriteria.getSearchMap().remove("type");
		request.getSession().setAttribute("searchCriteria", null);
		
		String returnUrl = "blank/blank";
		String type = getRequestType(request);
		if (isDriverPayRateRequestAndHasPriv(type, request)) {
			searchDriverPayRateAlert(model, request, searchCriteria);
			returnUrl = getUrlContext() + driverPayRateAlertPg;
		} else if (isHourlyPayRateRequestAndHasPriv(type, request)) {
			searchHourlyPayRateAlert(model, request, searchCriteria);
			returnUrl = getUrlContext() + hourlyPayRateAlertPg;
		}
	
		return returnUrl;
	}
	
	private String getRequestType(HttpServletRequest request) {
		return request.getParameter("type");
	}
	
	private boolean isDriverPayRateRequestAndHasPriv(String type, HttpServletRequest request) {
		return (StringUtils.equals("driverPayRate", type) && hasDriverPayRateAlertPriv(request));
	}
	
	private boolean isHourlyPayRateRequestAndHasPriv(String type, HttpServletRequest request) {
		return (StringUtils.equals("hourlyPayRate", type) && hasHourlyPayRateAlertPriv(request));
	}
	
	private List<DriverPayRate> retrieveDriverPayRates(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {		
		List<DriverPayRate> driverPayRateList = null;
		String sortBy = "company.name asc,transferStation.name asc,landfill.name asc,validTo";
		if (searchCriteria == null) {
			Map<String, Object> criterias = new HashMap<String, Object>();
			driverPayRateList = genericDAO.findByCriteria(DriverPayRate.class, criterias, sortBy, true);
		} else {
			driverPayRateList = genericDAO.search(DriverPayRate.class, searchCriteria, sortBy, true);				
		}
		return driverPayRateList;
	}
	
	private List<HourlyRate> retrieveHourlyPayRates(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {		
		List<HourlyRate> hourlyPayRateList = null;
		String sortBy = "driver.lastName asc,driver.firstName asc,validFrom desc,validTo";
		if (searchCriteria == null) {
			Map<String, Object> criterias = new HashMap<String, Object>();
			hourlyPayRateList = genericDAO.findByCriteria(HourlyRate.class, criterias, sortBy, true);
		} else {
			hourlyPayRateList = genericDAO.search(HourlyRate.class, searchCriteria, sortBy, true);				
		}
		return hourlyPayRateList;
	}
	
	private void searchDriverPayRateAlert(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {		
		List<DriverPayRate> driverPayRateList = retrieveDriverPayRates(model, request, searchCriteria);
		populateDriverPayRateAlert(model, driverPayRateList);
	}
	
	private void searchHourlyPayRateAlert(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {		
		List<HourlyRate> hourlyPayRateList = retrieveHourlyPayRates(model, request, searchCriteria);
		populateHourlyPayRateAlert(model, hourlyPayRateList);
	}
	
	private void populateDriverPayRateAlert(ModelMap model, List<DriverPayRate> driverPayRateList) {
		List<DriverPayRate> expiredDriverPayRateList = new ArrayList<DriverPayRate>();
		model.addAttribute("expiredDriverPayRateList", expiredDriverPayRateList);	
	
		if (driverPayRateList == null || driverPayRateList.isEmpty()) {
			return;
		}
		
		int driverPayRateExprngCount = 0, driverPayRateExprdCount = 0;
      for (DriverPayRate aDriverPayRate : driverPayRateList) {
			if (aDriverPayRate.getAlertStatus() == 0) {
				continue;
			}
			
			long currentTime = new Date().getTime();
			if (aDriverPayRate.getValidFrom().getTime() > currentTime) {
				continue;
			}
			
			int diffInDays = (int) ((aDriverPayRate.getValidTo().getTime()- currentTime) / (1000 * 60 * 60 * 24));		
			if (diffInDays >= 0 && diffInDays <= 30) {		
				driverPayRateExprngCount++;
				aDriverPayRate.setRateStatus("CURRENT");
				expiredDriverPayRateList.add(aDriverPayRate);
			} else if (diffInDays < 0) {
				driverPayRateExprdCount++;
				aDriverPayRate.setRateStatus("EXPIRED");
				expiredDriverPayRateList.add(aDriverPayRate);				
			}			
      }		
      
      sortDriverPayRate(expiredDriverPayRateList);
			
		model.addAttribute("driverPayRateExprdCount", driverPayRateExprdCount);
		model.addAttribute("driverPayRateExprngCount", driverPayRateExprngCount);
	}
	
	private void populateHourlyPayRateAlert(ModelMap model, List<HourlyRate> hourlyPayRateList) {
		List<HourlyRate> expiredHourlyPayRateList = new ArrayList<HourlyRate>();
		model.addAttribute("expiredHourlyPayRateList", expiredHourlyPayRateList);	
	
		if (hourlyPayRateList == null || hourlyPayRateList.isEmpty()) {
			return;
		}
		
		int hourlyPayRateExprngCount = 0, hourlyPayRateExprdCount = 0;
      for (HourlyRate aHourlyPayRate : hourlyPayRateList) {
			if (aHourlyPayRate.getAlertStatus() == 0) {
				continue;
			}
			
			long currentTime = new Date().getTime();
			if (aHourlyPayRate.getValidFrom().getTime() > currentTime) {
				continue;
			}
			
			int diffInDays = (int) ((aHourlyPayRate.getValidTo().getTime()- currentTime) / (1000 * 60 * 60 * 24));		
			if (diffInDays >= 0 && diffInDays <= 30) {		
				hourlyPayRateExprngCount++;
				aHourlyPayRate.setRateStatus("CURRENT");
				expiredHourlyPayRateList.add(aHourlyPayRate);
			} else if (diffInDays < 0) {
				hourlyPayRateExprdCount++;
				aHourlyPayRate.setRateStatus("EXPIRED");
				expiredHourlyPayRateList.add(aHourlyPayRate);				
			}			
      }		
      
      sortHourlyPayRate(expiredHourlyPayRateList);
			
		model.addAttribute("hourlyPayRateExprdCount", hourlyPayRateExprdCount);
		model.addAttribute("hourlyPayRateExprngCount", hourlyPayRateExprngCount);
	}
	
	private void sortDriverPayRate(List<DriverPayRate> driverPayRateList) {
		Comparator<DriverPayRate> compComparator = new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getCompany().getName().compareTo(o2.getCompany().getName());
			}
		};
		        
		Comparator<DriverPayRate> transferStationComparator = new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getTransferStation().getName().compareTo(o2.getTransferStation().getName());
			}
		};  
			
		Comparator<DriverPayRate> landfillComparator = new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getLandfill().getName().compareTo(o2.getLandfill().getName());
			}
		};
			
		Comparator<DriverPayRate> validToComparator = new Comparator<DriverPayRate>() {
			@Override
			public int compare(DriverPayRate o1, DriverPayRate o2) {
				return  o1.getValidTo().compareTo(o2.getValidTo());
			}
		};
			
		ComparatorChain chain = new ComparatorChain();  		
		chain.addComparator(compComparator);
		chain.addComparator(transferStationComparator);
		chain.addComparator(landfillComparator);
		chain.addComparator(validToComparator);
		Collections.sort(driverPayRateList, chain);
	}
	
	private void sortHourlyPayRate(List<HourlyRate> hourlyPayRateList) {
		 Comparator<HourlyRate> driverLastNameComparator = new Comparator<HourlyRate>() {
				@Override
				public int compare(HourlyRate o1, HourlyRate o2) {
					return  o1.getDriver().getLastName().compareTo(o2.getDriver().getLastName());
				}
		 };
		 
		 Comparator<HourlyRate> driverFirstNameComparator = new Comparator<HourlyRate>() {
				@Override
				public int compare(HourlyRate o1, HourlyRate o2) {
					return  o1.getDriver().getFirstName().compareTo(o2.getDriver().getFirstName());
				}
		 };
		 
		 Comparator<HourlyRate> validFromComparator = new Comparator<HourlyRate>() {
				@Override
				public int compare(HourlyRate o1, HourlyRate o2) {
					return  o2.getValidFrom().compareTo(o1.getValidFrom());
				}
		 };
				
		 Comparator<HourlyRate> validToComparator = new Comparator<HourlyRate>() {
				@Override
				public int compare(HourlyRate o1, HourlyRate o2) {
					return  o1.getValidTo().compareTo(o2.getValidTo());
				}
		 };
		        
		 ComparatorChain chain = new ComparatorChain();  		
		 chain.addComparator(driverLastNameComparator);
		 chain.addComparator(driverFirstNameComparator);			
		 chain.addComparator(validFromComparator);
		 chain.addComparator(validToComparator);
		 Collections.sort(hourlyPayRateList, chain);
	}
	
	private void initList(ModelMap model,HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 1);
		model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name", false));
		
		/*String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		criterias.clear();
		model.addAttribute("categories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias, "name",false));*/

		model.addAttribute("expiredDriverPayRateList", null);
		model.addAttribute("driverPayRateExprdCount", null);
		model.addAttribute("driverPayRateExprngCount", null);
		
		model.addAttribute("expiredHourlyPayRateList", null);
		model.addAttribute("hourlyPayRateExprdCount", null);
		model.addAttribute("hourlyPayRateExprngCount", null);
	}
}




