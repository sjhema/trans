INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('1409', '/admin/wmLocation/list.do?rst=1', 'Manage WM Location', '3', 'Manage WM Location', 
'/admin/wmLocation/list.do,/admin/wmLocation/create.do,/admin/wmLocation/save.do,/admin/wmLocation/ajax.do,/admin/wmLocation/edit.do,/admin/wmLocation/delete.do,
/admin/wmLocation/search.do,/admin/wmLocation/export.do',
'1', '8', '0', '140', '/1/140/1409/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1409', '1'); -- ADMIN
 
CREATE TABLE `wm_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `location` bigint(20) NOT NULL,
  `wm_location_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKWMLocationLocation_Idx` (`location`),
  CONSTRAINT `FKWMLocationLocation` FOREIGN KEY (`location`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `location_pair` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `origin` bigint(20) NOT NULL,
  `destination` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKLocationPairOrigLocation_Idx` (`origin`),
  KEY `FKLocationPairDestLocation_Idx` (`destination`),
  CONSTRAINT `FKLocationPairOrigLocation` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FKLocationPairDestLocation` FOREIGN KEY (`destination`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('1410', '/admin/locationPair/list.do?rst=1', 'Manage Location Pair', '3', 'Manage Location Pair', 
'/admin/locationPair/list.do,/admin/locationPair/create.do,/admin/locationPair/save.do,/admin/locationPair/ajax.do,/admin/locationPair/edit.do,/admin/locationPair/delete.do,
/admin/locationPair/search.do,/admin/locationPair/export.do',
'1', '9', '0', '140', '/1/140/1410/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1410', '1'); -- ADMIN
