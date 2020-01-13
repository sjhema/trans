INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('1412', '/admin/ticket/reports/start.do?rst=1', 'Ticket Reports', '3', 'Ticket Reports', 
'/admin/ticket/reports/start.do?rst=1,/admin/ticket/reports/search.do,/admin/ticket/reports/export.do,/admin/ticket/reports/ajax.do',
'1', '13', '0', '140', '/1/140/1412/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '1412', '1'); -- ADMIN
