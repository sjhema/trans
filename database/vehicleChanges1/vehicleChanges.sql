ALTER TABLE `lutransport`.`vehicle` 
ADD COLUMN `feature` VARCHAR(255) NULL DEFAULT NULL AFTER `unitNum`,
ADD COLUMN `active_status` INT(11) NULL DEFAULT NULL AFTER `feature`,
ADD COLUMN `inactive_date` DATETIME NULL DEFAULT NULL AFTER `active_status`;


INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('3001351', '/admin/vehicle/report/vehiclereport/start.do', 'Vehicle Report', '3', 'Vehicle Report', '/admin/vehicle/report/vehiclereport/start.do,/admin/vehicle/report/vehiclereport/search.do,/admin/vehicle/report/vehiclereport/export.do', '1', '51', '0', '202', '/1/202/3001351/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '3001351', '1'); -- ADMIN