SET SEARCH_PATH = seminar;


SELECT stat.instructor_id, first_name, last_name, "count" as lesson_count
FROM 

(SELECT instructor_id , COUNT(instructor_id) FROM

((SELECT instructor_id from group_lesson as l
JOIN group_lesson_instructed  as ins
ON l.group_lesson_id = ins.group_lesson_id 
WHERE date_trunc('month',lesson_date) = date_trunc('month',CURRENT_DATE)
ORDER BY instructor_id)

UNION ALL

(SELECT  instructor_id from ensamble as l
JOIN ensamble_instructed  as ins
ON l.ensamble_id = ins.ensamble_id 
WHERE date_trunc('month',lesson_date) = date_trunc('month',CURRENT_DATE)
ORDER BY instructor_id)

UNION ALL

(SELECT  instructor_id from individual_lesson as l
JOIN appointment as ap 
ON l.individual_lesson_id = ap.individual_lesson_id
JOIN individual_lesson_instructed  as ins
ON l.individual_lesson_id = ins.individual_lesson_id 
WHERE date_trunc('month',appointment_date) = date_trunc('month',CURRENT_DATE)
ORDER BY instructor_id)) 

GROUP by instructor_id) as "stat"

JOIN instructor as inst
ON stat.instructor_id = inst.instructor_id