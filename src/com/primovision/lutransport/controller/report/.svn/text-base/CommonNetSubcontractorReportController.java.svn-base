package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.report.SubcontractorBillingWrapper;
import com.primovision.lutransport.model.report.SubcontractorReportInput;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;


/*@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/drillsubcontractorreport")*/
public class CommonNetSubcontractorReportController extends BaseController {
	
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/drillsubcontractorreportexport.do")
	public String downloadSubcontractorReportsForCompmany(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
			value = "type") String type,@RequestParam(required = false, value = "company") String company,@RequestParam(required = false, value = "billBatchDate") String batchDate) throws IOException {
		
		
		String batchDateFrom="";
		String batchDateTo="";
		String[] billbatchDate=batchDate.split("to");
		if(billbatchDate.length>1){
			batchDateFrom=billbatchDate[0].trim();
			batchDateTo=billbatchDate[1].trim();
		}
		else{
			batchDateFrom=billbatchDate[0];
		}
		
		
		Map criterias=new HashMap();
		criterias.clear();
		criterias.put("name",company);
		Location locObj=genericDAO.getByCriteria(Location.class,criterias);		
		if(locObj!=null)
			company=locObj.getId().toString();
		
		//displayDrillReport(model, request, response,type,fromDate,toDate,company);
		exportDrillSunbocntractorReport(model, request, response,type,batchDateFrom,batchDateTo,company);
		
		
		return null;	
		
	}
	
	
	public String exportDrillSunbocntractorReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,String type,String batchdatefrom,String batchdateto,String company) throws IOException {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = new SearchCriteria();	
		criteria.setPageSize(500000);
		criteria.setPage(0);
		SubcontractorReportInput input = new SubcontractorReportInput();
		input.setBatchDateFrom(batchdatefrom);
		input.setBatchDateTo(batchdateto);
		input.setCompany(company);
		try {
			Map<String,Object> datas = generateData(criteria, request, input);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= subcontractorreport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("subcontractorreport",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("subcontractorreportpdf",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("subcontractorreport"+"print",
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			criteria.setPageSize(25);
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	
	
	
	protected  Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request, SubcontractorReportInput input) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		SubcontractorBillingWrapper wrapper=generateSubcontractorReport(criteria, input);
		/*double mesceCharge=0.00;
		double mCharge=0.00;
		DecimalFormat df2 = new DecimalFormat("###.##");
		if(!StringUtils.isEmpty(input.getMiscelleneousCharges())){
			
			String query="select obj from SubcontractorInvoice obj where obj.miscelleneousCharges='"+input.getMiscelleneousCharges()+"'";
			List<SubcontractorInvoice> invoices=genericDAO.executeSimpleQuery(query);
			for(SubcontractorInvoice invoice:invoices){
			mCharge	=Double.parseDouble(invoice.getMiscelleneousCharges());
			System.out.println("\n d value-->"+Double.valueOf(invoice.getMiscelleneousCharges()));
			
			mesceCharge+=Double.valueOf(df2.format(mCharge));
			}
			params.put("misceCharges","$"+Double.toString(mesceCharge));
			
		}*/
		/*params.put("grandTotal", wrapper.getSumTotal()+mesceCharge);*/
		params.put("misceCharges",wrapper.getMiscelleneousCharges());
		params.put("grandTotal",wrapper.getGrandTotal());
		params.put("sumOtherCharges", wrapper.getSumOtherCharges());
		params.put("sumBillableTon", wrapper.getSumBillableTon());
		params.put("sumOriginTon", wrapper.getSumOriginTon());
		params.put("sumDestinationTon", wrapper.getSumDestinationTon());
		
		params.put("sumTotal", wrapper.getSumTotal());
		//params.put("sumNet", wrapper.getSumNet());
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
		data.put("data", wrapper.getSubcontractorBillings());
		data.put("params",params);
		return data;
		//return null;
	}
	public SubcontractorBillingWrapper generateSubcontractorReport(SearchCriteria searchCriteria, SubcontractorReportInput input) {
		    return reportService.generateSubcontractorReportData(searchCriteria, input);
	}
	
}
