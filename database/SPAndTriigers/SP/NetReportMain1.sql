CREATE DEFINER=`lutransport`@`%` PROCEDURE `NetReportMain1`(IN updateFromDate varchar(200),IN updateToDate varchar(200))
BEGIN

DECLARE done int DEFAULT 0;

DECLARE flag int DEFAULT 0;

DECLARE done_holder int;

DECLARE No_Cal int;






DECLARE ticketIdVal bigint(225);

DECLARE bill_batchs DATETIME;

DECLARE subVoucherDateVal DATETIME;

DECLARE load_dates DATETIME;

DECLARE unload_dates DATETIME;

DECLARE drivercompany_id bigint(100); 

DECLARE compnay_locations bigint(100);

DECLARE terminals bigint(100);

DECLARE drivers bigint(100); 

DECLARE trailers bigint(100); 

DECLARE vehicles bigint(100); 

DECLARE subcontractor_ids bigint(100); 

DECLARE tollAmount  double default 0.0;

DECLARE fuelAmount double default 0.0;

DECLARE subcontractorAmount double default 0.0;

DECLARE billlingNewAmount double default 0.0;

DECLARE miscAmount varchar(225);

DECLARE subVoucherNumberVal varchar(225);

DECLARE fuelLogIDs varchar(225);

DECLARE tollTagIDS varchar(225);




DECLARE drivercompany_name varchar(225);

DECLARE EnteredBy varchar(225);

DECLARE destination_tickets varchar(225);

DECLARE landfill_grosss double;

DECLARE landfill_tares double;

DECLARE landfill_time_ins varchar(225);

DECLARE landfill_time_outs varchar(225);

DECLARE origin_tickets varchar(225);

DECLARE transfer_grosss double;

DECLARE transfer_tares double;

DECLARE transfer_time_ins varchar(225);

DECLARE tranfer_time_outs varchar(225);

DECLARE destinations bigint(225);

DECLARE origins bigint(200);

DECLARE customer_ids varchar(225);

DECLARE ticketstatuss varchar(50);

DECLARE landfill_tons double;

DECLARE transfer_tons double;

DECLARE invoice_numbers varchar(225);

DECLARE landfill_nets double;

DECLARE transfer_nets double;

DECLARE entered_bys varchar(225);

DECLARE invoice_dates varchar(225);

DECLARE voucher_dates varchar(225);

DECLARE voucher_statuss varchar(50);

DECLARE gallons double;

DECLARE TOtAmount double default 0.0;

DECLARE ProcessStatus varchar(225);




DECLARE masterCursor CURSOR for select id,driver_company,entered_by,bill_batch,destination_ticket,landfill_gross,landfill_tare,

landfill_time_in,landfill_time_out,load_date,origin_ticket,transfer_gross,transfer_tare,

tranfer_time_in,tranfer_time_out,unload_date,destination,driver,origin,terminal,

trailer,vehicle,customer_id,ticketstatus,landfill_ton,transfer_ton,subcontractor_id,

invoice_number,landfill_net,transfer_net,entered_by,invoice_date,company_location,

voucher_date,voucher_status,gallon from ticket where bill_batch >= updateFromDate and bill_batch <= updateToDate;



DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;

SET done=0;

OPEN masterCursor;



ticket_loop: LOOP 

FETCH masterCursor into ticketIdVal,drivercompany_id,EnteredBy,bill_batchs,destination_tickets,landfill_grosss,landfill_tares,

landfill_time_ins,landfill_time_outs,load_dates,origin_tickets,transfer_grosss,transfer_tares,

transfer_time_ins,tranfer_time_outs,unload_dates,destinations,drivers,origins,terminals,

trailers,vehicles,customer_ids,ticketstatuss,landfill_tons,transfer_tons,subcontractor_ids,

invoice_numbers,landfill_nets,transfer_nets,entered_bys,invoice_dates,compnay_locations,

voucher_dates,voucher_statuss,gallons;


IF done THEN

CLOSE masterCursor;

LEAVE ticket_loop;

END IF;


SET done_holder=done;

select sum(amount),CAST(GROUP_CONCAT(id SEPARATOR ',') AS CHAR) into fuelAmount,fuelLogIDs from fuel_log where transaction_date >= load_dates and transaction_date <= unload_dates and unit= vehicles;

select sum(amount),CAST(GROUP_CONCAT(id SEPARATOR ',') AS CHAR) into tollAmount,tollTagIDS from ez_toll where transaction_date >= load_dates and transaction_date <= unload_dates and unit= vehicles;

select total_amount into billlingNewAmount from invoice_detail_new where tickt_id = ticketIdVal;

select total_amount into subcontractorAmount from subcontractor_invoice_detail_new where tickt_id = ticketIdVal;

select miscelleneous_charges,sub_voucherdate,voucher_number into miscAmount,subVoucherDateVal,subVoucherNumberVal from subcontractor_invoice_detail where ticket_id = ticketIdVal;

SET done=done_holder;


delete from net_report_main where ticket_id = ticketIdVal;

insert into net_report_main values (0,null,null,null,null,1,bill_batchs,compnay_locations,destinations,drivers,drivercompany_id,fuelLogIDs,fuelAmount,
load_dates,origins,subVoucherDateVal,miscAmount,subcontractor_ids,subcontractorAmount,subVoucherNumberVal,terminals,ticketIdVal,billlingNewAmount,tollTagIDS,tollAmount,
trailers,unload_dates,vehicles,0.0);



SET TotAmount = 0.0;

SET tollAmount = 0.0;

SET fuelAmount = 0.0;

SET subcontractorAmount = 0.0;

SET billlingNewAmount = 0.0;

SET miscAmount = null;

SET subVoucherDateVal = null;

SET subVoucherNumberVal = null;

SET tollTagIDS = null;

SET fuelLogIDs = null;

END LOOP ticket_loop;


END