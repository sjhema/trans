package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;


/*@Controller*/
/*@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/drillrevenureport")*/
public class CommonNetRevenuReportController extends BaseController {
	
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
		
		@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/drillrevenureportexport.do")
		public String downloadRevenuReportsForCompmany(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
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
			exportDrillRevenuReport(model, request, response,type,batchDateFrom,batchDateTo,company);
			
			
			return null;	
			
		}
		
		
		
		public String exportDrillRevenuReport(ModelMap model, HttpServletRequest request,
				HttpServletResponse response,String type,String batchdatefrom,String batchdateto,String company) throws IOException {
			Map imagesMap = new HashMap();
			String sum=request.getParameter("typ");	
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			SearchCriteria criteria = new SearchCriteria();	
			criteria.setPageSize(500000);
			criteria.setPage(0);
			BillingHistoryInput input = new BillingHistoryInput();	
			input.setBatchDateFrom(batchdatefrom);
			input.setBatchDateTo(batchdateto);
			input.setCompany(company);
			  ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				Map<String,Object> datas = generateData(criteria, request, input);
				//List propertyList = (List<String>) request.getSession()
				//		.getAttribute("propertyList");
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= billinghistory." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));			
				Map<String, Object> params = (Map<String,Object>)datas.get("params");
				if (!type.equals("print")&&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("billinghistory",
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("billinghistorypdf",
							(List)datas.get("data"), params, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport("billinghistory"+"print",
							(List)datas.get("data"), params, type, request);
				}
			
				out.writeTo(response.getOutputStream());
				criteria.setPageSize(500);
				out.flush();
				out.close();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Unable to create file :" + e);
				request.getSession().setAttribute("errors", e.getMessage());
				out.flush();
				out.close();
				return "error";
			}
		}
	
		
		protected  Map<String,Object> generateData(SearchCriteria criteria,
				HttpServletRequest request, BillingHistoryInput input) {
			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> params = new HashMap<String,Object>();
			
			BillingWrapper wrapper = generateBillingHistoryReport(criteria, input);
			if(!StringUtils.isEmpty(input.getTotAmtFrom())&&!StringUtils.isEmpty(input.getTotAmtTo())){
				if(input.getTotAmtFrom().equalsIgnoreCase(input.getTotAmtTo())){
					wrapper.setSumTotal(Double.parseDouble(input.getTotAmtFrom()));
					String query="select obj from Invoice obj where obj.sumTotal="+input.getTotAmtFrom();
					List<Invoice> invoices=genericDAO.executeSimpleQuery(query);
					if(invoices.size()>0){
					wrapper.setSumAmount(invoices.get(0).getSumAmount());
					wrapper.setSumDemmurage(invoices.get(0).getSumDemmurage());
					wrapper.setSumTonnage(invoices.get(0).getSumTonnage());
					wrapper.setSumFuelSurcharge(invoices.get(0).getSumFuelSurcharge());
					}
				}
			}			
			params.put("sumDestinationTon", wrapper.getSumDestinationTon());
			params.put("sumOriginTon", wrapper.getSumOriginTon());
			params.put("sumBillableTon", wrapper.getSumBillableTon());
			params.put("sumTonnage", wrapper.getSumTonnage());
			params.put("sumTotal", wrapper.getSumTotal());
			params.put("sumDemurrage", wrapper.getSumDemmurage());
			params.put("sumNet", wrapper.getSumNet());
			params.put("sumAmount", wrapper.getSumAmount());
			params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
			data.put("data", wrapper.getBillings());			
			data.put("params", params);
			return data;
		}

		public BillingWrapper generateBillingHistoryReport(SearchCriteria searchCriteria, BillingHistoryInput input) {
			return reportService.generateBillingHistoryData(searchCriteria,input);
		}
		
	
}
