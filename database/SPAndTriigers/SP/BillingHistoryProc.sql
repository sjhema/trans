CREATE DEFINER=`lutransport`@`%` PROCEDURE `BillingHistoryProc`()
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

DECLARE origins bigint(225);

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

DECLARE originNetWt double;

DECLARE EffectiveNetWt double default 0.0;

DECLARE EffectiveTonsWt double default 0.0;

DECLARE destinationNetWt double default 0.0;

DECLARE Rate double;

DECLARE FAmount double;

DECLARE fuelSurcharge double default 0.0;

DECLARE Padd double default 0.0;

DECLARE effectiveDate DATETIME;

DECLARE PaddCode varchar(225);

DECLARE sign int default 1;

DECLARE term double;

DECLARE Percentage double;

DECLARE effectiveDatePadd DATETIME;

DECLARE FuelSurchargeRate double;

DECLARE fsw_rateType int;

DECLARE TonaageCode varchar(225);

DECLARE TonnageRate double;

DECLARE PremiumTonne double;

DECLARE TonnagePremium double;

DECLARE DemmurageCharge double;

DECLARE TOtAmount double;

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

DECLARE Company_Name varchar(225);

DECLARE Customer_Names varchar(255) default '';

DECLARE  Load_date_var varchar(225);

DECLARE  Unload_date_var varchar(225);

DECLARE  bill_batch_date_var varchar(225);

DECLARE sign_value int;



DECLARE masterCursor CURSOR for select id,driver_company,driver_payrate,entered_by,bill_batch,destination_ticket,landfill_gross,landfill_tare,

landfill_time_in,landfill_time_out,load_date,origin_ticket,transfer_gross,transfer_tare,

tranfer_time_in,tranfer_time_out,unload_date,destination,driver,origin,terminal,

trailer,vehicle,customer_id,ticketstatus,landfill_ton,transfer_ton,subcontractor_id,

invoice_number,landfill_net,transfer_net,entered_by,invoice_date,company_location,

voucher_date,voucher_status,gallon from ticket where id in (304161);
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;



SET done=0;



OPEN masterCursor;



ticket_loop: LOOP 

FETCH masterCursor into ticket_id,drivercompany_id,driverPayRate,EnteredBy,bill_batchs,destination_tickets,landfill_grosss,landfill_tares,

landfill_time_ins,landfill_time_outs,load_dates,origin_tickets,transfer_grosss,transfer_tares,

transfer_time_ins,tranfer_time_outs,unload_dates,destinations,drivers,origins,terminals,

trailers,vehicles,customer_ids,ticketstatuss,landfill_tons,transfer_tons,subcontractor_ids,

invoice_numbers,landfill_nets,transfer_nets,entered_bys,invoice_dates,compnay_locations,

voucher_dates,voucher_statuss,gallons;

IF done THEN

CLOSE masterCursor;

LEAVE ticket_loop;

END IF;

SET No_Cal=1;

SET done_holder=done;

select name into OriginName from location where id=origins;

select name into DestinationName from location where id=destinations;

SET done=done_holder;

IF DestinationName='grows' || DestinationName='tullytown'  then

SET destinations=91;

END IF;

