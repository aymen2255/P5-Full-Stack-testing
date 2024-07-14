CREATE TABLE IF NOT EXISTS `TEACHERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `SESSIONS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50),
  `description` VARCHAR(2000),
  `date` TIMESTAMP,
  `teacher_id` int,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `admin` BOOLEAN NOT NULL DEFAULT false,
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `PARTICIPATE` (
  `user_id` INT,
  `session_id` INT
);

ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('John', 'Doe'),
       ('Jane', 'Doet');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES
('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$B4edsb96F10PjxK5Lc4J4uOEygft7Yeg3b9zYlNnv50cvc5aSJSou'),
('utilisateur_exemple', 'exemple', false, 'utilisateur@exemple.com', '$2a$10$B4edsb96F10PjxK5Lc4J4uOEygft7Yeg3b9zYlNnv50cvc5aSJSou');

INSERT INTO SESSIONS
    (name, date, description, teacher_id, created_at, updated_at)
VALUES
    ('Seance 1', '2022-10-01', 'Description pour la Seance 1', 1, NOW(), NOW()),
    ('Seance 2', '2022-10-08', 'Description pour la Seance 2', 2, NOW(), NOW());
