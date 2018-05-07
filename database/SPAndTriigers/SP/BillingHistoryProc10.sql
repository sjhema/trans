CREATE DEFINER=`lutransport`@`%` PROCEDURE `BillingHistoryProc10`()
BEGIN
DECLARE done int DEFAULT 0;
DECLARE temp_paddcode VARCHAR(255);
DECLARE temp_padd_id bigint(150);



DECLARE mainPaddCursor CURSOR FOR SELECT id,temp_padd_code FROM temp_fuel_surcharge_padd;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;
SET done=0;

OPEN mainPaddCursor;

main_padd_loop: LOOP

FETCH mainPaddCursor into temp_padd_id,temp_paddcode;

IF done THEN
CLOSE mainPaddCursor;
LEAVE main_padd_loop;
END IF;
call BillingHistoryProc4(temp_paddcode);
delete from temp_fuel_surcharge_padd where id=temp_padd_id;
END LOOP main_padd_loop;
END