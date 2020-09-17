package com.primovision.lutransport.controller.hr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.MiscellaneousAmount;
import com.primovision.lutransport.model.hr.MiscellaneousPayReportInput;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@RequestMapping("/hr/miscellaneousamount/report/miscellaneouspayreport")
public class MiscellaneousPayReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public MiscellaneousPayReportController() {
		setUrlContext("hr/miscellaneousamount/report/miscellaneouspayreport");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null && criteria.getSearchMap() != null) {
				criteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		return "hr/miscellaneousamount/report/" + "miscellaneousPayReport";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") MiscellaneousPayReportInput input) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request, input);
		List<MiscellaneousAmount> MiscellaneousAmountList = (List<MiscellaneousAmount>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("miscellaneousPayReport",
					MiscellaneousAmountList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			return getUrlContext() + "/"+type;
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
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		MiscellaneousPayReportInput input = (MiscellaneousPayReportInput) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateData(criteria, request, input);
		List<MiscellaneousAmount> miscellaneousAmountList = (List<MiscellaneousAmount>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=miscellaneousPayReport." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "miscellaneousPayReport";
		if (StringUtils.equals("pdf", type)) {
			reportName += "pdf";
		}
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
					miscellaneousAmountList, params, type, request);
			
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
	
	private Map<String,Object> generateData(SearchCriteria searchCriteria, 
			HttpServletRequest request, MiscellaneousPayReportInput input) {
		List<MiscellaneousAmount> miscellaneousAmountList = performSearch(searchCriteria, input);  
	
		Map<String, Object> params = new HashMap<String, Object>();
		
		Location company = retrieveLocation(input.getCompany());
		String companyName = company == null ? StringUtils.EMPTY : company.getName();
		params.put("company", companyName);
		
		Location terminal = retrieveLocation(input.getTerminal());
		String terminalName = terminal == null ? StringUtils.EMPTY : terminal.getName();
		params.put("terminal", terminalName);
		
		String batchDateRange = StringUtils.isEmpty(input.getBatchDateFrom()) ? StringUtils.EMPTY : input.getBatchDateFrom();
		batchDateRange += " - ";
		batchDateRange += StringUtils.isEmpty(input.getBatchDateTo()) ? StringUtils.EMPTY : input.getBatchDateTo();
		params.put("batchDateRange", batchDateRange);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", miscellaneousAmountList);
		data.put("params", params);
		     
		return data;
	}
	
	private Location retrieveLocation(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(Location.class, Long.valueOf(id));
	}
	
	private List<MiscellaneousAmount> performSearch(SearchCriteria criteria, MiscellaneousPayReportInput input) {
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String employee = input.getEmployee();
		String miscellaneousDesc = input.getMiscellaneousDesc();
		
		String batchDateFrom = input.getBatchDateFrom();
		String batchDateTo = input.getBatchDateTo();
		
		String ssn = input.getSsn();
		
		StringBuffer query = new StringBuffer("select obj from MiscellaneousAmount obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from MiscellaneousAmount obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.company=" + company);
		}
		if (StringUtils.isNotEmpty(terminal)) {
			whereClause.append(" and obj.terminal=" + terminal);
		}
		if (StringUtils.isNotEmpty(employee)) {
			whereClause.append(" and obj.driver.fullName='" + employee + "'");
		}
		if (StringUtils.isNotEmpty(ssn)) {
			whereClause.append(" and obj.driver.ssn='" + ssn + "'");
		}
		if (StringUtils.isNotEmpty(miscellaneousDesc)) {
			String[] miscellaneousDescTokens = miscellaneousDesc.split(",");
			String miscDescIn = StringUtils.EMPTY;
			for (int i = 0; i < miscellaneousDescTokens.length; i++) {
				miscDescIn += "'"+miscellaneousDescTokens[i]+"',";
			}
			miscDescIn = miscDescIn.substring(0, miscDescIn.length()-1);
			whereClause.append(" and obj.miscNotes in (" + miscDescIn + ")");
		}
		
		StringBuffer batchDateClause = new StringBuffer();
		
	   if (StringUtils.isNotEmpty(batchDateFrom)) {
        	try {
        		batchDateClause.append(" (obj.batchFrom>='"+sdf.format(dateFormat.parse(batchDateFrom))+"'");
        		batchDateClause.append(" and obj.batchFrom<='"+sdf.format(dateFormat.parse(batchDateTo))+"')");
        		
        		batchDateClause.append(" OR ");
        		
        		batchDateClause.append(" (obj.batchTo>='"+sdf.format(dateFormat.parse(batchDateFrom))+"'");
        		batchDateClause.append(" and obj.batchTo<='"+sdf.format(dateFormat.parse(batchDateTo))+"')");
   	   } catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      
      if (StringUtils.isNotEmpty(batchDateClause.toString())) {
      	whereClause.append(" and (")
      				  .append(batchDateClause)
      				  .append(" )");
      }
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.driver.fullName asc, obj.miscNotes asc, obj.batchFrom asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<MiscellaneousAmount> miscellaneousAmountList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return miscellaneousAmountList;
	}

	public void setupList(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias, "fullName",false));
		
		criterias.clear();	
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name",false));
		
		criterias.clear();		
		model.addAttribute("miscellaneousDesc", genericDAO.executeSimpleQuery("select obj from SetupData obj where dataType='Miscellaneous_Desc' order by obj.dataLabel asc"));
		criterias.clear();		
	}
	
	@ModelAttribute("modelObject")
	public MiscellaneousPayReportInput setupModel(HttpServletRequest request) {
		return new MiscellaneousPayReportInput();
	}
}
