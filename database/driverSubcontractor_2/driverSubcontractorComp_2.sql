SELECT count(*)
FROM `driver`
WHERE subcontractor_comp IS NOT NULL
ORDER BY id DESC 

update `driver`
set subcontractor_comp = null
WHERE subcontractor_comp IS NOT NULL;

ALTER TABLE `lutransport`.`driver` 
DROP FOREIGN KEY `FKDriverSubCompLocation`;

ALTER TABLE `lutransport`.`driver` 
ADD INDEX `FKDriverSubCompSubcon_idx` (`subcontractor_comp` ASC),
DROP INDEX `FKDriverSubCompLocation_idx` ;

ALTER TABLE `lutransport`.`driver` 
ADD CONSTRAINT `FKDriverSubCompSubcon`
  FOREIGN KEY (`subcontractor_comp`)
  REFERENCES `lutransport`.`subcontractor` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `lutransport`.`driver_tripsheet` 
ADD COLUMN `subcontractor` BIGINT(20) NULL DEFAULT NULL AFTER `entered_by`,
ADD INDEX `FKDriverTSSubconSubcon_idx` (`subcontractor` ASC);

ALTER TABLE `lutransport`.`driver_tripsheet` 
ADD CONSTRAINT `FKDriverTSSubconSubcon`
  FOREIGN KEY (`subcontractor`)
  REFERENCES `lutransport`.`subcontractor` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  
  ALTER TABLE `lutransport`.`subcontractor` 
ADD COLUMN `company_1` BIGINT(20) NULL DEFAULT NULL AFTER `date_terminated`,
ADD COLUMN `terminal_1` BIGINT(20) NULL DEFAULT NULL AFTER `company_1`,
ADD COLUMN `company_2` BIGINT(20) NULL DEFAULT NULL AFTER `terminal_1`,
ADD COLUMN `terminal_2` BIGINT(20) NULL DEFAULT NULL AFTER `company_2`,
ADD COLUMN `company_3` BIGINT(20) NULL DEFAULT NULL AFTER `terminal_2`,
ADD COLUMN `terminal_3` BIGINT(20) NULL DEFAULT NULL AFTER `company_3`,
ADD INDEX `FKSubConLocationComp1_idx` (`company_1` ASC),
ADD INDEX `FKSubConLocationComp2_idx` (`company_2` ASC),
ADD INDEX `FKSubConLocationComp3_idx` (`company_3` ASC),
ADD INDEX `FKSubConLocationTerminal1_idx` (`terminal_1` ASC),
ADD INDEX `FKSubConLocationTerminal2_idx` (`terminal_2` ASC),
ADD INDEX `FKSubConLocationTerminal3_idx` (`terminal_3` ASC);

ALTER TABLE `lutransport`.`subcontractor` 
ADD CONSTRAINT `FKSubConLocationComp1`
  FOREIGN KEY (`company_1`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FKSubConLocationComp2`
  FOREIGN KEY (`company_2`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FKSubConLocationComp3`
  FOREIGN KEY (`company_3`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FKSubConLocationTerminal1`
  FOREIGN KEY (`terminal_1`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FKSubConLocationTerminal2`
  FOREIGN KEY (`terminal_2`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FKSubConLocationTerminal3`
  FOREIGN KEY (`terminal_3`)
  REFERENCES `lutransport`.`location` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  