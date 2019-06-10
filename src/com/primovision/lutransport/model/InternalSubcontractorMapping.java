package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="internal_subcon_mapping")
public class InternalSubcontractorMapping extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name = "destination")
	private Location destination;
	
	@ManyToOne
	@JoinColumn(name = "origin")
	private Location origin;
	
	@ManyToOne
	@JoinColumn(name = "driver_company")
	private Location driverCompany;
	
	@ManyToOne
	@JoinColumn(name = "bill_company")
	private Location billingCompany;
	
	@ManyToOne
	@JoinColumn(name = "subcontractor")
	private SubContractor subcontractor;

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDriverCompany() {
		return driverCompany;
	}

	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}

	public Location getBillingCompany() {
		return billingCompany;
	}

	public void setBillingCompany(Location billingCompany) {
		this.billingCompany = billingCompany;
	}

	public SubContractor getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(SubContractor subcontractor) {
		this.subcontractor = subcontractor;
	}
}
