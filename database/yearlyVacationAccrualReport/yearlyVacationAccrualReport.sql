INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('80111', '/hr/report/leaveaccrual/start.do', 'Yearly Vacation Accrual Report', '3', 'Yearly Vacation Accrual Report', '/hr/report/leaveaccrual/start.do,/hr/report/leaveaccrual/search.do,/hr/report/leaveaccrual/export.do', '1', '9', '0', '801', '/1/801/80111/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '80111', '1'); -- ADMIN
-- INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '80111', '2'); -- OPERATOR
-- INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '80111', '6'); -- BILLING
-- INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '80111', '8'); -- ADMIN_HR1
-- INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '80111', '10'); -- SPECIAL_PRIVILEGE_1

UPDATE `lutransport`.`business_object` SET `display_order`='10' WHERE `ID`='8019';
UPDATE `lutransport`.`business_object` SET `display_order`='11' WHERE `ID`='80110';