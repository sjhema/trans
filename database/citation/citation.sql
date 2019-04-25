CREATE TABLE `lutransport`.`violation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `incident_date` datetime NOT NULL,
  `citation_no` varchar(75) COLLATE utf8_unicode_ci DEFAULT NULL,
  `driver` bigint(20) NOT NULL,
  `company` bigint(20) NOT NULL,
  `truck` bigint(20) NOT NULL,
  `trailer` bigint(20) NOT NULL,
  `out_of_service` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `violation_type` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`violation` 
ADD INDEX `VIO_INC_DATE_IDX` (`incident_date` ASC);

ALTER TABLE `lutransport`.`violation` 
ADD UNIQUE INDEX `VIO_CTN_NO_UNIQ` (`citation_no` ASC);

---
INSERT INTO `lutransport`.`business_object` 
(`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30016', '/admin/citation/citationmaint/list.do?rst=1', 'Citations', '2', 'Citations', 
'/admin/citation/citationmaint/list.do?rst=1,/admin/citation/citationmaint/create.do,/admin/citation/citationmaint/edit.do,/admin/citation/citationmaint/delete.do,/admin/citation/citationmaint/save.do,/admin/citation/citationmaint/search.do,/admin/citation/citationmaint/export.do', '1', '7', '0', '1', '/1/30016/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '30016', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300161', '/admin/citation/citationmaint/list.do?rst=1', 'Manage Citations', '3', 'Manage Citations', 
'/admin/citation/citationmaint/list.do?rst=1,/admin/citation/citationmaint/create.do,/admin/citation/citationmaint/edit.do,/admin/citation/citationmaint/delete.do,/admin/citation/citationmaint/save.do,/admin/citation/citationmaint/search.do,/admin/citation/citationmaint/export.do,/admin/citation/citationmaint/ajax.do,/admin/citation/citationmaint/managedocs/start.do,/admin/citation/citationmaint/managedocs/uploaddoc.do,/admin/citation/citationmaint/managedocs/downloaddoc.do,/admin/citation/citationmaint/managedocs/deletedoc.do', '1', '1', '0', '30016', '/1/30016/300161/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300161', '1'); -- ADMIN


