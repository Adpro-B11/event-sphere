package id.ac.ui.cs.advprog.eventsphere.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    // A Base64 encoded 512-bit secret key for HS512
    private final String testSecretKey = "Sy9g9Kfh0NtnZk0TNL/206IdXoP0tLPnMx1nkoHh4tV1yA2E0j7zN1YxKzC5oJ7hV4K9kNcPpP8W7nI6xH3LzA==";
    private final String alternativeSecretKey = "DifferentSecretKeyForTestingPurposesThatIsAlsoVeryLongAndBase64EncodedAndSecureEnoughForHS512TestingYeah!";


    @BeforeEach
    void setUp() {
        // Inject the test secret key into the JwtService instance
        ReflectionTestUtils.setField(jwtService, "jwtSecret", testSecretKey);
        // Ensure JWT_EXPIRATION_MS is set if not default, for this test we use the default 1 hour
        ReflectionTestUtils.setField(jwtService, "JWT_EXPIRATION_MS", 3600000L); // 1 hour
    }

    @Test
    void testGenerateToken_shouldReturnValidToken() {
        String userId = "testUser";
        String role = "USER";
        String token = jwtService.generateToken(userId, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUserIdFromJWT_shouldReturnCorrectUserId() {
        String userId = "user123";
        String role = "ADMIN";
        String token = jwtService.generateToken(userId, role);

        String extractedUserId = jwtService.getUserIdFromJWT(token);
        assertEquals(userId, extractedUserId);
    }

    @Test
    void testGetRoleFromJWT_shouldReturnCorrectRole() {
        String userId = "user123";
        String role = "ORGANIZER";
        String token = jwtService.generateToken(userId, role);

        String extractedRole = jwtService.getRoleFromJWT(token);
        assertEquals(role, extractedRole);
    }

    @Test
    void testValidateToken_withValidToken_shouldReturnTrue() {
        String token = jwtService.generateToken("validUser", "USER");
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testValidateToken_withInvalidSignature_shouldReturnFalse() {
        // Generate token with the original service (original key)
        String token = jwtService.generateToken("userWithOriginalKey", "USER");

        // Create another service instance or reconfigure the current one with a different key
        JwtService serviceWithDifferentKey = new JwtService();
        ReflectionTestUtils.setField(serviceWithDifferentKey, "jwtSecret", alternativeSecretKey); // Different key
        ReflectionTestUtils.setField(serviceWithDifferentKey, "JWT_EXPIRATION_MS", 3600000L);


        assertFalse(serviceWithDifferentKey.validateToken(token));
    }

    @Test
    void testValidateToken_withMalformedToken_shouldReturnFalse() {
        String malformedToken = "this.is.not.a.valid.jwt.token";
        assertFalse(jwtService.validateToken(malformedToken));
    }

    @Test
    void testValidateToken_withUnsupportedToken_shouldReturnFalse() {
        // Example: A token that has a structure but isn't what's expected (e.g., different claims structure that leads to parsing error)
        // For this specific test, JJWT might classify it as MalformedJwtException or another.
        // A token signed with a completely different algorithm not supported by the key might be one example,
        // but more often it's structural. Here we use an empty claims token.
        String unsupportedToken = Jwts.builder().compact(); // Empty, effectively unsupported
        assertFalse(jwtService.validateToken(unsupportedToken));
    }

    @Test
    void testValidateToken_withEmptyClaims_shouldReturnFalse() {
        // An empty subject or critical missing claims might be caught by IllegalArgumentException by JJWT during parsing
        // Or, if it parses but lacks what your app needs, the higher-level logic would fail.
        // Here, we test a structurally valid JWT but with no standard claims (which might be considered malformed by some definitions)
        String tokenWithEmptyClaims = Jwts.builder()
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        // Depending on JJWT version, this might be caught by MalformedJwtException or IllegalArgumentException
        // The current validateToken catches IllegalArgumentException.
        assertFalse(jwtService.validateToken(tokenWithEmptyClaims));
    }


    @Test
    void testGetUserIdFromJWT_withInvalidToken_shouldThrowException() {
        String invalidToken = "invalid.token.string";
        // Expecting MalformedJwtException or SignatureException based on how invalid it is
        assertThrows(JwtException.class, () -> jwtService.getUserIdFromJWT(invalidToken));
    }

    @Test
    void testGetRoleFromJWT_withInvalidToken_shouldThrowException() {
        String invalidToken = "invalid.token.string";
        assertThrows(JwtException.class, () -> jwtService.getRoleFromJWT(invalidToken));
    }
}