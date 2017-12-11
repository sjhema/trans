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
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.accident.AccidentCause;
import com.primovision.lutransport.model.accident.AccidentRoadCondition;
import com.primovision.lutransport.model.accident.AccidentType;
import com.primovision.lutransport.model.accident.AccidentWeather;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.injury.InjuryIncidentType;
import com.primovision.lutransport.model.injury.InjuryToType;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

public class WorkerCompUtils {
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
	
	public static final String ACCIDENT_MAIN_COL_INUSRANCE_COMPANY = "Insurance Co";
	public static final String ACCIDENT_MAIN_COL_CLAIM_NO = "Claim #";
	public static final String ACCIDENT_MAIN_COL_INCIDENT_DATE = "Accident Date";
	public static final String ACCIDENT_MAIN_COL_MONTHS_OF_SERVICE = "Mths of Service";
	public static final String ACCIDENT_MAIN_COL_HIRE_DATE = "Hire Date";
	public static final String ACCIDENT_MAIN_COL_DAY = "Day of week";
	public static final String ACCIDENT_MAIN_COL_LOCATION = "Location";
	public static final String ACCIDENT_MAIN_COL_STATE = "State";
	public static final String ACCIDENT_MAIN_COL_DRIVER_NAME = "Name";
	public static final String ACCIDENT_MAIN_COL_COMPANY = "Company";
	public static final String ACCIDENT_MAIN_COL_UNIT = "Unit";
	public static final String ACCIDENT_MAIN_COL_VEHICLE_DAMAGE = "Vehicle Damage";
	public static final String ACCIDENT_MAIN_COL_NO_INJURED = "# Injured";
	public static final String ACCIDENT_MAIN_COL_TOWED = "Towed";
	public static final String ACCIDENT_MAIN_COL_CITATION = "Citation";
	public static final String ACCIDENT_MAIN_COL_RECORDABLE = "Recordable";
	public static final String ACCIDENT_MAIN_COL_HM_RELEASE = "HM Release";
	public static final String ACCIDENT_MAIN_COL_ROAD = "Road";
	public static final String ACCIDENT_MAIN_COL_WEATHER = "Weather";
	public static final String ACCIDENT_MAIN_COL_CAUSE = "Cause";
	public static final String ACCIDENT_MAIN_COL_ACCIDENT_COMMENTS = "Notes";
	public static final String ACCIDENT_MAIN_COL_CLAIM_REP = "Claim Rep Contact Info";
	public static final String ACCIDENT_MAIN_COL_STATUS = "Status";
	public static final String ACCIDENT_MAIN_COL_SUBCONTRACTOR = "Subcontractor";
	
	public static final String ACCIDENT_REPORTED_COL_INUSRANCE_COMPANY = "Insurance";
	public static final String ACCIDENT_REPORTED_COL_CLAIM = "Claim #";
	public static final String ACCIDENT_REPORTED_COL_INCIDENT_DATE = "Date";
	public static final String ACCIDENT_REPORTED_COL_EMPLOYEE = "Employee";
	public static final String ACCIDENT_REPORTED_COL_STATUS = "Status";
	public static final String ACCIDENT_REPORTED_COL_PAID = "Paid";
	public static final String ACCIDENT_REPORTED_COL_DEDUCTIBLE = "Deductible";
	public static final String ACCIDENT_REPORTED_COL_EXPENSE = "Expense";
	public static final String ACCIDENT_REPORTED_COL_RESERVE = "Reserve";
	public static final String ACCIDENT_REPORTED_COL_TOTAL_COST = "Incurred";
	
	public static final String ACCIDENT_NOT_REPORTED_COL_NAME = "Last Name";
	public static final String ACCIDENT_NOT_REPORTED_COL_INCIDENT_DATE = "Incident Date";
	public static final String ACCIDENT_NOT_REPORTED_COL_TOTAL_COST = "Cost";
	
