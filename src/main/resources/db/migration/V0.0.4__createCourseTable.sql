create table course(
    course_id INT AUTO_INCREMENT PRIMARY KEY, -- Use autoincrement
    teacher_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    max_subscribed INT NOT NULL,
    classroom INT NOT NULL,
    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,
    description varchar(500) NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);