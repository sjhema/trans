package com.primovision.lutransport.service;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface ImportMainSheetService {
	
	public List<String> importMainSheet(InputStream is) throws Exception;
	public List<String> importfuellogMainSheet(InputStream is,Boolean flag) throws Exception;
	public List<String> importeztollMainSheet(InputStream is,Boolean override) throws Exception;
	public List<String> importVehiclePermitMainSheet(InputStream is)throws Exception;
	public List<LinkedList<Object>> importVendorSpecificFuelLog(InputStream is, LinkedHashMap<String, String> vendorSpecificColumns, Long vendor, HashMap<String, Object> additionalVendorData) throws Exception;
	public List<LinkedList<Object>> importTollCompanySpecificTollTag(InputStream is,
			LinkedHashMap<String, String> tollCompanySpecificColumns, Long tollCompanyId) throws Exception;
	
	public List<String> importMileageLogMainSheet(InputStream is, Date period, Double resetMiles, Long createdBy) throws Exception;
}

