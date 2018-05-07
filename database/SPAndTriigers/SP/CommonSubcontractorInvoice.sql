CREATE DEFINER=`lutransport`@`%` PROCEDURE `CommonSubcontractorInvoice`()
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

DECLARE voucher_dates varchar(225);

DECLARE voucher_statuss varchar(50);

DECLARE gallons double;

DECLARE locationName varchar(225);

DECLARE OtherCharges double;

DECLARE sumTotal double;



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

DECLARE subcont_id bigint(150);

DECLARE Company_Name varchar(225);

DECLARE Customer_Names varchar(225);

DECLARE  Load_date_var varchar(225);

DECLARE  Unload_date_var varchar(225);

DECLARE  bill_batch_date_var varchar(225);

DECLARE sign_value int;

DECLARE fuel_surcharge_amounts double;



DECLARE masterCursor CURSOR for select id,driver_company,entered_by,bill_batch,destination_ticket,landfill_gross,landfill_tare,

landfill_time_in,landfill_time_out,load_date,origin_ticket,transfer_gross,transfer_tare,

tranfer_time_in,tranfer_time_out,unload_date,destination,driver,origin,terminal,

trailer,vehicle,customer_id,ticketstatus,landfill_ton,transfer_ton,subcontractor_id,

invoice_number,landfill_net,transfer_net,entered_by,invoice_date,company_location,

voucher_date,voucher_status,gallon from ticket where id in (151489,151490,151491,151492,151493,151494,151495,151496,151497,151498,151499,151500,164574,164575,164576,164577,164578,164579,164580,164581,164582,164583,164584,164585,164586,164587,164588,176061,365961,365962,365963,365964,365965,365966,365967,365968,365969,365970,365971,366033,366034,366035,366036,366037,366038,366039,366040,366041,366042,70643,70644,70645,70646,70647,74183,74184,74185,74186,74187,74188,74189,84634,84635,84636,84637,84638,84639,84640,84641,87159,87160,87161,87162,87163,87164,87165,87166,87167,87168,89638,89639,89640,89641,89642,89643,89644,89645,89646,89647,91605,91606,91607,91608,91609,91610,91611,91612,91613,91614,94729,94730,94731,94732,94733,94734,94735,94736,94737,94738,94739,94740,98258,98259,98260,98261,98262,98263,98264,98265,98266,98267,100942,100943,100944,100945,100946,100947,100948,100949,100950,103451,103452,103453,103454,103455,103456,103457,103458,103459,103460,106091,106092,106093,106094,106095,106096,106097,106098,108889,108890,108891,108892,108893,108894,108895,108896,108897,108898,111586,111587,111588,111589,111590,111591,111592,111593,111594,114404,114405,114406,114407,114408,114409,114410,117078,117079,117080,117081,117082,117083,117084,117085,117086,366025,366026,366027,366028,366029,366030,366031,366032,153426,153427,153428,153429,153430,153431,157646,157647,157648,157649,157650,157651,157652,157653,171753,164474,164475,167680,365913,365914,365915,365916,365917,365918,365919,365928,365929,365930,365931,365932,365933,365934,366052,366053,366054,366055,366056,366057,366058,366059,366060,366061,366062,366063,366064,366065,366066,330333,330334,330335,330336,330337,330338,330339,330340,365920,365921,365922,365923,365924,365925,365926,365927,365990,365991,365992,365993,365994,365995,365996,365997,365998,365999,366000,366001,366002,366003,366004,366005,365972,365973,365974,365975,365976,365977,365978,365979,365980,365935,365936,365937,365938,365939,365940,365941,365942,365943,365944,365945,365946,365947,365948,365949,365950,365951,365952,365953,365954,365955,365956,365957,365958,365959,365960,365981,365982,365983,365984,365985,365986,365987,365988,365989,366006,366007,366008,366009,366010,366011,366012,366013,366014,366015,366016,366017,366018,366019,366020,366021,366022,366023,366024);

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;



SET done=0;



OPEN masterCursor;



ticket_loop: LOOP 

FETCH masterCursor into ticket_id,drivercompany_id,EnteredBy,bill_batchs,destination_tickets,landfill_grosss,landfill_tares,

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

    valid_from,valid_to,bill_using,value,landfill,transfer_station,

    min_billable_gross_weight,rate_using,sort_by,fuel_surcharge_amount,other_charges,
    
    rate_type
    
    from subcontractor_rate where subcontractor=subcontractor_ids and transfer_station=origins and landfill=destinations;

    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET flag=1;

    SET flag=0;

    OPEN frstCursor;

    rate_loop: LOOP

    FETCH frstCursor into bcompany_locations,rate_types,

    valid_froms,valid_tos,bill_usings,rvalues,landfills,transfer_stations,

    min_billable_gross_weights,rate_usings,sort_bys,fuel_surcharge_amounts,
    
    OtherCharges,rate_types;

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

ELSE 

SET OtherCharges=0.0; 

SET fuel_surcharge_amounts=0.0;

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


SET sumTotal=FAmount+OtherCharges+fuel_surcharge_amounts;

SET Load_date_var=DATE_FORMAT(load_dates,'%m-%d-%Y');

SET Unload_date_var=DATE_FORMAT(unload_dates,'%m-%d-%Y');

SET bill_batch_date_var=DATE_FORMAT(bill_batchs,'%m-%d-%Y');



SET done_holder=done;


select username into EnteredByName from user_info where name=EnteredBy;

select full_name into DriverName from driver where id=drivers;

select name into drivercompany_name from location where id=drivercompany_id;

select name into TerminalName from location where id=terminals;

SET done=done_holder;






delete from subcontractor_invoice_detail_new where tickt_id=ticket_id ;

insert into subcontractor_invoice_detail_new values(0,null,null,null,null,1,FAmount,bcompany_locations,null,DemmurageCharge,
DestinationName,landfill_grosss,landfill_nets,landfill_tares,destination_tickets,
landfill_tons,DriverName,EffectiveGrossWt,EffectiveNetWt,EffectiveTareWt,EffectiveTonsWt,
EnteredByName,fuel_surcharge_amounts,Load_date_var,min_billable_gross_weights,OriginName,
transfer_grosss,transfer_nets,transfer_tares,origin_tickets,transfer_tons,
OtherCharges,subcontractor_ids,TerminalName,ticket_id,TonnagePremium,sumTotal,Unload_date_var,drivercompany_id,unload_dates);





SET subcont_id=null;

SET fuel_surcharge_amounts=0.0;

SET DemmurageCharge=0.0;

SET sumTotal=0.0;

SET TotAmount=0.0;

SET TonnagePremium=0.0;

SET fuelSurcharge=0.0;

SET drivercompany_name=null;

SET OtherCharges=0.0;

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