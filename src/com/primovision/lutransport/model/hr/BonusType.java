package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;

/**
 * @author kishor
 *
 */
@Entity
@Table(name="bonus_type")
public class BonusType extends AbstractBaseModel{
	
	@NotEmpty
	@Column(name="name")
	private String typename;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="catagory_id")
	private EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal_id")
	private Location terminal;
	
	@Column(name="experince_rage_From")
	private Integer experinceRageFrom;
	
	@Column(name="experince_rage_to")
	private Integer experinceRageTo;
	
	@NotNull
	@Column(name="amount")
	private Double amount;
	
	@Column(name="maptoticket")
	private Byte mapToTicket=0;
	
	@Column(name="whetherreferal")
	private Byte whetherReferal=0;
	
	@Column(name="date_effectivefrom")
	private Date dateEffectiveFrom;
	
	@Column(name="date_effectiveto")
	private Date dateEffectiveTo;

	

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EmployeeCatagory getCatagory() {
		return catagory;
	}

	public void setCatagory(EmployeeCatagory catagory) {
		this.catagory = catagory;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}

	public Integer getExperinceRageFrom() {
		return experinceRageFrom;
	}

	public void setExperinceRageFrom(Integer experinceRageFrom) {
		this.experinceRageFrom = experinceRageFrom;
	}

	public Integer getExperinceRageTo() {
		return experinceRageTo;
	}

	public void setExperinceRageTo(Integer experinceRageTo) {
		this.experinceRageTo = experinceRageTo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Byte getMapToTicket() {
		return mapToTicket;
	}

	public void setMapToTicket(Byte mapToTicket) {
		this.mapToTicket = mapToTicket;
	}

	public Byte getWhetherReferal() {
		return whetherReferal;
	}

	public void setWhetherReferal(Byte whetherReferal) {
		this.whetherReferal = whetherReferal;
	}

	public Date getDateEffectiveFrom() {
		return dateEffectiveFrom;
	}

	public void setDateEffectiveFrom(Date dateEffectiveFrom) {
		this.dateEffectiveFrom = dateEffectiveFrom;
	}

	public Date getDateEffectiveTo() {
		return dateEffectiveTo;
	}

	public void setDateEffectiveTo(Date dateEffectiveTo) {
		this.dateEffectiveTo = dateEffectiveTo;
	}
	
	

}
