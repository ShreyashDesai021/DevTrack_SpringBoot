package com.devtrack.service;

import com.devtrack.dto.AuthResponse;
import com.devtrack.dto.LoginRequest;
import com.devtrack.dto.RegisterRequest;
import com.devtrack.exception.AuthenticationException;
import com.devtrack.exception.ValidationException;
import com.devtrack.model.User;
import com.devtrack.repository.UserRepository;
import com.devtrack.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * JUnit tests for AuthService
 * Tests registration, login, and BCrypt password hashing
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("Ann", "ann@example.com", "password123");
        loginRequest = new LoginRequest("ann@example.com", "password123");
        
        testUser = new User("Ann", "ann@example.com", "hashedPassword");
        testUser.setId(1L);
    }

    /**
     * Test successful registration with BCrypt password hashing
     */
    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), any(Long.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Ann", response.getName());
        assertEquals("ann@example.com", response.getEmail());
        
        // Verify password was encoded
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Test registration fails if email already exists
     */
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("ann@example.com")).thenReturn(true);

        assertThrows(ValidationException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Test successful login with BCrypt password verification
     */
    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), any(Long.class))).thenReturn("jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        
        // Verify password was verified using BCrypt
        verify(passwordEncoder, times(1)).matches("password123", "hashedPassword");
    }

    /**
     * Test login fails with wrong password
     */
    @Test
    void shouldFailLoginWithWrongPassword() {
        when(userRepository.findByEmail("ann@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        LoginRequest wrongRequest = new LoginRequest("ann@example.com", "wrongpassword");
        
        assertThrows(AuthenticationException.class, () -> {
            authService.login(wrongRequest);
        });
    }

    /**
     * Test login fails with non-existent user
     */
    @Test
    void shouldFailLoginWithNonExistentUser() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        LoginRequest wrongRequest = new LoginRequest("notfound@example.com", "password123");
        
        assertThrows(AuthenticationException.class, () -> {
            authService.login(wrongRequest);
        });
    }
}
