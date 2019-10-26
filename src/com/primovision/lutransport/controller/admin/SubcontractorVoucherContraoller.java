package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.SubcontractorInvoice;
import com.primovision.lutransport.model.report.SubcontractorBilling;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@RequestMapping("/admin/subcontractorbilling")
public class SubcontractorVoucherContraoller extends CRUDController<SubcontractorInvoice>{
	public SubcontractorVoucherContraoller() {
		setUrlContext("/admin/subcontractorbilling");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		criterias.put("type", 1);
		model.addAttribute("transferStations", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfills", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class,	criterias, "name", false));
		criterias.put("type", 3);
		model.addAttribute("companyLocation",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "VoucherDate", "voucherDate");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria));
		criteria.getSearchMap().put("VoucherDate", request.getParameter("VoucherDate"));
		return urlContext + "/list";
	}
	
	@RequestMapping(value="/download.do")
	public String downlodinvoice(HttpServletRequest request,Model model,HttpServletResponse response){
		String invoiceId=request.getParameter("id");
		String type=request.getParameter("type");
		SubcontractorInvoice invoice=genericDAO.getById(SubcontractorInvoice.class, Long.valueOf(invoiceId));
		try{
		Map criteria = new HashMap();
		Map params = new HashMap();
		String misclecharg = null;
		criteria.put("invoiceNo", invoice.getVoucherNumber());
		criteria.put("subcontractorId",invoice.getSubContractorId());
		criteria.put("companyLocationId", invoice.getCompanyLocationId());
		List<SubcontractorBilling> datas = genericDAO.findByCriteria(SubcontractorBilling.class,criteria);
		if (datas!=null && datas.size()>0) {
		params.put("invoiceNo", datas.get(0).getInvoiceNo());
		params.put("origin", datas.get(0).getOrigin());
		params.put("destination",datas.get(0).getDestination());
		params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(invoice.getVoucherDate()));
		params.put("sumBillableTon", invoice.getSumBillableTon());
		params.put("sumOriginTon", invoice.getSumOriginTon());
		params.put("sumDestinationTon", invoice.getSumDestinationTon());
		params.put("sumAmount", invoice.getSumAmount());
		params.put("sumFuelSurcharge", invoice.getSumFuelSurcharge());
		params.put("sumTotal", invoice.getSumTotal());
		params.put("sumOtherCharges", invoice.getSumOtherCharges());
		params.put("grandTotal", invoice.getGrandTotal());
		params.put("subcontractorname", invoice.getSubcontractorname());
		params.put("address1", invoice.getAddress1());
		params.put("address2", invoice.getAddress2());
		params.put("city", invoice.getCity());
		params.put("zipcode", invoice.getZipcode());
		params.put("phone", invoice.getPhone());
		params.put("fax", invoice.getFax());
		params.put("state", invoice.getState());
		
		if(!StringUtils.isEmpty(invoice.getMiscelleneousNote())){			
			String[] miscellNotes=invoice.getMiscelleneousNote().split(",");			
			for(int i=0;i<miscellNotes.length;i++){
			params.put("misceNote"+i,miscellNotes[i]);	
			
		}
		}
			if(!StringUtils.isEmpty(invoice.getMiscelleneousCharges())){
				String[] miscellCharges=invoice.getMiscelleneousCharges().split(",");
				for(int i=0;i<miscellCharges.length;i++){				
					params.put("misceCharges"+i,"$"+miscellCharges[i]);				
					}
			}
		
		/*params.put("misceNote", invoice.getMiscelleneousNote());*/
		//params.put("companyname", invoice.getCompanyname());
		params.put("companyname", invoice.getCompanyLocationId().getName());
		/*if(!StringUtils.isEmpty(invoice.getMiscelleneousCharges()))
			misclecharg="$"+invoice.getMiscelleneousCharges();
		
		params.put("misceCharges", misclecharg);*/
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (type.equals("pdf")){
			out = dynamicReportService.generateStaticReport("subcontractorbillingpdf",
				datas, params, type, request);
			}
		if (type.equals("xls")){
			out = dynamicReportService.generateStaticReport("subcontractorbillingxls",
				datas, params, type, request);
			}
		if (type.equals("csv")){
			out = dynamicReportService.generateStaticReport("subcontractorbillingcsv",
				datas, params, type, request);
			}
			else{
				out = dynamicReportService.generateStaticReport("subcontractorbilling",
						datas, params, type, request);
			}
			response.setHeader("Content-Disposition",
				"attachment;filename=vouc" + invoice.getVoucherNumber() + "."+type);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		
		}
		else {
			request.getSession().setAttribute("errors", "Unable to download voucher. Plesae try again later");
			return "error";
		}
	}catch(Exception ex){
		ex.printStackTrace();
		request.getSession().setAttribute("errors", "Unable to download voucher. Plesae try again later");
		return "error";
	}
		
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") SubcontractorInvoice entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			SubcontractorInvoice invoice=genericDAO.getById(SubcontractorInvoice.class, Long.valueOf(entity.getId()));
			reportService.deleteSubcontractorBillingData(invoice);
			
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@ModelAttribute("modelObject")
	public SubcontractorInvoice setupModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
			SubcontractorInvoice invoice = getEntityInstance();
			invoice.setVoucherNumber(new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
		return invoice;
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}

}
