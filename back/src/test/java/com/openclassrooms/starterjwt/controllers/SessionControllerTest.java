package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    // Données de test initiales
    private final LocalDateTime rightNow = LocalDateTime.now();
    private final Date date = new Date();

    private final User initialUser = User.builder()
            .id(1L)
            .email("participant@mail.fr")
            .firstName("participant")
            .lastName("participant")
            .password("participant123")
            .admin(true)
            .createdAt(rightNow)
            .updatedAt(rightNow)
            .build();

    private final List<User> participationList = new ArrayList<User>() {{
        add(initialUser);
    }};

    private final Session initialSession = Session.builder()
            .id(2L)
            .name("test")
            .date(date)
            .description("description test")
            .createdAt(rightNow)
            .updatedAt(rightNow)
            .users(participationList)
            .build();

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetSessionByIdSuccess() throws Exception {
        Long id = initialSession.getId();
        when(sessionService.getById(id)).thenReturn(initialSession);

        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailGetSessionByIdWhenNotFound() throws Exception {
        Long id = initialSession.getId();
        when(sessionService.getById(id)).thenReturn(null);

        mockMvc.perform(get("/api/session/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetAllSessions() throws Exception {
        Session session = Session.builder()
                .name("test1")
                .date(date)
                .description("description test1")
                .createdAt(rightNow)
                .updatedAt(rightNow)
                .build();

        when(sessionService.findAll()).thenReturn(Stream.of(session, initialSession).collect(Collectors.toList()));

        mockMvc.perform(get("/api/session/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldCreateSessionSuccessfully() throws Exception {
    	SessionDto sessionDto = new SessionDto(
                4L,
                "Session 4",
                new Date(),
                1L,
                "Description 4",
                Arrays.asList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        String content = objectMapper.writeValueAsString(sessionDto);

        when(sessionService.create(initialSession)).thenReturn(initialSession);

        mockMvc.perform(post("/api/session")
        		.content(content)
        		.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldUpdateSessionSuccessfully() throws Exception {
        Long id = initialSession.getId();
        SessionDto updatedContent = new SessionDto(
                4L,
                "Session 4",
                new Date(),
                1L,
                "Description 4",
                Arrays.asList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        String content = objectMapper.writeValueAsString(updatedContent);

        when(sessionService.update(id, initialSession)).thenReturn(initialSession);

        mockMvc.perform(put("/api/session/" + id).accept(MediaType.APPLICATION_JSON).content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDeleteSessionSuccessfully() throws Exception {
        Long id = initialSession.getId();
        when(sessionService.getById(id)).thenReturn(initialSession);

        mockMvc.perform(delete("/api/session/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAddParticipationSuccessfully() throws Exception {
        Long id = initialSession.getId();
        User user = User.builder()
                .id(2L)
                .email("test@mail.fr")
                .firstName("firstName")
                .lastName("lastName")
                .password("test123")
                .admin(false)
                .createdAt(rightNow)
                .updatedAt(rightNow)
                .build();
        Long userId = user.getId();

        mockMvc.perform(post("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldCancelParticipationSuccessfully() throws Exception {
        Long id = initialSession.getId();
        Long userId = initialUser.getId();

        mockMvc.perform(delete("/api/session/" + id + "/participate/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailGetSessionByIdWhenIdIsInvalid() throws Exception {
        String invalidId = "abc";

        mockMvc.perform(get("/api/session/" + invalidId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFailUpdateSessionWhenIdIsInvalid() throws Exception {
        String invalidId = "abc";
        SessionDto sessionDto = new SessionDto(
                4L,
                "Session 4",
                new Date(),
                1L,
                "Description 4",
                Arrays.asList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        String content = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/" + invalidId).accept(MediaType.APPLICATION_JSON).content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
