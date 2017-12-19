ALTER TABLE `lutransport`.`timesheet` 
ADD COLUMN `seq_num1` INT(11) NULL DEFAULT NULL AFTER `w_lunch_hours`;

ALTER TABLE `lutransport`.`hourlypayroll_invoice_details` 
ADD COLUMN `seq_num1` INT(11) NULL DEFAULT NULL AFTER `bereavementAmount`;
