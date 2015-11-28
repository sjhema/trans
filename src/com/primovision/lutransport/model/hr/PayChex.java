package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;


@Entity
@Table(name="paychex")
public class PayChex extends AbstractBaseModel{

	@ManyToOne
	@JoinColumn(name="company_id")
	private Location companyLocation;
	
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminalLocation;
	
	
	private Date checkDate;


	public Location getCompanyLocation() {
		return companyLocation;
	}


	public void setCompanyLocation(Location companyLocation) {
		this.companyLocation = companyLocation;
	}


	public Location getTerminalLocation() {
		return terminalLocation;
	}


	public void setTerminalLocation(Location terminalLocation) {
		this.terminalLocation = terminalLocation;
	}


	public Date getCheckDate() {
		return checkDate;
	}


	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}	
}
