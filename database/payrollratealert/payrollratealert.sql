ALTER TABLE `lutransport`.`driver_rate` 
ADD COLUMN `alert_status` INT(11) NULL DEFAULT 1 COMMENT '' AFTER `probationrate`;
 
// Driver Pay Rate
 UPDATE `lutransport`.`business_object` SET `display_order`='2' WHERE `ID`='9024';
 UPDATE `lutransport`.`business_object` SET `URL`='/hr/driverpayrate/list.do,/hr/driverpayrate/create.do,/hr/driverpayrate/edit.do,/hr/driverpayrate/delete.do,/hr/driverpayrate/save.do,/hr/driverpayrate/search.do,/hr/driverpayrate/ajax.do,/hr/driverpayrate/export.do,/hr/driverpayrate/changeAlertStatus.do' WHERE `ID`='9024';

// Hidden Alert
INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
 VALUES (9025,'/hr/payrollratealert/list.do?type=all','Payroll Rate Alert Message',3,'Payroll Rate Alert Message','/hr/payrollratealert/list.do?type=all',NULL,NULL,NULL,NULL,1,1,1,902,'/1/902/9025/',NULL);

 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9025', '1'); -- ADMIN

// Driver Pay Rate
 INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
 VALUES (9026,'/hr/payrollratealert/list.do?type=driverPayRate','Manage Driver Pay Rate Alert',3,'Manage Driver Pay Rate Alert','/hr/payrollratealert/list.do?type=driverPayRate,/hr/payrollratealert/search.do?type=driverPayRate',NULL,NULL,NULL,NULL,1,6,0,902,'/1/902/9026/',NULL);

 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9026', '1'); -- ADMIN

update driver_rate set alert_status = 0 where valid_to < '2020-05-31';

// Hourly Rate
ALTER TABLE `lutransport`.`hourly_rate` 
ADD COLUMN `alert_status` INT(11) NULL DEFAULT 1 COMMENT '' AFTER `weekly_hours`;
update hourly_rate set alert_status = 0 where valid_to < '2020-05-31';

// Hourly Rate
UPDATE `lutransport`.`business_object` SET `URL`='/hr/hourlyrate/list.do,/hr/hourlyrate/form.do,/hr/hourlyrate/save.do,/hr/hourlyrate/create.do,/hr/hourlyrate/search.do,/hr/hourlyrate/delete.do,/hr/hourlyrate/ajax.do,/hr/hourlyrate/edit.do,/hr/hourlyrate/export.do,/hr/hourlyrate/changeAlertStatus.do' 
WHERE `ID`='9021';

// Hourly Rate
 INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
 VALUES (9027,'/hr/payrollratealert/list.do?type=hourlyPayRate','Manage Hourly Pay Rate Alert',3,'Manage Hourly Pay Rate Alert','/hr/payrollratealert/list.do?type=hourlyPayRate,/hr/payrollratealert/search.do?type=hourlyPayRate',NULL,NULL,NULL,NULL,1,7,0,902,'/1/902/9027/',NULL);

 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9027', '1'); -- ADMIN
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




// Weekly salary Rate
ALTER TABLE `lutransport`.`weekly_salary` 
ADD COLUMN `alert_status` INT(11) NULL DEFAULT 1 COMMENT '' AFTER `daily_salary`;
update weekly_salary set alert_status = 0 where valid_to < '2020-05-31';

// Weekly salary Rate
UPDATE `lutransport`.`business_object` SET `URL`='/hr/weeklysalaryrate/list.do,/hr/weeklysalaryrate/form.do,/hr/weeklysalaryrate/save.do,/hr/weeklysalaryrate/create.do,/hr/weeklysalaryrate/search.do,/hr/weeklysalaryrate/delete.do,/hr/weeklysalaryrate/ajax.do,/hr/weeklysalaryrate/edit.do,/hr/weeklysalaryrate/export.do,/hr/weeklysalaryrate/changeAlertStatus.do' 
WHERE `ID`='9022';

// Weekly salary Rate
 INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
 VALUES (9028,'/hr/payrollratealert/list.do?type=weeklySalaryRate','Manage Weekly Salary Rate Alert',3,'Manage Weekly Salary Rate Alert','/hr/payrollratealert/list.do?type=weeklySalaryRate,/hr/payrollratealert/search.do?type=weeklySalaryRate',NULL,NULL,NULL,NULL,1,8,0,902,'/1/902/9028/',NULL);

 INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '9028', '1'); -- ADMIN


