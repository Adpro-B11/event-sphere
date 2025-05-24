package id.ac.ui.cs.advprog.eventsphere.auth.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.Role;
import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class UserService {

    @Value("${auth.service.url:http://localhost:8080}")
    private String authServiceUrl;

    private final RestTemplate restTemplate;

    public UserService() {
        this.restTemplate = new RestTemplate();
    }

    public User getUserByToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    authServiceUrl + "/api/users/me",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getBody() != null) {
                return mapToUser(response.getBody());
            }
        } catch (Exception e) {
            // Log error or handle appropriately
        }
        return null;
    }

    public User getUserById(String userId) {
        try {
            String url = authServiceUrl + "/api/users/" + userId;

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Map.class
            );

            if (response.getBody() != null) {
                return mapToUser(response.getBody());
            }
        } catch (Exception e) {
        }
        return null;
    }

    private User mapToUser(Map<String, Object> userMap) {
        return User.builder()
                .id(UUID.fromString((String) userMap.get("id")))
                .username((String) userMap.get("username"))
                .email((String) userMap.get("email"))
                .phoneNumber((String) userMap.get("phoneNumber"))
                .role(Role.valueOf((String) userMap.get("role")))
                .balance(new BigDecimal(userMap.get("balance").toString()))
                .build();
    }

    @Service
    public static class JwtService {

        @Value("${jwt.secret-key}")
        private String secretKey;

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        public boolean isTokenValid(String token) {
            try {
                return !isTokenExpired(token);
            } catch (Exception e) {
                return false;
            }
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        private Claims extractAllClaims(String token) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private Key getSigningKey() {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        public String extractRole(String token) {
            Claims claims = extractAllClaims(token);
            return claims.get("role", String.class);
        }

        public String extractUserId(String token) {
            Claims claims = extractAllClaims(token);
            return claims.get("userId", String.class);
        }
    }
}
