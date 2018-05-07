CREATE DEFINER=`lutransport`@`%` PROCEDURE `BillingHistoryProc8`(IN tick_id bigint(150))
BEGIN


DECLARE compnay_locations varchar(200);

DECLARE customer_ids varchar(225);

DECLARE Company_Name varchar(225);

DECLARE Customer_Names varchar(225);

DECLARE bill_history_id bigint(150);

DECLARE drivercompany_id bigint(225);

DECLARE drivercompany_name varchar(225);
DECLARE driverPayRate double default 0.0;



select company_location,customer_id,driver_company,driver_payrate into compnay_locations,customer_ids,drivercompany_id,driverPayRate from ticket where id=tick_id;


IF compnay_locations IS NOT NULL THEN

select name into Company_Name from location where id=compnay_locations;

END IF;

IF customer_ids IS NOT NULL THEN

select name into Customer_Names from customer where id=customer_ids;

END IF;

IF driverPayRate is null THEN
SET driverPayRate = 0.0;
END IF;

select name into drivercompany_name from location where id=drivercompany_id;


SELECT id into bill_history_id from invoice_detail_new where tickt_id=tick_id;



update invoice_detail_new set tcompany=Company_Name,bill_customer_name=Customer_Names,driver_companylocname=drivercompany_name,driver_companylocid=drivercompany_id,driver_pay_rate=driverPayRate
where id=bill_history_id;

SET Company_Name=null;

SET Customer_Names=null;

SET bill_history_id=null;

SET drivercompany_name=null;


END