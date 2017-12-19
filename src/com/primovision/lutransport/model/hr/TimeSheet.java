package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;


/**
 * @author subodh
 * 
 */

@Entity
@Table(name ="timesheet")
public class TimeSheet extends AbstractBaseModel {
	@Transient
	public static String ADD_TO_PREV_MODE = "ADD_TO_PREV";

	private static final long serialVersionUID = 1L;

	@Column(name="seq_num1")
	private Integer sequenceNum1;
	
	@Transient
	private String mode = StringUtils.EMPTY;
	
	@ManyToOne
	@JoinColumn(name="employee")
	protected Driver driver;
	
	@ManyToOne
	@JoinColumn(name="catagory")
	protected EmployeeCatagory catagory;
	
	@ManyToOne
	@JoinColumn(name="company")
	protected Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;
	
	
	@NotNull
	@Column(name="batch_date")
	protected Date batchdate;
	
	//@NotNull
	@Column(name ="daily_hours")
	protected Double dailyHours=0.0;
	
	//@NotNull
	@Column(name ="weekly_hours")
	protected Double weeklyHours=0.0;
	
	@Column(name ="total_hours_worked_inweek")
	protected Double totalhoursworkedInweek;
	
	@Column(name ="total_hours_worked_inweek_rounded")
	protected Double totalhoursworkedInweekRounded;
	
	@Column(name ="hours_worked_inweek_rounded_value")
	protected Double hoursworkedInweekRoundedValue;
	
	@Column(name ="regular_hours")
	protected Double regularhours;
	
	@Column(name ="holiday_hours")
	protected Double holidayhours;
	
	@Column(name ="total_ot_hours_inweek")
	protected Double totalothoursinweek;
	
	/*@Column(name ="total_ot_hours_inweek_rounded")
	protected Double totalothoursinweekRounded;*/
	
	@Column(name ="total_dt_hours_inweek")
	protected Double totaldthoursinweek;
	
	/*@Column(name ="total_dt_hours_inweek_rounded")
	protected Double totaldthoursinweekRounded;*/
	
	@Column(name="hourly_payroll_status")
	protected Integer hourlypayrollstatus=1;
	
	@Column(name="hourly_payroll_invoice_date")
	protected Date hourlypayrollinvoiceDate;
	
	@Column(name="hourly_payroll_invoice_number")
	protected String hourlypayrollinvoiceNumber;
	
	
	
	public Integer getHourlypayrollstatus() {
		return hourlypayrollstatus;
	}

	public void setHourlypayrollstatus(Integer hourlypayrollstatus) {
		this.hourlypayrollstatus = hourlypayrollstatus;
	}

	public Date getHourlypayrollinvoiceDate() {
		return hourlypayrollinvoiceDate;
	}

	public void setHourlypayrollinvoiceDate(Date hourlypayrollinvoiceDate) {
		this.hourlypayrollinvoiceDate = hourlypayrollinvoiceDate;
	}

	public String getHourlypayrollinvoiceNumber() {
		return hourlypayrollinvoiceNumber;
	}

	public void setHourlypayrollinvoiceNumber(String hourlypayrollinvoiceNumber) {
		this.hourlypayrollinvoiceNumber = hourlypayrollinvoiceNumber;
	}

	/**********************/
	
	
	
	
	//@NotEmpty
	@Column(name ="sun_day_name")
	protected String sundayname;
	
	@Column(name ="mon_day_name")
	protected String mondayname;
	
	@Column(name ="tues_day_name")
	protected String tuesdayname;
	
	@Column(name ="wed_day_name")
	protected String wednesdayname;
	
	@Column(name ="thrus_day_name")
	protected String thrusdayname;
	
	@Column(name ="fri_day_name")
	protected String fridayname;
	
	@Column(name ="st_day_name")
	protected String stdayname;
	/*@NotNull*/
	@Column(name="s_date")
	protected Date sdate;

	@Column(name="m_date")
	protected Date mdate;
	
	@Column(name="t_date")
	protected Date tdate;
	
	@Column(name="w_date")
	protected Date wdate;
	
	@Column(name="th_date")
	protected Date thdate;
	
	@Column(name="f_date")
	protected Date fdate;
	
	@Column(name="st_date")
	protected Date stadate;
	
	
	//@NotEmpty
	@Column(name ="s_signin_time")
	protected String ssignintime;
	
	@Column(name ="m_signin_time")
	protected String msignintime;
	
	@Column(name ="t_signin_time")
	protected String tsignintime;
	
	@Column(name ="w_signin_time")
	protected String wsignintime;
	
	@Column(name ="th_signin_time")
	protected String thsignintime;
	
	@Column(name ="fr_signin_time")
	protected String fsignintime;
	
	@Column(name ="st_signin_time")
	protected String stsignintime;
	
	@Column(name ="s_signin_ampm")
	protected String ssigninampm;
	
	@Column(name ="m_signin_ampm")
	protected String msigninampm;
	
	@Column(name ="t_signin_ampm")
	protected String tsigninampm;
	
	@Column(name ="w_signin_ampm")
	protected String wsigninampm;
	
	@Column(name ="th_signin_ampm")
	protected String thsigninampm;
	
	@Column(name ="f_signin_ampm")
	protected String fsigninampm;
	
	@Column(name ="st_signin_ampm")
	protected String stsigninampm;
	
	
	//**************************************
	@Column(name ="s2_signin_time")
	protected String s2signintime;
	
	@Column(name ="m2_signin_time")
	protected String m2signintime;
	
	@Column(name ="t2_signin_time")
	protected String t2signintime;
	
	@Column(name ="w2_signin_time")
	protected String w2signintime;
	
	@Column(name ="th2_signin_time")
	protected String th2signintime;
	
	@Column(name ="fr2_signin_time")
	protected String fs2ignintime;
	
	@Column(name ="st2_signin_time")
	protected String st2signintime;
	
	@Column(name ="s2_signin_ampm")
	protected String s2signinampm;
	
