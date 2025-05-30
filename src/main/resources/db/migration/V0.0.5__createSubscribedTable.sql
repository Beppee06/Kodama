create table subscribed(
   subscribed_id INT AUTO_INCREMENT PRIMARY KEY, -- Use autoincrement
   course_id INT NOT NULL,
   student_id INT NOT NULL,
   FOREIGN KEY (course_id) REFERENCES course(course_id),
   FOREIGN KEY (student_id) REFERENCES student(student_id)
)