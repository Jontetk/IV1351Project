CREATE SCHEMA seminar;
SET SEARCH_PATH = seminar;


CREATE TABLE instrument(
	instrument_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	brand varchar(100),
	instrument_type varchar(100),
	instrument_code varchar(10),
	rental_fee float(10),
	PRIMARY KEY(instrument_id)

);
CREATE TABLE rental(
	rental_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE,
	instrument_id INT,
	PRIMARY KEY(rental_id),
	FOREIGN KEY(instrument_id) REFERENCES instrument(instrument_id) ON DELETE SET NULL
	

);

CREATE TABLE student(
    student_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	person_number VARCHAR(13) UNIQUE NOT NULL,
    first_name VARCHAR(200),
    last_name VARCHAR(200),
	zip CHAR(5),
	city VARCHAR(100),
	street_adress VARCHAR(100),
    enrollment_date DATE NOT NULL,
	PRIMARY KEY (student_id)

	

);

CREATE TABLE students_renting(
	rental_id INT NOT NULL,
	student_id INT NOT NULL,
	UNIQUE(rental_id,student_id),
	FOREIGN KEY(rental_id) REFERENCES rental(rental_id) ON DELETE CASCADE,
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE

);

CREATE TABLE max_rental_per_student(
	max_rental_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	max_rental INT,
	set_date DATE,
	PRIMARY KEY(max_rental_id)
);





CREATE TABLE phone_number_student(
	phone_number VARCHAR(20) NOT NULL,
	student_id INT NOT NULL,
	PRIMARY KEY (phone_number),
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

CREATE TABLE mail_adress_student(
	mail_adress VARCHAR(100) NOT NULL,
	student_id INT NOT NULL,
	PRIMARY KEY (mail_adress),
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);


CREATE TABLE sibling_of_student(
	student_id INT NOT NULL,
	sibling_id INT NOT NULL,
	UNIQUE(student_id,sibling_id),
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE,
	FOREIGN KEY(sibling_id) REFERENCES student(student_id) ON DELETE CASCADE
);
CREATE TABLE contact_person(
	contact_person_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	person_number VARCHAR(13) UNIQUE NOT NULL,
    first_name VARCHAR(200),
    last_name VARCHAR(200),
	PRIMARY KEY(contact_person_id)
);

CREATE TABLE contact_person_of_student(
	contact_person_id INT NOT NULL,
	student_id INT NOT NULL,
	UNIQUE(student_id,contact_person_id),
	FOREIGN KEY (contact_person_id) REFERENCES contact_person(contact_person_id) ON DELETE CASCADE,
	FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE
);


CREATE TABLE phone_number_contact_person(
	phone_number VARCHAR(20) NOT NULL,
	contact_person_id INT NOT NULL,
	PRIMARY KEY (phone_number),
	FOREIGN KEY(contact_person_id) REFERENCES contact_person(contact_person_id) ON DELETE CASCADE
);

CREATE TABLE mail_adress_contact_person(
	mail_adress VARCHAR(100) NOT NULL,
	contact_person_id INT NOT NULL,
	PRIMARY KEY (mail_adress),
	FOREIGN KEY(contact_person_id) REFERENCES contact_person(contact_person_id) ON DELETE CASCADE
);

CREATE TABLE instructor(
	instructor_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	person_number VARCHAR(13) UNIQUE NOT NULL,
    first_name VARCHAR(200),
    last_name VARCHAR(200),
	zip CHAR(5),
	city VARCHAR(100),
	street_adress VARCHAR(100),
	PRIMARY KEY(instructor_id)

);

CREATE TABLE phone_number_instructor(
	phone_number VARCHAR(20) NOT NULL,
	instructor_id INT NOT NULL,
	PRIMARY KEY (phone_number),
	FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id) ON DELETE CASCADE
);

CREATE TABLE mail_adress_instructor(
	mail_adress VARCHAR(100) NOT NULL,
	instructor_id INT NOT NULL,
	PRIMARY KEY (mail_adress),
	FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id) ON DELETE CASCADE
);

CREATE TABLE discount(
	discount_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	discount_percentage FLOAT(5),
	discount_date DATE,
	number_of_siblings_required INT,
	PRIMARY KEY (discount_id)
);

CREATE TABLE avalible_time(
	instructor_id INT NOT NULL,
	avalible_time_id INT GENERATED ALWAYS AS IDENTITY NOT NULL, 
	time_start TIME(6),
	time_end TIME(6),
	avalible_date DATE,
	PRIMARY KEY(avalible_time_id),
	FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id) ON DELETE CASCADE
);
CREATE TABLE cost_per_lesson(
	lesson_type VARCHAR(50) NOT NULL,
	skill_level VARCHAR(50) NOT NULL,
	price_set_date DATE NOT NULL,
	price CHAR(10),
	PRIMARY KEY(lesson_type,skill_level,price_set_date)
);