	@Column(name ="m2_signin_ampm")
	protected String m2signinampm;
	
	@Column(name ="t2_signin_ampm")
	protected String t2signinampm;
	
	@Column(name ="w2_signin_ampm")
	protected String w2signinampm;
	
	@Column(name ="th2_signin_ampm")
	protected String th2signinampm;
	
	@Column(name ="f2_signin_ampm")
	protected String f2signinampm;
	
	@Column(name ="st2_signin_ampm")
	protected String st2signinampm;	
	
	//***************************************
	
	
	
	
	
	//@NotEmpty
	@Column(name ="s_signout_time")
	protected String ssignouttime;
	
	@Column(name ="m_signout_time")
	protected String msignouttime;
	
	@Column(name ="t_signout_time")
	protected String tsignouttime;
	
	@Column(name ="w_signout_time")
	protected String w_signouttime;
	
	@Column(name ="th_signout_time")
	protected String thsignouttime;
	
	@Column(name ="f_signout_time")
	protected String fsignouttime;
	
	@Column(name ="st_signout_time")
	protected String st_signouttime;

	
	@Column(name ="s_signout_ampm")
	protected String ssignoutampm;
	
	@Column(name ="m_signout_ampm")
	protected String msignoutampm;
	
	@Column(name ="t_signout_ampm")
	protected String tsignoutampm;
	
	@Column(name ="w_signout_ampm")
	protected String wsignoutampm;
	
	@Column(name ="th_signout_ampm")
	protected String thsignoutampm;
	
	@Column(name ="f_signout_ampm")
	protected String fsignoutampm;
	
	@Column(name ="st_signout_ampm")
	protected String stsignoutampm;
	
	//***************************************
	
	@Column(name ="s2_signout_time")
	protected String s2signouttime;
	
	@Column(name ="m2_signout_time")
	protected String m2signouttime;
	
	@Column(name ="t2_signout_time")
	protected String t2signouttime;
	
	@Column(name ="w2_signout_time")
	protected String w2_signouttime;
	
	@Column(name ="th2_signout_time")
	protected String th2signouttime;
	
	@Column(name ="f2_signout_time")
	protected String f2signouttime;
	
	@Column(name ="st2_signout_time")
	protected String st2_signouttime;

	
	@Column(name ="s2_signout_ampm")
	protected String s2signoutampm;
	
	@Column(name ="m2_signout_ampm")
	protected String m2signoutampm;
	
	@Column(name ="t2_signout_ampm")
	protected String t2signoutampm;
	
	@Column(name ="w2_signout_ampm")
	protected String w2signoutampm;
	
	@Column(name ="th2_signout_ampm")
	protected String th2signoutampm;
	
	@Column(name ="f2_signout_ampm")
	protected String f2signoutampm;
	
	@Column(name ="st2_signout_ampm")
	protected String st2signoutampm;
	
	//***************************************
	
	
	//@NotEmpty
	@Column(name ="s_timestart_hours")
	protected String stimestarthours;
	
	@Column(name ="m_timestart_hours")
	protected String mtimestarthours;
	
	@Column(name ="t_timestart_hours")
	protected String ttimestarthours;
	
	@Column(name ="w_timestart_hours")
	protected String wtimestarthours;
	
	@Column(name ="th_timestart_hours")
	protected String thtimestarthours;
	
	@Column(name ="f_timestart_hours")
	protected String ftimestarthours;
	
	@Column(name ="st_timestart_hours")
	protected String sttimestarthours;
	
	
	
	
    //@NotEmpty
	@Column(name ="s_timestart_minuts")
	protected String stimestartminuts;
	
    @Column(name ="m_timestart_minuts")
	protected String mtimestartminuts;
    
    @Column(name ="t_timestart_minuts")
	protected String ttimestartminuts;
    
    @Column(name ="w_timestart_minuts")
	protected String wtimestartminuts;
    
    @Column(name ="th_timestart_minuts")
	protected String thtimestartminuts;
    
    @Column(name ="fr_timestart_minuts")
	protected String frtimestartminuts;
    
    @Column(name ="st_timestart_minuts")
	protected String sttimestartminuts;
    
    
   //  @NotEmpty
 	@Column(name ="s_timeendin_hours")
 	protected String stimeendinhours;
     
    @Column(name ="m_timeendin_hours")
 	protected String mtimeendinhours;
    
    @Column(name ="t_timeendin_hours")
	protected String ttimeendinhours;
    
    @Column(name ="w_timeendin_hours")
	protected String wtimeendinhours;
    
    @Column(name ="th_timeendin_hours")
	protected String thtimeendinhours;
    
    @Column(name ="fr_timeendin_hours")
	protected String frtimeendinhours;
    
    @Column(name ="st_timeendin_hours")
	protected String sttimeendinhours;
     
     
     
     
	//@NotEmpty
	@Column(name ="s_timeendin_minuts")
	protected String stimeendinminuts;
	
	@Column(name ="m_timeendin_minuts")
	protected String mtimeendinminuts;
	
	@Column(name ="t_timeendin_minuts")
	protected String ttimeendinminuts;
	
	@Column(name ="w_timeendin_minuts")
	protected String wtimeendinminuts;
	
	@Column(name ="th_timeendin_minuts")
	protected String thtimeendinminuts;
	
	@Column(name ="f_timeendin_minuts")
	protected String ftimeendinminuts;
	
	@Column(name ="st_timeendin_minuts")
	protected String sttimeendinminuts;
	
	
	
	
	@Column(name ="s_hours_worked")
	protected Double shoursworked;
	
	@Column(name ="m_hours_worked")
	protected Double mhoursworked;
	
	@Column(name ="t_hours_worked")
	protected Double thoursworked;
	
	@Column(name ="w_hours_worked")
	protected Double whoursworked;
	
	@Column(name ="th_hours_worked")
	protected Double thhoursworked;
	
	@Column(name ="f_hours_worked")
	protected Double fhoursworked;
	
