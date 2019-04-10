package com.primovision.lutransport.service;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ImportMainSheetService {
	
	public List<String> importMainSheet(InputStream is) throws Exception;
	public List<String> importfuellogMainSheet(InputStream is,Boolean flag) throws Exception;
	public List<String> importeztollMainSheet(InputStream is,Boolean override) throws Exception;
	public List<String> importVehiclePermitMainSheet(InputStream is)throws Exception;
	public List<LinkedList<Object>> importVendorSpecificFuelLog(InputStream is, LinkedHashMap<String, String> vendorSpecificColumns, Long vendor, HashMap<String, Object> additionalVendorData) throws Exception;
	public List<LinkedList<Object>> importTollCompanySpecificTollTag(InputStream is,
			LinkedHashMap<String, String> tollCompanySpecificColumns, Long tollCompanyId) throws Exception;
	
	public List<String> importOldGPSMileageLogMainSheet(InputStream is, Date period, Double resetMiles, Long createdBy) throws Exception;
	public List<String> importMileageLogMainSheet(InputStream is, Date period, Double resetMiles, Long createdBy) throws Exception;
	
	public List<String> importSubcontractorRateMainSheet(InputStream is, Date validFrom, Date validTo, Long createdBy) throws Exception;
	public List<String> importEmployeeMainSheet(InputStream is, Long createdBy) throws Exception;
	public List<String> importWMTickets(InputStream is, String locationType, String destinationLocation, Long createdBy) throws Exception;
	public List<String> importWMInvoice(InputStream is, Long createdBy) throws Exception;
	public List<String> importInjuryMainData(InputStream is, Long createdBy) throws Exception;
	public List<String> importInjuryReportedData(InputStream is, Long createdBy) throws Exception;
	public List<String> importInjuryNotReportedData(InputStream is, Long createdBy) throws Exception;
	public List<String> importAccidentMainData(InputStream is, Long createdBy) throws Exception;
	public List<String> importAccidentReportedData(InputStream is, Long createdBy) throws Exception;
	public List<String> importAccidentNotReportedData(InputStream is, Long createdBy) throws Exception;
	public List<String> importLoadMiles(InputStream is, Long createdBy) throws Exception;
}

