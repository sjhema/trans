CREATE TABLE `loading_fee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `code` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `rate` double NOT NULL,
  `valid_from` datetime NOT NULL,
  `valid_to` datetime NOT NULL,
  `notes` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


INSERT INTO `business_object` (`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`) 
VALUES 
(2058,'/admin/loadingfee/list.do?rst=1','Loading Fee',3,'Loading Fee',
'/admin/loadingfee/list.do,/admin/loadingfee/create.do,/admin/loadingfee/edit.do,/admin/loadingfee/delete.do,/admin/loadingfee/save.do,/admin/loadingfee/search.do',
NULL,NULL,NULL,NULL,1,8,0,205,'/1/205/2058',NULL);

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) 
VALUES (now(), '1', '2058', '1'); -- ADMIN


----
ALTER TABLE `lutransport`.`billing_rate` 
ADD COLUMN `loading_fee` BIGINT(20) NULL COMMENT '' AFTER `peak_rate_valid_to`,
ADD INDEX `loading_fee_fk_idx` (`loading_fee` ASC);

ALTER TABLE `lutransport`.`billing_rate` 
ADD CONSTRAINT `loading_fee_fk`
  FOREIGN KEY (`loading_fee`)
  REFERENCES `lutransport`.`loading_fee` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;