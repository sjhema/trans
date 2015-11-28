package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "net_report_buffer")
public class NetReportBuffer extends AbstractBaseModel
{
	
	@Column(name="trans_company")
	protected String Company;
	
	@Column(name="trans_unloadfrom")
	protected String unloadDateFrom;
	
	@Column(name="trans_unloadto")
	protected String unloadDateTo;

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getUnloadDateFrom() {
		return unloadDateFrom;
	}

	public void setUnloadDateFrom(String unloadDateFrom) {
		this.unloadDateFrom = unloadDateFrom;
	}

	public String getUnloadDateTo() {
		return unloadDateTo;
	}

	public void setUnloadDateTo(String unloadDateTo) {
		this.unloadDateTo = unloadDateTo;
	}
	
	
	
	 
	
	

}
