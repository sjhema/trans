ALTER TABLE `lutransport`.`driver_rate` 
ADD COLUMN `alert_status` INT(11) NULL DEFAULT 1 COMMENT '' AFTER `probationrate`;
 
// Driver Pay Rate
 UPDATE `lutransport`.`business_object` SET `display_order`='2' WHERE `ID`='9024';
 UPDATE `lutransport`.`business_object` SET `URL`='/hr/driverpayrate/list.do,/hr/driverpayrate/create.do,/hr/driverpayrate/edit.do,/hr/driverpayrate/delete.do,/hr/driverpayrate/save.do,/hr/driverpayrate/search.do,/hr/driverpayrate/ajax.do,/hr/driverpayrate/export.do,/hr/driverpayrate/changeAlertStatus.do' WHERE `ID`='9024';

INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
 VALUES (9025,'/hr/payrollratealert/list.do?type=all','Payroll Rate Alert Message',3,'Payroll Rate Alert Message','/hr/payrollratealert/list.do?type=all',NULL,NULL,NULL,NULL,1,1,1,902,'/1/902/9025/',NULL);

 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9025', '1'); -- ADMIN

 INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
 VALUES (9026,'/hr/payrollratealert/list.do?type=driverPayRate','Manage Driver Pay Rate Alert',3,'Manage Driver Pay Rate Alert','/hr/payrollratealert/list.do?type=driverPayRate,/hr/payrollratealert/search.do?type=driverPayRate',NULL,NULL,NULL,NULL,1,6,0,902,'/1/902/9026/',NULL);

 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9026', '1'); -- ADMIN

update driver_rate set alert_status = 0 where valid_to < '2020-05-31';
----

// Payroll rate alert
 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9025', '7'); -- HR

// Driver Payroll rate alert
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9026', '7'); -- HR

// Payroll rate alert
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9025', '10'); -- Sp Priv 1

// Driver Payroll rate alert
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9026', '10'); -- Sp Priv 1