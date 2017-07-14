INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('1406', '/uploadData/wmInvoice.do', 'Upload WM Invoice', '3', 'Upload WM Invoice', '/uploadData/wmInvoice.do,/uploadData/wmInvoice/upload.do', '1', '8', '0', '140', '/1/140/1406/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1406', '1'); -- ADMIN

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('1407', '/admin/wmInvoice/list.do?rst=1', 'Manage WM Invoice', '3', 'Manage WM Invoice', '/admin/wmInvoice/list.do,/admin/wmInvoice/create.do,/admin/wmInvoice/save.do,/admin/wmInvoice/ajax.do,/admin/wmInvoice/edit.do,/admin/wmInvoice/delete.do,/admin/wmInvoice/search.do,/admin/wmInvoice/export.do', '1', '9', '0', '140', '/1/140/1407/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1407', '1'); -- ADMIN
 
INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('1408', '/admin/wmInvoiceVerification/start.do', 'Verify WM Invoice Tickets', '3', 'Verify WM Invoice Tickets', '/admin/wmInvoiceVerification/start.do,/admin/wmInvoiceVerification/search.do,/admin/wmInvoiceVerification/save.do,/admin/wmInvoiceVerification/ajax.do,/admin/wmInvoiceVerification/export.do', '1', '10', '0', '140', '/1/140/1408/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1408', '1'); -- ADMIN
 
 ------
 CREATE TABLE `wm_invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ticket` bigint(20) NOT NULL,
  `reissued_ticket` bigint(20) DEFAULT NULL,
  `txn_date` datetime DEFAULT NULL,
  `time_in` varchar(10) DEFAULT NULL,
  `time_out` varchar(10) DEFAULT NULL,
  `wm_origin` varchar(255) DEFAULT NULL,
  `wm_destination` varchar(255) DEFAULT NULL,
  `wm_vehicle` varchar(75) DEFAULT NULL,
  `wm_trailer` varchar(75) DEFAULT NULL,
  `gross` double DEFAULT NULL,
  `tare` double DEFAULT NULL,
  `net` double DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `fsc` double DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `wm_status` varchar(75) DEFAULT NULL,
  `wm_status_code` varchar(75) DEFAULT NULL,
  `wm_ticket` varchar(100) DEFAULT NULL,
  `origin` bigint(20) DEFAULT NULL,
  `destination` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKWMInvoiceOriginLocation_Idx` (`origin`),
  KEY `FKWMInvoiceDestinationLocation_Idx` (`destination`),
  CONSTRAINT `FKWMInvoiceOriginLocation` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FKWMInvoiceDestinationLocation` FOREIGN KEY (`destination`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


