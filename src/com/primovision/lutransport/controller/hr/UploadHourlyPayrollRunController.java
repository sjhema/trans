package com.primovision.lutransport.controller.hr;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.configuration.Configuration;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.HourlyPayrollInvoice;
import com.primovision.lutransport.model.hrreport.HourlyPayrollInvoiceDetails;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@RequestMapping("/hr/uploadpayrollrun")
public class UploadHourlyPayrollRunController extends CRUDController<HourlyPayrollInvoice> {
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
	public UploadHourlyPayrollRunController() {
		setUrlContext("hr/uploadpayrollrun");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	@Autowired
	private HrReportService HrreportService;
	
	public void setHrReportService(HrReportService HrreportService) {
		this.HrreportService = HrreportService;
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
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class, false));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
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
	
	
	@ModelAttribute("modelObject")
	public HourlyPayrollInvoice setupModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
			HourlyPayrollInvoice runinvoice = getEntityInstance();
			runinvoice.setPayrollinvoicedate(Calendar.getInstance().getTime());
		return runinvoice;
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"payrollinvoicedate",true));
		return urlContext + "/list";
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "Payrollinvoicedate", "payrollinvoicedate");
		dateupdateService.updateDate(request, "BillBatchFrom", "billBatchFrom");
		dateupdateService.updateDate(request, "BillBatchTo", "billBatchTo");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"payrollinvoicedate",true));
	     criteria.getSearchMap().put("Payrollinvoicedate", request.getParameter("Payrollinvoicedate"));
	     criteria.getSearchMap().put("BillBatchFrom", request.getParameter("BillBatchFrom"));
		 criteria.getSearchMap().put("BillBatchTo", request.getParameter("BillBatchTo"));
		return urlContext + "/list";
	}
	
	// Multiple same batch payroll - 10th July 2020
	private List<HourlyPayrollInvoice> retrieveSameBatchInvoiceHeaders(HourlyPayrollInvoice runInvoice) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payrollinvoicedate", runInvoice.getPayrollinvoicedate());
		params.put("billBatchFrom", runInvoice.getBillBatchFrom());
		params.put("billBatchTo", runInvoice.getBillBatchTo());
		
		if (runInvoice.getCompanyLoc() != null) {
			params.put("companyLoc.id", runInvoice.getCompanyLoc().getId());
		}
		
		if (runInvoice.getTerminal() != null) {
			params.put("terminal.id", runInvoice.getTerminal().getId());
		}
        
		List<HourlyPayrollInvoice> sameBatchInvoiceHeaders = genericDAO.findByCriteria(HourlyPayrollInvoice.class, params, "id desc", false);
		return sameBatchInvoiceHeaders;
	}
	
	// Multiple same batch payroll - 10th July 2020
	private Date determineMinInvoiceCreatedDate(HourlyPayrollInvoice runInvoice) {
		List<HourlyPayrollInvoice> sameBatchInvoiceHeaders = retrieveSameBatchInvoiceHeaders(runInvoice);
		if (sameBatchInvoiceHeaders.size() < 2) {
			return null;
		}
		
		int i = 0;
		for ( ; i < sameBatchInvoiceHeaders.size(); i++) {
			if (sameBatchInvoiceHeaders.get(i).getId().longValue() == runInvoice.getId().longValue()) {
				break;
			}
		}
		
		i++;
		if (i == sameBatchInvoiceHeaders.size()) {
			return null;
		}
		
		return sameBatchInvoiceHeaders.get(i).getCreatedAt();
	}
	
	
	@RequestMapping(value="/downloadrun.do")
	public String downlodrun(HttpServletRequest request,Model model,HttpServletResponse response)
	{
		String payrollRunId=request.getParameter("id");
		String type=request.getParameter("type");
		HourlyPayrollInvoice runinvoice=genericDAO.getById(HourlyPayrollInvoice.class, Long.valueOf(payrollRunId));
		try 
		{
			Map params = new HashMap();
			Map criti = new HashMap();
			List<HourlyPayrollInvoiceDetails> datas=null;
			List<HourlyPayrollInvoiceDetails> datas1= new ArrayList<HourlyPayrollInvoiceDetails>();
			params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(runinvoice.getPayrollinvoicedate()));
			/*/params.put("batchdate",new SimpleDateFormat("yyyy-MM-dd").format(runinvoice.getBillBatchFrom()));
			params.put("batchdateTo",new SimpleDateFormat("yyyy-MM-dd").format(runinvoice.getBillBatchTo()));/*/
			params.put("payRollBatchFrom",runinvoice.getBillBatchFrom());
			params.put("payRollBatchTo",runinvoice.getBillBatchTo());
			
			if(runinvoice.getCompanyLoc()!=null)
				params.put("company",runinvoice.getCompanyLoc().getName());
			
	        if(runinvoice.getTerminal()!=null)
	        	params.put("terminal",runinvoice.getTerminal().getName());
	        
			datas = genericDAO.findByCriteria(HourlyPayrollInvoiceDetails.class, params,"terminal asc,driver",false);
		
			// Multiple same batch payroll - 10th July 2020
			Date minInvoiceCreatedDate = determineMinInvoiceCreatedDate(runinvoice);
			for(HourlyPayrollInvoiceDetails hpdObj:datas){
				// Multiple same batch payroll - 10th July 2020
				if (hpdObj.getCreatedAt().after(runinvoice.getCreatedAt())) {
					continue;
				}
				// Multiple same batch payroll - 10th July 2020
				if (minInvoiceCreatedDate != null) {
					int compare = hpdObj.getCreatedAt().compareTo(minInvoiceCreatedDate); 
					if (compare == 0
							|| compare == -1) {
						continue;
					}
				}
				
				if(!StringUtils.isEmpty(hpdObj.getBatchdate())){
					String[] splitedDate = hpdObj.getBatchdate().split("-");					
					String formattedDate = splitedDate[1]+"-"+splitedDate[2]+"-"+splitedDate[0];
					hpdObj.setBatchdate(formattedDate);
					datas1.add(hpdObj);					
				}				
			}
			
			
			if (datas1!=null && datas1.size()>0) {
		     try{
				params.clear();
				//params.put("date", runinvoice.getPayrollinvoicedate());
				params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(runinvoice.getPayrollinvoicedate()));
				params.put("sumtotalAmount", runinvoice.getSumtotalamount());
				params.put("sumdtAmount", runinvoice.getSumdtamount());
				params.put("sumotAmount", runinvoice.getSumotamount());
				params.put("sumregularAmount", runinvoice.getSumregularamount());
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				out = dynamicReportService.generateStaticReport("hourlyPayrollinvoice",datas1, params, type, request);
				response.setHeader("Content-Disposition",
						"attachment;filename=hourly_payroll."+type);
					out.writeTo(response.getOutputStream());
					out.close();
					return null;
					
		        }catch (Exception e) 
			    {
					e.printStackTrace();
					request.getSession().setAttribute("errors", e.getMessage());
					return "error";
				}
			}
		     request.getSession().setAttribute("errors", "Unable to download payroll run. Plesae try again later");
				return "error";
		
		}catch(Exception ex){
			ex.printStackTrace();
			request.getSession().setAttribute("errors", "Unable to download payroll run. Plesae try again later");
			return "error";
		}
	}
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") HourlyPayrollInvoice entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			HourlyPayrollInvoice invoice=genericDAO.getById(HourlyPayrollInvoice.class, Long.valueOf(entity.getId()));
			HrreportService.deleteEmployeePayrollData(invoice);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"error",
					"This payroll data can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
	
	
	public ByteArrayOutputStream downloadattachment(String path,
			HttpServletRequest request) {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			FileInputStream fis=new FileInputStream(new File(path));
			BufferedInputStream bis=new BufferedInputStream(fis);
			int i;
			while((i=bis.read())!=-1){
				baos.write(i);
			}
			fis.close();
			bis.close();;
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return baos;
	}
	
}
