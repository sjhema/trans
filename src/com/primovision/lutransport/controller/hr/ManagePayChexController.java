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

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.PayChex;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayFreezWrapper;
import com.primovision.lutransport.model.hrreport.DriverPayroll;
import com.primovision.lutransport.model.hrreport.PayChexDetail;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

@Controller
@RequestMapping("/hr/paychexmanage")
public class ManagePayChexController extends CRUDController<PayChex>{

	public ManagePayChexController() {
		// TODO Auto-generated constructor stub
	    setUrlContext("/hr/paychexmanage");
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
		model.addAttribute("list",genericDAO.search(getEntityClass(),criteria,"checkDate desc,companyLocation.name asc,terminalLocation",false));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "checkDate", "checkDate");
		
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria,"checkDate desc,companyLocation.name asc,terminalLocation",false));
		criteria.getSearchMap().put("checkDate", request.getParameter("checkDate"));
		
		return urlContext + "/list";
	}
	@RequestMapping(value="/download.do")
	public String downlodinvoice(HttpServletRequest request,Model model,HttpServletResponse response){
		String invoiceId=request.getParameter("id");
		String type=request.getParameter("type");
		PayChex paychex=genericDAO.getById(PayChex.class, Long.valueOf(invoiceId));
		try{
			Map criteria = new HashMap();
			Map params = new HashMap();
			criteria.put("companyLocation", paychex.getCompanyLocation());
			criteria.put("checkDate", paychex.getCheckDate());
			
			
			if(paychex.getTerminalLocation()!=null)
				criteria.put("terminalLocation", paychex.getTerminalLocation());
			
			List<PayChexDetail> datas=genericDAO.findByCriteria(PayChexDetail.class, criteria);
			if (datas!=null && datas.size()>0) {
			
			
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("paychex",
					datas, params, type, request);
				}
			if (type.equals("csv")){
				out = dynamicReportService.generateStaticReport("paychex",
					datas, params, type, request);
				}
				else{
					out = dynamicReportService.generateStaticReport("paychex",
							datas, params, type, request);
				}
				response.setHeader("Content-Disposition",
					"attachment;filename=paychex"+paychex.getCompanyLocation().getName()+"."+type);
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
			System.out.println("Unable to download Pay Chex. Plesae try again later");
			request.getSession().setAttribute("errors", "Unable to download Pay Chex. Plesae try again later");
			return "error";
		}
		//return null;
	}
	@Override
	public String delete(@ModelAttribute("modelObject") PayChex entity,
			BindingResult bindingResult, HttpServletRequest request) {
		   try{
			   PayChex paychex=genericDAO.getById(PayChex.class,entity.getId());
			   
			   Map criteria = new HashMap();
			   criteria.put("companyLocation", paychex.getCompanyLocation());
			   criteria.put("checkDate", paychex.getCheckDate());
				
				
				if(paychex.getTerminalLocation()!=null)
					criteria.put("terminalLocation", paychex.getTerminalLocation());
				
				List<PayChexDetail> datas=genericDAO.findByCriteria(PayChexDetail.class, criteria);
				if (datas!=null && datas.size()>0) {
					for(PayChexDetail pay:datas){
						genericDAO.delete(pay);
					}
					
					genericDAO.delete(paychex);
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
