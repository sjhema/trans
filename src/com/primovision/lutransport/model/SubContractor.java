package com.primovision.lutransport.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
	@JoinColumn(name="company_1")
	private Location company1;
	
	@ManyToOne
	@JoinColumn(name = "terminal_1")
	private Location terminal1;
	
	@ManyToOne
	@JoinColumn(name="company_2")
	private Location company2;
	
	@ManyToOne
	@JoinColumn(name = "terminal_2")
	private Location terminal2;
	
	@ManyToOne
	@JoinColumn(name="company_3")
	private Location company3;
	
	@ManyToOne
	@JoinColumn(name = "terminal_3")
	private Location terminal3;

	
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
	
	@Column(name="owner_op")
	private String ownerOp;


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

	public Location getCompany1() {
		return company1;
	}

	public void setCompany1(Location company1) {
		this.company1 = company1;
	}

	public Location getTerminal1() {
		return terminal1;
	}

	public void setTerminal1(Location terminal1) {
		this.terminal1 = terminal1;
	}

	public Location getCompany2() {
		return company2;
	}

	public void setCompany2(Location company2) {
		this.company2 = company2;
	}

	public Location getTerminal2() {
		return terminal2;
	}

	public void setTerminal2(Location terminal2) {
		this.terminal2 = terminal2;
	}

	public Location getCompany3() {
		return company3;
	}

	public void setCompany3(Location company3) {
		this.company3 = company3;
	}

	public Location getTerminal3() {
		return terminal3;
	}

	public void setTerminal3(Location terminal3) {
		this.terminal3 = terminal3;
	}
	
	@Transient
	public List<Location> getCompanies() {
		List<Location> companies = new ArrayList<Location>();
		if (getCompany1() != null) {
			companies.add(getCompany1());
		}
		if (getCompany2() != null) {
			companies.add(getCompany2());
		}
		if (getCompany3() != null) {
			companies.add(getCompany3());
		}
		
		return companies;
	}
	@Transient
	public List<Location> getTerminals() {
		List<Location> terminals = new ArrayList<Location>();
		if (getTerminal1() != null) {
			terminals.add(getTerminal1());
		}
		if (getTerminal2() != null) {
			terminals.add(getTerminal2());
		}
		if (getTerminal3() != null) {
			terminals.add(getTerminal3());
		}
		
		return terminals;
	}

	public String getOwnerOp() {
		return ownerOp;
	}

	public void setOwnerOp(String ownerOp) {
		this.ownerOp = ownerOp;
	}
}
