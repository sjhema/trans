package com.primovision.lutransport.model.hrreport;

import java.util.List;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.TimeSheet;



public class EmployeeWrapper
{
	
	int totalRowCount=0;
	
	
	
	List<Driver> drivers=null;



	public int getTotalRowCount() {
		return totalRowCount;
	}



	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}



	public List<Driver> getDrivers() {
		return drivers;
	}



	public void setDrivers(List<Driver> employees) {
		this.drivers = employees;
	}
	
	
	
	
	
	
    
}