---
CREATE TABLE `lutransport`.`roadside_inspection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `inspection_date` datetime NOT NULL,
  `driver` bigint(20) NOT NULL,
  `company` bigint(20) NOT NULL,
  `truck` bigint(20) NOT NULL,
  `trailer` bigint(20) NOT NULL,
  `inspection_level` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `violation` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `citation` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`roadside_inspection` 
ADD INDEX `ROAD_INSP_DATE_IDX` (`inspection_date` ASC) ;

ALTER TABLE `lutransport`.`violation` 
ADD COLUMN `roadside_inspection` BIGINT(20) NULL COMMENT '' AFTER `violation_type`;

ALTER TABLE `lutransport`.`violation` 
ADD INDEX `VIO_ROAD_INSP_FK_idx` (`roadside_inspection` ASC);
ALTER TABLE `lutransport`.`violation` 
ADD CONSTRAINT `VIO_ROAD_INSP_FK`
  FOREIGN KEY (`roadside_inspection`)
  REFERENCES `lutransport`.`roadside_inspection` (`id`);
  
UPDATE `lutransport`.`business_object` 
SET `URL`='/admin/citation/citationmaint/list.do?rst=1,/admin/citation/citationmaint/create.do,/admin/citation/citationmaint/edit.do,/admin/citation/citationmaint/delete.do,/admin/citation/citationmaint/save.do,/admin/citation/citationmaint/search.do,/admin/citation/citationmaint/export.do,/admin/citation/citationmaint/ajax.do,/admin/citation/citationmaint/managedocs/start.do,/admin/citation/citationmaint/managedocs/uploaddoc.do,/admin/citation/citationmaint/managedocs/downloaddoc.do,/admin/citation/citationmaint/managedocs/deletedoc.do' 
WHERE `ID`='300161';

ALTER TABLE `lutransport`.`violation` 
ADD COLUMN `docs` VARCHAR(10) NOT NULL DEFAULT 'N' AFTER `out_of_service`;

ALTER TABLE `lutransport`.`roadside_inspection` 
ADD COLUMN `docs` VARCHAR(10) NOT NULL DEFAULT 'N' AFTER `inspection_level`;
  
INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('300162', '/admin/roadsideinspec/roadsideinspecmaint/list.do?rst=1', 'Manage Roadside Inspections', '3', 'Manage Roadside Inspections', 
'/admin/roadsideinspec/roadsideinspecmaint/list.do?rst=1,/admin/roadsideinspec/roadsideinspecmaint/create.do,/admin/roadsideinspec/roadsideinspecmaint/edit.do,/admin/roadsideinspec/roadsideinspecmaint/delete.do,/admin/roadsideinspec/roadsideinspecmaint/save.do,/admin/roadsideinspec/roadsideinspecmaint/search.do,/admin/roadsideinspec/roadsideinspecmaint/export.do,/admin/roadsideinspec/roadsideinspecmaint/ajax.do,/admin/roadsideinspec/roadsideinspecmaint/deleteViolation.do',
 '1', '2', '0', '30016', '/1/30016/300162/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '300162', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30109', '/reportuser/report/bonusqualifn/start.do', 'Bonus Qualification Report', '3', 'Bonus Qualification Report', 
'/reportuser/report/bonusqualifn/start.do,/reportuser/report/bonusqualifn/ajax.do,/reportuser/report/bonusqualifn/bonusQualifiedSearch.do,/reportuser/report/bonusqualifn/bonusNotQualifiedSearch.do,/reportuser/report/bonusqualifn/bonusQualifiedExport.do,/reportuser/report/bonusqualifn/bonusNotQualifiedExport.do', 
'1', '17', '0', '301', '/1/301/30109');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '30109', '1'); -- ADMIN
 
ALTER TABLE `lutransport`.`violation` 
CHANGE COLUMN `out_of_service` `out_of_service` VARCHAR(10) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL COMMENT '' ;

ALTER TABLE `lutransport`.`violation` 
DROP FOREIGN KEY `FKA092E125169AD222`;
ALTER TABLE `lutransport`.`violation` 
CHANGE COLUMN `trailer` `trailer` BIGINT(20) NULL COMMENT '' ;
ALTER TABLE `lutransport`.`violation` 
ADD CONSTRAINT `FKA092E125169AD222`
  FOREIGN KEY (`trailer`)
  REFERENCES `lutransport`.`vehicle` (`id`);
  
  UPDATE `lutransport`.`business_object` 
SET `URL`='/admin/roadsideinspec/roadsideinspecmaint/list.do?rst=1,/admin/roadsideinspec/roadsideinspecmaint/create.do,/admin/roadsideinspec/roadsideinspecmaint/edit.do,/admin/roadsideinspec/roadsideinspecmaint/delete.do,/admin/roadsideinspec/roadsideinspecmaint/save.do,/admin/roadsideinspec/roadsideinspecmaint/search.do,/admin/roadsideinspec/roadsideinspecmaint/export.do,/admin/roadsideinspec/roadsideinspecmaint/ajax.do,/admin/roadsideinspec/roadsideinspecmaint/deleteViolation.do,/admin/roadsideinspec/roadsideinspecmaint/managedocs/start.do,/admin/roadsideinspec/roadsideinspecmaint/managedocs/uploaddoc.do,/admin/roadsideinspec/roadsideinspecmaint/managedocs/downloaddoc.do,/admin/roadsideinspec/roadsideinspecmaint/managedocs/deletedoc.do' WHERE `ID`='300162';

 UPDATE `lutransport`.`business_object` SET `DISPLAY_TAG`='Roadside and Citations', `OBJECT_NAME`='Roadside and Citations' WHERE `ID`='30016';

 -------*****----
 
 ---***---


