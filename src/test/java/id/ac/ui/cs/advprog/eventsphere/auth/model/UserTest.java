package id.ac.ui.cs.advprog.eventsphere.auth.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testCreateUser() {
        // Arrange
        Long id = 1L;
        String username = "testuser";
        String email = "test@example.com";
        String password = "Password123!";
        String phone = "1234567890";

        // Act
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(phone, user.getPhone());
    }

    @Test
    public void testInvalidEmail() {
        // Arrange
        User user = new User();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("invalid-email");
        });

        assertTrue(exception.getMessage().contains("Invalid email format"));
    }

    @Test
    public void testWeakPassword() {
        // Arrange
        User user = new User();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword("weak");
        });

        assertTrue(exception.getMessage().contains("Password must be at least 8 characters"));
    }
}
