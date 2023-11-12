drop database lika;
create database lika;
use lika;

CREATE TABLE `users`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `username` nvarchar(30) NOT NULL UNIQUE,
    `password` nvarchar(255) NOT NULL,
    `email` nvarchar(50) DEFAULT '' UNIQUE,
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
    `name` nvarchar(255) NOT NULL UNIQUE,
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
    `subject_name` nvarchar(50) NOT NULL UNIQUE,
    `credit_hours` smallint NOT NULL,
    PRIMARY KEY(`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `chapters`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `chapter_number` smallint NOT NULL,
    `chapter_name` nvarchar(255) NOT NULL UNIQUE,
    `subject_id` varchar(12) NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_chapter_subject_id` (`subject_id`),
    CONSTRAINT `fk_chapter_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `questions`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `question_content` text NOT NULL UNIQUE ,
    `image` varchar(255),
    `level` smallint NOT NULL,
    `question_type` varchar(20),
    `chapter_id` bigint unsigned NOT NULL,
    `teacher_id` bigint unsigned NOT NULL,
    `is_used` tinyint(1) NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_question_chapter_id` (`chapter_id`),
    KEY `fk_question_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_question_chapter_id` FOREIGN KEY (`chapter_id`) REFERENCES `chapters` (`id`),
    CONSTRAINT `fk_question_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;


CREATE TABLE `answers`(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `aswers_content` text NOT NULL,
    `is_answer` tinyint(1) NOT NULL,
    `order` char(3) NOT NULL,
    `question_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_answer_question_id` (`question_id`),
    CONSTRAINT `fk_answer_question_id` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;


CREATE TABLE `credit_classes`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `group` smallint unsigned NOT NULL,
    `academic_year` char(9) NOT NULL,
    `semester` smallint NOT NULL,
    `is_cancel` tinyint(1) NOT NULL,
    `subject_id` varchar(12) NOT NULL,
    `teacher_id` bigint unsigned NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_credit_class_subject_id` (`subject_id`),
    KEY `fk_credit_class_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_credit_class_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`),
    CONSTRAINT `fk_credit_class_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `registers`(
	`student_id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `credit_class_id` bigint unsigned NOT NULL,
    `attendance_grade` float default 0,
    `midterm_grade` float default 0,
    `final_grade` float default 0,
    PRIMARY KEY(`student_id`,`credit_class_id`),
    KEY `fk_register_student_id` (`student_id`),
    KEY `fk_register_creadit_class_id` (`credit_class_id`),
    CONSTRAINT `fk_register_student_id` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_register_credit_class_id` FOREIGN KEY (`credit_class_id`) REFERENCES `credit_classes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_schedule`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `exam_date` date NOT NULL,
    `name` nvarchar(255) NOT NULL,
    `attendance_period` smallint NOT NULL,
    `is_cancel` tinyint(1) NOT NULL,
    `is_completed` tinyint(1) NOT NULL,
    `time_limit` smallint NOT NULL,
    `exam_type` varchar(50) NOT NULL,
    `teacher_id` bigint unsigned NOT NULL,
    PRIMARY KEY(`id`),
    KEY `fk_exam_schedule_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_exam_schedule_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exams`(
	`id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `exam_name` nvarchar(255) NOT NULL,
    `is_used` tinyint(1) NOT NULL,
    `exam_schedule_id` bigint unsigned NOT NULL,
    `subject_id` varchar(12) NOT NULL,
    `teacher_id` bigint unsigned NOT NULL,
    `is_using` tinyint(1) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `fk_exam_exam_schedule_id` (`exam_schedule_id`),
    KEY `fk_exam_subject_id` (`subject_id`),
    KEY `fk_exam_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_exam_exam_schedule_id` FOREIGN KEY (`exam_schedule_id`) REFERENCES `exam_schedule` (`id`),
    CONSTRAINT `fk_exam_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`),
	CONSTRAINT `fk_exam_teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE `exam_question`(
	`exam_id` bigint unsigned NOT NULL,
    `question_id` bigint unsigned NOT NULL,
    PRIMARY KEY(`exam_id`,`question_id`),
    KEY `fk_exam_id` (`exam_id`),
    KEY `fk_question_id` (`question_id`),
    CONSTRAINT `fk_exam_id` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`),
	CONSTRAINT `fk_question_id` FOREIGN KEY(`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `test`(
	`attempt` smallint NOT NULL,
    `grade` float NOT NULL,
    `exam_schedule_id` bigint unsigned NOT NULL,
    `credit_class_id` bigint unsigned NOT NULL,
    `student_id` bigint unsigned NOT NULL,
    `exam_id` bigint unsigned NOT NULL,
    `is_completed` tinyint(1) NOT NULL,
    PRIMARY KEY(`exam_schedule_id`,`credit_class_id`,`student_id`),
    KEY `fk_test_exam_schedule_id` (`exam_schedule_id`),
    KEY `fk_test_credit_class_id` (`credit_class_id`),
    KEY `fk_test_student_id` (`student_id`),
    KEY `fk_test_exam_id` (`exam_id`),
	CONSTRAINT `fk_test_exam_schedule_id` FOREIGN KEY (`exam_schedule_id`) REFERENCES `exam_schedule` (`id`),
    CONSTRAINT `fk_test_credit_class_id` FOREIGN KEY (`credit_class_id`) REFERENCES `credit_classes` (`id`),
    CONSTRAINT `fk_test_student_id` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_test_exam_id` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

LOCK TABLES `roles` WRITE;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_TEACHER'),(3,'ROLE_STUDENT');
UNLOCK TABLES;