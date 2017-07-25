ALTER TABLE `lutransport`.`billing_rate` 
ADD COLUMN `min_billable_tons` DOUBLE NULL DEFAULT NULL AFTER `demmurage_charge`;

ALTER TABLE `lutransport`.`invoice_detail` 
ADD COLUMN `min_billable_tons` DOUBLE NULL DEFAULT NULL AFTER `driver_companyid`;

