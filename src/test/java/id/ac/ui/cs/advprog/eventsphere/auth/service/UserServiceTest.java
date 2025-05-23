package id.ac.ui.cs.advprog.eventsphere.auth.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testFindUsernameById() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        User mockUser = new User(userId, "john_doe", "john@example.com", "Password123", "08123456789");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        String username = userService.getUsernameById(userId);

        // Assert
        assertEquals("john_doe", username);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindUsernameById_NotFound() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUsernameById(userId);
        });
    }
}
