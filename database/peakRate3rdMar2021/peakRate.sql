ALTER TABLE `lutransport`.`billing_rate` 
ADD COLUMN `peak_rate` DOUBLE NULL DEFAULT NULL COMMENT '' AFTER `min_billable_tons`,
ADD COLUMN `peak_rate_valid_from` VARCHAR(10) NULL DEFAULT NULL COMMENT '' AFTER `peak_rate`,
ADD COLUMN `peak_rate_valid_to` VARCHAR(10) NULL DEFAULT NULL COMMENT '' AFTER `peak_rate_valid_from`;
