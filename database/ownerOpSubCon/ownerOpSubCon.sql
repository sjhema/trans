ALTER TABLE `lutransport`.`subcontractor` 
ADD COLUMN `owner_op` VARCHAR(45) NULL DEFAULT 'No' AFTER `terminal_3`;

 CREATE TABLE `location_distance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `origin` bigint(20) NOT NULL,
  `destination` bigint(20) NOT NULL,
  `company` bigint(20) DEFAULT NULL,
  `origin_state` bigint(20) DEFAULT NULL,
  `destn_state` bigint(20) DEFAULT NULL,
  `miles` double NOT NULL,
  `ny_miles` double DEFAULT NULL,
  `nj_miles` double DEFAULT NULL,
  `pa_miles` double DEFAULT NULL,
  `md_miles` double DEFAULT NULL,
  `va_miles` double DEFAULT NULL,
  `de_miles` double DEFAULT NULL,
  `wv_miles` double DEFAULT NULL,
  `dc_miles` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKLocationDistOriginLocn_Idx` (`origin`),
  KEY `FKLocationDistDestnLocn_Idx` (`destination`),
  KEY `FKLocationDistCompLocn_Idx` (`company`),
  KEY `FKLocationDistOriginState_Idx` (`origin_state`),
  KEY `FKLocationDistDestnState_Idx` (`destn_state`),
  CONSTRAINT `FKLocationDistOriginLocn` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FKLocationDistDestnLocn` FOREIGN KEY (`destination`) REFERENCES `location` (`id`),
  CONSTRAINT `FKLocationDistCompLocn` FOREIGN KEY (`company`) REFERENCES `location` (`id`),
  CONSTRAINT `FKLocationDistOriginState` FOREIGN KEY (`origin_state`) REFERENCES `state` (`id`),
  CONSTRAINT `FKLocationDistDestnState` FOREIGN KEY (`destn_state`) REFERENCES `state` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `lutransport`.`location_distance` 
ADD COLUMN `il_miles` DOUBLE NULL DEFAULT NULL COMMENT '' AFTER `dc_miles`,
ADD COLUMN `fl_miles` DOUBLE NULL DEFAULT NULL COMMENT '' AFTER `il_miles`,
ADD COLUMN `ct_miles` DOUBLE NULL DEFAULT NULL COMMENT '' AFTER `fl_miles`;


INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30107', '/admin/locationdist/list.do?rst=1', 'Manage Location Distance', '3', 'Manage Location Distance', '/admin/locationdist/home.do,/admin/locationdist/list.do,/admin/locationdist/search.do,/admin/locationdist/create.do,/admin/locationdist/save.do,/admin/locationdist/ajax.do,/admin/locationdist/edit.do,/admin/locationdist/delete.do,/admin/locationdist/export.do', '1', '17', '0', '301', '/1/301/30107');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '30107', '1');

UPDATE `lutransport`.`business_object` SET `DISPLAY_TAG`='Manage Load Miles', `OBJECT_NAME`='Manage Load Miles', `display_order`='8', `parent_id`='140', `hierarchy`='/1/140/30107' WHERE `ID`='30107';