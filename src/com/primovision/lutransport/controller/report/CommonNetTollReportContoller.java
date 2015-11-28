package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
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
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.report.EztollReportInput;
import com.primovision.lutransport.model.report.EztollReportWrapper;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.model.report.NetReportInput;
import com.primovision.lutransport.model.report.NetReportWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;


@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/drilltollreport")
public class CommonNetTollReportContoller extends BaseController {

	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	
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
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/drilltollreportexport.do")
	public String downloadTollReportsForCompmany(ModelMap model, HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false, 
			value = "type") String type,@RequestParam(required = false,
			value = "fromDate") String fromDate,@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "company") String company,@RequestParam(required = false, value = "batchdate") String batchDate ) {
		
		
		Map criterias=new HashMap();
		criterias.clear();
		criterias.put("name",company);
		Location locObj=genericDAO.getByCriteria(Location.class,criterias);		
		if(locObj!=null)
			company=locObj.getId().toString();
		
		//displayDrillReport(model, request, response,type,fromDate,toDate,company);
		exportDrillTollReport(model, request, response,type,fromDate,toDate,company);
		
		
		return null;	
		
	}
	
	
	
	
	
	
	public String exportDrillTollReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,String type,String fromdate,String todate,String repcompany){
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		NetReportInput input = (NetReportInput)request.getSession().getAttribute("input");
		
		
		SimpleDateFormat oracleFormatter = new SimpleDateFormat(
		"yyyy-MM-dd");
		
		boolean invoi=true;
		boolean execstr=false;
		String DriverReport = (String) request.getSession().getAttribute(
				"DriverReport");
		String TruckReport = (String) request.getSession().getAttribute(
				"TruckReport");
		String TerminalReport = (String) request.getSession().getAttribute(
				"TerminalReport");
		String CompanyReport = (String) request.getSession().getAttribute(
				"CompanyReport");
		String TrailerReport=(String)request.getSession().getAttribute("TrailerReport");

		System.out.println("\ngenerateNetReportData==Driver===>" + DriverReport
				+ "\n");
		System.out.println("\ngenerateNetReportData==Truck===>" + TruckReport
				+ "\n");
		System.out.println("\ngenerateNetReportData==terminal===>"
				+ TerminalReport + "\n");
		System.out.println("\ngenerateNetReportData==Company===>"
				+ CompanyReport + "\n");

		String batchDateFrom = ReportDateUtil.getFromDate(input
				.getBatchDateFrom());
		String batchDateTo = ReportDateUtil.getToDate(input.getBatchDateTo());
		String loadDateFrom = ReportDateUtil.getFromDate(input.getLoadedFrom());
		String loadDateTo = ReportDateUtil.getToDate(input.getLoadedTo());
		String unloadDateFrom = ReportDateUtil.getFromDate(input
				.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());
		String dateRangeFrom = null;
		String dateRangeTo = null;
		System.out.println("\nunloadDateFrom===>" + unloadDateFrom + "\n");
		System.out.println("\nunloadDateTo===>" + unloadDateTo + "\n");

		String BatchDateStart = input.getBatchDateFrom();
		String BatchDateEnd = input.getBatchDateTo();
		String loadDateStart = input.getLoadedFrom();
		String loadDateEnd = input.getLoadedTo();
		String UnloadDateStart = input.getUnloadedFrom();
		String UnloadDateEnd = input.getUnloadedTo();
		String invoiceDatefrom=input.getInvoiceDateFrom();
		String invoiceDateTo=input.getInvoiceDateTo();
		invoiceDatefrom=ReportDateUtil.getFromDate(invoiceDatefrom);
		invoiceDateTo=ReportDateUtil.getToDate(invoiceDateTo);
		System.out.println("\nUnloadDateStart===>" + UnloadDateStart + "\n");
		System.out.println("\nUnloadDateEnd===>" + UnloadDateEnd + "\n");

		if (!StringUtils.isEmpty(batchDateFrom)
				&& !StringUtils.isEmpty(batchDateTo)) {
			dateRangeFrom = batchDateFrom;
			dateRangeTo = batchDateTo;
		}
		if (!StringUtils.isEmpty(loadDateFrom)
				&& !StringUtils.isEmpty(loadDateTo)) {
			dateRangeFrom = loadDateFrom;
			dateRangeTo = loadDateTo;
		}
		if (!StringUtils.isEmpty(unloadDateFrom)
				&& !StringUtils.isEmpty(unloadDateTo)) {
			dateRangeFrom = unloadDateFrom;
			dateRangeTo = unloadDateTo;
		}
        String trailer = input.getTrailer();
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String driver = input.getDriver();
		String truck = input.getUnit();
		String  ticketstatus=input.getTicketStatus();
		StringBuffer ticketquery = new StringBuffer("");
		StringBuffer subcontractorquery = new StringBuffer("");
		StringBuffer terminalids=new StringBuffer("");
		if(TerminalReport!=null&&!StringUtils.isEmpty(company)&&!StringUtils.contains(TerminalReport, "Terminalac")){
		String terquery="select obj.terminal from Terminal obj where obj.company in ("+company+")";
		List<Location> locations=genericDAO.executeSimpleQuery(terquery);
		for(Location lolterminal:locations){
			
			terminalids.append(lolterminal.getId());
			terminalids.append(",");
			}
		int t=terminalids.lastIndexOf(",");
		if(t>0)
		terminalids.deleteCharAt(t);
		}
		System.out.println("\nticketstatus==>"+ticketstatus+"\n");
		/*System.out.println("\nticketstatus.length()==>"+ticketstatus.length()+"\n");
		System.out.println("\nticketstatus.length()==1>"+ticketstatus.length()==1+"\n");
		System.out.println("\nticketstatus==1>"+ticketstatus==1+"\n");*/
		
		
		/*
		 * ticketquery.append(
		 * "select obj from Ticket obj where obj.status=1 and ticketStatus=2");
		 */
		List<Vehicle> vehicles=null;
		StringBuffer vehicIds = new StringBuffer("");
		if(TruckReport!=null&&(StringUtils.contains(TruckReport, "Truckcc")||StringUtils.contains(TruckReport, "Truckca"))){
			Map criterias=new HashMap();
			criterias.put("owner.id", Long.parseLong(input.getCompany()));
			criterias.put("type", 1);
			vehicles=genericDAO.findByCriteria(Vehicle.class, criterias);
			for(Vehicle vehicle:vehicles){
				vehicIds.append(vehicle.getId());
				vehicIds.append(",");
				
			}
			int v = vehicIds.lastIndexOf(",");
			if (v > 0)
				vehicIds.deleteCharAt(v);
		}
		List<Vehicle> trailvehicle=null;
		StringBuffer vehicleidstrai=new StringBuffer("");
		if(TrailerReport!=null&&(StringUtils.contains(TrailerReport,"Trailercc")||StringUtils.contains(TrailerReport,"Trailerca"))){
			Map criterias=new HashMap();
			criterias.put("owner.id", Long.parseLong(input.getCompany()));
			criterias.put("type", 2);
			trailvehicle=genericDAO.findByCriteria(Vehicle.class, criterias);
			for(Vehicle vehicle:trailvehicle){
				vehicleidstrai.append(vehicle.getId());
				vehicleidstrai.append(",");
				
			}
			int v = vehicleidstrai.lastIndexOf(",");
			if (v > 0)
				vehicleidstrai.deleteCharAt(v);
		}
		List<Driver> drivers=null;
		StringBuffer driveIds = new StringBuffer("");
		if(DriverReport!=null&&(StringUtils.contains(DriverReport, "Drivercc")||StringUtils.contains(DriverReport, "Driverca"))){
			Map criterias=new HashMap();
			criterias.put("company.id", Long.parseLong(input.getCompany()));
			drivers=genericDAO.findByCriteria(Driver.class, criterias);
			
			for(Driver driver2:drivers){
				driveIds.append(driver2.getId());
				driveIds.append(",");
			}
			int d = driveIds.lastIndexOf(",");
			if (d > 0)
				driveIds.deleteCharAt(d);
		}
		ticketquery.append("select obj from Ticket obj where obj.status=1");
		//subcontractorquery.append("select obj from Ticket obj where obj.status=1 and voucherStatus=2");
		subcontractorquery.append("select obj from Ticket obj where obj.status=1 and obj.subcontractor is not null");
		
		if(TerminalReport!=null&&!StringUtils.isEmpty(company)&&terminalids.length()>0){
			ticketquery.append(" and  obj.terminal in (").append(terminalids)
			.append(")");
	subcontractorquery.append(" and  obj.terminal in (")
			.append(terminalids).append(")");
			
		}
		if (!StringUtils.isEmpty(ticketstatus)) {
			ticketquery.append(" and  obj.ticketStatus in (").append(ticketstatus).append(")");
			subcontractorquery.append(" and  obj.voucherStatus in (").append(ticketstatus).append(")");
		}
		if(drivers!=null){
			ticketquery.append(" and  obj.driver.id in (").append(driveIds)
			.append(")");
	subcontractorquery.append(" and  obj.driver.id in (")
			.append(driveIds).append(")");
		}
		
		if (!StringUtils.isEmpty(batchDateFrom)) {
			ticketquery.append(" and  obj.billBatch>='").append(
					batchDateFrom + "'");
			subcontractorquery.append(" and  obj.billBatch>='").append(
					batchDateFrom + "'");
			invoi=false;

		}
		if (!StringUtils.isEmpty(batchDateTo)) {
			ticketquery.append(" and  obj.billBatch<='").append(
					batchDateTo + "'");
			subcontractorquery.append(" and  obj.billBatch<='").append(
					batchDateTo + "'");
			invoi=false;

		}
		if(!StringUtils.isEmpty(trailer)){
			
			
			String vehiclequery="select obj from Vehicle obj where obj.type=2 and obj.unit in ("
				+trailer
				+")";
			
			System.out.println("******** the trailer query is "+vehiclequery);
			
			List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
			String trailerIds="";
			if(vehicleLists!=null && vehicleLists.size()>0){				
				int count=0;
			for(Vehicle vehicleObj : vehicleLists) {
				if(count==0){
					count++;
					trailerIds=String.valueOf(vehicleObj.getId());
				}
				else{
					trailerIds=trailerIds+","+String.valueOf(vehicleObj.getId());
				}
			}
			}			
			
			System.out.println("********* trailer id is "+trailerIds);			
			ticketquery.append(" and  obj.trailer.id in (").append(trailerIds)
			.append(")");
			subcontractorquery.append(" and  obj.trailer.id in (")
			.append(trailerIds).append(")");
		}
		if(trailvehicle!=null){
			ticketquery.append(" and  obj.trailer.id in (").append(vehicleidstrai)
			.append(")");
			subcontractorquery.append(" and  obj.trailer.id in (")
			.append(vehicleidstrai).append(")");
		}
		if(vehicles!=null){
			ticketquery.append(" and  obj.vehicle.id in (").append(vehicIds)
			.append(")");
			subcontractorquery.append(" and  obj.vehicle.id in (")
			.append(vehicIds).append(")");
		}
		if (!StringUtils.isEmpty(loadDateFrom)) {
			ticketquery.append(" and  obj.loadDate>='").append(
					loadDateFrom + "'");
			subcontractorquery.append(" and  obj.loadDate>='").append(
					loadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(loadDateTo)) {
			ticketquery.append(" and  obj.loadDate<='")
					.append(loadDateTo + "'");
			subcontractorquery.append(" and  obj.loadDate<='").append(
					loadDateTo + "'");
		}
		if (!StringUtils.isEmpty(unloadDateFrom)) {
			ticketquery.append(" and  obj.unloadDate>='").append(
					unloadDateFrom + "'");
			subcontractorquery.append(" and  obj.unloadDate>='").append(
					unloadDateFrom + "'");
			execstr=true;
		}
		if (!StringUtils.isEmpty(unloadDateTo)) {
			ticketquery.append(" and  obj.unloadDate<='").append(
					unloadDateTo + "'");
			subcontractorquery.append(" and  obj.unloadDate<='").append(
					unloadDateTo + "'");
			execstr=true;
		}
		if(!StringUtils.isEmpty(invoiceDatefrom)){
			ticketquery.append(" and obj.invoiceDate>='").append(invoiceDatefrom+"'");
			subcontractorquery.append(" and obj.invoiceDate>='").append(invoiceDatefrom+"'");
			invoi=false;
		}
		if(!StringUtils.isEmpty(invoiceDateTo)){
			ticketquery.append(" and obj.invoiceDate<='").append(invoiceDateTo+"'");
			subcontractorquery.append(" and obj.invoiceDate<='").append(invoiceDateTo+"'");
			invoi=false;
		}
		StringBuffer drivernames = new StringBuffer("");
		if (!StringUtils.isEmpty(driver)) {
			ticketquery.append(" and  obj.driver.id in (").append(driver)
					.append(")");
			subcontractorquery.append(" and  obj.driver.id in (")
					.append(driver).append(")");

			String driverIds = "select obj from Driver obj where obj.id in ("
					+ driver + ")";
			List<Driver> driverlist = genericDAO.executeSimpleQuery(driverIds);
			for (Driver driverOb : driverlist) {
				drivernames.append("'" + driverOb.getFullName() + "',");

			}
			int i = drivernames.lastIndexOf(",");
			if(i>0)
			drivernames.deleteCharAt(i);

		}
		StringBuffer units = new StringBuffer("");
		if (!StringUtils.isEmpty(truck)) {			
			
			String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
					+truck
					+")";
				
				System.out.println("******** the truck query is "+vehiclequery);
				
				List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
				String truckIds="";
				if(vehicleLists!=null && vehicleLists.size()>0){				
					int count=0;
				for(Vehicle vehicleObj : vehicleLists) {
					if(count==0){
						count++;
						truckIds=String.valueOf(vehicleObj.getId());
					}
					else{
					truckIds=truckIds+","+String.valueOf(vehicleObj.getId());
					}
				}
				}
				System.out.println("********* truck id is "+truckIds);			
			
			ticketquery.append(" and  obj.vehicle.id in (").append(truckIds)
					.append(")");
			subcontractorquery.append(" and  obj.vehicle.id in (")
					.append(truckIds).append(")");

			String VehicleIds = "select obj from Vehicle obj where obj.id in ("
					+ truckIds + ")";
			List<Vehicle> vehiclelist = genericDAO
					.executeSimpleQuery(VehicleIds);
			for (Vehicle vehiclOb : vehiclelist) {
				units.append("'" + vehiclOb.getUnit() + "',");

			}
			int i = units.lastIndexOf(",");
			units.deleteCharAt(i);
		}
		StringBuffer locationnames = new StringBuffer("");
		if (!StringUtils.isEmpty(terminal)) {
			// System.out.println("\nTerminal===>"+terminal+"\n");
			ticketquery.append(" and  obj.terminal in (").append(terminal)
					.append(")");
			subcontractorquery.append(" and  obj.terminal in (")
					.append(terminal).append(")");

			String locationIds = "select obj from Location obj where obj.id in ("
					+ terminal + ")";
			List<Location> locationlist = genericDAO
					.executeSimpleQuery(locationIds);
			for (Location LocationOb : locationlist) {
				locationnames.append("'" + LocationOb.getName() + "',");

			}
			int i = locationnames.lastIndexOf(",");
			locationnames.deleteCharAt(i);

		}

		StringBuffer companynames = new StringBuffer("");
		if (!StringUtils.isEmpty(input.getCompany())&&!StringUtils.contains(DriverReport, "Driverca")&&!StringUtils.contains(TruckReport, "Truckca")&&!StringUtils.contains(TerminalReport, "Terminalca")&&!StringUtils.contains(TrailerReport, "Trailerca")) {
			System.out.println("\n inside company");
			ticketquery.append(" and obj.companyLocation.id in (")
					.append(company).append(")");
			subcontractorquery.append(" and obj.companyLocation.id in (")
					.append(company).append(")");

			String locationIds = "select obj from Location obj where obj.id in ("
					+ company + ")";
			List<Location> locationIdlist = genericDAO
					.executeSimpleQuery(locationIds);
			for (Location LocationOb : locationIdlist) {
				companynames.append("'" + LocationOb.getName() + "',");

			}
			int i = companynames.lastIndexOf(",");
			companynames.deleteCharAt(i);

		}		
		System.out.println("******* the query for ticket is "+ticketquery
				.toString());
		
		List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketquery
				.toString());

		StringBuffer ticketIds = new StringBuffer("");
		//List<Ticket> subtickets = genericDAO.executeSimpleQuery(subcontractorquery.toString());
		StringBuffer subticketIds = new StringBuffer("");
		int NoOfTickets = tickets.size();
		List<NetReportWrapper> wrapperlist = new ArrayList<NetReportWrapper>();
		int less1 = 0;
		if (NoOfTickets > 0) {
			// StringBuffer driverID = new StringBuffer("");
			for (Ticket ticket : tickets) {
				if (less1 != NoOfTickets - 1) {
					ticketIds.append("'" + ticket.getId() + "',");
					less1++;
				} else {
					ticketIds.append("'" + ticket.getId() + "'");
				}
			}
			System.out.println("\nTickets ID`s===>" + ticketIds + "\n");		
		}				
		
			List<EzToll> summarys = new ArrayList<EzToll>();
			EztollReportWrapper wrapper = new EztollReportWrapper();
			wrapper.setEztolls(summarys);
			long totalColumn = 0;
			double totalAmounts = 0.0;				
				
			System.out.println("********* the compnay value is "+repcompany);				
			Location companyName = genericDAO.getById(Location.class,Long.parseLong(repcompany));
			String ticketsForAlocation = "select obj from Ticket obj where obj.companyLocation="
							+ companyName.getId()
							+ " and obj.id in ("
							+ ticketIds
							+ ") order by loadDate asc";
			List<Ticket> ticketsForAcampany = genericDAO.executeSimpleQuery(ticketsForAlocation);		
			
			System.out.println("Enetered here in new logic");			
			
			List tollIds=new ArrayList();
			tollIds.add(0);
			for (Ticket ticket : ticketsForAcampany) {
				
				StringBuffer TolltagCharge = new StringBuffer("");
				String strloaddate=sdf.format(ticket.getLoadDate());
				String strunloaddate=sdf.format(ticket.getUnloadDate());
				TolltagCharge
				.append("select obj from EzToll obj where obj.company='"
						+ companyName.getId()
						+ "'and obj.transactiondate>='"
						+ strloaddate
						+ "' and obj.transactiondate<='"
						+ strunloaddate 
						+ "' and obj.plateNumber="
						+ticket.getVehicle().getId());

		if (!StringUtils.isEmpty(terminal)) {
			TolltagCharge.append("and  obj.terminal in ("
					+ terminal + ")");
		}
		/*if (!StringUtils.isEmpty(truck)) {
			TolltagCharge.append("and  obj.plateNumber in ("
					+ truck + ")");
		}*/
		
		System.out.println("******* the toll query is "+TolltagCharge.toString());
		
			
		List<EzToll> TollListforCompany = genericDAO.executeSimpleQuery(TolltagCharge.toString());
		
        if (TollListforCompany != null	&& TollListforCompany.size() > 0) {			
			System.out.println("inside fuel if ok ok"+TollListforCompany.size());				
			for (EzToll eztol : TollListforCompany){
				if(!tollIds.contains(eztol.getId())){
					System.out.println("***** toll not Contains ********"+eztol.getId());
					tollIds.add(eztol.getId());
				totalColumn = totalColumn + 1;					
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
		    else{
		    	System.out.println("***** toll Contains ********"+eztol.getId());
			}
		}		
	}				
}	
			totalAmounts = MathUtil.roundUp(totalAmounts, 2);
			wrapper.setTotalAmounts(totalAmounts);
			wrapper.setTotalColumn(totalColumn);
		
			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> params = new HashMap<String,Object>();				 
			params.put("totalAmounts",wrapper.getTotalAmounts());
			params.put("totalColumn",wrapper.getTotalColumn());		  
			data.put("data", wrapper.getEztolls());
			data.put("params",params);			
		SearchCriteria criterias = new SearchCriteria();		
		criterias.setPageSize(15000);
		criterias.setPage(0);	
		criterias.setRecordCount(Integer.parseInt(String.valueOf(totalColumn)));
		try {
			Map<String,Object> datas = data;
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= eztollReport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> paramss = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")) {
				out = dynamicReportService.generateStaticReport("eztollReport",
						(List)datas.get("data"), paramss, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("eztollReport"+"print",
						(List)datas.get("data"), paramss, type, request);
			}
		
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
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,EztollReportInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		EztollReportWrapper wrapper = generateEztollReport(searchCriteria,input);	 
		params.put("totalAmounts",wrapper.getTotalAmounts());
		params.put("totalColumn",wrapper.getTotalColumn());		  
		    data.put("data", wrapper.getEztolls());
			data.put("params",params);
		     
		  return data;
	}
	
	
	public EztollReportWrapper generateEztollReport(SearchCriteria searchCriteria,EztollReportInput input) {
	return reportService.generateEztollData(searchCriteria,input);
	}
	
	
	
}
