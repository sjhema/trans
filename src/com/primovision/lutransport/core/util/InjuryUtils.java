package com.primovision.lutransport.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.core.dao.GenericDAO;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.StaticData;

import com.primovision.lutransport.model.hr.EmployeeCatagory;

import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.injury.InjuryIncidentType;
import com.primovision.lutransport.model.injury.InjuryToType;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

public class InjuryUtils {
	public static SimpleDateFormat injuryTimeOfDayFormat1 = new SimpleDateFormat("hh:mm");
	public static SimpleDateFormat injuryTimeOfDayFormat2 = new SimpleDateFormat("hh:mm a");
	public static SimpleDateFormat requiredTimeFormat = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat requiredDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dayOfWeekSDF = new SimpleDateFormat("E");
	
	public static final String INJURY_MAIN_COL_INUSRANCE_COMPANY = "Insurance Company";
	public static final String INJURY_MAIN_COL_CLAIM_NO = "Claim #";
	public static final String INJURY_MAIN_COL_LAST_NAME = "Last Name";
	public static final String INJURY_MAIN_COL_FIRST_NAME = "First Name";
	public static final String INJURY_MAIN_COL_AGE = "Age";
	public static final String INJURY_MAIN_COL_MONTHS_OF_SERVICE = "Months of Service";
	public static final String INJURY_MAIN_COL_COMPANY = "Company";
	public static final String INJURY_MAIN_COL_POSITION = "Position";
	public static final String INJURY_MAIN_COL_INCIDENT_DATE = "Incident Date";
	public static final String INJURY_MAIN_COL_DAY = "Day";
	public static final String INJURY_MAIN_COL_RETURN_TO_WORK = "Return to Work";
	public static final String INJURY_MAIN_COL_TIME_OF_DAY = "Time of day";
	public static final String INJURY_MAIN_COL_AM_PM = "AM/PM";
	public static final String INJURY_MAIN_COL_LOCATION = "Location";
	public static final String INJURY_MAIN_COL_INJURY_COMMENTS = "Injury  comments";
	public static final String INJURY_MAIN_COL_INCIDENT_TYPE = "Incident type";
	public static final String INJURY_MAIN_COL_INJURY_TO = "Injury to";
	public static final String INJURY_MAIN_COL_LOST_WORK_DAYS = "# lost work days ";
	public static final String INJURY_MAIN_COL_TARP_RELATED_INJURY = "Tarp Related Injury";
	public static final String INJURY_MAIN_COL_FIRST_INJURY = "1st. Rep of Injury";
	public static final String INJURY_MAIN_COL_CLAIM_REP = "Claim Rep Contact Info";
	public static final String INJURY_MAIN_COL_STATUS = "Status";
	
	public static final String INJURY_REPORTED_COL_INUSRANCE_COMPANY = "Insurance";
	public static final String INJURY_REPORTED_COL_EMPLOYEE = "Employee";
	public static final String INJURY_REPORTED_COL_EMPLOYED = "Employed";
	public static final String INJURY_REPORTED_COL_WORKING = "Working";
	public static final String INJURY_REPORTED_COL_INCIDENT_DATE = "Date";
	public static final String INJURY_REPORTED_COL_STATUS = "Status";
	public static final String INJURY_REPORTED_COL_MEDICAL = "Medical";
	public static final String INJURY_REPORTED_COL_INDEMNITY = "Indemnity";
	public static final String INJURY_REPORTED_COL_EXPENSE = "Expense";
	public static final String INJURY_REPORTED_COL_RESERVE = "Reserve";
	public static final String INJURY_REPORTED_COL_TOTAL_PAID = "Total Paid ";
	public static final String INJURY_REPORTED_COL_TOTAL_CLAIM = "Total Claim";
	public static final String INJURY_REPORTED_COL_ATTORNEY = "Attorney";
	
	public static final String INJURY_NOT_REPORTED_COL_LAST_NAME = "Last Name";
	public static final String INJURY_NOT_REPORTED_COL_FIRST_NAME = "First Name";
	public static final String INJURY_NOT_REPORTED_COL_INJURY_DATE = "Injury Date";
	public static final String INJURY_NOT_REPORTED_COL_RETURN_TO_WORK_DATE = "Return to work Date";
	public static final String INJURY_NOT_REPORTED_COL_LOST_DAYS = "Lost days";
	public static final String INJURY_NOT_REPORTED_COL_MEDICAL = "Medical";
	public static final String INJURY_NOT_REPORTED_COL_INDEMNITY = "Indemnity";
	public static final String INJURY_NOT_REPORTED_COL_TOTAL_PAID = "Total Paid ";
	
