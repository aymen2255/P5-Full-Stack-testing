package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;

//@WebMvcTest configure automatiquement l'infrastructure Spring MVC pour les tests unitaires.
//@WebMvcTest(controllers = TeacherController.class)
//@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private TeacherMapper teacherMapper;


	private Teacher mockTeacher;
	private TeacherDto teacherDto;

	@BeforeEach
	void setup(WebApplicationContext webApplicationContext) {
		mockTeacher = new Teacher();
		mockTeacher.setId(1L);
		mockTeacher.setFirstName("John");
		mockTeacher.setLastName("Doe");
		mockTeacher.setCreatedAt(LocalDateTime.now());
		mockTeacher.setUpdatedAt(LocalDateTime.now());

		teacherDto = new TeacherDto();
		teacherDto.setId(1L);
		teacherDto.setFirstName("John");
		teacherDto.setLastName("Doe");
	}

	@Test
	@WithMockUser(roles = "USER")
	public void shouldGetTeacherByIdSuccessTest() throws Exception {
		Long id = mockTeacher.getId();
		when(teacherService.findById(id)).thenReturn(mockTeacher);
		when(teacherMapper.toDto(mockTeacher)).thenReturn(teacherDto);

		mockMvc.perform(get("/api/teacher/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(mockTeacher.getId().intValue()))
				.andExpect(jsonPath("$.firstName").value(mockTeacher.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(mockTeacher.getLastName()));

	}

	@Test
	@WithMockUser
	void testFindById_NotFound() throws Exception {
		Long id = mockTeacher.getId();
		when(teacherService.findById(1L)).thenReturn(null);

		mockMvc.perform(get("/api/teacher/" + id)).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser
	void testFindById_InvalidId() throws Exception {
		mockMvc.perform(get("/api/teacher/invalidId")).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "USER")
    public void testFindAll() throws Exception {
        Teacher mockTeacher2 = new Teacher();
        List<Teacher> teachers = Arrays.asList(mockTeacher, mockTeacher2);

        TeacherDto teacherDto1 = new TeacherDto();
        TeacherDto teacherDto2 = new TeacherDto();
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

}
