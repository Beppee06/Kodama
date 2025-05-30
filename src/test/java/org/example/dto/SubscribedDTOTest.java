package org.example.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubscribedDTOTest {

    @Test
    void testEmptyConstructor() {
        // Arrange & Act
        SubscribedDTO subscribedDTO = new SubscribedDTO();

        // Assert
        assertNull(subscribedDTO.getSubscribedId());
        assertNull(subscribedDTO.getStudentId());
        assertNull(subscribedDTO.getCourseId());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange & Act
        Integer subscribedId = 1;
        Integer studentId = 2;
        Integer courseId = 3;
        SubscribedDTO subscribedDTO = new SubscribedDTO(subscribedId, studentId, courseId);

        // Assert
        assertEquals(subscribedId, subscribedDTO.getSubscribedId());
        assertEquals(studentId, subscribedDTO.getStudentId());
        assertEquals(courseId, subscribedDTO.getCourseId());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        SubscribedDTO subscribedDTO = new SubscribedDTO();

        // Act
        Integer subscribedId = 1;
        Integer studentId = 2;
        Integer courseId = 3;

        subscribedDTO.setSubscribedId(subscribedId);
        subscribedDTO.setStudentId(studentId);
        subscribedDTO.setCourseId(courseId);

        // Assert
        assertEquals(subscribedId, subscribedDTO.getSubscribedId());
        assertEquals(studentId, subscribedDTO.getStudentId());
        assertEquals(courseId, subscribedDTO.getCourseId());
    }

    @Test
    void testNullValues() {
        // Arrange
        SubscribedDTO subscribedDTO = new SubscribedDTO(1, 2, 3);

        // Act
        subscribedDTO.setSubscribedId(null);
        subscribedDTO.setStudentId(null);
        subscribedDTO.setCourseId(null);

        // Assert
        assertNull(subscribedDTO.getSubscribedId());
        assertNull(subscribedDTO.getStudentId());
        assertNull(subscribedDTO.getCourseId());
    }

    @Test
    void testEquality() {
        // Arrange
        SubscribedDTO dto1 = new SubscribedDTO(1, 2, 3);
        SubscribedDTO dto2 = new SubscribedDTO(1, 2, 3);
        SubscribedDTO dto3 = new SubscribedDTO(2, 3, 4);

        // Act & Assert
        // Note: DTO doesn't override equals/hashCode, so this tests reference equality
        assertEquals(dto1, dto1); // Same instance
        assertNotEquals(dto1, dto2); // Different instances, same values
        assertNotEquals(dto1, dto3); // Different instances, different values
    }
}