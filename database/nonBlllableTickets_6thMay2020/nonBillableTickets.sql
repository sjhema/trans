ALTER TABLE `lutransport`.`ticket` 
ADD COLUMN `not_billable` VARCHAR(3) NULL DEFAULT NULL COMMENT '' AFTER `autoCreated`;

