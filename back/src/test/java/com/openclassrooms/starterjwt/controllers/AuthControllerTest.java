package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();

	// Encodage du mot de passe pour les tests
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Création d'un utilisateur initial pour les tests
	private final LocalDateTime now = LocalDateTime.now();
	private final User initialUser = User.builder().id(1L).email("test@mail.fr").firstName("test").lastName("test")
			.password(passwordEncoder().encode("test123")).admin(true).createdAt(now).updatedAt(now).build();

	@Test
	void shouldLoginUserSuccessfully() throws Exception {
		// Préparer la requête de connexion
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(initialUser.getEmail());
		loginRequest.setPassword("test123");

		// Configurer le comportement du repository
		when(userRepository.findByEmail(initialUser.getEmail())).thenReturn(Optional.of(initialUser));

		// Créer la requête POST pour /login
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/login")
				.content(objectMapper.writeValueAsString(loginRequest)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		// Exécuter et vérifier la réponse
		mockMvc.perform(mockRequest).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void shouldFailLoginUserWhenEmailNotFound() throws Exception {
		// Préparer la requête de connexion avec un email incorrect
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("wrong@mail.fr");
		loginRequest.setPassword("test123");

		// Configurer le comportement du repository
		when(userRepository.findByEmail("wrong@mail.fr")).thenReturn(Optional.empty());

		// Créer la requête POST pour /login
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/login")
				.content(objectMapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		// Exécuter et vérifier la réponse
		mockMvc.perform(mockRequest).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	void shouldFailLoginUserWhenWrongPassword() throws Exception {
		// Préparer la requête de connexion avec un mauvais mot de passe
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(initialUser.getEmail());
		loginRequest.setPassword("test1234");

		// Configurer le comportement du repository
		when(userRepository.findByEmail(initialUser.getEmail())).thenReturn(Optional.of(initialUser));

		// Créer la requête POST pour /login
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/login")
				.content(objectMapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		// Exécuter et vérifier la réponse
		mockMvc.perform(mockRequest).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	void shouldRegisterUserSuccessfully() throws Exception {
		// Préparer la requête d'inscription
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("newaccount@mail.fr");
		signupRequest.setPassword("test123");
		signupRequest.setFirstName("test");
		signupRequest.setLastName("test");

		// Configurer le comportement du repository
		when(userRepository.existsByEmail("newaccount@mail.fr")).thenReturn(false);

		// Créer la requête POST pour /register
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/register")
				.content(objectMapper.writeValueAsString(signupRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		// Exécuter et vérifier la réponse
		mockMvc.perform(mockRequest).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.message").value("User registered successfully!"));
	}

	@Test
	void shouldNotRegisterWhenUserAlreadyExists() throws Exception {
		// Préparer la requête d'inscription avec un email déjà utilisé
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("newaccount@mail.fr");
		signupRequest.setPassword("test123");
		signupRequest.setFirstName("test");
		signupRequest.setLastName("test");

		// Configurer le comportement du repository
		when(userRepository.existsByEmail("newaccount@mail.fr")).thenReturn(true);

		// Créer la requête POST pour /register
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/register")
				.content(objectMapper.writeValueAsString(signupRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		// Exécuter et vérifier la réponse
		mockMvc.perform(mockRequest).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
	}
}
