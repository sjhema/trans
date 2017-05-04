package com.primovision.lutransport.model.report;

import java.util.Iterator;

import com.primovision.lutransport.model.AbstractBaseModel;

public class Summary extends AbstractBaseModel {
	String origin="";
	String company="";
	String destination="";
	Integer count=0;
	Double amount=0.0;
	Double billableTons = 0.0;
	
	public Double getBillableTons() {
		return billableTons;
	}
	public void setBillableTons(Double billableTons) {
		this.billableTons = billableTons;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	

}
