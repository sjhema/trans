ALTER TABLE `lutransport`.`ez_toll`
ADD COLUMN `invoice_date` DATETIME NULL DEFAULT NULL COMMENT '';

ALTER TABLE `lutransport`.`tollcompany`
ADD COLUMN `company_id` BIGINT(20) NULL DEFAULT NULL COMMENT '';

ALTER TABLE `lutransport`.`tollcompany`
ADD CONSTRAINT `TOLL_COMPANY_LOCAION_FK` FOREIGN KEY (`company_id`) REFERENCES `location` (`id`);

