package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
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

import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.PaymentUtil;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.equipment.EquipmentBuyer;
import com.primovision.lutransport.model.equipment.EquipmentLender;
import com.primovision.lutransport.model.equipment.EquipmentReportInput;
import com.primovision.lutransport.model.equipment.EquipmentReportOutput;
import com.primovision.lutransport.model.equipment.VehicleLoan;
import com.primovision.lutransport.model.equipment.VehicleSale;
import com.primovision.lutransport.model.equipment.VehicleTitle;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@RequestMapping("/admin/equipment/report/equipmentreport")
public class EquipmentReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public EquipmentReportController() {
		setUrlContext("admin/equipment/report/equipmentreport");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	//private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null && criteria.getSearchMap() != null) {
				criteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		return "admin/equipment/report/" + "equipmentReport";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") EquipmentReportInput input) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(5000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request, input);
		List<EquipmentReportOutput> equipmentReportOutputList = (List<EquipmentReportOutput>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("equipmentReport",
					equipmentReportOutputList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "admin/equipment/report/equipmentreport/"+type;
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
		
		EquipmentReportInput input = (EquipmentReportInput) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateData(criteria, request, input);
		List<EquipmentReportOutput> equipmentReportOutputList = (List<EquipmentReportOutput>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=equipmentReport." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport("equipmentReport",
					equipmentReportOutputList, params, type, request);
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
			HttpServletRequest request, EquipmentReportInput input) {
		List<EquipmentReportOutput> equipmentReportOutputList = performSearch(searchCriteria, input); 
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		Location company = retrieveCompany(input.getCompany());
		String companyName = company == null ? StringUtils.EMPTY : company.getName();
		params.put("company", companyName);
		
		EquipmentLender equipmentLender = retrieveLender(input.getLender());
		String lenderName = equipmentLender == null ? StringUtils.EMPTY : equipmentLender.getName();
		params.put("lender", lenderName);
		
		EquipmentBuyer equipmentBuyer = retrieveBuyer(input.getBuyer());
		String buyerName = equipmentBuyer == null ? StringUtils.EMPTY : equipmentBuyer.getName();
		params.put("buyer", buyerName);
		
		Location titleOwner = retrieveCompany(input.getTitleOwner());
		String titleOwnerName = titleOwner == null ? StringUtils.EMPTY : titleOwner.getName();
		params.put("titleOwner", titleOwnerName);
		
		Location registeredOwner = retrieveCompany(input.getRegisteredOwner());
		String registeredOwnerName = registeredOwner == null ? StringUtils.EMPTY : registeredOwner.getName();
		params.put("registeredOwner", registeredOwnerName);
		
		String loanDateRange = StringUtils.isEmpty(input.getLoanStartDate()) ? StringUtils.EMPTY : input.getLoanStartDate();
		loanDateRange += " - ";
		loanDateRange += StringUtils.isEmpty(input.getLoanEndDate()) ? StringUtils.EMPTY : input.getLoanEndDate();
		params.put("loanDateRange", loanDateRange);
		
		params.put("noOfLoans", new Integer(equipmentReportOutputList.size()));
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", equipmentReportOutputList);
		data.put("params", params);
		     
		return data;
	}
	
	private Location retrieveCompany(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(Location.class, Long.valueOf(id));
	}
	
	private EquipmentLender retrieveLender(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(EquipmentLender.class, Long.valueOf(id));
	}
	
	private EquipmentBuyer retrieveBuyer(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(EquipmentBuyer.class, Long.valueOf(id));
	}
	
	private List<EquipmentReportOutput> performSearch(SearchCriteria criteria, EquipmentReportInput input) {
		String company = input.getCompany();
		String vehicle = input.getVehicle();
		String includeSoldVehiclesStr = input.getIncludeSoldVehicle();
		
		StringBuffer query = new StringBuffer("select obj from Vehicle obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Vehicle obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.owner=" + company);
		}
		
		if (StringUtils.isNotEmpty(vehicle)) {
			whereClause.append(" and obj.unit=" + vehicle);
		}
		
		boolean includeSoldVehicles = true;
		if (StringUtils.isNotEmpty(includeSoldVehiclesStr)) {
			includeSoldVehicles = BooleanUtils.toBoolean(includeSoldVehiclesStr);
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.unit asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Vehicle> vehicleList = 
				genericDAO.getEntityManager().createQuery(query.toString())
					.setMaxResults(criteria.getPageSize())
					.setFirstResult(criteria.getPage() * criteria.getPageSize())
					.getResultList();
		
		List<EquipmentReportOutput> equipmentReportOutputList = new ArrayList<EquipmentReportOutput>();
		map(equipmentReportOutputList, vehicleList, includeSoldVehicles);
		sort(equipmentReportOutputList);
		
		return equipmentReportOutputList;
	}
	
	private void sort(List<EquipmentReportOutput> equipmentReportOutputList) {
		if (equipmentReportOutputList == null || equipmentReportOutputList.isEmpty()) {
			return;
		}
		
		Collections.sort(equipmentReportOutputList, new Comparator<EquipmentReportOutput>() {
			@Override
			public int compare(final EquipmentReportOutput record1, final EquipmentReportOutput record2) {
				String loanNo1 = StringUtils.isEmpty(record1.getLoanNo()) ? StringUtils.EMPTY : record1.getLoanNo();
				String loanNo2 = StringUtils.isEmpty(record2.getLoanNo()) ? StringUtils.EMPTY : record2.getLoanNo();
				Integer unit1 = record1.getUnit();
				Integer unit2 = record2.getUnit();
				
				int c = loanNo1.compareTo(loanNo2);
				if (c == 0) {
					c = ((unit1 == null) ?
					         (unit2 == null ? 0 : -1):
					         (unit2 == null ? 1 : unit1.compareTo(unit2)));
				}
				
				return c;
			}
		});
	}
	
	private void map(List<EquipmentReportOutput> equipmentReportOutputList, List<Vehicle> vehicleList,
			boolean includeSoldVehicles) {
		if (vehicleList == null || vehicleList.isEmpty()) {
			return;
		}
		
		for (Vehicle aVehicle : vehicleList) {
			VehicleLoan aVehicleLoan = retrieveVehicleLoan(aVehicle.getId());
			VehicleTitle aVehicleTitle = retrieveVehicleTitle(aVehicle.getId());
			VehicleSale aVehicleSale = retrieveVehicleSale(aVehicle.getId());
			
			if (!includeSoldVehicles && aVehicleSale != null) {
				continue;
			}
			if (aVehicleLoan == null && aVehicleTitle == null && aVehicleSale == null) {
				continue;
			}
			
			if (aVehicleLoan != null) {
				int noOfPaymentsLeft = PaymentUtil.calculateNoOfPaymentsLeft(aVehicleLoan.getNoOfPayments(), 
						aVehicleLoan.getEndDate(), aVehicleLoan.getPaymentDueDom());
				aVehicleLoan.setPaymentsLeft(noOfPaymentsLeft);
			}
			
			EquipmentReportOutput aEquipmentReportOutput = new EquipmentReportOutput();
			map(aEquipmentReportOutput, aVehicle);
			map(aEquipmentReportOutput, aVehicleLoan);
			map(aEquipmentReportOutput, aVehicleTitle);
			map(aEquipmentReportOutput, aVehicleSale);
			
			equipmentReportOutputList.add(aEquipmentReportOutput);
		}
	}
	
	private void map(EquipmentReportOutput equipmentReportOutput, Vehicle vehicle) {
		equipmentReportOutput.setUnit(vehicle.getUnit());
		equipmentReportOutput.setCompany(vehicle.getOwner().getName());
		equipmentReportOutput.setVin(vehicle.getVinNumber());
		equipmentReportOutput.setYear(vehicle.getYear());
		equipmentReportOutput.setMake(vehicle.getMake());
		equipmentReportOutput.setModel(vehicle.getModel());
	}
	
	private void map(EquipmentReportOutput equipmentReportOutput, VehicleLoan vehicleLoan) {
		if (vehicleLoan == null) {
			return;
		}
		
		equipmentReportOutput.setLoanNo(vehicleLoan.getLoanNo());
		equipmentReportOutput.setLender(vehicleLoan.getLender().getName());
		
		equipmentReportOutput.setPaymentAmount(vehicleLoan.getPaymentAmount());
		equipmentReportOutput.setPaymentDueDom(vehicleLoan.getPaymentDueDom());
		equipmentReportOutput.setLoanStartDate(dateFormat.format(vehicleLoan.getStartDate()));
		equipmentReportOutput.setLoanEndDate(dateFormat.format(vehicleLoan.getEndDate()));
		equipmentReportOutput.setInterestRate(vehicleLoan.getInterestRate());
		equipmentReportOutput.setNoOfPayments(vehicleLoan.getNoOfPayments());
		equipmentReportOutput.setPaymentsLeft(vehicleLoan.getPaymentsLeft());
	}
	
	private void map(EquipmentReportOutput equipmentReportOutput, VehicleTitle vehicleTitle) {
		if (vehicleTitle == null) {
			return;
		}
		
		equipmentReportOutput.setHoldsTitle(vehicleTitle.getHoldsTitle());
		equipmentReportOutput.setTitleOwner(vehicleTitle.getTitleOwner().getName());
		equipmentReportOutput.setRegisteredOwner(vehicleTitle.getRegisteredOwner().getName());
		
		String title = StringUtils.isEmpty(vehicleTitle.getTitle()) ? StringUtils.EMPTY : vehicleTitle.getTitle();
		equipmentReportOutput.setTitle(title);
	}
	
	private void map(EquipmentReportOutput equipmentReportOutput, VehicleSale vehicleSale) {
		if (vehicleSale == null) {
			return;
		}
		
		equipmentReportOutput.setBuyer(vehicleSale.getBuyer().getName());
		equipmentReportOutput.setSaleDate(dateFormat.format(vehicleSale.getSaleDate()));
		equipmentReportOutput.setSalePrice(vehicleSale.getSalePrice());
	}
	
	private VehicleLoan retrieveVehicleLoan(Long vehicleId) {
		String query = "select obj from VehicleLoan obj where obj.vehicle.id=" + vehicleId
								+ " order by id desc";
		List<VehicleLoan> vehicleLoanList = genericDAO.executeSimpleQuery(query);
		
		return (vehicleLoanList.isEmpty() ? null : vehicleLoanList.get(0));
	}
	
	private VehicleTitle retrieveVehicleTitle(Long vehicleId) {
		String query = "select obj from VehicleTitle obj where obj.vehicle.id=" + vehicleId
								+ " order by id desc";
		List<VehicleTitle> vehicleTitleList = genericDAO.executeSimpleQuery(query);
		
		return (vehicleTitleList.isEmpty() ? null : vehicleTitleList.get(0));
	}
	
	private VehicleSale retrieveVehicleSale(Long vehicleId) {
		String query = "select obj from VehicleSale obj where obj.vehicle.id=" + vehicleId
								+ " order by id desc";
		List<VehicleSale> vehicleSaleList = genericDAO.executeSimpleQuery(query);
		
		return (vehicleSaleList.isEmpty() ? null : vehicleSaleList.get(0));
	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		model.addAttribute("owners", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.clear();
		
		model.addAttribute("lenders", genericDAO.findByCriteria(EquipmentLender.class, criterias, "name", false));
		model.addAttribute("buyers", genericDAO.findByCriteria(EquipmentBuyer.class, criterias, "name", false));
		
		model.addAttribute("titles", genericDAO.findByCriteria(VehicleTitle.class, criterias, "title", false));
	
		String loanQuery = "select distinct obj.loanNo from VehicleLoan obj order by obj.loanNo asc";
		model.addAttribute("vehicleLoans", genericDAO.executeSimpleQuery(loanQuery));
	}
	
	@ModelAttribute("modelObject")
	public EquipmentReportInput setupModel(HttpServletRequest request) {
		return new EquipmentReportInput();
	}
	
	/*private List<EquipmentReportOutput> performSearch(SearchCriteria criteria, EquipmentReportInput input) {
		String loanNo = input.getLoanNo();
		String company = input.getCompany();
		String lender = input.getLender();
		String vehicle = input.getVehicle();
		String loanStartDate = input.getLoanStartDate();
		String loanEndDate = input.getLoanEndDate();
		
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
	   
	   addTitleCriteria(whereClause, input);
	   addSaleCriteria(whereClause, input);
	   
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
		
		List<EquipmentReportOutput> equipmentReportOutputList = new ArrayList<EquipmentReportOutput>();
		map(equipmentReportOutputList, vehicleLoanList);
		
		return equipmentReportOutputList;
	}

	private void addTitleCriteria(StringBuffer whereClause, EquipmentReportInput input) {
		String titleOwner = input.getTitleOwner();
		String registeredOwner = input.getRegisteredOwner();
		String holdsTitle = input.getHoldsTitle();
		String title = input.getTitle();
		
		if (StringUtils.isEmpty(titleOwner) && StringUtils.isEmpty(registeredOwner)
				&& StringUtils.isEmpty(holdsTitle) && StringUtils.isEmpty(title)) {
			return;
		}
		
		StringBuffer titleQuery = new StringBuffer("select obj from VehicleTitle obj where 1=1");
		StringBuffer titleWhereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(titleOwner)) {
			titleWhereClause.append(" and obj.titleOwner=" + titleOwner);
		}
		if (StringUtils.isNotEmpty(registeredOwner)) {
			titleWhereClause.append(" and obj.registeredOwner=" + registeredOwner);
		}
		if (StringUtils.isNotEmpty(holdsTitle)) {
			titleWhereClause.append(" and obj.holdsTitle='" + holdsTitle + "'");
		}
		if (StringUtils.isNotEmpty(title)) {
			titleWhereClause.append(" and obj.id=" + title);
		}
		
		titleQuery.append(titleWhereClause);
		
		List<VehicleTitle> vehicleTitleList = genericDAO.executeSimpleQuery(titleQuery.toString());
		if (vehicleTitleList.isEmpty()) {
			return;
		}
		
		StringBuffer vehicleIdsBuff = new StringBuffer();
		for (VehicleTitle aVehicleTitle : vehicleTitleList) {
			vehicleIdsBuff.append(aVehicleTitle.getVehicle().getId() + ",");
		}
		
		String vehicleIds = vehicleIdsBuff.toString();
		vehicleIds = vehicleIds.substring(0, vehicleIds.length()-1);
		whereClause.append(" and obj.vehicle.id in (" + vehicleIds + ")");
	}
	
	private void addSaleCriteria(StringBuffer whereClause, EquipmentReportInput input) {
		String buyer = input.getBuyer();
		
		if (StringUtils.isEmpty(buyer)) {
			return;
		}
		
		StringBuffer saleQuery = new StringBuffer("select obj from VehicleSale obj where 1=1");
		StringBuffer saleWhereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(buyer)) {
			saleWhereClause.append(" and obj.buyer=" + buyer);
		}
		
		saleQuery.append(saleWhereClause);
		
		List<VehicleSale> vehicleSaleList = genericDAO.executeSimpleQuery(saleQuery.toString());
		if (vehicleSaleList.isEmpty()) {
			return;
		}
		
		StringBuffer vehicleIdsBuff = new StringBuffer();
		for (VehicleSale aVehicleSale : vehicleSaleList) {
			vehicleIdsBuff.append(aVehicleSale.getVehicle().getId() + ",");
		}
		
		String vehicleIds = vehicleIdsBuff.toString();
		vehicleIds = vehicleIds.substring(0, vehicleIds.length()-1);
		whereClause.append(" and obj.vehicle.id in (" + vehicleIds + ")");
	}
	
	private void map(List<EquipmentReportOutput> equipmentReportOutputList, List<VehicleLoan> vehicleLoanList) {
		for (VehicleLoan aVehicleLoan : vehicleLoanList) {
			int noOfPaymentsLeft = PaymentUtil.calculateNoOfPaymentsLeft(aVehicleLoan.getNoOfPayments(), 
					aVehicleLoan.getEndDate(), aVehicleLoan.getPaymentDueDom());
			aVehicleLoan.setPaymentsLeft(noOfPaymentsLeft);
			
			EquipmentReportOutput aEquipmentReportOutput = new EquipmentReportOutput();
			map(aEquipmentReportOutput, aVehicleLoan);
			
			VehicleTitle aVehicleTitle = retrieveVehicleTitle(aVehicleLoan.getVehicle().getId());
			map(aEquipmentReportOutput, aVehicleTitle);
			
			VehicleSale aVehicleSale = retrieveVehicleSale(aVehicleLoan.getVehicle().getId());
			map(aEquipmentReportOutput, aVehicleSale);
			
			equipmentReportOutputList.add(aEquipmentReportOutput);
		}
	}*/
}
