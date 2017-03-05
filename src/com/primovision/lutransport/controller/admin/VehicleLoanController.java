package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.core.util.ExcelUtil;
import com.primovision.lutransport.core.util.IOUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.PaymentUtil;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.equipment.EquipmentLender;
import com.primovision.lutransport.model.equipment.VehicleLoan;

@Controller
@RequestMapping("/admin/equipment/loan")
public class VehicleLoanController extends CRUDController<VehicleLoan> {
	public VehicleLoanController() {
		setUrlContext("admin/equipment/loan");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EquipmentLender.class, new AbstractModelEditor(EquipmentLender.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<VehicleLoan> vehicleLoanList = performSearch(criteria);
		model.addAttribute("list", vehicleLoanList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<VehicleLoan> vehicleLoanList = performSearch(criteria);
		model.addAttribute("list", vehicleLoanList);
		
		return urlContext + "/list";
	}
	
	private List<VehicleLoan> performSearch(SearchCriteria criteria) {
		String loanNo = (String) criteria.getSearchMap().get("loanNo");
		String company = (String) criteria.getSearchMap().get("company.id");
		String lender = (String) criteria.getSearchMap().get("lender.id");
		String vehicle = (String) criteria.getSearchMap().get("vehicle.unit");	
		String loanStartDate = (String) criteria.getSearchMap().get("loanStartDate");
		String loanEndDate = (String) criteria.getSearchMap().get("loanEndDate");
		
		StringBuffer query = new StringBuffer("select obj from VehicleLoan obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from VehicleLoan obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(loanNo)) {
			whereClause.append(" and obj.loanNo='" + loanNo + "'");
		}
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.vehicle.owner=" + company);
		}
		if (StringUtils.isNotEmpty(lender)) {
			whereClause.append(" and obj.lender=" + lender);
		}
		if (StringUtils.isNotEmpty(vehicle)) {
			whereClause.append(" and obj.vehicle.unit=" + vehicle);
		}
	   if (StringUtils.isNotEmpty(loanStartDate)){
        	try {
        		whereClause.append(" and obj.startDate >='"+sdf.format(dateFormat.parse(loanStartDate))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(loanEndDate)){
	     	try {
	     		whereClause.append(" and obj.endDate <='"+sdf.format(dateFormat.parse(loanEndDate))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.loanNo asc, obj.vehicle.unit asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<VehicleLoan> vehicleLoanList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		populateAdditionalData(vehicleLoanList);
		
		return vehicleLoanList;
	}
	
	private void populateAdditionalData(List<VehicleLoan> vehicleLoanList) {
		for (VehicleLoan aVehicleLoan : vehicleLoanList) {
			populateAdditionalData(aVehicleLoan);
		}
	}
	
	private void populateAdditionalData(VehicleLoan aVehicleLoan) {
		aVehicleLoan.setLenderName(aVehicleLoan.getLender().getName());
		populateNoOfPaymentsLeft(aVehicleLoan);
	}
	
	private void populateNoOfPaymentsLeft(VehicleLoan aVehicleLoan) {
		int noOfPaymentsLeft = PaymentUtil.calculateNoOfPaymentsLeft(aVehicleLoan.getNoOfPayments(), 
					aVehicleLoan.getEndDate(), aVehicleLoan.getPaymentDueDom());
		aVehicleLoan.setPaymentsLeft(noOfPaymentsLeft);
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap();
		
		// select obj from Vehicle obj where obj.type=1 group by obj.unit
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
		model.addAttribute("lenders", genericDAO.findByCriteria(EquipmentLender.class, criterias, "name", false));
	
		model.addAttribute("paymentDueDates", buildPaymentDueDates());
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
		String loanQuery = "select distinct obj.loanNo from VehicleLoan obj order by obj.loanNo asc";
		model.addAttribute("vehicleLoans", genericDAO.executeSimpleQuery(loanQuery));
	}

	private void validateSave(VehicleLoan entity, BindingResult bindingResult) {
		if (StringUtils.isEmpty(entity.getLoanNo())) {
			bindingResult.rejectValue("loanNo", "NotNull.java.lang.String", null, null);
		}
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
		/*if (entity.getLender() == null) {
			bindingResult.rejectValue("lender", "error.select.option", null, null);
		}*/
		if (StringUtils.isEmpty(entity.getLenderName())) {
			bindingResult.rejectValue("lenderName", "NotNull.java.lang.String", null, null);
		}
		if (entity.getPaymentAmount() == null) {
			bindingResult.rejectValue("paymentAmount", "NotNull.java.lang.Float", null, null);
		}
		if (entity.getPaymentDueDom() == null) {
			bindingResult.rejectValue("paymentDueDom", "error.select.option", null, null);
		}
		if (entity.getNoOfPayments() == null) {
			bindingResult.rejectValue("noOfPayments", "NotNull.java.lang.Integer", null, null);
		}
		if (entity.getStartDate() == null) {
			bindingResult.rejectValue("startDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getEndDate() == null) {
			bindingResult.rejectValue("endDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getInterestRate() == null) {
			bindingResult.rejectValue("interestRate", "NotNull.java.lang.Float", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") VehicleLoan entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
		
		beforeSave(request, entity, model);
		
		populateLender(entity);
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Vehicle loan saved successfully");
		
		setupCreate(model, request);
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new VehicleLoan());
		} else {
			Vehicle vehicle = genericDAO.getById(Vehicle.class, entity.getVehicle().getId());
			entity.setVehicle(vehicle);
		}
		
		return getUrlContext() + "/form";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/uploadMain.do")
	public String uploadMain(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return getUrlContext() + "/upload";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/upload.do")
	public void upload(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		Long createdBy = getUser(request).getId();
		
		ByteArrayOutputStream out = null;
		InputStream dataFileInputStream1 = null;
		InputStream dataFileInputStream2 = null;
		try {
			dataFileInputStream1 = file.getInputStream();
			List<String> errorList = uploadVehicleLoan(dataFileInputStream1, createdBy);
			IOUtil.closeInputStream(dataFileInputStream1);
			
			dataFileInputStream2 = file.getInputStream();
			out = createVehicleLoanUploadResponse(dataFileInputStream2, errorList);
			IOUtil.closeInputStream(dataFileInputStream2);
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			out = createVehicleLoanUploadExceptionResponse(ex);
		} finally {
			IOUtil.closeInputStream(dataFileInputStream1);
			IOUtil.closeInputStream(dataFileInputStream2);
		}
		
		String responseFileName = "VehicleLoanUploadResults_" + StringUtils.replace(file.getOriginalFilename(), " ", "_");
		String type = file.getContentType();
		response.setHeader("Content-Disposition", "attachment;filename="+ responseFileName);
		response.setContentType(type);
	
		try {
			out.writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeOutputStream(out);
		}
	}
	
	
	public ByteArrayOutputStream createVehicleLoanUploadResponse(InputStream is, List<String> errors) throws IOException {
		if (errors == null || errors.isEmpty()) {
			return createVehicleLoanUploadSuccessResponse();
		} else {
			return createVehicleLoanUploadErrorResponse(is, errors);
		}
	}
	
	public ByteArrayOutputStream createVehicleLoanUploadErrorResponse(InputStream is, List<String> errors) throws IOException {
		return ExcelUtil.createUploadErrorResponse(is, errors);
	}
	
	public ByteArrayOutputStream createVehicleLoanUploadSuccessResponse() {
		String msg = "ALL vehicle loan records uploaded successfully";
		return ExcelUtil.createUploadSuccessResponse(msg);
	}
	
	public ByteArrayOutputStream createVehicleLoanUploadExceptionResponse(Exception e) {
		return ExcelUtil.createUploadExceptionResponse(e);
	}
	
	private void populateLender(VehicleLoan entity) {
		EquipmentLender lender = checkAndAddLender(entity.getLenderName(), entity.getCreatedAt(), entity.getCreatedBy());
		entity.setLender(lender);
	}
	
	private EquipmentLender checkAndAddLender(String lenderName, Date createdAt, Long createdBy) {
		if (StringUtils.isEmpty(lenderName)) {
			return null;
		}
		
		String escapedLenderName = StringUtils.replace(lenderName, "'", "''", -1);
		escapedLenderName = StringUtils.upperCase(escapedLenderName);
		String query = "select obj from EquipmentLender obj "
						+ " where UPPER(obj.name)='" + escapedLenderName + "' order by id desc";
		List<EquipmentLender> lenderList = genericDAO.executeSimpleQuery(query);
		
		EquipmentLender lender = null;
		if (lenderList.isEmpty()) {
			lender = addLender(lenderName, createdAt, createdBy);
		} else {
			lender = lenderList.get(0);
		}
		
		return lender;
	}
	
	private EquipmentLender addLender(String lenderName, Date createdAt, Long createdBy) {
		EquipmentLender lender = new EquipmentLender();
		lender.setName(lenderName);
		lender.setAddress1("address1");
		lender.setCity("Chicago");
		
		State state = retrieveState("IL");
		lender.setState(state);
		
		lender.setZipcode("60632");
		
		lender.setCreatedAt(createdAt);
		lender.setCreatedBy(createdBy);
		
		genericDAO.save(lender);
		
		return lender;
	}
	
	private State retrieveState(String code) {
		String query = "select obj from State obj "
				+ " where obj.code='" + code + "'";
		List<State> stateList = genericDAO.executeSimpleQuery(query);
		return stateList.get(0);
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("calculateNoOfPaymentsAndLeft", action)) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String paymentDueDom = request.getParameter("paymentDueDom");
			
			int noOfPayments = PaymentUtil.calculateNoOfPayments(startDate, endDate);
			int noOfPaymentsLeft = PaymentUtil.calculateNoOfPaymentsLeft(noOfPayments, endDate, paymentDueDom);
			
			Integer paymentsArr[] = new Integer[2];
			paymentsArr[0] = noOfPayments;
			paymentsArr[1] = noOfPaymentsLeft;
			return gson.toJson(paymentsArr);
		} else if (StringUtils.equalsIgnoreCase("retrieveVehicle", action)) {
			String id = request.getParameter("id");
			Vehicle vehicle = genericDAO.getById(Vehicle.class, Long.valueOf(id));
			return gson.toJson(vehicle);
		} 
		
		return StringUtils.EMPTY;
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		VehicleLoan entity = (VehicleLoan) model.get("modelObject");
		populateAdditionalData(entity);
		
		return urlContext + "/form";
	}
	
	private List<VehicleLoan> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		
		criteria.setPage(0);
		criteria.setPageSize(100000);
		
		List<VehicleLoan> vehicleLoanList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(25);
		
		return vehicleLoanList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + urlContext + "Report." + type);
		}
		
		List<VehicleLoan> vehicleLoanList = searchForExport(model, request);
		
		List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), vehicleLoanList,
						columnPropertyList, request);
			out.writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<String> buildPaymentDueDates() {
		List<String> paymentDueDates = new ArrayList<String>();
		paymentDueDates.add("1st");
		paymentDueDates.add("2nd");
		paymentDueDates.add("3rd");
		
		for (int i = 4; i < 21; i++) {
			paymentDueDates.add(i + "th");
		}
		
		paymentDueDates.add("21st");
		paymentDueDates.add("22nd");
		paymentDueDates.add("23rd");
		
		for (int i = 24; i < 31; i++) {
			paymentDueDates.add(i + "th");
		}
		
		paymentDueDates.add("31st");
		
		return paymentDueDates;
	}
	
	private List<String> uploadVehicleLoan(InputStream is, Long createdBy) throws Exception {
		List<VehicleLoan> vehicleLoanList = new ArrayList<VehicleLoan>();
		List<String> errorList = new ArrayList<String>();
		
		int recordCount = 0;
		int errorCount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			Iterator<Row> rows = sheet.rowIterator();
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
				VehicleLoan aVehicleLoan = null;
				try {
					String unit = ((String) ExcelUtil.getCellValue(row.getCell(0), true));
					if (StringUtils.equals("END_OF_DATA", unit)) {
						break;
					}
					
					aVehicleLoan = new VehicleLoan();
					
					Vehicle vehicle = retrieveVehicle(unit);
					if (vehicle == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Unit#,");
					} else {
						aVehicleLoan.setVehicle(vehicle);
					}
					
					String lenderName = ((String) ExcelUtil.getCellValue(row.getCell(6), true));
					Date createdAt = Calendar.getInstance().getTime();
					EquipmentLender lender = checkAndAddLender(lenderName, createdAt, createdBy);
					if (lender == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Lender,");
					} else {
						aVehicleLoan.setLender(lender);
					}
					
					String loanNo = ((String) ExcelUtil.getCellValue(row.getCell(7), true));
					if (StringUtils.isEmpty(loanNo)) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Loan No.,");
					} else {
						aVehicleLoan.setLoanNo(loanNo);
					}
					
					String paymentAmount = ((String) ExcelUtil.getCellValue(row.getCell(8), true));
					Double paymentAmountDbl = processPaymentAmount(paymentAmount);
					if (paymentAmountDbl == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Payment Amount,");
					} else {
						aVehicleLoan.setPaymentAmount(paymentAmountDbl);
					}
					
					String startDateStr = ((String) ExcelUtil.getCellValue(row.getCell(9), true));
					Date startDate = processDate(startDateStr);
					if (startDate == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Start Date,");
					} else {
						aVehicleLoan.setStartDate(startDate);;
					}
					
					String endDateStr = ((String) ExcelUtil.getCellValue(row.getCell(10), true));
					Date endDate = processDate(endDateStr);
					if (endDate == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("End Date,");
					} else {
						aVehicleLoan.setEndDate(endDate);;
					}
					
					row.getCell(11).toString();
					String interestRate = ((String) ExcelUtil.getCellValue(row.getCell(11), true));
					Double interestRateDbl = processInterestRate(interestRate);
					if (interestRateDbl == null) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Interest Rate,");
					} else {
						aVehicleLoan.setInterestRate(interestRateDbl);
					}
					
					String paymentDueDom = ((String) ExcelUtil.getCellValue(row.getCell(12), true));
					if (StringUtils.isEmpty(paymentDueDom)) {
						recordError = true;
						fatalRecordError = true;
						recordErrorMsg.append("Due Date,");
					} else {
						aVehicleLoan.setPaymentDueDom(paymentDueDom);;
					}
					
					if (StringUtils.isNotEmpty(startDateStr) && StringUtils.isNotEmpty(endDateStr)
							&&  StringUtils.isNotEmpty(paymentDueDom)) {
						int noOfPayments = PaymentUtil.calculateNoOfPayments(startDateStr, endDateStr);
						aVehicleLoan.setNoOfPayments(noOfPayments);
					}
					
					if (checkDuplicate(vehicleLoanList, aVehicleLoan)) {
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
					vehicleLoanList.add(aVehicleLoan);
				}
			}
			
			System.out.println("Done processing...Total record count: " + recordCount 
					+ ". Error count: " + errorCount
					+ ". Number of records being loaded: " + vehicleLoanList.size());
			if (!vehicleLoanList.isEmpty()) {
				for (VehicleLoan aVehicleLoanToBeSaved : vehicleLoanList) {
					aVehicleLoanToBeSaved.setCreatedBy(createdBy);
					aVehicleLoanToBeSaved.setCreatedAt(Calendar.getInstance().getTime());
					genericDAO.saveOrUpdate(aVehicleLoanToBeSaved);
				}
			}
		} catch (Exception ex) {
			errorList.add("Not able to upload XL!!! Please try again.");
			log.warn("Error while importing Mileage log: " + ex);
		}
	
		return errorList;
	}
	
	private Date processDate(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	private boolean checkDuplicate(List<VehicleLoan> aVehicleLoanList, VehicleLoan aVehicleLoan) {
		if (aVehicleLoan.getVehicle() == null || aVehicleLoan.getLender() == null
				|| StringUtils.isEmpty(aVehicleLoan.getLoanNo())) {
			return false;
		}
		
		/*if (aVehicleLoanList.contains(aVehicleLoan)) {
			return true;
		}*/
		
		String query = "select obj from VehicleLoan obj where obj.loanNo='" + aVehicleLoan.getLoanNo() + "'"
				+ " and obj.vehicle=" + aVehicleLoan.getVehicle().getId()
				+ " and obj.lender=" + aVehicleLoan.getLender().getId();
		
		List<VehicleLoan> vehicleLoanList = genericDAO.executeSimpleQuery(query);
		return !vehicleLoanList.isEmpty();
	}
	
	private Double processPaymentAmount(String paymentAmount) {
		if (StringUtils.isEmpty(paymentAmount)) {
			return null;
		}
		
		Double paymentAmountDbl = null;
		try {
			paymentAmountDbl = new Double(paymentAmount);
			
		} catch (Exception ex) {
			log.warn("Exception while processing payment amount", ex);
		}
		
		return paymentAmountDbl;
	}
	
	private Double processInterestRate(String interestRate) {
		if (StringUtils.isEmpty(interestRate)) {
			return null;
		}
		
		Double interestRateDbl = null;
		try {
			interestRateDbl = new Double(interestRate);
		} catch (Exception ex) {
			log.warn("Exception while processing interest rate", ex);
		}
		
		return interestRateDbl;
	}
	
	private Vehicle retrieveVehicle(String unit) {
		if (StringUtils.isEmpty(unit)) {
			return null;
		}
		
		String query = "select obj from Vehicle obj where obj.unit=" + unit
				+ " order by obj.id desc";
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(query);
		if (vehicleList == null || vehicleList.isEmpty()) {
			return null;
		} else {
			return vehicleList.get(0);
		}
	}
}
