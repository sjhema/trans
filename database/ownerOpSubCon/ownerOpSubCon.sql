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
  `miles` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKLocationDistOriginLocn_Idx` (`origin`),
  KEY `FKLocationDistDestnLocn_Idx` (`destination`),
  CONSTRAINT `FKLocationDistOriginLocn` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FKLocationDistDestnLocn` FOREIGN KEY (`destination`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30107', '/admin/locationdist/list.do?rst=1', 'Manage Location Distance', '3', 'Manage Location Distance', '/admin/locationdist/home.do,/admin/locationdist/list.do,/admin/locationdist/search.do,/admin/locationdist/create.do,/admin/locationdist/save.do,/admin/locationdist/ajax.do,/admin/locationdist/edit.do,/admin/locationdist/delete.do,/admin/locationdist/export.do', '1', '17', '0', '301', '/1/301/30107');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '30107', '1');