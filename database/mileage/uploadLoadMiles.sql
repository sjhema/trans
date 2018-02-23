INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('30108', '/uploadData/loadMiles.do', 'Upload Load Miles', '3', 'Upload Load Miles', 
'/uploadData/loadMiles.do,/uploadData/loadMiles/upload.do', '1', '10', '0', '140', '/1/140/30108/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '30108', '1'); -- ADMIN
 
 
 CREATE TABLE `no_gps_vehicle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `vehicle` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKNoGps_Vehicle_Idx` (`vehicle`),
  CONSTRAINT `FKNoGps_Vehicle` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `lutransport`.`business_object` 
(`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('2029', '/admin/nogps/list.do?rst=1', 'No GPS', '3', 'No GPS', 
'/admin/nogps/list.do,/admin/nogps/create.do,/admin/nogps/edit.do,/admin/nogps/delete.do,/admin/nogps/search.do,/admin/nogps/save.do,/admin/nogps/ajax.do,/admin/nogps/export.do', 
'1', '52', '0', '202', '/1/202/2029/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '2029', '1'); -- ADMIN
 
 UPDATE `lutransport`.`business_object` SET `URL`='/reportuser/report/mileagelog/start.do,/reportuser/report/mileagelog/save.do,/reportuser/report/mileagelog/search.do,/reportuser/report/mileagelog/iftaSearch.do,/reportuser/report/mileagelog/mpgSearch.do,
/reportuser/report/mileagelog/export.do,/reportuser/report/mileagelog/iftaExport.do,/reportuser/report/mileagelog/mpgExport.do,
/reportuser/report/mileagelog/ajax.do,/reportuser/report/mileagelog/mileageLogDetailsDrillDownReport.do,/reportuser/report/mileagelog/ownerOpSubConSearch.do,
/reportuser/report/mileagelog/ownerOpSubConExport.do,/reportuser/report/mileagelog/noGPSSearch.do,/reportuser/report/mileagelog/noGPSExport.do' 
WHERE `ID`='30105';


UPDATE `lutransport`.`business_object` SET `display_order`='9' WHERE `ID`='30107';
UPDATE `lutransport`.`business_object` SET `display_order`='11' WHERE `ID`='1410';
