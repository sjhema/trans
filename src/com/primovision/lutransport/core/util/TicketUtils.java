package com.primovision.lutransport.core.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import org.springframework.ui.Model;

import com.google.gson.Gson;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.driver.TripSheet;
import com.primovision.lutransport.model.hr.DriverPayRate;

public class TicketUtils {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
	public static void save(Ticket entity, String type, StringBuffer errorMsgBuff, GenericDAO genericDAO) {
		if (type.equals("complete")) {
			saveComplete(entity, errorMsgBuff, genericDAO);
		} 
		else {
			saveIncomplete(entity, errorMsgBuff, genericDAO);
		}
	}
	
	private static void saveComplete(Ticket entity, StringBuffer errorMsgBuff, GenericDAO genericDAO) {
		Long ticketId = -1l;
		Map<String, Object> criteria = new HashMap<String, Object>();
		
		if (entity.getId() != null) {
			ticketId = entity.getId();				
		}
		if (entity.getVehicle() == null) {
			errorMsgBuff.append("Vehicle, ");
		}
		if (entity.getTrailer() == null) {
			errorMsgBuff.append("Trailer, ");
		}
		if (entity.getDriver() == null) {
			errorMsgBuff.append("Driver, ");
		}
		if (entity.getDriverCompany() == null) {
			errorMsgBuff.append("Driver Company, ");
		}
		if (entity.getTerminal() == null) {
			errorMsgBuff.append("Terminal, ");
		}
		if (entity.getOrigin() == null) {
			errorMsgBuff.append("Origin, ");
		}
		if (entity.getDestination() == null) {
			errorMsgBuff.append("Destination, ");
		}
		if (entity.getOriginTicket() == null) {
			errorMsgBuff.append("Origin Ticket, ");
		}
		if (entity.getDestinationTicket() == null) {
			errorMsgBuff.append("Destination Ticket, ");
		}
		if (entity.getCreatedBy() == null) {
			errorMsgBuff.append("Created By, ");
		}
		if (entity.getLoadDate() == null) {
			errorMsgBuff.append("Load Date, ");
		}
		if (entity.getUnloadDate() == null) {
			errorMsgBuff.append("Unload Date, ");
		}
		if (entity.getBillBatch() == null) {
			errorMsgBuff.append("Batch Date, ");
		}
		if (entity.getUnloadDate() != null && entity.getLoadDate() != null) {
			if (entity.getUnloadDate().before(entity.getLoadDate())) {
				errorMsgBuff.append("Unload Date before Load Date, ");
			}
			
			if (entity.getBillBatch() != null) {
				validateDates(entity.getLoadDate(), entity.getUnloadDate(), entity.getBillBatch(), errorMsgBuff);
			}
		}
		
		if (entity.getOrigin() != null && entity.getDestination() != null 
				&& entity.getOriginTicket() != null && entity.getDestinationTicket() != null) {
			String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()
					+ " and obj.originTicket="+entity.getOriginTicket()+" and obj.id != "+ticketId;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
			if (tickets != null && tickets.size() > 0) {	
				errorMsgBuff.append("Duplicate Origin Ticket, ");		
			}
			
			String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()
					+ " and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
			List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
			if (tickets1 != null && tickets1.size() > 0){		
				errorMsgBuff.append("Duplicate Destination Ticket, ");			
			}
		}
		
		if (entity.getDriverCompany() != null) {
			if	(entity.getDriverCompany().getId() != 149l) {
				if (entity.getUnloadDate()!= null && entity.getBillBatch()!= null) {
					if (entity.getUnloadDate().after(entity.getBillBatch()) || entity.getUnloadDate().before(entity.getBillBatch())) {
						LocalDate billBatchDate = new LocalDate(entity.getBillBatch());
						int billBatchDateWeek = billBatchDate.get(DateTimeFieldType.weekOfWeekyear());
						int billBatchDateYear = billBatchDate.get(DateTimeFieldType.weekyear());
					
						LocalDate unloadDate = new LocalDate(entity.getUnloadDate());
						int unloadDateWeek = unloadDate.get(DateTimeFieldType.weekOfWeekyear());
						int unloadDateYear = unloadDate.get(DateTimeFieldType.weekyear());			
						
						if ((billBatchDateWeek != unloadDateWeek) || (billBatchDateYear != unloadDateYear)) {
							errorMsgBuff.append("Invalid Unload Date, ");
						}
					}
				}
			} else {
				// Do nothing
			}
		}	
	
		if (errorMsgBuff.length() != 0) {
			return;
		}
		
		if (entity.getVehicle() != null) {
			// The incoming ID only maps to the right unit number, not to the vehicle ID
			System.out.println("The vehicle UnitNum(ID) to be saved is " + entity.getVehicle().getId());
			System.out.println("The vehicle UnitNum to be saved is " + entity.getVehicle().getUnitNum());
			
			boolean sendError = true;
			
			String vehiclequery = "select obj from Vehicle obj where obj.type=1 and obj.unit in ("
					+ entity.getVehicle().getUnitNum()
					+") order by obj.validFrom desc";
			
			List<Vehicle> vehicleLists = genericDAO.executeSimpleQuery(vehiclequery);
			if (vehicleLists != null && vehicleLists.size() > 0) {
				for (Vehicle vehicleObj : vehicleLists) {
					System.out.println("Vehicle id mapped to unit number " + entity.getVehicle().getUnitNum() + " is "
							+ vehicleObj.getId());
					if (vehicleObj.getValidFrom() != null && vehicleObj.getValidTo() != null) {
						if ((entity.getLoadDate().getTime() >= vehicleObj.getValidFrom().getTime()
								&& entity.getLoadDate().getTime() <= vehicleObj.getValidTo().getTime())
								&& (entity.getUnloadDate().getTime() >= vehicleObj.getValidFrom().getTime()
										&& entity.getUnloadDate().getTime() <= vehicleObj.getValidTo().getTime())) {
							entity.setVehicle(vehicleObj);
							sendError = false;
							break;
						}
					}
				}
			}

			if (sendError) {
				errorMsgBuff.append("No Matching Vehicle Entries Found for Selected Truck and Entered Date(s), ");
				return;
			}
		}
		
		if (entity.getTrailer() != null) {
			boolean sendError = true;
			
			Vehicle vehob = genericDAO.getById(Vehicle.class,entity.getTrailer().getId());
			String vehiclequery = "select obj from Vehicle obj where obj.type=2 and obj.unit in ("
				+vehob.getUnit()
				+") order by obj.validFrom desc";
			
			List<Vehicle> vehicleLists = genericDAO.executeSimpleQuery(vehiclequery);
			if (vehicleLists!=null && vehicleLists.size() > 0) {				
				for (Vehicle vehicleObj : vehicleLists) {					
					if (vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
						if ((entity.getLoadDate().getTime() >= vehicleObj.getValidFrom().getTime() 
								&& entity.getLoadDate().getTime() <= vehicleObj.getValidTo().getTime()) 
								&& (entity.getUnloadDate().getTime() >= vehicleObj.getValidFrom().getTime() 
								&& entity.getUnloadDate().getTime()<= vehicleObj.getValidTo().getTime())) {
							entity.setTrailer(vehicleObj);
							sendError = false;
							break;
						}	
					}
				}
			}
			
			if (sendError) {
				errorMsgBuff.append("No Matching Vehicle Entries Found for Selected Trailer and Entered Date(s), ");
				return;
			}
		}
		
		entity.setStatus(1);
		if (entity.getTicketStatus() == null) {				
			entity.setTicketStatus(1);
		}
		
		if (entity.getPayRollStatus() == null) {
			if (entity.getPayRollBatch() == null)
				entity.setPayRollStatus(1);
			else
				entity.setPayRollStatus(2);
		} else {
			if (entity.getPayRollBatch()==null) {
				//Do nothing
			} else {
				entity.setPayRollStatus(2);
			}
		}
		
		//FOR CUSTOMER AND COMPANY_LOCATION
		String query;
		//For Grows or Tullytown
		if (entity.getDestination().getId() == 253 || entity.getDestination().getId() == 254) {
			query = "select obj from BillingRate obj where obj.transferStation="+entity.getOrigin().getId()+" and obj.landfill=91";
		} else {
			query = "select obj from BillingRate obj where obj.transferStation="+entity.getOrigin().getId()+" and obj.landfill="+entity.getDestination().getId();
		}
		
		List<BillingRate> rates = genericDAO.executeSimpleQuery(query);
		BillingRate billingRate = null;
		for (BillingRate rate : rates) {
			if (rate.getRateUsing() == null) {
				billingRate = rate;
				break;
			} else if (rate.getRateUsing() == 1) {
				// Calculation for a load date
				if ((entity.getLoadDate().getTime() >= rate.getValidFrom().getTime()) && (entity.getLoadDate().getTime() <= rate.getValidTo().getTime())) {
					billingRate = rate;
					break;
				}
			} else if (rate.getRateUsing() == 2) {
				// Calculation for a unload date
				if ((entity.getUnloadDate().getTime() >= rate.getValidFrom().getTime()) && (entity.getUnloadDate().getTime() <= rate.getValidTo().getTime())) {
					billingRate = rate;
					break;
				}
			}
		}
		
      if (billingRate != null) {
      	entity.setCustomer((billingRate.getCustomername()!=null) ? billingRate.getCustomername() : null);
      	entity.setCompanyLocation((billingRate.getCompanyLocation()!=null) ? billingRate.getCompanyLocation() : null);
		  
		  	// Change to 8.35 - 28th Dec 2016
     		entity.setGallons(entity.getTransferNet()/8.35);
     		
     		Driver driver = genericDAO.getById(Driver.class,Long.valueOf(entity.getDriver().getId()));
     		entity.setTerminal(driver.getTerminal());	
		
     		if (entity.getSubcontractor() != null) {
			SubContractor subcontractor = genericDAO.getById(SubContractor.class, entity.getSubcontractor().getId());
			if (subcontractor.getName().equalsIgnoreCase("blank"))
				entity.setSubcontractor(null);	
     		}
     	}
		
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setEnteredBy(user.getName());
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(user.getId());
		
		// Merge into datasource
		genericDAO.saveOrUpdate(entity);
		
		String tripsheetQuery = "select obj from TripSheet obj where (obj.origin in ("+entity.getOrigin().getId()+") and obj.originTicket in ("+entity.getOriginTicket()
			+")) OR (obj.destination in ("+entity.getDestination().getId()+") and obj.destinationTicket in ("+entity.getDestinationTicket()+"))";
		List<TripSheet> tripsheets  = genericDAO.executeSimpleQuery(tripsheetQuery);
		if (tripsheets != null && tripsheets.size() > 0) {
			for (TripSheet tripsheet : tripsheets) {
				tripsheet.setBatchDate(entity.getBillBatch());
				tripsheet.setDriverCompany(entity.getDriverCompany());
				
				criteria.clear();
				criteria.put("id", entity.getDriverCompany().getId());
				Location loc = genericDAO.getByCriteria(Location.class, criteria);
				tripsheet.setCompanyName(loc.getName());	
				
				tripsheet.setTerminal(entity.getTerminal());
				tripsheet.setTerminalName(entity.getTerminal().getName());				
				tripsheet.setDriver(entity.getDriver());				
				tripsheet.setTrailer(entity.getTrailer());
				tripsheet.setTempTrailer(entity.getTrailer().getUnitNum());				
				tripsheet.setTruck(entity.getVehicle());
				tripsheet.setTempTruck(entity.getVehicle().getUnitNum());
				tripsheet.setDestination(entity.getDestination());
				tripsheet.setDestinationTicket(entity.getDestinationTicket());					
				tripsheet.setIncomplete("No");
				tripsheet.setLoadDate(entity.getLoadDate());
				tripsheet.setOrigin(entity.getOrigin());
				tripsheet.setOriginTicket(entity.getOriginTicket());					
				tripsheet.setUnloadDate(entity.getUnloadDate());
				tripsheet.setVerificationStatus("Verified");
				
				genericDAO.saveOrUpdate(tripsheet);
			}
		} else {
			TripSheet tripsheet = new TripSheet();
			tripsheet.setBatchDate(entity.getBillBatch());
			tripsheet.setDriverCompany(entity.getDriverCompany());
			
			criteria.clear();
			criteria.put("id", entity.getDriverCompany().getId());
			Location loc = genericDAO.getByCriteria(Location.class, criteria);
			tripsheet.setCompanyName(loc.getName());	
			
			tripsheet.setTerminal(entity.getTerminal());
			tripsheet.setTerminalName(entity.getTerminal().getName());				
			tripsheet.setDriver(entity.getDriver());				
			tripsheet.setTrailer(entity.getTrailer());
			tripsheet.setTempTrailer(entity.getTrailer().getUnitNum());				
			tripsheet.setTruck(entity.getVehicle());
			tripsheet.setTempTruck(entity.getVehicle().getUnitNum());
			tripsheet.setDestination(entity.getDestination());
			tripsheet.setDestinationTicket(entity.getDestinationTicket());					
			tripsheet.setIncomplete("No");
			tripsheet.setLoadDate(entity.getLoadDate());
			tripsheet.setOrigin(entity.getOrigin());
			tripsheet.setOriginTicket(entity.getOriginTicket());					
			tripsheet.setUnloadDate(entity.getUnloadDate());
			tripsheet.setVerificationStatus("Verified");
			tripsheet.setEnteredBy("system");
			
			genericDAO.saveOrUpdate(tripsheet);			
		}
	}
	
