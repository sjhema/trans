| 256406 | lutransport | %:56974   | lutransport | Execute | 11274 | Copying to tmp table | insert into driver_net_report
select 0,CURDATE(),null,null,null,1,0.0,coalesce(rev.driver_payamount,0.0),loc.id,loc.name,driver_fullname,0.0,coalesce(rev.revenue_amount,0.0),0.0,0.0,report_date,null
from driver_revenueamount rev
left join driver_net_report net
on net.employee_company_id=rev.location_id and net.unload_date = rev.report_date and net.employee_name=rev.driver_fullname
join location loc
on rev.location_id=loc.id
where net.employee_name is null |
| 256570 | root        | localhost | NULL        | Query   |     0 | NULL                 | show full processlist    

show full processlist 
Select concat('KILL ',id,';') from information_schema.processlist    

explain extended select 0,CURDATE(),null,null,null,1,0.0,coalesce(rev.driver_payamount,0.0),loc.id,loc.name,driver_fullname,0.0,coalesce(rev.revenue_amount,0.0),0.0,0.0,report_date,null
from driver_revenueamount rev force index(Index_3)
left join driver_net_report net
on net.employee_company_id=rev.location_id and net.unload_date = rev.report_date and net.employee_name=rev.driver_fullname
join location loc
on rev.location_id=loc.id
where net.employee_name is null

force index(Index_3)
use index(Index_3)

analyze table driver_net_report
optimize table lutransport.driver_net_report
optimize table lutransport.driver_revenueamount
optimize table driver_fuelamount
optimize table driver_tollamount
optimize table driver_list_tbl