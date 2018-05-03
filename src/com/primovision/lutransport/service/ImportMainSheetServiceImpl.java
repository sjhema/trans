package com.primovision.lutransport.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.util.WorkerCompUtils;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.TicketUtils;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.ErrorData;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelDiscount;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.LocationDistance;
import com.primovision.lutransport.model.LocationPair;
import com.primovision.lutransport.model.MileageLog;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehiclePermit;
import com.primovision.lutransport.model.VehicleTollTag;
import com.primovision.lutransport.model.WMInvoice;
import com.primovision.lutransport.model.WMLocation;
import com.primovision.lutransport.model.WMTicket;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.accident.AccidentCause;
import com.primovision.lutransport.model.accident.AccidentRoadCondition;
import com.primovision.lutransport.model.accident.AccidentWeather;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.Odometer;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.SetupData;
import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.injury.InjuryIncidentType;
import com.primovision.lutransport.model.injury.InjuryToType;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;
import com.primovision.lutransport.model.report.MileageLogReportInput;

@Transactional(readOnly = true)
public class ImportMainSheetServiceImpl implements ImportMainSheetService {

	public static SimpleDateFormat drvdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

	private static Logger log = Logger.getLogger(ImportMainSheetServiceImpl.class.getName());
	private static SimpleDateFormat sdf = new SimpleDateFormat("mm-DD-yy");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
	
	public static SimpleDateFormat mileageSearchDateFormat = new SimpleDateFormat("MMMMM yyyy");
	
	@Autowired
	protected GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	@Autowired
	private ReportService reportService;

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getAsIntegerString(String value) {
		return value.replaceAll("\\.\\d*$", "");
	}

