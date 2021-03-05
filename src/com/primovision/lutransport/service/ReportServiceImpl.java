package com.primovision.lutransport.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.poi.util.StringUtil;
import org.hibernate.bytecode.buildtime.ExecutionException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.tags.StaticDataUtil;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverInspection;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.FuelSurchargeWeeklyRate;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.LocationDistance;
import com.primovision.lutransport.model.MileageLog;
import com.primovision.lutransport.model.NoGPSVehicle;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.SubcontractorInvoice;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehiclePermit;
import com.primovision.lutransport.model.WMInvoice;
import com.primovision.lutransport.model.WMInvoiceVerification;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.model.report.Billing_New;
import com.primovision.lutransport.model.report.EztollReportInput;
import com.primovision.lutransport.model.report.EztollReportWrapper;
import com.primovision.lutransport.model.report.FuelDistributionReportInput;
import com.primovision.lutransport.model.report.FuelDistributionReportWrapper;
import com.primovision.lutransport.model.report.FuelLogAverageReportWrapper;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.model.report.FuelLogVerificationReportData;
import com.primovision.lutransport.model.report.FuelLogVerificationReportInput;
import com.primovision.lutransport.model.report.FuelLogVerificationReportWrapper;
import com.primovision.lutransport.model.report.FuelOverLimitInput;
import com.primovision.lutransport.model.report.FuelOverLimitReportWrapper;
import com.primovision.lutransport.model.report.FuelViolationInput;
import com.primovision.lutransport.model.report.FuelViolationReportWrapper;
import com.primovision.lutransport.model.report.IFTAReport;
import com.primovision.lutransport.model.report.IFTAReportInput;
import com.primovision.lutransport.model.report.IFTAReportWrapper;
import com.primovision.lutransport.model.report.MileageLogReportInput;
import com.primovision.lutransport.model.report.MileageLogReportWrapper;
import com.primovision.lutransport.model.report.NetReportInput;
import com.primovision.lutransport.model.report.NetReportMain;
import com.primovision.lutransport.model.report.NetReportWrapper;
import com.primovision.lutransport.model.report.SubcontractorBilling;
import com.primovision.lutransport.model.report.SubcontractorBillingNew;
import com.primovision.lutransport.model.report.SubcontractorBillingWrapper;
import com.primovision.lutransport.model.report.SubcontractorReportInput;
import com.primovision.lutransport.model.report.SubcontractorSummary;
import com.primovision.lutransport.model.report.Summary;
import com.primovision.lutransport.model.report.TollDistributionReportInput;
import com.primovision.lutransport.model.report.TollDistributionReportWrapper;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.model.Customer;

@Transactional(readOnly = false)
public class ReportServiceImpl implements ReportService {
	public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	public static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat drvdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");	
	public static SimpleDateFormat tdff = new SimpleDateFormat("yyyy-MM-dd");	
	
	public static SimpleDateFormat mileageSearchDateFormat = new SimpleDateFormat("MMMMM yyyy");
	public static SimpleDateFormat mileageReportDateFormat = new SimpleDateFormat("MM-yyyy"); 

	@Autowired
	private GenericDAO genericDAO;

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Override
	public BillingWrapper generateBillingData(SearchCriteria searchCriteria) {
		
		String fromDateStr = (String) searchCriteria.getSearchMap().get(
				"fromDate");
		String toDateStr = (String) searchCriteria.getSearchMap().get("toDate");
		String fromloadDate = (String) searchCriteria.getSearchMap().get(
				"fromloadDate");
		String toloadDate = (String) searchCriteria.getSearchMap().get(
				"toloadDate");
		String fromunloadDate = (String) searchCriteria.getSearchMap().get(
				"fromunloadDate");
		String tounloadDate = (String) searchCriteria.getSearchMap().get(
				"tounloadDate");
		fromDateStr = ReportDateUtil.getFromDate(fromDateStr);
		toDateStr = ReportDateUtil.getToDate(toDateStr);
		fromloadDate = ReportDateUtil.getFromDate(fromloadDate);
		toloadDate = ReportDateUtil.getToDate(toloadDate);
		fromunloadDate = ReportDateUtil.getFromDate(fromunloadDate);
		tounloadDate = ReportDateUtil.getToDate(tounloadDate);

		String origin = (String) searchCriteria.getSearchMap().get("origin");
		String destination = (String) searchCriteria.getSearchMap().get(
				"destination");
		
		// WM Invoice - 17th Jul 2017
		String ticketIds = (String) searchCriteria.getSearchMap().get("ticketIds");

		StringBuffer query = new StringBuffer("");
		/*
		 * query.append(
		 * "select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 and obj.billBatch between '"
		 * + fromDateStr + "'and '" + toDateStr + "'");
		 */
		query.append("select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 ");

		/*
		 * if (!StringUtils.isEmpty(fromDateStr) &&
		 * !StringUtils.isEmpty(toDateStr)) {
		 * query.append("and  obj.billBatch between '" + fromDateStr + "' and '"
		 * + toDateStr + "'"); }
		 */

		if (!StringUtils.isEmpty(fromDateStr)) {
			query.append(" and  obj.billBatch>='").append(fromDateStr + "'");
		}
		if (!StringUtils.isEmpty(toDateStr)) {
			query.append(" and  obj.billBatch<='").append(toDateStr + "'");
		}

		if (!StringUtils.isEmpty(fromloadDate)
				&& !StringUtils.isEmpty(toloadDate)) {
			query.append("and  obj.loadDate between '" + fromloadDate
					+ "' and '" + toloadDate + "'");
		}
		if (!StringUtils.isEmpty(fromunloadDate)
				&& !StringUtils.isEmpty(tounloadDate)) {
			query.append("and  obj.unloadDate between '" + fromunloadDate
					+ "' and '" + tounloadDate + "'");
		}

		if (!StringUtils.isEmpty(origin)) {
			query.append("and  obj.origin=").append(origin);
		}
		/*
		 * if (!StringUtils.isEmpty(destination)) {
		 * query.append("and  obj.destination=").append(destination); }
		 */

		if (!StringUtils.isEmpty(destination)) {
			if (destination.equalsIgnoreCase("91")) {
				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows = genericDAO.getByCriteria(Location.class,
						criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown = genericDAO.getByCriteria(Location.class,
						criterias);

				query.append("and  obj.destination in(" + grows.getId() + ","
						+ tullyTown.getId() + ")");
			} else {

				query.append("and  obj.destination=").append(destination);
			}
		}
		
		// WM Invoice - 17th Jul 2017
		if (StringUtils.isNotEmpty(ticketIds)) {
			query.append(" and obj.id in (").append(ticketIds).append(")");
		}
		
		if (!StringUtils.isEmpty(origin) && !StringUtils.isEmpty(destination)) {
			String rateQuery = "select obj from BillingRate obj where obj.transferStation="
					+ origin + " and obj.landfill=" + destination
					+ " order by obj.validTo desc";
			List<BillingRate> fs = genericDAO.executeSimpleQuery(rateQuery);
			BillingRate billingRate = null;
			if (fs != null && fs.size() > 0) {
				billingRate = fs.get(0);
			}
			if (billingRate != null) {
				int sortBy = (billingRate.getSortBy() == null) ? 1
						: billingRate.getSortBy();
				if (sortBy == 1) {
					query.append(" order by obj.originTicket");
				} else {
					query.append(" order by obj.destinationTicket");
				}
			}
		} else {
			query.append(" order by obj.billBatch desc");
		}
		
		System.out.println(query);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(query.toString());
		return processTickets(tickets, new HashMap());
	}

	@Override
	public BillingWrapper generateBillingHistoryData(
			SearchCriteria searchCriteria, BillingHistoryInput input) {
		String fromDateInvoiceStr = ReportDateUtil.getFromDate(input
				.getFromInvoiceDate());
		String toDateInvoiceStr = ReportDateUtil.getToDate(input
				.getInvoiceDateTo());
		String invoiceNumberFrom = input.getInvoiceNumberFrom();
		String invoiceNumberTo = input.getInvoiceNumberTo();

		String fuelSurchargeFrom1 = input.getFuelSurchargeFrom();
		String fuelSurchargeTo1 = input.getFuelSurchargeTo();

		String tonnagePremiumFrom = input.getTonnagePremiumFrom();
		String tonnagePremiumTo = input.getTonnagePremiumTo();

		String demurrageChargeFrom = input.getDemurrageChargeFrom();
		String demurrageChargeTo = input.getDemurrageChargeTo();

		String amountFrom = input.getAmountFrom();
		String amountTo = input.getAmountTo();

		String totAmtFrom = input.getTotAmtFrom();
		String totAmtTo = input.getTotAmtTo();

		String totalAmtTo = input.getTotalAmtTo();
		String totalAmtFrom = input.getTotalAmtFrom();

		String rateFrom = input.getRateFrom();
		String rateTo = input.getRateTo();
		
		// Peak rate 2nd Feb 2021
		String isPeakRate = input.getIsPeakRate();
		
		boolean useInvoice = false;
		StringBuffer ticketIds = new StringBuffer("-1,");
		if ( (!StringUtils.isEmpty(invoiceNumberFrom))
				|| (!StringUtils.isEmpty(invoiceNumberTo))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeFrom()))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeTo()))
				|| (!StringUtils.isEmpty(tonnagePremiumFrom))
				|| (!StringUtils.isEmpty(tonnagePremiumTo))
				|| (!StringUtils.isEmpty(demurrageChargeFrom))
				|| (!StringUtils.isEmpty(demurrageChargeTo))
				|| (!StringUtils.isEmpty(amountFrom))
				|| (!StringUtils.isEmpty(amountTo))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(totalAmtTo))
				|| (!StringUtils.isEmpty(totalAmtFrom))//
				|| (!StringUtils.isEmpty(input.getRateFrom()))
				|| (!StringUtils.isEmpty(input.getRateTo()))) {
			StringBuffer query2 = new StringBuffer(
					"select bill.ticket from Invoice inv, Billing bill where 1=1 and bill.invoiceNo = inv.invoiceNumber and bill.origin=inv.transferStation.name and ((bill.destination=inv.landfill.name AND bill.destination not in ('Grows','Tullytown')) OR bill.destination in ('Grows','Tullytown'))");

			if (!StringUtils.isEmpty(input.getRateFrom())) {

				query2.append(" and bill.rate>=").append(rateFrom);
			}
			if (!StringUtils.isEmpty(input.getRateTo())) {

				query2.append(" and bill.rate<=").append(rateTo);
			}

			if (!StringUtils.isEmpty(fromDateInvoiceStr)) {
				query2.append(" and  inv.invoiceDate>='").append(
						fromDateInvoiceStr + "'");
			}
			if (!StringUtils.isEmpty(toDateInvoiceStr)) {
				query2.append(" and  inv.invoiceDate<='").append(
						toDateInvoiceStr + "'");
			}
			if (!StringUtils.isEmpty(invoiceNumberFrom)) {
				query2.append(" and  bill.invoiceNo>='").append(
						invoiceNumberFrom + "'");
			}
			if (!StringUtils.isEmpty(invoiceNumberTo)) {
				query2.append(" and  bill.invoiceNo<='").append(
						invoiceNumberTo + "'");
			}

			if (!StringUtils.isEmpty(fuelSurchargeFrom1)) {
				query2.append(" and bill.fuelSurcharge >= '").append(
						fuelSurchargeFrom1 + "'");
			}
			if (!StringUtils.isEmpty(fuelSurchargeTo1)) {
				query2.append(" and bill.fuelSurcharge <= '").append(
						fuelSurchargeTo1 + "'");
			}
			// /hereeee

			if (!StringUtils.isEmpty(tonnagePremiumFrom)) {
				query2.append(" and bill.tonnagePremium >= ").append(
						tonnagePremiumFrom);
			}
			if (!StringUtils.isEmpty(tonnagePremiumTo)) {
				query2.append(" and bill.tonnagePremium <= ").append(
						tonnagePremiumTo);
			}

			if (!StringUtils.isEmpty(demurrageChargeFrom)) {
				query2.append(" and bill.demurrageCharge >= ").append(
						demurrageChargeFrom);
			}
			if (!StringUtils.isEmpty(demurrageChargeTo)) {
				query2.append(" and bill.demurrageCharge <= ").append(
						demurrageChargeTo);
			}
			if (!StringUtils.isEmpty(amountFrom)) {
				query2.append(" and bill.amount >= ").append(amountFrom);
			}
			if (!StringUtils.isEmpty(amountTo)) {
				query2.append(" and bill.amount <= ").append(amountTo);
			}
			if (!StringUtils.isEmpty(totalAmtFrom)) {
				query2.append(" and bill.totAmt >= ").append(totalAmtFrom);
			}
			if (!StringUtils.isEmpty(totalAmtTo)) {
				query2.append(" and bill.totAmt <= ").append(totalAmtTo);
			}
			if (!StringUtils.isEmpty(totAmtFrom)) {
				query2.append(" and inv.sumTotal >= ").append(totAmtFrom);
			}
			if (!StringUtils.isEmpty(totAmtTo)) {
				query2.append(" and inv.sumTotal <= ").append(totAmtTo);
			}

			useInvoice = true;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(query2
					.toString());
			System.out.println("\nquery2.toString()===>" + query2.toString()
					+ "\n");
			System.out.println("\ntickets.size===>" + tickets.size() + "\n");
			if (tickets != null && tickets.size() > 0) {
				for (Ticket tkt : tickets) {
					ticketIds.append(tkt.getId()).append(",");
				}
			}
			if (ticketIds.indexOf(",") != -1) {
				ticketIds.deleteCharAt(ticketIds.length() - 1);
			}
		}
		Map<String, String> params = new HashMap<String, String>();
		String batchDateFrom = ReportDateUtil.getFromDate(input
				.getBatchDateFrom());
		String batchDateTo = ReportDateUtil.getToDate(input.getBatchDateTo());
		String loadDateFrom = ReportDateUtil.getFromDate(input.getLoadedFrom());
		String loadDateTo = ReportDateUtil.getToDate(input.getLoadedTo());
		String unloadDateFrom = ReportDateUtil.getFromDate(input
				.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());

		String ticketStatus = input.getTicketStatus();
		String notBillable = input.getNotBillable();
		String terminal = input.getTerminal();
		String createdBy = input.getCreatedBy();
		String origin = input.getOrigin();
		String destination = input.getDestination();
		String driver = input.getDriver();
		String truck = input.getTruck();
		String trailer = input.getTrailer();
		String subcontractor = input.getSubcontractor();

		String originTicketFrom = input.getOriginTicketFrom();
		String destinationTicketFrom = input.getDestinationTicketFrom();
		String originGrossWtFrom = input.getOriginGrossWtFrom();
		String originTareWtFrom = input.getOriginTareWtFrom();
		String originTonWtFrom = input.getOriginTonsWtFrom();
		String landfillGrossWtFrom = input.getLandfillGrossWtFrom();
		String landfillTareWtFrom = input.getLandfillTareWtFrom();
		String landfillTonWtFrom = input.getLandfillTonsWtFrom();

		String transferTimeInFrom = input.getTransferTimeInFrom();
		String transferTimeInTo = input.getTransferTimeInTo();
		String transferTimeOutFrom = input.getTransferTimeOutFrom();
		String transferTimeOutTo = input.getTransferTimeOutTo();

		String landfillTimeInFrom = input.getLandfillTimeInFrom();
		String landfillTimeInTo = input.getLandfillTimeInTo();
		String landfillTimeOutFrom = input.getLandfillTimeOutFrom();
		String landfillTimeOutTo = input.getLandfillTimeOutTo();

		String originTicketTo = input.getOriginTicketTo();
		String destinationTicketTo = input.getDestinationTicketTo();
		String originGrossWtTo = input.getOriginGrossWtTo();
		String originTareWtTo = input.getOriginTareWtTo();
		String originTonWtTo = input.getOriginTonsWtTo();
		String landfillGrossWtTo = input.getLandfillGrossWtTo();
		String landfillTareWtTo = input.getLandfillTareWtTo();
		String landfillTonWtTo = input.getLandfillTonsWtTo();

		/* String */rateFrom = input.getRateFrom();
		params.put("rateFrom", rateFrom);
		// String amountFrom=input.getAmountFrom();
		params.put("amountFrom", amountFrom);
		String fuelSurchargeFrom = input.getFuelSurchargeFrom();
		params.put("fuelSurchargeFrom", fuelSurchargeFrom);
		// String tonnagePremiumFrom=input.getTonnagePremiumFrom();
		params.put("tonnagePremiumFrom", tonnagePremiumFrom);
		// String demurrageChargeFrom=input.getDemurrageChargeFrom();
		params.put("demurrageChargeFrom", demurrageChargeFrom);
		// String totAmtFrom=input.getTotAmtFrom();
		params.put("totAmtFrom", totAmtFrom);

		/* String */rateTo = input.getRateTo();
		params.put("rateTo", rateTo);
		// String amountTo=input.getAmountTo();
		params.put("amountTo", amountTo);
		String fuelSurchargeTo = input.getFuelSurchargeTo();
		params.put("fuelSurchargeTo", fuelSurchargeTo);
		// String demurrageChargeTo=input.getDemurrageChargeTo();
		params.put("demurrageChargeTo", demurrageChargeTo);
		// String tonnagePremiumTo=input.getTonnagePremiumTo();
		params.put("tonnagePremiumTo", tonnagePremiumTo);
		// String totAmtTo=input.getTotAmtTo();
		params.put("totAmtTo", totAmtTo);
		
		String company = input.getCompany();
		params.put("company", company);
		
		
		String driverCompany = input.getDriverCompany();
		params.put("driverCompany",driverCompany);
		
		params.put("customer", input.getCustomer());

		/*
		 * boolean rate=false; StringBuffer ticketIds1 = new
		 * StringBuffer("-1,"); if((!StringUtils.isEmpty(input.getCompany())) ||
		 * (!StringUtils.isEmpty(input.getCustomer())) ){ StringBuffer query3 =
		 * new StringBuffer(
		 * "select tic from Ticket tic,BillingRate bill where tic.status =1 and 1=1 and tic.billBatch between '"
		 * +batchDateFrom+"' and '"+batchDateTo+"'"+
		 * "and (tic.origin= bill.transferStation and tic.destination= bill.landfill)"
		 * ); StringBuffer query3 = new StringBuffer(
		 * "select tic from Ticket tic,BillingRate bill where tic.status =1 and 1=1 and (tic.origin= bill.transferStation and tic.destination= bill.landfill)"
		 * ); if(!StringUtils.isEmpty(input.getRateFrom())){
		 * 
		 * query3.append(" and bill.value>=").append(rateFrom); }
		 * if(!StringUtils.isEmpty(input.getRateTo())){
		 * 
		 * query3.append(" and bill.value<=").append(rateTo); }
		 * if(!StringUtils.isEmpty(input.getCompany())){
		 * 
		 * query3.append(" and bill.companyLocation.id in (").append(company).append
		 * (")"); }
		 * 
		 * if(!StringUtils.isEmpty(input.getCustomer())){
		 * 
		 * query3.append(" and bill.customername.id in (").append(input.getCustomer
		 * ()).append(")"); } rate=true;
		 * 
		 * List<Ticket> tickets =
		 * genericDAO.executeSimpleQuery(query3.toString());
		 * 
		 * if (tickets!=null && tickets.size()>0) {
		 * 
		 * for(Ticket tkt:tickets) {
		 * 
		 * ticketIds1.append(tkt.getId()).append(","); } } if
		 * (ticketIds1.indexOf(",")!=-1) {
		 * ticketIds1.deleteCharAt(ticketIds1.length()-1); } }
		 */

		StringBuffer query = new StringBuffer("");
		StringBuffer countQuery = new StringBuffer("");
		/*
		 * query.append(
		 * "select obj from Ticket obj where obj.status=1 and obj.billBatch between '"
		 * +batchDateFrom+"' and '"+batchDateTo+"'");
		 */
		query.append("select obj from Ticket obj where (obj.status=1 OR obj.status=3) ");
		/*
		 * countQuery.append(
		 * "select count(obj) from Ticket obj where obj.status=1 and obj.billBatch between '"
		 * +batchDateFrom+"' and '"+batchDateTo+"'");
		 */
		countQuery
				.append("select count(obj) from Ticket obj where (obj.status=1 OR obj.status=3)");
		if (useInvoice) {
			query.append(" and obj.id in (" + ticketIds.toString() + ")");
			countQuery.append(" and obj.id in (" + ticketIds.toString() + ")");
		}

		/*
		 * if (rate) {
		 * query.append(" and obj.id in ("+ticketIds1.toString()+")");
		 * countQuery.append(" and obj.id in ("+ticketIds1.toString()+")"); }
		 */
		//
		if (!StringUtils.isEmpty(input.getCompany())) {

			query.append(" and obj.companyLocation.id in (").append(company)
					.append(")");
			countQuery.append(" and obj.companyLocation.id in (")
					.append(company).append(")");
		}
		
		if (!StringUtils.isEmpty(input.getDriverCompany())) {

			query.append(" and obj.driverCompany.id in (").append(driverCompany)
					.append(")");
			countQuery.append(" and obj.driverCompany.id in (")
					.append(driverCompany).append(")");
		}
		

		if (!StringUtils.isEmpty(input.getCustomer())) {

			query.append(" and obj.customer.id in (")
					.append(input.getCustomer()).append(")");
			countQuery.append(" and obj.customer.id in (")
					.append(input.getCustomer()).append(")");
		}

		//
		if (!StringUtils.isEmpty(batchDateTo)) {
			query.append(" and  obj.billBatch<='").append(batchDateTo + "'");
			countQuery.append(" and  obj.billBatch<='").append(
					batchDateTo + "'");
		}
		if (!StringUtils.isEmpty(batchDateFrom)) {
			query.append(" and  obj.billBatch>='").append(batchDateFrom + "'");
			countQuery.append(" and  obj.billBatch>='").append(
					batchDateFrom + "'");
		}

		if (!StringUtils.isEmpty(ticketStatus)) {
			query.append(" and  obj.ticketStatus in (").append(ticketStatus)
					.append(")");
			countQuery.append(" and  obj.ticketStatus in (")
					.append(ticketStatus).append(")");
		}
		/*if (StringUtils.isNotEmpty(notBillable)) {
			query.append(" and obj.notBillable='").append(notBillable).append("'");
			countQuery.append(" and obj.notBillable='").append(notBillable).append("'");
		}*/
		// For search detail report
		if (StringUtils.equals(BillingHistoryInput.EXCLUDE_NOT_BILLABLE, notBillable)) {
			query.append(" and (obj.notBillable = '' or obj.notBillable is null)");
			countQuery.append(" and (obj.notBillable = '' or obj.notBillable is null)");
		}
		if (!StringUtils.isEmpty(fromDateInvoiceStr)) {
			query.append(" and  obj.invoiceDate>='").append(
					fromDateInvoiceStr + "'");
			countQuery.append(" and  obj.invoiceDate>='").append(
					fromDateInvoiceStr + "'");
		}
		if (!StringUtils.isEmpty(toDateInvoiceStr)) {
			query.append(" and  obj.invoiceDate<='").append(
					toDateInvoiceStr + "'");
			countQuery.append(" and  obj.invoiceDate<='").append(
					toDateInvoiceStr + "'");
		}
		/*
		 * if (!StringUtils.isEmpty(terminal)) {
		 * query.append(" and  obj.driver.terminal.id in ("
		 * ).append(terminal).append(")");
		 * countQuery.append(" and  obj.driver.terminal.id in ("
		 * ).append(terminal).append(")"); }
		 */
		if (!StringUtils.isEmpty(terminal)) {
			// System.out.println("\nTerminal===>"+terminal+"\n");
			query.append(" and  obj.terminal in (").append(terminal)
					.append(")");
			countQuery.append(" and  obj.terminal in (").append(terminal)
					.append(")");
		}
		if (!StringUtils.isEmpty(createdBy)) {
			query.append(" and  obj.createdBy in (").append(createdBy)
					.append(")");
			countQuery.append(" and  obj.createdBy in (").append(createdBy)
					.append(")");
		}
		if (!StringUtils.isEmpty(origin)) {
			query.append(" and  obj.origin.id in (").append(origin).append(")");
			countQuery.append(" and  obj.origin.id in (").append(origin)
					.append(")");
		}

		StringUtils.contains("194", destination);
		System.out.println("\n 194-->"
				+ StringUtils.contains("194", destination));
		System.out.println("\n destination-->" + destination);
		if (!StringUtils.isEmpty(destination)) {
			String[] elements = destination.split(",");

			boolean isGrowsTullyTown = false;
			for (int i = 0; i < elements.length; i++) {
				System.out.println(elements[i]);
				if (elements[i].contains("91")) {
					isGrowsTullyTown = true;
					/*
					 * Map criterias = new HashMap(); criterias.put("name",
					 * "Grows"); criterias.put("type", 2); Location grows=
					 * genericDAO.getByCriteria(Location.class, criterias);
					 * criterias.clear(); criterias.put("name", "Tullytown");
					 * criterias.put("type", 2); Location tullyTown=
					 * genericDAO.getByCriteria(Location.class, criterias);
					 * query
					 * .append(" and  obj.destination.id in(").append(destination
					 * +","+grows.getId()+","+tullyTown.getId()).append(")");
					 * countQuery
					 * .append(" and  obj.destination.id in (").append(
					 * destination
					 * +","+grows.getId()+","+tullyTown.getId()).append(")");
					 */
				}
			}
			if (isGrowsTullyTown) {
				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows = genericDAO.getByCriteria(Location.class,
						criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown = genericDAO.getByCriteria(Location.class,
						criterias);
				query.append(" and  obj.destination.id in(")
						.append(destination + "," + grows.getId() + ","
								+ tullyTown.getId()).append(")");
				countQuery
						.append(" and  obj.destination.id in (")
						.append(destination + "," + grows.getId() + ","
								+ tullyTown.getId()).append(")");
			}

			else {
				query.append(" and  obj.destination.id in(")
						.append(destination).append(")");
				countQuery.append(" and  obj.destination.id in (")
						.append(destination).append(")");
			}

			/*
			 * query.append(" and  obj.destination.id in(").append(destination).
			 * append(")");
			 * countQuery.append(" and  obj.destination.id in (").append
			 * (destination).append(")");
			 */
		}
		if (!StringUtils.isEmpty(originTicketFrom)) {
			query.append(" and obj.originTicket >= ").append(originTicketFrom);
			countQuery.append(" and obj.originTicket >= ").append(
					originTicketFrom);
		}
		if (!StringUtils.isEmpty(originTicketTo)) {
			query.append(" and obj.originTicket <= ").append(originTicketTo);
			countQuery.append(" and obj.originTicket <= ").append(
					originTicketTo);
		}
		if (!StringUtils.isEmpty(destinationTicketFrom)) {
			query.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
			countQuery.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
		}
		if (!StringUtils.isEmpty(destinationTicketTo)) {
			query.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
			countQuery.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
		}
		if (!StringUtils.isEmpty(driver)) {
			query.append(" and  obj.driver.id in (").append(driver).append(")");
			countQuery.append(" and  obj.driver.id in (").append(driver)
					.append(")");
		}
		if (!StringUtils.isEmpty(truck)) {
			
			String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
				+truck
				+")";
			
			
			
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
			
			query.append(" and  obj.vehicle.id in (").append(truckIds).append(")");
			;
			countQuery.append(" and  obj.vehicle.id in (").append(truckIds)
					.append(")");
			;
		}
		
		
		if (!StringUtils.isEmpty(trailer)) {
			
			String vehiclequery="select obj from Vehicle obj where obj.type=2 and obj.unit in ("
				+trailer
				+")";
			
			
			
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
			
			
			query.append(" and  obj.trailer.id in (").append(trailerIds)
					.append(")");
			;
			countQuery.append(" and  obj.trailer.id in (").append(trailerIds)
					.append(")");
			;
		}
		
		
		if (!StringUtils.isEmpty(subcontractor)) {
			query.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
			countQuery.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
		}
		if (!StringUtils.isEmpty(originGrossWtFrom)) {
			query.append(" and  obj.transferGross>=").append(originGrossWtFrom);
			countQuery.append(" and  obj.transferGross>=").append(
					originGrossWtFrom);
		}
		if (!StringUtils.isEmpty(originGrossWtTo)) {
			query.append(" and  obj.transferGross<=").append(originGrossWtTo);
			countQuery.append(" and  obj.transferGross<=").append(
					originGrossWtTo);
		}
		if (!StringUtils.isEmpty(originTareWtFrom)) {
			query.append(" and  obj.transferTare>=").append(originTareWtFrom);
			countQuery.append(" and  obj.transferTare>=").append(
					originTareWtFrom);
		}
		if (!StringUtils.isEmpty(originTareWtTo)) {
			query.append(" and  obj.transferTare<=").append(originTareWtTo);
			countQuery.append(" and  obj.transferTare<=")
					.append(originTareWtTo);
		}
		if (!StringUtils.isEmpty(originTonWtFrom)) {
			query.append(" and  obj.transferTons>=").append(originTonWtFrom);
			countQuery.append(" and  obj.transferTons>=").append(
					originTonWtFrom);
		}
		if (!StringUtils.isEmpty(originTonWtTo)) {
			query.append(" and  obj.transferTons<=").append(originTonWtTo);
			countQuery.append(" and  obj.transferTons<=").append(originTonWtTo);
		}
		if (!StringUtils.isEmpty(landfillGrossWtFrom)) {
			query.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
			countQuery.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
		}
		if (!StringUtils.isEmpty(landfillGrossWtTo)) {
			query.append(" and  obj.landfillGross<=").append(landfillGrossWtTo);
			countQuery.append(" and  obj.landfillGross<=").append(
					landfillGrossWtTo);
		}
		if (!StringUtils.isEmpty(landfillTareWtFrom)) {
			query.append(" and  obj.landfillTare>=").append(landfillTareWtFrom);
			countQuery.append(" and  obj.landfillTare>=").append(
					landfillTareWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTareWtTo)) {
			query.append(" and  obj.landfillTare<=").append(landfillTareWtTo);
			countQuery.append(" and  obj.landfillTare<=").append(
					landfillTareWtTo);
		}
		if (!StringUtils.isEmpty(landfillTonWtFrom)) {
			query.append(" and  obj.landfillTons>=").append(landfillTonWtFrom);
			countQuery.append(" and  obj.landfillTons>=").append(
					landfillTonWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTonWtTo)) {
			query.append(" and  obj.landfillTons<=").append(landfillTonWtTo);
			countQuery.append(" and  obj.landfillTons<=").append(
					landfillTonWtTo);
		}
		if (!StringUtils.isEmpty(loadDateFrom)) {
			query.append(" and  obj.loadDate>='").append(loadDateFrom + "'");
			countQuery.append(" and  obj.loadDate>='").append(
					loadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(loadDateTo)) {
			query.append(" and  obj.loadDate<='").append(loadDateTo + "'");
			countQuery.append(" and  obj.loadDate<='").append(loadDateTo + "'");

		}
		if (!StringUtils.isEmpty(unloadDateFrom)) {
			query.append(" and  obj.unloadDate>='")
					.append(unloadDateFrom + "'");
			countQuery.append(" and  obj.unloadDate>='").append(
					unloadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(unloadDateTo)) {
			query.append(" and  obj.unloadDate<='").append(unloadDateTo + "'");
			countQuery.append(" and  obj.unloadDate<='").append(
					unloadDateTo + "'");
		}

		if (!StringUtils.isEmpty(transferTimeInFrom)
				&& !StringUtils.isEmpty(transferTimeInTo)) {
			query.append(" and  obj.transferTimeIn Between '")
					.append(transferTimeInFrom + "'").append(" and '")
					.append(transferTimeInTo + "'");
			countQuery.append(" and  obj.transferTimeIn Between '")
					.append(transferTimeInFrom + "'").append(" and '")
					.append(transferTimeInTo + "'");

		}
		if (!StringUtils.isEmpty(transferTimeOutFrom)
				&& !StringUtils.isEmpty(transferTimeOutTo)) {
			query.append(" and  obj.transferTimeOut Between '")
					.append(transferTimeOutFrom + "'").append(" and '")
					.append(transferTimeOutTo + "'");
			countQuery.append(" and  obj.transferTimeOut Between '")
					.append(transferTimeOutFrom + "'").append(" and '")
					.append(transferTimeOutTo + "'");
		}

		if (!StringUtils.isEmpty(landfillTimeInFrom)
				&& !StringUtils.isEmpty(landfillTimeInTo)) {
			query.append(" and  obj.landfillTimeIn Between '")
					.append(landfillTimeInFrom + "'").append(" and '")
					.append(landfillTimeInTo + "'");
			countQuery.append(" and  obj.landfillTimeIn Between '")
					.append(landfillTimeInFrom + "'").append(" and '")
					.append(landfillTimeInTo + "'");

		}
		if (!StringUtils.isEmpty(landfillTimeOutFrom)
				&& !StringUtils.isEmpty(landfillTimeOutTo)) {
			query.append(" and  obj.landfillTimeOut Between '")
					.append(landfillTimeOutFrom + "'").append(" and '")
					.append(landfillTimeOutTo + "'");
			countQuery.append(" and  obj.landfillTimeOut Between '")
					.append(landfillTimeOutFrom + "'").append(" and '")
					.append(landfillTimeOutTo + "'");
		}

		System.out.println("\nquery.toString()=>" + query.toString() + "\n");
		Long recordCount = (Long) genericDAO.getEntityManager()
				.createQuery(countQuery.toString()).getSingleResult();
		searchCriteria.setRecordCount(recordCount.intValue());
		System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");
		query.append(" order by billBatch desc, id");
		List<Ticket> tickets = (List<Ticket>) genericDAO
				.getEntityManager()
				.createQuery(query.toString())
				.setMaxResults(searchCriteria.getPageSize())
				.setFirstResult(
						searchCriteria.getPage() * searchCriteria.getPageSize())
				.getResultList();
		// ticketIds1=null;
		ticketIds = null;
		// Subcontractor summary report - 16thMar2016
		// Peak rate 2nd Feb 2021
		return processTicketsNew(tickets, params, false, isPeakRate);

		
	}
	
	private void setNotBillable(List<Billing_New> billingNewList, List<Ticket> ticketList) {
		if (billingNewList == null || billingNewList.isEmpty()
				|| ticketList == null || ticketList.isEmpty()) {
			return;
		}
		
		for (Ticket aTicket : ticketList) {
			if (StringUtils.isNotEmpty(aTicket.getNotBillable()) 
					&& BooleanUtils.toBoolean(aTicket.getNotBillable(), "Y", "N")) {
				Billing_New aBilling_New = findMathcingBillingNew(billingNewList, aTicket);
				if (aBilling_New != null) {
					aBilling_New.setNotBillable(aTicket.getNotBillable());
				}
			}
		}
	}
	
	private Billing_New findMathcingBillingNew(List<Billing_New> billingNewList, Ticket aTicket) {
		for (Billing_New aBilling_New : billingNewList) {
			if (aBilling_New.getTicket() == aTicket.getId().longValue()) {
				return aBilling_New;
			}
		}
		return null;
	}
	
	// Subcontractor summary report - 16thMar2016
	private BillingWrapper processTicketsNew(List<Ticket> tickets,Map<String, String> params, 
				boolean isSummary, String isPeakRate) { // Peak rate 2nd Feb 2021
		
		List<Billing_New> summarys = new ArrayList<Billing_New>();
		BillingWrapper wrapper = new BillingWrapper();
		double sumGallon = 0.0;
		double sumNet = 0.0;
		double sumBillableTon = 0.0;
		double sumOriginTon = 0.0;
		double sumDestinationTon = 0.0;
		double sumAmount = 0.0;
		double sumFuelSurcharge = 0.0;
		double sumTonnage = 0.0;
		double sumDemmurage = 0.0;
		double sumTotal = 0.0;	
		
		
		
		
		 StringBuffer ticketIds = new StringBuffer("-1,");
			for (Ticket tkt : tickets) {
				ticketIds.append(tkt.getId()).append(",");
			}
			if (ticketIds.indexOf(",") != -1) {
				ticketIds.deleteCharAt(ticketIds.length() - 1);
			}			
			
			String query="select obj from Billing_New obj where obj.ticket in ("
				+ticketIds.toString()+")";
			
			// Peak rate 2nd Feb 2021
			if (StringUtils.isNotEmpty(isPeakRate)) {
				query += (" and obj.isPeakRate='" + isPeakRate + "'");
			}
			
			summarys=genericDAO.executeSimpleQuery(query);	
			 
			 setNotBillable(summarys, tickets);
			 
			// Subcontractor summary report - 16thMar2016
		   wrapper.setBillings(summarys);
		   if (isSummary) {
		   	return wrapper;
		   }
			
			 String sum_query="select sum(obj.effectiveNetWt),sum(obj.effectiveTonsWt),sum(obj.originTonsWt),sum(obj.destinationTonsWt),sum(obj.amount),sum(obj.fuelSurcharge),sum(obj.tonnagePremium),sum(obj.demurrageCharge) from Billing_New obj where obj.ticket in ("
				+ticketIds.toString()+")";
			 
			 List<Billing_New> sum_list=genericDAO.executeSimpleQuery(sum_query);
			
			 if(sum_list!=null && sum_list.size()>0){
			 for(Object obj:sum_list){
				 Object[] objarry=(Object[])obj;
				if(objarry!=null){
				if(objarry[0] !=null)	
				 sumNet=Double.parseDouble(objarry[0].toString());
				if(objarry[1] !=null)
				 sumBillableTon=Double.parseDouble(objarry[1].toString());
				if(objarry[2] !=null)
				 sumOriginTon=Double.parseDouble(objarry[2].toString());
				if(objarry[3] !=null)
				 sumDestinationTon=Double.parseDouble(objarry[3].toString());
				if(objarry[4] !=null)
				 sumAmount=Double.parseDouble(objarry[4].toString());
				if(objarry[5] !=null)
				 sumFuelSurcharge=Double.parseDouble(objarry[5].toString());
				if(objarry[6] !=null)
				 sumTonnage=Double.parseDouble(objarry[6].toString());
				if(objarry[7] !=null)
				 sumDemmurage=Double.parseDouble(objarry[7].toString());
				}
			 }
			 }		
		
		// Subcontractor summary report - 16thMar2016
	   // wrapper.setBillings(summarys);	     
		sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);
		sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
		sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);
		sumNet = MathUtil.roundUp(sumNet, 2);
		sumAmount = MathUtil.roundUp(sumAmount, 2);
		sumFuelSurcharge = MathUtil.roundUp(sumFuelSurcharge, 2);
		sumDemmurage = MathUtil.roundUp(sumDemmurage, 2);
		sumTonnage = MathUtil.roundUp(sumTonnage, 2);
		sumGallon = MathUtil.roundUp(sumGallon, 2);		

		sumTotal = sumAmount + sumFuelSurcharge + sumDemmurage + sumTonnage;
		wrapper.setSumBillableTon(sumBillableTon);
		wrapper.setSumOriginTon(sumOriginTon);
		wrapper.setSumDestinationTon(sumDestinationTon);
		wrapper.setSumTonnage(sumTonnage);
		wrapper.setSumNet(sumNet);
		wrapper.setSumAmount(sumAmount);
		wrapper.setSumFuelSurcharge(sumFuelSurcharge);
		wrapper.setSumTotal(sumTotal);
		wrapper.setSumGallon(sumGallon);
		wrapper.setTotalRowCount(tickets.size());
		return wrapper;
	}
	
	// Subcontractor summary report - 16thMar2016
	public List<SubcontractorSummary> generateSubcontractorSummaryReport(SearchCriteria criteria, 
			SubcontractorReportInput input) {
		List<SubcontractorSummary> subcontractorSummaryList = new ArrayList<SubcontractorSummary>();
		
		// First get the subcontractor billing data
		SubcontractorBillingWrapper subcontractorBillingWrapper = generateSubcontractorReportData(criteria, input, true);
		List<SubcontractorBillingNew> subcontractorBillingNewList = subcontractorBillingWrapper.getSubcontractorBillingsNew();
		if (subcontractorBillingNewList == null || subcontractorBillingNewList.isEmpty()) {
			return subcontractorSummaryList;
		}
		
		// Subcontractor summary by load report - 14thApr2017
		boolean summaryByLoad = false;
		if (criteria.getSearchMap().get("summaryByLoad") != null) {
			summaryByLoad = BooleanUtils.toBoolean((String)criteria.getSearchMap().get("summaryByLoad"));
		}
		
		// Group by subcontractor name and calculate total amount paid
		Map<String, SubcontractorSummary> subcontractorSummaryMap = new HashMap<String, SubcontractorSummary>();
		for(SubcontractorBillingNew aSubcontractorBillingNew : subcontractorBillingNewList) {
			String subContractorName = aSubcontractorBillingNew.getSubcontractor();
			// Subcontractor summary by load report - 14thApr2017
			String subContractorOrigin = aSubcontractorBillingNew.getSub_origin();
			String subContractorDestination = aSubcontractorBillingNew.getSub_destination();
			
			// Subcontractor summary by load report - 14thApr2017
			String key = subContractorName;
			if (summaryByLoad) {
				key += ("_" + subContractorOrigin + "_" + subContractorDestination);
			}
			
			// Subcontractor summary by load report - 14thApr2017
			SubcontractorSummary aSubcontractorSummary = subcontractorSummaryMap.get(key);
			if (aSubcontractorSummary == null) {
				aSubcontractorSummary = new SubcontractorSummary();
				aSubcontractorSummary.setSubcontractorName(subContractorName);
				// Subcontractor summary by load report - 14thApr2017
				aSubcontractorSummary.setOrigin(subContractorOrigin);
				aSubcontractorSummary.setDestination(subContractorDestination);
				aSubcontractorSummary.setLoadCount(0);
				subcontractorSummaryMap.put(key, aSubcontractorSummary);
				
				subcontractorSummaryList.add(aSubcontractorSummary);
			}
			
			Double amountPaid = aSubcontractorSummary.getAmountPaid() + aSubcontractorBillingNew.getTotAmt();
			aSubcontractorSummary.setAmountPaid(amountPaid);
			
			// Subcontractor summary by load report - 14thApr2017
			aSubcontractorSummary.setLoadCount(aSubcontractorSummary.getLoadCount() + 1);
		}
		
		// Then get customer billing data for the same tickets retrieved above
		BillingWrapper billingWrapper = processTicketsNew(subcontractorBillingWrapper.getTickets(), null, true,
					StringUtils.EMPTY); // Peak rate 2nd Feb 2021
		List<Billing_New> billingNewList = billingWrapper.getBillings();
		if (billingNewList == null || billingNewList.isEmpty()) {
			System.out.println("Subcontractor summary report: No billing history found for specified subcontractor tickets");
			return subcontractorSummaryList;
		}
		
		// Group by subcontractor name in customer billing data and calculate total revenue
		Map<String, SubcontractorSummary> subcontractorSummaryBillingMap = new HashMap<String, SubcontractorSummary>();
		for(Billing_New aBillingNew : billingNewList) {
			String subContractorName = aBillingNew.getSubcontractor();
			if (StringUtils.isEmpty(subContractorName)) {
				System.out.println("Subcontractor summary report: Empty subcontractor in customer billing history");
				continue;
			}
			
			// Subcontractor summary by load report - 14thApr2017
			String subContractorOrigin = aBillingNew.getT_origin();
			String subContractorDestination = aBillingNew.getT_destination();
		
			// Subcontractor summary by load report - 14thApr2017
			String key = subContractorName;
			if (summaryByLoad) {
				if (StringUtils.isEmpty(subContractorOrigin) || StringUtils.isEmpty(subContractorDestination)) {
					System.out.println("Subcontractor summary by load report: Empty origin or destination in customer billing history");
					continue;
				}
				
				key += ("_" + subContractorOrigin + "_" + subContractorDestination);
			}
			
			// Subcontractor summary by load report - 14thApr2017
			SubcontractorSummary aSubcontractorSummary = subcontractorSummaryBillingMap.get(key);
			if (aSubcontractorSummary == null) {
				aSubcontractorSummary = new SubcontractorSummary();
				aSubcontractorSummary.setSubcontractorName(subContractorName);
				// Subcontractor summary by load report - 14thApr2017
				aSubcontractorSummary.setOrigin(subContractorOrigin);
				aSubcontractorSummary.setDestination(subContractorDestination);
				
				// Subcontractor summary by load report - 14thApr2017
				subcontractorSummaryBillingMap.put(key, aSubcontractorSummary);
			}
			
			Double revenue = aSubcontractorSummary.getRevenue() + aBillingNew.getTotAmt();
			aSubcontractorSummary.setRevenue(revenue);
		}
		
		// Merge subcontractor paid data and customer billed data and calculate net amount
		for(SubcontractorSummary aSubcontractorSummary : subcontractorSummaryList) {
			String subContractorName = aSubcontractorSummary.getSubcontractorName();
			// Subcontractor summary by load report - 14thApr2017
			String subContractorOrigin = aSubcontractorSummary.getOrigin();
			String subContractorDestination = aSubcontractorSummary.getDestination();
			
			// Subcontractor summary by load report - 14thApr2017
			String key = subContractorName;
			if (summaryByLoad) {
				key += ("_" + subContractorOrigin + "_" + subContractorDestination);
			}
			
			// Subcontractor summary by load report - 14thApr2017
			SubcontractorSummary aSubcontractorBillingSummary = subcontractorSummaryBillingMap.get(key);
			if (aSubcontractorBillingSummary == null) {
				System.out.println("Subcontractor summary report: Subcontractor not found in customer billing history: " + subContractorName
						+ "/" + subContractorOrigin + "/" + subContractorDestination);
			} else {
				aSubcontractorSummary.setRevenue(aSubcontractorBillingSummary.getRevenue());
				Double netAmount = aSubcontractorSummary.getRevenue() - aSubcontractorSummary.getAmountPaid();
				aSubcontractorSummary.setNetAmount(netAmount);
			}
		}
		
		return subcontractorSummaryList;
	}

	private BillingWrapper processTickets(List<Ticket> tickets,
			Map<String, String> params) {
		List<Billing> summarys = new ArrayList<Billing>();
		BillingWrapper wrapper = new BillingWrapper();

		wrapper.setBilling(summarys);
		double sumGallon = 0.0;
		double sumNet = 0.0;
		double sumBillableTon = 0.0;
		double sumOriginTon = 0.0;
		double sumDestinationTon = 0.0;
		double sumAmount = 0.0;
		double sumFuelSurcharge = 0.0;
		double sumTonnage = 0.0;
		double sumDemmurage = 0.0;
		double sumTotal = 0.0;
		Map<String, List<BillingRate>> billingMap = new HashMap<String, List<BillingRate>>();
		for (Ticket ticket : tickets) {

			Billing billing = new Billing();
			// Billing invoice=null;
			Map criterias = new HashMap();
			if (ticket.getTicketStatus() == 2) {
				Billing invoice = null;
				criterias.put("ticket.id", ticket.getId());
				invoice = genericDAO.getByCriteria(Billing.class, criterias);
				if (invoice == null)
					continue;

				billing = invoice;

				sumNet += (Double) billing.getEffectiveNetWt();
				sumBillableTon += (Double) billing.getEffectiveTonsWt();

				sumOriginTon += (Double) billing.getOriginTonsWt();
				sumDestinationTon += (Double) billing.getDestinationTonsWt();

				sumAmount += billing.getAmount();
				sumFuelSurcharge += billing.getFuelSurcharge();
				sumTonnage += billing.getTonnagePremium();
				sumDemmurage += billing.getDemurrageCharge();
				// sumTotal+=billing.getTotAmt();
				if (ticket.getCreatedBy() != null) {
					User user = genericDAO.getById(User.class,
							ticket.getCreatedBy());
					if (user != null) {
						billing.setEnteredBy(user.getUsername());
					}
				}
				billing.setTransferTimeIn(ticket.getTransferTimeIn());
				billing.setTransferTimeOut(ticket.getTransferTimeOut());
				billing.setLandfillTimeIn(ticket.getLandfillTimeIn());
				billing.setLandfillTimeOut(ticket.getLandfillTimeOut());
				billing.setProcessStatus(StaticDataUtil.getText(
						"TICKET_STATUS", "" + ticket.getTicketStatus()));
				if (ticket.getCompanyLocation() != null)
					billing.setCompany((ticket.getCompanyLocation() != null) ? ticket
							.getCompanyLocation().getName() : "");
				if (ticket.getCustomer() != null)
					billing.setCustomer((ticket.getCustomer() != null) ? ticket
							.getCustomer().getName() : "");
				

				if (ticket.getTrailer() != null) {
					billing.setTrailer("" + ticket.getTrailer().getUnit());
				}

				if (ticket.getDriverCompany() != null){
					billing.setDriverCompany(ticket.getDriverCompany());
					billing.setDriverCompanyName(ticket.getDriverCompany().getName());
					
				}
				else{
					billing.setDriverCompanyName("");
				}
				
				billing.setSubcontractor((ticket.getSubcontractor() != null) ? ticket
						.getSubcontractor().getName() : "");
				billing.setInvoiceDate((ticket.getInvoiceDate() != null) ? (sdf
						.format(ticket.getInvoiceDate())) : null);
				//
				billing.setLoaded(sdf.format(ticket.getLoadDate()));
				if (ticket.getVehicle() != null) {
					billing.setUnit("" + ticket.getVehicle().getUnit());
				}
				if (ticket.getTrailer() != null) {
					billing.setTrailer("" + ticket.getTrailer().getUnit());
				}
				billing.setTerminal((ticket.getTerminal() != null) ? ticket
						.getTerminal().getName() : "");
				billing.setDriver((ticket.getDriver() != null) ? ticket
						.getDriver().getFullName() : "");
				billing.setUnloaded(sdf.format(ticket.getUnloadDate()));
				billing.setOriginTicket(String.valueOf(ticket.getOriginTicket()));
				billing.setDestinationTicket(String.valueOf(ticket
						.getDestinationTicket()));
				summarys.add(billing);
				System.out.println("\n continue");
				continue;

			}
			billing.setTicket(ticket);
			billing.setLoaded(sdf.format(ticket.getLoadDate()));
			if (ticket.getVehicle() != null) {
				billing.setUnit("" + ticket.getVehicle().getUnit());
			}
			if (ticket.getTrailer() != null) {
				billing.setTrailer("" + ticket.getTrailer().getUnit());
			}
			billing.setDate(sdf.format(ticket.getBillBatch()));
			billing.setDriver((ticket.getDriver() != null) ? ticket.getDriver()
					.getFullName() : "");
			billing.setSubcontractor((ticket.getSubcontractor() != null) ? ticket
					.getSubcontractor().getName() : "");
			billing.setOrigin((ticket.getOrigin() != null) ? ticket.getOrigin()
					.getName() : "");
			billing.setDestination((ticket.getDestination() != null) ? ticket
					.getDestination().getName() : "");
			
			if (ticket.getDriverCompany() != null){
				billing.setDriverCompany(ticket.getDriverCompany());
				billing.setDriverCompanyName(ticket.getDriverCompany().getName());
				
			}
			else{
				billing.setDriverCompanyName("");
			}
			
			
			
			billing.setTerminal((ticket.getTerminal() != null) ? ticket
					.getTerminal().getName() : "");
			
			if (ticket.getCreatedBy() != null) {
				User user = genericDAO.getById(User.class,
						ticket.getCreatedBy());
				if (user != null) {
					billing.setEnteredBy(user.getUsername());
				}
			}
			billing.setUnloaded(sdf.format(ticket.getUnloadDate()));
			billing.setOriginTicket(String.valueOf(ticket.getOriginTicket()));
			billing.setDestinationTicket(String.valueOf(ticket
					.getDestinationTicket()));
			billing.setOriginGrossWt(ticket.getTransferGross());
			billing.setOriginTareWt(ticket.getTransferTare());
			billing.setOriginNetWt(ticket.getTransferNet());
			billing.setOriginTonsWt(ticket.getTransferTons());
			billing.setDestinationGrossWt(ticket.getLandfillGross());
			billing.setDestinationTareWt(ticket.getLandfillTare());
			billing.setDestinationNetWt(ticket.getLandfillNet());
			billing.setDestinationTonsWt(ticket.getLandfillTons());
			billing.setInvoiceDate((ticket.getInvoiceDate() != null) ? (sdf
					.format(ticket.getInvoiceDate())) : null);
			billing.setInvoiceNo(ticket.getInvoiceNumber());
			billing.setTransferTimeIn(ticket.getTransferTimeIn());
			billing.setTransferTimeOut(ticket.getTransferTimeOut());
			billing.setLandfillTimeIn(ticket.getLandfillTimeIn());
			billing.setLandfillTimeOut(ticket.getLandfillTimeOut());
			billing.setProcessStatus(StaticDataUtil.getText("TICKET_STATUS", ""
					+ ticket.getTicketStatus()));
			BillingRate billingRate = null;
			try {
				Long destination_id;
				Location location = genericDAO.getById(Location.class, ticket
						.getDestination().getId());
				if (location.getName().equalsIgnoreCase("grows")
						|| location.getName().equalsIgnoreCase("tullytown")) {

					destination_id = 91l;

				} else {
					destination_id = ticket.getDestination().getId();
				}
				String rateQuery = "select obj from BillingRate obj where obj.transferStation='"
						+ ticket.getOrigin().getId() + "' and obj.landfill='"
						/* + ticket.getDestination().getId() + "'"; */
						+ destination_id + "'";
				String key = ticket.getOrigin().getId() + "_" + destination_id;
				
				List<BillingRate> fs = null;
				fs = billingMap.get(key);
				if (fs == null) {
					fs = genericDAO.executeSimpleQuery(rateQuery);
					billingMap.put(key, fs);
				}
				if (fs != null && fs.size() > 0) {
					for (BillingRate rate : fs) {
						if (rate.getRateUsing() == null) {
							billingRate = rate;
							break;
						} else if (rate.getRateUsing() == 1) {
							// calculation for a load date
							if ((ticket.getLoadDate().getTime() >= rate
									.getValidFrom().getTime())
									&& (ticket.getLoadDate().getTime() <= rate
											.getValidTo().getTime())) {
								billingRate = rate;
								break;
							}
						} else if (rate.getRateUsing() == 2) {
							// calculation for a unload date
							if ((ticket.getUnloadDate().getTime() >= rate
									.getValidFrom().getTime())
									&& (ticket.getUnloadDate().getTime() <= rate
											.getValidTo().getTime())) {
								billingRate = rate;
								break;
							}
						}
					}
					if (!StringUtils.isEmpty(params.get("company"))) {
						String[] companies = params.get("company").split(",");
						boolean sameCompany = false;
						for (String cmp : companies) {
							if (cmp.equalsIgnoreCase(billingRate
									.getCompanyLocation().getId().toString())) {
								sameCompany = true;
								break;
							}
						}
						if (!sameCompany)
							continue;
					}
					if ((billingRate.getCompanyLocation()) != null)
						billing.setCompany(billingRate.getCompanyLocation()
								.getName());
					if ((billingRate.getCompanyLocation()) != null)
						billing.setCompanyId(billingRate.getCompanyLocation());
					if (!StringUtils.isEmpty(params.get("customer"))) {
						String[] customers = params.get("customer").split(",");
						boolean sameCustomer = false;
						for (String cmp : customers) {
							if (cmp.equalsIgnoreCase(billingRate
									.getCustomername().getId().toString())) {
								sameCustomer = true;
								break;
							}
						}
						if (!sameCustomer)
							continue;
					}

					if ((billingRate.getCustomername()) != null)
						billing.setCustomer(billingRate.getCustomername()
								.getName());
					if ((billingRate.getCustomername()) != null)
						billing.setCustomerId(billingRate.getCustomername());
					if (billingRate.getBilledby().equals("bygallon")) {
					// Change to 8.35 - 28th Dec 2016
						billing.setGallon(ticket.getTransferNet()/8.35);
					} else {
						billing.setGallon(0.0);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (billingRate != null) {
				// Peak rate 2nd Feb 2021
				Object[] derivedRateInfo = deriveBillingRate(ticket, billingRate);
				boolean isPeakRate = (Boolean)derivedRateInfo[0];
				Double derivedRate = (Double)derivedRateInfo[1];
				
				// billing.setCustomer(billingRate.getCustomername().getName());
				int billUsing = (billingRate.getBillUsing() == null) ? 1
						: billingRate.getBillUsing();
				int rateType = billingRate.getRateType();
				if (billUsing == 1) {
					billing.setBillUsing("Transfer");
				}
				if (billUsing == 2) {
					billing.setBillUsing("Landfill");
				}
				if (billUsing == 1) {
					if (rateType == 2 || rateType == 3) {
						Double minbilgrosswt = billingRate.getMinbillablegrossWeight();
						if (minbilgrosswt != null
								&& ticket.getTransferGross() < minbilgrosswt) {
							billing.setEffectiveGrossWt(minbilgrosswt);
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getTransferTare());
							Double originNetWt = minbilgrosswt
									- ticket.getTransferTare();
							billing.setEffectiveNetWt(originNetWt);
							billing.setEffectiveTonsWt(originNetWt / 2000.0);
						} else {
							billing.setEffectiveGrossWt(ticket
									.getTransferGross());
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getTransferTare());
							billing.setEffectiveNetWt(ticket.getTransferNet());
							billing.setEffectiveTonsWt(ticket.getTransferTons());
						}
						
						// Min billable tons - 25th Jul 2017
						Double minBillableTons = billingRate.getMinBillableTons();
						billing.setMinimumBillableTons(minBillableTons);
						if (minBillableTons != null) {
							billing.setEffectiveGrossWt(ticket.getTransferGross());
							billing.setEffectiveTareWt(ticket.getTransferTare());
							billing.setEffectiveNetWt(ticket.getTransferNet());
							if (ticket.getTransferTons() < minBillableTons) {
								billing.setEffectiveTonsWt(minBillableTons);
							} else {
								billing.setEffectiveTonsWt(ticket.getTransferTons());
							}
						}
					}
				} else {
					if (rateType == 2 || rateType == 3) {
						Double minbilgrosswt = billingRate
								.getMinbillablegrossWeight();
						if (minbilgrosswt != null
								&& ticket.getLandfillGross() < minbilgrosswt) {
							billing.setEffectiveGrossWt(minbilgrosswt);
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getLandfillTare());
							Double destinationNetWt = minbilgrosswt
									- ticket.getLandfillTare();
							billing.setEffectiveNetWt(destinationNetWt);
							billing.setEffectiveTonsWt(destinationNetWt / 2000.0);

						} else {
							billing.setEffectiveGrossWt(ticket
									.getLandfillGross());
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getLandfillTare());
							billing.setEffectiveNetWt(ticket.getLandfillNet());
							billing.setEffectiveTonsWt(ticket.getLandfillTons());
						}
						
						// Min billable tons - 25th Jul 2017
						Double minBillableTons = billingRate.getMinBillableTons();
						billing.setMinimumBillableTons(minBillableTons);
						if (minBillableTons != null) {
							billing.setEffectiveGrossWt(ticket.getLandfillGross());
							billing.setEffectiveTareWt(ticket.getLandfillTare());
							billing.setEffectiveNetWt(ticket.getLandfillNet());
							if (ticket.getLandfillTons() < minBillableTons) {
								billing.setEffectiveTonsWt(minBillableTons);
							} else {
								billing.setEffectiveTonsWt(ticket.getLandfillTons());
							}
						}
					}
				}
				// int rateType = billingRate.getRateType();
				if (rateType == 1) {
					if (billUsing == 1) {
						Double minbilgrosswt = billingRate
								.getMinbillablegrossWeight();
						if (minbilgrosswt != null
								&& ticket.getTransferGross() < minbilgrosswt) {
							billing.setEffectiveGrossWt(minbilgrosswt);
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getTransferTare());
							Double originNetWt = minbilgrosswt
									- ticket.getTransferTare();
							billing.setEffectiveNetWt(originNetWt);
						} else {
							billing.setEffectiveGrossWt(ticket
									.getTransferGross());
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getTransferTare());
							billing.setEffectiveNetWt(ticket.getTransferNet());
						}
					} else {
						Double minbilgrosswt = billingRate
								.getMinbillablegrossWeight();
						if (minbilgrosswt != null
								&& ticket.getLandfillGross() < minbilgrosswt) {
							billing.setEffectiveGrossWt(minbilgrosswt);
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getLandfillTare());
							Double destinationNetWt = minbilgrosswt
									- ticket.getLandfillTare();
							billing.setEffectiveNetWt(destinationNetWt);
						} else {
							billing.setEffectiveGrossWt(ticket
									.getLandfillGross());
							billing.setMinimumbillablegrossweight(minbilgrosswt);
							billing.setEffectiveTareWt(ticket.getLandfillTare());
							billing.setEffectiveNetWt(ticket.getLandfillNet());
						}
					}
					
					// Peak rate 2nd Feb 2021
					//billing.setRate(billingRate.getValue());
					billing.setRate(derivedRate);
					billing.setIsPeakRate(BooleanUtils.toString(isPeakRate, "Y", "N"));
					
				// Change to 8.35 - 28th Dec 2016
					billing.setAmount((billing.getEffectiveNetWt() / 8.35)
							* derivedRate); // Peak rate 2nd Feb 2021
							// * billingRate.getValue());
				} else if (rateType == 2) {
					// per load
					// Peak rate 2nd Feb 2021
					//billing.setRate(billingRate.getValue());
					//billing.setAmount(billingRate.getValue());
					billing.setRate(derivedRate);
					billing.setAmount(derivedRate);
				} else if (rateType == 3) {
					// per tonne
					// Peak rate 2nd Feb 2021
					//billing.setRate(billingRate.getValue());
					billing.setRate(derivedRate);
					billing.setAmount(billing.getEffectiveTonsWt()
							* derivedRate); // Peak rate 2nd Feb 2021
							//* billingRate.getValue());
				}
				sumGallon += billing.getGallon();
				sumBillableTon += billing.getEffectiveTonsWt();
				sumOriginTon += billing.getOriginTonsWt();
				sumDestinationTon += billing.getDestinationTonsWt();
				sumNet += billing.getEffectiveNetWt();
				
				// Sum amount rounding fix - 8th Aug 2017
				//sumAmount += billing.getAmount();
				// Sum amount rounding fix changes - 23rd aug 2017
				if (isWMTicket(ticket, billingRate)) {
					sumAmount += MathUtil.roundUp(billing.getAmount(), 2);
				} else {
					sumAmount += billing.getAmount();
				}
				
				billing.setAmount(MathUtil.roundUp(billing.getAmount(), 2));
				
				String fuelSurchargeType = billingRate.getFuelSurchargeType();
				Double fuelSurcharge = 0.0;
				if ("N".equalsIgnoreCase(fuelSurchargeType)) {
					fuelSurcharge = 0.0;
				}
				if ("M".equalsIgnoreCase(fuelSurchargeType)) {
					Double fuelSurchargePerTon = billingRate
							.getSurchargePerTon();
					Double surchargeAmount = billingRate.getSurchargeAmount();
					if (fuelSurchargePerTon == null && surchargeAmount == null)
						fuelSurcharge = 0.0;
					else if (fuelSurchargePerTon != null) {
						fuelSurcharge = billing.getEffectiveTonsWt()
								* fuelSurchargePerTon;
					} else {
						fuelSurcharge = surchargeAmount;
					}
				}
				
				if ("A".equalsIgnoreCase(fuelSurchargeType)) {
					FuelSurchargePadd currentPadd = billingRate.getPadd();
					if (currentPadd != null) {
						Integer paddUsing = billingRate.getPaddUsing();
						Double padd = 0.0;
						StringBuffer paddBuffer = new StringBuffer(
								"select obj from FuelSurchargePadd obj where obj.code='"
										+ currentPadd.getCode() + "'");
						Date effectiveDate = null;
						if (paddUsing != null) {
							if (paddUsing == 1) {
								effectiveDate = ticket.getLoadDate();
							} else if (paddUsing == 2) {
								effectiveDate = ticket.getUnloadDate();
							} else if (paddUsing == 3) {
								effectiveDate = ticket.getBillBatch();
							}
						}
						if (effectiveDate != null) {
							paddBuffer.append(" and obj.validTo>='"
									+ mysqldf.format(effectiveDate)
									+ "' and obj.validFrom<='"
									+ mysqldf.format(effectiveDate) + "'");
						}
						List<FuelSurchargePadd> padds = genericDAO
								.executeSimpleQuery(paddBuffer.toString());
						if (padds != null && padds.size() > 0) {
							padd = padds.get(0).getAmount();
						}
						if ((padd != -1) && (billingRate.getPeg() != null)
								&& (billingRate.getMiles() != null)) {
							int sign = 1;
							if ((padd - billingRate.getPeg()) < 0) {
								sign = -1;
							}
							
							//System.out.println("***** The tet value is "+MathUtil.roundUp(padd - billingRate.getPeg(),3));
							double term = Math.floor(sign
									* (MathUtil.roundUp(padd - billingRate.getPeg(),3))/ 0.05);
							fuelSurcharge = sign * term
									* billingRate.getMiles() * 0.01;
						}
						if ((padd != -1) && (billingRate.getPeg() != null)
								&& (billingRate.getMiles() == null)) {
							double percentage = Math.floor((MathUtil.roundUp(padd - billingRate
									.getPeg(),3)) / 0.08);
							if (rateType == 2) {
								fuelSurcharge = percentage
										* derivedRate * 0.01; // Peak rate 2nd Feb 2021
										//* billingRate.getValue() * 0.01;
							} else if (rateType == 3) {
								// per tonne
								fuelSurcharge = billing.getEffectiveTonsWt()
										* derivedRate * percentage // Peak rate 2nd Feb 2021
										//* billingRate.getValue() * percentage
										* 0.01;
							}
						}
					}
					// Weekly fuel surcharge calculation
					int wfsr = billingRate.getFuelsurchargeweeklyRate();
					if (wfsr == 1) {
						StringBuffer weeklyRateQuery = new StringBuffer(
								"select obj from FuelSurchargeWeeklyRate obj where obj.transferStations="
										+ ticket.getOrigin().getId()
										+ " and obj.landfillStations="
										+ ticket.getDestination().getId() + " ");
						FuelSurchargeWeeklyRate fuelsurchargeweeklyrate = null;
						Date effectiveDatePadd = null;
						if (billingRate.getWeeklyRateUsing() != null) {
							if (billingRate.getWeeklyRateUsing() == 1) {
								effectiveDatePadd = ticket.getLoadDate();
							}
							if (billingRate.getWeeklyRateUsing() == 2) {
								effectiveDatePadd = ticket.getUnloadDate();
							}
							if (billingRate.getWeeklyRateUsing() == 3) {
								effectiveDatePadd = ticket.getBillBatch();
							}
							weeklyRateQuery.append(" and obj.fromDate <= '"
									+ mysqldf.format(effectiveDatePadd)
									+ "' and obj.toDate >= '"
									+ mysqldf.format(effectiveDatePadd) + "'");
						}
						List<FuelSurchargeWeeklyRate> listfswr = genericDAO
								.executeSimpleQuery(weeklyRateQuery.toString());
						if (listfswr != null && listfswr.size() > 0) {
							fuelsurchargeweeklyrate = listfswr.get(0);
						}
						if (fuelsurchargeweeklyrate != null) {
							int fswrateType = fuelsurchargeweeklyrate
									.getRateType();
							if (fswrateType == 3) {
								fuelSurcharge = billing.getEffectiveTonsWt()
										* fuelsurchargeweeklyrate
												.getFuelSurchargeRate();
							}
							if (fswrateType == 2) {
								fuelSurcharge = fuelsurchargeweeklyrate
										.getFuelSurchargeRate();
							}

							if (fswrateType == 5) {
								fuelSurcharge = fuelsurchargeweeklyrate
										.getFuelSurchargeRate()
										* billing.getAmount();
							}

							if (fswrateType == 1) {
							// Change to 8.35 - 28th Dec 2016
								fuelSurcharge = (billing.getEffectiveNetWt() / 8.35)
										* fuelsurchargeweeklyrate
												.getFuelSurchargeRate();
							}
						}
					}
				}// end of Weekly fuel surcharge calculation
				sumFuelSurcharge += fuelSurcharge;
				fuelSurcharge = MathUtil.roundUp(fuelSurcharge, 2);
				billing.setFuelSurcharge(fuelSurcharge);

				// if (billingRate.getTonnagePremium() != null) {
				if (billingRate.getTonnagePremium().getCode() != null) {
					Double premiumTonne = billingRate.getTonnagePremium()
							.getPremiumTonne();
					Double rate = billingRate.getTonnagePremium().getRate();
					if (billing.getEffectiveTonsWt() > premiumTonne) {
						billing.setTonnagePremium((billing.getEffectiveTonsWt() - premiumTonne)
								* rate);
					}
				} else {
					billing.setTonnagePremium(0.0);
				}
				sumTonnage += billing.getTonnagePremium();
				billing.setTonnagePremium(MathUtil.roundUp(
						billing.getTonnagePremium(), 2));
				
				if(billingRate.getDemmurageCharge()!=null){
				if (billingRate.getDemmurageCharge().getDemurragecode() != null) {
					Integer chargeAfterTime = billingRate.getDemmurageCharge().getChargeAfter();							
					Integer incrementTime = billingRate.getDemmurageCharge().getTimeIncrements();
					Double rate = billingRate.getDemmurageCharge().getDemurragerate();
					if(billingRate.getDemmurageCharge().getTimesUsed().equals("1")){
						String[] landfillInhourMin = ticket.getLandfillTimeIn().split(":");
					    int landfillInhour = Integer.parseInt(landfillInhourMin[0]);
					    int landfillInmins = Integer.parseInt(landfillInhourMin[1]);
					    int landfillInhoursInMins = landfillInhour * 60;
					    int landfilltimein = landfillInhoursInMins+landfillInmins;
					    
					    String[] landfillOuthourMin = ticket.getLandfillTimeOut().split(":");
					    int landfillOuthour = Integer.parseInt(landfillOuthourMin[0]);
					    int landfillOutmins = Integer.parseInt(landfillOuthourMin[1]);
					    int landfillOuthoursInMins = landfillOuthour * 60;
					    int landfilltimeout = landfillOuthoursInMins+landfillOutmins;
					    
					    int diffTime = landfilltimeout - landfilltimein;
					    if(diffTime < 0){
					    	diffTime = 1440+diffTime;
					    }
					    
					    double minCharge = 0.0;
					    if(diffTime > chargeAfterTime){
					    	minCharge = rate;
					    	
					    	int timeRemaining = diffTime - (chargeAfterTime+incrementTime);
					    	double numberOfIteration = (double)timeRemaining /(double) incrementTime;	
					    	
					    	for(int i=0;i<numberOfIteration;i++){					    		
					    		//if(timeRemaining > incrementTime){
					    			minCharge = minCharge + rate;
					    			timeRemaining = timeRemaining - incrementTime;
					    		//}
					    	}					    	
					    }	
					    billing.setDemurrageCharge(minCharge);
					}
					else if(billingRate.getDemmurageCharge().getTimesUsed().equals("2")){
						String[] landfillInhourMin = ticket.getTransferTimeIn().split(":");
					    int landfillInhour = Integer.parseInt(landfillInhourMin[0]);
					    int landfillInmins = Integer.parseInt(landfillInhourMin[1]);
					    int landfillInhoursInMins = landfillInhour * 60;
					    int landfilltimein = landfillInhoursInMins+landfillInmins;
					    
					    String[] landfillOuthourMin = ticket.getTransferTimeOut().split(":");
					    int landfillOuthour = Integer.parseInt(landfillOuthourMin[0]);
					    int landfillOutmins = Integer.parseInt(landfillOuthourMin[1]);
					    int landfillOuthoursInMins = landfillOuthour * 60;
					    int landfilltimeout = landfillOuthoursInMins+landfillOutmins;
					   
					    int diffTime = landfilltimeout - landfilltimein;
					    if(diffTime < 0){
					    	diffTime = 1440+diffTime;
					    }
					    
					    double minCharge = 0.0;
					    if(diffTime > chargeAfterTime){
					    	minCharge = rate;
					    	
					    	int timeRemaining = diffTime - (chargeAfterTime+incrementTime);
					    	double numberOfIteration = (double)timeRemaining /(double) incrementTime;	
					    	
					    	for(int i=0;i<numberOfIteration;i++){					    		
					    		//if(timeRemaining > incrementTime){
					    			minCharge = minCharge + rate;
					    			timeRemaining = timeRemaining - incrementTime;
					    		//}
					    	}
					    	
					    }
					    billing.setDemurrageCharge(minCharge);
					}
					
				} 
				else {
					billing.setDemurrageCharge(0.0);
				}				
			}
			else{
				billing.setDemurrageCharge(0.0);
			}
				sumDemmurage += billing.getDemurrageCharge();
				billing.setDemurrageCharge(MathUtil.roundUp(billing.getDemurrageCharge(), 2));

			} else {
				billing.setRate(0.0);
				billing.setFuelSurcharge(0.0);
			}
			double amount = billing.getAmount() + billing.getFuelSurcharge()
					+ billing.getDemurrageCharge()
					+ billing.getTonnagePremium();
			
			billing.setTotAmt(amount);
			summarys.add(billing);
		}
		billingMap.clear();
		billingMap = null;
		sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);
		sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
		sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);
		sumNet = MathUtil.roundUp(sumNet, 2);
		sumAmount = MathUtil.roundUp(sumAmount, 2);
		sumFuelSurcharge = MathUtil.roundUp(sumFuelSurcharge, 2);
		sumDemmurage = MathUtil.roundUp(sumDemmurage, 2);
		sumTonnage = MathUtil.roundUp(sumTonnage, 2);
		sumGallon = MathUtil.roundUp(sumGallon, 2);

		sumTotal = sumAmount + sumFuelSurcharge + sumDemmurage + sumTonnage;
		wrapper.setSumBillableTon(sumBillableTon);
		wrapper.setSumOriginTon(sumOriginTon);
		wrapper.setSumDestinationTon(sumDestinationTon);
		wrapper.setSumTonnage(sumTonnage);
		wrapper.setSumDemmurage(sumDemmurage);
		wrapper.setSumNet(sumNet);
		wrapper.setSumAmount(sumAmount);
		wrapper.setSumFuelSurcharge(sumFuelSurcharge);
		wrapper.setSumTotal(sumTotal);
		wrapper.setSumGallon(sumGallon);
		wrapper.setTotalRowCount(tickets.size());
		return wrapper;
	}
	
	private boolean isWMTicket(Ticket ticket, BillingRate billingRate) {
		if (billingRate != null) {
			Customer customer = billingRate.getCustomername();
			if (customer != null) {
				if (customer.getId() == 55l // Waste Management The Forge
						|| customer.getId() == 24l // WM Philadelphia Transfer Station
						|| customer.getId() == 26l) { // Hunterdon Transfer Station
					return true;
				}
			}
		}
		
		String countQuery = "select count(obj) from WMInvoice obj where 1=1";
		countQuery += " and obj.origin=" + ticket.getOrigin().getId();
		countQuery += " and obj.destination=" + ticket.getDestination().getId();
		Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		
		return (recordCount > 0) ? true : false;
	}
	
	// Peak rate 2nd Feb 2021
	private Object[] deriveBillingRate(Ticket ticket, BillingRate billingRate) {
		Double regularRate = billingRate.getValue();
		Double peakRate = billingRate.getPeakRate();
		
		Object[] retObjArr = new Object[2];
		retObjArr[0] = false;
		retObjArr[1] = regularRate;
		
		String landfillTimeIn = ticket.getLandfillTimeIn();
		String landfillTimeOut = ticket.getLandfillTimeOut();
		String peakRateValidFrom = billingRate.getPeakRateValidFrom();
		String peakRateValidTo = billingRate.getPeakRateValidTo();
		if (peakRate == null || peakRate == 0.0
				|| StringUtils.isEmpty(peakRateValidFrom)
				|| StringUtils.isEmpty(peakRateValidTo)
				|| StringUtils.isEmpty(landfillTimeIn)
				|| StringUtils.isEmpty(landfillTimeOut)) {
			return retObjArr;
		}
		
		DateTime start = toDateTime(peakRateValidFrom, null);
      DateTime end = toDateTime(peakRateValidTo, start);
      DateTime landfillTimeInInstant = toDateTime(landfillTimeIn, null);
      DateTime landfillTimeOutInstant = toDateTime(landfillTimeOut, null);
      if (start == null || end == null
      		|| landfillTimeInInstant == null || landfillTimeOutInstant == null) {
      	return retObjArr;
      }
      
      Interval interval = new Interval(start, end);
      if (interval.contains(landfillTimeInInstant) || interval.contains(landfillTimeOutInstant)) {
      	retObjArr[0] = true;
   		retObjArr[1] = peakRate;
      } else {
      	landfillTimeInInstant = landfillTimeInInstant.plusDays(1);
      	landfillTimeOutInstant = landfillTimeOutInstant.plusDays(1);
      	if (interval.contains(landfillTimeInInstant) || interval.contains(landfillTimeOutInstant)) {
         	retObjArr[0] = true;
      		retObjArr[1] = peakRate;
         }
      }
      return retObjArr;
	}
	
	// Peak rate 2nd Feb 2021
	private DateTime toDateTime(String timeStr, DateTime start) {
		String[] timeSplit = timeStr.split(":");
		if (timeSplit.length <= 1) {
			return null;
		}
		
		DateTime dt = null;
		try {
		   int hour = Integer.parseInt(timeSplit[0]);
		   int mins = Integer.parseInt(timeSplit[1]);
		   dt = new DateTime(2021, 1, 1, hour, mins);
		   
		   if (start != null && dt.isBefore(start)) {
		   	dt = dt.plusDays(1);
		   }
		} catch (Throwable t) {
			dt = null;
		}
	   
	   return dt;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveBillingData(HttpServletRequest request,
			SearchCriteria criteria) throws Exception {
		Location origin = null;
		Location destination = null;
		Long destinationInput=0L;
		Long originInput=0L;
		// List<Billing> data = generateBillingData(criteria);
		
		String invoiceNumber = (String) criteria.getSearchMap().get(
				"invoiceNumber");
		
		if(!StringUtils.isEmpty((String) criteria.getSearchMap().get("destination"))){
		destinationInput = Long.parseLong((String) criteria.getSearchMap()
				.get("destination"));
	}else{
		request.getSession().setAttribute("error",
		"Please Select Both Origin and Destination");
throw new Exception("origin and destindation is empty");
		}
		
		if(!StringUtils.isEmpty((String) criteria.getSearchMap().get("origin"))){
		originInput = Long.parseLong((String) criteria.getSearchMap().get(
				"origin"));
		}
		else{
			request.getSession().setAttribute("error",
			"Please Select Both Origin and Destination");
	throw new Exception("origin and destindation is empty");
		}
		if (invoiceNumber.isEmpty()) {
			request.getSession().setAttribute("error",
					"Please Enter Invoice Number");
			throw new Exception("invoice number null");
		}
		/*
		 * String query =
		 * "select obj from Invoice obj where obj.transferStation="
		 * +originInput+" and obj.landfill="
		 * +destinationInput+" and obj.invoiceNumber="+invoiceNumber;
		 * List<Invoice> list=genericDAO.executeSimpleQuery(query);
		 */
		
		BillingWrapper wrapper = generateBillingData(criteria);
		List<Billing> billList=wrapper.getBilling();
		String startdate="";
		String enddate="";
		Date startDate=null;
		Date endDate=null;
		if (billList.size() > 0) {			
			    Collections.sort(billList, new Comparator<Billing>() {
			        @Override
			        public int compare(final Billing object1, final Billing object2) {
			            return object1.getLoaded().compareTo(object2.getLoaded());
			        }					
			       } );
			    
			    int lstindex=billList.size()-1;
				startdate=billList.get(0).getLoaded();
				enddate=billList.get(lstindex).getLoaded();
				
				startDate=sdf.parse(startdate);
				endDate=sdf.parse(enddate);	
				
				startdate=mysqldf.format(startDate);
				enddate=mysqldf.format(endDate);
				
				
		}		
		String query = "select obj from BillingRate obj where obj.transferStation="
				+ originInput + " and obj.landfill=" + destinationInput+ " and obj.validFrom<='"+startdate+"' and obj.validTo >='"+enddate+"'";
		
		Location company =null;
		List<BillingRate> billingRates = genericDAO.executeSimpleQuery(query);
		if(!billingRates.isEmpty() && billingRates.size()>0){
			company = billingRates.get(0).getCompanyLocation();
		}
		else
		{
			String query1 = "select obj from BillingRate obj where obj.transferStation="
				+ originInput + " and obj.landfill=" + destinationInput+ "order by obj.validFrom desc";
			List<BillingRate> billingRates1 = genericDAO.executeSimpleQuery(query1);
			company = billingRates1.get(0).getCompanyLocation();
		}	
		
		
         
		String queryinvoice = "select obj from Billing_New obj where obj.company='"
				+company.getName()
				+ "' and obj.invoiceNo='"
				+invoiceNumber+"'";			
		 
		List<Invoice> invoices = genericDAO.executeSimpleQuery(queryinvoice);	

		/*
		 * Invoice invoice = genericDAO.getByUniqueAttribute(Invoice.class,
		 * "invoiceNumber", invoiceNumber);
		 */
		if (!StringUtils
				.isEmpty((String) criteria.getSearchMap().get("origin"))) {
			origin = genericDAO.getById(
					Location.class,
					Long.parseLong((String) criteria.getSearchMap().get(
							"origin")));
		} else {
			throw new Exception("Invalid transfer location");
		}
		if (!StringUtils.isEmpty((String) criteria.getSearchMap().get(
				"destination"))) {
			destination = genericDAO.getById(
					Location.class,
					Long.parseLong((String) criteria.getSearchMap().get(
							"destination")));
		} else {
			throw new Exception("Invalid landfill location");
		}
		if (invoices.isEmpty()) {
			Invoice invoice = new Invoice();
			String invoiceDate = (String) criteria.getSearchMap().get(
					"invoiceDate");
			if (!invoiceDate.isEmpty()) {
				Date invDate = new SimpleDateFormat("MM-dd-yyyy")
						.parse(invoiceDate);
				invoice.setInvoiceDate(invDate);
			} else {
				invoice.setInvoiceDate(Calendar.getInstance().getTime());
			}
			User user = (User) request.getSession().getAttribute("userInfo");
			invoice.setInvoiceNumber(invoiceNumber);
			invoice.setTransferStation(origin);
			invoice.setLandfill(destination);
			invoice.setSumBillableTon(wrapper.getSumBillableTon());
			invoice.setSumOriginTon(wrapper.getSumOriginTon());
			invoice.setSumDestinationTon(wrapper.getSumDestinationTon());
			invoice.setSumNet(wrapper.getSumNet());
			invoice.setSumFuelSurcharge(wrapper.getSumFuelSurcharge());
			invoice.setSumTonnage(wrapper.getSumTonnage());
			invoice.setSumDemmurage(wrapper.getSumDemmurage());
			invoice.setSumTotal(wrapper.getSumTotal());
			invoice.setSumAmount(wrapper.getSumAmount());
			invoice.setCreatedBy(user.getId());
			invoice.setSumGallon(wrapper.getSumGallon());
			genericDAO.saveOrUpdate(invoice);

			for (Billing billing : wrapper.getBilling()) {
				Ticket ticket = genericDAO.getById(Ticket.class, billing
						.getTicket().getId());
				ticket.setCustomer((billing.getCustomerId() != null) ? billing
						.getCustomerId() : null);
				ticket.setCompanyLocation((billing.getCompanyId() != null) ? billing
						.getCompanyId() : null);
				ticket.setTicketStatus(2);
				ticket.setInvoiceNumber(invoiceNumber);
				ticket.setInvoiceDate(invoice.getInvoiceDate());
				genericDAO.saveOrUpdate(ticket);
				billing.setCompany(ticket.getCompanyLocation().getName());
				billing.setCreatedBy(user.getId());
				billing.setInvoiceNo(invoiceNumber);
				billing.setInvoice_Date(invoiceDate);
				boolean duplicateTicket = ckeckTicketDuplicate(billing
						.getTicket().getId());

				if (duplicateTicket)
					genericDAO.saveOrUpdate(billing);

			}
		} else {
			request.getSession().setAttribute("error",
					"Invoice Number already exists. Please choose another number");
			throw new Exception(

			"Invoice Number already exists. Please choose another number");
		}
	}

	public boolean ckeckTicketDuplicate(Long ticketId) {

		String query = "select obj from Billing obj where ticket.id="
				+ ticketId;
		List<Billing> billing = genericDAO.executeSimpleQuery(query);
		if (billing.isEmpty()) {
			return true;
		}
		return false;

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void saveSubcontractorBillingData(HttpServletRequest request,
			SearchCriteria criteria) throws Exception {
		Location origin = null;
		Location destination = null;
		Long destinationInput = null;
		Long originInput = null;
		String destinationStr = (String) criteria.getSearchMap().get(
				"destination");
		String originStr = (String) criteria.getSearchMap().get("origin");
		String voucherNo = (String) criteria.getSearchMap()
				.get("invoiceNumber");
		String company = (String) criteria.getSearchMap().get("company");
		if (!StringUtils.isEmpty(destinationStr))
			destinationInput = Long.parseLong(destinationStr);
		if (!StringUtils.isEmpty(originStr))
			originInput = Long.parseLong(originStr);
		String subcontractorId = (String) criteria.getSearchMap().get(
				"subcontractorName");
		Long subcontractorid = Long.valueOf(Long.parseLong(subcontractorId));
		SubContractor subContractor = (SubContractor) genericDAO.getById(
				SubContractor.class, (subcontractorid));
		if (voucherNo.isEmpty()) {
			request.getSession().setAttribute("error",
					"Please Enter voucher  Number");
			throw new Exception("Voucher number null");
		}
		if (!StringUtils.isEmpty(originStr)) {
			origin = genericDAO.getById(Location.class, originInput);
		}
		if (!StringUtils.isEmpty(destinationStr)) {
			destination = genericDAO.getById(Location.class, destinationInput);
		}

		String query = "select obj from SubcontractorInvoice obj where obj.subContractorId.id="
				+ subcontractorId
				+ " and obj.voucherNumber='"
				+ voucherNo
				+ "' and  obj.companyLocationId.id=" + company;
		List<SubcontractorInvoice> subcontractorInvoices = genericDAO
				.executeSimpleQuery(query);
		if (!subcontractorInvoices.isEmpty()) {
			request.getSession()
					.setAttribute("error",
							"Voucher number already exists. Please choose another number");
			throw new ExecutionException("Voucher number already exists");
		}

		SubcontractorBillingWrapper wrapper = generateSubcontractorBillingData(criteria);
		if (subcontractorInvoices.isEmpty()) {
			SubcontractorInvoice invoice = new SubcontractorInvoice();

			String invoiceDate = (String) criteria.getSearchMap().get(
					"invoiceDate");
			if (!invoiceDate.isEmpty()) {
				Date voucherDate = new SimpleDateFormat("MM-dd-yyyy")
						.parse(invoiceDate);
				invoice.setVoucherDate(voucherDate);
			} else {
				invoice.setVoucherDate(Calendar.getInstance().getTime());
			}

			User user = (User) request.getSession().getAttribute("userInfo");
			invoice.setCreatedBy(user.getId());
			invoice.setVoucherNumber(voucherNo);
			invoice.setTransferStation(origin);
			invoice.setLandfill(destination);
			invoice.setSubContractorId(subContractor);
			invoice.setSumBillableTon(wrapper.getSumBillableTon());
			invoice.setSumOriginTon(wrapper.getSumOriginTon());
			invoice.setSumDestinationTon(wrapper.getSumDestinationTon());
			invoice.setSumAmount(wrapper.getSumAmount());
			invoice.setSumFuelSurcharge(wrapper.getSumFuelSurcharge());
			invoice.setSumOtherCharges(wrapper.getSumOtherCharges());
			invoice.setSumTotal(wrapper.getSumTotal());
			invoice.setGrandTotal(wrapper.getGrandTotal());
			invoice.setMiscelleneousNote(wrapper.getMiscelleneousNote());
			invoice.setMiscelleneousCharges(wrapper.getMiscelleneousCharges());
			invoice.setCompanyLocationId(wrapper.getCompanyLocationId());
			// invoice.setCompanyname(wrapper.getCompany());
			invoice.setSubcontractorname(wrapper.getSubcontractorname());
			invoice.setAddress1(wrapper.getAddress1());
			invoice.setAddress2(wrapper.getAddress2());
			invoice.setCity(wrapper.getCity());
			invoice.setZipcode(wrapper.getZipcode());
			invoice.setPhone(wrapper.getPhone());
			invoice.setFax(wrapper.getFax());
			invoice.setState(wrapper.getState());
			genericDAO.saveOrUpdate(invoice);

			for (SubcontractorBilling billing : wrapper
					.getSubcontractorBillings()) {
				Ticket ticket = genericDAO.getById(Ticket.class, billing
						.getTicket().getId());
				ticket.setVoucherStatus(2);
				ticket.setVoucherNumber(invoice.getVoucherNumber());
				ticket.setVoucherDate(invoice.getVoucherDate());

				genericDAO.saveOrUpdate(ticket);
				billing.setCreatedBy(user.getId());
				billing.setInvoiceNo(voucherNo);
				billing.setSubVoucherDate(invoice.getVoucherDate());
				billing.setMiscelleneousCharges(wrapper.getMiscelleneousCharges());

				genericDAO.saveOrUpdate(billing);

			}
		} else {
			request.getSession()
					.setAttribute("error",
							"Voucher number already exists. Please choose another number");
			throw new ExecutionException("Voucher number already exists");
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteBillingData(Invoice invoice) throws Exception {
		try {
			Map params = new HashMap();
			/*
			 * params.put("invoiceNo", invoice.getInvoiceNumber());
			 * params.put("origin", invoice.getTransferStation().getName());
			 * params.put("destination",invoice.getLandfill().getName());
			 */
			/*
			 * List<Billing> datas = genericDAO.findByCriteria(Billing.class,
			 * params);
			 */
			List<Billing> datas = null;
			if (invoice.getLandfill().getName()
					.equalsIgnoreCase("Grows/Tullytown")) {
				Location grows = null;
				Location tullyTown = null;

				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				grows = genericDAO.getByCriteria(Location.class, criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				tullyTown = genericDAO.getByCriteria(Location.class, criterias);
				String query = " select obj from Billing obj where obj.invoiceNo='"
						+ invoice.getInvoiceNumber()
						+ "' and obj.origin='"
						+ invoice.getTransferStation().getName()
						+ "' and obj.destination in ('"
						+ grows.getName()
						+ "','" + tullyTown.getName() + "')";
				datas = genericDAO.executeSimpleQuery(query);
			}

			else {
				params.put("invoiceNo", invoice.getInvoiceNumber());
				params.put("origin", invoice.getTransferStation().getName());
				params.put("destination", invoice.getLandfill().getName());
				datas = genericDAO.findByCriteria(Billing.class, params);
			}
			if (datas != null && datas.size() > 0) {
				for (Billing billing : datas) {
					Ticket ticket = genericDAO.getById(Ticket.class, billing
							.getTicket().getId());
					ticket.setTicketStatus(1);
					ticket.setInvoiceNumber(null);
					ticket.setInvoiceDate(null);
					genericDAO.saveOrUpdate(ticket);
					genericDAO.delete(billing);
				}
			}
			genericDAO.deleteById(Invoice.class, invoice.getId());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteSubcontractorBillingData(SubcontractorInvoice invoice)
			throws Exception {
		try {
			Map params = new HashMap();
			params.put("invoiceNo", invoice.getVoucherNumber());
			params.put("subcontractorId", invoice.getSubContractorId());
			params.put("companyLocationId", invoice.getCompanyLocationId());
			List<SubcontractorBilling> datas = genericDAO.findByCriteria(
					SubcontractorBilling.class, params);
			if (datas != null && datas.size() > 0) {
				for (SubcontractorBilling billing : datas) {
					Ticket ticket = genericDAO.getById(Ticket.class, billing
							.getTicket().getId());
					ticket.setVoucherStatus(1);
					ticket.setVoucherNumber(null);
					ticket.setVoucherDate(null);
					genericDAO.saveOrUpdate(ticket);
					genericDAO.delete(billing);
				}
				genericDAO.deleteById(SubcontractorInvoice.class, invoice.getId());
			}
			
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public boolean checkDuplicate(Ticket ticket, String ticketType) {
		StringBuffer query = new StringBuffer("");
		List<Ticket> tickets = null;
		Long ticketId = -1l;
		if (ticket.getId() != null)
			ticketId = ticket.getId();
		try {
			if ("O".equalsIgnoreCase(ticketType)) {
				/*String origin = "select obj from Ticket obj where obj.origin.id="
						+ ticket.getOrigin().getId()
						+ " and obj.destination.id="
						+ ticket.getDestination().getId()
						+ " and obj.originTicket="
						+ ticket.getOriginTicket()
						+ " and obj.id != " + ticketId;*/
				
				String origin = "select obj from Ticket obj where obj.origin.id="
					+ ticket.getOrigin().getId()
					+ " and obj.originTicket="
					+ ticket.getOriginTicket()
					+ " and obj.id != " + ticketId;
				tickets = genericDAO.executeSimpleQuery(origin);
				if (tickets != null && tickets.size() > 0)
					return true;
			}
			if ("D".equalsIgnoreCase(ticketType)) {
				/*String destination = "select obj from Ticket obj where obj.origin.id="
						+ ticket.getOrigin().getId()
						+ " and obj.destination.id="
						+ ticket.getDestination().getId()
						+ " and obj.destinationTicket="
						+ ticket.getDestinationTicket()
						+ " and obj.id != "
						+ ticketId;*/
				String destination = "select obj from Ticket obj where 1=1"
					+ " and obj.destination.id="
					+ ticket.getDestination().getId()
					+ " and obj.destinationTicket="
					+ ticket.getDestinationTicket()
					+ " and obj.id != "
					+ ticketId;
				
				tickets = genericDAO.executeSimpleQuery(destination);
				if (tickets != null && tickets.size() > 0)
					return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public SubcontractorBillingWrapper generateSubcontractorBillingData(
			SearchCriteria searchCriteria) {
		List<String> str = new ArrayList<String>();
		int errorCount = 0;
		String misceCharges = (String) searchCriteria.getSearchMap().get(
				"miscelleneousCharges");
		String misceNote = (String) searchCriteria.getSearchMap().get(
				"miscelleneousNote");
		String subcontractorName = (String) searchCriteria.getSearchMap().get(
				"subcontractorName");
		String fromDateStr = (String) searchCriteria.getSearchMap().get(
				"fromDate");
		// String companyName=null;
		Long companyId = null;
		Location companylocation = null;
		String toDateStr = (String) searchCriteria.getSearchMap().get("toDate");

		String fromloadDate = (String) searchCriteria.getSearchMap().get(
				"fromloadDate");
		String toloadDate = (String) searchCriteria.getSearchMap().get(
				"toloadDate");
		String fromunloadDate = (String) searchCriteria.getSearchMap().get(
				"fromunloadDate");
		String tounloadDate = (String) searchCriteria.getSearchMap().get(
				"tounloadDate");
		String company = (String) searchCriteria.getSearchMap().get("company");
		Long subcontractorId = Long.valueOf(Long.parseLong(subcontractorName));
		if (!StringUtils.isEmpty(company)) {
			companyId = Long.valueOf(Long.parseLong(company));
			companylocation = genericDAO.getById(Location.class, companyId);
		}
		SubContractor subContractor = (SubContractor) genericDAO.getById(
				SubContractor.class, subcontractorId);

		fromDateStr = ReportDateUtil.getFromDate(fromDateStr);
		toDateStr = ReportDateUtil.getToDate(toDateStr);

		fromloadDate = ReportDateUtil.getFromDate(fromloadDate);
		toloadDate = ReportDateUtil.getToDate(toloadDate);

		fromunloadDate = ReportDateUtil.getFromDate(fromunloadDate);
		tounloadDate = ReportDateUtil.getToDate(tounloadDate);

		String origin = (String) searchCriteria.getSearchMap().get("origin");
		String destination = (String) searchCriteria.getSearchMap().get(
				"destination");
		System.out.println(destination);
		StringBuffer query = new StringBuffer("");
		/*
		 * query.append(
		 * "select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 and obj.billBatch between '"
		 * + fromDateStr + "'and '" + toDateStr + "'");
		 */
		query.append("select obj from Ticket obj where obj.status=1 and voucherStatus=1");

		if (!StringUtils.isEmpty(company) && !StringUtils.isEmpty(subcontractorName)) {
			query.append("and  obj.subcontractor=").append(subcontractorName);			
		}		
		else if (!StringUtils.isEmpty(subcontractorName) && StringUtils.isEmpty(company)) {
			query.append("and  obj.subcontractor=").append(subcontractorName);
		}
		else if (StringUtils.isEmpty(subcontractorName) && !StringUtils.isEmpty(company)){
			String subQuery ="select obj from SubcontractorRate obj where obj.companyLocation="+company+" group by obj.subcontractor";
		
			List<SubcontractorRate> subRateData = genericDAO.executeSimpleQuery(subQuery);
			String subInfo = "";
			for(SubcontractorRate subObj:subRateData){
				if(subInfo.equals("")){
					subInfo = subObj.getSubcontractor().getId().toString();
				}
				else{
					subInfo = subInfo+","+subObj.getSubcontractor().getId();
				}				
			}
			
			query.append("and  obj.subcontractor in (").append(subInfo).append(")");
		}
		

		if (!StringUtils.isEmpty(fromDateStr)) {
			query.append("and  obj.billBatch >='" + fromDateStr + "'");
		}

		if (!StringUtils.isEmpty(toDateStr)) {
			query.append("and  obj.billBatch <='" + toDateStr + "'");
		}

		if (!StringUtils.isEmpty(fromloadDate)) {
			query.append("and  obj.loadDate >='" + fromloadDate + "'");
		}

		if (!StringUtils.isEmpty(toloadDate)) {
			query.append("and  obj.loadDate <='" + toloadDate + "'");
		}

		if (!StringUtils.isEmpty(fromunloadDate)) {
			query.append("and  obj.unloadDate >= '" + fromunloadDate + "'");
		}
		if (!StringUtils.isEmpty(tounloadDate)) {
			query.append("and  obj.unloadDate <= '" + tounloadDate + "'");
		}

		if (StringUtils.isEmpty(subcontractorName)) {
			return null;
		}

		if (!StringUtils.isEmpty(origin)) {
			query.append("and  obj.origin=").append(origin);
		}
		if (!StringUtils.isEmpty(destination)) {
			query.append("and  obj.destination=").append(destination);
		}
		if (!StringUtils.isEmpty(origin) && !StringUtils.isEmpty(destination)) {
			StringBuffer rateQuery = new StringBuffer("");
			rateQuery
					.append("select obj from SubcontractorRate obj where obj.transferStation="
							+ origin + " and obj.landfill=" + destination);
			if (!StringUtils.isEmpty(subcontractorName)) {
				rateQuery.append("and obj.subcontractor=").append(
						subcontractorName);
			}
			rateQuery.append("order by obj.validFrom desc");
			List<SubcontractorRate> fs = genericDAO
					.executeSimpleQuery(rateQuery.toString());
			SubcontractorRate billingRate = null;
			if (fs != null && fs.size() > 0) {
				billingRate = fs.get(0);
			}
			if (billingRate != null) {
				int sortBy = (billingRate.getSortBy() == null) ? 1
						: billingRate.getSortBy();
				if (sortBy == 1) {
					query.append(" order by obj.originTicket");
				} else {
					query.append(" order by obj.destinationTicket");
				}
			}
		} else {
			query.append(" order by obj.origin.name asc,obj.destination.name asc,obj.originTicket asc");
		}
		System.out.println("\n query-->" + query.toString() + "\n");
		List<Ticket> tickets = genericDAO.executeSimpleQuery(query.toString());
		List<SubcontractorBilling> summarys = new ArrayList<SubcontractorBilling>();
		SubcontractorBillingWrapper wrapper = new SubcontractorBillingWrapper();
		wrapper.setSubcontractorBillings(summarys);
		double sumNet = 0.0;
		double sumBillableTon = 0.0;
		double sumOriginTon = 0.0;
		double sumDestinationTon = 0.0;
		double sumAmount = 0.0;
		double sumFuelSurcharge = 0.0;
		double sumTotal = 0.0;
		double sumOtherCharges = 0.0;
		double grandTotal = 0.0;
		double amount = 0.0;
		boolean expiredRate = false;
		for (Ticket ticket : tickets) {
			try {
                
				StringBuffer subbillrateQuery = new StringBuffer("select obj from SubcontractorRate obj where obj.transferStation='"
					+ ticket.getOrigin().getId()
					+ "' and obj.landfill='"
					+ ticket.getDestination().getId() 
					+"' and obj.subcontractor="
					+ticket.getSubcontractor().getId());
							
				
				List<SubcontractorRate> subres = null;

				subres = genericDAO.executeSimpleQuery(subbillrateQuery.toString());
				
				if(subres != null && subres.size() > 0){				
					
					Integer rateUsing = subres.get(0).getRateUsing();
					
					StringBuffer subbillrateQuery2 = new StringBuffer("select obj from SubcontractorRate obj where obj.transferStation='"
							+ ticket.getOrigin().getId()
							+ "' and obj.landfill='"
							+ ticket.getDestination().getId() 
							+"' and obj.subcontractor="
							+ticket.getSubcontractor().getId());
					    if(subres.get(0).getRateUsing()==2){
					    	subbillrateQuery2.append(" and obj.validFrom<='"
							+ticket.getUnloadDate()
							+"' and obj.validTo>='"
							+ticket.getUnloadDate()+"'");
					    }
					    else{
					    	subbillrateQuery2.append(" and obj.validFrom<='"
									+ticket.getLoadDate()
									+"' and obj.validTo>='"
									+ticket.getLoadDate()+"'");
					    }
					    
					    
						
						
						
						
						List<SubcontractorRate> subres2 = null;

						subres2 = genericDAO.executeSimpleQuery(subbillrateQuery2.toString());
					
						if(subres2 != null && subres2.size() > 0){
						
							boolean processTicket = false;
							SubcontractorRate rateObj = null;
							for(SubcontractorRate subRateObj:subres2){
								if(!StringUtils.isEmpty(company)){
									if(company.equals(subRateObj.getCompanyLocation().getId().toString())){
										processTicket = true;
										rateObj = subRateObj;
										break;
									}
								}
							}
							
			if(processTicket){
				companylocation = rateObj.getCompanyLocation();
				
				if (ticket.getCompanyLocation() == null) {
					Ticket tempticket = null;
					tempticket = ticket;
					SubcontractorRate billRate = null;

					String billrateQuery = "select obj from SubcontractorRate obj where obj.transferStation='"
							+ ticket.getOrigin().getId()
							+ "' and obj.landfill='"
							+ ticket.getDestination().getId() + "' and obj.subcontractor="
							+ticket.getSubcontractor().getId();

					List<SubcontractorRate> fss = null;

					fss = genericDAO.executeSimpleQuery(billrateQuery);
					if (fss != null && fss.size() > 0) {

						for (SubcontractorRate rate : fss) {
							
							if (rate.getRateUsing() == null) {
								billRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
								// calculation for a load date
								if ((ticket.getLoadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getLoadDate().getTime() <= rate
												.getValidTo().getTime())) {
									billRate = rate;
									break;
								}
							} else if (rate.getRateUsing() == 2) {
								// calculation for a unload date
								if ((ticket.getUnloadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getUnloadDate().getTime() <= rate
												.getValidTo().getTime())) {
									billRate = rate;
									break;
								}
							}
						}
					}
					if (fss != null && fss.size() > 0) {
						if ((billRate.getCompanyLocation()) != null)
							tempticket.setCompanyLocation(billRate
									.getCompanyLocation());
						genericDAO.saveOrUpdate(tempticket);
					}

				}
				// /

				SubcontractorBilling billing = new SubcontractorBilling();
				billing.setTicket(ticket);
				billing.setLoaded(sdf.format(ticket.getLoadDate()));
				if (ticket.getVehicle() != null) {
					billing.setUnit("" + ticket.getVehicle().getUnit());

				}
				billing.setDate(sdf.format(ticket.getBillBatch()));
				billing.setOrigin((ticket.getOrigin() != null) ? ticket
						.getOrigin().getName() : "");
				billing.setDestination((ticket.getDestination() != null) ? ticket
						.getDestination().getName() : "");
				billing.setOriginGrossWt(ticket.getTransferGross());
				billing.setOriginTareWt(ticket.getTransferTare());
				billing.setOriginNetWt(ticket.getTransferNet());
				billing.setOriginTonsWt(ticket.getTransferTons());
				billing.setDestinationGrossWt(ticket.getLandfillGross());
				billing.setDestinationTareWt(ticket.getLandfillTare());
				billing.setDestinationNetWt(ticket.getLandfillNet());
				billing.setDestinationTonsWt(ticket.getLandfillTons());
				billing.setUnloaded(sdf.format(ticket.getUnloadDate()));
				billing.setTransferTimeIn(ticket.getTransferTimeIn());
				billing.setTransferTimeOut(ticket.getTransferTimeOut());
				billing.setLandfillTimeIn(ticket.getLandfillTimeIn());
				billing.setLandfillTimeOut(ticket.getLandfillTimeOut());
				billing.setOriginTicket(String.valueOf(ticket.getOriginTicket()));
				billing.setDestinationTicket(String.valueOf(ticket
						.getDestinationTicket()));
				billing.setCompanyLocationId(companylocation);
				billing.setDriver((ticket.getDriver() != null) ? ticket
						.getDriver().getFullName() : "");
				SubcontractorRate billingRate = null;
				try {
					String rateQuery = "select obj from SubcontractorRate obj where obj.transferStation='"
							+ ticket.getOrigin().getId()
							+ "' and obj.landfill='"
							+ ticket.getDestination().getId()
							+ "' and obj.subcontractor='"
							+ subcontractorName
							+ "' order by obj.validFrom desc";
					List<SubcontractorRate> fs = genericDAO
							.executeSimpleQuery(rateQuery);
					if (fs != null && fs.size() > 0) {
						for (SubcontractorRate rate : fs) {
							if (rate.getRateUsing() == null) {
								billingRate = rate;
								break;
							} else if (rate.getRateUsing() == 1) {
								// calculation for a load date
								if ((ticket.getLoadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getLoadDate().getTime() <= rate
												.getValidTo().getTime())) {
									billingRate = rate;
									break;
								}
							} else if (rate.getRateUsing() == 2) {
								// calculation for a unload date
								if ((ticket.getUnloadDate().getTime() >= rate
										.getValidFrom().getTime())
										&& (ticket.getUnloadDate().getTime() <= rate
												.getValidTo().getTime())) {
									billingRate = rate;
									break;
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				//
				if (billingRate != null) {
					int billUsing = (billingRate.getBillUsing() == null) ? 1
							: billingRate.getBillUsing();
					int rateType = billingRate.getRateType();
					if (billUsing == 1) {
						billing.setBillUsing("Transfer");
					}
					if (billUsing == 2) {
						billing.setBillUsing("Landfill");
					}
					if (billUsing == 1) {
						if (rateType == 2 || rateType == 3) {
							Double minbilgrosswt = billingRate
									.getMinbillablegrossWeight();
							if (minbilgrosswt != null
									&& ticket.getTransferGross() < minbilgrosswt) {
								billing.setEffectiveGrossWt(minbilgrosswt);
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getTransferTare());
								Double originNetWt = minbilgrosswt
										- ticket.getTransferTare();
								billing.setEffectiveNetWt(originNetWt);
								billing.setEffectiveTonsWt(originNetWt / 2000.0);
							} else {
								billing.setEffectiveGrossWt(ticket
										.getTransferGross());
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getTransferTare());
								billing.setEffectiveNetWt(ticket
										.getTransferNet());
								billing.setEffectiveTonsWt(ticket
										.getTransferTons());
							}
						}
					} else {
						if (rateType == 2 || rateType == 3) {
							Double minbilgrosswt = billingRate
									.getMinbillablegrossWeight();
							if (minbilgrosswt != null
									&& ticket.getLandfillGross() < minbilgrosswt) {
								billing.setEffectiveGrossWt(minbilgrosswt);
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getLandfillTare());
								Double destinationNetWt = minbilgrosswt
										- ticket.getLandfillTare();
								billing.setEffectiveNetWt(destinationNetWt);
								billing.setEffectiveTonsWt(destinationNetWt / 2000.0);

							} else {
								billing.setEffectiveGrossWt(ticket
										.getLandfillGross());
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getLandfillTare());
								billing.setEffectiveNetWt(ticket
										.getLandfillNet());
								billing.setEffectiveTonsWt(ticket
										.getLandfillTons());
							}
						}
					}
					// int rateType = billingRate.getRateType();
					if (rateType == 1) {
						if (billUsing == 1) {
							Double minbilgrosswt = billingRate
									.getMinbillablegrossWeight();
							if (minbilgrosswt != null
									&& ticket.getTransferGross() < minbilgrosswt) {
								billing.setEffectiveGrossWt(minbilgrosswt);
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getTransferTare());
								Double originNetWt = minbilgrosswt
										- ticket.getTransferTare();
								billing.setEffectiveNetWt(originNetWt);
							} else {
								billing.setEffectiveGrossWt(ticket
										.getTransferGross());
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getTransferTare());
								billing.setEffectiveNetWt(ticket
										.getTransferNet());
							}
						} else {
							Double minbilgrosswt = billingRate
									.getMinbillablegrossWeight();
							if (minbilgrosswt != null
									&& ticket.getLandfillGross() < minbilgrosswt) {
								billing.setEffectiveGrossWt(minbilgrosswt);
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getLandfillTare());
								Double destinationNetWt = minbilgrosswt
										- ticket.getLandfillTare();
								billing.setEffectiveNetWt(destinationNetWt);
							} else {
								billing.setEffectiveGrossWt(ticket
										.getLandfillGross());
								billing.setMinimumbillablegrossweight(minbilgrosswt);
								billing.setEffectiveTareWt(ticket
										.getLandfillTare());
								billing.setEffectiveNetWt(ticket
										.getLandfillNet());
							}
						}
						billing.setRate(billingRate.getValue());
					// Change to 8.35 - 28th Dec 2016
						billing.setAmount((billing.getEffectiveNetWt() / 8.35)
								* billingRate.getValue());
					} else if (rateType == 2) {
						// per load
						billing.setRate(billingRate.getValue());
						billing.setAmount(billingRate.getValue());
					} else if (rateType == 3) {
						// per tonne
						billing.setRate(billingRate.getValue());
						billing.setAmount(billing.getEffectiveTonsWt()
								* billingRate.getValue());
					}
					sumBillableTon += billing.getEffectiveTonsWt();
					sumOriginTon += billing.getOriginTonsWt();
					sumDestinationTon += billing.getDestinationTonsWt();
					sumNet += billing.getEffectiveNetWt();
					sumAmount += billing.getAmount();
					billing.setAmount(MathUtil.roundUp(billing.getAmount(), 2));

					/*
					 * if((billingRate.getCompanyLocation().getName())!=null){
					 * companyName=billingRate.getCompanyLocation().getName(); }
					 */

					/*
					 * String fuelSurchargeType =
					 * billingRate.getFuelSurchargeType(); Double fuelSurcharge
					 * = 0.0;
					 */
					/*
					 * if ("N".equalsIgnoreCase(fuelSurchargeType)) {
					 * fuelSurcharge = 0.0; } if
					 * ("M".equalsIgnoreCase(fuelSurchargeType)) { Double
					 * fuelSurchargePerTon = billingRate .getSurchargePerTon();
					 * Double surchargeAmount =
					 * billingRate.getSurchargeAmount(); if (fuelSurchargePerTon
					 * == null && surchargeAmount == null) fuelSurcharge = 0.0;
					 * else if (fuelSurchargePerTon != null) { fuelSurcharge =
					 * billing.getEffectiveTonsWt() fuelSurchargePerTon; } else
					 * { fuelSurcharge = surchargeAmount; } }
					 */
					/*
					 * if ("M".equalsIgnoreCase(fuelSurchargeType)) { Map
					 * criterias = new HashMap(); criterias.put("fromPlace.id",
					 * ticket.getOrigin().getId()); criterias.put("toPlace.id",
					 * ticket.getDestination().getId()); FuelSurcharge surcharge
					 * = genericDAO.getByCriteria(FuelSurcharge.class,
					 * criterias); if (surcharge!=null) { fuelSurcharge =
					 * surcharge.getTruckingCharge(); } }
					 */
					/*
					 * if ("A".equalsIgnoreCase(fuelSurchargeType)) {
					 * FuelSurchargePadd currentPadd = billingRate.getPadd(); if
					 * (currentPadd != null) { Integer paddUsing =
					 * billingRate.getPaddUsing(); Double padd = 0.0;
					 * StringBuffer paddBuffer = new StringBuffer(
					 * "select obj from FuelSurchargePadd obj where obj.code='"
					 * + currentPadd.getCode() + "'"); Date effectiveDate =
					 * null; if (paddUsing != null) { if (paddUsing == 1) {
					 * effectiveDate = ticket.getLoadDate(); } else if
					 * (paddUsing == 2) { effectiveDate =
					 * ticket.getUnloadDate(); } else if (paddUsing == 3) {
					 * effectiveDate = ticket.getBillBatch(); } } if
					 * (effectiveDate != null) {
					 * paddBuffer.append(" and obj.validTo>='" +
					 * mysqldf.format(effectiveDate) + "' and obj.validFrom<='"
					 * + mysqldf.format(effectiveDate) + "'"); } ////
					 * List<FuelSurchargePadd> padds = genericDAO
					 * .executeSimpleQuery(paddBuffer.toString()); if (padds !=
					 * null && padds.size() > 0) { padd =
					 * padds.get(0).getAmount(); } if ((padd != -1) &&
					 * (billingRate.getPeg() != null) && (billingRate.getMiles()
					 * != null)) { int sign = 1; if ((padd -
					 * billingRate.getPeg()) < 0) { sign = -1; } double term =
					 * Math.floor(sign (padd - billingRate.getPeg()) / 0.05);
					 * fuelSurcharge = sign * term billingRate.getMiles() *
					 * 0.01; } if ((padd != -1) && (billingRate.getPeg() !=
					 * null) && (billingRate.getMiles() == null)) { double
					 * percentage = Math.floor((padd - billingRate .getPeg()) /
					 * 0.08); if (rateType == 2) { fuelSurcharge = percentage
					 * billingRate.getValue() * 0.01; } else if (rateType == 3)
					 * { // per tonne fuelSurcharge =
					 * billing.getEffectiveTonsWt() billingRate.getValue() *
					 * percentage 0.01; } } }
					 */
					/*
					 * // Weekly fuel surcharge calculation int wfsr =
					 * billingRate.getFuelsurchargeweeklyRate(); if (wfsr == 1)
					 * { StringBuffer weeklyRateQuery = new StringBuffer(
					 * "select obj from FuelSurchargeWeeklyRate obj where obj.transferStations="
					 * + ticket.getOrigin().getId() +
					 * " and obj.landfillStations=" +
					 * ticket.getDestination().getId() + " ");
					 * FuelSurchargeWeeklyRate fuelsurchargeweeklyrate = null;
					 * Date effectiveDatePadd = null; if
					 * (billingRate.getWeeklyRateUsing() != null) { if
					 * (billingRate.getWeeklyRateUsing() == 1) {
					 * effectiveDatePadd = ticket.getLoadDate(); } if
					 * (billingRate.getWeeklyRateUsing() == 2) {
					 * effectiveDatePadd = ticket.getUnloadDate(); } if
					 * (billingRate.getWeeklyRateUsing() == 3) {
					 * effectiveDatePadd = ticket.getBillBatch(); }
					 * weeklyRateQuery.append(" and obj.fromDate <= '" +
					 * mysqldf.format(effectiveDatePadd) +
					 * "' and obj.toDate >= '" +
					 * mysqldf.format(effectiveDatePadd) + "'"); }
					 * List<FuelSurchargeWeeklyRate> listfswr = genericDAO
					 * .executeSimpleQuery(weeklyRateQuery.toString()); if
					 * (listfswr != null && listfswr.size() > 0) {
					 * fuelsurchargeweeklyrate = listfswr.get(0); } if
					 * (fuelsurchargeweeklyrate != null) { int fswrateType =
					 * fuelsurchargeweeklyrate .getRateType(); if (fswrateType
					 * == 3) { fuelSurcharge = billing.getEffectiveTonsWt()
					 * fuelsurchargeweeklyrate .getFuelSurchargeRate(); } if
					 * (fswrateType == 2) { fuelSurcharge =
					 * fuelsurchargeweeklyrate .getFuelSurchargeRate(); } if
					 * (fswrateType == 1) { fuelSurcharge =
					 * (billing.getEffectiveNetWt() / 8.35)
					 * fuelsurchargeweeklyrate .getFuelSurchargeRate(); } } } }
					 */// end of Weekly fuel surcharge calculation
					Double otherCharges = billingRate.getOtherCharges();
					otherCharges = MathUtil.roundUp(otherCharges, 2);
					billing.setOtherCharges(otherCharges);
					sumOtherCharges += otherCharges;

					Double fuelSurcharge = billingRate.getFuelSurchargeAmount();
					fuelSurcharge = MathUtil.roundUp(fuelSurcharge, 2);
					billing.setFuelSurcharge(fuelSurcharge);
					sumFuelSurcharge += fuelSurcharge;

					sumOtherCharges = MathUtil.roundUp(sumOtherCharges, 2);
					sumFuelSurcharge = MathUtil.roundUp(sumFuelSurcharge, 2);
					/*
					 * if (billingRate.getTonnagePremium() != null) { Double
					 * premiumTonne = billingRate.getTonnagePremium()
					 * .getPremiumTonne(); Double rate =
					 * billingRate.getTonnagePremium().getRate(); if
					 * (billing.getEffectiveTonsWt() > premiumTonne) {
					 * billing.setTonnagePremium((billing.getEffectiveTonsWt() -
					 * premiumTonne) rate); } } else {
					 * billing.setTonnagePremium(0.0); }
					 * sumTonnage+=billing.getTonnagePremium();
					 * billing.setTonnagePremium
					 * (MathUtil.roundUp(billing.getTonnagePremium(),2)); double
					 * demmurageCharge = (billingRate.getDemmurageCharge() !=
					 * null) ? billingRate .getDemmurageCharge() : 0.0;
					 * sumDemmurage+=billing.getDemurrageCharge();
					 * billing.setDemurrageCharge
					 * (MathUtil.roundUp(demmurageCharge,2));
					 */
					// }
					//
				} else {
					if (!expiredRate)
						str.add("<u>Rates Are Expired or not Available</u></br>");
					expiredRate = true;
					errorCount++;
					billing.setRate(0.0);
					billing.setFuelSurcharge(0.0);
					sumOriginTon += billing.getOriginTonsWt();
					sumDestinationTon += billing.getDestinationTonsWt();
					Location originName = genericDAO.getById(Location.class,
							ticket.getOrigin().getId());
					Location DestinationName = genericDAO.getById(
							Location.class, ticket.getDestination().getId());
					boolean cont = str.contains((originName.getName() + " - "
							+ DestinationName.getName() + "</br>"));
					String string = (originName.getName() + " - "
							+ DestinationName.getName() + "</br>");

					if (!cont)
						str.add(string);
					wrapper.setList(str);
				}
				if (billingRate != null) {
					amount = billing.getAmount() + billing.getFuelSurcharge()
							+ billing.getOtherCharges();
					billing.setTotAmt(amount);
				}
				billing.setSubcontractorId(subContractor);
				summarys.add(billing);
						}
				}else{

					if (!expiredRate)
						str.add("<u>Rates Are Expired or not Available</u></br>");
					expiredRate = true;
					errorCount++;
					
					Location originName = genericDAO.getById(Location.class,
							ticket.getOrigin().getId());
					Location DestinationName = genericDAO.getById(
							Location.class, ticket.getDestination().getId());
					boolean cont = str.contains((originName.getName() + " - "
							+ DestinationName.getName() + "</br>"));
					String string = (originName.getName() + " - "
							+ DestinationName.getName() + "</br>");

					if (!cont)
						str.add(string);
					wrapper.setList(str);
				
				
				}			
			}
			else{


				if (!expiredRate)
					str.add("<u>Rates Are Expired or not Available</u></br>");
				expiredRate = true;
				errorCount++;
				
				Location originName = genericDAO.getById(Location.class,
						ticket.getOrigin().getId());
				Location DestinationName = genericDAO.getById(
						Location.class, ticket.getDestination().getId());
				boolean cont = str.contains((originName.getName() + " - "
						+ DestinationName.getName() + "</br>"));
				String string = (originName.getName() + " - "
						+ DestinationName.getName() + "</br>");

				if (!cont)
					str.add(string);
				wrapper.setList(str);
			
			
			
			}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		/*
		 * Long subcontractorId =
		 * Long.valueOf(Long.parseLong(subcontractorName));
		 * 
		 * SubContractor subContractor =
		 * (SubContractor)genericDAO.getById(SubContractor.class,
		 * subcontractorId);
		 */
		wrapper.setSubcontratorId(subContractor);
		wrapper.setSubcontractorname(subContractor.getName());
		wrapper.setAddress1(subContractor.getAddress());
		wrapper.setAddress2(subContractor.getAddress2());
		wrapper.setCity(subContractor.getCity());
		wrapper.setZipcode(subContractor.getZipcode());
		wrapper.setPhone(subContractor.getPhone());
		wrapper.setFax(subContractor.getFax());
		wrapper.setState(subContractor.getState().getName());
		if (companylocation != null)
			wrapper.setCompanyLocationId(companylocation);
		// wrapper.setCompany(companyName);

		sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);
		sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
		sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);
		sumNet = MathUtil.roundUp(sumNet, 2);
		sumAmount = MathUtil.roundUp(sumAmount, 2);

		
		System.out.println("********* the string before "+misceCharges);
		Double miscelCharges = 0.0;
		
		
		if (!misceCharges.isEmpty()) {
			String[] miscellCharges=misceCharges.split(",");
			for(int i=0;i<miscellCharges.length;i++){				
				System.out.println("**** the sepearted value is "+Double.parseDouble(miscellCharges[i]));
					 miscelCharges =miscelCharges+Double.parseDouble(miscellCharges[i]);
				
			}
		}
			

		sumTotal = sumAmount + sumFuelSurcharge + sumOtherCharges;
		wrapper.setSumBillableTon(sumBillableTon);
		wrapper.setSumOriginTon(sumOriginTon);
		wrapper.setSumDestinationTon(sumDestinationTon);
		wrapper.setSumNet(sumNet);
		wrapper.setSumAmount(sumAmount);
		wrapper.setSumFuelSurcharge(sumFuelSurcharge);
		wrapper.setSumTotal(sumTotal);
		wrapper.setSumOtherCharges(sumOtherCharges);
		if (!misceCharges.isEmpty()) {
			if (misceCharges.charAt(0) == '-') {
				grandTotal = sumTotal - miscelCharges;
			}
		} else {
			grandTotal = sumTotal + miscelCharges;
		}
		grandTotal = sumTotal + miscelCharges;
		grandTotal = MathUtil.roundUp(grandTotal, 2);
		wrapper.setGrandTotal(grandTotal);
		if (!(misceCharges == "")) {
			// misceCharges="$"+misceCharges;
			wrapper.setMiscelleneousCharges(misceCharges);
			wrapper.setMiscelleneousNote(misceNote);
		}
		
		wrapper.setTotalRowCount(tickets.size());
		
		
		return wrapper;
	}

	@Override
	public FuelViolationReportWrapper generateFuelViolationData(
			SearchCriteria searchCriteria, FuelViolationInput input) {

		Map<String, String> params = new HashMap<String, String>();
		
		String transactionDateFrom1 = (String) searchCriteria.getSearchMap()
				.get("transactionDateFrom");
		
		String transactionDateTo1 = (String) searchCriteria.getSearchMap().get(
				"transactionDateTo");

		String transactionDateFrom = ReportDateUtil.getFromDate(input
				.getTransactionDateFrom());
		
		String transactionDateTo = ReportDateUtil.getToDate(input
				.getTransactionDateTo());		

		String terminal = input.getTerminal();
		
		String driver = input.getDriver();
		
		String company = input.getCompany();
		params.put("company", company);

		StringBuffer query = new StringBuffer("");
		
		StringBuffer countQuery = new StringBuffer("");
		
		query.append("select f from FuelLog f where f.fuelViolation='Violated'   ");
		
		

		if (!StringUtils.isEmpty(company)) {
			query.append("and  f.company in (" + company + ")");
			
		}
		if (!StringUtils.isEmpty(terminal)) {
			query.append(" and  f.terminal in (" + terminal + ")");
			
		}

		

		if (!StringUtils.isEmpty(transactionDateFrom1)
				&& !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom)
					&& !StringUtils.isEmpty(transactionDateTo)) {
				query.append(" and  f.transactiondate >='"
						+ transactionDateFrom +"' and f.transactiondate<='"+transactionDateTo
						+ "'");
				
			}
		}
		if (!StringUtils.isEmpty(driver)) {			
			query.append("and  f.driversid in (" + driver + ")");
			
		}
		
		query.append(" order by f.driversid.fullName asc,f.transactiondate asc,f.transactiontime asc,f.amount asc,f.gallons asc");
 
		System.out.println("\nquery=fuelog=>" + query + "\n");
		
		//Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();
		
		
		
		//System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");

		
		
		List<FuelLog> fs = (List<FuelLog>) genericDAO
		.getEntityManager()
		.createQuery(query.toString())
		.setMaxResults(searchCriteria.getPageSize())
		.setFirstResult(
				searchCriteria.getPage() * searchCriteria.getPageSize())
		.getResultList();
			
		List<FuelLog> summarys = new ArrayList<FuelLog>();
		FuelViolationReportWrapper wrapper = new FuelViolationReportWrapper();
		wrapper.setFuellog(summarys);

		
		double totalGallons = 0.0;
		double totalFees = 0.0;
		double totaldiscounts = 0.0;
		double totalAmounts = 0.0;
		double totalGrossCost=0.0;

		Map<String, List<FuelLog>> fuellogMap = new HashMap<String, List<FuelLog>>();
		
		for (FuelLog fuelog : fs) {
			
			//String  drvinspcquery = "select obj from DriverInspection obj where obj.company="+fuelog.getCompany().getId()+" and obj.terminal="+fuelog.getTerminal().getId()+" and obj.weekDate='"+drvdf.format(fuelog.getTransactiondate())+"' and obj.driver="+fuelog.getDriversid().getId()+" and inspectionStatus='No Loads'";
			//System.out.println("*****************");
			//System.out.println("***************** Driver Inspection Query is "+drvinspcquery);
			//System.out.println("*****************");
			//List<DriverInspection> drvinspObj = genericDAO.executeSimpleQuery(drvinspcquery); 
			//System.out.println("*********** drviver lis valeu is "+drvinspObj);
			//if(!drvinspObj.isEmpty() && drvinspObj.size() > 0){		
			
			if (fuelog != null) {
				FuelLog output = new FuelLog();				
				
				output.setFuelVenders((fuelog.getFuelvendor() != null) ? fuelog
						.getFuelvendor().getName() : "");
				output.setCompanies((fuelog.getCompany() != null) ? fuelog
						.getCompany().getName() : "");
				output.setTerminals((fuelog.getTerminal() != null) ? fuelog
						.getTerminal().getName() : "");
				
				output.setInvoicedDate((fuelog.getInvoiceDate() != null) ? sdf
						.format(fuelog.getInvoiceDate()) : "");
				output.setInvoiceNo((fuelog.getInvoiceNo() != null) ? fuelog
						.getInvoiceNo() : "");
				output.setUnits((fuelog.getUnit() != null) ? fuelog.getUnit()
						.getUnit().toString() : "");

				
				String fuelcardNum=null;
                if(fuelog.getFuelcard()!=null){                                        
                        String fuelcardnumber=fuelog.getFuelcard().getFuelcardNum();                                        
                        
                        if(fuelcardnumber.length()>8 && fuelcardnumber.length()<=12){
                                String[] fuelcardnum = new String[10];
                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
                                        fuelcardnum[2]=fuelcardnumber.substring(8,fuelcardnumber.length());
                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2];                                                
                        }
                        
                        else if(fuelcardnumber.length()>12){
                                String[] fuelcardnum = new String[10];
                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
                                        fuelcardnum[2]=fuelcardnumber.substring(8,12);
                                        fuelcardnum[3]=fuelcardnumber.substring(12,fuelcardnumber.length());
                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2]+"-"+fuelcardnum[3];                                                
                        }
                        else
                        {
                                fuelcardNum=fuelcardnumber;
                        }  
                }					
				output.setDrivers((fuelog.getDriversid() != null) ? fuelog
						.getDriversid().getFullName() : "");
				
				output.setFuelCardNumbers((fuelog.getFuelcard() != null) ? fuelcardNum : "");
				

				output.setFueltype((fuelog.getFueltype() != null) ? fuelog
						.getFueltype() : "");

				output.setCity((fuelog.getCity() != null) ? fuelog.getCity()
						: "");
				output.setStates((fuelog.getState() != null) ? fuelog
						.getState().getName() : "");
				output.setTransactiontime((fuelog.getTransactiontime() != null) ? fuelog
						.getTransactiontime() : "");
				output.setTransactionsDate((fuelog.getTransactiondate() != null) ? sdf
						.format(fuelog.getTransactiondate()) : "");
				output.setGallons((fuelog.getGallons() != null) ? fuelog
						.getGallons() : 0.0);
				
				output.setUnitprice((fuelog.getUnitprice() != null) ? fuelog
						.getUnitprice() : 0.0);
				
				output.setGrosscost((fuelog.getGrosscost() !=null) ? fuelog.getGrosscost(): 0.0);
				output.setFees((fuelog.getFees() != null) ? fuelog.getFees()
						: 0.0);
				output.setDiscounts((fuelog.getDiscounts() != null) ? fuelog
						.getDiscounts() : 0.0);
				output.setAmount((fuelog.getAmount() != null) ? fuelog
						.getAmount() : 0.0);

				if (fuelog.getGallons() != null)
					totalGallons += fuelog.getGallons();
				if(fuelog.getGrosscost() != null)
					totalGrossCost+=fuelog.getGrosscost();
				if (fuelog.getFees() != null)
					totalFees += fuelog.getFees();
				if (fuelog.getDiscounts() != null)
					totaldiscounts += fuelog.getDiscounts();
				if (fuelog.getAmount() != null)
					totalAmounts += fuelog.getAmount();

				summarys.add(output);
			}
		//}
        //else{
			//do nothing 	
		//}			
		}        
		totalAmounts = MathUtil.roundUp(totalAmounts, 2);
		totaldiscounts = MathUtil.roundUp(totaldiscounts, 2);
		totalFees = MathUtil.roundUp(totalFees, 2);
		totalGallons = MathUtil.roundUp(totalGallons, 3);
		totalGrossCost=MathUtil.roundUp(totalGrossCost, 2);		
		
		wrapper.setTotalRowCount(fs.size());
		
		wrapper.setTotalGallons(totalGallons);
		
		return wrapper;
 }
	
	
	
	@Override
	public FuelOverLimitReportWrapper generateFuelOverLimitData(
			SearchCriteria searchCriteria, FuelOverLimitInput input) {

		Map<String, String> params = new HashMap<String, String>();
		
		String transactionDateFrom1 = (String) searchCriteria.getSearchMap()
				.get("transactionDateFrom");
		
		String transactionDateTo1 = (String) searchCriteria.getSearchMap().get(
				"transactionDateTo");

		String transactionDateFrom = ReportDateUtil.getFromDate(input
				.getTransactionDateFrom());
		
		String transactionDateTo = ReportDateUtil.getToDate(input
				.getTransactionDateTo());		

		String terminal = input.getTerminal();
		
		String driver = input.getDriver();
		
		String company = input.getCompany();
		
		String maxGallons = input.getMaxGallons();	
		
		params.put("company", company);

		StringBuffer query = new StringBuffer("");		
		
		
		query.append("select f1.* from fuel_log f1 INNER JOIN (select driver_id,transaction_date,company,terminal from fuel_log where 1=1");	

		if (!StringUtils.isEmpty(company)) {
			query.append(" and  company in (" + company + ")");			
		}
		if (!StringUtils.isEmpty(terminal)) {
			query.append(" and  terminal in (" + terminal + ")");			
		}		

		if (!StringUtils.isEmpty(transactionDateFrom1)
				&& !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom)
					&& !StringUtils.isEmpty(transactionDateTo)) {
				query.append(" and  transaction_date between '"
						+ transactionDateFrom + "' and '" + transactionDateTo
						+ "'");				
			}
		}
		
		if (!StringUtils.isEmpty(driver)) {			
			query.append(" and  driver_id in (" + driver + ")");			
		}		

		query.append(" group by driver_id,transaction_date");
		
		
		if (!StringUtils.isEmpty(maxGallons)) {			
			query.append(" having sum(gallons) > " + maxGallons);			
		}
		
		query.append(") f2  ON f1.driver_id=f2.driver_id and f1.transaction_date=f2.transaction_date and f1.company=f2.company and f1.terminal=f2.terminal  INNER JOIN driver d1  ON f1.driver_id=d1.id    order by d1.full_name asc,f1.transaction_date asc,f1.transaction_time asc,f1.amount asc,f1.gallons asc");
		
		
		
		System.out.println("\nquery=fuelog=>" + query + "\n");		
		
		List<FuelLog> fs = (List<FuelLog>) genericDAO
		.getEntityManager()
		.createNativeQuery(query.toString(),FuelLog.class)
		.setMaxResults(searchCriteria.getPageSize())
		.setFirstResult(
				searchCriteria.getPage() * searchCriteria.getPageSize())
		.getResultList();
		
			
		List<FuelLog> summarys = new ArrayList<FuelLog>();
		FuelOverLimitReportWrapper wrapper = new FuelOverLimitReportWrapper();
		wrapper.setFuellog(summarys);

		
		double totalGallons = 0.0;
		double totalFees = 0.0;
		double totaldiscounts = 0.0;
		double totalAmounts = 0.0;
		double totalGrossCost=0.0;

		Map<String, List<FuelLog>> fuellogMap = new HashMap<String, List<FuelLog>>();

		for (FuelLog fuelog : fs) {			
			if (fuelog != null) {
				FuelLog output = new FuelLog();
				output.setFuelVenders((fuelog.getFuelvendor() != null) ? fuelog
						.getFuelvendor().getName() : "");
				output.setCompanies((fuelog.getCompany() != null) ? fuelog
						.getCompany().getName() : "");
				output.setTerminals((fuelog.getTerminal() != null) ? fuelog
						.getTerminal().getName() : "");
				
				output.setInvoicedDate((fuelog.getInvoiceDate() != null) ? sdf
						.format(fuelog.getInvoiceDate()) : "");
				output.setInvoiceNo((fuelog.getInvoiceNo() != null) ? fuelog
						.getInvoiceNo() : "");
				output.setUnits((fuelog.getUnit() != null) ? fuelog.getUnit()
						.getUnit().toString() : "");

				
				String fuelcardNum=null;
                if(fuelog.getFuelcard()!=null){                                        
                        String fuelcardnumber=fuelog.getFuelcard().getFuelcardNum();                                        
                        
                        if(fuelcardnumber.length()>8 && fuelcardnumber.length()<=12){
                                String[] fuelcardnum = new String[10];
                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
                                        fuelcardnum[2]=fuelcardnumber.substring(8,fuelcardnumber.length());
                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2];                                                
                        }
                        
                        else if(fuelcardnumber.length()>12){
                                String[] fuelcardnum = new String[10];
                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
                                        fuelcardnum[2]=fuelcardnumber.substring(8,12);
                                        fuelcardnum[3]=fuelcardnumber.substring(12,fuelcardnumber.length());
                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2]+"-"+fuelcardnum[3];                                                
                        }
                        else
                        {
                                fuelcardNum=fuelcardnumber;
                        }  
                }					
				output.setDrivers((fuelog.getDriversid() != null) ? fuelog
						.getDriversid().getFullName() : "");
				
				output.setFuelCardNumbers((fuelog.getFuelcard() != null) ? fuelcardNum : "");
				

				output.setFueltype((fuelog.getFueltype() != null) ? fuelog
						.getFueltype() : "");

				output.setCity((fuelog.getCity() != null) ? fuelog.getCity()
						: "");
				output.setStates((fuelog.getState() != null) ? fuelog
						.getState().getName() : "");
				output.setTransactiontime((fuelog.getTransactiontime() != null) ? fuelog
						.getTransactiontime() : "");
				output.setTransactionsDate((fuelog.getTransactiondate() != null) ? sdf
						.format(fuelog.getTransactiondate()) : "");
				output.setGallons((fuelog.getGallons() != null) ? fuelog
						.getGallons() : 0.0);
				
				output.setUnitprice((fuelog.getUnitprice() != null) ? fuelog
						.getUnitprice() : 0.0);
				
				output.setGrosscost((fuelog.getGrosscost() !=null) ? fuelog.getGrosscost(): 0.0);
				output.setFees((fuelog.getFees() != null) ? fuelog.getFees()
						: 0.0);
				output.setDiscounts((fuelog.getDiscounts() != null) ? fuelog
						.getDiscounts() : 0.0);
				output.setAmount((fuelog.getAmount() != null) ? fuelog
						.getAmount() : 0.0);

				if (fuelog.getGallons() != null)
					totalGallons += fuelog.getGallons();
				if(fuelog.getGrosscost() != null)
					totalGrossCost+=fuelog.getGrosscost();
				if (fuelog.getFees() != null)
					totalFees += fuelog.getFees();
				if (fuelog.getDiscounts() != null)
					totaldiscounts += fuelog.getDiscounts();
				if (fuelog.getAmount() != null)
					totalAmounts += fuelog.getAmount();

				summarys.add(output);
			}
		
        			
		}        
		totalAmounts = MathUtil.roundUp(totalAmounts, 2);
		totaldiscounts = MathUtil.roundUp(totaldiscounts, 2);
		totalFees = MathUtil.roundUp(totalFees, 2);
		totalGallons = MathUtil.roundUp(totalGallons, 3);
		totalGrossCost=MathUtil.roundUp(totalGrossCost, 2);		
		
		wrapper.setTotalGallons(totalGallons);
		
		return wrapper;
 }
	
	@Override
	public List<FuelLogAverageReportWrapper> generateFuellogAverageData(
			SearchCriteria searchCriteria, FuelLogReportInput input) {
		

		Map<String, String> params = new HashMap<String, String>();
		
		List<FuelLogAverageReportWrapper> wrapperlist = new ArrayList<FuelLogAverageReportWrapper>();
		
		String fuelVendor = input.getFuelVendor();
		String fromInvoiceDate1 = (String) searchCriteria.getSearchMap().get(
				"fromInvoiceDate");
		String invoiceDateTo1 = (String) searchCriteria.getSearchMap().get(
				"invoiceDateTo");
		String transactionDateFrom1 = (String) searchCriteria.getSearchMap()
				.get("transactionDateFrom");
		String transactionDateTo1 = (String) searchCriteria.getSearchMap().get(
				"transactionDateTo");

		String fromInvoiceDate = ReportDateUtil.getFromDate(input
				.getFromInvoiceDate());
		String invoiceDateTo = ReportDateUtil.getToDate(input
				.getInvoiceDateTo());
		String transactionDateFrom = ReportDateUtil.getFromDate(input
				.getTransactionDateFrom());
		String transactionDateTo = ReportDateUtil.getToDate(input
				.getTransactionDateTo());

		String cardno = (String) searchCriteria.getSearchMap().get("cardno");

		/*
		 * String fromInvoiceNo = (String)
		 * searchCriteria.getSearchMap().get("fromInvoiceNo"); String
		 * InvoiceNoTo = (String)
		 * searchCriteria.getSearchMap().get("InvoiceNoTo");
		 */
		String fueltype = (String) searchCriteria.getSearchMap()
				.get("fueltype");
		String fueltype1 = input.getFueltype();
		// System.out.println("\nfueltype===>"+fueltype+"\n");
		// System.out.println("\nfueltype1===>"+fueltype1+"\n");

		String terminal = input.getTerminal();
		String driver = input.getDriver();
		String truck = input.getUnit();
		String state = input.getState();

		String company = input.getCompany();
		params.put("company", company);

		String gallansfrom = input.getGallonsFrom();
		String gallansto = input.getGallonsTo();

		String unitpricefrom = input.getUnitPriceFrom();
		String unitpriceto = input.getUnitPriceTo();

		String feesfrom = input.getFeesFrom();
		String feesto = input.getFeesTo();

		String discountfrom = input.getDiscountFrom();
		String discountto = input.getDiscountTo();

		String amountfrom = input.getAmountFrom();
		String amountto = input.getAmountTo();

		String fromInvoiceNo = input.getFromInvoiceNo();
		String InvoiceNoTo = input.getInvoiceNoTo();
		// String fueltype = input.getFueltype();

		StringBuffer query = new StringBuffer("");
		
		query.append("select obj.company,obj.unit,sum(obj.amount),sum(obj.gallons) from FuelLog obj where 1=1 ");
		

		if (!StringUtils.isEmpty(company)) {
			query.append(" and  obj.company in (" + company + ")");		
		}		

		if (!StringUtils.isEmpty(transactionDateFrom1) && !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom)&& !StringUtils.isEmpty(transactionDateTo)) {
				query.append("  and  obj.transactiondate >= '"
						+ transactionDateFrom + "' and obj.transactiondate <='" + transactionDateTo
						+ "'");
				
			}
		}
		

		
		if (!StringUtils.isEmpty(truck)) {	
			String truckIds="";			
				   
				   String vehiclequery="select obj from Vehicle obj where obj.type=1" 
				   		+" and obj.unit in ("
						+truck
						+")";				
					
					List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
					
					if(vehicleLists!=null && vehicleLists.size()>0){						
						for(Vehicle vehicleObj : vehicleLists) {						
							if(StringUtils.isEmpty(truckIds)){	
							truckIds=String.valueOf(vehicleObj.getId());
							}
							else{
								truckIds=truckIds+","+String.valueOf(vehicleObj.getId());
							}
						}
					}	
			
			query.append(" and  obj.unit in (" + truckIds + ")");			
		}
		
		query.append(" group by obj.company,obj.unit");
		
		System.out.println("\nquery=fuelog=>" + query.toString() + "\n");
		
		List<FuelLog> fulelogAverageList = genericDAO.executeSimpleQuery(query.toString());
		
		for(Object fuelAverageReportData: fulelogAverageList){
			Object[] objarry=(Object[])fuelAverageReportData;
			if(objarry!=null){							
				FuelLogAverageReportWrapper wrapper = new FuelLogAverageReportWrapper();
				//Location locObj = genericDAO.getById(Location.class,Long.parseLong(objarry[0].toString()));
				wrapper.setCompany(((Location)objarry[0]).getName());
				//Vehicle vehObj = genericDAO.getById(Vehicle.class, Long.parseLong(objarry[1].toString()));
				wrapper.setTruck(((Vehicle)objarry[1]).getUnit().toString());
				wrapper.setNetAmount(Double.parseDouble(objarry[2].toString()));
				wrapper.setTotalGallon(Double.parseDouble(objarry[3].toString()));
				wrapper.setAverageAmount(wrapper.getNetAmount()/wrapper.getTotalGallon());				
				wrapperlist.add(wrapper);
			}
		}

		if (wrapperlist.size() > 0) {
			Collections.sort(wrapperlist, new Comparator<FuelLogAverageReportWrapper>() {
			        @Override
			        public int compare(final FuelLogAverageReportWrapper object1, final FuelLogAverageReportWrapper object2) {
			        	return (Integer.parseInt(object1.getTruck())-Integer.parseInt(object2.getTruck()));
			        }
			} );
		}
		
		return wrapperlist;
			
	}
	
	@Override
	public FuelDistributionReportWrapper generateFuelDistributionData(SearchCriteria searchCriteria, FuelDistributionReportInput input) {
		FuelLogReportInput fuelLogReportInput = new FuelLogReportInput();
		map(input, fuelLogReportInput);
		
		FuelLogReportWrapper fuelLogReportWrapper = generateFuellogData(searchCriteria, fuelLogReportInput, true);
		
		FuelDistributionReportWrapper fuelDistributionReportWrapper = new FuelDistributionReportWrapper();
		map(fuelDistributionReportWrapper, fuelLogReportWrapper);
		
		if (!fuelDistributionReportWrapper.getFuellog().isEmpty()) {
			List<FuelLog> groupedFuelLogList = groupFuelLogs(fuelDistributionReportWrapper.getFuellog());
			fuelDistributionReportWrapper.setFuellog(groupedFuelLogList);
		}
		
		return fuelDistributionReportWrapper;
	}
	
	private void map(FuelDistributionReportInput fuelDistributionReportInput, FuelLogReportInput fuelLogReportInput) {
		fuelLogReportInput.setCompany(fuelDistributionReportInput.getCompany());
		fuelLogReportInput.setTerminal(fuelDistributionReportInput.getTerminal());
		fuelLogReportInput.setFuelVendor(fuelDistributionReportInput.getFuelVendor());
		fuelLogReportInput.setFromInvoiceNo(fuelDistributionReportInput.getFromInvoiceNo());
		fuelLogReportInput.setInvoiceNoTo(fuelDistributionReportInput.getInvoiceNoTo());
		fuelLogReportInput.setFromInvoiceDate(fuelDistributionReportInput.getFromInvoiceDate());
		fuelLogReportInput.setInvoiceDateTo(fuelDistributionReportInput.getInvoiceDateTo());
		fuelLogReportInput.setTransactionDateFrom(fuelDistributionReportInput.getTransactionDateFrom());
		fuelLogReportInput.setTransactionDateTo(fuelDistributionReportInput.getTransactionDateTo());
	}
	
	private void map(FuelDistributionReportWrapper fuelDistributionReportWrapper, FuelLogReportWrapper fuelLogReportWrapper ) {
		fuelDistributionReportWrapper.setFuellog(fuelLogReportWrapper.getFuellog());
		fuelDistributionReportWrapper.setTotalAmounts(fuelLogReportWrapper.getTotalAmounts());
		fuelDistributionReportWrapper.setTotalColumn(fuelLogReportWrapper.getTotalColumn());
		fuelDistributionReportWrapper.setInvoiceDateFrom(fuelLogReportWrapper.getInvoiceDateFrom());
		fuelDistributionReportWrapper.setInvoiceDateTo(fuelLogReportWrapper.getInvoiceDateTo());
		fuelDistributionReportWrapper.setTransactionDateFrom(fuelLogReportWrapper.getTransactionDateFrom());
		fuelDistributionReportWrapper.setTransactionDateTo(fuelLogReportWrapper.getTransactionDateTo());
		fuelDistributionReportWrapper.setInvoiceNumberFrom(fuelLogReportWrapper.getInvoiceNumberFrom());
		fuelDistributionReportWrapper.setInvoiceNumberTo(fuelLogReportWrapper.getInvoiceNumberTo());
	}
	
	private List<FuelLog> groupFuelLogs(List<FuelLog> fuelLogList) {
		String prevKey = StringUtils.EMPTY;
		String currentKey = StringUtils.EMPTY;
		double groupAmount = 0.0;
		FuelLog previousFuelLog = null;
		List<FuelLog> groupedFuelLogList = new ArrayList<FuelLog>();
		for (FuelLog aFuelLog :  fuelLogList) {
			currentKey = aFuelLog.getFuelVenders() + "|" + aFuelLog.getCompanies() + "|" + aFuelLog.getTerminals() 
				+ "|" + aFuelLog.getDriverCategory() + "|" + aFuelLog.getFueltype();
			
			if (StringUtils.isEmpty(prevKey)) {
				prevKey = currentKey;
				previousFuelLog = aFuelLog;
			}
			
			if (!currentKey.equals(prevKey)) {
				previousFuelLog.setAmount(groupAmount);
				groupedFuelLogList.add(previousFuelLog);
				
				prevKey = currentKey;
				previousFuelLog = aFuelLog;
				groupAmount = aFuelLog.getAmount();
			} else {
				groupAmount += aFuelLog.getAmount();			
			}
		}
		
		previousFuelLog.setAmount(groupAmount);
		groupedFuelLogList.add(previousFuelLog);
		
		return groupedFuelLogList;
	}
	
	@Override
	public IFTAReportWrapper generateIFTAData(SearchCriteria searchCriteria, IFTAReportInput iftaReportInput) {
		MileageLogReportInput mileageLogReportInput = new MileageLogReportInput();
		map(mileageLogReportInput, iftaReportInput);
		mileageLogReportInput.setReportType(MileageLogReportInput.REPORT_TYPE_TOTALS);
		
		MileageLogReportWrapper mileageLogReportWrapper = generateMileageLogData(searchCriteria, mileageLogReportInput);
		
		FuelLogReportInput fuelLogReportInput = new FuelLogReportInput();
		map(fuelLogReportInput, searchCriteria, iftaReportInput);
		fuelLogReportInput.setReportType(FuelLogReportInput.REPORT_TYPE_FUEL_TRUCK);
		
		FuelLogReportWrapper fuelLogReportWrapper = generateFuellogData(searchCriteria, fuelLogReportInput, false);
		Map<String, FuelLog> aggreateFuelLogMap = aggregateFuelLogByCompanyState(fuelLogReportWrapper.getFuellog());
		
		List<IFTAReport> iftaReportList = new ArrayList<IFTAReport>();
		for (MileageLog aMileageLog : mileageLogReportWrapper.getMileageLogList()) {
			IFTAReport anIFTAReport = new IFTAReport();
			anIFTAReport.setCompany(aMileageLog.getCompany());
			anIFTAReport.setState(aMileageLog.getState());
			anIFTAReport.setMiles(aMileageLog.getMiles());
			
			String key = aMileageLog.getCompany().getName() + "|" + aMileageLog.getState().getName();
			FuelLog aFuelLog = aggreateFuelLogMap.get(key);
			if (aFuelLog != null) {
				anIFTAReport.setGallons(aFuelLog.getGallons());
				
				if (anIFTAReport.getMiles() != null && anIFTAReport.getGallons() != null) {
					Double mpg = anIFTAReport.getMiles()/anIFTAReport.getGallons();
					anIFTAReport.setMpg(mpg);
				}
			}
			
			iftaReportList.add(anIFTAReport);
		}
		
		IFTAReportWrapper anIFTAReportWrapper = new IFTAReportWrapper();
		anIFTAReportWrapper.setCompanies(mileageLogReportWrapper.getCompanies());
		anIFTAReportWrapper.setPeriodFrom(mileageLogReportWrapper.getPeriodFrom());
		anIFTAReportWrapper.setPeriodTo(mileageLogReportWrapper.getPeriodTo());
		anIFTAReportWrapper.setLastInStateFrom(mileageLogReportWrapper.getLastInStateFrom());
		anIFTAReportWrapper.setLastInStateTo(mileageLogReportWrapper.getLastInStateTo());
		anIFTAReportWrapper.setStates(mileageLogReportWrapper.getStates());
		anIFTAReportWrapper.setTotalMiles(mileageLogReportWrapper.getTotalMiles());
		anIFTAReportWrapper.setTotalRows(mileageLogReportWrapper.getTotalRows());
		anIFTAReportWrapper.setTotalGallons(fuelLogReportWrapper.getTotalGallons());
		anIFTAReportWrapper.setIftaReportList(iftaReportList);
		
		cleanMileageLogReportSearchCriteria(searchCriteria);
		
		return anIFTAReportWrapper;
	}
	
	@Override
	public IFTAReportWrapper generateMPGData(SearchCriteria searchCriteria, IFTAReportInput iftaReportInput) {
		MileageLogReportInput mileageLogReportInput = new MileageLogReportInput();
		map(mileageLogReportInput, iftaReportInput);
		mileageLogReportInput.setReportType(MileageLogReportInput.REPORT_TYPE_DETAILS);
		
		MileageLogReportWrapper mileageLogReportWrapper = generateMileageLogData(searchCriteria, mileageLogReportInput);
		List<MileageLog> aggregateMileageLogList = aggregateMileageLogByUnit(mileageLogReportWrapper.getMileageLogList());
		
		FuelLogReportInput fuelLogReportInput = new FuelLogReportInput();
		map(fuelLogReportInput, searchCriteria, iftaReportInput);
		fuelLogReportInput.setReportType(FuelLogReportInput.REPORT_TYPE_FUEL_TRUCK);
		
		FuelLogReportWrapper fuelLogReportWrapper = generateFuellogData(searchCriteria, fuelLogReportInput, false);
		Map<String, FuelLog> aggreateFuelLogMap = aggregateFuelLogByUnit(fuelLogReportWrapper.getFuellog());
		
		List<IFTAReport> iftaReportList = new ArrayList<IFTAReport>();
		for (MileageLog aMileageLog : aggregateMileageLogList) {
			IFTAReport anIFTAReport = new IFTAReport();
			anIFTAReport.setCompanyName(aMileageLog.getCompany().getName());
			anIFTAReport.setUnitNum(aMileageLog.getUnitNum());
			anIFTAReport.setMiles(aMileageLog.getMiles());
			
			String key = aMileageLog.getUnitNum();
			FuelLog aFuelLog = aggreateFuelLogMap.get(key);
			if (aFuelLog != null) {
				anIFTAReport.setGallons(aFuelLog.getGallons());
				
				if (anIFTAReport.getMiles() != null && anIFTAReport.getGallons() != null) {
					Double mpg = anIFTAReport.getMiles()/anIFTAReport.getGallons();
					anIFTAReport.setMpg(mpg);
				}
				
				aggreateFuelLogMap.remove(key);
			}
			
			iftaReportList.add(anIFTAReport);
		}
		
		if (!mileageLogReportInput.isServiceTruck()
				&& StringUtils.isEmpty(mileageLogReportInput.getGps())) {
			Iterator<Map.Entry<String, FuelLog>> itr = aggreateFuelLogMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, FuelLog> pair = itr.next();
				FuelLog aFuelLog = pair.getValue();
				
				IFTAReport anIFTAReport = new IFTAReport();
				anIFTAReport.setCompanyName(aFuelLog.getVehicleCompany());
				anIFTAReport.setUnitNum(aFuelLog.getUnits());
				anIFTAReport.setGallons(aFuelLog.getGallons());
				
				iftaReportList.add(anIFTAReport);
			}
		}
		
		Collections.sort(iftaReportList, new Comparator<IFTAReport>() {
			@Override
         public int compare(IFTAReport lhs, IFTAReport rhs) {
         	Integer lhsUnitNumInt = new Integer(lhs.getUnitNum());
         	Integer rhsUnitNumInt = new Integer(rhs.getUnitNum());
				return (lhsUnitNumInt > rhsUnitNumInt) ? 1 : (lhsUnitNumInt < rhsUnitNumInt ) ? -1 : 0;
         }
     });
		
		IFTAReportWrapper anIFTAReportWrapper = new IFTAReportWrapper();
		anIFTAReportWrapper.setCompanies(mileageLogReportWrapper.getCompanies());
		anIFTAReportWrapper.setPeriodFrom(mileageLogReportWrapper.getPeriodFrom());
		anIFTAReportWrapper.setPeriodTo(mileageLogReportWrapper.getPeriodTo());
		anIFTAReportWrapper.setLastInStateFrom(mileageLogReportWrapper.getLastInStateFrom());
		anIFTAReportWrapper.setLastInStateTo(mileageLogReportWrapper.getLastInStateTo());
		anIFTAReportWrapper.setStates(mileageLogReportWrapper.getStates());
		anIFTAReportWrapper.setTotalMiles(mileageLogReportWrapper.getTotalMiles());
		anIFTAReportWrapper.setTotalRows(mileageLogReportWrapper.getTotalRows());
		anIFTAReportWrapper.setTotalGallons(fuelLogReportWrapper.getTotalGallons());
		anIFTAReportWrapper.setIftaReportList(iftaReportList);
		
		cleanMileageLogReportSearchCriteria(searchCriteria);
		
		return anIFTAReportWrapper;
	}
	
	private void cleanMileageLogReportSearchCriteria(SearchCriteria searchCriteria) {
		searchCriteria.getSearchMap().remove("transactionDateFrom");
		searchCriteria.getSearchMap().remove("transactionDateTo");
	}
	
	private void map(FuelLogReportInput fuelLogReportInput, SearchCriteria searchCriteria, IFTAReportInput iftaReportInput) {
		fuelLogReportInput.setCompany(iftaReportInput.getCompany());
		fuelLogReportInput.setState(iftaReportInput.getState());
		fuelLogReportInput.setUnit(iftaReportInput.getUnit());
		
		String periodFromStr = StringUtils.EMPTY;
		String periodToStr = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(iftaReportInput.getPeriodFrom()) && StringUtils.isNotEmpty(iftaReportInput.getPeriodTo())) {
			try {
				periodFromStr = ReportDateUtil.dateFormatter.format(mileageSearchDateFormat.parse(iftaReportInput.getPeriodFrom()));
				
				Date periodTo = mileageSearchDateFormat.parse(iftaReportInput.getPeriodTo());
				Calendar c = Calendar.getInstance();
				c.setTime(periodTo);
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
				periodTo = c.getTime();
				periodToStr = ReportDateUtil.dateFormatter.format(periodTo);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (StringUtils.isNotEmpty(iftaReportInput.getLastInStateFrom()) && StringUtils.isNotEmpty(iftaReportInput.getLastInStateTo())) {
			periodFromStr = iftaReportInput.getLastInStateFrom();
			periodToStr = iftaReportInput.getLastInStateTo();
		}
		
		if (StringUtils.isNotEmpty(periodFromStr) && StringUtils.isNotEmpty(periodToStr)) {
			fuelLogReportInput.setTransactionDateFrom(periodFromStr);
			fuelLogReportInput.setTransactionDateTo(periodToStr);
			searchCriteria.getSearchMap().put("transactionDateFrom", periodFromStr);
			searchCriteria.getSearchMap().put("transactionDateTo", periodToStr);
		}
	}

	private void map(MileageLogReportInput mileageLogReportInput, IFTAReportInput iftaReportInput) {
		mileageLogReportInput.setCompany(iftaReportInput.getCompany());
		mileageLogReportInput.setState(iftaReportInput.getState());
		mileageLogReportInput.setUnit(iftaReportInput.getUnit());
		
		mileageLogReportInput.setPeriodFrom(iftaReportInput.getPeriodFrom());
		mileageLogReportInput.setPeriodTo(iftaReportInput.getPeriodTo());
		
		mileageLogReportInput.setFirstInStateFrom(iftaReportInput.getFirstInStateFrom());
		mileageLogReportInput.setFirstInStateTo(iftaReportInput.getFirstInStateTo());
		mileageLogReportInput.setLastInStateFrom(iftaReportInput.getLastInStateFrom());
		mileageLogReportInput.setLastInStateTo(iftaReportInput.getLastInStateTo());
		
		mileageLogReportInput.setGps(iftaReportInput.getGps());
		
		if (StringUtils.equals(MileageLogReportInput.REPORT_TYPE_SERVICE_TRUCK_MPG, 
				iftaReportInput.getReportType())) {
			mileageLogReportInput.setServiceTruck(true);
		}
	}

	private Map<String, FuelLog> aggregateFuelLogByCompanyState(List<FuelLog> srcFuelLogList) {
		Map<String, FuelLog> aggreateFuelLogMap = new HashMap<String, FuelLog>();
		if (srcFuelLogList == null || srcFuelLogList.isEmpty()) {
			return aggreateFuelLogMap;
		}
		
		for (FuelLog aSrcFuelLog : srcFuelLogList) {
			if (StringUtils.isEmpty(aSrcFuelLog.getVehicleCompany()) || StringUtils.isEmpty(aSrcFuelLog.getStates())) {
				continue;
			}
			String key = aSrcFuelLog.getVehicleCompany() + "|" + aSrcFuelLog.getStates();
			
			FuelLog aggregateFuelLog = aggreateFuelLogMap.get(key);
			if (aggregateFuelLog == null) {
				aggreateFuelLogMap.put(key, aSrcFuelLog);
			} else {
				Double aggregateGallons = aggregateFuelLog.getGallons() + aSrcFuelLog.getGallons();
				aggregateFuelLog.setGallons(aggregateGallons);
			}
		}
		
		return aggreateFuelLogMap;
	}
	
	private Map<String, FuelLog> aggregateFuelLogByUnit(List<FuelLog> srcFuelLogList) {
		Map<String, FuelLog> aggreateFuelLogMap = new HashMap<String, FuelLog>();
		if (srcFuelLogList == null || srcFuelLogList.isEmpty()) {
			return aggreateFuelLogMap;
		}
		
		for (FuelLog aSrcFuelLog : srcFuelLogList) {
			if (StringUtils.isEmpty(aSrcFuelLog.getUnits())) {
				continue;
			}
			String key = aSrcFuelLog.getUnits();
			
			FuelLog aggregateFuelLog = aggreateFuelLogMap.get(key);
			if (aggregateFuelLog == null) {
				aggreateFuelLogMap.put(key, aSrcFuelLog);
			} else {
				Double aggregateGallons = aggregateFuelLog.getGallons() + aSrcFuelLog.getGallons();
				aggregateFuelLog.setGallons(aggregateGallons);
			}
		}
		
		return aggreateFuelLogMap;
	}
	
	private String retrieveOwnerOpSubConIds(String subcontractorIds, String companyIds) {
		if (StringUtils.isNotEmpty(subcontractorIds) && StringUtils.isEmpty(companyIds)) {
			return subcontractorIds;
		}
		
		String query = "select obj from SubContractor obj where obj.ownerOp='Yes'";
		if (StringUtils.isNotEmpty(companyIds)) {
			String companyCondn = " AND ( (obj.company1.id in (" + companyIds + "))" +
													 " OR (obj.company2.id in (" + companyIds + "))" +
													 " OR (obj.company3.id in (" + companyIds + "))" +
												")";
			query += companyCondn;
		}
		if (StringUtils.isNotEmpty(subcontractorIds)) {
			String subConCondn = " AND (obj.id in (" + subcontractorIds + "))";
			query += subConCondn;
		}
		query += " order by obj.name asc";
		
		List<SubContractor> subConList = genericDAO.executeSimpleQuery(query);
		if (subConList == null || subConList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String finalSubConIds = StringUtils.EMPTY;
		for (SubContractor aSubContractor : subConList) {
			finalSubConIds += String.valueOf(aSubContractor.getId()) + ",";
		}
		return finalSubConIds.substring(0, finalSubConIds.length()-1);
	}
	
	private String retrieveNoGPSVehicleIds(IFTAReportInput input) {
		/*String query = "select obj from NoGPSVehicle obj where 1=1";
		if (StringUtils.isNotEmpty(companyIds)) {
			String companyCondn = " AND (obj.vehicle.owner.id in (" + companyIds + "))";
			query += companyCondn;
		}
		query += " order by obj.vehicle.unitNum asc";
		
		List<NoGPSVehicle> noGPSList = genericDAO.executeSimpleQuery(query);
		if (noGPSList == null || noGPSList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String noGPSVehicleIds = StringUtils.EMPTY;
		for (NoGPSVehicle aNoGPS : noGPSList) {
			noGPSVehicleIds += String.valueOf(aNoGPS.getVehicle().getId()) + ",";
		}
		return noGPSVehicleIds.substring(0, noGPSVehicleIds.length()-1);*/
		
		String company = input.getCompany();
		String periodFrom = input.getPeriodFrom();
		String periodTo = input.getPeriodTo();
		String lastInStateFrom = ReportDateUtil.getToDate(input.getLastInStateFrom());
		String lastInStateTo = ReportDateUtil.getToDate(input.getLastInStateTo());
		
		StringBuffer query = new StringBuffer("select obj from MileageLog obj where 1=1");
		
		if (!StringUtils.isEmpty(company)){
			query.append(" and  obj.company in (" + company + ")");
		}
        
      if (!StringUtils.isEmpty(periodFrom)) {
        	try {
				query.append(" and obj.period >='"+mysqldf.format(mileageSearchDateFormat.parse(periodFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        
      if (!StringUtils.isEmpty(periodTo)){
        	try {
				query.append(" and obj.period <='"+mysqldf.format(mileageSearchDateFormat.parse(periodTo))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
      }
      
      if (StringUtils.isNotEmpty(lastInStateFrom) && StringUtils.isNotEmpty(lastInStateTo)) {
      	/*lastInStateFrom += " 00:00:00";
      	lastInStateTo += " 23:59:59";*/
        	
      	query.append(" and obj.lastInState >='"+lastInStateFrom+"'");
			query.append(" and obj.lastInState <='"+lastInStateTo+"'");
			
			/*query.append(" and  obj.lastInState between '" + lastInStateFrom
					+ "' and '" + lastInStateTo + "'");*/
		}
      
      String serviceTruckCondnOp = "!=";
      String modelServiceTruck = "Service Truck";
      query.append(" and obj.unit.model " + serviceTruckCondnOp + " '"+modelServiceTruck+"'");
		
      System.out.println("\nquery=mileageLog=>" + query + "\n");
		List<MileageLog> retrievedMileageLogList = genericDAO.executeSimpleQuery(query.toString());
		if (retrievedMileageLogList == null || retrievedMileageLogList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String gpsVehicleIds = StringUtils.EMPTY;
		for (MileageLog aMileageLog : retrievedMileageLogList) {
			//gpsVehicleIds += String.valueOf(aMileageLog.getUnit().getId()) + ",";
			gpsVehicleIds += aMileageLog.getUnitNum() + ",";
		}
		gpsVehicleIds = gpsVehicleIds.substring(0, gpsVehicleIds.length()-1);
		
		String vehicleQuery = "select obj from Vehicle obj where obj.type=1 and obj.activeStatus=1" 
	   		//+ " and obj.id not in ("+gpsVehicleIds+")"
	   		+ " and obj.unit not in ("+gpsVehicleIds+")"
	   		+ " and obj.unit != 0"
	   		+ " and obj.model " + serviceTruckCondnOp + " '"+modelServiceTruck+"'";
		List<Vehicle> noGpsVehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		if (noGpsVehicleList == null || noGpsVehicleList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String noGPSVehicleIds = StringUtils.EMPTY;
		for (Vehicle aVehicle : noGpsVehicleList) {
			noGPSVehicleIds += String.valueOf(aVehicle.getId()) + ",";
		}
		return noGPSVehicleIds.substring(0, noGPSVehicleIds.length()-1);
	}
	
	@Override
	public MileageLogReportWrapper generateOwnerOpSubConMileageLogData(SearchCriteria searchCriteria, MileageLogReportInput input) {
		String companyIds = input.getCompany();
		
		String subcontractorIds = input.getSubcontractor();
		subcontractorIds = retrieveOwnerOpSubConIds(subcontractorIds, companyIds);
		if (StringUtils.isEmpty(subcontractorIds)) {
			return null;
		}
		
		String periodFrom = input.getPeriodFrom();
		String periodTo = input.getPeriodTo();
		String lastInStateFrom = ReportDateUtil.getToDate(input.getLastInStateFrom());
		String lastInStateTo = ReportDateUtil.getToDate(input.getLastInStateTo());
		
		StringBuffer query = new StringBuffer("select obj from Ticket obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Ticket obj where 1=1");
		
		if (!StringUtils.isEmpty(companyIds)){
			query.append(" and obj.driverCompany.id in (" + companyIds + ")");
			countQuery.append(" and obj.driverCompany.id in (" + companyIds + ")");
		}
		
		if (!StringUtils.isEmpty(subcontractorIds)){
			query.append(" and obj.subcontractor.id in (" + subcontractorIds + ")");
			countQuery.append(" and  obj.subcontractor.id in (" + subcontractorIds + ")");
		}
		
      if (StringUtils.isNotEmpty(periodFrom) && StringUtils.isNotEmpty(periodTo)) {
        	try {
        		String periodFromDateStr = mysqldf.format(mileageSearchDateFormat.parse(periodFrom));
        		String periodToDateStr = mysqldf.format(determineMonthEndDate(mileageSearchDateFormat.parse(periodTo)));
				String dateCondn = " and ( (obj.unloadDate >='"+periodFromDateStr+"'"
									  + "        and obj.unloadDate <='"+periodToDateStr+"')";
				/*dateCondn +=       "    OR (obj.loadDate >='"+periodFromDateStr+"'"
							 +        "        and obj.loadDate <='"+periodToDateStr+"')"*/
				dateCondn		  +=     ")";
				query.append(dateCondn);
				countQuery.append(dateCondn);
        	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      if (StringUtils.isNotEmpty(lastInStateFrom) && StringUtils.isNotEmpty(lastInStateTo)) {
      	/*lastInStateFrom += " 00:00:00";
      	lastInStateTo += " 23:59:59";*/
      	
      	String dateCondn = " and ( (obj.unloadDate >='"+lastInStateFrom+"'"
      						  + "        and obj.unloadDate <='"+lastInStateTo+"')";
      	/*dateCondn +=       "    OR (obj.loadDate >='"+lastInStateFrom+"'"
      				 +        "        and obj.loadDate <='"+lastInStateTo+"')"*/
      	dateCondn        +=     ")";
      	query.append(dateCondn);
      	countQuery.append(dateCondn);
       
			/*query.append(" and  obj.unloadDate between '" + lastInStateFrom
					+ "' and '" + lastInStateTo + "'");
			countQuery.append(" and  obj.unloadDate between '" + lastInStateFrom
					+ "' and '" + lastInStateTo + "'");*/
		}
      
      /*query.append(" order by obj.subcontractor.name asc, obj.driverCompany.name asc, obj.unloadDate desc");
      countQuery.append(" order by obj.subcontractor.name asc, obj.driverCompany.name asc, obj.unloadDate desc");
     */
      
		System.out.println("\nquery=ownerOpSubConmileageLog=>" + query + "\n");
		Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();
		searchCriteria.setRecordCount(recordCount.intValue());
		System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");

		List<Ticket> ticketList = (List<Ticket>) genericDAO
			.getEntityManager()
			.createQuery(query.toString())
			.setMaxResults(searchCriteria.getPageSize())
			.setFirstResult(searchCriteria.getPage() * searchCriteria.getPageSize())
			.getResultList();
		
		MileageLogReportWrapper wrapper = new MileageLogReportWrapper();
		wrapper.setTotalRows(ticketList.size());
		wrapper.setCompanies(companyIds);
		wrapper.setPeriodFrom(periodFrom);
		wrapper.setPeriodTo(periodTo);
		wrapper.setLastInStateFrom(lastInStateFrom);
		wrapper.setLastInStateTo(lastInStateTo);
		
		Map<String, List<LocationDistance>> locationDistanceMap = retrieveAllLocationDistance();
		List<MileageLog> returnMileageLogList = new ArrayList<MileageLog>();
		double totalMiles = 0.0;
		for (Ticket aTicket : ticketList) {
			Double miles = retrieveLocationDistance(locationDistanceMap, aTicket);
			totalMiles += miles;
			
			MileageLog aReturnMileageLog = new MileageLog();
			map(aReturnMileageLog, aTicket, miles);
			returnMileageLogList.add(aReturnMileageLog);
		}
		wrapper.setTotalMiles(totalMiles);
		
		returnMileageLogList = aggregateMileageLogBySubConCompany(returnMileageLogList);
		
		wrapper.setMileageLogList(returnMileageLogList);
		return wrapper;
	}
	
	private List<Ticket> retrieveNoGPSVehicleTickets(IFTAReportInput input,
			String company, String state, String truck) {
		String companyIds = input.getCompany();
		
		String noGPSVehicleIds = retrieveNoGPSVehicleIds(input);
		if (StringUtils.isEmpty(noGPSVehicleIds)) {
			return null;
		}
		
		String periodFrom = input.getPeriodFrom();
		String periodTo = input.getPeriodTo();
		String unit = input.getUnit();
		String lastInStateFrom = ReportDateUtil.getToDate(input.getLastInStateFrom());
		String lastInStateTo = ReportDateUtil.getToDate(input.getLastInStateTo());
		
		StringBuffer query = new StringBuffer("select obj from Ticket obj where 1=1");
		query.append(" and obj.subcontractor is null");
		
		String companyToBeUsed = companyIds;
		if (!StringUtils.isEmpty(company)){
			companyToBeUsed = company;
		}
		if (!StringUtils.isEmpty(companyToBeUsed)) {
			query.append(" and obj.driverCompany.id in (" + companyToBeUsed + ")");
		}
		
		if (!StringUtils.isEmpty(noGPSVehicleIds)){
			query.append(" and obj.vehicle.id in (" + noGPSVehicleIds + ")");
		}
		
		String unitToBeUsed = unit;
		if (!StringUtils.isEmpty(truck)) {
			unitToBeUsed = truck;
		}
		if (!StringUtils.isEmpty(unitToBeUsed)){
			query.append(" and obj.vehicle.unit in (" + unitToBeUsed + ")");
		}
		
      if (StringUtils.isNotEmpty(periodFrom) && StringUtils.isNotEmpty(periodTo)) {
        	try {
        		String periodFromDateStr = mysqldf.format(mileageSearchDateFormat.parse(periodFrom));
        		String periodToDateStr = mysqldf.format(determineMonthEndDate(mileageSearchDateFormat.parse(periodTo)));
				String dateCondn = " and ( (obj.unloadDate >='"+periodFromDateStr+"'"
									  + "        and obj.unloadDate <='"+periodToDateStr+"')";
				/*dateCondn +=       "    OR (obj.loadDate >='"+periodFromDateStr+"'"
							 +        "        and obj.loadDate <='"+periodToDateStr+"')"*/
				dateCondn		  +=     ")";
				query.append(dateCondn);
        	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      if (StringUtils.isNotEmpty(lastInStateFrom) && StringUtils.isNotEmpty(lastInStateTo)) {
      	/*lastInStateFrom += " 00:00:00";
      	lastInStateTo += " 23:59:59";*/
      	
      	String dateCondn = " and ( (obj.unloadDate >='"+lastInStateFrom+"'"
      						  + "        and obj.unloadDate <='"+lastInStateTo+"')";
      	/*dateCondn +=       "    OR (obj.loadDate >='"+lastInStateFrom+"'"
      				 +        "        and obj.loadDate <='"+lastInStateTo+"')"*/
      	dateCondn        +=     ")";
      	query.append(dateCondn);
       
			/*query.append(" and  obj.unloadDate between '" + lastInStateFrom
					+ "' and '" + lastInStateTo + "'");*/
		}
      
      /*query.append(" order by obj.subcontractor.name asc, obj.driverCompany.name asc, obj.unloadDate desc");
      */
      
		System.out.println("\nquery=noGPSnmileageLog Ticket=>" + query + "\n");
		return genericDAO.executeSimpleQuery(query.toString());
	}
	
	@Override
	public IFTAReportWrapper generateNoGPSMileageLogMPGData(SearchCriteria searchCriteria, IFTAReportInput input) {
		input.setGps(IFTAReportInput.REPORT_FOR_NO_GPS);
		return generateMPGData(searchCriteria, input);
	}
	
	/*@Override
	public IFTAReportWrapper generateNoGPSMileageLogMPGData(SearchCriteria searchCriteria, IFTAReportInput input) {
		List<Ticket> ticketList = retrieveNoGPSVehicleTickets(input, null, null, null);
		if (ticketList == null || ticketList.isEmpty()) {
			return null;
		}
		
		Map<String, List<LocationDistance>> locationDistanceMap = retrieveAllLocationDistance();
		List<MileageLog> aggregateMileageLogList = new ArrayList<MileageLog>();
		double totalMiles = 0.0;
		for (Ticket aTicket : ticketList) {
			Double miles = retrieveLocationDistance(locationDistanceMap, aTicket);
			totalMiles += miles;
			
			MileageLog aReturnMileageLog = new MileageLog();
			map(aReturnMileageLog, aTicket, miles);
			aggregateMileageLogList.add(aReturnMileageLog);
		}
		
		aggregateMileageLogList = aggregateMileageLogByUnit(aggregateMileageLogList);
		
		FuelLogReportInput fuelLogReportInput = new FuelLogReportInput();
		map(fuelLogReportInput, searchCriteria, input);
		fuelLogReportInput.setReportType(FuelLogReportInput.REPORT_TYPE_FUEL_TRUCK);
		
		FuelLogReportWrapper fuelLogReportWrapper = generateFuellogData(searchCriteria, fuelLogReportInput, false);
		Map<String, FuelLog> aggreateFuelLogMap = aggregateFuelLogByUnit(fuelLogReportWrapper.getFuellog());
		
		List<IFTAReport> iftaReportList = new ArrayList<IFTAReport>();
		for (MileageLog aMileageLog : aggregateMileageLogList) {
			IFTAReport anIFTAReport = new IFTAReport();
			anIFTAReport.setCompanyName(aMileageLog.getCompany().getName());
			anIFTAReport.setUnitNum(aMileageLog.getUnitNum());
			anIFTAReport.setMiles(aMileageLog.getMiles());
			
			String key = aMileageLog.getUnitNum();
			FuelLog aFuelLog = aggreateFuelLogMap.get(key);
			if (aFuelLog != null) {
				anIFTAReport.setGallons(aFuelLog.getGallons());
				
				if (anIFTAReport.getMiles() != null && anIFTAReport.getGallons() != null) {
					Double mpg = anIFTAReport.getMiles()/anIFTAReport.getGallons();
					anIFTAReport.setMpg(mpg);
				}
				
				aggreateFuelLogMap.remove(key);
			}
			
			iftaReportList.add(anIFTAReport);
		}
		
		Collections.sort(iftaReportList, new Comparator<IFTAReport>() {
			@Override
         public int compare(IFTAReport lhs, IFTAReport rhs) {
         	Integer lhsUnitNumInt = new Integer(lhs.getUnitNum());
         	Integer rhsUnitNumInt = new Integer(rhs.getUnitNum());
				return (lhsUnitNumInt > rhsUnitNumInt) ? 1 : (lhsUnitNumInt < rhsUnitNumInt ) ? -1 : 0;
         }
      });
		
		IFTAReportWrapper anIFTAReportWrapper = new IFTAReportWrapper();
		anIFTAReportWrapper.setCompanies(input.getCompany());
		anIFTAReportWrapper.setPeriodFrom(input.getPeriodFrom());
		anIFTAReportWrapper.setPeriodTo(input.getPeriodTo());
		anIFTAReportWrapper.setLastInStateFrom(ReportDateUtil.getToDate(input.getLastInStateFrom()));
		anIFTAReportWrapper.setLastInStateTo( ReportDateUtil.getToDate(input.getLastInStateTo()));
		anIFTAReportWrapper.setTotalMiles(totalMiles);
		anIFTAReportWrapper.setTotalRows(ticketList.size());
		anIFTAReportWrapper.setTotalGallons(fuelLogReportWrapper.getTotalGallons());
		anIFTAReportWrapper.setIftaReportList(iftaReportList);
		
		cleanMileageLogReportSearchCriteria(searchCriteria);
		
		return anIFTAReportWrapper;
	}*/
	
	private Date determineMonthEndDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}
	
	private void map(MileageLog aMilageLog, Ticket aTicket, Double miles) {
		if (aTicket.getSubcontractor() != null) {
			aMilageLog.setSubcontractorStr(aTicket.getSubcontractor().getName());
		}
		
		Vehicle vehicle = aTicket.getVehicle();
		if (vehicle != null) {
			aMilageLog.setUnit(vehicle);
			aMilageLog.setUnitNum(String.valueOf(vehicle.getUnit().intValue()));
			aMilageLog.setVin(vehicle.getVinNumber());
		}
		
		aMilageLog.setCompany(aTicket.getDriverCompany());
		
		aMilageLog.setMiles(miles);
	}
	
	private Map<String, List<LocationDistance>> retrieveAllLocationDistance() {
		String query = "select obj from LocationDistance obj order by obj.origin.name asc, obj.destination.name asc";
		List<LocationDistance> locationDistanceList = genericDAO.executeSimpleQuery(query);
		
		Map<String, List<LocationDistance>> locationDistanceMap = new HashMap<String, List<LocationDistance>>();
		for (LocationDistance aLocationDistance : locationDistanceList) {
			String key = aLocationDistance.getOrigin().getId().longValue() + "|" 
						+ aLocationDistance.getDestination().getId().longValue();
			List<LocationDistance> groupedLocationDistanceList = locationDistanceMap.get(key);
			if (groupedLocationDistanceList == null) {
				groupedLocationDistanceList = new ArrayList<LocationDistance>();
				locationDistanceMap.put(key, groupedLocationDistanceList);
			}
			groupedLocationDistanceList.add(aLocationDistance);
		}
		return locationDistanceMap;
	}
	
	private Double retrieveLocationDistance(Map<String, List<LocationDistance>> locationDistanceMap, Ticket ticket) {
		Double zeroMiles = new Double(0.0);
		Location origin = ticket.getOrigin();
		Location destination = ticket.getDestination();
		if (origin == null || destination == null) {
			return zeroMiles;
		}
		
		String key = origin.getId().longValue() + "|" + destination.getId().longValue();
		List<LocationDistance> locationDistanceList = locationDistanceMap.get(key);
		if (locationDistanceList == null) {
			return zeroMiles;
		} else {
			Double miles = new Double(0.0);
			for (LocationDistance aLocationDistance : locationDistanceList) {
				miles += aLocationDistance.getMiles();
			}
			return miles;
		}
	}
	
	private List<LocationDistance> retrieveAllLocationDistance(Map<String, List<LocationDistance>> locationDistanceMap, Ticket ticket) {
		Location origin = ticket.getOrigin();
		Location destination = ticket.getDestination();
		if (origin == null || destination == null) {
			return null;
		}
		
		String key = origin.getId().longValue() + "|" + destination.getId().longValue();
		List<LocationDistance> locationDistanceList = locationDistanceMap.get(key);
		return locationDistanceList;
	}
	
	private Double retrieveLocationDistance(Ticket ticket) {
		Double zeroMiles = new Double(0.0);
		Location origin = ticket.getOrigin();
		Location destination = ticket.getDestination();
		if (origin == null || destination == null) {
			return zeroMiles;
		}
		
		String query = "select obj from LocationDistance obj where 1=1";
		query += " and obj.origin.id="+origin.getId();
		query += " and obj.destination.id="+destination.getId();
		List<LocationDistance> locationDistanceList = genericDAO.executeSimpleQuery(query);
		if (locationDistanceList == null || locationDistanceList.isEmpty()) {
			return zeroMiles;
		} else {
			return locationDistanceList.get(0).getMiles();
		}
	}
	
	@Override
	public MileageLogReportWrapper generateMileageLogData(SearchCriteria searchCriteria, MileageLogReportInput input) {
		boolean drilldown = false;
		
		String company = input.getCompany();
		String drillDownCompany = input.getDrillDownCompany();
		if (StringUtils.isNotEmpty(drillDownCompany)) {
			company = drillDownCompany;
			drilldown = true;
		}
		
		String state = input.getState();
		String drillDownState = input.getDrillDownState();
		if (StringUtils.isNotEmpty(drillDownState)) {
			state = drillDownState;
			drilldown = true;
		}
		
		String truck = input.getUnit();
		String drillDownTruck = input.getDrillDownUnit();
		if (StringUtils.isNotEmpty(drillDownTruck)) {
			truck = drillDownTruck;
			drilldown = true;
		}
		
		String periodFrom = input.getPeriodFrom();
		String periodTo = input.getPeriodTo();
		String lastInStateFrom = ReportDateUtil.getToDate(input.getLastInStateFrom());
		String lastInStateTo = ReportDateUtil.getToDate(input.getLastInStateTo());
		String gps = input.getGps();
		
		StringBuffer query = new StringBuffer("select obj from MileageLog obj  where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from MileageLog obj where 1=1");
		
		if (!StringUtils.isEmpty(company)){
			query.append(" and  obj.company in (" + company + ")");
			countQuery.append(" and  obj.company in (" + company + ")");
		}
		
		if (!StringUtils.isEmpty(state)){
			query.append(" and  obj.state in (" + state + ")");
			countQuery.append(" and  obj.state in (" + state + ")");
		}
		
		if (!StringUtils.isEmpty(truck)){
			query.append(" and  obj.unitNum in (" + truck + ")");
			countQuery.append(" and  obj.unitNum in (" + truck + ")");
		}
        
      if (!StringUtils.isEmpty(periodFrom)) {
        	try {
				query.append(" and obj.period >='"+mysqldf.format(mileageSearchDateFormat.parse(periodFrom))+"'");
				countQuery.append(" and obj.period >='"+mysqldf.format(mileageSearchDateFormat.parse(periodFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        
      if (!StringUtils.isEmpty(periodTo)){
        	try {
				query.append(" and obj.period <='"+mysqldf.format(mileageSearchDateFormat.parse(periodTo))+"'");
				countQuery.append(" and obj.period <='"+mysqldf.format(mileageSearchDateFormat.parse(periodTo))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
      }
      
      if (StringUtils.isNotEmpty(lastInStateFrom) && StringUtils.isNotEmpty(lastInStateTo)) {
      	/*lastInStateFrom += " 00:00:00";
      	lastInStateTo += " 23:59:59";*/
        	
      	query.append(" and obj.lastInState >='"+lastInStateFrom+"'");
			query.append(" and obj.lastInState <='"+lastInStateTo+"'");
			countQuery.append(" and obj.lastInState >='"+lastInStateFrom+"'");
			countQuery.append(" and obj.lastInState <='"+lastInStateTo+"'");
			
			/*query.append(" and  obj.lastInState between '" + lastInStateFrom
					+ "' and '" + lastInStateTo + "'");
			countQuery.append(" and  obj.lastInState between '" + lastInStateFrom
					+ "' and '" + lastInStateTo + "'");*/
		}
      
      if (StringUtils.isNotEmpty(gps)){
			query.append(" and obj.gps = '" + gps + "'");
			countQuery.append(" and obj.gps = '" + gps + "'");
		}
      
      // Mileage log service model change - 5th Jul 2017
      String serviceTruckCondnOp = "!=";
      if (input.isServiceTruck()) {
      	serviceTruckCondnOp = "=";
      }
      String modelServiceTruck = "Service Truck";
      query.append(" and obj.unit.model " + serviceTruckCondnOp + " '"+modelServiceTruck+"'");
		countQuery.append(" and obj.unit.model " + serviceTruckCondnOp + " '"+modelServiceTruck+"'");

      query.append(" order by obj.company.name asc, obj.state.name asc, obj.unitNum asc, obj.period desc");
      countQuery.append(" order by obj.period desc");
     
		System.out.println("\nquery=mileageLog=>" + query + "\n");
		Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();
		searchCriteria.setRecordCount(recordCount.intValue());
		System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");

		List<MileageLog> retrievedMileageLogList = (List<MileageLog>) genericDAO
			.getEntityManager()
			.createQuery(query.toString())
			.setMaxResults(searchCriteria.getPageSize())
			.setFirstResult(searchCriteria.getPage() * searchCriteria.getPageSize())
			.getResultList();
		
		MileageLogReportWrapper wrapper = new MileageLogReportWrapper();
		wrapper.setTotalRows(retrievedMileageLogList.size());
		wrapper.setCompanies(company);
		wrapper.setStates(state);
		wrapper.setPeriodFrom(periodFrom);
		wrapper.setPeriodTo(periodTo);
		wrapper.setLastInStateFrom(lastInStateFrom);
		wrapper.setLastInStateTo(lastInStateTo);
		
		List<MileageLog> returnMileageLogList = new ArrayList<MileageLog>();
		double totalMiles = 0.0;
		for (MileageLog aSrcMileageLog : retrievedMileageLogList) {
			totalMiles += aSrcMileageLog.getMiles();
			
			MileageLog aDestMileageLog = new MileageLog();
			copy(aSrcMileageLog, aDestMileageLog);
			returnMileageLogList.add(aDestMileageLog);
		}
		wrapper.setTotalMiles(totalMiles);
		
		/*if (!input.isServiceTruck()) {
			List<MileageLog> noGPSMileageLogList = generateNoGPSMileageLogData(searchCriteria, input, company, state, truck);
			if (noGPSMileageLogList != null && !noGPSMileageLogList.isEmpty()) {
				if (MileageLogReportInput.REPORT_TYPE_DETAILS.equals(input.getReportType())
						&& !drilldown) {
					noGPSMileageLogList = aggregateMileageLogByCompanyStateUnit(noGPSMileageLogList);
				}
				
				wrapper.setTotalRows(wrapper.getTotalRows() + noGPSMileageLogList.size());
				
				double noGPSTotalMiles = 0.0;
				for (MileageLog aNoGPSMileageLog : noGPSMileageLogList) {
					noGPSTotalMiles += aNoGPSMileageLog.getMiles();
				}
				wrapper.setTotalMiles(wrapper.getTotalMiles() + noGPSTotalMiles);
				
				returnMileageLogList.addAll(noGPSMileageLogList);
				sort(returnMileageLogList);
			}
		}*/
		
		if (MileageLogReportInput.REPORT_TYPE_TOTALS.equals(input.getReportType())) {
			returnMileageLogList = aggregateMileageLogByCompanyState(returnMileageLogList);
			// New GPS change - 1st July 2019
		} else if (MileageLogReportInput.REPORT_TYPE_DETAILS.equals(input.getReportType())
				&& !drilldown) {
			returnMileageLogList = aggregateMileageLogByCompanyStateUnit(returnMileageLogList);
		}
		
		wrapper.setMileageLogList(returnMileageLogList);
		return wrapper;
	}
	
	private void sort(List<MileageLog> mileageLogList) {
		if (mileageLogList == null || mileageLogList.isEmpty()) {
			return;
		}
		
		Comparator<MileageLog> companyComparator = new Comparator<MileageLog>() {
			@Override
			public int compare(MileageLog o1, MileageLog o2) {
				if (o1.getCompany() == null || o2.getCompany() == null) {
					return 0;
				}
				return o1.getCompany().getName().compareTo(o2.getCompany().getName());
			}
		};
		
		Comparator<MileageLog> stateComparator = new Comparator<MileageLog>() {
			@Override
			public int compare(MileageLog o1, MileageLog o2) {
				if (o1.getState() == null || o2.getState() == null) {
					return 0;
				}
				return o1.getState().getCode().compareTo(o2.getState().getCode());
			}
		};
		
		Comparator<MileageLog> unitComparator = new Comparator<MileageLog>() {
			@Override
			public int compare(MileageLog o1, MileageLog o2) {
				if (StringUtils.isEmpty(o1.getUnitNum()) || StringUtils.isEmpty(o2.getUnitNum())) {
					return 0;
				}
				return o1.getUnitNum().compareTo(o2.getUnitNum());
			}
		};
	        
		Comparator<MileageLog> periodComparator = new Comparator<MileageLog>() {
			@Override
			public int compare(MileageLog o1, MileageLog o2) {
				if (o1.getPeriod() == null || o2.getPeriod() == null) {
					return 0;
				}
				return o2.getPeriod().compareTo(o1.getPeriod());
			}
		};  
		
		ComparatorChain chain = new ComparatorChain(); 
		chain.addComparator(companyComparator);
		chain.addComparator(stateComparator);			
		chain.addComparator(unitComparator);
		chain.addComparator(periodComparator);
		
		Collections.sort(mileageLogList, chain);
	}
	
	private void map(IFTAReportInput iftaReportInput, MileageLogReportInput mileageLogReportInput) {
		iftaReportInput.setCompany(mileageLogReportInput.getCompany());
		iftaReportInput.setState(mileageLogReportInput.getState());
		iftaReportInput.setUnit(mileageLogReportInput.getUnit());
		
		iftaReportInput.setPeriodFrom(mileageLogReportInput.getPeriodFrom());
		iftaReportInput.setPeriodTo(mileageLogReportInput.getPeriodTo());
		
		iftaReportInput.setReportType(mileageLogReportInput.getReportType());
		
		iftaReportInput.setFirstInStateFrom(mileageLogReportInput.getFirstInStateFrom());
		iftaReportInput.setFirstInStateTo(mileageLogReportInput.getFirstInStateTo());
		iftaReportInput.setLastInStateFrom(mileageLogReportInput.getLastInStateFrom());
		iftaReportInput.setLastInStateTo(mileageLogReportInput.getLastInStateTo());
	}
	
	private Date[] deduceDatesToBeUsed(IFTAReportInput iftaReportInput) {
		String periodFrom = iftaReportInput.getPeriodFrom();
		String periodTo = iftaReportInput.getPeriodTo();
		String lastInStateFrom = ReportDateUtil.getToDate(iftaReportInput.getLastInStateFrom());
		String lastInStateTo = ReportDateUtil.getToDate(iftaReportInput.getLastInStateTo());
		
		Date startDate = null;
		Date endDate = null;
		
		if (StringUtils.isNotEmpty(periodFrom) && StringUtils.isNotEmpty(periodTo)) {
        try {
      	  startDate = mileageSearchDateFormat.parse(periodFrom);
      	  endDate = determineMonthEndDate(mileageSearchDateFormat.parse(periodTo));
        } catch (ParseException e) {
				e.printStackTrace();
			}
		}
	      
      if (StringUtils.isNotEmpty(lastInStateFrom) && StringUtils.isNotEmpty(lastInStateTo)) {
      	/*lastInStateFrom += " 00:00:00";
      	lastInStateTo += " 23:59:59";*/
      	try {
				startDate = mysqldf.parse(lastInStateFrom);
				endDate = mysqldf.parse(lastInStateTo);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} 
      
      Date[] datesToBeUsed = new Date[2];
      datesToBeUsed[0] = startDate;
      datesToBeUsed[1] = endDate;
      return datesToBeUsed;
	}
	
	@Override
	public List<MileageLog> generateNoGPSMileageLogData(SearchCriteria searchCriteria, MileageLogReportInput input,
			String company, String state, String truck, List<String> errorList) {
		IFTAReportInput iftaReportInput = new IFTAReportInput();
		map(iftaReportInput, input);
		List<Ticket> ticketList = retrieveNoGPSVehicleTickets(iftaReportInput, company, state, truck);
		if (ticketList == null || ticketList.isEmpty()) {
			return null;
		}
		
		Date[] datesToBeUsed = deduceDatesToBeUsed(iftaReportInput);
		Map<String, List<VehiclePermit>> vehiclePermitMap = retrieveVehiclePermitCache(ticketList, datesToBeUsed);
		
		Map<String, State> stateMap = retrieveStateMap();
		
		State searchStateObj = null;
		if (StringUtils.isNotEmpty(state)) {
			searchStateObj = genericDAO.getById(State.class, Long.valueOf(state));
		}
		
		Map<String, List<LocationDistance>> locationDistanceMap = retrieveAllLocationDistance();
		
		List<String> errorUnits = new ArrayList<String>();
		List<MileageLog> aggregateMileageLogList = new ArrayList<MileageLog>();
		for (Ticket aTicket : ticketList) {
			Vehicle vehicle = aTicket.getVehicle();
			String unitNum = StringUtils.EMPTY;
			if (vehicle != null) {
				unitNum = String.valueOf(vehicle.getUnit().intValue());
			}
			if (errorUnits.contains(unitNum)) {
				continue;
			}
			
			List<LocationDistance> locationDistanceList = retrieveAllLocationDistance(locationDistanceMap, aTicket);
			if (locationDistanceList == null || locationDistanceList.isEmpty()) {
				Location origin = aTicket.getOrigin();
				Location destination = aTicket.getDestination();
				StringBuffer locationPairBuff = new StringBuffer();
				if (origin != null) {
					locationPairBuff.append(origin.getName());
				}
				if (destination == null) {
					locationPairBuff.append(" and " + destination.getName());
				}
				
				errorList.add("Could not calulate miles for unit: " + unitNum 
						+ " as following location pair not added in the system: " + locationPairBuff.toString());
				if (StringUtils.isNotEmpty(unitNum)) {
					errorUnits.add(unitNum);
				}
				
				continue;
			}
			
			mapAndAdd(aggregateMileageLogList, locationDistanceList.get(0), aTicket, searchStateObj, 
					vehiclePermitMap, stateMap);
		}
		
		return aggregateMileageLogList;
	}
	
	private Map<String, List<VehiclePermit>> retrieveVehiclePermitCache(List<Ticket> ticketList, Date[] datesToBeUsed) {
		Map<String, List<VehiclePermit>> aggreateVehiclePermitMap = new HashMap<String, List<VehiclePermit>>();
		if (ticketList == null || ticketList.isEmpty() || datesToBeUsed == null 
				|| datesToBeUsed[0] == null || datesToBeUsed[1] == null) {
			return aggreateVehiclePermitMap;
		}
		
		String unitNums = StringUtils.EMPTY;
		for (Ticket aTicket : ticketList) {
			unitNums += "'" + aTicket.getVehicle().getUnitNum() + "'" + ",";
		}
		unitNums = unitNums.substring(0, unitNums.length()-1);
		
		List<VehiclePermit> vehiclePermitList = retrieveVehiclePermit(unitNums, datesToBeUsed);
		Map<String, List<VehiclePermit>> aggregateVehiclePermitMap = aggregateVehiclePermitByUnit(vehiclePermitList);
		return aggregateVehiclePermitMap;
	}
	
	private Map<String, List<VehiclePermit>> aggregateVehiclePermitByUnit(List<VehiclePermit> vehiclePermitList) {
		Map<String, List<VehiclePermit>> aggreateVehiclePermitMap = new HashMap<String, List<VehiclePermit>>();
		if (vehiclePermitList == null || vehiclePermitList.isEmpty()) {
			return aggreateVehiclePermitMap;
		}
		
		for (VehiclePermit aVehiclePermit : vehiclePermitList) {
			String key = aVehiclePermit.getVehicle().getUnitNum();
			List<VehiclePermit> aggreateVehiclePermitList = aggreateVehiclePermitMap.get(key);
			if (aggreateVehiclePermitList == null) {
				aggreateVehiclePermitList = new ArrayList<VehiclePermit>();
				aggreateVehiclePermitMap.put(key, aggreateVehiclePermitList);
			}
			aggreateVehiclePermitList.add(aVehiclePermit);
		}
		
		return aggreateVehiclePermitMap;
	}
	
	private List<VehiclePermit> retrieveVehiclePermit(String unitNums, Date[] datesToBeUsed) {
		if (StringUtils.isEmpty(unitNums) || datesToBeUsed == null 
				|| datesToBeUsed[0] == null || datesToBeUsed[1] == null) {
			return null;
		}
		
		String startDateStr = mysqldf.format(datesToBeUsed[0]);
		String endDateStr = mysqldf.format(datesToBeUsed[1]);
		
		String query = "select obj from VehiclePermit obj where obj.status=1"
				+ " and obj.issueDate <= '" + startDateStr + "'"
				+ " and obj.expirationDate > '" + endDateStr + "'"
				+ " and obj.vehicle.unitNum in (" + unitNums  + ")"
				+ " order by obj.id desc";
		List<VehiclePermit> permits = genericDAO.executeSimpleQuery(query);
		return permits;
	}
	
	private VehiclePermit retrieveVehiclePermit(Map<String, List<VehiclePermit>> aggregateVehiclePermitMap, Ticket aTicket) {
		if (aTicket.getVehicle() == null) {
			return null;
		}
		
		String key = aTicket.getVehicle().getUnitNum();
		List<VehiclePermit> vehiclePermitList = aggregateVehiclePermitMap.get(key);
		if (vehiclePermitList == null || vehiclePermitList.isEmpty()) {
			return null;
		} else {
			return vehiclePermitList.get(0);
		}
	}
	
	private VehiclePermit retrieveVehiclePermit(Ticket aTicket) {
		if (aTicket.getVehicle() == null || aTicket.getUnloadDate() == null) {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(aTicket.getUnloadDate());
		
		String query = "select obj from VehiclePermit obj where obj.status=1"
				+ " and obj.issueDate <= '" + dateStr + "'"
				+ " and obj.expirationDate > '" + dateStr + "'"
				+ " and obj.vehicle.unitNum = " + aTicket.getVehicle().getUnitNum();
		List<VehiclePermit> permits = genericDAO.executeSimpleQuery(query);
		if (permits != null && !permits.isEmpty()) {
			return permits.get(0);
		} else {
			return null;
		}
	}
	
	private void mapAndAdd(List<MileageLog> mileageLogList, LocationDistance aLocationDistance, Ticket aTicket,
			State searchStateObj, Map<String, List<VehiclePermit>> vehiclePermitMap, Map<String, State> stateMap) {
		VehiclePermit permit = retrieveVehiclePermit(vehiclePermitMap, aTicket);
		
		Date period = aTicket.getUnloadDate();
		
		Double nyMiles = aLocationDistance.getNyMiles();
		mapAndAdd(mileageLogList, aTicket, nyMiles, "NY", permit, period, searchStateObj, stateMap);
		
		Double njMiles = aLocationDistance.getNjMiles();
		mapAndAdd(mileageLogList, aTicket, njMiles, "NJ", permit, period, searchStateObj, stateMap);
		
		Double paMiles = aLocationDistance.getPaMiles();
		mapAndAdd(mileageLogList, aTicket, paMiles, "PA", permit, period, searchStateObj, stateMap);
	
		Double mdMiles = aLocationDistance.getMdMiles();
		mapAndAdd(mileageLogList, aTicket, mdMiles, "MD", permit, period, searchStateObj, stateMap);
		
		Double vaMiles = aLocationDistance.getVaMiles();
		mapAndAdd(mileageLogList, aTicket, vaMiles, "VA", permit, period, searchStateObj, stateMap);
		
		Double deMiles = aLocationDistance.getDeMiles();
		mapAndAdd(mileageLogList, aTicket, deMiles, "DE", permit, period, searchStateObj, stateMap);
		
		Double wvMiles = aLocationDistance.getWvMiles();
		mapAndAdd(mileageLogList, aTicket, wvMiles, "WV", permit, period, searchStateObj, stateMap);
		
		Double dcMiles = aLocationDistance.getDcMiles();
		mapAndAdd(mileageLogList, aTicket, dcMiles, "DC", permit, period, searchStateObj, stateMap);
		
		Double ilMiles = aLocationDistance.getIlMiles();
		mapAndAdd(mileageLogList, aTicket, ilMiles, "IL", permit, period, searchStateObj, stateMap);
		
		Double flMiles = aLocationDistance.getFlMiles();
		mapAndAdd(mileageLogList, aTicket, flMiles, "FL", permit, period, searchStateObj, stateMap);
		
		Double ctMiles = aLocationDistance.getCtMiles();
		mapAndAdd(mileageLogList, aTicket, ctMiles, "CT", permit, period, searchStateObj, stateMap);
		
		Double scMiles = aLocationDistance.getScMiles();
		mapAndAdd(mileageLogList, aTicket, scMiles, "SC", permit, period, searchStateObj, stateMap);
	}
	
	private void mapAndAdd(List<MileageLog> mileageLogList, Ticket aTicket, Double miles, String state, 
			VehiclePermit permit, Date period, State searchStateObj, Map<String, State> stateMap) {
		if (miles == null || miles.doubleValue() == 0.0) {
			return;
		}
		
		if (searchStateObj != null) {
			if (!StringUtils.equals(state, searchStateObj.getCode())) {
				return;
			}
		}
		
		String permitNumber = StringUtils.EMPTY;
		if (permit != null) {
			permitNumber = permit.getPermitNumber();
		}
		
		MileageLog aReturnMileageLog = new MileageLog();
		map(aReturnMileageLog, aTicket, miles);
		aReturnMileageLog.setState(stateMap.get(state));
		aReturnMileageLog.setVehiclePermit(permit);
		aReturnMileageLog.setVehiclePermitNumber(permitNumber);
		
		aReturnMileageLog.setPeriod(period);
		String periodStr = StringUtils.EMPTY;
		if (period != null) {
			periodStr = mileageReportDateFormat.format(period);
		}
		aReturnMileageLog.setPeriodStr(periodStr);
		
		aReturnMileageLog.setFirstInState(period);
		aReturnMileageLog.setLastInState(period);
		
		mileageLogList.add(aReturnMileageLog);
	}
	
	private Map<String, State> retrieveStateMap() {
		String query = "select obj from State obj";
		List<State> stateList = genericDAO.executeSimpleQuery(query);
		
		Map<String, State> stateMap = new HashMap<String, State>();
		for (State aState : stateList) {
			String key = aState.getCode();
			stateMap.put(key, aState);
		}
		return stateMap;
	}
	
	private State retrieveState(String code) {
		String query = "select obj from State obj "
				+ " where obj.code='" + code + "'";
		List<State> stateList = genericDAO.executeSimpleQuery(query);
		return stateList.get(0);
	}
	
	private void copy(MileageLog srcMileageLog, MileageLog destMileageLog) {
		destMileageLog.setCompany(srcMileageLog.getCompany());
		destMileageLog.setState(srcMileageLog.getState());
		destMileageLog.setMiles(srcMileageLog.getMiles());
		destMileageLog.setPeriod(srcMileageLog.getPeriod());
		destMileageLog.setUnit(srcMileageLog.getUnit());
		destMileageLog.setUnitNum(srcMileageLog.getUnitNum());
		destMileageLog.setVin(srcMileageLog.getVin());
		destMileageLog.setVehiclePermit(srcMileageLog.getVehiclePermit());
		destMileageLog.setVehiclePermitNumber(srcMileageLog.getVehiclePermitNumber());
		destMileageLog.setGroups(srcMileageLog.getGroups());
		destMileageLog.setFirstInState(srcMileageLog.getFirstInState());
		destMileageLog.setLastInState(srcMileageLog.getLastInState());
		
		String periodStr = StringUtils.EMPTY;
		if (srcMileageLog.getPeriod() != null) {
			periodStr = mileageReportDateFormat.format(srcMileageLog.getPeriod());
		}
		destMileageLog.setPeriodStr(periodStr);
	}
	
	private List<MileageLog> aggregateMileageLogBySubConCompany(List<MileageLog> srcMileageLogList) {
		List<MileageLog> aggreateMileageLogList = new ArrayList<MileageLog>();
		if (srcMileageLogList == null || srcMileageLogList.isEmpty()) {
			return aggreateMileageLogList;
		}
		
		Map<String, MileageLog> aggreateMileageLogMap = new HashMap<String, MileageLog>();
		for (MileageLog aSrcMileageLog : srcMileageLogList) {
			String key = aSrcMileageLog.getSubcontractorStr() + "|" + aSrcMileageLog.getCompany().getName();
			
			MileageLog aggregateMileageLog = aggreateMileageLogMap.get(key);
			if (aggregateMileageLog == null) {
				aggreateMileageLogMap.put(key, aSrcMileageLog);
			} else {
				Double aggregateMiles = aggregateMileageLog.getMiles() + aSrcMileageLog.getMiles();
				aggregateMileageLog.setMiles(aggregateMiles);
			}
		}
		
		SortedSet<String> sortedKeys = new TreeSet<String>(aggreateMileageLogMap.keySet());
		for (String aKey : sortedKeys) {
			aggreateMileageLogList.add(aggreateMileageLogMap.get(aKey));
		}
		
		return aggreateMileageLogList;
	}
	
	private List<MileageLog> aggregateMileageLogByCompanyState(List<MileageLog> srcMileageLogList) {
		List<MileageLog> aggreateMileageLogList = new ArrayList<MileageLog>();
		if (srcMileageLogList == null || srcMileageLogList.isEmpty()) {
			return aggreateMileageLogList;
		}
		
		Map<String, MileageLog> aggreateMileageLogMap = new HashMap<String, MileageLog>();
		for (MileageLog aSrcMileageLog : srcMileageLogList) {
			String key = aSrcMileageLog.getCompany().getName() + "|" + aSrcMileageLog.getState().getName();
			
			MileageLog aggregateMileageLog = aggreateMileageLogMap.get(key);
			if (aggregateMileageLog == null) {
				aggreateMileageLogMap.put(key, aSrcMileageLog);
			} else {
				Double aggregateMiles = aggregateMileageLog.getMiles() + aSrcMileageLog.getMiles();
				aggregateMileageLog.setMiles(aggregateMiles);
			}
		}
		
		SortedSet<String> sortedKeys = new TreeSet<String>(aggreateMileageLogMap.keySet());
		for (String aKey : sortedKeys) {
			aggreateMileageLogList.add(aggreateMileageLogMap.get(aKey));
		}
		
		return aggreateMileageLogList;
	}
	
	private List<MileageLog> aggregateMileageLogByCompanyStateUnit(List<MileageLog> srcMileageLogList) {
		List<MileageLog> aggreateMileageLogList = new ArrayList<MileageLog>();
		if (srcMileageLogList == null || srcMileageLogList.isEmpty()) {
			return aggreateMileageLogList;
		}
		
		Map<String, MileageLog> aggreateMileageLogMap = new HashMap<String, MileageLog>();
		for (MileageLog aSrcMileageLog : srcMileageLogList) {
			String key = aSrcMileageLog.getCompany().getName() + "|" + aSrcMileageLog.getState().getName()
					+ "|" + aSrcMileageLog.getUnitNum();
			
			MileageLog aggregateMileageLog = aggreateMileageLogMap.get(key);
			if (aggregateMileageLog == null) {
				aggreateMileageLogMap.put(key, aSrcMileageLog);
			} else {
				Double aggregateMiles = aggregateMileageLog.getMiles() + aSrcMileageLog.getMiles();
				aggregateMileageLog.setMiles(aggregateMiles);
			}
		}
		
		SortedSet<String> sortedKeys = new TreeSet<String>(aggreateMileageLogMap.keySet());
		for (String aKey : sortedKeys) {
			aggreateMileageLogList.add(aggreateMileageLogMap.get(aKey));
		}
		
		return aggreateMileageLogList;
	}
	
	private List<MileageLog> aggregateMileageLogByUnit(List<MileageLog> srcMileageLogList) {
		List<MileageLog> aggreateMileageLogList = new ArrayList<MileageLog>();
		if (srcMileageLogList == null || srcMileageLogList.isEmpty()) {
			return aggreateMileageLogList;
		}
		
		Map<String, MileageLog> aggreateMileageLogMap = new HashMap<String, MileageLog>();
		for (MileageLog aSrcMileageLog : srcMileageLogList) {
			String key = aSrcMileageLog.getUnitNum();
			
			MileageLog aggregateMileageLog = aggreateMileageLogMap.get(key);
			if (aggregateMileageLog == null) {
				aggreateMileageLogMap.put(key, aSrcMileageLog);
			} else {
				Double aggregateMiles = aggregateMileageLog.getMiles() + aSrcMileageLog.getMiles();
				aggregateMileageLog.setMiles(aggregateMiles);
			}
		}
		
		SortedSet<String> sortedKeys = new TreeSet<String>(aggreateMileageLogMap.keySet());
		for (String aKey : sortedKeys) {
			aggreateMileageLogList.add(aggreateMileageLogMap.get(aKey));
		}
		
		return aggreateMileageLogList;
	}
	
	@Override
	public FuelLogReportWrapper generateFuellogData(
			SearchCriteria searchCriteria, FuelLogReportInput input, boolean distributionData) {
		Map<String, String> params = new HashMap<String, String>();
		// String fuelVendor = (String)
		// searchCriteria.getSearchMap().get("fuelVendor");
		String fuelVendor = input.getFuelVendor();
		// Fuel log - subcontractor
		String subContractor = input.getSubContractor();
		String fromInvoiceDate1 = (String) searchCriteria.getSearchMap().get(
				"fromInvoiceDate");
		String invoiceDateTo1 = (String) searchCriteria.getSearchMap().get(
				"invoiceDateTo");
		String transactionDateFrom1 = (String) searchCriteria.getSearchMap()
				.get("transactionDateFrom");
		String transactionDateTo1 = (String) searchCriteria.getSearchMap().get(
				"transactionDateTo");

		String fromInvoiceDate = ReportDateUtil.getFromDate(input
				.getFromInvoiceDate());
		String invoiceDateTo = ReportDateUtil.getToDate(input
				.getInvoiceDateTo());
		String transactionDateFrom = ReportDateUtil.getFromDate(input
				.getTransactionDateFrom());
		String transactionDateTo = ReportDateUtil.getToDate(input
				.getTransactionDateTo());

		String cardno = (String) searchCriteria.getSearchMap().get("cardno");

		/*
		 * String fromInvoiceNo = (String)
		 * searchCriteria.getSearchMap().get("fromInvoiceNo"); String
		 * InvoiceNoTo = (String)
		 * searchCriteria.getSearchMap().get("InvoiceNoTo");
		 */
		String fueltype = (String) searchCriteria.getSearchMap()
				.get("fueltype");
		String fueltype1 = input.getFueltype();
		// System.out.println("\nfueltype===>"+fueltype+"\n");
		// System.out.println("\nfueltype1===>"+fueltype1+"\n");

		String terminal = input.getTerminal();
		String driver = input.getDriver();
		String truck = input.getUnit();
		String state = input.getState();

		String company = input.getCompany();
		params.put("company", company);

		String gallansfrom = input.getGallonsFrom();
		String gallansto = input.getGallonsTo();

		String unitpricefrom = input.getUnitPriceFrom();
		String unitpriceto = input.getUnitPriceTo();

		String feesfrom = input.getFeesFrom();
		String feesto = input.getFeesTo();

		String discountfrom = input.getDiscountFrom();
		String discountto = input.getDiscountTo();

		String amountfrom = input.getAmountFrom();
		String amountto = input.getAmountTo();

		String fromInvoiceNo = input.getFromInvoiceNo();
		String InvoiceNoTo = input.getInvoiceNoTo();
		// String fueltype = input.getFueltype();

		StringBuffer query = new StringBuffer("");
		StringBuffer countQuery = new StringBuffer("");
		query.append("select obj from FuelLog obj where 1=1 ");
		countQuery.append("select count(obj) from FuelLog obj where 1=1");

		if (!StringUtils.isEmpty(fuelVendor)) {
			/* query.append("and  obj.fuelvendor = '" + fuelVendor + "'"); */

			query.append(" and  obj.fuelvendor in (" + fuelVendor + ")");
			countQuery.append(" and  obj.fuelvendor in (" + fuelVendor + ")");
		}
		
		// Fuel log - subcontractor
		if (!StringUtils.isEmpty(subContractor)) {
			query.append(" and  obj.subContractor in (" + subContractor + ")");
			countQuery.append(" and  obj.subContractor in (" + subContractor + ")");
		}

		if (!StringUtils.isEmpty(company)) {
			// Fuel log - truck report - 3rd Jun 2016
			if (FuelLogReportInput.REPORT_TYPE_FUEL_TRUCK.equals(input.getReportType())) {
				if (StringUtils.isEmpty(truck)) {	
					String truckIds = retrieveTruckIds(company);
					if (StringUtils.isNotEmpty(truckIds)) {
						query.append(" and  obj.unit in (" + truckIds + ")");
						countQuery.append(" and  obj.unit in (" + truckIds + ")");
					}
				}
			} else {
				query.append(" and  obj.company in (" + company + ")");
				countQuery.append(" and  obj.company in (" + company + ")");
			}
		}
		
		if (!StringUtils.isEmpty(terminal)) {
			query.append(" and  obj.terminal in (" + terminal + ")");
			countQuery.append(" and  obj.terminal in (" + terminal + ")");
		}

		if (!StringUtils.isEmpty(fromInvoiceDate1)
				&& !StringUtils.isEmpty(invoiceDateTo1)) {
			if (!StringUtils.isEmpty(fromInvoiceDate)
					&& !StringUtils.isEmpty(invoiceDateTo)) {
				query.append(" and  obj.invoiceDate between '" + fromInvoiceDate
						+ "' and '" + invoiceDateTo + "'");
				countQuery.append(" and  obj.invoiceDate between '" + fromInvoiceDate
						+ "' and '" + invoiceDateTo + "'");
				// query.append("and  obj.invoiceDate between '" +
				// fromInvoiceDate + "' and '" + invoiceDateTo +
				// "'or obj.invoiceDate IS null ");
			}
		}
		/*
		 * else{ query.append("and  obj.invoiceDate between '" + fromInvoiceDate
		 * + "' and '" + invoiceDateTo + "'or obj.invoiceDate IS null "); }
		 */

		if (!StringUtils.isEmpty(transactionDateFrom1)
				&& !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom)
					&& !StringUtils.isEmpty(transactionDateTo)) {
				query.append(" and  obj.transactiondate >= '"
						+ transactionDateFrom + "' and obj.transactiondate <='" + transactionDateTo
						+ "'");
				countQuery.append(" and  obj.transactiondate >= '"
						+ transactionDateFrom + "' and obj.transactiondate <='" + transactionDateTo
						+ "'");
			}
		}
		/*
		 * else{ query.append("and  obj.transactiondate between '" +
		 * transactionDateFrom + "' and '" + transactionDateTo +
		 * "' or obj.transactiondate IS null "); }
		 */

		if (!StringUtils.isEmpty(cardno)) {
			/* query.append("and  obj.fuelCardNumber = '" + cardno + "'"); */
			query.append(" and  obj.fuelcard.fuelcardNum like '%" + cardno
					+ "%'");
			countQuery.append(" and  obj.fuelcard.fuelcardNum like '%" + cardno+ "%'");
		}

		if (!StringUtils.isEmpty(driver)) {
			String drivers="";
			String  driverIds = "";
			String[] driverSplitEntry = driver.split(",");
			// query.append("and  obj.driverFname in ("+ driver + ")");
			for(int i=0;i<driverSplitEntry.length;i++){
				if(drivers.equals("")){
					drivers = "'"+driverSplitEntry[i]+"'";
				}
				else{
					drivers = drivers+","+"'"+driverSplitEntry[i]+"'";
				}
			}
			
			List<Driver> driverIdList = genericDAO.executeSimpleQuery("Select obj from Driver obj where obj.fullName in ("+drivers+")");
			
			for(Driver driverObj:driverIdList){
				if(driverIds.equals("")){
					driverIds = driverObj.getId().toString();
				}
				else{
					driverIds = driverIds+","+driverObj.getId().toString();
				}
			}
			
			query.append(" and  obj.driversid in (" + driverIds + ")");
			countQuery.append(" and  obj.driversid in (" + driverIds + ")");
		}
		if (!StringUtils.isEmpty(truck)) {	
			String truckIds="";			
				   
				   String vehiclequery="select obj from Vehicle obj where obj.type=1" 
				   		+" and obj.unit in ("
						+truck
						+")";
					
					System.out.println("******** the truck query is "+vehiclequery);
					
					List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
					
					if(vehicleLists!=null && vehicleLists.size()>0){						
						for(Vehicle vehicleObj : vehicleLists) {						
							if(StringUtils.isEmpty(truckIds)){	
							truckIds=String.valueOf(vehicleObj.getId());
							}
							else{
								truckIds=truckIds+","+String.valueOf(vehicleObj.getId());
							}
						}
					}			
		
			
			
			query.append(" and  obj.unit in (" + truckIds + ")");
			countQuery.append(" and  obj.unit in (" + truckIds + ")");
		}
		if (!StringUtils.isEmpty(state)) {
			query.append(" and  obj.state in (" + state + ")");
			countQuery.append( "and  obj.state in (" + state + ")");
		}

		if (!StringUtils.isEmpty(gallansfrom)) {
			query.append(" and  obj.gallons>=").append(gallansfrom);
			countQuery.append(" and  obj.gallons>=").append(gallansfrom);
		}
		if (!StringUtils.isEmpty(gallansto)) {
			query.append(" and  obj.gallons<='").append(gallansto + "'");
			countQuery.append(" and  obj.gallons<='").append(gallansto + "'");
		}

		if (!StringUtils.isEmpty(unitpricefrom)) {
			query.append(" and  obj.unitprice>=").append(unitpricefrom);
			countQuery.append(" and  obj.unitprice>=").append(unitpricefrom);
		}
		if (!StringUtils.isEmpty(unitpriceto)) {
			query.append(" and  obj.unitprice<='").append(unitpriceto + "'");
			countQuery.append(" and  obj.unitprice<='").append(unitpriceto + "'");
		}

		if (!StringUtils.isEmpty(feesfrom)) {
			query.append(" and  obj.fees>=").append(feesfrom);
			countQuery.append(" and  obj.fees>=").append(feesfrom);
		}
		if (!StringUtils.isEmpty(feesto)) {
			query.append(" and  obj.fees<='").append(feesto + "'");
			countQuery.append(" and  obj.fees<='").append(feesto + "'");
		}

		if (!StringUtils.isEmpty(discountfrom)) {
			query.append(" and  obj.discounts>=").append(discountfrom);
			countQuery.append(" and  obj.discounts>=").append(discountfrom);
		}
		if (!StringUtils.isEmpty(discountto)) {
			query.append(" and  obj.discounts<='").append(discountto + "'");
			countQuery.append(" and  obj.discounts<='").append(discountto + "'");
		}

		if (!StringUtils.isEmpty(amountfrom)) {
			query.append(" and  obj.amount>=").append(amountfrom);
			countQuery.append(" and  obj.amount>='").append(amountfrom+"'");
		}
		if (!StringUtils.isEmpty(amountto)) {
			query.append(" and  obj.amount<='").append(amountto + "'");
			countQuery.append(" and  obj.amount<='").append(amountto + "'");
		}

		if (!StringUtils.isEmpty(fromInvoiceNo)) {
			query.append(" and  obj.invoiceNo>='").append(fromInvoiceNo + "'");
			countQuery.append(" and  obj.invoiceNo>='").append(fromInvoiceNo + "'");
		}
		if (!StringUtils.isEmpty(InvoiceNoTo)) {
			query.append(" and  obj.invoiceNo<='").append(InvoiceNoTo + "'");
			countQuery.append(" and  obj.invoiceNo<='").append(InvoiceNoTo + "'");
		}
		if (!StringUtils.isEmpty(fueltype1)) {
			StringBuffer fuelt = new StringBuffer("");
			StringTokenizer st2 = new StringTokenizer(fueltype1, ",");

			int no = st2.countTokens();
			int less = 0;
			while (st2.hasMoreTokens()) {
				// System.out.println(st2.nextToken());
				String str = st2.nextToken();
				if (less != no - 1) {
					fuelt.append("'" + str + "',");
					less++;
				} else {
					fuelt.append("'" + str + "'");
				}
			}
			query.append(" and  obj.fueltype in (" + fuelt + ")");
			countQuery.append(" and  obj.fueltype in (" + fuelt + ")");
			// System.out.println("\nfuelt===>"+fuelt+"\n");
		}
		
		if (distributionData) {
			query.append(" order by obj.fuelvendor.name, obj.company.name, obj.terminal.name, obj.driversid.catagory.name, obj.fueltype");
		}
		
		System.out.println("\nquery=fuelog=>" + query + "\n");
		Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();
		searchCriteria.setRecordCount(recordCount.intValue());
		System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");

		List<FuelLog> fs = null;
		if (!distributionData) {
			fs = (List<FuelLog>) genericDAO
			.getEntityManager()
			.createQuery(query.toString())
			.setMaxResults(searchCriteria.getPageSize())
			.setFirstResult(searchCriteria.getPage() * searchCriteria.getPageSize())
			.getResultList();
		} else {
			fs = (List<FuelLog>) genericDAO
					.getEntityManager()
					.createQuery(query.toString())
					.getResultList();
		}
			
		List<FuelLog> summarys = new ArrayList<FuelLog>();
		FuelLogReportWrapper wrapper = new FuelLogReportWrapper();
		wrapper.setFuellog(summarys);

		long totalColumn = 0;
		double totalGallons = 0.0;
		double totalFees = 0.0;
		double totaldiscounts = 0.0;
		double totalAmounts = 0.0;
		double totalGrossCost=0.0;

		//Map<String, List<FuelLog>> fuellogMap = new HashMap<String, List<FuelLog>>();

		for (FuelLog fuelog : fs) {
			totalColumn = totalColumn + 1;
			if (fuelog != null) {
				FuelLog output = new FuelLog();
				output.setFuelVenders((fuelog.getFuelvendor() != null) ? fuelog
						.getFuelvendor().getName() : "");
				output.setCompanies((fuelog.getCompany() != null) ? fuelog
						.getCompany().getName() : "");
				output.setTerminals((fuelog.getTerminal() != null) ? fuelog
						.getTerminal().getName() : "");
				// output.setInvoicedDate(sdf.format(fuelog.getInvoiceDate()));
				output.setInvoicedDate((fuelog.getInvoiceDate() != null) ? sdf
						.format(fuelog.getInvoiceDate()) : "");
				output.setInvoiceNo((fuelog.getInvoiceNo() != null) ? fuelog
						.getInvoiceNo() : "");
				output.setUnits((fuelog.getUnit() != null) ? fuelog.getUnit()
						.getUnit().toString() : "");
				
				output.setVehicleCompany((fuelog.getUnit() != null && fuelog.getUnit().getOwner() != null) 
						? fuelog.getUnit().getOwner().getName() : StringUtils.EMPTY);
				
				String fuelcardNum=null;
                if(fuelog.getFuelcard()!=null){                                        
                        String fuelcardnumber=fuelog.getFuelcard().getFuelcardNum();                                        
                        
                        if(fuelcardnumber.length()>8 && fuelcardnumber.length()<=12){
                                String[] fuelcardnum = new String[10];
                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
                                        fuelcardnum[2]=fuelcardnumber.substring(8,fuelcardnumber.length());
                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2];                                                
                        }
                        
                        else if(fuelcardnumber.length()>12){
                                String[] fuelcardnum = new String[10];
                                        fuelcardnum[0]=fuelcardnumber.substring(0,4);
                                        fuelcardnum[1]=fuelcardnumber.substring(4,8);
                                        fuelcardnum[2]=fuelcardnumber.substring(8,12);
                                        fuelcardnum[3]=fuelcardnumber.substring(12,fuelcardnumber.length());
                                        fuelcardNum=fuelcardnum[0]+"-"+fuelcardnum[1]+"-"+fuelcardnum[2]+"-"+fuelcardnum[3];                                                
                        }
                        else
                        {
                                fuelcardNum=fuelcardnumber;
                        }  
                }				
				
            // Fuel log - subcontractor
            output.setSubContractorName((fuelog.getSubContractor() != null) ? fuelog
    						.getSubContractor().getName() : "");    
				// output.setDrivers((fuelog.getDriverFname()!=null)?fuelog.getDriverFname().getFullName():"");
				output.setDrivers((fuelog.getDriversid() != null) ? fuelog
						.getDriversid().getFullName() : "");
				output.setDriverCategory((fuelog.getDriversid() != null) && fuelog.getDriversid().getCatagory() != null ? fuelog
						.getDriversid().getCatagory().getName() : "");
			
				// JJ Keller Fuel card - 12th Dec 2016
				output.setSsn((fuelog.getDriversid() != null && fuelog.getDriversid().getSsn() != null) ? fuelog
						.getDriversid().getSsn() : StringUtils.EMPTY);
				// billing.setDestination((ticket.getDestination()!=null)?ticket.getDestination().getName():"");
				/*
				 * output.setFuelCardNumbers(fuelog.getFuelCardNumber().toString(
				 * ));
				 */
				output.setFuelCardNumbers((fuelog.getFuelcard() != null) ? fuelcardNum : "");
				/*
				 * output.setFuelCardNumbers((fuelog.getFuelCardNumber()!=null)?
				 * fuelog.getFuelCardNumber():"");
				 */
				/* output.setFuelCardNumber((fuelog.getFuelCardNumber())); */// added
																				// today

				// output.setFueltype(fuelog.getFueltype());

				output.setFueltype((fuelog.getFueltype() != null) ? fuelog
						.getFueltype() : "");

				output.setCity((fuelog.getCity() != null) ? fuelog.getCity()
						: "");
				output.setStates((fuelog.getState() != null) ? fuelog
						.getState().getName() : "");
				output.setTransactiontime((fuelog.getTransactiontime() != null) ? fuelog
						.getTransactiontime() : "");
				output.setTransactionsDate((fuelog.getTransactiondate() != null) ? sdf
						.format(fuelog.getTransactiondate()) : "");
				output.setTransactiondate(fuelog.getTransactiondate());
				output.setGallons((fuelog.getGallons() != null) ? fuelog
						.getGallons() : 0.0);
				// output.setGallons(fuelog.getGallons());
				output.setUnitprice((fuelog.getUnitprice() != null) ? fuelog
						.getUnitprice() : 0.0);
				
				output.setGrosscost((fuelog.getGrosscost() !=null) ? fuelog.getGrosscost(): 0.0);
				output.setFees((fuelog.getFees() != null) ? fuelog.getFees()
						: 0.0);
				output.setDiscounts((fuelog.getDiscounts() != null) ? fuelog
						.getDiscounts() : 0.0);
				output.setAmount((fuelog.getAmount() != null) ? fuelog
						.getAmount() : 0.0);

				if (fuelog.getGallons() != null)
					totalGallons += fuelog.getGallons();
				if(fuelog.getGrosscost() != null)
					totalGrossCost+=fuelog.getGrosscost();
				if (fuelog.getFees() != null)
					totalFees += fuelog.getFees();
				if (fuelog.getDiscounts() != null)
					totaldiscounts += fuelog.getDiscounts();
				if (fuelog.getAmount() != null)
					totalAmounts += fuelog.getAmount();

				summarys.add(output);

			}

		}
        
		totalAmounts = MathUtil.roundUp(totalAmounts, 2);
		totaldiscounts = MathUtil.roundUp(totaldiscounts, 2);
		totalFees = MathUtil.roundUp(totalFees, 2);
		totalGallons = MathUtil.roundUp(totalGallons, 3);
		totalGrossCost=MathUtil.roundUp(totalGrossCost, 2);
		wrapper.setTotalAmounts(totalAmounts);
		wrapper.setTotaldiscounts(totaldiscounts);
		wrapper.setTotalFees(totalFees);
		wrapper.setTotalGallons(totalGallons);
		wrapper.setTotalGrossCost(totalGrossCost);
		//wrapper.setTotalColumn(totalColumn);
		wrapper.setTotalColumn(fs.size());
		
		if (StringUtils.isNotEmpty(fromInvoiceDate)) {
			wrapper.setInvoiceDateFrom(fromInvoiceDate);
		}
		if (StringUtils.isNotEmpty(invoiceDateTo)) {
			wrapper.setInvoiceDateTo(invoiceDateTo);
		}
		
		// Fuel log - truck report 3rd Jun 2016
		if (StringUtils.isNotEmpty(transactionDateFrom)) {
			wrapper.setTransactionDateFrom(input.getTransactionDateFrom());
		}
		if (StringUtils.isNotEmpty(transactionDateTo)) {
			wrapper.setTransactionDateTo(input.getTransactionDateTo());
		}
		
		if (StringUtils.isNotEmpty(fromInvoiceNo)) {
			wrapper.setInvoiceNumberFrom(fromInvoiceNo);
		}
		if (StringUtils.isNotEmpty(InvoiceNoTo)) {
			wrapper.setInvoiceNumberTo(InvoiceNoTo);
		}
		
		// Fuel log - truck report 3rd Jun 2016
		if (StringUtils.isNotEmpty(company)) {
			wrapper.setCompany(company);
		}
		
		return wrapper;
	}
	
	// Fuel log - truck report - 3rd Jun 2016
	private String retrieveTruckIds(String company) {
		String vehicleQuery = "select obj from Vehicle obj where obj.type=1" 
	   		+" and obj.owner in ("
	   		+company
	   		+")";
		System.out.println("******** the vehicleQuery in retrieveTruckIds is "+ vehicleQuery);
		
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		String truckIds = StringUtils.EMPTY;
		if(vehicleList == null || vehicleList.isEmpty()) {
			return truckIds;
		}
		
		for (Vehicle vehicleObj : vehicleList) {	
			truckIds = truckIds + "," + String.valueOf(vehicleObj.getId());
		}
		
		return truckIds.substring(1);
	}

	@Override
	public FuelLogVerificationReportWrapper generateFuelLogVerificationData(
			SearchCriteria searchCriteria, FuelLogVerificationReportInput input) {
		
		Map<String, String> params = new HashMap<String, String>();		
		
		String transactionDateFrom1 = (String) searchCriteria.getSearchMap()
				.get("transactionDateFrom");
		String transactionDateTo1 = (String) searchCriteria.getSearchMap().get(
				"transactionDateTo");
		
		String transactionDateFrom = ReportDateUtil.getFromDate(input
				.getTransactionDateFrom());
		String transactionDateTo = ReportDateUtil.getToDate(input
				.getTransactionDateTo());
		
		
		String terminal = input.getTerminal();
		
		String driver = input.getDriver();
		String driverids="-1";
		if(!StringUtils.isEmpty(driver)){
			List<Driver> drvObjList = genericDAO.executeSimpleQuery("select obj from Driver obj where obj.fullName in ('"+driver+"')");
			if(drvObjList!=null && drvObjList.size()>0){
				for(Driver drvObj:drvObjList){
					driverids=driverids+","+drvObj.getId();
				}
			}
		}

		String company = input.getCompany();
		

		

		StringBuffer query = new StringBuffer("");
		
		query.append("select df.id,f.id,(df.gallons-f.gallons) as galdiff,df.tempTruck,df.driverName,df.companyName,df.terminalName,df.tempTransactionDate,df.tempFuelCardNum,df.gallons,f.gallons from DriverFuelLog df,FuelLog f where df.truck=f.unit and df.driverCompany=f.company " +
				"and df.terminal=f.terminal and df.driver=f.driversid and df.transactionDate=f.transactiondate");
		
		
		if (!StringUtils.isEmpty(company)) {
			query.append(" and  df.driverCompany in (" + company + ")");
			
		}
		if (!StringUtils.isEmpty(terminal)) {
			query.append(" and  df.terminal in (" + terminal + ")");
			
		}	
		
		if (!StringUtils.isEmpty(driver)) {
			query.append(" and  df.driver in (" + driverids + ")");			
		}

		if (!StringUtils.isEmpty(transactionDateFrom1) && !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom) && !StringUtils.isEmpty(transactionDateTo)) {
				query.append(" and  df.transactionDate >= '"
						+ transactionDateFrom + "' and df.transactionDate <='" + transactionDateTo
						+ "'");				
			}
		}
		

		
		System.out.println("\nquery=fuelog=>" + query.toString() + "\n");
		
		
		//searchCriteria.setRecordCount(recordCount.intValue());
		

		List<DriverFuelLog> fs = genericDAO.executeSimpleQuery(query.toString());
			
		List<FuelLogVerificationReportData> summarys = new ArrayList<FuelLogVerificationReportData>();
		FuelLogVerificationReportWrapper wrapper = new FuelLogVerificationReportWrapper();
		wrapper.setFuellogverification(summarys);	
		Map criterias = new HashMap();
		String drvfuellogids="-1";
		String fuellogids="-1";
		Double gallonDiff = 0.0;
		for(Object obj:fs){
			 Object[] objarry=(Object[])obj;
			if(objarry!=null){
				if(objarry[0] !=null)	
					drvfuellogids = drvfuellogids+","+objarry[0].toString();
				if(objarry[1] !=null)
					fuellogids = fuellogids+","+objarry[1].toString();
				if(objarry[2] !=null){
					gallonDiff = Double.parseDouble(objarry[2].toString());
					if(gallonDiff<-5 || gallonDiff>5){
						FuelLogVerificationReportData flvrdObj = new FuelLogVerificationReportData();
						flvrdObj.setDriver(objarry[4].toString());
						flvrdObj.setDriverCompany(objarry[5].toString());
						flvrdObj.setTerminal(objarry[6].toString());
						flvrdObj.setTruck(objarry[3].toString());
						flvrdObj.setTransactionDate(objarry[7].toString());
						if(objarry[8]!=null)
							flvrdObj.setDriverFuelCard(objarry[8].toString());
						else
							flvrdObj.setDriverFuelCard("");
						flvrdObj.setDriverGallons(Double.parseDouble(objarry[9].toString()));
						flvrdObj.setUploadedGallons(Double.parseDouble(objarry[10].toString()));
						summarys.add(flvrdObj);
					}
				}
			}
		}
		
		StringBuilder driverFuelLogListQuery = new StringBuilder("select obj  from DriverFuelLog obj where obj.id  not in ("+drvfuellogids+")");
		if (!StringUtils.isEmpty(company)) {
			driverFuelLogListQuery.append(" and  obj.driverCompany in (" + company + ")");			
		}
		if (!StringUtils.isEmpty(terminal)) {
			driverFuelLogListQuery.append(" and  obj.terminal in (" + terminal + ")");
			
		}	
		
		if (!StringUtils.isEmpty(driver)) {
			driverFuelLogListQuery.append(" and  obj.driver in (" + driverids + ")");			
		}

		if (!StringUtils.isEmpty(transactionDateFrom1) && !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom) && !StringUtils.isEmpty(transactionDateTo)) {
				driverFuelLogListQuery.append(" and  obj.transactionDate >= '"
						+ transactionDateFrom + "' and obj.transactionDate <='" + transactionDateTo
						+ "'");				
			}
		}
		
		List<DriverFuelLog> driverFuelLogList = genericDAO.executeSimpleQuery(driverFuelLogListQuery.toString());
        
		for(DriverFuelLog drfl:driverFuelLogList){
			FuelLogVerificationReportData flvrdObj = new FuelLogVerificationReportData();
			flvrdObj.setDriver(drfl.getDriverName());
			flvrdObj.setDriverCompany(drfl.getCompanyName());
			flvrdObj.setTerminal(drfl.getTerminalName());
			flvrdObj.setTruck(drfl.getTempTruck());
			flvrdObj.setTransactionDate(drfl.getTempTransactionDate());
			flvrdObj.setDriverFuelCard(drfl.getTempFuelCardNum());
			flvrdObj.setDriverGallons(drfl.getGallons());
			flvrdObj.setUploadedGallons(null);
			summarys.add(flvrdObj);
		}
		
		
		StringBuilder fuelLogListQuery = new StringBuilder("select obj  from FuelLog obj where obj.id  not in ("+fuellogids+")");
		if (!StringUtils.isEmpty(company)) {
			fuelLogListQuery.append(" and  obj.company in (" + company + ")");			
		}
		if (!StringUtils.isEmpty(terminal)) {
			fuelLogListQuery.append(" and  obj.terminal in (" + terminal + ")");
			
		}	
		
		if (!StringUtils.isEmpty(driver)) {
			fuelLogListQuery.append(" and  obj.driversid in (" + driverids + ")");			
		}

		if (!StringUtils.isEmpty(transactionDateFrom1) && !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom) && !StringUtils.isEmpty(transactionDateTo)) {
				fuelLogListQuery.append(" and  obj.transactiondate >= '"
						+ transactionDateFrom + "' and obj.transactiondate <='" + transactionDateTo
						+ "'");				
			}
		}
		
		List<FuelLog> fuelLogList = genericDAO.executeSimpleQuery(fuelLogListQuery.toString());
        
		for(FuelLog fl:fuelLogList){
			FuelLogVerificationReportData flvrdObj = new FuelLogVerificationReportData();
			flvrdObj.setDriver(fl.getDriversid().getFullName());
			flvrdObj.setDriverCompany(fl.getCompany().getName());
			flvrdObj.setTerminal(fl.getTerminal().getName());
			flvrdObj.setTruck(fl.getUnit().getUnitNum());
			flvrdObj.setTransactionDate(sdf.format(fl.getTransactiondate()));
			if(fl.getFuelcard()!=null)
				flvrdObj.setDriverFuelCard(fl.getFuelcard().getFuelcardNum());
			else
				flvrdObj.setDriverFuelCard("");
			flvrdObj.setDriverGallons(null);
			flvrdObj.setUploadedGallons(fl.getGallons());
			summarys.add(flvrdObj);
		}
		
		return wrapper;
	}
	
	@Override
	public TollDistributionReportWrapper generateTollDistributionData(SearchCriteria searchCriteria, TollDistributionReportInput input) {
		EztollReportInput ezTollReportInput = new EztollReportInput();
		map(input, ezTollReportInput);
		
		EztollReportWrapper eztollReportWrapper = generateEztollData(searchCriteria, ezTollReportInput, true);
		
		TollDistributionReportWrapper tollDistributionReportWrapper = new TollDistributionReportWrapper();
		map(tollDistributionReportWrapper, eztollReportWrapper);
		
		if (!tollDistributionReportWrapper.getEztolls().isEmpty()) {
			List<EzToll> groupedEzTollList = groupEzTolls(tollDistributionReportWrapper.getEztolls());
			tollDistributionReportWrapper.setEztolls(groupedEzTollList);
		}
		
		return tollDistributionReportWrapper;
	}
	
	private void map(TollDistributionReportInput tollDistributionReportInput, EztollReportInput ezTollReportInput) {
		ezTollReportInput.setCompany(tollDistributionReportInput.getCompany());
		ezTollReportInput.setTerminal(tollDistributionReportInput.getTerminal());
		ezTollReportInput.setToolcompany(tollDistributionReportInput.getToolcompany());
		ezTollReportInput.setFromInvoiceDate(tollDistributionReportInput.getFromInvoiceDate());
		ezTollReportInput.setInvoiceDateTo(tollDistributionReportInput.getInvoiceDateTo());
		ezTollReportInput.setTransactionDateFrom(tollDistributionReportInput.getTransactionDateFrom());
		ezTollReportInput.setTransactionDateTo(tollDistributionReportInput.getTransactionDateTo());
	}
	
	private void map(TollDistributionReportWrapper tollDistributionReportWrapper, EztollReportWrapper eztollReportWrapper ) {
		tollDistributionReportWrapper.setEztolls(eztollReportWrapper.getEztolls());
		tollDistributionReportWrapper.setTotalAmounts(eztollReportWrapper.getTotalAmounts());
		tollDistributionReportWrapper.setTotalColumn(eztollReportWrapper.getTotalColumn());
		tollDistributionReportWrapper.setInvoiceDateFrom(eztollReportWrapper.getInvoiceDateFrom());
		tollDistributionReportWrapper.setInvoiceDateTo(eztollReportWrapper.getInvoiceDateTo());
		tollDistributionReportWrapper.setTransactionDateFrom(eztollReportWrapper.getTransactionDateFrom());
		tollDistributionReportWrapper.setTransactionDateTo(eztollReportWrapper.getTransactionDateTo());
	}
	
	private List<EzToll> groupEzTolls(List<EzToll> ezTollList) {
		String prevKey = StringUtils.EMPTY;
		String currentKey = StringUtils.EMPTY;
		double groupAmount = 0.0;
		EzToll previousEzToll= null;
		List<EzToll> groupedEzTollList = new ArrayList<EzToll>();
		for (EzToll aEzToll :  ezTollList) {
			currentKey = aEzToll.getTollcompanies() + "|" + aEzToll.getCompanies() + "|" + aEzToll.getTerminals() 
				+ "|" + aEzToll.getDriverCategory();
			
			if (StringUtils.isEmpty(prevKey)) {
				prevKey = currentKey;
				previousEzToll = aEzToll;
			}
			
			if (!currentKey.equals(prevKey)) {
				previousEzToll.setAmount(groupAmount);
				groupedEzTollList.add(previousEzToll);
				
				prevKey = currentKey;
				previousEzToll = aEzToll;
				groupAmount = aEzToll.getAmount();
			} else {
				groupAmount += aEzToll.getAmount();			
			}
		}
		
		previousEzToll.setAmount(groupAmount);
		groupedEzTollList.add(previousEzToll);
		
		return groupedEzTollList;
	}
	
	@Override
	public EztollReportWrapper generateEztollData(
			SearchCriteria searchCriteria, EztollReportInput input, boolean distributionData) {

		Map<String, String> params = new HashMap<String, String>();

		String transferDateFrom = ReportDateUtil.getFromDate(input
				.getTransferDateFrom());
		String transferDateTo = ReportDateUtil.getToDate(input
				.getTransferDateTo());

		String tollcompany = input.getToolcompany();

		params.put("tollcompany", tollcompany);

		String company = input.getCompany();

		params.put("company", company);

		String terminal = input.getTerminal();
		String tolltagnumber = input.getTollTagNumber();
		String plate = input.getPlateNumber();
		String unit = input.getUnit();

		String transferTimeFrom1 = (String) searchCriteria.getSearchMap().get(
				"transferTimeFrom");
		String transfertimeTo1 = (String) searchCriteria.getSearchMap().get(
				"transferTimeTo");

		String agency = input.getAgency();

		String amountfrom = input.getAmountsfrom();
		String amountto = input.getAmountsto();
         
		String driver=input.getDriver();
		
		
		String fromInvoiceDate1 = (String) searchCriteria.getSearchMap().get(
				"fromInvoiceDate");
		String invoiceDateTo1 = (String) searchCriteria.getSearchMap().get(
				"invoiceDateTo");
		String transactionDateFrom1 = (String) searchCriteria.getSearchMap()
				.get("transactionDateFrom");
		String transactionDateTo1 = (String) searchCriteria.getSearchMap().get(
				"transactionDateTo");

		String fromInvoiceDate = ReportDateUtil.getFromDate(input
				.getFromInvoiceDate());
		String invoiceDateTo = ReportDateUtil.getToDate(input
				.getInvoiceDateTo());
		String transactionDateFrom = ReportDateUtil.getFromDate(input
				.getTransactionDateFrom());
		String transactionDateTo = ReportDateUtil.getToDate(input
				.getTransactionDateTo());
		
		 
		StringBuffer query = new StringBuffer("");
		StringBuffer countQuery = new StringBuffer("");
		query.append("select obj from EzToll obj where 1=1");
		countQuery.append("select count(obj) from EzToll obj where 1=1");
		if (!StringUtils.isEmpty(tollcompany)) {
			query.append("and  obj.toolcompany in (" + tollcompany + ")");
			countQuery.append("and  obj.toolcompany in (" + tollcompany + ")");
		}
		if (!StringUtils.isEmpty(company)) {
			query.append("and  obj.company in (" + company + ")");
			countQuery.append("and  obj.company in (" + company + ")");
		}
		if (!StringUtils.isEmpty(terminal)) {
			query.append("and  obj.terminal in (" + terminal + ")");
			countQuery.append("and  obj.terminal in (" + terminal + ")");
		}

		if (!StringUtils.isEmpty(tolltagnumber)) {
			query.append("and  obj.tollTagNumber in (" + tolltagnumber + ")");
			countQuery.append("and  obj.tollTagNumber in (" + tolltagnumber + ")");
		}

		if (!StringUtils.isEmpty(unit)) {
			
			String truckIds="";				
				   
				   String vehiclequery="select obj from Vehicle obj where obj.type=1"
				   		+" and obj.unit in ("
						+unit
						+")";
					
					System.out.println("******** the truck query is "+vehiclequery);
					
					List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
					
					if(vehicleLists!=null && vehicleLists.size()>0){						
						for(Vehicle vehicleObj : vehicleLists) {						
							if(StringUtils.isEmpty(truckIds)){	
							truckIds=String.valueOf(vehicleObj.getId());
							}
							else{
								truckIds=truckIds+","+String.valueOf(vehicleObj.getId());
							}
						}
					}			
						
			
			query.append("and  obj.plateNumber in (" + truckIds + ")");
			countQuery.append("and  obj.plateNumber in (" + truckIds + ")");
			
			/*query.append("and  (obj.tollTagNumber in (select obj.id from VehicleTollTag obj where obj.vehicle in ("
					+ unit + ")) or obj.plateNumber in (" + unit + "))");
			countQuery.append("and  (obj.tollTagNumber in (select obj.id from VehicleTollTag obj where obj.vehicle in ("
					+ unit + ")) or obj.plateNumber in (" + unit + "))");*/
		}

		if (!StringUtils.isEmpty(plate)) {
			query.append("and  obj.plateNumber in (" + plate + ")");
			countQuery.append("and  obj.plateNumber in (" + plate + ")");
		}
		
		
		if (!StringUtils.isEmpty(fromInvoiceDate1)
				&& !StringUtils.isEmpty(invoiceDateTo1)) {
			if (!StringUtils.isEmpty(fromInvoiceDate)
					&& !StringUtils.isEmpty(invoiceDateTo)) {
				query.append(" and  obj.invoiceDate between '" + fromInvoiceDate
						+ "' and '" + invoiceDateTo + "'");
				countQuery.append(" and  obj.invoiceDate between '" + fromInvoiceDate
						+ "' and '" + invoiceDateTo + "'");
				// query.append("and  obj.invoiceDate between '" +
				// fromInvoiceDate + "' and '" + invoiceDateTo +
				// "'or obj.invoiceDate IS null ");
			}
		}
		/*
		 * else{ query.append("and  obj.invoiceDate between '" + fromInvoiceDate
		 * + "' and '" + invoiceDateTo + "'or obj.invoiceDate IS null "); }
		 */

		if (!StringUtils.isEmpty(transactionDateFrom1)
				&& !StringUtils.isEmpty(transactionDateTo1)) {
			if (!StringUtils.isEmpty(transactionDateFrom)
					&& !StringUtils.isEmpty(transactionDateTo)) {
				query.append(" and  obj.transactiondate >= '"
						+ transactionDateFrom + "' and obj.transactiondate <='" + transactionDateTo
						+ "'");
				countQuery.append(" and  obj.transactiondate >= '"
						+ transactionDateFrom + "' and obj.transactiondate <='" + transactionDateTo
						+ "'");
			}
		}
		
		
		

		if (!StringUtils.isEmpty(transferDateFrom)
				&& !StringUtils.isEmpty(transferDateTo)) {
			query.append("and  obj.transactiondate>='"
					+ transferDateFrom +" 00:00:00' and obj.transactiondate<='" +transferDateTo+" 23:59:59'");
			countQuery.append("and  obj.transactiondate>='"
					+ transferDateFrom +" 00:00:00' and obj.transactiondate<='" +transferDateTo+" 23:59:59'");
		}

		if (!StringUtils.isEmpty(transferTimeFrom1)
				&& !StringUtils.isEmpty(transfertimeTo1)) {
			query.append("and  obj.transactiontime>='"
					+ transferTimeFrom1 + "' and obj.transactiontime<='" + transfertimeTo1 +"'");
			countQuery.append("and  obj.transactiontime>='"
					+ transferTimeFrom1 + "' and obj.transactiontime<='" + transfertimeTo1 +"'");
		}

		if (!StringUtils.isEmpty(agency)) {
			/* query.append("and  obj.agency = '" + agency + "'"); */
			query.append("and  obj.agency like '%" + agency + "%'");
			countQuery.append("and  obj.agency like '%" + agency + "%'");
		}

		if (!StringUtils.isEmpty(amountfrom)) {
			query.append(" and  obj.amount>=").append(amountfrom);
			countQuery.append(" and  obj.amount>=").append(amountfrom);
		}
		if (!StringUtils.isEmpty(amountto)) {
			query.append(" and  obj.amount<='").append(amountto + "'");
			countQuery.append(" and  obj.amount<='").append(amountto + "'");
		}
		
		if (!StringUtils.isEmpty(driver)) {
			query.append(" and  obj.driver in ("+driver+")");
			countQuery.append(" and  obj.driver in ("+driver+")");
		}
		
		if (distributionData) {
			query.append(" order by obj.toolcompany.name, obj.company.name, obj.terminal.name, obj.driver.catagory.name");
		}

		System.out.println("\nquery.toString()===>" + query.toString() + "\n");
		
		Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();
		searchCriteria.setRecordCount(recordCount.intValue());
		System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");
		
		List<EzToll> fs = null;
		if (!distributionData) {
			fs = (List<EzToll>) genericDAO
					.getEntityManager()
					.createQuery(query.toString())
					.setMaxResults(searchCriteria.getPageSize())
					.setFirstResult(searchCriteria.getPage() * searchCriteria.getPageSize())
					.getResultList();
		} else {
			fs = (List<EzToll>) genericDAO
					.getEntityManager()
					.createQuery(query.toString())
					.getResultList();
		}

		List<EzToll> summarys = new ArrayList<EzToll>();
		EztollReportWrapper wrapper = new EztollReportWrapper();
		wrapper.setEztolls(summarys);

		long totalColumn = 0;
		double totalAmounts = 0.0;

		// Map<String, List<FuelLog>> fuellogMap = new HashMap<String,
		// List<FuelLog>>();

		for (EzToll eztol : fs) {
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
				output.setTransactiondate(eztol.getTransactiondate());
				output.setAgency(eztol.getAgency());
				output.setAmount(eztol.getAmount());
				output.setDrivername(eztol.getDriver()!=null?eztol.getDriver().getFullName():"");
				output.setDriverCategory((eztol.getDriver() != null) && eztol.getDriver().getCatagory() != null ? eztol
						.getDriver().getCatagory().getName() : "");

				if (eztol.getAmount() != null)
					totalAmounts += eztol.getAmount();

				summarys.add(output);

			}

		}

		totalAmounts = MathUtil.roundUp(totalAmounts, 2);
		wrapper.setTotalAmounts(totalAmounts);

		wrapper.setTotalColumn(totalColumn);
		
		if (StringUtils.isNotEmpty(fromInvoiceDate)) {
			wrapper.setInvoiceDateFrom(fromInvoiceDate);
		}
		if (StringUtils.isNotEmpty(invoiceDateTo)) {
			wrapper.setInvoiceDateTo(invoiceDateTo);
		}
		if (StringUtils.isNotEmpty(transactionDateFrom)) {
			wrapper.setTransactionDateFrom(transactionDateFrom);
		}
		if (StringUtils.isNotEmpty(transactionDateTo)) {
			wrapper.setTransactionDateTo(transactionDateTo);
		}


		
		System.out.println("******* nquery.toString()===>" + query.toString() + "\n");
		
		
		return wrapper;
	}

	
	@Override
	public List<Summary> generateSummaryNew(SearchCriteria criteria,
			BillingHistoryInput input, Map<String, Object> reportParams) { // Peak rate 2nd Feb 2021
		String fromDateInvoiceStr = ReportDateUtil.getFromDate(input
				.getFromInvoiceDate());
		String toDateInvoiceStr = ReportDateUtil.getToDate(input
				.getInvoiceDateTo());
		String invoiceNumberFrom = input.getInvoiceNumberFrom();
		String invoiceNumberTo = input.getInvoiceNumberTo();

		String fuelSurchargeFrom1 = input.getFuelSurchargeFrom();
		String fuelSurchargeTo1 = input.getFuelSurchargeTo();

		String tonnagePremiumFrom = input.getTonnagePremiumFrom();
		String tonnagePremiumTo = input.getTonnagePremiumTo();

		String demurrageChargeFrom = input.getDemurrageChargeFrom();
		String demurrageChargeTo = input.getDemurrageChargeTo();

		String amountFrom = input.getAmountFrom();
		String amountTo = input.getAmountTo();

		String totAmtFrom = input.getTotAmtFrom();
		String totAmtTo = input.getTotAmtTo();

		String totalAmtTo = input.getTotalAmtTo();
		String totalAmtFrom = input.getTotalAmtFrom();

		String rateFrom = input.getRateFrom();
		String rateTo = input.getRateTo();
		
		// Peak rate 2nd Feb 2021
		String isPeakRate = input.getIsPeakRate();

		boolean useInvoice = false;
		StringBuffer ticketIds = new StringBuffer("-1,");
		if ( (!StringUtils.isEmpty(invoiceNumberFrom))
				|| (!StringUtils.isEmpty(invoiceNumberTo))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeFrom()))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeTo()))
				|| (!StringUtils.isEmpty(tonnagePremiumFrom))
				|| (!StringUtils.isEmpty(tonnagePremiumTo))
				|| (!StringUtils.isEmpty(demurrageChargeFrom))
				|| (!StringUtils.isEmpty(demurrageChargeTo))
				|| (!StringUtils.isEmpty(amountFrom))
				|| (!StringUtils.isEmpty(amountTo))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(totalAmtTo))
				|| (!StringUtils.isEmpty(totalAmtFrom))//
				|| (!StringUtils.isEmpty(input.getRateFrom()))
				|| (!StringUtils.isEmpty(input.getRateTo()))) {
			StringBuffer query2 = new StringBuffer(
					"select bill.ticket from Invoice inv, Billing bill where 1=1 and bill.invoiceNo = inv.invoiceNumber and bill.origin=inv.transferStation.name and bill.destination=inv.landfill.name");

			if (!StringUtils.isEmpty(input.getRateFrom())) {

				query2.append(" and bill.rate>=").append(rateFrom);
			}
			if (!StringUtils.isEmpty(input.getRateTo())) {

				query2.append(" and bill.rate<=").append(rateTo);
			}

			if (!StringUtils.isEmpty(fromDateInvoiceStr)) {
				query2.append(" and  inv.invoiceDate>='").append(
						fromDateInvoiceStr + "'");
			}
			if (!StringUtils.isEmpty(toDateInvoiceStr)) {
				query2.append(" and  inv.invoiceDate<='").append(
						toDateInvoiceStr + "'");
			}
			if (!StringUtils.isEmpty(invoiceNumberFrom)) {
				query2.append(" and  bill.invoiceNo>='").append(
						invoiceNumberFrom + "'");
			}
			if (!StringUtils.isEmpty(invoiceNumberTo)) {
				query2.append(" and  bill.invoiceNo<='").append(
						invoiceNumberTo + "'");
			}

			if (!StringUtils.isEmpty(fuelSurchargeFrom1)) {
				query2.append(" and bill.fuelSurcharge >= '").append(
						fuelSurchargeFrom1 + "'");
			}
			if (!StringUtils.isEmpty(fuelSurchargeTo1)) {
				query2.append(" and bill.fuelSurcharge <= '").append(
						fuelSurchargeTo1 + "'");
			}
			// /hereeee

			if (!StringUtils.isEmpty(tonnagePremiumFrom)) {
				query2.append(" and bill.tonnagePremium >= ").append(
						tonnagePremiumFrom);
			}
			if (!StringUtils.isEmpty(tonnagePremiumTo)) {
				query2.append(" and bill.tonnagePremium <= ").append(
						tonnagePremiumTo);
			}

			if (!StringUtils.isEmpty(demurrageChargeFrom)) {
				query2.append(" and bill.demurrageCharge >= ").append(
						demurrageChargeFrom);
			}
			if (!StringUtils.isEmpty(demurrageChargeTo)) {
				query2.append(" and bill.demurrageCharge <= ").append(
						demurrageChargeTo);
			}
			if (!StringUtils.isEmpty(amountFrom)) {
				query2.append(" and bill.amount >= ").append(amountFrom);
			}
			if (!StringUtils.isEmpty(amountTo)) {
				query2.append(" and bill.amount <= ").append(amountTo);
			}
			if (!StringUtils.isEmpty(totalAmtFrom)) {
				query2.append(" and bill.totAmt >= ").append(totalAmtFrom);
			}
			if (!StringUtils.isEmpty(totalAmtTo)) {
				query2.append(" and bill.totAmt <= ").append(totalAmtTo);
			}
			if (!StringUtils.isEmpty(totAmtFrom)) {
				query2.append(" and inv.sumTotal >= ").append(totAmtFrom);
			}
			if (!StringUtils.isEmpty(totAmtTo)) {
				query2.append(" and inv.sumTotal <= ").append(totAmtTo);
			}

			useInvoice = true;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(query2
					.toString());
			System.out.println("\nquery2.toString()===>" + query2.toString()
					+ "\n");
			System.out.println("\ntickets.size===>" + tickets.size() + "\n");
			if (tickets != null && tickets.size() > 0) {
				for (Ticket tkt : tickets) {
					ticketIds.append(tkt.getId()).append(",");
				}
			}
			if (ticketIds.indexOf(",") != -1) {
				ticketIds.deleteCharAt(ticketIds.length() - 1);
			}
		}
		Map<String, String> params = new HashMap<String, String>();
		String batchDateFrom = ReportDateUtil.getFromDate(input
				.getBatchDateFrom());
		String batchDateTo = ReportDateUtil.getToDate(input.getBatchDateTo());
		String loadDateFrom = ReportDateUtil.getFromDate(input.getLoadedFrom());
		String loadDateTo = ReportDateUtil.getToDate(input.getLoadedTo());
		String unloadDateFrom = ReportDateUtil.getFromDate(input
				.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());

		String ticketStatus = input.getTicketStatus();
		String notBillable = input.getNotBillable();
		String terminal = input.getTerminal();
		String createdBy = input.getCreatedBy();
		
		String origin = input.getOrigin();
		/*// Truck driver report
		String drillDownOrigin = input.getDrillDownOrigin();
		if (!StringUtils.isEmpty(drillDownOrigin)) {
			origin = drillDownOrigin;
		}*/
		
		String destination = input.getDestination();
		/*// Truck driver report
		String drillDownDestination = input.getDrillDownDestination();
		if (!StringUtils.isEmpty(drillDownDestination)) {
			destination = drillDownDestination;
		}*/
		
		String driver = input.getDriver();
		String truck = input.getTruck();
		String trailer = input.getTrailer();
		String subcontractor = input.getSubcontractor();

		String originTicketFrom = input.getOriginTicketFrom();
		String destinationTicketFrom = input.getDestinationTicketFrom();
		String originGrossWtFrom = input.getOriginGrossWtFrom();
		String originTareWtFrom = input.getOriginTareWtFrom();
		String originTonWtFrom = input.getOriginTonsWtFrom();
		String landfillGrossWtFrom = input.getLandfillGrossWtFrom();
		String landfillTareWtFrom = input.getLandfillTareWtFrom();
		String landfillTonWtFrom = input.getLandfillTonsWtFrom();

		String transferTimeInFrom = input.getTransferTimeInFrom();
		String transferTimeInTo = input.getTransferTimeInTo();
		String transferTimeOutFrom = input.getTransferTimeOutFrom();
		String transferTimeOutTo = input.getTransferTimeOutTo();

		String landfillTimeInFrom = input.getLandfillTimeInFrom();
		String landfillTimeInTo = input.getLandfillTimeInTo();
		String landfillTimeOutFrom = input.getLandfillTimeOutFrom();
		String landfillTimeOutTo = input.getLandfillTimeOutTo();

		String originTicketTo = input.getOriginTicketTo();
		String destinationTicketTo = input.getDestinationTicketTo();
		String originGrossWtTo = input.getOriginGrossWtTo();
		String originTareWtTo = input.getOriginTareWtTo();
		String originTonWtTo = input.getOriginTonsWtTo();
		String landfillGrossWtTo = input.getLandfillGrossWtTo();
		String landfillTareWtTo = input.getLandfillTareWtTo();
		String landfillTonWtTo = input.getLandfillTonsWtTo();

		/* String */rateFrom = input.getRateFrom();
		params.put("rateFrom", rateFrom);
		// String amountFrom=input.getAmountFrom();
		params.put("amountFrom", amountFrom);
		String fuelSurchargeFrom = input.getFuelSurchargeFrom();
		params.put("fuelSurchargeFrom", fuelSurchargeFrom);
		// String tonnagePremiumFrom=input.getTonnagePremiumFrom();
		params.put("tonnagePremiumFrom", tonnagePremiumFrom);
		// String demurrageChargeFrom=input.getDemurrageChargeFrom();
		params.put("demurrageChargeFrom", demurrageChargeFrom);
		// String totAmtFrom=input.getTotAmtFrom();
		params.put("totAmtFrom", totAmtFrom);

		/* String */rateTo = input.getRateTo();
		params.put("rateTo", rateTo);
		// String amountTo=input.getAmountTo();
		params.put("amountTo", amountTo);
		String fuelSurchargeTo = input.getFuelSurchargeTo();
		params.put("fuelSurchargeTo", fuelSurchargeTo);
		// String demurrageChargeTo=input.getDemurrageChargeTo();
		params.put("demurrageChargeTo", demurrageChargeTo);
		// String tonnagePremiumTo=input.getTonnagePremiumTo();
		params.put("tonnagePremiumTo", tonnagePremiumTo);
		// String totAmtTo=input.getTotAmtTo();
		params.put("totAmtTo", totAmtTo);
		
		
		String company = input.getCompany();
		/*// Truck driver report
		String drillDownCompany = input.getDrillDownCompany();
		if (!StringUtils.isEmpty(drillDownCompany)) {
			company = drillDownCompany;
		}*/
		
		params.put("company", company);
		params.put("customer", input.getCustomer());

		String driverCompany = input.getDriverCompany();
		params.put("driverCompany",driverCompany);
		
		

		StringBuffer query = new StringBuffer("");
		StringBuffer countQuery = new StringBuffer("");
		/*
		 * query.append(
		 * "select obj from Ticket obj where obj.status=1 and obj.billBatch between '"
		 * +batchDateFrom+"' and '"+batchDateTo+"'");
		 */
		query.append("select obj from Ticket obj where (obj.status=1 OR obj.status=3) ");
		/*
		 * countQuery.append(
		 * "select count(obj) from Ticket obj where obj.status=1 and obj.billBatch between '"
		 * +batchDateFrom+"' and '"+batchDateTo+"'");
		 */
		countQuery
				.append("select count(obj) from Ticket obj where (obj.status=1 OR obj.status=3)");
		if (useInvoice) {
			query.append(" and obj.id in (" + ticketIds.toString() + ")");
			countQuery.append(" and obj.id in (" + ticketIds.toString() + ")");
		}

		if (!StringUtils.isEmpty(input.getCompany())) {

			query.append(" and obj.companyLocation.id in (").append(company)
					.append(")");
			countQuery.append(" and obj.companyLocation.id in (")
					.append(company).append(")");
		}
		
		if (!StringUtils.isEmpty(input.getDriverCompany())) {

			query.append(" and obj.driverCompany.id in (").append(driverCompany)
					.append(")");
			countQuery.append(" and obj.driverCompany.id in (")
					.append(driverCompany).append(")");
		}
		

		if (!StringUtils.isEmpty(input.getCustomer())) {

			query.append(" and obj.customer.id in (")
					.append(input.getCustomer()).append(")");
			countQuery.append(" and obj.customer.id in (")
					.append(input.getCustomer()).append(")");
		}

		//
		if (!StringUtils.isEmpty(batchDateTo)) {
			query.append(" and  obj.billBatch<='").append(batchDateTo + "'");
			countQuery.append(" and  obj.billBatch<='").append(
					batchDateTo + "'");
		}
		if (!StringUtils.isEmpty(batchDateFrom)) {
			query.append(" and  obj.billBatch>='").append(batchDateFrom + "'");
			countQuery.append(" and  obj.billBatch>='").append(
					batchDateFrom + "'");
		}

		if (!StringUtils.isEmpty(ticketStatus)) {
			query.append(" and  obj.ticketStatus in (").append(ticketStatus)
					.append(")");
			countQuery.append(" and  obj.ticketStatus in (")
					.append(ticketStatus).append(")");
		}
		/*if (StringUtils.isNotEmpty(notBillable)) {
			query.append(" and obj.notBillable='").append(notBillable).append("'");
			countQuery.append(" and obj.notBillable='").append(notBillable).append("'");
		}*/
		// For summary report
		if (StringUtils.isEmpty(notBillable) 
				|| StringUtils.equals(BillingHistoryInput.EXCLUDE_NOT_BILLABLE, notBillable)) {
			query.append(" and (obj.notBillable = '' or obj.notBillable is null)");
			countQuery.append(" and (obj.notBillable = '' or obj.notBillable is null)");
		}
		if (!StringUtils.isEmpty(fromDateInvoiceStr)) {
			query.append(" and  obj.invoiceDate>='").append(
					fromDateInvoiceStr + "'");
			countQuery.append(" and  obj.invoiceDate>='").append(
					fromDateInvoiceStr + "'");
		}
		if (!StringUtils.isEmpty(toDateInvoiceStr)) {
			query.append(" and  obj.invoiceDate<='").append(
					toDateInvoiceStr + "'");
			countQuery.append(" and  obj.invoiceDate<='").append(
					toDateInvoiceStr + "'");
		}
		
		if (!StringUtils.isEmpty(terminal)) {
			// System.out.println("\nTerminal===>"+terminal+"\n");
			query.append(" and  obj.terminal in (").append(terminal)
					.append(")");
			countQuery.append(" and  obj.terminal in (").append(terminal)
					.append(")");
		}
		if (!StringUtils.isEmpty(createdBy)) {
			query.append(" and  obj.createdBy in (").append(createdBy)
					.append(")");
			countQuery.append(" and  obj.createdBy in (").append(createdBy)
					.append(")");
		}
		if (!StringUtils.isEmpty(origin)) {
			query.append(" and  obj.origin.id in (").append(origin).append(")");
			countQuery.append(" and  obj.origin.id in (").append(origin)
					.append(")");
		}

		StringUtils.contains("194", destination);
		System.out.println("\n 194-->"
				+ StringUtils.contains("194", destination));
		System.out.println("\n destination-->" + destination);
		if (!StringUtils.isEmpty(destination)) {
			String[] elements = destination.split(",");

			boolean isGrowsTullyTown = false;
			for (int i = 0; i < elements.length; i++) {
				System.out.println(elements[i]);
				if (elements[i].contains("91")) {
					isGrowsTullyTown = true;					
				}
			}
			if (isGrowsTullyTown) {
				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows = genericDAO.getByCriteria(Location.class,
						criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown = genericDAO.getByCriteria(Location.class,
						criterias);
				query.append(" and  obj.destination.id in(")
						.append(destination + "," + grows.getId() + ","
								+ tullyTown.getId()).append(")");
				countQuery
						.append(" and  obj.destination.id in (")
						.append(destination + "," + grows.getId() + ","
								+ tullyTown.getId()).append(")");
			}

			else {
				query.append(" and  obj.destination.id in(")
						.append(destination).append(")");
				countQuery.append(" and  obj.destination.id in (")
						.append(destination).append(")");
			}

			/*
			 * query.append(" and  obj.destination.id in(").append(destination).
			 * append(")");
			 * countQuery.append(" and  obj.destination.id in (").append
			 * (destination).append(")");
			 */
		}
		if (!StringUtils.isEmpty(originTicketFrom)) {
			query.append(" and obj.originTicket >= ").append(originTicketFrom);
			countQuery.append(" and obj.originTicket >= ").append(
					originTicketFrom);
		}
		if (!StringUtils.isEmpty(originTicketTo)) {
			query.append(" and obj.originTicket <= ").append(originTicketTo);
			countQuery.append(" and obj.originTicket <= ").append(
					originTicketTo);
		}
		if (!StringUtils.isEmpty(destinationTicketFrom)) {
			query.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
			countQuery.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
		}
		if (!StringUtils.isEmpty(destinationTicketTo)) {
			query.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
			countQuery.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
		}
		if (!StringUtils.isEmpty(driver)) {
			query.append(" and  obj.driver.id in (").append(driver).append(")");
			countQuery.append(" and  obj.driver.id in (").append(driver)
					.append(")");
		}
		if (!StringUtils.isEmpty(truck)) {
			
			String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
				+truck
				+")";
			
			
			
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
			
			query.append(" and  obj.vehicle.id in (").append(truckIds).append(")");
			;
			countQuery.append(" and  obj.vehicle.id in (").append(truckIds)
					.append(")");
			;
		}
		
		
		if (!StringUtils.isEmpty(trailer)) {
			
			String vehiclequery="select obj from Vehicle obj where obj.type=2 and obj.unit in ("
				+trailer
				+")";
			
			
			
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
			
			
			query.append(" and  obj.trailer.id in (").append(trailerIds)
					.append(")");
			;
			countQuery.append(" and  obj.trailer.id in (").append(trailerIds)
					.append(")");
			;
		}
		
		
		if (!StringUtils.isEmpty(subcontractor)) {
			query.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
			countQuery.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
		}
		if (!StringUtils.isEmpty(originGrossWtFrom)) {
			query.append(" and  obj.transferGross>=").append(originGrossWtFrom);
			countQuery.append(" and  obj.transferGross>=").append(
					originGrossWtFrom);
		}
		if (!StringUtils.isEmpty(originGrossWtTo)) {
			query.append(" and  obj.transferGross<=").append(originGrossWtTo);
			countQuery.append(" and  obj.transferGross<=").append(
					originGrossWtTo);
		}
		if (!StringUtils.isEmpty(originTareWtFrom)) {
			query.append(" and  obj.transferTare>=").append(originTareWtFrom);
			countQuery.append(" and  obj.transferTare>=").append(
					originTareWtFrom);
		}
		if (!StringUtils.isEmpty(originTareWtTo)) {
			query.append(" and  obj.transferTare<=").append(originTareWtTo);
			countQuery.append(" and  obj.transferTare<=")
					.append(originTareWtTo);
		}
		if (!StringUtils.isEmpty(originTonWtFrom)) {
			query.append(" and  obj.transferTons>=").append(originTonWtFrom);
			countQuery.append(" and  obj.transferTons>=").append(
					originTonWtFrom);
		}
		if (!StringUtils.isEmpty(originTonWtTo)) {
			query.append(" and  obj.transferTons<=").append(originTonWtTo);
			countQuery.append(" and  obj.transferTons<=").append(originTonWtTo);
		}
		if (!StringUtils.isEmpty(landfillGrossWtFrom)) {
			query.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
			countQuery.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
		}
		if (!StringUtils.isEmpty(landfillGrossWtTo)) {
			query.append(" and  obj.landfillGross<=").append(landfillGrossWtTo);
			countQuery.append(" and  obj.landfillGross<=").append(
					landfillGrossWtTo);
		}
		if (!StringUtils.isEmpty(landfillTareWtFrom)) {
			query.append(" and  obj.landfillTare>=").append(landfillTareWtFrom);
			countQuery.append(" and  obj.landfillTare>=").append(
					landfillTareWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTareWtTo)) {
			query.append(" and  obj.landfillTare<=").append(landfillTareWtTo);
			countQuery.append(" and  obj.landfillTare<=").append(
					landfillTareWtTo);
		}
		if (!StringUtils.isEmpty(landfillTonWtFrom)) {
			query.append(" and  obj.landfillTons>=").append(landfillTonWtFrom);
			countQuery.append(" and  obj.landfillTons>=").append(
					landfillTonWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTonWtTo)) {
			query.append(" and  obj.landfillTons<=").append(landfillTonWtTo);
			countQuery.append(" and  obj.landfillTons<=").append(
					landfillTonWtTo);
		}
		if (!StringUtils.isEmpty(loadDateFrom)) {
			query.append(" and  obj.loadDate>='").append(loadDateFrom + "'");
			countQuery.append(" and  obj.loadDate>='").append(
					loadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(loadDateTo)) {
			query.append(" and  obj.loadDate<='").append(loadDateTo + "'");
			countQuery.append(" and  obj.loadDate<='").append(loadDateTo + "'");

		}
		if (!StringUtils.isEmpty(unloadDateFrom)) {
			query.append(" and  obj.unloadDate>='")
					.append(unloadDateFrom + "'");
			countQuery.append(" and  obj.unloadDate>='").append(
					unloadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(unloadDateTo)) {
			query.append(" and  obj.unloadDate<='").append(unloadDateTo + "'");
			countQuery.append(" and  obj.unloadDate<='").append(
					unloadDateTo + "'");
		}

		if (!StringUtils.isEmpty(transferTimeInFrom)
				&& !StringUtils.isEmpty(transferTimeInTo)) {
			query.append(" and  obj.transferTimeIn Between '")
					.append(transferTimeInFrom + "'").append(" and '")
					.append(transferTimeInTo + "'");
			countQuery.append(" and  obj.transferTimeIn Between '")
					.append(transferTimeInFrom + "'").append(" and '")
					.append(transferTimeInTo + "'");

		}
		if (!StringUtils.isEmpty(transferTimeOutFrom)
				&& !StringUtils.isEmpty(transferTimeOutTo)) {
			query.append(" and  obj.transferTimeOut Between '")
					.append(transferTimeOutFrom + "'").append(" and '")
					.append(transferTimeOutTo + "'");
			countQuery.append(" and  obj.transferTimeOut Between '")
					.append(transferTimeOutFrom + "'").append(" and '")
					.append(transferTimeOutTo + "'");
		}

		if (!StringUtils.isEmpty(landfillTimeInFrom)
				&& !StringUtils.isEmpty(landfillTimeInTo)) {
			query.append(" and  obj.landfillTimeIn Between '")
					.append(landfillTimeInFrom + "'").append(" and '")
					.append(landfillTimeInTo + "'");
			countQuery.append(" and  obj.landfillTimeIn Between '")
					.append(landfillTimeInFrom + "'").append(" and '")
					.append(landfillTimeInTo + "'");

		}
		if (!StringUtils.isEmpty(landfillTimeOutFrom)
				&& !StringUtils.isEmpty(landfillTimeOutTo)) {
			query.append(" and  obj.landfillTimeOut Between '")
					.append(landfillTimeOutFrom + "'").append(" and '")
					.append(landfillTimeOutTo + "'");
			countQuery.append(" and  obj.landfillTimeOut Between '")
					.append(landfillTimeOutFrom + "'").append(" and '")
					.append(landfillTimeOutTo + "'");
		}

		
		//Long recordCount = (Long) genericDAO.getEntityManager()
				//.createQuery(countQuery.toString()).getSingleResult();
		//searchCriteria.setRecordCount(recordCount.intValue());
		//System.out.println("\nrecordCount=>" + recordCount.intValue() + "\n");
		query.append(" order by billBatch desc, id");
		List<Ticket> tickets = (List<Ticket>) genericDAO
				.getEntityManager()
				.createQuery(query.toString())				
				.getResultList();
		// ticketIds1=null;
		ticketIds = null;
		
		// Truck driver report
		if (!StringUtils.isEmpty(input.getDrillDownCompany())) {
			return processTicketsForSummaryTruckDriver(tickets, input.getDrillDownCompany(), 
					input.getDrillDownOrigin(), 
					input.getDrillDownDestination(),
					isPeakRate); // Peak rate 2nd Feb 2021
		} else {
			return processTicketsForSummary(tickets, reportParams, isPeakRate); // Peak rate 2nd Feb 2021
		}
	}
	
	
/*private List<Summary> processTicketsForSummary(List<Ticket> tickets,Map<String, String> params) {
		
		 StringBuffer ticketIds = new StringBuffer("-1,");
			for (Ticket tkt : tickets) {
				ticketIds.append(tkt.getId()).append(",");
			}
			if (ticketIds.indexOf(",") != -1) {
				ticketIds.deleteCharAt(ticketIds.length() - 1);
			}			
			
			String query="select obj.t_origin,obj.t_destination ,count(obj), sum(amount), obj.company, sum(effectiveTonsWt), count(distinct t_unit), "
					+ " sum(totAmt), sum(fuelSurcharge), sum(driverPayRate) from Billing_New obj where obj.ticket in ("
				+ticketIds.toString()+") group by t_origin,t_destination " + 
				// Billing History Summary - Order fix	
				//" order by obj.t_origin asc,obj.t_destination asc";
				" order by obj.company asc, obj.t_origin asc, obj.t_destination asc";
			List<Summary> summarys=genericDAO.executeSimpleQuery(query);						
		
	
		return summarys;
}*/
	
	private List<Summary> processTicketsForSummary(List<Ticket> tickets, Map<String, Object> reportParams,
			String isPeakRate) { // Peak rate 2nd Feb 2021
		List<Summary> returnSummaryList = new ArrayList<Summary>();
		if (tickets == null || tickets.isEmpty()) {
			return returnSummaryList;
		}
		
		StringBuffer ticketIds = new StringBuffer("-1,");
		for (Ticket tkt : tickets) {
			ticketIds.append(tkt.getId()).append(",");
		}
		
		if (ticketIds.indexOf(",") != -1) {
			ticketIds.deleteCharAt(ticketIds.length() - 1);
		}			
		
		String query = "select obj.t_origin, obj.t_destination, count(obj), sum(amount), obj.company, sum(effectiveTonsWt), count(distinct t_unit), "
				+ " sum(totAmt), sum(fuelSurcharge), sum(driverPayRate) from Billing_New obj where obj.ticket in ("
			+ ticketIds.toString()+")";
		
		// Peak rate 2nd Feb 2021
		if (StringUtils.isNotEmpty(isPeakRate)) {
			query += (" and obj.isPeakRate='" + isPeakRate + "'");
		}
		
		query	+= (" group by t_origin,t_destination "); 
			// Billing History Summary - Order fix	
			//" order by obj.t_origin asc,obj.t_destination asc";
		query	+= (" order by obj.company asc, obj.t_origin asc, obj.t_destination asc");
		
		List<Summary> summarys = genericDAO.executeSimpleQuery(query);	
		if (summarys == null || summarys.isEmpty()) {
			return returnSummaryList;
		}
		
		for (Object obj : summarys) {						
			Object[] objArry = (Object[]) obj;
			Summary summary = new Summary();
			
			if (objArry[0]!=null)
				summary.setOrigin(objArry[0].toString());
			if (objArry[1]!=null)
				summary.setDestination(objArry[1].toString());
			if (objArry[2]!=null)
				summary.setCount(Integer.parseInt(objArry[2].toString()));
			if (objArry[3]!=null)
				summary.setAmount(Double.parseDouble(objArry[3].toString()));
			if (objArry[4]!=null)
				summary.setCompany(objArry[4].toString());
			if (objArry[5] != null) {
				Double billableTon = Double.parseDouble(objArry[5].toString());
				billableTon = MathUtil.roundUp(billableTon, 2);
				summary.setBillableTons(billableTon);
			}
			if (objArry[6] != null) {
				summary.setCountTrucks(Integer.parseInt(objArry[6].toString()));
			}
			if (objArry[7] != null) {
				summary.setNetAmount(Double.parseDouble(objArry[7].toString()));
			}
			if (objArry[8] != null) {
				summary.setFuelSurcharge(Double.parseDouble(objArry[8].toString()));
			}
			if (objArry[9] != null) {
				summary.setDriverPay(Double.parseDouble(objArry[9].toString()));
			}
			
			// Truck driver report
			/*String truckDriverInfo = retrieveBillingTruckDriverInfoAsString(ticketIds.toString(), summary.getCompany(), 
					summary.getOrigin(), summary.getDestination());
			summary.setTruckDriverInfo(truckDriverInfo);*/
			
			returnSummaryList.add(summary);
		}
		
		// Peak rate 2nd Feb 2021
		Long peakRateCount = 0l;
		Long regularRateCount = 0l;
		String rateTypeCountQuery = "select count(obj) from Billing_New obj where obj.ticket in ("
			+ ticketIds.toString()+")";
		String rateTypeCountQueryCondn = " and obj.isPeakRate=";
		
		List<Summary> rateTypeCountSummary = genericDAO.executeSimpleQuery(rateTypeCountQuery 
				+ rateTypeCountQueryCondn + "'Y'");
		for (Object obj : rateTypeCountSummary) {						
			peakRateCount = (Long)obj;
		}
		
		rateTypeCountSummary = genericDAO.executeSimpleQuery(rateTypeCountQuery 
				+ rateTypeCountQueryCondn + "'N'");
		for (Object obj : rateTypeCountSummary) {						
			regularRateCount = (Long)obj;
		}
		
		// Peak rate 2nd Feb 2021
		List<Map<String, Object>> rateTypeCountMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> rateTypeCountMap = new HashMap<String, Object>();
		rateTypeCountMap.put("rateType", "Regular Rate");
		rateTypeCountMap.put("count", regularRateCount);
		rateTypeCountMapList.add(rateTypeCountMap);
		rateTypeCountMap = new HashMap<String, Object>();
		rateTypeCountMap.put("rateType", "Peak Rate");
		rateTypeCountMap.put("count", peakRateCount);
		rateTypeCountMapList.add(rateTypeCountMap);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(rateTypeCountMapList);
		reportParams.put("rateTypeCountMap", jrMap);
	
		return returnSummaryList;
	}
	
	// Truck driver report
	private List<Summary> processTicketsForSummaryTruckDriver(List<Ticket> tickets, String drillDownCompany, String drillDownOrigin, 
			String drillDownDestination, String isPeakRate) { // Peak rate 2nd Feb 2021
		List<Summary> returnSummaryList = new ArrayList<Summary>();
		if (tickets == null || tickets.isEmpty()) {
			return returnSummaryList;
		}
		
		StringBuffer ticketIds = new StringBuffer("-1,");
		for (Ticket tkt : tickets) {
			ticketIds.append(tkt.getId()).append(",");
		}
		
		if (ticketIds.indexOf(",") != -1) {
			ticketIds.deleteCharAt(ticketIds.length() - 1);
		}
		
		List<Summary> summarys = retrieveBillingTruckDriverInfo(ticketIds.toString(), drillDownCompany, drillDownOrigin, 
				drillDownDestination, isPeakRate); // Peak rate 2nd Feb 2021
		if (summarys == null || summarys.isEmpty()) {
			return returnSummaryList;
		}
		
		for (Object obj : summarys) {						
			Object[] objArry = (Object[]) obj;
			Summary summary = new Summary();
			
			if (objArry[0] != null) {
				summary.setTruck(objArry[0].toString());
			}
			if (objArry[1] != null) {
				summary.setDriver(objArry[1].toString());
			}
			
			returnSummaryList.add(summary);
		}
		
		return returnSummaryList;
	}
	
	// Truck driver report
	private List<Summary> retrieveBillingTruckDriverInfo(String ticketIds, String company, String origin,
			String destination, String isPeakRate) { // Peak rate 2nd Feb 2021
		String query = "select distinct obj.t_unit, obj.driver"
				+ " from Billing_New obj "
				+ " where obj.ticket in ("+ticketIds+")"
				+ " and obj.company='" + company + "'"
				+ " and obj.t_origin='" + origin + "'"
				+ " and obj.t_destination='" + destination + "'";
		
		// Peak rate 2nd Feb 2021
		if (StringUtils.isNotEmpty(isPeakRate)) {
			query += (" and obj.isPeakRate='" + isPeakRate + "'");
		}
				
		query += (" order by CAST(obj.t_unit AS int), obj.driver");
		
		List<Summary> summarys = genericDAO.executeSimpleQuery(query);
		return summarys;
	}
	
	/*
	// Truck driver report
	private String retrieveBillingTruckDriverInfoAsString(String ticketIds, String company, String origin,
			String destination) {
		StringBuffer truckDriverInfo = new StringBuffer();
		
		List<Summary> summarys = retrieveBillingTruckDriverInfo(ticketIds, company, origin, destination);
		if (summarys == null || summarys.isEmpty()) {
			return truckDriverInfo.toString();
		}
		
		for (Object obj : summarys) {						
			Object[] objArry = (Object[]) obj;
			
			if (objArry[0] != null) {
				truckDriverInfo.append(objArry[0].toString());
			}
			if (objArry[1] != null) {
				truckDriverInfo.append(":" + objArry[1].toString() + "|");
			}
		}
		
		if (truckDriverInfo.length() != 0) {
			truckDriverInfo.deleteCharAt(truckDriverInfo.length() - 1);
		}
		return truckDriverInfo.toString();
	}*/
	
	@Override
	public List<Summary> generateSummary(SearchCriteria criteria,
			BillingHistoryInput input) {

		String fromDateInvoiceStr = ReportDateUtil.getFromDate(input
				.getFromInvoiceDate());
		String toDateInvoiceStr = ReportDateUtil.getToDate(input
				.getInvoiceDateTo());
		String invoiceNumberFrom = input.getInvoiceNumberFrom();
		String invoiceNumberTo = input.getInvoiceNumberTo();

		String fuelSurchargeFrom1 = input.getFuelSurchargeFrom();
		String fuelSurchargeTo1 = input.getFuelSurchargeTo();

		String tonnagePremiumFrom = input.getTonnagePremiumFrom();
		String tonnagePremiumTo = input.getTonnagePremiumTo();

		String demurrageChargeFrom = input.getDemurrageChargeFrom();
		String demurrageChargeTo = input.getDemurrageChargeTo();

		String amountFrom = input.getAmountFrom();
		String amountTo = input.getAmountTo();

		String totAmtFrom = input.getTotAmtFrom();
		String totAmtTo = input.getTotAmtTo();

		String rateFrom = input.getRateFrom();
		String rateTo = input.getRateTo();

		boolean useInvoice = false;
		StringBuffer ticketIds = new StringBuffer("-1,");
		if ((!StringUtils.isEmpty(input.getFromInvoiceDate()))
				|| (!StringUtils.isEmpty(input.getInvoiceDateTo()))
				|| (!StringUtils.isEmpty(invoiceNumberFrom))
				|| (!StringUtils.isEmpty(invoiceNumberTo))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeFrom()))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeTo()))
				|| (!StringUtils.isEmpty(tonnagePremiumFrom))
				|| (!StringUtils.isEmpty(tonnagePremiumTo))
				|| (!StringUtils.isEmpty(demurrageChargeFrom))
				|| (!StringUtils.isEmpty(demurrageChargeTo))
				|| (!StringUtils.isEmpty(amountFrom))
				|| (!StringUtils.isEmpty(amountTo))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(input.getRateFrom()))
				|| (!StringUtils.isEmpty(input.getRateTo()))) {
			StringBuffer query2 = new StringBuffer(
					"select bill.ticket from Invoice inv, Billing bill where 1=1 and bill.invoiceNo = inv.invoiceNumber");

			if (!StringUtils.isEmpty(input.getRateFrom())) {

				query2.append(" and bill.rate>=").append(rateFrom);
			}
			if (!StringUtils.isEmpty(input.getRateTo())) {

				query2.append(" and bill.rate<=").append(rateTo);
			}

			if (!StringUtils.isEmpty(fromDateInvoiceStr)) {
				query2.append(" and  inv.invoiceDate>='").append(
						fromDateInvoiceStr + "'");
			}
			if (!StringUtils.isEmpty(toDateInvoiceStr)) {
				query2.append(" and  inv.invoiceDate<='").append(
						toDateInvoiceStr + "'");
			}
			if (!StringUtils.isEmpty(invoiceNumberFrom)) {
				query2.append(" and  bill.invoiceNo>='").append(
						invoiceNumberFrom + "'");
			}
			if (!StringUtils.isEmpty(invoiceNumberTo)) {
				query2.append(" and  bill.invoiceNo<='").append(
						invoiceNumberTo + "'");
			}

			if (!StringUtils.isEmpty(fuelSurchargeFrom1)) {
				query2.append(" and bill.fuelSurcharge >= '").append(
						fuelSurchargeFrom1 + "'");
			}
			if (!StringUtils.isEmpty(fuelSurchargeTo1)) {
				query2.append(" and bill.fuelSurcharge <= '").append(
						fuelSurchargeTo1 + "'");
			}
			// /hereeee

			if (!StringUtils.isEmpty(tonnagePremiumFrom)) {
				query2.append(" and bill.tonnagePremium >= ").append(
						tonnagePremiumFrom);
			}
			if (!StringUtils.isEmpty(tonnagePremiumTo)) {
				query2.append(" and bill.tonnagePremium <= ").append(
						tonnagePremiumTo);
			}

			if (!StringUtils.isEmpty(demurrageChargeFrom)) {
				query2.append(" and bill.demurrageCharge >= ").append(
						demurrageChargeFrom);
			}
			if (!StringUtils.isEmpty(demurrageChargeTo)) {
				query2.append(" and bill.demurrageCharge <= ").append(
						demurrageChargeTo);
			}
			if (!StringUtils.isEmpty(amountFrom)) {
				query2.append(" and bill.amount >= ").append(amountFrom);
			}
			if (!StringUtils.isEmpty(amountTo)) {
				query2.append(" and bill.amount <= ").append(amountTo);
			}
			if (!StringUtils.isEmpty(totAmtFrom)) {
				query2.append(" and bill.totAmt >= ").append(totAmtFrom);
			}
			if (!StringUtils.isEmpty(totAmtTo)) {
				query2.append(" and bill.totAmt <= ").append(totAmtTo);
			}

			useInvoice = true;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(query2
					.toString());
			System.out.println("\nquery2.toString()===>" + query2.toString()
					+ "\n");
			System.out.println("\ntickets.size===>" + tickets.size() + "\n");
			if (tickets != null && tickets.size() > 0) {
				for (Ticket tkt : tickets) {
					ticketIds.append(tkt.getId()).append(",");
				}
			}
			if (ticketIds.indexOf(",") != -1) {
				ticketIds.deleteCharAt(ticketIds.length() - 1);
			}
		}
		Map<String, String> params = new HashMap<String, String>();
		String batchDateFrom = ReportDateUtil.getFromDate(input
				.getBatchDateFrom());
		String batchDateTo = ReportDateUtil.getToDate(input.getBatchDateTo());
		String loadDateFrom = ReportDateUtil.getFromDate(input.getLoadedFrom());
		String loadDateTo = ReportDateUtil.getToDate(input.getLoadedTo());
		String unloadDateFrom = ReportDateUtil.getFromDate(input
				.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());

		String ticketStatus = input.getTicketStatus();
		String terminal = input.getTerminal();
		String createdBy = input.getCreatedBy();
		String origin = input.getOrigin();
		String destination = input.getDestination();
		String driver = input.getDriver();
		String truck = input.getTruck();
		String trailer = input.getTrailer();
		String subcontractor = input.getSubcontractor();

		String originTicketFrom = input.getOriginTicketFrom();
		String destinationTicketFrom = input.getDestinationTicketFrom();
		String originGrossWtFrom = input.getOriginGrossWtFrom();
		String originTareWtFrom = input.getOriginTareWtFrom();
		String originTonWtFrom = input.getOriginTonsWtFrom();
		String landfillGrossWtFrom = input.getLandfillGrossWtFrom();
		String landfillTareWtFrom = input.getLandfillTareWtFrom();
		String landfillTonWtFrom = input.getLandfillTonsWtFrom();

		String transferTimeInFrom = input.getTransferTimeInFrom();
		String transferTimeInTo = input.getTransferTimeInTo();
		String transferTimeOutFrom = input.getTransferTimeOutFrom();
		String transferTimeOutTo = input.getTransferTimeOutTo();

		String landfillTimeInFrom = input.getLandfillTimeInFrom();
		String landfillTimeInTo = input.getLandfillTimeInTo();
		String landfillTimeOutFrom = input.getLandfillTimeOutFrom();
		String landfillTimeOutTo = input.getLandfillTimeOutTo();

		String originTicketTo = input.getOriginTicketTo();
		String destinationTicketTo = input.getDestinationTicketTo();
		String originGrossWtTo = input.getOriginGrossWtTo();
		String originTareWtTo = input.getOriginTareWtTo();
		String originTonWtTo = input.getOriginTonsWtTo();
		String landfillGrossWtTo = input.getLandfillGrossWtTo();
		String landfillTareWtTo = input.getLandfillTareWtTo();
		String landfillTonWtTo = input.getLandfillTonsWtTo();

		rateFrom = input.getRateFrom();
		params.put("rateFrom", rateFrom);

		params.put("amountFrom", amountFrom);
		String fuelSurchargeFrom = input.getFuelSurchargeFrom();
		params.put("fuelSurchargeFrom", fuelSurchargeFrom);

		params.put("tonnagePremiumFrom", tonnagePremiumFrom);

		params.put("demurrageChargeFrom", demurrageChargeFrom);

		params.put("totAmtFrom", totAmtFrom);

		rateTo = input.getRateTo();
		params.put("rateTo", rateTo);

		params.put("amountTo", amountTo);
		String fuelSurchargeTo = input.getFuelSurchargeTo();
		params.put("fuelSurchargeTo", fuelSurchargeTo);

		params.put("demurrageChargeTo", demurrageChargeTo);

		params.put("tonnagePremiumTo", tonnagePremiumTo);

		params.put("totAmtTo", totAmtTo);
		
		String company = input.getCompany();
		params.put("company", company);		
		
		
		String driverCompany = input.getDriverCompany();
		params.put("driverCompany",driverCompany);
		
		
		params.put("customer", input.getCustomer());

		StringBuffer query = new StringBuffer("");

		query.append("select  obj.origin.name,obj.destination.name ,count(obj) from Ticket obj where obj.status=1 ");

		if (useInvoice) {
			query.append(" and obj.id in (" + ticketIds.toString() + ")");

		}

		if (!StringUtils.isEmpty(input.getCompany())) {

			query.append(" and obj.companyLocation.id in (").append(company)
					.append(")");

		}
		
		
		if (!StringUtils.isEmpty(input.getDriverCompany())) {

			query.append(" and obj.driverCompany.id in (").append(driverCompany)
					.append(")");			
		}
		
		

		if (!StringUtils.isEmpty(input.getCustomer())) {

			query.append(" and obj.customer.id in (")
					.append(input.getCustomer()).append(")");

		}

		if (!StringUtils.isEmpty(batchDateTo)) {
			query.append(" and  obj.billBatch<='").append(batchDateTo + "'");

		}
		if (!StringUtils.isEmpty(batchDateFrom)) {
			query.append(" and  obj.billBatch>='").append(batchDateFrom + "'");

		}

		if (!StringUtils.isEmpty(ticketStatus)) {
			query.append(" and  obj.ticketStatus in (").append(ticketStatus)
					.append(")");

		}

		if (!StringUtils.isEmpty(terminal)) {

			query.append(" and  obj.terminal in (").append(terminal)
					.append(")");

		}
		if (!StringUtils.isEmpty(createdBy)) {
			query.append(" and  obj.createdBy in (").append(createdBy)
					.append(")");

		}
		if (!StringUtils.isEmpty(origin)) {
			query.append(" and  obj.origin.id in (").append(origin).append(")");
		}

		if (!StringUtils.isEmpty(destination)) {
			if (destination.equalsIgnoreCase("91")) {
				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows = genericDAO.getByCriteria(Location.class,
						criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown = genericDAO.getByCriteria(Location.class,
						criterias);

				query.append("and  obj.destination in(" + grows.getId() + ","
						+ tullyTown.getId() + ")");
			} else {

				query.append("and  obj.destination=").append(destination);
			}
		}
		/*
		 * if (!StringUtils.isEmpty(destination)) {
		 * query.append(" and  obj.destination.id in("
		 * ).append(destination).append(")"); }
		 */
		if (!StringUtils.isEmpty(originTicketFrom)) {
			query.append(" and obj.originTicket >= ").append(originTicketFrom);
		}
		if (!StringUtils.isEmpty(originTicketTo)) {
			query.append(" and obj.originTicket <= ").append(originTicketTo);
		}
		if (!StringUtils.isEmpty(destinationTicketFrom)) {
			query.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
		}
		if (!StringUtils.isEmpty(destinationTicketTo)) {
			query.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
		}
		if (!StringUtils.isEmpty(driver)) {
			query.append(" and  obj.driver.id in (").append(driver).append(")");
		}
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
			
			query.append(" and  obj.vehicle.id in (").append(truckIds).append(")");
			;
		}
		if (!StringUtils.isEmpty(trailer)){
			
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
			
			query.append(" and  obj.trailer.id in (").append(trailerIds)
					.append(")");
			;
		}
		if (!StringUtils.isEmpty(subcontractor)) {
			query.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
		}
		if (!StringUtils.isEmpty(originGrossWtFrom)) {
			query.append(" and  obj.transferGross>=").append(originGrossWtFrom);
		}
		if (!StringUtils.isEmpty(originGrossWtTo)) {
			query.append(" and  obj.transferGross<=").append(originGrossWtTo);
		}
		if (!StringUtils.isEmpty(originTareWtFrom)) {
			query.append(" and  obj.transferTare>=").append(originTareWtFrom);
		}
		if (!StringUtils.isEmpty(originTareWtTo)) {
			query.append(" and  obj.transferTare<=").append(originTareWtTo);
		}
		if (!StringUtils.isEmpty(originTonWtFrom)) {
			query.append(" and  obj.transferTons>=").append(originTonWtFrom);
		}
		if (!StringUtils.isEmpty(originTonWtTo)) {
			query.append(" and  obj.transferTons<=").append(originTonWtTo);
		}
		if (!StringUtils.isEmpty(landfillGrossWtFrom)) {
			query.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
		}
		if (!StringUtils.isEmpty(landfillGrossWtTo)) {
			query.append(" and  obj.landfillGross<=").append(landfillGrossWtTo);
		}
		if (!StringUtils.isEmpty(landfillTareWtFrom)) {
			query.append(" and  obj.landfillTare>=").append(landfillTareWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTareWtTo)) {
			query.append(" and  obj.landfillTare<=").append(landfillTareWtTo);
		}
		if (!StringUtils.isEmpty(landfillTonWtFrom)) {
			query.append(" and  obj.landfillTons>=").append(landfillTonWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTonWtTo)) {
			query.append(" and  obj.landfillTons<=").append(landfillTonWtTo);
		}
		if (!StringUtils.isEmpty(loadDateFrom)) {
			query.append(" and  obj.loadDate>='").append(loadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(loadDateTo)) {
			query.append(" and  obj.loadDate<='").append(loadDateTo + "'");

		}
		if (!StringUtils.isEmpty(unloadDateFrom)) {
			query.append(" and  obj.unloadDate>='")
					.append(unloadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(unloadDateTo)) {
			query.append(" and  obj.unloadDate<='").append(unloadDateTo + "'");
		}

		if (!StringUtils.isEmpty(transferTimeInFrom)
				&& !StringUtils.isEmpty(transferTimeInTo)) {
			query.append(" and  obj.transferTimeIn Between '")
					.append(transferTimeInFrom + "'").append(" and '")
					.append(transferTimeInTo + "'");

		}
		if (!StringUtils.isEmpty(transferTimeOutFrom)
				&& !StringUtils.isEmpty(transferTimeOutTo)) {
			query.append(" and  obj.transferTimeOut Between '")
					.append(transferTimeOutFrom + "'").append(" and '")
					.append(transferTimeOutTo + "'");
		}

		if (!StringUtils.isEmpty(landfillTimeInFrom)
				&& !StringUtils.isEmpty(landfillTimeInTo)) {
			query.append(" and  obj.landfillTimeIn Between '")
					.append(landfillTimeInFrom + "'").append(" and '")
					.append(landfillTimeInTo + "'");

		}
		if (!StringUtils.isEmpty(landfillTimeOutFrom)
				&& !StringUtils.isEmpty(landfillTimeOutTo)) {
			query.append(" and  obj.landfillTimeOut Between '")
					.append(landfillTimeOutFrom + "'").append(" and '")
					.append(landfillTimeOutTo + "'");
		}
		query.append(" group by origin,destination order by obj.origin.name asc,obj.destination.name asc");

		List<Summary> list = genericDAO.executeSimpleQuery(query.toString());

		return list;
	}

	@Override
	// Subcontractor summary report - 16thMar2016
	public SubcontractorBillingWrapper generateSubcontractorReportData(
			SearchCriteria searchCriteria, SubcontractorReportInput input, boolean isSummary) {

		String amountFrom = input.getAmountFrom();
		String amountTo = input.getAmountTo();
		String rateFrom = input.getRateFrom();
		String rateTo = input.getRateTo();
		String fuelSurchargeFrom = input.getFuelSurchargeFrom();
		String fuelSurchargeTo = input.getFuelSurchargeTo();
		String totAmtFrom = input.getTotAmtFrom();
		String totAmtTo = input.getTotAmtTo();
		String fromDateVoucherStr = ReportDateUtil.getFromDate(input
				.getFromVoucherDate());
		String toDateVoucherStr = ReportDateUtil.getToDate(input
				.getVoucherDateTo());
		String voucherNumberFrom = input.getVoucherNumberFrom();
		String voucherNumberTo = input.getVoucherNumberTo();
		String misceCharge = input.getMiscelleneousCharges();
		
		

		boolean useVoucher = false;
		String ticketIds = "-1";
		if ((!StringUtils.isEmpty(input.getFromVoucherDate()))
				|| (!StringUtils.isEmpty(input.getVoucherDateTo())) || (!StringUtils.isEmpty(misceCharge))){
			
			StringBuffer query2 = new StringBuffer(
			"select tckt from Ticket tckt, SubcontractorInvoice inv where 1=1 and tckt.voucherDate=inv.voucherDate");

			

			if (!StringUtils.isEmpty(fromDateVoucherStr)) {
				query2.append(" and  inv.voucherDate>='").append(
				fromDateVoucherStr + "'");
			}
			if (!StringUtils.isEmpty(toDateVoucherStr)) {
				query2.append(" and  inv.voucherDate<='").append(
				toDateVoucherStr + "'");
			}
	
	        if (!StringUtils.isEmpty(misceCharge)) {		
		       query2.append(" and inv.miscelleneousCharges ='").append(
				misceCharge + "'");
		    }		
			
	        useVoucher = true;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(query2
					.toString());
			System.out.println("\nquery2.toString()===>" + query2.toString()
					+ "\n");
			System.out.println("\ntickets.size===>" + tickets.size() + "\n");
			if (tickets != null && tickets.size() > 0) {
				for (Ticket tkt : tickets) {
					if(ticketIds.equals("")){
					  ticketIds = tkt.getId().toString();
					}
					else{
					  ticketIds =ticketIds+","+ tkt.getId().toString();
					}
				}
			}
			
		}
		
		
		
		if ((!StringUtils.isEmpty(voucherNumberFrom))
				|| (!StringUtils.isEmpty(voucherNumberTo))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeFrom()))
				|| (!StringUtils.isEmpty(input.getFuelSurchargeTo()))
				|| (!StringUtils.isEmpty(amountFrom))
				|| (!StringUtils.isEmpty(amountTo))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(totAmtFrom))
				|| (!StringUtils.isEmpty(input.getRateFrom()))
				|| (!StringUtils.isEmpty(input.getRateTo()))) {
			StringBuffer query2 = new StringBuffer(
					"select bill.ticket from SubcontractorBilling bill where 1=1 ");

			if (!StringUtils.isEmpty(input.getRateFrom())) {

				query2.append(" and bill.rate>=").append(rateFrom);
			}
			if (!StringUtils.isEmpty(input.getRateTo())) {

				query2.append(" and bill.rate<=").append(rateTo);
			}

			
			
			if (!StringUtils.isEmpty(voucherNumberFrom)) {
				query2.append(" and  bill.invoiceNo>='").append(
						voucherNumberFrom + "'");
			}
			if (!StringUtils.isEmpty(voucherNumberTo)) {
				query2.append(" and  bill.invoiceNo<='").append(
						voucherNumberTo + "'");
			}

			if (!StringUtils.isEmpty(fuelSurchargeFrom)) {
				query2.append(" and bill.fuelSurcharge >= '").append(
						fuelSurchargeFrom + "'");
			}
			if (!StringUtils.isEmpty(fuelSurchargeTo)) {
				query2.append(" and bill.fuelSurcharge <= '").append(
						fuelSurchargeTo + "'");
			}

			if (!StringUtils.isEmpty(amountFrom)) {
				query2.append(" and bill.amount >= ").append(amountFrom);
			}
			if (!StringUtils.isEmpty(amountTo)) {
				query2.append(" and bill.amount <= ").append(amountTo);
			}
			if (!StringUtils.isEmpty(totAmtFrom)) {
				query2.append(" and bill.totAmt >= ").append(totAmtFrom);
			}
			if (!StringUtils.isEmpty(totAmtTo)) {
				query2.append(" and bill.totAmt <= ").append(totAmtTo);
			}
			

			useVoucher = true;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(query2
					.toString());
			System.out.println("\nquery2.toString()===>" + query2.toString()
					+ "\n");
			System.out.println("\ntickets.size===>" + tickets.size() + "\n");
			if (tickets != null && tickets.size() > 0) {
				for (Ticket tkt : tickets) {
					if(ticketIds.equals("")){
						  ticketIds = tkt.getId().toString();
						}
						else{
						  ticketIds =ticketIds+","+ tkt.getId().toString();
						}
				}
			}
			

		}

		String batchDateFrom = ReportDateUtil.getFromDate(input.getBatchDateFrom());
		String batchDateTo = ReportDateUtil.getToDate(input.getBatchDateTo());
		
		String voucherStatus = input.getVoucherStatus();
		String terminal = input.getTerminal();
		String subcontractor = input.getSubcontractor();
		String company = input.getCompany();
		String origin = input.getOrigin();
		String originTicketFrom = input.getOriginTicketFrom();
		String originTicketTo = input.getOriginTicketTo();
		String loadDateFrom = ReportDateUtil.getFromDate(input.getLoadedFrom());
		String loadDateTo = ReportDateUtil.getToDate(input.getLoadedTo());
		String originGrossWtFrom = input.getOriginGrossWtFrom();
		String originGrossWtTo = input.getOriginGrossWtTo();
		String originTareWtFrom = input.getOriginTareWtFrom();
		String originTareWtTo = input.getOriginTareWtTo();
		String originTonWtFrom = input.getOriginTonsWtFrom();
		String originTonWtTo = input.getOriginTonsWtTo();
		String destination = input.getDestination();
		String destinationTicketFrom = input.getDestinationTicketFrom();
		String destinationTicketTo = input.getDestinationTicketTo();
		String unloadDateFrom = ReportDateUtil.getFromDate(input
				.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());
		String landfillGrossWtFrom = input.getLandfillGrossWtFrom();
		String landfillGrossWtTo = input.getLandfillGrossWtTo();
		String landfillTareWtFrom = input.getLandfillTareWtFrom();
		String landfillTareWtTo = input.getLandfillTareWtTo();
		String landfillTonWtFrom = input.getLandfillTonsWtFrom();
		String landfillTonWtTo = input.getLandfillTonsWtTo();

		StringBuffer query = new StringBuffer("");
		StringBuffer countQuery = new StringBuffer("");
		query.append("select obj from Ticket obj where (obj.status=1 or obj.status=3) and obj.subcontractor is not null ");
		countQuery
				.append("select count(obj) from Ticket obj where (obj.status=1 or obj.status=3) and obj.subcontractor is not null");
		if (useVoucher) {
			query.append(" and obj.id in (" + ticketIds + ")");
			countQuery.append(" and obj.id in (" + ticketIds + ")");
		}

		if (!StringUtils.isEmpty(voucherStatus)) {
			query.append(" and  obj.voucherStatus in (").append(voucherStatus)
					.append(")");
			countQuery.append(" and  obj.voucherStatus in (")
					.append(voucherStatus).append(")");
		}
		
		
		if (!StringUtils.isEmpty(batchDateFrom)) {
			query.append(" and  obj.billBatch>='").append(batchDateFrom + "'");
			countQuery.append(" and  obj.billBatch>='").append(
					batchDateFrom + "'");
		}
		
		if (!StringUtils.isEmpty(batchDateTo)) {
			query.append(" and  obj.billBatch<='").append(batchDateTo + "'");
			countQuery.append(" and  obj.billBatch<='").append(
					batchDateTo + "'");
		}
		
		
		if (!StringUtils.isEmpty(terminal)) {

			query.append(" and  obj.terminal in (").append(terminal)
					.append(")");
			countQuery.append(" and  obj.terminal in (").append(terminal)
					.append(")");
		}
		if (!StringUtils.isEmpty(subcontractor)) {
			query.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
			countQuery.append(" and  obj.subcontractor.id in (")
					.append(subcontractor).append(")");
			;
		}
		// Uncommented as part of subcontrator summary revenue fix - 8th July 2016
		if (!StringUtils.isEmpty(input.getCompany())) {

			query.append(" and obj.companyLocation.id in (").append(company)
					.append(")");
			countQuery.append(" and obj.companyLocation.id in (")
					.append(company).append(")");
		}
		if (!StringUtils.isEmpty(origin)) {
			query.append(" and  obj.origin.id in (").append(origin).append(")");
			countQuery.append(" and  obj.origin.id in (").append(origin)
					.append(")");
		}
		if (!StringUtils.isEmpty(originTicketFrom)) {
			query.append(" and obj.originTicket >= ").append(originTicketFrom);
			countQuery.append(" and obj.originTicket >= ").append(
					originTicketFrom);
		}
		if (!StringUtils.isEmpty(originTicketTo)) {
			query.append(" and obj.originTicket <= ").append(originTicketTo);
			countQuery.append(" and obj.originTicket <= ").append(
					originTicketTo);
		}
		if (!StringUtils.isEmpty(loadDateFrom)) {
			query.append(" and  obj.loadDate>='").append(loadDateFrom + "'");
			countQuery.append(" and  obj.loadDate>='").append(
					loadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(loadDateTo)) {
			query.append(" and  obj.loadDate<='").append(loadDateTo + "'");
			countQuery.append(" and  obj.loadDate<='").append(loadDateTo + "'");

		}
		
		if (!StringUtils.isEmpty(unloadDateFrom)) {
			query.append(" and  obj.unloadDate>='").append(unloadDateFrom + "'");
			countQuery.append(" and  obj.unloadDate>='").append(
					unloadDateFrom + "'");

		}
		if (!StringUtils.isEmpty(unloadDateTo)) {
			query.append(" and  obj.unloadDate<='").append(unloadDateTo + "'");
			countQuery.append(" and  obj.unloadDate<='").append(unloadDateTo + "'");

		}
		
		if (!StringUtils.isEmpty(originGrossWtFrom)) {
			query.append(" and  obj.transferGross>=").append(originGrossWtFrom);
			countQuery.append(" and  obj.transferGross>=").append(
					originGrossWtFrom);
		}
		if (!StringUtils.isEmpty(originGrossWtTo)) {
			query.append(" and  obj.transferGross<=").append(originGrossWtTo);
			countQuery.append(" and  obj.transferGross<=").append(
					originGrossWtTo);
		}
		if (!StringUtils.isEmpty(originTareWtFrom)) {
			query.append(" and  obj.transferTare>=").append(originTareWtFrom);
			countQuery.append(" and  obj.transferTare>=").append(
					originTareWtFrom);
		}
		if (!StringUtils.isEmpty(originTareWtTo)) {
			query.append(" and  obj.transferTare<=").append(originTareWtTo);
			countQuery.append(" and  obj.transferTare<=")
					.append(originTareWtTo);
		}
		if (!StringUtils.isEmpty(originTonWtFrom)) {
			query.append(" and  obj.transferTons>=").append(originTonWtFrom);
			countQuery.append(" and  obj.transferTons>=").append(
					originTonWtFrom);
		}
		if (!StringUtils.isEmpty(originTonWtTo)) {
			query.append(" and  obj.transferTons<=").append(originTonWtTo);
			countQuery.append(" and  obj.transferTons<=").append(originTonWtTo);
		}
		if (!StringUtils.isEmpty(destination)) {
			String[] elements = destination.split(",");

			boolean isGrowsTullyTown = false;
			for (int i = 0; i < elements.length; i++) {
				System.out.println(elements[i]);
				if (elements[i].contains("91")) {
					isGrowsTullyTown = true;
				}
			}
			if (isGrowsTullyTown) {
				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows = genericDAO.getByCriteria(Location.class,
						criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown = genericDAO.getByCriteria(Location.class,
						criterias);
				query.append(" and  obj.destination.id in(")
						.append(destination + "," + grows.getId() + ","
								+ tullyTown.getId()).append(")");
				countQuery
						.append(" and  obj.destination.id in (")
						.append(destination + "," + grows.getId() + ","
								+ tullyTown.getId()).append(")");
			}

			else {
				query.append(" and  obj.destination.id in(")
						.append(destination).append(")");
				countQuery.append(" and  obj.destination.id in (")
						.append(destination).append(")");
			}

		}
		if (!StringUtils.isEmpty(destinationTicketFrom)) {
			query.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
			countQuery.append(" and obj.destinationTicket >= ").append(
					destinationTicketFrom);
		}
		if (!StringUtils.isEmpty(destinationTicketTo)) {
			query.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
			countQuery.append(" and obj.destinationTicket <= ").append(
					destinationTicketTo);
		}
		if (!StringUtils.isEmpty(landfillGrossWtFrom)) {
			query.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
			countQuery.append(" and  obj.landfillGross>=").append(
					landfillGrossWtFrom);
		}
		if (!StringUtils.isEmpty(landfillGrossWtTo)) {
			query.append(" and  obj.landfillGross<=").append(landfillGrossWtTo);
			countQuery.append(" and  obj.landfillGross<=").append(
					landfillGrossWtTo);
		}
		if (!StringUtils.isEmpty(landfillTareWtFrom)) {
			query.append(" and  obj.landfillTare>=").append(landfillTareWtFrom);
			countQuery.append(" and  obj.landfillTare>=").append(
					landfillTareWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTareWtTo)) {
			query.append(" and  obj.landfillTare<=").append(landfillTareWtTo);
			countQuery.append(" and  obj.landfillTare<=").append(
					landfillTareWtTo);
		}
		if (!StringUtils.isEmpty(landfillTonWtFrom)) {
			query.append(" and  obj.landfillTons>=").append(landfillTonWtFrom);
			countQuery.append(" and  obj.landfillTons>=").append(
					landfillTonWtFrom);
		}
		if (!StringUtils.isEmpty(landfillTonWtTo)) {
			query.append(" and  obj.landfillTons<=").append(landfillTonWtTo);
			countQuery.append(" and  obj.landfillTons<=").append(
					landfillTonWtTo);
		}
		query.append(" order by obj.subcontractor.name asc,obj.companyLocation.name asc,obj.origin.name asc,obj.destination.name asc,obj.originTicket asc");
		Long recordCount = (Long) genericDAO.getEntityManager()
				.createQuery(countQuery.toString()).getSingleResult();
		searchCriteria.setRecordCount(recordCount.intValue());
		System.out.println("******");
		System.out.println("****** Second and main query is "+query.toString());
		System.out.println("******");
		
		// Subcontractor summary report - 16thMar2016
		List<Ticket> tickets = null;
		if (isSummary) {
			tickets = (List<Ticket>) genericDAO
					.getEntityManager()
					.createQuery(query.toString())
					.getResultList();
		} else {
			tickets = (List<Ticket>) genericDAO
					.getEntityManager()
					.createQuery(query.toString())
					.setMaxResults(searchCriteria.getPageSize())
					.setFirstResult(
							searchCriteria.getPage() * searchCriteria.getPageSize())
					.getResultList();
		}

		List<SubcontractorBillingNew> summarys = new ArrayList<SubcontractorBillingNew>();
		SubcontractorBillingWrapper wrapper = new SubcontractorBillingWrapper();
		
		double sumNet = 0.0;
		double sumBillableTon = 0.0;
		double sumOriginTon = 0.0;
		double sumDestinationTon = 0.0;
		double sumAmount = 0.0;
		double sumFuelSurcharge = 0.0;
		double sumTotal = 0.0;
		double sumOtherCharges = 0.0;
		double grandTotal = 0.0;
		double amount = 0.0;
		double miscelleneousCharge = 0.0;
		List<String> str = new ArrayList<String>();
		
		String subticketIds = "";
	if(!tickets.isEmpty()){
		
		for (Ticket tkt : tickets) {
			if(subticketIds.equals("")){
				subticketIds = tkt.getId().toString();
			}
			else{
				subticketIds =subticketIds+","+tkt.getId().toString();
			}		
		}	
		
		System.out.println("******* The sub ticket id is "+subticketIds);
		
		String subinvquery="select obj from SubcontractorBillingNew obj where obj.ticket in ("
			+subticketIds.toString()+")";
	
		if (!StringUtils.isEmpty(input.getCompany())) {

			subinvquery = subinvquery+" and obj.companyLocationId in ("+company+")";
				
		}
		
		// Subcontractor summary report - 16thMar2016
		if (isSummary) {
			subinvquery += " order by obj.subcontractor, obj.sub_origin, obj.sub_destination"; // Subcontractor summary by load report - 14thApr2017
		}
		
		summarys=genericDAO.executeSimpleQuery(subinvquery);			
		
		wrapper.setSubcontractorBillingsNew(summarys);
		
		// Subcontractor summary report - 16thMar2016
		if (isSummary) {
			wrapper.setTickets(tickets);
			return wrapper;
		}
		
		String sum_query="select sum(obj.effectiveNetWt),sum(obj.effectiveTonsWt),sum(obj.originTonsWt),sum(obj.destinationTonsWt),sum(obj.amount),sum(obj.fuelSurcharge),sum(obj.otherCharges),sum(obj.totAmt) from SubcontractorBillingNew obj where obj.ticket in ("
			+subticketIds.toString()+")";
		
		if (!StringUtils.isEmpty(input.getCompany())) {

			sum_query = sum_query+" and obj.companyLocationId in ("+company+")";
				
		}
		 
		 List<SubcontractorBillingNew> sum_list=genericDAO.executeSimpleQuery(sum_query);
			
		 if(sum_list!=null && sum_list.size()>0){
		 for(Object obj:sum_list){
			 Object[] objarry=(Object[])obj;
			if(objarry!=null){
			if(objarry[0] !=null)	
			 sumNet=Double.parseDouble(objarry[0].toString());
			if(objarry[1] !=null)
			 sumBillableTon=Double.parseDouble(objarry[1].toString());
			if(objarry[2] !=null)
			 sumOriginTon=Double.parseDouble(objarry[2].toString());
			if(objarry[3] !=null)
			 sumDestinationTon=Double.parseDouble(objarry[3].toString());
			if(objarry[4] !=null)
			 sumAmount=Double.parseDouble(objarry[4].toString());
			if(objarry[5] !=null)
			 sumFuelSurcharge=Double.parseDouble(objarry[5].toString());
			if(objarry[6] !=null)
			 sumOtherCharges=Double.parseDouble(objarry[6].toString());
			if(objarry[7] !=null)
			 sumTotal=Double.parseDouble(objarry[7].toString());
			}
		 }
		 }	
		
		 
		 
		 String subMiscAmount = "select miscelleneousCharges from SubcontractorBillingNew obj where obj.ticket in ("
				+ subticketIds.toString()+")" ;
				
				if (!StringUtils.isEmpty(input.getCompany())) {

					subMiscAmount = subMiscAmount+" and obj.companyLocationId in ("+company+")";
						
				}
				
				subMiscAmount = subMiscAmount+" group by obj.invoiceNo,subcontractorId";
				
				System.out.println("********* ubMiscAmount query is "+subMiscAmount);
				
				
				List<Ticket> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
				
				if(submiscamountList!=null && submiscamountList.size()>0){
					for(Object submiscobj:submiscamountList){
						//Object[] submiscobjarry=(Object[])submiscobj;
						if(submiscobj!=null){
							
							String subMiscAmt = submiscobj.toString();
							
							
							String[]  subMiscAmts = subMiscAmt.split(",");
							for(int i=0;i< subMiscAmts.length;i++){									
								miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
							}
							
						}
					}						
				}
		 
		 
		 
		 /*String miscellQuery="select distinct obj.subcontractorId ,obj.miscelleneousCharges from SubcontractorBillingNew obj where obj.ticket in ("+subticketIds.toString()+")";
			
			List<SubcontractorBillingNew> misscellamount=genericDAO.executeSimpleQuery(miscellQuery);
			
			if(misscellamount!=null && misscellamount.size()>0){							
				for(Object obj:misscellamount){	
					Object[] objs=(Object[])obj;
					
					if(objs[1]!=null)
					{
						String misamt=objs[1].toString();
						String[] miscell=misamt.split(",");										
						for(int i=0;i<miscell.length;i++){
							
							miscelleneousCharge+=Double.parseDouble(miscell[i]);
						}
						
					}
					//subchargesForCompanyInv=Double.parseDouble(obj.toString());
				}						
			}*/		

		sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);
		sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
		sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);
		sumNet = MathUtil.roundUp(sumNet, 2);
		sumAmount = MathUtil.roundUp(sumAmount, 2);

		//sumTotal = sumAmount + sumFuelSurcharge + sumOtherCharges;
		
		sumTotal = MathUtil.roundUp(sumTotal, 2);
		wrapper.setSumBillableTon(sumBillableTon);
		wrapper.setSumOriginTon(sumOriginTon);
		wrapper.setSumDestinationTon(sumDestinationTon);
		wrapper.setSumNet(sumNet);
		wrapper.setSumAmount(sumAmount);
		wrapper.setSumFuelSurcharge(sumFuelSurcharge);
		wrapper.setSumTotal(sumTotal);
		wrapper.setSumOtherCharges(sumOtherCharges);
		wrapper.setMiscelleneousCharges(Double.toString(miscelleneousCharge));
		wrapper.setGrandTotal(sumTotal + miscelleneousCharge);

	}
		
		return wrapper;
	}

	// Filtering inactive trucks in net report - 15th Sep 2017
	private boolean filterInactiveTruck(NetReportWrapper anNetReportWrapper) {
		if (anNetReportWrapper.getSumTotal() != 0.0
				|| anNetReportWrapper.getFuellogCharge() != 0.0
				|| anNetReportWrapper.getTollTagAmount() != 0.0
				|| anNetReportWrapper.getTransportationAmount() != 0.0
				|| anNetReportWrapper.getNetAmount() != 0.0) {
			return false;
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("unit", Integer.valueOf(anNetReportWrapper.getUnit()));
		List<Vehicle> truckList = genericDAO.findByCriteria(Vehicle.class, criterias);
		if (truckList == null || truckList.isEmpty()) {
			return false;
		}
		
		Vehicle truck = truckList.get(0);
		if (truck.getActiveStatus() == 1) { // Active
			return false;
		} else {
			return true;
		}	
	}
	
	
	
	@Override
	public List<NetReportWrapper> generateNetReportData(
			SearchCriteria searchCriteria, NetReportInput input,
			HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();

		
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

		

		
		String unloadDateFrom = ReportDateUtil.getFromDate(input
				.getUnloadedFrom());
		String unloadDateTo = ReportDateUtil.getToDate(input.getUnloadedTo());
		
		String driverCompany=input.getDriverCompany();
		String truckCompany=input.getTruckCompany();
		String trailerCompany=input.getTrailerCompany();
        String trailer = input.getTrailer();
		String company = input.getCompany();
		String terminal = input.getTerminal();
		String driver = input.getDriver();
		String truck = input.getUnit();
		String  ticketstatus=input.getTicketStatus();
		StringBuffer ticketquery = new StringBuffer("");
		StringBuffer subcontractorquery = new StringBuffer("");
		StringBuffer terminalids=new StringBuffer("");		
		
		
		List<NetReportWrapper> wrapperlist = new ArrayList<NetReportWrapper>();
		List<NetReportWrapper> finalwrapperlist = new ArrayList<NetReportWrapper>();
		

		

		if (CompanyReport != null) {
			
			//********************New Net Report Logic************************
			
			StringBuilder companyNetReportQuery = new  StringBuilder("Select obj.employeeCompanyName,sum(obj.revenueAmount),sum(obj.fuelAmount),sum(obj.subcontractorAmount),sum(obj.tollAmount),sum(obj.driverPayAmount),sum(obj.NetAmount) from CompanyNetReport obj where obj.unloadDate>='"+unloadDateFrom+"' and obj.unloadDate<='"+unloadDateTo+"'");
			
			if(!StringUtils.isEmpty(company)){
				companyNetReportQuery.append(" and obj.employeeCompanyId in (").append(company).append(")");
			}
			
			companyNetReportQuery.append(" group by obj.employeeCompanyName order by obj.employeeCompanyName");
			
			System.out.println("**** Company net report query is "+companyNetReportQuery.toString());
			
			
			List<Ticket> companyNetReportDataList=genericDAO.executeSimpleQuery(companyNetReportQuery.toString());
			
			for(Object companyNetReportData: companyNetReportDataList){
				Object[] objarry=(Object[])companyNetReportData;
				if(objarry!=null){							
					NetReportWrapper wrapper = new NetReportWrapper();
					wrapper.setCompany(objarry[0].toString());
					wrapper.setSumTotal(Double.parseDouble(objarry[1].toString()));
					wrapper.setFuellogCharge(Double.parseDouble(objarry[2].toString()));
					wrapper.setSubcontractorCharge(Double.parseDouble(objarry[3].toString()));
					wrapper.setTollTagAmount(Double.parseDouble(objarry[4].toString()));	
					wrapper.setTransportationAmount(Double.parseDouble(objarry[5].toString()));				
					wrapper.setNetAmount(Double.parseDouble(objarry[6].toString()));
					wrapperlist.add(wrapper);
				}
			}
			//********************New Net Report Logic End Here****************
			
			/* Commented to test new logic
			if(ticketIds.length()>0){
			String disting = "select distinct obj.driverCompany from Ticket obj where obj.id in ("+ ticketIds+ ") order by obj.driverCompany.name";
			List<Location> ticketsdestinct = genericDAO.executeSimpleQuery(disting);
			Date startLoadDate=null;
			Date endLoadDate=null;
			
			String query = "select sbh.companyLocationId,sum(bh.totAmt) from  Billing_New bh,SubcontractorBillingNew sbh where bh.ticket=sbh.ticket and bh.ticket in ("+ticketIds+ ") group by sbh.companyLocationId";
			
			List<Location> subRevAmount = genericDAO.executeSimpleQuery(query);
			
			Map<Long,Double> subcontractorAmounts = new HashMap<Long,Double>();
			for(Object locObj: subRevAmount){
				//Location Company  = null;
				double amount = 0.0;
				long companyId = 0;				
				Object[] objarry=(Object[])locObj;
				if(objarry!=null){
					if(objarry[0] !=null && objarry[1] !=null){	
						amount = Double.parseDouble(objarry[1].toString());
						companyId = Long.parseLong(objarry[0].toString());
						subcontractorAmounts.put(companyId, amount);						
					}				
				}
			}
			
			for(Location locObj: ticketsdestinct){
				
				if(locObj.getId()!=149){
				
				Double fuelNetValue=0.0;
				Double tollNetValue=0.0;
				Double subchargescharges = 0.0;			
				Double miscelleneousCharge=0.0;	
				Double transportationAmount=0.0;
				Double netamount = 0.0;				
				Double revenue = 0.0;
				Location companyObj = null;
				
				String driverPayQuery = "Select sum(obj.driverPayRate) from Ticket obj where obj.id in ("+ticketIds+ ") and obj.driverCompany in (" +
				locObj.getId()+")";
				
				String BillingHistoryQuery = "Select sum(obj.totAmt) from Billing_New obj where obj.ticket in ("+ticketIds+ ") and obj.driverCompanyId in (" +
				locObj.getId()+")";
				
				String SubcontractorBillingQuery = "Select sum(obj.totAmt) from SubcontractorBillingNew obj where obj.ticket in ("+ticketIds+ ") and obj.companyLocationId in (" +
				locObj.getId()+")";
				
				
				String tollTagQuery =  "Select sum(obj.amount) from EzToll obj where obj.transactiondate>='"+unloadDateFrom+" 00:00:00' and obj.transactiondate<='"+unloadDateTo+" 23:59:59'";	
				tollTagQuery = tollTagQuery+" and obj.company in ("+locObj.getId()+")";
				
				if (!StringUtils.isEmpty(terminal)) {
					tollTagQuery = tollTagQuery+" and obj.terminal in ("+terminal+")";
				}
	            if (!StringUtils.isEmpty(truck)) {
	            	tollTagQuery = tollTagQuery+" and obj.plateNumber in ("+truck+")";
				}
				
				if (!StringUtils.isEmpty(driver)) {
					tollTagQuery = tollTagQuery+" and obj.driver in ("+driver+")";
				}
				
				String fuelLogQuery =  "Select sum(obj.amount) from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";	
				fuelLogQuery = fuelLogQuery+" and obj.company in ("+locObj.getId()+")";
				
				if (!StringUtils.isEmpty(terminal)) {
					fuelLogQuery = fuelLogQuery+" and obj.terminal in ("+terminal+")";
				}
	            if (!StringUtils.isEmpty(driver)) {
	            	fuelLogQuery = fuelLogQuery+" and obj.driversid in ("+driver+")";
				}
				if (!StringUtils.isEmpty(truck)) {
					fuelLogQuery = fuelLogQuery+" and obj.unit in ("+truck+")";
				}
				
				
				List<Ticket> driverPayAmount = genericDAO.executeSimpleQuery(driverPayQuery);	
				
				List<Ticket> revenueAmount = genericDAO.executeSimpleQuery(BillingHistoryQuery);	
				
				List<Ticket> subcontractorAmount = genericDAO.executeSimpleQuery(SubcontractorBillingQuery);	
				
				List<Ticket> tollAmount = genericDAO.executeSimpleQuery(tollTagQuery);	
				
				List<Ticket> fuelAmount = genericDAO.executeSimpleQuery(fuelLogQuery);	
				
				for(Object obj:driverPayAmount){
					if(obj!=null)
						transportationAmount=Double.parseDouble(obj.toString());					
				}
				
				for(Object obj:revenueAmount){
					if(obj!=null)
						revenue=Double.parseDouble(obj.toString());		
					if(subcontractorAmounts.get(locObj.getId())!=null){
						revenue = revenue + subcontractorAmounts.get(locObj.getId());
					}
				}
				
				for(Object obj:subcontractorAmount){
					if(obj!=null)
						subchargescharges=Double.parseDouble(obj.toString());	
				}
				
				for(Object obj:tollAmount){
					if(obj!=null)
						tollNetValue=Double.parseDouble(obj.toString());					
				}
				
				for(Object obj:fuelAmount){	
					if(obj!=null)
						fuelNetValue=Double.parseDouble(obj.toString());			
				}
				
				if(subchargescharges!=0.0){
					String subMiscAmount = "select miscelleneousCharges from SubcontractorBilling obj where obj.ticket in ("
					+ ticketIds
					+ ") and obj.companyLocationId="+locObj.getId()+" group by obj.invoiceNo,obj.subcontractorId";
				
				
				
					List<SubcontractorBilling> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
				
					if(submiscamountList!=null && submiscamountList.size()>0){
						for(Object submiscobj:submiscamountList){
							//Object[] submiscobjarry=(Object[])submiscobj;
							if(submiscobj!=null){								
								String subMiscAmt = submiscobj.toString();							
								String[]  subMiscAmts = subMiscAmt.split(",");
								for(int i=0;i< subMiscAmts.length;i++){									
									miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
								}								
							}
						}						
					}
				}
				
				
				netamount = revenue-(fuelNetValue +(subchargescharges+miscelleneousCharge)+ tollNetValue+transportationAmount);
				
				NetReportWrapper wrapper = new NetReportWrapper();				
				wrapper.setFuellogCharge(fuelNetValue);
				wrapper.setTollTagAmount(tollNetValue);	
				wrapper.setTransportationAmount(transportationAmount);
				wrapper.setSubcontractorCharge(subchargescharges+miscelleneousCharge);
				wrapper.setCompany(locObj.getName());
				wrapper.setSumTotal(revenue);
				wrapper.setNetAmount(netamount);
				wrapperlist.add(wrapper);

			}
			}		
			
		}	Commented to test new logic end here*/ 
			
		if (wrapperlist.size() > 0) {
			Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
			        @Override
			        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
			            return object1.getCompany().compareTo(object2.getCompany());
			        }
			} );
		}
			
	}
		
		
	if(TerminalReport != null){}
		
	if(DriverReport != null){
		
		StringBuilder driverNetReportQuery = new  StringBuilder("Select obj.employeeCompanyName,sum(obj.revenueAmount),sum(obj.fuelAmount),sum(obj.tollAmount),sum(obj.driverPayAmount),sum(obj.NetAmount),obj.employeeName from DriverNetReport obj where obj.unloadDate>='"+unloadDateFrom+"' and obj.unloadDate<='"+unloadDateTo+"' and active_flag='Y'");
		
		if(!StringUtils.isEmpty(driverCompany)){
			driverNetReportQuery.append(" and obj.employeeCompanyId in (").append(driverCompany).append(")");
		}
		
		if(!StringUtils.isEmpty(driver)){
			String drivers="";		
			String[] driverSplitEntry = driver.split(",");			
			for(int i=0;i<driverSplitEntry.length;i++){
				if(drivers.equals("")){
					drivers = "'"+driverSplitEntry[i]+"'";
				}
				else{
					drivers = drivers+","+"'"+driverSplitEntry[i]+"'";
				}
			}
			
			driverNetReportQuery.append(" and obj.employeeName in (").append(drivers).append(")");
		}
		
		
		
		driverNetReportQuery.append(" group by obj.employeeCompanyName,obj.employeeName order by obj.employeeCompanyName,obj.employeeName");
		
		System.out.println("**** Driver net report query is "+driverNetReportQuery.toString());
		
		
		List<Ticket> driverNetReportDataList=genericDAO.executeSimpleQuery(driverNetReportQuery.toString());
		
		for(Object driverNetReportData: driverNetReportDataList){
			Object[] objarry=(Object[])driverNetReportData;
			if(objarry!=null){							
				NetReportWrapper wrapper = new NetReportWrapper();
				wrapper.setCompany(objarry[0].toString());
				wrapper.setSumTotal(Double.parseDouble(objarry[1].toString()));
				wrapper.setFuellogCharge(Double.parseDouble(objarry[2].toString()));
				wrapper.setSubcontractorCharge(0.0);
				wrapper.setTollTagAmount(Double.parseDouble(objarry[3].toString()));	
				wrapper.setTransportationAmount(Double.parseDouble(objarry[4].toString()));				
				wrapper.setNetAmount(Double.parseDouble(objarry[5].toString()));
				wrapper.setDriver(objarry[6].toString());
				wrapperlist.add(wrapper);
			}
		}
		

		/*if(ticketIds.length()>0){
		String disting = "select distinct obj.driver from Ticket obj where obj.id in ("+ ticketIds+ ") order by obj.driver.fullName";
		List<Driver> ticketsdestinct = genericDAO.executeSimpleQuery(disting);
		Date startLoadDate=null;
		Date endLoadDate=null;
		String driversIdsInTicket="-1";
		for(Driver drvObj: ticketsdestinct){
			
			Map criti = new HashMap();
			String drvIDs="";
			criti.clear();
			criti.put("fullName", drvObj.getFullName());
			List <Driver> drivers =  genericDAO.findByCriteria(Driver.class, criti);
			
			for(Driver drvs:drivers){
				if(drvIDs.equals("")){
					drvIDs = drvs.getId().toString();
				}
				else{
					drvIDs = drvIDs+","+drvs.getId();
				}
			}
			
			Double fuelNetValue=0.0;
			Double tollNetValue=0.0;
			Double subchargescharges = 0.0;			
			Double miscelleneousCharge=0.0;	
			Double transportationAmount=0.0;
			Double netamount = 0.0;				
			Double revenue = 0.0;
			Location companyObj = null;
			
			String driverPayQuery = "Select sum(obj.driverPayRate) from Ticket obj where obj.id in ("+ticketIds+ ") and obj.driver in (" +
			drvIDs+")";
			
			String BillingHistoryQuery = "Select sum(obj.totAmt) from Billing_New obj where obj.ticket in ("+ticketIds+ ") and obj.driver in ('" +
			drvObj.getFullName()+"')";
			
			String SubcontractorBillingQuery = "Select sum(obj.totAmt) from SubcontractorBillingNew obj where obj.ticket in ("+ticketIds+ ") and obj.driver in ('" +
			drvObj.getFullName()+"')";
			
			
			String tollTagQuery =  "Select sum(obj.amount) from EzToll obj where obj.transactiondate>='"+unloadDateFrom+" 00:00:00' and obj.transactiondate<='"+unloadDateTo+" 23:59:59'";	
			tollTagQuery = tollTagQuery+" and obj.driver in ("+drvIDs+")";
			
			if (!StringUtils.isEmpty(terminal)) {
				tollTagQuery = tollTagQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(truck)) {
            	tollTagQuery = tollTagQuery+" and obj.plateNumber in ("+truck+")";
			}			
			if (!StringUtils.isEmpty(company)) {
				tollTagQuery = tollTagQuery+" and obj.company in ("+company+")";
			}
			
			String fuelLogQuery =  "Select sum(obj.amount) from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";	
			fuelLogQuery = fuelLogQuery+" and obj.driversid in ("+drvIDs+")";
			
			if (!StringUtils.isEmpty(terminal)) {
				fuelLogQuery = fuelLogQuery+" and obj.terminal in ("+terminal+")";
			}
			if (!StringUtils.isEmpty(company)) {
				fuelLogQuery = fuelLogQuery+" and obj.company in ("+company+")";
			}
			if (!StringUtils.isEmpty(truck)) {
				fuelLogQuery = fuelLogQuery+" and obj.unit in ("+truck+")";
			}
			
			
			List<Ticket> driverPayAmount = genericDAO.executeSimpleQuery(driverPayQuery);	
			
			List<Ticket> revenueAmount = genericDAO.executeSimpleQuery(BillingHistoryQuery);	
			
			List<Ticket> subcontractorAmount = genericDAO.executeSimpleQuery(SubcontractorBillingQuery);	
			
			List<Ticket> tollAmount = genericDAO.executeSimpleQuery(tollTagQuery);	
			
			List<Ticket> fuelAmount = genericDAO.executeSimpleQuery(fuelLogQuery);	
			
			for(Object obj:driverPayAmount){
				if(obj!=null)
					transportationAmount=Double.parseDouble(obj.toString());					
			}
			
			for(Object obj:revenueAmount){
				if(obj!=null)
					revenue=Double.parseDouble(obj.toString());					
			}
			
			for(Object obj:subcontractorAmount){
				if(obj!=null)
					subchargescharges=Double.parseDouble(obj.toString());	
			}
			
			for(Object obj:tollAmount){
				if(obj!=null)
					tollNetValue=Double.parseDouble(obj.toString());					
			}
			
			for(Object obj:fuelAmount){	
				if(obj!=null)
					fuelNetValue=Double.parseDouble(obj.toString());			
			}
			
			/*if(subchargescharges!=0.0){
				String subMiscAmount = "select miscelleneousCharges from SubcontractorBilling obj where obj.ticket in ("
				+ ticketIds
				+ ") and obj.driver='"+drvObj.getFullName()+"' group by obj.invoiceNo,obj.subcontractorId";
			
			
			
				List<SubcontractorBilling> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
			
				if(submiscamountList!=null && submiscamountList.size()>0){
					for(Object submiscobj:submiscamountList){
						//Object[] submiscobjarry=(Object[])submiscobj;
						if(submiscobj!=null){								
							String subMiscAmt = submiscobj.toString();							
							String[]  subMiscAmts = subMiscAmt.split(",");
							for(int i=0;i< subMiscAmts.length;i++){									
								miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
							}								
						}
					}						
				}
			}
			
			
			netamount = revenue-(fuelNetValue +(subchargescharges+miscelleneousCharge)+ tollNetValue+transportationAmount);
			
			NetReportWrapper wrapper = new NetReportWrapper();				
			wrapper.setFuellogCharge(fuelNetValue);
			wrapper.setTollTagAmount(tollNetValue);	
			wrapper.setTransportationAmount(transportationAmount);
			wrapper.setSubcontractorCharge(subchargescharges+miscelleneousCharge);
			wrapper.setDriver(drvObj.getFullName());
			wrapper.setSumTotal(revenue);
			wrapper.setNetAmount(netamount);
			wrapperlist.add(wrapper);
			
			driversIdsInTicket = driversIdsInTicket+","+drvIDs;

		}		
		
		
		String driverQuery = "select obj from Driver obj where obj.status=1 and obj.catagory=2 and obj.id not in ("+driversIdsInTicket+")";
		
		List<Driver> driverResultSet = genericDAO.executeSimpleQuery(driverQuery);
		for(Driver drvObj: driverResultSet){
			NetReportWrapper wrapper = new NetReportWrapper();				
			wrapper.setFuellogCharge(0.0);
			wrapper.setTollTagAmount(0.0);	
			wrapper.setTransportationAmount(0.0);
			wrapper.setSubcontractorCharge(0.0);
			wrapper.setDriver(drvObj.getFullName());
			wrapper.setSumTotal(0.0);
			wrapper.setNetAmount(0.0);
			wrapperlist.add(wrapper);
		}
	}	*/
		
		
		
	if (wrapperlist.size() > 0) {
		Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
		        @Override
		        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
		            return object1.getDriver().compareTo(object2.getDriver());
		        }
		} );
	}
		

	}
		
	
	
	
	if(TruckReport != null){
		

		
		StringBuilder truckNetReportQuery = new  StringBuilder("Select obj.employeeCompanyName,sum(obj.revenueAmount),sum(obj.fuelAmount),sum(obj.tollAmount),sum(obj.driverPayAmount),sum(obj.NetAmount),obj.unitNum from TruckNetReport obj where obj.unloadDate>='"+unloadDateFrom+"' and obj.unloadDate<='"+unloadDateTo+"' and active_flag='Y'");
		
		if(!StringUtils.isEmpty(truckCompany)){
			truckNetReportQuery.append(" and obj.employeeCompanyId in (").append(truckCompany).append(")");
		}
		
		if(!StringUtils.isEmpty(truck)){
			String trucks="";		
			String[] truckSplitEntry = truck.split(",");			
			for(int i=0;i<truckSplitEntry.length;i++){
				if(trucks.equals("")){
					trucks = "'"+truckSplitEntry[i]+"'";
				}
				else{
					trucks = trucks+","+"'"+truckSplitEntry[i]+"'";
				}
			}
			
			truckNetReportQuery.append(" and obj.unitNum in (").append(trucks).append(")");
		}
		
		
		
		truckNetReportQuery.append(" group by obj.employeeCompanyName,obj.unitNum order by obj.employeeCompanyName,obj.unitNum");
		
		System.out.println("**** truck net report query is "+truckNetReportQuery.toString());
		
		
		List<Ticket> truckNetReportDataList=genericDAO.executeSimpleQuery(truckNetReportQuery.toString());
		
		for(Object truckNetReportData: truckNetReportDataList){
			Object[] objarry=(Object[])truckNetReportData;
			if(objarry!=null){							
				NetReportWrapper wrapper = new NetReportWrapper();
				wrapper.setCompany(objarry[0].toString());
				wrapper.setSumTotal(Double.parseDouble(objarry[1].toString()));
				wrapper.setFuellogCharge(Double.parseDouble(objarry[2].toString()));
				wrapper.setSubcontractorCharge(0.0);
				wrapper.setTollTagAmount(Double.parseDouble(objarry[3].toString()));	
				wrapper.setTransportationAmount(Double.parseDouble(objarry[4].toString()));				
				wrapper.setNetAmount(Double.parseDouble(objarry[5].toString()));
				wrapper.setUnit(objarry[6].toString());
				
				// Filtering inactive trucks in net report - 15th Sep 2017
				if (!filterInactiveTruck(wrapper)) {
					wrapperlist.add(wrapper);
				}
			}
		}
		
		
		/*if(ticketIds.length()>0){
		String disting = "select distinct obj.vehicle from Ticket obj where obj.id in ("+ ticketIds+ ") order by obj.vehicle.unit";
		List<Vehicle> ticketsdestinct = genericDAO.executeSimpleQuery(disting);
		Date startLoadDate=null;
		Date endLoadDate=null;
		String vehicleIdsInTicket="-1";
		for(Vehicle vehObj: ticketsdestinct){			
			
			Double fuelNetValue=0.0;
			Double tollNetValue=0.0;
			Double subchargescharges = 0.0;			
			Double miscelleneousCharge=0.0;	
			Double transportationAmount=0.0;
			Double netamount = 0.0;				
			Double revenue = 0.0;
			
			
			String driverPayQuery = "Select sum(obj.driverPayRate) from Ticket obj where obj.id in ("+ticketIds+ ") and obj.vehicle in (" +
			vehObj.getId()+")";
			
			String BillingHistoryQuery = "Select sum(obj.totAmt) from Billing_New obj where obj.ticket in ("+ticketIds+ ") and obj.t_unit in (" +
			vehObj.getUnitNum()+")";
			
			
			
			String SubcontractorBillingQuery = "Select sum(obj.totAmt) from SubcontractorBillingNew obj where obj.ticket in ("+ticketIds+ ") and obj.sub_unit in (" +
			vehObj.getUnitNum()+")";
			
			
			String tollTagQuery =  "Select sum(obj.amount) from EzToll obj where obj.transactiondate>='"+unloadDateFrom+" 00:00:00' and obj.transactiondate<='"+unloadDateTo+" 23:59:59'";	
			tollTagQuery = tollTagQuery+" and obj.plateNumber in ("+vehObj.getId()+")";
			
			if(!StringUtils.isEmpty(driver)){
				tollTagQuery = tollTagQuery+" and obj.driver in ("+driver+")";
			}
			
			if (!StringUtils.isEmpty(terminal)) {
				tollTagQuery = tollTagQuery+" and obj.terminal in ("+terminal+")";
			}				
			if (!StringUtils.isEmpty(company)) {
				tollTagQuery = tollTagQuery+" and obj.company in ("+company+")";
			}
			
			String fuelLogQuery =  "Select sum(obj.amount) from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";	
			fuelLogQuery = fuelLogQuery+" and obj.unit in ("+vehObj.getId()+")";
			
			
			if(!StringUtils.isEmpty(driver)){
				fuelLogQuery = fuelLogQuery+" and obj.driversid in ("+driver+")";
			}
			
			if (!StringUtils.isEmpty(terminal)) {
				fuelLogQuery = fuelLogQuery+" and obj.terminal in ("+terminal+")";
			}
			if (!StringUtils.isEmpty(company)) {
				fuelLogQuery = fuelLogQuery+" and obj.company in ("+company+")";
			}
			
				
			
			
			
			List<Ticket> driverPayAmount = genericDAO.executeSimpleQuery(driverPayQuery);	
			
			List<Ticket> revenueAmount = genericDAO.executeSimpleQuery(BillingHistoryQuery);	
			
			List<Ticket> subcontractorAmount = genericDAO.executeSimpleQuery(SubcontractorBillingQuery);	
			
			List<Ticket> tollAmount = genericDAO.executeSimpleQuery(tollTagQuery);	
			
			List<Ticket> fuelAmount = genericDAO.executeSimpleQuery(fuelLogQuery);	
			
			for(Object obj:driverPayAmount){
				if(obj!=null)
					transportationAmount=Double.parseDouble(obj.toString());					
			}
			
			for(Object obj:revenueAmount){
				if(obj!=null)
					revenue=Double.parseDouble(obj.toString());					
			}
			
			for(Object obj:subcontractorAmount){
				if(obj!=null)
					subchargescharges=Double.parseDouble(obj.toString());	
			}
			
			for(Object obj:tollAmount){
				if(obj!=null)
					tollNetValue=Double.parseDouble(obj.toString());					
			}
			
			for(Object obj:fuelAmount){	
				if(obj!=null)
					fuelNetValue=Double.parseDouble(obj.toString());			
			}
			
			/*if(subchargescharges!=0.0){
				String subMiscAmount = "select miscelleneousCharges from SubcontractorBilling obj where obj.ticket in ("
				+ ticketIds
				+ ") and obj.unit="+vehObj.getUnitNum()+" group by obj.invoiceNo,obj.subcontractorId";
			
			
			
				List<SubcontractorBilling> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
			
				if(submiscamountList!=null && submiscamountList.size()>0){
					for(Object submiscobj:submiscamountList){
						//Object[] submiscobjarry=(Object[])submiscobj;
						if(submiscobj!=null){								
							String subMiscAmt = submiscobj.toString();							
							String[]  subMiscAmts = subMiscAmt.split(",");
							for(int i=0;i< subMiscAmts.length;i++){									
								miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
							}								
						}
					}						
				}
			}
			
			
			netamount = revenue-(fuelNetValue +(subchargescharges+miscelleneousCharge)+ tollNetValue+transportationAmount);
			
			NetReportWrapper wrapper = new NetReportWrapper();				
			wrapper.setFuellogCharge(fuelNetValue);
			wrapper.setTollTagAmount(tollNetValue);	
			wrapper.setTransportationAmount(transportationAmount);
			wrapper.setSubcontractorCharge(subchargescharges+miscelleneousCharge);
			wrapper.setUnit(vehObj.getUnitNum());
			wrapper.setUnitID(vehObj.getId().toString());
			wrapper.setSumTotal(revenue);
			wrapper.setNetAmount(netamount);
			wrapperlist.add(wrapper);
			
			vehicleIdsInTicket = vehicleIdsInTicket+","+vehObj.getId();

		}		
		
		
		String vehicleQuery = "select obj from Vehicle obj where obj.type=1 and obj.id not in ("+vehicleIdsInTicket+")";
		
		List<Vehicle> vehicleResultSet = genericDAO.executeSimpleQuery(vehicleQuery);
		for(Vehicle vehObj: vehicleResultSet){
			NetReportWrapper wrapper = new NetReportWrapper();				
			wrapper.setFuellogCharge(0.0);
			wrapper.setTollTagAmount(0.0);	
			wrapper.setTransportationAmount(0.0);
			wrapper.setSubcontractorCharge(0.0);
			wrapper.setUnit(vehObj.getUnitNum());
			wrapper.setUnitID(vehObj.getId().toString());
			wrapper.setSumTotal(0.0);
			wrapper.setNetAmount(0.0);
			wrapperlist.add(wrapper);
		}
	}	*/
		
		
		
	if (wrapperlist.size() > 0) {
		Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
		        @Override
		        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
		        	return (Integer.parseInt(object1.getUnit())-Integer.parseInt(object2.getUnit()));
		        }
		} );
	}
		

	
	}
	
	
if(TrailerReport!=null){

	StringBuilder trailerNetReportQuery = new  StringBuilder("Select obj.employeeCompanyName,sum(obj.revenueAmount),sum(obj.driverPayAmount),sum(obj.NetAmount),obj.unitNum from TrailerNetReport obj where obj.unloadDate>='"+unloadDateFrom+"' and obj.unloadDate<='"+unloadDateTo+"' and active_flag='Y'");
	
	if(!StringUtils.isEmpty(trailerCompany)){
		trailerNetReportQuery.append(" and obj.employeeCompanyId in (").append(trailerCompany).append(")");
	}
	
	if(!StringUtils.isEmpty(trailer)){
		String trailers="";		
		String[] trailersSplitEntry = trailer.split(",");			
		for(int i=0;i<trailersSplitEntry.length;i++){
			if(trailers.equals("")){
				trailers = "'"+trailersSplitEntry[i]+"'";
			}
			else{
				trailers = trailers+","+"'"+trailersSplitEntry[i]+"'";
			}
		}
		
		trailerNetReportQuery.append(" and obj.unitNum in (").append(trailers).append(")");
	}
	
	
	
	trailerNetReportQuery.append(" group by obj.employeeCompanyName,obj.unitNum order by obj.employeeCompanyName,obj.unitNum");
	
	System.out.println("**** trailer net report query is "+trailerNetReportQuery.toString());
	
	
	List<Ticket> trailerNetReportDataList=genericDAO.executeSimpleQuery(trailerNetReportQuery.toString());
	
	for(Object trailerNetReportData: trailerNetReportDataList){
		Object[] objarry=(Object[])trailerNetReportData;
		if(objarry!=null){							
			NetReportWrapper wrapper = new NetReportWrapper();
			wrapper.setCompany(objarry[0].toString());
			wrapper.setSumTotal(Double.parseDouble(objarry[1].toString()));
			wrapper.setFuellogCharge(0.0);
			wrapper.setSubcontractorCharge(0.0);
			wrapper.setTollTagAmount(0.0);	
			wrapper.setTransportationAmount(Double.parseDouble(objarry[2].toString()));				
			wrapper.setNetAmount(Double.parseDouble(objarry[3].toString()));
			wrapper.setUnit(objarry[4].toString());
			wrapperlist.add(wrapper);
		}
	}
	
	/*if(ticketIds.length()>0){
	String disting = "select distinct obj.trailer from Ticket obj where obj.id in ("+ ticketIds+ ") order by obj.trailer.unit";
	List<Vehicle> ticketsdestinct = genericDAO.executeSimpleQuery(disting);
	Date startLoadDate=null;
	Date endLoadDate=null;
	String vehicleIdsInTicket="-1";
	for(Vehicle vehObj: ticketsdestinct){			
		
		Double fuelNetValue=0.0;
		Double tollNetValue=0.0;
		Double subchargescharges = 0.0;			
		Double miscelleneousCharge=0.0;	
		Double transportationAmount=0.0;
		Double netamount = 0.0;				
		Double revenue = 0.0;
		
		
		String driverPayQuery = "Select sum(obj.driverPayRate) from Ticket obj where obj.id in ("+ticketIds+ ") and obj.trailer in (" +
		vehObj.getId()+")";
		
		String BillingHistoryQuery = "Select sum(obj.totAmt) from Billing_New obj where obj.ticket in ("+ticketIds+ ") and obj.trailer in (" +
		vehObj.getUnitNum()+")";
		
		
		
		String SubcontractorBillingQuery = "Select sum(obj.totAmt) from SubcontractorBillingNew obj where obj.ticket in ("+ticketIds+ ") and obj.trailer in (" +
		vehObj.getUnitNum()+")";
		
		
		
		
		
		List<Ticket> driverPayAmount = genericDAO.executeSimpleQuery(driverPayQuery);	
		
		List<Ticket> revenueAmount = genericDAO.executeSimpleQuery(BillingHistoryQuery);	
		
		List<Ticket> subcontractorAmount = genericDAO.executeSimpleQuery(SubcontractorBillingQuery);	
		
		
		
		for(Object obj:driverPayAmount){
			if(obj!=null)
				transportationAmount=Double.parseDouble(obj.toString());					
		}
		
		for(Object obj:revenueAmount){
			if(obj!=null)
				revenue=Double.parseDouble(obj.toString());					
		}
		
		for(Object obj:subcontractorAmount){
			if(obj!=null)
				subchargescharges=Double.parseDouble(obj.toString());	
		}
		
		
		
		/*if(subchargescharges!=0.0){
			String subMiscAmount = "select miscelleneousCharges from SubcontractorBillingNew obj where obj.ticket in ("
			+ ticketIds
			+ ") and obj.trailer="+vehObj.getUnitNum()+" group by obj.invoiceNo,obj.subcontractorId";
		
		
		
			List<SubcontractorBilling> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
		
			if(submiscamountList!=null && submiscamountList.size()>0){
				for(Object submiscobj:submiscamountList){
					//Object[] submiscobjarry=(Object[])submiscobj;
					if(submiscobj!=null){								
						String subMiscAmt = submiscobj.toString();							
						String[]  subMiscAmts = subMiscAmt.split(",");
						for(int i=0;i< subMiscAmts.length;i++){									
							miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
						}								
					}
				}						
			}
		}
		
		
		netamount = revenue-(0.0+(subchargescharges+miscelleneousCharge)+ 0.0+transportationAmount);
		
		NetReportWrapper wrapper = new NetReportWrapper();				
		wrapper.setFuellogCharge(0.0);
		wrapper.setTollTagAmount(0.0);	
		wrapper.setTransportationAmount(transportationAmount);
		wrapper.setSubcontractorCharge(subchargescharges+miscelleneousCharge);
		wrapper.setUnit(vehObj.getUnitNum());
		wrapper.setUnitID(vehObj.getId().toString());
		wrapper.setSumTotal(revenue);
		wrapper.setNetAmount(netamount);
		wrapperlist.add(wrapper);
		
		vehicleIdsInTicket = vehicleIdsInTicket+","+vehObj.getId();

	}		
	
	
	String vehicleQuery = "select obj from Vehicle obj where obj.type=2 and obj.id not in ("+vehicleIdsInTicket+")";
	
	List<Vehicle> vehicleResultSet = genericDAO.executeSimpleQuery(vehicleQuery);
	for(Vehicle vehObj: vehicleResultSet){
		NetReportWrapper wrapper = new NetReportWrapper();				
		wrapper.setFuellogCharge(0.0);
		wrapper.setTollTagAmount(0.0);	
		wrapper.setTransportationAmount(0.0);
		wrapper.setSubcontractorCharge(0.0);
		wrapper.setUnit(vehObj.getUnitNum());
		wrapper.setUnitID(vehObj.getId().toString());
		wrapper.setSumTotal(0.0);
		wrapper.setNetAmount(0.0);
		wrapperlist.add(wrapper);
	}
}	*/
	
	
	
if (wrapperlist.size() > 0) {
	Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
	        @Override
	        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
	        	return (Integer.parseInt(object1.getUnit())-Integer.parseInt(object2.getUnit()));
	        }
	} );
}
}
		
		// For terminal Report
		//if (TerminalReport != null) {
			/*if(ticketIds.length()>0){
			
			
			
			
			
			String tollTagQuery =  "Select obj from EzToll obj where obj.transactiondate>='"+unloadDateFrom+" 00:00:00' and obj.transactiondate<='"+unloadDateTo+" 23:59:59'";	
			
			
			
			if (!StringUtils.isEmpty(company)) {
				
				if(!StringUtils.equalsIgnoreCase(TerminalReport,"Terminalac")){
					tollTagQuery = tollTagQuery+" and obj.company in ("+company+")";
				}
			}
			if (!StringUtils.isEmpty(terminal)) {
				tollTagQuery = tollTagQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(truck)) {
            	tollTagQuery = tollTagQuery+" and obj.plateNumber in ("+truck+")";
			}
			
			if (!StringUtils.isEmpty(driver)) {
				tollTagQuery = tollTagQuery+" and obj.driver in ("+driver+")";
			}
			tollTagQuery = tollTagQuery+" order by obj.company,obj.terminal";
			
			
			List<EzToll> tollTagList = genericDAO.executeSimpleQuery(tollTagQuery);				
			
			
			String tollTerminalTempId="-1";
			String tollCompanyTempId="-1";
			Double  tollAmount=0.0;
			
			Map<String,Double> tollAmountMap = new HashMap<String,Double>();
			for(EzToll ezObj:tollTagList ){
				if(!tollTerminalTempId.equals(ezObj.getTerminal().getId().toString()+"-"+ezObj.getCompany().getId().toString())){										
					tollAmount=0.0;					
					tollTerminalTempId=ezObj.getTerminal().getId().toString()+"-"+ezObj.getCompany().getId().toString();					
					//tollCompanyTempId=ezObj.getCompany().getId().toString();
					tollAmount=tollAmount+ezObj.getAmount();
					tollAmountMap.put(ezObj.getTerminal().getId().toString()+"-"+ezObj.getCompany().getId().toString(),tollAmount);					
				}
				else{
					tollAmount=tollAmount+ezObj.getAmount();
					tollAmountMap.put(ezObj.getTerminal().getId().toString()+"-"+ezObj.getCompany().getId().toString(),tollAmount);					
				}
			}
			
			
			if(tollTagList!=null && tollTagList.size()>0){
				for(Object obj:tollTagList){
					Object[] objarry=(Object[])obj;
					if(objarry!=null){
						if(objarry[0] !=null && objarry[1] !=null)	
							tollAmountList.put(Long.parseLong(objarry[0].toString())+"-"+Long.parseLong(objarry[2].toString()),Double.parseDouble(objarry[1].toString()));							
					}
				}
		    }
				
				
			String fuelLogQuery =  "Select obj from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";	
				
			if (!StringUtils.isEmpty(company)) {
				if(!StringUtils.equalsIgnoreCase(TerminalReport,"Terminalac")){
					fuelLogQuery = fuelLogQuery+" and obj.company in ("+company+")";
				}
			}
			if (!StringUtils.isEmpty(terminal)) {
				fuelLogQuery = fuelLogQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(driver)) {
            	fuelLogQuery = fuelLogQuery+" and obj.driversid in ("+driver+")";
			}
			if (!StringUtils.isEmpty(truck)) {
				fuelLogQuery = fuelLogQuery+" and obj.unit in ("+truck+")";
			}
			
			fuelLogQuery = fuelLogQuery+" order by obj.company,obj.terminal";			
			
			List<FuelLog> fuelLogList = genericDAO.executeSimpleQuery(fuelLogQuery);				
					
			
			String fuelTerminalTempId="-1";
			String fuelCompanyTempId="-1";
			Double  fuelAmount=0.0;
			Double  totalfuelAmount=0.0;
			Map<String,Double> fuelAmountMap = new HashMap<String,Double>();
			for(FuelLog fuelObj:fuelLogList ){
				if(!fuelTerminalTempId.equals(fuelObj.getTerminal().getId().toString()+"-"+fuelObj.getCompany().getId().toString())){										
					
					fuelAmount=0.0;					
					fuelTerminalTempId=fuelObj.getTerminal().getId().toString()+"-"+fuelObj.getCompany().getId().toString();	
					//fuelCompanyTempId=fuelObj.getCompany().getId().toString();
					fuelAmount=fuelAmount+fuelObj.getAmount();
					totalfuelAmount=totalfuelAmount+fuelObj.getAmount();
					fuelAmountMap.put(fuelObj.getTerminal().getId().toString()+"-"+fuelObj.getCompany().getId().toString(),fuelAmount);
					
				}
				else{
					fuelAmount=fuelAmount+fuelObj.getAmount();
					fuelAmountMap.put(fuelObj.getTerminal().getId().toString()+"-"+fuelObj.getCompany().getId().toString(),fuelAmount);
					totalfuelAmount=totalfuelAmount+fuelObj.getAmount();
					
				}
			}
			
			System.out.println("************** Terminal tot fuel amount is "+totalfuelAmount);
			if(fuelLogList!=null && fuelLogList.size()>0){
				for(Object obj:fuelLogList){
					Object[] objarry=(Object[])obj;
					if(objarry!=null){
						if(objarry[0] !=null && objarry[1] !=null)	
							fuelAmountList.put(Long.parseLong(objarry[0].toString())+"-"+Long.parseLong(objarry[2].toString()),Double.parseDouble(objarry[1].toString()));							
					}
				}
			}
			
			
			
			
			
			String ticketsForAlocation = "select sum(obj.ticketTotAmt),sum(obj.subcontractorTotAmt),terminal,company,sum(obj.transportationAmount) from NetReportMain obj where  obj.ticketID in  ("
			+ ticketIds
			+ ") and company is not null group by obj.company,obj.terminal";
				
				
			List<Ticket> ticketsForAterminal = genericDAO.executeSimpleQuery(ticketsForAlocation);
				
			if(ticketsForAterminal!=null && ticketsForAterminal.size()>0){
			 for(Object obj:ticketsForAterminal){
				
				Double fuelNetValue=0.0;
				Double tollNetValue=0.0;
				Double subchargescharges = 0.0;			
				Double miscelleneousCharge=0.0;	
				Double transportationAmount=0.0;
				Double netamount = 0.0;				
				Double revenue = 0.0;
				Location terminalObj = null;
				Location companyObj = null;
				String fuelLogIDs="-1";
				String tollTagIDs="-1";
				
				Object[] objarry=(Object[])obj;
				if(objarry!=null){
					if(objarry[0] !=null)	
						revenue=Double.parseDouble(objarry[0].toString())		;		
					if(objarry[1] !=null)
						subchargescharges=Double.parseDouble(objarry[1].toString());					
					if(objarry[2] !=null)
						terminalObj = genericDAO.getById(Location.class,Long.parseLong(objarry[2].toString()));
					if(objarry[3] !=null)
						companyObj = genericDAO.getById(Location.class,Long.parseLong(objarry[3].toString()));
					if(objarry[4]!=null)
						transportationAmount= Double.parseDouble(objarry[4].toString());
					
					String subMiscAmount = "select subMiscAmt from NetReportMain obj where obj.ticketID in ("
						+ ticketIds
						+ ") and obj.terminal="+terminalObj.getId()+" and obj.company="+companyObj.getId()+" group by obj.subcontractorVoucherNumber,obj.subcontractor";
						
					if(subchargescharges!=0.0){
					System.out.println("***** Terminal misc query is "+subMiscAmount);
						List<Ticket> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
						
						if(submiscamountList!=null && submiscamountList.size()>0){
							for(Object submiscobj:submiscamountList){
								//Object[] submiscobjarry=(Object[])submiscobj;
								if(submiscobj!=null){
									
									String subMiscAmt = submiscobj.toString();
									
									System.out.println("******* misc amoun t is "+subMiscAmt);
									String[]  subMiscAmts = subMiscAmt.split(",");
									for(int i=0;i< subMiscAmts.length;i++){									
										miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
									}
									
								}
							}						
						}
				  }	
					
					System.out.println("******** Fuel Amount is "+terminalObj.getId()+"-"+companyObj.getId()+"--->>"+fuelAmountMap.get(terminalObj.getId()+"-"+companyObj.getId()));
					
					fuelNetValue = fuelAmountMap.get(terminalObj.getId().toString()+"-"+companyObj.getId().toString());					
					
					tollNetValue = tollAmountMap.get(terminalObj.getId().toString()+"-"+companyObj.getId().toString());
					
					if(fuelNetValue==null)
						fuelNetValue = 0.0;
					
					if(tollNetValue==null)
						tollNetValue = 0.0;	
					
								
			}			
				netamount = revenue-(fuelNetValue +(subchargescharges+miscelleneousCharge)+ tollNetValue+transportationAmount);
				
				NetReportWrapper wrapper = new NetReportWrapper();				
				wrapper.setFuellogCharge(fuelNetValue);
				wrapper.setTollTagAmount(tollNetValue);	
				wrapper.setTransportationAmount(transportationAmount);
				wrapper.setTerminal(terminalObj.getName());
				wrapper.setCompany(companyObj.getName());
				wrapper.setSumTotal(revenue);
				wrapper.setNetAmount(netamount);
				wrapper.setSubcontractorCharge(subchargescharges+miscelleneousCharge);
				wrapperlist.add(wrapper);
			}
			}
		}
			
			 if (wrapperlist.size() > 0) {
					
				    Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
				        @Override
				        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
				        	
				        	int companyResult = object1.getCompany().compareTo(object2.getCompany());
				        	
				        	if(companyResult!=0){
				        		return companyResult;
				        	}
				        	
				        	return object1.getTerminal().compareTo(object2.getTerminal());
				        }
				       } );
				    
				  
		   }
			
			
	*///}
		
		
		
		// For Truck Report
		//if (TruckReport != null) {
			
			/*Date startLoadDate=null;
			Date endLoadDate=null;
			
		    String vehid="";
			List<Long> vehicleIds = new ArrayList<Long>();
			
			
			
			
			String tollTagQuery =  "Select obj from EzToll obj where obj.transactiondate>='"+unloadDateFrom+" 00:00:00' and obj.transactiondate<='"+unloadDateTo+" 23:59:59'";	
			
			if (!StringUtils.isEmpty(company)) {
				if(!StringUtils.equalsIgnoreCase(TruckReport,"Truckac")){
					tollTagQuery = tollTagQuery+" and obj.company in ("+company+")";
				}
			}
			if (!StringUtils.isEmpty(terminal)) {
				tollTagQuery = tollTagQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(truck)) {
            	tollTagQuery = tollTagQuery+" and obj.plateNumber in ("+truck+")";
			}
			
			if (!StringUtils.isEmpty(driver)) {
				tollTagQuery = tollTagQuery+" and obj.driver in ("+driver+")";
			}
			tollTagQuery = tollTagQuery+" order by obj.plateNumber";
			
			
			List<EzToll> tollTagList = genericDAO.executeSimpleQuery(tollTagQuery);				
				
			
			String tollUnitTempId="-1";
			Double  tollAmount=0.0;
			
			Map<Long,Double> tollAmountMap = new HashMap<Long,Double>();
			for(EzToll ezObj:tollTagList ){
				if(!tollUnitTempId.equals(ezObj.getPlateNumber().getId().toString())){										
					tollAmount=0.0;					
					tollUnitTempId=ezObj.getPlateNumber().getId().toString();					
					tollAmount=tollAmount+ezObj.getAmount();
					tollAmountMap.put(ezObj.getPlateNumber().getId(),tollAmount);					
				}
				else{
					tollAmount=tollAmount+ezObj.getAmount();
					tollAmountMap.put(ezObj.getPlateNumber().getId(),tollAmount);					
				}
			}
			
			
			if(tollTagList!=null && tollTagList.size()>0){
				for(Object obj:tollTagList){
					Object[] objarry=(Object[])obj;
					if(objarry!=null){
						if(objarry[0] !=null && objarry[1] !=null)	
							tollAmountList.put(Long.parseLong(objarry[0].toString()),Double.parseDouble(objarry[1].toString()));							
					}
				}
		    }
				
				
			String fuelLogQuery =  "Select obj from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";	
				
			if (!StringUtils.isEmpty(company)) {
				if(!StringUtils.equalsIgnoreCase(TruckReport,"Truckac")){
					fuelLogQuery = fuelLogQuery+" and obj.company in ("+company+")";
				}
			}
			if (!StringUtils.isEmpty(terminal)) {
				fuelLogQuery = fuelLogQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(driver)) {
            	fuelLogQuery = fuelLogQuery+" and obj.driversid in ("+driver+")";
			}
			if (!StringUtils.isEmpty(truck)) {
				fuelLogQuery = fuelLogQuery+" and obj.unit in ("+truck+")";
			}
			
			fuelLogQuery = fuelLogQuery+" order by obj.unit";
			
			List<FuelLog> fuelLogList = genericDAO.executeSimpleQuery(fuelLogQuery);				
					
			
			String fuelUnitTempId="-1";
			Double  fuelAmount=0.0;
			Double totFuelAmount=0.0;
			Map<Long,Double> fuelAmountMap = new HashMap<Long,Double>();
			for(FuelLog fuelObj:fuelLogList ){
				if(!fuelUnitTempId.equals(fuelObj.getUnit().getId().toString())){										
					fuelAmount=0.0;					
					fuelUnitTempId=fuelObj.getUnit().getId().toString();					
					fuelAmount=fuelAmount+fuelObj.getAmount();
					totFuelAmount=totFuelAmount+fuelObj.getAmount();
					fuelAmountMap.put(fuelObj.getUnit().getId(),fuelAmount);
					
				}
				else{
					fuelAmount=fuelAmount+fuelObj.getAmount();
					fuelAmountMap.put(fuelObj.getUnit().getId(),fuelAmount);
					totFuelAmount=totFuelAmount+fuelObj.getAmount();
				}
			}
			
			
			if(fuelLogList!=null && fuelLogList.size()>0){
				for(Object obj:fuelLogList){
					Object[] objarry=(Object[])obj;
					if(objarry!=null){
						if(objarry[0] !=null && objarry[1] !=null)	
							fuelAmountList.put(Long.parseLong(objarry[0].toString()),Double.parseDouble(objarry[1].toString()));							
					}
				}
			}
			
			
			Map<Long,Double> revenueAmountMap = new HashMap<Long,Double>();
			Map<Long,Double> subcontractorAmountMap = new HashMap<Long,Double>();
			Map<Long,Double> miscAmountMap = new HashMap<Long,Double>();
			
				
			String ticketsForAVehicle = "select sum(obj.ticketTotAmt),sum(obj.subcontractorTotAmt),vehicle  from NetReportMain obj where  obj.ticketID in ("
			+ ticketIds
			+ ") group by obj.vehicle";
			
			List<Ticket> ticketsForAvehicle = genericDAO.executeSimpleQuery(ticketsForAVehicle);
				
			if(ticketsForAvehicle!=null && ticketsForAvehicle.size()>0){
			 for(Object obj:ticketsForAvehicle){
			
				Double fuelNetValue=0.0;
				Double tollNetValue=0.0;
				Double subchargescharges = 0.0;			
				Double miscelleneousCharge=0.0;				
				Double netamount = 0.0;				
				Double revenue = 0.0;
				Vehicle truckObj = null;
				String fuelLogIDs="-1";
				String tollTagIDs="-1";
				
				Object[] objarry=(Object[])obj;
				if(objarry!=null){
					if(objarry[0] !=null)	
						revenue=Double.parseDouble(objarry[0].toString());
					
					if(objarry[1] !=null)
						subchargescharges=Double.parseDouble(objarry[1].toString());
					
					if(objarry[2] !=null)
						truckObj = genericDAO.getById(Vehicle.class,Long.parseLong(objarry[2].toString())); 
					
					
					vehicleIds.add(truckObj.getId());
					
					if(vehid.equals("")){
						vehid=truckObj.getId().toString();
					}
					else{
					    vehid=vehid+","+truckObj.getId().toString();
					}
					
					
					
					if(subchargescharges!=0.0){
					
					 String subMiscAmount = "select subMiscAmt from NetReportMain obj where obj.ticketID in ("
						+ ticketIds
						+ ") and obj.vehicle="+truckObj.getId()+" group by obj.subcontractorVoucherNumber,subcontractor";
						
						List<Ticket> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
						
						if(submiscamountList!=null && submiscamountList.size()>0){
							for(Object submiscobj:submiscamountList){
								//Object[] submiscobjarry=(Object[])submiscobj;
								if(submiscobj!=null){
									
									String subMiscAmt = submiscobj.toString();
									
									System.out.println("******* misc amoun t is "+subMiscAmt);
									String[]  subMiscAmts = subMiscAmt.split(",");
									for(int i=0;i< subMiscAmts.length;i++){									
										miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
									}
									
								}
							}						
						}
				
					}					
					
				//	fuelNetValue =  fuelAmountMap.get(truckObj.getId());					
					
					//tollNetValue = tollAmountMap.get(truckObj.getId());
					
					//if(fuelNetValue==null)
					//	fuelNetValue = 0.0;
					
					//if(tollNetValue==null)
					//	tollNetValue = 0.0;			
				}
					
				
				revenueAmountMap.put(truckObj.getId(), revenue);
				subcontractorAmountMap.put(truckObj.getId(), subchargescharges);
				miscAmountMap.put(truckObj.getId(), miscelleneousCharge);
				
				NetReportWrapper wrapper = new NetReportWrapper();	
				
				if(fuelNetValue!=null)
					wrapper.setFuellogCharge(fuelNetValue);
				else
					wrapper.setFuellogCharge(0.0);	
				
				if(tollNetValue!=null)
					wrapper.setTollTagAmount(tollNetValue);	
				else
					wrapper.setTollTagAmount(0.0);
				
				netamount = revenue- (fuelNetValue + (subchargescharges +miscelleneousCharge)+ tollNetValue);
				wrapper.setUnit(truckObj.getUnit().toString());
				wrapper.setUnitID(truckObj.getId().toString());
				wrapper.setCompany(truckObj.getOwner().getName());
				wrapper.setSubcontractorCharge(subchargescharges +miscelleneousCharge);
				wrapper.setSumTotal(revenue);
				wrapper.setNetAmount(netamount);
				wrapperlist.add(wrapper);
			}
		}
			
			
			String query = "select obj from Vehicle obj where obj.type=1";
			
			List<Vehicle> vehList = genericDAO.executeSimpleQuery(query);
			Double vehamounttolls=0.0;
			Double vehamountfuels=0.0;
			for(Vehicle veh: vehList){
				
				Double tollamount = tollAmountMap.get(veh.getId());
				
				if(tollamount==null)
					tollamount = 0.0;
				
				
				Double fuelamount = fuelAmountMap.get(veh.getId());
				
				if(fuelamount==null)
					fuelamount = 0.0; 
				
				Double revenueAmount = revenueAmountMap.get(veh.getId());
				
				if(revenueAmount==null)
					revenueAmount = 0.0;
				
				Double subcontractorAmount = subcontractorAmountMap.get(veh.getId());
				
				if(subcontractorAmount==null)
					subcontractorAmount = 0.0;
				
				Double miscAmount = miscAmountMap.get(veh.getId());
				
				if(miscAmount==null)
					miscAmount = 0.0;
				
				
				
				Double netAmount = revenueAmount - (fuelamount+tollamount+(subcontractorAmount+miscAmount));
				
				NetReportWrapper wrapper = new NetReportWrapper();	
				
				wrapper.setFuellogCharge(fuelamount);
				wrapper.setTollTagAmount(tollamount);					
				wrapper.setUnit(veh.getUnit().toString());
				wrapper.setUnitID(veh.getId().toString());
				wrapper.setCompany(veh.getOwner().getName());
				wrapper.setSubcontractorCharge(subcontractorAmount+miscAmount);
				wrapper.setSumTotal(revenueAmount);
				wrapper.setNetAmount(netAmount);
				wrapperlist.add(wrapper);
			}
		
			
			
			if(!StringUtils.isEmpty(vehid)){
	        	String vehquery="";
	        if(TruckReport.equalsIgnoreCase("Truckcc") || TruckReport.equalsIgnoreCase("Truckca"))	{
	        	vehquery="select obj from Vehicle obj where obj.type=1 and obj.id not in ("+vehid+") ";
	        }
	        else{
	        	vehquery="select obj from Vehicle obj where obj.type=1 and obj.id not in ("+vehid+") ";
	        	}
			
	        if (!StringUtils.isEmpty(company)) {
	        	vehquery = vehquery+" and obj.owner in ("+company+")";
			}
			
			if (!StringUtils.isEmpty(truck)) {
				vehquery = vehquery+" and obj.id in ("+truck+")";
			}
			
			vehquery = vehquery+" order by obj.unit";
	        
	        
	        
			List<Vehicle> vehlist=genericDAO.executeSimpleQuery(vehquery);				
			if(vehlist!=null && vehlist.size()>0){
				
				for(Vehicle veh:vehlist){
				if(vehicleIds.contains(veh.getId())){
					//do nothing
				}
				else{
					Double fuelNetValue=0.0;
					Double tollNetValue=0.0;
					
					NetReportWrapper wrapper = new NetReportWrapper();
					
					wrapper.setCompany(veh.getOwner().getName());
					
					fuelNetValue = fuelAmountMap.get(veh.getId());					
					
					tollNetValue = tollAmountMap.get(veh.getId());
					
					if(fuelNetValue==null)
						fuelNetValue = 0.0;
					
					if(tollNetValue==null)
						tollNetValue = 0.0;
					
					wrapper.setFuellogCharge(fuelNetValue);
					wrapper.setNetAmount(0.0);
					wrapper.setSubcontractorCharge(0.0);
					wrapper.setSumTotal(0.0);
					wrapper.setUnit(veh.getUnit().toString());
					wrapper.setUnitID(veh.getId().toString());
					wrapper.setTollTagAmount(tollNetValue);					
					wrapperlist.add(wrapper);						
				}
				}					
			}
			
	        }
			
			
			
			
			
		        if(!StringUtils.isEmpty(vehid)){
		        	String vehquery="";
		        if(TruckReport.equalsIgnoreCase("Truckcc") || TruckReport.equalsIgnoreCase("Truckca"))	{
		        	vehquery="select obj from Vehicle obj where obj.type=1 and obj.id not in ("+vehid+") and  ((obj.validFrom <='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo >='"+oracleFormatter.format(mainEndDate)+"') OR (obj.validFrom >='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo <='"+oracleFormatter.format(mainEndDate)+"') OR (obj.validFrom <='"+oracleFormatter.format(mainStartDate)+"' and (obj.validTo >='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo <='"+oracleFormatter.format(mainEndDate)+"')) OR ((obj.validFrom >='"+oracleFormatter.format(mainStartDate)+"' and obj.validFrom <='"+oracleFormatter.format(mainEndDate)+"')and obj.validTo >='"+oracleFormatter.format(mainEndDate)+"'))";	
		        }
		        else{
		        	vehquery="select obj from Vehicle obj where obj.type=1 and obj.id not in ("+vehid+")";
		        }
				
		        if (!StringUtils.isEmpty(company)) {
		        	vehquery = vehquery+" and obj.owner in ("+company+")";
				}
				
				if (!StringUtils.isEmpty(truck)) {
					vehquery = vehquery+" and obj.id in ("+truck+")";
				}
				
				vehquery = vehquery+" order by obj.unit";
		        
		        
		        
				List<Vehicle> vehlist=genericDAO.executeSimpleQuery(vehquery);				
				if(vehlist!=null && vehlist.size()>0){
					
					for(Vehicle veh:vehlist){
					if(vehicleIds.contains(veh.getId())){
						//do nothing
					}
					else{
						Double fuelNetValue=0.0;
						Double tollNetValue=0.0;
						
						NetReportWrapper wrapper = new NetReportWrapper();
						
						wrapper.setCompany(veh.getOwner().getName());
						
						fuelNetValue = fuelAmountMap.get(veh.getId());					
						
						tollNetValue = tollAmountMap.get(veh.getId());
						
						if(fuelNetValue==null)
							fuelNetValue = 0.0;
						
						if(tollNetValue==null)
							tollNetValue = 0.0;
						
						wrapper.setFuellogCharge(fuelNetValue);
						wrapper.setNetAmount(0.0);
						wrapper.setSubcontractorCharge(0.0);
						wrapper.setSumTotal(0.0);
						wrapper.setUnit(veh.getUnit().toString());
						wrapper.setUnitID(veh.getId().toString());
						wrapper.setTollTagAmount(tollNetValue);					
						wrapperlist.add(wrapper);						
					}
					}					
				}
				
		        }	
		        
		        if (wrapperlist.size() > 0) {
					
					    Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
					        @Override
					        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
					            return (Integer.parseInt(object1.getUnit())-Integer.parseInt(object2.getUnit()));
					        }
					       } );
			   }	        
		*/
			//}
		
		
		//for Trailer Report
		//if(TrailerReport!=null){
			/*	
			String vehid="";
			List<Long> vehicleIds = new ArrayList<Long>();
			
				
				String ticketsForTrailer = "select sum(obj.ticketTotAmt),sum(obj.subcontractorTotAmt),trailer from NetReportMain obj where  obj.ticketID in ("
				+ ticketIds
				+ ") group by obj.trailer";
				
				
				
				List<Ticket> ticketsForAvehicle = genericDAO.executeSimpleQuery(ticketsForTrailer);
				
				if(ticketsForAvehicle!=null && ticketsForAvehicle.size()>0){
				for(Object obj:ticketsForAvehicle){
								
				Double netamount = 0.0;
				Double revenue = 0.0;
				Double fuelNetValue=0.0;
				Double tollNetValue=0.0;				
				Double miscelleneousCharge=0.0;				
				Double subchargescharges = 0.0;
				Vehicle trailerObj = null;
				String fuelLogIDs="-1";
				String tollTagIDs="-1";
				
				
				Object[] objarry=(Object[])obj;
				if(objarry!=null){
					if(objarry[0] !=null)	
						revenue=Double.parseDouble(objarry[0].toString());					
					if(objarry[1] !=null)
						subchargescharges=Double.parseDouble(objarry[1].toString());					
					if(objarry[2] !=null)
						trailerObj = genericDAO.getById(Vehicle.class,Long.parseLong(objarry[2].toString()));
					
					vehicleIds.add(trailerObj.getId());
					
					if(vehid.equals("")){
						vehid=trailerObj.getUnit().toString();  
					}
					else{
					    vehid=vehid+","+trailerObj.getUnit().toString();
					}
					
					if(subchargescharges!=0.0){
					String subMiscAmount = "select subMiscAmt from NetReportMain obj where obj.ticketID in ("
						+ ticketIds
						+ ") and obj.trailer="+trailerObj.getId()+" group by obj.subcontractorVoucherNumber,subcontractor";
						
						List<Ticket> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
						
						if(submiscamountList!=null && submiscamountList.size()>0){
							for(Object submiscobj:submiscamountList){
								//Object[] submiscobjarry=(Object[])submiscobj;
								if(submiscobj!=null){
									
									String subMiscAmt = submiscobj.toString();
									
									System.out.println("******* misc amoun t is "+subMiscAmt);
									String[]  subMiscAmts = subMiscAmt.split(",");
									for(int i=0;i< subMiscAmts.length;i++){									
										miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
									}
									
								}
							}						
						}
					}					
					
				}
				
				
				NetReportWrapper wrapper = new NetReportWrapper();	
				wrapper.setFuellogCharge(0.0);
				wrapper.setTollTagAmount(0.0);
				netamount = revenue- (0.0 + (subchargescharges +miscelleneousCharge)+ 0.0);
				wrapper.setUnit(trailerObj.getUnit().toString());
				wrapper.setUnitID(trailerObj.getId().toString());
				wrapper.setCompany(trailerObj.getOwner().getName());
				wrapper.setSumTotal(revenue);
				wrapper.setNetAmount(netamount);
				wrapper.setSubcontractorCharge(subchargescharges +miscelleneousCharge);
				wrapperlist.add(wrapper);
			}
		}
			
				 if(!StringUtils.isEmpty(vehid)){
			        	String vehquery="";
			        if(TrailerReport.equalsIgnoreCase("Trailercc") || TrailerReport.equalsIgnoreCase("Trailerca"))	{
			        	vehquery="select obj from Vehicle obj where obj.type=2 and obj.unit not in ("+vehid+") and  ((obj.validFrom <='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo >='"+oracleFormatter.format(mainEndDate)+"') OR (obj.validFrom >='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo <='"+oracleFormatter.format(mainEndDate)+"') OR (obj.validFrom <='"+oracleFormatter.format(mainStartDate)+"' and (obj.validTo >='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo <='"+oracleFormatter.format(mainEndDate)+"')) OR ((obj.validFrom >='"+oracleFormatter.format(mainStartDate)+"' and obj.validFrom <='"+oracleFormatter.format(mainEndDate)+"')and obj.validTo >='"+oracleFormatter.format(mainEndDate)+"'))";	
			        }
			        else{
			        	vehquery="select obj from Vehicle obj where obj.type=2 and obj.unit not in ("+vehid+") and  ((obj.validFrom <='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo >='"+oracleFormatter.format(mainEndDate)+"') OR (obj.validFrom >='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo <='"+oracleFormatter.format(mainEndDate)+"') OR (obj.validFrom <='"+oracleFormatter.format(mainStartDate)+"' and (obj.validTo >='"+oracleFormatter.format(mainStartDate)+"' and obj.validTo <='"+oracleFormatter.format(mainEndDate)+"')) OR ((obj.validFrom >='"+oracleFormatter.format(mainStartDate)+"' and obj.validFrom <='"+oracleFormatter.format(mainEndDate)+"')and obj.validTo >='"+oracleFormatter.format(mainEndDate)+"'))";			
			        }
			        
			        
			        if (!StringUtils.isEmpty(company)) {
			        	vehquery = vehquery+" and obj.owner in ("+company+")";
					}
					
		            if (!StringUtils.isEmpty(truck)) {
		            	vehquery = vehquery+" and obj.id in ("+truck+")";
					}
					
					
			        
					vehquery = vehquery+" order by obj.unit";
					
					List<Vehicle> vehlist=genericDAO.executeSimpleQuery(vehquery);				
					if(vehlist!=null && vehlist.size()>0){
						
						for(Vehicle veh:vehlist){
						if(vehicleIds.contains(veh.getId())){
							//do nothing
						}
						else{
							Double fuelNetValue=0.0;
							Double tollNetValue=0.0;
							
							NetReportWrapper wrapper = new NetReportWrapper();
							
							wrapper.setCompany(veh.getOwner().getName());						
							
							wrapper.setFuellogCharge(0.0);
							wrapper.setNetAmount(0.0);
							wrapper.setSubcontractorCharge(0.0);
							wrapper.setSumTotal(0.0);
							wrapper.setUnit(veh.getUnit().toString());
							wrapper.setUnitID(veh.getId().toString());
							wrapper.setTollTagAmount(0.0);					
							wrapperlist.add(wrapper);						
						}
						}					
					}
					
			        }	
				
				
				
				
				if (wrapperlist.size() > 0) {
					
				    Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
				        @Override
				        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
				            return (Integer.parseInt(object1.getUnit())-Integer.parseInt(object2.getUnit()));
				        }
				       } );
		       }
				
		*///}
		
		
		// For Driver Report
		//if (DriverReport != null) {
			/*if(ticketIds.length()>0){
			
			
			String drvid="";  
			
			List<Long> driverIds =  new ArrayList<Long>();
			
			
			
			
			
			String tollTagQuery =  "Select obj from EzToll obj where obj.transactiondate>='"+unloadDateFrom+" 00:00:00' and obj.transactiondate<='"+unloadDateTo+" 23:59:59'";	
			
			if (!StringUtils.isEmpty(company)) {
				if(!StringUtils.equalsIgnoreCase(DriverReport,"Driverac")){
					tollTagQuery = tollTagQuery+" and obj.company in ("+company+")";
				}
			}
			if (!StringUtils.isEmpty(terminal)) {
				tollTagQuery = tollTagQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(truck)) {
            	tollTagQuery = tollTagQuery+" and obj.plateNumber in ("+truck+")";
			}
			
			if (!StringUtils.isEmpty(driver)) {
				tollTagQuery = tollTagQuery+" and obj.driver in ("+driver+")";
			}
			tollTagQuery = tollTagQuery+" order by obj.driver";
			
			System.out.println("******* Driver Toll Query is "+tollTagQuery);
			
			
			List<EzToll> tollTagList = genericDAO.executeSimpleQuery(tollTagQuery);				
				
			String driverTempId="-1";
			Double  tollAmount=0.0;
			
			Map<Long,Double> tollAmountMap = new HashMap<Long,Double>();
			for(EzToll ezObj:tollTagList ){
				if(!driverTempId.equals(ezObj.getDriver().getId().toString())){										
					tollAmount=0.0;					
					driverTempId=ezObj.getDriver().getId().toString();					
					tollAmount=tollAmount+ezObj.getAmount();
					tollAmountMap.put(ezObj.getDriver().getId(),tollAmount);					
				}
				else{
					tollAmount=tollAmount+ezObj.getAmount();
					tollAmountMap.put(ezObj.getDriver().getId(),tollAmount);					
				}
			}
			
			if(tollTagList!=null && tollTagList.size()>0){
				for(Object obj:tollTagList){
					Object[] objarry=(Object[])obj;
					if(objarry!=null){						
						if(objarry[0] !=null && objarry[1] !=null)	
							tollAmountList.put(objarry[0].toString(),Double.parseDouble(objarry[1].toString()));							
					}
				}
		    }
				
				
			String fuelLogQuery =  "Select obj from FuelLog obj where obj.transactiondate>='"+unloadDateFrom+"' and obj.transactiondate<='"+unloadDateTo+"'";	
				
			if (!StringUtils.isEmpty(company)) {
				if(!StringUtils.equalsIgnoreCase(DriverReport,"Driverac")){
					fuelLogQuery = fuelLogQuery+" and obj.company in ("+company+")";
				}
			}
			if (!StringUtils.isEmpty(terminal)) {
				fuelLogQuery = fuelLogQuery+" and obj.terminal in ("+terminal+")";
			}
            if (!StringUtils.isEmpty(driver)) {
            	fuelLogQuery = fuelLogQuery+" and obj.driversid in ("+driver+")";
			}
			if (!StringUtils.isEmpty(truck)) {
				fuelLogQuery = fuelLogQuery+" and obj.unit in ("+truck+")";
			}
			
			fuelLogQuery = fuelLogQuery+" order by obj.driversid";
			
			
			
			List<FuelLog> fuelLogList = genericDAO.executeSimpleQuery(fuelLogQuery);				
			
			
			
			String fueldriverTempId="-1";
			Double  fuelAmount=0.0;
			
			Map<Long,Double> fuelAmountMap = new HashMap<Long,Double>();
			for(FuelLog fuelObj:fuelLogList ){
				if(!fueldriverTempId.equals(fuelObj.getDriversid().getId().toString())){										
					fuelAmount=0.0;					
					fueldriverTempId=fuelObj.getDriversid().getId().toString();					
					fuelAmount=fuelAmount+fuelObj.getAmount();
					fuelAmountMap.put(fuelObj.getDriversid().getId(),fuelAmount);
					
				}
				else{
					fuelAmount=fuelAmount+fuelObj.getAmount();
					fuelAmountMap.put(fuelObj.getDriversid().getId(),fuelAmount);
					
				}
			}
			
			
			
			
			if(fuelLogList!=null && fuelLogList.size()>0){
				for(Object obj:fuelLogList){
					Object[] objarry=(Object[])obj;
					if(objarry!=null){
						if(objarry[0] !=null && objarry[1] !=null)	
							fuelAmountList.put(objarry[0].toString(),Double.parseDouble(objarry[1].toString()));							
					}
				}
			}
			
			
			String ticketForADiver = "select sum(obj.ticketTotAmt),sum(obj.subcontractorTotAmt),driver,sum(obj.transportationAmount) from NetReportMain obj where  obj.ticketID in ("
				+ ticketIds
				+ ") group by obj.driver";
		
			List<Ticket> ticketsForADriver = genericDAO.executeSimpleQuery(ticketForADiver);			
				
			Map<Long,Double> revenueAmountMap = new HashMap<Long,Double>();
			Map<Long,Double> subcontractorAmountMap = new HashMap<Long,Double>();
			Map<Long,Double> miscAmountMap = new HashMap<Long,Double>();
			Map<Long,Double> transAmountMap = new HashMap<Long,Double>();
			
			if(ticketsForADriver!=null && ticketsForADriver.size()>0){
				for(Object obj:ticketsForADriver){
						 
						    Double netamount = 0.0;
							Double revenue = 0.0;
							Double fuelNetValue=0.0;
							Double tollNetValue=0.0;				
							Double miscelleneousCharge=0.0;				
							Double subchargescharges = 0.0;
							Double transportationAmount = 0.0;
							Driver drvObj = null;
							String fuelLogIDs="-1";
							String tollTagIDs="-1";
							
							
						 Object[] objarry=(Object[])obj;
						if(objarry!=null){
							if(objarry[0] !=null)	
								revenue=Double.parseDouble(objarry[0].toString());							
							if(objarry[1] !=null)
								subchargescharges=Double.parseDouble(objarry[1].toString());							
							if(objarry[2]!=null)
								drvObj =  genericDAO.getById(Driver.class,Long.parseLong(objarry[2].toString()));
							if(objarry[3]!=null)
								transportationAmount= Double.parseDouble(objarry[3].toString());
							
							driverIds.add(drvObj.getId());							
							
							
							if(drvid.equals("")){
								drvid=drvObj.getId().toString();
							}
							else{
							    drvid=drvid+","+drvObj.getId().toString();
							}
							
							
							
							
							if(subchargescharges!=0.0){
							
								String subMiscAmount = "select subMiscAmt from NetReportMain obj where obj.ticketID in ("
								+ ticketIds
								+ ") and obj.driver="+drvObj.getId()+" group by obj.subcontractorVoucherNumber,subcontractor";
								
								List<Ticket> submiscamountList = genericDAO.executeSimpleQuery(subMiscAmount);				
								
								if(submiscamountList!=null && submiscamountList.size()>0){
									for(Object submiscobj:submiscamountList){
										//Object[] submiscobjarry=(Object[])submiscobj;
										if(submiscobj!=null){
											
											String subMiscAmt = submiscobj.toString();
											
											System.out.println("******* misc amoun t is "+subMiscAmt);
											String[]  subMiscAmts = subMiscAmt.split(",");
											for(int i=0;i< subMiscAmts.length;i++){									
												miscelleneousCharge+=Double.parseDouble(subMiscAmts[i]);
											}
											
										}
									}						
								}				
							}							
							
							fuelNetValue = fuelAmountMap.get(drvObj.getId());					
							
							tollNetValue =  tollAmountMap.get(drvObj.getId());
							
							if(fuelNetValue==null)
								fuelNetValue = 0.0;
							
							if(tollNetValue==null)
								tollNetValue = 0.0;	
							
						}		

						revenueAmountMap.put(drvObj.getId(), revenue);
						subcontractorAmountMap.put(drvObj.getId(), subchargescharges);
						miscAmountMap.put(drvObj.getId(), miscelleneousCharge);
						transAmountMap.put(drvObj.getId(),transportationAmount);
						
				NetReportWrapper wrapper = new NetReportWrapper();
				wrapper.setFuellogCharge(fuelNetValue);
				wrapper.setTollTagAmount(tollNetValue); 			     
				netamount = revenue- (fuelNetValue + (subchargescharges+miscelleneousCharge)+ tollNetValue+transportationAmount);
				wrapper.setDriver(drvObj.getFullName());
				wrapper.setCompany(drvObj.getCompany().getName());
				wrapper.setSumTotal(revenue);
				wrapper.setTransportationAmount(transportationAmount);
				wrapper.setSubcontractorCharge(subchargescharges+miscelleneousCharge);
				wrapper.setNetAmount(netamount);
				wrapperlist.add(wrapper);
			 }
		 }	
			
			
			List<String> driverId =  new  ArrayList<String>();			
			
			String query = "select obj from Driver obj";
			
			List<Driver> drvList = genericDAO.executeSimpleQuery(query);
			Double drvamounttolls=0.0;
			Double drvamountfuels=0.0;
			for(Driver drv: drvList){
				
				Double tollamount = tollAmountMap.get(drv.getId());
				
				if(tollamount==null)
					tollamount = 0.0;
				
				
				Double fuelamount = fuelAmountMap.get(drv.getId());
				
				if(fuelamount==null)
					fuelamount = 0.0; 
				
				Double revenueAmount = revenueAmountMap.get(drv.getId());
				  
				if(revenueAmount==null)
					revenueAmount = 0.0;
				
				Double subcontractorAmount = subcontractorAmountMap.get(drv.getId());
				
				if(subcontractorAmount==null)
					subcontractorAmount = 0.0;
				
				Double miscAmount = miscAmountMap.get(drv.getId());
				
				if(miscAmount==null)
					miscAmount = 0.0;
				
				Double transAmt = transAmountMap.get(drv.getId());
				
				if(transAmt==null)
					transAmt = 0.0;
				
				
				Double netAmount = revenueAmount - (fuelamount+tollamount+(subcontractorAmount+miscAmount)+transAmt);
				
				if(!driverId.contains(drv.getFullName())){					
					if(drv.getStatus()==1 && drv.getCatagory().getId()==2){						
						driverId.add(drv.getFullName());					
					}
					else if(drv.getStatus()==0 && drv.getCatagory().getId()==2){
						if(revenueAmount==0.0 && fuelamount==0.0 && tollamount==0.0 && subcontractorAmount==0.0 && miscAmount==0.0 && transAmt==0.0){
							if(drv.getDateTerminated()!=null){
								if(drv.getDateTerminated().after( mainStartDate) && drv.getDateTerminated().before(mainEndDate)){							
									driverId.add(drv.getFullName());							
								}
								else{							
									continue;
								}
							}
						}
						else{
							driverId.add(drv.getFullName());
						}
						
						
					}
					else if(drv.getCatagory().getId()!=2){
						if(revenueAmount==0.0 && fuelamount==0.0 && tollamount==0.0 && subcontractorAmount==0.0 && miscAmount==0.0 && transAmt==0.0){
							continue;
						}		
					}					
				}
				else{
					if(revenueAmount==0.0 && fuelamount==0.0 && tollamount==0.0 && subcontractorAmount==0.0 && miscAmount==0.0 && transAmt==0.0){
						continue;
					}					
				}
				
				NetReportWrapper wrapper = new NetReportWrapper();
				wrapper.setFuellogCharge(fuelamount);
				wrapper.setTollTagAmount(tollamount); 			     
				//netamount = revenueAmount- (fuelamount + (subcontractorAmount+miscAmount)+ tollNetValue+transportationAmount);
				wrapper.setDriver(drv.getFullName());
				wrapper.setCompany(drv.getCompany().getName());
				wrapper.setSumTotal(revenueAmount);
				wrapper.setTransportationAmount(transAmt);
				wrapper.setSubcontractorCharge(subcontractorAmount+miscAmount);
				wrapper.setNetAmount(netAmount);
				wrapperlist.add(wrapper);
				
				
			}
			
			
			if(!StringUtils.isEmpty(drvid)){
				
				String drvquery="";
				if(DriverReport.equalsIgnoreCase("Drivercc") || DriverReport.equalsIgnoreCase("Driverca"))	{
		        	drvquery="select obj from Driver obj where obj.status=1 and obj.id not in ("+drvid+")" ;
		        }
		        else{
		        	drvquery="select obj from Driver obj where obj.status=1 and obj.id not in ("+drvid+")";
		        }								
				
				if (!StringUtils.isEmpty(company)) {
					drvquery = drvquery+" and obj.company in ("+company+")";
				}
				if (!StringUtils.isEmpty(terminal)) {
					drvquery = drvquery+" and obj.terminal in ("+terminal+")";
				}	            
				if (!StringUtils.isEmpty(driver)) {
					drvquery = drvquery+" and obj.id in ("+driver+")";
				}
				
			   
				List<Driver> drvlist=genericDAO.executeSimpleQuery(drvquery);				
				if(drvlist!=null && drvlist.size()>0){
					for(Driver drv:drvlist){
						
						if(driverIds.contains(drv.getId())){
							//do nothing
						}
						else{
						Double fuelNetValue=0.0;
						Double tollNetValue=0.0;
						
						fuelNetValue = fuelAmountMap.get(drv.getId());					
						
						tollNetValue =  tollAmountMap.get(drv.getId());
						
						if(fuelNetValue==null)
							fuelNetValue = 0.0;
						
						if(tollNetValue==null)
							tollNetValue = 0.0;	
						
						NetReportWrapper wrapper = new NetReportWrapper();
						wrapper.setCompany(drv.getCompany().getName());
						wrapper.setFuellogCharge(fuelNetValue);
						wrapper.setNetAmount(0.0);
						wrapper.setSubcontractorCharge(0.0);
						wrapper.setTransportationAmount(0.0);
						wrapper.setSumTotal(0.0);
						wrapper.setDriver(drv.getFullName());
						wrapper.setTollTagAmount(tollNetValue);					
						wrapperlist.add(wrapper);
					}
					}
					
				}
				
			}
			
			
			if(!StringUtils.isEmpty(drvid)){
				
				String drvquery="";
				if(DriverReport.equalsIgnoreCase("Drivercc") || DriverReport.equalsIgnoreCase("Driverca"))	{
		        	drvquery="select obj from Driver obj where  obj.id not in ("+drvid+") and ((obj.dateHired <='"+oracleFormatter.format(mainStartDate)+"' and obj.dateTerminated >='"+oracleFormatter.format(mainEndDate)+"') OR (obj.dateHired >='"+oracleFormatter.format(mainStartDate)+"' and obj.dateTerminated <='"+oracleFormatter.format(mainEndDate)+"') OR (obj.dateHired <='"+oracleFormatter.format(mainStartDate)+"' and (obj.dateTerminated >='"+oracleFormatter.format(mainStartDate)+"' and obj.dateTerminated <='"+oracleFormatter.format(mainEndDate)+"')) OR ((obj.dateHired >='"+oracleFormatter.format(mainStartDate)+"' and obj.dateHired <='"+oracleFormatter.format(mainEndDate)+"')and obj.dateTerminated >='"+oracleFormatter.format(mainEndDate)+"'))";
				}
		        else{
		        	drvquery="select obj from Driver obj where  obj.id not in ("+drvid+") and ((obj.dateHired <='"+oracleFormatter.format(mainStartDate)+"' and obj.dateTerminated >='"+oracleFormatter.format(mainEndDate)+"') OR (obj.dateHired >='"+oracleFormatter.format(mainStartDate)+"' and obj.dateTerminated <='"+oracleFormatter.format(mainEndDate)+"') OR (obj.dateHired <='"+oracleFormatter.format(mainStartDate)+"' and (obj.dateTerminated >='"+oracleFormatter.format(mainStartDate)+"' and obj.dateTerminated <='"+oracleFormatter.format(mainEndDate)+"')) OR ((obj.dateHired >='"+oracleFormatter.format(mainStartDate)+"' and obj.dateHired <='"+oracleFormatter.format(mainEndDate)+"')and obj.dateTerminated >='"+oracleFormatter.format(mainEndDate)+"'))";		
		        }									
				
				if (!StringUtils.isEmpty(company)) {
					drvquery = drvquery+" and obj.company in ("+company+")";
				}
				if (!StringUtils.isEmpty(terminal)) {
					drvquery = drvquery+" and obj.terminal in ("+terminal+")";
				}	            
				if (!StringUtils.isEmpty(driver)) {
					drvquery = drvquery+" and obj.id in ("+driver+")";
				}
				
			   
				System.out.println("*********** The Driver Toll Query and Fuel Query is "+drvquery);
				
				List<Driver> drvlist=genericDAO.executeSimpleQuery(drvquery);				
				if(drvlist!=null && drvlist.size()>0){
					for(Driver drv:drvlist){
						
						if(driverIds.contains(drv.getId())){
							//do nothing
						}
						else{
						Double fuelNetValue=0.0;
						Double tollNetValue=0.0;
						
						fuelNetValue = fuelAmountMap.get(drv.getId());					
						
						tollNetValue =  tollAmountMap.get(drv.getId());
						
						if(fuelNetValue==null)
							fuelNetValue = 0.0;
						
						if(tollNetValue==null)
							tollNetValue = 0.0;	
						
						NetReportWrapper wrapper = new NetReportWrapper();
						wrapper.setCompany(drv.getCompany().getName());
						wrapper.setFuellogCharge(fuelNetValue);
						wrapper.setNetAmount(0.0);
						wrapper.setSubcontractorCharge(0.0);
						wrapper.setTransportationAmount(0.0);
						wrapper.setSumTotal(0.0);
						wrapper.setDriver(drv.getFullName());
						wrapper.setTollTagAmount(tollNetValue);					
						wrapperlist.add(wrapper);
					}
					}
					
				}
				
			}
			
			
			
			
			}		
			 if (wrapperlist.size() > 0) {				 
				    Collections.sort(wrapperlist, new Comparator<NetReportWrapper>() {
				        @Override
				        public int compare(final NetReportWrapper object1, final NetReportWrapper object2) {
				            return object1.getDriver().compareTo(object2.getDriver());
				        }
				       } );
			}
			 
			 
			    String tempName="name";
				NetReportWrapper tempObj=null;
				int count=0;
				for(NetReportWrapper obj:wrapperlist){		
					count++;
					if(!tempName.equals(obj.getDriver())){
						tempName = obj.getDriver();
						if(tempObj!=null){
							finalwrapperlist.add(tempObj);
						}
						tempObj = obj;
						if(count==wrapperlist.size()){
							finalwrapperlist.add(tempObj);
						}
						
					}
					else{
						obj.setFuellogCharge(obj.getFuellogCharge()+tempObj.getFuellogCharge());
						obj.setTollTagAmount(obj.getTollTagAmount()+tempObj.getTollTagAmount());
						obj.setTransportationAmount(obj.getTransportationAmount()+tempObj.getTransportationAmount());
						obj.setSubcontractorCharge(obj.getSubcontractorCharge()+tempObj.getSubcontractorCharge());
						obj.setSumTotal(obj.getSumTotal()+tempObj.getSumTotal());
						obj.setNetAmount(obj.getNetAmount()+tempObj.getNetAmount());
						tempObj = obj;
						if(count==wrapperlist.size()){
							finalwrapperlist.add(tempObj);
						}
					}			
					
				}			 
			 
		    */
			//}
		
		
		/*if (DriverReport != null) {
			return finalwrapperlist;
		}
		else{*/
			return wrapperlist;
		//}
	}
	
	
	
	
	double ProcessAmount(Ticket ticket,Billing billing){
		System.out.println("\nTicket is not invoiced\n");
		Map<String, List<BillingRate>> billingMap = new HashMap<String, List<BillingRate>>();
		BillingRate billingRate = null;
		Double sumFuelSurcharge = 0.0;
		Double sumTonnage = 0.0;
		Double sumDemmurage = 0.0;

		try {
			Long destination_id;
			Location location = genericDAO.getById(
					Location.class, ticket.getDestination()
							.getId());
			if (location.getName().equalsIgnoreCase("grows")
					|| location.getName().equalsIgnoreCase(
							"tullytown")) {
				destination_id = 91l;
			} else {
				destination_id = ticket.getDestination()
						.getId();
			}
			String rateQuery = "select obj from BillingRate obj where obj.transferStation='"
					+ ticket.getOrigin().getId()
					+ "' and obj.landfill='"
					+ destination_id
					+ "'";
			String key = ticket.getOrigin().getId() + "_"
					+ destination_id;
			List<BillingRate> fs = null;
			fs = billingMap.get(key);
			if (fs == null) {
				fs = genericDAO.executeSimpleQuery(rateQuery);
				billingMap.put(key, fs);
			}

			if (fs != null && fs.size() > 0) {
				for (BillingRate rate : fs) {
					if (rate.getRateUsing() == null) {
						billingRate = rate;
						break;
					} else if (rate.getRateUsing() == 1) {
						// calculation for a load date
						if ((ticket.getLoadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getLoadDate()
										.getTime() <= rate
										.getValidTo().getTime())) {
							billingRate = rate;
							break;
						}
					} else if (rate.getRateUsing() == 2) {
						// calculation for a unload date
						if ((ticket.getUnloadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getUnloadDate()
										.getTime() <= rate
										.getValidTo().getTime())) {
							billingRate = rate;
							break;
						}
					}
				}
			}// if (fs != null && fs.size() > 0)

		} catch (Exception e) {

		}
		if (billingRate != null) {
			// billing.setCustomer(billingRate.getCustomername().getName());
			int billUsing = (billingRate.getBillUsing() == null) ? 1
					: billingRate.getBillUsing();
			int rateType = billingRate.getRateType();
			if (billUsing == 1) {
				billing.setBillUsing("Transfer");
			}
			if (billUsing == 2) {
				billing.setBillUsing("Landfill");
			}
			if (billUsing == 1) {
				if (rateType == 2 || rateType == 3) {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getTransferGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						Double originNetWt = minbilgrosswt
								- ticket.getTransferTare();
						billing.setEffectiveNetWt(originNetWt);
						billing.setEffectiveTonsWt(originNetWt / 2000.0);
					} else {
						billing.setEffectiveGrossWt(ticket
								.getTransferGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						billing.setEffectiveNetWt(ticket
								.getTransferNet());
						billing.setEffectiveTonsWt(ticket
								.getTransferTons());
					}
				}
			} else {
				if (rateType == 2 || rateType == 3) {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getLandfillGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						Double destinationNetWt = minbilgrosswt
								- ticket.getLandfillTare();
						billing.setEffectiveNetWt(destinationNetWt);
						billing.setEffectiveTonsWt(destinationNetWt / 2000.0);

					} else {
						billing.setEffectiveGrossWt(ticket
								.getLandfillGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						billing.setEffectiveNetWt(ticket
								.getLandfillNet());
						billing.setEffectiveTonsWt(ticket
								.getLandfillTons());
					}
				}
			}
			// int rateType = billingRate.getRateType();
			if (rateType == 1) {
				if (billUsing == 1) {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getTransferGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						Double originNetWt = minbilgrosswt
								- ticket.getTransferTare();
						billing.setEffectiveNetWt(originNetWt);
					} else {
						billing.setEffectiveGrossWt(ticket
								.getTransferGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						billing.setEffectiveNetWt(ticket
								.getTransferNet());
					}
				} else {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getLandfillGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						Double destinationNetWt = minbilgrosswt
								- ticket.getLandfillTare();
						billing.setEffectiveNetWt(destinationNetWt);
					} else {
						billing.setEffectiveGrossWt(ticket
								.getLandfillGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						billing.setEffectiveNetWt(ticket
								.getLandfillNet());
					}
				}
				billing.setRate(billingRate.getValue());
			// Change to 8.35 - 28th Dec 2016
				billing.setAmount((billing.getEffectiveNetWt() / 8.35)
						* billingRate.getValue());
			} else if (rateType == 2) {
				// per load
				billing.setRate(billingRate.getValue());
				billing.setAmount(billingRate.getValue());
			} else if (rateType == 3) {
				// per tonne
				billing.setRate(billingRate.getValue());
				billing.setAmount(billing.getEffectiveTonsWt()
						* billingRate.getValue());
			}
			// sumGallon+=billing.getGallon();
			// sumBillableTon+=billing.getEffectiveTonsWt();
			// sumOriginTon+=billing.getOriginTonsWt();
			// sumDestinationTon+=billing.getDestinationTonsWt();
			// sumNet+=billing.getEffectiveNetWt();
			// sumAmount+=billing.getAmount();
			billing.setAmount(MathUtil.roundUp(
					billing.getAmount(), 2));
			String fuelSurchargeType = billingRate
					.getFuelSurchargeType();
			Double fuelSurcharge = 0.0;
			if ("N".equalsIgnoreCase(fuelSurchargeType)) {
				fuelSurcharge = 0.0;
			}
			if ("M".equalsIgnoreCase(fuelSurchargeType)) {
				Double fuelSurchargePerTon = billingRate
						.getSurchargePerTon();
				Double surchargeAmount = billingRate
						.getSurchargeAmount();
				if (fuelSurchargePerTon == null
						&& surchargeAmount == null)
					fuelSurcharge = 0.0;
				else if (fuelSurchargePerTon != null) {
					fuelSurcharge = billing
							.getEffectiveTonsWt()
							* fuelSurchargePerTon;
				} else {
					fuelSurcharge = surchargeAmount;
				}
			}

			if ("A".equalsIgnoreCase(fuelSurchargeType)) {
				FuelSurchargePadd currentPadd = billingRate
						.getPadd();
				if (currentPadd != null) {
					Integer paddUsing = billingRate
							.getPaddUsing();
					Double padd = 0.0;
					StringBuffer paddBuffer = new StringBuffer(
							"select obj from FuelSurchargePadd obj where obj.code='"
									+ currentPadd.getCode()
									+ "'");
					Date effectiveDate = null;
					if (paddUsing != null) {
						if (paddUsing == 1) {
							effectiveDate = ticket
									.getLoadDate();
						} else if (paddUsing == 2) {
							effectiveDate = ticket
									.getUnloadDate();
						} else if (paddUsing == 3) {
							effectiveDate = ticket
									.getBillBatch();
						}
					}
					if (effectiveDate != null) {
						paddBuffer.append(" and obj.validTo>='"
								+ mysqldf.format(effectiveDate)
								+ "' and obj.validFrom<='"
								+ mysqldf.format(effectiveDate)
								+ "'");
					}
					List<FuelSurchargePadd> padds = genericDAO
							.executeSimpleQuery(paddBuffer
									.toString());
					if (padds != null && padds.size() > 0) {
						padd = padds.get(0).getAmount();
					}
					if ((padd != -1)
							&& (billingRate.getPeg() != null)
							&& (billingRate.getMiles() != null)) {
						int sign = 1;
						if ((padd - billingRate.getPeg()) < 0) {
							sign = -1;
						}
						double term = Math.floor(sign
								* (padd - billingRate.getPeg())
								/ 0.05);
						fuelSurcharge = sign * term
								* billingRate.getMiles() * 0.01;
					}
					if ((padd != -1)
							&& (billingRate.getPeg() != null)
							&& (billingRate.getMiles() == null)) {
						double percentage = Math
								.floor((padd - billingRate
										.getPeg()) / 0.08);
						if (rateType == 2) {
							fuelSurcharge = percentage
									* billingRate.getValue()
									* 0.01;
						} else if (rateType == 3) {
							// per tonne
							fuelSurcharge = billing
									.getEffectiveTonsWt()
									* billingRate.getValue()
									* percentage * 0.01;
						}
					}
				}
				// Weekly fuel surcharge calculation
				int wfsr = billingRate
						.getFuelsurchargeweeklyRate();
				if (wfsr == 1) {
					StringBuffer weeklyRateQuery = new StringBuffer(
							"select obj from FuelSurchargeWeeklyRate obj where obj.transferStations="
									+ ticket.getOrigin()
											.getId()
									+ " and obj.landfillStations="
									+ ticket.getDestination()
											.getId() + " ");
					FuelSurchargeWeeklyRate fuelsurchargeweeklyrate = null;
					Date effectiveDatePadd = null;
					if (billingRate.getWeeklyRateUsing() != null) {
						if (billingRate.getWeeklyRateUsing() == 1) {
							effectiveDatePadd = ticket
									.getLoadDate();
						}
						if (billingRate.getWeeklyRateUsing() == 2) {
							effectiveDatePadd = ticket
									.getUnloadDate();
						}
						if (billingRate.getWeeklyRateUsing() == 3) {
							effectiveDatePadd = ticket
									.getBillBatch();
						}
						weeklyRateQuery
								.append(" and obj.fromDate <= '"
										+ mysqldf
												.format(effectiveDatePadd)
										+ "' and obj.toDate >= '"
										+ mysqldf
												.format(effectiveDatePadd)
										+ "'");
					}
					List<FuelSurchargeWeeklyRate> listfswr = genericDAO
							.executeSimpleQuery(weeklyRateQuery
									.toString());
					if (listfswr != null && listfswr.size() > 0) {
						fuelsurchargeweeklyrate = listfswr
								.get(0);
					}
					if (fuelsurchargeweeklyrate != null) {
						int fswrateType = fuelsurchargeweeklyrate
								.getRateType();
						if (fswrateType == 3) {
							fuelSurcharge = billing
									.getEffectiveTonsWt()
									* fuelsurchargeweeklyrate
											.getFuelSurchargeRate();
						}
						if (fswrateType == 2) {
							fuelSurcharge = fuelsurchargeweeklyrate
									.getFuelSurchargeRate();
						}

						if (fswrateType == 5) {
							fuelSurcharge = fuelsurchargeweeklyrate
									.getFuelSurchargeRate()
									* billing.getAmount();
						}

						if (fswrateType == 1) {
						// Change to 8.35 - 28th Dec 2016
							fuelSurcharge = (billing
									.getEffectiveNetWt() / 8.35)
									* fuelsurchargeweeklyrate
											.getFuelSurchargeRate();
						}
					}
				}
			}// end of Weekly fuel surcharge calculation
			sumFuelSurcharge += fuelSurcharge;
			fuelSurcharge = MathUtil.roundUp(fuelSurcharge, 2);
			billing.setFuelSurcharge(fuelSurcharge);

			// if (billingRate.getTonnagePremium() != null) {
			if (billingRate.getTonnagePremium().getCode() != null) {
				Double premiumTonne = billingRate
						.getTonnagePremium().getPremiumTonne();
				Double rate = billingRate.getTonnagePremium()
						.getRate();
				if (billing.getEffectiveTonsWt() > premiumTonne) {
					billing.setTonnagePremium((billing
							.getEffectiveTonsWt() - premiumTonne)
							* rate);
				}
			} else {
				billing.setTonnagePremium(0.0);
			}
			sumTonnage += billing.getTonnagePremium();
			billing.setTonnagePremium(MathUtil.roundUp(
					billing.getTonnagePremium(), 2));
			if(billingRate.getDemmurageCharge()!=null){
			if (billingRate.getDemmurageCharge().getDemurragecode() != null) {
				Integer chargeAfterTime = billingRate.getDemmurageCharge().getChargeAfter();							
				Integer incrementTime = billingRate.getDemmurageCharge().getTimeIncrements();
				Double rate = billingRate.getDemmurageCharge().getDemurragerate();
				if(billingRate.getDemmurageCharge().getTimesUsed().equals("1")){
					String[] landfillInhourMin = ticket.getLandfillTimeIn().split(":");
				    int landfillInhour = Integer.parseInt(landfillInhourMin[0]);
				    int landfillInmins = Integer.parseInt(landfillInhourMin[1]);
				    int landfillInhoursInMins = landfillInhour * 60;
				    int landfilltimein = landfillInhoursInMins+landfillInmins;
				   
				    String[] landfillOuthourMin = ticket.getLandfillTimeOut().split(":");
				    int landfillOuthour = Integer.parseInt(landfillOuthourMin[0]);
				    int landfillOutmins = Integer.parseInt(landfillOuthourMin[1]);
				    int landfillOuthoursInMins = landfillOuthour * 60;
				    int landfilltimeout = landfillOuthoursInMins+landfillOutmins;
				   
				    int diffTime = landfilltimeout - landfilltimein;
				    if(diffTime < 0){
				    	diffTime = 1440+diffTime;
				    }
				    double minCharge = 0.0;
				    if(diffTime > chargeAfterTime){
				    	minCharge = rate;
				    	
				    	int timeRemaining = diffTime - (chargeAfterTime+incrementTime);
				    	double numberOfIteration = (double)timeRemaining / (double)incrementTime;					    	
				    	for(int i=0;i<numberOfIteration;i++){					    		
				    		//if(timeRemaining > incrementTime){
				    			minCharge = minCharge + rate;
				    			timeRemaining = timeRemaining - incrementTime;
				    		//}
				    	}					    	
				    }	
				    billing.setDemurrageCharge(minCharge);
				}
				else if(billingRate.getDemmurageCharge().getTimesUsed().equals("2")){
					String[] landfillInhourMin = ticket.getTransferTimeIn().split(":");
				    int landfillInhour = Integer.parseInt(landfillInhourMin[0]);
				    int landfillInmins = Integer.parseInt(landfillInhourMin[1]);
				    int landfillInhoursInMins = landfillInhour * 60;
				    int landfilltimein = landfillInhoursInMins+landfillInmins;				    
				    String[] landfillOuthourMin = ticket.getTransferTimeOut().split(":");
				    int landfillOuthour = Integer.parseInt(landfillOuthourMin[0]);
				    int landfillOutmins = Integer.parseInt(landfillOuthourMin[1]);
				    int landfillOuthoursInMins = landfillOuthour * 60;
				    int landfilltimeout = landfillOuthoursInMins+landfillOutmins;
				    
				    int diffTime = landfilltimeout - landfilltimein;
				    if(diffTime < 0){
				    	diffTime = 1440+diffTime;
				    }
				    
				    double minCharge = 0.0;
				    if(diffTime > chargeAfterTime){
				    	minCharge = rate;
				    	
				    	int timeRemaining = diffTime - (chargeAfterTime+incrementTime);
				    	double numberOfIteration = (double) timeRemaining / (double) incrementTime;					    	
				    	for(int i=0;i<numberOfIteration;i++){					    		
				    		//if(timeRemaining > incrementTime){
				    			minCharge = minCharge + rate;
				    			timeRemaining = timeRemaining - incrementTime;
				    		//}
				    	}
				    	
				    }
				    
				    billing.setDemurrageCharge(minCharge);
				}
				
			} 
			else {
				billing.setDemurrageCharge(0.0);
			}
		}
		else{
			billing.setDemurrageCharge(0.0);
		}
			
			
			sumDemmurage += billing.getDemurrageCharge();
			billing.setDemurrageCharge(MathUtil.roundUp(
					billing.getDemurrageCharge(), 2));

		} else {
			billing.setRate(0.0);
			billing.setFuelSurcharge(0.0);
		}
		double amount = billing.getAmount()
				+ billing.getFuelSurcharge()
				+ billing.getDemurrageCharge()
				+ billing.getTonnagePremium();
		return amount;
	}
	
	double ProcessSubcontractorAmount(Ticket ticket,SubcontractorBilling billing){
		double amount=0.0;
		double sumTotal=0.0;
		
		double sumOtherCharges=0.0;
		double sumAmount=0.0;
		
		double sumFuelSurcharge=0.0;
		SubcontractorRate billingRate = null;
		try {
			String rateQuery = "select obj from SubcontractorRate obj where obj.subcontractor='"
					+ ticket.getSubcontractor().getId()
					+ "' and obj.transferStation='"
					+ ticket.getOrigin().getId()
					+ "' and obj.landfill='"
					+ ticket.getDestination().getId()
					+ "' order by obj.validFrom desc";
			List<SubcontractorRate> fs = genericDAO
					.executeSimpleQuery(rateQuery);
			if (fs != null && fs.size() > 0) {
				for (SubcontractorRate rate : fs) {
					if (rate.getRateUsing() == null) {
						billingRate = rate;
						break;
					} else if (rate.getRateUsing() == 1) {
						// calculation for a load date
						if ((ticket.getLoadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getLoadDate().getTime() <= rate
										.getValidTo().getTime())) {
							billingRate = rate;
							break;
						}
					} else if (rate.getRateUsing() == 2) {
						// calculation for a unload date
						if ((ticket.getUnloadDate().getTime() >= rate
								.getValidFrom().getTime())
								&& (ticket.getUnloadDate().getTime() <= rate
										.getValidTo().getTime())) {
							billingRate = rate;
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//
		if (billingRate != null) {
			int billUsing = (billingRate.getBillUsing() == null) ? 1
					: billingRate.getBillUsing();
			int rateType = billingRate.getRateType();
			if (billUsing == 1) {
				billing.setBillUsing("Transfer");
			}
			if (billUsing == 2) {
				billing.setBillUsing("Landfill");
			}
			if (billUsing == 1) {
				if (rateType == 2 || rateType == 3) {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getTransferGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						Double originNetWt = minbilgrosswt
								- ticket.getTransferTare();
						billing.setEffectiveNetWt(originNetWt);
						billing.setEffectiveTonsWt(originNetWt / 2000.0);
					} else {
						billing.setEffectiveGrossWt(ticket
								.getTransferGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						billing.setEffectiveNetWt(ticket
								.getTransferNet());
						billing.setEffectiveTonsWt(ticket
								.getTransferTons());
					}
				}
			} else {
				if (rateType == 2 || rateType == 3) {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getLandfillGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						Double destinationNetWt = minbilgrosswt
								- ticket.getLandfillTare();
						billing.setEffectiveNetWt(destinationNetWt);
						billing.setEffectiveTonsWt(destinationNetWt / 2000.0);

					} else {
						billing.setEffectiveGrossWt(ticket
								.getLandfillGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						billing.setEffectiveNetWt(ticket
								.getLandfillNet());
						billing.setEffectiveTonsWt(ticket
								.getLandfillTons());
					}
				}
			}
			// int rateType = billingRate.getRateType();
			if (rateType == 1) {
				if (billUsing == 1) {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getTransferGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						Double originNetWt = minbilgrosswt
								- ticket.getTransferTare();
						billing.setEffectiveNetWt(originNetWt);
					} else {
						billing.setEffectiveGrossWt(ticket
								.getTransferGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getTransferTare());
						billing.setEffectiveNetWt(ticket
								.getTransferNet());
					}
				} else {
					Double minbilgrosswt = billingRate
							.getMinbillablegrossWeight();
					if (minbilgrosswt != null
							&& ticket.getLandfillGross() < minbilgrosswt) {
						billing.setEffectiveGrossWt(minbilgrosswt);
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						Double destinationNetWt = minbilgrosswt
								- ticket.getLandfillTare();
						billing.setEffectiveNetWt(destinationNetWt);
					} else {
						billing.setEffectiveGrossWt(ticket
								.getLandfillGross());
						billing.setMinimumbillablegrossweight(minbilgrosswt);
						billing.setEffectiveTareWt(ticket
								.getLandfillTare());
						billing.setEffectiveNetWt(ticket
								.getLandfillNet());
					}
				}
				billing.setRate(billingRate.getValue());
			// Change to 8.35 - 28th Dec 2016
				billing.setAmount((billing.getEffectiveNetWt() / 8.35)
						* billingRate.getValue());
			} else if (rateType == 2) {
				// per load
				billing.setRate(billingRate.getValue());
				billing.setAmount(billingRate.getValue());
			} else if (rateType == 3) {
				// per tonne
				billing.setRate(billingRate.getValue());
				billing.setAmount(billing.getEffectiveTonsWt()
						* billingRate.getValue());
			}
			/*sumBillableTon += billing.getEffectiveTonsWt();
			sumOriginTon += billing.getOriginTonsWt();
			sumDestinationTon += billing.getDestinationTonsWt();
			sumNet += billing.getEffectiveNetWt();*/
			sumAmount += billing.getAmount();
			billing.setAmount(MathUtil.roundUp(billing.getAmount(), 2));

			Double otherCharges = billingRate.getOtherCharges();
			otherCharges = MathUtil.roundUp(otherCharges, 2);
			billing.setOtherCharges(otherCharges);
			sumOtherCharges += otherCharges;

			Double fuelSurcharge = billingRate.getFuelSurchargeAmount();
			fuelSurcharge = MathUtil.roundUp(fuelSurcharge, 2);
			billing.setFuelSurcharge(fuelSurcharge);
			sumFuelSurcharge += fuelSurcharge;

			sumOtherCharges = MathUtil.roundUp(sumOtherCharges, 2);
			sumFuelSurcharge = MathUtil.roundUp(sumFuelSurcharge, 2);

		} else {

			billing.setRate(0.0);
			billing.setFuelSurcharge(0.0);
			/*sumOriginTon += billing.getOriginTonsWt();
			sumDestinationTon += billing.getDestinationTonsWt();*/

		}
		if (billingRate != null) {
			amount = billing.getAmount() + billing.getFuelSurcharge()+ billing.getOtherCharges();
			billing.setTotAmt(amount);
		}
		/*sumBillableTon = MathUtil.roundUp(sumBillableTon, 2);*/
        /*sumOriginTon = MathUtil.roundUp(sumOriginTon, 2);
        sumDestinationTon = MathUtil.roundUp(sumDestinationTon, 2);*/
        /*sumNet = MathUtil.roundUp(sumNet, 2);*/
       // sumAmount = MathUtil.roundUp(sumAmount, 2);
		sumTotal = sumAmount + sumFuelSurcharge + sumOtherCharges;
		
		System.out.println("\nFor Subcontractor --sumtotal===>"+sumTotal);
		
		
		
		return sumTotal;
	}
	
}
