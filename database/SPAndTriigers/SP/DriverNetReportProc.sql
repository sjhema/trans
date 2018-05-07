CREATE DEFINER=`lutransport`@`%` PROCEDURE `DriverNetReportProc`()
BEGIN

DECLARE cur_year int;
DECLARE cur_month int;
DECLARE temp_date VARCHAR(10);
DECLARE start_date DATE;
DECLARE end_date DATE;
DECLARE date_diff int;
DECLARE unloadDate DATE;

DECLARE companyID bigint(225);
DECLARE companyName VARCHAR(225);
DECLARE revenueAmount double default 0.0;
DECLARE subRevenueAmount double default 0.0;
DECLARE fuelAmount double default 0.0;
DECLARE subcontractorAmount double default 0.0;
DECLARE tollAmount double default 0.0;
DECLARE driverPayAmount double default 0.0;
DECLARE netAmount double default 0.0;


delete from driver_net_report where unload_date >= CURDATE() - INTERVAL 2 MONTH;

delete from driver_revenueamount;

delete from driver_fuelamount;

delete from driver_tollamount;

delete from driver_list_tbl;


SET start_date = CURDATE() - INTERVAL 2 MONTH;

SET end_date = CURDATE() + INTERVAL 4 DAY;

SET date_diff = DATEDIFF(end_date, start_date);


insert into driver_list_tbl (driver_fullname,location_id)
select distinct full_name,company_id from driver where company_id  in (4,5,6) and pay_term=1 ;

update driver_list_tbl drv,location loc
set drv.location_name=loc.name 
where drv.location_id=loc.id;

insert into driver_net_report 
select 0,CURDATE(),null,null,null,1,0.0,0.0,drv.location_id,drv.location_name,drv.driver_fullname,0.0,0.0,0.0,0.0,d.date_rec,null from driver_list_tbl drv cross join date_table d  
where d.date_rec  between start_date and end_date;


insert into driver_revenueamount
select unload_date,sum(total_amount),sum(driver_pay_rate),t_driver,driver_companylocid from invoice_detail_new 
where unload_date between start_date and end_date
group by driver_companylocid,t_driver,unload_date;

insert into driver_fuelamount 
select company,transaction_date,sum(amount),driver_fullname from fuel_log 
where transaction_date between start_date and end_date
group by company,driver_fullname,transaction_date;

insert into driver_tollamount
select company,transaction_date,sum(amount),driver_fullname from ez_toll 
where transaction_date between start_date and end_date
group by company,driver_fullname,transaction_date;


update driver_net_report c, driver_revenueamount rev
set c.revenue_amount= coalesce(rev.revenue_amount,0.0),c.driver_pay_amount=coalesce(rev.driver_payamount,0.0)
where c.employee_company_id=rev.location_id and  c.unload_date = rev.report_date and c.employee_name=rev.driver_fullname;

insert into driver_net_report 
select 0,CURDATE(),null,null,null,1,0.0,coalesce(rev.driver_payamount,0.0),loc.id,loc.name,driver_fullname,0.0,coalesce(rev.revenue_amount,0.0),0.0,0.0,report_date,null
from driver_revenueamount rev 
left join driver_net_report net
on net.employee_company_id=rev.location_id and net.unload_date = rev.report_date and net.employee_name=rev.driver_fullname
join location loc 
on rev.location_id=loc.id
where net.employee_name is null;

update driver_net_report c,driver_fuelamount fuel
set c.fuel_amount= coalesce(fuel.fuel_amount,0.0)
where c.employee_company_id=fuel.location_id and  c.unload_date = fuel.report_date and c.employee_name=fuel.driver_fullname;


insert into driver_net_report 
select 0,CURDATE(),null,null,null,1,0.0,0.0,loc.id,loc.name,driver_fullname,coalesce(fuel.fuel_amount,0.0),0.0,0.0,0.0,report_date,null
from driver_fuelamount fuel
left join driver_net_report net
on net.employee_company_id=fuel.location_id and net.unload_date = fuel.report_date and net.employee_name=fuel.driver_fullname
join location loc 
on fuel.location_id=loc.id
where net.employee_name is null;


update driver_net_report c, driver_tollamount toll
set c.toll_amount = coalesce(toll.toll_amount,0.0)
where c.employee_company_id=toll.location_id and  c.unload_date = toll.report_date and c.employee_name=toll.driver_fullname;


insert into driver_net_report 
select 0,CURDATE(),null,null,null,1,0.0,0.0,loc.id,loc.name,driver_fullname,0.0,0.0,0.0,coalesce(toll.toll_amount,0.0),report_date,null
from driver_tollamount toll
left join driver_net_report net
on net.employee_company_id=toll.location_id and net.unload_date = toll.report_date and net.employee_name=toll.driver_fullname
join location loc 
on toll.location_id=loc.id
where net.employee_name is null;


update driver_net_report set net_amount = revenue_amount - (fuel_amount+toll_amount+driver_pay_amount) where unload_date between start_date and end_date;

update driver_net_report dnet
left join
(
select dnet.employee_name,dnet.employee_company_id,dnet.unload_date,'Y' from driver_net_report dnet join driver dr
on dnet.employee_name=dr.full_name
and dnet.employee_company_id=dr.company_id
and dnet.unload_date between coalesce(date_rehired,date_hired) and coalesce(date_terminated,curdate())
and dnet.unload_date  between start_date and end_date
) d_actv
on dnet.employee_name=d_actv.employee_name
and dnet.employee_company_id=d_actv.employee_company_id
and dnet.unload_date=d_actv.unload_date
set dnet.active_flag = case when d_actv.employee_name is null then 'N' else 'Y' end
where dnet.unload_date  between start_date and end_date;

END