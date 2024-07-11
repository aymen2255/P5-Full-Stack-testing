package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // garantit que chaque test s'exécute dans une transaction et que les modifications sont annulées après chaque test
public class UserControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
    @Test
    @WithMockUser(roles = "USER")
    public void testFindById_UserFound() throws Exception {
        mockMvc.perform(get("/api/user/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.firstName").value("utilisateur_exemple"))
                .andExpect(jsonPath("$.lastName").value("exemple"))
                .andExpect(jsonPath("$.email").value("utilisateur@exemple.com"));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    public void testFindById_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/user/22")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    public void testFindById_InvalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/user/invalid-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "utilisateur@exemple.com", roles = "USER")
    public void testDeleteUser_UserFoundAndAuthorized() throws Exception {
    	
        mockMvc.perform(delete("/api/user/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteUser_UserNotFound() throws Exception {
        mockMvc.perform(delete("/api/user/22")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteUser_InvalidIdFormat() throws Exception {
        mockMvc.perform(delete("/api/user/invalid-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
}
