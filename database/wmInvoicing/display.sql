-- Change for Reports menu
UPDATE `lutransport`.`business_object` SET `ACTION`='/reportuser/report/billinghistory/start.do' WHERE `ID`='301';

INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('40012', '/reportuser/report/billing/start.do', 'Invoicing', '2', 'Invoicing', '/reportuser/report/billing/start.do', '1', '4', '0', '1', '/1/40012/');
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`)
 VALUES (now(), '1', '40012', '1'); -- ADMIN
 
-- Invoicing sub menu 
UPDATE `lutransport`.`business_object` SET `parent_id`='40012', `hierarchy`='/1/40012/3011/' WHERE `ID`='3011';
-- Manage Invoices
UPDATE `lutransport`.`business_object` SET `parent_id`='40012', `hierarchy`='/1/40012/3013/' WHERE `ID`='3013';
UPDATE `lutransport`.`business_object` SET `display_order`='2' WHERE `ID`='3013';

-- Upload, Manage, Verify WM invoices
UPDATE `lutransport`.`business_object` SET `display_order`='3', `parent_id`='40012', `hierarchy`='/1/40012/1406/' WHERE `ID`='1406';
UPDATE `lutransport`.`business_object` SET `display_order`='4', `parent_id`='40012', `hierarchy`='/1/40012/1407/' WHERE `ID`='1407';
UPDATE `lutransport`.`business_object` SET `display_order`='5', `parent_id`='40012', `hierarchy`='/1/40012/1408/' WHERE `ID`='1408';


