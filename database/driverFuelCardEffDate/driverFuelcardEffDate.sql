ALTER TABLE `lutransport`.`driver_fuelcard` 
ADD COLUMN `valid_from` DATETIME NULL DEFAULT NULL AFTER `vehicle`,
ADD COLUMN `valid_to` DATETIME NULL DEFAULT NULL AFTER `valid_from`;
