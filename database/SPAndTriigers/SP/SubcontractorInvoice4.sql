CREATE DEFINER=`lutransport`@`%` PROCEDURE `SubcontractorInvoice4`(IN `tick_id` BIGINT(250), IN `inv_id` BIGINT(250))
BEGIN
DECLARE done int DEFAULT 0;

DECLARE flag int DEFAULT 0;

DECLARE done_holder int;

DECLARE No_Cal int;



DECLARE ticket_id bigint(225);

DECLARE drivercompany_id bigint(225);

DECLARE drivercompany_name varchar(225);

DECLARE EnteredBy varchar(225);

DECLARE bill_batchs DATETIME;

DECLARE destination_tickets varchar(225);

DECLARE landfill_grosss double;

DECLARE landfill_tares double;

DECLARE landfill_time_ins varchar(225);

DECLARE landfill_time_outs varchar(225);

DECLARE load_dates DATETIME;

DECLARE origin_tickets varchar(225);

DECLARE transfer_grosss double;

DECLARE transfer_tares double;

DECLARE transfer_time_ins varchar(225);

DECLARE tranfer_time_outs varchar(225);

DECLARE unload_dates DATETIME;

DECLARE destinations bigint(225);

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

DECLARE subcontravoucherdate DATETIME;

DECLARE voucher_statuss varchar(50);

DECLARE gallons double;

DECLARE locationName varchar(225);

DECLARE OtherCharges double default 0.0;

DECLARE sumTotal double default 0.0;



DECLARE bcompany_locations varchar(225);

DECLARE rate_types int;

DECLARE valid_froms DATETIME;

DECLARE valid_tos DATETIME;

DECLARE bill_usings int;

DECLARE rvalues double;

DECLARE demmurage_charges double;

DECLARE driver_rates double;

DECLARE surcharge_types varchar(225);

DECLARE miless double;



DECLARE owner_rates int;

DECLARE owner_rate_amounts double;

DECLARE pegs double;

DECLARE surcharge_amounts double;

DECLARE landfills varchar(225);

DECLARE padds varchar(225);


DECLARE transfer_stations varchar(225);

DECLARE tonnage_premiums bigint;

DECLARE surcharge_per_tons double;

DECLARE min_billable_gross_weights double;

DECLARE padd_usings int;

DECLARE fuel_surcharge_weeklyrates int;

DECLARE weekly_rate_usings int;

DECLARE billed_bys varchar(225);

DECLARE rate_usings int;

DECLARE sort_bys int;



DECLARE EffectiveGrossWt double default 0.0;

DECLARE Minimumbillablegrossweight double;

DECLARE EffectiveTareWt double default 0.0;

DECLARE originNetWt double default 0.0;

DECLARE EffectiveNetWt double default 0.0;

DECLARE EffectiveTonsWt double default 0.0;

DECLARE destinationNetWt double;

DECLARE Rate double;

DECLARE SubRate double default 0.0;

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

DECLARE miscelleneousCharges varchar(225);

DECLARE miscelleneousChargesSum double default 0.0;

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

DECLARE subvoucherNumber varchar(225);

DECLARE SubcontractorName varchar(450);

DECLARE TrailerNumber varchar(225);

DECLARE UnitNumber varchar(225);

DECLARE subcont_id bigint(150);

DECLARE Company_Name varchar(225);

DECLARE Customer_Names varchar(225);

DECLARE  Load_date_var varchar(225);

DECLARE  Unload_date_var varchar(225);

DECLARE subvoucherdate_VAR varchar(225);

DECLARE  bill_batch_date_var varchar(225);

DECLARE sign_value int;

DECLARE fuel_surcharge_amounts double default 0.0;



DECLARE masterCursor CURSOR for select id,driver_company,entered_by,bill_batch,destination_ticket,landfill_gross,landfill_tare,

landfill_time_in,landfill_time_out,load_date,origin_ticket,transfer_gross,transfer_tare,

tranfer_time_in,tranfer_time_out,unload_date,destination,driver,origin,terminal,

trailer,vehicle,customer_id,ticketstatus,landfill_ton,transfer_ton,subcontractor_id,

invoice_number,landfill_net,transfer_net,entered_by,invoice_date,company_location,

voucher_status,gallon from ticket where id=tick_id;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;



SET done=0;



OPEN masterCursor;



ticket_loop: LOOP 

FETCH masterCursor into ticket_id,drivercompany_id,EnteredBy,bill_batchs,destination_tickets,landfill_grosss,landfill_tares,

landfill_time_ins,landfill_time_outs,load_dates,origin_tickets,transfer_grosss,transfer_tares,

