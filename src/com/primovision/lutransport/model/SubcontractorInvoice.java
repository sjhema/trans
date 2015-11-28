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
@Table(name="subcontractor_invoices")
public class SubcontractorInvoice extends AbstractBaseModel {
	
	//*********************************************//
	@NotNull
	@Column(name = "subcontractvoucherdate")
	private Date voucherDate;
	
	@NotEmpty
	@Column(name="voucher_number")
	private String voucherNumber;
		
	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;
	
	@ManyToOne
	@JoinColumn(name="subcontractor_id")
	private SubContractor subContractorId;
	
	@ManyToOne
	@JoinColumn(name="company_location")
	private Location companyLocationId;
	
	
	@ManyToOne
	@JoinColumn(name="driver_companyLoc")
	private Location driverCompanyId;
	
	
	//***********************************//
	 
	public Location getCompanyLocationId() {
		return companyLocationId;
	}

	public void setCompanyLocationId(Location companyLocationId) {
		this.companyLocationId = companyLocationId;
	}
	
	
	public Location getDriverCompanyId() {
		return driverCompanyId;
	}
	
	
	public void setDriverCompanyId(Location driverCompanyId) {
		this.driverCompanyId = driverCompanyId;
	}

	@Column(name="sum_bill_ton")
	private Double sumBillableTon=0.0;
	
	
	@Column(name="sum_origin_ton")
	private Double sumOriginTon=0.0;
	
	
	@Column(name="sum_destination_ton")
	private Double sumDestinationTon=0.0;
	
	
	@Column(name="sum_amount")
	private Double sumAmount=0.0;
	
	
	@Column(name="sum_fuel_surcharge")
	private Double sumFuelSurcharge=0.0;
	
	
	@Column(name="sum_total")
	private Double sumTotal=0.0;
	
	
	@Column(name="sum_other_charges")
	private Double sumOtherCharges=0.0;
	
	
	@Column(name="grand_total")
	private Double grandTotal=0.0;
	
	@Column(name="miscelleneous_note")
	private String miscelleneousNote;
	
	@Column(name="miscelleneous_charges")
	private String miscelleneousCharges;
	
	//**********************************//
	@Column(name="subcontractor_name")
	private String subcontractorname;
	
	@Column(name="address_line1")
	private String address1;
	
	@Column(name="address_line2")
	private String address2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="state_name")
	private String state;
	
	/*@Column(name="company_name")
	private String companyname;*/
	
	//***********************************//

	public Date getVoucherDate() {
		return voucherDate;
	}

	/*public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}*/

	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
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

	public SubContractor getSubContractorId() {
		return subContractorId;
	}

	public void setSubContractorId(SubContractor subContractorId) {
		this.subContractorId = subContractorId;
	}

	public Double getSumBillableTon() {
		return sumBillableTon;
	}

	public void setSumBillableTon(Double sumBillableTon) {
		this.sumBillableTon = sumBillableTon;
	}

	public Double getSumOriginTon() {
		return sumOriginTon;
	}

	public void setSumOriginTon(Double sumOriginTon) {
		this.sumOriginTon = sumOriginTon;
	}

	public Double getSumDestinationTon() {
		return sumDestinationTon;
	}

	public void setSumDestinationTon(Double sumDestinationTon) {
		this.sumDestinationTon = sumDestinationTon;
	}

	public Double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Double getSumFuelSurcharge() {
		return sumFuelSurcharge;
	}

	public void setSumFuelSurcharge(Double sumFuelSurcharge) {
		this.sumFuelSurcharge = sumFuelSurcharge;
	}

	public Double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(Double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public Double getSumOtherCharges() {
		return sumOtherCharges;
	}

	public void setSumOtherCharges(Double sumOtherCharges) {
		this.sumOtherCharges = sumOtherCharges;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getMiscelleneousNote() {
		return miscelleneousNote;
	}

	public void setMiscelleneousNote(String miscelleneousNote) {
		this.miscelleneousNote = miscelleneousNote;
	}

	public String getMiscelleneousCharges() {
		return miscelleneousCharges;
	}

	public void setMiscelleneousCharges(String miscelleneousCharges) {
		this.miscelleneousCharges = miscelleneousCharges;
	}

	public String getSubcontractorname() {
		return subcontractorname;
	}

	public void setSubcontractorname(String subcontractorname) {
		this.subcontractorname = subcontractorname;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
