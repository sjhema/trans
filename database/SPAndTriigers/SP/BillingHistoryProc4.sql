CREATE DEFINER=`lutransport`@`%` PROCEDURE `BillingHistoryProc4`(IN fuelpadd_code varchar(200))
BEGIN

DECLARE finish int DEFAULT 0;
DECLARE tckt_transfer_id bigint(150);
DECLARE tckt_landfill_id bigint(150);


DECLARE paddCursor CURSOR for select t_origin_id,t_destination_id from invoice_detail_new where fuel_padd_code=fuelpadd_code and process_status='available';
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET finish=1;
SET finish=0;

OPEN paddCursor;

padd_loop: LOOP
FETCH paddCursor into tckt_transfer_id,tckt_landfill_id;
IF finish THEN
CLOSE paddCursor;
LEAVE padd_loop;
END IF;

if tckt_landfill_id=91 THEN
call BillingHistoryProc1(tckt_transfer_id);
ELSE
call BillingHistoryProc3(tckt_transfer_id,tckt_landfill_id);
END IF;


END LOOP padd_loop;
END