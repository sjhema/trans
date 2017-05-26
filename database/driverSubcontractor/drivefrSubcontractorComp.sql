ALTER TABLE `lutransport`.`driver` 
ADD COLUMN `subcontractor_comp` BIGINT(20) NULL DEFAULT NULL AFTER `driver_license_state`,
ADD INDEX `FKDriverSubCompLocation_idx` (`subcontractor_comp` ASC);
ALTER TABLE `lutransport`.`driver` 
ADD CONSTRAINT `FKDriverSubCompLocation`
  FOREIGN KEY (`subcontractor_comp`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;