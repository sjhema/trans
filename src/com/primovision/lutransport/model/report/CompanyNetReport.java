package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.ReportModel;

@Entity
@Table(name="company_net_report")
public class CompanyNetReport extends AbstractBaseModel implements ReportModel {		
		
		@Column(name="employee_company_id")
		protected Long employeeCompanyId;
		
		@Column(name="employee_company_name")
		protected String employeeCompanyName;
		
		@Column(name = "unload_date")
		protected Date unloadDate;
		
		@Column(name="revenue_amount")
		protected Double revenueAmount=0.0;
		
		@Column(name="fuel_amount")
		protected Double fuelAmount=0.0;
		
		@Column(name="subcontractor_amount")
		protected Double subcontractorAmount=0.0;
		
		@Column(name="toll_amount")
		protected Double tollAmount=0.0;
		
		@Column(name="driver_pay_amount")
		protected Double driverPayAmount=0.0;
		
		@Column(name="net_amount")
		protected Double NetAmount=0.0;

		public Long getEmployeeCompanyId() {
			return employeeCompanyId;
		}

		public void setEmployeeCompanyId(Long employeeCompanyId) {
			this.employeeCompanyId = employeeCompanyId;
		}

		public String getEmployeeCompanyName() {
			return employeeCompanyName;
		}
		
		public void setEmployeeCompanyName(String employeeCompanyName) {
			this.employeeCompanyName = employeeCompanyName;
		}

		public Date getUnloadDate() {
			return unloadDate;
		}

		public void setUnloadDate(Date unloadDate) {
			this.unloadDate = unloadDate;
		}

		public Double getRevenueAmount() {
			return revenueAmount;
		}

		public void setRevenueAmount(Double revenueAmount) {
			this.revenueAmount = revenueAmount;
		}

		public Double getFuelAmount() {
			return fuelAmount;
		}

		public void setFuelAmount(Double fuelAmount) {
			this.fuelAmount = fuelAmount;
		}

		public Double getSubcontractorAmount() {
			return subcontractorAmount;
		}

		public void setSubcontractorAmount(Double subcontractorAmount) {
			this.subcontractorAmount = subcontractorAmount;
		}

		public Double getTollAmount() {
			return tollAmount;
		}

		public void setTollAmount(Double tollAmount) {
			this.tollAmount = tollAmount;
		}

		public Double getDriverPayAmount() {
			return driverPayAmount;
		}

		public void setDriverPayAmount(Double driverPayAmount) {
			this.driverPayAmount = driverPayAmount;
		}

		public Double getNetAmount() {
			return NetAmount;
		}

		public void setNetAmount(Double netAmount) {
			NetAmount = netAmount;
		}
		
        
		
		
}
