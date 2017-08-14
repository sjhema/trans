INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30106', '/reportuser/report/driverlogreport/start.do', 'Driver Log Report', '3', 'Driver Log Report', '/reportuser/report/driverlogreport/start.do,/reportuser/report/driverlogreport/save.do,/reportuser/report/driverlogreport/search.do,/reportuser/report/driverlogreport/export.do,/reportuser/report/driverlogreport/ajax.do', '1', '16', '0', '301', '/1/301/30106');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '30106', '1'); -- ADMIN


-----------
SELECT * FROM `invoice_detail_new`
where t_driver in ('Miles Reginald')
and unload_date >= '2017-05-01' and unload_date <= '2017-05-01'
order by landfill_time_out asc
