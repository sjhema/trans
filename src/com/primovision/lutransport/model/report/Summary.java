package com.primovision.lutransport.model.report;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.AbstractBaseModel;

public class Summary extends AbstractBaseModel {
	String origin="";
	String company="";
	String destination="";
	Integer count=0;
	Double amount=0.0;
	Double billableTons = 0.0;
	Integer countTrucks = 0;
	Double netAmount=0.0;
	Double fuelSurcharge=0.0;
	Double driverPay=0.0;
	
	/*// Truck driver report
	String truckDriverInfo = StringUtils.EMPTY;*/
	
	// Truck driver report
	String truck = StringUtils.EMPTY;
	String driver = StringUtils.EMPTY;
	
	public String getTruck() {
		return truck;
	}
	public void setTruck(String truck) {
		this.truck = truck;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	/*
	public String getTruckDriverInfo() {
		return truckDriverInfo;
	}
	public void setTruckDriverInfo(String truckDriverInfo) {
		this.truckDriverInfo = truckDriverInfo;
	}*/
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getFuelSurcharge() {
		return fuelSurcharge;
	}
	public void setFuelSurcharge(Double fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	public Double getDriverPay() {
		return driverPay;
	}
	public void setDriverPay(Double driverPay) {
		this.driverPay = driverPay;
	}
	public Integer getCountTrucks() {
		return countTrucks;
	}
	public void setCountTrucks(Integer countTrucks) {
		this.countTrucks = countTrucks;
	}
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
