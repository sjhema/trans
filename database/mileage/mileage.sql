INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('2026', '/uploadData/mileagelog.do', 'Upload Mileage', '3', 'Upload Mileage', '/uploadData/mileagelog.do,/uploadData/mileagelog/upload.do', '1', '9', '0', '202', '/1/202/2026/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2026', '1'); -- ADMIN
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2026', '2'); -- OPERATOR
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2026', '6'); -- BILLING
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2026', '8'); -- ADMIN_HR1
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2026', '10'); -- SPECIAL_PRIVILEGE_1

----
INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('2027', '/operator/mileagelog/list.do?rst=1', 'Manage Mileage', '3', 'Manage Mileage', '/operator/mileagelog/home.do,/operator/mileagelog/list.do,/operator/mileagelog/search.do,/operator/mileagelog/create.do,/operator/mileagelog/save.do,/operator/mileagelog/ajax.do,/operator/mileagelog/edit.do,/operator/mileagelog/delete.do,/operator/mileagelog/bulkdelete.do,/operator/mileagelog/export.do', '1', '10', '0', '202', '/1/202/2027/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2027', '1'); -- ADMIN
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2027', '2'); -- OPERATOR
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2027', '6'); -- BILLING
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2027', '8'); -- ADMIN_HR1
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2027', '10'); -- SPECIAL_PRIVILEGE_1

----
INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30105', '/reportuser/report/mileagelog/start.do', 'Mileage Report', '3', 'Mileage Report', '/reportuser/report/mileagelog/start.do,/reportuser/report/mileagelog/save.do,/reportuser/report/mileagelog/search.do,/reportuser/report/mileagelog/export.do,/reportuser/report/mileagelog/ajax.do', '1', '15', '0', '301', '/1/301/30105');

UPDATE `lutransport`.`business_object` SET `URL`='/reportuser/report/mileagelog/start.do,/reportuser/report/mileagelog/save.do,/reportuser/report/mileagelog/search.do,/reportuser/report/mileagelog/iftaSearch.do,/reportuser/report/mileagelog/mpgSearch.do,/reportuser/report/mileagelog/export.do,/reportuser/report/mileagelog/iftaExport.do,/reportuser/report/mileagelog/mpgExport.do,/reportuser/report/mileagelog/ajax.do' WHERE `ID`='30105';
UPDATE `lutransport`.`business_object` SET `URL`='/reportuser/report/mileagelog/start.do,/reportuser/report/mileagelog/save.do,/reportuser/report/mileagelog/search.do,/reportuser/report/mileagelog/iftaSearch.do,/reportuser/report/mileagelog/mpgSearch.do,/reportuser/report/mileagelog/export.do,/reportuser/report/mileagelog/iftaExport.do,/reportuser/report/mileagelog/mpgExport.do,/reportuser/report/mileagelog/ajax.do,/reportuser/report/mileagelog/mileageLogDetailsDrillDownReport.do' WHERE `ID`='30105';

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '30105', '1'); -- ADMIN
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '30105', '6'); -- BILLING

