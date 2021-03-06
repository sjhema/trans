package com.primovision.lutransport.controller.hr;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayroll;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

@Controller
@RequestMapping("/hr/driverpay")
public class ManageDriverpayContoller extends CRUDController<DriverPayroll>{
	public ManageDriverpayContoller() {
		// TODO Auto-generated constructor stub
	    setUrlContext("/hr/driverpay");
	}
	@Autowired
	private DateUpdateService dateupdateService;
	
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	@Autowired
	protected HrReportService hrReportService;
	
	public void setHrReportService(
			HrReportService hrReportService) {
		this.hrReportService = hrReportService;
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
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(),criteria,"payRollBatch",true));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "PayRollBatch", "payRollBatch");
		dateupdateService.updateDate(request, "BillBatchFrom", "billBatchFrom");
		dateupdateService.updateDate(request, "BillBatchTo", "billBatchTo");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria,"payRollBatch",true));
		criteria.getSearchMap().put("PayRollBatch", request.getParameter("PayRollBatch"));
		criteria.getSearchMap().put("BillBatchFrom", request.getParameter("BillBatchFrom"));
		criteria.getSearchMap().put("BillBatchTo", request.getParameter("BillBatchTo"));
		return urlContext + "/list";
	}
	@RequestMapping(value="/download.do")
	public String downlodinvoice(HttpServletRequest request,Model model,HttpServletResponse response){
		String invoiceId=request.getParameter("id");
		String type=request.getParameter("type");
		DriverPayroll payroll=genericDAO.getById(DriverPayroll.class, Long.valueOf(invoiceId));
		try{
			Map criteria = new HashMap();
			Map params = new HashMap();
			criteria.put("company", payroll.getCompany());
			criteria.put("payRollBatch", payroll.getPayRollBatch());
			criteria.put("billBatchDateFrom", payroll.getBillBatchFrom());
			criteria.put("billBatchDateTo", payroll.getBillBatchTo());
			
			if(payroll.getTerminal()!=null)
				criteria.put("terminal", payroll.getTerminal());
			List<DriverPay> datas=genericDAO.findByCriteria(DriverPay.class, criteria);
			if (datas!=null && datas.size()>0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String billbatchfrom=dateFormat.format(payroll.getBillBatchFrom());
			String billbatchto=dateFormat.format(payroll.getBillBatchTo());
			String payrollbatch=dateFormat.format(payroll.getPayRollBatch());
			params.put("totalRowCount", payroll.getTotalRowCount());
			params.put("company", payroll.getCompany().getName());
			params.put("batchDateFrom", billbatchfrom);
			params.put("batchDateTo",billbatchto);
			params.put("sumTotal",payroll.getSumTotal());
			params.put("payRollBatch", payrollbatch);
			params.put("sumAmount", payroll.getSumAmount());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("driverpayall",
					datas, params, type, request);
				}
			if (type.equals("csv")){
				out = dynamicReportService.generateStaticReport("driverpayall",
					datas, params, type, request);
				}
				else{
					out = dynamicReportService.generateStaticReport("driverpayall",
							datas, params, type, request);
				}
				response.setHeader("Content-Disposition",
					"attachment;filename=driverpay"+payroll.getCompany().getName()+"."+type);
				out.writeTo(response.getOutputStream());
				out.close();
				return null;
			}
			else {
				request.getSession().setAttribute("errors", "Unable to download Driver pay. Plesae try again later");
				return "error";
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Unable to download Driver pay. Plesae try again later");
			request.getSession().setAttribute("errors", "Unable to download Driver pay. Plesae try again later");
			return "error";
		}
		//return null;
	}
	@Override
	public String delete(@ModelAttribute("modelObject") DriverPayroll entity,
			BindingResult bindingResult, HttpServletRequest request) {
		   try{
			   DriverPayroll driverPayroll=genericDAO.getById(DriverPayroll.class,entity.getId());
			   
			   hrReportService.deleteDriverPayData(driverPayroll);
			   
			   Map criteria = new HashMap();
				criteria.put("company", driverPayroll.getCompany());
				criteria.put("payRollBatch",driverPayroll.getPayRollBatch());
				criteria.put("billBatchDateFrom",driverPayroll.getBillBatchFrom());
				criteria.put("billBatchDateTo",driverPayroll.getBillBatchTo());
				if(driverPayroll.getTerminal()!=null)
				   criteria.put("terminal", driverPayroll.getTerminal());
				List<DriverPayFreezWrapper> datas=genericDAO.findByCriteria(DriverPayFreezWrapper.class, criteria);
				if (datas!=null && datas.size()>0) {
					for(DriverPayFreezWrapper pay:datas){
						genericDAO.delete(pay);
					}
				}
				
		   } catch (Exception ex) {
				request.getSession().setAttribute(
						"errors",
						"This" + entity.getClass().getSimpleName()
								+ " can't be deleted");
				log.warn("Error deleting record " + entity.getId(), ex);
			}
		
			return "redirect:/" + urlContext + "/list.do";
		
	}
}
