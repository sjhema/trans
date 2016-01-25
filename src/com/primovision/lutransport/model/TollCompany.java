package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="tollcompany")
public class TollCompany extends AbstractBaseModel{
	
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
	
	@NotNull
	@Column(name="zipcode")
	private Integer zipcode;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="fax")
	private String fax;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	/*@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;	
	*/

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}
	
	public String getName() {
		return name;
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

	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
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

	/*public Location getTransferStation() {
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
	}*/

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
