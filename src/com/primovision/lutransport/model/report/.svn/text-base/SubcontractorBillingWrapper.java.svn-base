package com.primovision.lutransport.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;



import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SubContractor;

public class SubcontractorBillingWrapper {
	double sumNet=0.0;
	double sumBillableTon=0.0;
	double sumOriginTon=0.0;
	double sumDestinationTon=0.0;
	double sumAmount=0.0;
	double sumFuelSurcharge=0.0;
	double sumTonnage=0.0;
	double sumDemmurage=0.0;
	double sumTotal=0.0;
	double sumOtherCharges=0.0;
	double grandTotal=0.0;
	int totalRowCount=0;
	
	List<String> list = new ArrayList<String>();
	
	List<SubcontractorBilling> subcontractorBillings=null;
	List<SubcontractorBillingNew> subcontractorBillingsNew=null;
	String subcontractorname;
	String address1;
	String address2;
	String city;
	String zipcode;
	String phone;
	String fax;
	String state;
	String miscelleneousNote;
	String miscelleneousCharges;
	
	@Transient
	@ManyToOne
	private Location companyLocationId;
	/*String company;*/
	
	public Location getCompanyLocationId() {
		return companyLocationId;
	}

	public void setCompanyLocationId(Location companyLocationId) {
		this.companyLocationId = companyLocationId;
	}

	@Transient
	@ManyToOne
	SubContractor subcontratorId;
	
	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public SubContractor getSubcontratorId() {
		return subcontratorId;
	}

	/*public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}*/

	public void setSubcontratorId(SubContractor subcontratorId) {
		this.subcontratorId = subcontratorId;
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

	
	
	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public double getSumOtherCharges() {
		return sumOtherCharges;
	}

	public void setSumOtherCharges(double sumOtherCharges) {
		this.sumOtherCharges = sumOtherCharges;
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


	public double getSumNet() {
		return sumNet;
	}

	public void setSumNet(double sumNet) {
		this.sumNet = sumNet;
	}



	public double getSumBillableTon() {
		return sumBillableTon;
	}

	public void setSumBillableTon(double sumBillableTon) {
		this.sumBillableTon = sumBillableTon;
	}

	public double getSumOriginTon() {
		return sumOriginTon;
	}

	public void setSumOriginTon(double sumOriginTon) {
		this.sumOriginTon = sumOriginTon;
	}

	public double getSumDestinationTon() {
		return sumDestinationTon;
	}

	public void setSumDestinationTon(double sumDestinationTon) {
		this.sumDestinationTon = sumDestinationTon;
	}

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public double getSumFuelSurcharge() {
		return sumFuelSurcharge;
	}

	public void setSumFuelSurcharge(double sumFuelSurcharge) {
		this.sumFuelSurcharge = sumFuelSurcharge;
	}

	public double getSumTonnage() {
		return sumTonnage;
	}

	public void setSumTonnage(double sumTonnage) {
		this.sumTonnage = sumTonnage;
	}

	public double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(double sumTotal) {
		this.sumTotal = sumTotal;
	}

	

	public List<SubcontractorBilling> getSubcontractorBillings() {
		return subcontractorBillings;
	}
	
	public void setSubcontractorBillings(
			List<SubcontractorBilling> subcontractorBillings) {
		this.subcontractorBillings = subcontractorBillings;
	}
	

	public double getSumDemmurage() {
		return sumDemmurage;
	}

	public void setSumDemmurage(double sumDemmurage) {
		this.sumDemmurage = sumDemmurage;
	}
	
	
	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
	
	public int getTotalRowCount() {
		return totalRowCount;
	}
	
	public List<SubcontractorBillingNew> getSubcontractorBillingsNew() {
		return subcontractorBillingsNew;
	}
	
	public void setSubcontractorBillingsNew(
			List<SubcontractorBillingNew> subcontractorBillingsNew) {
		this.subcontractorBillingsNew = subcontractorBillingsNew;
	}
	
	
}