	public static Map<String, Integer> getAccidentMainColMapping() {
		Map<String, Integer> colMapping = new HashMap<String, Integer>();
		colMapping.put(ACCIDENT_MAIN_COL_INUSRANCE_COMPANY, 0);
		colMapping.put(ACCIDENT_MAIN_COL_CLAIM_NO, 1);
		colMapping.put(ACCIDENT_MAIN_COL_INCIDENT_DATE, 2);
		colMapping.put(ACCIDENT_MAIN_COL_MONTHS_OF_SERVICE, 3);
		colMapping.put(ACCIDENT_MAIN_COL_HIRE_DATE, 4);
		colMapping.put(ACCIDENT_MAIN_COL_DAY, 5);
		colMapping.put(ACCIDENT_MAIN_COL_LOCATION, 6);
		colMapping.put(ACCIDENT_MAIN_COL_STATE, 7);
		colMapping.put(ACCIDENT_MAIN_COL_DRIVER_NAME, 8);
		colMapping.put(ACCIDENT_MAIN_COL_COMPANY, 9);
		colMapping.put(ACCIDENT_MAIN_COL_UNIT, 10);
		colMapping.put(ACCIDENT_MAIN_COL_VEHICLE_DAMAGE, 11);
		colMapping.put(ACCIDENT_MAIN_COL_NO_INJURED, 12);
		colMapping.put(ACCIDENT_MAIN_COL_TOWED, 13);
		colMapping.put(ACCIDENT_MAIN_COL_CITATION, 14);
		colMapping.put(ACCIDENT_MAIN_COL_RECORDABLE, 15);
		colMapping.put(ACCIDENT_MAIN_COL_HM_RELEASE, 16);
		colMapping.put(ACCIDENT_MAIN_COL_ROAD, 17);
		colMapping.put(ACCIDENT_MAIN_COL_WEATHER, 18);
		colMapping.put(ACCIDENT_MAIN_COL_CAUSE, 19);
		colMapping.put(ACCIDENT_MAIN_COL_ACCIDENT_COMMENTS, 20);
		colMapping.put(ACCIDENT_MAIN_COL_CLAIM_REP, 23);
		colMapping.put(ACCIDENT_MAIN_COL_STATUS, 24);
		colMapping.put(ACCIDENT_MAIN_COL_SUBCONTRACTOR, 25);
		
		return colMapping;
	}
	
	public static Map<String, Integer> getAccidentReportedColMapping() {
		Map<String, Integer> colMapping = new HashMap<String, Integer>();
		colMapping.put(ACCIDENT_REPORTED_COL_INUSRANCE_COMPANY, 1);
		colMapping.put(ACCIDENT_REPORTED_COL_CLAIM, 2);
		colMapping.put(ACCIDENT_REPORTED_COL_INCIDENT_DATE, 3);
		colMapping.put(ACCIDENT_REPORTED_COL_EMPLOYEE, 4);
		colMapping.put(ACCIDENT_REPORTED_COL_STATUS, 5);
		colMapping.put(ACCIDENT_REPORTED_COL_PAID, 6);
		colMapping.put(ACCIDENT_REPORTED_COL_RESERVE, 7);
		colMapping.put(ACCIDENT_REPORTED_COL_EXPENSE, 8);
		colMapping.put(ACCIDENT_REPORTED_COL_TOTAL_COST, 9);
		colMapping.put(ACCIDENT_REPORTED_COL_DEDUCTIBLE, 10);
		
		return colMapping;
	}
	
	public static Map<String, Integer> getAccidentNotReportedColMapping() {
		Map<String, Integer> colMapping = new HashMap<String, Integer>();
		colMapping.put(ACCIDENT_NOT_REPORTED_COL_NAME, 1);
		colMapping.put(ACCIDENT_NOT_REPORTED_COL_INCIDENT_DATE, 2);
		colMapping.put(ACCIDENT_NOT_REPORTED_COL_TOTAL_COST, 4);
		
		return colMapping;
	}
	
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
	