	public static Map<String, Integer> getInjuryNotReportedColMapping() {
		Map<String, Integer> colMapping = new HashMap<String, Integer>();
		colMapping.put(INJURY_NOT_REPORTED_COL_LAST_NAME, 1);
		colMapping.put(INJURY_NOT_REPORTED_COL_FIRST_NAME, 2);
		colMapping.put(INJURY_NOT_REPORTED_COL_INJURY_DATE, 3);
		colMapping.put(INJURY_NOT_REPORTED_COL_RETURN_TO_WORK_DATE, 4);
		colMapping.put(INJURY_NOT_REPORTED_COL_LOST_DAYS, 5);
		colMapping.put(INJURY_NOT_REPORTED_COL_MEDICAL, 6);
		colMapping.put(INJURY_NOT_REPORTED_COL_INDEMNITY, 7);
		colMapping.put(INJURY_NOT_REPORTED_COL_TOTAL_PAID, 8);
		
		return colMapping;
	}
	
	public static Map<String, Integer> getInjuryReportedColMapping() {
		Map<String, Integer> colMapping = new HashMap<String, Integer>();
		colMapping.put(INJURY_REPORTED_COL_INUSRANCE_COMPANY, 1);
		colMapping.put(INJURY_REPORTED_COL_EMPLOYEE, 2);
		colMapping.put(INJURY_REPORTED_COL_EMPLOYED, 3);
		colMapping.put(INJURY_REPORTED_COL_WORKING, 4);
		colMapping.put(INJURY_REPORTED_COL_INCIDENT_DATE, 5);
		colMapping.put(INJURY_REPORTED_COL_STATUS, 6);
		colMapping.put(INJURY_REPORTED_COL_MEDICAL, 7);
		colMapping.put(INJURY_REPORTED_COL_INDEMNITY, 8);
		colMapping.put(INJURY_REPORTED_COL_EXPENSE, 9);
		colMapping.put(INJURY_REPORTED_COL_RESERVE, 10);
		colMapping.put(INJURY_REPORTED_COL_TOTAL_PAID, 11);
		colMapping.put(INJURY_REPORTED_COL_TOTAL_CLAIM, 12);
		colMapping.put(INJURY_REPORTED_COL_ATTORNEY, 13);
		
		return colMapping;
	}
	
	public static Map<String, Integer> getInjuryMainColMapping() {
		Map<String, Integer> colMapping = new HashMap<String, Integer>();
		colMapping.put(INJURY_MAIN_COL_INUSRANCE_COMPANY, 0);
		colMapping.put(INJURY_MAIN_COL_CLAIM_NO, 1);
		colMapping.put(INJURY_MAIN_COL_LAST_NAME, 2);
		colMapping.put(INJURY_MAIN_COL_FIRST_NAME, 3);
		colMapping.put(INJURY_MAIN_COL_AGE, 4);
		colMapping.put(INJURY_MAIN_COL_MONTHS_OF_SERVICE, 5);
		colMapping.put(INJURY_MAIN_COL_COMPANY, 6);
		colMapping.put(INJURY_MAIN_COL_POSITION, 7);
		colMapping.put(INJURY_MAIN_COL_INCIDENT_DATE, 8);
		colMapping.put(INJURY_MAIN_COL_DAY, 9);
		colMapping.put(INJURY_MAIN_COL_RETURN_TO_WORK, 10);
		colMapping.put(INJURY_MAIN_COL_TIME_OF_DAY, 11);
		colMapping.put(INJURY_MAIN_COL_AM_PM, 12);
		colMapping.put(INJURY_MAIN_COL_LOCATION, 13);
		colMapping.put(INJURY_MAIN_COL_INJURY_COMMENTS, 14);
		colMapping.put(INJURY_MAIN_COL_INCIDENT_TYPE, 15);
		colMapping.put(INJURY_MAIN_COL_INJURY_TO, 16);
		colMapping.put(INJURY_MAIN_COL_LOST_WORK_DAYS, 17);
		colMapping.put(INJURY_MAIN_COL_TARP_RELATED_INJURY, 18);
		colMapping.put(INJURY_MAIN_COL_FIRST_INJURY, 19);
		colMapping.put(INJURY_MAIN_COL_CLAIM_REP, 20);
		colMapping.put(INJURY_MAIN_COL_STATUS, 21);
		
		return colMapping;
	}
	
