package org.example.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherDTOTest {

    @Test
    public void testEmptyConstructor() {
        TeacherDTO teacher = new TeacherDTO();
        assertNull(teacher.getTeacherId());
        assertNull(teacher.getEmail());
    }

    @Test
    public void testParameterizedConstructor() {
        Integer teacherId = 1;
        String email = "teacher@example.com";

        TeacherDTO teacher = new TeacherDTO(teacherId, email);

        assertEquals(teacherId, teacher.getTeacherId());
        assertEquals(email, teacher.getEmail());
    }

    @Test
    public void testGettersAndSetters() {
        TeacherDTO teacher = new TeacherDTO();

        Integer teacherId = 1;
        String email = "teacher@example.com";

        teacher.setTeacherId(teacherId);
        teacher.setEmail(email);

        assertEquals(teacherId, teacher.getTeacherId());
        assertEquals(email, teacher.getEmail());
    }
}