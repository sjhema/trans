INSERT INTO `lutransport`.`business_object` (`ID`, `ACTION`, `DISPLAY_TAG`, `OBJECT_LEVEL`, `OBJECT_NAME`, `URL`, `status`, `display_order`, `hidden`, `parent_id`, `hierarchy`) 
VALUES ('9034', '/hr/salaryoverride/list.do?rst=1', 'Salary Override', '3', 'Salary Override', '/hr/salaryoverride/list.do,/hr/salaryoverride/create.do,/hr/salaryoverride/save.do,/hr/salaryoverride/edit.do,/hr/salaryoverride/search.do,/hr/salaryoverride/export.do,/hr/salaryoverride/ajax.do', '1', '4', '0', '903', '/1/903/9034/');

INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '9034', '1'); -- ADMIN
INSERT INTO `lutransport`.`role_privilege` (`created_at`, `status`, `business_object_id`, `role_id`) VALUES (now(), '1', '9034', '7'); -- HR

CREATE TABLE `lutransport`.`salary_override` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `payroll_batch` datetime NOT NULL,
  `amount` double NOT NULL,
  `notes` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `company_id` bigint(20) NOT NULL,
  `employee_id` bigint(20) NOT NULL,
  `terminal` bigint(20) NOT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `salary_override_payroll_batch_idx` (`payroll_batch`),
  KEY `salary_override_employee_fk` (`employee_id`),
  KEY `salary_override_terminal_fk` (`terminal`),
  KEY `salary_override_company_fk` (`company_id`),
  CONSTRAINT `salary_override_company_fk` FOREIGN KEY (`company_id`) REFERENCES `location` (`id`),
  CONSTRAINT `salary_override_employee_fk` FOREIGN KEY (`employee_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `salary_override_terminal_fk` FOREIGN KEY (`terminal`) REFERENCES `location` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
