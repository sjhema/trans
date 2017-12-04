CREATE TABLE `accident_cause` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `cause` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`accident_cause` 
ADD UNIQUE INDEX `accident_cause_cause_UNIQUE` (`cause` ASC);

CREATE TABLE `accident_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `accident_type` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`accident_type` 
ADD UNIQUE INDEX `accident_type_type_UNIQUE` (`accident_type` ASC);

CREATE TABLE `accident_weather` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `weather` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`accident_weather` 
ADD UNIQUE INDEX `accident_weather_weather_UNIQUE` (`weather` ASC);

CREATE TABLE `accident_road_condition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `road_condition` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`accident_road_condition` 
ADD UNIQUE INDEX `accident_road_condition_condition_UNIQUE` (`road_condition` ASC);

CREATE TABLE `accident` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `accident_type` bigint(20) DEFAULT NULL,
  `accident_cause` bigint(20) DEFAULT NULL,
  `insurance_company` bigint(20) DEFAULT NULL,
  `claim_rep` bigint(20) DEFAULT NULL,
  `claim_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `driver` bigint(20) NOT NULL,
  `driver_company` bigint(20) NOT NULL,
  `driver_terminal` bigint(20) NOT NULL,
  `driver_months_of_service` int(11) DEFAULT NULL,
  `driver_hired_date` datetime DEFAULT NULL,
  `incident_date` datetime NOT NULL,
  `incident_day_of_week` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  `vehicle` bigint(20) DEFAULT NULL,
  `notes` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weather` bigint(20) DEFAULT NULL,
  `road_condition` bigint(20) DEFAULT NULL,
  `vehicle_damage` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `no_injured` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `towed` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `citation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recordable` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hm_release` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `accident_status` int(11) NOT NULL,
  `paid` double DEFAULT NULL,
  `reserve` double DEFAULT NULL,
  `expense` double DEFAULT NULL,
  `total_cost` double DEFAULT NULL,
  `deductible` double DEFAULT NULL,
  `incident_time` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fatality` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `accident_insurance_comp_fk_idx` (`insurance_company`),
  KEY `accident_insurance_comp_rep_fk_idx` (`claim_rep`),
  KEY `accident_driver_fk_idx` (`driver`),
  KEY `accident_driver_comp_location_fk_idx` (`driver_company`),
  KEY `accident_driver_terminal_location_fk_idx` (`driver_terminal`),
  KEY `accident_type_fk_idx` (`accident_type`),
  KEY `accident_cause_fk_idx` (`accident_cause`),
  KEY `accident_state_fk_idx` (`state`),
  KEY `accident_vehicle_fk_idx` (`vehicle`),
  KEY `accident_weather_fk_idx` (`weather`),
  KEY `accident_road_condition_fk_idx` (`road_condition`),
  CONSTRAINT `accident_insurance_comp_fk` FOREIGN KEY (`insurance_company`) REFERENCES `insurance_company` (`id`),
  CONSTRAINT `accident_insurance_comp_rep_fk` FOREIGN KEY (`claim_rep`) REFERENCES `insurance_company_rep` (`id`),
  CONSTRAINT `accident_driver_fk` FOREIGN KEY (`driver`) REFERENCES `driver` (`id`),
  CONSTRAINT `accident_vehicle_fk` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `accident_driver_comp_location_fk` FOREIGN KEY (`driver_company`) REFERENCES `location` (`id`),
  CONSTRAINT `accident_driver_terminal_location_fk` FOREIGN KEY (`driver_terminal`) REFERENCES `location` (`id`),
  CONSTRAINT `accident_type_fk` FOREIGN KEY (`accident_type`) REFERENCES `accident_type` (`id`),
  CONSTRAINT `accident_cause_fk` FOREIGN KEY (`accident_cause`) REFERENCES `accident_cause` (`id`),
  CONSTRAINT `accident_state_fk` FOREIGN KEY (`state`) REFERENCES `state` (`id`),
  CONSTRAINT `accident_road_condition_fk` FOREIGN KEY (`road_condition`) REFERENCES `accident_road_condition` (`id`),
  CONSTRAINT `accident_weather_fk` FOREIGN KEY (`weather`) REFERENCES `accident_weather` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`accident` 