	private static void saveIncomplete(Ticket entity, StringBuffer errorMsgBuff, GenericDAO genericDAO) {
		Long ticketId = -1l;
		Map<String, Object> criteria = new HashMap<String, Object>();
		
		if (entity.getId() != null) {
			ticketId = entity.getId();				
		}	
		if (entity.getVehicle() == null) {
			errorMsgBuff.append("Vehicle, ");
		}
		if (entity.getTrailer() == null) {
			errorMsgBuff.append("Trailer, ");
		}
		if (entity.getDriver() == null) {
			errorMsgBuff.append("Driver, ");
		}
		if (entity.getDriverCompany() == null) {
			errorMsgBuff.append("Driver Company, ");
		}
		if (entity.getTerminal() == null) {
			errorMsgBuff.append("Terminal, ");
		}
		if (entity.getOrigin() == null) {
			errorMsgBuff.append("Origin, ");
		}
		if (entity.getDestination() == null) {
			errorMsgBuff.append("Destination, ");
		}
		if (entity.getOriginTicket() == null) {
			errorMsgBuff.append("Origin Ticket, ");
		}
		if (entity.getDestinationTicket() == null) {
			errorMsgBuff.append("Destination Ticket, ");
		}
		if (entity.getCreatedBy() == null) {
			errorMsgBuff.append("Created By, ");
		}
		if (entity.getLoadDate() == null) {
			errorMsgBuff.append("Load Date, ");
		}
		if (entity.getUnloadDate() == null) {
			errorMsgBuff.append("Unload Date, ");
		}
		if (entity.getBillBatch() == null) {
			errorMsgBuff.append("Batch Date, ");
		}
		
		if (entity.getUnloadDate() != null && entity.getLoadDate() != null) {
			if (entity.getUnloadDate().before(entity.getLoadDate())) {
				errorMsgBuff.append("Unload Date before Load Date, ");
			}
			
			if (entity.getBillBatch() != null) {
				validateDates(entity.getLoadDate(), entity.getUnloadDate(), entity.getBillBatch(), errorMsgBuff);
			}
		}
		
		if (entity.getOrigin() != null && entity.getDestination() != null 
				&& entity.getOriginTicket() != null && entity.getDestinationTicket() != null) {
			String origin = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()+" and obj.originTicket="
				+entity.getOriginTicket()+" and obj.id != "+ticketId;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
			if (tickets != null && tickets.size() > 0) {	
				errorMsgBuff.append("Duplicate Origin Ticket, ");		
			}
			
			String destination = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()
					+" and obj.destinationTicket="+entity.getDestinationTicket()+" and obj.id != "+ticketId;
			List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
			if (tickets1 != null && tickets1.size() > 0) {
				errorMsgBuff.append("Duplicate Destination Ticket, ");		
			}
		}	
		
		if (errorMsgBuff.length() != 0) {
			return;
		}
	
		entity.setStatus(3);
		if (entity.getTicketStatus()!=null) {
			if (entity.getTicketStatus()!=2) {
				System.out.println("\nNEXT entity.getTicketStatus()!=2\n");
				entity.setTicketStatus(0);
			}
		} else {
			entity.setTicketStatus(0);
		}
		
		if (entity.getPayRollStatus() == null) {
			if (entity.getPayRollBatch() == null) {
				entity.setPayRollStatus(1);
			} else {
				entity.setPayRollStatus(2);
			}
		} else {
			if (entity.getPayRollBatch()==null) {
				//Do nothing
			} else {
				entity.setPayRollStatus(2);
			}
		}
		
		if (entity.getSubcontractor() != null) {
			SubContractor subcontractor = genericDAO.getById(SubContractor.class, entity.getSubcontractor().getId());
			if(subcontractor.getName().equalsIgnoreCase("blank"))
				entity.setSubcontractor(null);				
		}
		
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setEnteredBy(user.getName());
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(user.getId());
		
		genericDAO.saveOrUpdate(entity);
		
		String tripsheetQuery = "select obj from TripSheet obj where (obj.origin in ("+entity.getOrigin().getId()+") and obj.originTicket in ("+entity.getOriginTicket()+")) "
				+ " OR (obj.destination in ("+entity.getDestination().getId()+") and obj.destinationTicket in ("+entity.getDestinationTicket()+"))";
		List<TripSheet> tripsheets = genericDAO.executeSimpleQuery(tripsheetQuery);
		if (tripsheets != null && tripsheets.size() > 0) {
			for (TripSheet tripsheet:tripsheets) {
				tripsheet.setBatchDate(entity.getBillBatch());
				if (entity.getDriverCompany() != null) {
					tripsheet.setDriverCompany(entity.getDriverCompany());
					
					criteria.clear();
					criteria.put("id", entity.getDriverCompany().getId());
					Location loc = genericDAO.getByCriteria(Location.class, criteria);
					tripsheet.setCompanyName(loc.getName());
				}
				if (entity.getTerminal() != null) {
					tripsheet.setTerminal(entity.getTerminal());
					
					criteria.clear();
					criteria.put("id", entity.getTerminal().getId());
					Location loc = genericDAO.getByCriteria(Location.class, criteria);
					tripsheet.setTerminalName(loc.getName());
				}
				if (entity.getDriver() != null) {
					tripsheet.setDriver(entity.getDriver());
				}
				if (entity.getTrailer() != null) {
					tripsheet.setTrailer(entity.getTrailer());
					criteria.clear();
					criteria.put("id", entity.getTrailer().getId());
					Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
					tripsheet.setTempTrailer(veh.getUnitNum());
				}
				if (entity.getVehicle() != null) {
					tripsheet.setTruck(entity.getVehicle());
					criteria.clear();
					criteria.put("id", entity.getVehicle().getId());
					Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
					tripsheet.setTempTruck(veh.getUnitNum());
				}
				
				tripsheet.setDestination(entity.getDestination());
				tripsheet.setDestinationTicket(entity.getDestinationTicket());					
				tripsheet.setIncomplete("No");
				tripsheet.setLoadDate(entity.getLoadDate());
				tripsheet.setOrigin(entity.getOrigin());
				tripsheet.setOriginTicket(entity.getOriginTicket());					
				tripsheet.setUnloadDate(entity.getUnloadDate());
				tripsheet.setVerificationStatus("Verified");
				
				genericDAO.saveOrUpdate(tripsheet);
			}
		} else {
			TripSheet tripsheet = new TripSheet();

			tripsheet.setBatchDate(entity.getBillBatch());
			if (entity.getDriverCompany()!=null) {
				tripsheet.setDriverCompany(entity.getDriverCompany());
				criteria.clear();
				criteria.put("id", entity.getDriverCompany().getId());
				Location loc = genericDAO.getByCriteria(Location.class, criteria);
				tripsheet.setCompanyName(loc.getName());
			} 
			if (entity.getTerminal() != null) {
				tripsheet.setTerminal(entity.getTerminal());
				criteria.clear();
				criteria.put("id", entity.getTerminal().getId());
				Location loc = genericDAO.getByCriteria(Location.class, criteria);
				tripsheet.setTerminalName(loc.getName());
			}
			if (entity.getDriver() != null) {
				tripsheet.setDriver(entity.getDriver());
			}
			if (entity.getTrailer() != null) {
				tripsheet.setTrailer(entity.getTrailer());
				criteria.clear();
				criteria.put("id", entity.getTrailer().getId());
				Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
				tripsheet.setTempTrailer(veh.getUnitNum());
			}
			if (entity.getVehicle() != null) {
				tripsheet.setTruck(entity.getVehicle());
				criteria.clear();
				criteria.put("id", entity.getVehicle().getId());
				Vehicle veh = genericDAO.getByCriteria(Vehicle.class, criteria);
				tripsheet.setTempTruck(veh.getUnitNum());
			}
			
			tripsheet.setDestination(entity.getDestination());
			tripsheet.setDestinationTicket(entity.getDestinationTicket());					
			tripsheet.setIncomplete("No");
			tripsheet.setLoadDate(entity.getLoadDate());
			tripsheet.setOrigin(entity.getOrigin());
			tripsheet.setOriginTicket(entity.getOriginTicket());					
			tripsheet.setUnloadDate(entity.getUnloadDate());
			tripsheet.setVerificationStatus("Verified");
			tripsheet.setEnteredBy("system");
			genericDAO.saveOrUpdate(tripsheet);
		}
	}
	
