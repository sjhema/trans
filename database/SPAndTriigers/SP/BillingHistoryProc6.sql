CREATE DEFINER=`lutransport`@`%` PROCEDURE `BillingHistoryProc6`(IN tick_id bigint(250),IN inv_id bigint(250))
BEGIN

DECLARE done int DEFAULT 0;

DECLARE flag int DEFAULT 0;

DECLARE done_holder int;

DECLARE No_Cal int;



DECLARE tickets_id bigint(225);

DECLARE drivercompany_id bigint(225);

DECLARE drivercompany_name varchar(225);

DECLARE EnteredBy varchar(225);

DECLARE bill_batchs varchar(225);

DECLARE destination_tickets varchar(225);

DECLARE landfill_grosss double;

DECLARE landfill_tares double;

DECLARE landfill_time_ins varchar(225);

DECLARE landfill_time_outs varchar(225);

DECLARE load_dates varchar(225);

DECLARE origin_tickets varchar(225);

DECLARE transfer_grosss double;

DECLARE transfer_tares double;

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

DECLARE landfill_tons double;

DECLARE transfer_tons double;

DECLARE subcontractor_ids varchar(200);

DECLARE invoice_numbers varchar(225);

DECLARE landfill_nets double;

DECLARE transfer_nets double;

DECLARE entered_bys varchar(225);

DECLARE invoice_dates varchar(225);

DECLARE compnay_locations varchar(200);

DECLARE voucher_dates varchar(225);

DECLARE voucher_statuss varchar(50);

DECLARE gallons double;

DECLARE locationName varchar(225);

DECLARE driverPayRate double default 0.0;


DECLARE EffectiveGrossWt double default 0.0;

DECLARE Minimumbillablegrossweight double;

DECLARE EffectiveTareWt double default 0.0;

DECLARE originNetWt double;

DECLARE EffectiveNetWt double default 0.0;

DECLARE EffectiveTonsWt double default 0.0;

DECLARE destinationNetWt double;

DECLARE FRate double;

DECLARE FAmount double default 0.0;

DECLARE fuelSurcharge double default 0.0; 

DECLARE Padd double default 0.0;

DECLARE effectiveDate varchar(225);

DECLARE PaddCode varchar(225);

DECLARE sign int default 1;

DECLARE term double;

DECLARE Percentage double;

DECLARE effectiveDatePadd varchar(225);

DECLARE FuelSurchargeRate double;

DECLARE fsw_rateType int;

DECLARE TonaageCode varchar(225);

DECLARE TonnageRate double;

DECLARE PremiumTonne double;

DECLARE TonnagePremium double default 0.0;

DECLARE DemmurageCharge double default 0.0;

DECLARE TOtAmount double default 0.0;

DECLARE fBill_using varchar(225);

DECLARE DestinationName varchar(225);

DECLARE OriginName varchar(225);

DECLARE DriverName varchar(225);

DECLARE TerminalName varchar(225);

DECLARE EnteredByName varchar(225);

DECLARE ProcessStatus varchar(225);

DECLARE SubcontractorName varchar(450);

DECLARE TrailerNumber varchar(225);

DECLARE UnitNumber varchar(225);

DECLARE bill_history_id bigint(150);

DECLARE Company_Name varchar(225);

DECLARE Customer_Names varchar(225);


DECLARE  Load_date_var varchar(225);

DECLARE  Unload_date_var varchar(225);

DECLARE  bill_batch_date_var varchar(225);

DECLARE isPeakRate varchar(15);


DECLARE masterCursor CURSOR for select id,driver_company,driver_payrate,entered_by,bill_batch,destination_ticket,landfill_gross,landfill_tare,

landfill_time_in,landfill_time_out,load_date,origin_ticket,transfer_gross,transfer_tare,

tranfer_time_in,tranfer_time_out,unload_date,destination,driver,origin,terminal,

trailer,vehicle,ticketstatus,landfill_ton,transfer_ton,subcontractor_id,

landfill_net,transfer_net,entered_by,

voucher_date,voucher_status,gallon from ticket where id=tick_id ;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;



SET done=0;



