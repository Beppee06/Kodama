CREATE TABLE student (
     student_id INT AUTO_INCREMENT PRIMARY KEY, -- Use autoincrement
     email VARCHAR(255) NOT NULL UNIQUE, -- Added UNIQUE constraint for email
     password VARCHAR(255) NOT NULL
);