	public static void save(TripSheet entity, StringBuffer errorMsgBuff, GenericDAO genericDAO) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		if (entity.getOriginTicket() == null && entity.getDestinationTicket() == null) {
			errorMsgBuff.append("Either origin or destination ticket is required, ");
		}
		
		if (StringUtils.isEmpty(entity.getTempTruck())) {
			errorMsgBuff.append("Truck, ");
		} else {
			criterias.clear();
			criterias.put("unit", Integer.parseInt(entity.getTempTruck()));
			Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
			if (truck == null) {
				errorMsgBuff.append("Truck, ");
			} else {
				/**
				 * Added: Bug Fix - Save the truck ID corresponding to the latest date range 
				 */
				System.out.println("Truck ID (Incoming) = " + truck.getId());
				setTruckForTripSheet(entity, genericDAO);
				System.out.println("Truck ID (After date check) = " + entity.getTruck().getId());
			}			
		}
		
		if (StringUtils.isEmpty(entity.getTempTrailer())) {
			errorMsgBuff.append("Trailer, ");
		} else {
			criterias.clear();
			criterias.put("unit", Integer.parseInt(entity.getTempTrailer()));
			Vehicle trailer = genericDAO.getByCriteria(Vehicle.class, criterias);
			if (trailer == null) {
				errorMsgBuff.append("Trailer, ");
			}
			else{
				entity.setTrailer(trailer);
			}
		}
		
