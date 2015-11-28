package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.hr.SetupData;

/**
 * @author Ravi
 * 
 */

@Entity
@Table(name = "vehicle_permit")
public class VehiclePermit extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name = "vehicle_id")
    protected Vehicle vehicle;
	
	
	@ManyToOne
	@JoinColumn(name = "company_location")
	protected Location companyLocation;
	
	/*@Column(name = "permit_type")
    protected Integer permitType;*/
	
	@ManyToOne
	@JoinColumn(name = "permit_type")
    protected SetupData permitType;
	
	@NotEmpty
    @Column(name = "permit_number")
    protected String permitNumber;
	
	@NotNull
    @Column(name = "issue_date")
    protected Date issueDate;
	
	@NotNull
    @Column(name = "expiration_date")
    protected Date expirationDate;

	
	
	@Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;
	
	
	
	public Date getIssueDate() {
		setValidFromTemp(issueDate);
		return issueDate;
	}
	
	public Date getExpirationDate() {
		setValidToTemp(expirationDate);
		return expirationDate;
	}
	
	
	public Date getValidFromTemp() {
		return validFromTemp;
	}

	public void setValidFromTemp(Date validFromTemp) {
		this.validFromTemp = validFromTemp;
	}

	public Date getValidToTemp() {
		return validToTemp;
	}

	public void setValidToTemp(Date validToTemp) {
		this.validToTemp = validToTemp;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	
	
	
	
	
	
	
	

	/*public Integer getPermitType() {
		return permitType;
	}

	public void setPermitType(Integer permitType) {
		this.permitType = permitType;
	}*/

	public SetupData getPermitType() {
		return permitType;
	}

	public void setPermitType(SetupData permitType) {
		this.permitType = permitType;
	}

	public String getPermitNumber() {
		return permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}

	

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	
	public Location getCompanyLocation() {
		return companyLocation;
	}
	
	public void setCompanyLocation(Location companyLocation) {
		this.companyLocation = companyLocation;
	}
	
}
