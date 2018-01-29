CREATE TABLE `changed_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `base_ticket_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `changed_status` int(11) DEFAULT NULL,
  `bill_batch` datetime DEFAULT NULL,
  `new_bill_batch` datetime DEFAULT NULL,
  `destination_ticket` bigint(20) DEFAULT NULL,
  `landfill_gross` double DEFAULT NULL,
  `landfill_tare` double DEFAULT NULL,
  `landfill_time_in` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `landfill_time_out` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `load_date` datetime DEFAULT NULL,
  `origin_ticket` bigint(20) DEFAULT NULL,
  `transfer_gross` double DEFAULT NULL,
  `transfer_tare` double DEFAULT NULL,
  `tranfer_time_in` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tranfer_time_out` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unload_date` datetime DEFAULT NULL,
  `new_destination` bigint(20) DEFAULT NULL,
  `destination` bigint(20) DEFAULT NULL,
  `new_driver` bigint(20) DEFAULT NULL,
  `driver` bigint(20) DEFAULT NULL,
  `new_origin` bigint(20) DEFAULT NULL,
  `origin` bigint(20) DEFAULT NULL,
  `terminal` bigint(20) DEFAULT NULL,
  `trailer` bigint(20) DEFAULT NULL,
  `vehicle` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `ticketstatus` int(11) DEFAULT NULL,
  `landfill_ton` double DEFAULT NULL,
  `transfer_ton` double DEFAULT NULL,
  `subcontractor_id` bigint(20) DEFAULT NULL,
  `invoice_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `landfill_net` double DEFAULT NULL,
  `transfer_net` double DEFAULT NULL,
  `entered_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `invoice_date` datetime DEFAULT NULL,
  `company_location` bigint(20) DEFAULT NULL,
  `voucher_date` datetime DEFAULT NULL,
  `voucher_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `voucher_status` int(11) DEFAULT NULL,
  `gallon` double DEFAULT NULL,
  `payroll_batch` datetime DEFAULT NULL,
  `new_payroll_batch` datetime DEFAULT NULL,
  `payRollStatus` int(11) DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `new_notes` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `driver_company` bigint(20) DEFAULT NULL,
  `driver_payrate` double DEFAULT '0',
  `new_driver_payrate` double DEFAULT '0',
  `paperVerifiedStatus` int(11) DEFAULT '0',
  `autoCreated` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_CHANGED_TICKET_VEHICLE_idx` (`vehicle`),
  KEY `FK_CHANGED_TICKET_TRAILER_idx` (`trailer`),
  KEY `FK_CHANGED_TICKET_TERMINAL_idx` (`terminal`),
  KEY `FK_CHANGED_TICKET_DRIVER_idx` (`driver`),
  KEY `FK_CHANGED_TICKET_NEW_DRIVER_idx` (`new_driver`),
  KEY `FK_CHANGED_TICKET_DEST_idx` (`destination`),
  KEY `FK_CHANGED_TICKET_ORIG_idx` (`origin`),
  KEY `FK_CHANGED_TICKET_NEW_DEST_idx` (`new_destination`),
  KEY `FK_CHANGED_TICKET_NEW_ORIG_idx` (`new_origin`),
  KEY `FK_CHANGED_TICKET_CUST_idx` (`customer_id`),
  KEY `FK_CHANGED_TICKET_SUBCON_idx` (`subcontractor_id`),
  KEY `CHANGED_TICKET_Index_billbatch_date` (`bill_batch`) USING BTREE,
  KEY `CHANGED_TICKET_Index_ticketstatus` (`ticketstatus`) USING BTREE,
  KEY `CHANGED_TICKET_Index_createdBy` (`created_by`) USING BTREE,
  KEY `FK_CHANGED_TICKET_COMP_LOC_idx` (`company_location`) USING BTREE,
  KEY `CHANGED_TICKET_idx_test` (`bill_batch`,`id`),
  KEY `CHANGED_TICKET_Index_payRollStatus` (`payRollStatus`) USING BTREE,
  KEY `CHANGED_TICKET_Index_payRollBatch` (`payroll_batch`) USING BTREE,
  KEY `FK_CHANGED_TICKET_DRIVER_COMP_idx` (`driver_company`),
  CONSTRAINT `FK_CHANGED_TICKET_DEST` FOREIGN KEY (`destination`) REFERENCES `location` (`id`),
   CONSTRAINT `FK_CHANGED_TICKET_NEW_DEST` FOREIGN KEY (`new_destination`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_TRAILER` FOREIGN KEY (`trailer`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_TERMINAL` FOREIGN KEY (`terminal`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_ORIG` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_NEW_ORIG` FOREIGN KEY (`new_origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_SUBCON` FOREIGN KEY (`subcontractor_id`) REFERENCES `subcontractor` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_VEHICLE` FOREIGN KEY (`vehicle`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_DRIVER` FOREIGN KEY (`driver`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_NEW_DRIVER` FOREIGN KEY (`new_driver`) REFERENCES `driver` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_CUST` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_DRIVER_COMP` FOREIGN KEY (`driver_company`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_CHANGED_TICKET_COMP_LOC` FOREIGN KEY (`company_location`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `lutransport`.`changed_ticket` 
ADD UNIQUE INDEX `changed_tic_base_ticket_id_UNIQUE` (`base_ticket_id` ASC);


CREATE TABLE `changed_driverpay_freezed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `base_id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `changed_status` int(11) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `billbatch_fromdate` datetime DEFAULT NULL,
  `billbatch_fromdatestring` varchar(255) DEFAULT NULL,
  `billbatch_todate` datetime DEFAULT NULL,
  `billbatch_todatestring` varchar(255) DEFAULT NULL,
  `bonus_amount` double DEFAULT NULL,
  `bonusAmount0` double DEFAULT NULL,
  `bonusAmount1` double DEFAULT NULL,
  `bonusAmount2` double DEFAULT NULL,
  `bonusAmount3` double DEFAULT NULL,
  `bonusAmount4` double DEFAULT NULL,
  `bonusNotes` varchar(255) DEFAULT NULL,
  `bonusNotes0` varchar(255) DEFAULT NULL,
  `bonusNotes1` varchar(255) DEFAULT NULL,
  `bonusNotes2` varchar(255) DEFAULT NULL,
  `bonusNotes3` varchar(255) DEFAULT NULL,
  `bonusNotes4` varchar(255) DEFAULT NULL,
  `bonusTypeName` varchar(255) DEFAULT NULL,
  `bonusTypeName0` varchar(255) DEFAULT NULL,
  `bonusTypeName1` varchar(255) DEFAULT NULL,
  `bonusTypeName2` varchar(255) DEFAULT NULL,
  `bonusTypeName3` varchar(255) DEFAULT NULL,
  `bonusTypeName4` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `deductionAmount` double DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `driver` varchar(255) DEFAULT NULL,
  `holiday_amount` double DEFAULT NULL,
  `holidaydateFrom` varchar(255) DEFAULT NULL,
  `holidaydateTo` varchar(255) DEFAULT NULL,
  `holidayname` varchar(255) DEFAULT NULL,
  `is_main_row` varchar(255) DEFAULT NULL,
  `miscAmount` double DEFAULT NULL,
  `miscamt` double DEFAULT NULL,
  `miscamt0` double DEFAULT NULL,
  `miscamt1` double DEFAULT NULL,
  `miscamt2` double DEFAULT NULL,
  `miscamt3` double DEFAULT NULL,
  `miscamt4` double DEFAULT NULL,
  `miscamt5` double DEFAULT NULL,
  `miscnote` varchar(255) DEFAULT NULL,
  `miscnote0` varchar(255) DEFAULT NULL,
  `miscnote1` varchar(255) DEFAULT NULL,
  `miscnote2` varchar(255) DEFAULT NULL,
  `miscnote3` varchar(255) DEFAULT NULL,
  `miscnote4` varchar(255) DEFAULT NULL,
  `miscnote5` varchar(255) DEFAULT NULL,
  `no_of_load` int(11) DEFAULT NULL,
  `no_of_loadtotal` int(11) DEFAULT NULL,
  `numberOfSickDays` int(11) DEFAULT NULL,
  `numberOfVactionDays` int(11) DEFAULT NULL,
  `origin` varchar(255) DEFAULT NULL,
  `payroll_batch` datetime DEFAULT NULL,
  `payroll_batchstring` varchar(255) DEFAULT NULL,
  `probationDeductionAmount` double DEFAULT NULL,
  `quatarAmount` double DEFAULT NULL,
  `qutarAmt` double DEFAULT NULL,
  `qutarNotes` varchar(255) DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `reimburseAmount` double DEFAULT NULL,
  `reimburseAmt` double DEFAULT NULL,
  `reimburseNotes` varchar(255) DEFAULT NULL,
  `seq_num` varchar(255) DEFAULT NULL,
  `sickParsonalAmount` double DEFAULT NULL,
  `sickPersonalAmount` double DEFAULT NULL,
  `subTotalAmount` double DEFAULT NULL,
  `sumAmount` double DEFAULT NULL,
  `sumTotal` double DEFAULT NULL,
  `terminal_name` varchar(255) DEFAULT NULL,
  `totalamount` double DEFAULT NULL,
  `totalRowCount` int(11) DEFAULT NULL,
  `transportationAmount` double DEFAULT NULL,
  `vacation_amount` double DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `terminal` bigint(20) DEFAULT NULL,
  `bereavementAmount` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CHANGED_DRIVERPAYFREEZED_COMP_IDX` (`company_id`),
  KEY `FK_CHANGED_DRIVERPAYFREEZED_TERM_IDX` (`terminal`),
  CONSTRAINT `FK_CHANGED_DRIVERPAYFREEZED_TERM` FOREIGN KEY (`terminal`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_CHANGED_DRIVERPAYFREEZED_COMP` FOREIGN KEY (`company_id`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `lutransport`.`changed_driverpay_freezed` 
ADD UNIQUE INDEX `changed_driverpay_freez_base_id_UNIQUE` (`base_id` ASC);

CREATE TABLE `updated_driverpay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated_status` int(11) DEFAULT NULL,
   `company` bigint(20) DEFAULT NULL,
  `terminal` bigint(20) DEFAULT NULL,
  `driver` varchar(255) NOT NULL,
  `no_of_load` int(11) NOT NULL,
  `amount` double DEFAULT NULL,
  `billbatch_fromdate` datetime NOT NULL,
  `billbatch_todate` datetime NOT NULL,
  `payroll_batch` datetime DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_UPDATED_DRIVERPAY_COMP_IDX` (`company`),
  KEY `FK_UPDATED_DRIVERPAY_TERM_IDX` (`terminal`),
  CONSTRAINT `FK_UPDATED_DRIVERPAY_COMP` FOREIGN KEY (`terminal`) REFERENCES `location` (`id`),
  CONSTRAINT `FK_UPDATED_DRIVERPAY_TERM` FOREIGN KEY (`company`) REFERENCES `location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `lutransport`.`driverpay_detail` 
ADD COLUMN `transportationAmountDiff` DOUBLE NULL DEFAULT 0.0 COMMENT '' AFTER `bereavementAmount`;

ALTER TABLE `lutransport`.`driverpay_freezed` 
ADD COLUMN `transportationAmountDiff` DOUBLE NULL DEFAULT 0.0 COMMENT '' AFTER `bereavementAmount`;

---
INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('6014', '/admin/driverpayadjust/reports/start.do?rst=1', 'Driver Pay Adj. Reports', '3', 'Driver Pay Adj. Reports', 
'/admin/driverpayadjust/reports/start.do?rst=1,/admin/driverpayadjust/reports/search.do,/admin/driverpayadjust/reports/export.do,/admin/driverpayadjust/reports/ajax.do',
 '1', '4', '0', '601', '/1/601/6014/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '6014', '1'); -- ADMIN