		if (entity.getDriver() == null) {
			errorMsgBuff.append("Driver, ");
		}
		
		if (entity.getOrigin() == null) {
			errorMsgBuff.append("Origin, ");
		}
		
		if (entity.getDestination() == null) {
			errorMsgBuff.append("Destination, ");
		}		
		
		if (entity.getOrigin() != null &&  entity.getOriginTicket() !=null) {
			String originTSQuery = "select obj from TripSheet obj where obj.origin.id="+entity.getOrigin().getId()
					+ " and obj.originTicket="+entity.getOriginTicket();
			if (entity.getId() != null) {
				originTSQuery = originTSQuery + " and obj.id != " + entity.getId();
			}
			List<TripSheet> tripsheets = genericDAO.executeSimpleQuery(originTSQuery);
			String originTicketQuery = "select obj from Ticket obj where obj.origin.id="+entity.getOrigin().getId()
					+ " and obj.originTicket="+entity.getOriginTicket();
			List<Ticket> tickets = genericDAO.executeSimpleQuery(originTicketQuery);
			
			if ((tripsheets != null && tripsheets.size() > 0) || (tickets != null && tickets.size() > 0)) {		
				errorMsgBuff.append("Duplicate Origin Ticket, ");		
			}		
		}		
		
		if (entity.getDestination() != null && entity.getDestinationTicket() != null) {
			String destinationTSQuery = "select obj from TripSheet obj where obj.destination.id="+entity.getDestination().getId()
					+ " and obj.destinationTicket="+entity.getDestinationTicket();
			if (entity.getId() != null) {
				destinationTSQuery = destinationTSQuery + " and obj.id != " + entity.getId();
			}
			List<TripSheet> tripsheets1 = genericDAO.executeSimpleQuery(destinationTSQuery);
			
			String destinationTicketQuery = "select obj from Ticket obj where obj.destination.id="+entity.getDestination().getId()
					+ " and obj.destinationTicket=" + entity.getDestinationTicket();
			List<Ticket> tickets = genericDAO.executeSimpleQuery(destinationTicketQuery);
			if ((tripsheets1 != null && tripsheets1.size() > 0) || (tickets != null && tickets.size() > 0)) {		
				errorMsgBuff.append("Duplicate Destinantion Ticket, ");		
			}
		}	
		
