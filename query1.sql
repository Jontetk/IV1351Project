SELECT 
  to_char(year_month,'Mon') as month, 
  (SELECT count(*) as group_lesson FROM group_lesson WHERE date_trunc('year',lesson_date) = date_trunc('year',CURRENT_DATE)
   AND date_trunc('month',lesson_date) = date_trunc('month',year_month)),
	(SELECT count(*) as ensamble FROM ensamble WHERE date_trunc('year',lesson_date) = date_trunc('year',CURRENT_DATE)
   AND date_trunc('month',lesson_date) = date_trunc('month',year_month)
  ),(SELECT count(*) as individual from individual_lesson  as i
	JOIN appointment as a
	ON a.individual_lesson_id = i.individual_lesson_id 
	WHERE date_trunc('year',a.appointment_date) = date_trunc('year',CURRENT_DATE)
 	AND date_trunc('month',a.appointment_date) = date_trunc('month',year_month))
  FROM (SELECT date_trunc('month',generate_series('2023-01-01'::DATE, '2023-12-31'::DATE, '1 month')) AS year_month) m
  ORDER BY year_month;
 