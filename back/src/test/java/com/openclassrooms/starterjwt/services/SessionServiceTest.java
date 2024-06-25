package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SessionService sessionService;

	@Test
	void testCreate() {
		Session session = new Session();
		when(sessionRepository.save(session)).thenReturn(session);

		Session createdSession = sessionService.create(session);
		assertEquals(session, createdSession);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	void testDelete() {
		Long id = 1L;
		sessionService.delete(id);
		verify(sessionRepository, times(1)).deleteById(id);
	}

	@Test
	void testFindAll() {
		Session session1 = new Session();
		Session session2 = new Session();
		List<Session> sessions = Arrays.asList(session1, session2);

		when(sessionRepository.findAll()).thenReturn(sessions);

		List<Session> foundSessions = sessionService.findAll();
		assertEquals(sessions, foundSessions);
		verify(sessionRepository, times(1)).findAll();
	}

	@Test
	void testGetById_SessionExists() {
		Session session = new Session();
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

		Session foundSession = sessionService.getById(1L);
		assertEquals(session, foundSession);
		verify(sessionRepository, times(1)).findById(1L);
	}

	@Test
	void testGetById_SessionDoesNotExist() {
		when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

		Session foundSession = sessionService.getById(1L);
		assertNull(foundSession);
		verify(sessionRepository, times(1)).findById(1L);
	}

	@Test
	void testUpdate() {
		Session session = new Session();
		session.setId(1L);
		when(sessionRepository.save(session)).thenReturn(session);

		Session updatedSession = sessionService.update(1L, session);
		assertEquals(session, updatedSession);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	void testParticipate_UserAndSessionExist() {
		Session session = new Session();
		session.setId(1L);
		User user = new User();
		user.setId(1L);
		session.setUsers(new ArrayList<>());

		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(sessionRepository.save(session)).thenReturn(session);

		sessionService.participate(1L, 1L);

		assertTrue(session.getUsers().contains(user));

		verify(sessionRepository, times(1)).findById(session.getId());
		verify(userRepository, times(1)).findById(user.getId());
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	void testParticipate_UserOrSessionDoesNotExist() {
		when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			sessionService.participate(1L, 1L);
		});

		verify(sessionRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).findById(1L);
	}
	
	 @Test
	    void testParticipate_UserAlreadyParticipates() {
	        User user = new User();
	        user.setId(1L);
	        Session session = new Session();
	        session.setUsers(Arrays.asList(user));
	        
	        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
	        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
	        
	        assertThrows(BadRequestException.class, () -> {
	            sessionService.participate(1L, 1L);
	        });
	        verify(sessionRepository, times(1)).findById(1L);
			verify(userRepository, times(1)).findById(1L);
	    }

	    @Test
	    void testNoLongerParticipate_UserAndSessionExist() {
	        User user = new User();
	        user.setId(1L);
	        Session session = new Session();
	        session.setUsers(Arrays.asList(user));
	        
	        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
	        when(sessionRepository.save(session)).thenReturn(session);
	        
	        sessionService.noLongerParticipate(1L, 1L);
	        
	        assertFalse(session.getUsers().contains(user));
	        verify(sessionRepository, times(1)).findById(1L);
	        verify(sessionRepository, times(1)).save(session);
	    }

	    @Test
	    void testNoLongerParticipate_SessionDoesNotExist() {
	        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
	        
	        assertThrows(NotFoundException.class, () -> {
	            sessionService.noLongerParticipate(1L, 1L);
	        });
	        verify(sessionRepository, times(1)).findById(1L);
	    }

	    @Test
	    void testNoLongerParticipate_UserDoesNotParticipate() {
	        User user = new User();
	        user.setId(1L);
	        Session session = new Session();
	        session.setUsers(new ArrayList<>());
	        
	        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
	        
	        assertThrows(BadRequestException.class, () -> {
	            sessionService.noLongerParticipate(1L, 1L);
	        });
	        verify(sessionRepository, times(1)).findById(1L);
	    }
}