		if (errorMsgBuff.length() != 0) {		
			return;
		}
		
		if (entity.getOriginTicket() == null || entity.getDestinationTicket() == null) {
			entity.setIncomplete("Yes");
		} else {
			entity.setIncomplete("No");
		}
		
		criterias.clear();
	  	criterias.put("id", entity.getDriver().getId());
	  	Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
		
		entity.setDriverCompany(driver.getCompany());
   	entity.setTerminal(driver.getTerminal());
   	entity.setTerminalName(driver.getTerminal().getName());
   	entity.setCompanyName(driver.getCompany().getName());
		  
		Location location = genericDAO.getById(Location.class, entity.getDestination().getId());
		
		int payUsing = 0;
		
		String initRateQuery = "select obj from DriverPayRate obj where obj.transferStation='"
			+ entity.getOrigin().getId() + "' and obj.landfill='"						
			+ entity.getDestination().getId() 
			+ "' and obj.company='"+driver.getCompany().getId()
			+ "' and obj.terminal='"+driver.getTerminal().getId()
			+ "' order by obj.validTo";
		List<DriverPayRate>	initfs = genericDAO.executeSimpleQuery(initRateQuery);
	    if (initfs != null && initfs.size() > 0) {
	    	payUsing = initfs.get(0).getRateUsing();
	    } else {
	    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
	    		String initRateQuery1 = "select obj from DriverPayRate obj where obj.transferStation='"
					+ entity.getOrigin().getId() + "' and obj.landfill='"						
					+ 91 
					+ "' and obj.company='"+driver.getCompany().getId()
					+ "' and obj.terminal='"+driver.getTerminal().getId()
					+ "' order by obj.validTo";
	    		List<DriverPayRate>	initfs2 = genericDAO.executeSimpleQuery(initRateQuery1);
	    		if (initfs2 != null && initfs2.size() > 0) {
	    			payUsing = initfs2.get(0).getRateUsing();
	    		}				    		
	      }			       
	  }
		
		StringBuffer rateQuery = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
				+ entity.getOrigin().getId() + "' and obj.landfill='"
				+ entity.getDestination().getId() 
				+ "' and obj.company='"+driver.getCompany().getId()
				+ "' and obj.terminal='"+driver.getTerminal().getId()+"'");
		if (payUsing==1) {
			rateQuery.append(" and obj.validFrom <='"+dateFormat.format(entity.getLoadDate())
			+"' and obj.validTo >='"+dateFormat.format(entity.getLoadDate())+"'");
		} else {
			rateQuery.append(" and obj.validFrom <='"+dateFormat.format(entity.getUnloadDate())
			+"' and obj.validTo >='"+dateFormat.format(entity.getUnloadDate())+"'");
		}
		
		List<DriverPayRate>	fs = new ArrayList<DriverPayRate>();
		List<DriverPayRate>	fs1 = genericDAO.executeSimpleQuery(rateQuery.toString());
		if (fs1 != null && fs1.size() > 0) {
	    	fs = fs1;
	   } else {
	    	if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
				StringBuffer rateQuery1 = new StringBuffer("select obj from DriverPayRate obj where obj.transferStation='"
					+ entity.getOrigin().getId() + "' and obj.landfill='"
					+ 91 
					+ "' and obj.company='"+driver.getCompany().getId()
					+ "' and obj.terminal='"+driver.getTerminal().getId()+"'");
				if (payUsing==1) {	
					rateQuery1.append(" and obj.validFrom <='"+dateFormat.format(entity.getLoadDate())
					+"' and obj.validTo >='"+dateFormat.format(entity.getLoadDate())+"'");
				} else {
					rateQuery1.append(" and obj.validFrom <='"+dateFormat.format(entity.getUnloadDate())
					+"' and obj.validTo >='"+dateFormat.format(entity.getUnloadDate())+"'");
				}
				
				List<DriverPayRate>	fs2 = genericDAO.executeSimpleQuery(rateQuery1.toString());
				if (fs2 != null && fs2.size() > 0) {
					fs = fs2;
				}
	    	}
	   }
		
		Double payrate = 0.0;
		if (fs != null && fs.size() > 0) {	
			boolean wbDrivers = false;
			if (driver.getCompany().getId() == 4l && driver.getTerminal().getId() == 93l) {
				if (driver.getDateProbationEnd()!=null){
					if (new LocalDate(driver.getDateProbationEnd()).isAfter(new LocalDate(entity.getBatchDate())) || new LocalDate(driver.getDateProbationEnd()).isEqual(new LocalDate(entity.getBatchDate()))){
						wbDrivers = true;
					}
				}
			}
			
			if (wbDrivers) {
				payrate = fs.get(0).getProbationRate() ;						
			} else {
				if (driver.getShift().equals("1")) {								
					payrate = fs.get(0).getPayRate();
				} else {									
					payrate = fs.get(0).getNightPayRate();
				}
			}					

			LocalDate unloadDate = new LocalDate(entity.getUnloadDate());	
			if (unloadDate.getDayOfWeek() == DateTimeConstants.SUNDAY) {
				payrate = payrate * fs.get(0).getSundayRateFactor();
			}
		}
		
	  	entity.setPayRate(payrate);
	  	
	  	if (entity.getId() == null) {
	  		entity.setEnteredBy("system");   	  		
   	}
		
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(user.getId());
		
		genericDAO.saveOrUpdate(entity);
		
		String mobileEntryTableUpdateQuery = "update DriverMobileEntry d set "
				+ "d.tripsheet_flag='Y' "
				+ ", d.enteredBy='" + user.getFullName() + "'"
				+ ", d.enteredById=" + user.getId()
				+ ", d.enteredByType='OPS'"
				+ " where "
				+ "d.employeeName in ('"+driver.getFullName()+"') and d.entryDate='"+mysqldf.format(entity.getLoadDate())+"'";
		genericDAO.executeSimpleUpdateQuery(mobileEntryTableUpdateQuery.toString());
	}
	
	private static void setTruckForTripSheet(TripSheet entity, GenericDAO genericDAO) {
		String vehicleQuery = StringUtils.EMPTY;
		if (entity.getLoadDate() != null) {
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())
				+" and obj.validFrom<='"+dateFormat.format(entity.getLoadDate())+"' and obj.validTo>='"+dateFormat.format(entity.getLoadDate())+"'";
		} else if(entity.getUnloadDate() != null) {
			vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="+Integer.parseInt(entity.getTempTruck())
				+" and obj.validFrom<='"+entity.getUnloadDate()+"' and obj.validTo>='"+entity.getUnloadDate()+"'";
		}
		
		System.out.println("******* The vehicle query for driver tripsheet is "+vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList != null && vehicleList.size() > 0) {
			entity.setTruck(vehicleList.get(0));
		}
	}
	
	public static void calculateNetAndTons(Ticket ticket) {
		DecimalFormat df = new DecimalFormat("#0.00");
		
		Double transferGross = ticket.getTransferGross();
		Double transferTare = ticket.getTransferTare();
		Double transferNet = transferGross - transferTare;
		Double transferTons = transferNet / 2000.0;
		
		String transferNetStr = df.format(transferNet);
		String transferTonsStr = df.format(transferTons);
		
		ticket.setTransferNet(Double.parseDouble(transferNetStr));
		ticket.setTransferTons(Double.parseDouble(transferTonsStr));
		
		Double landfillGross = ticket.getLandfillGross();
		Double landfillTare = ticket.getLandfillTare();;
		Double landfillNet = landfillGross - landfillTare;
		Double landfillTons = landfillNet / 2000.0;
		
		String landfillNetStr = df.format(landfillNet);
		String landfillTonsStr = df.format(landfillTons);
		
		ticket.setLandfillNet(Double.parseDouble(landfillNetStr));
		ticket.setLandfillTons(Double.parseDouble(landfillTonsStr));
	}
	
	public static void validateDates(Date loadDate, Date unloadDate, Date batchDate, StringBuffer errorMsgBuff) {
	    try {
	   	 DateMidnight batchDateMidnight = new DateMidnight(batchDate);
	   	 DateMidnight loadDateMidnight = new DateMidnight(loadDate);
	   	 DateMidnight unloadDateMidnight = new DateMidnight(unloadDate);	
	    
		    int daysBtwnLb = Days.daysBetween(loadDateMidnight, batchDateMidnight).getDays();
		    int daysBtwnULb = Days.daysBetween(unloadDateMidnight, batchDateMidnight).getDays();
		    System.out.println("****** The date between are "+daysBtwnLb+" and "+daysBtwnULb);
		    
		    if (daysBtwnLb < 0 && daysBtwnULb < 0) {
		   	 errorMsgBuff.append("Load and Unload Dates are Past the Billbatch Date, ");
		    } else if (daysBtwnLb < 0) {
		   	 errorMsgBuff.append("Load Date is Past the Billbatch Date, ");
		    } else if (daysBtwnULb < 0) {		    
		   	 errorMsgBuff.append("UnLoad Date is Past the Billbatch Date, ");
		    }
	    } catch (Exception e) {
	   	 errorMsgBuff.append("Exception while validationg dates, ");
		}			
	}
	
	private static String processAjaxRequest(HttpServletRequest request,
			String action, Model model, GenericDAO genericDAO) {
		if ("weekOfDate".equalsIgnoreCase(action)) {	
			try {
				List<String> list = new ArrayList<String>();
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			
			String unloadDate=dateFormat1.format(sdf.parse(request.getParameter("unloadDate")));			
			LocalDate now = new LocalDate(unloadDate);			
			LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
			list.add(sdf.format(sunday.toDate()));
			Gson gson = new Gson();
			return gson.toJson(list);
			} catch (ParseException e) {				
				return "";
			}			
		}
		
		if("getRemainingTicketData".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String landfill=request.getParameter("landfil");
		    String landticket=request.getParameter("originTcket");
		    List tickList = new ArrayList();
		    String origin = "select obj from TripSheet obj where obj.origin.id="+landfill+" and obj.originTicket="+landticket;
		    List<TripSheet> tripsheets = genericDAO.executeSimpleQuery(origin);
			if (tripsheets!=null && tripsheets.size()>0){	
				for(TripSheet tripSheetObj:tripsheets){
					tickList.add(tripSheetObj.getDriverCompany().getId());
					tickList.add(tripSheetObj.getDriver().getId());
					tickList.add(tripSheetObj.getTerminal().getId());
					//tickList.add(tripSheetObj.getTruck().getId());
					tickList.add(tripSheetObj.getTruck().getUnitNum());
					tickList.add(tripSheetObj.getTrailer().getId());
					tickList.add(dateFormat.format(tripSheetObj.getBatchDate()));
					tickList.add(tripSheetObj.getOrigin().getId());
					tickList.add(tripSheetObj.getOriginTicket());
					tickList.add(dateFormat.format(tripSheetObj.getLoadDate()));
					tickList.add(tripSheetObj.getDestination().getId());
					tickList.add(tripSheetObj.getDestinationTicket());
					tickList.add(dateFormat.format(tripSheetObj.getUnloadDate()));
					System.out.println("***** Enetered here");
				}
				Gson gson = new Gson();
				return gson.toJson(tickList);
			}
			else{
				return "";
			}
		}
		
		if ("checkOriginTicket".equalsIgnoreCase(action)) {
			String mssg="";
			
			String landfill=request.getParameter("landfil");
		    String landticket=request.getParameter("originTcket");
		    String tickId=request.getParameter("ticId");
		    
            if(StringUtils.isEmpty(tickId)){
            	tickId=null;
		    }
		    
		    
		    String origin = "select obj from Ticket obj where obj.origin.id="+landfill+" and obj.originTicket="+landticket+" and obj.id != "+tickId;
			List<Ticket> tickets = genericDAO.executeSimpleQuery(origin);
			if (tickets!=null && tickets.size()>0){		
				mssg="Duplicate Origin Ticket";	
			}
		    
			 return mssg;
			
		}
		
		if ("checkDestinationTicket".equalsIgnoreCase(action)) {
			String mssg="";
			
			String transfersttn=request.getParameter("transferstation");
		    String transferticket=request.getParameter("destinationTcket");
		    String tickId=request.getParameter("ticId");
			
		    if(StringUtils.isEmpty(tickId)){
            	tickId=null;
		    }
		    
		    
		    String destination = "select obj from Ticket obj where obj.destination.id="+transfersttn+" and obj.destinationTicket="+transferticket+" and obj.id != "+tickId;
			List<Ticket> tickets1 = genericDAO.executeSimpleQuery(destination);
			if (tickets1!=null && tickets1.size()>0){		
				mssg="Duplicate Destination Ticket";			
			}
			 return mssg;
		}
		
		if ("checkVehicle".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			 boolean sendError=true;
			String truckId=request.getParameter("truckId");
		    String loaddte=request.getParameter("loaddte");
		    String unloaddte=request.getParameter("unloaddte");
			
		    try{		   
		    Date loaddate=dateFormat.parse(loaddte);
		    Date unloaddate=dateFormat.parse(unloaddte);		    		
			Vehicle vehob = genericDAO.getById(Vehicle.class,Long.parseLong(truckId));
			
			String vehiclequery="select obj from Vehicle obj where obj.type=1 and obj.unit in ("
				+vehob.getUnit()
				+") order by obj.validFrom desc";
			
			List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
			
			if(vehicleLists!=null && vehicleLists.size()>0){				
			for(Vehicle vehicleObj : vehicleLists) {	
				if(vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
				if ((loaddate.getTime()>= vehicleObj.getValidFrom().getTime() && loaddate.getTime()<= vehicleObj.getValidTo().getTime()) && (unloaddate.getTime()>= vehicleObj.getValidFrom().getTime() && unloaddate.getTime()<= vehicleObj.getValidTo().getTime())) {					
					sendError=false;
				}
			}
			}
			}		    
		    }
		    catch (Exception e) {
				// TODO: handle exception
			}	
		    if(sendError){
		    	return "No Valid Vehicle Entry Found for Selected Truck.";
		    } 
		}
		
		if ("checkTrailer".equalsIgnoreCase(action)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			 boolean sendError=true;	
			String trailerId=request.getParameter("trailerId");
		    String loaddte=request.getParameter("loaddte");
		    String unloaddte=request.getParameter("unloaddte");
			
		    try{		   
		    Date loaddate=dateFormat.parse(loaddte);
		    Date unloaddate=dateFormat.parse(unloaddte);
		    
		    Vehicle vehob=genericDAO.getById(Vehicle.class,Long.parseLong(trailerId));
			String vehiclequery="select obj from Vehicle obj where obj.type=2 and obj.unit in ("
				+vehob.getUnit()
				+") order by obj.validFrom desc";
			
			List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
			
			if(vehicleLists!=null && vehicleLists.size()>0){				
			for(Vehicle vehicleObj : vehicleLists) {					
				if(vehicleObj.getValidFrom()!=null && vehicleObj.getValidTo()!=null){
				if ((loaddate.getTime()>= vehicleObj.getValidFrom().getTime() && loaddate.getTime()<= vehicleObj.getValidTo().getTime()) && (unloaddate.getTime()>= vehicleObj.getValidFrom().getTime() && unloaddate.getTime()<= vehicleObj.getValidTo().getTime())) {
					
					sendError=false;
				}	
				}
			}
			}		    
		    }
		    catch (Exception e) {
				// TODO: handle exception
			}
		    
		    if(sendError){
		    	return "No Valid Vehicle Entry Found for Selected Trailer.";
		    }    
		    
		}
		
		return "";
	}
}
