package id.ac.ui.cs.advprog.eventsphere.auth.security;

import id.ac.ui.cs.advprog.eventsphere.auth.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private UserService.JwtService jwtService;

    private final String testSecretKey = "YVBRDFJDSkFGU0RGR0pIREZHR0hKS0xPUFJUVVlDSlNLSkRGV1FVUkpK"; // Ensure this is a valid Base64 encoded key of sufficient length for HS256
    private Key signingKey;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", testSecretKey);
        byte[] keyBytes = Decoders.BASE64.decode(testSecretKey);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateTestToken(String username, String role, String userId, long expirationTimeMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredTestToken(String username, String role, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void testExtractUsername() {
        String username = "testuser@example.com";
        String token = generateTestToken(username, "USER", UUID.randomUUID().toString(), 1000 * 60 * 60); // 1 hour expiration
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void testExtractRole() {
        String role = "ADMIN";
        String token = generateTestToken("admin@example.com", role, UUID.randomUUID().toString(), 1000 * 60 * 60);
        assertEquals(role, jwtService.extractRole(token));
    }

    @Test
    void testExtractUserId() {
        String userId = UUID.randomUUID().toString();
        String token = generateTestToken("user@example.com", "USER", userId, 1000 * 60 * 60);
        assertEquals(userId, jwtService.extractUserId(token));
    }

    @Test
    void testIsTokenValid_withValidToken() {
        String token = generateTestToken("validuser@example.com", "USER", UUID.randomUUID().toString(), 1000 * 60 * 60);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void testIsTokenValid_withExpiredToken() {
        String token = generateExpiredTestToken("expireduser@example.com", "USER", UUID.randomUUID().toString());
        assertFalse(jwtService.isTokenValid(token));
    }

    @Test
    void testIsTokenValid_withMalformedToken() {
        String malformedToken = "this.is.not.a.valid.jwt";
        assertFalse(jwtService.isTokenValid(malformedToken));
    }

    @Test
    void testIsTokenValid_withTamperedToken() {
        String token = generateTestToken("tamper@example.com", "USER", UUID.randomUUID().toString(), 1000 * 60 * 60);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx"; // Tamper the signature
        assertFalse(jwtService.isTokenValid(tamperedToken));
    }

    @Test
    void testExtractClaim() {
        String username = "claimuser@example.com";
        String userId = UUID.randomUUID().toString();
        String token = generateTestToken(username, "ORGANIZER", userId, 1000 * 60 * 60);

        String extractedUsername = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals(username, extractedUsername);

        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));

        String extractedUserId = jwtService.extractClaim(token, claims -> claims.get("userId", String.class));
        assertEquals(userId, extractedUserId);
    }
}
