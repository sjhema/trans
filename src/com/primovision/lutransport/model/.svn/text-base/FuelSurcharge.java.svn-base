/**
 * 
 */
package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Amit
 * 
 */
@Entity
@Table(name = "fuel_surcharge")
public class FuelSurcharge extends AbstractBaseModel {

    private static final long serialVersionUID = 1807241954265797561L;

    @ManyToOne
	@JoinColumn(name="from_place")
	protected Location fromPlace;
    
    @ManyToOne
    @JoinColumn(name = "to_place")
    protected Location toPlace;

    @Column(name = "fuel_price")
    protected Double fuelPrice;
    
    @Column(name = "surcharge_escalator")
    protected Double surchargeEscalator;
    
    @Column(name = "trucking_charge")
    protected Double truckingCharge;

    @Column(name = "distance_miles")
    protected Double distance;

  
	public Location getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(Location fromPlace) {
		this.fromPlace = fromPlace;
	}

	public Location getToPlace() {
		return toPlace;
	}

	public void setToPlace(Location toPlace) {
		this.toPlace = toPlace;
	}

	public Double getFuelPrice() {
		return fuelPrice;
	}

	public void setFuelPrice(Double fuelPrice) {
		this.fuelPrice = fuelPrice;
	}

	public Double getSurchargeEscalator() {
		return surchargeEscalator;
	}

	public void setSurchargeEscalator(Double surchargeEscalator) {
		this.surchargeEscalator = surchargeEscalator;
	}

	public Double getTruckingCharge() {
		return truckingCharge;
	}

	public void setTruckingCharge(Double truckingCharge) {
		this.truckingCharge = truckingCharge;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}


}
