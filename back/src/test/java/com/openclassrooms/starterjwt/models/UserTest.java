package com.openclassrooms.starterjwt.models;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserTest {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testValidUser() {
		User user = new User();
		user.setEmail("johndoe@example.com");
		user.setLastName("Doe");
		user.setFirstName("John");
		user.setPassword("securepassword123");
		user.setAdmin(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertTrue(violations.isEmpty());
	}

	@Test
	void testEmailValid() {
		User user = new User();
		user.setEmail("invalid-email");
		user.setLastName("Doe");
		user.setFirstName("John");
		user.setPassword("securepassword123");
		user.setAdmin(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertEquals(1, violations.size());
		ConstraintViolation<User> violation = violations.iterator().next();
		assertEquals("doit être une adresse électronique syntaxiquement correcte", violation.getMessage());
		assertEquals("email", violation.getPropertyPath().toString());
	}
	@Test
	void testEmailNull() {
		User user = new User();

		assertThrows(NullPointerException.class, () -> {
			user.setEmail(null);
		});
	}
	
	@Test
	void testLastNameNotNull() {
		User user = new User();

		assertThrows(NullPointerException.class, () -> {
			user.setLastName(null);
		});
	}
	
	@Test
	void testFirstNameNull() {
		User user = new User();

		assertThrows(NullPointerException.class, () -> {
			user.setFirstName(null);
		});
	}

	@Test
	void testPasswordNull() {
		User user = new User();

		assertThrows(NullPointerException.class, () -> {
			user.setPassword(null);
		});
	}

	@Test
	void testLastNameSize() {
		User user = new User();
		user.setEmail("johndoe@example.com");
		user.setLastName("DoeDoeDoeDoeDoeDoeDoeDoeDoeDoe");
		user.setFirstName("John");
		user.setPassword("securepassword123");
		user.setAdmin(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertEquals(1, violations.size());
		ConstraintViolation<User> violation = violations.iterator().next();
		assertEquals("la taille doit être comprise entre 0 et 20", violation.getMessage());
		assertEquals("lastName", violation.getPropertyPath().toString());
	}

	@Test
	void testFirstNameSize() {
		User user = new User();
		user.setEmail("johndoe@example.com");
		user.setLastName("Doe");
		user.setFirstName("JohnJohnJohnJohnJohnJohnJohnJohnJohnJohn");
		user.setPassword("securepassword123");
		user.setAdmin(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertEquals(1, violations.size());
		ConstraintViolation<User> violation = violations.iterator().next();
		assertEquals("la taille doit être comprise entre 0 et 20", violation.getMessage());
		assertEquals("firstName", violation.getPropertyPath().toString());
	}

	@Test
	void testPasswordSize() {
		User user = new User();
		user.setEmail("johndoe@example.com");
		user.setLastName("Doe");
		user.setFirstName("John");
		user.setPassword(
				"Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression. Le Lorem Ipsum ");
		user.setAdmin(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertEquals(1, violations.size());
		ConstraintViolation<User> violation = violations.iterator().next();
		assertEquals("la taille doit être comprise entre 0 et 120", violation.getMessage());
		assertEquals("password", violation.getPropertyPath().toString());
	}

}
