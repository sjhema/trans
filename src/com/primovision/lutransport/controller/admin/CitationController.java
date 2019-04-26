package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.WorkerCompUtils;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.Violation;
import com.primovision.lutransport.model.accident.Accident;
import com.primovision.lutransport.model.accident.AccidentCause;
import com.primovision.lutransport.model.accident.AccidentRoadCondition;
import com.primovision.lutransport.model.accident.AccidentType;
import com.primovision.lutransport.model.accident.AccidentWeather;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

@Controller
@RequestMapping("/admin/citation/citationmaint")
public class CitationController extends CRUDController<Violation> {
	private static final String UPLOAD_DIR = "/trans/storage/citations";
	private static final String FILE_SUFFIX = "_citation_doc";
	
	public CitationController() {
		setUrlContext("admin/citation/citationmaint");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName asc, id desc", false));
		
		//model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type in (1, 4)"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
	}
	
	public void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCommon(model, request);
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<Violation> violationList = performSearch(criteria);
		model.addAttribute("list", violationList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<Violation> violationList = performSearch(criteria);
		model.addAttribute("list", violationList);
		
		return urlContext + "/list";
	}
	
	private List<Violation> performSearch(SearchCriteria criteria) {
		String company = (String) criteria.getSearchMap().get("company");
		String driver = (String) criteria.getSearchMap().get("driver");
		
		String citationDateFrom = (String) criteria.getSearchMap().get("citationDateFrom");
		String citationDateTo = (String) criteria.getSearchMap().get("citationDateTo");
		
		StringBuffer query = new StringBuffer("select obj from Violation obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Violation obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.roadsideInspection is null");
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.company.id=" + company);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(citationDateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(citationDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(citationDateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(citationDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.incidentDate desc, obj.company.name asc, obj.driver.fullName asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Violation> violationList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return violationList;
	}

	private void validateSave(Violation entity, BindingResult bindingResult, HttpServletRequest request) {
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		
		if (entity.getTruck() == null || StringUtils.isEmpty(entity.getTruck().getUnitNum())) {
			bindingResult.rejectValue("truck.unitNum", "error.select.option", null, null);
		}
		if (entity.getTruck() != null && StringUtils.isNotEmpty(entity.getTruck().getUnitNum())
				&& entity.getIncidentDate() != null) {
			Vehicle matchingTruck = WorkerCompUtils.retrieveVehicleForUnit(entity.getTruck().getUnitNum(), 1,  
					entity.getIncidentDate(), genericDAO);
			if (matchingTruck == null) {
				bindingResult.rejectValue("truck.unitNum", "error.select.option", null, null);
				request.getSession().setAttribute("error",
						"No Matching Truck Entries Found for Selected Truck and Citation Date.");
			} else {
				entity.setTruck(matchingTruck);
			}
		}
		
		if (entity.getTrailer() == null || StringUtils.isEmpty(entity.getTrailer().getUnitNum())) {
			//bindingResult.rejectValue("trailer.unitNum", "error.select.option", null, null);
			entity.setTrailer(null);
		}
		if (entity.getTrailer() != null && StringUtils.isNotEmpty(entity.getTrailer().getUnitNum())
				&& entity.getIncidentDate() != null) {
			Vehicle matchingTrailer = WorkerCompUtils.retrieveVehicleForUnit(entity.getTrailer().getUnitNum(), 2,  
					entity.getIncidentDate(), genericDAO);
			if (matchingTrailer == null) {
				bindingResult.rejectValue("trailer.unitNum", "error.select.option", null, null);
				request.getSession().setAttribute("error",
						"No Matching Trailer Entries Found for Selected Trailer and Citation Date.");
			} else {
				entity.setTrailer(matchingTrailer);
			}
		}
		
		if (entity.getCompany() == null) {
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if (entity.getIncidentDate() == null) {
			bindingResult.rejectValue("incidentDate", "NotNull.java.lang.String", null, null);
		}
		
		String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
		if (StringUtils.isEmpty(citationNo)) {
			bindingResult.rejectValue("citationNo", "NotNull.java.lang.String", null, null);
		} else {
			if (isDuplicate(entity.getId(), citationNo)) {
				bindingResult.rejectValue("citationNo", "error.duplicate.entry", null, null);
			}
		}
		
		/*if (StringUtils.isEmpty(entity.getOutOfService())) {
			bindingResult.rejectValue("outOfService", "error.select.option", null, null);
		}*/
		if (StringUtils.isEmpty(entity.getViolationType())) {
			bindingResult.rejectValue("violationType", "NotNull.java.lang.String", null, null);
		}
	}
	
	private List<Violation> retrieveCitation(Long violationId, String citationNo) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.citationNo='" + citationNo + "'");
		if (violationId != null) {
			query.append(" and obj.id !=" + violationId);
		}
		
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private boolean isDuplicate(Long violationId, String citationNo) {
		List<Violation> citationList = retrieveCitation(violationId, citationNo);
		return (citationList == null || citationList.isEmpty()) ? false : true;
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Violation entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult, request);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Citation saved successfully");
		
		setupCreate(model, request);
		
		return getUrlContext() + "/form";
	}
	
