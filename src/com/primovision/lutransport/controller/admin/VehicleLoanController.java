package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

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
      
      query.append(" order by obj.id asc");
      
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
		Map criterias = new HashMap();
		
		// select obj from Vehicle obj where obj.type=1 group by obj.unit
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
		model.addAttribute("lenders", genericDAO.findByCriteria(EquipmentLender.class, criterias, "name", false));
	
		model.addAttribute("paymentDueDates", buildPaymentDueDates());
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		
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
	
	private void populateLender(VehicleLoan entity) {
		String lenderName = StringUtils.replace(entity.getLenderName(), "'", "''", -1);
		lenderName = StringUtils.upperCase(lenderName);
		String query = "select obj from EquipmentLender obj "
						+ " where UPPER(obj.name)='" + lenderName + "' order by id desc";
		List<EquipmentLender> lenderList = genericDAO.executeSimpleQuery(query);
		
		EquipmentLender lender = null;
		if (lenderList.isEmpty()) {
			lender = addLender(entity);
		} else {
			lender = lenderList.get(0);
		}
		
		entity.setLender(lender);
	}
	
	private EquipmentLender addLender(VehicleLoan entity) {
		EquipmentLender lender = new EquipmentLender();
		lender.setName(entity.getLenderName());
		lender.setAddress1("address1");
		lender.setCity("Chicago");
		
		State state = retrieveState("IL");
		lender.setState(state);
		
		lender.setZipcode("60632");
		
		lender.setCreatedAt(entity.getCreatedAt());
		lender.setCreatedBy(entity.getCreatedBy());
		
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
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + urlContext + "Report." + type);
		}
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<VehicleLoan> vehicleLoanList = performSearch(criteria);
		
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
}
