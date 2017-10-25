CREATE TABLE `insurance_company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zipcode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `insurance_company_state_fk_idx` (`state`),
  CONSTRAINT `insurance_company_state_fk` FOREIGN KEY (`state`) REFERENCES `state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`insurance_company` 
ADD UNIQUE INDEX `insurance_company_name_UNIQUE` (`name` ASC);

CREATE TABLE `insurance_company_rep` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `insurance_company` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `insurance_company_rep_comp_fk_idx` (`insurance_company`),
  CONSTRAINT `insurance_company_rep_comp_fk` FOREIGN KEY (`insurance_company`) REFERENCES `insurance_company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `injury_incident_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `incident_type` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`injury_incident_type` 
ADD UNIQUE INDEX `injury_incident_type_incident_type_UNIQUE` (`incident_type` ASC);

CREATE TABLE `injury_to_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `injury_to` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`injury_to_type` 
ADD UNIQUE INDEX `injury_to_injury_to_UNIQUE` (`injury_to` ASC);

CREATE TABLE `injury` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `incident_type` bigint(20) NOT NULL,
  `insurance_company` bigint(20) DEFAULT NULL,
  `claim_rep` bigint(20) DEFAULT NULL,
  `claim_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `driver` bigint(20) NOT NULL,
  `driver_company` bigint(20) NOT NULL,
  `driver_terminal` bigint(20) NOT NULL,
  `driver_category` bigint(20) NOT NULL,
  `driver_age` int(11) DEFAULT NULL,
  `driver_months_of_service` int(11) DEFAULT NULL,
  `incident_date` datetime NOT NULL,
  `incident_time` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `incident_time_ampm` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `incident_day_of_week` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `return_to_work` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `notes` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `injury_to` bigint(20) DEFAULT NULL,
  `return_to_work_date` datetime DEFAULT NULL,
  `no_of_lost_work_days` int(11) DEFAULT NULL,
  `tarp_related_injury` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_report_of_injury` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `osha_recordable` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `injury_status` int(11) NOT NULL,
  `medical_cost` double DEFAULT NULL,
  `indemnity_cost` double DEFAULT NULL,
  `expense` double DEFAULT NULL,
  `reserve` double DEFAULT NULL,
  `total_paid` double DEFAULT NULL,
  `total_claimed` double DEFAULT NULL,
  `attorney` varchar(75) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `injury_insurance_comp_fk_idx` (`insurance_company`),
  KEY `injury_insurance_comp_rep_fk_idx` (`claim_rep`),
  KEY `injury_driver_fk_idx` (`driver`),
  KEY `injury_driver_comp_location_fk_idx` (`driver_company`),
  KEY `injury_driver_terminal_location_fk_idx` (`driver_terminal`),
  KEY `injury_incident_type_fk_idx` (`incident_type`),
  KEY `injury_injury_to_fk_idx` (`injury_to`),
  KEY `injury_driver_category_fk_idx` (`driver_category`),
  CONSTRAINT `injury_insurance_comp_fk` FOREIGN KEY (`insurance_company`) REFERENCES `insurance_company` (`id`),
  CONSTRAINT `injury_insurance_comp_rep_fk` FOREIGN KEY (`claim_rep`) REFERENCES `insurance_company_rep` (`id`),
  CONSTRAINT `injury_driver_fk` FOREIGN KEY (`driver`) REFERENCES `driver` (`id`),
  CONSTRAINT `injury_driver_comp_location_fk` FOREIGN KEY (`driver_company`) REFERENCES `location` (`id`),
  CONSTRAINT `injury_driver_terminal_location_fk` FOREIGN KEY (`driver_terminal`) REFERENCES `location` (`id`),
  CONSTRAINT `injury_incident_type_fk` FOREIGN KEY (`incident_type`) REFERENCES `injury_incident_type` (`id`),
  CONSTRAINT `injury_injury_to_fk` FOREIGN KEY (`injury_to`) REFERENCES `injury_to_type` (`id`),
  CONSTRAINT `injury_driver_category_fk` FOREIGN KEY (`driver_category`) REFERENCES `employee_catagory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`injury` 
ADD UNIQUE INDEX `injury_claim_no_UNIQUE` (`claim_no` ASC);

INSERT INTO `lutransport`.`business_object` 
(`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30014', '/admin/injury/injurymaint/list.do?rst=1', 'Injury', '2', 'Injury', '/admin/injury/injurymaint/list.do?rst=1,/admin/injury/injurymaint/create.do,/admin/injury/injurymaint/edit.do,/admin/injury/injurymaint/delete.do,/admin/injury/injurymaint/save.do,/admin/injury/injurymaint/search.do,/admin/injury/injurymaint/export.do,/admin/injury/injurymaint/ajax.do,/admin/injury/injurymaint/copy.do,/admin/injury/injurymaint/print.do', '1', '9', '0', '1', '/1/30014/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '30014', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300141', '/admin/injury/injurymaint/list.do?rst=1', 'Manage Injuries', '3', 'Manage Injuries', 
'/admin/injury/injurymaint/list.do?rst=1,/admin/injury/injurymaint/create.do,/admin/injury/injurymaint/edit.do,/admin/injury/injurymaint/delete.do,/admin/injury/injurymaint/save.do,/admin/injury/injurymaint/search.do,/admin/injury/injurymaint/export.do,/admin/injury/injurymaint/ajax.do,/admin/injury/injurymaint/copy.do,/admin/injury/injurymaint/print.do', '1', '1', '0', '30014', '/1/30014/300141/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300141', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300142', '/admin/injury/reports/start.do?rst=1', 'Injury Reports', '3', 'Injury Reports', 
'/admin/injury/reports/start.do?rst=1,/admin/injury/reports/notReportedSearch.do,/admin/injury/reports/notReportedExport.do,/admin/injury/reports/reportedSearch.do,/admin/injury/reports/reportedExport.do,/admin/injury/reports/ajax.do', '1', '2', '0', '30014', '/1/30014/300142/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300142', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300143', '/uploadData/injury.do', 'Upload Injury', '3', 'Upload Injury', 
'/uploadData/injury.do,/uploadData/injury/uploadMain.do,/uploadData/injury/uploadReported.do,/uploadData/injury/uploadNotReported.do', '1', '3', '0', '30014', '/1/30014/300143/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300143', '1'); -- ADMIN


