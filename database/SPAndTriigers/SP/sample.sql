CREATE DEFINER=`root`@`%` PROCEDURE `sample`()
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





DECLARE masterCursor CURSOR for select id,driver_company from ticket where 1=1 ;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;



SET done=0;



OPEN masterCursor;

ticket_loop: LOOP 

FETCH masterCursor into tickets_id,drivercompany_id;

IF done THEN

CLOSE masterCursor;

LEAVE ticket_loop;

END IF;

update invoice_detail_new set driver_companylocid=drivercompany_id where tickt_id=tickets_id;


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