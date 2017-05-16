ALTER TABLE `lutransport`.`driver` 
ADD COLUMN `dob` DATETIME NULL DEFAULT NULL AFTER `ssn`,
ADD COLUMN `driver_license` VARCHAR(100) NULL DEFAULT NULL AFTER `dob`,
ADD COLUMN `driver_license_state` BIGINT(20) NULL DEFAULT NULL AFTER `driver_license`,
ADD INDEX `FKDriverLicenseState_idx` (`driver_license_state` ASC);

ALTER TABLE `lutransport`.`driver` 
ADD CONSTRAINT `FKDriverLicenseState`
  FOREIGN KEY (`driver_license_state`)
  REFERENCES `lutransport`.`state` (`id`);
  
--
INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('2035', '/uploadData/employee.do', 'Upload Employee', '3', 'Upload Employee', '/uploadData/employee.do,/uploadData/employee/upload.do', '1', '2', '0', '203', '/1/203/2035/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '2035', '1'); -- ADMIN  
  
UPDATE `lutransport`.`business_object` SET `display_order`='3' WHERE `ID`='2032'; -- Moving subcontractor down
---
select MAX(CAST(obj.staff_id AS UNSIGNED)) from driver obj;
select * from driver where staff_id=727;

select * from driver where staff_id = 12345678 and id=1297;

delete from driver where staff_id = 12345678 and id=1297;

select * from leave_current_balance where emp_name=1297;

delete from leave_current_balance where emp_name=1297;  