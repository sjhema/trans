INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) VALUES ('2028', '/admin/odometerreset/list.do?rst=1', 'Odometer Reset', '3', 'Odometer Reset', '/admin/odometerreset/list.do,/admin/odometerreset/create.do,/admin/odometerreset/edit.do,/admin/odometerreset/delete.do,/admin/odometerreset/search.do,/admin/odometerreset/save.do,/admin/odometerreset/ajax.do,/admin/odometerreset/export.do', '1', '11', '0', '202', '/1/202/2028/');

UPDATE `lutransport`.`business_object` SET `display_order`='12' WHERE `ID`='600115';
UPDATE `lutransport`.`business_object` SET `display_order`='13' WHERE `ID`='600117';

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2028', '1'); -- ADMIN
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2028', '2'); -- OPERATOR
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2028', '6'); -- BILLING
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2028', '8'); -- ADMIN_HR1
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2028', '10'); -- SPECIAL_PRIVILEGE_1

-------
CREATE TABLE `odometer_reset` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `truck` bigint(20) NOT NULL,
  `reset_date` datetime NOT NULL,
  `reset_reading` int(11) NOT NULL,
  `entered_by` varchar(255) NOT NULL,
  `entered_by_id` bigint(20) NOT NULL,
  `tempTruck` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `odometerresetTruck_Idx` (`truck`) USING BTREE,
  CONSTRAINT `odometerresetVehicle_Fk` FOREIGN KEY (`truck`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;