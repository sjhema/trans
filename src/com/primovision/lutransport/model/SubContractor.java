package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="subcontractor")
public class SubContractor extends AbstractBaseModel{
	
	@NotEmpty
	@Column(name="name")
	private String name;
	
	@NotEmpty
	@Column(name="address")
	private String address;
	
	@Column(name="address2")
	private String address2;
	
	@NotEmpty
	@Column(name="city")
	private String city;
	
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	@NotEmpty
	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="fax")
	private String fax;
	
	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;	
	
	//----------
	
	@Column(name="date_terminated")
	private Date subdateTerminated;
	
	@NotNull
	@Column(name="date_hired")
	private Date subdateHired;
	
	
	//---------
	



	public String getName() {
		return name;
	}

	public Date getSubdateTerminated() {
		return subdateTerminated;
	}

	public void setSubdateTerminated(Date subdateTerminated) {
		this.subdateTerminated = subdateTerminated;
	}

	public Date getSubdateHired() {
		return subdateHired;
	}

	public void setSubdateHired(Date subdateHired) {
		this.subdateHired = subdateHired;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getZipcode() {
		return zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Location getTransferStation() {
		return transferStation;
	}

	public void setTransferStation(Location transferStation) {
		this.transferStation = transferStation;
	}

	public Location getLandfill() {
		return landfill;
	}

	public void setLandfill(Location landfill) {
		this.landfill = landfill;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
}
