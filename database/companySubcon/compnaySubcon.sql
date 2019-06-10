CREATE TABLE `internal_subcon_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `driver_company` bigint(20) NOT NULL,
  `bill_company` bigint(20) NOT NULL,
  `origin` bigint(20) NOT NULL,
  `destination` bigint(20) NOT NULL,
  `subcontractor` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKInternalSubconMapOrigLocation_Idx` (`origin`),
  KEY `FKInternalSubconMapDestLocation_Idx` (`destination`),
  KEY `FKInternalSubconMapDriverComp_Idx` (`driver_company`),
  KEY `FKInternalSubconMapBillComp_Idx` (`bill_company`),
  KEY `FKInternalSubconMapSubcon_Idx` (`subcontractor`),
  CONSTRAINT `FKInternalSubconMapOrigLocation` FOREIGN KEY (`origin`) REFERENCES `location` (`id`),
  CONSTRAINT `FKInternalSubconMapDestLocation` FOREIGN KEY (`destination`) REFERENCES `location` (`id`),
  CONSTRAINT `FKInternalSubconMapDriverComp` FOREIGN KEY (`driver_company`) REFERENCES `location` (`id`),
  CONSTRAINT `FKInternalSubconMapBillComp` FOREIGN KEY (`bill_company`) REFERENCES `location` (`id`),
  CONSTRAINT `FKInternalSubconMapSubcon` FOREIGN KEY (`subcontractor`) REFERENCES `subcontractor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `internal_subcon_mapping`
ADD CONSTRAINT `UKInternalSubconMap` UNIQUE KEY(driver_company,bill_company,origin,destination);
---
-- Bill WB, Emp LU
INSERT INTO `lutransport`.`internal_subcon_mapping` (`created_at`, `created_by`, `status`, `driver_company`, `bill_company`, `subcontractor`, `origin`, `destination`) 
VALUES 
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '24', '81'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '411', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '411', '129'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '28', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '29', '130'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '29', '136'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '31', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '31', '78'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '40', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '40', '351'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '40', '130'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '432', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '374', '78'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '55', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '55', '136'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '55', '78'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '56', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '56', '78'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '431', '392'),
('2019-05-22 13:02:32', '1', '1', '6', '4', '183', '67', '230');
--------------------

-- Bill WB, Emp Drew
INSERT INTO `lutransport`.`internal_subcon_mapping` (`created_at`, `created_by`, `status`, `driver_company`, `bill_company`, `subcontractor`, `origin`, `destination`) 
VALUES 
('2019-05-22 13:02:32', '1', '1', '5', '4', '184', '245', '330'),
('2019-05-22 13:02:32', '1', '1', '5', '4', '184', '331', '330'),
('2019-05-22 13:02:32', '1', '1', '5', '4', '184', '426', '312'),
('2019-05-22 13:02:32', '1', '1', '5', '4', '184', '25', '312'),
('2019-05-22 13:02:32', '1', '1', '5', '4', '184', '32', '123');
------

-- Bill Drew, Emp WB
INSERT INTO `lutransport`.`internal_subcon_mapping` (`created_at`, `created_by`, `status`, `driver_company`, `bill_company`, `subcontractor`, `origin`, `destination`) 
VALUES 
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '10', '103'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '231', '226'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '231', '106'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '231', '118'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '231', '90'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '20', '103'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '20', '226'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '20', '75'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '25', '103'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '25', '226'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '25', '90'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '35', '123'),
('2019-05-22 13:02:32', '1', '1', '4', '5', '185', '40', '392');

----
 select subcontractor_id, count(*) from lutransport.ticket where subcontractor_id in (183, 184, 185)
 group by subcontractor_id;
 
 ----
 INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`)
VALUES ('1411', '/admin/internalSubcon/list.do?rst=1', 'Manage Internal Subcontractor', '3', 'Manage Internal Subcontractor', 
'/admin/internalSubcon/list.do,/admin/internalSubcon/create.do,/admin/internalSubcon/save.do,/admin/internalSubcon/ajax.do,/admin/internalSubcon/edit.do,/admin/internalSubcon/delete.do,
/admin/internalSubcon/search.do,/admin/internalSubcon/export.do',
'1', '10', '0', '140', '/1/140/1411/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '1411', '1'); -- ADMIN

