CREATE DEFINER=`lutransport`@`%` PROCEDURE `NetReportMain2`(IN updateFromDate varchar(200),IN updateToDate varchar(200))
BEGIN
DECLARE done int DEFAULT 0;

DECLARE flag int DEFAULT 0;

DECLARE done_holder int;

DECLARE No_Cal int;



DECLARE ticket_ids bigint(225);

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


DECLARE DRpayrate double;
DECLARE DRnightPayRate double;
DECLARE DRSundayratefactor double;
DECLARE DRprobationrate double;
DECLARE DRrate_type int;
DECLARE DRrate_using int;
DECLARE DRnote varchar(255);
DECLARE DRvalid_from DATETIME;
DECLARE DRvalid_to DATETIME;
DECLARE DRcatagory BIGINT;
DECLARE DRcompany_id BIGINT;
DECLARE DRlandfill BIGINT;
DECLARE DRterminal BIGINT;
DECLARE DRtransfer_station BIGINT;

DECLARE DRshift int;

DECLARE dayOfWeek int default 0;

DECLARE Rate double;

DECLARE FAmount double;




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



DECLARE masterCursor CURSOR for select id,driver_company,entered_by,bill_batch,destination_ticket,
landfill_gross,landfill_tare,

landfill_time_in,landfill_time_out,load_date,origin_ticket,transfer_gross,transfer_tare,

tranfer_time_in,tranfer_time_out,unload_date,destination,driver,origin,terminal,

trailer,vehicle,customer_id,ticketstatus,landfill_ton,transfer_ton,subcontractor_id,

invoice_number,landfill_net,transfer_net,entered_by,invoice_date,company_location,

voucher_date,voucher_status,gallon from ticket where bill_batch >= updateFromDate and bill_batch <= updateToDate;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;



SET done=0;



OPEN masterCursor;



ticket_loop: LOOP 

FETCH masterCursor into ticket_ids,drivercompany_id,EnteredBy,bill_batchs,destination_tickets,landfill_grosss,landfill_tares,

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

select shift into DRshift from driver where id=drivers;

SELECT DAYOFWEEK(unload_dates) INTO dayOfWeek;

SET done=done_holder;

IF DestinationName='grows' || DestinationName='tullytown'  then

SET destinations=91;

END IF;

BLOCK2: BEGIN

    DECLARE frstCursor CURSOR for select payrate,rate_type,rate_using,valid_from,valid_to,catagory,company_id
    ,landfill,terminal,transfer_station,note,nightPayRate,Sundayratefactor,probationrate
    from driver_rate where transfer_station=origins and landfill=destinations 
    and company_id=drivercompany_id and terminal=terminals;

    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET flag=1;

    SET flag=0;

    OPEN frstCursor;

    rate_loop: LOOP

    FETCH frstCursor into DRpayrate,DRrate_type,DRrate_using,DRvalid_from,DRvalid_to,DRcatagory,DRcompany_id,DRlandfill,DRterminal,
    DRtransfer_station,DRnote,DRnightPayRate,DRSundayratefactor,DRprobationrate;

    IF flag THEN

    CLOSE frstCursor;

    LEAVE rate_loop;

    END IF;

    IF DRrate_using IS NULL THEN   
    
        IF DRshift = 1 THEN
            IF dayOfWeek = 8 THEN           
                SET Rate = DRpayrate * DRSundayratefactor; 
            ELSE
                SET Rate = DRpayrate;
            END IF;
        ELSEIF DRshift = 2 THEN
            IF dayOfWeek = 8 THEN           
                SET Rate = DRnightPayRate * DRSundayratefactor; 
            ELSE
                SET Rate = DRnightPayRate;
            END IF;
        END IF;    
    
    CLOSE frstCursor;

    LEAVE rate_loop;

    ELSEIF  DRrate_using=1 THEN

    IF load_dates >= DRvalid_from && load_dates <= DRvalid_to THEN

        IF DRshift = 1 THEN
            IF dayOfWeek = 8 THEN           
                SET Rate = DRpayrate * DRSundayratefactor; 
            ELSE
                SET Rate = DRpayrate;
            END IF;
        ELSEIF DRshift = 2 THEN
            IF dayOfWeek = 8 THEN           
                SET Rate = DRnightPayRate * DRSundayratefactor; 
            ELSE
                SET Rate = DRnightPayRate;
            END IF;
        END IF;
    
        CLOSE frstCursor;

        LEAVE rate_loop;    

    END IF;

    ELSEIF DRrate_using=2 THEN

    IF unload_dates >= DRvalid_from && unload_dates <= DRvalid_to THEN

        IF DRshift = 1 THEN
            IF dayOfWeek = 8 THEN           
                SET Rate = DRpayrate * DRSundayratefactor; 
            ELSE
                SET Rate = DRpayrate;
            END IF;
        ELSEIF DRshift = 2 THEN
            IF dayOfWeek = 8 THEN           
                SET Rate = DRnightPayRate * DRSundayratefactor; 
            ELSE
                SET Rate = DRnightPayRate;
            END IF;
        END IF;
    
        CLOSE frstCursor;

        LEAVE rate_loop;    

    END IF;

    END IF;

    END LOOP rate_loop;

END BLOCK2;


IF Rate is null THEN
    SET Rate = 0.0;
END IF;

update ticket set driver_payrate=Rate where id=ticket_ids ;

SET Rate=0.0;


END LOOP ticket_loop;

END