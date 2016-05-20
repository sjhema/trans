ALTER TABLE `lutransport`.`driver_mobile_entry` 
ADD COLUMN `entered_by` VARCHAR(255) NULL DEFAULT NULL;

ALTER TABLE `lutransport`.`driver_mobile_entry` 
ADD COLUMN `entered_by_id` BIGINT(20) NULL DEFAULT NULL,
ADD COLUMN `entered_by_type` VARCHAR(45) NULL DEFAULT NULL;