	public static SubContractor retrieveSubcontractor(String subcontractorName, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(subcontractorName)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("name", subcontractorName);
	
		List<SubContractor> subcontractorList = genericDAO.findByCriteria(SubContractor.class, criterias, "id", true);
		if (subcontractorList == null || subcontractorList.isEmpty()) {
			return null;
		} else {
			return subcontractorList.get(0);
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
	
	public static Driver retrieveDriverByCommaSep(String nameWithComma, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(nameWithComma) || StringUtils.indexOf(nameWithComma, ",") < 0) {
			return null;
		}
		
		String nameTokens[] = nameWithComma.split("[,]", 2);
		String firstName = StringUtils.trimToEmpty(nameTokens[1]);
		String lastName = StringUtils.trimToEmpty(nameTokens[0]);
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
	
	
	public static Vehicle retrieveVehicleForUnit(String unit, Date transactionDate, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(unit) || !StringUtils.isNumeric(unit)) {
			return null;
		}
		
		String vehicleQuery = "Select obj from Vehicle obj where obj.unit=" + unit;
		if (transactionDate != null) {
			String transactionDateStr = requiredDateFormat.format(transactionDate);
			vehicleQuery += (" and obj.validFrom <='"
					+ transactionDateStr + "' and obj.validTo >= '" + transactionDateStr + "'");
		}		
		vehicleQuery += " order by obj.id DESC";
		
		System.out.println("******************** Vehicle query is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		return (vehicleList == null || vehicleList.isEmpty()) ? null : vehicleList.get(0);
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
	
	public static StaticData retrieveAccidentStatus(String statusStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(statusStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("dataType", "ACCIDENT_STATUS");
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
	
	public static State retrieveState(String stateStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(stateStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("name", stateStr);
	
		List<State> stateList = genericDAO.findByCriteria(State.class, criterias);
		if (stateList == null || stateList.isEmpty()) {
			return null;
		} else {
			return stateList.get(0);
		}
	}
	
	public static AccidentCause retrieveAccidentCause(String causeStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(causeStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("cause", causeStr);
	
		List<AccidentCause> accidentCauseList = genericDAO.findByCriteria(AccidentCause.class, criterias);
		if (accidentCauseList == null || accidentCauseList.isEmpty()) {
			return null;
		} else {
			return accidentCauseList.get(0);
		}
	}
	
	public static AccidentWeather retrieveAccidentWeather(String weatherStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(weatherStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("weather", weatherStr);
	
		List<AccidentWeather> accidentWeatherList = genericDAO.findByCriteria(AccidentWeather.class, criterias);
		if (accidentWeatherList == null || accidentWeatherList.isEmpty()) {
			return null;
		} else {
			return accidentWeatherList.get(0);
		}
	}
	
	public static AccidentRoadCondition retrieveAccidentRoadCondition(String roadConditionStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(roadConditionStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("roadCondition", roadConditionStr);
	
		List<AccidentRoadCondition> accidentRoadConditionList = genericDAO.findByCriteria(AccidentRoadCondition.class, criterias);
		if (accidentRoadConditionList == null || accidentRoadConditionList.isEmpty()) {
			return null;
		} else {
			return accidentRoadConditionList.get(0);
		}
	}
	
	public static AccidentType retrieveAccidentType(String accidentTypeStr, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(accidentTypeStr)) {
			return null;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("accidentType", accidentTypeStr);
	
		List<AccidentType> accidentTypeList = genericDAO.findByCriteria(AccidentType.class, criterias);
		if (accidentTypeList == null || accidentTypeList.isEmpty()) {
			return null;
		} else {
			return accidentTypeList.get(0);
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
	
	public static boolean checkDuplicateAccident(Accident anAccident, GenericDAO genericDAO) {
		Accident existingAccident = retrieveMatchingAccident(anAccident, genericDAO);
		return (existingAccident == null) ? false : true;
	}
	
	public static Accident retrieveMatchingAccident(Accident anAccident, GenericDAO genericDAO) {
		if (anAccident == null 
				|| anAccident.getIncidentDate() == null) {
			return null;
		}
		
		String query = "select obj from Accident obj where "
				+ " obj.incidentDate='" + requiredDateFormat.format(anAccident.getIncidentDate()) + "'";
		if (anAccident.getDriver() != null) {
			query += " and obj.driver.id=" + anAccident.getDriver().getId();
		}
		if (anAccident.getSubcontractor() != null) {
			query += " and obj.subcontractor.id=" + anAccident.getSubcontractor().getId();
		}
		if (anAccident.getInsuranceCompany() != null) {
			query += " and obj.insuranceCompany.id=" + anAccident.getInsuranceCompany().getId();
		}
		if (StringUtils.isNotEmpty(anAccident.getClaimNumber())) {
			query += " and obj.claimNumber='" + anAccident.getClaimNumber() + "'";
		}
		
		List<Accident> accidentList = genericDAO.executeSimpleQuery(query);
		return ((accidentList == null || accidentList.isEmpty()) ? null : accidentList.get(0));
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
	
	public static Injury retrieveInjuryByClaimNo(String claimNo, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(claimNo)) {
			return null;
		}
		
		String query = "select obj from Injury obj where obj.claimNumber='" + claimNo + "'";
		List<Injury> injuryList = genericDAO.executeSimpleQuery(query);
		return ((injuryList == null || injuryList.isEmpty()) ? null : injuryList.get(0));
	}
	
	public static Accident retrieveAccidentByClaimNo(String claimNo, GenericDAO genericDAO) {
		if (StringUtils.isEmpty(claimNo)) {
			return null;
		}
		
		String query = "select obj from Accident obj where obj.claimNumber='" + claimNo + "'";
		List<Accident> accidentList = genericDAO.executeSimpleQuery(query);
		return ((accidentList == null || accidentList.isEmpty()) ? null : accidentList.get(0));
	}
	
	
	public static String deriveDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		
		String dayOfWeek = dayOfWeekSDF.format(date);
		return dayOfWeek;
	}
}