	private List<Violation> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		int origPageSize = criteria.getPageSize();
		
		criteria.setPage(0);
		criteria.setPageSize(15000);
		
		List<Violation> violationList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(origPageSize);
		
		return violationList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String reportName = "violationReport";
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		List<Violation> violationList = searchForExport(model, request);
		//Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, getEntityClass(), violationList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					violationList, params, type, request);*/
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
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equals("retrieveDriverCompTerm", action)) {
			String driverIdStr = request.getParameter("driverId");
			if (StringUtils.isEmpty(driverIdStr)) {
				return StringUtils.EMPTY;
			}
			
			Long driverId = Long.valueOf(driverIdStr);
			Driver driver = genericDAO.getById(Driver.class, driverId);
			if (driver == null) {
				return StringUtils.EMPTY;
			} 
			
			return driver.getCompany().getId().longValue() + "|" + driver.getTerminal().getId().longValue();
		} else if (StringUtils.equalsIgnoreCase("doesDocExist", action)) {
			String file = request.getParameter("file");
			if (StringUtils.isEmpty(file)) {
				return StringUtils.EMPTY;
			}
			
			String idStr = request.getParameter("id");
			Long id = new Long(idStr);
			boolean responseBool = doesDocExist(id, file);
			return BooleanUtils.toStringTrueFalse(responseBool);
		}  
		
		return StringUtils.EMPTY;
	}
	
	private boolean doesDocExist(Violation entity, MultipartFile file) {
		String filePath = constructDocFilePath(entity.getId(), file);
		return doesDocExist(filePath);
	}
	
	private boolean doesDocExist(String file) {
		File fileToCheck = new File(file);
		return fileToCheck.exists();
	}
	
	private boolean doesDocExist(Long id, String file) {
		String filePath = constructDocFilePath(id, file);
		return doesDocExist(filePath);
	}
	
	@RequestMapping("/managedocs/deletedoc.do")
	public String deleteDoc(ModelMap model, HttpServletRequest request, 
				@ModelAttribute("modelObject") Violation entity) {
		String filePath = constructDocFilePath(entity);
		File file = new File(filePath);
		
		boolean status = file.delete();
		if (status) {
			if (!docsUploaded(entity)) {
				entity.setDocs("N");
				entity.setModifiedAt(Calendar.getInstance().getTime());
				entity.setModifiedBy(getUser(request).getId());
				genericDAO.saveOrUpdate(entity);
			}
			
			request.getSession().setAttribute("msg", "Successfully deleted the pdf");
		} else {
			request.getSession().setAttribute("error", "Error occured while deleting the pdf!!");
		}
		
		
		setupManageDocs(model, entity);
		return urlContext + "/manageDocs";
	}
	
	private boolean docsUploaded(Violation entity) {
		String[] filaeNamesList = getUploadedFileNames(entity);
		return (filaeNamesList.length > 0) ? true : false;
	}
	
	@RequestMapping("/managedocs/downloaddoc.do")
	public String downloadDoc(ModelMap model, HttpServletRequest request, HttpServletResponse response,
				@ModelAttribute("modelObject") Violation entity) {
		try {
			processDocDownload(request, response, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void processDocDownload(HttpServletRequest request,
         HttpServletResponse response, @ModelAttribute("modelObject") Violation entity) {
		// Reads input file from an absolute path
		String filePath = constructDocFilePath(entity);
		File downloadFile = new File(filePath);
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(downloadFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
      
		// Obtains ServletContext
		ServletContext context = request.getServletContext();
     
		/*// If you want to use a relative path to context root:
     	String relativePath = context.getRealPath("");
     	System.out.println("relativePath = " + relativePath);*/
      
		// Gets MIME type of the file
		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {        
			// Set to binary type if MIME mapping not found
         mimeType = "application/pdf";
		}
		System.out.println("MIME type: " + mimeType);
      
		// Modifies response
		response.setContentType(mimeType);
		response.setContentLength((int)downloadFile.length());
      
		// Forces download
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);
      
		// Obtains response's output stream
		OutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
	      
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  
	}
	
	@RequestMapping("/managedocs/start.do")
	public String manageDocsStart(ModelMap model, HttpServletRequest request, 
			@ModelAttribute("modelObject") Violation entity) {
		setupManageDocs(model, entity);
		return urlContext + "/manageDocs";
	}
	
	private void setupManageDocs(ModelMap model, Violation entity) {
		String[] fileNamesList = getUploadedFileNames(entity);
		model.addAttribute("fileList", fileNamesList);
	}
	
	private String[] getUploadedFileNames(Violation entity) {
		String docPattern = constructDocFilePattern(entity.getId());
		FileFilter fileFilter = new WildcardFileFilter(docPattern);
		File dir = new File(UPLOAD_DIR);
		File[] files = dir.listFiles(fileFilter);
		String[] fileNamesList = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			fileNamesList[i] = files[i].getName();
		}
		return fileNamesList;
	}
	
	private boolean validateUploadDoc(List<String> errorList, Violation entity, MultipartFile file) {
		if (StringUtils.isEmpty(file.getOriginalFilename())) {
		    errorList.add("Please choose a file to upload !!");
		    return false;
	   }
		
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!(ext.equalsIgnoreCase(".pdf"))) {
      	errorList.add("Please choose a file to upload with extention .pdf!!");
		   return false;
		}
		
		/*if (doesDocExist(entity, file)) {
			errorList.add("PDF with same name is already uploaded");
		   return false;
		}*/
		
		return true;
	}
	
	@RequestMapping("/managedocs/uploaddoc.do")
	public String uploadDoc(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@ModelAttribute("modelObject") Violation entity,
			@RequestParam("dataFile") MultipartFile file) {
		List<String> errorList = new ArrayList<String>();
		model.addAttribute("errorList", errorList);
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (!validateUploadDoc(errorList, entity, file)) {
				 setupManageDocs(model, entity);
			    return urlContext + "/manageDocs";
			}
			
			Long createdBy = getUser(request).getId();
			saveDoc(request, entity, file, createdBy, errorList);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded Citation pdf");
			} 
		} catch (Exception ex) {
			log.warn("Unable to upload Citation doc:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Citation doc!!");
		}
		
		setupManageDocs(model, entity);
		return urlContext + "/manageDocs";
	}
	
	private void saveDoc(HttpServletRequest request, Violation entity, MultipartFile file,
			Long userId, List<String> errorList) {
		if (file.isEmpty()) {
			errorList.add("Empty file");
			return;
		}
	
		try {
			/*String realPathToUploads =  request.getServletContext().getRealPath(UPLOAD_DIR);
			if (!new File(realPathtoUploads).exists()) {
			    new File(realPathtoUploads).mkdir();
			}*/

			String filePath = constructDocFilePath(entity.getId(), file);
			File dest = new File(filePath);
			file.transferTo(dest);
			
			entity.setDocs("Y");
			entity.setModifiedAt(Calendar.getInstance().getTime());
			entity.setModifiedBy(getUser(request).getId());
			genericDAO.saveOrUpdate(entity);
		} catch (Exception e) {
			errorList.add("Error occured while uploading file");
			return;
		}
	}
	
	private String constructDocFilePath(Violation entity) {
		String filePath = UPLOAD_DIR + "/" + entity.getFileList()[0];
		return filePath;
	}
	
	private String constructDocFilePath(Long id, MultipartFile file) {
		return constructDocFilePath(id, file.getOriginalFilename());
	}
	
	private String constructDocFilePath(Long id, String file) {
		String filePath = UPLOAD_DIR + "/" + id + FILE_SUFFIX;
		String originalFileName = file.replaceAll("\\s", StringUtils.EMPTY);
		return filePath + "_" + originalFileName;
	}
	
	private String constructDocFilePattern(Long id) {
		String filePath = id + FILE_SUFFIX + "*.*";
		return filePath;
	}
}
