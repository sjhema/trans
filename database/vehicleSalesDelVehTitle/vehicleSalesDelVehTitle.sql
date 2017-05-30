select t.id as t_id, t.created_at, t.vehicle, t.title, s.id as s_id, s.created_at, s.vehicle, s.buyer from vehicle_title t join vehicle_sale s
where t.vehicle = s.vehicle
order by t.vehicle asc;

select * from vehicle_title t where
vehicle in 
(select vehicle from vehicle_sale)
order by t.vehicle asc;

select unit from vehicle
where id=34;

delete from vehicle_title where id != 0 and 
vehicle in 
(select vehicle from vehicle_sale)