	private Date getAsDate(String validCellValue) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.parse(validCellValue);
	}
	
	private void validateAndResetTollTagAndPlateNumber(HSSFRow row, EzToll ezToll) {
		String tollNum = getCellValue(row.getCell(3)).toString();
		if (StringUtils.isEmpty(tollNum)) {
			return;
		} 
		
		String tollQuery = "select obj from VehicleTollTag obj where obj.tollTagNumber='" + tollNum + "'";
		if (ezToll.getToolcompany() != null) {
			tollQuery += " and obj.tollCompany='" + ezToll.getToolcompany().getId() + "'";
		}
		
		List<VehicleTollTag> vehicleTollTags = genericDAO.executeSimpleQuery(tollQuery);
		if (vehicleTollTags != null && vehicleTollTags.size() > 0) {
			String plateNum = getCellValue(row.getCell(4)).toString();
			if (StringUtils.contains(tollNum, plateNum) || StringUtils.contains(plateNum, tollNum)) {
				row.getCell(4).setCellValue(StringUtils.EMPTY);
			}
		} else {
			row.getCell(3).setCellValue(StringUtils.EMPTY);
		}
	}
	
	private Vehicle retrieveVehicleForPlate(String plateNum, HSSFRow row) {
		String transactiondate = StringUtils.EMPTY;
		if (validDate(getCellValue(row.getCell(6)))) {
			transactiondate = dateFormat.format(((Date) getCellValue(row.getCell(6))).getTime());
		}
		
		String vehicleQuery = "select obj from Vehicle obj where obj.plate='" + plateNum + "'" 
				+ " and obj.validFrom <='" + transactiondate + "' and obj.validTo >= '" + transactiondate + "'";
		System.out.println("******* vehicleQuery  ======>" + vehicleQuery);
		
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery.toString());
		if (vehicleList == null || vehicleList.isEmpty()) {
			return null;
		} else {
			return vehicleList.get(0);
		}	
	}
	
	private List<Vehicle> retrieveVehicle(VehicleTollTag vehicletolltag, String transactionDate) throws ParseException {
		/***Correction for unit no. mapping to multiple vehicle ids***/
		/*String vehicleQuery = "Select obj from Vehicle obj where obj.id="
				+ vehicletolltag.getVehicle().getId() + " and obj.validFrom <='"
				+ transactionDate + "' and obj.validTo >= '" + transactionDate + "'";
		System.out.println("******************** First vehicle query is " + vehicleQuery);
		
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList == null || vehicleList.isEmpty()) {
			vehicleQuery = "Select obj from Vehicle obj where obj.unit="
					+ vehicletolltag.getVehicle().getUnit() + " and obj.validFrom <='"
					+ transactionDate + "' and obj.validTo >= '" + transactionDate + "' order by obj.id DESC";
			System.out.println("******************** Second vehicle query is " + vehicleQuery);
			
			vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		}*/
		
		List<Vehicle> vehicleList = retrieveVehicleForUnit(vehicletolltag.getVehicle().getUnit(), transactionDate);
		if (vehicleList.size() <= 1) {
			return vehicleList;
		}
		
		List<Ticket> ticketList = getTicketsForVehicle(vehicleList, transactionDate);
		vehicleList = new ArrayList<Vehicle>();
		if (ticketList.isEmpty()) {
			return vehicleList;
		}
		vehicleList.add(ticketList.get(0).getVehicle());
		
		return vehicleList;
	}
	
	private List<Vehicle> retrieveVehicleForUnit(Integer unit, String transactionDate) {
		String vehicleQuery = "Select obj from Vehicle obj where obj.unit="
				+ unit + " and obj.validFrom <='"
				+ transactionDate + "' and obj.validTo >= '" + transactionDate + "'"
				+ " order by obj.id DESC";
		System.out.println("******************** Vehicle query is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		return vehicleList;
	}
	
	private List<Vehicle> retrieveVehicleForUnit(Integer unit, Date transactionDate, int type) {
		if (unit == null || transactionDate == null) {
			return null;
		}
		
		String transactionDateStr = dateFormat.format(transactionDate);
		
		String vehicleQuery = "Select obj from Vehicle obj where obj.unit="
				+ unit + " and obj.validFrom <='"
				+ transactionDateStr + "' and obj.validTo >= '" + transactionDateStr + "'"
				+ " and obj.type=" + type
				+ " order by obj.id DESC";
		System.out.println("******************** Vehicle query is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		return vehicleList;
	}
	
	private List<StaticData> retrieveStaticData(String staticDataType, String dataText) {
		Map criterias = new HashMap();
		criterias.put("dataType", staticDataType);
		criterias.put("dataText", dataText);
		return genericDAO.findByCriteria(StaticData.class, criterias);
	}
	
	private Location retrieveLocationById(String locationIdStr) {
		if (StringUtils.isEmpty(locationIdStr)) {
			return null;
		}
		return retrieveLocationById(Long.valueOf(locationIdStr));
	}
	
	private Location retrieveLocationById(Long locationId) {
		Location location = genericDAO.getById(Location.class, locationId);
		return location;
	}
	
	private List<Location> retrieveLocationDataByQualifier(int locationType, String qualifierType, 
			String qualifier) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("status", 1);
		criterias.put("type", locationType);
		criterias.put(qualifierType, qualifier);
		return genericDAO.findByCriteria(Location.class, criterias, "name", false);
	}
	
	private List<Location> retrieveLocationDataByLongName(int locationType, String locationLongName) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("status", 1);
		criterias.put("type", locationType);
		criterias.put("longName", locationLongName);
		return genericDAO.findByCriteria(Location.class, criterias, "name", false);
	}
	
	private List<Location> retrieveLocationData(int locationType, String locationName) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("status", 1);
		criterias.put("type", locationType);
		criterias.put("name", locationName);
		return genericDAO.findByCriteria(Location.class, criterias, "name", false);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importLoadMiles(InputStream is, Long createdBy) throws Exception {
		List<LocationDistance> locationDistanceList = new ArrayList<LocationDistance>();
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Iterator rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount == 1) {
					continue;
				}
				
				boolean recordError = false;
				boolean fatalRecordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				LocationDistance locationDistance = null;
				try {
					String originName = ((String) getCellValue(row.getCell(3)));
					if (StringUtils.equals("END_OF_DATA", originName)) {
						break;
					}
					
					locationDistance = new LocationDistance();
					
					List<Location> originList = retrieveLocationData(1, originName);
					if (originList.isEmpty()) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Transfer Station,");
					} else {
						locationDistance.setOrigin(originList.get(0));
					}
					
					String destinationName = ((String) getCellValue(row.getCell(4)));
					List<Location> destinationList = retrieveLocationData(2, destinationName);
					if (destinationList.isEmpty()) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Landfill,");
					} else {
						locationDistance.setDestination(destinationList.get(0));
					}

					Double miles = row.getCell(5).getNumericCellValue();
					if (miles == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Miles,");
					} else {
						locationDistance.setMiles(miles);
					}
					
					if (checkDuplicate(locationDistance)) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Duplicate record,");
					}
				} catch (Exception ex) {
					recordError = true;
					fatalRecordError = true;
					recordErrorMsg.append("Error while processing record, Line: " + recordCount);
				}
				
				if (recordError) {
					String msgPreffix = fatalRecordError ? "Record NOT loaded->" : "Record LOADED, but has errors->";
					errorList.add(msgPreffix 
							+ "Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
					errorCount++;
				} 
				
				if (!fatalRecordError) {
					locationDistanceList.add(locationDistance);
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records being loaded: " + locationDistanceList.size());
			if (!locationDistanceList.isEmpty()) {
				for (LocationDistance aLocationDistance : locationDistanceList) {
					aLocationDistance.setStatus(1);
					aLocationDistance.setCreatedBy(createdBy);
					aLocationDistance.setCreatedAt(Calendar.getInstance().getTime());
					
					genericDAO.saveOrUpdate(aLocationDistance);
				}
			}
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Location Distancedata: " + ex);
		}
		
		return errorList;
	}
	
	private boolean checkDuplicate(LocationDistance entity) {
		Location origin = entity.getOrigin();
		Location destination = entity.getDestination();
		if (origin == null || destination == null) {
			return false;
		}
		
		String query = "select count(obj) from LocationDistance obj where 1=1"
				+ " and obj.origin.id=" + origin.getId()
				+ " and obj.destination.id=" + destination.getId();
		 Long recordCount = (Long) genericDAO.getEntityManager().createQuery(
				 query.toString()).getSingleResult(); 
		 return (recordCount > 0);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importSubcontractorRateMainSheet(InputStream is, Date validFrom, Date validTo, Long createdBy) throws Exception {
		List<SubcontractorRate> subcontractorRateList = new ArrayList<SubcontractorRate>();
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Iterator rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount == 1) {
					continue;
				}
				
				boolean recordError = false;
				boolean fatalRecordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				SubcontractorRate subcontractorRate = null;
				try {
					String subcontractorName = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", subcontractorName)) {
						break;
					}
					
					String companyName = ((String) getCellValue(row.getCell(1)));
					String transferStationName = ((String) getCellValue(row.getCell(2)));
					String landfillName = ((String) getCellValue(row.getCell(3)));
					String billUsing = ((String) getCellValue(row.getCell(4)));
					String sortBy = ((String) getCellValue(row.getCell(5)));
					String rateType = ((String) getCellValue(row.getCell(6)));
					
					// Load date - 1, Unload date - 2
					String rateUsing = "Load date";
					
					Double rate = row.getCell(7).getNumericCellValue();
					Double fuelSurchargeAmount = row.getCell(8).getNumericCellValue();
					Double otherCharges = row.getCell(9).getNumericCellValue();
					
					subcontractorRate = new SubcontractorRate();
					
					String query = "select obj from SubContractor obj where "
							+ " obj.status=1"
							+ " and obj.name='" + subcontractorName + "'";
					List<SubContractor> subcontractorList = genericDAO.executeSimpleQuery(query);
					
					if (subcontractorList.isEmpty()) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Subcontractor,");
					} else {
						subcontractorRate.setSubcontractor(subcontractorList.get(0));
					}
					
					List<Location> companyList = retrieveLocationData(3, companyName);
					if (companyList.isEmpty()) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Company,");
					} else {
						subcontractorRate.setCompanyLocation(companyList.get(0));
					}
					
					List<Location> transferStationList = retrieveLocationData(1, transferStationName);
					if (transferStationList.isEmpty()) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Transfer Station,");
					} else {
						subcontractorRate.setTransferStation(transferStationList.get(0));
					}
					
					List<Location> landfillList = retrieveLocationData(2, landfillName);
					if (landfillList.isEmpty()) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Landfill,");
					} else {
						subcontractorRate.setLandfill(landfillList.get(0));
					}
				
					List<StaticData> rateTypesList = retrieveStaticData("RATE_TYPE", rateType);
					List<StaticData> billUsingList = retrieveStaticData("BILL_USING", billUsing);
					List<StaticData> sortByList = retrieveStaticData("BILL_USING", sortBy);
					List<StaticData> rateUsingList = retrieveStaticData("RATE_USING", rateUsing);
					
					subcontractorRate.setRateType(new Integer(rateTypesList.get(0).getDataValue()));
					subcontractorRate.setBillUsing(new Integer(billUsingList.get(0).getDataValue()));
					subcontractorRate.setSortBy(new Integer(sortByList.get(0).getDataValue()));
					subcontractorRate.setRateUsing(new Integer(rateUsingList.get(0).getDataValue()));
					
					subcontractorRate.setFuelSurchargeAmount(fuelSurchargeAmount);
					subcontractorRate.setOtherCharges(otherCharges);
					subcontractorRate.setValue(rate);
					
					subcontractorRate.setValidFrom(validFrom);
					subcontractorRate.setValidTo(validTo);
					
					if (checkDuplicate(subcontractorRate)) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Duplicate record,");
					}
				} catch (Exception ex) {
					recordError = true;
					fatalRecordError = true;
					recordErrorMsg.append("Error while processing record,");
				}
				
				if (recordError) {
					String msgPreffix = fatalRecordError ? "Record NOT loaded->" : "Record LOADED, but has errors->";
					errorList.add(msgPreffix 
							+ "Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
					errorCount++;
				} 
				
				if (!fatalRecordError) {
					subcontractorRateList.add(subcontractorRate);
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records being loaded: " + subcontractorRateList.size());
			if (!subcontractorRateList.isEmpty()) {
				for (SubcontractorRate aSubcontractorRate : subcontractorRateList) {
					aSubcontractorRate.setStatus(1);
					aSubcontractorRate.setCreatedBy(createdBy);
					aSubcontractorRate.setCreatedAt(Calendar.getInstance().getTime());
					
					genericDAO.saveOrUpdate(aSubcontractorRate);
				}
			}
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Subcontractor Rate data: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importWMInvoice(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat wmDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat wmDateTimeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		// 5/30/2017 4:15:53 AM
		SimpleDateFormat wmDateTimeStrFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		
		SimpleDateFormat requiredTimeFormat = new SimpleDateFormat("HH:mm");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = TicketUtils.getWMInvoiceColMapping();
			if (colMapping.size() <= 0) {
				errorList.add("Location not supported");
				return errorList;
			}
			
			int recordsToBeSkipped = TicketUtils.getWMInvoiceRecordsToBeSkipped();
			
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				WMInvoice currentWMInvoice = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentWMInvoice = new WMInvoice();
					
					Integer ticketCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TICKET);
					String ticketStr = ((String) getCellValue(row.getCell(ticketCol)));
					if (StringUtils.isEmpty(ticketStr)) {
						recordError = true;
						recordErrorMsg.append("Ticket, ");
					} else {
						Long ticket = Long.parseLong(ticketStr);
						currentWMInvoice.setTicket(ticket);
					}
					
					Integer txnDateCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TXN_DATE);
					Object txnDateObj = getCellValue(row.getCell(txnDateCol), true);
					if (txnDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Date, ");
					} else if (txnDateObj instanceof Date) {
						currentWMInvoice.setTxnDate((Date)txnDateObj);
					} else {
						String txnDateStr = txnDateObj.toString();
						Date txnDate = wmDateFormat.parse(txnDateStr);
						currentWMInvoice.setTxnDate(txnDate);
					}
					
					Integer timeInCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TIME_IN);
					Object timeInObj = getCellValue(row.getCell(timeInCol), true);
					if (timeInObj == null) {
						recordError = true;
						recordErrorMsg.append("Time In, ");
					} else {
						SimpleDateFormat timeInDateFormat = wmDateTimeFormat;
						if (!(timeInObj instanceof Date)) {
							timeInDateFormat = wmDateTimeStrFormat;
						}
						
						String timeInStr = timeInObj.toString();
						timeInStr = StringUtils.replace(timeInStr, ".", StringUtils.EMPTY);
						String reqTimeInStr = convertDateFormat(timeInStr, timeInDateFormat, requiredTimeFormat);
						if (StringUtils.isEmpty(reqTimeInStr)) {
							recordError = true;
							recordErrorMsg.append("Time In, ");
						} else {
							currentWMInvoice.setTimeIn(reqTimeInStr);
						}
					} 
					
					Integer timeOutCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TIME_OUT);
					Object timeOutObj = getCellValue(row.getCell(timeOutCol), true);
					if (timeOutObj == null) {
						recordError = true;
						recordErrorMsg.append("Time Out, ");
					} else {
						SimpleDateFormat timeOutDateFormat = wmDateTimeFormat;
						if (!(timeOutObj instanceof Date)) {
							timeOutDateFormat = wmDateTimeStrFormat;
						}
						
						String timeOutStr = timeOutObj.toString();
						timeOutStr = StringUtils.replace(timeOutStr, ".", StringUtils.EMPTY);
						String reqTimeOutStr = convertDateFormat(timeOutStr, timeOutDateFormat, requiredTimeFormat);
						if (StringUtils.isEmpty(reqTimeOutStr)) {
							recordError = true;
							recordErrorMsg.append("Time Out, ");
						} else {
							currentWMInvoice.setTimeOut(reqTimeOutStr);
						}
					}
					
					Integer wmOriginCol = colMapping.get(TicketUtils.WM_INVOICE_COL_ORIGIN);
					String wmOriginStr = ((String) getCellValue(row.getCell(wmOriginCol)));
					if (StringUtils.isEmpty(wmOriginStr)) {
						recordError = true;
						recordErrorMsg.append("Origin, ");
					} else {
						currentWMInvoice.setWmOrigin(wmOriginStr);
						
						List<Location> originList = retrieveLocationDataByLongName(1, wmOriginStr);
						if (originList == null || originList.isEmpty()) {
							recordError = true;
							recordErrorMsg.append("Origin, ");
						} else {
							currentWMInvoice.setOrigin(originList.get(0));
						}
					}
					
					Integer wmDestinationCol = colMapping.get(TicketUtils.WM_INVOICE_COL_DESTINATION);
					String wmDestinationStr = ((String) getCellValue(row.getCell(wmDestinationCol)));
					if (StringUtils.isEmpty(wmDestinationStr)) {
						recordError = true;
						recordErrorMsg.append("Destination, ");
					} else {
						currentWMInvoice.setWmDestination(wmDestinationStr);
						
						List<Location> destinationList = retrieveLocationDataByLongName(2, wmDestinationStr);
						if (destinationList == null || destinationList.isEmpty()) {
							recordError = true;
							recordErrorMsg.append("Destination, ");
						} else {
							currentWMInvoice.setDestination(destinationList.get(0));
						}
					}
					
					Integer wmVehicleCol = colMapping.get(TicketUtils.WM_INVOICE_COL_VEHICLE);
					if (wmVehicleCol != null) {
						String wmVehicleStr = ((String) getCellValue(row.getCell(wmVehicleCol)));
						if (StringUtils.isNotEmpty(wmVehicleStr)) {
							currentWMInvoice.setWmVehicle(wmVehicleStr);
						} 
					}
					Integer wmTrailerCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TRAILER);
					if (wmTrailerCol != null) {
						String wmTrailerStr = ((String) getCellValue(row.getCell(wmTrailerCol)));
						if (StringUtils.isNotEmpty(wmTrailerStr)) {
							currentWMInvoice.setWmTrailer(wmTrailerStr);
						} 
					}
					
					Integer grossCol = colMapping.get(TicketUtils.WM_INVOICE_COL_GROSS);
					Object grossObj = getCellValue(row.getCell(grossCol), true);
					Double grossWeight = null;
					if (grossObj instanceof Double) {
						grossWeight = (Double) grossObj;
					} else {
						String grossObjStr = (String) grossObj;
						grossObjStr = StringUtils.replace(grossObjStr, ",", StringUtils.EMPTY);
						grossWeight = Double.valueOf(grossObjStr);
					}
					currentWMInvoice.setGross(grossWeight);
					
					Integer tareCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TARE);
					Object tareObj = getCellValue(row.getCell(tareCol), true);
					Double tareWeight = null;
					if (tareObj instanceof Double) {
						tareWeight = (Double) tareObj;
					} else {
						String tareObjStr = (String) tareObj;
						tareObjStr = StringUtils.replace(tareObjStr, ",", StringUtils.EMPTY);
						tareWeight = Double.valueOf(tareObjStr);
					}
					currentWMInvoice.setTare(tareWeight);
					
					Integer netCol = colMapping.get(TicketUtils.WM_INVOICE_COL_NET);
					Object netObj = getCellValue(row.getCell(netCol), true);
					Double netWeight = null;
					if (netObj instanceof Double) {
						netWeight = (Double) netObj;
					} else {
						String netObjStr = (String) netObj;
						netObjStr = StringUtils.replace(netObjStr, ",", StringUtils.EMPTY);
						netWeight = Double.valueOf(netObjStr);
					}
					currentWMInvoice.setNet(netWeight);
					
					Integer amountCol = colMapping.get(TicketUtils.WM_INVOICE_COL_AMOUNT);
					Object amountObj = getCellValue(row.getCell(amountCol), true);
					Double amount = null;
					if (amountObj instanceof Double) {
						amount = (Double) amountObj;
					} else {
						amount = Double.valueOf((String) amountObj);
					}
					currentWMInvoice.setAmount(amount);
					
					Integer fscCol = colMapping.get(TicketUtils.WM_INVOICE_COL_FSC);
					Object fscObj = getCellValue(row.getCell(fscCol), true);
					Double fsc = null;
					if (fscObj instanceof Double) {
						fsc = (Double) fscObj;
					} else {
						fsc = Double.valueOf((String) fscObj);
					}
					currentWMInvoice.setFsc(fsc);
					
					Integer totalAmountCol = colMapping.get(TicketUtils.WM_INVOICE_COL_TOTAL_AMOUNT);
					Object totalAmountObj = getCellValue(row.getCell(totalAmountCol), true);
					Double totalAmount = null;
					if (totalAmountObj instanceof Double) {
						totalAmount = (Double) totalAmountObj;
					} else {
						totalAmount = Double.valueOf((String) totalAmountObj);
					}
					currentWMInvoice.setTotalAmount(totalAmount);
				
					Integer wmStatusCol = colMapping.get(TicketUtils.WM_INVOICE_COL_STATUS);
					if (wmStatusCol != null) {
						String wmStatusStr = ((String) getCellValue(row.getCell(wmStatusCol)));
						if (StringUtils.isNotEmpty(wmStatusStr)) {
							currentWMInvoice.setWmStatus(wmStatusStr);
						} 
					}
					
					Integer wmStatusCodeCol = colMapping.get(TicketUtils.WM_INVOICE_COL_STATUS_CODE);
					if (wmStatusCodeCol != null) {
						String wmStatusCodeStr = ((String) getCellValue(row.getCell(wmStatusCodeCol)));
						if (StringUtils.isNotEmpty(wmStatusCodeStr)) {
							currentWMInvoice.setWmStatusCode(wmStatusCodeStr);
						} 
					}
					
					Integer wmTicketCol = colMapping.get(TicketUtils.WM_INVOICE_COL_WM_TICKET);
					if (wmTicketCol != null) {
						String wmTicketStr = ((String) getCellValue(row.getCell(wmTicketCol)));
						if (StringUtils.isNotEmpty(wmTicketStr)) {
							currentWMInvoice.setWmTicket(wmTicketStr);
						} 
					}
					
					WMInvoice existingWMInvoice = checkDuplicateWMInvoice(currentWMInvoice);
					if (existingWMInvoice != null) {
						if (StringUtils.equals(currentWMInvoice.getWmStatusCode(), existingWMInvoice.getWmStatusCode())) {
							recordError = true;
							recordErrorMsg.append("Duplicate WM Invoice, ");
						} else {
							existingWMInvoice.setWmStatusCode(currentWMInvoice.getWmStatusCode());
							existingWMInvoice.setWmStatus(currentWMInvoice.getWmStatus());
							existingWMInvoice.setModifiedBy(createdBy);
							existingWMInvoice.setModifiedAt(Calendar.getInstance().getTime());
							genericDAO.saveOrUpdate(existingWMInvoice);
							
							successCount++;
							continue;
						}
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					currentWMInvoice.setStatus(1);
					currentWMInvoice.setCreatedBy(createdBy);
					currentWMInvoice.setCreatedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(currentWMInvoice);
					
					successCount++;
				} catch (Exception ex) {
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing WM Invoice: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importAccidentNotReportedData(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat accidentDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = WorkerCompUtils.getAccidentNotReportedColMapping();
			
			Iterator<Row> rows = sheet.rowIterator();
			int recordsToBeSkipped = 1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				Accident currentAccident = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentAccident = new Accident();
					
					Integer driverNameCol = colMapping.get(WorkerCompUtils.ACCIDENT_NOT_REPORTED_COL_NAME);
					String driverName = ((String) getCellValue(row.getCell(driverNameCol)));
					driverName = StringUtils.trimToEmpty(driverName);
					Driver driver = null;
					if (StringUtils.contains(driverName, ",")) {
						driver = WorkerCompUtils.retrieveDriverByCommaSep(driverName, genericDAO);
					} else {
						driver = WorkerCompUtils.retrieveDriver(driverName, genericDAO, true);
					}
					if (driver == null) {
						recordError = true;
						recordErrorMsg.append("Employee Name, ");
					} else {
						currentAccident.setDriver(driver);
					}
					
					Integer incidentDateCol = colMapping.get(WorkerCompUtils.ACCIDENT_NOT_REPORTED_COL_INCIDENT_DATE);
					Object incidentDateObj = getCellValue(row.getCell(incidentDateCol), true);
					if (incidentDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Incident Date, ");
					} else if (incidentDateObj instanceof Date) {
						currentAccident.setIncidentDate((Date)incidentDateObj);
					} else {
						String incidentDateStr = incidentDateObj.toString();
						incidentDateStr = StringUtils.trimToEmpty(incidentDateStr);
						Date incidentDate = accidentDateFormat.parse(incidentDateStr);
						currentAccident.setIncidentDate(incidentDate);
					}
					
					Accident existingAccident = WorkerCompUtils.retrieveMatchingAccident(currentAccident, genericDAO);
					if (existingAccident == null) {
						recordError = true;
						recordErrorMsg.append("No matching existing Accident record found to update costs, ");
						
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					Integer totalCostCol = colMapping.get(WorkerCompUtils.ACCIDENT_NOT_REPORTED_COL_TOTAL_COST);
					Object totalCostObj = getCellValue(row.getCell(totalCostCol), true);
					Double totalCost = null;
					if (totalCostObj != null) {
						if (totalCostObj instanceof Double) {
							totalCost = (Double) totalCostObj;
						} else {
							String totalCostStr = (String) totalCostObj;
							if (StringUtils.isNotEmpty(totalCostStr)) {
								totalCost = Double.valueOf(totalCostStr);
							}
						}
						existingAccident.setTotalCost(totalCost);
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					existingAccident.setModifiedBy(createdBy);
					existingAccident.setModifiedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(existingAccident);
					
					successCount++;
				} catch (Exception ex) {
					log.warn("Error while processing Accident Not Reported record: " + recordCount + ". " + ex);
					
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing accidents not reported...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Accident Not Reported data: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importAccidentReportedData(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat accidentDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = WorkerCompUtils.getAccidentReportedColMapping();
			
			Iterator<Row> rows = sheet.rowIterator();
			int recordsToBeSkipped = 1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				Accident currentAccident = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentAccident = new Accident();
					
					Integer insuranceCompanyCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_INUSRANCE_COMPANY);
					String inuranceCompanyStr = ((String) getCellValue(row.getCell(insuranceCompanyCol)));
					inuranceCompanyStr = StringUtils.trimToEmpty(inuranceCompanyStr);
					InsuranceCompany insuranceCompany = WorkerCompUtils.retrieveInsuranceCompanyByName(inuranceCompanyStr, genericDAO);
					if (insuranceCompany == null) {
						recordError = true;
						recordErrorMsg.append("Inurance Company, ");
					} else {
						currentAccident.setInsuranceCompany(insuranceCompany);
					}
					
					Integer insuranceClaimNoCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_CLAIM);
					String claimNo = ((String) getCellValue(row.getCell(insuranceClaimNoCol)));
					claimNo = StringUtils.trimToEmpty(claimNo);
					if (StringUtils.isEmpty(claimNo)) {
						recordError = true;
						recordErrorMsg.append("Claim No, ");
					} else {
						currentAccident.setClaimNumber(claimNo);
					}
					
					Integer driverNameCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_EMPLOYEE);
					String driverName = ((String) getCellValue(row.getCell(driverNameCol)));
					driverName = StringUtils.trimToEmpty(driverName);
					Driver driver = WorkerCompUtils.retrieveDriver(driverName, genericDAO, true);
					if (driver == null) {
						recordError = true;
						recordErrorMsg.append("Employee Name, ");
					} else {
						currentAccident.setDriver(driver);
					}
					
					Integer incidentDateCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_INCIDENT_DATE);
					Object incidentDateObj = getCellValue(row.getCell(incidentDateCol), true);
					if (incidentDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Incident Date, ");
					} else if (incidentDateObj instanceof Date) {
						currentAccident.setIncidentDate((Date)incidentDateObj);
					} else {
						String incidentDateStr = incidentDateObj.toString();
						incidentDateStr = StringUtils.trimToEmpty(incidentDateStr);
						Date incidentDate = accidentDateFormat.parse(incidentDateStr);
						currentAccident.setIncidentDate(incidentDate);
					}
					
					Accident existingAccident = WorkerCompUtils.retrieveMatchingAccident(currentAccident, genericDAO);
					if (existingAccident == null) {
						recordError = true;
						recordErrorMsg.append("No matching existing Accident record found to update costs, ");
						
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					Integer statusCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_STATUS);
					String statusStr = ((String) getCellValue(row.getCell(statusCol)));
					statusStr = StringUtils.trimToEmpty(statusStr);
					StaticData status = WorkerCompUtils.retrieveAccidentStatus(statusStr, genericDAO);
					if (status == null) {
						recordError = true;
						recordErrorMsg.append("Status, ");
					} else {
						existingAccident.setAccidentStatus(Integer.valueOf(status.getDataValue()));
					}
					
					Integer deductibleCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_DEDUCTIBLE);
					Object deductibleObj = getCellValue(row.getCell(deductibleCol), true);
					Double deductible = null;
					if (deductibleObj != null) {
						if (deductibleObj instanceof Double) {
							deductible = (Double) deductibleObj;
						} else {
							String deductibleStr = (String) deductibleObj;
							if (StringUtils.isNotEmpty(deductibleStr)) {
								deductible = Double.valueOf(deductibleStr);
							}
						}
						existingAccident.setDeductible(deductible);
					}
					
					Integer expenseCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_EXPENSE);
					Object expenseObj = getCellValue(row.getCell(expenseCol), true);
					Double expense = null;
					if (expenseObj != null) {
						if (expenseObj instanceof Double) {
							expense = (Double) expenseObj;
						} else {
							String expenseStr = (String) expenseObj;
							if (StringUtils.isNotEmpty(expenseStr)) {
								expense = Double.valueOf(expenseStr);
							}
						}
						existingAccident.setExpense(expense);
					}
					
					Integer reserveCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_RESERVE);
					Object reserveObj = getCellValue(row.getCell(reserveCol), true);
					Double reserve = null;
					if (reserveObj != null) {
						if (reserveObj instanceof Double) {
							reserve = (Double) reserveObj;
						} else {
							String reserveStr = (String) reserveObj;
							if (StringUtils.isNotEmpty(reserveStr)) {
								reserve = Double.valueOf(reserveStr);
							}
						}
						existingAccident.setReserve(reserve);
					}
					
					Integer paidCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_PAID);
					Object paidObj = getCellValue(row.getCell(paidCol), true);
					Double paid = null;
					if (paidObj != null) {
						if (paidObj instanceof Double) {
							paid = (Double) paidObj;
						} else {
							String paidStr = (String) paidObj;
							if (StringUtils.isNotEmpty(paidStr)) {
								paid = Double.valueOf(paidStr);
							}
						}
						existingAccident.setPaid(paid);
					}
					
					Integer totalCostCol = colMapping.get(WorkerCompUtils.ACCIDENT_REPORTED_COL_TOTAL_COST);
					Object totalCostObj = getCellValue(row.getCell(totalCostCol), true);
					Double totalCost = null;
					if (totalCostObj != null) {
						if (totalCostObj instanceof Double) {
							totalCost = (Double) totalCostObj;
						} else {
							String totalCostStr = (String) totalCostObj;
							if (StringUtils.isNotEmpty(totalCostStr)) {
								totalCost = Double.valueOf(totalCostStr);
							}
						}
						existingAccident.setTotalCost(totalCost);
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					existingAccident.setModifiedBy(createdBy);
					existingAccident.setModifiedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(existingAccident);
					
					successCount++;
				} catch (Exception ex) {
					log.warn("Error while processing Accident Reported record: " + recordCount + ". " + ex);
					
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing accidents reported...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Accident Reported data: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importAccidentMainData(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat accidentDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = WorkerCompUtils.getAccidentMainColMapping();
			
			Iterator<Row> rows = sheet.rowIterator();
			int recordsToBeSkipped = 1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				Accident currentAccident = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentAccident = new Accident();
					
					Integer insuranceCompanyCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_INUSRANCE_COMPANY);
					String inuranceCompanyStr = ((String) getCellValue(row.getCell(insuranceCompanyCol)));
					inuranceCompanyStr = StringUtils.trimToEmpty(inuranceCompanyStr);
					InsuranceCompany insuranceCompany = null;
					if (!StringUtils.equalsIgnoreCase(inuranceCompanyStr, "Not Reported")) {
						insuranceCompany = WorkerCompUtils.retrieveInsuranceCompanyByName(inuranceCompanyStr, genericDAO);
						if (insuranceCompany == null) {
							recordError = true;
							recordErrorMsg.append("Inurance Company, ");
						} else {
							currentAccident.setInsuranceCompany(insuranceCompany);
						}
					}
					
					Integer insuranceClaimNoCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_CLAIM_NO);
					String claimNo = ((String) getCellValue(row.getCell(insuranceClaimNoCol)));
					claimNo = StringUtils.trimToEmpty(claimNo);
					if (StringUtils.isEmpty(claimNo)) {
						claimNo = null;
					}
					currentAccident.setClaimNumber(claimNo);
					
					boolean missingDriver = false;
					Integer driverNameCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_DRIVER_NAME);
					String driverName = ((String) getCellValue(row.getCell(driverNameCol)));
					driverName = StringUtils.trimToEmpty(driverName);
					Driver driver = WorkerCompUtils.retrieveDriverByCommaSep(driverName, genericDAO);
					if (driver == null) {
						/*recordError = true;
						recordErrorMsg.append("Employee Name, ");*/
						missingDriver = true;
					} else {
						currentAccident.setDriver(driver);
					}
					
					if (missingDriver) {
						Integer subcontractorNameCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_SUBCONTRACTOR);
						String subcontractorName = ((String) getCellValue(row.getCell(subcontractorNameCol)));
						subcontractorName = StringUtils.trimToEmpty(subcontractorName);
						SubContractor subcontractor = WorkerCompUtils.retrieveSubcontractor(subcontractorName, genericDAO);
						if (subcontractor == null) {
							recordError = true;
							recordErrorMsg.append("Either Employee or Subcontractor is required ");
						} else {
							currentAccident.setSubcontractor(subcontractor);
						}
					}
					
					Integer incidentDateCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_INCIDENT_DATE);
					Object incidentDateObj = getCellValue(row.getCell(incidentDateCol), true);
					if (incidentDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Incident Date, ");
					} else if (incidentDateObj instanceof Date) {
						currentAccident.setIncidentDate((Date)incidentDateObj);
					} else {
						String incidentDateStr = incidentDateObj.toString();
						incidentDateStr = StringUtils.trimToEmpty(incidentDateStr);
						Date incidentDate = accidentDateFormat.parse(incidentDateStr);
						currentAccident.setIncidentDate(incidentDate);
					}
					String dayOfWeek = WorkerCompUtils.deriveDayOfWeek(currentAccident.getIncidentDate());
					currentAccident.setIncidentDayOfWeek(dayOfWeek);
					
					Integer vehicleCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_UNIT);
					String unit = ((String) getCellValue(row.getCell(vehicleCol)));
					unit = StringUtils.trimToEmpty(unit);
					if (StringUtils.isNotEmpty(unit)) {
						Vehicle vehicle = WorkerCompUtils.retrieveVehicleForUnit(unit, currentAccident.getIncidentDate(), 
										genericDAO);
						if (vehicle == null) {
							recordError = true;
							recordErrorMsg.append("Vehicle (either unit is invalid or not valid for incident date), ");
						} else {
							currentAccident.setVehicle(vehicle);
						}
					}
					
					Integer monthsOfServiceCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_MONTHS_OF_SERVICE);
					String monthsOfServiceStr = ((String) getCellValue(row.getCell(monthsOfServiceCol), true));
					monthsOfServiceStr = StringUtils.trimToEmpty(monthsOfServiceStr);
					if (StringUtils.isNotEmpty(monthsOfServiceStr)) {
						//if (!StringUtils.isNumeric(monthsOfServiceStr)) {
							monthsOfServiceStr = StringUtils.substringBefore(monthsOfServiceStr, ".");
							Integer monthsOfService = Integer.valueOf(monthsOfServiceStr);
							currentAccident.setDriverMonthsOfService(monthsOfService);
						//}
					}
					
					Integer hireDateCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_HIRE_DATE);
					Object hireDateObj = getCellValue(row.getCell(hireDateCol), true);
					if (hireDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Hire Date, ");
					} else if (hireDateObj instanceof Date) {
						currentAccident.setDriverHiredDate((Date)hireDateObj);
					} else {
						String hireDateStr = hireDateObj.toString();
						hireDateStr = StringUtils.trimToEmpty(hireDateStr);
						if (StringUtils.isNotEmpty(hireDateStr)) {
							Date hireDate = accidentDateFormat.parse(hireDateStr);
							currentAccident.setDriverHiredDate(hireDate);
						}
					}
					
					Integer loationCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_LOCATION);
					String locationStr = ((String) getCellValue(row.getCell(loationCol)));
					locationStr = StringUtils.trimToEmpty(locationStr);
					currentAccident.setLocation(locationStr);
					
					Integer companyCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_COMPANY);
					String companyStr = ((String) getCellValue(row.getCell(companyCol)));
					companyStr = StringUtils.trimToEmpty(companyStr);
					List<Location> locationList = WorkerCompUtils.retrieveCompanyTerminal(companyStr, genericDAO);
					if (locationList == null || locationList.isEmpty()) {
						recordError = true;
						recordErrorMsg.append("Company, ");
					} else {
						currentAccident.setDriverCompany(locationList.get(0));
						currentAccident.setDriverTerminal(locationList.get(1));
					}
						
					Integer stateCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_STATE);
					String stateStr = ((String) getCellValue(row.getCell(stateCol)));
					stateStr = StringUtils.trimToEmpty(stateStr);
					if (StringUtils.isNotEmpty(stateStr)) {
						State state = WorkerCompUtils.retrieveState(stateStr, genericDAO);
						if (state == null) {
							recordError = true;
							recordErrorMsg.append("State, ");
						} else {
							currentAccident.setState(state);
						}
					}
					
					Integer accidentCauseCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_CAUSE);
					String accidentCauseStr = ((String) getCellValue(row.getCell(accidentCauseCol)));
					accidentCauseStr = StringUtils.trimToEmpty(accidentCauseStr);
					if (StringUtils.isNotEmpty(accidentCauseStr)) {
						AccidentCause accidentCause = WorkerCompUtils.retrieveAccidentCause(accidentCauseStr, genericDAO);
						if (accidentCause == null) {
							recordError = true;
							recordErrorMsg.append("Accident Cause, ");
						} else {
							currentAccident.setAccidentCause(accidentCause);
						}
					}
					
					Integer accidentRoadConditionCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_ROAD);
					String accidentRoadConditionStr = ((String) getCellValue(row.getCell(accidentRoadConditionCol)));
					accidentRoadConditionStr = StringUtils.trimToEmpty(accidentRoadConditionStr);
					if (StringUtils.isNotEmpty(accidentRoadConditionStr)) {
						AccidentRoadCondition accidentRoadCondition = WorkerCompUtils.retrieveAccidentRoadCondition(accidentRoadConditionStr, genericDAO);
						if (accidentRoadCondition == null) {
							recordError = true;
							recordErrorMsg.append("Road Condition, ");
						} else {
							currentAccident.setRoadCondition(accidentRoadCondition);
						}
					}
					
					Integer accidentWeatherCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_WEATHER);
					String accidentWeatherStr = ((String) getCellValue(row.getCell(accidentWeatherCol)));
					accidentWeatherStr = StringUtils.trimToEmpty(accidentWeatherStr);
					if (StringUtils.isNotEmpty(accidentWeatherStr)) {
						AccidentWeather accidentWeather = WorkerCompUtils.retrieveAccidentWeather(accidentWeatherStr, genericDAO);
						if (accidentWeather == null) {
							recordError = true;
							recordErrorMsg.append("Weather, ");
						} else {
							currentAccident.setWeather(accidentWeather);
						}
					}
					
					Integer commentsCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_ACCIDENT_COMMENTS);
					String commentsStr = ((String) getCellValue(row.getCell(commentsCol)));
					commentsStr = StringUtils.trimToEmpty(commentsStr);
					currentAccident.setNotes(commentsStr);
					
					Integer vehicleDamageCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_VEHICLE_DAMAGE);
					String vehicleDamageStr = ((String) getCellValue(row.getCell(vehicleDamageCol)));
					vehicleDamageStr = StringUtils.trimToEmpty(vehicleDamageStr);
					if (StringUtils.equalsIgnoreCase(vehicleDamageStr, "Yes")) {
						currentAccident.setVehicleDamage("Yes");
					} else if (StringUtils.equalsIgnoreCase(vehicleDamageStr, "No")) {
						currentAccident.setVehicleDamage("No");
					}
					
					Integer towedCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_TOWED);
					String towedStr = ((String) getCellValue(row.getCell(towedCol)));
					towedStr = StringUtils.trimToEmpty(towedStr);
					if (StringUtils.equalsIgnoreCase(towedStr, "Yes")) {
						currentAccident.setTowed("Yes");
					} else if (StringUtils.equalsIgnoreCase(towedStr, "No")) {
						currentAccident.setTowed("No");
					}
					
					Integer noInjuredCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_NO_INJURED);
					String noInjuredStr = ((String) getCellValue(row.getCell(noInjuredCol)));
					noInjuredStr = StringUtils.trimToEmpty(noInjuredStr);
					currentAccident.setNoInjured(noInjuredStr);
					
					Integer citationCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_CITATION);
					String citationStr = ((String) getCellValue(row.getCell(citationCol)));
					citationStr = StringUtils.trimToEmpty(citationStr);
					if (StringUtils.equalsIgnoreCase(citationStr, "Yes")) {
						currentAccident.setCitation("Yes");
					} else if (StringUtils.equalsIgnoreCase(citationStr, "No")) {
						currentAccident.setCitation("No");
					}
					
					Integer recordableCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_RECORDABLE);
					String recordableStr = ((String) getCellValue(row.getCell(recordableCol)));
					recordableStr = StringUtils.trimToEmpty(recordableStr);
					if (StringUtils.equalsIgnoreCase(recordableStr, "Yes")) {
						currentAccident.setRecordable("Yes");
					} else if (StringUtils.equalsIgnoreCase(recordableStr, "No")) {
						currentAccident.setRecordable("No");
					}
					
					Integer hmRelaseCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_HM_RELEASE);
					String hmReleaseStr = ((String) getCellValue(row.getCell(hmRelaseCol)));
					hmReleaseStr = StringUtils.trimToEmpty(hmReleaseStr);
					if (StringUtils.equalsIgnoreCase(hmReleaseStr, "Yes")) {
						currentAccident.setHmRelease("Yes");
					} else if (StringUtils.equalsIgnoreCase(hmReleaseStr, "No")) {
						currentAccident.setHmRelease("No");
					}
					
					Integer claimRepCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_CLAIM_REP);
					String claimRepStr = ((String) getCellValue(row.getCell(claimRepCol)));
					claimRepStr = StringUtils.trimToEmpty(claimRepStr);
					if (StringUtils.isNotEmpty(claimRepStr) && insuranceCompany != null) { 
						InsuranceCompanyRep claimRep = WorkerCompUtils.retrieveClaimRep(claimRepStr, insuranceCompany, genericDAO);
						/*if (claimRep == null) {
							recordError = true;
							recordErrorMsg.append("Claim Rep, ");
						} else {*/
							currentAccident.setClaimRep(claimRep);
						//}
					}
					
					Integer statusCol = colMapping.get(WorkerCompUtils.ACCIDENT_MAIN_COL_STATUS);
					String statusStr = ((String) getCellValue(row.getCell(statusCol)));
					statusStr = StringUtils.trimToEmpty(statusStr);
					StaticData status = WorkerCompUtils.retrieveAccidentStatus(statusStr, genericDAO);
					if (status == null) {
						recordError = true;
						recordErrorMsg.append("Status, ");
					} else {
						currentAccident.setAccidentStatus(Integer.valueOf(status.getDataValue()));
					}
					
					if (WorkerCompUtils.checkDuplicateAccident(currentAccident, genericDAO)) {
						recordError = true;
						recordErrorMsg.append("Duplicate Accident, ");
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					currentAccident.setStatus(1);
					currentAccident.setCreatedBy(createdBy);
					currentAccident.setCreatedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(currentAccident);
					
					successCount++;
				} catch (Exception ex) {
					log.warn("Error while processing Accident Main record: " + recordCount + ". " + ex);
					
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing accidents...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Accident Main: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importInjuryMainData(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat injuryDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = WorkerCompUtils.getInjuryMainColMapping();
			
			Iterator<Row> rows = sheet.rowIterator();
			int recordsToBeSkipped = 1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				Injury currentInjury = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentInjury = new Injury();
					
					Integer insuranceCompanyCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_INUSRANCE_COMPANY);
					String inuranceCompanyStr = ((String) getCellValue(row.getCell(insuranceCompanyCol)));
					InsuranceCompany insuranceCompany = null;
					if (!StringUtils.equalsIgnoreCase(inuranceCompanyStr, "Not Reported")) {
						insuranceCompany = WorkerCompUtils.retrieveInsuranceCompanyByName(inuranceCompanyStr, genericDAO);
						if (insuranceCompany == null) {
							recordError = true;
							recordErrorMsg.append("Inurance Company, ");
						} else {
							currentInjury.setInsuranceCompany(insuranceCompany);
						}
					}
					
					Integer insuranceClaimNoCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_CLAIM_NO);
					String claimNo = ((String) getCellValue(row.getCell(insuranceClaimNoCol)));
					if (StringUtils.isEmpty(claimNo)) {
						claimNo = null;
					}
					currentInjury.setClaimNumber(claimNo);
					
					Integer driverLastNameCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_LAST_NAME);
					String driverLastName = ((String) getCellValue(row.getCell(driverLastNameCol)));
					Integer driverFirstNameCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_FIRST_NAME);
					String driverFirstName = ((String) getCellValue(row.getCell(driverFirstNameCol)));
					Driver driver = WorkerCompUtils.retrieveDriver(driverFirstName, driverLastName, genericDAO);
					if (driver == null) {
						recordError = true;
						recordErrorMsg.append("Employee Name, ");
					} else {
						currentInjury.setDriver(driver);
					}
					
					Integer driverAgeCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_AGE);
					String driverAgeStr = ((String) getCellValue(row.getCell(driverAgeCol), true));
					if (StringUtils.isNotEmpty(driverAgeStr)) {
						//if (!StringUtils.isNumeric(driverAgeStr)) {
							driverAgeStr = StringUtils.substringBefore(driverAgeStr, ".");
							Integer driverAge = Integer.valueOf(driverAgeStr);
							currentInjury.setDriverAge(driverAge);
						//}
					}
					
					Integer monthsOfServiceCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_MONTHS_OF_SERVICE);
					String monthsOfServiceStr = ((String) getCellValue(row.getCell(monthsOfServiceCol), true));
					if (StringUtils.isNotEmpty(monthsOfServiceStr)) {
						//if (!StringUtils.isNumeric(monthsOfServiceStr)) {
							monthsOfServiceStr = StringUtils.substringBefore(monthsOfServiceStr, ".");
							Integer monthsOfService = Integer.valueOf(monthsOfServiceStr);
							currentInjury.setDriverMonthsOfService(monthsOfService);
						//}
					}
					
					Integer companyCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_COMPANY);
					String companyStr = ((String) getCellValue(row.getCell(companyCol)));
					List<Location> locationList = WorkerCompUtils.retrieveCompanyTerminal(companyStr, genericDAO);
					if (locationList == null || locationList.isEmpty()) {
						recordError = true;
						recordErrorMsg.append("Company, ");
					} else {
						currentInjury.setDriverCompany(locationList.get(0));
						currentInjury.setDriverTerminal(locationList.get(1));
					}
						
					Integer positionCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_POSITION);
					String positionStr = ((String) getCellValue(row.getCell(positionCol)));
					EmployeeCatagory employeeCategory = WorkerCompUtils.retrieveEmployeeCategory(positionStr, genericDAO);
					if (employeeCategory == null) {
						recordError = true;
						recordErrorMsg.append("Position, ");
					} else {
						currentInjury.setDriverCategory(employeeCategory);
					}
					
					Integer incidentDateCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_INCIDENT_DATE);
					Object incidentDateObj = getCellValue(row.getCell(incidentDateCol), true);
					if (incidentDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Incident Date, ");
					} else if (incidentDateObj instanceof Date) {
						currentInjury.setIncidentDate((Date)incidentDateObj);
					} else {
						String incidentDateStr = incidentDateObj.toString();
						Date incidentDate = injuryDateFormat.parse(incidentDateStr);
						currentInjury.setIncidentDate(incidentDate);
					}
					String dayOfWeek = WorkerCompUtils.deriveDayOfWeek(currentInjury.getIncidentDate());
					currentInjury.setIncidentDayOfWeek(dayOfWeek);
					
					Integer timeOfDayCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_TIME_OF_DAY);
					Object timeOfDayObj = getCellValue(row.getCell(timeOfDayCol), true);
					Integer amPMCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_AM_PM);
					String amPMStr = ((String) getCellValue(row.getCell(amPMCol)));
					String incidentTime = WorkerCompUtils.deriveIncidentTime(timeOfDayObj, amPMStr);
					currentInjury.setIncidentTime(incidentTime);
					currentInjury.setIncidentTimeAMPM(amPMStr);
					
					Integer returnToWorkCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_RETURN_TO_WORK);
					Object returnToWorkObj = getCellValue(row.getCell(returnToWorkCol), true);
					String returnToWorkStr = StringUtils.EMPTY;
					if (returnToWorkObj != null) {
						if (returnToWorkObj instanceof Date) {
							returnToWorkStr = injuryDateFormat.format((Date) returnToWorkObj);
						} else {
							returnToWorkStr = (String) returnToWorkObj;
						}
					}
					currentInjury.setReturnToWork(returnToWorkStr);
					
					Integer incidentTypeCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_INCIDENT_TYPE);
					String incidentTypeStr = ((String) getCellValue(row.getCell(incidentTypeCol)));
					InjuryIncidentType injuryIncidentType = WorkerCompUtils.retrieveIncidentType(incidentTypeStr, genericDAO);
					if (injuryIncidentType == null) {
						recordError = true;
						recordErrorMsg.append("Incident Type, ");
					} else {
						currentInjury.setIncidentType(injuryIncidentType);
					}
					
					Integer injuryToCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_INJURY_TO);
					String injuryToStr = ((String) getCellValue(row.getCell(injuryToCol)));
					if (StringUtils.isNotEmpty(injuryToStr)) { 
						InjuryToType injuryToType = WorkerCompUtils.retrieveInjuryToType(injuryToStr, genericDAO);
						if (injuryToType == null) {
							recordError = true;
							recordErrorMsg.append("Injury To, ");
						} else {
							currentInjury.setInjuryTo(injuryToType);
						}
					}
					
					Integer commentsCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_INJURY_COMMENTS);
					String commentsStr = ((String) getCellValue(row.getCell(commentsCol)));
					currentInjury.setNotes(commentsStr);
					
					Integer lostWorkDaysCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_LOST_WORK_DAYS);
					String lostWorkDaysStr = ((String) getCellValue(row.getCell(lostWorkDaysCol), true));
					if (StringUtils.isNotEmpty(lostWorkDaysStr)) {
						//if (StringUtils.isNumeric(lostWorkDaysStr)) {
							lostWorkDaysStr = StringUtils.substringBefore(lostWorkDaysStr, ".");
							Integer lostWorkDays = Integer.valueOf(lostWorkDaysStr);
							currentInjury.setNoOfLostWorkDays(lostWorkDays);
						//}
					}
					
					Integer tarpRelatedInjuryCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_TARP_RELATED_INJURY);
					String tarpRelatedInjuryStr = ((String) getCellValue(row.getCell(tarpRelatedInjuryCol)));
					if (StringUtils.equals(tarpRelatedInjuryStr, "Yes") || StringUtils.equals(tarpRelatedInjuryStr, "No")) {
						currentInjury.setTarpRelatedInjury(tarpRelatedInjuryStr);
					}
					
					Integer firstReportOfInjuryCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_FIRST_INJURY);
					String firstReportOfInjuryStr = ((String) getCellValue(row.getCell(firstReportOfInjuryCol)));
					if (StringUtils.equals(firstReportOfInjuryStr, "Yes") || StringUtils.equals(firstReportOfInjuryStr, "No")) {
						currentInjury.setFirstReportOfInjury(firstReportOfInjuryStr);
					}
					
					Integer claimRepCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_CLAIM_REP);
					String claimRepStr = ((String) getCellValue(row.getCell(claimRepCol)));
					if (StringUtils.isNotEmpty(claimRepStr) && insuranceCompany != null) { 
						InsuranceCompanyRep claimRep = WorkerCompUtils.retrieveClaimRep(claimRepStr, insuranceCompany, genericDAO);
						/*if (claimRep == null) {
							recordError = true;
							recordErrorMsg.append("Claim Rep, ");
						} else {*/
							currentInjury.setClaimRep(claimRep);
						//}
					}
					
					Integer statusCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_STATUS);
					String statusStr = ((String) getCellValue(row.getCell(statusCol)));
					StaticData status = WorkerCompUtils.retrieveInjuryStatus(statusStr, genericDAO);
					if (status == null) {
						recordError = true;
						recordErrorMsg.append("Status, ");
					} else {
						currentInjury.setInjuryStatus(Integer.valueOf(status.getDataValue()));
					}
					
					Integer loationCol = colMapping.get(WorkerCompUtils.INJURY_MAIN_COL_LOCATION);
					String locationStr = ((String) getCellValue(row.getCell(loationCol)));
					currentInjury.setLocation(locationStr);
					/*if (StringUtils.isNotEmpty(locationStr)) { 
						Location location = InjuryUtils.retrieveInjuryLocation(locationStr, genericDAO);
						if (location == null) {
							recordError = true;
							recordErrorMsg.append("Location, ");
						} else {
							currentInjury.setLocation(location);
						}
					}*/
					
					if (WorkerCompUtils.checkDuplicateInjury(currentInjury, genericDAO)) {
						recordError = true;
						recordErrorMsg.append("Duplicate Injury, ");
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					currentInjury.setStatus(1);
					currentInjury.setCreatedBy(createdBy);
					currentInjury.setCreatedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(currentInjury);
					
					successCount++;
				} catch (Exception ex) {
					log.warn("Error while processing Injury Main record: " + recordCount + ". " + ex);
					
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing injuries...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Injury Main: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importInjuryReportedData(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat injuryDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = WorkerCompUtils.getInjuryReportedColMapping();
			
			Iterator<Row> rows = sheet.rowIterator();
			int recordsToBeSkipped = 1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				Injury currentInjury = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentInjury = new Injury();
					
					Integer insuranceCompanyCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_INUSRANCE_COMPANY);
					String inuranceCompanyStr = ((String) getCellValue(row.getCell(insuranceCompanyCol)));
					InsuranceCompany insuranceCompany = null;
					insuranceCompany = WorkerCompUtils.retrieveInsuranceCompanyByName(inuranceCompanyStr, genericDAO);
					if (insuranceCompany == null) {
						recordError = true;
						recordErrorMsg.append("Inurance Company, ");
					} else {
						currentInjury.setInsuranceCompany(insuranceCompany);
					}
					
					Integer employeeCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_EMPLOYEE);
					String employeeName = ((String) getCellValue(row.getCell(employeeCol)));
					Driver driver = WorkerCompUtils.retrieveDriver(employeeName, genericDAO, true);
					if (driver == null) {
						recordError = true;
						recordErrorMsg.append("Employee, ");
					} else {
						currentInjury.setDriver(driver);
					}
					
					Integer incidentDateCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_INCIDENT_DATE);
					Object incidentDateObj = getCellValue(row.getCell(incidentDateCol), true);
					if (incidentDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Incident Date, ");
					} else if (incidentDateObj instanceof Date) {
						currentInjury.setIncidentDate((Date)incidentDateObj);
					} else {
						String incidentDateStr = incidentDateObj.toString();
						Date incidentDate = injuryDateFormat.parse(incidentDateStr);
						currentInjury.setIncidentDate(incidentDate);
					}
					
					Injury existingInjury = WorkerCompUtils.retrieveMatchingInjury(currentInjury, genericDAO);
					if (existingInjury == null) {
						recordError = true;
						recordErrorMsg.append("No matching existing Injury record found to update costs, ");
						
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					Integer employedCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_EMPLOYED);
					String employedStr = ((String) getCellValue(row.getCell(employedCol)));
					if (StringUtils.equals(employedStr, "Yes") || StringUtils.equals(employedStr, "No")) {
						existingInjury.setEmployed(employedStr);
					}
					
					Integer workingCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_WORKING);
					String workingStr = ((String) getCellValue(row.getCell(workingCol)));
					if (StringUtils.equals(workingStr, "Yes") || StringUtils.equals(workingStr, "No")) {
						existingInjury.setWorking(workingStr);
					}
					
					Integer statusCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_STATUS);
					String statusStr = ((String) getCellValue(row.getCell(statusCol)));
					StaticData status = WorkerCompUtils.retrieveInjuryStatus(statusStr, genericDAO);
					if (status == null) {
						recordError = true;
						recordErrorMsg.append("Status, ");
					} else {
						existingInjury.setInjuryStatus(Integer.valueOf(status.getDataValue()));
					}
					
					Integer medicalCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_MEDICAL);
					Object medicalObj = getCellValue(row.getCell(medicalCol), true);
					Double medical = null;
					if (medicalObj != null) {
						if (medicalObj instanceof Double) {
							medical = (Double) medicalObj;
						} else {
							String medicalStr = (String) medicalObj;
							if (StringUtils.isNotEmpty(medicalStr)) {
								medical = Double.valueOf(medicalStr);
							}
						}
						existingInjury.setMedicalCost(medical);
					}
					
					Integer indemnityCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_INDEMNITY);
					Object indemnityObj = getCellValue(row.getCell(indemnityCol), true);
					Double indemnity = null;
					if (indemnityObj != null) {
						if (indemnityObj instanceof Double) {
							indemnity = (Double) indemnityObj;
						} else {
							String indemnityStr = (String) indemnityObj;
							if (StringUtils.isNotEmpty(indemnityStr)) {
								indemnity = Double.valueOf(indemnityStr);
							}
						}
						existingInjury.setIndemnityCost(indemnity);
					}
					
					Integer expenseCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_EXPENSE);
					Object expenseObj = getCellValue(row.getCell(expenseCol), true);
					Double expense = null;
					if (expenseObj != null) {
						if (expenseObj instanceof Double) {
							expense = (Double) expenseObj;
						} else {
							String expenseStr = (String) expenseObj;
							if (StringUtils.isNotEmpty(expenseStr)) {
								expense = Double.valueOf(expenseStr);
							}
						}
						existingInjury.setExpense(expense);
					}
					
					Integer reserveCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_RESERVE);
					Object reserveObj = getCellValue(row.getCell(reserveCol), true);
					Double reserve = null;
					if (reserveObj != null) {
						if (reserveObj instanceof Double) {
							reserve = (Double) reserveObj;
						} else {
							String reserveStr = (String) reserveObj;
							if (StringUtils.isNotEmpty(reserveStr)) {
								reserve = Double.valueOf(reserveStr);
							}
						}
						existingInjury.setReserve(reserve);
					}
					
					Integer totalPaidCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_TOTAL_PAID);
					Object totalPaidObj = getCellValue(row.getCell(totalPaidCol), true);
					Double totalPaid = null;
					if (totalPaidObj != null) {
						if (totalPaidObj instanceof Double) {
							totalPaid = (Double) totalPaidObj;
						} else {
							String totalPaidStr = (String) totalPaidObj;
							if (StringUtils.isNotEmpty(totalPaidStr)) {
								totalPaid = Double.valueOf(totalPaidStr);
							}
						}
						existingInjury.setTotalPaid(totalPaid);
					}
					
					Integer totalClaimCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_TOTAL_CLAIM);
					Object totalClaimObj = getCellValue(row.getCell(totalClaimCol), true);
					Double totalClaim = null;
					if (totalClaimObj != null) {
						if (totalClaimObj instanceof Double) {
							totalClaim = (Double) totalClaimObj;
						} else {
							String totalClaimStr = (String) totalClaimObj;
							if (StringUtils.isNotEmpty(totalClaimStr)) {
								totalClaim = Double.valueOf(totalClaimStr);
							}
						}
						existingInjury.setTotalClaimed(totalClaim);
					}
					
					Integer attorneyCol = colMapping.get(WorkerCompUtils.INJURY_REPORTED_COL_ATTORNEY);
					String attorneyStr = ((String) getCellValue(row.getCell(attorneyCol)));
					if (StringUtils.equals(attorneyStr, "Yes") || StringUtils.equals(attorneyStr, "No")) {
						existingInjury.setAttorney(attorneyStr);
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					existingInjury.setModifiedBy(createdBy);
					existingInjury.setModifiedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(existingInjury);
					
					successCount++;
				} catch (Exception ex) {
					log.warn("Error while processing Injury Reported record: " + recordCount + ". " + ex);
					
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing injuries...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Injury Reported data: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importInjuryNotReportedData(InputStream is, Long createdBy) throws Exception {
		SimpleDateFormat injuryDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Map<String, Integer> colMapping = WorkerCompUtils.getInjuryNotReportedColMapping();
			
			Iterator<Row> rows = sheet.rowIterator();
			int recordsToBeSkipped = 1;
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				Injury currentInjury = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentInjury = new Injury();
					
					Integer driverLastNameCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_LAST_NAME);
					String driverLastName = ((String) getCellValue(row.getCell(driverLastNameCol)));
					Integer driverFirstNameCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_FIRST_NAME);
					String driverFirstName = ((String) getCellValue(row.getCell(driverFirstNameCol)));
					Driver driver = WorkerCompUtils.retrieveDriver(driverFirstName, driverLastName, genericDAO);
					if (driver == null) {
						recordError = true;
						recordErrorMsg.append("Employee Name, ");
					} else {
						currentInjury.setDriver(driver);
					}
					
					Integer incidentDateCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_INJURY_DATE);
					Object incidentDateObj = getCellValue(row.getCell(incidentDateCol), true);
					if (incidentDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Injury Date, ");
					} else if (incidentDateObj instanceof Date) {
						currentInjury.setIncidentDate((Date)incidentDateObj);
					} else {
						String incidentDateStr = incidentDateObj.toString();
						Date incidentDate = injuryDateFormat.parse(incidentDateStr);
						currentInjury.setIncidentDate(incidentDate);
					}
					
					Injury existingInjury = WorkerCompUtils.retrieveMatchingInjury(currentInjury, genericDAO);
					if (existingInjury == null) {
						recordError = true;
						recordErrorMsg.append("No matching existing Injury record found to update costs, ");
						
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					}
					
					Integer returnToWorkDateCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_RETURN_TO_WORK_DATE);
					Object returnToWorkDateObj = getCellValue(row.getCell(returnToWorkDateCol), true);
					if (returnToWorkDateObj != null) {
						if (returnToWorkDateObj instanceof Date) {
							existingInjury.setReturnToWorkDate((Date)returnToWorkDateObj);
						} else { 
							String returnToWorkDateStr = returnToWorkDateObj.toString();
							if (StringUtils.isNotEmpty(returnToWorkDateStr)) {
								Date returnToWorkDate = injuryDateFormat.parse(returnToWorkDateStr);
								existingInjury.setReturnToWorkDate(returnToWorkDate);
							}
						}
					}
					
					Integer lostWorkDaysCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_LOST_DAYS);
					String lostWorkDaysStr = ((String) getCellValue(row.getCell(lostWorkDaysCol), true));
					if (StringUtils.isNotEmpty(lostWorkDaysStr)) {
						if (StringUtils.isNumeric(lostWorkDaysStr)) {
							Integer lostWorkDays = Integer.valueOf(lostWorkDaysStr);
							existingInjury.setNoOfLostWorkDays(lostWorkDays);
						}
					}
					
					Integer medicalCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_MEDICAL);
					Object medicalObj = getCellValue(row.getCell(medicalCol), true);
					Double medical = null;
					if (medicalObj != null) {
						if (medicalObj instanceof Double) {
							medical = (Double) medicalObj;
						} else {
							String medicalStr = (String) medicalObj;
							if (StringUtils.isNotEmpty(medicalStr)) {
								medical = Double.valueOf(medicalStr);
							}
						}
						existingInjury.setMedicalCost(medical);
					}
					
					Integer indemnityCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_INDEMNITY);
					Object indemnityObj = getCellValue(row.getCell(indemnityCol), true);
					Double indemnity = null;
					if (indemnityObj != null) {
						if (indemnityObj instanceof Double) {
							indemnity = (Double) indemnityObj;
						} else {
							String indemnityStr = (String) indemnityObj;
							if (StringUtils.isNotEmpty(indemnityStr)) {
								indemnity = Double.valueOf(indemnityStr);
							}
						}
						existingInjury.setIndemnityCost(indemnity);
					}
					
					Integer totalPaidCol = colMapping.get(WorkerCompUtils.INJURY_NOT_REPORTED_COL_TOTAL_PAID);
					Object totalPaidObj = getCellValue(row.getCell(totalPaidCol), true);
					Double totalPaid = null;
					if (totalPaidObj != null) {
						if (totalPaidObj instanceof Double) {
							totalPaid = (Double) totalPaidObj;
						} else {
							String totalPaidStr = (String) totalPaidObj;
							if (StringUtils.isNotEmpty(totalPaidStr)) {
								totalPaid = Double.valueOf(totalPaidStr);
							}
						}
						existingInjury.setTotalPaid(totalPaid);
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					existingInjury.setModifiedBy(createdBy);
					existingInjury.setModifiedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(existingInjury);
					
					successCount++;
				} catch (Exception ex) {
					log.warn("Error while processing Injury Not Reported record: " + recordCount + ". " + ex);
					
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing injuries not reported...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Injury Not Reported data: " + ex);
		}
		
		return errorList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importWMTickets(InputStream is, String locationType, String destinationLocation, 
			Long createdBy) throws Exception {
		SimpleDateFormat wmDateTimeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		// 5/30/2017 4:15:53 AM
		SimpleDateFormat wmDateTimeStrFormat = new SimpleDateFormat("M/dd/yyyy h:mm:ss a");
		// 2017-02-03 00:00:00
		SimpleDateFormat requiredDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat requiredTimeFormat = new SimpleDateFormat("HH:mm");
		
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		int successCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Location origin = null;
			Location destination = null;
			Location locationPtr = null;
			if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, locationType)) {
				origin = retrieveOriginForWM(sheet);
				locationPtr = origin;
				if (origin == null) {
					errorList.add("Origin could not be determined");
				}
			} else {
				//destination = retrieveDestinationForWM(sheet);
				destination = retrieveDestinationForWM(destinationLocation);
				locationPtr = destination;
				if (destination == null) {
					errorList.add("Destination could not be determined");
				}
			}
			
			if (!errorList.isEmpty()) {
				return errorList;
			}
			
			Map<String, Integer> colMapping = TicketUtils.getWMTicketColMapping(locationPtr.getId());
			if (colMapping.size() <= 0) {
				errorList.add("Location not supported");
				return errorList;
			}
			
			int recordsToBeSkipped = TicketUtils.getWMTicketRecordsToBeSkipped(locationPtr.getId());
			
			Iterator rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount <= recordsToBeSkipped) {
					continue;
				}
				
				boolean recordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				WMTicket currentWMTicket = null;
				Ticket ticketToBeSaved = null;
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						break;
					}
					
					currentWMTicket = new WMTicket();
					if (origin != null) {
						currentWMTicket.setTicketType(WMTicket.ORIGIN_TICKET_TYPE);
						currentWMTicket.setOrigin(origin);
					} else {
						currentWMTicket.setTicketType(WMTicket.DESTINATION_TICKET_TYPE);
						currentWMTicket.setDestination(destination);
					}
					
					Integer ticketCol = colMapping.get(TicketUtils.WM_COL_TICKET);
					String ticketStr = ((String) getCellValue(row.getCell(ticketCol)));
					if (StringUtils.isEmpty(ticketStr)) {
						recordError = true;
						recordErrorMsg.append("Ticket, ");
					} else {
						Long ticket = Long.parseLong(ticketStr);
						currentWMTicket.setTicket(ticket);
						if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, currentWMTicket.getTicketType())) {
							currentWMTicket.setOriginTicket(ticket);
						} else {
							currentWMTicket.setDestinationTicket(ticket);
						}
					}
					
					Integer haulingTicketCol = colMapping.get(TicketUtils.WM_COL_HAULING_TICKET);
					if (haulingTicketCol != null) {
						String haulingTicketStr = ((String) getCellValue(row.getCell(haulingTicketCol)));
						if (StringUtils.isNotEmpty(haulingTicketStr)
								&& StringUtils.isNumeric(haulingTicketStr)) {
							currentWMTicket.setHaulingTicket(Long.parseLong(haulingTicketStr));
						} 
					}
					
					Integer txnDateCol = colMapping.get(TicketUtils.WM_COL_TXN_DATE);
					Object txnDateObj = getCellValue(row.getCell(txnDateCol), true);
					if (txnDateObj == null) {
						recordError = true;
						recordErrorMsg.append("Date, ");
					} else if (txnDateObj instanceof Date) {
						currentWMTicket.setTxnDate((Date)txnDateObj);
					} else {
						String txnDateStr = txnDateObj.toString();
						Date txnDate = wmDateTimeStrFormat.parse(txnDateStr);
						currentWMTicket.setTxnDate(txnDate);
					}
					
					Integer timeInCol = colMapping.get(TicketUtils.WM_COL_TIME_IN);
					Object timeInObj = getCellValue(row.getCell(timeInCol), true);
					if (timeInObj == null) {
						recordError = true;
						recordErrorMsg.append("Time In, ");
					} else {
						SimpleDateFormat timeInDateFormat = wmDateTimeFormat;
						if (!(timeInObj instanceof Date)) {
							timeInDateFormat = wmDateTimeStrFormat;
						}
						
						String timeInStr = timeInObj.toString();
						String reqTimeInStr = convertDateFormat(timeInStr, timeInDateFormat, requiredTimeFormat);
						if (StringUtils.isEmpty(reqTimeInStr)) {
							recordError = true;
							recordErrorMsg.append("Time In, ");
						} else {
							currentWMTicket.setTimeIn(reqTimeInStr);
						}
					} 
					
					Integer timeOutCol = colMapping.get(TicketUtils.WM_COL_TIME_OUT);
					Object timeOutObj = getCellValue(row.getCell(timeOutCol), true);
					if (timeOutObj == null) {
						recordError = true;
						recordErrorMsg.append("Time Out, ");
					} else {
						SimpleDateFormat timeOutDateFormat = wmDateTimeFormat;
						if (!(timeOutObj instanceof Date)) {
							timeOutDateFormat = wmDateTimeStrFormat;
						}
						
						String timeOutStr = timeOutObj.toString();
						String reqTimeOutStr = convertDateFormat(timeOutStr, timeOutDateFormat, requiredTimeFormat);
						if (StringUtils.isEmpty(reqTimeOutStr)) {
							recordError = true;
							recordErrorMsg.append("Time Out, ");
						} else {
							currentWMTicket.setTimeOut(reqTimeOutStr);
							
							Date loadUnloadDate = convertDate(timeOutStr, timeOutDateFormat, requiredDateFormat);
							if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, currentWMTicket.getTicketType())) {
								currentWMTicket.setLoadDate(loadUnloadDate);
							} else {
								currentWMTicket.setUnloadDate(loadUnloadDate);
								Date batchDate = TicketUtils.calculateBatchDate(loadUnloadDate);
								currentWMTicket.setBillBatch(batchDate);
							}
						}
					}
					
					Integer wmCompanyCol = colMapping.get(TicketUtils.WM_COL_COMPANY);
					if (wmCompanyCol != null) {
						String wmCompanyStr = ((String) getCellValue(row.getCell(wmCompanyCol)));
						if (StringUtils.isNotEmpty(wmCompanyStr)) {
							currentWMTicket.setWmCompany(wmCompanyStr);
						} 
					}
					
					if (checkCompanyToBeSkipped(currentWMTicket)) {
						continue;
					}
					
					Integer wmHaulingCompanyCol = colMapping.get(TicketUtils.WM_COL_HAULING_COMPANY);
					if (wmHaulingCompanyCol != null) {
						String wmHaulingCompanyStr = ((String) getCellValue(row.getCell(wmHaulingCompanyCol)));
						if (StringUtils.isNotEmpty(wmHaulingCompanyStr)) {
							currentWMTicket.setWmHaulingCompany(wmHaulingCompanyStr);
							
							if (StringUtils.equals(WMTicket.DESTINATION_TICKET_TYPE, currentWMTicket.getTicketType())) {
								/*List<Location> derivedOriginList = retrieveLocationDataByQualifier(1, "haulingName", 
										wmHaulingCompanyStr);
								if (derivedOriginList != null && !derivedOriginList.isEmpty()) {
									currentWMTicket.setOrigin(derivedOriginList.get(0));
								}*/
								
								List<WMLocation> wmLocationList = retrieveWMLocationByName(wmHaulingCompanyStr, 1);
								if (wmLocationList != null && !wmLocationList.isEmpty()) {
									currentWMTicket.setOrigin(wmLocationList.get(0).getLocation());
								}
							}
						} 
					}
					Integer wmDestinationCol = colMapping.get(TicketUtils.WM_COL_DESTINATION);
					if (wmDestinationCol != null) {
						String wmDestinationStr = ((String) getCellValue(row.getCell(wmDestinationCol)));
						if (StringUtils.isNotEmpty(wmDestinationStr)) {
							currentWMTicket.setWmDestination(wmDestinationStr);
						} 
					}
					Integer wmVehicleCol = colMapping.get(TicketUtils.WM_COL_VEHICLE);
					if (wmVehicleCol != null) {
						String wmVehicleStr = ((String) getCellValue(row.getCell(wmVehicleCol)));
						if (StringUtils.isNotEmpty(wmVehicleStr)) {
							currentWMTicket.setWmVehicle(wmVehicleStr);
						} 
					}
					Integer wmTrailerCol = colMapping.get(TicketUtils.WM_COL_TRAILER);
					if (wmTrailerCol != null) {
						String wmTrailerStr = ((String) getCellValue(row.getCell(wmTrailerCol)));
						if (StringUtils.isNotEmpty(wmTrailerStr)) {
							currentWMTicket.setWmTrailer(wmTrailerStr);
						} 
					}
					
					Integer grossCol = colMapping.get(TicketUtils.WM_COL_GROSS);
					Object grossObj = getCellValue(row.getCell(grossCol), true);
					Double grossWeight = null;
					if (grossObj instanceof Double) {
						grossWeight = (Double) grossObj;
					} else {
						grossWeight = Double.valueOf((String) grossObj);
					}
					currentWMTicket.setGross(grossWeight);
					
					Integer tareCol = colMapping.get(TicketUtils.WM_COL_TARE);
					Object tareObj = getCellValue(row.getCell(tareCol), true);
					Double tareWeight = null;
					if (tareObj instanceof Double) {
						tareWeight = (Double) tareObj;
					} else {
						tareWeight = Double.valueOf((String) tareObj);
					}
					currentWMTicket.setTare(tareWeight);
					
					Integer netCol = colMapping.get(TicketUtils.WM_COL_NET);
					if (netCol != null) {
						Object netObj = getCellValue(row.getCell(netCol), true);
						Double netWeight = null;
						if (netObj instanceof Double) {
							netWeight = (Double) netObj;
						} else {
							netWeight = Double.valueOf((String) netObj);
						}
						currentWMTicket.setNet(netWeight);
					}
					Integer tonsCol = colMapping.get(TicketUtils.WM_COL_TONS);
					if (tonsCol != null) {
						Object tonsObj = getCellValue(row.getCell(tonsCol), true);
						Double tonWeight = null;
						if (tonsObj instanceof Double) {
							tonWeight = (Double) tonsObj;
						} else {
							tonWeight = Double.valueOf((String) tonsObj);
						}
						currentWMTicket.setTons(tonWeight);
					}
					Integer rateCol = colMapping.get(TicketUtils.WM_COL_RATE);
					if (rateCol != null) {
						Object rateObj = getCellValue(row.getCell(rateCol), true);
						Double rate = null;
						if (rateObj instanceof Double) {
							rate = (Double) rateObj;
						} else {
							rate = Double.valueOf((String) rateObj);
						}
						currentWMTicket.setRate(rate);
					}
					Integer amountCol = colMapping.get(TicketUtils.WM_COL_AMOUNT);
					if (amountCol != null) {
						Object amountObj = getCellValue(row.getCell(amountCol), true);
						Double amount = null;
						if (amountObj instanceof Double) {
							amount = (Double) amountObj;
						} else {
							amount = Double.valueOf((String) amountObj);
						}
						currentWMTicket.setAmount(amount);
					}
					
					mapBasedOnTicketType(currentWMTicket);
					TicketUtils.calculateNetAndTons(currentWMTicket);
					
					if (checkDuplicateWMTicket(currentWMTicket)) {
						/*recordError = true;
						recordErrorMsg.append("Duplicate WM Ticket, ");*/
						continue;
					}
					
					if (recordError) {
						errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					} 
					
					currentWMTicket.setStatus(1);
					currentWMTicket.setCreatedBy(createdBy);
					currentWMTicket.setCreatedAt(Calendar.getInstance().getTime());
					
					Ticket existigTicket = retrieveMatchingTicket(currentWMTicket);
					if (existigTicket != null) {
						currentWMTicket.setProcessingStatus(WMTicket.PROCESSING_STATUS_TICKET_ALREADY_EXISTS);
						genericDAO.saveOrUpdate(currentWMTicket);
						
						/*recordError = true;
						errorList.add("Line " + recordCount + ": " + "Ticket already exists" + "<br/>");
						errorCount++;*/
						
						continue;
					}
					
					StringBuffer errorMsgBuff = new StringBuffer();
					WMTicket destinationTicketCopy = checkAndSetUpAsDestinationWMTicket(currentWMTicket, errorMsgBuff);
					if (errorMsgBuff.length() != 0) {
						recordError = true;
						errorList.add("Line " + recordCount + ": " + errorMsgBuff.toString() + "<br/>");
						errorCount++;
						continue;
					}
					
					TripSheet tripSheet = retrieveMatchingTripsheet(currentWMTicket);
					if (tripSheet == null) {
						currentWMTicket.setProcessingStatus(WMTicket.PROCESSING_STATUS_NO_TRIPSHEET);
						genericDAO.saveOrUpdate(currentWMTicket);
						
						if (destinationTicketCopy != null) {
							destinationTicketCopy.setProcessingStatus(WMTicket.PROCESSING_STATUS_NO_TRIPSHEET);
							genericDAO.saveOrUpdate(destinationTicketCopy);
						}
						
						/*recordError = true;
						errorList.add("Line " + recordCount + ": " + "Did not find matching Trip sheet.  WM Ticket is saved for further processing." + "<br/>");
						errorCount++;*/
						
						continue;
					}
					
					TicketUtils.map(currentWMTicket, tripSheet);
					if (destinationTicketCopy != null) {
						TicketUtils.map(destinationTicketCopy, tripSheet);
						destinationTicketCopy.setProcessingStatus(WMTicket.PROCESSING_STATUS_PROCESSING);
						genericDAO.saveOrUpdate(destinationTicketCopy);
					}
					
					// Driver subcontractor change 2 - 21st Jul 2017
					if (!TicketUtils.canCreateTicket(tripSheet)) {
						currentWMTicket.setProcessingStatus(WMTicket.PROCESSING_STATUS_PROCESSING);
						genericDAO.saveOrUpdate(currentWMTicket);
						continue;
					}
						
					WMTicket wmOriginTicket = null;
					WMTicket wmDestinationTicket = null;
					WMTicket correspondingTicket = null;
					if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, currentWMTicket.getTicketType())) {
						wmDestinationTicket = TicketUtils.retrieveWMTicket(tripSheet.getDestinationTicket(), tripSheet.getDestination().getId(), 
								false, WMTicket.PROCESSING_STATUS_PROCESSING, genericDAO);
						if (wmDestinationTicket != null) {
							currentWMTicket.setLandfillGross(wmDestinationTicket.getLandfillGross());
							currentWMTicket.setLandfillTare(wmDestinationTicket.getLandfillTare());
							currentWMTicket.setLandfillTimeIn(wmDestinationTicket.getLandfillTimeIn());
							currentWMTicket.setLandfillTimeOut(wmDestinationTicket.getLandfillTimeOut());
							
							ticketToBeSaved = new Ticket();
							correspondingTicket = wmDestinationTicket;
						}
					} else {
						wmOriginTicket = TicketUtils.retrieveWMTicket(tripSheet.getOriginTicket(), tripSheet.getOrigin().getId(),
								true, WMTicket.PROCESSING_STATUS_PROCESSING, genericDAO);
						if (wmOriginTicket != null) {
							currentWMTicket.setTransferGross(wmOriginTicket.getTransferGross());
							currentWMTicket.setTransferTare(wmOriginTicket.getTransferTare());
							currentWMTicket.setTransferTimeIn(wmOriginTicket.getTransferTimeIn());
							currentWMTicket.setTransferTimeOut(wmOriginTicket.getTransferTimeOut());
							
							ticketToBeSaved = new Ticket();
							correspondingTicket = wmOriginTicket;
						}
					}
					
					if (ticketToBeSaved == null) {
						currentWMTicket.setProcessingStatus(WMTicket.PROCESSING_STATUS_PROCESSING);
						genericDAO.saveOrUpdate(currentWMTicket);
						continue;
					}
					
					map(ticketToBeSaved, currentWMTicket);
					TicketUtils.calculateNetAndTons(ticketToBeSaved);
					TicketUtils.setAutomaticTicketData(ticketToBeSaved);
					TicketUtils.save(ticketToBeSaved, "complete", recordErrorMsg, genericDAO);
					
					if (recordErrorMsg.length() != 0) {
						errorList.add("Line " + recordCount + ": Error while saving Ticket: "
											+ recordErrorMsg.toString() + "<br/>");
						errorCount++;
						continue;
					}
					
					successCount++;
					currentWMTicket.setProcessingStatus(WMTicket.PROCESSING_STATUS_DONE);
					genericDAO.saveOrUpdate(currentWMTicket);
					
					if (correspondingTicket != null) {
						correspondingTicket.setProcessingStatus(WMTicket.PROCESSING_STATUS_DONE);
						correspondingTicket.setModifiedBy(createdBy);
						correspondingTicket.setModifiedAt(Calendar.getInstance().getTime());
						genericDAO.saveOrUpdate(correspondingTicket);
					}
				} catch (Exception ex) {
					recordError = true;
					errorCount++;
					recordErrorMsg.append("Error while processing record: " + recordCount + ", ");
					errorList.add("Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records loaded: " + successCount);
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing WM Tickets: " + ex);
		}
		
		return errorList;
	}
	
	private WMTicket checkAndSetUpAsDestinationWMTicket(WMTicket wmTicket, StringBuffer errorMsgBuff) {
		if (!StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, wmTicket.getTicketType())) {
			return null;
		}
		
		if (StringUtils.isEmpty(wmTicket.getWmDestination())) {
			return null;
		}
		
		long originId = wmTicket.getOrigin().getId().longValue();
		List<LocationPair> locationPairList = retrieveLocationPairForOrigin(originId);
		if (locationPairList == null || locationPairList.isEmpty()) {
			return null;
		}
		
		/*if (originId != 67l // Varick I Transfer 
				&& originId != 70l) { // Waverly Transfer 
			return null;
		}*/
		
		/*List<Location> derivedDestinationList = retrieveLocationDataByQualifier(2, "haulingName", 
				wmTicket.getWmDestination());
		if (derivedDestinationList == null || derivedDestinationList.isEmpty()) {
			return null;
		} 
		Location derivedDestination = derivedDestinationList.get(0);
		if (derivedDestination.getId() != 230l // Varick II Landfill
				&& derivedDestination.getId() != 82l // Reco Landfill
				&& derivedDestination.getId() != 384l // Chesapeake Landfill
			   && derivedDestination.getId() != 379l) { // Shamrock Landfill
			return null;
		}*/
		
		List<WMLocation> wmLocationList = retrieveWMLocationByName(wmTicket.getWmDestination(), 2);
		if (wmLocationList == null || wmLocationList.isEmpty()) {
			errorMsgBuff.append("Destination not found");
			return null;
		}
		Location derivedDestination = wmLocationList.get(0).getLocation();
		long derivedDestinationId = derivedDestination.getId().longValue();
		
		boolean copyAsDest = false;
		for (LocationPair aLocationPair : locationPairList) {
			if (derivedDestinationId == aLocationPair.getDestination().getId().longValue()) {
				copyAsDest = true;
				break;
			}
		}
		if (!copyAsDest) {
			return null;
		}
		
		WMTicket destinationWMTicket = new WMTicket();
		copyAsDestination(destinationWMTicket, wmTicket, derivedDestination);
		
		return destinationWMTicket;
	}
	
	private boolean checkCompanyToBeSkipped(WMTicket wmTicket) {
		if (!StringUtils.equals(WMTicket.DESTINATION_TICKET_TYPE, wmTicket.getTicketType())) {
			return false;
		}
		if (wmTicket.getDestination().getId().longValue() != 392l) { // Fairless Landfill
			return false;
		}
		if (StringUtils.isEmpty(wmTicket.getWmCompany())) {
			return true;
		}
		if (StringUtils.equals("LU", wmTicket.getWmCompany()) 
				|| StringUtils.equals("WB", wmTicket.getWmCompany())) {
			return false;
		}
		return true;
	}
	
	private void copyAsDestination(WMTicket copy, WMTicket orig, Location destination) {
		copy.setTicket(orig.getTicket());
		copy.setTxnDate(orig.getTxnDate());
		copy.setCreatedAt(orig.getCreatedAt());
		copy.setCreatedBy(orig.getCreatedBy());
		copy.setStatus(orig.getStatus());
		copy.setProcessingStatus(orig.getProcessingStatus());
		
		copy.setWmVehicle(orig.getWmVehicle());
		copy.setWmTrailer(orig.getWmTrailer());
		copy.setWmCompany(orig.getWmCompany());
		
		copy.setVehicle(orig.getVehicle());
		copy.setTrailer(orig.getTrailer());
		
		copy.setDriver(orig.getDriver());
		copy.setDriverCompany(orig.getDriverCompany());
		copy.setTerminal(orig.getTerminal());
		
		copy.setTimeIn(orig.getTimeIn());
		copy.setTimeOut(orig.getTimeOut());
		
		copy.setGross(orig.getGross());
		copy.setTare(orig.getTare());
		copy.setNet(orig.getNet());
		copy.setTons(orig.getTons());
		
		copy.setTicketType(WMTicket.DESTINATION_TICKET_TYPE);
		copy.setDestination(destination);
		copy.setDestinationTicket(orig.getTicket());
		
		copy.setOrigin(orig.getOrigin());
		copy.setHaulingTicket(orig.getTicket());
		
		copy.setUnloadDate(orig.getLoadDate());
		Date batchDate = TicketUtils.calculateBatchDate(copy.getUnloadDate());
		copy.setBillBatch(batchDate);
		
		copy.setLandfillTimeIn(orig.getTransferTimeIn());
		copy.setLandfillTimeOut(orig.getTransferTimeOut());
		copy.setLandfillGross(orig.getTransferGross());
		copy.setLandfillTare(orig.getTransferTare());
		copy.setLandfillNet(orig.getTransferNet());
		copy.setLandfillTons(orig.getTransferTons());
	}
	
	private Location retrieveOriginForWM(HSSFSheet sheet) {
		int originRowNo = 2;
		HSSFRow originRow = sheet.getRow(originRowNo);
		String originName = ((String) getCellValue(originRow.getCell(0)));
		originName = StringUtils.trimToEmpty(originName);
		if (StringUtils.isEmpty(originName)) {
			return null;
		}
		
		List<WMLocation> wmLocationList = retrieveWMLocationByName(originName, 1);
		if (wmLocationList == null || wmLocationList.isEmpty()) {
			return null;
		} else {
			return wmLocationList.get(0).getLocation();
		}
		
		/*if (StringUtils.contains(originName, Location.FORGE_TRANSFER_STATION)) { 
			originName = Location.FORGE_TRANSFER_STATION;
		} else if (StringUtils.contains(originName, Location.PHILADELPHIA_TRANSFER_STATION)) { 
			originName = Location.PHILADELPHIA_TRANSFER_STATION;
		} else if (StringUtils.contains(originName, Location.BQE_TRANSFER_STATION)) { 
			originName = Location.BQE_TRANSFER_STATION;
		} else if (StringUtils.contains(originName, Location.VARICK_I_TRANSFER_STATION)) { 
			originName = Location.VARICK_I_TRANSFER_STATION;
		} else if (StringUtils.contains(originName, Location.YONKERS_TRANSFER_STATION)) { 
			originName = Location.YONKERS_TRANSFER_STATION;
		} else if (StringUtils.contains(originName, Location.WAVERLY_TRANSFER_STATION)) { 
			originName = Location.WAVERLY_TRANSFER_STATION;
		}
		
		List<Location> originList = retrieveLocationDataByLongName(1, originName);
		if (originList == null || originList.isEmpty()) {
			return null;
		} else {
			return originList.get(0);
		}*/
	}
	
	private Location retrieveDestinationForWM(HSSFSheet sheet) {
		HSSFRow destinationRow = sheet.getRow(1);
		String destinationName = ((String) getCellValue(destinationRow.getCell(0)));
		destinationName = StringUtils.trimToEmpty(destinationName);
		if (StringUtils.isEmpty(destinationName)) {
			return null;
		}
		
		List<Location> destinationList = retrieveLocationDataByLongName(2, destinationName);
		if (destinationList == null || destinationList.isEmpty()) {
			return null;
		} else {
			return destinationList.get(0);
		}
	}
	
	private Location retrieveDestinationForWM(String destinationLocation) {
		if (StringUtils.isEmpty(destinationLocation)) {
			return null;
		}
		
		return retrieveLocationById(destinationLocation);
	}
	
	private void mapBasedOnTicketType(WMTicket wmTicket) {
		if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, wmTicket.getTicketType())) {
			wmTicket.setTransferGross(wmTicket.getGross());
			wmTicket.setTransferTare(wmTicket.getTare());
			wmTicket.setTransferTimeIn(wmTicket.getTimeIn());
			wmTicket.setTransferTimeOut(wmTicket.getTimeOut());
		} else {
			wmTicket.setLandfillGross(wmTicket.getGross());
			wmTicket.setLandfillTare(wmTicket.getTare());
			wmTicket.setLandfillTimeIn(wmTicket.getTimeIn());
			wmTicket.setLandfillTimeOut(wmTicket.getTimeOut());
		}
	}
	
	private String convertDateFormat(String actualDateStr, SimpleDateFormat actualDateFormat, SimpleDateFormat requiredDateFormat) throws ParseException {
		if (StringUtils.isEmpty(actualDateStr)) {
			return StringUtils.EMPTY;
		}
		
		Date actualDate = actualDateFormat.parse(actualDateStr);
		String requiredDateStr = requiredDateFormat.format(actualDate);
		return requiredDateStr;
	}
	
	private Date convertDate(String actualDateStr, SimpleDateFormat actualDateFormat, SimpleDateFormat requiredDateFormat) throws ParseException {
		if (StringUtils.isEmpty(actualDateStr)) {
			return null;
		}
		
		Date actualDate = actualDateFormat.parse(actualDateStr);
		String requiredDateStr = requiredDateFormat.format(actualDate);
		return requiredDateFormat.parse(requiredDateStr);
	}
	
	private void map(Ticket ticket, WMTicket wmTicket) {
		ticket.setVehicle(wmTicket.getVehicle());
		ticket.setTrailer(wmTicket.getTrailer());
		
		ticket.setDriver(wmTicket.getDriver());
		ticket.setDriverCompany(wmTicket.getDriverCompany());
		ticket.setTerminal(wmTicket.getTerminal());
		
		ticket.setBillBatch(wmTicket.getBillBatch());
		
		ticket.setOrigin(wmTicket.getOrigin());
		ticket.setDestination(wmTicket.getDestination());
		
		ticket.setOriginTicket(wmTicket.getOriginTicket());
		ticket.setDestinationTicket(wmTicket.getDestinationTicket());
		
		ticket.setLoadDate(wmTicket.getLoadDate());
		ticket.setUnloadDate(wmTicket.getUnloadDate());
		
		ticket.setTransferTimeIn(wmTicket.getTransferTimeIn());
		ticket.setTransferTimeOut(wmTicket.getTransferTimeOut());
		ticket.setLandfillTimeIn(wmTicket.getLandfillTimeIn());
		ticket.setLandfillTimeOut(wmTicket.getLandfillTimeOut());
		
		ticket.setTransferGross(wmTicket.getTransferGross());
		ticket.setTransferTare(wmTicket.getTransferTare());
		
		ticket.setLandfillGross(wmTicket.getLandfillGross());
		ticket.setLandfillTare(wmTicket.getLandfillTare());
		
		// Driver subcontractor change 2 - 21st Jul 2017
		ticket.setSubcontractor(wmTicket.getSubcontractor());
	}
	
	private Ticket retrieveMatchingTicket(WMTicket wmTicket) {
		Ticket ticket = null;
		if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, wmTicket.getTicketType())) {
			ticket = retrieveTicket(wmTicket.getTicket(), wmTicket.getOrigin(), null);
		} else {
			ticket = retrieveTicket(wmTicket.getTicket(), null, wmTicket.getDestination());
		}
		
		return ticket;
	}
	
	private Ticket retrieveTicket(Long ticketNo, Location origin, Location destination) {
		String query = "select obj from Ticket obj where";
		if (origin != null) {
			query += (" obj.origin=" + origin.getId() + " and obj.originTicket=" + ticketNo);
		} else {
			query += (" obj.destination=" + destination.getId() + " and obj.destinationTicket=" + ticketNo);
		}
		
		List<Ticket> ticketList = genericDAO.executeSimpleQuery(query);
		return (ticketList == null || ticketList.isEmpty()) ? null : ticketList.get(0);
	}
	
	private TripSheet retrieveMatchingTripsheet(WMTicket wmTicket) {
		TripSheet tripSheet = null;
		if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, wmTicket.getTicketType())) {
			tripSheet = retrieveTripSheet(wmTicket.getTicket(), wmTicket.getOrigin(), null);
		} else {
			tripSheet = retrieveTripSheet(wmTicket.getTicket(), null, wmTicket.getDestination());
		}
		
		return tripSheet;
	}
	
	private TripSheet retrieveTripSheet(Long ticketNo, Location origin, Location destination) {
		String query = "select obj from TripSheet obj where";
		if (origin != null) {
			query += (" obj.origin=" + origin.getId() + " and obj.originTicket=" + ticketNo);
		} else {
			query += (" obj.destination=" + destination.getId() + " and obj.destinationTicket=" + ticketNo);
		}
		
		List<TripSheet> tripSheetList = genericDAO.executeSimpleQuery(query);
		return (tripSheetList == null || tripSheetList.isEmpty()) ? null : tripSheetList.get(0);
	}
	
	private List<LocationPair> retrieveLocationPairForOrigin(Long originId) {
		String query = "select obj from LocationPair obj where";
		query += (" obj.origin=" + originId);
		
		List<LocationPair> locationPairList = genericDAO.executeSimpleQuery(query);
		return locationPairList;
	}
	
	private List<WMLocation> retrieveWMLocationByName(String wmLocationName, int type) {
		String query = "select obj from WMLocation obj where";
		query += (" obj.wmLocationName='" + wmLocationName + "'");
		query += (" and obj.location.type=" + type);
		
		List<WMLocation> wmLocationList = genericDAO.executeSimpleQuery(query);
		return wmLocationList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importEmployeeMainSheet(InputStream is, Long createdBy) throws Exception {
		List<Driver> driverListToBeSaved = new ArrayList<Driver>();
		List<String> errorList = new ArrayList<String>();
		
		SimpleDateFormat dobFormat = new SimpleDateFormat("M/d/yyyy");
		
		int recordCount = 0;
		int dataSetIndex = 0;
		int errorCount = 0;
		String employeeName = StringUtils.EMPTY;
		boolean dataSetError = false;
		boolean fatalDataSetError = false;
		StringBuffer dataSetErrorMsg = new StringBuffer();
		List<Driver> dataSetDriverList = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Iterator rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				/*if (recordCount < 10) {
					continue;
				}*/
				
				try {
					String endOfData = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", endOfData)) {
						if (dataSetError) {
							String msgPreffix = fatalDataSetError ? "Record NOT loaded->" : "Record LOADED, but has errors->";
							errorList.add(msgPreffix 
									+ "Data set: " + (dataSetIndex) + ","
									+ " For employee: " + employeeName
									+ "->Errors: " + dataSetErrorMsg.toString() + "<br/>");
							errorCount++;
						} 
						
						if (!fatalDataSetError && dataSetDriverList != null && !dataSetDriverList.isEmpty()) {
							driverListToBeSaved.addAll(dataSetDriverList);
						}
						
						break;
					}
					
					String codeHeader = ((String) getCellValue(row.getCell(8)));
					if (StringUtils.equals("Code:", codeHeader)) {
						dataSetIndex++;
						
						if (dataSetError) {
							String msgPreffix = fatalDataSetError ? "Record NOT loaded->" : "Record LOADED, but has errors->";
							errorList.add(msgPreffix 
									+ "Data set: " + (dataSetIndex-1) + ","
									+ " For employee: " + employeeName
									+ "->Errors: " + dataSetErrorMsg.toString() + "<br/>");
							errorCount++;
						} 
						
						if (!fatalDataSetError && dataSetDriverList != null && !dataSetDriverList.isEmpty()) {
							driverListToBeSaved.addAll(dataSetDriverList);
						}
						
						dataSetDriverList = null;
						dataSetError = false;
						fatalDataSetError = false;
						dataSetErrorMsg = new StringBuffer();
						
						employeeName = ((String) getCellValue(row.getCell(1)));
						employeeName = StringUtils.trimToEmpty(employeeName);
						
						dataSetDriverList = retrieveDriver(employeeName);
						if (dataSetDriverList == null || dataSetDriverList.isEmpty()) {
							dataSetError = true;
							fatalDataSetError = true;
							dataSetErrorMsg.append("Employee Name, ");
							continue;
						}
						
						String ssn = ((String) getCellValue(row.getCell(9)));
						ssn = StringUtils.trimToEmpty(ssn);
						if (StringUtils.isEmpty(ssn)) {
							dataSetError = true;
							fatalDataSetError = true;
							dataSetErrorMsg.append("SSN, ");
							continue;
						}
						
						if (!fatalDataSetError && dataSetDriverList != null && !dataSetDriverList.isEmpty()) {
							for (Driver driver: dataSetDriverList) {
								driver.setSsn(ssn);
							}
						}
					}
					
					String licenseHeader = ((String) getCellValue(row.getCell(1)));
					if (StringUtils.equals("License Number:", licenseHeader)) {
						String driverLicense = ((String) getCellValue(row.getCell(5)));
						driverLicense = StringUtils.trimToEmpty(driverLicense);
						if (StringUtils.isEmpty(driverLicense)) {
							dataSetError = true;
							fatalDataSetError = true;
							dataSetErrorMsg.append("Driver License, ");
							continue;
						}
						
						String driverLicenseStateStr = ((String) getCellValue(row.getCell(8)));
						driverLicenseStateStr = StringUtils.trimToEmpty(driverLicenseStateStr);
						State driverLicenseState = retrieveStateByCode(driverLicenseStateStr);
						if (driverLicenseState == null) {
							dataSetError = true;
							fatalDataSetError = true;
							dataSetErrorMsg.append("Driver License State, ");
							continue;
						}
						
						if (!fatalDataSetError && dataSetDriverList != null && !dataSetDriverList.isEmpty()) {
							for (Driver driver: dataSetDriverList) {
								driver.setDriverLicense(driverLicense);
								driver.setDriverLicenseState(driverLicenseState);
							}
						}
					}
					
					String dobHeader = ((String) getCellValue(row.getCell(1)));
					if (StringUtils.equals("Date of Birth:", dobHeader)) {
						String dobStr = ((String) getCellValue(row.getCell(5)));
						dobStr = StringUtils.trimToEmpty(dobStr);
						if (StringUtils.isEmpty(dobStr)) {
							dataSetError = true;
							fatalDataSetError = true;
							dataSetErrorMsg.append("DOB, ");
							continue;
						}
						
						Date dob = null;
						try {
							dob = dobFormat.parse(dobStr);
						} catch (ParseException pe) {
							dataSetError = true;
							fatalDataSetError = true;
							dataSetErrorMsg.append("DOB, ");
							continue;
						}
						
						if (!fatalDataSetError && dataSetDriverList != null && !dataSetDriverList.isEmpty()) {
							for (Driver driver: dataSetDriverList) {
								driver.setDob(dob);						
							}
						}
					}
					
					/*if (checkDuplicate()) {
						dataSetError = true;
						fatalDataSetError = true;
						dataSetErrorMsg.append("Duplicate Record, ");
					}*/
				} catch (Exception ex) {
					dataSetError = true;
					fatalDataSetError = true;
					dataSetErrorMsg.append("Error while processing record, ");
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount
					+ ". Data set count: " + dataSetIndex
					+ ". Error count: " + errorCount
					+ ". Number of records being loaded: " + driverListToBeSaved.size());
			for (Driver driverToBeSaved : driverListToBeSaved) {
				System.out.println("Now loading employee: " + driverToBeSaved.getFullName());
				
				driverToBeSaved.setModifiedBy(createdBy);
				driverToBeSaved.setModifiedAt(Calendar.getInstance().getTime());
				
				genericDAO.saveOrUpdate(driverToBeSaved);
			}
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Employee data: " + ex);
		}
		
		return errorList;
	}
	
	private List<Driver> retrieveDriver(String fullName) {
		if (StringUtils.isEmpty(fullName)) {
			return null;
		}
		
		if (fullName.contains("'")) {
			return null;
		}
		
		String nameTokens[] = fullName.split("[\\s]", 2);
		if (nameTokens.length < 2) {
			return null;
		}
		
		String baseQuery = "select obj from Driver obj where"; 
		String whereClause = " obj.firstName='"+nameTokens[0]+"' and obj.lastName='"+nameTokens[1]+"'";		
		List<Driver> driverList = genericDAO.executeSimpleQuery(baseQuery + whereClause);
		
		return driverList;
	}
	
	private State retrieveStateByCode(String stateCode) {
		if (StringUtils.isEmpty(stateCode)) {
			return null;
		}
		
		String baseQuery = "select obj from State obj where"; 
		String whereClause = " obj.code='"+stateCode+"'";		
		List<State> stateList = genericDAO.executeSimpleQuery(baseQuery + whereClause);
		
		if (stateList == null || stateList.isEmpty() || stateList.size() > 1) {
			return null;
		}
		
		return stateList.get(0);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importMileageLogMainSheet(InputStream is, Date period, Double resetMiles, Long createdBy) throws Exception {
		List<MileageLog> mileageLogList = new ArrayList<MileageLog>();
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Iterator rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				
				recordCount++;
				System.out.println("Processing record No: " + recordCount);
				if (recordCount == 1) {
					continue;
				}
				
				boolean recordError = false;
				boolean fatalRecordError = false;
				StringBuffer recordErrorMsg = new StringBuffer();
				MileageLog mileageLog = null;
				try {
					String unit = ((String) getCellValue(row.getCell(0)));
					if (StringUtils.equals("END_OF_DATA", unit)) {
						break;
					}
					
					String stateStr = ((String) getCellValue(row.getCell(2)));
					if (StringUtils.equals("Total", stateStr)) {
						continue;
					}
					
					mileageLog = new MileageLog();
					
					String firstInStateStr = ((String) getCellValue(row.getCell(4)));
					Date firstInState = processFirstInState(firstInStateStr);
					if (firstInState == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("First in State,");
					} else {
						mileageLog.setFirstInState(firstInState);
					}
					
					String lastInStateStr = ((String) getCellValue(row.getCell(5)));
					Date lastInState = processLastInState(lastInStateStr);
					if (lastInState == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Last in State,");
					} else {
						mileageLog.setLastInState(lastInState);
					}
					
					Vehicle vehicle = retrieveVehicle(unit, lastInState);
					if (vehicle == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Unit,");
					} else {
						mileageLog.setUnitNum(unit);
						mileageLog.setUnit(vehicle);
						mileageLog.setCompany(vehicle.getOwner());
					}
					
					State state = retrieveState(stateStr);
					if (state == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("State,");
					} else {
						mileageLog.setState(state);
					}
					
					String miles = ((String) getCellValue(row.getCell(3)));
					Double milesDbl = processMiles(miles, resetMiles);
					if (milesDbl == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Miles,");
					} else {
						mileageLog.setMiles(milesDbl);
					}
					
					String vin = ((String) getCellValue(row.getCell(7)));
					if (!validateVin(vin)) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("VIN,");
					} else {
						mileageLog.setVin(vin);
					}
					
					String groups = ((String) getCellValue(row.getCell(6)));
					groups = StringUtils.isEmpty(groups) ? StringUtils.EMPTY : groups;
					mileageLog.setGroups(groups);
					
					mileageLog.setPeriod(period);
					
					VehiclePermit vehiclePermit = retrieveVehiclePermit(mileageLog);
					if (vehiclePermit != null && StringUtils.isNotEmpty(vehiclePermit.getPermitNumber())) {
						mileageLog.setVehiclePermit(vehiclePermit);
						mileageLog.setVehiclePermitNumber(vehiclePermit.getPermitNumber());
					} else {
						mileageLog.setVehiclePermit(null);
						mileageLog.setVehiclePermitNumber(StringUtils.EMPTY);
						
						if (mileageLog.getState() != null 
								&& StringUtils.equals("NY", mileageLog.getState().getCode())) {
							recordError = true;
							recordErrorMsg.append("Could not determine vehicle permit,");
						}
					}
					
					if (checkDuplicate(mileageLog)) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Duplicate record,");
					}
				} catch (Exception ex) {
					recordError = true;
					fatalRecordError = true;
					recordErrorMsg.append("Error while processing record,");
				}
				
				if (recordError) {
					String msgPreffix = fatalRecordError ? "Record NOT loaded->" : "Record LOADED, but has errors->";
					errorList.add(msgPreffix 
							+ "Line " + recordCount + ": " + recordErrorMsg.toString() + "<br/>");
					errorCount++;
				} 
				
				if (!fatalRecordError) {
					mileageLogList.add(mileageLog);
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records being loaded: " + mileageLogList.size());
			if (!mileageLogList.isEmpty()) {
				for (MileageLog aMileageLog : mileageLogList) {
					aMileageLog.setGps("Y");
					
					aMileageLog.setCreatedBy(createdBy);
					aMileageLog.setCreatedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(aMileageLog);
				}
				
				uploadNoGPSMileageLogData(period, createdBy);
			}
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Mileage log: " + ex);
		}
		
		return errorList;
	}
	
	private List<MileageLog> aggregateMileageLogByUnitState(List<MileageLog> srcMileageLogList) {
		List<MileageLog> aggreateMileageLogList = new ArrayList<MileageLog>();
		if (srcMileageLogList == null || srcMileageLogList.isEmpty()) {
			return aggreateMileageLogList;
		}
		
		Map<String, MileageLog> aggreateMileageLogMap = new HashMap<String, MileageLog>();
		for (MileageLog aSrcMileageLog : srcMileageLogList) {
			String key = aSrcMileageLog.getUnitNum() + "|" + aSrcMileageLog.getState().getName();
			
			MileageLog aggregateMileageLog = aggreateMileageLogMap.get(key);
			if (aggregateMileageLog == null) {
				aggreateMileageLogMap.put(key, aSrcMileageLog);
			} else {
				Double aggregateMiles = aggregateMileageLog.getMiles() + aSrcMileageLog.getMiles();
				aggregateMileageLog.setMiles(aggregateMiles);
				
				if (aSrcMileageLog.getFirstInState().before((aggregateMileageLog.getFirstInState()))) {
					aggregateMileageLog.setFirstInState(aSrcMileageLog.getFirstInState());
				}
				if (aSrcMileageLog.getLastInState().after((aggregateMileageLog.getLastInState()))) {
					aggregateMileageLog.setLastInState(aSrcMileageLog.getLastInState());
				}
			}
		}
		
		SortedSet<String> sortedKeys = new TreeSet<String>(aggreateMileageLogMap.keySet());
		for (String aKey : sortedKeys) {
			aggreateMileageLogList.add(aggreateMileageLogMap.get(aKey));
		}
		
		return aggreateMileageLogList;
	}
	
	private void uploadNoGPSMileageLogData(Date period, Long createdBy) {
		List<MileageLog> noGPSMileageLogList = retrieveNoGPSMileageLogData(period);
		if (noGPSMileageLogList == null || noGPSMileageLogList.isEmpty()) {
			return;
		}
		
		for (MileageLog aMileageLog : noGPSMileageLogList) {
			aMileageLog.setPeriod(period);
			
			if (checkDuplicate(aMileageLog)) {
				continue;
			}
			
			aMileageLog.setGps("N");
			
			aMileageLog.setCreatedBy(createdBy);
			aMileageLog.setCreatedAt(Calendar.getInstance().getTime());
			
			genericDAO.saveOrUpdate(aMileageLog);
		}
	}
	
	private List<MileageLog> retrieveNoGPSMileageLogData(Date period) {
		MileageLogReportInput input =  new MileageLogReportInput();
		String periodStr = mileageSearchDateFormat.format(period);
		input.setPeriodFrom(periodStr);
		input.setPeriodTo(periodStr);
		
		List<MileageLog> noGPSMileageLogList = reportService.generateNoGPSMileageLogData(null, input,
				null, null, null);
		
		List<MileageLog> aggregateMileageLogList = aggregateMileageLogByUnitState(noGPSMileageLogList);
		return aggregateMileageLogList;
	}
	
	private VehiclePermit retrieveVehiclePermit(MileageLog mileageLog) {
		if (mileageLog.getUnit() == null || mileageLog.getCompany() == null 
				|| mileageLog.getLastInState() == null) {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String lastInStateStr = dateFormat.format(mileageLog.getLastInState());
		
		String query = "select obj from VehiclePermit obj where obj.status=1"
				+ " and obj.issueDate <= '" + lastInStateStr + "'"
				+ " and obj.expirationDate > '" + lastInStateStr + "'"
				+ " and obj.vehicle.unitNum = " + mileageLog.getUnit().getUnitNum();
				//+ " and obj.companyLocation = " + mileageLog.getCompany().getId();
		List<VehiclePermit> permits = genericDAO.executeSimpleQuery(query);
		if (permits != null && !permits.isEmpty()) {
			return permits.get(0);
		} else {
			return null;
		}
	}
	
	private boolean checkDuplicate(MileageLog aMileageLog) {
		if (aMileageLog.getPeriod() == null || aMileageLog.getState() == null
				|| StringUtils.isEmpty(aMileageLog.getUnitNum())) {
			return false;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String period = dateFormat.format(aMileageLog.getPeriod());
		String query = "select obj from MileageLog obj where obj.period='" + period + "'"
				+ " and obj.state=" + aMileageLog.getState().getId()
				+ " and obj.unitNum='" + aMileageLog.getUnitNum() + "'";
		
		List<MileageLog> mileageLogList = genericDAO.executeSimpleQuery(query);
		return !mileageLogList.isEmpty();
	}
	
	private boolean checkDuplicateWMTicket(WMTicket wmTicket) {
		if (wmTicket == null || wmTicket.getTicket() == null
				|| (wmTicket.getOrigin() == null && wmTicket.getDestination() == null)) {
			return false;
		}
		
		String query = "select obj from WMTicket obj where obj.ticket=" + wmTicket.getTicket();
		if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, wmTicket.getTicketType())) {
			query += (" and obj.origin=" + wmTicket.getOrigin().getId());
			query += (" and obj.ticketType='" + WMTicket.ORIGIN_TICKET_TYPE + "'");
		} else {
			query += (" and obj.destination=" + wmTicket.getDestination().getId());
			query += (" and obj.ticketType='" + WMTicket.DESTINATION_TICKET_TYPE + "'");
		}
		
		List<WMTicket> wmTicketList = genericDAO.executeSimpleQuery(query);
		return !wmTicketList.isEmpty();
	}
	
	private WMInvoice checkDuplicateWMInvoice(WMInvoice wmInvoice) {
		if (wmInvoice == null || wmInvoice.getTicket() == null
				|| wmInvoice.getOrigin() == null || wmInvoice.getDestination() == null) {
			return null;
		}
		
		String baseQuery = "select obj from WMInvoice obj where obj.ticket=" + wmInvoice.getTicket();
		String originQuery = /*baseQuery + */ " and obj.origin=" + wmInvoice.getOrigin().getId();
		String destinationQuery = /*baseQuery + */ " and obj.destination=" + wmInvoice.getDestination().getId();
		
		/*List<WMInvoice> wmInvoiceList = genericDAO.executeSimpleQuery(originQuery);
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			wmInvoiceList = genericDAO.executeSimpleQuery(destinationQuery);
		}*/
		List<WMInvoice> wmInvoiceList = genericDAO.executeSimpleQuery(baseQuery + originQuery + destinationQuery);
		return ((wmInvoiceList == null || wmInvoiceList.isEmpty()) ? null : wmInvoiceList.get(0));
	}
	
	private boolean checkDuplicate(SubcontractorRate aSubcontractorRate) {
		if (aSubcontractorRate.getValidFrom() == null || aSubcontractorRate.getValidTo() == null
				 || aSubcontractorRate.getSubcontractor() == null || aSubcontractorRate.getCompanyLocation() == null
				 || aSubcontractorRate.getTransferStation() == null  || aSubcontractorRate.getLandfill() == null) {
			return false;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String validFrom = dateFormat.format(aSubcontractorRate.getValidFrom());
		String validTo = dateFormat.format(aSubcontractorRate.getValidTo());
		
		String query = "select obj from SubcontractorRate obj where "
				+ " obj.validFrom='" + validFrom + "'"
				+ " and obj.validTo='" + validTo + "'"
				+ " and obj.subcontractor=" + aSubcontractorRate.getSubcontractor().getId()
				+ " and obj.companyLocation=" + aSubcontractorRate.getCompanyLocation().getId()
				+ " and obj.transferStation=" + aSubcontractorRate.getTransferStation().getId()
				+ " and obj.landfill=" + aSubcontractorRate.getLandfill().getId();
		
		List<SubcontractorRate> subcontractorRateList = genericDAO.executeSimpleQuery(query);
		return !subcontractorRateList.isEmpty();
	}
	
	private boolean validateVin(String vin) {
		if (StringUtils.isEmpty(vin)) {
			return false;
		}
		
		return true;
	}
	
	private Double processMiles(String miles, Double resetMiles) {
		if (StringUtils.isEmpty(miles)) {
			return null;
		}
		
		Double maxMiles = 100000.0;
		Double milesDbl = null;
		try {
			miles = StringUtils.replace(miles, "*", StringUtils.EMPTY);
			miles = StringUtils.replace(miles, ",", StringUtils.EMPTY);
			milesDbl = new Double(miles);
			if (milesDbl > maxMiles) {
				milesDbl = resetMiles;
			}
		} catch (Exception ex) {
			log.warn("Exception while processing miles", ex);
		}
		
		return milesDbl;
	}
	
	private Date processLastInState(String lastInStateStr) {
		if (StringUtils.isEmpty(lastInStateStr)) {
			return null;
		}
		
		//1/30/16 11:35 AM EDT
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy h:mm a z");
		Date lastInState = null;
		try {
			lastInState = sdf.parse(lastInStateStr);
		} catch (Exception ex) {
			log.warn("Exception while processing lastInState: " + lastInStateStr, ex);
		}
		
		return lastInState;
	}
	
	private Date processFirstInState(String firstInStateStr) {
		if (StringUtils.isEmpty(firstInStateStr)) {
			return null;
		}
		
		//5/17/16 10:29 PM EDT
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy h:mm a z");
		Date firstInState = null;
		try {
			firstInState = sdf.parse(firstInStateStr);
		} catch (Exception ex) {
			log.warn("Exception while processing firstInState: " + firstInStateStr, ex);
		}
		
		return firstInState;
	}
	
	private Vehicle retrieveVehicle(String unit, Date lastInState) {
		if (StringUtils.isEmpty(unit) || lastInState == null) {
			return null;
		}
		
		List<Vehicle> vehicleList = null;
		String lastInStateStr = dateFormat.format(lastInState);
		try {
			vehicleList = retrieveVehicleForUnit(new Integer(unit), lastInStateStr);
		} catch (Exception ex) {
			log.warn("Exception while retrieving vehicle: " + unit, ex);
		}
		
		if (vehicleList == null || vehicleList.isEmpty()) {
			return null;
		} else {
			return vehicleList.get(0);
		}
	}
	
	private State retrieveState(String stateLongName) {
		if (StringUtils.isEmpty(stateLongName)) {
			return null;
		}
		
		State state = null;
		try {
			Map criterias = new HashMap();
			criterias.put("longName", stateLongName);
			state = genericDAO.getByCriteria(State.class, criterias);
		} catch (Exception ex) {
			log.warn("Exception while retrieving state: " + stateLongName, ex);
		}
		
		return state;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importeztollMainSheet(InputStream is, Boolean override) throws Exception {
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance //XSSFWorkbook
		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<EzToll> eztolls = new ArrayList<EzToll>();
		// List<String> emptydatalist=new ArrayList<String>();
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();
			// FileWriter writer = new FileWriter("e:/errordata.txt");
			wb = new HSSFWorkbook(fs);
			int numOfSheets = wb.getNumberOfSheets();
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			EzToll eztoll = null;

			Iterator rows = sheet.rowIterator();
			StringBuffer lineError;
			while (rows.hasNext()) {
				boolean error = false;
				buffer = new StringBuffer();
				int cellCount = 0;
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				lineError = new StringBuffer("");
				try {
					eztoll = new EzToll();
					// FOR Toll COMPANY
					String tollcompany = ((String) getCellValue(row.getCell(0)));
					try {
						criterias.clear();
						criterias.put("name", tollcompany);
						TollCompany tollcompanyName = genericDAO.getByCriteria(TollCompany.class, criterias);
						if (tollcompanyName == null)
							throw new Exception("Invalid Toll Company Name");
						eztoll.setToolcompany(tollcompanyName);
					} catch (Exception ex) {
						// System.out.println("\n\n Error in Driver first
						// name========>"+ex);
						error = true;
						lineError.append("Toll Company Name,");
						log.warn(ex.getMessage());
					}
					// FOR COMPANY
					String company = ((String) getCellValue(row.getCell(1)));
					try {
						criterias.clear();
						criterias.put("type", 3);
						criterias.put("name", company);
						Location companyName = genericDAO.getByCriteria(Location.class, criterias);
						// System.out.println("\ncompanyName====>"+companyName+"\n");

						if (companyName == null)
							throw new Exception("Invalid Company Name");
						eztoll.setCompany(companyName);
					} catch (Exception ex) {
						// System.out.println("\n\n Error in Driver first
						// name========>"+ex);
						error = true;
						lineError.append("Invalid Company Name,");
						log.warn(ex.getMessage());
					}
					// FOR TERMINAL
					/*
					 * try { criterias.clear(); String name = (String)
					 * getCellValue(row.getCell(2));
					 * //System.out.println("\nTerminal====>"+name+"\n"); if
					 * (StringUtils.isEmpty(name)) throw new
					 * Exception("Invalid terminal"); else {
					 * criterias.put("name", name); criterias.put("type", 4); }
					 * Location location =
					 * genericDAO.getByCriteria(Location.class, criterias); if
					 * (location == null) throw new
					 * Exception("no such Terminal"); else
					 * eztoll.setTerminal(location); } catch (Exception ex) {
					 * error = true; lineError.append("Terminal,");
					 * log.warn(ex.getMessage()); }
					 */
					
					if (override == false) {
						Date date2 = row.getCell(10).getDateCellValue();

						try {
							if (validDate(date2)) {
								eztoll.setInvoiceDate(dateFormat1.parse(dateFormat1.format(date2)));
							} else {
								error = true;
								lineError.append("Invoice Date,");
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Invoice Date,");
							log.warn(ex.getMessage());

						}
					} else {
						if (validDate(getCellValue(row.getCell(10))))
							eztoll.setInvoiceDate((Date) getCellValue(row.getCell(10)));
						else {
							eztoll.setInvoiceDate(null);
						}
					}

					validateAndResetTollTagAndPlateNumber(row, eztoll);
					
					String plateNum = null;

					if (getCellValue(row.getCell(4)) == null) {
						// do nothing
					} else if (getCellValue(row.getCell(4)).equals("")) {
						// do nothing
					} else {
						plateNum = getCellValue(row.getCell(4)).toString();
					}

					String tollNum = null;

					if (getCellValue(row.getCell(3)) == null) {
						// do nothing
					} else if (getCellValue(row.getCell(3)).equals("")) {
						// do nothing
					} else {
						tollNum = getCellValue(row.getCell(3)).toString();
					}

					// if both toll number and plate number is empty
					if (tollNum == null && plateNum == null) {
						error = true;
						lineError.append("Either tolltag number or plate number is required,");
						log.warn("Either Toll tag number or Plate number is required ");
					} else {
						// for toll number
						if (tollNum != null) {
							try {
								String transactiondate = null;
								if (validDate(getCellValue(row.getCell(6)))) {
									transactiondate = dateFormat.format(((Date) getCellValue(row.getCell(6))).getTime());
								}
								StringBuffer query = new StringBuffer(
										"select obj from VehicleTollTag obj where obj.tollTagNumber='"
												+ (String) getCellValue(row.getCell(3)) + "'");
								
								if (eztoll.getToolcompany() != null) {
									query.append(" and obj.tollCompany='" + eztoll.getToolcompany().getId() + "'");
								}

								query.append(" and obj.validFrom <='" + transactiondate + "' and obj.validTo >= '"
										+ transactiondate + "'");

								List<VehicleTollTag> vehicletolltags = genericDAO.executeSimpleQuery(query.toString());

								if (vehicletolltags.isEmpty() && vehicletolltags.size() == 0)
									throw new Exception("no such Toll tag Number");
								else {
									/***Correction for unit no. mapping to multiple vehicle ids***/
									/*String vehquery = "Select obj from Vehicle obj where obj.unit="
											+ vehicletolltags.get(0).getVehicle().getUnit() + " and obj.validFrom <='"
											+ transactiondate + "' and obj.validTo >= '" + transactiondate + "'";
									String vehquery = "Select obj from Vehicle obj where obj.id="
											+ vehicletolltags.get(0).getVehicle().getId() + " and obj.validFrom <='"
											+ transactiondate + "' and obj.validTo >= '" + transactiondate + "'";
									System.out.println("******************** the vehicle query is " + vehquery);
									List<Vehicle> vehicle = genericDAO.executeSimpleQuery(vehquery.toString());*/
									
									List<Vehicle> vehicle = retrieveVehicle(vehicletolltags.get(0), transactiondate);
									if (vehicle.isEmpty() && vehicle.size() == 0) {
										throw new Exception("TOLL_ERROR_MSG: Invalid Toll Tag Number - No matching vehicle found for given id and txn date");
									} else {
										eztoll.setUnit(vehicle.get(0));
										eztoll.setTollTagNumber(vehicletolltags.get(0));
										String drv_name = (String) getCellValue(row.getCell(5));
										if (!(StringUtils.isEmpty(drv_name))) {
											/*criterias.clear();
											criterias.put("fullName", getCellValue(row.getCell(5)));
											
											Driver driver = genericDAO.getByCriteria(Driver.class, criterias);*/
											
											Driver driver = getDriverObjectFromName(drv_name, row);
											if (driver == null) {
												error = true;
												lineError.append("Invalid Driver Name, ");
											} else {
												eztoll.setDriver(driver);
												eztoll.setTerminal(driver.getTerminal());
											}
										} else {
											/*String drivequery = "select obj from Ticket obj where obj.loadDate<='"
													+ transactiondate + "' and obj.unloadDate>='" + transactiondate
													+ "' and obj.vehicle=" + vehicle.get(0).getId();

											System.out.println(" my query is " + drivequery);
											List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);*/
											
											List<Vehicle> vehicleList = retrieveVehicleForUnit(vehicletolltags.get(0).getVehicle().getUnit(),
													transactiondate);
											List<Ticket> tickets = getTicketsForVehicle(vehicleList, transactiondate);
											
											// More than one driver fix - 13th May 2016
											String txnTime = getCellValue(row.getCell(7)).toString();
											Date txnDate = (Date) getCellValue(row.getCell(6));
											tickets = determineCorrectTicket(tickets, txnDate, txnTime);
											
											if (!tickets.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> driverid = new ArrayList<String>();
												for (Ticket ticket : tickets) {
													boolean d = driverid.contains(ticket.getDriver().getId() + "");
													driverid.add(ticket.getDriver().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														if (override == false) {
															error = true;
															lineError.append("More than one Driver, ");
															tic = false;
														} else {
															tic = false;

															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}

														}
													}
												}
												if (tic) {
													eztoll.setDriver(tickets.get(0).getDriver());
													Driver driver = genericDAO.getById(Driver.class,
															tickets.get(0).getDriver().getId());
													eztoll.setTerminal(driver.getTerminal());
												}
											} else {
												String driverFuelLogQuery = "select obj from DriverFuelLog obj where obj.transactionDate='"
														+ transactiondate + "' and obj.truck=" + vehicle.get(0).getId();

												System.out.println(" my query is " + driverFuelLogQuery);
												List<DriverFuelLog> driverFuelLog = genericDAO
														.executeSimpleQuery(driverFuelLogQuery);

												if (!driverFuelLog.isEmpty()) {
													boolean tic = true;
													boolean first = true;
													List<String> driverid = new ArrayList<String>();
													for (DriverFuelLog drvFuelLog : driverFuelLog) {

														boolean d = driverid
																.contains(drvFuelLog.getDriver().getId() + "");
														driverid.add(drvFuelLog.getDriver().getId() + "");
														if (first) {
															first = false;
															continue;
														}
														if (!d) {
															if (override == false) {
																error = true;
																lineError.append("More than one Driver, ");
																tic = false;
															} else {
																tic = false;

																try {
																	criterias.clear();
																	String name = (String) getCellValue(row.getCell(2));
																	// System.out.println("\nTerminal====>"+name+"\n");
																	if (StringUtils.isEmpty(name))
																		throw new Exception("Invalid terminal");
																	else {
																		criterias.put("name", name);
																		criterias.put("type", 4);
																	}
																	Location location = genericDAO
																			.getByCriteria(Location.class, criterias);
																	if (location == null)
																		throw new Exception("no such Terminal");
																	else
																		eztoll.setTerminal(location);
																} catch (Exception ex) {
																	error = true;
																	lineError.append("Terminal,");
																	log.warn(ex.getMessage());
																}

															}
														}

													}
													if (tic) {
														eztoll.setDriver(driverFuelLog.get(0).getDriver());
														Driver driver = genericDAO.getById(Driver.class,
																driverFuelLog.get(0).getDriver().getId());
														eztoll.setTerminal(driver.getTerminal());
													}
												} else {

													String driverOdometerQuery = "select obj from Odometer obj where obj.recordDate='"
															+ transactiondate + "' and obj.truck="
															+ vehicle.get(0).getId();

													System.out.println(" odometer query is " + driverOdometerQuery);
													List<Odometer> odometer = genericDAO
															.executeSimpleQuery(driverOdometerQuery);

													if (!odometer.isEmpty()) {

														boolean tic = true;
														boolean first = true;
														List<String> driverid = new ArrayList<String>();
														for (Odometer odometerObj : odometer) {

															boolean d = driverid
																	.contains(odometerObj.getDriver().getId() + "");
															driverid.add(odometerObj.getDriver().getId() + "");
															if (first) {
																first = false;
																continue;
															}
															if (!d) {
																if (override == false) {
																	error = true;
																	lineError.append("More than one Driver, ");
																	tic = false;
																} else {
																	tic = false;

																	try {
																		criterias.clear();
																		String name = (String) getCellValue(
																				row.getCell(2));
																		// System.out.println("\nTerminal====>"+name+"\n");
																		if (StringUtils.isEmpty(name))
																			throw new Exception("Invalid terminal");
																		else {
																			criterias.put("name", name);
																			criterias.put("type", 4);
																		}
																		Location location = genericDAO.getByCriteria(
																				Location.class, criterias);
																		if (location == null)
																			throw new Exception("no such Terminal");
																		else
																			eztoll.setTerminal(location);
																	} catch (Exception ex) {
																		error = true;
																		lineError.append("Terminal,");
																		log.warn(ex.getMessage());
																	}

																}
															}

														}
														if (tic) {
															eztoll.setDriver(odometer.get(0).getDriver());
															Driver driver = genericDAO.getById(Driver.class,
																	odometer.get(0).getDriver().getId());
															eztoll.setTerminal(driver.getTerminal());
														}

													} else {
														if (override == false) {
															error = true;
															lineError.append(
																	"No matching  Ticket, Fuel Log,  Odometer entry, ");
														} else {
															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}
														}
													}
												}
											}
										}

									}
								}

							} catch (Exception ex) {
								error = true;
								//lineError.append("Invalid Toll Tag Number, ");
								String errMsg = "Invalid Toll Tag Number";
								String exceptionMsg = StringUtils.substringAfter(ex.getMessage(), "TOLL_ERROR_MSG: ");
								if (!StringUtils.isEmpty(exceptionMsg)) {
									errMsg = exceptionMsg;
								}
								lineError.append(errMsg + ", ");
								log.warn(ex.getMessage());
							}
						}

						// FOR PLATE#
						if (plateNum != null) {
							try {
								/**Correction for plate no. verification - adding txn date***/
								/*criterias.clear();
								criterias.put("plate", (String) getCellValue(row.getCell(4)));
								Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);*/
								
								Vehicle vehicle = retrieveVehicleForPlate((String) getCellValue(row.getCell(4)), row);
								
								if (vehicle == null)
									throw new Exception("no such Plate or Toll tag Number");
								else {
									if (tollNum != null) {
										String transactiondate = null;

										if (validDate(getCellValue(row.getCell(6)))) {
											transactiondate = dateFormat
													.format(((Date) getCellValue(row.getCell(6))).getTime());
											System.out.println("\n****--****\n");
										}

										StringBuffer query = new StringBuffer(
												"select obj from VehicleTollTag obj where obj.tollTagNumber='"
														+ (String) getCellValue(row.getCell(3)) + "'");
										if (eztoll.getToolcompany() != null) {
											query.append(
													" and obj.tollCompany='" + eztoll.getToolcompany().getId() + "'");
										}
										query.append(" and obj.vehicle='" + vehicle.getId() + "' and obj.validFrom <='"
												+ transactiondate + "' and obj.validTo >= '" + transactiondate + "'");
										
										System.out.println("******* query  ======>" + query);
										try {
											List<VehicleTollTag> vehicletolltags = genericDAO.executeSimpleQuery(query.toString());
											if (vehicletolltags.isEmpty() && vehicletolltags.size() == 0)
												throw new Exception("Invalid Plate Number");
											else {
												/*
												 * Code to get the active plate
												 * numbers
												 */
												/*List<Vehicle> vehicleList = genericDAO
														.executeSimpleQuery(
																// Correction for unit no. mapping to multiple vehicle ids
																//"select o from Vehicle o where o.unit="
																//+ vehicletolltags.get(0).getUnit()
																//+ " and o.validFrom<=SYSDATE() and o.validTo>=SYSDATE() ");
																"select o from Vehicle o where o.id="
																+ vehicletolltags.get(0).getVehicle().getId()
																+ " and o.validFrom <='"+ transactiondate + "' and o.validTo >= '" 
																+ transactiondate + "'");*/
												List<Vehicle> vehicleList = retrieveVehicle(vehicletolltags.get(0), transactiondate);
												if (vehicleList.isEmpty() && vehicleList.size() == 0)
													throw new Exception("Invalid Plate Number");
												else
													eztoll.setPlateNumber(vehicleList.get(0));
											}
										} catch (Exception ex) {
											System.out.println("\n*******\n");
										}
									} else {

										String transactiondate1 = null;
										if (validDate(getCellValue(row.getCell(6)))) {
											transactiondate1 = dateFormat
													.format(((Date) getCellValue(row.getCell(6))).getTime());
											System.out.println("\n****--****\n");
										}

										StringBuffer query = new StringBuffer(
												"select obj from VehicleTollTag obj where ");
										query.append("obj.vehicle='" + vehicle.getId() + "' and obj.validFrom <='"
												+ transactiondate1 + "' and obj.validTo >= '" + transactiondate1 + "'");
										if (eztoll.getToolcompany() != null) {
											query.append(
													" and obj.tollCompany='" + eztoll.getToolcompany().getId() + "'");
										}

										System.out.println("******* query  ======>" + query);
										try {
											List<VehicleTollTag> vehicletolltags = genericDAO.executeSimpleQuery(query.toString());
											if (vehicletolltags.isEmpty() && vehicletolltags.size() == 0) {
												// throw new Exception("Invalid Plate Number");
											} else {
												eztoll.setTollTagNumber(vehicletolltags.get(0));
											}
										} catch (Exception ex) {
											System.out.println("\n*******\n");
										}

										String drv_name = (String) getCellValue(row.getCell(5));
										if (!(StringUtils.isEmpty(drv_name))) {
											/*criterias.clear();
											criterias.put("fullName", getCellValue(row.getCell(5)));
											
											Driver driver = genericDAO.getByCriteria(Driver.class, criterias);*/
											
											Driver driver = getDriverObjectFromName(drv_name, row);
											if (driver == null) {
												error = true;
												lineError.append("Invalid Driver Name, ");
											} else {
												eztoll.setDriver(driver);
												eztoll.setTerminal(driver.getTerminal());
											}
										} else {

											String transactiondate = null;
											if (validDate(getCellValue(row.getCell(6)))) {
												transactiondate = dateFormat
														.format(((Date) getCellValue(row.getCell(6))).getTime());
											}

											/*String drivequery = "select obj from Ticket obj where obj.loadDate<='"
													+ transactiondate + "' and obj.unloadDate>='" + transactiondate
													+ "' and obj.vehicle=" + vehicle.getId();
											System.out.println(" my query is " + drivequery);
											List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);*/
											
											List<Vehicle> vehicleListForDriver = new ArrayList<Vehicle>();
											vehicleListForDriver.add(vehicle);
											List<Ticket> tickets = getTicketsForVehicle(vehicleListForDriver, transactiondate);
											
											// More than one driver fix - 13th May 2016
											String txnTime = getCellValue(row.getCell(7)).toString();
											Date txnDate = (Date) getCellValue(row.getCell(6));
											tickets = determineCorrectTicket(tickets, txnDate, txnTime);
											
											if (!tickets.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> driverid = new ArrayList<String>();
												for (Ticket ticket : tickets) {
													boolean d = driverid.contains(ticket.getDriver().getId() + "");
													driverid.add(ticket.getDriver().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														if (override == false) {
															error = true;
															lineError.append("More than one Driver, ");
															tic = false;
														} else {
															tic = false;

															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}

														}
													}
												}
												if (tic) {
													eztoll.setDriver(tickets.get(0).getDriver());
													Driver driver = genericDAO.getById(Driver.class,
															tickets.get(0).getDriver().getId());
													eztoll.setTerminal(driver.getTerminal());
												}
											} else {
												String driverFuelLogQuery = "select obj from DriverFuelLog obj where obj.transactionDate='"
														+ transactiondate + "' and obj.truck=" + vehicle.getId();

												System.out.println(" my query is " + driverFuelLogQuery);
												List<DriverFuelLog> driverFuelLog = genericDAO
														.executeSimpleQuery(driverFuelLogQuery);

												if (!driverFuelLog.isEmpty()) {
													boolean tic = true;
													boolean first = true;
													List<String> driverid = new ArrayList<String>();
													for (DriverFuelLog drvFuelLog : driverFuelLog) {

														boolean d = driverid
																.contains(drvFuelLog.getDriver().getId() + "");
														driverid.add(drvFuelLog.getDriver().getId() + "");
														if (first) {
															first = false;
															continue;
														}
														if (!d) {
															if (override == false) {
																error = true;
																lineError.append("More than one Driver, ");
																tic = false;
															} else {
																tic = false;

																try {
																	criterias.clear();
																	String name = (String) getCellValue(row.getCell(2));
																	// System.out.println("\nTerminal====>"+name+"\n");
																	if (StringUtils.isEmpty(name))
																		throw new Exception("Invalid terminal");
																	else {
																		criterias.put("name", name);
																		criterias.put("type", 4);
																	}
																	Location location = genericDAO
																			.getByCriteria(Location.class, criterias);
																	if (location == null)
																		throw new Exception("no such Terminal");
																	else
																		eztoll.setTerminal(location);
																} catch (Exception ex) {
																	error = true;
																	lineError.append("Terminal,");
																	log.warn(ex.getMessage());
																}

															}
														}

													}
													if (tic) {
														eztoll.setDriver(driverFuelLog.get(0).getDriver());
														Driver driver = genericDAO.getById(Driver.class,
																driverFuelLog.get(0).getDriver().getId());
														eztoll.setTerminal(driver.getTerminal());
													}
												} else {

													String driverOdometerQuery = "select obj from Odometer obj where obj.recordDate='"
															+ transactiondate + "' and obj.truck=" + vehicle.getId();

													System.out.println(" odometer query is " + driverOdometerQuery);
													List<Odometer> odometer = genericDAO
															.executeSimpleQuery(driverOdometerQuery);

													if (!odometer.isEmpty()) {

														boolean tic = true;
														boolean first = true;
														List<String> driverid = new ArrayList<String>();
														for (Odometer odometerObj : odometer) {

															boolean d = driverid
																	.contains(odometerObj.getDriver().getId() + "");
															driverid.add(odometerObj.getDriver().getId() + "");
															if (first) {
																first = false;
																continue;
															}
															if (!d) {
																if (override == false) {
																	error = true;
																	lineError.append("More than one Driver, ");
																	tic = false;
																} else {
																	tic = false;

																	try {
																		criterias.clear();
																		String name = (String) getCellValue(
																				row.getCell(2));
																		// System.out.println("\nTerminal====>"+name+"\n");
																		if (StringUtils.isEmpty(name))
																			throw new Exception("Invalid terminal");
																		else {
																			criterias.put("name", name);
																			criterias.put("type", 4);
																		}
																		Location location = genericDAO.getByCriteria(
																				Location.class, criterias);
																		if (location == null)
																			throw new Exception("no such Terminal");
																		else
																			eztoll.setTerminal(location);
																	} catch (Exception ex) {
																		error = true;
																		lineError.append("Terminal,");
																		log.warn(ex.getMessage());
																	}

																}
															}

														}
														if (tic) {
															eztoll.setDriver(odometer.get(0).getDriver());
															Driver driver = genericDAO.getById(Driver.class,
																	odometer.get(0).getDriver().getId());
															eztoll.setTerminal(driver.getTerminal());
														}

													} else {
														if (override == false) {
															error = true;
															lineError.append(
																	"No matching  Ticket, Fuel Log,  Odometer entry, ");
														} else {
															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}
														}
													}
												}
											}
										}

										eztoll.setPlateNumber(vehicle);
										eztoll.setUnit(vehicle);
									}

								}
							} catch (Exception ex) {
								error = true;
								lineError.append("Invalid Plate or Toll tag Number, ");
								log.warn(ex.getMessage());
							}

						} else {
							if (eztoll.getTollTagNumber() != null) {
								String transactiondate = null;
								if (validDate(getCellValue(row.getCell(6)))) {
									transactiondate = dateFormat.format(((Date) getCellValue(row.getCell(6))).getTime());
								}
								
								VehicleTollTag vehicletoll = genericDAO.getById(VehicleTollTag.class, eztoll.getTollTagNumber().getId());

								/* Code to get the active plate numbers */
								/*List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(
										// Correction for unit no. mapping to multiple vehicle ids
										//"select o from Vehicle o where o.unit=" + vehicletoll.getUnit()
										//+ " and o.validFrom<=SYSDATE() and o.validTo>=SYSDATE() ");
										"select o from Vehicle o where o.id=" + vehicletoll.getVehicle().getId()
										+ " and o.validFrom <='"+ transactiondate + "' and o.validTo >= '" 
										+ transactiondate + "'");*/
								List<Vehicle> vehicleList = retrieveVehicle(vehicletoll, transactiondate);
								if (vehicleList.isEmpty() && vehicleList.size() == 0)
									//throw new Exception("Invalid Plate Number");
									throw new Exception("Invalid Toll Tag Number - no matching vehicle found for given id and txn date");
								else
									eztoll.setPlateNumber(vehicleList.get(0));

								// eztoll.setPlateNumber(vehicletoll.getVehicle());
							}
						}
					}

					// FOR TRANSACTION DATE
					try {
						if (validDate(getCellValue(row.getCell(6))))
							eztoll.setTransactiondate((Date) getCellValue(row.getCell(6)));
						else {
							error = true;
							lineError.append("Transaction Date,");
						}
					} catch (Exception ex) {
						System.out.println("\nERROR IN TRANSACTION DATE\n");
						log.warn(ex.getMessage());

					}
					// FOR TRANSACTION TIME
					try {
						if (validDate(getCellValue(row.getCell(7)))) {
							eztoll.setTransactiontime(dateFormat2.format((Date) getCellValue(row.getCell(7))));
						} else {
							String trxTime1 = (String) getCellValue(row.getCell(7));

							if (!(StringUtils.isEmpty(trxTime1))) {
								if (trxTime1.length() == 5 || trxTime1.length() == 8 || trxTime1.length() == 7) {
									StringBuilder time = new StringBuilder(
											StringUtils.leftPad((String) getCellValue(row.getCell(7)), 4, '0'));
									// time.insert(2, ':');
									if (trxTime1.length() == 8) {
										eztoll.setTransactiontime(time.toString().substring(0, 5));
									} else if (trxTime1.length() == 7) {
										eztoll.setTransactiontime(time.toString().substring(0, 4));
									} else {
										eztoll.setTransactiontime(time.toString());
									}

								} else {
									// System.out.println("\ntrx time is not
									// valid\n");
									error = true;
									lineError.append("Transaction Time,");
								}
							} else {
								lineError.append("Transaction Time,");
							}

						}
					} catch (Exception e) {

					}
					/*
					 * if (validTime((String) getCellValue(row.getCell(6)))) {
					 * StringBuilder time = new
					 * StringBuilder(StringUtils.leftPad((String)getCellValue(
					 * row.getCell(6)),4,'0')); int
					 * hh=Integer.parseInt(time.substring(0,2)); int
					 * mm=Integer.parseInt(time.substring(2));
					 * 
					 * if(hh==24) { if(mm==0) { time.insert(2, ':');
					 * eztoll.setTransactiontime(time.toString()); //
					 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n");
					 * } else { error = true;
					 * lineError.append("transaction time,"); } } else {
					 * if(hh<24) { if(mm<=59) { time.insert(2, ':');
					 * eztoll.setTransactiontime(time.toString()); //
					 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n");
					 * } else { error = true;
					 * lineError.append("transaction time minut is > 59,"); } }
					 * else { error = true;
					 * lineError.append("transaction time hours is > 24,"); } }
					 * } else { error = true;
					 * lineError.append("transaction time more than 5 degits,");
					 * 
					 * }
					 */

					// FOR AGENCY
					try {
						eztoll.setAgency((String) getCellValue(row.getCell(8)));
					} catch (Exception ex) {
						error = true;
						lineError.append("Agency,");
						log.warn(ex.getMessage());
					}
					// FOR AMOUNTS
					String amount1 = row.getCell(9).toString();
					Double amount2 = getValidGallon(amount1);
					if (amount2 != null) {
						//eztoll.setAmount(Math.abs(amount2));
						eztoll.setAmount(amount2);
					} else {
						lineError.append("Amount,");
						error = true;
					}
					
					/**Added dup check**/
					// END OF CELL
					if (override == false) {
						System.out.println("***** eneter here ok 0");
						if (!error) {
							System.out.println("***** eneter here ok 1");
							Map prop = new HashMap();
							prop.put("toolcompany", eztoll.getToolcompany().getId());
							prop.put("company", eztoll.getCompany().getId());
							prop.put("driver", eztoll.getDriver().getId());
							prop.put("terminal", eztoll.getTerminal().getId());
							prop.put("unit", eztoll.getUnit().getId());
							prop.put("agency", eztoll.getAgency());
							prop.put("invoiceDate", dateFormat1.format(eztoll.getInvoiceDate()));
							prop.put("transactiondate", dateFormat1.format(eztoll.getTransactiondate()));
							prop.put("transactiontime", eztoll.getTransactiontime());
							if (eztoll.getTollTagNumber() != null) {
								prop.put("tollTagNumber", eztoll.getTollTagNumber().getId());
							} 
							if (eztoll.getPlateNumber() != null) {
								prop.put("plateNumber", eztoll.getPlateNumber().getId());
							} 
							prop.put("amount", eztoll.getAmount());
							boolean rst = genericDAO.isUnique(EzToll.class, eztoll, prop);
							System.out.println("***** eneter here ok 2" + rst);
							if (!rst) {
								System.out.println("***** eneter here ok 3");
								lineError.append("Toll tag entry already exists(Duplicate),");
								error = true;
								errorcount++;
							}
							
							if (eztolls.contains(eztoll)) {
								 lineError.append("Duplicate eztoll in excel,"); 
								 error = true;
							 }
							 // Toll upload improvement - 23rd Jul 2016
							 /*else { 
								 eztolls.add(eztoll);
							 }*/
						} else {
							errorcount++;
						}
					} else {
						if (!error) {
							eztolls.add(eztoll);
						} else {
							errorcount++;
						}
					}
					/**End of adding dup check**/
					
					/*// END OF CELL
					if (!error) {
						 if (eztolls.contains(eztoll)) {
							 lineError.append("Duplicate eztoll,"); 
							 error = true;
						 } else { 
							 eztolls.add(eztoll);
						 }
						 
						//eztolls.add(eztoll);
					} else {
						errorcount++;
					}*/

				} // TRY INSIDE SHILE(LOOP)
				catch (Exception ex) {
					error = true;
					lineError.append("Exception while processing toll upload records,"); 
					log.warn(ex);
				}
				if (lineError.length() > 0) {
					System.out.println("Error :" + lineError.toString());
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				// Toll upload improvement - 23rd Jul 2016
				else { 
					eztolls.add(eztoll);
				}
				
				System.out.println("Record No :" + count);
				count++;
			} // CLOSE while (rows.hasNext())
		} // FIRST TRY
		catch (Exception e) {
			log.warn("Error in import eztoll :" + e);
			// Toll upload improvement - 23rd Jul 2016
			throw e;
		}
		
		// Toll upload improvement - 23rd Jul 2016
		//if (errorcount == 0) {
			for (EzToll etoll : eztolls) {
				Map criti = new HashMap();
				criti.clear();
				criti.put("id", etoll.getDriver().getId());
				Driver drvOBj = genericDAO.getByCriteria(Driver.class, criti);
				if (drvOBj != null)
					etoll.setDriverFullName(drvOBj.getFullName());

				criti.clear();
				criti.put("id", etoll.getUnit().getId());
				Vehicle vehObj = genericDAO.getByCriteria(Vehicle.class, criti);
				if (vehObj != null)
					etoll.setUnitNum(vehObj.getUnitNum());
				genericDAO.saveOrUpdate(etoll);
			}
		// Toll upload improvement - 23rd Jul 2016
		//}
		return list;
	}

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 * public List<String> importeztollMainSheet(InputStream is,Boolean
	 * override) throws Exception { // initializing the InputStream from a file
	 * using // POIFSFileSystem, before converting the result // into an
	 * HSSFWorkbook instance //XSSFWorkbook HSSFWorkbook wb = null; StringBuffer
	 * buffer = null; List<String> list = new ArrayList<String>(); List<EzToll>
	 * eztolls = new ArrayList<EzToll>(); // List<String> emptydatalist=new
	 * ArrayList<String>(); int count = 1; int errorcount = 0; try {
	 * POIFSFileSystem fs = new POIFSFileSystem(is); ErrorData edata = new
	 * ErrorData(); // FileWriter writer = new FileWriter("e:/errordata.txt");
	 * wb = new HSSFWorkbook(fs); int numOfSheets = wb.getNumberOfSheets(); Map
	 * criterias = new HashMap(); HSSFSheet sheet = wb.getSheetAt(0); HSSFRow
	 * row = null; HSSFCell cell = null; EzToll eztoll = null;
	 * 
	 * Iterator rows = sheet.rowIterator(); StringBuffer lineError; while
	 * (rows.hasNext()) { boolean error = false; buffer = new StringBuffer();
	 * int cellCount = 0; row = (HSSFRow) rows.next(); if (count == 1) {
	 * count++; continue; } lineError = new StringBuffer(""); try { eztoll = new
	 * EzToll(); //FOR Toll COMPANY String tollcompany = ((String)
	 * getCellValue(row.getCell(0))); try { criterias.clear();
	 * criterias.put("name", tollcompany); TollCompany tollcompanyName =
	 * genericDAO.getByCriteria(TollCompany.class,criterias); if
	 * (tollcompanyName == null) throw new
	 * Exception("Invalid Toll Company Name");
	 * eztoll.setToolcompany(tollcompanyName); } catch(Exception ex) {
	 * //System.out.println("\n\n Error in Driver first name========>"+ex);
	 * error = true; lineError.append("Toll Company Name,");
	 * log.warn(ex.getMessage()); } //FOR COMPANY String company = ((String)
	 * getCellValue(row.getCell(1))); try { criterias.clear();
	 * criterias.put("type", 3); criterias.put("name", company); Location
	 * companyName = genericDAO.getByCriteria(Location.class,criterias);
	 * //System.out.println("\ncompanyName====>"+companyName+"\n");
	 * 
	 * if (companyName == null) throw new Exception("Invalid Company Name");
	 * eztoll.setCompany(companyName); } catch(Exception ex) {
	 * //System.out.println("\n\n Error in Driver first name========>"+ex);
	 * error = true; lineError.append("Company,"); log.warn(ex.getMessage()); }
	 * //FOR TERMINAL try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); }
	 * 
	 * 
	 * 
	 * String plateNum=null;
	 * 
	 * if(getCellValue(row.getCell(4))==null){ //do nothing } else
	 * if(getCellValue(row.getCell(4)).equals("")){ //do nothing } else{
	 * plateNum=getCellValue(row.getCell(4)).toString(); }
	 * 
	 * 
	 * String tollNum=null;
	 * 
	 * if(getCellValue(row.getCell(3))==null){ //do nothing } else
	 * if(getCellValue(row.getCell(3)).equals("")){ //do nothing } else{
	 * tollNum=getCellValue(row.getCell(3)).toString(); }
	 * 
	 * 
	 * //if both toll number and plate number is empty if(tollNum==null &&
	 * plateNum==null) { error = true;
	 * lineError.append("Either tolltag number or plate number is required,");
	 * log.warn("Either Toll tag number or Plate number is required "); } else {
	 * //for toll number if(tollNum!=null ) { try { String transactiondate =
	 * null; if (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime()); } StringBuffer query=new
	 * StringBuffer(
	 * "select obj from VehicleTollTag obj where obj.tollTagNumber='"+(String)
	 * getCellValue(row.getCell(3))+"'") ; //String
	 * query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
	 * if(eztoll.getToolcompany()!=null){
	 * query.append(" and obj.tollCompany='"+eztoll.getToolcompany().getId()+"'"
	 * ); }
	 * 
	 * 
	 * query.append(" and obj.validFrom <='"+ transactiondate+
	 * "' and obj.validTo >= '"+transactiondate+"'");
	 * System.out.println("************ query =======>"+query.toString());
	 * 
	 * List<VehicleTollTag> vehicletolltags =
	 * genericDAO.executeSimpleQuery(query.toString());
	 * 
	 * System.out.println(" Size of vehicle is "+vehicletolltags.size()); if
	 * (vehicletolltags.isEmpty()&& vehicletolltags.size()==0) throw new
	 * Exception("no such Toll tag Number"); else { String
	 * vehquery="Select obj from Vehicle obj where obj.unit="+vehicletolltags.
	 * get(0).getVehicle().getUnit()+" and obj.validFrom <='"+ transactiondate+
	 * "' and obj.validTo >= '"+transactiondate+"'";
	 * System.out.println("******************** the vehicle query is "+vehquery)
	 * ; List<Vehicle> vehicle =
	 * genericDAO.executeSimpleQuery(vehquery.toString()); if(vehicle.isEmpty()
	 * && vehicle.size()==0){ throw new Exception("no such Toll tag Number"); }
	 * else{ eztoll.setUnit(vehicle.get(0));
	 * eztoll.setTollTagNumber(vehicletolltags.get(0)); String drv_name=(String)
	 * getCellValue(row.getCell(5)); if(!(StringUtils.isEmpty(drv_name))){
	 * criterias.clear();
	 * criterias.put("fullName",getCellValue(row.getCell(5))); List<Driver>
	 * driver =genericDAO.findByCriteria(Driver.class,criterias); Driver
	 * driver=genericDAO.getByCriteria(Driver.class, criterias);
	 * if(driver==null) { error = true;
	 * lineError.append("Invalid Driver Name, "); } else {
	 * eztoll.setDriver(driver); eztoll.setTerminal(driver.getTerminal()); } }
	 * else{ String
	 * drivequery="select obj from Ticket obj where   obj.loadDate<='"
	 * +transactiondate+"' and obj.unloadDate>='"+transactiondate+
	 * "' and obj.vehicle="+vehicle.get(0).getId();
	 * 
	 * System.out.println(" my query is "+drivequery); List<Ticket>
	 * tickets=genericDAO.executeSimpleQuery(drivequery);
	 * if(!tickets.isEmpty()){ boolean tic=true; boolean first=true;
	 * List<String> driverid=new ArrayList<String>(); for(Ticket
	 * ticket:tickets){ boolean
	 * d=driverid.contains(ticket.getDriver().getId()+"");
	 * driverid.add(ticket.getDriver().getId()+""); if(first){ first=false;
	 * continue; } if(!d){ if(override==false){ error = true;
	 * lineError.append("More than one Driver, "); tic=false; }else{ tic=false;
	 * 
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); }
	 * 
	 * 
	 * } } } if(tic){ eztoll.setDriver(tickets.get(0).getDriver()); Driver
	 * driver=genericDAO.getById(Driver.class,tickets.get(0).getDriver().getId()
	 * ); eztoll.setTerminal(driver.getTerminal()); } }else{
	 * if(override==false){ error = true;
	 * lineError.append("Ticket Data Does not match with Toll Tag, "); } else{
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); } } } }
	 * 
	 * } }
	 * 
	 * } catch (Exception ex) { error = true;
	 * lineError.append("Invalid Toll Tag Number, "); log.warn(ex.getMessage());
	 * } }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * //FOR PLATE# if(plateNum!=null) { try { criterias.clear();
	 * criterias.put("plate", (String) getCellValue(row.getCell(4))); Vehicle
	 * vehicle = genericDAO.getByCriteria(Vehicle.class, criterias); if (vehicle
	 * == null) throw new Exception("no such Plate Number"); else {
	 * 
	 * if(tollNum!=null) { String transactiondate = null;
	 * 
	 * if (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime());
	 * System.out.println("\n****--****\n"); }
	 * 
	 * 
	 * StringBuffer query=new StringBuffer(
	 * "select obj from VehicleTollTag obj where obj.tollTagNumber='"+(String)
	 * getCellValue(row.getCell(3))+"'"); if(eztoll.getToolcompany()!=null){
	 * query.append(" and obj.tollCompany='"+eztoll.getToolcompany().getId()+"'"
	 * ); } query.append(" and obj.vehicle='"+vehicle.getId()+
	 * "' and obj.validFrom <='"+ transactiondate+
	 * "' and obj.validTo >= '"+transactiondate+"'");
	 * 
	 * String
	 * query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
	 * +(String)getCellValue(row.getCell(3))+"' and obj.vehicle='"
	 * +vehicle.getId()+"' and obj.validFrom <='" + transactiondate+
	 * "' and obj.validTo >= '" +transactiondate+"'";
	 * System.out.println("******* query  ======>"+query); try{
	 * List<VehicleTollTag> vehicletolltags =
	 * genericDAO.executeSimpleQuery(query.toString()); if
	 * (vehicletolltags.isEmpty()&& vehicletolltags.size()==0) throw new
	 * Exception("Invalid Plate Number"); else {
	 * eztoll.setPlateNumber(vehicletolltags.get(0).getVehicle()); } }
	 * catch(Exception ex){ System.out.println("\n*******\n"); } } else {
	 * 
	 * String transactiondate1 = null; if
	 * (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate1=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime());
	 * System.out.println("\n****--****\n"); }
	 * 
	 * StringBuffer query=new
	 * StringBuffer("select obj from VehicleTollTag obj where ");
	 * query.append("obj.vehicle='"+vehicle.getId()+"' and obj.validFrom <='"+
	 * transactiondate1+ "' and obj.validTo >= '"+transactiondate1+"'");
	 * if(eztoll.getToolcompany()!=null){
	 * query.append(" and obj.tollCompany='"+eztoll.getToolcompany().getId()+"'"
	 * ); }
	 * 
	 * 
	 * System.out.println("******* query  ======>"+query); try{
	 * List<VehicleTollTag> vehicletolltags =
	 * genericDAO.executeSimpleQuery(query.toString()); if
	 * (vehicletolltags.isEmpty()&& vehicletolltags.size()==0){ //throw new
	 * Exception("Invalid Plate Number"); }else {
	 * eztoll.setTollTagNumber(vehicletolltags.get(0)); } } catch(Exception ex){
	 * System.out.println("\n*******\n"); }
	 * 
	 * 
	 * 
	 * String drv_name=(String) getCellValue(row.getCell(5));
	 * if(!(StringUtils.isEmpty(drv_name))){ criterias.clear();
	 * criterias.put("fullName",getCellValue(row.getCell(5))); List<Driver>
	 * driver =genericDAO.findByCriteria(Driver.class,criterias); Driver
	 * driver=genericDAO.getByCriteria(Driver.class, criterias);
	 * if(driver==null) { error = true;
	 * lineError.append("Invalid Driver Name, "); } else {
	 * eztoll.setDriver(driver); eztoll.setTerminal(driver.getTerminal()); } }
	 * else{
	 * 
	 * String transactiondate = null; if
	 * (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime()); }
	 * 
	 * String drivequery="select obj from Ticket obj where   obj.loadDate<='"
	 * +transactiondate+"' and obj.unloadDate>='"+transactiondate+
	 * "' and obj.vehicle="+vehicle.getId();
	 * 
	 * System.out.println(" my query is "+drivequery); List<Ticket>
	 * tickets=genericDAO.executeSimpleQuery(drivequery);
	 * if(!tickets.isEmpty()){ boolean tic=true; boolean first=true;
	 * List<String> driverid=new ArrayList<String>(); for(Ticket
	 * ticket:tickets){ boolean
	 * d=driverid.contains(ticket.getDriver().getId()+"");
	 * driverid.add(ticket.getDriver().getId()+""); if(first){ first=false;
	 * continue; } if(!d){ if(override==false){ error = true;
	 * lineError.append("More than one Driver, "); tic=false; }else{ tic=false;
	 * 
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); }
	 * 
	 * 
	 * } } } if(tic){ eztoll.setDriver(tickets.get(0).getDriver()); Driver
	 * driver=genericDAO.getById(Driver.class,tickets.get(0).getDriver().getId()
	 * ); eztoll.setTerminal(driver.getTerminal()); } }else{
	 * if(override==false){ error = true;
	 * lineError.append("Ticket Data Does not match with Toll Tag, "); } else{
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); } } } }
	 * 
	 * 
	 * eztoll.setPlateNumber(vehicle); }
	 * 
	 * } } catch (Exception ex) { error = true;
	 * lineError.append("Invalid Plate Number, "); log.warn(ex.getMessage()); }
	 * 
	 * } else { if(eztoll.getTollTagNumber()!=null) { VehicleTollTag
	 * vehicletoll=genericDAO.getById(VehicleTollTag.class,eztoll.
	 * getTollTagNumber().getId());
	 * eztoll.setPlateNumber(vehicletoll.getVehicle()); } }
	 * 
	 * }
	 * 
	 * //FOR TRANSACTION DATE try { if (validDate(getCellValue(row.getCell(6))))
	 * eztoll.setTransactiondate((Date) getCellValue(row.getCell(6))); else {
	 * error = true; lineError.append("Transaction Date,"); } } catch(Exception
	 * ex) { System.out.println("\nERROR IN TRANSACTION DATE\n");
	 * log.warn(ex.getMessage());
	 * 
	 * } //FOR TRANSACTION TIME try{ if
	 * (validDate(getCellValue(row.getCell(7)))){
	 * eztoll.setTransactiontime(dateFormat2.format((Date)
	 * getCellValue(row.getCell(7)))); } else{ String trxTime1=(String)
	 * getCellValue(row.getCell(7));
	 * 
	 * if(!(StringUtils.isEmpty(trxTime1))){ if (trxTime1.length() == 5 ||
	 * trxTime1.length() == 8||trxTime1.length() == 7){ StringBuilder time = new
	 * StringBuilder(StringUtils.leftPad((String)getCellValue(row.getCell(7)),4,
	 * '0')); //time.insert(2, ':'); if(trxTime1.length() == 8){
	 * eztoll.setTransactiontime(time.toString().substring(0, 5)); } else
	 * if(trxTime1.length() == 7){
	 * eztoll.setTransactiontime(time.toString().substring(0, 4)); } else{
	 * eztoll.setTransactiontime(time.toString()); }
	 * 
	 * } else { //System.out.println("\ntrx time is not valid\n"); error = true;
	 * lineError.append("Transaction Time,"); } } else{
	 * lineError.append("Transaction Time,"); }
	 * 
	 * } } catch(Exception e){
	 * 
	 * } if (validTime((String) getCellValue(row.getCell(6)))) { StringBuilder
	 * time = new
	 * StringBuilder(StringUtils.leftPad((String)getCellValue(row.getCell(6)),4,
	 * '0')); int hh=Integer.parseInt(time.substring(0,2)); int
	 * mm=Integer.parseInt(time.substring(2));
	 * 
	 * if(hh==24) { if(mm==0) { time.insert(2, ':');
	 * eztoll.setTransactiontime(time.toString()); //
	 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n"); } else { error
	 * = true; lineError.append("transaction time,"); } } else { if(hh<24) {
	 * if(mm<=59) { time.insert(2, ':');
	 * eztoll.setTransactiontime(time.toString()); //
	 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n"); } else { error
	 * = true; lineError.append("transaction time minut is > 59,"); } } else {
	 * error = true; lineError.append("transaction time hours is > 24,"); } } }
	 * else { error = true;
	 * lineError.append("transaction time more than 5 degits,");
	 * 
	 * }
	 * 
	 * //FOR AGENCY try { eztoll.setAgency((String)
	 * getCellValue(row.getCell(8))); } catch (Exception ex) { error = true;
	 * lineError.append("Agency,"); log.warn(ex.getMessage()); } //FOR AMOUNTS
	 * String amount1=row.getCell(9).toString(); Double amount2 =
	 * getValidGallon(amount1); if (amount2 != null) {
	 * eztoll.setAmount(Math.abs(amount2)); } else {
	 * lineError.append("Amount,"); error = true; } //END OF CELL
	 * 
	 * if (!error) { java.sql.Date transacDt = new
	 * java.sql.Date(eztoll.getTransactiondate().getTime()); try { StringBuffer
	 * query = new StringBuffer(); query.append("select obj from EzToll obj ");
	 * query.append(" where obj.toolcompany.name='"+tollcompany+"'");
	 * query.append(" and obj.company.name='"+company+"' "); if(tollNum != null)
	 * query.append(" and obj.tollTagNumber.tollTagNumber="+tollNum+" ");
	 * if(plateNum != null)
	 * query.append(" and obj.plateNumber.plate="+plateNum+" ");
	 * query.append(" and obj.transactiondate='"+transacDt+"' ");
	 * query.append(" and obj.amount="+amount1);
	 * query.append(" and obj.driver.fullName='"+getCellValue(row.getCell(5))+
	 * "'"); query.append(" and obj.agency='"+getCellValue(row.getCell(8))+"'");
	 * 
	 * List<EzToll> duplicateCheck =
	 * genericDAO.executeSimpleQuery(query.toString());
	 * 
	 * if(duplicateCheck!= null && duplicateCheck.size()>0){
	 * 
	 * error=true; errorcount++; lineError.append("Toll Tag already exists, ");
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * if (eztolls.contains(eztoll)) { lineError.append("Duplicate eztoll,");
	 * error = true; errorcount++; }else{ eztolls.add(eztoll); } } else {
	 * errorcount++; }
	 * 
	 * if (!error) { java.sql.Date transacDt = new
	 * java.sql.Date(eztoll.getTransactiondate().getTime());
	 * 
	 * try {
	 * 
	 * StringBuffer query = new StringBuffer();
	 * query.append("select obj from EzToll obj ");
	 * query.append(" where obj.toolcompany.name='"+tollcompany+"'");
	 * query.append(" and obj.company.name='"+company+"' "); if(tollNum != null)
	 * query.append(" and obj.tollTagNumber.tollTagNumber="+tollNum+" ");
	 * if(plateNum != null)
	 * query.append(" and obj.plateNumber.plate="+plateNum+" ");
	 * query.append(" and obj.transactiondate='"+transacDt+"' ");
	 * query.append(" and obj.amount="+amount1);
	 * query.append(" and obj.driver.fullName='"+getCellValue(row.getCell(5))+
	 * "'"); query.append(" and obj.agency='"+getCellValue(row.getCell(8))+"'");
	 * 
	 * List<EzToll> duplicateCheck =
	 * genericDAO.executeSimpleQuery(query.toString());
	 * 
	 * if(duplicateCheck!= null && duplicateCheck.size()>0){
	 * 
	 * error=true; errorcount++; lineError.append("Toll Tag already exists, ");
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * 
	 * if (eztolls.contains(eztoll)) { lineError.append("Duplicate eztoll,");
	 * error = true; errorcount++; }
	 * 
	 * eztolls.add(eztoll); } else { errorcount++; }
	 * 
	 * }//TRY INSIDE SHILE(LOOP) catch (Exception ex) { error=true;
	 * log.warn(ex); } if (lineError.length()>0) {
	 * System.out.println("Error :"+lineError.toString());
	 * list.add("Line "+count+":"+lineError.toString()+"<br/>"); }
	 * System.out.println("Record No :"+count); count++; }//CLOSE while
	 * (rows.hasNext()) }//FIRST TRY catch (Exception e) {
	 * log.warn("Error in import customer :" + e); } if (errorcount==0) {
	 * for(EzToll etoll:eztolls) {
	 * etoll.setTerminal(etoll.getDriver().getTerminal());
	 * etoll.setCompany(etoll.getDriver().getCompany());
	 * genericDAO.saveOrUpdate(etoll); } } return list; }
	 */

	// Fuel log - subcontractor
	private void processSubContractor(HSSFRow row, FuelLog fuelLog) {
		String driverLastName = ((String) getCellValue(row.getCell(7)));
		String driverFirstName = ((String) getCellValue(row.getCell(8)));
		if (StringUtils.isEmpty(driverLastName) && StringUtils.isEmpty(driverFirstName) ) {
			return;
		}
		
		Map criterias = new HashMap();
		List<Driver> driversList = getDriversFromName(criterias, driverLastName, driverFirstName);
		if (!driversList.isEmpty()) {
			return; 
		}
		
		SubContractor subContractor = getSubcontractorObjectFromName(driverLastName, driverFirstName);
		if (subContractor == null) {
			return; 
		}
		
		fuelLog.setSubContractor(subContractor);
		row.getCell(7).setCellValue("Subcontractor"); // Last name
		row.getCell(8).setCellValue("Subcontractor"); // First name
		row.getCell(6).setCellValue("0"); // Unit #
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<String> importfuellogMainSheet(InputStream is, Boolean override) throws Exception {
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance //XSSFWorkbook

		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<FuelLog> fuellogs = new ArrayList<FuelLog>();

		// List<String> emptydatalist=new ArrayList<String>();
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();

			wb = new HSSFWorkbook(fs);

			int numOfSheets = wb.getNumberOfSheets();
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			FuelLog fuellog = null;

			Iterator rows = sheet.rowIterator();
			StringBuffer lineError;

			while (rows.hasNext()) {
				System.out.println("Parsing row..");
				boolean error = false;
				buffer = new StringBuffer();
				int cellCount = 0;
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				lineError = new StringBuffer("");
				try {
					fuellog = new FuelLog();
					
					// Fuel log - subcontractor
					processSubContractor(row, fuellog);
					
					String Fname = (String) getCellValue(row.getCell(0));
					if (override == false) {
						try {
							if (StringUtils.isEmpty(Fname)) {
								error = true;
								lineError.append("Fuel Vendor is blank,");
							} else {
								criterias.clear();
								criterias.put("name", Fname);
								FuelVendor fuelvendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
								if (fuelvendor == null) {
									error = true;
									lineError.append("no such Fuel Vendor,");
									// throw new Exception("no such
									// fuelvender");
								} else {
									fuellog.setFuelvendor(fuelvendor);
								}
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("fuelvendor,");
							log.warn(ex.getMessage());
						}
					} else {
						criterias.clear();
						criterias.put("name", Fname);
						FuelVendor fuelvendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
						fuellog.setFuelvendor(fuelvendor);
					}

					// FOR COMPANY
					String company = ((String) getCellValue(row.getCell(1)));
					if (override == false) {
						try {
							if (StringUtils.isEmpty(company)) {
								error = true;
								lineError.append("Company is blank,");
							} else {
								criterias.clear();
								criterias.put("type", 3);
								criterias.put("name", company);
								Location companyName = genericDAO.getByCriteria(Location.class, criterias);
								if (companyName == null) {
									error = true;
									lineError.append("no such Company,");
								} else {
									fuellog.setCompany(companyName);
								}
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Company,");
							log.warn(ex.getMessage());
						}
					} else {
						criterias.clear();
						criterias.put("type", 3);
						criterias.put("name", company);
						Location companyName = genericDAO.getByCriteria(Location.class, criterias);
						fuellog.setCompany(companyName);
					}

					if (override == false) {
						Date date2 = row.getCell(2).getDateCellValue();

						try {
							if (validDate(date2)) {
								fuellog.setInvoiceDate(dateFormat1.parse(dateFormat1.format(date2)));
							} else {
								error = true;
								lineError.append("Invoice Date,");
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Invoice Date,");
							log.warn(ex.getMessage());

						}
					} else {
						if (validDate(getCellValue(row.getCell(2))))
							fuellog.setInvoiceDate((Date) getCellValue(row.getCell(2)));
						else {
							fuellog.setInvoiceDate(null);
						}
					}

					// FOR UNVOICED NUMBER
					// System.out.println("\nInvoiceNo====>"+(String)
					// getCellValue(row.getCell(4))+"\n");
					String invoiceNo = "";
					try {
						invoiceNo = (String) getCellValue(row.getCell(3));
					} catch (Exception e) {
						error = true;
						lineError.append("Invalid Invoice Number, ");
					}
					if (override == false) {
						try {
							if ((StringUtils.isEmpty(invoiceNo))) {
								error = true;
								lineError.append("Invoice# is blank,");
							} else {
								fuellog.setInvoiceNo((String) getCellValue(row.getCell(3)));
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Invoice Number,");
							log.warn(ex.getMessage());
						}
					} else {
						if ((StringUtils.isEmpty(invoiceNo)))
							fuellog.setInvoiceNo(null);
						else {
							fuellog.setInvoiceNo((String) getCellValue(row.getCell(3)));
						}
					}

					// FOR TRANSACTION DATE
					/*
					 * try { if (validDate(getCellValue(row.getCell(5))))
					 * fuellog.setTransactiondate((Date)
					 * getCellValue(row.getCell(5))); else { error = true;
					 * lineError.append("transaction date,"); } }
					 * catch(Exception ex) {
					 * //System.out.println("\nERROR IN TRANSACTION DATE\n");
					 * log.warn(ex.getMessage());
					 * 
					 * }
					 */
					if (override == false) {
						try {

							Date date4 = row.getCell(4).getDateCellValue();

							if (validDate(date4)) {
								fuellog.setTransactiondate(dateFormat1.parse(dateFormat1.format(date4)));

							} else {
								error = true;
								lineError.append("Transaction Date,");
							}
						} catch (Exception ex) {

							error = true;
							lineError.append("Transaction Date,");
							log.warn(ex.getMessage());

						}
					} else {
						if (validDate(getCellValue(row.getCell(4)))) {

							fuellog.setTransactiondate((Date) getCellValue(row.getCell(4)));
						} else {
							fuellog.setTransactiondate(null);
						}
					}

					try {
						if (validDate(getCellValue(row.getCell(5)))) {
							fuellog.setTransactiontime(dateFormat2.format((Date) getCellValue(row.getCell(5))));
						} else {
							// new trx time,uploading in 00:00 format
							String trxTime1 = (String) getCellValue(row.getCell(5));
							if (!(StringUtils.isEmpty(trxTime1))) {
								if (override == false) {
									if (trxTime1.length() == 5 || trxTime1.length() == 8) {
										StringBuilder time = new StringBuilder(
												StringUtils.leftPad((String) getCellValue(row.getCell(5)), 4, '0'));
										// time.insert(2, ':');
										if (trxTime1.length() == 8) {
											fuellog.setTransactiontime(time.toString().substring(0, 5));
										} else {
											fuellog.setTransactiontime(time.toString());
										}

									} else {
										// System.out.println("\ntrx time is not
										// valid\n");
										error = true;
										lineError.append("Transaction Time,");
									}
								} else {
									if (trxTime1.length() == 5 || trxTime1.length() == 8) {
										StringBuilder time = new StringBuilder(
												StringUtils.leftPad((String) getCellValue(row.getCell(5)), 4, '0'));
										// time.insert(2, ':');
										if (trxTime1.length() == 8) {
											fuellog.setTransactiontime(time.toString().substring(0, 5));
										} else {
											fuellog.setTransactiontime(time.toString());
										}
									} else {
										fuellog.setTransactiontime((String) getCellValue(row.getCell(5)));
									}
								}
							} else {
								fuellog.setTransactiontime((String) getCellValue(row.getCell(5)));
								// System.out.println("\nElse trxTime
								// empty=="+trxTime1+"\n");
							}

						}
					} catch (Exception ex) {
						fuellog.setTransactiontime((String) getCellValue(row.getCell(5)));
					}

					//String unit = ((String) getCellValue(row.getCell(6)));
					// if(override==false){
					//error = setUnitNumberInFuelLog(criterias, row, fuellog, lineError, error, unit);
					try {
						String unit = validateAndResetUnitNumber(criterias, row);
						if (StringUtils.isEmpty(unit)) {

							String lastName = ((String) getCellValue(row.getCell(7)));
							String firstName = ((String) getCellValue(row.getCell(8)));
							if (!lastName.isEmpty() && !firstName.isEmpty()) {
								Driver driver = getDriverObjectFromName(criterias, firstName, lastName, row);
								if (driver == null) {
									error = true;
									lineError.append("Unit is blank (check driver name),");
								} else {
									// HEMA: Added
									fuellog.setDriversid(driver);
									fuellog.setTerminal(driver.getTerminal());
									String transdate = null;

									if (validDate(getCellValue(row.getCell(4)))) {
										transdate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
									}

									/*String drivequery = "select obj from Ticket obj where   obj.loadDate<='" + transdate
											+ "' and obj.unloadDate>='" + transdate + "' and obj.driver="
											+ driver.getId();*/
									
									/*String drivequery = "select obj from Ticket obj where (obj.loadDate ='" + transdate
											+ "' OR obj.unloadDate ='" + transdate + "') and obj.driver="
											+ driver.getId();
									System.out.println("******** query is " + drivequery);
									List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);*/
									
									List<Ticket> tickets = getAllTicketsForDriver(String.valueOf(driver.getId()), transdate);
									if (!tickets.isEmpty()) {
										boolean tic = true;
										boolean first = true;
										List<String> vehicleid = new ArrayList<String>();
										for (Ticket ticket : tickets) {
											boolean d = vehicleid.contains(ticket.getVehicle().getId() + "");
											vehicleid.add(ticket.getVehicle().getId() + "");
											if (first) {
												first = false;
												continue;
											}
											if (!d) {
												error = true;
												lineError.append("More than one vehicle, ");
												tic = false;
											}
										}
										if (tic) {
											fuellog.setUnit(tickets.get(0).getVehicle());
										}
									} else {
										String driveFuelLogquery = "select obj from DriverFuelLog obj where  obj.transactionDate='"
												+ transdate + "' and obj.driver=" + driver.getId();
										System.out.println("********driver fuel query is " + driveFuelLogquery);
										List<DriverFuelLog> driverFuelLog = genericDAO
												.executeSimpleQuery(driveFuelLogquery);
										if (!driverFuelLog.isEmpty()) {
											boolean tic = true;
											boolean first = true;
											List<String> truckid = new ArrayList<String>();
											for (DriverFuelLog drvFuelLog : driverFuelLog) {
												boolean d = truckid.contains(drvFuelLog.getTruck().getId() + "");
												truckid.add(drvFuelLog.getTruck().getId() + "");
												if (first) {
													first = false;
													continue;
												}
												if (!d) {
													error = true;
													lineError.append("More than one vehicle, ");
													tic = false;
												}
											}
											if (tic) {
												fuellog.setUnit(driverFuelLog.get(0).getTruck());
											}
										} else {

											String odometerQery = "select obj from Odometer obj where obj.recordDate='"
													+ transdate + "' and obj.driver=" + driver.getId();
											System.out.println("********odometer query is " + odometerQery);
											List<Odometer> driverOdometer = genericDAO.executeSimpleQuery(odometerQery);

											if (!driverOdometer.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> vehid = new ArrayList<String>();
												for (Odometer odometer : driverOdometer) {
													boolean d = vehid.contains(odometer.getTruck().getId() + "");
													vehid.add(odometer.getTruck().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														error = true;
														lineError.append("More than one vehicle, ");
														tic = false;
													}
												}
												if (tic) {
													fuellog.setUnit(driverOdometer.get(0).getTruck());
												}
											} else {
												error = true;
												lineError.append(
														"Unit is either blank or not valid for given transaction date and no matching Ticket, Fuel Log, Odometer entry found while detrmining correct unit ");
											}
										}
									}
								}

							} else {

								error = true;
								lineError.append("Unit is blank,");
							}
						} else {
							criterias.clear();

							String transactionDate = null;
							System.out.println("********** date value is " + getCellValue(row.getCell(4)));
							if (validDate(getCellValue(row.getCell(4)))) {
								transactionDate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
							}
							Vehicle vehicle = null;
							String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
									+ Integer.parseInt((String) getCellValue(row.getCell(6))) + " and obj.validFrom<='"
									+ transactionDate + "' and obj.validTo>='" + transactionDate + "'";

							System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
							List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);

							if (vehicleList == null || vehicleList.size() == 0) {
								System.out.println("Entered here ");
								error = true;
								lineError.append("no such Vehicle,");
							} else {
								fuellog.setUnit(vehicleList.get(0));
								vehicle = vehicleList.get(0);
								// ***** newly added *********

								String lastName = ((String) getCellValue(row.getCell(7)));
								try {
									if (!StringUtils.isEmpty(lastName)) {
										criterias.clear();
										criterias.put("lastName", lastName);
										Driver lname = genericDAO.getByCriteria(Driver.class, criterias);
										if (lname == null) {
											error = true;
											lineError.append("No such Last Name,");
										} else {
											// fuellog.setDriverLname(lname);
										}
									}
								} catch (Exception ex) {
									error = true;
									lineError.append("Driver Last Name,");
									log.warn(ex.getMessage());
								}

								String firstName = ((String) getCellValue(row.getCell(8)));
								try {
									if (!StringUtils.isEmpty(firstName)) {
										criterias.clear();
										criterias.put("firstName", firstName);
										Driver fname = genericDAO.getByCriteria(Driver.class, criterias);
										if (fname == null) {
											error = true;
											lineError.append("No such First Name,");
										} else {
											// fuellog.setDriverFname(fname);
										}
									}
								} catch (Exception ex) {
									error = true;
									lineError.append("Driver First Name,");
									log.warn(ex.getMessage());
								}

								// taking driverFname and driverLName and
								// storing as fullname
								try {
									if (StringUtils.isEmpty(lastName) && StringUtils.isEmpty(firstName)) {
										String transactiondate = null;
										if (validDate(getCellValue(row.getCell(4)))) {
											transactiondate = dateFormat
													.format(((Date) getCellValue(row.getCell(4))).getTime());
										}

										/*String drivequery = "select obj from Ticket obj where   obj.loadDate<='"
												+ transactiondate + "' and obj.unloadDate>='" + transactiondate
												+ "' and obj.vehicle=" + vehicle.getId();*/
										
										/*String drivequery = "select obj from Ticket obj where (obj.loadDate ='"
												+ transactiondate + "' OR obj.unloadDate ='" + transactiondate
												+ "') and obj.vehicle=" + vehicle.getId();
										System.out.println("******** query is " + drivequery);
										List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);*/
										
										List<Vehicle> vehicleListForDriver = new ArrayList<Vehicle>();
										vehicleListForDriver.add(vehicle);
										List<Ticket> tickets = getTicketsForVehicle(vehicleListForDriver, transactiondate);
										if (!tickets.isEmpty()) {
											boolean tic = true;
											boolean first = true;
											List<String> driverid = new ArrayList<String>();
											for (Ticket ticket : tickets) {
												boolean d = driverid.contains(ticket.getDriver().getId() + "");
												driverid.add(ticket.getDriver().getId() + "");
												if (first) {
													first = false;
													continue;
												}
												if (!d) {
													error = true;
													lineError.append("More than one Driver, ");
													tic = false;
												}
											}
											if (tic) {
												fuellog.setDriversid(tickets.get(0).getDriver());
												Driver driver = genericDAO.getById(Driver.class,
														tickets.get(0).getDriver().getId());
												fuellog.setTerminal(driver.getTerminal());
											}
										} else {
											String driveFuelLogquery = "select obj from DriverFuelLog obj where   obj.transactionDate='"
													+ transactiondate + "' and obj.truck=" + vehicle.getId();
											System.out.println("********driver fuel query is " + driveFuelLogquery);
											List<DriverFuelLog> driverFuelLog = genericDAO
													.executeSimpleQuery(driveFuelLogquery);
											if (!driverFuelLog.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> driverid = new ArrayList<String>();
												for (DriverFuelLog drvFuelLog : driverFuelLog) {
													boolean d = driverid.contains(drvFuelLog.getDriver().getId() + "");
													driverid.add(drvFuelLog.getDriver().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														error = true;
														lineError.append("More than one Driver, ");
														tic = false;
													}
												}
												if (tic) {
													fuellog.setDriversid(driverFuelLog.get(0).getDriver());
													Driver driver = genericDAO.getById(Driver.class,
															driverFuelLog.get(0).getDriver().getId());
													fuellog.setTerminal(driver.getTerminal());
												}
											} else {

												String odometerQery = "select obj from Odometer obj where obj.recordDate='"
														+ transactiondate + "' and obj.truck=" + vehicle.getId();
												System.out.println("********odometer query is " + odometerQery);
												List<Odometer> driverOdometer = genericDAO
														.executeSimpleQuery(odometerQery);

												if (!driverOdometer.isEmpty()) {
													boolean tic = true;
													boolean first = true;
													List<String> driverid = new ArrayList<String>();
													for (Odometer odometer : driverOdometer) {
														boolean d = driverid
																.contains(odometer.getDriver().getId() + "");
														driverid.add(odometer.getDriver().getId() + "");
														if (first) {
															first = false;
															continue;
														}
														if (!d) {
															error = true;
															lineError.append("More than one Driver, ");
															tic = false;
														}
													}
													if (tic) {
														fuellog.setDriversid(driverOdometer.get(0).getDriver());
														Driver driver = genericDAO.getById(Driver.class,
																driverOdometer.get(0).getDriver().getId());
														fuellog.setTerminal(driver.getTerminal());
													}
												} else {
													error = true;
													lineError
															.append("No matching  Ticket, Fuel Log,  Odometer entry, ");
												}
											}
										}
									} else {
										Driver driver = getDriverObjectFromName(criterias, firstName, lastName, row);
										if (driver == null) {
											error = true;
											lineError.append("Invalid Driver,");
											//throw new Exception("Invalid Driver");
										} else {
											fuellog.setDriversid(driver);
											fuellog.setTerminal(driver.getTerminal());
										}
									}
								} catch (Exception ex) {
									ex.printStackTrace();
									log.warn(ex.getMessage());
								}

								// ******** newly added ends here ********

							}
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("Unit,");
						log.warn(ex.getMessage());
					}
 
					
					// FOR FUEL CARD NUMBER
					String cardNo = ((String) getCellValue(row.getCell(9)));
					/*
					 * if(override==false) { try {
					 * //fuellog.setFuelCardNumber(((String)
					 * getCellValue(row.getCell(10)))); if(!cardNo.isEmpty()){
					 * fuellog.setFuelCardNumber((String)getCellValue(row.
					 * getCell(10))); } else{ error = true;
					 * lineError.append("Card Number is blank,"); } } catch
					 * (Exception ex) { error = true;
					 * lineError.append("Card Number,"); } } else{
					 * fuellog.setFuelCardNumber((String)getCellValue(row.
					 * getCell(10))); }
					 */

					///

					try {
						// fuellog.setFuelCardNumber(((String)
						// getCellValue(row.getCell(10))));
						if (override == false) {// StringUtils.isEmpty
							/* if(!cardNo.isEmpty()){ */
							
							if (!StringUtils.isEmpty(cardNo)) {
								
								if (handleExcludedCardNumberChecks(fuellog, cardNo)) {
									// reset cardNo
									cardNo = StringUtils.EMPTY;
								} else {
									criterias.clear();
	
									if (fuellog.getFuelvendor() != null)
										criterias.put("fuelvendor.id", fuellog.getFuelvendor().getId());
									criterias.put("fuelcardNum", cardNo);
									System.out.println("Criterias for getting fuelcard = " + "fuelvendor.id = " + fuellog.getFuelvendor().getId() + ", fuelcardNum = " + cardNo);
									
									List<FuelCard> fuelcard = genericDAO.findByCriteria(FuelCard.class, criterias);
									if (!fuelcard.isEmpty() && fuelcard.size() > 0) {
										if (fuellog.getDriversid() != null && fuellog.getFuelvendor() != null) {
											
											// HEMA: Added to get fuel card using IN clause for driver
											String firstName = fuellog.getDriversid().getFirstName();
											String lastName = fuellog.getDriversid().getLastName();
											List<Driver> listOfDrivers = getDriversFromName(criterias, lastName, firstName);
											String listOfDriversStr = getCommaSeparatedListOfDriverID(listOfDrivers);

											criterias.clear();
											System.out.println("Criterias for choosing card number -> DriverID: " + listOfDriversStr + ", FuelVendorID: " + fuellog.getFuelvendor().getId() + ", FuelCardID: " + fuelcard.get(0).getId());
											
											String fuelCardQuery = "select obj from DriverFuelCard obj where "
													+ "obj.driver IN (" + listOfDriversStr + ") "
															+ " and obj.fuelvendor =" + fuellog.getFuelvendor().getId()
															+ " and obj.fuelcard = " +  fuelcard.get(0).getId();
											System.out.println("********fuelcard query is " + fuelCardQuery);
											List<DriverFuelCard> driverfuelcard = genericDAO
													.executeSimpleQuery(fuelCardQuery);
											
											if (!driverfuelcard.isEmpty() && driverfuelcard.size() > 0) {
												fuellog.setFuelcard(fuelcard.get(0));
											} else {
												if (validateFuelCardForVehicle(fuellog, fuelcard.get(0))) {
													fuellog.setFuelcard(fuelcard.get(0));
												} else {
													error = true;
													lineError.append(
														" Invalid Fuel Card# for entered Fuel Vendor and Driver/Vehicle, ");
												}
											}
										} 
									} else {
										error = true;
										lineError.append(" Invalid Card Number,");
									}
								}
							} else {
								error = true;
								lineError.append("Card Number is blank,");
							}
						} else {
							System.out.println("\nELSE OVerride card Number1\n");
							/* if(!cardNo.isEmpty()){ */
							if (!StringUtils.isEmpty(cardNo)) {
								System.out.println("\nOVerride card Number2\n");
								criterias.clear();
								if (fuellog.getFuelvendor() != null)
									criterias.put("fuelvendor.id", fuellog.getFuelvendor().getId());
								criterias.put("fuelcardNum", cardNo);
								List<FuelCard> fuelcard = genericDAO.findByCriteria(FuelCard.class, criterias);
								if (!fuelcard.isEmpty() && fuelcard.size() > 0) {
									if (fuellog.getDriversid() != null && fuellog.getFuelvendor() != null) {
										// HEMA: Added to get fuel card using IN clause for driver
										String firstName = fuellog.getDriversid().getFirstName();
										String lastName = fuellog.getDriversid().getLastName();
										List<Driver> listOfDrivers = getDriversFromName(criterias, lastName, firstName);
										String listOfDriversStr = getCommaSeparatedListOfDriverID(listOfDrivers);

										criterias.clear();
										System.out.println("Criterias for choosing card number -> DriverID: " + listOfDriversStr + ", FuelVendorID: " + fuellog.getFuelvendor().getId() + ", FuelCardID: " + fuelcard.get(0).getId());
										
										String fuelCardQuery = "select obj from DriverFuelCard obj where "
												+ "obj.driver IN (" + listOfDriversStr + ") "
														+ " and obj.fuelvendor =" + fuellog.getFuelvendor().getId()
														+ " and obj.fuelcard = " +  fuelcard.get(0).getId();
										System.out.println("********fuelcard query is " + fuelCardQuery);
										List<DriverFuelCard> driverfuelcard = genericDAO
												.executeSimpleQuery(fuelCardQuery);
										
										
										if (!driverfuelcard.isEmpty() && driverfuelcard.size() > 0)
											fuellog.setFuelcard(fuelcard.get(0));

									}
								}
								/*
								 * FuelCard fuelCard=null;
								 * fuellog.setFuelcard(fuelCard);
								 * System.out.println(
								 * "\nOVerride card Number3\n");
								 */
							} else {
								System.out.println("\nOVerride card Number4\n");
								FuelCard fuelCard = null;
								fuellog.setFuelcard(fuelCard);
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();	
						error = true;
						System.out.println("\n\n Error in Card Number\n");
						lineError.append("Card Number,");
					}

					///

					// new FOR FUEL CARD NUMBER for long
					/*
					 * String cardNo = ((String) getCellValue(row.getCell(10)));
					 * if(override==false) { try {
					 * fuellog.setFuelCardNumber(Long.parseLong((String)
					 * getCellValue(row.getCell(10)))); if(!cardNo.isEmpty()){
					 * fuellog.setFuelCardNumber((String)getCellValue(row.
					 * getCell(10)));
					 * fuellog.setFuelCardNumber((Long)getCellValue(row.getCell(
					 * 10))); } else{ error = true;
					 * lineError.append("Card Number is blank,"); } } catch
					 * (Exception ex) { error = true;
					 * lineError.append("Card Number,"); } } else{
					 * fuellog.setFuelCardNumber((Long)getCellValue(row.getCell(
					 * 10))); }
					 */

					String fueltext = ((String) getCellValue(row.getCell(10)));
					if (override == false) {
						try {
							if (!fueltext.isEmpty()) {
								fuellog.setFueltype((String) getCellValue(row.getCell(10)));
								criterias.clear();
								criterias.put("dataType", "fuel_type");
								criterias.put("dataText", fueltext);
								StaticData staticdata = genericDAO.getByCriteria(StaticData.class, criterias);
								if (staticdata == null) {
									error = true;
									lineError.append("Fuel Type,");
								} else {
									/*
									 * System.out.println("\nstaticdata---id=>"+
									 * staticdata.getId()+"\n");
									 * System.out.println(
									 * "\nstaticdata---dataType=>"+staticdata.
									 * getDataType()+"\n"); System.out.println(
									 * "\nstaticdata---dataText=>"+staticdata.
									 * getDataText()+"\n");
									 */
									fuellog.setFueltype((String) getCellValue(row.getCell(10)));
								}
							} else {
								error = true;
								lineError.append("Fuel Type is blank,");
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Fuel Type,");
							log.warn(ex.getMessage());
						}
					} else {
						fuellog.setFueltype((String) getCellValue(row.getCell(10)));
					}

					// FOR FUEL TYPE
					/*
					 * String fueltype=((String) getCellValue(row.getCell(11)));
					 * if(override==false){ try { if(!fueltype.isEmpty()){
					 * fuellog.setFueltype((String)getCellValue(row.getCell(11))
					 * ); } else{ error = true;
					 * lineError.append("Fueltype is blank,"); } }
					 * catch(Exception ex) { error = true;
					 * lineError.append("Fueltype,"); log.warn(ex.getMessage());
					 * } } else{
					 * fuellog.setFueltype((String)getCellValue(row.getCell(11))
					 * ); }
					 */

					// FOR CITY
					String city = ((String) getCellValue(row.getCell(11)));
					if (!(StringUtils.isEmpty(city))) {
						if (override == false) {
							try {
								if (!city.isEmpty()) {
									fuellog.setCity((String) getCellValue(row.getCell(11)));
								} else {
									error = true;
									lineError.append("City is blank,");
								}
							} catch (Exception ex) {
								error = true;
								lineError.append("City,");
							}
						} else {
							fuellog.setCity((String) getCellValue(row.getCell(11)));
						}
					} else {
						fuellog.setCity("");
						System.out.println("\ncity is empty\n" + fuellog.getCity());
					}

					// FOR STATE
					String name = (String) getCellValue(row.getCell(12));
					if (!(StringUtils.isEmpty(name))) {
						if (override == false) {
							try {
								criterias.clear();
								if (StringUtils.isEmpty(name)) {
									error = true;
									lineError.append("State is blank,");
									// throw new Exception("Invalid state
									// name");
								} else {
									criterias.clear();
									criterias.put("name", name);
									State state = genericDAO.getByCriteria(State.class, criterias);
									if (state == null) {
										error = true;
										lineError.append("no such State,");
										// throw new Exception("no such state");
									} else {
										fuellog.setState(state);
									}
								}
							} catch (Exception ex) {
								error = true;
								lineError.append("State,");
								System.out.println("\nerroe in state==>" + ex + "\n");
								log.warn(ex.getMessage());
							}
						} else {
							criterias.clear();
							criterias.put("name", name);
							State state = genericDAO.getByCriteria(State.class, criterias);
							fuellog.setState(state);
						}
					} else {
						// System.out.println("\nstate is empty 11\n");

						criterias.clear();
						criterias.put("id", 3600l);
						State state = genericDAO.getByCriteria(State.class, criterias);
						// System.out.println("\nstate is empty44\n");
						fuellog.setState(state);
						System.out.println("\nstate is empty55\n");

					}

					// for GALLONS

					/*
					 * String testgallon=row.getCell(14).toString(); Double
					 * gallon = getValidGallon(testgallon); if (gallon != null)
					 * { fuellog.setGallons(gallon); } else {
					 * lineError.append("gallons,"); error = true; }
					 */

					String gallon = "";
					if (override == false) {
						if (row.getCell(13) != null) {
							gallon = row.getCell(13).toString();
						}
						Double gallon2 = getValidGallon(gallon);
						if (gallon2 != null) {
							fuellog.setGallons(gallon2);
						} else {
							lineError.append("Gallon is blank,");
							error = true;
						}
					} else {
						if (row.getCell(13) != null) {
							gallon = row.getCell(13).toString();
						}
						Double gallon2 = getValidGallon(gallon);
						if (gallon2 != null) {
							fuellog.setGallons(gallon2);
						} else {
						}
					}

					// for unitprice
					/*
					 * String unitprice=row.getCell(15).toString(); Double
					 * unitprice1 = getValidGallon(unitprice); if (unitprice1 !=
					 * null) { fuellog.setUnitprice(unitprice1); } else {
					 * System.out.println("\nunitprice is null\n");
					 * lineError.append("unitprice,"); error = true; }
					 */
					String unitprice1 = "";
					if (override == false) {
						if (row.getCell(14) != null) {
							unitprice1 = row.getCell(14).toString();
						}

						Double unitprice2 = getValidGallon(unitprice1);
						if (unitprice2 != null) {
							fuellog.setUnitprice(unitprice2);
						} else {
							lineError.append("Unit Price is blank,");
							error = true;
						}
					} else {

						if (row.getCell(14) != null) {
							unitprice1 = row.getCell(14).toString();
						}

						Double unitprice2 = getValidGallon(unitprice1);
						if (unitprice2 != null) {
							fuellog.setUnitprice(unitprice2);
						} else {
						}
					}

					// Gross Cost
					String grossamount1 = "";
					if (override == false) {
						if (row.getCell(15) != null) {
							grossamount1 = row.getCell(15).toString();
						}

						if (!(StringUtils.isEmpty(grossamount1))) {
							Double grossamount2 = getValidGallon(grossamount1);
							if (grossamount2 != null) {
								fuellog.setGrosscost(grossamount2);
							} else {
								lineError.append("Gross Cost,");
								error = true;
							}
						}
					} else {
						if (row.getCell(15) != null) {
							grossamount1 = row.getCell(15).toString();
						}

						Double grossamount2 = getValidGallon(grossamount1);
						if (grossamount2 != null) {
							fuellog.setGrosscost(grossamount2);
						} else {
						}
					}
					// Gross Cost

					// FOR FEES

					String fees1 = "";
					if (override == false) {
						if (row.getCell(16) != null) {
							fees1 = row.getCell(16).toString();
						}

						Double fees2 = getValidGallon(fees1);
						if (fees2 != null) {
							fuellog.setFees(fees2);
						} else {
							lineError.append("Fees is blank,");
							error = true;
						}
					} else {
						if (row.getCell(16) != null) {
							fees1 = row.getCell(16).toString();
						}

						Double fees2 = getValidGallon(fees1);
						if (fees2 != null) {
							fuellog.setFees(fees2);
						} else {
						}
					}
					// FOR DISCOUNTS
					String discount1 = "";
					// System.out.println("\ndiscount1===>"+discount1+"\n");
					if (override == false) {
						if (row.getCell(17) != null) {
							discount1 = row.getCell(17).toString();
						}

						Double discount2 = getValidGallon(discount1);
						if (discount2 != null) {
							discount2 = Math.abs(discount2);
							fuellog.setDiscounts(discount2);
						} else {
							lineError.append("Discount is blank,");
							error = true;
						}
					} else {
						if (row.getCell(17) != null) {
							discount1 = row.getCell(17).toString();
						}

						Double discount2 = getValidGallon(discount1);
						if (discount2 != null) {
							discount2 = Math.abs(discount2);
							fuellog.setDiscounts(discount2);
						} else {
						}
					}

					// FOR AMOUNTS
					/*
					 * String amount1=row.getCell(18).toString(); Double amount2
					 * = getValidGallon(amount1); if (amount2 != null){
					 * fuellog.setAmount(amount2); } else {
					 * System.out.println("\namount2 is null\n");
					 * lineError.append("amount,"); error = true; }
					 */
					String amount1 = null;
					/*
					 * try{ amount1=row.getCell(18).toString(); } catch
					 * (Exception e) { lineError.append("amount,"); error =
					 * true; }
					 */
					if (override == false) {
						try {
							amount1 = row.getCell(18).toString();
							Double amount2 = getValidGallon(amount1);
							if (amount2 != null) {
								fuellog.setAmount(amount2);
							} else {
								lineError.append("Amount is blank,");
								error = true;
							}
						} catch (Exception e) {
							lineError.append("Amount,");
							error = true;
						}
					} else {
						if (row.getCell(18) != null)
							amount1 = row.getCell(18).toString();

						Double amount2 = getValidGallon(amount1);
						if (amount2 != null) {
							fuellog.setAmount(amount2);
						} else {
						}
					}

					// CALCULATING DISCOUNT AND NET AMMOUNT IF FUELDISCOUNT
					// PERCENTAGE IS PRESENT
					if (override == false) {
						if (!error) {
							try {
								if (!StringUtils.isEmpty(grossamount1)) {

									criterias.clear();
									criterias.put("name", Fname);
									FuelVendor vendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
									if (vendor != null) {
										System.out.println("\nDiscount first\n");
										criterias.clear();
										criterias.put("fuelvendor.id", vendor.getId());
										FuelDiscount fueldicount = genericDAO.getByCriteria(FuelDiscount.class,
												criterias);
										System.out.println("\nDiscount Second\n");
										if (fueldicount != null) {
											double firstdiscountamount = fuellog.getDiscounts();
											double seconddiscountpercentage = fueldicount.getFuelDiscountPercentage();
											double seconddiscountAmount = 0.0;
											double totalDiscount = 0.0;
											Double grossamount = getValidGallon(grossamount1);
											if (grossamount != null) {
												if (grossamount != 0) {
													// System.out.println("\nFirst
													// grossamount--->"+grossamount+"\n");
													grossamount = grossamount - firstdiscountamount;
													seconddiscountAmount = (grossamount * seconddiscountpercentage);
													grossamount = grossamount - seconddiscountAmount;
													totalDiscount = firstdiscountamount + seconddiscountAmount;

													totalDiscount = MathUtil.roundUp(totalDiscount, 2);
													grossamount = MathUtil.roundUp(grossamount, 2);

													fuellog.setDiscounts(totalDiscount);
													fuellog.setAmount(grossamount);

													/*
													 * System.out.println(
													 * "\nfirstdiscountamount==>"
													 * +firstdiscountamount+"\n"
													 * ); System.out.println(
													 * "\nseconddiscountpercentage==>"
													 * +seconddiscountpercentage
													 * +"\n");
													 * System.out.println(
													 * "\nseconddiscountAmount==>"
													 * +seconddiscountAmount+
													 * "\n");
													 * System.out.println(
													 * "\ntotalDiscount==>"+
													 * totalDiscount+"\n");
													 * System.out.println(
													 * "\nsetAmount(grossamount)==>"
													 * +grossamount+"\n");
													 */
												}
											}

										}
									}
								}
								Double grossamount = getValidGallon(grossamount1);
								if (grossamount == null) {
									double discountAmount = fuellog.getDiscounts();
									double feesAmount = fuellog.getFees();
									// System.out.println("grossamount ==
									// null");
									Double NetAmount = getValidGallon(amount1);
									// System.out.println("NetAmount ==
									// "+NetAmount+"\n");
									if (discountAmount == 0 && feesAmount == 0) {
										// double
										// grossAmount=NetAmount+(discountAmount-feesAmount);
										fuellog.setGrosscost(NetAmount);
									} else {
										lineError.append("Discount and Fees should be zero,");
										error = true;
									}

								}
								if (grossamount == 0) {
									double discountAmount = fuellog.getDiscounts();
									double feesAmount = fuellog.getFees();
									// System.out.println("grossamount ==
									// null");
									Double NetAmount = getValidGallon(amount1);
									// System.out.println("NetAmount ==
									// "+NetAmount+"\n");
									if (discountAmount == 0 && feesAmount == 0) {
										// double
										// grossAmount=NetAmount+(discountAmount-feesAmount);
										fuellog.setGrosscost(NetAmount);
									} else {
										lineError.append("Discount and Fees should be zero,");
										error = true;
									}
								}

							} catch (Exception ex) {
								System.out.println("error calculating total discount");
							}
						}
					}
					/// If override is true

					else {
						/* if (!error) { */
						try {
							if (!StringUtils.isEmpty(grossamount1)) {

								criterias.clear();
								criterias.put("name", Fname);
								FuelVendor vendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
								if (vendor != null) {
									System.out.println("\nDiscount first\n");
									criterias.clear();
									criterias.put("fuelvendor.id", vendor.getId());
									FuelDiscount fueldicount = genericDAO.getByCriteria(FuelDiscount.class, criterias);
									System.out.println("\nDiscount Second\n");
									if (fueldicount != null) {
										double firstdiscountamount = fuellog.getDiscounts();
										double seconddiscountpercentage = fueldicount.getFuelDiscountPercentage();
										double seconddiscountAmount = 0.0;
										double totalDiscount = 0.0;
										Double grossamount = getValidGallon(grossamount1);
										if (grossamount != null) {
											if (grossamount != 0) {
												// System.out.println("\nFirst
												// grossamount--->"+grossamount+"\n");
												grossamount = grossamount - firstdiscountamount;
												seconddiscountAmount = (grossamount * seconddiscountpercentage);
												grossamount = grossamount - seconddiscountAmount;
												totalDiscount = firstdiscountamount + seconddiscountAmount;

												totalDiscount = MathUtil.roundUp(totalDiscount, 2);
												grossamount = MathUtil.roundUp(grossamount, 2);

												fuellog.setDiscounts(totalDiscount);
												fuellog.setAmount(grossamount);

												/*
												 * System.out.println(
												 * "\nfirstdiscountamount==>"+
												 * firstdiscountamount+"\n");
												 * System.out.println(
												 * "\nseconddiscountpercentage==>"
												 * +seconddiscountpercentage+
												 * "\n"); System.out.println(
												 * "\nseconddiscountAmount==>"+
												 * seconddiscountAmount+"\n");
												 * System.out.println(
												 * "\ntotalDiscount==>"+
												 * totalDiscount+"\n");
												 * System.out.println(
												 * "\nsetAmount(grossamount)==>"
												 * +grossamount+"\n");
												 */
											}
										}

									}
								}
							}
							Double grossamount = getValidGallon(grossamount1);
							if (grossamount == null) {
								/*
								 * double discountAmount=fuellog.getDiscounts();
								 * double feesAmount=fuellog.getFees();
								 */
								// System.out.println("grossamount == null");
								Double NetAmount = getValidGallon(amount1);
								System.out.println("\ngrossamount == null when orride true\n");
								System.out.println("\nNetAmount==>" + NetAmount + "\n");
								// System.out.println("NetAmount ==
								// "+NetAmount+"\n");
								/* if(discountAmount ==0 && feesAmount ==0){ */
								// double
								// grossAmount=NetAmount+(discountAmount-feesAmount);
								fuellog.setGrosscost(NetAmount);
								/* } */
								/*
								 * else{ lineError.append(
								 * "discount and fees should be zero,"); error =
								 * true; }
								 */

							}
							if (grossamount == 0) {
								/*
								 * double discountAmount=fuellog.getDiscounts();
								 * double feesAmount=fuellog.getFees();
								 */
								// System.out.println("grossamount == null");
								Double NetAmount = getValidGallon(amount1);
								System.out.println("\ngrossamount == 0 when orride true\n");
								System.out.println("\nNetAmount==>" + NetAmount + "\n");
								// System.out.println("NetAmount ==
								// "+NetAmount+"\n");
								/* if(discountAmount ==0 && feesAmount ==0){ */
								// double
								// grossAmount=NetAmount+(discountAmount-feesAmount);
								fuellog.setGrosscost(NetAmount);
								/* } */
								/*
								 * else{ lineError.append(
								 * "discount and fees should be zero,"); error =
								 * true; }
								 */
							}

						} catch (Exception ex) {
							System.out.println("exp--->" + ex + "\n");
							System.out.println("error calculating total discount when override id true");
						}
						// }
					}

					// END OF CELL
					if (override == false) {
						System.out.println("***** eneter here ok 0");
						if (!error) {
							System.out.println("***** eneter here ok 1");
							Map prop = new HashMap();
							prop.put("fuelvendor", fuellog.getFuelvendor().getId());
							prop.put("driversid", fuellog.getDriversid().getId());
							prop.put("company", fuellog.getCompany().getId());
							prop.put("terminal", fuellog.getTerminal().getId());
							prop.put("state", fuellog.getState().getId());
							prop.put("unit", fuellog.getUnit().getId());
							prop.put("invoiceDate", dateFormat1.format(fuellog.getInvoiceDate()));
							prop.put("invoiceNo", fuellog.getInvoiceNo());
							prop.put("transactiondate", dateFormat1.format(fuellog.getTransactiondate()));
							prop.put("transactiontime", fuellog.getTransactiontime());
							if (fuellog.getFuelcard() != null) {
								prop.put("fuelcard", fuellog.getFuelcard().getId());
							} 
							prop.put("fueltype", fuellog.getFueltype());
							prop.put("city", fuellog.getCity());
							prop.put("gallons", fuellog.getGallons());
							prop.put("unitprice", fuellog.getUnitprice());
							prop.put("fees", fuellog.getFees());
							prop.put("discounts", fuellog.getDiscounts());
							prop.put("amount", fuellog.getAmount());
							boolean rst = genericDAO.isUnique(FuelLog.class, fuellog, prop);
							System.out.println("***** eneter here ok 2" + rst);
							if (!rst) {
								System.out.println("***** eneter here ok 3");
								lineError.append("Fuel log entry already exists(Duplicate),");
								error = true;
								errorcount++;
							}

							if (fuellogs.contains(fuellog)) {
								lineError.append("Duplicate Fuel log record in file,");
								error = true;
								errorcount++;
							} else {
								fuellogs.add(fuellog);
							}
						} else {
							errorcount++;
						}
					} else {
						if (!error) {
							fuellogs.add(fuellog);
						} else {
							errorcount++;
						}
					}

				} // TRY INSIDE WHILE(LOOP)
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("***** Entered here in exception" + ex.getMessage());

					error = true;
					log.warn(ex);
				}
				if (lineError.length() > 0) {
					System.out.println("Error :" + lineError.toString());
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				System.out.println("Record No :" + count);
				count++;
			} // CLOSE while (rows.hasNext())
		} // FIRST TRY
		catch (Exception e) {
			list.add("Not able to upload XL !!! please try again");
			log.warn("Error in import Fuel log :" + e);
			e.printStackTrace();
		}

		System.out.println("Done here.. " + errorcount);
		if (errorcount == 0) {
			System.out.println("Error count = 0");
			for (FuelLog fuelog : fuellogs) {
				/*String ticktQuery = "select obj from Ticket obj where obj.driver=" + fuelog.getDriversid().getId()
						+ " and obj.loadDate <='" + drvdf.format(fuelog.getTransactiondate())
						+ "' and obj.unloadDate>='" + drvdf.format(fuelog.getTransactiondate()) + "'";*/
				
				String ticktQuery = "select obj from Ticket obj where obj.driver=" + fuelog.getDriversid().getId()
						+ " and (obj.loadDate ='" + drvdf.format(fuelog.getTransactiondate())
						+ "' OR obj.unloadDate ='" + drvdf.format(fuelog.getTransactiondate()) + "')";

				System.out.println("Fuel Log Violation query = " + ticktQuery);
				List<Ticket> tickObj = genericDAO.executeSimpleQuery(ticktQuery);

				if (tickObj.size() > 0 && tickObj != null) {
					fuelog.setFuelViolation("Not Violated");
				} else {
					fuelog.setFuelViolation("Violated");
				}

				Map criti = new HashMap();
				criti.clear();
				criti.put("id", fuelog.getDriversid().getId());
				Driver drvOBj = genericDAO.getByCriteria(Driver.class, criti);
				if (drvOBj != null)
					fuelog.setDriverFullName(drvOBj.getFullName());

				criti.clear();
				criti.put("id", fuelog.getUnit().getId());
				Vehicle vehObj = genericDAO.getByCriteria(Vehicle.class, criti);
				if (vehObj != null)
					fuelog.setUnitNum(vehObj.getUnitNum());

				genericDAO.saveOrUpdate(fuelog);
			} 
		} else {
			System.out.println("Line Error = " + list );
		}
		
		System.out.println("Returning list");
		return list;
	}
	
	private String validateAndResetUnitNumber(Map criterias, HSSFRow row) {
		String unit = (String) getCellValue(row.getCell(6));
		if (StringUtils.isEmpty(unit)) {
			return StringUtils.EMPTY;
		}
		
		criterias.clear();

		String transactionDate = null;
		System.out.println("********** date value is " + getCellValue(row.getCell(4)));
		if (validDate(getCellValue(row.getCell(4)))) {
			transactionDate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
		}
		Vehicle vehicle = null;
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
				+ Integer.parseInt(unit) + " and obj.validFrom<='"
				+ transactionDate + "' and obj.validTo>='" + transactionDate + "'";

		System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);

		if (vehicleList == null || vehicleList.size() == 0) {
			System.out.println("User given unit number " + unit + " is not valid, returning EMPTY");
			return StringUtils.EMPTY;
		} else {
			System.out.println("User given unit number " + unit + " is valid");
			return unit;
		}
	}
	
	private Driver getDriverObjectFromName(String fullName, HSSFRow row) {
		List<Driver> drivers = getDriversFromName(fullName);

		if (drivers.size() == 0) { // no driver, continue to existing flow
			System.out.println("<<Custom code for Driver>>: No driver matching fullName = " + fullName + " was found.");
			return null;
		}
		
		if (drivers.size() == 1) { // exactly 1 matching driver, continue to existing flow
			System.out.println("<<Custom code for Driver>>: Exactly 1 matching driver was found with id = " + drivers.get(0).getId());
			return drivers.get(0);
		}
		
		// more than 1 driver found
		//return retrieveActualDriver(drivers, row);
		String transdate = getTransactionDateFromExcel(row, 6);
		return retrieveActualDriver(drivers, transdate);
	}

	// Fuel log - subcontractor
	private SubContractor getSubcontractorObjectFromName(String lastName, String firstName) {
		String baseSubcontractorQuery = "select obj from SubContractor obj where "
				+ "obj.name LIKE '"; 
		
		String name = lastName + " " + firstName + "%";
		String subcontractorQuery = baseSubcontractorQuery + name + "'";
		System.out.println("******* The subcontractor query for fuel upload - first attempt is " + subcontractorQuery);
		List<SubContractor> subContractorList = genericDAO.executeSimpleQuery(subcontractorQuery);
		if (subContractorList.isEmpty()) {
			name = firstName + " " + lastName + "%";
			subcontractorQuery = baseSubcontractorQuery + name + "'";
			System.out.println("******* The subcontractor query for fuel upload - second attempt is " + subcontractorQuery);
			subContractorList = genericDAO.executeSimpleQuery(subcontractorQuery);
		} 
		
		if (!subContractorList.isEmpty()) {
			return subContractorList.get(0);
		} else {
			return null;
		}
	}
	
	private Driver getDriverObjectFromName(Map criterias, String firstName, String lastName, HSSFRow row) {
		
		/*// Existing Buggy code
		criterias.clear();
		System.out.println("Firstname = " + firstName + ", lastname = " + lastName);
		criterias.put("firstName", firstName);
		criterias.put("lastName", lastName);
		Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		
		return driver; */
		
		List<Driver> drivers = getDriversFromName(criterias, lastName, firstName);

		if (drivers.size() == 0) { // no driver, continue to existing flow
			System.out.println("<<Custom code for Driver>>: No driver matching firstname = " + firstName + ", lastname = " + lastName + " was found.");
			return null;
		}
		
		if (drivers.size() == 1) { // exactly 1 matching driver, continue to existing flow
			System.out.println("<<Custom code for Driver>>: Exactly 1 matching driver was found with id = " + drivers.get(0).getId());
			return drivers.get(0);
		}
		
		// more than 1 driver found
		//return retrieveActualDriver(drivers, row);
		String transdate = getTransactionDateFromExcel(row, 4);
		return retrieveActualDriver(drivers, transdate);
	}

	//private Driver retrieveActualDriver(List<Driver> drivers, HSSFRow row) {
	private Driver retrieveActualDriver(List<Driver> drivers, String transdate) {
		//String transdate = getTransactionDateFromExcel(row);
		String listOfDrivers = getCommaSeparatedListOfDriverID(drivers);
		
		Ticket ticket = getTicketForDriver(listOfDrivers, transdate);
		if(ticket != null) { // got matching ticket
			return ticket.getDriver();
		}
		
		DriverFuelLog driverFuelLog = getDriverFuelLogForDriver(listOfDrivers, transdate);
		if(driverFuelLog != null) { // got matching DriverFuelLog 
			return driverFuelLog.getDriver();
		}
		
		Odometer odometer = getOdometerForDriver(listOfDrivers, transdate);
		if(odometer != null) { // got matching Odometer
			return odometer.getDriver();
		}
		
		// could not set using Ticket, DriverFuelLog, Odometer, try to set the driver using Active flag
		// already sorted in desc order of ID
		System.out.println("<<Custom code for Driver>>: Most Recent driver ID = " + drivers.get(0).getId());
		return drivers.get(0);
		//return getRecentDriver(drivers) ;
	}

	private boolean setUnitNumberInFuelLogRefactored(Map criterias, HSSFRow row, FuelLog fuellog, StringBuffer lineError, String unit) {
		boolean isError = false;

		try {
			String lastName = ((String) getCellValue(row.getCell(7)));
			String firstName = ((String) getCellValue(row.getCell(8)));

			// Unit is EMPTY, Driver Name is EMPTY
			if(StringUtils.isEmpty(unit) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(firstName)) {
				isError = true;
				lineError.append("Unit is empty, Driver is empty");
				return isError;
			}
	
			// if unit number is NOT EMPTY
			if(!StringUtils.isEmpty(unit)) {
				String transdate = getTransactionDateFromExcel(row, 4);
				if (!setVehicleInFuelLogFromUnitNumber(row, transdate, fuellog)) {
					isError = true;
					lineError.append("no such Vehicle,");
					return isError;
				} // else : able to set the unit number, proceed down to set driver
			} // else : derive unit number using driver, so first proceed down to set driver
			
			
			// If driverName is NOT EMPTY
			isError = setDriverAndOrUnitInFuelLog(criterias, row, fuellog, lineError, lastName, firstName, unit); 
			
			/*if (StringUtils.isEmpty(unit)) {
				isError = true;
				lineError.append("Unit is blank, No matching Ticket, Fuel Log,  Odometer entry for given driver");
				return isError;
				
			} */
			
		} catch (Exception ex) {
			isError = true;
			lineError.append("Unit,");
			log.warn(ex.getMessage());
		}
		return isError;
	}

	private boolean setDriverAndOrUnitInFuelLog(Map criterias, HSSFRow row, FuelLog fuellog, StringBuffer lineError,
			String lastName, String firstName, String unit) {
		
		boolean isError = false;
		
		List<Driver> driver = new ArrayList<Driver>();
		
		if (!lastName.isEmpty() && !firstName.isEmpty()) { // user has given driver name 
			driver = getDriversFromName(criterias, lastName, firstName);
			
			if (driver.size() == 0) { // No drivers found for given name, do not try to derive name, send error
				isError = true;
				lineError.append("Invalid Driver,");
				return isError ;
			} 
		}
		
		// driver -> can be 0 , 1 , > 1
		return setDriverAndOrUnitUsingNameInFuelLog(criterias, row, fuellog, lineError, lastName, firstName, driver, unit);
	
	}

	private boolean setDriverAndOrUnitUsingNameInFuelLog(Map criterias, HSSFRow row, FuelLog fuellog, StringBuffer lineError, String lastName,
			String firstName, List<Driver> driver, String unit) {
			
		boolean isError = false;
		String listOfDrivers = null;
		// 1 or more drivers found
		if (driver.size() > 0) {
			listOfDrivers = getCommaSeparatedListOfDriverID(driver);
		}
		
		String transdate = getTransactionDateFromExcel(row, 4);
		
		// pass the Stringbuffer lineError -> to capture the right error
		
		if (setFuelLogDetailsUsingTicket(listOfDrivers, transdate, fuellog, unit)) {
			// driver, terminal, unit set using ticket, so return
			return isError;
		}
	
		if (setFuelLogDetailsUsingDriverFuelLog(listOfDrivers, transdate, fuellog, unit)) {
			// driver, terminal, unit set using driverfuellog, so return
			return isError;
		}
		
		if (setFuelLogDetailsUsingOdometer(listOfDrivers, transdate, fuellog, unit)) {
			// driver, terminal, unit set using Odometer, so return
			return isError;
		}
		
		// could not set using Ticket, DriverFuelLog, Odometer, try to set the driver using Active flag
		if (setActiveDriverInFuelLog(criterias, fuellog, lastName, firstName)) {
			// could set the right driver using active flag
			return isError;
		}
		
		isError = true;
		lineError.append("Invalid Driver,");
		return isError;
	}

	private List<Driver> getDriversFromName(String fullName) {
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("fullName", fullName);
		List<Driver> driver = genericDAO.findByCriteria(Driver.class, criterias, "id", true);
		return driver;
	}
	
	private List<Driver> getDriversFromName(Map criterias, String lastName, String firstName) {
		criterias.clear();
		criterias.put("firstName", firstName);
		criterias.put("lastName", lastName);
		List<Driver> driver = genericDAO.findByCriteria(Driver.class, criterias, "id", true);
		return driver;
	}

	private boolean setActiveDriverInFuelLog(Map criterias, FuelLog fuellog, String lastName,
			String firstName) {
		boolean error;
		criterias.clear();
		criterias.put("firstName", firstName);
		criterias.put("lastName", lastName);
		criterias.put("status", 1);
		List<Driver> activeDrivers = genericDAO.findByCriteria(Driver.class, criterias);
		if (activeDrivers.size() == 0) {
			// no drivers found
			return false;
		} else {
			// take first one blindly !! :(
			fuellog.setDriversid(activeDrivers.get(0));
			fuellog.setTerminal(activeDrivers.get(0).getTerminal());
			return true;
		}
	}

	private boolean setVehicleInFuelLogFromUnitNumber(HSSFRow row, String transdate, FuelLog fuelLog) {
		
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
				+ Integer.parseInt((String) getCellValue(row.getCell(6))) + " and obj.validFrom<='"
				+ transdate + "' and obj.validTo>='" + transdate + "'";

		System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList == null || vehicleList.size() == 0) {
			System.out.println("Entered here ");
			return true;
		} else {
			fuelLog.setUnit(vehicleList.get(0));
			return true;
		}
	}

	private Vehicle getVehicleInFuelLogFromUnitNumber(String unit, String transdate) {
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
				+ Integer.parseInt(unit) + " and obj.validFrom<='"
				+ transdate + "' and obj.validTo>='" + transdate + "'";

		System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList == null || vehicleList.size() == 0) {
			return null;
		} else {
			return vehicleList.get(0);
		}
	}
	
	private boolean setFuelLogDetailsUsingOdometer(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		Odometer matchingOdometer = getOdometerForDriver(listOfDrivers, transdate);
		if (matchingOdometer == null) {
			return false;
		}
		fuelLog.setDriversid(matchingOdometer.getDriver());
		fuelLog.setTerminal(matchingOdometer.getTerminal());
		fuelLog.setUnit(matchingOdometer.getTruck());
		return true;
	}

	private Odometer getOdometerForDriver(String listOfDrivers, String transdate) {
		String odometerQuery = "select obj from Odometer obj where "
				+ "obj.recordDate='" + transdate 
				+ "' and obj.driver IN ("
				+ listOfDrivers + ")";
		
		System.out.println("Select Odomoeter with list of drivers -> " + odometerQuery);
		List<Odometer> driverOdometer = genericDAO.executeSimpleQuery(odometerQuery);
		
		if (driverOdometer.size() == 0) {
			return null;
		} 
		
		Odometer matchingOdometer = driverOdometer.get(0);
		System.out.println("<<Custom code for Driver>>: Matching Odometer = " + matchingOdometer.getId());
		return matchingOdometer;
	}

	private boolean setFuelLogDetailsUsingDriverFuelLog(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		DriverFuelLog matchingDriverFuelLog = getDriverFuelLogForDriver(listOfDrivers, transdate);
		if (matchingDriverFuelLog == null) {
			return false;
		}
		fuelLog.setDriversid(matchingDriverFuelLog.getDriver());
		fuelLog.setTerminal(matchingDriverFuelLog.getTerminal());
		fuelLog.setUnit(matchingDriverFuelLog.getTruck());
		
		return true;
	}

	private DriverFuelLog getDriverFuelLogForDriver(String listOfDrivers, String transdate) {
		String driveFuelLogquery = "select obj from DriverFuelLog obj where  "
				+ "obj.transactionDate='" + transdate 
				+ "' and obj.driver IN ("
				+ listOfDrivers + ")";
		
		System.out.println("Select DriverFuelLog with list of drivers -> " + driveFuelLogquery);
		List<DriverFuelLog> driverFuelLog = genericDAO.executeSimpleQuery(driveFuelLogquery);
		
		if (driverFuelLog.size() == 0) {
			return null; // could not set
		} 
		
		DriverFuelLog matchingDriverFuelLog = driverFuelLog.get(0);
		System.out.println("<<Custom code for Driver>>: Matching DriverFuelLog ID = " + matchingDriverFuelLog.getId());
		return matchingDriverFuelLog;
	}

	private boolean setFuelLogDetailsUsingTicket(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		
		if (StringUtils.isEmpty(listOfDrivers)) {
			// retrieve and set using unit
			return setFuelLogUsingUnit(listOfDrivers, transdate, fuelLog, unit);
		} else {
			return setFuelLogUsingDriver(listOfDrivers, transdate, fuelLog, unit);
		}
	}

	private boolean setFuelLogUsingUnit(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		
		Vehicle vehicle = getVehicleInFuelLogFromUnitNumber(unit, transdate);
		String driverquery = "select obj from Ticket obj where "
				+ "(obj.loadDate ='" + transdate + "' OR obj.unloadDate ='" + transdate
				+ "') and obj.vehicle=" + vehicle.getId();
		System.out.println("******** query is " + driverquery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(driverquery);
		if (tickets.size() == 0) { // no matching tickets found
			return false;
		}
		
		// set Driver, Terminal, Vehicle
		Ticket matchingTicket = tickets.get(0);
		fuelLog.setDriversid(matchingTicket.getDriver());
		fuelLog.setTerminal(matchingTicket.getTerminal());
		fuelLog.setUnit(matchingTicket.getVehicle());
		
		return false;
	}

	private boolean setFuelLogUsingDriver(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		Ticket matchingTicket = getTicketForDriver(listOfDrivers, transdate);
		if (matchingTicket == null) {
			return false;
		}
		
		fuelLog.setDriversid(matchingTicket.getDriver());
		fuelLog.setTerminal(matchingTicket.getTerminal());
		
		if (!StringUtils.isEmpty(unit)) {
			// validate if ticket.unit == unit
			System.out.println("Unit number does not match the unit number retrieved through ticket = " + matchingTicket.getVehicle().getUnitNum());
			return false;
		}
		
		fuelLog.setUnit(matchingTicket.getVehicle());
		return true;
	}
	
	// More than one driver fix - 13th May 2016
	private int calculateDuration(String startTime, String endTime) {
		if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
			return 0;
		}
		
		DateTimeFormatter durationFormat = DateTimeFormat.forPattern("HH:mm");
		
		DateTime dtStart = durationFormat.parseDateTime(startTime);
	   DateTime dtStop = durationFormat.parseDateTime(endTime);
	   if (dtStop.isBefore(dtStart)) {
	   	DateTime temp = dtStart;
	   	dtStart = dtStop;
	   	dtStop = temp;
	   }
	
	   /*System.out.print(Days.daysBetween(dtStart, dtStop).getDays() + " days, ");
	   System.out.print(Hours.hoursBetween(dtStart, dtStop).getHours() % 24 + " hours, ");
	   System.out.print(org.joda.time.Minutes.minutesBetween(dtStart, dtStop).getMinutes() % 60 + " minutes, ");
	   System.out.println(Seconds.secondsBetween(dtStart, dtStop).getSeconds() % 60 + " seconds.");*/
	   
	   int hours = Hours.hoursBetween(dtStart, dtStop).getHours() % 24;
	   int mins = org.joda.time.Minutes.minutesBetween(dtStart, dtStop).getMinutes() % 60;
	   return mins + (hours * 60);
	}
	
	// More than one driver fix - 13th May 2016
	private List<Ticket> determineCorrectTicket(List<Ticket> ticketsList, Date txnDate, String txnTime) {
		if (ticketsList.isEmpty()) {
			return ticketsList;
		}
		
		if (ticketsList.size() == 1) {
			return ticketsList;
		}
		
		List<Long> driverIdList = new ArrayList<Long>();
		for (Ticket aTicket : ticketsList) {
			if (!driverIdList.contains(aTicket.getDriver().getId())) {
				driverIdList.add(aTicket.getDriver().getId());
			}
		}
		
		if (driverIdList.size() == 1) {
			return ticketsList;
		}
		
		Map<Integer, Ticket> ticketDurationMap = new HashMap<Integer, Ticket>();
		for (Ticket aTicket : ticketsList) {
			List<Integer> durationsList = new ArrayList<Integer>();
			
			if (DateUtils.isSameDay(txnDate, aTicket.getLoadDate()) ) {
				durationsList.add(calculateDuration(aTicket.getTransferTimeIn(), txnTime));
				durationsList.add(calculateDuration(aTicket.getTransferTimeOut(), txnTime));
				
			}
			
			if (DateUtils.isSameDay(txnDate, aTicket.getUnloadDate()) ) {
				durationsList.add(calculateDuration(aTicket.getLandfillTimeIn(), txnTime));
				durationsList.add(calculateDuration(aTicket.getLandfillTimeOut(), txnTime));
			}
			
			if (!durationsList.isEmpty()) {
				Object[] sortedDurations = durationsList.toArray();
				Arrays.sort(sortedDurations);
				ticketDurationMap.put((Integer)sortedDurations[0], aTicket);
			}
		}
		
		List<Ticket> correctTicketsList = new ArrayList<Ticket>();
		if (!ticketDurationMap.isEmpty()) {
			Integer[] finalDurations = ticketDurationMap.keySet().toArray(new Integer[0]);
			Arrays.sort(finalDurations);
			correctTicketsList.add(ticketDurationMap.get(finalDurations[0]));
		}
		
		return correctTicketsList;
	}
	
	private List<Ticket> getTicketsForVehicle(List<Vehicle> vehicleList, String transDateStr) throws ParseException {
		String vehicleIds = getCommaSeparatedListOfVehicleId(vehicleList);
		
		String ticketQuery = "select obj from Ticket obj where "
				+ "(obj.loadDate ='" + transDateStr
				+ "' OR obj.unloadDate ='" + transDateStr 
				+ "') and obj.vehicle IN ("
				+ vehicleIds + ")";
		System.out.println("<<Custom code for Vehicle>>: Select Ticket with list of vehicles query 1-> " + ticketQuery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		if (!tickets.isEmpty()) {
			return tickets;
		}
		
		Date transDate = dateFormat.parse(transDateStr);
		//Date transDateMin = DateUtils.addDays(transDate, -3);
		Date transDateMin = DateUtils.addDays(transDate, 0);
		Date transDateMax = DateUtils.addDays(transDate, 4);
		String transDateMinStr = dateFormat.format(transDateMin);
		String transDateMaxStr = dateFormat.format(transDateMax);
		
		ticketQuery = "select obj from Ticket obj where "
				+ "( (obj.loadDate between '" + transDateMinStr + "' and '" + transDateMaxStr + "')"
				+ "   OR (obj.unloadDate between '" + transDateMinStr + "' and '" + transDateMaxStr + "')" 
				+ ") and obj.vehicle IN ("
				+ vehicleIds + ")";
		System.out.println("<<Custom code for Vehicle>>: Select Ticket with list of vehicles query 2-> " + ticketQuery);
		tickets = genericDAO.executeSimpleQuery(ticketQuery);
		return tickets;
	}

	private String getCommaSeparatedListOfVehicleId(List<Vehicle> vehicleList) {
		StringBuffer vehicleIdBuff = new StringBuffer();
		for (Vehicle v : vehicleList) {
			vehicleIdBuff.append("," + v.getId());
		}
		
		return vehicleIdBuff.toString().replaceFirst(",", "");
	}
	
	private List<Ticket> getAllTicketsForDriver(String listOfDrivers, String transDateStr) throws ParseException {
		String ticketQuery = "select obj from Ticket obj where "
				+ "(obj.loadDate ='" + transDateStr
				+ "' OR obj.unloadDate ='" + transDateStr 
				+ "') and obj.driver IN ("
				+ listOfDrivers + ")";
		System.out.println("<<Custom code for Driver>>: Select Ticket with list of drivers query 1-> " + ticketQuery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery);
		if (!tickets.isEmpty()) {
			return tickets;
		}
		
		Date transDate = dateFormat.parse(transDateStr);
		//Date transDateMin = DateUtils.addDays(transDate, -3);
		Date transDateMin = DateUtils.addDays(transDate, 0);
		Date transDateMax = DateUtils.addDays(transDate, 4);
		String transDateMinStr = dateFormat.format(transDateMin);
		String transDateMaxStr = dateFormat.format(transDateMax);
		
		ticketQuery = "select obj from Ticket obj where "
				+ "( (obj.loadDate between '" + transDateMinStr + "' and '" + transDateMaxStr + "')"
				+ "   OR (obj.unloadDate between '" + transDateMinStr + "' and '" + transDateMaxStr + "')" 
				+ ") and obj.driver IN ("
				+ listOfDrivers + ")";
		System.out.println("<<Custom code for Driver>>: Select Ticket with list of drivers query 2-> " + ticketQuery);
		tickets = genericDAO.executeSimpleQuery(ticketQuery);
		return tickets;
	}

	private Ticket getTicketForDriver(String listOfDrivers, String transdate) {
		String driverquery = "select obj from Ticket obj where "
				+ "(obj.loadDate ='" + transdate
				+ "' OR obj.unloadDate ='" + transdate 
				+ "') and obj.driver IN ("
				+ listOfDrivers + ")";
		System.out.println("<<Custom code for Driver>>: Select Ticket with list of drivers -> " + driverquery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(driverquery);
		
		if (tickets.size() == 0) { // no matching tickets found
			return null;
		} 
		
		Ticket matchingTicket = tickets.get(0);
		System.out.println("<<Custom code for Driver>>: Matching Ticket ID = " + matchingTicket.getId());
		return matchingTicket;
	}

	private String getCommaSeparatedListOfDriverID(List<Driver> drivers) {
		StringBuffer driverIdList = new StringBuffer();
		for ( Driver d : drivers) {
			driverIdList.append("," + d.getId());
		}
		
		return driverIdList.toString().replaceFirst(",", "");
	}

	private String getTransactionDateFromExcel(HSSFRow row, int colIndex) {
		String transdate = null;
		if (validDate(getCellValue(row.getCell(4)))) {
			transdate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
		}
		
		System.out.println("<<Custom code for Driver>>: Transaction date = " + transdate);
		return transdate;
	}

	private boolean handleExcludedCardNumberChecks(FuelLog fuellog, String cardNo) {
		if (!StringUtils.equalsIgnoreCase("EXCLUDE_ERROR_CHECK", cardNo)) {
			return false;
		}
		
		System.out.println("\nOVerride card Number\n");
		FuelCard fuelCard = null;
		fuellog.setFuelcard(fuelCard);
		return true;
	}
	
	private boolean validateFuelCardForVehicle(FuelLog fuelLog, FuelCard fuelCard) {
		if (fuelLog.getUnit() == null || fuelLog.getUnit().getId() == null
				|| fuelLog.getFuelvendor() == null || fuelLog.getFuelvendor().getId() == null
				|| fuelCard == null || fuelCard.getId() == null) {
			return false;
		}
		
		String fuelCardVehicleQuery = "select obj from DriverFuelCard obj where "
				+ " obj.vehicle =" + fuelLog.getUnit().getId()
						+ " and obj.fuelvendor =" + fuelLog.getFuelvendor().getId()
						+ " and obj.fuelcard =" +  fuelCard.getId();
		System.out.println("********Fuel card vehicle query is " + fuelCardVehicleQuery);
		List<DriverFuelCard> driverFuelCardList = genericDAO.executeSimpleQuery(fuelCardVehicleQuery);
		
		return (driverFuelCardList != null && !driverFuelCardList.isEmpty());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importMainSheet(InputStream is) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance
		System.out.println("***** Here step 2");
		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<Ticket> tickets = new ArrayList<Ticket>();
		// List<String> emptydatalist=new ArrayList<String>();
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();
			// FileWriter writer = new FileWriter("e:/errordata.txt");
			wb = new HSSFWorkbook(fs);
			// loop for every worksheet in the workbook
			int numOfSheets = wb.getNumberOfSheets();
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			Ticket ticket = null;
			Iterator rows = sheet.rowIterator();
			StringBuffer lineError;
			while (rows.hasNext()) {
				boolean error = false;
				buffer = new StringBuffer();
				int cellCount = 0;
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				lineError = new StringBuffer("");
				try {
					ticket = new Ticket();
					ticket.setTicketStatus(1);
					ticket.setPayRollStatus(1);

					Object loadDateObj = getCellValue(row.getCell(0), true);
					if (loadDateObj == null) {
						error = true;
						lineError.append("Load Date,");
					} else if (loadDateObj instanceof Date) {
						ticket.setLoadDate((Date)loadDateObj);
					} else {
						String loadDateStr = loadDateObj.toString();
						loadDateStr = StringUtils.trimToEmpty(loadDateStr);
						Date loadDate = sdf.parse(loadDateStr);
						ticket.setLoadDate(loadDate);
					}
					/*try {
						Date loadDate = sdf.parse((String) getCellValue(row.getCell(0), true));
						ticket.setLoadDate(loadDate);
					} catch (ParseException p) {
						error = true;
						lineError.append("Load Date,");
					}*/
					
					/*if (validDate(getCellValue(row.getCell(0))))
						ticket.setLoadDate((Date) getCellValue(row.getCell(0)));
					else {
						error = true;
						lineError.append("Load Date,");
					}*/
					if (validTime((String) getCellValue(row.getCell(1)))) {
						StringBuilder timeIn = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(1)), 4, '0'));
						timeIn.insert(2, ':');
						ticket.setTransferTimeIn(timeIn.toString());
					} else {
						error = true;
						lineError.append("Transfer Time In,");
					}
					if (validTime((String) getCellValue(row.getCell(2)))) {
						StringBuilder timeOut = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(2)), 4, '0'));
						timeOut.insert(2, ':');
						ticket.setTransferTimeOut(timeOut.toString());
					} else {
						error = true;
						lineError.append("Transfer Time Out,");
					}
					try {
						criterias.clear();
						criterias.put("type", 1);
						criterias.put("unit", Integer.parseInt((String) getCellValue(row.getCell(3))));
						Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
						if (vehicle == null)
							throw new Exception("no such truck");
						else
							ticket.setVehicle(vehicle);
					} catch (Exception ex) {
						error = true;
						lineError.append("Truck,");
						log.warn(ex.getMessage());
					}
					try {
						criterias.clear();
						criterias.put("type", 2);
						criterias.put("unit", Integer.parseInt((String) getCellValue(row.getCell(4))));
						Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
						if (vehicle == null)
							throw new Exception("no such trailer");
						else
							ticket.setTrailer(vehicle);
					} catch (Exception ex) {
						error = true;
						lineError.append("Trailer,");
						log.warn(ex.getMessage());
					}
					
					Object unloadDateObj = getCellValue(row.getCell(5), true);
					if (unloadDateObj == null) {
						error = true;
						lineError.append("Unload Date,");
					} else if (unloadDateObj instanceof Date) {
						ticket.setUnloadDate((Date)unloadDateObj);
					} else {
						String unloadDateStr = unloadDateObj.toString();
						unloadDateStr = StringUtils.trimToEmpty(unloadDateStr);
						Date unloadDate = sdf.parse(unloadDateStr);
						ticket.setUnloadDate(unloadDate);
					}
					/*try {
						Date unloadDate = sdf.parse((String) getCellValue(row.getCell(5)));
						ticket.setUnloadDate(unloadDate);
					} catch (ParseException p) {
						error = true;
						lineError.append("Unload Date,");
					}*/
					/*Object unloadDate = getCellValue(row.getCell(5));
					if (validDate(unloadDate))
						ticket.setUnloadDate((Date) unloadDate);
					else {
						error = true;
						lineError.append("" + " Date,");
					}*/
					if (validTime((String) getCellValue(row.getCell(6)))) {
						StringBuilder timeIn = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(6)), 4, '0'));
						timeIn.insert(2, ':');
						ticket.setLandfillTimeIn(timeIn.toString());
					} else {
						error = true;
						lineError.append("Landfill Time In,");
					}
					if (validTime((String) getCellValue(row.getCell(7)))) {
						StringBuilder timeOut = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(7)), 4, '0'));
						timeOut.insert(2, ':');
						ticket.setLandfillTimeOut(timeOut.toString());
					} else {
						error = true;
						lineError.append("Landfill Time Out,");
					}
					try {
						criterias.clear();
						criterias.put("type", 1);
						criterias.put("name", (String) getCellValue(row.getCell(8)));
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null)
							throw new Exception("no such origin");
						else
							ticket.setOrigin(location);
					} catch (Exception ex) {
						error = true;
						lineError.append("Origin,");
						// log.warn(ex.getMessage());
					}
					try {
						ticket.setOriginTicket(Long.parseLong((String) getCellValue(row.getCell(9))));
					} catch (Exception ex) {
						error = true;
						lineError.append("Origin Ticket,");
					}
					try {
						criterias.clear();
						criterias.put("type", 2);
						criterias.put("name", (String) getCellValue(row.getCell(10)));
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null)
							throw new Exception("no such destination");
						else
							ticket.setDestination(location);
					} catch (Exception ex) {
						error = true;
						lineError.append("Destination,");
						// log.warn(ex.getMessage());
					}

					// FOR CUSTOMER AND COMPANY_LACATION
					BillingRate billingRate = null;
					try {
						criterias.clear();
						criterias.put("type", 1);
						criterias.put("name", (String) getCellValue(row.getCell(8)));
						Location originid = genericDAO.getByCriteria(Location.class, criterias);

						criterias.clear();
						criterias.put("type", 2);
						criterias.put("name", (String) getCellValue(row.getCell(10)));
						Location destinationid = genericDAO.getByCriteria(Location.class, criterias);

						if (originid != null && destinationid != null) {
							// BillingRate billingRate = null;
							String query = "select obj from BillingRate obj where transferStation=" + originid.getId()
									+ " and landfill=" + destinationid.getId();
							List<BillingRate> rates = genericDAO.executeSimpleQuery(query);

							for (BillingRate rate : rates) {
								if (rate.getRateUsing() == null) {
									billingRate = rate;
									break;
								} else if (rate.getRateUsing() == 1) {
									// calculation for a load date
									if ((ticket.getLoadDate().getTime() >= rate.getValidFrom().getTime())
											&& (ticket.getLoadDate().getTime() <= rate.getValidTo().getTime())) {
										billingRate = rate;
										break;
									}
								} else if (rate.getRateUsing() == 2) {
									// calculation for a unload date
									if ((ticket.getUnloadDate().getTime() >= rate.getValidFrom().getTime())
											&& (ticket.getUnloadDate().getTime() <= rate.getValidTo().getTime())) {
										billingRate = rate;
										break;
									}
								}
							}
							if (billingRate != null) {
								ticket.setCompanyLocation((billingRate.getCompanyLocation() != null)
										? billingRate.getCompanyLocation() : null);
								ticket.setCustomer(
										(billingRate.getCustomername() != null) ? billingRate.getCustomername() : null);
							} else {
								System.out.println("Customer and Company Location");

							}
							/*
							 * { error = true; lineError.append(
							 * "Rate is expired for this origin and destination,please contact to administrator,"
							 * ); }
							 */
						}

					} catch (Exception ex) {
						System.out.println("Customer and Company Location");
						log.warn(ex.getMessage());
					}

					try {
						ticket.setDestinationTicket(Long.parseLong((String) getCellValue(row.getCell(11))));
					} catch (Exception ex) {
						error = true;
						lineError.append("Destination Ticket,");
					}

					if (ticket.getOrigin() != null) {
						if (reportService.checkDuplicate(ticket, "O")) {
							lineError.append("Duplicate Origin Ticket,");
							error = true;
						}
					}

					if (ticket.getDestination() != null) {
						if (reportService.checkDuplicate(ticket, "D")) {
							lineError.append("Duplicates Dest. Ticket,");
							error = true;
						}

					}

					if (ticket.getUnloadDate() != null && ticket.getLoadDate() != null) {
						if (ticket.getUnloadDate().before(ticket.getLoadDate())) {
							lineError.append("Unload Date is before Load Date,");
							error = true;
						}
					}
					Double tgross = getValidAmount((String) getCellValue(row.getCell(12)));
					if (tgross != null)
						ticket.setTransferGross(tgross);
					else {
						error = true;
						lineError.append("Transfer Gross,");
					}
					Double ttare = getValidAmount((String) getCellValue(row.getCell(13)));
					if (ttare != null)
						ticket.setTransferTare(ttare);
					else {
						lineError.append("Transfer Tare,");
						error = true;
					}
					if (tgross != null && ttare != null) {
						ticket.setTransferNet(tgross - ttare);
						ticket.setTransferTons((tgross - ttare) / 2000);
						/* if(billingRate.getBilledby().equals("bygallon")){ */
					// Change to 8.35 - 28th Dec 2016
						ticket.setGallons(ticket.getTransferNet() / 8.35);
						// }
					}
					Double lgross = getValidAmount((String) getCellValue(row.getCell(16)));
					if (lgross != null)
						ticket.setLandfillGross(lgross);
					else {
						error = true;
						lineError.append("Landfill Gross,");
					}
					Double ltare = getValidAmount((String) getCellValue(row.getCell(17)));
					if (ltare != null)
						ticket.setLandfillTare(ltare);
					else {
						lineError.append("Landfill Tare,");
						error = true;
					}
					if (lgross != null && ltare != null) {
						ticket.setLandfillNet(lgross - ltare);
						ticket.setLandfillTons((lgross - ltare) / 2000);
					}
					String driverName = ((String) getCellValue(row.getCell(21)));
					Driver driver = null;
					try {
						if (StringUtils.isEmpty(driverName))
							throw new Exception("Invalid driver");
						else {
							// String[] names = driverName.split(" ");
							criterias.clear();
							/*
							 * criterias.put("firstName", names[1]);
							 * criterias.put("lastName", names[0]);
							 */
							criterias.put("status", 1);
							criterias.put("fullName", driverName);
							driver = genericDAO.getByCriteria(Driver.class, criterias);
							if (driver == null)
								throw new Exception("Invalid driver");
							ticket.setDriver(driver);
							// ticket.setDriverCompany(driver.getCompany());
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("Driver,");
					}

					try {
						String employeeCompanyName = ((String) getCellValue(row.getCell(23)));
						if (StringUtils.isEmpty(employeeCompanyName))
							throw new Exception("Invalid company");
						else {
							criterias.clear();
							criterias.put("status", 1);
							criterias.put("name", employeeCompanyName);
							Location employeeCompany = genericDAO.getByCriteria(Location.class, criterias);
							if (employeeCompany == null)
								throw new Exception("Invalid company");
							ticket.setDriverCompany(employeeCompany);
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("employee company,");
					}

					String subcontractor = ((String) getCellValue(row.getCell(20)));
					try {
						if (!StringUtils.isEmpty(subcontractor)) {
							/*
							 * if (driver != null && !"Subcontractor"
							 * .equalsIgnoreCase(driver .getLastName().trim()))
							 * { throw new Exception("Invalid subcontractor"); }
							 * 
							 * 
							 * else {
							 */
							//String subcontractorMod = subcontractor.replace("&", "\"&\"");
							criterias.clear();
							criterias.put("name", subcontractor);
							SubContractor contractor = genericDAO.getByCriteria(SubContractor.class, criterias);
							if (contractor == null) {
								throw new Exception("Invalid subcontractor");
							} else {
								ticket.setSubcontractor(contractor);
							}
							criterias.clear();
						}
						// }
					} catch (Exception ex) {
						error = true;
						lineError.append("Sub Contractor,");
					}
					
					Object billbatchDateObj = getCellValue(row.getCell(22), true);
					if (billbatchDateObj == null) {
						error = true;
						lineError.append("Bill Batch Date,");
					} else if (billbatchDateObj instanceof Date) {
						ticket.setBillBatch((Date)billbatchDateObj);
					} else {
						String billbatchDateStr = billbatchDateObj.toString();
						billbatchDateStr = StringUtils.trimToEmpty(billbatchDateStr);
						Date billbatchDate = sdf.parse(billbatchDateStr);
						ticket.setBillBatch(billbatchDate);
					}
					/*try {
						Date billBatch = sdf.parse((String) getCellValue(row.getCell(22)));
						ticket.setBillBatch(billBatch);
					} catch (ParseException p) {
						error = true;
						lineError.append("Batch Date,");
					}*/
					
					/*Object billBatch = getCellValue(row.getCell(22));
					if (validDate(billBatch))
						ticket.setBillBatch((Date) billBatch);
					else {
						error = true;
						lineError.append("Batch Date,");
					}*/
					try {
						criterias.clear();
						String locCode = (String) getCellValue(row.getCell(24));
						if (StringUtils.isEmpty(locCode))
							throw new Exception("Invalid terminal");
						else {
							criterias.put("code", locCode);
							criterias.put("type", 4);
						}
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null) {
							throw new Exception("no such terminal");
						} else {
							criterias.clear();
							criterias.put("status", 1);
							criterias.put("fullName", driverName);
							criterias.put("terminal", location);
							Driver driverobj = genericDAO.getByCriteria(Driver.class, criterias);
							if (driverobj == null) {
								throw new Exception("Terminal does not match with driver");
							} else {
								ticket.setTerminal(location);
							}
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("Terminal does not match with driver,");
						log.warn(ex.getMessage());
					}
					try {
						User user = genericDAO.getByUniqueAttribute(User.class, "username",
								(String) getCellValue(row.getCell(25)));
						if (user == null) {
							throw new Exception("Invalid user");
						} else {
							ticket.setCreatedBy(user.getId());
							ticket.setEnteredBy(user.getName());
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("User,");
					}
					if (!error) {
						if (tickets.contains(ticket)) {
							lineError.append("Duplicate Ticket,");
							error = true;
							errorcount++;
						} else {
							tickets.add(ticket);
							//genericDAO.saveOrUpdate(ticket);
						}
					} else {
						errorcount++;
					}

				} catch (Exception ex) {
					error = true;
					log.warn(ex);
				}
				if (lineError.length() > 0) {
					System.out.println("Error :" + lineError.toString());
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				System.out.println("Record No :" + count);
				count++;
			}
		} catch (Exception e) {
			log.warn("Error in import customer :" + e);
		}
		if (errorcount == 0) {
			for (Ticket ticket : tickets) {
				genericDAO.saveOrUpdate(ticket);
			}
		}
		return list;
	}

	/**
	 * This is a helper method to retrieve the value of a cell regardles of its
	 * type, which will be converted into a String.
	 * 
	 * @param cell
	 * @return
	 */
	private Object getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		Object result = null;
		int cellType = cell.getCellType();
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? Boolean.TRUE : Boolean.FALSE;
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			
			result = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			HSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			// assumption is made that dataFormat = 14,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			if (dataFormat == 164) {
				result = cell.getDateCellValue();
			} else {
				result = cell.getNumericCellValue();
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}
		if (result instanceof Double) {
			return String.valueOf(((Double) result).longValue());
		}
		if (result instanceof Date) {
			return result;
		}
		return result.toString();
	}

	private Object getCellValue(HSSFCell cell, boolean resolveFormula) {
		if (cell == null) {
			return null;
		}
		Object result = null;
		int cellType = cell.getCellType();
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? Boolean.TRUE : Boolean.FALSE;
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			
			switch(cell.getCachedFormulaResultType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                System.out.println("Last evaluated as: " + cell.getNumericCellValue());
                result = cell.getNumericCellValue();
                break;
            case HSSFCell.CELL_TYPE_STRING:
                System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                result = cell.getRichStringCellValue();
                break;
			}
			
			//result = cell.getCellFormula();
			
			
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			HSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			System.out.println("Data format for " + cell.getColumnIndex() + " = " + dataFormat);
			// assumption is made that dataFormat = 14,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			//if (dataFormat == 165 || dataFormat == 164 || dataFormat == 14) {
			if (DateUtil.isCellDateFormatted(cell)) {
				result = cell.getDateCellValue();
			} else {
				result = cell.getNumericCellValue();
			}
			
			if (dataFormat == 0) { // alternative way of getting value : can this be replaced for the entire block
				result = new HSSFDataFormatter().formatCellValue(cell);
			}
			System.out.println("Numeric cell value == " + result);
			
			break;
		case HSSFCell.CELL_TYPE_STRING:
			//result = cell.getStringCellValue();
			result = cell.getRichStringCellValue();
			System.out.println("String -> " + result);
			break;
		default:
			break;
		}
		
		if (result instanceof Integer) {
			return String.valueOf((Integer) result);
		} else if (result instanceof Double) {
			return String.valueOf(((Double) result)); //.longValue());
		}
		if (result instanceof Date) {
			return result;
		}
		return result.toString();
	}

	
	private boolean validTime(String cellValue) {
		if (StringUtils.isEmpty(cellValue))
			return false;
		if (cellValue.length() > 4)
			return false;
		return true;
	}

	private boolean validDate(Object cellValue) {
		if (cellValue instanceof Date)
			return true;
		else
			return false;
	}

	private Double getValidAmount(String cellValue) {

		if (StringUtils.isEmpty(cellValue) || cellValue.length() > 5)
			return null;
		try {
			return Double.parseDouble(cellValue);

		} catch (Exception ex) {
			return null;
		}
	}

	private Double getValidGallon(String cellValue) {

		if (StringUtils.isEmpty(cellValue))
			return null;
		try {
			return Double.parseDouble(cellValue);

		} catch (Exception ex) {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importVehiclePermitMainSheet(InputStream is) {

		List<String> list = new ArrayList<String>();
		List<VehiclePermit> vehiclePermits = new ArrayList<VehiclePermit>();
		int errorcount = 0;
		HSSFWorkbook wb = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			Iterator rows = sheet.rowIterator();
			VehiclePermit vehiclePermit = null;
			int count = 1;
			while (rows.hasNext()) {
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				boolean error = false;
				StringBuffer lineError = new StringBuffer();
				vehiclePermit = new VehiclePermit();

				String unitNum = "";
				try {
					unitNum = (String) getCellValue(row.getCell(0));
					if (!StringUtils.isEmpty(unitNum)) {
						criterias.clear();
						criterias.put("unitNum", unitNum);
						Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
						if (vehicle == null) {
							error = true;
							lineError.append("Invalid Vehicle Number, ");
						} else {
							vehiclePermit.setVehicle(vehicle);
						}
					} else {
						error = true;
						lineError.append("Vehicle Number is Empty, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Unit,");
					log.warn(e.getMessage());
				}

				String company = "";
				try {
					company = (String) getCellValue(row.getCell(1));
					if (!StringUtils.isEmpty(company)) {
						criterias.clear();
						criterias.put("name", company);
						criterias.put("type", 3);
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null) {
							error = true;
							lineError.append("Invalid Company Name, ");
						} else {
							vehiclePermit.setCompanyLocation(location);
						}
					} else {
						error = true;
						lineError.append("Company is Empty, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Company, ");
					log.warn(e.getMessage());
				}

				String permitType = "";
				try {
					permitType = (String) getCellValue(row.getCell(2));
					if (!StringUtils.isEmpty(permitType)) {
						criterias.clear();
						criterias.put("dataType", "PERMIT_TYPE");
						criterias.put("dataLabel", permitType);
						SetupData setUpdata = genericDAO.getByCriteria(SetupData.class, criterias);
						if (setUpdata == null) {
							error = true;
							lineError.append("Permit Type doesn't exists,");
						} else {
							SetupData data = genericDAO.getById(SetupData.class, setUpdata.getId());
							vehiclePermit.setPermitType(data);
						}
					} else {
						error = true;
						lineError.append("Permit Type is Empty, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Permit Type, ");
					log.warn(e.getMessage());
				}

				String permitNumber = "";
				try {
					permitNumber = (String) getCellValue(row.getCell(3));
					vehiclePermit.setPermitNumber(permitNumber);
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Permit Number, ");
					log.warn(e.getMessage());
				}

				Date date4 = row.getCell(4).getDateCellValue();
				try {
					if (validDate(date4)) {
						vehiclePermit.setIssueDate(dateFormat1.parse(dateFormat1.format(date4)));
					} else {
						error = true;
						lineError.append("Invalid Effective date, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Invalid Effective Date, ");
					log.warn(e.getMessage());
				}

				Date date5 = row.getCell(5).getDateCellValue();
				try {
					if (validDate(date5)) {
						vehiclePermit.setExpirationDate(dateFormat1.parse(dateFormat1.format(date5)));
					} else {
						error = true;
						lineError.append("Invalid Expiration Date");
					}
				} catch (Exception e) {
					error = true;
					e.printStackTrace();
					lineError.append("Invalid Expiration Date, ");
					log.warn(e.getMessage());
				}

				if (!error) {

					java.sql.Date issueDate = new java.sql.Date(vehiclePermit.getIssueDate().getTime());
					java.sql.Date expirationDate = new java.sql.Date(vehiclePermit.getExpirationDate().getTime());

					criterias.clear();
					criterias.put("dataType", "PERMIT_TYPE");
					criterias.put("dataLabel", permitType);
					SetupData setupData = genericDAO.getByCriteria(SetupData.class, criterias);
					String query;
					try {
						query = "select obj from VehiclePermit obj " + " where obj.vehicle.unitNum='" + unitNum + "'"
								+ " and obj.companyLocation.name='" + company + "' "
								+ " and obj.companyLocation.type=3 " + " and obj.permitType=" + setupData.getId() + " "
								+ " and (('" + issueDate + " 00:00:00' BETWEEN obj.issueDate and obj.expirationDate)"
								+ " or ('" + expirationDate + " 00:00:00' BETWEEN obj.issueDate and obj.expirationDate)"
								+ " or obj.issueDate>='" + issueDate + " 00:00:00' AND obj.expirationDate<='"
								+ expirationDate + " 00:00:00')";
						// +" and obj.issueDate='"+dateFormat1.format(date4)+"'
						// "
						// +" and
						// obj.expirationDate='"+dateFormat1.format(date5)+"'";

						List<VehiclePermit> duplicateCheck = genericDAO.executeSimpleQuery(query);
						if (duplicateCheck != null && duplicateCheck.size() > 0) {
							lineError.append("Vehicle Permit Already exists for specified date range, ");
							errorcount++;
						}
						/*
						 * else{
						 * 
						 * genericDAO.save(vehiclePermit); }
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}

					vehiclePermits.add(vehiclePermit);

				} else {
					errorcount++;
				}
				if (lineError.length() > 0) {
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			list.add("Not able to upload the file, please try again");
		}

		if (errorcount == 0) {
			for (VehiclePermit vPermit : vehiclePermits) {
				genericDAO.save(vPermit);
			}
		}
		return list;
	}

	@Override
	public List<LinkedList<Object>> importVendorSpecificFuelLog(InputStream is,
			LinkedHashMap<String, String> vendorSpecificColumns, Long vendor, HashMap<String, Object> additionalVendorData) throws Exception {
		List<LinkedList<Object>> data = new ArrayList<LinkedList<Object>>();
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);

			HSSFWorkbook wb = new HSSFWorkbook(fs);
			Sheet sheet = wb.getSheetAt(0);
			Row titleRow = sheet.getRow(sheet.getFirstRowNum());

			LinkedHashMap<String, Integer> orderedColIndexes = getOrderedColumnIndexes(titleRow, vendorSpecificColumns);
			Set<Entry<String, Integer>> keySet = orderedColIndexes.entrySet();
			
			System.out.println("Physical number of rows in Excel = " + sheet.getPhysicalNumberOfRows());
			System.out.println("While reading values from vendor specific Excel Sheet: ");

			Map criterias = new HashMap();
			criterias.put("id", vendor);
			FuelVendor fuelVendor = genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false).get(0);
			
			boolean stopParsing = false;
			for (int i = titleRow.getRowNum() + 1; !stopParsing && i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
				LinkedList<Object> rowObjects = new LinkedList<Object>();
				
				rowObjects.add(fuelVendor.getName());
				rowObjects.add(fuelVendor.getCompany().getName());
				
				Row row = sheet.getRow(i);

				Iterator<Entry<String, Integer>> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					Entry<String, Integer> entry = iterator.next();

					// corresponding column not found in actual column list, find in additionalVendorData
					if (entry.getValue() == -1) {
						System.out.println("Additional vendor data = " + additionalVendorData);
						System.out.println("Column " + entry.getKey() + " not found in Vendor Excel, checking in additionalVendorData");
						Object cellValueObj = additionalVendorData.get(entry.getKey());
						if(cellValueObj != null) {
							rowObjects.add(cellValueObj);
						} else {
							rowObjects.add(StringUtils.EMPTY); 
						}
						continue;
					}
					
					Object cellValueObj = getCellValue((HSSFCell) row.getCell(entry.getValue()), true);
					if (cellValueObj != null && cellValueObj.toString().equalsIgnoreCase("END_OF_DATA")) {
						System.out.println("Received END_OF_DATA");
						stopParsing = true;
						rowObjects.clear();
						break;
					}
					rowObjects.add(cellValueObj);
				}

				if (!stopParsing) {
					data.add(rowObjects);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return data;
	}
	
	@Override
	public List<LinkedList<Object>> importTollCompanySpecificTollTag(InputStream is,
			LinkedHashMap<String, String> tollCompanySpecificColumns, Long tollCompanyId) throws Exception {
		List<LinkedList<Object>> data = new ArrayList<LinkedList<Object>>();
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);

			HSSFWorkbook wb = new HSSFWorkbook(fs);
			Sheet sheet = wb.getSheetAt(0);
			Row titleRow = sheet.getRow(sheet.getFirstRowNum());

			LinkedHashMap<String, Integer> orderedColIndexes = getOrderedColumnIndexes(titleRow, tollCompanySpecificColumns);
			Set<Entry<String, Integer>> keySet = orderedColIndexes.entrySet();
			
			System.out.println("Physical number of rows in Excel = " + sheet.getPhysicalNumberOfRows());
			System.out.println("While reading values from vendor specific Excel Sheet: ");

			Map criterias = new HashMap();
			criterias.put("id", tollCompanyId);
			TollCompany tollCompany = genericDAO.findByCriteria(TollCompany.class, criterias, "name", false).get(0);
			
			//boolean stopParsing = false;
			for (int i = titleRow.getRowNum() + 1; i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
				Row row = sheet.getRow(i);
				
				Object firstCellValueObj = getCellValue((HSSFCell) row.getCell(0), true);
				if (firstCellValueObj != null && firstCellValueObj.toString().equalsIgnoreCase("END_OF_DATA")) {
					System.out.println("Received END_OF_DATA");
					break;
				}
				
				LinkedList<Object> rowObjects = new LinkedList<Object>();
				
				rowObjects.add(tollCompany.getName());
				
				/*// TODO: For now, need to get logic 
				String company = StringUtils.substringAfterLast(tollCompany.getName(), " ");
				company = StringUtils.defaultIfEmpty(company, "LU");
				rowObjects.add(company);*/
				
				rowObjects.add(tollCompany.getCompany().getName());

				Iterator<Entry<String, Integer>> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					Entry<String, Integer> entry = iterator.next();
					
					if (entry.getValue() == -1) {
						// corresponding column not found
						rowObjects.add(StringUtils.EMPTY); 
						continue;
					}
					
					Object cellValueObj = getCellValue((HSSFCell) row.getCell(entry.getValue()), true);
					if (cellValueObj != null) {
						System.out.println("Adding " + cellValueObj.toString());
					} else {
						System.out.println("Adding NULL");
					}
					rowObjects.add(cellValueObj);
				}

				data.add(rowObjects);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	private LinkedHashMap<String, Integer> getOrderedColumnIndexes(Row titleRow,
			LinkedHashMap<String, String> vendorSpecificColumns) {
		LinkedHashMap<String, Integer> orderedColumnIndexesMap = new LinkedHashMap<String, Integer>();

		int startCellNumber = titleRow.getFirstCellNum();

		Set<Entry<String, String>> keySet = vendorSpecificColumns.entrySet();
		Iterator<Entry<String, String>> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			
			if (StringUtils.isEmpty(entry.getValue())) {
				orderedColumnIndexesMap.put(entry.getKey(), -1);
			}
			
			boolean foundExpectedColumn = false;
			for (int i = startCellNumber; i < titleRow.getLastCellNum(); i++) {
				String columnHeader = (String) getCellValue(((HSSFCell) titleRow.getCell(i)));
				if (StringUtils.isEmpty(columnHeader.trim()) && StringUtils.isEmpty(entry.getValue().trim())) {
					continue;
				}
				
				if (columnHeader.trim().equalsIgnoreCase(entry.getValue().trim())) {
					// match found
					foundExpectedColumn = true;
					// orderedColumnIndexes.add(i);
					orderedColumnIndexesMap.put(entry.getKey(), i);

					System.out.println(
							"Column Index Mapping for " + entry.getKey() + " = " + columnHeader + " @ index = " + i);
					break;
				}
			}
			if (!foundExpectedColumn) {
				// throw error??
				System.out.println("Could not find expected column " + entry.getValue());
				orderedColumnIndexesMap.put(entry.getKey(), -1);
			}
		}

		System.out.println("Ordered Column Indexes Map = " + orderedColumnIndexesMap);
		return orderedColumnIndexesMap;
	}
}
