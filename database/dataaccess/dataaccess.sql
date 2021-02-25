INSERT INTO `business_object` 
(`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`) 
VALUES (2044,'/admin/access/dataprivilege/list.do?rst=1','Manage Data Access',3,'Manage Data Access',
'/admin/access/dataprivilege/list.do,/admin/access/dataprivilege/create.do,/admin/access/dataprivilege/save.do,/admin/access/dataprivilege/edit.do,/admin/access/dataprivilege/delete.do,/admin/access/dataprivilege/search.do',
NULL,NULL,NULL,NULL,1,4,1,204,'/1/204/2044/',NULL);

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
VALUES (now(), '1', '2044', '1'); -- ADMIN

CREATE TABLE `data_privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `data_type` varchar(255) NOT NULL,
  `privilege` varchar(255) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `business_object_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `data_priv_role_fk_idx` (`role_id`),
  KEY `data_priv_bo_idx` (`business_object_id`),
  CONSTRAINT `data_priv_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `data_priv_bo_fk` FOREIGN KEY (`business_object_id`) REFERENCES `business_object` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

INSERT INTO `lutransport`.`data_privilege` 
(`id`, `created_at`, `created_by`, `status`, `data_type`, `privilege`, `role_id`, `business_object_id`)
VALUES ('1', '2021-02-23 11:27:22', '1', '1', 'EMP_CAT', '1,2,3,4,5,6,7,8,9,10,11', '1', 6013); -- ADMIN payroll report

INSERT INTO `lutransport`.`data_privilege` 
(`id`, `created_at`, `created_by`, `status`, `data_type`, `privilege`, `role_id`, `business_object_id`)
VALUES ('2', '2021-02-23 11:27:22', '1', '1', 'EMP_CAT', '1,2,3,4,5,6,7,8,9,10,11', '1', 2031); -- ADMIN manage employee

INSERT INTO `lutransport`.`data_privilege` 
(`id`, `created_at`, `created_by`, `status`, `data_type`, `privilege`, `role_id`, `business_object_id`)
VALUES ('3', '2021-02-23 11:27:22', '1', '1', 'EMP_CAT', '2,3,6', '3', 6013); -- REPORTUSER payroll report


