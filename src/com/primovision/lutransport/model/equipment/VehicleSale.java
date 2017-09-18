package com.primovision.lutransport.model.equipment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="vehicle_sale")
public class VehicleSale extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name="buyer")
	private EquipmentBuyer buyer;
	
	@Column(name="sale_date")
	private Date saleDate;
	
	@Column(name="sale_price")
	private Double salePrice;
	
	@Column(name="scrapped")
	private Integer scrapped;
	
	@Transient
	private String buyerName;
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public EquipmentBuyer getBuyer() {
		return buyer;
	}

	public void setBuyer(EquipmentBuyer buyer) {
		this.buyer = buyer;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	@Transient
	public String getBuyerName() {
		return buyerName;
	}

	@Transient
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Integer getScrapped() {
		return scrapped;
	}

	public void setScrapped(Integer scrapped) {
		this.scrapped = scrapped;
	}
}
