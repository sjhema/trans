ALTER TABLE `lutransport`.`billing_rate` 
ADD COLUMN `peak_rate` DOUBLE NULL DEFAULT NULL COMMENT '' AFTER `min_billable_tons`,
ADD COLUMN `peak_rate_valid_from` VARCHAR(10) NULL DEFAULT NULL COMMENT '' AFTER `peak_rate`,
ADD COLUMN `peak_rate_valid_to` VARCHAR(10) NULL DEFAULT NULL COMMENT '' AFTER `peak_rate_valid_from`;

ALTER TABLE `lutransport`.`invoice_detail` 
ADD COLUMN `is_peak_rate` VARCHAR(15) NULL DEFAULT 'N' COMMENT '' AFTER `min_billable_tons`;

ALTER TABLE `lutransport`.`invoice_detail_new` 
ADD COLUMN `is_peak_rate` VARCHAR(15) NULL DEFAULT 'N' COMMENT '' AFTER `driver_pay_rate`;

update invoice_detail
set is_peak_rate='Y'
where origin='BCRRF' 
and destination='Bresco'
and rate=15.95;

update invoice_detail
set is_peak_rate = 'Y'
where origin='Western Acceptance' 
and destination='Bresco'
and rate=13.43;

update invoice_detail_new
set is_peak_rate = 'Y'
where t_origin='BCRRF' 
and t_destination='Bresco'
and rate = 15.95;

update invoice_detail_new
set is_peak_rate = 'Y'
where t_origin='Western Acceptance' 
and t_destination='Bresco'
and rate = 13.43;

----
ALTER TABLE `lutransport`.`invoice_detail` 
ADD INDEX `idx_is_peak_rate` USING BTREE (`is_peak_rate` ASC)  COMMENT '';
