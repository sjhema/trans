-- Worker comp change - Driver

ALTER TABLE `lutransport`.`driverpay_detail` 
ADD COLUMN `workerCompAmount` DOUBLE NULL DEFAULT NULL; 

update `lutransport`.`driverpay_detail`
set workerCompAmount = 0.0
where workerCompAmount is null;

ALTER TABLE `lutransport`.`driverpay_freezed` 
ADD COLUMN `workerCompAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`driverpay_freezed`
set workerCompAmount = 0.0
where workerCompAmount is null;

ALTER TABLE `lutransport`.`paychex_detail` 
ADD COLUMN `workerCompAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`paychex_detail` 
set workerCompAmount = 0.0
where workerCompAmount is null;

-- Bereavement change - Hourly

ALTER TABLE `lutransport`.`hourlypayroll_invoice_details` 
ADD COLUMN `workerCompAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`hourlypayroll_invoice_details`
set workerCompAmount = 0.0
where workerCompAmount is null;
