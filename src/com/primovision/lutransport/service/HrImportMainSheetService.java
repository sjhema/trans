package com.primovision.lutransport.service;
/**
 * @author subodh
 *
 */
import java.io.InputStream;
import java.util.List;

public interface HrImportMainSheetService {
	
	public List<String> importAttendanceMainSheet(InputStream is) throws Exception;
	
}

