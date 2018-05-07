CREATE DEFINER=`lutransport`@`%` PROCEDURE `BillingHistoryProc7`(IN tick_id bigint(150))
BEGIN
DECLARE tickets_id bigint(225);

DECLARE drivercompany_id bigint(225);

DECLARE drivercompany_name varchar(225);

DECLARE destination_tickets varchar(225);

DECLARE landfill_time_ins varchar(225);

DECLARE landfill_time_outs varchar(225);

DECLARE load_dates varchar(225);

DECLARE origin_tickets varchar(225);

DECLARE transfer_time_ins varchar(225);

DECLARE tranfer_time_outs varchar(225);

DECLARE unload_dates varchar(225);

DECLARE destinations bigint(200);

DECLARE drivers varchar(225);

DECLARE origins bigint(200); 

DECLARE terminals varchar(225);

DECLARE trailers bigint(100); 

DECLARE vehicles bigint(100);

DECLARE customer_ids varchar(225);

DECLARE ticketstatuss varchar(50);

DECLARE transfer_tons double;

DECLARE subcontractor_ids varchar(200);

DECLARE bill_history_id bigint(150);

DECLARE  Unload_date_var varchar(225);

DECLARE DriverName varchar(225);

DECLARE TerminalName varchar(225);

DECLARE SubcontractorName varchar(450);

DECLARE TrailerNumber varchar(225);

DECLARE UnitNumber varchar(225);

DECLARE driverPayRate double default 0.0;


select driver_company,driver_payrate,destination_ticket,landfill_time_in,landfill_time_out,origin_ticket,tranfer_time_in,
tranfer_time_out,unload_date,driver,terminal,trailer,vehicle,subcontractor_id into drivercompany_id,driverPayRate,destination_tickets,
landfill_time_ins,landfill_time_outs,origin_tickets,transfer_time_ins,tranfer_time_outs,unload_dates,
drivers,terminals,trailers,vehicles,subcontractor_ids from ticket where id=tick_id and ticketstatus=2;

IF driverPayRate is null THEN
SET driverPayRate = 0.0;
END IF;

SET Unload_date_var=DATE_FORMAT(unload_dates,'%m-%d-%Y');

select name into SubcontractorName from subcontractor where id=subcontractor_ids; 

select full_name into DriverName from driver where id=drivers;

select unit into UnitNumber  from vehicle where id=vehicles;

select name into drivercompany_name from location where id=drivercompany_id;

select unit into TrailerNumber from vehicle where id=trailers;

select name into TerminalName from location where id=terminals;


SELECT id into bill_history_id from invoice_detail_new where tickt_id=tick_id;



update invoice_detail_new set destination_ticket=destination_tickets,landfill_time_in=landfill_time_ins,
landfill_time_out=landfill_time_outs,origin_ticket=origin_tickets,transfer_time_in=transfer_time_ins,transfer_time_out=tranfer_time_outs,
unloaded=Unload_date_var,t_driver=DriverName,t_terminal=TerminalName,trailer_num=TrailerNumber,t_unit=UnitNumber,bill_subcontractor=SubcontractorName,driver_companylocname=drivercompany_name,driver_companylocid=drivercompany_id,unload_date=unload_dates,driver_pay_rate=driverPayRate
where id=bill_history_id;



SET bill_history_id=null;

SET Unload_date_var=null;

SET drivercompany_name=null;

SET TrailerNumber=null;

SET UnitNumber=null;

SET DriverName=null;

SET TerminalName=null;

SET SubcontractorName=null;
END