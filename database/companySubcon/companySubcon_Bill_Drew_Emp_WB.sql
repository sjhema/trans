select * from ticket
where load_date >= '2019-01-01' 
and driver_company = 4 -- WB
and company_location = 5 -- Drew
and (
(origin = 10 and destination = 103)
or (origin = 231 and destination = 226)
or (origin = 231 and destination = 106)
or (origin = 231 and destination = 118)
or (origin = 231 and destination = 90)
or (origin = 20 and destination = 103)
or (origin = 20 and destination = 226)
or (origin = 20 and destination = 75)
or (origin = 25 and destination = 103)
or (origin = 25 and destination = 226)
or (origin = 25 and destination = 90)
or (origin = 35 and destination = 123)
or (origin = 40 and destination = 392)
)
and subcontractor_id is null
order by load_date desc

update ticket
set subcontractor_id = 185  -- WB subcon
where load_date >= '2019-01-01' 
and driver_company = 4 -- WB
and company_location = 5 -- Drew
and subcontractor_id is null
and (
(origin = 10 and destination = 103)
or (origin = 231 and destination = 226)
or (origin = 231 and destination = 106)
or (origin = 231 and destination = 118)
or (origin = 231 and destination = 90)
or (origin = 20 and destination = 103)
or (origin = 20 and destination = 226)
or (origin = 20 and destination = 75)
or (origin = 25 and destination = 103)
or (origin = 25 and destination = 226)
or (origin = 25 and destination = 90)
or (origin = 35 and destination = 123)
or (origin = 40 and destination = 392)
)
-------------------------

