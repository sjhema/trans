package com.primovision.lutransport.model.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
@Entity
@Table(name="empbonustypelist")
public class EmpBonusTypesList extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name="bonus_type")
	private BonusType bonusType;
	
	@Column(name="bonus_amount")
	private Double bonusamount;
	
	@Column(name="note")
	private String note;
	
	@Column(name="misc_notes")
	protected String miscNotes;
	
	
	@Column(name="misc_amount")
	protected Double misamount;	
	

    @ManyToOne
	@JoinColumn(name="empbonus")
	private EmployeeBonus bonus;
    
	public BonusType getBonusType() {
		return bonusType;
	}
	public void setBonusType(BonusType bonusType) {
		this.bonusType = bonusType;
	}
	public Double getBonusamount() {
		return bonusamount;
	}
	public void setBonusamount(Double bonusamount) {
		this.bonusamount = bonusamount;
	}
	public EmployeeBonus getBonus() {
		return bonus;
	}
	public void setBonus(EmployeeBonus bonus) {
		this.bonus = bonus;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public void setMiscNotes(String miscNotes) {
		this.miscNotes = miscNotes;
	}
	
	public void setMisamount(Double misamount) {
		this.misamount = misamount;
	}
	
	public String getMiscNotes() {
		return miscNotes;
	}
	
	public Double getMisamount() {
		return misamount;
	}

}
