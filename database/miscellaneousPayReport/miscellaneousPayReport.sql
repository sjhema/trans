INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) VALUES ('9082', '/hr/miscellaneousamount/report/miscellaneouspayreport/start.do?rst=1', 'Miscellaneous Pay Report', '3', 'Miscellaneous Pay Report', '/hr/miscellaneousamount/report/miscellaneouspayreport/start.do?rst=1,/hr/miscellaneousamount/report/miscellaneouspayreport/search.do,/hr/miscellaneousamount/report/miscellaneouspayreport/export.do', '1', '2', '0', '908', '/1/908/9082/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '9082', '1'); -- ADMIN