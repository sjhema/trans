package com.primovision.lutransport.model.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.ReportModel;


@Entity
@Table(name="netreport_autoupdate_trigger_table")
public class NetReportAutoUpdateTriggerTable extends AbstractBaseModel implements ReportModel {

	
	@Column(name="from_date")
	private String fromDate ;
	
	@Column(name="to_date")
	private String toDate ;

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
	
	
}
