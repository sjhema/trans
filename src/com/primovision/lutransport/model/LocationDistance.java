package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "location_distance")
public class LocationDistance extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="origin")
	private Location origin;
	
	@ManyToOne
	@JoinColumn(name="destination")
	private Location destination;

	@ManyToOne
	@JoinColumn(name="origin_state")
	private State originState;
	
	@ManyToOne
	@JoinColumn(name="destn_state")
	private State destnState;
	
	@Column(name="ny_miles")
	private Double nyMiles;
	
	@Column(name="nj_miles")
	private Double njMiles;

	@Column(name="pa_miles")
	private Double paMiles;
	
	@Column(name="md_miles")
	private Double mdMiles;
	
	@Column(name="va_miles")
	private Double vaMiles;
	
	@Column(name="de_miles")
	private Double deMiles;
	
	@Column(name="wv_miles")
	private Double wvMiles;
	
	@Column(name="dc_miles")
	private Double dcMiles;
	
	@Column(name="il_miles")
	private Double ilMiles;
	
	@Column(name="fl_miles")
	private Double flMiles;
	
	@Column(name="ct_miles")
	private Double ctMiles;
	
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@NotNull
	@Column(name="miles")
	private Double miles;

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Double getMiles() {
		return miles;
	}

	public void setMiles(Double miles) {
		this.miles = miles;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public State getOriginState() {
		return originState;
	}

	public void setOriginState(State originState) {
		this.originState = originState;
	}

	public State getDestnState() {
		return destnState;
	}

	public void setDestnState(State destnState) {
		this.destnState = destnState;
	}

	public Double getNyMiles() {
		return nyMiles;
	}

	public void setNyMiles(Double nyMiles) {
		this.nyMiles = nyMiles;
	}

	public Double getNjMiles() {
		return njMiles;
	}

	public void setNjMiles(Double njMiles) {
		this.njMiles = njMiles;
	}

	public Double getPaMiles() {
		return paMiles;
	}

	public void setPaMiles(Double paMiles) {
		this.paMiles = paMiles;
	}

	public Double getMdMiles() {
		return mdMiles;
	}

	public void setMdMiles(Double mdMiles) {
		this.mdMiles = mdMiles;
	}

	public Double getVaMiles() {
		return vaMiles;
	}

	public void setVaMiles(Double vaMiles) {
		this.vaMiles = vaMiles;
	}

	public Double getDeMiles() {
		return deMiles;
	}

	public void setDeMiles(Double deMiles) {
		this.deMiles = deMiles;
	}

	public Double getWvMiles() {
		return wvMiles;
	}

	public void setWvMiles(Double wvMiles) {
		this.wvMiles = wvMiles;
	}

	public Double getDcMiles() {
		return dcMiles;
	}

	public void setDcMiles(Double dcMiles) {
		this.dcMiles = dcMiles;
	}

	public Double getIlMiles() {
		return ilMiles;
	}

	public void setIlMiles(Double ilMiles) {
		this.ilMiles = ilMiles;
	}

	public Double getFlMiles() {
		return flMiles;
	}

	public void setFlMiles(Double flMiles) {
		this.flMiles = flMiles;
	}

	public Double getCtMiles() {
		return ctMiles;
	}

	public void setCtMiles(Double ctMiles) {
		this.ctMiles = ctMiles;
	}
}
