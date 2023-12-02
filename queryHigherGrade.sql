SET SEARCH_PATH = seminar;


CREATE OR REPLACE TEMPORARY VIEW  student_and_mail AS
(SELECT st.student_id as st_id, first_name, last_name, array_agg(mail_adress) as mail_adresses 
 FROM student as st
JOIN mail_adress_student as ma
ON ma.student_id = st.student_id
GROUP BY  st_id, first_name, last_name);


DROP VIEW if exists group_lesson_info;
CREATE TEMPORARY VIEW group_lesson_info AS 
(SELECT gl.lesson_type, instrument_type, price, array_agg(concat(sm.first_name,' ', sm.last_name,' ', sm.mail_adresses)) as "name_mail" FROM group_lesson AS gl
JOIN cost_per_lesson AS cpl
ON cpl.price_set_date = gl.price_set_date 
AND  cpl.skill_level = gl.skill_level
AND cpl.lesson_type = gl.lesson_type
JOIN group_lesson_taken as lt
ON gl.group_lesson_id = lt.group_lesson_id
JOIN student_and_mail as sm 
ON lt.student_id = sm.st_id
GROUP BY gl.lesson_type, instrument_type, price
);


DROP VIEW if exists ensamble_info;
CREATE TEMPORARY VIEW ensamble_info AS 
(SELECT ens.lesson_type,genre, price, array_agg(concat(sm.first_name,' ', sm.last_name,' ', sm.mail_adresses)) as "name_mail" FROM ensamble AS ens
JOIN cost_per_lesson AS cpl
ON cpl.price_set_date = ens.price_set_date 
AND  cpl.skill_level = ens.skill_level
AND cpl.lesson_type = ens.lesson_type
 
JOIN ensamble_taken as lt
ON ens.ensamble_id = lt.ensamble_id
JOIN student_and_mail as sm 
ON lt.student_id = sm.st_id
GROUP BY ens.lesson_type,genre, price

);


DROP VIEW if exists individual_lesson_info;
CREATE TEMPORARY VIEW individual_lesson_info AS 
(SELECT il.lesson_type,instrument_type, price, array_agg(concat(sm.first_name,' ', sm.last_name,' ', sm.mail_adresses)) as "name_mail" FROM individual_lesson as il
JOIN cost_per_lesson AS cpl
ON cpl.price_set_date = il.price_set_date 
AND  cpl.skill_level = il.skill_level
AND cpl.lesson_type = il.lesson_type

JOIN individual_lesson_taken as lt
ON il.individual_lesson_id = lt.individual_lesson_id
JOIN student_and_mail as sm 
ON lt.student_id = sm.st_id
GROUP BY il.lesson_type,instrument_type, price
);

	CREATE OR REPLACE FUNCTION todays_data() RETURNS void AS $$
	BEGIN
  	EXECUTE'CREATE TABLE lesson_data_'||to_char(CURRENT_TIMESTAMP, 'YYYY_MM_DD_HH24_MI_SS')||' AS SELECT lesson_type,genre, null AS instrument_type ,price, name_mail FROM ensamble_info
	UNION ALL
	SELECT lesson_type,null AS genre, instrument_type, price, name_mail FROM group_lesson_info
	UNION ALL
	SELECT lesson_type,null AS genre, instrument_type, price, name_mail FROM individual_lesson_info';
END;
$$ LANGUAGE plpgsql;
SELECT todays_data();