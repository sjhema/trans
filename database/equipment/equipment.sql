-- INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
-- VALUES ('30013', '/admin/equipment/loan/list.do?rst=1', 'Equipment', '2', 'Equipment', 
-- '/admin/equipment/loan/list.do?rst=1,/admin/equipment/loan/create.do,/admin/equipment/loan/edit.do,/admin/equipment/loan/delete.do,/admin/equipment/loan/save.do,/admin/equipment/loan/search.do,/admin/equipment/loan/export.do,/admin/equipment/loan/ajax.do,/admin/equipment/loan/print.do', '1', '9', '0', '1', '/1/30013/');
-- INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '30013', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300131', '/admin/equipment/loan/list.do?rst=1', 'Manage Vehicle Loan', '3', 'Manage Vehicle Loan', 
'/admin/equipment/loan/list.do?rst=1,/admin/equipment/loan/create.do,/admin/equipment/loan/edit.do,/admin/equipment/loan/delete.do,/admin/equipment/loan/save.do,/admin/equipment/loan/search.do,/admin/equipment/loan/export.do,/admin/equipment/loan/ajax.do,/admin/equipment/loan/print.do', '1', '1', '0', '30013', '/1/30013/300131/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300131', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300132', '/admin/equipment/lender/list.do?rst=1', 'Manage Lender', '3', 'Manage Lender', 
'/admin/equipment/lender/list.do?rst=1,/admin/equipment/lender/create.do,/admin/equipment/lender/edit.do,/admin/equipment/lender/delete.do,/admin/equipment/lender/save.do,/admin/equipment/lender/search.do,/admin/equipment/lender/export.do,/admin/equipment/lender/ajax.do,/admin/equipment/lender/print.do', '1', '2', '0', '30013', '/1/30013/300132/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300132', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300133', '/admin/equipment/title/list.do?rst=1', 'Manage Title', '3', 'Manage Title', 
'/admin/equipment/title/list.do?rst=1,/admin/equipment/title/create.do,/admin/equipment/title/edit.do,/admin/equipment/title/delete.do,/admin/equipment/title/save.do,/admin/equipment/title/search.do,/admin/equipment/title/export.do,/admin/equipment/title/ajax.do,/admin/equipment/title/print.do', '1', '3', '0', '30013', '/1/30013/300133/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300133', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300134', '/admin/equipment/sale/list.do?rst=1', 'Manage Sale', '3', 'Manage Sale', 
'/admin/equipment/sale/list.do?rst=1,/admin/equipment/sale/create.do,/admin/equipment/sale/edit.do,/admin/equipment/sale/delete.do,/admin/equipment/sale/save.do,/admin/equipment/sale/search.do,/admin/equipment/sale/export.do,/admin/equipment/sale/ajax.do,/admin/equipment/sale/print.do', '1', '4', '0', '30013', '/1/30013/300134/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300134', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300135', '/admin/equipment/buyer/list.do?rst=1', 'Manage Buyer', '3', 'Manage Buyer', 
'/admin/equipment/buyer/list.do?rst=1,/admin/equipment/buyer/create.do,/admin/equipment/buyer/edit.do,/admin/equipment/buyer/delete.do,/admin/equipment/buyer/save.do,/admin/equipment/buyer/search.do,/admin/equipment/buyer/export.do,/admin/equipment/buyer/ajax.do,/admin/equipment/buyer/print.do', '1', '5', '0', '30013', '/1/30013/300135/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300135', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('3001350', '/admin/equipment/report/equipmentreport/start.do', 'Equipment Report', '3', 'Equipment Report', 
'/admin/equipment/report/equipmentreport/start.do,/admin/equipment/report/equipmentreport/search.do,/admin/equipment/report/equipmentreport/export.do', '1', '50', '0', '30013', '/1/30013/3001350/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '3001350', '1'); -- ADMIN
---------------
CREATE TABLE `equipment_lender` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
   `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address1` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zipcode` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `state` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `equipmentlender_state_fk_idx` (`state`),
  CONSTRAINT `equipmentlender_state_fk` FOREIGN KEY (`state`) REFERENCES `state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`equipment_lender` 
ADD UNIQUE INDEX `equipmentlender_name_UNQ` (`name` ASC);

CREATE TABLE `vehicle_loan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `loan_no` varchar(75) NOT NULL,
  `vehicle` bigint(20) NOT NULL,
  `lender` bigint(20) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `interest_rate` double NOT NULL,
  `payment_due_dom` varchar(5) NOT NULL,
  `payment_amount` double NOT NULL,
  `no_of_payments` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `vehicleloanVehicle_fk_idx` (`vehicle`) USING BTREE,
  KEY `vehicleloanLender_fk_idx` (`lender`) USING BTREE,
  CONSTRAINT `vehicleloanVehicle_fk` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `vehicleloanLender_fk` FOREIGN KEY (`lender`) REFERENCES `equipment_lender` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`vehicle_loan` 
ADD UNIQUE INDEX `vehicleloan_loanno_UNQ` (`loan_no` ASC);

CREATE TABLE `vehicle_title` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `vehicle` bigint(20) NOT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title_owner` bigint(20) NOT NULL,
  `registered_owner` bigint(20) NOT NULL,
  `holds_title` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `vehicletitleVehicle_fk_idx` (`vehicle`) USING BTREE,
  KEY `vehicletitleOwnerLocation_fk_idx` (`title_owner`) USING BTREE,
  KEY `vehicletitleRegOwnerLocation_fk_idx` (`registered_owner`) USING BTREE,
  CONSTRAINT `vehicletitleVehicle_fk` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `vehicletitleOwnerLocation_fk` FOREIGN KEY (`title_owner`) REFERENCES `location` (`id`),
  CONSTRAINT `vehicletitleRegOwnerLocation_fk` FOREIGN KEY (`registered_owner`) REFERENCES `location` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`vehicle_title` 
ADD UNIQUE INDEX `vehicletitle_title_UNQ` (`title` ASC);

CREATE TABLE `equipment_buyer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
   `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address1` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `address2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zipcode` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `state` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `equipmentbuyer_state_fk_idx` (`state`),
  CONSTRAINT `equipmentbuyer_state_fk` FOREIGN KEY (`state`) REFERENCES `state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`equipment_buyer` 
ADD UNIQUE INDEX `equipmentbuyer_name_UNQ` (`name` ASC);

CREATE TABLE `vehicle_sale` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `vehicle` bigint(20) NOT NULL,
  `sale_date` datetime NOT NULL,
  `sale_price` double NOT NULL,
  `buyer` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vehiclesaleVehicle_fk_idx` (`vehicle`) USING BTREE,
  KEY `vehiclesaleEquipmentBuyer_fk_idx` (`buyer`) USING BTREE,
  CONSTRAINT `vehiclesaleVehicle_fk` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `vehiclesaleEquipmentBuyer_fk` FOREIGN KEY (`buyer`) REFERENCES `equipment_buyer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
UPDATE `lutransport`.`business_object` SET `DISPLAY_TAG`='Vehicle Loan', `OBJECT_NAME`='Vehicle Loan' WHERE `ID`='300131';
UPDATE `lutransport`.`business_object` SET `DISPLAY_TAG`='Vehicle Title', `OBJECT_NAME`='Vehicle Title' WHERE `ID`='300133';
UPDATE `lutransport`.`business_object` SET `DISPLAY_TAG`='Vehicle Sales', `OBJECT_NAME`='Vehicle Sales' WHERE `ID`='300134';

UPDATE `lutransport`.`business_object` SET `parent_id`='202' WHERE `ID`='300131';
UPDATE `lutransport`.`business_object` SET `parent_id`='202' WHERE `ID`='300132';
UPDATE `lutransport`.`business_object` SET `parent_id`='202' WHERE `ID`='300133';
UPDATE `lutransport`.`business_object` SET `parent_id`='202' WHERE `ID`='300134';
UPDATE `lutransport`.`business_object` SET `parent_id`='202' WHERE `ID`='300135';
UPDATE `lutransport`.`business_object` SET `parent_id`='202' WHERE `ID`='3001350';

UPDATE `lutransport`.`business_object` SET `display_order`='12' WHERE `ID`='600115';
UPDATE `lutransport`.`business_object` SET `display_order`='13' WHERE `ID`='600117';

UPDATE `lutransport`.`business_object` SET `display_order`='14' WHERE `ID`='300131';
UPDATE `lutransport`.`business_object` SET `display_order`='15' WHERE `ID`='300132';
UPDATE `lutransport`.`business_object` SET `display_order`='16' WHERE `ID`='300133';
UPDATE `lutransport`.`business_object` SET `display_order`='17' WHERE `ID`='300134';
UPDATE `lutransport`.`business_object` SET `display_order`='18' WHERE `ID`='300135';

UPDATE `lutransport`.`business_object` SET `hierarchy`='/1/202/3001350/' WHERE `ID`='3001350';
UPDATE `lutransport`.`business_object` SET `hierarchy`='/1/202/300135/' WHERE `ID`='300135';
UPDATE `lutransport`.`business_object` SET `hierarchy`='/1/202/300134/' WHERE `ID`='300134';
UPDATE `lutransport`.`business_object` SET `hierarchy`='/1/202/300133/' WHERE `ID`='300133';
UPDATE `lutransport`.`business_object` SET `hierarchy`='/1/202/300132/' WHERE `ID`='300132';
UPDATE `lutransport`.`business_object` SET `hierarchy`='/1/202/300131/' WHERE `ID`='300131';

UPDATE `lutransport`.`business_object` SET `hidden`='1' WHERE `ID`='300132';
UPDATE `lutransport`.`business_object` SET `hidden`='1' WHERE `ID`='300135';

delete  from role_privilege where business_object_id = 30013;
DELETE FROM `lutransport`.`business_object` WHERE `ID`='30013';

-----
-- 27th Feb 2017:
ALTER TABLE `lutransport`.`vehicle_loan` 
DROP INDEX `vehicleloan_loanno_UNQ` ;

ALTER TABLE `lutransport`.`vehicle_loan` 
ADD INDEX `vehicleloan_loanno_idx` (`loan_no` ASC);





