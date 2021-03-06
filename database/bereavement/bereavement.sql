-- Bereavement change

ALTER TABLE `lutransport`.`hourlypayroll_invoice_details` 
ADD COLUMN `bereavementAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`hourlypayroll_invoice_details`
set bereavementAmount = 0.0
where bereavementAmount is null;

update `lutransport`.`paychex_detail` 
set bereavementAmount = 0.0
where bereavementAmount is null;

-- Bereavement change - salary

ALTER TABLE `lutransport`.`weeklypay_detail` 
ADD COLUMN `bereavementAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`weeklypay_detail`
set bereavementAmount = 0.0
where bereavementAmount is null;

-- Bereavement change - Driver

ALTER TABLE `lutransport`.`driverpay_detail` 
ADD COLUMN `bereavementAmount` DOUBLE NULL DEFAULT NULL; 

update `lutransport`.`driverpay_detail`
set bereavementAmount = 0.0
where bereavementAmount is null;

ALTER TABLE `lutransport`.`driverpay_freezed` 
ADD COLUMN `bereavementAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`driverpay_freezed`
set bereavementAmount = 0.0
where bereavementAmount is null;
