UPDATE `lutransport`.`ticket` SET `payroll_batch`= null, `payRollStatus`='1' WHERE `id` in (1658974, 1658969);

update ticket set driver = 1397
where id in (1658974, 1658969);

select driver from ticket where id in (1658974, 1658969);