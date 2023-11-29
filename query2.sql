SET SEARCH_PATH = seminar;


Select  "sb_count" as "No of Siblings", COUNT("id") as "No of Students"  FROM
(
select id,sum(sb_count) as "sb_count" from (

  select sb_count,id from (SELECT count(sb.student_id) as "sb_count", s.student_id as "id"
	FROM student as s
	FULL JOIN sibling_of_student as sb
	ON s.student_id = sb.sibling_id
	GROUP by s.student_id
	ORDER BY id)
  union all
  select sb_count,id from (SELECT count(sb.student_id) as "sb_count", s.student_id as "id"
	FROM student as s
	FULL JOIN sibling_of_student as sb
	ON s.student_id = sb.student_id
	GROUP by s.student_id
	ORDER BY id
	)


)
	group by id)
GROUP by "No of Siblings"

ORDER BY "No of Siblings"