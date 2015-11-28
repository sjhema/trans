package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="pay_roll_rate")
public class PayRollRate extends AbstractBaseModel{
	
		/**
	 * 
	 */
	private static final long serialVersionUID = -1723738690969462253L;
		
		@ManyToOne
		@JoinColumn(name="state")
		private Location state;
		
		@ManyToOne
		@JoinColumn(name="origin")
		private Location origin;
		
		@ManyToOne
		@JoinColumn(name="destination")
		private Location destination;
		
		@NotNull
		@Column(name="rate")
		private Double rate;
	
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
		public Double getRate() {
			return rate;
		}
		public void setRate(Double rate) {
			this.rate = rate;
		}
		public void setState(Location state) {
			this.state = state;
		}
		public Location getState() {
			return state;
		}
		
		
		

}
