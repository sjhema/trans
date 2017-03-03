INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300136', '/admin/equipment/loan/uploadMain.do?rst=1', 'Upload Vehicle Loan', '3', 'Upload Vehicle Loan', 
'/admin/equipment/loan/uploadMain.do?rst=1,/admin/equipment/loan/upload.do', '1', '19', '0', '202', '/1/202/300136/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300136', '1'); -- ADMIN
