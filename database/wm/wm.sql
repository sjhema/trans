INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('1404', '/uploadData/wmTicket.do', 'Upload WM Ticket', '3', 'Upload WM Ticket', '/uploadData/wmTicket.do,/uploadData/wmTicket/upload.do', '1', '6', '0', '140', '/1/140/1404/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1404', '1'); -- ADMIN

 UPDATE `lutransport`.`business_object` SET `display_order`='7' WHERE `ID`='1404'; -- Upload WM Ticket

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('1405', '/admin/wmTicket/list.do?rst=1', 'Manage WM Ticket', '3', 'Manage WM Ticket', '/admin/wmTicket/list.do,/admin/wmTicket/create.do,/admin/wmTicket/save.do,/admin/wmTicket/ajax.do,/admin/wmTicket/edit.do,/admin/wmTicket/delete.do,/admin/wmTicket/search.do', '1', '6', '0', '140', '/1/140/1405/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1405', '1'); -- ADMIN
 
 ------
 CREATE TABLE `wm_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ticket` bigint(20) DEFAULT NULL,
  `hauling_ticket` bigint(20) DEFAULT NULL,
  `ticket_type` varchar(20) DEFAULT NULL,
  `processing_status` int(11) DEFAULT NULL,
  `txn_date` datetime DEFAULT NULL,
  `time_in` varchar(10) DEFAULT NULL,
  `time_out` varchar(10) DEFAULT NULL,
  `wm_company` varchar(75) DEFAULT NULL,
  `wm_vehicle` varchar(75) DEFAULT NULL,
  `wm_trailer` varchar(75) DEFAULT NULL,
  `wm_destination` varchar(255) DEFAULT NULL,
  `wm_hauling_company` varchar(255) DEFAULT NULL,
  `gross` double DEFAULT NULL,
  `tare` double DEFAULT NULL,
  `net` double DEFAULT NULL,
  `tons` double DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `driver` bigint(20) DEFAULT NULL,
  `driver_company` bigint(20) DEFAULT NULL,
  `terminal` bigint(20) DEFAULT NULL,
  `vehicle` bigint(20) DEFAULT NULL,
  `trailer` bigint(20) DEFAULT NULL,
  `bill_batch` datetime DEFAULT NULL,
  `load_date` datetime DEFAULT NULL,
  `origin` bigint(20) DEFAULT NULL,
  `origin_ticket` bigint(20) DEFAULT NULL,
  `tranfer_time_in` varchar(10) DEFAULT NULL,
  `tranfer_time_out` varchar(10) DEFAULT NULL,
  `transfer_gross` double DEFAULT NULL,
  `transfer_tare` double DEFAULT NULL,
  `transfer_net` double DEFAULT NULL,
  `transfer_ton` double DEFAULT NULL,
  `unload_date` datetime DEFAULT NULL,
  `destination` bigint(20) DEFAULT NULL,
  `destination_ticket` bigint(20) DEFAULT NULL,
  `landfill_time_in` varchar(10) DEFAULT NULL,
  `landfill_time_out` varchar(10) DEFAULT NULL,
  `landfill_gross` double DEFAULT NULL,
  `landfill_tare` double DEFAULT NULL,
  `landfill_net` double DEFAULT NULL,
  `landfill_ton` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKWMTicketDriverCompanyLocation_Idx` (`driver_company`),
  KEY `FKWMTicketTerminalLocation_Idx` (`terminal`),
  KEY `FKWMTicketDriverDriver_Idx` (`driver`),
  KEY `FKWMTicketVehicleVehicle_Idx` (`vehicle`),
  KEY `FKWMTicketTrailerVehicle_Idx` (`trailer`),
  KEY `FKWMTicketOriginLocation_Idx` (`origin`),
  KEY `FKWMTicketDestinationLocation_Idx` (`destination`),
  CONSTRAINT `FKWMTicketDriverCompanyLocation` FOREIGN KEY (`driver_company`) REFERENCES `location` (`id`),
  CONSTRAINT `FKWMTicketTerminalLocation` FOREIGN KEY (`terminal`) REFERENCES `location` (`id`),
  CONSTRAINT `FKWMTicketDriverDriver` FOREIGN KEY (`driver`) REFERENCES `driver` (`id`),
  CONSTRAINT `FKWMTicketVehicleVehicle` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKWMTicketTrailerVehicle` FOREIGN KEY (`trailer`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKWMTicketOriginLocation` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FKWMTicketDestinationLocation` FOREIGN KEY (`destination`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


---
ALTER TABLE `lutransport`.`location` 
ADD COLUMN `long_name` VARCHAR(255) NULL DEFAULT NULL AFTER `code`;
ALTER TABLE `lutransport`.`location` 
ADD COLUMN `hauling_name` VARCHAR(255) NULL DEFAULT NULL AFTER `long_name`;

------------
ALTER TABLE `lutransport`.`ticket` 
ADD COLUMN `paperVerifiedStatus` INT(11) NULL DEFAULT 0 AFTER `driver_payrate`;

ALTER TABLE `lutransport`.`ticket` 
ADD COLUMN `autoCreated` INT(11) NULL DEFAULT 0 AFTER `paperVerifiedStatus`;
----

update ticket 
set autoCreated=1
where entered_by = 'Automatic';

update ticket set paperVerifiedStatus = 1
where ticketStatus=2
and id!=0;

