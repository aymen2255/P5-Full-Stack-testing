package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.validation.ConstraintViolation;

@ExtendWith(MockitoExtension.class)
class TeacherTest {

	  private Validator validator;

	    @BeforeEach
	    public void setUp() {
	        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	        validator = factory.getValidator();
	    }

	    @Test
	    void testValidTeacher() {
	        Teacher teacher = new Teacher();
	        teacher.setLastName("Doe");
	        teacher.setFirstName("John");
	        teacher.setCreatedAt(LocalDateTime.now());
	        teacher.setUpdatedAt(LocalDateTime.now());

	        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
	        assertTrue(violations.isEmpty());
	    }

	    @Test
	    void testLastNameNotBlank() {
	        Teacher teacher = new Teacher();
	        teacher.setFirstName("John");
	        teacher.setCreatedAt(LocalDateTime.now());
	        teacher.setUpdatedAt(LocalDateTime.now());

	        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
	        assertEquals(1, violations.size());
	        ConstraintViolation<Teacher> violation = violations.iterator().next();
	        assertEquals("ne doit pas être vide", violation.getMessage());
	        assertEquals("lastName", violation.getPropertyPath().toString());
	    }

	    @Test
	    void testFirstNameNotBlank() {
	        Teacher teacher = new Teacher();
	        teacher.setLastName("Doe");
	        teacher.setCreatedAt(LocalDateTime.now());
	        teacher.setUpdatedAt(LocalDateTime.now());

	        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
	        assertEquals(1, violations.size());
	        ConstraintViolation<Teacher> violation = violations.iterator().next();
	        assertEquals("ne doit pas être vide", violation.getMessage());
	        assertEquals("firstName", violation.getPropertyPath().toString());
	    }

	    @Test
	    void testLastNameSize() {
	        Teacher teacher = new Teacher();
	        teacher.setLastName("DoeDoeDoeDoeDoeDoeDoeDoeDoeDoe");
	        teacher.setFirstName("John");
	        teacher.setCreatedAt(LocalDateTime.now());
	        teacher.setUpdatedAt(LocalDateTime.now());

	        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
	        assertEquals(1, violations.size());
	        ConstraintViolation<Teacher> violation = violations.iterator().next();
	        assertEquals("la taille doit être comprise entre 0 et 20", violation.getMessage());
	        assertEquals("lastName", violation.getPropertyPath().toString());
	    }

	    @Test
	    void testFirstNameSize() {
	        Teacher teacher = new Teacher();
	        teacher.setLastName("Doe");
	        teacher.setFirstName("JohnJohnJohnJohnJohnJohnJohnJohnJohnJohn");
	        teacher.setCreatedAt(LocalDateTime.now());
	        teacher.setUpdatedAt(LocalDateTime.now());

	        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
	        assertEquals(1, violations.size());
	        ConstraintViolation<Teacher> violation = violations.iterator().next();
	        assertEquals("la taille doit être comprise entre 0 et 20", violation.getMessage());
	        assertEquals("firstName", violation.getPropertyPath().toString());
	    }

}
