package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTest {

    @Test
    void testEquals_SameObject() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).admin(false).build();
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    void testEquals_NullObject() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();
        assertFalse(userDetails.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();
        assertFalse(userDetails.equals("String"));
    }

    @Test
    void testEquals_SameClassDifferentId() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(2L).build();
        assertFalse(userDetails1.equals(userDetails2));
    }

    @Test
    void testEquals_SameClassSameId() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(1L).build();
        assertTrue(userDetails1.equals(userDetails2));
    }
}
