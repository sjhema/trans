package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="terminal")
public class Terminal extends AbstractBaseModel{
	
	@Column(name="homebranch")
	private Integer homeBranch;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	private Location terminal;

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}
	
	public Integer getHomeBranch() {
		return homeBranch;
	}
	
	public void setHomeBranch(Integer homeBranch) {
		this.homeBranch = homeBranch;
	}
	

}
