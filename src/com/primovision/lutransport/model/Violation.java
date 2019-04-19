package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="violation")
public class Violation extends AbstractBaseModel {
	@Column(name="incident_date")
	private Date incidentDate;
	
	@Column(name="citation_no")
	private String citationNo;
	
	@ManyToOne
	@JoinColumn(name="truck")
	private Vehicle truck;
	
	@ManyToOne
	@JoinColumn(name="trailer")
	private Vehicle trailer;
	
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="driver")
	private Driver driver;
	
	@Column(name="violation_type")
	private String violationType;
	
	@Column(name="out_of_service")
	private String outOfService;
	
	@ManyToOne
	@JoinColumn(name="roadside_inspection")
	private RoadsideInspection roadsideInspection;

	public Vehicle getTruck() {
		return truck;
	}

	public void setTruck(Vehicle truck) {
		this.truck = truck;
	}

	public Vehicle getTrailer() {
		return trailer;
	}

	public void setTrailer(Vehicle trailer) {
		this.trailer = trailer;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getCitationNo() {
		return citationNo;
	}

	public void setCitationNo(String citationNo) {
		this.citationNo = citationNo;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getViolationType() {
		return violationType;
	}

	public void setViolationType(String violationType) {
		this.violationType = violationType;
	}

	public RoadsideInspection getRoadsideInspection() {
		return roadsideInspection;
	}

	public void setRoadsideInspection(RoadsideInspection roadsideInspection) {
		this.roadsideInspection = roadsideInspection;
	}

	public String getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}
}
