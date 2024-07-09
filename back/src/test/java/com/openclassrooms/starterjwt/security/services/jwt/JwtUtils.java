package com.openclassrooms.starterjwt.security.services.jwt;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private String jwtSecret = "testSecret";
    private int jwtExpirationMs = 3600000; // 1 hour in milliseconds

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);

        Field jwtSecretField = JwtUtils.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(jwtUtils, jwtSecret);

        Field jwtExpirationMsField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        jwtExpirationMsField.setAccessible(true);
        jwtExpirationMsField.set(jwtUtils, jwtExpirationMs);
    }

    @Test
    void testGenerateJwtToken() {

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        assertEquals("testUser", username);
    }

    @Test
    void testGetUserNameFromJwtToken() {

        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("testUser", username);
    }

    @Test
    void testValidateJwtToken_ValidToken() {

        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateJwtToken_InvalidToken() {    
        String invalidToken = "invalidToken";
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_ExpiredToken() {

        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date((new Date()).getTime() - jwtExpirationMs * 2)) // token expired 2 hours ago
                .setExpiration(new Date((new Date()).getTime() - jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        assertFalse(isValid);
    }
}