transfer_time_ins,tranfer_time_outs,unload_dates,destinations,drivers,origins,terminals,

trailers,vehicles,customer_ids,ticketstatuss,landfill_tons,transfer_tons,subcontractor_ids,

invoice_numbers,landfill_nets,transfer_nets,entered_bys,invoice_dates,compnay_locations,

voucher_statuss,gallons;


IF done THEN

CLOSE masterCursor;

LEAVE ticket_loop;

END IF;

SET done_holder=done;


select sub_voucherdate,company_location,demmurage_charge,minimum_billablewt,tonnage_premium,sbrate,other_charges,total_amount,
miscelleneous_charges,bill_gross,bill_net,bill_tare,bill_tons,amount,voucher_number
,fuel_surcharge  into subcontravoucherdate,bcompany_locations,DemmurageCharge,min_billable_gross_weights,TonnagePremium ,
SubRate,OtherCharges,sumTotal,miscelleneousCharges,EffectiveGrossWt ,EffectiveNetWt ,
EffectiveTareWt,EffectiveTonsWt,FAmount,subvoucherNumber,fuel_surcharge_amounts from subcontractor_invoice_detail where id=inv_id;




SET ProcessStatus='Paid';

IF bcompany_locations IS NOT NULL THEN

select name into Company_Name from location where id=bcompany_locations;

END IF;

IF bcompany_locations IS NULL THEN

SET bcompany_locations = 0;

END IF;


select name into SubcontractorName from subcontractor where id=subcontractor_ids; 

select username into EnteredByName from user_info where name=EnteredBy;

select full_name into DriverName from driver where id=drivers;

select name into TerminalName from location where id=terminals;

select unit into UnitNumber  from vehicle where id=vehicles;

select unit into TrailerNumber from vehicle where id=trailers;
select name into drivercompany_name from location where id=drivercompany_id;
select name into OriginName from location where id=origins;

select name into DestinationName from location where id=destinations;

SET done=done_holder;


SET Load_date_var=DATE_FORMAT(load_dates,'%m-%d-%Y');

SET Unload_date_var=DATE_FORMAT(unload_dates,'%m-%d-%Y');

SET bill_batch_date_var=DATE_FORMAT(bill_batchs,'%m-%d-%Y');


SET subvoucherdate_VAR=DATE_FORMAT(subcontravoucherdate,'%m-%d-%Y');



insert into subcontractor_invoice_detail_new values(0,null,null,null,null,1,FAmount ,fBill_using,Company_Name,bcompany_locations,bill_batchs,DemmurageCharge ,landfill_grosss ,landfill_nets ,
landfill_tares ,destination_tickets ,landfill_tons ,DriverName ,EffectiveGrossWt ,EffectiveNetWt ,
EffectiveTareWt,EffectiveTonsWt ,EnteredByName ,fuel_surcharge_amounts ,subvoucherdate_VAR,subvoucherNumber,landfill_time_ins ,
landfill_time_outs,Load_date_var ,min_billable_gross_weights ,miscelleneousCharges,
transfer_grosss ,transfer_nets ,transfer_tares ,origin_tickets ,transfer_tons ,
OtherCharges ,ProcessStatus,DestinationName ,OriginName,UnitNumber,SubcontractorName ,subcontractor_ids ,SubRate,TerminalName,ticket_id ,TonnagePremium ,sumTotal ,
TrailerNumber,transfer_time_ins ,tranfer_time_outs ,Unload_date_var,drivercompany_id,unload_dates);




SET subcont_id=null;

SET fuel_surcharge_amounts=0.0;

SET DemmurageCharge=0.0;

SET sumTotal=0.0;

SET TotAmount=0.0;

SET TonnagePremium=0.0;

SET fuelSurcharge=0.0;

SET OtherCharges=0.0;
SET drivercompany_name=null;
SET SubcontractorName=null;

SET Customer_Names=null;

SET EnteredByName=null;

SET Company_Name=null;

SET UnitNumber=null;

SET DriverName=null;

SET TerminalName=null;

SET OriginName=null;

SET DestinationName=null;


SET EffectiveGrossWt=0.0;

SET EffectiveNetWt=0.0;

SET EffectiveTareWt=0.0;

SET EffectiveTonsWt=0.0;

SET originNetWt=0.0;

SET destinationNetWt=0.0;

SET Minimumbillablegrossweight=0.0;

SET sign=1;

SET Padd=0.0;

SET FuelSurchargeRate=0.0;

SET fsw_rateType=555;

SET effectiveDatePadd=null;

SET PaddCode=null;

SET effectiveDate=null;

SET FAmount=0.0;

SET Rate=0.0;


END LOOP ticket_loop;
END