ADD UNIQUE INDEX `accident_claim_no_UNIQUE` (`claim_no` ASC);

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300144', '/admin/accident/accidentmaint/list.do?rst=1', 'Manage Accidents', '3', 'Manage Accidents', 
'/admin/accident/accidentmaint/list.do?rst=1,/admin/accident/accidentmaint/create.do,/admin/accident/accidentmaint/edit.do,/admin/accident/accidentmaint/delete.do,/admin/accident/accidentmaint/save.do,/admin/accident/accidentmaint/search.do,/admin/accident/accidentmaint/export.do,/admin/accident/accidentmaint/ajax.do,/admin/accident/accidentmaint/copy.do,/admin/accident/accidentmaint/print.do', '1', '4', '0', '30014', '/1/30014/300144/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300144', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300145', '/admin/accident/reports/start.do?rst=1', 'Accident Reports', '3', 'Accident Reports', 
'/admin/accident/reports/start.do?rst=1,/admin/accident/reports/notReportedSearch.do,/admin/accident/reports/notReportedExport.do,/admin/accident/reports/reportedSearch.do,/admin/accident/reports/reportedExport.do,/admin/accident/reports/allSearch.do,/admin/accident/reports/allExport.do,/admin/accident/reports/yoySearch.do,/admin/accident/reports/yoyExport.do,/admin/accident/reports/ajax.do', '1', '5', '0', '30014', '/1/30014/300145/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300145', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300146', '/uploadData/accident.do', 'Upload Accident', '3', 'Upload Accident', 
'/uploadData/accident.do,/uploadData/accident/uploadMain.do,/uploadData/accident/uploadReported.do,/uploadData/accident/uploadNotReported.do', '1', '6', '0', '30014', '/1/30014/300146/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300146', '1'); -- ADMIN

----
UPDATE `lutransport`.`business_object` SET `URL`='/reportuser/report/mileagelog/start.do,/reportuser/report/mileagelog/save.do,/reportuser/report/mileagelog/search.do,/reportuser/report/mileagelog/iftaSearch.do,/reportuser/report/mileagelog/mpgSearch.do,/reportuser/report/mileagelog/export.do,/reportuser/report/mileagelog/iftaExport.do,/reportuser/report/mileagelog/mpgExport.do,/reportuser/report/mileagelog/ajax.do,/reportuser/report/mileagelog/mileageLogDetailsDrillDownReport.do,/reportuser/report/mileagelog/ownerOpSubConSearch.do,/reportuser/report/mileagelog/ownerOpSubConExport.do' WHERE `ID`='30105';
---

UPDATE `lutransport`.`business_object` SET `DISPLAY_TAG`='Workers Comp.', `OBJECT_NAME`='Workers Comp.' WHERE `ID`='30014';

INSERT INTO `lutransport`.`business_object` 
(`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30015', '/admin/accident/accidentmaint/list.do?rst=1', 'Accidents', '2', 'Accidents', '/admin/accident/accidentmaint/list.do?rst=1,/admin/accident/accidentmaint/create.do,/admin/accident/accidentmaint/edit.do,/admin/accident/accidentmaint/delete.do,/admin/accident/accidentmaint/save.do,/admin/accident/accidentmaint/search.do,/admin/accident/accidentmaint/export.do,/admin/accident/accidentmaint/ajax.do,/admin/accident/accidentmaint/copy.do,/admin/accident/accidentmaint/print.do', '1', '7', '0', '1', '/1/30015/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '30015', '1'); -- ADMIN

UPDATE `lutransport`.`business_object` SET `display_order`='1', `parent_id`='30015', `hierarchy`='/1/30015/300144/' WHERE `ID`='300144';
UPDATE `lutransport`.`business_object` SET `display_order`='2', `parent_id`='30015', `hierarchy`='/1/30015/300145/' WHERE `ID`='300145';
UPDATE `lutransport`.`business_object` SET `display_order`='3', `parent_id`='30015', `hierarchy`='/1/30015/300146/' WHERE `ID`='300146';

---
ALTER TABLE `lutransport`.`accident` 
DROP FOREIGN KEY `accident_driver_comp_location_fk`,
DROP FOREIGN KEY `accident_driver_fk`,
DROP FOREIGN KEY `accident_driver_terminal_location_fk`;
ALTER TABLE `lutransport`.`accident` 
CHANGE COLUMN `driver` `driver` BIGINT(20) NULL DEFAULT NULL ,
CHANGE COLUMN `driver_company` `driver_company` BIGINT(20) NULL DEFAULT NULL ,
CHANGE COLUMN `driver_terminal` `driver_terminal` BIGINT(20) NULL DEFAULT NULL ;
ALTER TABLE `lutransport`.`accident` 
ADD CONSTRAINT `accident_driver_comp_location_fk`
  FOREIGN KEY (`driver_company`)
  REFERENCES `lutransport`.`location` (`id`),
ADD CONSTRAINT `accident_driver_fk`
  FOREIGN KEY (`driver`)
  REFERENCES `lutransport`.`driver` (`id`),
ADD CONSTRAINT `accident_driver_terminal_location_fk`
  FOREIGN KEY (`driver_terminal`)
  REFERENCES `lutransport`.`location` (`id`);
  
  ALTER TABLE `lutransport`.`accident` 
ADD COLUMN `subcontractor` BIGINT(20) NULL DEFAULT NULL AFTER `fatality`,
ADD INDEX `accident_subcon_fk_idx` (`subcontractor` ASC);
ALTER TABLE `lutransport`.`accident` 
ADD CONSTRAINT `accident_subcon_fk`
  FOREIGN KEY (`subcontractor`)
  REFERENCES `lutransport`.`subcontractor` (`id`);
  
