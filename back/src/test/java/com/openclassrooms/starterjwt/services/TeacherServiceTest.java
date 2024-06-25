package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@InjectMocks
	private TeacherService teacherService;

	@Test
	public void testFindAll_TeachersExist() {
		Teacher teacher1 = new Teacher();
		teacher1.setId(1L);

		Teacher teacher2 = new Teacher();
		teacher2.setId(2L);

		List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);

		when(teacherRepository.findAll()).thenReturn(expectedTeachers);

		List<Teacher> actualTeachers = teacherService.findAll();

		assertEquals(actualTeachers, expectedTeachers);

		verify(teacherRepository, times(1)).findAll();
	}

	@Test
	void testFindAll_NoTeachersExist() {
		when(teacherRepository.findAll()).thenReturn(Collections.emptyList());

		List<Teacher> foundTeachers = teacherService.findAll();
		assertEquals(Collections.emptyList(), foundTeachers);
		verify(teacherRepository, times(1)).findAll();
	}

	@Test
	void testFindById_TeacherExists() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacher.setFirstName("John Doe");

		when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

		Teacher foundTeacher = teacherService.findById(1L);
		assertEquals(teacher, foundTeacher);
		verify(teacherRepository, times(1)).findById(1L);
	}

	@Test
	void testFindById_TeacherDoesNotExist() {
		when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

		Teacher foundTeacher = teacherService.findById(1L);
		assertNull(foundTeacher);
		verify(teacherRepository, times(1)).findById(1L);
	}
}
