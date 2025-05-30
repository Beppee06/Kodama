package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @Spy
    private JwtUtil jwtUtil;

    private final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvclRlc3RpbmdKd3RUb2tlbkdlbmVyYXRpb25BbmRWYWxpZGF0aW9u";
    private final long TEST_EXPIRATION = 3600000; // 1 ora in millisecondi

    @BeforeEach
    public void setUp() {
        // Imposta le proprietà private tramite reflection
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", TEST_EXPIRATION);
        jwtUtil.init(); // Chiama il metodo @PostConstruct
    }

    @Test
    public void testGenerateToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(username, jwtUtil.getSubjectFromToken(token));
    }

    @Test
    public void testGenerateTokenWithClaims() {
        String username = "testuser";
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        claims.put("userId", 123);

        String token = jwtUtil.generateToken(username, claims);

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(username, jwtUtil.getSubjectFromToken(token));

        // Verifica i claims personalizzati
        Claims extractedClaims = jwtUtil.getAllClaimsFromToken(token);
        assertEquals("ADMIN", extractedClaims.get("role"));
        assertEquals(123, extractedClaims.get("userId"));
    }

    @Test
    public void testValidateExpiredToken() {
        // Imposta una scadenza di -1 ora (token già scaduto)
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", -3600000);

        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.validateToken(token));

        // Dovrebbe lanciare ExpiredJwtException quando tentiamo di leggere i claims
        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtil.getSubjectFromToken(token);
        });
    }

    @Test
    public void testValidateInvalidToken() {
        // Token inventato
        String invalidToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTYxNTQ0NjQwMCwiZXhwIjoxNjE1NDUwMDAwfQ.invalidSignature";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    public void testGetExpirationDateFromToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);

        // La data di scadenza dovrebbe essere circa TEST_EXPIRATION ms nel futuro
        long expectedTimestamp = System.currentTimeMillis() + TEST_EXPIRATION;
        long actualTimestamp = expirationDate.getTime();

        // Considerando un margine di 1 secondo per l'esecuzione del test
        assertTrue(Math.abs(expectedTimestamp - actualTimestamp) < 1000);
    }

    @Test
    public void testIsTokenExpiredWithPrivateMethod() throws Exception {
        // Test token scaduto
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", -3600000);
        String expiredToken = jwtUtil.generateToken("testuser");

        // Accedi al metodo privato isTokenExpired tramite reflection
        java.lang.reflect.Method isTokenExpiredMethod = JwtUtil.class.getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);

        boolean result = (boolean) isTokenExpiredMethod.invoke(jwtUtil, expiredToken);
        assertTrue(result);

        // Ripristina l'expiration per un token valido
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", TEST_EXPIRATION);
        String validToken = jwtUtil.generateToken("testuser");

        result = (boolean) isTokenExpiredMethod.invoke(jwtUtil, validToken);
        assertFalse(result);
    }
}