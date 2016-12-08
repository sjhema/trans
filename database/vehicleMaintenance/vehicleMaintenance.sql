INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30012', '/admin/vehiclemaint/repairorders/list.do?rst=1', 'Vehicle Maint.', '2', 'Vehicle Maint.', 
'/admin/vehiclemaint/repairorders/list.do?rst=1,/admin/vehiclemaint/repairorders/create.do,/admin/vehiclemaint/repairorders/edit.do,/admin/vehiclemaint/repairorders/delete.do,/admin/vehiclemaint/repairorders/save.do,/admin/vehiclemaint/repairorders/search.do,/admin/vehiclemaint/repairorders/export.do,/admin/vehiclemaint/repairorders/ajax.do,/admin/vehiclemaint/repairorders/deleteLineItem.do,/admin/vehiclemaint/repairorders/copy.do,/admin/vehiclemaint/repairorders/print.do', '1', '8', '0', '1', '/1/30012/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '30012', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300121', '/admin/vehiclemaint/repairorders/list.do?rst=1', 'Manage Repair Orders', '3', 'Manage Repair Orders', 
'/admin/vehiclemaint/repairorders/list.do?rst=1,/admin/vehiclemaint/repairorders/create.do,/admin/vehiclemaint/repairorders/edit.do,/admin/vehiclemaint/repairorders/delete.do,/admin/vehiclemaint/repairorders/save.do,/admin/vehiclemaint/repairorders/search.do,/admin/vehiclemaint/repairorders/export.do,/admin/vehiclemaint/repairorders/ajax.do,/admin/vehiclemaint/repairorders/deleteLineItem.do,/admin/vehiclemaint/repairorders/copy.do,/admin/vehiclemaint/repairorders/print.do', '1', '1', '0', '30012', '/1/30012/300121/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300121', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300122', '/admin/vehiclemaint/repairorders/hourlylaborrate/list.do?rst=1', 'Manage Hourly Labor Rates', '3', 'Manage Hourly Labor Rates', 
'/admin/vehiclemaint/repairorders/hourlylaborrate/list.do?rst=1,/admin/vehiclemaint/repairorders/hourlylaborrate/create.do,/admin/vehiclemaint/repairorders/hourlylaborrate/edit.do,/admin/vehiclemaint/repairorders/hourlylaborrate/delete.do,/admin/vehiclemaint/repairorders/hourlylaborrate/save.do,/admin/vehiclemaint/repairorders/hourlylaborrate/search.do,/admin/vehiclemaint/repairorders/hourlylaborrate/export.do,/admin/vehiclemaint/repairorders/hourlylaborrate/ajax.do', '1', '2', '0', '30012', '/1/30012/300122/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300122', '1');

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300123', '/admin/vehiclemaint/repairorders/lineitemtype/list.do?rst=1', 'Manage Line Item Types', '3', 'Manage Line Item Types', 
'/admin/vehiclemaint/repairorders/lineitemtype/list.do?rst=1,/admin/vehiclemaint/repairorders/lineitemtype/create.do,/admin/vehiclemaint/repairorders/lineitemtype/edit.do,/admin/vehiclemaint/repairorders/lineitemtype/delete.do,/admin/vehiclemaint/repairorders/lineitemtype/save.do,/admin/vehiclemaint/repairorders/lineitemtype/search.do,/admin/vehiclemaint/repairorders/lineitemtype/export.do,/admin/vehiclemaint/repairorders/lineitemtype/ajax.do', '1', '3', '0', '30012', '/1/30012/300123/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300123', '1');

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('300124', '/admin/vehiclemaint/repairorders/component/list.do?rst=1', 'Manage Components', '3', 'Manage Components', 
'/admin/vehiclemaint/repairorders/component/list.do?rst=1,/admin/vehiclemaint/repairorders/component/create.do,/admin/vehiclemaint/repairorders/component/edit.do,/admin/vehiclemaint/repairorders/component/delete.do,/admin/vehiclemaint/repairorders/component/save.do,/admin/vehiclemaint/repairorders/component/search.do,/admin/vehiclemaint/repairorders/component/export.do,/admin/vehiclemaint/repairorders/component/ajax.do', '1', '4', '0', '30012', '/1/30012/300124/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '300124', '1');
-------
CREATE TABLE `repair_order_hourly_labor_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `labor_rate` double NOT NULL,
  `effective_start_date` datetime NOT NULL,
  `effective_end_date` datetime NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `repair_order_line_item_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `repairorderlineitemtype_Idx` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `repair_order_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `component` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `repairordercomponent_Idx` (`component`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `repair_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `repair_order_date` datetime NOT NULL,
  `vehicle` bigint(20) NOT NULL,
  `company` bigint(20) NOT NULL,
  `subcontractor` bigint(20) DEFAULT NULL,
  `mechanic` bigint(20) NOT NULL,
  `total_cost` double NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `repairorderVehicle_Idx` (`vehicle`) USING BTREE,
  KEY `repairorderMechanic_Idx` (`mechanic`) USING BTREE,
  KEY `repairorderCompany_Idx` (`company`) USING BTREE,
  KEY `repairorderSubcontractor_Idx` (`subcontractor`) USING BTREE,
  CONSTRAINT `repairorderCompany_Fk` FOREIGN KEY (`company`) REFERENCES `location` (`id`),
  CONSTRAINT `repairorderMechanic_Fk` FOREIGN KEY (`mechanic`) REFERENCES `driver` (`id`),
  CONSTRAINT `repairorderSubcontractor_Fk` FOREIGN KEY (`subcontractor`) REFERENCES `subcontractor` (`id`),
  CONSTRAINT `repairorderVehicle_Fk` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `repair_order_line_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `repair_order` bigint(20) NOT NULL,
  `line_item_type` bigint(20) NOT NULL,
  `component` bigint(20) NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `no_of_hours` double NOT NULL,
  `labor_rate` double NOT NULL,
  `total_labor_cost` double NOT NULL,
  `total_parts_cost` double NOT NULL,
  `total_cost` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `repairorderlineitemOrder_Idx` (`repair_order`) USING BTREE,
  CONSTRAINT `repairorderlineitemOrder_Fk` FOREIGN KEY (`repair_order`) REFERENCES `repair_order` (`id`),
  CONSTRAINT `repairorderlineitemType_Fk` FOREIGN KEY (`line_item_type`) REFERENCES `repair_order_line_item_type` (`id`),
  CONSTRAINT `repairorderlineitemComponent_Fk` FOREIGN KEY (`component`) REFERENCES `repair_order_component` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `repair_order_hourly_labor_rate` (`id`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`labor_rate`,`effective_start_date`,`effective_end_date`,`description`) VALUES (1,'2016-12-06 18:15:25',1,'2016-12-06 23:15:49',1,1,45,'2016-12-01 00:00:00','2017-12-31 00:00:00','');

INSERT INTO `lutransport`.`repair_order_line_item_type`(`created_at`,`created_by`,`status`,`type`,`description`)
VALUES (now(), '1', 1, 'Add', 'Add');

INSERT INTO `lutransport`.`repair_order_line_item_type`(`created_at`,`created_by`,`status`,`type`,`description`)
VALUES (now(), '1', 1, 'Adjust', 'Adjust');

INSERT INTO `lutransport`.`repair_order_line_item_type`(`created_at`,`created_by`,`status`,`type`,`description`)
VALUES (now(), '1', 1, 'Inspect', 'Inspect');

INSERT INTO `lutransport`.`repair_order_component`(`created_at`,`created_by`,`status`,`component`,`description`)
VALUES (now(), '1', 1, 'Air System', 'Air System');

INSERT INTO `lutransport`.`repair_order_component`(`created_at`,`created_by`,`status`,`component`,`description`)
VALUES (now(), '1', 1, 'Body', 'Body');

INSERT INTO `lutransport`.`repair_order_component`(`created_at`,`created_by`,`status`,`component`,`description`)
VALUES (now(), '1', 1, 'Brakes', 'Brakes');






