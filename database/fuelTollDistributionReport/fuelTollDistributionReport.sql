-- Fuel Distribution Report - 30103
INSERT INTO `lutransport`.`business_object`
(`ID`, `ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,
  `URL`,
  `created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
VALUES (30103, '/reportuser/report/fueldistribution/start.do','Fuel Distribution Report',3,'Fuel Distribution Report',
  '/reportuser/report/fueldistribution/start.do,/reportuser/report/fueldistribution/save.do,/reportuser/report/fueldistribution/search.do,/reportuser/report/fueldistribution/export.do,/reportuser/report/fueldistribution/ajax.do',
  NULL,NULL,NULL,NULL,1,13,0,301,'/1/301/30103',NULL);

  INSERT INTO `lutransport`.`role_privilege` (`status`, `business_object_id`, `role_id`) VALUES ('1', '30103', '1'); -- 1 -  ADMIN
  -- INSERT INTO `lutransport`.`role_privilege` (`status`, `business_object_id`, `role_id`) VALUES ('1', '30103', '10'); -- 10 - SPECIAL_PRIVILEGE_1
  -- INSERT INTO `lutransport`.`role_privilege` (`status`, `business_object_id`, `role_id`) VALUES ('1', '30103', '8'); -- 8 - ADMIN_HR1

-- Toll Distribution Report - 30104
INSERT INTO `lutransport`.`business_object`
(`ID`, `ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,
  `URL`,
  `created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`)
VALUES (30104, '/reportuser/report/tolldistribution/start.do','Toll Distribution Report',3,'Toll Distribution Report',
  '/reportuser/report/tolldistribution/start.do,/reportuser/report/tolldistribution/save.do,/reportuser/report/tolldistribution/search.do,/reportuser/report/tolldistribution/export.do,/reportuser/report/tolldistribution/ajax.do',
  NULL,NULL,NULL,NULL,1,14,0,301,'/1/301/30104',NULL);

  INSERT INTO `lutransport`.`role_privilege` (`status`, `business_object_id`, `role_id`) VALUES ('1', '30104', '1'); -- 1 -  ADMIN
  -- INSERT INTO `lutransport`.`role_privilege` (`status`, `business_object_id`, `role_id`) VALUES ('1', '30104', '10'); -- 10 - SPECIAL_PRIVILEGE_1
  -- INSERT INTO `lutransport`.`role_privilege` (`status`, `business_object_id`, `role_id`) VALUES ('1', '30104', '8'); -- 8 - ADMIN_HR1