-----
CREATE TABLE `lutransport`.`mileage_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `period` datetime NOT NULL,
  `state` bigint(20) NOT NULL,
  `miles` double NOT NULL,
  `unit` bigint(20) NOT NULL,
  `unit_num` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `company` bigint(20) NOT NULL,
  `vin` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `groups` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_in_state` datetime NOT NULL,
  `last_in_state` datetime NOT NULL,
  `vehicle_permit` bigint(20) DEFAULT NULL,
  `vehicle_permit_number` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_FK_MILEAGE_LOG_STATE_ID` (`state`),
  KEY `idx_FK_MILEAGE_LOG_UNIT_VEHICLE_ID` (`unit`),
  KEY `idx_FK_MILEAGE_LOG_COMPANY_LOCATION_ID` (`company`),
  KEY `idx_FK_MILEAGE_LOG_PERMIT_VEHICLE_PERMIT_ID` (`vehicle_permit`),
  KEY `idx_period` (`period`),
  CONSTRAINT `FK_MILEAGE_LOG_COMPANY_LOCATION_ID` FOREIGN KEY (`company`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_MILEAGE_LOG_PERMIT_VEHICLE_PERMIT_ID` FOREIGN KEY (`vehicle_permit`) REFERENCES `vehicle_permit` (`id`),
  CONSTRAINT `FK_MILEAGE_LOG_STATE_ID` FOREIGN KEY (`state`) REFERENCES `state` (`id`),
  CONSTRAINT `FK_MILEAGE_LOG_UNIT_VEHICLE_ID` FOREIGN KEY (`unit`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`mileage_log` 
ADD COLUMN `gps` VARCHAR(45) NULL DEFAULT 'Y' COMMENT '' AFTER `vehicle_permit_number`;

update mileage_log set gps = 'Y';
-- 
ALTER TABLE `lutransport`.`mileage_log` 
ADD COLUMN `source` VARCHAR(45) NULL COMMENT '' AFTER `gps`;

update lutransport.mileage_log
 set source = 'OLD_GPS' where period != '2019-03-01'
--
ALTER TABLE `lutransport`.`state` 
ADD COLUMN `long_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '' AFTER `country`;

UPDATE `lutransport`.`state` SET `long_name`='New York' WHERE `id`='1' and `code`='NY';
UPDATE `lutransport`.`state` SET `long_name`='Florida' WHERE `id`='16' and `code`='FL';
UPDATE `lutransport`.`state` SET `long_name`='New Jersey' WHERE `id`='37'and `code`='NJ';
UPDATE `lutransport`.`state` SET `long_name`='Pennsylvania' WHERE `id`='45' and `code`='PA';
UPDATE `lutransport`.`state` SET `long_name`='Virginia' WHERE `id`='53' and `code`='VA';
UPDATE `lutransport`.`state` SET `long_name`='Illinois' WHERE `id`='20' and code='IL';
UPDATE `lutransport`.`state` SET `long_name`='Maryland' WHERE `id`='27' and `code`='MD' ;

UPDATE `lutransport`.`state` SET `long_name`='Delaware' WHERE `id`='14' and `code`='DE' ;
UPDATE `lutransport`.`state` SET `long_name`='District of Columbia' WHERE `id`='15' and `code`='DC' ;
UPDATE `lutransport`.`state` SET `long_name`='Georgia' WHERE `id`='17' and `code`='GA';
UPDATE `lutransport`.`state` SET `long_name`='Indiana' WHERE `id`='21' and `code`='IN';
UPDATE `lutransport`.`state` SET `long_name`='Kentucky' WHERE `id`='24' and `code`='KY';
UPDATE `lutransport`.`state` SET `long_name`='Ohio' WHERE `id`='42' and `code`='OH';
UPDATE `lutransport`.`state` SET `long_name`='Tennessee' WHERE `id`='49' and `code`='TN';
UPDATE `lutransport`.`state` SET `long_name`='West Virginia' WHERE `id`='55' and `code`='WV';

UPDATE `lutransport`.`state` SET `long_name`='Alabama' WHERE `id`='7' and `code`='AL';
UPDATE `lutransport`.`state` SET `long_name`='Connecticut' WHERE `id`='13' and `code`='CT';
UPDATE `lutransport`.`state` SET `long_name`='North Carolina' WHERE `id`='40' and `code`='NC';
UPDATE `lutransport`.`state` SET `long_name`='South Carolina' WHERE `id`='47' and `code`='SC';

UPDATE `lutransport`.`state` SET `long_name`='Arizona' WHERE `id`='9';
UPDATE `lutransport`.`state` SET `long_name`='California' WHERE `id`='11';
UPDATE `lutransport`.`state` SET `long_name`='Colorado' WHERE `id`='12';
UPDATE `lutransport`.`state` SET `long_name`='Iowa' WHERE `id`='22';
UPDATE `lutransport`.`state` SET `long_name`='Nebraska' WHERE `id`='34';
UPDATE `lutransport`.`state` SET `long_name`='Utah' WHERE `id`='51';




