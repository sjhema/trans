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
@Table(name="billing_rate")
public class BillingRate extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name="company_location")
	private Location companyLocation;
	
	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;
	
	@ManyToOne
	@JoinColumn(name="fuelpadd")
	private FuelSurchargePadd padd;
	
	@ManyToOne
	@JoinColumn(name="tonnage_premium")
	private TonnagePremium tonnagePremium;
	
	@ManyToOne
	@JoinColumn(name="customer_name")
	private Customer customername;
	
	@Column(name="peg")
	private Double peg;
	
	@Column(name="miles")
	private Double miles;
	
	@Column(name="surcharge_type")
	private String fuelSurchargeType;
	
	@Column(name="surcharge_amount")
	private Double surchargeAmount;
	
	
	
	@Column(name="bill_using")
	private Integer  billUsing;
	
	@Column(name="sort_by")
	private Integer  sortBy;
	
	
	@ManyToOne
	@JoinColumn(name="demmurage_charge")	
	private DemurrageCharges demmurageCharge;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name="rate_type")
	private Integer rateType;
	
	@NotNull
	@Column(name="rvalue")
	private Double value;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "surcharge_per_ton")
	private Double surchargePerTon;
	
	@Column(name="fuel_surcharge_amount")
	private Double fuelSurchargeAmount;
	
	@Column(name="fuel_surcharge_weeklyrate")
	private Byte fuelsurchargeweeklyRate  = 0;
	
	
	@Column(name="min_billable_gross_weight")
	private Double minbillablegrossWeight;
	
	@Column(name="min_billable_tons")
	private Double minBillableTons;
	
	@Column(name="padd_using")
	private Integer  paddUsing;
	
	@Column(name="weekly_rate_using")
	private Integer  weeklyRateUsing;
	
	@Column(name="rate_using")
	private Integer rateUsing;

	@Column(name="billed_by")
	private String billedby;
	
	// Peak rate 2nd Feb 2021
	@Column(name="peak_rate")
	private Double peakRate;
	
	// Peak rate 2nd Feb 2021
	@Column(name="peak_rate_valid_from")
	private String peakRateValidFrom;
	
	// Peak rate 2nd Feb 2021
	@Column(name="peak_rate_valid_to")
	private String peakRateValidTo;
	

	@Transient
	    private String ratestatus;
	
	@Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;

	

public Double getMinBillableTons() {
		return minBillableTons;
	}

	public void setMinBillableTons(Double minBillableTons) {
		this.minBillableTons = minBillableTons;
	}

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

	public String getBilledby() {
		return billedby;
	}

	public void setBilledby(String billedby) {
		this.billedby = billedby;
	}
	
	
	public Customer getCustomername() {
		return customername;
	}

	public void setCustomername(Customer customername) {
		this.customername = customername;
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

	/*public Date getValidFrom() {
		return validFrom;
	}*/

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

	public FuelSurchargePadd getPadd() {
		return padd;
	}

	public void setPadd(FuelSurchargePadd padd) {
		this.padd = padd;
	}
	public Double getPeg() {
		return peg;
	}

	public void setPeg(Double peg) {
		this.peg = peg;
	}

	public Double getMiles() {
		return miles;
	}

	public void setMiles(Double miles) {
		this.miles = miles;
	}

	public String getFuelSurchargeType() {
		return fuelSurchargeType;
	}

	public void setFuelSurchargeType(String fuelSurchargeType) {
		this.fuelSurchargeType = fuelSurchargeType;
	}

	public Double getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(Double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	

	public Integer getBillUsing() {
		return billUsing;
	}

	public void setBillUsing(Integer billUsing) {
		this.billUsing = billUsing;
	}

	public DemurrageCharges getDemmurageCharge() {
		return demmurageCharge;
	}

	

	public void setDemmurageCharge(DemurrageCharges demmurageCharge) {
		this.demmurageCharge = demmurageCharge;
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

	public TonnagePremium getTonnagePremium() {
		return tonnagePremium;
	}

	public void setTonnagePremium(TonnagePremium tonnagePremium) {
		this.tonnagePremium = tonnagePremium;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Double getSurchargePerTon() {
		return surchargePerTon;
	}

	public void setSurchargePerTon(Double surchargePerTon) {
		this.surchargePerTon = surchargePerTon;
	}

	

	public Double getFuelSurchargeAmount() {
		return fuelSurchargeAmount;
	}

	public void setFuelSurchargeAmount(Double fuelSurchargeAmount) {
		this.fuelSurchargeAmount = fuelSurchargeAmount;
	}

	public void setFuelsurchargeweeklyRate(Byte fuelsurchargeweeklyRate) {
		this.fuelsurchargeweeklyRate = fuelsurchargeweeklyRate;
	}

	public Byte getFuelsurchargeweeklyRate() {
		return fuelsurchargeweeklyRate;
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

	public void setPaddUsing(Integer paddUsing) {
		this.paddUsing = paddUsing;
	}

	public Integer getPaddUsing() {
		return paddUsing;
	}

	public Integer getWeeklyRateUsing() {
		return weeklyRateUsing;
	}

	public void setWeeklyRateUsing(Integer weeklyRateUsing) {
		this.weeklyRateUsing = weeklyRateUsing;
	}

	// Peak rate 2nd Feb 2021
	public Double getPeakRate() {
		return peakRate;
	}

	// Peak rate 2nd Feb 2021
	public void setPeakRate(Double peakRate) {
		this.peakRate = peakRate;
	}

	// Peak rate 2nd Feb 2021
	public String getPeakRateValidFrom() {
		return peakRateValidFrom;
	}

	// Peak rate 2nd Feb 2021
	public void setPeakRateValidFrom(String peakRateValidFrom) {
		this.peakRateValidFrom = peakRateValidFrom;
	}

	// Peak rate 2nd Feb 2021
	public String getPeakRateValidTo() {
		return peakRateValidTo;
	}

	// Peak rate 2nd Feb 2021
	public void setPeakRateValidTo(String peakRateValidTo) {
		this.peakRateValidTo = peakRateValidTo;
	}
}
