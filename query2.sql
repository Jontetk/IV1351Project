SET SEARCH_PATH = seminar;


Select  "sb_count" as "No of Siblings", COUNT("id") as "No of Students"  FROM
(SELECT count(s.student_id) as "sb_count", s.student_id as "id"
FROM student as s
JOIN sibling_of_student as sb
ON s.student_id = sb.student_id
GROUP by s.student_id)
GROUP by "No of Siblings"

ORDER BY "No of Siblings"