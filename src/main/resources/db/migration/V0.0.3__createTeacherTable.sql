create table teacher(
    teacher_id INT AUTO_INCREMENT PRIMARY KEY, -- Use autoincrement
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);