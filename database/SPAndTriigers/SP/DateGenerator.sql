CREATE DEFINER=`lutransport`@`%` PROCEDURE `DateGenerator`()
BEGIN

DECLARE start_date DATE;
DECLARE end_date DATE;
DECLARE date_diff int;


SET start_date = CURDATE() - INTERVAL 5 YEAR;

SET end_date = CURDATE() + INTERVAL 90 YEAR;

SET date_diff = DATEDIFF(end_date, start_date);

WHILE date_diff > 0 DO

insert into date_table values (start_date);

SET start_date = start_date + INTERVAL 1 DAY;

SET date_diff = date_diff - 1;

END WHILE;



END