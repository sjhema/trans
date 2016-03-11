-- Fuel log - subcontractor
ALTER TABLE `lutransport`.`fuel_log` 
ADD COLUMN `subcontractor_id` BIGINT(20) NULL DEFAULT NULL;

ALTER TABLE `lutransport`.`fuel_log` 
ADD CONSTRAINT `FK_FUEL_LOG_SUBCONTRACTOR_ID`
  FOREIGN KEY (`subcontractor_id`)
  REFERENCES `lutransport`.`subcontractor` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;