package com.primovision.lutransport.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;

public class UpdateDataServiceImpl {

	private static Logger log = Logger.getLogger(UpdateDataServiceImpl.class.getName());
	@PersistenceContext
	protected EntityManager entityManager;

	private ApplicationContext applicationContext;
	
	private String[] getConfigLocations() {
		return new String[] { "classpath*:/**/applicationContext-*.xml" };
	}
	
	public void updateCustomer() throws Exception {
		try {
			DataSource dataSource = (DataSource)applicationContext.getBean("dataSource");
			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			StringBuilder builder = new StringBuilder(); 
			ResultSet rs = stmt.executeQuery("select * from ticket");
			String updateQuery = "update ticket set tranfer_time_in=?, tranfer_time_out=?, landfill_time_in=?, landfill_time_out=? where id=?";
			PreparedStatement ps = conn.prepareStatement(updateQuery);
			while (rs.next()) {
				String transferTimeIn = rs.getString("tranfer_time_in");
				String landfillTimeIn = rs.getString("landfill_time_in");
				String transferTimeOut = rs.getString("tranfer_time_out");
				String landfillTimeOut = rs.getString("landfill_time_out");
				if (StringUtils.isEmpty(transferTimeIn))
					transferTimeIn="0000";
				if (StringUtils.isEmpty(transferTimeOut))
					transferTimeOut="0000";
				if (StringUtils.isEmpty(landfillTimeIn))
					landfillTimeIn="0000";
				if (StringUtils.isEmpty(landfillTimeOut))
					landfillTimeOut="0000";
				Long ticketId = rs.getLong("id");
				System.out.println(transferTimeIn+" "+landfillTimeIn+" "+transferTimeOut+" "+landfillTimeOut);
				if (transferTimeIn.indexOf(":")!=-1) {
					continue;
				}
				if (transferTimeIn.indexOf(":")==-1) {
					builder.append(transferTimeIn);
					builder.insert(2, ":");
					transferTimeIn=builder.toString();
					builder.delete(0,builder.length());
				}
				if (transferTimeOut.indexOf(":")==-1) {
					builder.append(transferTimeOut);
					builder.insert(2, ":");
					transferTimeOut = builder.toString();
					builder.delete(0,builder.length());
				}
				if (landfillTimeIn.indexOf(":")==-1) {
					builder.append(landfillTimeIn);
					builder.insert(2, ":");
					landfillTimeIn=builder.toString();
					builder.delete(0,builder.length());
				}
				if (landfillTimeOut.indexOf(":")==-1) {
					builder.append(landfillTimeOut);
					builder.insert(2, ":");
					landfillTimeOut = builder.toString();
					builder.delete(0,builder.length());
				}
				ps.setString(1, transferTimeIn);
				ps.setString(2, transferTimeOut);
				ps.setString(3, landfillTimeIn);
				ps.setString(4, landfillTimeOut);
				ps.setLong(5, ticketId);
				ps.addBatch();
			}
			ps.executeBatch();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Error in import customer :"+e);
		}
	}
	
	
	public void updateTons() throws Exception {
		try {
			Map<Long,String> driverMap = new HashMap<Long,String>();
			Map<Long,String> terminalMap = new HashMap<Long,String>();
			
			GenericDAO genericDAO = (GenericDAO)applicationContext.getBean("genericDAO");
			List<Driver> drivers = genericDAO.findAll(Driver.class);
			for(Driver driver:drivers) {
				driverMap.put(driver.getId(),driver.getFullName());
			}
			Map criterias = new HashMap();
			criterias.put("type", 3);
			List<Location> terminals = genericDAO.findByCriteria(Location.class,criterias);
			for(Location terminal:terminals) {
				terminalMap.put(terminal.getId(),terminal.getName());
			}
			DataSource dataSource = (DataSource)applicationContext.getBean("dataSource");
			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			StringBuilder builder = new StringBuilder();
			stmt.executeUpdate("update invoice_detail SET bill_gross=origin_gross,bill_net=origin_net,bill_tare=origin_tare,bill_tons=origin_tons WHERE bill_tons IS null");
			ResultSet rs = stmt.executeQuery("select t.id, t.transfer_gross, t.transfer_net, t.transfer_tare, t.transfer_ton, t.landfill_gross, t.landfill_net, t.landfill_tare, t.landfill_ton, t.driver, t.terminal from invoice_detail i, ticket t where i.ticket_id = t.id and i.dest_tons is null");
			String updateQuery = "update invoice_detail set origin_gross=?, origin_net=?, origin_tare=?, origin_tons=?,dest_gross=?, dest_net=?, dest_tare=?, dest_tons=?, driver=?, terminal=? where ticket_id=?";
			PreparedStatement ps = conn.prepareStatement(updateQuery);
			int count=1;
			while (rs.next()) {
				Double originGross = rs.getDouble("transfer_gross");
				Double originNet = rs.getDouble("transfer_net");
				Double originTare = rs.getDouble("transfer_tare");
				Double originTons = rs.getDouble("transfer_ton");
				Double destGross = rs.getDouble("landfill_gross");
				Double destNet = rs.getDouble("landfill_net");
				Double destTare = rs.getDouble("landfill_tare");
				Double destTons = rs.getDouble("landfill_ton");
				Long driverId = rs.getLong("driver");
				Long terminal = rs.getLong("terminal");
				Long ticketId = rs.getLong("id");
				ps.setDouble(1, originGross);
				ps.setDouble(2, originNet);
				ps.setDouble(3, originTare);
				ps.setDouble(4, originTons);
				ps.setDouble(5, destGross);
				ps.setDouble(6, destNet);
				ps.setDouble(7, destTare);
				ps.setDouble(8, destTons);
				ps.setString(9, driverMap.get(driverId));
				ps.setString(10, driverMap.get(terminal));
				ps.setLong(11, ticketId);
				ps.addBatch();
				count++;
			}
			ps.executeBatch();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Error in import customer :"+e);
		}
	}
	
	public void initialize() {
		try {
			applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			UpdateDataServiceImpl updateDataServiceImpl = new UpdateDataServiceImpl();
			updateDataServiceImpl.initialize();
			//updateDataServiceImpl.updateCustomer();
			updateDataServiceImpl.updateTons();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn(ex);
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
