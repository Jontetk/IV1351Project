drop view if exists lessons_this_week;
create temporary view lessons_this_week as 
select * from ensamble where date_trunc('week',lesson_date) = date_trunc('week',Current_date+7);

select TO_char(ltw.lesson_date, 'Dy') as "Day",ltw.genre as "Genre",
CASE  
	When ltw.max_student_allowed - count(et.student_id)  = 0 THEN 'No seats'
	When ltw.max_student_allowed - count(et.student_id)  >0 AND ltw.max_student_allowed - count(et.student_id)  <2 THEN '1 or 2 seats'
	ELSE 'Many seats'
END as "No of Free Seats"
 	



from ensamble_taken as et 
join lessons_this_week as ltw
on et.ensamble_id = ltw.ensamble_id
Group by et.ensamble_id,ltw.lesson_date,ltw.genre,ltw.max_student_allowed
Order by ltw.lesson_date