BLOCK2: BEGIN

    DECLARE frstCursor CURSOR for select company_location,rate_type,

    valid_from,valid_to,bill_using,rvalue,demmurage_charge,driver_rate,surcharge_type,

    miles,owner_rate,owner_rate_amount,peg,surcharge_amount,landfill,fuelpadd,transfer_station,

    tonnage_premium,surcharge_per_ton,min_billable_gross_weight,padd_using,weekly_rate_using,

    rate_using,billed_by,sort_by,fuel_surcharge_weeklyrate,customer_name

    from billing_rate where transfer_station=origins and landfill=destinations;

    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET flag=1;

    SET flag=0;

    OPEN frstCursor;

    rate_loop: LOOP

    FETCH frstCursor into bcompany_locations,rate_types,

    valid_froms,valid_tos,bill_usings,rvalues,demmurage_charges,driver_rates,surcharge_types,

    miless,owner_rates,owner_rate_amounts,pegs,surcharge_amounts,landfills,padds,transfer_stations,

    tonnage_premiums,surcharge_per_tons,min_billable_gross_weights,padd_usings,weekly_rate_usings,

    rate_usings,billed_bys,sort_bys,fuel_surcharge_weeklyrates,customer_ids;

    IF flag THEN

    CLOSE frstCursor;

    LEAVE rate_loop;

    END IF;

    IF rate_usings IS NULL THEN   

    SET No_Cal=1;
    
    CLOSE frstCursor;

    LEAVE rate_loop;

    ELSEIF  rate_usings=1 THEN

    IF load_dates >= valid_froms && load_dates <= valid_tos THEN

    SET No_Cal=0;
    
    CLOSE frstCursor;

    LEAVE rate_loop;

    END IF;

    ELSEIF rate_usings=2 THEN

    IF unload_dates >= valid_froms && unload_dates <= valid_tos THEN

    SET No_Cal=0;
    
    CLOSE frstCursor;

    LEAVE rate_loop;

     END IF;

    END IF;

    END LOOP rate_loop;

END BLOCK2;

IF No_Cal !=1 THEN 

IF bill_usings=1 THEN

SET fBill_using='Transfer';

END IF;

IF bill_usings=2 THEN

SET fBill_using='Landfill';

END IF;

IF fBill_using='Transfer' THEN

IF rate_types=2 || rate_types=3 THEN

IF min_billable_gross_weights IS NOT NULL && transfer_grosss < min_billable_gross_weights THEN 

SET EffectiveGrossWt=min_billable_gross_weights;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=transfer_tares;

SET originNetWt=min_billable_gross_weights-transfer_tares;

SET EffectiveNetWt=originNetWt;

SET EffectiveTonsWt=originNetWt/2000.0;

ELSE

SET EffectiveGrossWt=transfer_grosss;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=transfer_tares;

SET EffectiveNetWt=transfer_nets;

SET EffectiveTonsWt=transfer_tons;

END IF;

END IF;

ELSE

IF rate_types=2 || rate_types=3 THEN

IF min_billable_gross_weights IS NOT NULL && landfill_grosss < min_billable_gross_weights THEN 

SET EffectiveGrossWt=min_billable_gross_weights;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=landfill_tares;

SET destinationNetWt=min_billable_gross_weights-landfill_tares;

SET EffectiveNetWt=destinationNetWt;

SET EffectiveTonsWt=destinationNetWt/2000.0;

ELSE

SET EffectiveGrossWt=landfill_grosss;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=landfill_tares;

SET EffectiveNetWt=landfill_nets;

SET EffectiveTonsWt=landfill_tons;

END IF;

END IF;

END IF;



IF rate_types=1 THEN

IF fBill_using='Transfer' THEN

IF min_billable_gross_weights IS NOT NULL && transfer_grosss < min_billable_gross_weights THEN 

SET EffectiveGrossWt=min_billable_gross_weights;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=transfer_tares;

SET originNetWt=min_billable_gross_weights-transfer_tares;

SET EffectiveNetWt=originNetWt;

ELSE

SET EffectiveGrossWt=transfer_grosss;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=transfer_tares;

SET EffectiveNetWt=transfer_nets;

END IF;

ELSE

IF min_billable_gross_weights IS NOT NULL && landfill_grosss < min_billable_gross_weights THEN 

SET EffectiveGrossWt=min_billable_gross_weights;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=landfill_tares;

SET destinationNetWt=min_billable_gross_weights-landfill_tares;

SET EffectiveNetWt=destinationNetWt;

ELSE

SET EffectiveGrossWt=landfill_grosss;

SET Minimumbillablegrossweight=min_billable_gross_weights;

