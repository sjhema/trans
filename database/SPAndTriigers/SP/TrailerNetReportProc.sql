CREATE DEFINER=`lutransport`@`%` PROCEDURE `TrailerNetReportProc`()
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


delete from trailer_net_report where unload_date >= CURDATE() - INTERVAL 2 MONTH;

delete from trailer_revenueamount;

delete from trailer_list_tbl;


SET start_date = CURDATE() - INTERVAL 2 MONTH;

SET end_date = CURDATE() + INTERVAL 4 DAY;

SET date_diff = DATEDIFF(end_date, start_date);


insert into trailer_list_tbl (unitNum,location_id)
select distinct unitNum,owner from vehicle where owner  in (4,5,6) and type=2;

update trailer_list_tbl trk,location loc
set trk.location_name=loc.name 
where trk.location_id=loc.id;

insert into trailer_net_report 
select 0,CURDATE(),null,null,null,1,0.0,0.0,trk.location_id,trk.location_name,0.0,trk.unitNum,d.date_rec,null from trailer_list_tbl trk cross join date_table d  
where d.date_rec  between start_date and end_date;

insert into trailer_revenueamount
select unload_date,sum(total_amount),sum(driver_pay_rate),trailer_num,driver_companylocid from invoice_detail_new 
where unload_date between start_date and end_date
group by driver_companylocid,trailer_num,unload_date;


update trailer_net_report c, trailer_revenueamount rev
set c.revenue_amount= coalesce(rev.revenue_amount,0.0),c.driver_pay_amount=coalesce(rev.driver_payamount,0.0)
where c.employee_company_id=rev.location_id and  c.unload_date = rev.report_date and c.unitNum=rev.unitNum;

insert into trailer_net_report
select 0,CURDATE(),null,null,null,1,0.0,coalesce(rev.driver_payamount,0.0),loc.id,loc.name,coalesce(rev.revenue_amount,0.0),rev.unitNum,report_date,null
from trailer_revenueamount rev 
left join trailer_net_report net
on net.employee_company_id=rev.location_id and net.unload_date = rev.report_date and net.unitNum=rev.unitNum
join location loc 
on rev.location_id=loc.id
where net.unitNum is null;



update trailer_net_report 
set net_amount = revenue_amount - driver_pay_amount 
where unload_date between start_date and end_date;

update trailer_net_report tnet
left join
(
select tnet.unitNum,tnet.unload_date,'Y' from trailer_net_report tnet join vehicle vh
on tnet.unitNum=vh.unitNum
and tnet.unload_date between valid_from and valid_to
and tnet.unload_date  between start_date and end_date
) t_actv
on tnet.unitNum=t_actv.unitNum and tnet.unload_date=t_actv.unload_date
set tnet.active_flag = case when t_actv.unitNum is null then 'N' else 'Y' end
where tnet.unload_date  between start_date and end_date;



END