-- Beavement change

ALTER TABLE `lutransport`.`hourlypayroll_invoice_details` 
ADD COLUMN `bereavementAmount` DOUBLE NULL DEFAULT NULL;

update `lutransport`.`hourlypayroll_invoice_details`
set bereavementAmount = 0.0
where bereavementAmount is null;

update `lutransport`.`paychex_detail` 
set bereavementAmount = 0.0
where bereavementAmount is null;