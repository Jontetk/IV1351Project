Create or replace temporary view rented as
select i.instrument_id from instrument as i
join rental as r
on r.instrument_id = i.instrument_id
where end_date is null;


select i.instrument_id,brand,instrument_type,instrument_code,rental_fee from instrument as i
full join rented as r
on i.instrument_id = r.instrument_id
where r.instrument_id is null;