	@Column(name ="st_hours_worked")
	protected Double sthoursworked;
	
	
	
	@Column(name ="m_lunch_hours")
	protected Double mluncHours;
	
	@Column(name ="t_lunch_hours")
	protected Double tluncHours;
	
	@Column(name ="w_lunch_hours")
	protected Double wluncHours;
	
	@Column(name ="th_lunch_hours")
	protected Double thluncHours;
	
	@Column(name ="f_lunch_hours")
	protected Double fluncHours;
	
	@Column(name ="st_lunch_hours")
	protected Double stluncHours;
	
	@Column(name ="s_lunch_hours")
	protected Double sluncHours;
	
	
	
	
	
	
	
	
	
	
	
	@Column(name ="s_over_time_hours")
	protected Double sovertimehours;
	
	@Column(name ="m_over_time_hours")
	protected Double movertimehours;
	
	@Column(name ="t_over_time_hours")
	protected Double tovertimehours;
	
	@Column(name ="w_over_time_hours")
	protected Double wovertimehours;
	
	@Column(name ="th_over_time_hours")
	protected Double thovertimehours;
	
	@Column(name ="f_over_time_hours")
	protected Double fovertimehours;
	
	@Column(name ="st_over_time_hours")
	protected Double stovertimehours;
	
	
	
	
	@Column(name ="s_double_time_hours")
	protected Double sdoubletimehours;
	
	@Column(name ="m_double_time_hours")
	protected Double mdoubletimehours;
	
	@Column(name ="t_double_time_hours")
	protected Double tdoubletimehours;
	
	@Column(name ="w_double_time_hours")
	protected Double wdoubletimehours;
	
	@Column(name ="th_double_time_hours")
	protected Double thdoubletimehours;
	
	@Column(name ="f_double_time_hours")
	protected Double fdoubletimehours;
	
	@Column(name ="st_double_time_hours")
	protected Double stdoubletimehours;
	
	
	
	
	//@NotEmpty
	@Column(name ="s_ot_flag")
	protected String sotflag="NO";
	
	@Column(name ="m_ot_flag")
	protected String mtotflag="NO";
	
	@Column(name ="t_ot_flag")
	protected String totflag="NO";
	
	@Column(name ="w_ot_flag")
	protected String wotflag="NO";
	
	@Column(name ="th_ot_flag")
	protected String thotflag="NO";
	
	@Column(name ="f_ot_flag")
	protected String fotflag="NO";
	
	@Column(name ="st_ot_flag")
	protected String stotflag="NO";
	
	
	
	
	
	@Column(name ="s_dt_flag")
	protected String sdtflag="NO";
	
	@Column(name ="m_dt_flag")
	protected String mdtflag="NO";
	
	@Column(name ="t_dt_flag")
	protected String tdtflag="NO";
	
	@Column(name ="w_dt_flag")
	protected String wdtflag="NO";
	
	@Column(name ="th_dt_flag")
	protected String thdtflag="NO";
	
	@Column(name ="f_dt_flag")
	protected String fdtflag="NO";
	
	@Column(name ="st_dt_flag")
	protected String stdtflag="NO";

	
	
	@Column(name ="s_holiday_flag")
	protected String sholidayflag="NO";
	
	@Column(name ="m_holiday_flag")
	protected String mholidayflag="NO";
	
	
	@Column(name ="t_holiday_flag")
	protected String tholidayflag="NO";
	
	@Column(name ="w_holiday_flag")
	protected String wholidayflag="NO";
	
	@Column(name ="th_holiday_flag")
	protected String thholidayflag="NO";
	
	@Column(name ="f_holiday_flag")
	protected String fholidayflag="NO";
	
	@Column(name ="st_holiday_flag")
	protected String stholidayflag="NO";
	
	
	
	@Transient
	protected String companies;
	@Transient
	protected String terminals;
	@Transient
	protected String employees;
	@Transient
	protected String categories;
	@Transient
	protected String employeesNo;
	
	@Transient
	protected String batchDates;
	
	@Transient
	protected Double regularamounts;
	
	@Transient
	protected Double otamounts;
	
	@Transient
	protected Double dtamounts;
	
	@Transient
	protected Double totamounts;
	
	@Transient
	protected Double rHours;
	
	@Transient
	protected Double hoursworkeds;
	
	@Transient
	protected Double otHours;
	
	@Transient
	protected Double dtHours;
	
	@Transient
	protected Double rRate;
	
	@Transient
	protected Double otRate;
	
	@Transient
	protected Double dtRate;
	
	@Transient
	protected Double vacationAmount;
	
	@Transient
	protected Double sickParsanolAmount;
	
	@Transient
	protected Double bonusAmounts;
	
	@Transient
	protected Double sumOfTotVacSicBonus;
	
	@Transient
	private Double holidayAmount;

	public Double getHoursworkeds() {
		return hoursworkeds;
	}

	public void setHoursworkeds(Double hoursworkeds) {
		this.hoursworkeds = hoursworkeds;
	}

	public Double getVacationAmount() {
		return vacationAmount;
	}

	public void setVacationAmount(Double vacationAmount) {
		this.vacationAmount = vacationAmount;
	}

	public Double getSickParsanolAmount() {
		return sickParsanolAmount;
	}

