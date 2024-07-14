package com.openclassrooms.starterjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import com.openclassrooms.starterjwt.dto.SessionDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // garantit que chaque test s'exécute dans une transaction et que les modifications sont annulées après chaque test
public class SessionControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
	public void testFindById_Success() throws Exception {
		mockMvc.perform(get("/api/session/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Seance 1"));
	}

    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testFindById_NotFound() throws Exception {
        mockMvc.perform(get("/api/session/11")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testFindById_BadRequest() throws Exception {
        mockMvc.perform(get("/api/session/invalid")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
	@Test
	@WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
	public void findAll_shouldReturnListOfSessions() throws Exception {

		mockMvc.perform(get("/api/session")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name").value("Seance 1"))
				.andExpect(jsonPath("$[1].name").value("Seance 2"));
	}

    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testCreate_Success() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Seance de Test");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("description de test");
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/api/session"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
    }
    
    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testCreate_BadRequest_InvalidData() throws Exception {
        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"invalidField\": \"invalidValue\" }"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testUpdate_Success() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTeacher_id(1L);
        sessionDto.setName("Seance Mise a Jour");
        sessionDto.setDescription("description Mise a Jour");
        sessionDto.setDate(new Date());

        String jsonRequest = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Seance Mise a Jour"));
    }
    
    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testUpdate_BadRequest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTeacher_id(1L);
        sessionDto.setName("Seance Invalide");
        sessionDto.setDescription("Description Invalide");
        sessionDto.setDate(new Date());

        String jsonRequest = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testParticipate_Success() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testParticipate_BadRequest() throws Exception {
        mockMvc.perform(post("/api/session/abc/participate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/session/1/participate/abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

	@Test
	@WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
	public void testNoLongerParticipate_Success() throws Exception {
		mockMvc.perform(post("/api/session/1/participate/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/api/session/1/participate/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
	public void testNoLongerParticipate_BadRequest() throws Exception {
		mockMvc.perform(delete("/api/session/abc/participate/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		mockMvc.perform(delete("/api/session/1/participate/abc").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