	public static InsuranceCompany retrieveInsuranceCompanyByName(String inuranceCompanyStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(inuranceCompanyStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("name", inuranceCompanyStr);
	
		List<InsuranceCompany> inuranceCompanyList = genericDAO.findByCriteria(InsuranceCompany.class, criterias);
		if (inuranceCompanyList == null || inuranceCompanyList.isEmpty()) {
			return null;
		} else {
			return inuranceCompanyList.get(0);
		}
	}
	
	public static Driver retrieveDriver(String firstName, String lastName, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("firstName", firstName);
		criterias.put("lastName", lastName);
	
		List<Driver> driverList = genericDAO.findByCriteria(Driver.class, criterias, "id", true);
		if (driverList == null || driverList.isEmpty()) {
			return null;
		} else {
			return driverList.get(0);
		}
	}
	
	public static Driver retrieveDriver(String fullName, GenericDAO genericDAO, boolean reverseName) {
		if (StringUtils.isEmpty(fullName)) {
			return null;
		}
		
		String searchName = fullName;
		if (reverseName) {
			String nameTokens[] = searchName.split("[\\s]", 2);
			if (nameTokens.length != 2) {
				return null;
			}
			searchName = nameTokens[1] + " " + nameTokens[0];
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("fullName", searchName);
	
		List<Driver> driverList = genericDAO.findByCriteria(Driver.class, criterias, "id", true);
		if (driverList == null || driverList.isEmpty()) {
			return null;
		} else {
			return driverList.get(0);
		}
	}
	
	public static List<Location> retrieveCompanyTerminal(String companyTerminalName, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(companyTerminalName)) {
			return null;
		}
		
		String nameTokens[] = companyTerminalName.split("[\\s]", 2);
		if (nameTokens.length < 2) {
			return null;
		}
		
		if (StringUtils.equals(nameTokens[0], "UDS")) {
			nameTokens[0] = "United Disposal";
		} else if (StringUtils.equals(nameTokens[0], "AAA")) {
			nameTokens[0] = "Triple A";
		} else if (StringUtils.equals(nameTokens[0], "DB")) {
			nameTokens[0] = "DB Transport";
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("status", 1);
		
		criterias.put("type", 3);
		criterias.put("name", nameTokens[0]);
		List<Location> companyList = genericDAO.findByCriteria(Location.class, criterias, "name", false);
		if (companyList == null || companyList.isEmpty()) {
			return null;
		}
		
		criterias.clear();
		criterias.put("type", 4);
		criterias.put("code", nameTokens[1]);
		List<Location> terminalList = genericDAO.findByCriteria(Location.class, criterias, "name", false);
		if (terminalList == null || terminalList.isEmpty()) {
			return null;
		}
		
		List<Location> locationList = new ArrayList<Location>();
		locationList.add(companyList.get(0));
		locationList.add(terminalList.get(0));
		
		return locationList;
	}
	
	public static Location retrieveInjuryLocation(String locationName, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(locationName)) {
			return null;
		}
		
		List<Location> locationList = retrieveLocation(1, locationName, genericDAO);
		if (locationList == null || locationList.isEmpty()) {
			locationList = retrieveLocation(2, locationName, genericDAO);
		}
		
		return (locationList == null || locationList.isEmpty()) ? null : locationList.get(0);
	}
	
	public static List<Location> retrieveLocation(int locationType, String locationName, GenericDAO genericDAO) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("status", 1);
		criterias.put("type", locationType);
		criterias.put("name", locationName);
		return genericDAO.findByCriteria(Location.class, criterias, "name", false);
	}
	
	public static EmployeeCatagory retrieveEmployeeCategory(String categoryName, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(categoryName)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("name", categoryName);
	
		List<EmployeeCatagory> categoryList = genericDAO.findByCriteria(EmployeeCatagory.class, criterias, "id", true);
		if (categoryList == null || categoryList.isEmpty()) {
			return null;
		} else {
			return categoryList.get(0);
		}
	}
	
