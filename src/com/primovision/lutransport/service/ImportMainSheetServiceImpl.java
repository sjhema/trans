package com.primovision.lutransport.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.ErrorData;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelDiscount;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehiclePermit;
import com.primovision.lutransport.model.VehicleTollTag;
import com.primovision.lutransport.model.driver.DriverFuelLog;
import com.primovision.lutransport.model.driver.Odometer;
import com.primovision.lutransport.model.hr.SetupData;

@Transactional(readOnly = true)
public class ImportMainSheetServiceImpl implements ImportMainSheetService {

	public static SimpleDateFormat drvdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

	private static Logger log = Logger.getLogger(ImportMainSheetServiceImpl.class.getName());
	private static SimpleDateFormat sdf = new SimpleDateFormat("mm-DD-yy");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
	@Autowired
	protected GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	@Autowired
	private ReportService reportService;

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getAsIntegerString(String value) {
		return value.replaceAll("\\.\\d*$", "");
	}

	private Date getAsDate(String validCellValue) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.parse(validCellValue);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importeztollMainSheet(InputStream is, Boolean override) throws Exception {
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance //XSSFWorkbook
		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<EzToll> eztolls = new ArrayList<EzToll>();
		// List<String> emptydatalist=new ArrayList<String>();
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();
			// FileWriter writer = new FileWriter("e:/errordata.txt");
			wb = new HSSFWorkbook(fs);
			int numOfSheets = wb.getNumberOfSheets();
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			EzToll eztoll = null;

			Iterator rows = sheet.rowIterator();
			StringBuffer lineError;
			while (rows.hasNext()) {
				boolean error = false;
				buffer = new StringBuffer();
				int cellCount = 0;
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				lineError = new StringBuffer("");
				try {
					eztoll = new EzToll();
					// FOR Toll COMPANY
					String tollcompany = ((String) getCellValue(row.getCell(0)));
					try {
						criterias.clear();
						criterias.put("name", tollcompany);
						TollCompany tollcompanyName = genericDAO.getByCriteria(TollCompany.class, criterias);
						if (tollcompanyName == null)
							throw new Exception("Invalid Toll Company Name");
						eztoll.setToolcompany(tollcompanyName);
					} catch (Exception ex) {
						// System.out.println("\n\n Error in Driver first
						// name========>"+ex);
						error = true;
						lineError.append("Toll Company Name,");
						log.warn(ex.getMessage());
					}
					// FOR COMPANY
					String company = ((String) getCellValue(row.getCell(1)));
					try {
						criterias.clear();
						criterias.put("type", 3);
						criterias.put("name", company);
						Location companyName = genericDAO.getByCriteria(Location.class, criterias);
						// System.out.println("\ncompanyName====>"+companyName+"\n");

						if (companyName == null)
							throw new Exception("Invalid Company Name");
						eztoll.setCompany(companyName);
					} catch (Exception ex) {
						// System.out.println("\n\n Error in Driver first
						// name========>"+ex);
						error = true;
						lineError.append("Company,");
						log.warn(ex.getMessage());
					}
					// FOR TERMINAL
					/*
					 * try { criterias.clear(); String name = (String)
					 * getCellValue(row.getCell(2));
					 * //System.out.println("\nTerminal====>"+name+"\n"); if
					 * (StringUtils.isEmpty(name)) throw new
					 * Exception("Invalid terminal"); else {
					 * criterias.put("name", name); criterias.put("type", 4); }
					 * Location location =
					 * genericDAO.getByCriteria(Location.class, criterias); if
					 * (location == null) throw new
					 * Exception("no such Terminal"); else
					 * eztoll.setTerminal(location); } catch (Exception ex) {
					 * error = true; lineError.append("Terminal,");
					 * log.warn(ex.getMessage()); }
					 */

					String plateNum = null;

					if (getCellValue(row.getCell(4)) == null) {
						// do nothing
					} else if (getCellValue(row.getCell(4)).equals("")) {
						// do nothing
					} else {
						plateNum = getCellValue(row.getCell(4)).toString();
					}

					String tollNum = null;

					if (getCellValue(row.getCell(3)) == null) {
						// do nothing
					} else if (getCellValue(row.getCell(3)).equals("")) {
						// do nothing
					} else {
						tollNum = getCellValue(row.getCell(3)).toString();
					}

					// if both toll number and plate number is empty
					if (tollNum == null && plateNum == null) {
						error = true;
						lineError.append("Either tolltag number or plate number is required,");
						log.warn("Either Toll tag number or Plate number is required ");
					} else {
						// for toll number
						if (tollNum != null) {
							try {
								String transactiondate = null;
								if (validDate(getCellValue(row.getCell(6)))) {
									transactiondate = dateFormat
											.format(((Date) getCellValue(row.getCell(6))).getTime());
								}
								StringBuffer query = new StringBuffer(
										"select obj from VehicleTollTag obj where obj.tollTagNumber='"
												+ (String) getCellValue(row.getCell(3)) + "'");
								// String query="select obj from VehicleTollTag
								// obj where obj.tollTagNumber='"
								if (eztoll.getToolcompany() != null) {
									query.append(" and obj.tollCompany='" + eztoll.getToolcompany().getId() + "'");
								}

								query.append(" and obj.validFrom <='" + transactiondate + "' and obj.validTo >= '"
										+ transactiondate + "'");

								List<VehicleTollTag> vehicletolltags = genericDAO.executeSimpleQuery(query.toString());

								if (vehicletolltags.isEmpty() && vehicletolltags.size() == 0)
									throw new Exception("no such Toll tag Number");
								else {
									String vehquery = "Select obj from Vehicle obj where obj.unit="
											+ vehicletolltags.get(0).getVehicle().getUnit() + " and obj.validFrom <='"
											+ transactiondate + "' and obj.validTo >= '" + transactiondate + "'";
									System.out.println("******************** the vehicle query is " + vehquery);
									List<Vehicle> vehicle = genericDAO.executeSimpleQuery(vehquery.toString());
									if (vehicle.isEmpty() && vehicle.size() == 0) {
										throw new Exception("no such Toll tag Number");
									} else {
										eztoll.setUnit(vehicle.get(0));
										eztoll.setTollTagNumber(vehicletolltags.get(0));
										String drv_name = (String) getCellValue(row.getCell(5));
										if (!(StringUtils.isEmpty(drv_name))) {
											criterias.clear();
											criterias.put("fullName", getCellValue(row.getCell(5)));
											/*
											 * List<Driver> driver
											 * =genericDAO.findByCriteria(Driver
											 * .class,criterias);
											 */
											Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
											if (driver == null) {
												error = true;
												lineError.append("Invalid Driver Name, ");
											} else {
												eztoll.setDriver(driver);
												eztoll.setTerminal(driver.getTerminal());
											}
										} else {
											String drivequery = "select obj from Ticket obj where   obj.loadDate<='"
													+ transactiondate + "' and obj.unloadDate>='" + transactiondate
													+ "' and obj.vehicle=" + vehicle.get(0).getId();

											System.out.println(" my query is " + drivequery);
											List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);
											if (!tickets.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> driverid = new ArrayList<String>();
												for (Ticket ticket : tickets) {
													boolean d = driverid.contains(ticket.getDriver().getId() + "");
													driverid.add(ticket.getDriver().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														if (override == false) {
															error = true;
															lineError.append("More than one Driver, ");
															tic = false;
														} else {
															tic = false;

															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}

														}
													}
												}
												if (tic) {
													eztoll.setDriver(tickets.get(0).getDriver());
													Driver driver = genericDAO.getById(Driver.class,
															tickets.get(0).getDriver().getId());
													eztoll.setTerminal(driver.getTerminal());
												}
											} else {
												String driverFuelLogQuery = "select obj from DriverFuelLog obj where   obj.transactionDate='"
														+ transactiondate + "' and obj.truck=" + vehicle.get(0).getId();

												System.out.println(" my query is " + driverFuelLogQuery);
												List<DriverFuelLog> driverFuelLog = genericDAO
														.executeSimpleQuery(driverFuelLogQuery);

												if (!driverFuelLog.isEmpty()) {
													boolean tic = true;
													boolean first = true;
													List<String> driverid = new ArrayList<String>();
													for (DriverFuelLog drvFuelLog : driverFuelLog) {

														boolean d = driverid
																.contains(drvFuelLog.getDriver().getId() + "");
														driverid.add(drvFuelLog.getDriver().getId() + "");
														if (first) {
															first = false;
															continue;
														}
														if (!d) {
															if (override == false) {
																error = true;
																lineError.append("More than one Driver, ");
																tic = false;
															} else {
																tic = false;

																try {
																	criterias.clear();
																	String name = (String) getCellValue(row.getCell(2));
																	// System.out.println("\nTerminal====>"+name+"\n");
																	if (StringUtils.isEmpty(name))
																		throw new Exception("Invalid terminal");
																	else {
																		criterias.put("name", name);
																		criterias.put("type", 4);
																	}
																	Location location = genericDAO
																			.getByCriteria(Location.class, criterias);
																	if (location == null)
																		throw new Exception("no such Terminal");
																	else
																		eztoll.setTerminal(location);
																} catch (Exception ex) {
																	error = true;
																	lineError.append("Terminal,");
																	log.warn(ex.getMessage());
																}

															}
														}

													}
													if (tic) {
														eztoll.setDriver(driverFuelLog.get(0).getDriver());
														Driver driver = genericDAO.getById(Driver.class,
																driverFuelLog.get(0).getDriver().getId());
														eztoll.setTerminal(driver.getTerminal());
													}
												} else {

													String driverOdometerQuery = "select obj from Odometer obj where obj.recordDate='"
															+ transactiondate + "' and obj.truck="
															+ vehicle.get(0).getId();

													System.out.println(" odometer query is " + driverOdometerQuery);
													List<Odometer> odometer = genericDAO
															.executeSimpleQuery(driverOdometerQuery);

													if (!odometer.isEmpty()) {

														boolean tic = true;
														boolean first = true;
														List<String> driverid = new ArrayList<String>();
														for (Odometer odometerObj : odometer) {

															boolean d = driverid
																	.contains(odometerObj.getDriver().getId() + "");
															driverid.add(odometerObj.getDriver().getId() + "");
															if (first) {
																first = false;
																continue;
															}
															if (!d) {
																if (override == false) {
																	error = true;
																	lineError.append("More than one Driver, ");
																	tic = false;
																} else {
																	tic = false;

																	try {
																		criterias.clear();
																		String name = (String) getCellValue(
																				row.getCell(2));
																		// System.out.println("\nTerminal====>"+name+"\n");
																		if (StringUtils.isEmpty(name))
																			throw new Exception("Invalid terminal");
																		else {
																			criterias.put("name", name);
																			criterias.put("type", 4);
																		}
																		Location location = genericDAO.getByCriteria(
																				Location.class, criterias);
																		if (location == null)
																			throw new Exception("no such Terminal");
																		else
																			eztoll.setTerminal(location);
																	} catch (Exception ex) {
																		error = true;
																		lineError.append("Terminal,");
																		log.warn(ex.getMessage());
																	}

																}
															}

														}
														if (tic) {
															eztoll.setDriver(odometer.get(0).getDriver());
															Driver driver = genericDAO.getById(Driver.class,
																	odometer.get(0).getDriver().getId());
															eztoll.setTerminal(driver.getTerminal());
														}

													} else {
														if (override == false) {
															error = true;
															lineError.append(
																	"No matching  Ticket, Fuel Log,  Odometer entry, ");
														} else {
															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}
														}
													}
												}
											}
										}

									}
								}

							} catch (Exception ex) {
								error = true;
								lineError.append("Invalid Toll Tag Number, ");
								log.warn(ex.getMessage());
							}
						}

						// FOR PLATE#
						if (plateNum != null) {
							try {
								criterias.clear();
								criterias.put("plate", (String) getCellValue(row.getCell(4)));
								Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
								if (vehicle == null)
									throw new Exception("no such Plate Number");
								else {

									if (tollNum != null) {
										String transactiondate = null;

										if (validDate(getCellValue(row.getCell(6)))) {
											transactiondate = dateFormat
													.format(((Date) getCellValue(row.getCell(6))).getTime());
											System.out.println("\n****--****\n");
										}

										StringBuffer query = new StringBuffer(
												"select obj from VehicleTollTag obj where obj.tollTagNumber='"
														+ (String) getCellValue(row.getCell(3)) + "'");
										if (eztoll.getToolcompany() != null) {
											query.append(
													" and obj.tollCompany='" + eztoll.getToolcompany().getId() + "'");
										}
										query.append(" and obj.vehicle='" + vehicle.getId() + "' and obj.validFrom <='"
												+ transactiondate + "' and obj.validTo >= '" + transactiondate + "'");

										/*
										 * String query=
										 * "select obj from VehicleTollTag obj where obj.tollTagNumber='"
										 * +(String)getCellValue(row.getCell(3))
										 * +"' and obj.vehicle='"
										 * +vehicle.getId()+
										 * "' and obj.validFrom <='" +
										 * transactiondate+
										 * "' and obj.validTo >= '"
										 * +transactiondate+"'";
										 */
										System.out.println("******* query  ======>" + query);
										try {
											List<VehicleTollTag> vehicletolltags = genericDAO
													.executeSimpleQuery(query.toString());
											if (vehicletolltags.isEmpty() && vehicletolltags.size() == 0)
												throw new Exception("Invalid Plate Number");
											else {
												/*
												 * Code to get the active plate
												 * numbers
												 */
												List<Vehicle> vehicleList = genericDAO
														.executeSimpleQuery("select o from Vehicle o where o.unit="
																+ vehicletolltags.get(0).getUnit()
																+ " and o.validFrom<=SYSDATE() and o.validTo>=SYSDATE() ");
												if (vehicleList.isEmpty() && vehicleList.size() == 0)
													throw new Exception("Invalid Plate Number");
												else
													eztoll.setPlateNumber(vehicleList.get(0));
											}
										} catch (Exception ex) {
											System.out.println("\n*******\n");
										}
									} else {

										String transactiondate1 = null;
										if (validDate(getCellValue(row.getCell(6)))) {
											transactiondate1 = dateFormat
													.format(((Date) getCellValue(row.getCell(6))).getTime());
											System.out.println("\n****--****\n");
										}

										StringBuffer query = new StringBuffer(
												"select obj from VehicleTollTag obj where ");
										query.append("obj.vehicle='" + vehicle.getId() + "' and obj.validFrom <='"
												+ transactiondate1 + "' and obj.validTo >= '" + transactiondate1 + "'");
										if (eztoll.getToolcompany() != null) {
											query.append(
													" and obj.tollCompany='" + eztoll.getToolcompany().getId() + "'");
										}

										System.out.println("******* query  ======>" + query);
										try {
											List<VehicleTollTag> vehicletolltags = genericDAO
													.executeSimpleQuery(query.toString());
											if (vehicletolltags.isEmpty() && vehicletolltags.size() == 0) {
												// throw new Exception("Invalid
												// Plate Number");
											} else {
												eztoll.setTollTagNumber(vehicletolltags.get(0));
											}
										} catch (Exception ex) {
											System.out.println("\n*******\n");
										}

										String drv_name = (String) getCellValue(row.getCell(5));
										if (!(StringUtils.isEmpty(drv_name))) {
											criterias.clear();
											criterias.put("fullName", getCellValue(row.getCell(5)));
											/*
											 * List<Driver> driver
											 * =genericDAO.findByCriteria(Driver
											 * .class,criterias);
											 */
											Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
											if (driver == null) {
												error = true;
												lineError.append("Invalid Driver Name, ");
											} else {
												eztoll.setDriver(driver);
												eztoll.setTerminal(driver.getTerminal());
											}
										} else {

											String transactiondate = null;
											if (validDate(getCellValue(row.getCell(6)))) {
												transactiondate = dateFormat
														.format(((Date) getCellValue(row.getCell(6))).getTime());
											}

											String drivequery = "select obj from Ticket obj where   obj.loadDate<='"
													+ transactiondate + "' and obj.unloadDate>='" + transactiondate
													+ "' and obj.vehicle=" + vehicle.getId();

											System.out.println(" my query is " + drivequery);
											List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);
											if (!tickets.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> driverid = new ArrayList<String>();
												for (Ticket ticket : tickets) {
													boolean d = driverid.contains(ticket.getDriver().getId() + "");
													driverid.add(ticket.getDriver().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														if (override == false) {
															error = true;
															lineError.append("More than one Driver, ");
															tic = false;
														} else {
															tic = false;

															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}

														}
													}
												}
												if (tic) {
													eztoll.setDriver(tickets.get(0).getDriver());
													Driver driver = genericDAO.getById(Driver.class,
															tickets.get(0).getDriver().getId());
													eztoll.setTerminal(driver.getTerminal());
												}
											} else {
												String driverFuelLogQuery = "select obj from DriverFuelLog obj where   obj.transactionDate='"
														+ transactiondate + "' and obj.truck=" + vehicle.getId();

												System.out.println(" my query is " + driverFuelLogQuery);
												List<DriverFuelLog> driverFuelLog = genericDAO
														.executeSimpleQuery(driverFuelLogQuery);

												if (!driverFuelLog.isEmpty()) {
													boolean tic = true;
													boolean first = true;
													List<String> driverid = new ArrayList<String>();
													for (DriverFuelLog drvFuelLog : driverFuelLog) {

														boolean d = driverid
																.contains(drvFuelLog.getDriver().getId() + "");
														driverid.add(drvFuelLog.getDriver().getId() + "");
														if (first) {
															first = false;
															continue;
														}
														if (!d) {
															if (override == false) {
																error = true;
																lineError.append("More than one Driver, ");
																tic = false;
															} else {
																tic = false;

																try {
																	criterias.clear();
																	String name = (String) getCellValue(row.getCell(2));
																	// System.out.println("\nTerminal====>"+name+"\n");
																	if (StringUtils.isEmpty(name))
																		throw new Exception("Invalid terminal");
																	else {
																		criterias.put("name", name);
																		criterias.put("type", 4);
																	}
																	Location location = genericDAO
																			.getByCriteria(Location.class, criterias);
																	if (location == null)
																		throw new Exception("no such Terminal");
																	else
																		eztoll.setTerminal(location);
																} catch (Exception ex) {
																	error = true;
																	lineError.append("Terminal,");
																	log.warn(ex.getMessage());
																}

															}
														}

													}
													if (tic) {
														eztoll.setDriver(driverFuelLog.get(0).getDriver());
														Driver driver = genericDAO.getById(Driver.class,
																driverFuelLog.get(0).getDriver().getId());
														eztoll.setTerminal(driver.getTerminal());
													}
												} else {

													String driverOdometerQuery = "select obj from Odometer obj where obj.recordDate='"
															+ transactiondate + "' and obj.truck=" + vehicle.getId();

													System.out.println(" odometer query is " + driverOdometerQuery);
													List<Odometer> odometer = genericDAO
															.executeSimpleQuery(driverOdometerQuery);

													if (!odometer.isEmpty()) {

														boolean tic = true;
														boolean first = true;
														List<String> driverid = new ArrayList<String>();
														for (Odometer odometerObj : odometer) {

															boolean d = driverid
																	.contains(odometerObj.getDriver().getId() + "");
															driverid.add(odometerObj.getDriver().getId() + "");
															if (first) {
																first = false;
																continue;
															}
															if (!d) {
																if (override == false) {
																	error = true;
																	lineError.append("More than one Driver, ");
																	tic = false;
																} else {
																	tic = false;

																	try {
																		criterias.clear();
																		String name = (String) getCellValue(
																				row.getCell(2));
																		// System.out.println("\nTerminal====>"+name+"\n");
																		if (StringUtils.isEmpty(name))
																			throw new Exception("Invalid terminal");
																		else {
																			criterias.put("name", name);
																			criterias.put("type", 4);
																		}
																		Location location = genericDAO.getByCriteria(
																				Location.class, criterias);
																		if (location == null)
																			throw new Exception("no such Terminal");
																		else
																			eztoll.setTerminal(location);
																	} catch (Exception ex) {
																		error = true;
																		lineError.append("Terminal,");
																		log.warn(ex.getMessage());
																	}

																}
															}

														}
														if (tic) {
															eztoll.setDriver(odometer.get(0).getDriver());
															Driver driver = genericDAO.getById(Driver.class,
																	odometer.get(0).getDriver().getId());
															eztoll.setTerminal(driver.getTerminal());
														}

													} else {
														if (override == false) {
															error = true;
															lineError.append(
																	"No matching  Ticket, Fuel Log,  Odometer entry, ");
														} else {
															try {
																criterias.clear();
																String name = (String) getCellValue(row.getCell(2));
																// System.out.println("\nTerminal====>"+name+"\n");
																if (StringUtils.isEmpty(name))
																	throw new Exception("Invalid terminal");
																else {
																	criterias.put("name", name);
																	criterias.put("type", 4);
																}
																Location location = genericDAO
																		.getByCriteria(Location.class, criterias);
																if (location == null)
																	throw new Exception("no such Terminal");
																else
																	eztoll.setTerminal(location);
															} catch (Exception ex) {
																error = true;
																lineError.append("Terminal,");
																log.warn(ex.getMessage());
															}
														}
													}
												}
											}
										}

										eztoll.setPlateNumber(vehicle);
									}

								}
							} catch (Exception ex) {
								error = true;
								lineError.append("Invalid Plate Number, ");
								log.warn(ex.getMessage());
							}

						} else {
							if (eztoll.getTollTagNumber() != null) {
								VehicleTollTag vehicletoll = genericDAO.getById(VehicleTollTag.class,
										eztoll.getTollTagNumber().getId());

								/* Code to get the active plate numbers */
								List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(
										"select o from Vehicle o where o.unit=" + vehicletoll.getUnit()
												+ " and o.validFrom<=SYSDATE() and o.validTo>=SYSDATE() ");
								if (vehicleList.isEmpty() && vehicleList.size() == 0)
									throw new Exception("Invalid Plate Number");
								else
									eztoll.setPlateNumber(vehicleList.get(0));

								// eztoll.setPlateNumber(vehicletoll.getVehicle());
							}
						}

					}

					// FOR TRANSACTION DATE
					try {
						if (validDate(getCellValue(row.getCell(6))))
							eztoll.setTransactiondate((Date) getCellValue(row.getCell(6)));
						else {
							error = true;
							lineError.append("Transaction Date,");
						}
					} catch (Exception ex) {
						System.out.println("\nERROR IN TRANSACTION DATE\n");
						log.warn(ex.getMessage());

					}
					// FOR TRANSACTION TIME
					try {
						if (validDate(getCellValue(row.getCell(7)))) {
							eztoll.setTransactiontime(dateFormat2.format((Date) getCellValue(row.getCell(7))));
						} else {
							String trxTime1 = (String) getCellValue(row.getCell(7));

							if (!(StringUtils.isEmpty(trxTime1))) {
								if (trxTime1.length() == 5 || trxTime1.length() == 8 || trxTime1.length() == 7) {
									StringBuilder time = new StringBuilder(
											StringUtils.leftPad((String) getCellValue(row.getCell(7)), 4, '0'));
									// time.insert(2, ':');
									if (trxTime1.length() == 8) {
										eztoll.setTransactiontime(time.toString().substring(0, 5));
									} else if (trxTime1.length() == 7) {
										eztoll.setTransactiontime(time.toString().substring(0, 4));
									} else {
										eztoll.setTransactiontime(time.toString());
									}

								} else {
									// System.out.println("\ntrx time is not
									// valid\n");
									error = true;
									lineError.append("Transaction Time,");
								}
							} else {
								lineError.append("Transaction Time,");
							}

						}
					} catch (Exception e) {

					}
					/*
					 * if (validTime((String) getCellValue(row.getCell(6)))) {
					 * StringBuilder time = new
					 * StringBuilder(StringUtils.leftPad((String)getCellValue(
					 * row.getCell(6)),4,'0')); int
					 * hh=Integer.parseInt(time.substring(0,2)); int
					 * mm=Integer.parseInt(time.substring(2));
					 * 
					 * if(hh==24) { if(mm==0) { time.insert(2, ':');
					 * eztoll.setTransactiontime(time.toString()); //
					 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n");
					 * } else { error = true;
					 * lineError.append("transaction time,"); } } else {
					 * if(hh<24) { if(mm<=59) { time.insert(2, ':');
					 * eztoll.setTransactiontime(time.toString()); //
					 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n");
					 * } else { error = true;
					 * lineError.append("transaction time minut is > 59,"); } }
					 * else { error = true;
					 * lineError.append("transaction time hours is > 24,"); } }
					 * } else { error = true;
					 * lineError.append("transaction time more than 5 degits,");
					 * 
					 * }
					 */

					// FOR AGENCY
					try {
						eztoll.setAgency((String) getCellValue(row.getCell(8)));
					} catch (Exception ex) {
						error = true;
						lineError.append("Agency,");
						log.warn(ex.getMessage());
					}
					// FOR AMOUNTS
					String amount1 = row.getCell(9).toString();
					Double amount2 = getValidGallon(amount1);
					if (amount2 != null) {
						eztoll.setAmount(Math.abs(amount2));
					} else {
						lineError.append("Amount,");
						error = true;
					}
					// END OF CELL
					if (!error) {
						/*
						 * if (eztolls.contains(eztoll)) {
						 * lineError.append("Duplicate eztoll,"); error = true;
						 * } else { fuellogs.add(eztoll); }
						 */
						eztolls.add(eztoll);
					} else {
						errorcount++;
					}

				} // TRY INSIDE SHILE(LOOP)
				catch (Exception ex) {
					error = true;
					log.warn(ex);
				}
				if (lineError.length() > 0) {
					System.out.println("Error :" + lineError.toString());
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				System.out.println("Record No :" + count);
				count++;
			} // CLOSE while (rows.hasNext())
		} // FIRST TRY
		catch (Exception e) {
			log.warn("Error in import customer :" + e);
		}
		if (errorcount == 0) {
			for (EzToll etoll : eztolls) {
				Map criti = new HashMap();
				criti.clear();
				criti.put("id", etoll.getDriver().getId());
				Driver drvOBj = genericDAO.getByCriteria(Driver.class, criti);
				if (drvOBj != null)
					etoll.setDriverFullName(drvOBj.getFullName());

				criti.clear();
				criti.put("id", etoll.getUnit().getId());
				Vehicle vehObj = genericDAO.getByCriteria(Vehicle.class, criti);
				if (vehObj != null)
					etoll.setUnitNum(vehObj.getUnitNum());
				genericDAO.saveOrUpdate(etoll);
			}
		}
		return list;
	}

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	 * public List<String> importeztollMainSheet(InputStream is,Boolean
	 * override) throws Exception { // initializing the InputStream from a file
	 * using // POIFSFileSystem, before converting the result // into an
	 * HSSFWorkbook instance //XSSFWorkbook HSSFWorkbook wb = null; StringBuffer
	 * buffer = null; List<String> list = new ArrayList<String>(); List<EzToll>
	 * eztolls = new ArrayList<EzToll>(); // List<String> emptydatalist=new
	 * ArrayList<String>(); int count = 1; int errorcount = 0; try {
	 * POIFSFileSystem fs = new POIFSFileSystem(is); ErrorData edata = new
	 * ErrorData(); // FileWriter writer = new FileWriter("e:/errordata.txt");
	 * wb = new HSSFWorkbook(fs); int numOfSheets = wb.getNumberOfSheets(); Map
	 * criterias = new HashMap(); HSSFSheet sheet = wb.getSheetAt(0); HSSFRow
	 * row = null; HSSFCell cell = null; EzToll eztoll = null;
	 * 
	 * Iterator rows = sheet.rowIterator(); StringBuffer lineError; while
	 * (rows.hasNext()) { boolean error = false; buffer = new StringBuffer();
	 * int cellCount = 0; row = (HSSFRow) rows.next(); if (count == 1) {
	 * count++; continue; } lineError = new StringBuffer(""); try { eztoll = new
	 * EzToll(); //FOR Toll COMPANY String tollcompany = ((String)
	 * getCellValue(row.getCell(0))); try { criterias.clear();
	 * criterias.put("name", tollcompany); TollCompany tollcompanyName =
	 * genericDAO.getByCriteria(TollCompany.class,criterias); if
	 * (tollcompanyName == null) throw new
	 * Exception("Invalid Toll Company Name");
	 * eztoll.setToolcompany(tollcompanyName); } catch(Exception ex) {
	 * //System.out.println("\n\n Error in Driver first name========>"+ex);
	 * error = true; lineError.append("Toll Company Name,");
	 * log.warn(ex.getMessage()); } //FOR COMPANY String company = ((String)
	 * getCellValue(row.getCell(1))); try { criterias.clear();
	 * criterias.put("type", 3); criterias.put("name", company); Location
	 * companyName = genericDAO.getByCriteria(Location.class,criterias);
	 * //System.out.println("\ncompanyName====>"+companyName+"\n");
	 * 
	 * if (companyName == null) throw new Exception("Invalid Company Name");
	 * eztoll.setCompany(companyName); } catch(Exception ex) {
	 * //System.out.println("\n\n Error in Driver first name========>"+ex);
	 * error = true; lineError.append("Company,"); log.warn(ex.getMessage()); }
	 * //FOR TERMINAL try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); }
	 * 
	 * 
	 * 
	 * String plateNum=null;
	 * 
	 * if(getCellValue(row.getCell(4))==null){ //do nothing } else
	 * if(getCellValue(row.getCell(4)).equals("")){ //do nothing } else{
	 * plateNum=getCellValue(row.getCell(4)).toString(); }
	 * 
	 * 
	 * String tollNum=null;
	 * 
	 * if(getCellValue(row.getCell(3))==null){ //do nothing } else
	 * if(getCellValue(row.getCell(3)).equals("")){ //do nothing } else{
	 * tollNum=getCellValue(row.getCell(3)).toString(); }
	 * 
	 * 
	 * //if both toll number and plate number is empty if(tollNum==null &&
	 * plateNum==null) { error = true;
	 * lineError.append("Either tolltag number or plate number is required,");
	 * log.warn("Either Toll tag number or Plate number is required "); } else {
	 * //for toll number if(tollNum!=null ) { try { String transactiondate =
	 * null; if (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime()); } StringBuffer query=new
	 * StringBuffer(
	 * "select obj from VehicleTollTag obj where obj.tollTagNumber='"+(String)
	 * getCellValue(row.getCell(3))+"'") ; //String
	 * query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
	 * if(eztoll.getToolcompany()!=null){
	 * query.append(" and obj.tollCompany='"+eztoll.getToolcompany().getId()+"'"
	 * ); }
	 * 
	 * 
	 * query.append(" and obj.validFrom <='"+ transactiondate+
	 * "' and obj.validTo >= '"+transactiondate+"'");
	 * System.out.println("************ query =======>"+query.toString());
	 * 
	 * List<VehicleTollTag> vehicletolltags =
	 * genericDAO.executeSimpleQuery(query.toString());
	 * 
	 * System.out.println(" Size of vehicle is "+vehicletolltags.size()); if
	 * (vehicletolltags.isEmpty()&& vehicletolltags.size()==0) throw new
	 * Exception("no such Toll tag Number"); else { String
	 * vehquery="Select obj from Vehicle obj where obj.unit="+vehicletolltags.
	 * get(0).getVehicle().getUnit()+" and obj.validFrom <='"+ transactiondate+
	 * "' and obj.validTo >= '"+transactiondate+"'";
	 * System.out.println("******************** the vehicle query is "+vehquery)
	 * ; List<Vehicle> vehicle =
	 * genericDAO.executeSimpleQuery(vehquery.toString()); if(vehicle.isEmpty()
	 * && vehicle.size()==0){ throw new Exception("no such Toll tag Number"); }
	 * else{ eztoll.setUnit(vehicle.get(0));
	 * eztoll.setTollTagNumber(vehicletolltags.get(0)); String drv_name=(String)
	 * getCellValue(row.getCell(5)); if(!(StringUtils.isEmpty(drv_name))){
	 * criterias.clear();
	 * criterias.put("fullName",getCellValue(row.getCell(5))); List<Driver>
	 * driver =genericDAO.findByCriteria(Driver.class,criterias); Driver
	 * driver=genericDAO.getByCriteria(Driver.class, criterias);
	 * if(driver==null) { error = true;
	 * lineError.append("Invalid Driver Name, "); } else {
	 * eztoll.setDriver(driver); eztoll.setTerminal(driver.getTerminal()); } }
	 * else{ String
	 * drivequery="select obj from Ticket obj where   obj.loadDate<='"
	 * +transactiondate+"' and obj.unloadDate>='"+transactiondate+
	 * "' and obj.vehicle="+vehicle.get(0).getId();
	 * 
	 * System.out.println(" my query is "+drivequery); List<Ticket>
	 * tickets=genericDAO.executeSimpleQuery(drivequery);
	 * if(!tickets.isEmpty()){ boolean tic=true; boolean first=true;
	 * List<String> driverid=new ArrayList<String>(); for(Ticket
	 * ticket:tickets){ boolean
	 * d=driverid.contains(ticket.getDriver().getId()+"");
	 * driverid.add(ticket.getDriver().getId()+""); if(first){ first=false;
	 * continue; } if(!d){ if(override==false){ error = true;
	 * lineError.append("More than one Driver, "); tic=false; }else{ tic=false;
	 * 
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); }
	 * 
	 * 
	 * } } } if(tic){ eztoll.setDriver(tickets.get(0).getDriver()); Driver
	 * driver=genericDAO.getById(Driver.class,tickets.get(0).getDriver().getId()
	 * ); eztoll.setTerminal(driver.getTerminal()); } }else{
	 * if(override==false){ error = true;
	 * lineError.append("Ticket Data Does not match with Toll Tag, "); } else{
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); } } } }
	 * 
	 * } }
	 * 
	 * } catch (Exception ex) { error = true;
	 * lineError.append("Invalid Toll Tag Number, "); log.warn(ex.getMessage());
	 * } }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * //FOR PLATE# if(plateNum!=null) { try { criterias.clear();
	 * criterias.put("plate", (String) getCellValue(row.getCell(4))); Vehicle
	 * vehicle = genericDAO.getByCriteria(Vehicle.class, criterias); if (vehicle
	 * == null) throw new Exception("no such Plate Number"); else {
	 * 
	 * if(tollNum!=null) { String transactiondate = null;
	 * 
	 * if (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime());
	 * System.out.println("\n****--****\n"); }
	 * 
	 * 
	 * StringBuffer query=new StringBuffer(
	 * "select obj from VehicleTollTag obj where obj.tollTagNumber='"+(String)
	 * getCellValue(row.getCell(3))+"'"); if(eztoll.getToolcompany()!=null){
	 * query.append(" and obj.tollCompany='"+eztoll.getToolcompany().getId()+"'"
	 * ); } query.append(" and obj.vehicle='"+vehicle.getId()+
	 * "' and obj.validFrom <='"+ transactiondate+
	 * "' and obj.validTo >= '"+transactiondate+"'");
	 * 
	 * String
	 * query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
	 * +(String)getCellValue(row.getCell(3))+"' and obj.vehicle='"
	 * +vehicle.getId()+"' and obj.validFrom <='" + transactiondate+
	 * "' and obj.validTo >= '" +transactiondate+"'";
	 * System.out.println("******* query  ======>"+query); try{
	 * List<VehicleTollTag> vehicletolltags =
	 * genericDAO.executeSimpleQuery(query.toString()); if
	 * (vehicletolltags.isEmpty()&& vehicletolltags.size()==0) throw new
	 * Exception("Invalid Plate Number"); else {
	 * eztoll.setPlateNumber(vehicletolltags.get(0).getVehicle()); } }
	 * catch(Exception ex){ System.out.println("\n*******\n"); } } else {
	 * 
	 * String transactiondate1 = null; if
	 * (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate1=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime());
	 * System.out.println("\n****--****\n"); }
	 * 
	 * StringBuffer query=new
	 * StringBuffer("select obj from VehicleTollTag obj where ");
	 * query.append("obj.vehicle='"+vehicle.getId()+"' and obj.validFrom <='"+
	 * transactiondate1+ "' and obj.validTo >= '"+transactiondate1+"'");
	 * if(eztoll.getToolcompany()!=null){
	 * query.append(" and obj.tollCompany='"+eztoll.getToolcompany().getId()+"'"
	 * ); }
	 * 
	 * 
	 * System.out.println("******* query  ======>"+query); try{
	 * List<VehicleTollTag> vehicletolltags =
	 * genericDAO.executeSimpleQuery(query.toString()); if
	 * (vehicletolltags.isEmpty()&& vehicletolltags.size()==0){ //throw new
	 * Exception("Invalid Plate Number"); }else {
	 * eztoll.setTollTagNumber(vehicletolltags.get(0)); } } catch(Exception ex){
	 * System.out.println("\n*******\n"); }
	 * 
	 * 
	 * 
	 * String drv_name=(String) getCellValue(row.getCell(5));
	 * if(!(StringUtils.isEmpty(drv_name))){ criterias.clear();
	 * criterias.put("fullName",getCellValue(row.getCell(5))); List<Driver>
	 * driver =genericDAO.findByCriteria(Driver.class,criterias); Driver
	 * driver=genericDAO.getByCriteria(Driver.class, criterias);
	 * if(driver==null) { error = true;
	 * lineError.append("Invalid Driver Name, "); } else {
	 * eztoll.setDriver(driver); eztoll.setTerminal(driver.getTerminal()); } }
	 * else{
	 * 
	 * String transactiondate = null; if
	 * (validDate(getCellValue(row.getCell(6)))) {
	 * transactiondate=dateFormat.format(((Date)
	 * getCellValue(row.getCell(6))).getTime()); }
	 * 
	 * String drivequery="select obj from Ticket obj where   obj.loadDate<='"
	 * +transactiondate+"' and obj.unloadDate>='"+transactiondate+
	 * "' and obj.vehicle="+vehicle.getId();
	 * 
	 * System.out.println(" my query is "+drivequery); List<Ticket>
	 * tickets=genericDAO.executeSimpleQuery(drivequery);
	 * if(!tickets.isEmpty()){ boolean tic=true; boolean first=true;
	 * List<String> driverid=new ArrayList<String>(); for(Ticket
	 * ticket:tickets){ boolean
	 * d=driverid.contains(ticket.getDriver().getId()+"");
	 * driverid.add(ticket.getDriver().getId()+""); if(first){ first=false;
	 * continue; } if(!d){ if(override==false){ error = true;
	 * lineError.append("More than one Driver, "); tic=false; }else{ tic=false;
	 * 
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); }
	 * 
	 * 
	 * } } } if(tic){ eztoll.setDriver(tickets.get(0).getDriver()); Driver
	 * driver=genericDAO.getById(Driver.class,tickets.get(0).getDriver().getId()
	 * ); eztoll.setTerminal(driver.getTerminal()); } }else{
	 * if(override==false){ error = true;
	 * lineError.append("Ticket Data Does not match with Toll Tag, "); } else{
	 * try { criterias.clear(); String name = (String)
	 * getCellValue(row.getCell(2));
	 * //System.out.println("\nTerminal====>"+name+"\n"); if
	 * (StringUtils.isEmpty(name)) throw new Exception("Invalid terminal"); else
	 * { criterias.put("name", name); criterias.put("type", 4); } Location
	 * location = genericDAO.getByCriteria(Location.class, criterias); if
	 * (location == null) throw new Exception("no such Terminal"); else
	 * eztoll.setTerminal(location); } catch (Exception ex) { error = true;
	 * lineError.append("Terminal,"); log.warn(ex.getMessage()); } } } }
	 * 
	 * 
	 * eztoll.setPlateNumber(vehicle); }
	 * 
	 * } } catch (Exception ex) { error = true;
	 * lineError.append("Invalid Plate Number, "); log.warn(ex.getMessage()); }
	 * 
	 * } else { if(eztoll.getTollTagNumber()!=null) { VehicleTollTag
	 * vehicletoll=genericDAO.getById(VehicleTollTag.class,eztoll.
	 * getTollTagNumber().getId());
	 * eztoll.setPlateNumber(vehicletoll.getVehicle()); } }
	 * 
	 * }
	 * 
	 * //FOR TRANSACTION DATE try { if (validDate(getCellValue(row.getCell(6))))
	 * eztoll.setTransactiondate((Date) getCellValue(row.getCell(6))); else {
	 * error = true; lineError.append("Transaction Date,"); } } catch(Exception
	 * ex) { System.out.println("\nERROR IN TRANSACTION DATE\n");
	 * log.warn(ex.getMessage());
	 * 
	 * } //FOR TRANSACTION TIME try{ if
	 * (validDate(getCellValue(row.getCell(7)))){
	 * eztoll.setTransactiontime(dateFormat2.format((Date)
	 * getCellValue(row.getCell(7)))); } else{ String trxTime1=(String)
	 * getCellValue(row.getCell(7));
	 * 
	 * if(!(StringUtils.isEmpty(trxTime1))){ if (trxTime1.length() == 5 ||
	 * trxTime1.length() == 8||trxTime1.length() == 7){ StringBuilder time = new
	 * StringBuilder(StringUtils.leftPad((String)getCellValue(row.getCell(7)),4,
	 * '0')); //time.insert(2, ':'); if(trxTime1.length() == 8){
	 * eztoll.setTransactiontime(time.toString().substring(0, 5)); } else
	 * if(trxTime1.length() == 7){
	 * eztoll.setTransactiontime(time.toString().substring(0, 4)); } else{
	 * eztoll.setTransactiontime(time.toString()); }
	 * 
	 * } else { //System.out.println("\ntrx time is not valid\n"); error = true;
	 * lineError.append("Transaction Time,"); } } else{
	 * lineError.append("Transaction Time,"); }
	 * 
	 * } } catch(Exception e){
	 * 
	 * } if (validTime((String) getCellValue(row.getCell(6)))) { StringBuilder
	 * time = new
	 * StringBuilder(StringUtils.leftPad((String)getCellValue(row.getCell(6)),4,
	 * '0')); int hh=Integer.parseInt(time.substring(0,2)); int
	 * mm=Integer.parseInt(time.substring(2));
	 * 
	 * if(hh==24) { if(mm==0) { time.insert(2, ':');
	 * eztoll.setTransactiontime(time.toString()); //
	 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n"); } else { error
	 * = true; lineError.append("transaction time,"); } } else { if(hh<24) {
	 * if(mm<=59) { time.insert(2, ':');
	 * eztoll.setTransactiontime(time.toString()); //
	 * System.out.println("\nTRANSACTION TIME ====>"+time+"\n"); } else { error
	 * = true; lineError.append("transaction time minut is > 59,"); } } else {
	 * error = true; lineError.append("transaction time hours is > 24,"); } } }
	 * else { error = true;
	 * lineError.append("transaction time more than 5 degits,");
	 * 
	 * }
	 * 
	 * //FOR AGENCY try { eztoll.setAgency((String)
	 * getCellValue(row.getCell(8))); } catch (Exception ex) { error = true;
	 * lineError.append("Agency,"); log.warn(ex.getMessage()); } //FOR AMOUNTS
	 * String amount1=row.getCell(9).toString(); Double amount2 =
	 * getValidGallon(amount1); if (amount2 != null) {
	 * eztoll.setAmount(Math.abs(amount2)); } else {
	 * lineError.append("Amount,"); error = true; } //END OF CELL
	 * 
	 * if (!error) { java.sql.Date transacDt = new
	 * java.sql.Date(eztoll.getTransactiondate().getTime()); try { StringBuffer
	 * query = new StringBuffer(); query.append("select obj from EzToll obj ");
	 * query.append(" where obj.toolcompany.name='"+tollcompany+"'");
	 * query.append(" and obj.company.name='"+company+"' "); if(tollNum != null)
	 * query.append(" and obj.tollTagNumber.tollTagNumber="+tollNum+" ");
	 * if(plateNum != null)
	 * query.append(" and obj.plateNumber.plate="+plateNum+" ");
	 * query.append(" and obj.transactiondate='"+transacDt+"' ");
	 * query.append(" and obj.amount="+amount1);
	 * query.append(" and obj.driver.fullName='"+getCellValue(row.getCell(5))+
	 * "'"); query.append(" and obj.agency='"+getCellValue(row.getCell(8))+"'");
	 * 
	 * List<EzToll> duplicateCheck =
	 * genericDAO.executeSimpleQuery(query.toString());
	 * 
	 * if(duplicateCheck!= null && duplicateCheck.size()>0){
	 * 
	 * error=true; errorcount++; lineError.append("Toll Tag already exists, ");
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * if (eztolls.contains(eztoll)) { lineError.append("Duplicate eztoll,");
	 * error = true; errorcount++; }else{ eztolls.add(eztoll); } } else {
	 * errorcount++; }
	 * 
	 * if (!error) { java.sql.Date transacDt = new
	 * java.sql.Date(eztoll.getTransactiondate().getTime());
	 * 
	 * try {
	 * 
	 * StringBuffer query = new StringBuffer();
	 * query.append("select obj from EzToll obj ");
	 * query.append(" where obj.toolcompany.name='"+tollcompany+"'");
	 * query.append(" and obj.company.name='"+company+"' "); if(tollNum != null)
	 * query.append(" and obj.tollTagNumber.tollTagNumber="+tollNum+" ");
	 * if(plateNum != null)
	 * query.append(" and obj.plateNumber.plate="+plateNum+" ");
	 * query.append(" and obj.transactiondate='"+transacDt+"' ");
	 * query.append(" and obj.amount="+amount1);
	 * query.append(" and obj.driver.fullName='"+getCellValue(row.getCell(5))+
	 * "'"); query.append(" and obj.agency='"+getCellValue(row.getCell(8))+"'");
	 * 
	 * List<EzToll> duplicateCheck =
	 * genericDAO.executeSimpleQuery(query.toString());
	 * 
	 * if(duplicateCheck!= null && duplicateCheck.size()>0){
	 * 
	 * error=true; errorcount++; lineError.append("Toll Tag already exists, ");
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * 
	 * if (eztolls.contains(eztoll)) { lineError.append("Duplicate eztoll,");
	 * error = true; errorcount++; }
	 * 
	 * eztolls.add(eztoll); } else { errorcount++; }
	 * 
	 * }//TRY INSIDE SHILE(LOOP) catch (Exception ex) { error=true;
	 * log.warn(ex); } if (lineError.length()>0) {
	 * System.out.println("Error :"+lineError.toString());
	 * list.add("Line "+count+":"+lineError.toString()+"<br/>"); }
	 * System.out.println("Record No :"+count); count++; }//CLOSE while
	 * (rows.hasNext()) }//FIRST TRY catch (Exception e) {
	 * log.warn("Error in import customer :" + e); } if (errorcount==0) {
	 * for(EzToll etoll:eztolls) {
	 * etoll.setTerminal(etoll.getDriver().getTerminal());
	 * etoll.setCompany(etoll.getDriver().getCompany());
	 * genericDAO.saveOrUpdate(etoll); } } return list; }
	 */

	//

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importfuellogMainSheet(InputStream is, Boolean override) throws Exception {
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance //XSSFWorkbook

		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<FuelLog> fuellogs = new ArrayList<FuelLog>();

		// List<String> emptydatalist=new ArrayList<String>();
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();

			wb = new HSSFWorkbook(fs);

			int numOfSheets = wb.getNumberOfSheets();
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			FuelLog fuellog = null;

			Iterator rows = sheet.rowIterator();
			StringBuffer lineError;

			while (rows.hasNext()) {
				boolean error = false;
				buffer = new StringBuffer();
				int cellCount = 0;
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				lineError = new StringBuffer("");
				try {

					fuellog = new FuelLog();
					String Fname = (String) getCellValue(row.getCell(0));
					if (override == false) {
						try {
							if (StringUtils.isEmpty(Fname)) {
								error = true;
								lineError.append("Fuel Vendor is blank,");
							} else {
								criterias.clear();
								criterias.put("name", Fname);
								FuelVendor fuelvendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
								if (fuelvendor == null) {
									error = true;
									lineError.append("no such Fuel Vendor,");
									// throw new Exception("no such
									// fuelvender");
								} else {
									fuellog.setFuelvendor(fuelvendor);
								}
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("fuelvendor,");
							log.warn(ex.getMessage());
						}
					} else {
						criterias.clear();
						criterias.put("name", Fname);
						FuelVendor fuelvendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
						fuellog.setFuelvendor(fuelvendor);
					}

					// FOR COMPANY
					String company = ((String) getCellValue(row.getCell(1)));
					if (override == false) {
						try {
							if (StringUtils.isEmpty(company)) {
								error = true;
								lineError.append("Company is blank,");
							} else {
								criterias.clear();
								criterias.put("type", 3);
								criterias.put("name", company);
								Location companyName = genericDAO.getByCriteria(Location.class, criterias);
								if (companyName == null) {
									error = true;
									lineError.append("no such Company,");
								} else {
									fuellog.setCompany(companyName);
								}
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Company,");
							log.warn(ex.getMessage());
						}
					} else {
						criterias.clear();
						criterias.put("type", 3);
						criterias.put("name", company);
						Location companyName = genericDAO.getByCriteria(Location.class, criterias);
						fuellog.setCompany(companyName);
					}

					if (override == false) {
						Date date2 = row.getCell(2).getDateCellValue();

						try {
							if (validDate(date2)) {
								fuellog.setInvoiceDate(dateFormat1.parse(dateFormat1.format(date2)));
							} else {
								error = true;
								lineError.append("Invoice Date,");
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Invoice Date,");
							log.warn(ex.getMessage());

						}
					} else {
						if (validDate(getCellValue(row.getCell(2))))
							fuellog.setInvoiceDate((Date) getCellValue(row.getCell(2)));
						else {
							fuellog.setInvoiceDate(null);
						}
					}

					// FOR UNVOICED NUMBER
					// System.out.println("\nInvoiceNo====>"+(String)
					// getCellValue(row.getCell(4))+"\n");
					String invoiceNo = "";
					try {
						invoiceNo = (String) getCellValue(row.getCell(3));
					} catch (Exception e) {
						error = true;
						lineError.append("Invalid Invoice Number, ");
					}
					if (override == false) {
						try {
							if ((StringUtils.isEmpty(invoiceNo))) {
								error = true;
								lineError.append("Invoice# is blank,");
							} else {
								fuellog.setInvoiceNo((String) getCellValue(row.getCell(3)));
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Invoice Number,");
							log.warn(ex.getMessage());
						}
					} else {
						if ((StringUtils.isEmpty(invoiceNo)))
							fuellog.setInvoiceNo(null);
						else {
							fuellog.setInvoiceNo((String) getCellValue(row.getCell(3)));
						}
					}

					// FOR TRANSACTION DATE
					/*
					 * try { if (validDate(getCellValue(row.getCell(5))))
					 * fuellog.setTransactiondate((Date)
					 * getCellValue(row.getCell(5))); else { error = true;
					 * lineError.append("transaction date,"); } }
					 * catch(Exception ex) {
					 * //System.out.println("\nERROR IN TRANSACTION DATE\n");
					 * log.warn(ex.getMessage());
					 * 
					 * }
					 */
					if (override == false) {
						try {

							Date date4 = row.getCell(4).getDateCellValue();

							if (validDate(date4)) {
								fuellog.setTransactiondate(dateFormat1.parse(dateFormat1.format(date4)));

							} else {
								error = true;
								lineError.append("Transaction Date,");
							}
						} catch (Exception ex) {

							error = true;
							lineError.append("Transaction Date,");
							log.warn(ex.getMessage());

						}
					} else {
						if (validDate(getCellValue(row.getCell(4)))) {

							fuellog.setTransactiondate((Date) getCellValue(row.getCell(4)));
						} else {
							fuellog.setTransactiondate(null);
						}
					}

					try {
						if (validDate(getCellValue(row.getCell(5)))) {
							fuellog.setTransactiontime(dateFormat2.format((Date) getCellValue(row.getCell(5))));
						} else {
							// new trx time,uploading in 00:00 format
							String trxTime1 = (String) getCellValue(row.getCell(5));
							if (!(StringUtils.isEmpty(trxTime1))) {
								if (override == false) {
									if (trxTime1.length() == 5 || trxTime1.length() == 8) {
										StringBuilder time = new StringBuilder(
												StringUtils.leftPad((String) getCellValue(row.getCell(5)), 4, '0'));
										// time.insert(2, ':');
										if (trxTime1.length() == 8) {
											fuellog.setTransactiontime(time.toString().substring(0, 5));
										} else {
											fuellog.setTransactiontime(time.toString());
										}

									} else {
										// System.out.println("\ntrx time is not
										// valid\n");
										error = true;
										lineError.append("Transaction Time,");
									}
								} else {
									if (trxTime1.length() == 5 || trxTime1.length() == 8) {
										StringBuilder time = new StringBuilder(
												StringUtils.leftPad((String) getCellValue(row.getCell(5)), 4, '0'));
										// time.insert(2, ':');
										if (trxTime1.length() == 8) {
											fuellog.setTransactiontime(time.toString().substring(0, 5));
										} else {
											fuellog.setTransactiontime(time.toString());
										}
									} else {
										fuellog.setTransactiontime((String) getCellValue(row.getCell(5)));
									}
								}
							} else {
								fuellog.setTransactiontime((String) getCellValue(row.getCell(5)));
								// System.out.println("\nElse trxTime
								// empty=="+trxTime1+"\n");
							}

						}
					} catch (Exception ex) {
						fuellog.setTransactiontime((String) getCellValue(row.getCell(5)));
					}

					String unit = ((String) getCellValue(row.getCell(6)));
					// if(override==false){
					//error = setUnitNumberInFuelLog(criterias, row, fuellog, lineError, error, unit);
					try {
						if (StringUtils.isEmpty(unit)) {

							String lastName = ((String) getCellValue(row.getCell(7)));
							String firstName = ((String) getCellValue(row.getCell(8)));
							if (!lastName.isEmpty() && !firstName.isEmpty()) {
								criterias.clear();
								criterias.put("firstName", firstName);
								criterias.put("lastName", lastName);
								Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
								if (driver == null) {
									error = true;
									lineError.append("Unit is blank (check driver name),");
								} else {
									// HEMA: Added
									fuellog.setDriversid(driver);
									fuellog.setTerminal(driver.getTerminal());
									String transdate = null;

									if (validDate(getCellValue(row.getCell(4)))) {
										transdate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
									}

									String drivequery = "select obj from Ticket obj where   obj.loadDate<='" + transdate
											+ "' and obj.unloadDate>='" + transdate + "' and obj.driver="
											+ driver.getId();
									System.out.println("******** query is " + drivequery);
									List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);
									if (!tickets.isEmpty()) {
										boolean tic = true;
										boolean first = true;
										List<String> vehicleid = new ArrayList<String>();
										for (Ticket ticket : tickets) {
											boolean d = vehicleid.contains(ticket.getVehicle().getId() + "");
											vehicleid.add(ticket.getVehicle().getId() + "");
											if (first) {
												first = false;
												continue;
											}
											if (!d) {
												error = true;
												lineError.append("More than one vehicle, ");
												tic = false;
											}
										}
										if (tic) {
											fuellog.setUnit(tickets.get(0).getVehicle());
										}
									} else {
										String driveFuelLogquery = "select obj from DriverFuelLog obj where  obj.transactionDate='"
												+ transdate + "' and obj.driver=" + driver.getId();
										System.out.println("********driver fuel query is " + driveFuelLogquery);
										List<DriverFuelLog> driverFuelLog = genericDAO
												.executeSimpleQuery(driveFuelLogquery);
										if (!driverFuelLog.isEmpty()) {
											boolean tic = true;
											boolean first = true;
											List<String> truckid = new ArrayList<String>();
											for (DriverFuelLog drvFuelLog : driverFuelLog) {
												boolean d = truckid.contains(drvFuelLog.getTruck().getId() + "");
												truckid.add(drvFuelLog.getTruck().getId() + "");
												if (first) {
													first = false;
													continue;
												}
												if (!d) {
													error = true;
													lineError.append("More than one vehicle, ");
													tic = false;
												}
											}
											if (tic) {
												fuellog.setUnit(driverFuelLog.get(0).getTruck());
											}
										} else {

											String odometerQery = "select obj from Odometer obj where obj.recordDate='"
													+ transdate + "' and obj.driver=" + driver.getId();
											System.out.println("********odometer query is " + odometerQery);
											List<Odometer> driverOdometer = genericDAO.executeSimpleQuery(odometerQery);

											if (!driverOdometer.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> vehid = new ArrayList<String>();
												for (Odometer odometer : driverOdometer) {
													boolean d = vehid.contains(odometer.getTruck().getId() + "");
													vehid.add(odometer.getTruck().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														error = true;
														lineError.append("More than one vehicle, ");
														tic = false;
													}
												}
												if (tic) {
													fuellog.setUnit(driverOdometer.get(0).getTruck());
												}
											} else {
												error = true;
												lineError.append(
														"Unit is blank, No matching  Ticket, Fuel Log,  Odometer entry, ");
											}
										}
									}
								}

							} else {

								error = true;
								lineError.append("Unit is blank,");
							}
						} else {
							criterias.clear();

							String transactionDate = null;
							System.out.println("********** date value is " + getCellValue(row.getCell(4)));
							if (validDate(getCellValue(row.getCell(4)))) {
								transactionDate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
							}
							Vehicle vehicle = null;
							String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
									+ Integer.parseInt((String) getCellValue(row.getCell(6))) + " and obj.validFrom<='"
									+ transactionDate + "' and obj.validTo>='" + transactionDate + "'";

							System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
							List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);

							if (vehicleList == null || vehicleList.size() == 0) {
								System.out.println("Entered here ");
								error = true;
								lineError.append("no such Vehicle,");
							} else {
								fuellog.setUnit(vehicleList.get(0));
								vehicle = vehicleList.get(0);
								// ***** newly added *********

								String lastName = ((String) getCellValue(row.getCell(7)));
								try {
									if (!StringUtils.isEmpty(lastName)) {
										criterias.clear();
										criterias.put("lastName", lastName);
										Driver lname = genericDAO.getByCriteria(Driver.class, criterias);
										if (lname == null) {
											error = true;
											lineError.append("No such Last Name,");
										} else {
											// fuellog.setDriverLname(lname);
										}
									}
								} catch (Exception ex) {
									error = true;
									lineError.append("Driver Last Name,");
									log.warn(ex.getMessage());
								}

								String firstName = ((String) getCellValue(row.getCell(8)));
								try {
									if (!StringUtils.isEmpty(firstName)) {
										criterias.clear();
										criterias.put("firstName", firstName);
										Driver fname = genericDAO.getByCriteria(Driver.class, criterias);
										if (fname == null) {
											error = true;
											lineError.append("No such First Name,");
										} else {
											// fuellog.setDriverFname(fname);
										}
									}
								} catch (Exception ex) {
									error = true;
									lineError.append("Driver First Name,");
									log.warn(ex.getMessage());
								}

								// taking driverFname and driverLName and
								// storing as fullname
								try {
									if (StringUtils.isEmpty(lastName) && StringUtils.isEmpty(firstName)) {
										String transactiondate = null;
										if (validDate(getCellValue(row.getCell(4)))) {
											transactiondate = dateFormat
													.format(((Date) getCellValue(row.getCell(4))).getTime());
										}

										String drivequery = "select obj from Ticket obj where   obj.loadDate<='"
												+ transactiondate + "' and obj.unloadDate>='" + transactiondate
												+ "' and obj.vehicle=" + vehicle.getId();
										System.out.println("******** query is " + drivequery);
										List<Ticket> tickets = genericDAO.executeSimpleQuery(drivequery);
										if (!tickets.isEmpty()) {
											boolean tic = true;
											boolean first = true;
											List<String> driverid = new ArrayList<String>();
											for (Ticket ticket : tickets) {
												boolean d = driverid.contains(ticket.getDriver().getId() + "");
												driverid.add(ticket.getDriver().getId() + "");
												if (first) {
													first = false;
													continue;
												}
												if (!d) {
													error = true;
													lineError.append("More than one Driver, ");
													tic = false;
												}
											}
											if (tic) {
												fuellog.setDriversid(tickets.get(0).getDriver());
												Driver driver = genericDAO.getById(Driver.class,
														tickets.get(0).getDriver().getId());
												fuellog.setTerminal(driver.getTerminal());
											}
										} else {
											String driveFuelLogquery = "select obj from DriverFuelLog obj where   obj.transactionDate='"
													+ transactiondate + "' and obj.truck=" + vehicle.getId();
											System.out.println("********driver fuel query is " + driveFuelLogquery);
											List<DriverFuelLog> driverFuelLog = genericDAO
													.executeSimpleQuery(driveFuelLogquery);
											if (!driverFuelLog.isEmpty()) {
												boolean tic = true;
												boolean first = true;
												List<String> driverid = new ArrayList<String>();
												for (DriverFuelLog drvFuelLog : driverFuelLog) {
													boolean d = driverid.contains(drvFuelLog.getDriver().getId() + "");
													driverid.add(drvFuelLog.getDriver().getId() + "");
													if (first) {
														first = false;
														continue;
													}
													if (!d) {
														error = true;
														lineError.append("More than one Driver, ");
														tic = false;
													}
												}
												if (tic) {
													fuellog.setDriversid(driverFuelLog.get(0).getDriver());
													Driver driver = genericDAO.getById(Driver.class,
															driverFuelLog.get(0).getDriver().getId());
													fuellog.setTerminal(driver.getTerminal());
												}
											} else {

												String odometerQery = "select obj from Odometer obj where obj.recordDate='"
														+ transactiondate + "' and obj.truck=" + vehicle.getId();
												System.out.println("********odometer query is " + odometerQery);
												List<Odometer> driverOdometer = genericDAO
														.executeSimpleQuery(odometerQery);

												if (!driverOdometer.isEmpty()) {
													boolean tic = true;
													boolean first = true;
													List<String> driverid = new ArrayList<String>();
													for (Odometer odometer : driverOdometer) {
														boolean d = driverid
																.contains(odometer.getDriver().getId() + "");
														driverid.add(odometer.getDriver().getId() + "");
														if (first) {
															first = false;
															continue;
														}
														if (!d) {
															error = true;
															lineError.append("More than one Driver, ");
															tic = false;
														}
													}
													if (tic) {
														fuellog.setDriversid(driverOdometer.get(0).getDriver());
														Driver driver = genericDAO.getById(Driver.class,
																driverOdometer.get(0).getDriver().getId());
														fuellog.setTerminal(driver.getTerminal());
													}
												} else {
													error = true;
													lineError
															.append("No matching  Ticket, Fuel Log,  Odometer entry, ");
												}
											}
										}
									} else {
										criterias.clear();
										System.out.println("Firstname = " + firstName + ", lastname = " + lastName);
										criterias.put("firstName", firstName);
										criterias.put("lastName", lastName);
										Driver driver = genericDAO.getByCriteria(Driver.class, criterias);
										if (driver == null) {
											error = true;
											lineError.append("Invalid Driver,");
											throw new Exception("Invalid Driver");
										} else {
											fuellog.setDriversid(driver);
											fuellog.setTerminal(driver.getTerminal());
										}
									}
								} catch (Exception ex) {
									ex.printStackTrace();
									log.warn(ex.getMessage());
								}

								// ******** newly added ends here ********

							}
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("Unit,");
						log.warn(ex.getMessage());
					}
 
					
					// FOR FUEL CARD NUMBER
					String cardNo = ((String) getCellValue(row.getCell(9)));
					/*
					 * if(override==false) { try {
					 * //fuellog.setFuelCardNumber(((String)
					 * getCellValue(row.getCell(10)))); if(!cardNo.isEmpty()){
					 * fuellog.setFuelCardNumber((String)getCellValue(row.
					 * getCell(10))); } else{ error = true;
					 * lineError.append("Card Number is blank,"); } } catch
					 * (Exception ex) { error = true;
					 * lineError.append("Card Number,"); } } else{
					 * fuellog.setFuelCardNumber((String)getCellValue(row.
					 * getCell(10))); }
					 */

					///

					try {
						// fuellog.setFuelCardNumber(((String)
						// getCellValue(row.getCell(10))));
						if (override == false) {// StringUtils.isEmpty
							/* if(!cardNo.isEmpty()){ */
							if (!StringUtils.isEmpty(cardNo)) {
								
								if (handleExcludedCardNumberChecks(fuellog, cardNo)) {
									// reset cardNo
									cardNo = StringUtils.EMPTY;
								} else {
									criterias.clear();
	
									if (fuellog.getFuelvendor() != null)
										criterias.put("fuelvendor.id", fuellog.getFuelvendor().getId());
									criterias.put("fuelcardNum", cardNo);
									List<FuelCard> fuelcard = genericDAO.findByCriteria(FuelCard.class, criterias);
									if (!fuelcard.isEmpty() && fuelcard.size() > 0) {
										if (fuellog.getDriversid() != null && fuellog.getFuelvendor() != null) {
											criterias.clear();
											criterias.put("driver.id", fuellog.getDriversid().getId());
											criterias.put("fuelvendor.id", fuellog.getFuelvendor().getId());
											criterias.put("fuelcard.id", fuelcard.get(0).getId());
											List<DriverFuelCard> driverfuelcard = genericDAO
													.findByCriteria(DriverFuelCard.class, criterias);
											if (!driverfuelcard.isEmpty() && driverfuelcard.size() > 0)
												fuellog.setFuelcard(fuelcard.get(0));
											else {
												error = true;
												lineError.append(
														" Invalid  Fuel Card# for entered Fuel Vendor and Driver ,");
											}
										}
									} else {
										error = true;
										lineError.append(" Invalid Card Number,");
									}
								}
							} else {
								error = true;
								lineError.append("Card Number is blank,");
							}
						} else {
							System.out.println("\nELSE OVerride card Number1\n");
							/* if(!cardNo.isEmpty()){ */
							if (!StringUtils.isEmpty(cardNo)) {
								System.out.println("\nOVerride card Number2\n");
								criterias.clear();
								if (fuellog.getFuelvendor() != null)
									criterias.put("fuelvendor.id", fuellog.getFuelvendor().getId());
								criterias.put("fuelcardNum", cardNo);
								List<FuelCard> fuelcard = genericDAO.findByCriteria(FuelCard.class, criterias);
								if (!fuelcard.isEmpty() && fuelcard.size() > 0) {
									if (fuellog.getDriversid() != null && fuellog.getFuelvendor() != null) {
										criterias.clear();
										criterias.put("driver.id", fuellog.getDriversid().getId());
										criterias.put("fuelvendor.id", fuellog.getFuelvendor().getId());
										criterias.put("fuelcard.id", fuelcard.get(0).getId());
										List<DriverFuelCard> driverfuelcard = genericDAO
												.findByCriteria(DriverFuelCard.class, criterias);
										if (!driverfuelcard.isEmpty() && driverfuelcard.size() > 0)
											fuellog.setFuelcard(fuelcard.get(0));

									}
								}
								/*
								 * FuelCard fuelCard=null;
								 * fuellog.setFuelcard(fuelCard);
								 * System.out.println(
								 * "\nOVerride card Number3\n");
								 */
							} else {
								System.out.println("\nOVerride card Number4\n");
								FuelCard fuelCard = null;
								fuellog.setFuelcard(fuelCard);
							}
						}

					} catch (Exception ex) {

						error = true;
						System.out.println("\n\n Error in Card Number\n");
						lineError.append("Card Number,");
					}

					///

					// new FOR FUEL CARD NUMBER for long
					/*
					 * String cardNo = ((String) getCellValue(row.getCell(10)));
					 * if(override==false) { try {
					 * fuellog.setFuelCardNumber(Long.parseLong((String)
					 * getCellValue(row.getCell(10)))); if(!cardNo.isEmpty()){
					 * fuellog.setFuelCardNumber((String)getCellValue(row.
					 * getCell(10)));
					 * fuellog.setFuelCardNumber((Long)getCellValue(row.getCell(
					 * 10))); } else{ error = true;
					 * lineError.append("Card Number is blank,"); } } catch
					 * (Exception ex) { error = true;
					 * lineError.append("Card Number,"); } } else{
					 * fuellog.setFuelCardNumber((Long)getCellValue(row.getCell(
					 * 10))); }
					 */

					String fueltext = ((String) getCellValue(row.getCell(10)));
					if (override == false) {
						try {
							if (!fueltext.isEmpty()) {
								fuellog.setFueltype((String) getCellValue(row.getCell(10)));
								criterias.clear();
								criterias.put("dataType", "fuel_type");
								criterias.put("dataText", fueltext);
								StaticData staticdata = genericDAO.getByCriteria(StaticData.class, criterias);
								if (staticdata == null) {
									error = true;
									lineError.append("Fuel Type,");
								} else {
									/*
									 * System.out.println("\nstaticdata---id=>"+
									 * staticdata.getId()+"\n");
									 * System.out.println(
									 * "\nstaticdata---dataType=>"+staticdata.
									 * getDataType()+"\n"); System.out.println(
									 * "\nstaticdata---dataText=>"+staticdata.
									 * getDataText()+"\n");
									 */
									fuellog.setFueltype((String) getCellValue(row.getCell(10)));
								}
							} else {
								error = true;
								lineError.append("Fuel Type is blank,");
							}
						} catch (Exception ex) {
							error = true;
							lineError.append("Fuel Type,");
							log.warn(ex.getMessage());
						}
					} else {
						fuellog.setFueltype((String) getCellValue(row.getCell(10)));
					}

					// FOR FUEL TYPE
					/*
					 * String fueltype=((String) getCellValue(row.getCell(11)));
					 * if(override==false){ try { if(!fueltype.isEmpty()){
					 * fuellog.setFueltype((String)getCellValue(row.getCell(11))
					 * ); } else{ error = true;
					 * lineError.append("Fueltype is blank,"); } }
					 * catch(Exception ex) { error = true;
					 * lineError.append("Fueltype,"); log.warn(ex.getMessage());
					 * } } else{
					 * fuellog.setFueltype((String)getCellValue(row.getCell(11))
					 * ); }
					 */

					// FOR CITY
					String city = ((String) getCellValue(row.getCell(11)));
					if (!(StringUtils.isEmpty(city))) {
						if (override == false) {
							try {
								if (!city.isEmpty()) {
									fuellog.setCity((String) getCellValue(row.getCell(11)));
								} else {
									error = true;
									lineError.append("City is blank,");
								}
							} catch (Exception ex) {
								error = true;
								lineError.append("City,");
							}
						} else {
							fuellog.setCity((String) getCellValue(row.getCell(11)));
						}
					} else {
						fuellog.setCity("");
						System.out.println("\ncity is empty\n" + fuellog.getCity());
					}

					// FOR STATE
					String name = (String) getCellValue(row.getCell(12));
					if (!(StringUtils.isEmpty(name))) {
						if (override == false) {
							try {
								criterias.clear();
								if (StringUtils.isEmpty(name)) {
									error = true;
									lineError.append("State is blank,");
									// throw new Exception("Invalid state
									// name");
								} else {
									criterias.clear();
									criterias.put("name", name);
									State state = genericDAO.getByCriteria(State.class, criterias);
									if (state == null) {
										error = true;
										lineError.append("no such State,");
										// throw new Exception("no such state");
									} else {
										fuellog.setState(state);
									}
								}
							} catch (Exception ex) {
								error = true;
								lineError.append("State,");
								System.out.println("\nerroe in state==>" + ex + "\n");
								log.warn(ex.getMessage());
							}
						} else {
							criterias.clear();
							criterias.put("name", name);
							State state = genericDAO.getByCriteria(State.class, criterias);
							fuellog.setState(state);
						}
					} else {
						// System.out.println("\nstate is empty 11\n");

						criterias.clear();
						criterias.put("id", 3600l);
						State state = genericDAO.getByCriteria(State.class, criterias);
						// System.out.println("\nstate is empty44\n");
						fuellog.setState(state);
						System.out.println("\nstate is empty55\n");

					}

					// for GALLONS

					/*
					 * String testgallon=row.getCell(14).toString(); Double
					 * gallon = getValidGallon(testgallon); if (gallon != null)
					 * { fuellog.setGallons(gallon); } else {
					 * lineError.append("gallons,"); error = true; }
					 */

					String gallon = "";
					if (override == false) {
						if (row.getCell(13) != null) {
							gallon = row.getCell(13).toString();
						}
						Double gallon2 = getValidGallon(gallon);
						if (gallon2 != null) {
							fuellog.setGallons(gallon2);
						} else {
							lineError.append("Gallon is blank,");
							error = true;
						}
					} else {
						if (row.getCell(13) != null) {
							gallon = row.getCell(13).toString();
						}
						Double gallon2 = getValidGallon(gallon);
						if (gallon2 != null) {
							fuellog.setGallons(gallon2);
						} else {
						}
					}

					// for unitprice
					/*
					 * String unitprice=row.getCell(15).toString(); Double
					 * unitprice1 = getValidGallon(unitprice); if (unitprice1 !=
					 * null) { fuellog.setUnitprice(unitprice1); } else {
					 * System.out.println("\nunitprice is null\n");
					 * lineError.append("unitprice,"); error = true; }
					 */
					String unitprice1 = "";
					if (override == false) {
						if (row.getCell(14) != null) {
							unitprice1 = row.getCell(14).toString();
						}

						Double unitprice2 = getValidGallon(unitprice1);
						if (unitprice2 != null) {
							fuellog.setUnitprice(unitprice2);
						} else {
							lineError.append("Unit Price is blank,");
							error = true;
						}
					} else {

						if (row.getCell(14) != null) {
							unitprice1 = row.getCell(14).toString();
						}

						Double unitprice2 = getValidGallon(unitprice1);
						if (unitprice2 != null) {
							fuellog.setUnitprice(unitprice2);
						} else {
						}
					}

					// Gross Cost
					String grossamount1 = "";
					if (override == false) {
						if (row.getCell(15) != null) {
							grossamount1 = row.getCell(15).toString();
						}

						if (!(StringUtils.isEmpty(grossamount1))) {
							Double grossamount2 = getValidGallon(grossamount1);
							if (grossamount2 != null) {
								fuellog.setGrosscost(grossamount2);
							} else {
								lineError.append("Gross Cost,");
								error = true;
							}
						}
					} else {
						if (row.getCell(15) != null) {
							grossamount1 = row.getCell(15).toString();
						}

						Double grossamount2 = getValidGallon(grossamount1);
						if (grossamount2 != null) {
							fuellog.setGrosscost(grossamount2);
						} else {
						}
					}
					// Gross Cost

					// FOR FEES

					String fees1 = "";
					if (override == false) {
						if (row.getCell(16) != null) {
							fees1 = row.getCell(16).toString();
						}

						Double fees2 = getValidGallon(fees1);
						if (fees2 != null) {
							fuellog.setFees(fees2);
						} else {
							lineError.append("Fees is blank,");
							error = true;
						}
					} else {
						if (row.getCell(16) != null) {
							fees1 = row.getCell(16).toString();
						}

						Double fees2 = getValidGallon(fees1);
						if (fees2 != null) {
							fuellog.setFees(fees2);
						} else {
						}
					}
					// FOR DISCOUNTS
					String discount1 = "";
					// System.out.println("\ndiscount1===>"+discount1+"\n");
					if (override == false) {
						if (row.getCell(17) != null) {
							discount1 = row.getCell(17).toString();
						}

						Double discount2 = getValidGallon(discount1);
						if (discount2 != null) {
							discount2 = Math.abs(discount2);
							fuellog.setDiscounts(discount2);
						} else {
							lineError.append("Discount is blank,");
							error = true;
						}
					} else {
						if (row.getCell(17) != null) {
							discount1 = row.getCell(17).toString();
						}

						Double discount2 = getValidGallon(discount1);
						if (discount2 != null) {
							discount2 = Math.abs(discount2);
							fuellog.setDiscounts(discount2);
						} else {
						}
					}

					// FOR AMOUNTS
					/*
					 * String amount1=row.getCell(18).toString(); Double amount2
					 * = getValidGallon(amount1); if (amount2 != null){
					 * fuellog.setAmount(amount2); } else {
					 * System.out.println("\namount2 is null\n");
					 * lineError.append("amount,"); error = true; }
					 */
					String amount1 = null;
					/*
					 * try{ amount1=row.getCell(18).toString(); } catch
					 * (Exception e) { lineError.append("amount,"); error =
					 * true; }
					 */
					if (override == false) {
						try {
							amount1 = row.getCell(18).toString();
							Double amount2 = getValidGallon(amount1);
							if (amount2 != null) {
								fuellog.setAmount(amount2);
							} else {
								lineError.append("Amount is blank,");
								error = true;
							}
						} catch (Exception e) {
							lineError.append("Amount,");
							error = true;
						}
					} else {
						if (row.getCell(18) != null)
							amount1 = row.getCell(18).toString();

						Double amount2 = getValidGallon(amount1);
						if (amount2 != null) {
							fuellog.setAmount(amount2);
						} else {
						}
					}

					// CALCULATING DISCOUNT AND NET AMMOUNT IF FUELDISCOUNT
					// PERCENTAGE IS PRESENT
					if (override == false) {
						if (!error) {
							try {
								if (!StringUtils.isEmpty(grossamount1)) {

									criterias.clear();
									criterias.put("name", Fname);
									FuelVendor vendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
									if (vendor != null) {
										System.out.println("\nDiscount first\n");
										criterias.clear();
										criterias.put("fuelvendor.id", vendor.getId());
										FuelDiscount fueldicount = genericDAO.getByCriteria(FuelDiscount.class,
												criterias);
										System.out.println("\nDiscount Second\n");
										if (fueldicount != null) {
											double firstdiscountamount = fuellog.getDiscounts();
											double seconddiscountpercentage = fueldicount.getFuelDiscountPercentage();
											double seconddiscountAmount = 0.0;
											double totalDiscount = 0.0;
											Double grossamount = getValidGallon(grossamount1);
											if (grossamount != null) {
												if (grossamount != 0) {
													// System.out.println("\nFirst
													// grossamount--->"+grossamount+"\n");
													grossamount = grossamount - firstdiscountamount;
													seconddiscountAmount = (grossamount * seconddiscountpercentage);
													grossamount = grossamount - seconddiscountAmount;
													totalDiscount = firstdiscountamount + seconddiscountAmount;

													totalDiscount = MathUtil.roundUp(totalDiscount, 2);
													grossamount = MathUtil.roundUp(grossamount, 2);

													fuellog.setDiscounts(totalDiscount);
													fuellog.setAmount(grossamount);

													/*
													 * System.out.println(
													 * "\nfirstdiscountamount==>"
													 * +firstdiscountamount+"\n"
													 * ); System.out.println(
													 * "\nseconddiscountpercentage==>"
													 * +seconddiscountpercentage
													 * +"\n");
													 * System.out.println(
													 * "\nseconddiscountAmount==>"
													 * +seconddiscountAmount+
													 * "\n");
													 * System.out.println(
													 * "\ntotalDiscount==>"+
													 * totalDiscount+"\n");
													 * System.out.println(
													 * "\nsetAmount(grossamount)==>"
													 * +grossamount+"\n");
													 */
												}
											}

										}
									}
								}
								Double grossamount = getValidGallon(grossamount1);
								if (grossamount == null) {
									double discountAmount = fuellog.getDiscounts();
									double feesAmount = fuellog.getFees();
									// System.out.println("grossamount ==
									// null");
									Double NetAmount = getValidGallon(amount1);
									// System.out.println("NetAmount ==
									// "+NetAmount+"\n");
									if (discountAmount == 0 && feesAmount == 0) {
										// double
										// grossAmount=NetAmount+(discountAmount-feesAmount);
										fuellog.setGrosscost(NetAmount);
									} else {
										lineError.append("Discount and Fees should be zero,");
										error = true;
									}

								}
								if (grossamount == 0) {
									double discountAmount = fuellog.getDiscounts();
									double feesAmount = fuellog.getFees();
									// System.out.println("grossamount ==
									// null");
									Double NetAmount = getValidGallon(amount1);
									// System.out.println("NetAmount ==
									// "+NetAmount+"\n");
									if (discountAmount == 0 && feesAmount == 0) {
										// double
										// grossAmount=NetAmount+(discountAmount-feesAmount);
										fuellog.setGrosscost(NetAmount);
									} else {
										lineError.append("Discount and Fees should be zero,");
										error = true;
									}
								}

							} catch (Exception ex) {
								System.out.println("error calculating total discount");
							}
						}
					}
					/// If override is true

					else {
						/* if (!error) { */
						try {
							if (!StringUtils.isEmpty(grossamount1)) {

								criterias.clear();
								criterias.put("name", Fname);
								FuelVendor vendor = genericDAO.getByCriteria(FuelVendor.class, criterias);
								if (vendor != null) {
									System.out.println("\nDiscount first\n");
									criterias.clear();
									criterias.put("fuelvendor.id", vendor.getId());
									FuelDiscount fueldicount = genericDAO.getByCriteria(FuelDiscount.class, criterias);
									System.out.println("\nDiscount Second\n");
									if (fueldicount != null) {
										double firstdiscountamount = fuellog.getDiscounts();
										double seconddiscountpercentage = fueldicount.getFuelDiscountPercentage();
										double seconddiscountAmount = 0.0;
										double totalDiscount = 0.0;
										Double grossamount = getValidGallon(grossamount1);
										if (grossamount != null) {
											if (grossamount != 0) {
												// System.out.println("\nFirst
												// grossamount--->"+grossamount+"\n");
												grossamount = grossamount - firstdiscountamount;
												seconddiscountAmount = (grossamount * seconddiscountpercentage);
												grossamount = grossamount - seconddiscountAmount;
												totalDiscount = firstdiscountamount + seconddiscountAmount;

												totalDiscount = MathUtil.roundUp(totalDiscount, 2);
												grossamount = MathUtil.roundUp(grossamount, 2);

												fuellog.setDiscounts(totalDiscount);
												fuellog.setAmount(grossamount);

												/*
												 * System.out.println(
												 * "\nfirstdiscountamount==>"+
												 * firstdiscountamount+"\n");
												 * System.out.println(
												 * "\nseconddiscountpercentage==>"
												 * +seconddiscountpercentage+
												 * "\n"); System.out.println(
												 * "\nseconddiscountAmount==>"+
												 * seconddiscountAmount+"\n");
												 * System.out.println(
												 * "\ntotalDiscount==>"+
												 * totalDiscount+"\n");
												 * System.out.println(
												 * "\nsetAmount(grossamount)==>"
												 * +grossamount+"\n");
												 */
											}
										}

									}
								}
							}
							Double grossamount = getValidGallon(grossamount1);
							if (grossamount == null) {
								/*
								 * double discountAmount=fuellog.getDiscounts();
								 * double feesAmount=fuellog.getFees();
								 */
								// System.out.println("grossamount == null");
								Double NetAmount = getValidGallon(amount1);
								System.out.println("\ngrossamount == null when orride true\n");
								System.out.println("\nNetAmount==>" + NetAmount + "\n");
								// System.out.println("NetAmount ==
								// "+NetAmount+"\n");
								/* if(discountAmount ==0 && feesAmount ==0){ */
								// double
								// grossAmount=NetAmount+(discountAmount-feesAmount);
								fuellog.setGrosscost(NetAmount);
								/* } */
								/*
								 * else{ lineError.append(
								 * "discount and fees should be zero,"); error =
								 * true; }
								 */

							}
							if (grossamount == 0) {
								/*
								 * double discountAmount=fuellog.getDiscounts();
								 * double feesAmount=fuellog.getFees();
								 */
								// System.out.println("grossamount == null");
								Double NetAmount = getValidGallon(amount1);
								System.out.println("\ngrossamount == 0 when orride true\n");
								System.out.println("\nNetAmount==>" + NetAmount + "\n");
								// System.out.println("NetAmount ==
								// "+NetAmount+"\n");
								/* if(discountAmount ==0 && feesAmount ==0){ */
								// double
								// grossAmount=NetAmount+(discountAmount-feesAmount);
								fuellog.setGrosscost(NetAmount);
								/* } */
								/*
								 * else{ lineError.append(
								 * "discount and fees should be zero,"); error =
								 * true; }
								 */
							}

						} catch (Exception ex) {
							System.out.println("exp--->" + ex + "\n");
							System.out.println("error calculating total discount when override id true");
						}
						// }
					}

					// END OF CELL
					if (override == false) {
						System.out.println("***** eneter here ok 0");
						if (!error) {
							System.out.println("***** eneter here ok 1");
							Map prop = new HashMap();
							prop.put("fuelvendor", fuellog.getFuelvendor().getId());
							prop.put("driversid", fuellog.getDriversid().getId());
							prop.put("company", fuellog.getCompany().getId());
							prop.put("terminal", fuellog.getTerminal().getId());
							prop.put("state", fuellog.getState().getId());
							prop.put("unit", fuellog.getUnit().getId());
							prop.put("invoiceDate", dateFormat1.format(fuellog.getInvoiceDate()));
							prop.put("invoiceNo", fuellog.getInvoiceNo());
							prop.put("transactiondate", dateFormat1.format(fuellog.getTransactiondate()));
							prop.put("transactiontime", fuellog.getTransactiontime());
							if (fuellog.getFuelcard() != null) {
								prop.put("fuelcard", fuellog.getFuelcard().getId());
							} 
							prop.put("fueltype", fuellog.getFueltype());
							prop.put("city", fuellog.getCity());
							prop.put("gallons", fuellog.getGallons());
							prop.put("unitprice", fuellog.getUnitprice());
							prop.put("fees", fuellog.getFees());
							prop.put("discounts", fuellog.getDiscounts());
							prop.put("amount", fuellog.getAmount());
							boolean rst = genericDAO.isUnique(FuelLog.class, fuellog, prop);
							System.out.println("***** eneter here ok 2" + rst);
							if (!rst) {
								System.out.println("***** eneter here ok 3");
								lineError.append("Fuel log entry already exists(Duplicate),");
								error = true;
								errorcount++;
							}

							if (fuellogs.contains(fuellog)) {
								lineError.append("Duplicate Fuel log,");
								error = true;
								errorcount++;
							} else {
								fuellogs.add(fuellog);
							}
						} else {
							errorcount++;
						}
					} else {
						if (!error) {
							fuellogs.add(fuellog);
						} else {
							errorcount++;
						}
					}

				} // TRY INSIDE WHILE(LOOP)
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("***** Entered here in exception" + ex.getMessage());

					error = true;
					log.warn(ex);
				}
				if (lineError.length() > 0) {
					System.out.println("Error :" + lineError.toString());
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				System.out.println("Record No :" + count);
				count++;
			} // CLOSE while (rows.hasNext())
		} // FIRST TRY
		catch (Exception e) {
			list.add("Not able to upload XL !!! please try again");
			log.warn("Error in import Fuel log :" + e);
			e.printStackTrace();
		}

		if (errorcount == 0) {
			for (FuelLog fuelog : fuellogs) {
				String ticktQuery = "select obj from Ticket obj where obj.driver=" + fuelog.getDriversid().getId()
						+ " and obj.loadDate <='" + drvdf.format(fuelog.getTransactiondate())
						+ "' and obj.unloadDate>='" + drvdf.format(fuelog.getTransactiondate()) + "'";

				List<Ticket> tickObj = genericDAO.executeSimpleQuery(ticktQuery);

				if (tickObj.size() > 0 && tickObj != null) {
					fuelog.setFuelViolation("Not Violated");
				} else {
					fuelog.setFuelViolation("Violated");
				}

				Map criti = new HashMap();
				criti.clear();
				criti.put("id", fuelog.getDriversid().getId());
				Driver drvOBj = genericDAO.getByCriteria(Driver.class, criti);
				if (drvOBj != null)
					fuelog.setDriverFullName(drvOBj.getFullName());

				criti.clear();
				criti.put("id", fuelog.getUnit().getId());
				Vehicle vehObj = genericDAO.getByCriteria(Vehicle.class, criti);
				if (vehObj != null)
					fuelog.setUnitNum(vehObj.getUnitNum());

				genericDAO.saveOrUpdate(fuelog);
			}
		}
		return list;
	}
	
	private boolean setUnitNumberInFuelLogRefactored(Map criterias, HSSFRow row, FuelLog fuellog, StringBuffer lineError, String unit) {
		boolean isError = false;

		try {
			String lastName = ((String) getCellValue(row.getCell(7)));
			String firstName = ((String) getCellValue(row.getCell(8)));

			// Unit is EMPTY, Driver Name is EMPTY
			if(StringUtils.isEmpty(unit) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(firstName)) {
				isError = true;
				lineError.append("Unit is empty, Driver is empty");
				return isError;
			}
	
			// if unit number is NOT EMPTY
			if(!StringUtils.isEmpty(unit)) {
				String transdate = getTransactionDateFromExcel(row);
				if (!setVehicleInFuelLogFromUnitNumber(row, transdate, fuellog)) {
					isError = true;
					lineError.append("no such Vehicle,");
					return isError;
				} // else : able to set the unit number, proceed down to set driver
			} // else : derive unit number using driver, so first proceed down to set driver
			
			
			// If driverName is NOT EMPTY
			isError = setDriverAndOrUnitInFuelLog(criterias, row, fuellog, lineError, lastName, firstName, unit); 
			
			/*if (StringUtils.isEmpty(unit)) {
				isError = true;
				lineError.append("Unit is blank, No matching Ticket, Fuel Log,  Odometer entry for given driver");
				return isError;
				
			} */
			
		} catch (Exception ex) {
			isError = true;
			lineError.append("Unit,");
			log.warn(ex.getMessage());
		}
		return isError;
	}

	private boolean setDriverAndOrUnitInFuelLog(Map criterias, HSSFRow row, FuelLog fuellog, StringBuffer lineError,
			String lastName, String firstName, String unit) {
		
		boolean isError = false;
		
		List<Driver> driver = new ArrayList<Driver>();
		
		if (!lastName.isEmpty() && !firstName.isEmpty()) { // user has given driver name 
			driver = getDriversFromName(criterias, lastName, firstName);
			
			if (driver.size() == 0) { // No drivers found for given name, do not try to derive name, send error
				isError = true;
				lineError.append("Invalid Driver,");
				return isError ;
			} 
		}
		
		// driver -> can be 0 , 1 , > 1
		return setDriverAndOrUnitUsingNameInFuelLog(criterias, row, fuellog, lineError, lastName, firstName, driver, unit);
	
	}

	private boolean setDriverAndOrUnitUsingNameInFuelLog(Map criterias, HSSFRow row, FuelLog fuellog, StringBuffer lineError, String lastName,
			String firstName, List<Driver> driver, String unit) {
			
		boolean isError = false;
		String listOfDrivers = null;
		// 1 or more drivers found
		if (driver.size() > 0) {
			listOfDrivers = getCommaSeparatedListOfDriverID(driver);
		}
		
		String transdate = getTransactionDateFromExcel(row);
		
		// pass the Stringbuffer lineError -> to capture the right error
		
		if (setFuelLogDetailsUsingTicket(listOfDrivers, transdate, fuellog, unit)) {
			// driver, terminal, unit set using ticket, so return
			return isError;
		}
	
		if (setFuelLogDetailsUsingDriverFuelLog(listOfDrivers, transdate, fuellog, unit)) {
			// driver, terminal, unit set using driverfuellog, so return
			return isError;
		}
		
		if (setFuelLogDetailsUsingOdometer(listOfDrivers, transdate, fuellog, unit)) {
			// driver, terminal, unit set using Odometer, so return
			return isError;
		}
		
		// could not set using Ticket, DriverFuelLog, Odometer, try to set the driver using Active flag
		if (setActiveDriverInFuelLog(criterias, fuellog, lastName, firstName)) {
			// could set the right driver using active flag
			return isError;
		}
		
		isError = true;
		lineError.append("Invalid Driver,");
		return isError;
	}

	private List<Driver> getDriversFromName(Map criterias, String lastName, String firstName) {
		criterias.clear();
		criterias.put("firstName", firstName);
		criterias.put("lastName", lastName);
		List<Driver> driver = genericDAO.findByCriteria(Driver.class, criterias);
		return driver;
	}

	private boolean setActiveDriverInFuelLog(Map criterias, FuelLog fuellog, String lastName,
			String firstName) {
		boolean error;
		criterias.clear();
		criterias.put("firstName", firstName);
		criterias.put("lastName", lastName);
		criterias.put("status", 1);
		List<Driver> activeDrivers = genericDAO.findByCriteria(Driver.class, criterias);
		if (activeDrivers.size() == 0) {
			// no drivers found
			return false;
		} else {
			// take first one blindly !! :(
			fuellog.setDriversid(activeDrivers.get(0));
			fuellog.setTerminal(activeDrivers.get(0).getTerminal());
			return true;
		}
	}

	private boolean setVehicleInFuelLogFromUnitNumber(HSSFRow row, String transdate, FuelLog fuelLog) {
		
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
				+ Integer.parseInt((String) getCellValue(row.getCell(6))) + " and obj.validFrom<='"
				+ transdate + "' and obj.validTo>='" + transdate + "'";

		System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList == null || vehicleList.size() == 0) {
			System.out.println("Entered here ");
			return true;
		} else {
			fuelLog.setUnit(vehicleList.get(0));
			return true;
		}
	}

	private Vehicle getVehicleInFuelLogFromUnitNumber(String unit, String transdate) {
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
				+ Integer.parseInt(unit) + " and obj.validFrom<='"
				+ transdate + "' and obj.validTo>='" + transdate + "'";

		System.out.println("******* The vehicle query for fuel upload is " + vehicleQuery);
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		
		if (vehicleList == null || vehicleList.size() == 0) {
			return null;
		} else {
			return vehicleList.get(0);
		}
	}
	
	private boolean setFuelLogDetailsUsingOdometer(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		String odometerQuery = "select obj from Odometer obj where "
				+ "obj.recordDate='" + transdate 
				+ "' and obj.driver=IN ("
				+ listOfDrivers + ")";
		
		System.out.println("Select Odomoeter with list of drivers -> " + odometerQuery);
		List<Odometer> driverOdometer = genericDAO.executeSimpleQuery(odometerQuery);
		
		if (driverOdometer.size() == 0) {
			return false;
		} 
		
		Odometer matchingOdometer = driverOdometer.get(0);
		fuelLog.setDriversid(matchingOdometer.getDriver());
		fuelLog.setTerminal(matchingOdometer.getTerminal());
		fuelLog.setUnit(matchingOdometer.getTruck());
		return true;
	}

	private boolean setFuelLogDetailsUsingDriverFuelLog(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		String driveFuelLogquery = "select obj from DriverFuelLog obj where  "
				+ "obj.transactionDate='" + transdate 
				+ "' and obj.driver= IN ("
				+ listOfDrivers + ")";
		
		System.out.println("Select DriverFuelLog with list of drivers -> " + driveFuelLogquery);
		List<DriverFuelLog> driverFuelLog = genericDAO.executeSimpleQuery(driveFuelLogquery);
		
		if (driverFuelLog.size() == 0) {
			return false; // could not set
		} 
		
		DriverFuelLog matchingDriverFuelLog = driverFuelLog.get(0);
		fuelLog.setDriversid(matchingDriverFuelLog.getDriver());
		fuelLog.setTerminal(matchingDriverFuelLog.getTerminal());
		fuelLog.setUnit(matchingDriverFuelLog.getTruck());
		
		return true;
	}

	private boolean setFuelLogDetailsUsingTicket(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		
		if (StringUtils.isEmpty(listOfDrivers)) {
			// retrieve and set using unit
			return setFuelLogUsingUnit(listOfDrivers, transdate, fuelLog, unit);
		} else {
			return setFuelLogUsingDriver(listOfDrivers, transdate, fuelLog, unit);
		}
	}

	private boolean setFuelLogUsingUnit(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		
		Vehicle vehicle = getVehicleInFuelLogFromUnitNumber(unit, transdate);
		String driverquery = "select obj from Ticket obj where "
				+ "obj.loadDate<='" + transdate + "' and obj.unloadDate>='" + transdate
				+ "' and obj.vehicle=" + vehicle.getId();
		System.out.println("******** query is " + driverquery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(driverquery);
		if (tickets.size() == 0) { // no matching tickets found
			return false;
		}
		
		// set Driver, Terminal, Vehicle
		Ticket matchingTicket = tickets.get(0);
		fuelLog.setDriversid(matchingTicket.getDriver());
		fuelLog.setTerminal(matchingTicket.getTerminal());
		fuelLog.setUnit(matchingTicket.getVehicle());
		
		return false;
	}

	private boolean setFuelLogUsingDriver(String listOfDrivers, String transdate, FuelLog fuelLog, String unit) {
		String driverquery = "select obj from Ticket obj where "
				+ "obj.loadDate<='" + transdate
				+ "' and obj.unloadDate>='" + transdate 
				+ "' and obj.driver IN ("
				+ listOfDrivers + ")";
		System.out.println("Select Ticket with list of drivers -> " + driverquery);
		List<Ticket> tickets = genericDAO.executeSimpleQuery(driverquery);
		
		if (tickets.size() == 0) { // no matching tickets found
			return false;
		} 
		
		// set Driver, Terminal, Vehicle
		Ticket matchingTicket = tickets.get(0);
		fuelLog.setDriversid(matchingTicket.getDriver());
		fuelLog.setTerminal(matchingTicket.getTerminal());
		
		if (!StringUtils.isEmpty(unit)) {
			// validate if ticket.unit == unit
			System.out.println("Unit number does not match the unit number retrieved through ticket = " + matchingTicket.getVehicle().getUnitNum());
			return false;
		}
		
		fuelLog.setUnit(matchingTicket.getVehicle());
		return true;
	}

	private String getCommaSeparatedListOfDriverID(List<Driver> drivers) {
		StringBuffer driverIdList = new StringBuffer();
		for ( Driver d : drivers) {
			driverIdList.append("," + d.getId());
		}
		
		return driverIdList.toString().replaceFirst(",", "");
	}

	private String getTransactionDateFromExcel(HSSFRow row) {
		String transdate = null;
		if (validDate(getCellValue(row.getCell(4)))) {
			transdate = dateFormat.format(((Date) getCellValue(row.getCell(4))).getTime());
		}
		return transdate;
	}

	private boolean handleExcludedCardNumberChecks(FuelLog fuellog, String cardNo) {
		if (!StringUtils.equalsIgnoreCase("EXCLUDE_ERROR_CHECK", cardNo)) {
			return false;
		}
		
		System.out.println("\nOVerride card Number\n");
		FuelCard fuelCard = null;
		fuellog.setFuelcard(fuelCard);
		return true;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importMainSheet(InputStream is) throws Exception {
		// initializing the InputStream from a file using
		// POIFSFileSystem, before converting the result
		// into an HSSFWorkbook instance
		System.out.println("***** Here step 2");
		HSSFWorkbook wb = null;
		StringBuffer buffer = null;
		List<String> list = new ArrayList<String>();
		List<Ticket> tickets = new ArrayList<Ticket>();
		// List<String> emptydatalist=new ArrayList<String>();
		int count = 1;
		int errorcount = 0;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			ErrorData edata = new ErrorData();
			// FileWriter writer = new FileWriter("e:/errordata.txt");
			wb = new HSSFWorkbook(fs);
			// loop for every worksheet in the workbook
			int numOfSheets = wb.getNumberOfSheets();
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			Ticket ticket = null;
			Iterator rows = sheet.rowIterator();
			StringBuffer lineError;
			while (rows.hasNext()) {
				boolean error = false;
				buffer = new StringBuffer();
				int cellCount = 0;
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				lineError = new StringBuffer("");
				try {
					ticket = new Ticket();
					ticket.setTicketStatus(1);
					ticket.setPayRollStatus(1);
					if (validDate(getCellValue(row.getCell(0))))
						ticket.setLoadDate((Date) getCellValue(row.getCell(0)));
					else {
						error = true;
						lineError.append("Load Date,");
					}
					if (validTime((String) getCellValue(row.getCell(1)))) {
						StringBuilder timeIn = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(1)), 4, '0'));
						timeIn.insert(2, ':');
						ticket.setTransferTimeIn(timeIn.toString());
					} else {
						error = true;
						lineError.append("Transfer Time In,");
					}
					if (validTime((String) getCellValue(row.getCell(2)))) {
						StringBuilder timeOut = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(2)), 4, '0'));
						timeOut.insert(2, ':');
						ticket.setTransferTimeOut(timeOut.toString());
					} else {
						error = true;
						lineError.append("Transfer Time Out,");
					}
					try {
						criterias.clear();
						criterias.put("type", 1);
						criterias.put("unit", Integer.parseInt((String) getCellValue(row.getCell(3))));
						Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
						if (vehicle == null)
							throw new Exception("no such truck");
						else
							ticket.setVehicle(vehicle);
					} catch (Exception ex) {
						error = true;
						lineError.append("Truck,");
						log.warn(ex.getMessage());
					}
					try {
						criterias.clear();
						criterias.put("type", 2);
						criterias.put("unit", Integer.parseInt((String) getCellValue(row.getCell(4))));
						Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
						if (vehicle == null)
							throw new Exception("no such trailer");
						else
							ticket.setTrailer(vehicle);
					} catch (Exception ex) {
						error = true;
						lineError.append("Trailer,");
						log.warn(ex.getMessage());
					}
					Object unloadDate = getCellValue(row.getCell(5));
					if (validDate(unloadDate))
						ticket.setUnloadDate((Date) unloadDate);
					else {
						error = true;
						lineError.append("" + " Date,");
					}
					if (validTime((String) getCellValue(row.getCell(6)))) {
						StringBuilder timeIn = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(6)), 4, '0'));
						timeIn.insert(2, ':');
						ticket.setLandfillTimeIn(timeIn.toString());
					} else {
						error = true;
						lineError.append("Landfill Time In,");
					}
					if (validTime((String) getCellValue(row.getCell(7)))) {
						StringBuilder timeOut = new StringBuilder(
								StringUtils.leftPad((String) getCellValue(row.getCell(7)), 4, '0'));
						timeOut.insert(2, ':');
						ticket.setLandfillTimeOut(timeOut.toString());
					} else {
						error = true;
						lineError.append("Landfill Time Out,");
					}
					try {
						criterias.clear();
						criterias.put("type", 1);
						criterias.put("name", (String) getCellValue(row.getCell(8)));
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null)
							throw new Exception("no such origin");
						else
							ticket.setOrigin(location);
					} catch (Exception ex) {
						error = true;
						lineError.append("Origin,");
						// log.warn(ex.getMessage());
					}
					try {
						ticket.setOriginTicket(Long.parseLong((String) getCellValue(row.getCell(9))));
					} catch (Exception ex) {
						error = true;
						lineError.append("Origin Ticket,");
					}
					try {
						criterias.clear();
						criterias.put("type", 2);
						criterias.put("name", (String) getCellValue(row.getCell(10)));
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null)
							throw new Exception("no such destination");
						else
							ticket.setDestination(location);
					} catch (Exception ex) {
						error = true;
						lineError.append("Destination,");
						// log.warn(ex.getMessage());
					}

					// FOR CUSTOMER AND COMPANY_LACATION
					BillingRate billingRate = null;
					try {
						criterias.clear();
						criterias.put("type", 1);
						criterias.put("name", (String) getCellValue(row.getCell(8)));
						Location originid = genericDAO.getByCriteria(Location.class, criterias);

						criterias.clear();
						criterias.put("type", 2);
						criterias.put("name", (String) getCellValue(row.getCell(10)));
						Location destinationid = genericDAO.getByCriteria(Location.class, criterias);

						if (originid != null && destinationid != null) {
							// BillingRate billingRate = null;
							String query = "select obj from BillingRate obj where transferStation=" + originid.getId()
									+ " and landfill=" + destinationid.getId();
							List<BillingRate> rates = genericDAO.executeSimpleQuery(query);

							for (BillingRate rate : rates) {
								if (rate.getRateUsing() == null) {
									billingRate = rate;
									break;
								} else if (rate.getRateUsing() == 1) {
									// calculation for a load date
									if ((ticket.getLoadDate().getTime() >= rate.getValidFrom().getTime())
											&& (ticket.getLoadDate().getTime() <= rate.getValidTo().getTime())) {
										billingRate = rate;
										break;
									}
								} else if (rate.getRateUsing() == 2) {
									// calculation for a unload date
									if ((ticket.getUnloadDate().getTime() >= rate.getValidFrom().getTime())
											&& (ticket.getUnloadDate().getTime() <= rate.getValidTo().getTime())) {
										billingRate = rate;
										break;
									}
								}
							}
							if (billingRate != null) {
								ticket.setCompanyLocation((billingRate.getCompanyLocation() != null)
										? billingRate.getCompanyLocation() : null);
								ticket.setCustomer(
										(billingRate.getCustomername() != null) ? billingRate.getCustomername() : null);
							} else {
								System.out.println("Customer and Company Location");

							}
							/*
							 * { error = true; lineError.append(
							 * "Rate is expired for this origin and destination,please contact to administrator,"
							 * ); }
							 */
						}

					} catch (Exception ex) {
						System.out.println("Customer and Company Location");
						log.warn(ex.getMessage());
					}

					try {
						ticket.setDestinationTicket(Long.parseLong((String) getCellValue(row.getCell(11))));
					} catch (Exception ex) {
						error = true;
						lineError.append("Destination Ticket,");
					}

					if (ticket.getOrigin() != null) {
						if (reportService.checkDuplicate(ticket, "O")) {
							lineError.append("Duplicate Origin Ticket,");
							error = true;
						}
					}

					if (ticket.getDestination() != null) {
						if (reportService.checkDuplicate(ticket, "D")) {
							lineError.append("Duplicates Dest. Ticket,");
							error = true;
						}

					}

					if (ticket.getUnloadDate() != null && ticket.getLoadDate() != null) {
						if (ticket.getUnloadDate().before(ticket.getLoadDate())) {
							lineError.append("Unload Date is before Load Date,");
							error = true;
						}
					}
					Double tgross = getValidAmount((String) getCellValue(row.getCell(12)));
					if (tgross != null)
						ticket.setTransferGross(tgross);
					else {
						error = true;
						lineError.append("Transfer Gross,");
					}
					Double ttare = getValidAmount((String) getCellValue(row.getCell(13)));
					if (ttare != null)
						ticket.setTransferTare(ttare);
					else {
						lineError.append("Transfer Tare,");
						error = true;
					}
					if (tgross != null && ttare != null) {
						ticket.setTransferNet(tgross - ttare);
						ticket.setTransferTons((tgross - ttare) / 2000);
						/* if(billingRate.getBilledby().equals("bygallon")){ */
						ticket.setGallons(ticket.getTransferNet() / 8.34);
						// }
					}
					Double lgross = getValidAmount((String) getCellValue(row.getCell(16)));
					if (lgross != null)
						ticket.setLandfillGross(lgross);
					else {
						error = true;
						lineError.append("Landfill Gross,");
					}
					Double ltare = getValidAmount((String) getCellValue(row.getCell(17)));
					if (ltare != null)
						ticket.setLandfillTare(ltare);
					else {
						lineError.append("Landfill Tare,");
						error = true;
					}
					if (lgross != null && ltare != null) {
						ticket.setLandfillNet(lgross - ltare);
						ticket.setLandfillTons((lgross - ltare) / 2000);
					}
					String driverName = ((String) getCellValue(row.getCell(21)));
					Driver driver = null;
					try {
						if (StringUtils.isEmpty(driverName))
							throw new Exception("Invalid driver");
						else {
							// String[] names = driverName.split(" ");
							criterias.clear();
							/*
							 * criterias.put("firstName", names[1]);
							 * criterias.put("lastName", names[0]);
							 */
							criterias.put("status", 1);
							criterias.put("fullName", driverName);
							driver = genericDAO.getByCriteria(Driver.class, criterias);
							if (driver == null)
								throw new Exception("Invalid driver");
							ticket.setDriver(driver);
							// ticket.setDriverCompany(driver.getCompany());
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("Driver,");
					}

					try {
						String employeeCompanyName = ((String) getCellValue(row.getCell(23)));
						if (StringUtils.isEmpty(employeeCompanyName))
							throw new Exception("Invalid company");
						else {
							criterias.clear();
							criterias.put("status", 1);
							criterias.put("name", employeeCompanyName);
							Location employeeCompany = genericDAO.getByCriteria(Location.class, criterias);
							if (employeeCompany == null)
								throw new Exception("Invalid company");
							ticket.setDriverCompany(employeeCompany);
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("employee company,");
					}

					String subcontractor = ((String) getCellValue(row.getCell(20)));
					try {
						if (!StringUtils.isEmpty(subcontractor)) {
							/*
							 * if (driver != null && !"Subcontractor"
							 * .equalsIgnoreCase(driver .getLastName().trim()))
							 * { throw new Exception("Invalid subcontractor"); }
							 * 
							 * 
							 * else {
							 */
							criterias.clear();
							criterias.put("name", subcontractor);
							SubContractor contractor = genericDAO.getByCriteria(SubContractor.class, criterias);
							if (contractor == null) {
								throw new Exception("Invalid subcontractor");
							} else {
								ticket.setSubcontractor(contractor);
							}
							criterias.clear();
						}
						// }
					} catch (Exception ex) {
						error = true;
						lineError.append("Sub Contractor,");
					}
					Object billBatch = getCellValue(row.getCell(22));
					if (validDate(billBatch))
						ticket.setBillBatch((Date) billBatch);
					else {
						error = true;
						lineError.append("Batch Date,");
					}
					try {
						criterias.clear();
						String locCode = (String) getCellValue(row.getCell(24));
						if (StringUtils.isEmpty(locCode))
							throw new Exception("Invalid terminal");
						else {
							criterias.put("code", locCode);
							criterias.put("type", 4);
						}
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null) {
							throw new Exception("no such terminal");
						} else {
							criterias.clear();
							criterias.put("status", 1);
							criterias.put("fullName", driverName);
							criterias.put("terminal", location);
							Driver driverobj = genericDAO.getByCriteria(Driver.class, criterias);
							if (driverobj == null) {
								throw new Exception("Terminal does not match with driver");
							} else {
								ticket.setTerminal(location);
							}
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("Terminal does not match with driver,");
						log.warn(ex.getMessage());
					}
					try {
						User user = genericDAO.getByUniqueAttribute(User.class, "username",
								(String) getCellValue(row.getCell(25)));
						if (user == null) {
							throw new Exception("Invalid user");
						} else {
							ticket.setCreatedBy(user.getId());
							ticket.setEnteredBy(user.getName());
						}
					} catch (Exception ex) {
						error = true;
						lineError.append("User,");
					}
					if (!error) {
						if (tickets.contains(ticket)) {
							lineError.append("Duplicate Ticket,");
							error = true;
							errorcount++;
						} else {
							tickets.add(ticket);
						}
					} else {
						errorcount++;
					}

				} catch (Exception ex) {
					error = true;
					log.warn(ex);
				}
				if (lineError.length() > 0) {
					System.out.println("Error :" + lineError.toString());
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				System.out.println("Record No :" + count);
				count++;
			}
		} catch (Exception e) {
			log.warn("Error in import customer :" + e);
		}
		if (errorcount == 0) {
			for (Ticket ticket : tickets) {
				genericDAO.saveOrUpdate(ticket);
			}
		}
		return list;
	}

	/**
	 * This is a helper method to retrieve the value of a cell regardles of its
	 * type, which will be converted into a String.
	 * 
	 * @param cell
	 * @return
	 */
	private Object getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		Object result = null;
		int cellType = cell.getCellType();
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? Boolean.TRUE : Boolean.FALSE;
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			
			result = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			HSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			// assumption is made that dataFormat = 14,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			if (dataFormat == 164) {
				result = cell.getDateCellValue();
			} else {
				result = cell.getNumericCellValue();
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}
		if (result instanceof Double) {
			return String.valueOf(((Double) result).longValue());
		}
		if (result instanceof Date) {
			return result;
		}
		return result.toString();
	}

	private Object getCellValue(HSSFCell cell, boolean resolveFormula) {
		if (cell == null) {
			return null;
		}
		Object result = null;
		int cellType = cell.getCellType();
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? Boolean.TRUE : Boolean.FALSE;
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			
			switch(cell.getCachedFormulaResultType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                System.out.println("Last evaluated as: " + cell.getNumericCellValue());
                result = cell.getNumericCellValue();
                break;
            case HSSFCell.CELL_TYPE_STRING:
                System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                result = cell.getRichStringCellValue();
                break;
			}
			
			//result = cell.getCellFormula();
			
			
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			HSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			System.out.println("Data format = " + dataFormat);
			// assumption is made that dataFormat = 14,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			if (dataFormat == 165 || dataFormat == 164 || dataFormat == 14) {
				result = cell.getDateCellValue();
			} else {
				result = cell.getNumericCellValue();
			}
			System.out.println("Numeric cell value == " + result);
			break;
		case HSSFCell.CELL_TYPE_STRING:
			//result = cell.getStringCellValue();
			result = cell.getRichStringCellValue();
			System.out.println("String -> " + result);
			break;
		default:
			break;
		}
		
		if (result instanceof Integer) {
			return String.valueOf((Integer) result);
		} else if (result instanceof Double) {
			return String.valueOf(((Double) result)); //.longValue());
		}
		if (result instanceof Date) {
			return result;
		}
		return result.toString();
	}

	
	private boolean validTime(String cellValue) {
		if (StringUtils.isEmpty(cellValue))
			return false;
		if (cellValue.length() > 4)
			return false;
		return true;
	}

	private boolean validDate(Object cellValue) {
		if (cellValue instanceof Date)
			return true;
		else
			return false;
	}

	private Double getValidAmount(String cellValue) {

		if (StringUtils.isEmpty(cellValue) || cellValue.length() > 5)
			return null;
		try {
			return Double.parseDouble(cellValue);

		} catch (Exception ex) {
			return null;
		}
	}

	private Double getValidGallon(String cellValue) {

		if (StringUtils.isEmpty(cellValue))
			return null;
		try {
			return Double.parseDouble(cellValue);

		} catch (Exception ex) {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<String> importVehiclePermitMainSheet(InputStream is) {

		List<String> list = new ArrayList<String>();
		List<VehiclePermit> vehiclePermits = new ArrayList<VehiclePermit>();
		int errorcount = 0;
		HSSFWorkbook wb = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
			Map criterias = new HashMap();
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;
			Iterator rows = sheet.rowIterator();
			VehiclePermit vehiclePermit = null;
			int count = 1;
			while (rows.hasNext()) {
				row = (HSSFRow) rows.next();
				if (count == 1) {
					count++;
					continue;
				}
				boolean error = false;
				StringBuffer lineError = new StringBuffer();
				vehiclePermit = new VehiclePermit();

				String unitNum = "";
				try {
					unitNum = (String) getCellValue(row.getCell(0));
					if (!StringUtils.isEmpty(unitNum)) {
						criterias.clear();
						criterias.put("unitNum", unitNum);
						Vehicle vehicle = genericDAO.getByCriteria(Vehicle.class, criterias);
						if (vehicle == null) {
							error = true;
							lineError.append("Invalid Vehicle Number, ");
						} else {
							vehiclePermit.setVehicle(vehicle);
						}
					} else {
						error = true;
						lineError.append("Vehicle Number is Empty, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Unit,");
					log.warn(e.getMessage());
				}

				String company = "";
				try {
					company = (String) getCellValue(row.getCell(1));
					if (!StringUtils.isEmpty(company)) {
						criterias.clear();
						criterias.put("name", company);
						criterias.put("type", 3);
						Location location = genericDAO.getByCriteria(Location.class, criterias);
						if (location == null) {
							error = true;
							lineError.append("Invalid Company Name, ");
						} else {
							vehiclePermit.setCompanyLocation(location);
						}
					} else {
						error = true;
						lineError.append("Company is Empty, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Company, ");
					log.warn(e.getMessage());
				}

				String permitType = "";
				try {
					permitType = (String) getCellValue(row.getCell(2));
					if (!StringUtils.isEmpty(permitType)) {
						criterias.clear();
						criterias.put("dataType", "PERMIT_TYPE");
						criterias.put("dataLabel", permitType);
						SetupData setUpdata = genericDAO.getByCriteria(SetupData.class, criterias);
						if (setUpdata == null) {
							error = true;
							lineError.append("Permit Type doesn't exists,");
						} else {
							SetupData data = genericDAO.getById(SetupData.class, setUpdata.getId());
							vehiclePermit.setPermitType(data);
						}
					} else {
						error = true;
						lineError.append("Permit Type is Empty, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Permit Type, ");
					log.warn(e.getMessage());
				}

				String permitNumber = "";
				try {
					permitNumber = (String) getCellValue(row.getCell(3));
					vehiclePermit.setPermitNumber(permitNumber);
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Permit Number, ");
					log.warn(e.getMessage());
				}

				Date date4 = row.getCell(4).getDateCellValue();
				try {
					if (validDate(date4)) {
						vehiclePermit.setIssueDate(dateFormat1.parse(dateFormat1.format(date4)));
					} else {
						error = true;
						lineError.append("Invalid Effective date, ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
					lineError.append("Invalid Effective Date, ");
					log.warn(e.getMessage());
				}

				Date date5 = row.getCell(5).getDateCellValue();
				try {
					if (validDate(date5)) {
						vehiclePermit.setExpirationDate(dateFormat1.parse(dateFormat1.format(date5)));
					} else {
						error = true;
						lineError.append("Invalid Expiration Date");
					}
				} catch (Exception e) {
					error = true;
					e.printStackTrace();
					lineError.append("Invalid Expiration Date, ");
					log.warn(e.getMessage());
				}

				if (!error) {

					java.sql.Date issueDate = new java.sql.Date(vehiclePermit.getIssueDate().getTime());
					java.sql.Date expirationDate = new java.sql.Date(vehiclePermit.getExpirationDate().getTime());

					criterias.clear();
					criterias.put("dataType", "PERMIT_TYPE");
					criterias.put("dataLabel", permitType);
					SetupData setupData = genericDAO.getByCriteria(SetupData.class, criterias);
					String query;
					try {
						query = "select obj from VehiclePermit obj " + " where obj.vehicle.unitNum='" + unitNum + "'"
								+ " and obj.companyLocation.name='" + company + "' "
								+ " and obj.companyLocation.type=3 " + " and obj.permitType=" + setupData.getId() + " "
								+ " and (('" + issueDate + " 00:00:00' BETWEEN obj.issueDate and obj.expirationDate)"
								+ " or ('" + expirationDate + " 00:00:00' BETWEEN obj.issueDate and obj.expirationDate)"
								+ " or obj.issueDate>='" + issueDate + " 00:00:00' AND obj.expirationDate<='"
								+ expirationDate + " 00:00:00')";
						// +" and obj.issueDate='"+dateFormat1.format(date4)+"'
						// "
						// +" and
						// obj.expirationDate='"+dateFormat1.format(date5)+"'";

						List<VehiclePermit> duplicateCheck = genericDAO.executeSimpleQuery(query);
						if (duplicateCheck != null && duplicateCheck.size() > 0) {
							lineError.append("Vehicle Permit Already exists for specified date range, ");
							errorcount++;
						}
						/*
						 * else{
						 * 
						 * genericDAO.save(vehiclePermit); }
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}

					vehiclePermits.add(vehiclePermit);

				} else {
					errorcount++;
				}
				if (lineError.length() > 0) {
					list.add("Line " + count + ":" + lineError.toString() + "<br/>");
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			list.add("Not able to upload the file, please try again");
		}

		if (errorcount == 0) {
			for (VehiclePermit vPermit : vehiclePermits) {
				genericDAO.save(vPermit);
			}
		}
		return list;
	}

	@Override
	public List<LinkedList<Object>> importVendorSpecificFuelLog(InputStream is,
			LinkedHashMap<String, String> vendorSpecificColumns, Long vendor) throws Exception {
		List<LinkedList<Object>> data = new ArrayList<LinkedList<Object>>();
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);

			HSSFWorkbook wb = new HSSFWorkbook(fs);
			Sheet sheet = wb.getSheetAt(0);
			Row titleRow = sheet.getRow(sheet.getFirstRowNum());

			LinkedHashMap<String, Integer> orderedColIndexes = getOrderedColumnIndexes(titleRow, vendorSpecificColumns);
			Set<Entry<String, Integer>> keySet = orderedColIndexes.entrySet();
			
			System.out.println("Physical number of rows in Excel = " + sheet.getPhysicalNumberOfRows());
			System.out.println("While reading values from vendor specific Excel Sheet: ");

			Map criterias = new HashMap();
			criterias.put("id", vendor);
			FuelVendor fuelVendor = genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false).get(0);
			
			boolean stopParsing = false;
			for (int i = titleRow.getRowNum() + 1; !stopParsing && i <= sheet.getPhysicalNumberOfRows() - 1; i++) {
				LinkedList<Object> rowObjects = new LinkedList<Object>();
				
				rowObjects.add(fuelVendor.getName());
				rowObjects.add(fuelVendor.getCompany().getName());
				
				Row row = sheet.getRow(i);

				Iterator<Entry<String, Integer>> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					Entry<String, Integer> entry = iterator.next();
					
					if (entry.getValue() == -1) {
						// corresponding column not found
						rowObjects.add(StringUtils.EMPTY); 
						continue;
					}
					
					Object cellValueObj = getCellValue((HSSFCell) row.getCell(entry.getValue()), true);
					
					if (cellValueObj != null && cellValueObj.toString().equalsIgnoreCase("END_OF_DATA")) {
						System.out.println("Received END_OF_DATA");
						stopParsing = true;
						rowObjects.clear();
						break;
					}
					
					if (cellValueObj != null) {
						System.out.println("Adding " + cellValueObj.toString());
					} else {
						System.out.println("Adding NULL");
					}
					rowObjects.add(cellValueObj);
				}

				if (!stopParsing) {
					data.add(rowObjects);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return data;
	}

	private LinkedHashMap<String, Integer> getOrderedColumnIndexes(Row titleRow,
			LinkedHashMap<String, String> vendorSpecificColumns) {

		LinkedHashMap<String, Integer> orderedColumnIndexesMap = new LinkedHashMap<String, Integer>();

		int startCellNumber = titleRow.getFirstCellNum();

		Set<Entry<String, String>> keySet = vendorSpecificColumns.entrySet();
		Iterator<Entry<String, String>> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			
			if (StringUtils.isEmpty(entry.getValue())) {
				orderedColumnIndexesMap.put(entry.getKey(), -1);
			}
			
			boolean foundExpectedColumn = false;
			for (int i = startCellNumber; i < titleRow.getLastCellNum(); i++) {
				String columnHeader = (String) getCellValue(((HSSFCell) titleRow.getCell(i)));
				if (columnHeader.trim().equalsIgnoreCase(entry.getValue().trim())) {
					// match found
					foundExpectedColumn = true;
					// orderedColumnIndexes.add(i);
					orderedColumnIndexesMap.put(entry.getKey(), i);

					System.out.println(
							"Column Index Mapping for " + entry.getKey() + " = " + columnHeader + " @ index = " + i);
					break;
				}
			}
			if (!foundExpectedColumn) {
				// throw error??
				System.out.println("Could not find expected column " + entry.getValue());
				orderedColumnIndexesMap.put(entry.getKey(), -1);
			}
		}

		System.out.println("Ordered Column Indexes Map = " + orderedColumnIndexesMap);
		return orderedColumnIndexesMap;
	}

}
