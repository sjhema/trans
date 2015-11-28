package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="trip_rate")
public class TripRate  extends AbstractBaseModel {

	@ManyToOne
	@JoinColumn(name = "transfer_station")
    protected Location transferStation;

	@ManyToOne
	@JoinColumn(name = "landfill")
    protected Location landfill;

	@NotNull
    @Column(name = "rate")
    protected Double rate;

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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
}
