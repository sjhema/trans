package com.primovision.lutransport.controller.admin;

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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.RoadsideInspection;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.Violation;

@Controller
@RequestMapping("/admin/roadsideinspec/roadsideinspecmaint")
public class RoadsideInspectionController extends CRUDController<RoadsideInspection> {
	private static final String UPLOAD_DIR = "/trans/storage/roadsideinspec";
	private static final String FILE_SUFFIX = "_roadsideinspec_doc";
	
	public RoadsideInspectionController() {
		setUrlContext("admin/roadsideinspec/roadsideinspecmaint");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
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
		String roadsideInspectionId = (String) criteria.getSearchMap().get("id");
		String company = (String) criteria.getSearchMap().get("company.id");
		String truck = (String) criteria.getSearchMap().get("truck.unit");
		String trailer = (String) criteria.getSearchMap().get("trailer.unit");	
		String driver = (String) criteria.getSearchMap().get("driver");
		String inspectionDateFrom = (String) criteria.getSearchMap().get("inspectionDateFrom");
		String inspectionDateTo = (String) criteria.getSearchMap().get("inspectionDateTo");
		String violation = (String) criteria.getSearchMap().get("violation");
		String citation = (String) criteria.getSearchMap().get("citation");
		
		StringBuffer query = new StringBuffer("select obj from Violation obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Violation obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.roadsideInspection is not null");
		
		if (StringUtils.isNotEmpty(roadsideInspectionId)) {
			whereClause.append(" and obj.roadsideInspection.id=" + roadsideInspectionId);
		}
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.roadsideInspection.company=" + company);
		}
		if (StringUtils.isNotEmpty(truck)) {
			whereClause.append(" and obj.roadsideInspection.truck.unit=" + truck);
		}
		if (StringUtils.isNotEmpty(trailer)) {
			whereClause.append(" and obj.roadsideInspection.trailer.unit=" + trailer);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.roadsideInspection.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(inspectionDateFrom)){
        	try {
        		whereClause.append(" and obj.roadsideInspection.inspectionDate >='"+sdf.format(dateFormat.parse(inspectionDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(inspectionDateTo)){
	     	try {
	     		whereClause.append(" and obj.roadsideInspection.inspectionDate <='"+sdf.format(dateFormat.parse(inspectionDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(violation)) {
      	whereClause.append(" and obj.roadsideInspection.violation='" + violation + "'");
		}
      if (StringUtils.isNotEmpty(citation)) {
      	whereClause.append(" and obj.roadsideInspection.citation='" + citation + "'");
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by roadsideInspection.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Violation> violationList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return violationList;
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName asc, id desc", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCommon(model, request);
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
	}
	
	public void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		//model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type in (1, 4)"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 group by obj.unit"));
	}

	private void validateSave(RoadsideInspection entity, BindingResult bindingResult) {
		if (entity.getInspectionDate() == null) {
			bindingResult.rejectValue("inspectionDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getCompany() == null) {
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if (entity.getTruck() == null) {
			bindingResult.rejectValue("truck", "error.select.option", null, null);
		}
		if (entity.getTrailer() == null) {
			bindingResult.rejectValue("trailer", "error.select.option", null, null);
		}
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option", null, null);
		}
		if (StringUtils.isEmpty(entity.getInspectionLevel())) {
			bindingResult.rejectValue("inspectionLevel", "error.select.option", null, null);
		}
		
		validateSaveViolation(entity, bindingResult);
	}
	
	private void validateSaveViolation(RoadsideInspection entity, BindingResult bindingResult) {
		String violationType = StringUtils.trimToEmpty(entity.getViolationType());
		if (StringUtils.isEmpty(violationType)) {
			if (entity.getId() == null 
					|| StringUtils.isNotEmpty(entity.getViolationId())
					|| StringUtils.isNotEmpty(entity.getCitationNo())
					|| StringUtils.isNotEmpty(entity.getOutOfService())) {
				bindingResult.rejectValue("violationType", "NotNull.java.lang.String", null, null);
			}
		} else {
			if (StringUtils.isEmpty(entity.getOutOfService())) {
				bindingResult.rejectValue("outOfService", "error.select.option", null, null);
			}
			
			String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
			if (StringUtils.isNotEmpty(citationNo)) {
				if (isDuplicate(entity.getViolationId(), citationNo)) {
					bindingResult.rejectValue("citationNo", "error.duplicate.entry", null, null);
				}
			}
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") RoadsideInspection entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if (bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	setupViolation(model, request, entity);
        	return getUrlContext() + "/form";
      }
		
		String violationType = StringUtils.trimToEmpty(entity.getViolationType());
		entity.setViolationType(violationType);
		if (StringUtils.isNotEmpty(violationType)) {
			entity.setViolation("Y");
		} else {
			if (entity.getId() == null) {
				entity.setViolation("N");
			} else {
				if (!hasViolations(entity.getId())) {
					entity.setViolation("N");
				}
			}
		}
		
		String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
		entity.setCitationNo(citationNo);
		if (StringUtils.isNotEmpty(citationNo)) {
			entity.setCitation("Y");
		} else {
			if (entity.getId() == null) {
				entity.setCitation("N");
			} else {
				if (!hasCitations(entity.getId())) {
					entity.setCitation("N");
				}
			}
		}
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		saveViolation(entity);
		
		request.getSession().setAttribute("msg", "Roadside Inspection saved successfully");
		
		setupCreate(model, request);
		setupViolation(model, request, entity);
		return getUrlContext() + "/form";
	}
	
	private boolean hasViolations(Long roadInspectionId) {
		List<Violation> violationList = retrieveViolationsForRoadInspec(roadInspectionId);
		return (violationList == null || violationList.isEmpty()) ? false : true;
	}
	
	private boolean hasCitations(Long roadInspectionId) {
		List<Violation> citationList = retrieveCitationsForRoadInspec(roadInspectionId);
		return (citationList == null || citationList.isEmpty()) ? false : true;
	}
	
	private List<Violation> retrieveViolationsForRoadInspec(Long roadInspectionId) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.roadsideInspection.id=" + roadInspectionId);
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private List<Violation> retrieveCitationsForRoadInspec(Long roadInspectionId) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.roadsideInspection.id=" + roadInspectionId);
		query.append(" and obj.citationNo is not null");
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private List<Violation> retrieveCitation(String violationId, String citationNo) {
		StringBuffer query = new StringBuffer("select obj from Violation obj where obj.citationNo='" + citationNo + "'");
		if (StringUtils.isNotEmpty(violationId)) {
			query.append(" and obj.id !=" + Long.valueOf(violationId));
		}
		
		List<Violation> violationList = genericDAO.executeSimpleQuery(query.toString());
		return violationList;
	}
	
	private boolean isDuplicate(String violationId, String citationNo) {
		List<Violation> citationList = retrieveCitation(violationId, citationNo);
		return (citationList == null || citationList.isEmpty()) ? false : true;
	}
	
	private void saveViolation(RoadsideInspection entity) {
		String violationType = StringUtils.trimToEmpty(entity.getViolationType());
		if (StringUtils.isEmpty(violationType)) {
			return;
		}
		
		Violation aViolation = null;
		String violationIdStr = entity.getViolationId();
		if (StringUtils.isNotEmpty(violationIdStr)) {
			Long violationId = Long.valueOf(violationIdStr);
			aViolation = genericDAO.getById(Violation.class, violationId);
			aViolation.setModifiedAt(entity.getModifiedAt());
			aViolation.setModifiedBy(entity.getModifiedBy());
		} else {
			aViolation = new Violation();
			aViolation.setCreatedAt(entity.getCreatedAt());
			aViolation.setCreatedBy(entity.getCreatedBy());
			aViolation.setStatus(1);
		}
		
		aViolation.setCompany(entity.getCompany());
		aViolation.setDriver(entity.getDriver());
		aViolation.setTruck(entity.getTruck());
		aViolation.setTrailer(entity.getTrailer());
		aViolation.setIncidentDate(entity.getInspectionDate());
		aViolation.setOutOfService(entity.getOutOfService());
		aViolation.setViolationType(violationType);
		
		String citationNo = StringUtils.trimToEmpty(entity.getCitationNo());
		if (StringUtils.isNotEmpty(citationNo)) {
			aViolation.setCitationNo(citationNo);
		} else {
			aViolation.setCitationNo(null);
		}
		
		aViolation.setRoadsideInspection(entity);
		
		genericDAO.saveOrUpdate(aViolation);
		
		emptyViolation(entity);
	}
	
	private void emptyViolation(RoadsideInspection entity) {
		entity.setViolationId(StringUtils.EMPTY);
		entity.setCitationNo(StringUtils.EMPTY);
		entity.setViolationType(StringUtils.EMPTY);
		entity.setOutOfService(StringUtils.EMPTY);
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") RoadsideInspection entity,
			BindingResult bindingResult, HttpServletRequest request) {
		String query = "select obj from Violation obj where obj.roadsideInspection.id=" + entity.getId();
		List<Violation> violationList = genericDAO.executeSimpleQuery(query);
		for (Violation aViolation : violationList) {
			genericDAO.delete(aViolation);
		}
		
		String errorMsg = StringUtils.EMPTY;
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Roadside Inspection with id: %d", entity.getId());
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Roadside Inspection deleted successfully");
		}
		
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		setupViolation(model, request, null);
		
		return urlContext + "/form";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteViolation.do")
	public String deleteViolation(HttpServletRequest request, ModelMap model,
			@RequestParam(value = "id") Long roadsideInspectionId,
			@RequestParam(value = "violationId") Long violationId) {
		String query = "select obj from Violation obj where obj.roadsideInspection.id=" + roadsideInspectionId;
		List<Violation> violationList = genericDAO.executeSimpleQuery(query);
		if (violationList.size() == 1) {
			request.getSession().setAttribute("error", "Roadside inspection should have at least one violation");
			
			RoadsideInspection aRoadsideInspection = genericDAO.getById(RoadsideInspection.class, roadsideInspectionId);
			model.addAttribute("modelObject", aRoadsideInspection);
			
			setupCreate(model, request);
			setupViolation(model, request, aRoadsideInspection);
			
			return getUrlContext() + "/form";
		}
		
		String errorMsg = StringUtils.EMPTY;
		Violation aViolation = genericDAO.getById(Violation.class, violationId);
		try {
			genericDAO.delete(aViolation);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Violation with id: %d", violationId);
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Violation deleted successfully");
		}
		
		RoadsideInspection aRoadsideInspection = genericDAO.getById(RoadsideInspection.class, roadsideInspectionId);
		model.addAttribute("modelObject", aRoadsideInspection);
		
		setupCreate(model, request);
		setupViolation(model, request, aRoadsideInspection);
		
		return getUrlContext() + "/form";
	}
	
	private void setupViolation(ModelMap model, HttpServletRequest request, RoadsideInspection entity) {
		String roadsideInspectionIdStr = request.getParameter("id");
		if (entity == null && StringUtils.isEmpty(roadsideInspectionIdStr)) {
			return;
		}
		
		Long roadsideInspectionId = entity != null ? entity.getId() : Long.valueOf(roadsideInspectionIdStr);
		if (roadsideInspectionId == null) {
			return;
		}
		
		String query = "select obj from Violation obj where obj.roadsideInspection.id=" + roadsideInspectionId
								+ " order by obj.id asc";
		List<Violation> violationList = genericDAO.executeSimpleQuery(query);
		model.addAttribute("violationList", violationList);
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveViolation", action)) {
			Violation aViolation = retrieveViolation(request);
			return gson.toJson(aViolation);
		} else if (StringUtils.equals("retrieveDriverCompTerm", action)) {
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
	
	private Violation retrieveViolation(HttpServletRequest request) {
		Long violationId = Long.valueOf(request.getParameter("violationId"));
		return genericDAO.getById(Violation.class, violationId);
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
				@ModelAttribute("modelObject") RoadsideInspection entity) {
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
	
	private boolean docsUploaded(RoadsideInspection entity) {
		String[] filaeNamesList = getUploadedFileNames(entity);
		return (filaeNamesList.length > 0) ? true : false;
	}
	
	@RequestMapping("/managedocs/downloaddoc.do")
	public String downloadDoc(ModelMap model, HttpServletRequest request, HttpServletResponse response,
				@ModelAttribute("modelObject") RoadsideInspection entity) {
		try {
			processDocDownload(request, response, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void processDocDownload(HttpServletRequest request,
         HttpServletResponse response, @ModelAttribute("modelObject") RoadsideInspection entity) {
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
			@RequestParam(value = "id") Long id) {
		Violation violation = genericDAO.getById(Violation.class, id);
		//RoadsideInspection entity = genericDAO.getById(RoadsideInspection.class, violation.getRoadsideInspection().getId());
		RoadsideInspection entity = violation.getRoadsideInspection();
		model.addAttribute("modelObject", entity);
		
		setupManageDocs(model, entity);
		return urlContext + "/manageDocs";
	}
	
	private void setupManageDocs(ModelMap model, RoadsideInspection entity) {
		String[] fileNamesList = getUploadedFileNames(entity);
		model.addAttribute("fileList", fileNamesList);
	}
	
	private String[] getUploadedFileNames(RoadsideInspection entity) {
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
	
	private boolean validateUploadDoc(List<String> errorList, RoadsideInspection entity, MultipartFile file) {
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
			@ModelAttribute("modelObject") RoadsideInspection entity,
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
				model.addAttribute("msg", "Successfully uploaded Roadside Inspection pdf");
			} 
		} catch (Exception ex) {
			log.warn("Unable to upload Roadside Inspection doc:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Roadside Inspection pdf!!");
		}
		
		setupManageDocs(model, entity);
		return urlContext + "/manageDocs";
	}
	
	private void saveDoc(HttpServletRequest request, RoadsideInspection entity, MultipartFile file,
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
	
	private String constructDocFilePath(RoadsideInspection entity) {
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
