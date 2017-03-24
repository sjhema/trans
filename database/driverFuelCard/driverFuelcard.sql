ALTER TABLE `lutransport`.`driver_fuelcard` 
ADD COLUMN `vehicle` BIGINT(20) NULL DEFAULT NULL AFTER `fuel_vendor`,
ADD INDEX `driverFuelCardVehicle_fk_idx` (`vehicle` ASC);
ALTER TABLE `lutransport`.`driver_fuelcard` 
ADD CONSTRAINT `driverFuelCardVehicle_fk`
  FOREIGN KEY (`vehicle`)
  REFERENCES `lutransport`.`vehicle` (`id`);