package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @AfterEach
    public void cleanup() {
        userRepo.deleteAll();
    }

    @Test
    @WithMockUser(username = "testUser@example.com", roles = "USER")
    public void givenUserExists_whenFindById_thenStatus200() throws Exception {
        User user = new User("testUser@example.com", "Doe", "John", "pass123", false);
        user = userRepo.save(user);

        mockMvc.perform(get("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
    }

    @Test
    @WithMockUser(username = "testUser@example.com", roles = "USER")
    public void givenUserDoesNotExist_whenFindById_thenStatus404() throws Exception {
        mockMvc.perform(get("/api/user/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser@example.com", roles = "USER")
    public void givenInvalidId_whenFindById_thenStatus400() throws Exception {
        mockMvc.perform(get("/api/user/invalid_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "anotherUser@example.com", roles = "USER")
    public void givenUserDeletesSelf_whenDelete_thenStatus200() throws Exception {
        User user = new User("anotherUser@example.com", "Smith", "Jane", "securePass", false);
        user = userRepo.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> deletedUser = userRepo.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    @WithMockUser(username = "testUser@example.com", roles = "USER")
    public void givenUserDoesNotExist_whenDelete_thenStatus404() throws Exception {
        mockMvc.perform(delete("/api/user/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser@example.com", roles = "USER")
    public void givenInvalidId_whenDelete_thenStatus400() throws Exception {
        mockMvc.perform(delete("/api/user/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "wrongUser@example.com", roles = "USER")
    public void givenUnauthorizedUserTriesToDelete_whenDelete_thenStatus401() throws Exception {
        User user = new User("anotherUser@example.com", "Miller", "Chris", "password456", false);
        user = userRepo.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthenticatedUser_whenFindById_thenStatus401() throws Exception {
        User user = new User("testUser@example.com", "Doe", "John", "pass123", false);
        user = userRepo.save(user);

        mockMvc.perform(get("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthenticatedUser_whenDelete_thenStatus401() throws Exception {
        User user = new User("testUser@example.com", "Doe", "Jane", "pass456", false);
        user = userRepo.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
