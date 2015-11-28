package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="charge_formula")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="formula_type")
public class ChargeFormula extends AbstractBaseModel {
	
	@NotEmpty(message="Formula name is required field.")
	@Column(name="formula_name")
	private String formulaName;
	
	@Column(name="formula_type", insertable=false, updatable=false)
	private Integer formulaType;

	public String getFormulaName() {
		return formulaName;
	}

	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}

	public Integer getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(Integer formulaType) {
		this.formulaType = formulaType;
	}
}
