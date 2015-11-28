package com.primovision.lutransport.service;
/**
 * @author subodh
 *
 */
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.ErrorData;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehicleTollTag;
import com.primovision.lutransport.model.hr.Attendance;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.ShiftCalendar;

@Transactional(readOnly = true)
public class HrImportMainSheetServiceImpl implements HrImportMainSheetService {

	private static Logger log = Logger
			.getLogger(HrImportMainSheetServiceImpl.class.getName());
	private static SimpleDateFormat sdf = new SimpleDateFormat("mm-DD-yy");
	 private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	protected GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	@Autowired
	private HrReportService hrreportService;

	public void setHrReportService(HrReportService hrreportService) {
		this.hrreportService = hrreportService;
	}

	public String getAsIntegerString(String value) {
		return value.replaceAll("\\.\\d*$", "");
	}

	private Date getAsDate(String validCellValue) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.parse(validCellValue);
	}

	
	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importAttendanceMainSheet(InputStream is) throws Exception {
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance
		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<Attendance> attendances = new ArrayList<Attendance>();
		
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();
			
			wb = new HSSFWorkbook(fs);
			
			
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			Attendance attendance = null;
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
				try 
				{
					attendance = new Attendance();
					if (validDate(getCellValue(row.getCell(0))))
						attendance.setAttendancedate((Date) getCellValue(row.getCell(0)));
					else {
						error = true;
						lineError.append("Date,");
					}
					
					try {
						criterias.clear();
						criterias.put("name", (String) getCellValue(row.getCell(1)));
						ShiftCalendar shift = genericDAO.getByCriteria(ShiftCalendar.class,criterias);
						if (shift == null){
							//lineError.append("Error in Shift,");
							throw new Exception("Shift");
						}
						else
							attendance.setShift(shift);
					} catch (Exception ex) {
						lineError.append("Error in Shift,");
						error = true;
						log.warn(ex.getMessage());
					}
					
					try {
						criterias.clear();
						criterias.put("fullName", (String) getCellValue(row.getCell(2)));
						Driver emp = genericDAO.getByCriteria(Driver.class,criterias);
						if (emp == null){
							//lineError.append("Employee name,");
							throw new Exception("employee name");
						}
						else{
							  attendance.setDriver(emp);
							  attendance.setCatagory(emp.getCatagory());
							  attendance.setCompany(emp.getCompany());
							  attendance.setTerminal(emp.getTerminal());
							}
					} catch (Exception ex) {
						lineError.append("Employee name,");
						error = true;
						System.out.println("\ncatch\n");
						log.warn(ex.getMessage());
					}
					
					
					
					if (validTime((String) getCellValue(row.getCell(3)))) {
						StringBuilder timeIn = new StringBuilder(StringUtils.leftPad((String) getCellValue(row.getCell(3)),4,'0'));
						timeIn.insert(2, ':');
						attendance.setSignintime(timeIn.toString());
					}
					else {
						error = true;
						lineError.append("Time In,");
					}
					if (validTime((String) getCellValue(row.getCell(4)))){
						StringBuilder timeOut = new StringBuilder(StringUtils.leftPad((String) getCellValue(row
								.getCell(4)),4,'0'));
						timeOut.insert(2, ':');
						attendance.setSignouttime(timeOut.toString());
					}
					else {
						error = true;
						lineError.append("Time Out,");
					}
					
					
					 //checking  for duplicate	  
					  
					   String attendancedate=dateFormat.format(attendance.getAttendancedate());
					  
					   String attendanceQuery="select obj from Attendance obj where obj.shift='"+attendance.getShift().getId()+"' and obj.attendancedate='"+attendancedate+"' and obj.employee='"+attendance.getDriver().getId()+"'";
					   List<Attendance> dupAttendance = genericDAO.executeSimpleQuery(attendanceQuery);
					   //int duplicate=duplicateFuel.size();
					   System.out.println("\nattendanceQuery==>"+attendanceQuery+"\n");
					   System.out.println("\ndupAttendance.size()==>"+dupAttendance.size()+"\n");
					   if(dupAttendance.size()>0){
					   lineError.append("Duplicate Attendance.,");
					      error = true;
					      
					   }
					
					
					
					if (!error){
						attendances.add(attendance);
					}
					 else {
						errorcount++;
					 }
					
				} catch (Exception ex) {
					error=true;
					log.warn(ex);
				}
				if (lineError.length()>0) {
					System.out.println("Error :"+lineError.toString());
					list.add("Line "+count+":"+lineError.toString()+"<br/>");
				}
				System.out.println("Record No :"+count);
				count++;
			}
		} catch (Exception e) {
			log.warn("Error in import Attandance :" + e);
		}
		if (errorcount==0) {
			Date date = new Date();
			for(Attendance attan:attendances) {
				attan.setImportdate(date);
				genericDAO.saveOrUpdate(attan);
			}
		}
		return list;
	}

	
	
	
	
	
	
	
	
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

	
	

}
