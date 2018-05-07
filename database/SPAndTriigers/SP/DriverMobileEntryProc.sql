CREATE DEFINER=`lutransport`@`%` PROCEDURE `DriverMobileEntryProc`()
BEGIN

insert into driver_mobile_entry(employee_name,employee_id,created_at,entry_date,employee_company_name,employee_company_id,employee_terminal_name,employee_terminal_id)
SELECT  distinct drv.full_name,drv.id,curdate(),curdate(),loc.name,loc.id,loc_terminal.name,loc_terminal.id FROM driver drv,location loc,location loc_terminal  where drv.status=1 and drv.catagory=2 and  drv.company_id=loc.id and drv.terminal=loc_terminal.id;

update driver_mobile_entry d_core
join
(
select  distinct d_main.employee_name,d_main.entry_date,
case when pt.employee is not null then 'L' end as tripsheet_flag
from  driver_mobile_entry d_main
left join ptodapplication pt
on d_main.employee_id=pt.employee and date(d_main.entry_date)>=date(pt.leave_date_from) and date(d_main.entry_date)<=date(pt.leave_date_to)
where d_main.entry_date=curdate()
) d_flags
on d_core.employee_name=d_flags.employee_name and d_core.entry_date=d_flags.entry_date
set d_core.tripsheet_flag=d_flags.tripsheet_flag , d_core.odometer_flag=d_flags.tripsheet_flag, d_core.fuellog_flag=d_flags.tripsheet_flag
where d_core.entry_date=curdate();

END