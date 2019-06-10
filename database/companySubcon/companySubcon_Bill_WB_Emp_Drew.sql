select * from ticket
where load_date >= '2019-01-01' 
and driver_company = 5 -- Drew
and company_location = 4 -- WB
and (
(origin = 245 and destination = 330)
or (origin = 331 and destination = 330)
or (origin = 426 and destination = 312)
or (origin = 25 and destination = 312)
or (origin = 32 and destination = 123)
)
and subcontractor_id is null
order by load_date desc

update ticket
set subcontractor_id = 184  -- Drew subcon
where load_date >= '2019-01-01' 
and driver_company = 5 -- Drew
and company_location = 4 -- WB
and subcontractor_id is null
and (
(origin = 245 and destination = 330)
or (origin = 331 and destination = 330)
or (origin = 426 and destination = 312)
or (origin = 25 and destination = 312)
or (origin = 32 and destination = 123)
)
-------------------------