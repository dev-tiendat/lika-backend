-- drop database lika;
create database lika;
use lika;

CREATE TABLE `users`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `username` nvarchar(30) NOT NULL UNIQUE,
    `password` nvarchar(255) NOT NULL,
    `email` nvarchar(50) DEFAULT '',
    `phone_number` nvarchar(20) DEFAULT '',
	`first_name` nvarchar(100) NOT NULL,
    `last_name` nvarchar(100) NOT NULL,	
    `address` nvarchar(255) DEFAULT '',
    `gender` tinyint(1) DEFAULT 0,
    `date_of_birth` DATE DEFAULT '2000-01-01',
    `status` tinyint(1) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `roles`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `name` nvarchar(255) NOT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `user_role`(
    `user_id` bigint unsigned NOT NULL,
    `role_id` bigint unsigned NOT NULL,
    PRIMARY KEY(`user_id`, `role_id`),
    KEY `fk_security_user_id` (`user_id`),
    KEY `fk_security_role_id` (`role_id`),
    CONSTRAINT `fk_security_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_security_role_id` FOREIGN KEY(`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `subjects`(
	`subject_id` varchar(12) NOT NULL,
    `subject_name` nvarchar(50) NOT NULL,
    `credit_hours` smallint NOT NULL,
    PRIMARY KEY(`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `chapters`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `chapter_number` smallint NOT NULL,
    `chapter_name` nvarchar(255) NOT NULL,
    `subject_id` varchar(12) NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_chapter_subject_id` (`subject_id`),
    CONSTRAINT `fk_chapter_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `questions`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `question_content`  NVARCHAR(500) UNIQUE NOT NULL,
    `image` varchar(255),
    `level` smallint NOT NULL,
    `question_type` tinyint(1),
    `chapter_id` bigint unsigned NOT NULL,
    `teacher_id` bigint unsigned NOT NULL,
    `status` tinyint(1) NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_question_chapter_id` (`chapter_id`),
    KEY `fk_question_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_question_chapter_id` FOREIGN KEY (`chapter_id`) REFERENCES `chapters` (`id`),
    CONSTRAINT `fk_question_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `answers`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `answer_content` text NOT NULL,
    `is_correct` tinyint(1) NOT NULL,
    `option_letter` char(1) NOT NULL,
    `question_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_answer_question_id` (`question_id`),
    CONSTRAINT `fk_answer_question_id` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_sets`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `name` nvarchar(255) NOT NULL,
    `subject_id` varchar(12) NOT NULL,
    `status` tinyint(1) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`created_by` bigint unsigned NOT NULL,
    `updated_by` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_exam_set_subject_id` (`subject_id`),
    KEY `fk_exam_set_created_by` (`created_by`),
    KEY `fk_exam_set_updated_by` (`updated_by`),
    CONSTRAINT `fk_test_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`),
	CONSTRAINT `fk_exam_set_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_exam_set_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `criteria`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `level` smallint NOT NULL,
    `quantity` smallint NOT NULL,
    `chapter_id` bigint unsigned NOT NULL,
    `exam_set_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_criteria_chapter_id` (`chapter_id`),
    KEY `fk_criteria_exam_set_id` (`exam_set_id`),	
    CONSTRAINT `fk_criteria_chapter_id` FOREIGN KEY (`chapter_id`) REFERENCES `chapters` (`id`),
    CONSTRAINT `fk_criteria_exam_set_id` FOREIGN KEY (`exam_set_id`) REFERENCES `exam_sets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exams`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `exam_code` smallint NOT NULL,
    `exam_set_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_exam_exam_set_id` (`exam_set_id`),
    CONSTRAINT `fk_exam_exam_set_id` FOREIGN KEY (`exam_set_id`) REFERENCES `exam_sets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_question`(
	`exam_id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `question_id` bigint unsigned NOT NULL,
    PRIMARY KEY(`exam_id`,`question_id`),
    KEY `fk_exam_id` (`exam_id`),
    KEY `fk_question_id` (`question_id`),
    CONSTRAINT `fk_exam_id` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`),
	CONSTRAINT `fk_question_id` FOREIGN KEY(`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_schedule`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `title` nvarchar(255) NOT NULL,
    `sumary` text NOT NULL,
    `published_at` datetime NOT NULL,
    `closed_at` datetime NOT NULL,
	`time_allowance` smallint NOT NULL,
	`status` tinyint(1) NOT NULL,
    `exam_set_id` bigint unsigned,
    `teacher_id` bigint unsigned,
    PRIMARY KEY(`id`),
    KEY `fk_exam_schedule_exam_set_id` (`exam_set_id`),
    KEY `fk_exam_schedule_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_exam_schedule_exam_set_id` FOREIGN KEY (`exam_set_id`) REFERENCES `exam_sets` (`id`),
    CONSTRAINT `fk_exam_schedule_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `student_exam_schedule`(
	`student_id` bigint unsigned NOT NULL,
    `exam_schedule_id` bigint unsigned NOT NULL,
    KEY `fk_student_id_exam_schedule` (`student_id`),
    KEY `fk_student_exam_schedule_id` (`exam_schedule_id`),
    CONSTRAINT `fk_student_id_exam_schedule` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_student_exam_schedule_id` FOREIGN KEY (`exam_schedule_id`) REFERENCES `exam_schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_results`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `grade` float NOT NULL,
    `number_of_right_anwser` int NOT NULL DEFAULT 0,
    `exam_schedule_id` bigint unsigned NOT NULL,
    `student_id` bigint unsigned NOT NULL,
    `exam_id` bigint unsigned,
    `is_completed` tinyint(1) NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_exam_exam_schedule_id` (`exam_schedule_id`),
    KEY `fk_exam_student_id` (`student_id`),
    KEY `fk_exam_exam_id` (`exam_id`),
	CONSTRAINT `fk_exam_exam_schedule_id` FOREIGN KEY (`exam_schedule_id`) REFERENCES `exam_schedule` (`id`),
    CONSTRAINT `fk_exam_student_id` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_exam_exam_id` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_result_details`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT, 
    `answer_id` bigint unsigned NOT NULL,
    `exam_result_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_exam_result_detail_answer_id` (`answer_id`),
    KEY `fk_exam_result_detail_exam_result_id` (`exam_result_id`),
    CONSTRAINT `fk_exam_result_detail_answer_id` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`),
    CONSTRAINT `fk_exam_result_detail_exam_result_id` FOREIGN KEY (`exam_result_id`) REFERENCES `exam_results` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

LOCK TABLES `roles` WRITE;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_TEACHER'),(3,'ROLE_STUDENT');
UNLOCK TABLES;

INSERT INTO `users` (`username`, `password`, `email`, `phone_number`, `first_name`, `last_name`, `address`, `gender`, `date_of_birth`, `status`)
VALUES
('user1', '$2a$10$f5ZCCwghHQ1FU8uReNpn4ul91NAPo7vTFntq5lPJTAYN9lA9MCaKu', 'user1@example.com', '123456789', 'John', 'Doe', '123 Main St', 1, '1990-01-15', 1),
('user2', '$2a$10$f5ZCCwghHQ1FU8uReNpn4ul91NAPo7vTFntq5lPJTAYN9lA9MCaKu', 'user2@example.com', '987654321', 'Jane', 'Smith', '456 Oak St', 0, '1985-05-20', 1),
('admin', '$2a$10$f5ZCCwghHQ1FU8uReNpn4ul91NAPo7vTFntq5lPJTAYN9lA9MCaKu', 'admin@example.com', '555555555', 'Admin', 'User', '789 Pine St', 1, '1980-08-10', 1),
('user4', '$2a$10$f5ZCCwghHQ1FU8uReNpn4ul91NAPo7vTFntq5lPJTAYN9lA9MCaKu', 'user4@example.com', '444444444', 'Eva', 'Johnson', '987 Elm St', 0, '1993-03-08', 1),
('user99', '$2a$10$f5ZCCwghHQ1FU8uReNpn4ul91NAPo7vTFntq5lPJTAYN9lA9MCaKu', 'user99@example.com', '222222222', 'Bob', 'Williams', '654 Birch St', 1, '1988-03-30', 1),
('user100', '$2a$10$f5ZCCwghHQ1FU8uReNpn4ul91NAPo7vTFntq5lPJTAYN9lA9MCaKu', 'user100@example.com', '333333333', 'Charlie', 'Brown', '987 Cedar St', 1, '1992-07-05', 1);

INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT `id`, FLOOR(RAND() * 3) + 1 FROM `users` ORDER BY `id` LIMIT 6;

-- Insert 50 subjects
INSERT INTO `subjects` (`subject_id`, `subject_name`, `credit_hours`)
VALUES
('MATH101', 'Mathematics', 3),
('PHYS101', 'Physics', 4),
('CHEM101', 'Chemistry', 3),
('BIO101', 'Biology', 3),
('ENG101', 'English', 3),
('HIST101', 'History', 3),
('COMP101', 'Computer Science', 4),
('GEOG101', 'Geography', 3),
('ECON101', 'Economics', 3),
('PSYCH101', 'Psychology', 3),
('SOC101', 'Sociology', 3),
('ART101', 'Art', 3),
('MUS101', 'Music', 3),
('PHIL101', 'Philosophy', 3),
('POLSCI101', 'Political Science', 3),
('CHEM102', 'Advanced Chemistry', 4),
('PHYS102', 'Advanced Physics', 4),
('MATH102', 'Advanced Mathematics', 4),
('MKTG101', 'Marketing', 3),
('ACCT101', 'Accounting', 3),
('FIN101', 'Finance', 3),
('MGMT101', 'Management', 3),
('MIS101', 'Management Information Systems', 4),
('STAT101', 'Statistics', 3),
('COMM101', 'Communication', 3),
('PHIL102', 'Ethics', 3),
('SOC102', 'Cultural Anthropology', 3),
('PSYCH102', 'Developmental Psychology', 3),
('ENGLIT101', 'English Literature', 3),
('PHYS103', 'Quantum Mechanics', 4),
('BIO102', 'Genetics', 3),
('ART102', 'Modern Art', 3),
('HIST102', 'World History', 3),
('GEOL101', 'Geology', 3),
('ASTRO101', 'Astronomy', 4),
('ECON102', 'Macroeconomics', 3),
('POLSCI102', 'International Relations', 3),
('MKTG102', 'Consumer Behavior', 3),
('ACCT102', 'Managerial Accounting', 3),
('FIN102', 'Investments', 3),
('MGMT102', 'Organizational Behavior', 3),
('MIS102', 'Database Management', 4),
('STAT102', 'Regression Analysis', 3),
('COMM102', 'Public Speaking', 3),
('PHIL103', 'Logic', 3),
('SOC103', 'Sociological Theory', 3),
('PSYCH103', 'Abnormal Psychology', 3),
('ENGLIT102', 'American Literature', 3),
('BIO103', 'Ecology', 3);

-- Insert chapters for Mathematics
INSERT INTO `chapters` (`chapter_number`, `chapter_name`, `subject_id`)
VALUES
(1, 'Introduction to Algebra', 'MATH101'),
(2, 'Geometry Fundamentals', 'MATH101'),
(3, 'Calculus I', 'MATH101'),
(4, 'Linear Algebra', 'MATH101'),
(5, 'Trigonometry', 'MATH101');

-- Insert chapters for Physics
INSERT INTO `chapters` (`chapter_number`, `chapter_name`, `subject_id`)
VALUES
(1, 'Classical Mechanics', 'PHYS101'),
(2, 'Thermodynamics', 'PHYS101'),
(3, 'Quantum Physics', 'PHYS101'),
(4, 'Electromagnetism', 'PHYS101'),
(5, 'Special Relativity', 'PHYS101');

-- Insert chapters for Chemistry
INSERT INTO `chapters` (`chapter_number`, `chapter_name`, `subject_id`)
VALUES
(1, 'Chemical Bonding', 'CHEM101'),
(2, 'Organic Chemistry', 'CHEM101'),
(3, 'Inorganic Chemistry', 'CHEM101'),
(4, 'Physical Chemistry', 'CHEM101'),
(5, 'Analytical Chemistry', 'CHEM101');

-- Insert chapters for various subjects to reach 150 rows
INSERT INTO `chapters` (`chapter_number`, `chapter_name`, `subject_id`)
VALUES
-- Mathematics chapters
(6, 'Differential Equations', 'MATH101'),
(7, 'Probability and Statistics', 'MATH101'),
(8, 'Abstract Algebra', 'MATH101'),
(9, 'Number Theory', 'MATH101'),
(10, 'Mathematical Logic', 'MATH101'),

-- Physics chapters
(6, 'Optics', 'PHYS101'),
(7, 'Nuclear Physics', 'PHYS101'),
(8, 'Astrophysics', 'PHYS101'),
(9, 'Fluid Mechanics', 'PHYS101'),
(10, 'Experimental Physics', 'PHYS101'),

-- Chemistry chapters
(6, 'Biochemistry', 'CHEM101'),
(7, 'Environmental Chemistry', 'CHEM101'),
(8, 'Chemical Kinetics', 'CHEM101'),
(9, 'Polymer Chemistry', 'CHEM101'),
(10, 'Computational Chemistry', 'CHEM101'),

-- Biology chapters
(1, 'Cell Structure and Function', 'BIO101'),
(2, 'Genetics and Heredity', 'BIO101'),
(3, 'Evolutionary Biology', 'BIO101'),
(4, 'Ecology', 'BIO101'),
(5, 'Human Anatomy and Physiology', 'BIO101'),

-- Computer Science chapters
(1, 'Introduction to Programming', 'COMP101'),
(2, 'Data Structures and Algorithms', 'COMP101'),
(3, 'Database Management', 'COMP101'),
(4, 'Software Engineering', 'COMP101'),
(5, 'Artificial Intelligence', 'COMP101');
-- ... (Thêm dữ liệu cho các chương còn lại)





-- Insert 100 questions and answers
INSERT INTO `questions` (`question_content`, `level`, `question_type`, `chapter_id`, `teacher_id`, `status`)
VALUES
('What is the capital of France?', 1, 1, 12, 4, 1),
('Solve the equation: 2x + 5 = 11', 2, 1, 1, 4, 1),
('Explain the process of photosynthesis.', 1, 2, 8, 6, 1),
-- ... (Thêm câu hỏi khác)

-- Insert answers for the first question
INSERT INTO `answers` (`answer_content`, `is_correct`, `option_letter`, `question_id`)
VALUES
('Paris', 1, 'A', 1),
('London', 0, 'B', 1),
('Berlin', 0, 'C', 1),
('Madrid', 0, 'D', 1);

-- Insert answers for the second question
INSERT INTO `answers` (`answer_content`, `is_correct`, `option_letter`, `question_id`)
VALUES
('3', 0, 'A', 2),
('2', 1, 'B', 2),
('4', 0, 'C', 2),
('5', 0, 'D', 2);

-- Insert answers for the third question
INSERT INTO `answers` (`answer_content`, `is_correct`, `option_letter`, `question_id`)
VALUES
('Photosynthesis is the process by which green plants make their own food using sunlight.', 1, 'A', 3),
('Photosynthesis is the process of converting light energy into thermal energy.', 0, 'B', 3),
('Photosynthesis is the process of breaking down glucose into energy.', 0, 'C', 3),
('Photosynthesis is the process of releasing oxygen from the atmosphere.', 0, 'D', 3);
-- ... (Thêm câu trả lời cho các câu hỏi khác)

-- Insert 100 more questions and answers
INSERT INTO `questions` (`question_content`, `level`, `question_type`, `chapter_id`, `teacher_id`, `status`)
VALUES
('What is the capital of Japan?', 1, 1, 13, 5, 1),
('Solve the quadratic equation: x^2 - 4x + 4 = 0', 2, 1, 1, 4, 1),
('Describe the structure of a eukaryotic cell.', 2, 2, 8, 6, 1),
-- ... (Thêm câu hỏi khác)

-- Insert answers for the fourth question
INSERT INTO `answers` (`answer_content`, `is_correct`, `option_letter`, `question_id`)
VALUES
('Tokyo', 1, 'A', 4),
('Beijing', 0, 'B', 4),
('Seoul', 0, 'C', 4),
('Bangkok', 0, 'D', 4);

-- Insert answers for the fifth question
INSERT INTO `answers` (`answer_content`, `is_correct`, `option_letter`, `question_id`)
VALUES
('2', 0, 'A', 5),
('1', 1, 'B', 5),
('3', 0, 'C', 5),
('4', 0, 'D', 5);

-- Insert answers for the sixth question
INSERT INTO `answers` (`answer_content`, `is_correct`, `option_letter`, `question_id`)
VALUES
('Eukaryotic cells have a nucleus and membrane-bound organelles.', 1, 'A', 6),
('Eukaryotic cells do not have a defined nucleus.', 0, 'B', 6),
('Eukaryotic cells have a cell wall made of peptidoglycan.', 0, 'C', 6),
('Eukaryotic cells are only found in bacteria.', 0, 'D', 6);
-- ... (Thêm câu trả lời cho các câu hỏi khác)

-- Insert another 100 questions and answers
INSERT INTO `questions` (`question_content`, `level`, `question_type`, `chapter_id`, `teacher_id`, `status`)
VALUES
('What is the main function of the liver?', 2, 1, 4, 6, 1),
('Solve the system of equations: 2x + y = 5, x - y = 1', 3, 1, 1, 4, 1),
('Explain the process of DNA replication.', 2, 2, 8, 6, 1)
-- ... (Thêm câu hỏi khác)