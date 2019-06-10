select * from ticket
where load_date >= '2019-01-01' 
and driver_company = 6 -- LU
and company_location = 4 -- WB
and (
(origin = 24 and destination = 81)
or (origin = 411 and destination = 392)
or (origin = 411 and destination = 129)
or (origin = 28 and destination = 392)
or (origin = 29 and destination = 130)
or (origin = 29 and destination = 136)
or (origin = 31 and destination = 392)
or (origin = 31 and destination = 78)
or (origin = 40 and destination = 392)
or (origin = 40 and destination = 351)
or (origin = 40 and destination = 130)
or (origin = 432 and destination = 392)
or (origin = 374 and destination = 78)
or (origin = 55 and destination = 392)
or (origin = 55 and destination = 136)
or (origin = 55 and destination = 78)
or (origin = 56 and destination = 392)
or (origin = 56 and destination = 78)
or (origin = 431 and destination = 392)
or (origin = 67 and destination = 230)
)
and subcontractor_id is null
order by load_date desc

update ticket
set subcontractor_id = 183 -- LU subcon
where load_date >= '2019-01-01' 
and driver_company = 6 -- LU
and company_location = 4 -- WB
and subcontractor_id is null
and (
(origin = 24 and destination = 81)
or (origin = 411 and destination = 392)
or (origin = 411 and destination = 129)
or (origin = 28 and destination = 392)
or (origin = 29 and destination = 130)
or (origin = 29 and destination = 136)
or (origin = 31 and destination = 392)
or (origin = 31 and destination = 78)
or (origin = 40 and destination = 392)
or (origin = 40 and destination = 351)
or (origin = 40 and destination = 130)
or (origin = 432 and destination = 392)
or (origin = 374 and destination = 78)
or (origin = 55 and destination = 392)
or (origin = 55 and destination = 136)
or (origin = 55 and destination = 78)
or (origin = 56 and destination = 392)
or (origin = 56 and destination = 78)
or (origin = 431 and destination = 392)
or (origin = 67 and destination = 230)
)
-------------------------