CREATE TABLE individual_lesson(
	individual_lesson_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	lesson_description VARCHAR(1000),
	instrument_type VARCHAR(100),
	lesson_type VARCHAR(50),
	skill_level VARCHAR(50),
	price_set_date DATE,
	PRIMARY KEY(individual_lesson_id),
	FOREIGN KEY (lesson_type,skill_level,price_set_date) REFERENCES cost_per_lesson(lesson_type,skill_level,price_set_date) ON DELETE SET NULL

);

CREATE TABLE class_room(
	class_room_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	class_room_name VARCHAR(100),
	floor VARCHAR(10),
	room_code VARCHAR(10),
	PRIMARY KEY(class_room_id)


);


CREATE TABLE appointment(
	individual_lesson_id INT NOT NULL,
	time_start TIME(6),
	time_end TIME(6),
	appointment_date DATE,
	place VARCHAR(100),
	FOREIGN KEY(individual_lesson_id) REFERENCES individual_lesson(individual_lesson_id) ON DELETE CASCADE
);

CREATE TABLE individual_lesson_taken (
	individual_lesson_id INT NOT NULL,
	student_id INT NOT NULL,
	UNIQUE(individual_lesson_id,student_id),
	FOREIGN KEY(individual_lesson_id) REFERENCES individual_lesson(individual_lesson_id) ON DELETE CASCADE,
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);
CREATE TABLE individual_lesson_instructed (
	individual_lesson_id INT NOT NULL,
	instructor_id INT NOT NULL,
	UNIQUE(individual_lesson_id,instructor_id),
	FOREIGN KEY(individual_lesson_id) REFERENCES individual_lesson(individual_lesson_id) ON DELETE CASCADE,
	FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id) ON DELETE CASCADE
);
CREATE TABLE group_lesson (
	group_lesson_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	lesson_description VARCHAR(1000),
	min_student_allowed INT,
	max_student_allowed INT,
	time_start TIME(6),
	time_end TIME(6),
	lesson_date DATE,
	instrument_type VARCHAR(100),
	class_room_id INT,
	lesson_type VARCHAR(50),
	skill_level VARCHAR(50),
	price_set_date DATE,
	PRIMARY KEY (group_lesson_id),
	FOREIGN KEY(class_room_id) REFERENCES class_room(class_room_id) ON DELETE SET NULL,
	FOREIGN KEY (lesson_type,skill_level,price_set_date) REFERENCES cost_per_lesson(lesson_type,skill_level,price_set_date) ON DELETE SET NULL

);

CREATE TABLE group_lesson_taken(
	student_id INT NOT NULL,
	group_lesson_id INT NOT NULL,
	UNIQUE(student_id,group_lesson_id),
	FOREIGN KEY(group_lesson_id) REFERENCES group_lesson(group_lesson_id) ON DELETE CASCADE,
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

CREATE TABLE group_lesson_instructed (
	group_lesson_id INT NOT NULL,
	instructor_id INT NOT NULL,
	UNIQUE(group_lesson_id,instructor_id),
	FOREIGN KEY(group_lesson_id) REFERENCES group_lesson(group_lesson_id) ON DELETE CASCADE,
	FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id) ON DELETE CASCADE
);
CREATE TABLE ensamble (
	ensamble_id INT GENERATED ALWAYS AS IDENTITY NOT NULL,
	lesson_description VARCHAR(1000),
	min_student_allowed INT,
	max_student_allowed INT,
	time_start TIME(6),
	time_end TIME(6),
	lesson_date DATE,
	genre VARCHAR(100),
	class_room_id INT,
	lesson_type VARCHAR(50),
	skill_level VARCHAR(50),	
	price_set_date DATE,
	PRIMARY KEY (ensamble_id),
	FOREIGN KEY(class_room_id) REFERENCES class_room(class_room_id) ON DELETE SET NULL,
	FOREIGN KEY (lesson_type,skill_level,price_set_date) REFERENCES cost_per_lesson(lesson_type,skill_level,price_set_date) ON DELETE SET NULL


);


CREATE TABLE ensamble_instrument_type (
	instrument_type VARCHAR(100) NOT NULL,
	ensamble_id INT NOT NULL,
	UNIQUE(instrument_type,ensamble_id),
	FOREIGN KEY(ensamble_id) REFERENCES ensamble(ensamble_id) ON DELETE CASCADE

);



CREATE TABLE ensamble_taken (
	student_id INT NOT NULL,
	ensamble_id INT NOT NULL,
	UNIQUE(student_id,ensamble_id),
	FOREIGN KEY(ensamble_id) REFERENCES ensamble(ensamble_id) ON DELETE CASCADE,
	FOREIGN KEY(student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

CREATE TABLE ensamble_instructed (
	ensamble_id INT NOT NULL,
	instructor_id INT NOT NULL,
	UNIQUE(ensamble_id,instructor_id),
	FOREIGN KEY(ensamble_id) REFERENCES ensamble(ensamble_id) ON DELETE CASCADE,
	FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id) ON DELETE CASCADE
);