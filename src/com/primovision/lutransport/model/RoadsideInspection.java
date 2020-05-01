package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="roadside_inspection")
public class RoadsideInspection extends AbstractBaseModel {
	@Column(name="inspection_date")
	private Date inspectionDate;
	
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
	
	@Column(name="inspection_level")
	private String inspectionLevel;
	
	@Column(name="violation")
	private String violation;
	
	@Column(name="citation")
	private String citation;
	
	@Column(name="docs")
	private String docs = "N";
	
	@Transient
	private String isViolation;
	
	@Transient
	private String violationId;
	
	@Transient
	private String citationNo;
	
	@Transient
	private String outOfService;
	
	@Transient
	private String violationType;
	
	@Transient
	String[] fileList;
	
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

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getViolation() {
		return violation;
	}

	public void setViolation(String violation) {
		this.violation = violation;
	}

	public String getCitation() {
		return citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

	public String getInspectionLevel() {
		return inspectionLevel;
	}

	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}

	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	@Transient
	public String getCitationNo() {
		return citationNo;
	}

	@Transient
	public void setCitationNo(String citationNo) {
		this.citationNo = citationNo;
	}

	@Transient
	public String getViolationType() {
		return violationType;
	}

	@Transient
	public void setViolationType(String violationType) {
		this.violationType = violationType;
	}

	@Transient
	public String getOutOfService() {
		return outOfService;
	}

	@Transient
	public void setOutOfService(String outOfService) {
		this.outOfService = outOfService;
	}

	@Transient
	public String getViolationId() {
		return violationId;
	}

	@Transient
	public void setViolationId(String violationId) {
		this.violationId = violationId;
	}

	@Transient
	public String[] getFileList() {
		return fileList;
	}

	@Transient
	public void setFileList(String[] fileList) {
		this.fileList = fileList;
	}

	@Transient
	public String getIsViolation() {
		return isViolation;
	}

	@Transient
	public void setIsViolation(String isViolation) {
		this.isViolation = isViolation;
	}
	
}
