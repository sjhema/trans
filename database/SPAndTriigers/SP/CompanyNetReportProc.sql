CREATE DEFINER=`lutransport`@`%` PROCEDURE `CompanyNetReportProc`()
BEGIN

DECLARE done int DEFAULT 0;
DECLARE flag int DEFAULT 0;
DECLARE done_holder int;
DECLARE No_Cal int;
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

DECLARE MainCursor CURSOR for select id,name from location where id in (4,5,6) order by name asc;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;

SET done=0;


delete from company_net_report where unload_date >= CURDATE() - INTERVAL 3 MONTH;

delete from netreport_company_list;

delete from netreport_rev_amount_tbl;

delete from netreport_fuel_amount_tbl;

delete from netreport_subcontractor_amount_tbl;

delete from netreport_toll_amount_tbl;

delete from netreport_driverpay_amount_tbl;

OPEN MainCursor;

main_loop: LOOP

FETCH MainCursor into companyID,companyName;

IF done THEN

CLOSE MainCursor;

LEAVE main_loop;

END IF;

SET done_holder=done;

SET start_date = CURDATE() - INTERVAL 3 MONTH;

SET end_date = CURDATE() + INTERVAL 4 DAY;

SET date_diff = DATEDIFF(end_date, start_date);

SET unloadDate = start_date;

SET done=done_holder;

insert into company_net_report 
select 0,CURDATE(),null,null,null,1,0.0,0.0,loc.id,companyName,0.0,0.0,0.0,0.0,d.date_rec from location loc cross join date_table d  
where d.date_rec  between start_date and end_date
and loc.id=companyID;


insert into netreport_rev_amount_tbl
select driver_companylocid,unload_date,sum(total_amount) from invoice_detail_new 
where unload_date between start_date and end_date
and driver_companylocid=companyID  group by driver_companylocid,unload_date;

insert into netreport_fuel_amount_tbl
select company,date(transaction_date),sum(amount) from fuel_log 
where transaction_date between start_date and end_date
and company=companyID  group by company,date(transaction_date);

insert into netreport_subcontractor_amount_tbl
select company_location,unload_date,sum(total_amount) from subcontractor_invoice_detail_new 
where unload_date between start_date and end_date
and company_location=companyID  group by company_location,unload_date;

insert into netreport_toll_amount_tbl
select company,date(transaction_date),sum(amount) from ez_toll 
where transaction_date between start_date and end_date
and company=companyID  group by company,date(transaction_date);

insert into netreport_driverpay_amount_tbl
select driver_company,unload_date,sum(driver_payrate) from ticket 
where unload_date between start_date and end_date
and driver_company=companyID  group by driver_company,unload_date;


update company_net_report c, netreport_rev_amount_tbl rev
set c.revenue_amount= coalesce(rev.revenue_amount,0.0)
where c.employee_company_id=rev.location_id and  c.unload_date = rev.report_date;

update company_net_report c, netreport_fuel_amount_tbl fuel
set c.fuel_amount= coalesce(fuel.fuel_amount,0.0)
where c.employee_company_id=fuel.location_id and  c.unload_date = fuel.report_date;

update company_net_report c, netreport_subcontractor_amount_tbl sub
set c.subcontractor_amount = coalesce(sub.subcontractor_amount,0.0)
where c.employee_company_id=sub.location_id and  c.unload_date = sub.report_date;

update company_net_report c, netreport_toll_amount_tbl toll
set c.toll_amount = coalesce(toll.toll_amount,0.0)
where c.employee_company_id=toll.location_id and  c.unload_date = toll.report_date;

update company_net_report c, netreport_driverpay_amount_tbl drvpay
set c.driver_pay_amount = coalesce(drvpay.driverpay_amount,0.0)
where c.employee_company_id=drvpay.location_id and  c.unload_date = drvpay.report_date;

update company_net_report set net_amount = revenue_amount - (fuel_amount+subcontractor_amount+toll_amount+driver_pay_amount) where unload_date between start_date and end_date and employee_company_id=companyID;

END LOOP main_loop;

END