	public void setSickParsanolAmount(Double sickParsanolAmount) {
		this.sickParsanolAmount = sickParsanolAmount;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
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

	public Date getBatchdate() {
		return batchdate;
	}

	public void setBatchdate(Date batchdate) {
		this.batchdate = batchdate;
	}

	public Double getDailyHours() {
		return dailyHours;
	}

	public void setDailyHours(Double dailyHours) {
		this.dailyHours = dailyHours;
	}

	public Double getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(Double weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	public String getSundayname() {
		return sundayname;
	}

	public void setSundayname(String sundayname) {
		this.sundayname = sundayname;
	}

	public String getMondayname() {
		return mondayname;
	}

	public void setMondayname(String mondayname) {
		this.mondayname = mondayname;
	}

	public String getTuesdayname() {
		return tuesdayname;
	}

	public void setTuesdayname(String tuesdayname) {
		this.tuesdayname = tuesdayname;
	}

	public String getWednesdayname() {
		return wednesdayname;
	}

	public void setWednesdayname(String wednesdayname) {
		this.wednesdayname = wednesdayname;
	}

	public String getThrusdayname() {
		return thrusdayname;
	}

	public void setThrusdayname(String thrusdayname) {
		this.thrusdayname = thrusdayname;
	}

	public String getFridayname() {
		return fridayname;
	}

	public void setFridayname(String fridayname) {
		this.fridayname = fridayname;
	}

	

	public String getStdayname() {
		return stdayname;
	}

	public void setStdayname(String stdayname) {
		this.stdayname = stdayname;
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Date getMdate() {
		return mdate;
	}

	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}

	public Date getTdate() {
		return tdate;
	}

	public void setTdate(Date tdate) {
		this.tdate = tdate;
	}

	public Date getWdate() {
		return wdate;
	}

	public void setWdate(Date wdate) {
		this.wdate = wdate;
	}

	public Date getThdate() {
		return thdate;
	}

	public void setThdate(Date thdate) {
		this.thdate = thdate;
	}

	public Date getFdate() {
		return fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

	public Date getStadate() {
		return stadate;
	}

	public void setStadate(Date stadate) {
		this.stadate = stadate;
	}

	public String getSsignintime() {
		return ssignintime;
	}

	public void setSsignintime(String ssignintime) {
		this.ssignintime = ssignintime;
	}

	public String getMsignintime() {
		return msignintime;
	}

	public void setMsignintime(String msignintime) {
		this.msignintime = msignintime;
	}

	public String getTsignintime() {
		return tsignintime;
	}

	public void setTsignintime(String tsignintime) {
		this.tsignintime = tsignintime;
	}

	public String getWsignintime() {
		return wsignintime;
	}

	public void setWsignintime(String wsignintime) {
		this.wsignintime = wsignintime;
	}

	public String getThsignintime() {
		return thsignintime;
	}

	public void setThsignintime(String thsignintime) {
		this.thsignintime = thsignintime;
	}

	public String getFsignintime() {
		return fsignintime;
	}

	public void setFsignintime(String fsignintime) {
		this.fsignintime = fsignintime;
	}

	public String getStsignintime() {
		return stsignintime;
	}

	public void setStsignintime(String stsignintime) {
		this.stsignintime = stsignintime;
	}

	public String getSsignouttime() {
		return ssignouttime;
	}

	public void setSsignouttime(String ssignouttime) {
		this.ssignouttime = ssignouttime;
	}

	public String getMsignouttime() {
		return msignouttime;
	}

	public void setMsignouttime(String msignouttime) {
		this.msignouttime = msignouttime;
	}

	public String getTsignouttime() {
		return tsignouttime;
	}

	public void setTsignouttime(String tsignouttime) {
		this.tsignouttime = tsignouttime;
	}

	public String getW_signouttime() {
		return w_signouttime;
	}

	public void setW_signouttime(String w_signouttime) {
		this.w_signouttime = w_signouttime;
	}

	public String getThsignouttime() {
		return thsignouttime;
	}

	public void setThsignouttime(String thsignouttime) {
		this.thsignouttime = thsignouttime;
	}

	public String getFsignouttime() {
		return fsignouttime;
	}

	public void setFsignouttime(String fsignouttime) {
		this.fsignouttime = fsignouttime;
	}

	public String getSt_signouttime() {
		return st_signouttime;
	}

	public void setSt_signouttime(String st_signouttime) {
		this.st_signouttime = st_signouttime;
	}

	public String getStimestarthours() {
		return stimestarthours;
	}

	public void setStimestarthours(String stimestarthours) {
		this.stimestarthours = stimestarthours;
	}

	public String getMtimestarthours() {
		return mtimestarthours;
	}

	public void setMtimestarthours(String mtimestarthours) {
		this.mtimestarthours = mtimestarthours;
	}

	public String getTtimestarthours() {
		return ttimestarthours;
	}

	public void setTtimestarthours(String ttimestarthours) {
		this.ttimestarthours = ttimestarthours;
	}

	public String getWtimestarthours() {
		return wtimestarthours;
	}

	public void setWtimestarthours(String wtimestarthours) {
		this.wtimestarthours = wtimestarthours;
	}

	public String getThtimestarthours() {
		return thtimestarthours;
	}

	public void setThtimestarthours(String thtimestarthours) {
		this.thtimestarthours = thtimestarthours;
	}

	public String getFtimestarthours() {
		return ftimestarthours;
	}

	public void setFtimestarthours(String ftimestarthours) {
		this.ftimestarthours = ftimestarthours;
	}

	public String getSttimestarthours() {
		return sttimestarthours;
	}

	public void setSttimestarthours(String sttimestarthours) {
		this.sttimestarthours = sttimestarthours;
	}

	public String getStimestartminuts() {
		return stimestartminuts;
	}

	public void setStimestartminuts(String stimestartminuts) {
		this.stimestartminuts = stimestartminuts;
	}

	public String getMtimestartminuts() {
		return mtimestartminuts;
	}

	public void setMtimestartminuts(String mtimestartminuts) {
		this.mtimestartminuts = mtimestartminuts;
	}

	public String getTtimestartminuts() {
		return ttimestartminuts;
	}

	public void setTtimestartminuts(String ttimestartminuts) {
		this.ttimestartminuts = ttimestartminuts;
	}

	public String getWtimestartminuts() {
		return wtimestartminuts;
	}

	public void setWtimestartminuts(String wtimestartminuts) {
		this.wtimestartminuts = wtimestartminuts;
	}

	public String getThtimestartminuts() {
		return thtimestartminuts;
	}

	public void setThtimestartminuts(String thtimestartminuts) {
		this.thtimestartminuts = thtimestartminuts;
	}

	public String getFrtimestartminuts() {
		return frtimestartminuts;
	}

	public void setFrtimestartminuts(String frtimestartminuts) {
		this.frtimestartminuts = frtimestartminuts;
	}

	public String getSttimestartminuts() {
		return sttimestartminuts;
	}

	public void setSttimestartminuts(String sttimestartminuts) {
		this.sttimestartminuts = sttimestartminuts;
	}

	public String getStimeendinhours() {
		return stimeendinhours;
	}

	public void setStimeendinhours(String stimeendinhours) {
		this.stimeendinhours = stimeendinhours;
	}

	public String getMtimeendinhours() {
		return mtimeendinhours;
	}

	public void setMtimeendinhours(String mtimeendinhours) {
		this.mtimeendinhours = mtimeendinhours;
	}

	public String getTtimeendinhours() {
		return ttimeendinhours;
	}

	public void setTtimeendinhours(String ttimeendinhours) {
		this.ttimeendinhours = ttimeendinhours;
	}

	public String getWtimeendinhours() {
		return wtimeendinhours;
	}

	public void setWtimeendinhours(String wtimeendinhours) {
		this.wtimeendinhours = wtimeendinhours;
	}

	public String getThtimeendinhours() {
		return thtimeendinhours;
	}

	public void setThtimeendinhours(String thtimeendinhours) {
		this.thtimeendinhours = thtimeendinhours;
	}

	public String getFrtimeendinhours() {
		return frtimeendinhours;
	}

	public void setFrtimeendinhours(String frtimeendinhours) {
		this.frtimeendinhours = frtimeendinhours;
	}

	public String getSttimeendinhours() {
		return sttimeendinhours;
	}

	public void setSttimeendinhours(String sttimeendinhours) {
		this.sttimeendinhours = sttimeendinhours;
	}

	public String getStimeendinminuts() {
		return stimeendinminuts;
	}

	public void setStimeendinminuts(String stimeendinminuts) {
		this.stimeendinminuts = stimeendinminuts;
	}

	public String getMtimeendinminuts() {
		return mtimeendinminuts;
	}

	public void setMtimeendinminuts(String mtimeendinminuts) {
		this.mtimeendinminuts = mtimeendinminuts;
	}

	public String getTtimeendinminuts() {
		return ttimeendinminuts;
	}

	public void setTtimeendinminuts(String ttimeendinminuts) {
		this.ttimeendinminuts = ttimeendinminuts;
	}

	public String getWtimeendinminuts() {
		return wtimeendinminuts;
	}

	public void setWtimeendinminuts(String wtimeendinminuts) {
		this.wtimeendinminuts = wtimeendinminuts;
	}

	public String getThtimeendinminuts() {
		return thtimeendinminuts;
	}

	public void setThtimeendinminuts(String thtimeendinminuts) {
		this.thtimeendinminuts = thtimeendinminuts;
	}

	public String getFtimeendinminuts() {
		return ftimeendinminuts;
	}

	public void setFtimeendinminuts(String ftimeendinminuts) {
		this.ftimeendinminuts = ftimeendinminuts;
	}

	public String getSttimeendinminuts() {
		return sttimeendinminuts;
	}

	public void setSttimeendinminuts(String sttimeendinminuts) {
		this.sttimeendinminuts = sttimeendinminuts;
	}

	public Double getShoursworked() {
		return shoursworked;
	}

	public void setShoursworked(Double shoursworked) {
		this.shoursworked = shoursworked;
	}

	public Double getMhoursworked() {
		return mhoursworked;
	}

	public void setMhoursworked(Double mhoursworked) {
		this.mhoursworked = mhoursworked;
	}

	public Double getThoursworked() {
		return thoursworked;
	}

	public void setThoursworked(Double thoursworked) {
		this.thoursworked = thoursworked;
	}

	public Double getWhoursworked() {
		return whoursworked;
	}

	public void setWhoursworked(Double whoursworked) {
		this.whoursworked = whoursworked;
	}

	public Double getThhoursworked() {
		return thhoursworked;
	}

	public void setThhoursworked(Double thhoursworked) {
		this.thhoursworked = thhoursworked;
	}

	public Double getFhoursworked() {
		return fhoursworked;
	}

	public void setFhoursworked(Double fhoursworked) {
		this.fhoursworked = fhoursworked;
	}

	public Double getSthoursworked() {
		return sthoursworked;
	}

	public void setSthoursworked(Double sthoursworked) {
		this.sthoursworked = sthoursworked;
	}

	public Double getSovertimehours() {
		return sovertimehours;
	}

	public void setSovertimehours(Double sovertimehours) {
		this.sovertimehours = sovertimehours;
	}

	public Double getMovertimehours() {
		return movertimehours;
	}

	public void setMovertimehours(Double movertimehours) {
		this.movertimehours = movertimehours;
	}

	public Double getTovertimehours() {
		return tovertimehours;
	}

	public void setTovertimehours(Double tovertimehours) {
		this.tovertimehours = tovertimehours;
	}

	public Double getWovertimehours() {
		return wovertimehours;
	}

	public void setWovertimehours(Double wovertimehours) {
		this.wovertimehours = wovertimehours;
	}

	public Double getThovertimehours() {
		return thovertimehours;
	}

	public void setThovertimehours(Double thovertimehours) {
		this.thovertimehours = thovertimehours;
	}

	public Double getFovertimehours() {
		return fovertimehours;
	}

	public void setFovertimehours(Double fovertimehours) {
		this.fovertimehours = fovertimehours;
	}

	public Double getStovertimehours() {
		return stovertimehours;
	}

	public void setStovertimehours(Double stovertimehours) {
		this.stovertimehours = stovertimehours;
	}

	public Double getSdoubletimehours() {
		return sdoubletimehours;
	}

	public void setSdoubletimehours(Double sdoubletimehours) {
		this.sdoubletimehours = sdoubletimehours;
	}

	public Double getMdoubletimehours() {
		return mdoubletimehours;
	}

	public void setMdoubletimehours(Double mdoubletimehours) {
		this.mdoubletimehours = mdoubletimehours;
	}

	public Double getTdoubletimehours() {
		return tdoubletimehours;
	}

	public void setTdoubletimehours(Double tdoubletimehours) {
		this.tdoubletimehours = tdoubletimehours;
	}

	public Double getWdoubletimehours() {
		return wdoubletimehours;
	}

	public void setWdoubletimehours(Double wdoubletimehours) {
		this.wdoubletimehours = wdoubletimehours;
	}

	public Double getThdoubletimehours() {
		return thdoubletimehours;
	}

	public void setThdoubletimehours(Double thdoubletimehours) {
		this.thdoubletimehours = thdoubletimehours;
	}

	public Double getFdoubletimehours() {
		return fdoubletimehours;
	}

	public void setFdoubletimehours(Double fdoubletimehours) {
		this.fdoubletimehours = fdoubletimehours;
	}

	public Double getStdoubletimehours() {
		return stdoubletimehours;
	}

	public void setStdoubletimehours(Double stdoubletimehours) {
		this.stdoubletimehours = stdoubletimehours;
	}

	public String getSotflag() {
		return sotflag;
	}

	public void setSotflag(String sotflag) {
		this.sotflag = sotflag;
	}

	public String getMtotflag() {
		return mtotflag;
	}

	public void setMtotflag(String mtotflag) {
		this.mtotflag = mtotflag;
	}

	public String getTotflag() {
		return totflag;
	}

	public void setTotflag(String totflag) {
		this.totflag = totflag;
	}

	public String getWotflag() {
		return wotflag;
	}

	public void setWotflag(String wotflag) {
		this.wotflag = wotflag;
	}

	public String getThotflag() {
		return thotflag;
	}

	public void setThotflag(String thotflag) {
		this.thotflag = thotflag;
	}

	public String getFotflag() {
		return fotflag;
	}

	public void setFotflag(String fotflag) {
		this.fotflag = fotflag;
	}

	public String getStotflag() {
		return stotflag;
	}

	public void setStotflag(String stotflag) {
		this.stotflag = stotflag;
	}

	public String getSdtflag() {
		return sdtflag;
	}

	public void setSdtflag(String sdtflag) {
		this.sdtflag = sdtflag;
	}

	public String getMdtflag() {
		return mdtflag;
	}

	public void setMdtflag(String mdtflag) {
		this.mdtflag = mdtflag;
	}

	public String getTdtflag() {
		return tdtflag;
	}

	public void setTdtflag(String tdtflag) {
		this.tdtflag = tdtflag;
	}

	public String getWdtflag() {
		return wdtflag;
	}

	public void setWdtflag(String wdtflag) {
		this.wdtflag = wdtflag;
	}

	public String getThdtflag() {
		return thdtflag;
	}

	public void setThdtflag(String thdtflag) {
		this.thdtflag = thdtflag;
	}

	public String getFdtflag() {
		return fdtflag;
	}

	public void setFdtflag(String fdtflag) {
		this.fdtflag = fdtflag;
	}

	public String getStdtflag() {
		return stdtflag;
	}

	public void setStdtflag(String stdtflag) {
		this.stdtflag = stdtflag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getTotalhoursworkedInweek() {
		return totalhoursworkedInweek;
	}

	public void setTotalhoursworkedInweek(Double totalhoursworkedInweek) {
		this.totalhoursworkedInweek = totalhoursworkedInweek;
	}

	public Double getRegularhours() {
		return regularhours;
	}

	public void setRegularhours(Double regularhours) {
		this.regularhours = regularhours;
	}

	public Double getTotalothoursinweek() {
		return totalothoursinweek;
	}

	public void setTotalothoursinweek(Double totalothoursinweek) {
		this.totalothoursinweek = totalothoursinweek;
	}

	public Double getTotaldthoursinweek() {
		return totaldthoursinweek;
	}

	public void setTotaldthoursinweek(Double totaldthoursinweek) {
		this.totaldthoursinweek = totaldthoursinweek;
	}

	public String getSsigninampm() {
		return ssigninampm;
	}

	public void setSsigninampm(String ssigninampm) {
		this.ssigninampm = ssigninampm;
	}

	public String getMsigninampm() {
		return msigninampm;
	}

	public void setMsigninampm(String msigninampm) {
		this.msigninampm = msigninampm;
	}

	public String getTsigninampm() {
		return tsigninampm;
	}

	public void setTsigninampm(String tsigninampm) {
		this.tsigninampm = tsigninampm;
	}

	public String getWsigninampm() {
		return wsigninampm;
	}

	public void setWsigninampm(String wsigninampm) {
		this.wsigninampm = wsigninampm;
	}

	public String getThsigninampm() {
		return thsigninampm;
	}

	public void setThsigninampm(String thsigninampm) {
		this.thsigninampm = thsigninampm;
	}

	public String getFsigninampm() {
		return fsigninampm;
	}

	public void setFsigninampm(String fsigninampm) {
		this.fsigninampm = fsigninampm;
	}

	public String getStsigninampm() {
		return stsigninampm;
	}

	public void setStsigninampm(String stsigninampm) {
		this.stsigninampm = stsigninampm;
	}

	public String getSsignoutampm() {
		return ssignoutampm;
	}

	public void setSsignoutampm(String ssignoutampm) {
		this.ssignoutampm = ssignoutampm;
	}

	public String getMsignoutampm() {
		return msignoutampm;
	}

	public void setMsignoutampm(String msignoutampm) {
		this.msignoutampm = msignoutampm;
	}

	public String getTsignoutampm() {
		return tsignoutampm;
	}

	public void setTsignoutampm(String tsignoutampm) {
		this.tsignoutampm = tsignoutampm;
	}

	public String getWsignoutampm() {
		return wsignoutampm;
	}

	public void setWsignoutampm(String wsignoutampm) {
		this.wsignoutampm = wsignoutampm;
	}

	public String getThsignoutampm() {
		return thsignoutampm;
	}

	public void setThsignoutampm(String thsignoutampm) {
		this.thsignoutampm = thsignoutampm;
	}

	public String getFsignoutampm() {
		return fsignoutampm;
	}

	public void setFsignoutampm(String fsignoutampm) {
		this.fsignoutampm = fsignoutampm;
	}

	public String getStsignoutampm() {
		return stsignoutampm;
	}

	public void setStsignoutampm(String stsignoutampm) {
		this.stsignoutampm = stsignoutampm;
	}

	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}

	public String getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}

	public String getEmployees() {
		return employees;
	}

	public void setEmployees(String employees) {
		this.employees = employees;
	}

	public String getEmployeesNo() {
		return employeesNo;
	}

	public void setEmployeesNo(String employeesNo) {
		this.employeesNo = employeesNo;
	}

	public String getBatchDates() {
		return batchDates;
	}

	public void setBatchDates(String batchDates) {
		this.batchDates = batchDates;
	}

	public Double getRegularamounts() {
		return regularamounts;
	}

	public void setRegularamounts(Double regularamounts) {
		this.regularamounts = regularamounts;
	}

	public Double getOtamounts() {
		return otamounts;
	}

	public void setOtamounts(Double otamounts) {
		this.otamounts = otamounts;
	}

	public Double getDtamounts() {
		return dtamounts;
	}

	public void setDtamounts(Double dtamounts) {
		this.dtamounts = dtamounts;
	}

	public Double getTotamounts() {
		return totamounts;
	}

	public void setTotamounts(Double totamounts) {
		this.totamounts = totamounts;
	}

	public Double getrHours() {
		return rHours;
	}

	public void setrHours(Double rHours) {
		this.rHours = rHours;
	}

	public Double getOtHours() {
		return otHours;
	}

	public void setOtHours(Double otHours) {
		this.otHours = otHours;
	}

	public Double getDtHours() {
		return dtHours;
	}

	public void setDtHours(Double dtHours) {
		this.dtHours = dtHours;
	}

	public Double getrRate() {
		return rRate;
	}

	public void setrRate(Double rRate) {
		this.rRate = rRate;
	}

	public Double getOtRate() {
		return otRate;
	}

	public void setOtRate(Double otRate) {
		this.otRate = otRate;
	}

	public Double getDtRate() {
		return dtRate;
	}

	public void setDtRate(Double dtRate) {
		this.dtRate = dtRate;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public Double getTotalhoursworkedInweekRounded() {
		return totalhoursworkedInweekRounded;
	}

	public void setTotalhoursworkedInweekRounded(
			Double totalhoursworkedInweekRounded) {
		this.totalhoursworkedInweekRounded = totalhoursworkedInweekRounded;
	}

	/*public Double getTotalothoursinweekRounded() {
		return totalothoursinweekRounded;
	}

	public void setTotalothoursinweekRounded(Double totalothoursinweekRounded) {
		this.totalothoursinweekRounded = totalothoursinweekRounded;
	}

	public Double getTotaldthoursinweekRounded() {
		return totaldthoursinweekRounded;
	}

	public void setTotaldthoursinweekRounded(Double totaldthoursinweekRounded) {
		this.totaldthoursinweekRounded = totaldthoursinweekRounded;
	}*/

	public Double getHoursworkedInweekRoundedValue() {
		return hoursworkedInweekRoundedValue;
	}

	public void setHoursworkedInweekRoundedValue(
			Double hoursworkedInweekRoundedValue) {
		this.hoursworkedInweekRoundedValue = hoursworkedInweekRoundedValue;
	}

	public Double getBonusAmounts() {
		return bonusAmounts;
	}

	public void setBonusAmounts(Double bonusAmounts) {
		this.bonusAmounts = bonusAmounts;
	}

	public Double getSumOfTotVacSicBonus() {
		return sumOfTotVacSicBonus;
	}

	public void setSumOfTotVacSicBonus(Double sumOfTotVacSicBonus) {
		this.sumOfTotVacSicBonus = sumOfTotVacSicBonus;
	}

	public Double getHolidayAmount() {
		return holidayAmount;
	}

	public void setHolidayAmount(Double holidayAmount) {
		this.holidayAmount = holidayAmount;
	}

	public String getS2signintime() {
		return s2signintime;
	}

	public void setS2signintime(String s2signintime) {
		this.s2signintime = s2signintime;
	}

	public String getM2signintime() {
		return m2signintime;
	}

	public void setM2signintime(String m2signintime) {
		this.m2signintime = m2signintime;
	}

	public String getT2signintime() {
		return t2signintime;
	}

	public void setT2signintime(String t2signintime) {
		this.t2signintime = t2signintime;
	}

	public String getW2signintime() {
		return w2signintime;
	}

	public void setW2signintime(String w2signintime) {
		this.w2signintime = w2signintime;
	}

	public String getTh2signintime() {
		return th2signintime;
	}

	public void setTh2signintime(String th2signintime) {
		this.th2signintime = th2signintime;
	}

	public String getFs2ignintime() {
		return fs2ignintime;
	}

	public void setFs2ignintime(String fs2ignintime) {
		this.fs2ignintime = fs2ignintime;
	}

	public String getSt2signintime() {
		return st2signintime;
	}

	public void setSt2signintime(String st2signintime) {
		this.st2signintime = st2signintime;
	}

	public String getS2signinampm() {
		return s2signinampm;
	}

	public void setS2signinampm(String s2signinampm) {
		this.s2signinampm = s2signinampm;
	}

	public String getM2signinampm() {
		return m2signinampm;
	}

	public void setM2signinampm(String m2signinampm) {
		this.m2signinampm = m2signinampm;
	}

	public String getT2signinampm() {
		return t2signinampm;
	}

	public void setT2signinampm(String t2signinampm) {
		this.t2signinampm = t2signinampm;
	}

	public String getW2signinampm() {
		return w2signinampm;
	}

	public void setW2signinampm(String w2signinampm) {
		this.w2signinampm = w2signinampm;
	}

	public String getTh2signinampm() {
		return th2signinampm;
	}

	public void setTh2signinampm(String th2signinampm) {
		this.th2signinampm = th2signinampm;
	}

	public String getF2signinampm() {
		return f2signinampm;
	}

	public void setF2signinampm(String f2signinampm) {
		this.f2signinampm = f2signinampm;
	}

	public String getSt2signinampm() {
		return st2signinampm;
	}

	public void setSt2signinampm(String st2signinampm) {
		this.st2signinampm = st2signinampm;
	}

	public String getS2signouttime() {
		return s2signouttime;
	}

	public void setS2signouttime(String s2signouttime) {
		this.s2signouttime = s2signouttime;
	}

	public String getM2signouttime() {
		return m2signouttime;
	}

	public void setM2signouttime(String m2signouttime) {
		this.m2signouttime = m2signouttime;
	}

	public String getT2signouttime() {
		return t2signouttime;
	}

	public void setT2signouttime(String t2signouttime) {
		this.t2signouttime = t2signouttime;
	}

	public String getW2_signouttime() {
		return w2_signouttime;
	}

	public void setW2_signouttime(String w2_signouttime) {
		this.w2_signouttime = w2_signouttime;
	}

	public String getTh2signouttime() {
		return th2signouttime;
	}

	public void setTh2signouttime(String th2signouttime) {
		this.th2signouttime = th2signouttime;
	}

	public String getF2signouttime() {
		return f2signouttime;
	}

	public void setF2signouttime(String f2signouttime) {
		this.f2signouttime = f2signouttime;
	}

	public String getSt2_signouttime() {
		return st2_signouttime;
	}

	public void setSt2_signouttime(String st2_signouttime) {
		this.st2_signouttime = st2_signouttime;
	}

	public String getS2signoutampm() {
		return s2signoutampm;
	}

	public void setS2signoutampm(String s2signoutampm) {
		this.s2signoutampm = s2signoutampm;
	}

	public String getM2signoutampm() {
		return m2signoutampm;
	}

	public void setM2signoutampm(String m2signoutampm) {
		this.m2signoutampm = m2signoutampm;
	}

	public String getT2signoutampm() {
		return t2signoutampm;
	}

	public void setT2signoutampm(String t2signoutampm) {
		this.t2signoutampm = t2signoutampm;
	}

	public String getW2signoutampm() {
		return w2signoutampm;
	}

	public void setW2signoutampm(String w2signoutampm) {
		this.w2signoutampm = w2signoutampm;
	}

	public String getTh2signoutampm() {
		return th2signoutampm;
	}

	public void setTh2signoutampm(String th2signoutampm) {
		this.th2signoutampm = th2signoutampm;
	}

	public String getF2signoutampm() {
		return f2signoutampm;
	}

	public void setF2signoutampm(String f2signoutampm) {
		this.f2signoutampm = f2signoutampm;
	}

	public String getSt2signoutampm() {
		return st2signoutampm;
	}

	public void setSt2signoutampm(String st2signoutampm) {
		this.st2signoutampm = st2signoutampm;
	}

	public String getSholidayflag() {
		return sholidayflag;
	}

	public void setSholidayflag(String sholidayflag) {
		this.sholidayflag = sholidayflag;
	}

	public String getMholidayflag() {
		return mholidayflag;
	}

	public void setMholidayflag(String mholidayflag) {
		this.mholidayflag = mholidayflag;
	}

	public String getTholidayflag() {
		return tholidayflag;
	}

	public void setTholidayflag(String tholidayflag) {
		this.tholidayflag = tholidayflag;
	}

	public String getWholidayflag() {
		return wholidayflag;
	}

	public void setWholidayflag(String wholidayflag) {
		this.wholidayflag = wholidayflag;
	}

	public String getThholidayflag() {
		return thholidayflag;
	}

	public void setThholidayflag(String thholidayflag) {
		this.thholidayflag = thholidayflag;
	}

	public String getFholidayflag() {
		return fholidayflag;
	}

	public void setFholidayflag(String fholidayflag) {
		this.fholidayflag = fholidayflag;
	}

	public String getStholidayflag() {
		return stholidayflag;
	}

	public void setStholidayflag(String stholidayflag) {
		this.stholidayflag = stholidayflag;
	}	
	
	public Double getHolidayhours() {
		return holidayhours;
	}
	
	public void setHolidayhours(Double holidayhours) {
		this.holidayhours = holidayhours;
	}

	public Double getMluncHours() {
		return mluncHours;
	}

	public void setMluncHours(Double mluncHours) {
		this.mluncHours = mluncHours;
	}

	public Double getTluncHours() {
		return tluncHours;
	}

	public void setTluncHours(Double tluncHours) {
		this.tluncHours = tluncHours;
	}

	public Double getWluncHours() {
		return wluncHours;
	}

	public void setWluncHours(Double wluncHours) {
		this.wluncHours = wluncHours;
	}

	public Double getThluncHours() {
		return thluncHours;
	}

	public void setThluncHours(Double thluncHours) {
		this.thluncHours = thluncHours;
	}

	public Double getFluncHours() {
		return fluncHours;
	}

	public void setFluncHours(Double fluncHours) {
		this.fluncHours = fluncHours;
	}

	public Double getStluncHours() {
		return stluncHours;
	}

	public void setStluncHours(Double stluncHours) {
		this.stluncHours = stluncHours;
	}

	public Double getSluncHours() {
		return sluncHours;
	}

	public void setSluncHours(Double sluncHours) {
		this.sluncHours = sluncHours;
	}

	public Integer getSequenceNum1() {
		return sequenceNum1;
	}

	public void setSequenceNum1(Integer sequenceNum1) {
		this.sequenceNum1 = sequenceNum1;
	}

	@Transient
	public String getMode() {
		return mode;
	}

	@Transient
	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
