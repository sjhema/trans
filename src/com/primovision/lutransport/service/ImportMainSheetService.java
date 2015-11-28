package com.primovision.lutransport.service;

import java.io.InputStream;
import java.util.List;

public interface ImportMainSheetService {
	
	public List<String> importMainSheet(InputStream is) throws Exception;
	public List<String> importfuellogMainSheet(InputStream is,Boolean flag) throws Exception;
	public List<String> importeztollMainSheet(InputStream is,Boolean override) throws Exception;
	public List<String> importVehiclePermitMainSheet(InputStream is)throws Exception;
}

