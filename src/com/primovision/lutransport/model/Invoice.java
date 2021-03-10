package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name="invoices")
public class Invoice extends AbstractBaseModel {

	@NotNull
	@Column(name = "invoice_date")
	private Date invoiceDate;
	
	@NotEmpty
	@Column(name="invoice_number")
	private String invoiceNumber;
		
	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStation;
	
	@ManyToOne
	@JoinColumn(name="landfill")
	private Location landfill;
	
	@Column(name="sum_ton")
	private Double sumBillableTon=0.0;
	
	@Column(name="sum_origin_ton")
	private Double sumOriginTon=0.0;
	
	@Column(name="sum_destination_ton")
	private Double sumDestinationTon=0.0;
	
	@Column(name="sum_net")
	private Double sumNet=0.0;
	
	@Column(name="sum_amount")
	private Double sumAmount=0.0;
	
	@Column(name="sum_total")
	private Double sumTotal=0.0;
	
	@Column(name="sum_surcharge")
	private Double sumFuelSurcharge=0.0;
	
	@Column(name="sum_demmurage")
	private Double sumDemmurage=0.0;
	
	@Column(name="sum_tonnage")
	private Double sumTonnage=0.0;
	
	// Loading fee changes 10th Mar 2021
	@Column(name="sum_loading_fee")
	private Double sumLoadingFee = 0.0;
	
	@Column(name="sum_gallon")
	private Double sumGallon=0.0;

	@Column(name="file_path")
	private String filePath;
	
	public Double getSumGallon() {
		return sumGallon;
	}

	public void setSumGallon(Double sumGallon) {
		this.sumGallon = sumGallon;
	}

	@Transient
	private CommonsMultipartFile invoiceFile;
	
	public Location getTransferStation() {
		return transferStation;
	}

	public void setTransferStation(Location transferStation) {
		this.transferStation = transferStation;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Location getLandfill() {
		return landfill;
	}

	public void setLandfill(Location landfill) {
		this.landfill = landfill;
	}

	public void setInvoiceFile(CommonsMultipartFile invoiceFile) {
		this.invoiceFile = invoiceFile;
	}

	public CommonsMultipartFile getInvoiceFile() {
		return invoiceFile;
	}

	public Double getSumNet() {
		return sumNet;
	}

	public void setSumNet(Double sumNet) {
		this.sumNet = sumNet;
	}

	public Double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(Double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public Double getSumFuelSurcharge() {
		return sumFuelSurcharge;
	}

	public void setSumFuelSurcharge(Double sumFuelSurcharge) {
		this.sumFuelSurcharge = sumFuelSurcharge;
	}

	public Double getSumDemmurage() {
		return sumDemmurage;
	}

	public void setSumDemmurage(Double sumDemmurage) {
		this.sumDemmurage = sumDemmurage;
	}

	public Double getSumTonnage() {
		return sumTonnage;
	}

	public void setSumTonnage(Double sumTonnage) {
		this.sumTonnage = sumTonnage;
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

	public Double getSumLoadingFee() {
		return sumLoadingFee;
	}

	public void setSumLoadingFee(Double sumLoadingFee) {
		this.sumLoadingFee = sumLoadingFee;
	}
}
