package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.report.EztollReportWrapper;
import com.primovision.lutransport.model.report.NetReportMain;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;



@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/tolldrilldownreport")
public class TollChargesDrillDownReport extends BaseController {

public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd ");
public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");	
public static SimpleDateFormat tdff = new SimpleDateFormat("yyyy-MM-dd");	
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
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/tolldrilldownexport.do")
	public String downloadTollDrillDown(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
			value = "type") String typeKey,@RequestParam(required = false, value = "typeValue") String typeValue,@RequestParam(required = false, value = "unloadDateFromValue") String unloadDateFrom,@RequestParam(required = false, value = "unloadDateToValue") String unloadDateTo
			,@RequestParam(required = false, value = "companyValue") String companyValue) throws IOException {
		
		Map criti=new HashMap(); 
		Location locObj=null;
		Location terminalObj=null;
		Vehicle truckObj=null;
		Vehicle trailerObj=null;
		Driver driverObj=null;
		
		
		
		
		String tollTagQuery="select obj from EzToll obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";
		
	    
		
	    
	    if(!StringUtils.isEmpty(companyValue)){
	    	criti.clear();
			criti.put("name",companyValue);
			locObj=genericDAO.getByCriteria(Location.class, criti);
			if(locObj!=null)
				tollTagQuery = tollTagQuery+" and obj.company="+locObj.getId()+"";
	    }
		
		
	    if(typeKey.equalsIgnoreCase("company"))
		{				
			criti.clear();
			criti.put("name",typeValue);
			locObj=genericDAO.getByCriteria(Location.class, criti);
			if(locObj!=null)
				tollTagQuery = tollTagQuery+" and obj.company="+locObj.getId()+"";
			
		}
	    
	    if(typeKey.equalsIgnoreCase("terminal"))
		{
	    	criti.clear();
			criti.put("name",typeValue);
			terminalObj=genericDAO.getByCriteria(Location.class, criti);
			if(terminalObj!=null)
				tollTagQuery = tollTagQuery+" and obj.terminal="+terminalObj.getId()+"";
	    	
		}
	    
	    if(typeKey.equalsIgnoreCase("truck"))
		{	    	
	    	truckObj=genericDAO.getById(Vehicle.class,Long.parseLong(typeValue));
			if(truckObj!=null)			
				tollTagQuery = tollTagQuery+" and obj.plateNumber="+truckObj.getId()+"";
		}
	    
	   
	    
	    if(typeKey.equalsIgnoreCase("driver"))
		{	    	
	    	criti.clear();
			criti.put("fullName",typeValue);				
			driverObj=genericDAO.getByCriteria(Driver.class, criti);
			if(driverObj!=null)			
				tollTagQuery = tollTagQuery+" and obj.driver="+driverObj.getId()+"";
		}    
	    
	    
	    
		
		List<EzToll> tollTagObjList = genericDAO.executeSimpleQuery(tollTagQuery);
		
		
		

			
			List<EzToll> summarys = new ArrayList<EzToll>();
			EztollReportWrapper wrapper = new EztollReportWrapper();
			wrapper.setEztolls(summarys);

			long totalColumn = 0;
			double totalAmounts = 0.0;

			// Map<String, List<FuelLog>> fuellogMap = new HashMap<String,
			// List<FuelLog>>();

			for (EzToll eztol : tollTagObjList) {
				totalColumn = totalColumn + 1;
				if (eztol != null) {
					EzToll output = new EzToll();

					output.setTollcompanies(eztol.getToolcompany().getName());
					output.setCompanies(eztol.getCompany().getName());
					output.setTerminals(eztol.getTerminal().getName());
					if (eztol.getPlateNumber() != null) {
						output.setPlates((eztol.getPlateNumber().getPlate())
								.toString());
						output.setUnits((eztol.getPlateNumber().getUnit())
								.toString());
					} else
						output.setPlates("");

					if (eztol.getTollTagNumber() != null) {
						output.setTollTagNumbers((eztol.getTollTagNumber()
								.getTollTagNumber()));
						if (output.getUnits() == null)
							output.setUnits((eztol.getTollTagNumber().getVehicle()
									.getUnit()).toString());
					} else
						output.setTollTagNumbers("");
					// output.setUnits((eztol.getUnit().getUnit()).toString());
					// output.setTransfersDate(sdf.format(eztol.getTransactiondate()));
					output.setTransfersDate((eztol.getTransactiondate() != null) ? sdf
							.format(eztol.getTransactiondate()) : "");
					output.setTransfersDate(sdf.format(eztol.getTransactiondate()));
					output.setTransactiontime((eztol.getTransactiontime() != null) ? eztol
							.getTransactiontime() : "");
					output.setAgency(eztol.getAgency());
					output.setAmount(eztol.getAmount());
					output.setDrivername(eztol.getDriver()!=null?eztol.getDriver().getFullName():"");
					

					if (eztol.getAmount() != null)
						totalAmounts += eztol.getAmount();

					summarys.add(output);

				}

			}
			totalAmounts = MathUtil.roundUp(totalAmounts, 2);
			wrapper.setTotalAmounts(totalAmounts);
			wrapper.setTotalColumn(totalColumn);
			
			//Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> params = new HashMap<String,Object>();			 
			params.put("totalAmounts",wrapper.getTotalAmounts());
			params.put("totalColumn",wrapper.getTotalColumn());
			
			try{			
			String	type = "csv";
			response.setHeader("Content-Disposition",
						"attachment;filename= eztollReport." + type);			
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out = dynamicReportService.generateStaticReport("eztollReport",
						(List)wrapper.getEztolls(), params, type, request);			
		
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}		
	}
	
	
}