SET EffectiveTareWt=landfill_tares;

SET EffectiveNetWt=landfill_nets;

END IF;

END IF;

SET Rate=rvalues;

SET FAmount=(EffectiveNetWt/8.34) * rvalues;

ELSEIF rate_types=2 THEN

SET Rate=rvalues;

SET FAmount=rvalues;

ELSEIF rate_types=3 THEN

SET Rate=rvalues;

SET FAmount=EffectiveTonsWt * rvalues;

END IF;



IF surcharge_types='N' THEN

SET fuelSurcharge=0.0;

END IF;

IF surcharge_types='M' THEN

IF surcharge_per_tons IS NULL && surcharge_amounts IS NULL THEN

SET fuelSurcharge=0.0;

ELSEIF surcharge_per_tons IS NOT NULL THEN

SET fuelSurcharge=surcharge_per_tons*EffectiveTonsWt;

ELSE

SET fuelSurcharge=surcharge_amounts;

END IF;

END IF;



IF surcharge_types='A' THEN

IF padds IS NOT NULL THEN

IF padd_usings IS NOT NULL THEN

SET done_holder=done;

select tcode into PaddCode from fuel_surcharge_padd f where f.id=padds LIMIT 0,1;

SET done=done_holder;

IF padd_usings=1 THEN

SET effectiveDate=load_dates;

ELSEIF padd_usings=2 THEN

SET effectiveDate=unload_dates;

ELSEIF padd_usings=3 THEN

SET effectiveDate=bill_batchs;

END IF;

END IF;



IF effectiveDate IS NOT NULL THEN

SET done_holder=done;

select amount into Padd from fuel_surcharge_padd fp  where fp.tcode=PaddCode and fp.valid_from<=effectiveDate and fp.valid_to >=effectiveDate LIMIT 0,1;

SET done=done_holder;

END IF;

IF  Padd != -1 && pegs IS NOT NULL && miless IS NOT NULL THEN

SET sign_value=Padd-pegs;

IF sign_value < 0 THEN

SET sign=-1;

END IF;

SET term=FLOOR(sign * (Padd -pegs) /0.05);

SET fuelSurcharge=sign * term * miless *0.01;

END IF;

IF  Padd != -1 && pegs IS NOT NULL && miless IS NULL THEN

SET Percentage=FLOOR((Padd -pegs)/0.08);

IF rate_types=2 THEN

SET fuelSurcharge=Percentage * rvalues * 0.01;

ELSEIF rate_types=3 THEN

SET fuelSurcharge= EffectiveTonsWt*rvalues*0.01* Percentage;

END IF;

END IF;

END IF;

IF fuel_surcharge_weeklyrates=1 THEN

IF weekly_rate_usings IS NOT NULL THEN

IF weekly_rate_usings=1  THEN

SET effectiveDatePadd=load_dates;

END IF;

IF weekly_rate_usings=2 THEN

SET effectiveDatePadd=unload_dates;

END IF;

IF weekly_rate_usings=3 THEN 

SET effectiveDatePadd=bill_batchs;

END IF;

SET done_holder=done;

select fuel_surcharge_rate,rate_type into FuelSurchargeRate,fsw_rateType from fuel_surcharge_weeklyrate where

transfer_station=origins  and landfill_station=destinations  and 

from_date<=effectiveDatePadd and to_date>=effectiveDatePadd LIMIT 0,1;

SET done=done_holder;

END IF;

IF FuelSurchargeRate IS NOT NULL THEN

IF fsw_rateType=3 THEN

SET fuelSurcharge=EffectiveTonsWt * FuelSurchargeRate;

END IF;

IF fsw_rateType=2 THEN

SET fuelSurcharge=FuelSurchargeRate;

END IF;

IF fsw_rateType=5 THEN

SET fuelSurcharge=FuelSurchargeRate * FAmount;

END IF;

IF fsw_rateType=1 THEN