	public static InsuranceCompanyRep retrieveClaimRep(String claimRepName, InsuranceCompany insuranceCompany,
			GenericDAO genericDAO) {
		if (StringUtils.isEmpty(claimRepName) 
				|| StringUtils.contains(claimRepName, "-")
				|| StringUtils.contains(claimRepName, "@")) {
			return null;
		}
		
		String claimRepQuery = "select obj from InsuranceCompanyRep obj where obj.name='" + claimRepName + "'" 
				+ " and obj.insuranceCompany.id=" + insuranceCompany.getId();
		
		List<InsuranceCompanyRep> claimRepList = genericDAO.executeSimpleQuery(claimRepQuery);
		if (claimRepList == null || claimRepList.isEmpty()) {
			return null;
		} else {
			return claimRepList.get(0);
		}
	}
	
	public static StaticData retrieveInjuryStatus(String statusStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(statusStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("dataType", "INJURY_STATUS");
		criterias.put("dataText", statusStr);
	
		List<StaticData> staticDataList = genericDAO.findByCriteria(StaticData.class, criterias);
		if (staticDataList == null || staticDataList.isEmpty()) {
			return null;
		} else {
			return staticDataList.get(0);
		}
	}
	
	public static InjuryIncidentType retrieveIncidentType(String incidentTypeStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(incidentTypeStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("incidentType", incidentTypeStr);
	
		List<InjuryIncidentType> incidentTypeList = genericDAO.findByCriteria(InjuryIncidentType.class, criterias);
		if (incidentTypeList == null || incidentTypeList.isEmpty()) {
			return null;
		} else {
			return incidentTypeList.get(0);
		}
	}
	
	public static InjuryToType retrieveInjuryToType(String injuryToStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(injuryToStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("injuryTo", injuryToStr);
	
		List<InjuryToType> injuryToTypeList = genericDAO.findByCriteria(InjuryToType.class, criterias);
		if (injuryToTypeList == null || injuryToTypeList.isEmpty()) {
			return null;
		} else {
			return injuryToTypeList.get(0);
		}
	}
	
	public static String deriveIncidentTime(Object timeOfDayObj, String amPMStr) {
		if (timeOfDayObj == null  || StringUtils.isEmpty(amPMStr)) {
			return StringUtils.EMPTY;
		} 
		
		String incidentTimeAMPMStr = StringUtils.EMPTY;
		if ((timeOfDayObj instanceof Date)) {
			incidentTimeAMPMStr = injuryTimeOfDayFormat1.format(timeOfDayObj);
		} else {
			incidentTimeAMPMStr = timeOfDayObj.toString();
		}
		
		incidentTimeAMPMStr += (" " + amPMStr);
		
		Date incidentTime = null;
		try {
			incidentTime = injuryTimeOfDayFormat2.parse(incidentTimeAMPMStr);
		} catch (ParseException e) {
			return StringUtils.EMPTY;
		}
		
		String requiredIncidentTimeStr = StringUtils.EMPTY;
		if (incidentTime != null) {
			requiredIncidentTimeStr = requiredTimeFormat.format(incidentTime);
		}
		return requiredIncidentTimeStr;
	}
	
	public static boolean checkDuplicateInjury(Injury anInjury, GenericDAO genericDAO) {
		Injury existingInjury = retrieveMatchingInjury(anInjury, genericDAO);
		return (existingInjury == null) ? false : true;
	}
	
	public static Injury retrieveMatchingInjury(Injury anInjury, GenericDAO genericDAO) {
		if (anInjury == null || anInjury.getDriver() == null
				|| anInjury.getIncidentDate() == null) {
			return null;
		}
		
		String query = "select obj from Injury obj where obj.driver.id=" + anInjury.getDriver().getId()
				+ " and obj.incidentDate='" + requiredDateFormat.format(anInjury.getIncidentDate()) + "'";
		if (anInjury.getInsuranceCompany() != null) {
			query += " and obj.insuranceCompany.id=" + anInjury.getInsuranceCompany().getId();
		}	
		
		List<Injury> injuryList = genericDAO.executeSimpleQuery(query);
		return ((injuryList == null || injuryList.isEmpty()) ? null : injuryList.get(0));
	}
	
	public static String deriveDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		
		String dayOfWeek = dayOfWeekSDF.format(date);
		return dayOfWeek;
	}
}