OPEN masterCursor;

ticket_loop: LOOP 

FETCH masterCursor into tickets_id,drivercompany_id,driverPayRate,EnteredBy,bill_batchs,destination_tickets,landfill_grosss,landfill_tares,

landfill_time_ins,landfill_time_outs,load_dates,origin_tickets,transfer_grosss,transfer_tares,

transfer_time_ins,tranfer_time_outs,unload_dates,destinations,drivers,origins,terminals,

trailers,vehicles,ticketstatuss,landfill_tons,transfer_tons,subcontractor_ids,

landfill_nets,transfer_nets,entered_bys,

voucher_dates,voucher_statuss,gallons;

IF done THEN

CLOSE masterCursor;

LEAVE ticket_loop;

END IF;



SET done_holder=done;

select amount,demmurage_charge,fuel_surcharge,minimum_billablewt,

rate,tonnage_premium,total_amount,bill_gross,bill_net,bill_tare,bill_tons,invoice_date,
invoice_number,company_loc,custmr_id,is_peak_rate

into FAmount,DemmurageCharge,fuelSurcharge,Minimumbillablegrossweight,FRate,

TonnagePremium,TOtAmount,EffectiveGrossWt,EffectiveNetWt,EffectiveTareWt,EffectiveTonsWt,invoice_dates,
invoice_numbers,compnay_locations,customer_ids,isPeakRate

from invoice_detail where id=inv_id;

SET ProcessStatus='Invoiced';

IF customer_ids IS NOT NULL THEN

select name into Customer_Names from customer where id=customer_ids;

END IF;

IF compnay_locations IS NOT NULL THEN

select name into Company_Name from location where id=compnay_locations;

END IF;

IF driverPayRate is null THEN
SET driverPayRate = 0.0;
END IF;

select name into SubcontractorName from subcontractor where id=subcontractor_ids; 

select username into EnteredByName from user_info where name=EnteredBy;

select unit into TrailerNumber from vehicle where id=trailers;

select name into TerminalName from location where id=terminals;

select full_name into DriverName from driver where id=drivers;

select name into drivercompany_name from location where id=drivercompany_id;

select unit into UnitNumber  from vehicle where id=vehicles;

select name into OriginName from location where id=origins;

select name into DestinationName from location where id=destinations;

SET done=done_holder;


SET Load_date_var=DATE_FORMAT(load_dates,'%m-%d-%Y');

SET Unload_date_var=DATE_FORMAT(unload_dates,'%m-%d-%Y');

SET bill_batch_date_var=DATE_FORMAT(bill_batchs,'%m-%d-%Y');




insert into invoice_detail_new values(0,null,null,null,null,1,FAmount,fBill_using,bill_batch_date_var,Company_Name,Customer_Names,DemmurageCharge,landfill_grosss,

landfill_nets,landfill_tares,destination_tickets,landfill_tons,DriverName,EffectiveGrossWt,EffectiveNetWt,EffectiveTareWt,EffectiveTonsWt,EnteredByName,

fuelSurcharge,gallons,invoice_dates,invoice_numbers,landfill_time_ins,landfill_time_outs,Load_date_var,Minimumbillablegrossweight,

transfer_grosss,transfer_nets,transfer_tares,origin_tickets,transfer_tons,ProcessStatus,FRate,SubcontractorName,DestinationName,

OriginName,UnitNumber,TerminalName,tickets_id,TonnagePremium,TotAmount,TrailerNumber,transfer_time_ins,
tranfer_time_outs,Unload_date_var,null,destinations,origins,drivercompany_name,drivercompany_id,
unload_dates,driverPayRate,isPeakRate);


SET SubcontractorName=null;

SET Customer_Names=null;

SET EnteredByName=null;

SET Company_Name=null;

SET TrailerNumber=NULL;

SET UnitNumber=null;

SET DriverName=null;

SET drivercompany_name=null;

SET TerminalName=null;

SET OriginName=null;

SET DestinationName=null;

SET Load_date_var=null;

SET Unload_date_var=null;

SET bill_batch_date_var=null;


END LOOP ticket_loop;

END