SET fuelSurcharge=(EffectiveNetWt/8.34) *FuelSurchargeRate;

END IF;

END IF;

END IF;

END IF;


SET done_holder=done;

select premium_tonne,rate,tcode into PremiumTonne,TonnageRate,TonaageCode from tonnage_premium where id=tonnage_premiums LIMIT 0,1;

SET done=done_holder;

IF TonaageCode IS NOT NULL THEN

IF EffectiveTonsWt > PremiumTonne THEN

SET TonnagePremium=(EffectiveTonsWt - PremiumTonne) * TonnageRate;

END IF;

ELSE

SET TonnagePremium=0.0;

END IF;

IF demmurage_charges!=0.0 THEN

SET DemmurageCharge=demmurage_charges;

ELSE

SET DemmurageCharge=0.0;

END IF;

ELSE 

SET rvalues=0.0;

SET FAmount=0.0;

SET fuelSurcharge=0.0;

SET DemmurageCharge=0.0;

SET TonnagePremium=0.0;

SET EffectiveGrossWt=0.0;

SET EffectiveNetWt=0.0;

SET EffectiveTareWt=0.0;

SET EffectiveTonsWt=0.0;

END IF;


SET TotAmount=FAmount+DemmurageCharge+TonnagePremium+fuelSurcharge;

SET Load_date_var=DATE_FORMAT(load_dates,'%m-%d-%Y');

SET Unload_date_var=DATE_FORMAT(unload_dates,'%m-%d-%Y');

SET bill_batch_date_var=DATE_FORMAT(bill_batchs,'%m-%d-%Y');



SET done_holder=done;

IF compnay_locations IS NOT NULL THEN

select name into Company_Name from location where id=compnay_locations;

END IF;

IF customer_ids IS NOT NULL THEN

select name into Customer_Names from customer where id=customer_ids;

END IF;


IF driverPayRate is null THEN
SET driverPayRate = 0.0;
END IF;

select name into SubcontractorName from subcontractor where id=subcontractor_ids; 

select data_text into ProcessStatus from static_data where data_type='TICKET_STATUS' and data_value=ticketstatuss;

select username into EnteredByName from user_info where name=EnteredBy;

select full_name into DriverName from driver where id=drivers;

select name into drivercompany_name from location where id=drivercompany_id;

select unit into UnitNumber  from vehicle where id=vehicles;

select unit into TrailerNumber from vehicle where id=trailers;

select name into TerminalName from location where id=terminals;

SET done=done_holder;

insert into invoice_detail_new values(0,null,null,null,null,1,FAmount,fBill_using,bill_batch_date_var,Company_Name,Customer_Names,DemmurageCharge,landfill_grosss,

landfill_nets,landfill_tares,destination_tickets,landfill_tons,DriverName,EffectiveGrossWt,EffectiveNetWt,EffectiveTareWt,EffectiveTonsWt,EnteredByName,

fuelSurcharge,gallons,invoice_dates,invoice_numbers,landfill_time_ins,landfill_time_outs,Load_date_var,min_billable_gross_weights,

transfer_grosss,transfer_nets,transfer_tares,origin_tickets,transfer_tons,ProcessStatus,rvalues,SubcontractorName,DestinationName,

OriginName,UnitNumber,TerminalName,ticket_id,TonnagePremium,TotAmount,TrailerNumber,transfer_time_ins,tranfer_time_outs,Unload_date_var,PaddCode,destinations,origins,drivercompany_name,drivercompany_id,unload_dates,driverPayRate);



SET DemmurageCharge=0.0;

SET TotAmount=0.0;

SET TonnagePremium=0.0;
SET fuelSurcharge=0.0;

SET SubcontractorName=null;

SET Customer_Names=null;

SET EnteredByName=null;

SET Company_Name=null;

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

SET UnitNumber=null;

SET DriverName=null;

SET TerminalName=null;

SET OriginName=null;

SET DestinationName=null;

END LOOP ticket_loop;

END