INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('2057', '/uploadData/subcontractorrate.do', 'Upload Subcontractor Rate', '3', 'Upload Subcontractor Rate', '/uploadData/subcontractorrate.do,/uploadData/subcontractorrate/upload.do', '1', '6', '0', '205', '/1/205/2057/');
UPDATE `lutransport`.`business_object` SET `display_order`='7' WHERE `ID`='2056';

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2057', '1'); -- ADMIN
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2057', '2'); -- OPERATOR
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2057', '6'); -- BILLING
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2057', '8'); -- ADMIN_HR1
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2057', '10'); -- SPECIAL_PRIVILEGE_1
