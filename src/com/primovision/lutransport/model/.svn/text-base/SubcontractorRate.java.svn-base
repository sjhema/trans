
package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="subcontractor_rate")
public class SubcontractorRate extends AbstractBaseModel {

	@ManyToOne
	@JoinColumn(name="company_location")
	private Location companyLocation;
	
	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;
	
	@Column(name="bill_using")
	private Integer  billUsing;
	
	@Column(name="sort_by")
	private Integer  sortBy;
	
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name="rate_type")
	private Integer rateType;
	
	@NotNull
	@Column(name="value")
	private Double value;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name="fuel_surcharge_amount")
	private Double fuelSurchargeAmount=0.00;
	
	@Column(name="min_billable_gross_weight")
	private Double minbillablegrossWeight;
	
	@Column(name="rate_using")
	private Integer rateUsing;
	
	@ManyToOne
	@JoinColumn(name="subcontractor")
	private SubContractor subcontractor;
	
	@Column(name="other_charges")
	private Double otherCharges=0.00;
	
	@Transient
    private String ratestatus;
	
    @Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;

    
               public Date getValidFrom() {
		setValidFromTemp(validFrom);
		return validFrom;
	}


                public Date getValidTo() {	
	    setValidToTemp(validTo);		
		return validTo;
	}
 	
	public Date getValidFromTemp() {		
		return validFromTemp;
	}
	
	public void setValidFromTemp(Date validFromTemp) {
		this.validFromTemp = validFromTemp;
	}
	
	public Date getValidToTemp() {		
		return validToTemp;
	}
	
	public void setValidToTemp(Date validToTemp) {
		this.validToTemp = validToTemp;
	}


	public String getRatestatus() {
		return ratestatus;
	}
	
	public void setRatestatus(String ratestatus) {
		this.ratestatus = ratestatus;
	}

	public Double getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public SubContractor getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(SubContractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	public Integer getRateUsing() {
		return rateUsing;
	}

	public void setRateUsing(Integer rateUsing) {
		this.rateUsing = rateUsing;
	}

	
	public Location getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(Location companyLocation) {
		this.companyLocation = companyLocation;
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

/*	public Date getValidFrom() {
		return validFrom;
	}
*/
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	/*public Date getValidTo() {
		return validTo;
	}*/

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Integer getRateType() {
		return rateType;
	}



	public Integer getBillUsing() {
		return billUsing;
	}

	public void setBillUsing(Integer billUsing) {
		this.billUsing = billUsing;
	}

	

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}


	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	

	public Double getFuelSurchargeAmount() {
		return fuelSurchargeAmount;
	}

	public void setFuelSurchargeAmount(Double fuelSurchargeAmount) {
		this.fuelSurchargeAmount = fuelSurchargeAmount;
	}

	

	public void setSortBy(Integer sortBy) {
		this.sortBy = sortBy;
	}

	public Integer getSortBy() {
		return sortBy;
	}

	public void setMinbillablegrossWeight(Double minbillablegrossWeight) {
		this.minbillablegrossWeight = minbillablegrossWeight;
	}

	public Double getMinbillablegrossWeight() {
		return minbillablegrossWeight;
	}

	
}
