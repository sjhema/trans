package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.tags.StaticDataUtil;
import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehicleReport;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@RequestMapping("/admin/vehicle/report/vehiclereport")
public class VehicleReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public VehicleReportController() {
		setUrlContext("admin/vehicle/report/vehiclereport");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null && criteria.getSearchMap() != null) {
				criteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		return "admin/vehicle/report/" + "vehicleReport";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") VehicleReport input) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(2500);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request, input);
		List<VehicleReport> vehicleReportList = (List<VehicleReport>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "vehicleReport";
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					vehicleReportList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "admin/vehicle/report/vehiclereport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		VehicleReport input = (VehicleReport) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateData(criteria, request, input);
		List<VehicleReport> vehicleReportList = (List<VehicleReport>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "vehicleReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
					vehicleReportList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Map<String, Object> generateData(SearchCriteria searchCriteria, 
			HttpServletRequest request, VehicleReport input) {
		List<VehicleReport> vehicleReportList = performSearch(searchCriteria, input); 
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		Location company = retrieveCompany(input.getCompany());
		String companyName = company == null ? StringUtils.EMPTY : company.getName();
		params.put("company", companyName);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", vehicleReportList);
		data.put("params", params);
		     
		return data;
	}
	
	private Location retrieveCompany(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(Location.class, Long.valueOf(id));
	}
	
	private List<VehicleReport> performSearch(SearchCriteria criteria, VehicleReport input) {
		String company = input.getCompany();
		String vehicle = input.getVehicle();
		String feature = input.getFeature();
		String activeStatus = input.getActiveStatus();
		
		StringBuffer query = new StringBuffer("select obj from Vehicle obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Vehicle obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.owner=" + company);
		}
		
		if (StringUtils.isNotEmpty(vehicle)) {
			whereClause.append(" and obj.unit=" + vehicle);
		}
		
		if (StringUtils.isNotEmpty(feature)) {
			whereClause.append(" and obj.feature='" + feature + "'");
		}
		
		if (StringUtils.isEmpty(activeStatus)) {
			activeStatus = "1";
		}
		whereClause.append(" and obj.activeStatus=" + activeStatus);
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.unit asc, id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Vehicle> vehicleList = 
				genericDAO.getEntityManager().createQuery(query.toString())
					.setMaxResults(criteria.getPageSize())
					.setFirstResult(criteria.getPage() * criteria.getPageSize())
					.getResultList();
		
		List<VehicleReport> vehicleReportList = new ArrayList<VehicleReport>();
		map(vehicleReportList, vehicleList);
		
		return vehicleReportList;
	}
	
	private void map(List<VehicleReport> vehicleReportList, List<Vehicle> vehicleList) {
		if (vehicleList == null || vehicleList.isEmpty()) {
			return;
		}
		
		Map<String, String> vehicleMap = new HashMap<String, String>();
		for (Vehicle aVehicle : vehicleList) {
			String key = aVehicle.getUnit() + "_" + aVehicle.getVinNumber();
			if (vehicleMap.containsKey(key)) {
				continue;
			} else {
				vehicleMap.put(key, key);
				
				VehicleReport aVehicleReport = new VehicleReport();
				map(aVehicleReport, aVehicle);
				
				vehicleReportList.add(aVehicleReport);
			}
		}
	}
	
	private void map(VehicleReport aVehicleReport, Vehicle vehicle) {
		aVehicleReport.setVehicle(
				vehicle.getUnit() == null ? StringUtils.EMPTY : String.valueOf(vehicle.getUnit()));
		aVehicleReport.setCompany(vehicle.getOwner() == null ? StringUtils.EMPTY : vehicle.getOwner().getName());
		
		aVehicleReport.setVin(vehicle.getVinNumber());
		aVehicleReport.setYear(vehicle.getYear());
		aVehicleReport.setMake(vehicle.getMake());
		aVehicleReport.setModel(vehicle.getModel());
		
		String feature = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(vehicle.getFeature())) {
			feature = StaticDataUtil.getText("VEHICLE_FEATURE", vehicle.getFeature());
		}
		aVehicleReport.setFeature(feature);
		
		String activeStatus = StringUtils.EMPTY;
		if (vehicle.getActiveStatus() != null) {
			activeStatus = StaticDataUtil.getText("VEHICLE_STATUS", String.valueOf(vehicle.getActiveStatus()));
		}
		aVehicleReport.setActiveStatus(activeStatus);
		
		String inactiveDate = StringUtils.EMPTY;
		if (vehicle.getInactiveDate() != null) {
			inactiveDate = dateFormat.format(vehicle.getInactiveDate());
		}
		aVehicleReport.setInactiveDate(inactiveDate); 
	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		model.addAttribute("features", listStaticData("VEHICLE_FEATURE"));
		model.addAttribute("activeStauses", listStaticData("VEHICLE_STATUS"));
	}
	
	@ModelAttribute("modelObject")
	public VehicleReport setupModel(HttpServletRequest request) {
		return new VehicleReport();
	}
}
