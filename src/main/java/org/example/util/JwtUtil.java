package org.example.util; // O il tuo package appropriato

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // Importa questa specifica eccezione
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret; // Chiave segreta letta dalle properties (Base64 encoded)

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs; // Durata del token letta dalle properties

    private SecretKey key;

    // Inizializza la chiave segreta una volta dopo l'iniezione delle properties
    @jakarta.annotation.PostConstruct // Usa javax.annotation.PostConstruct per Spring Boot < 3 / Java < 11
    void init() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes); // Crea la SecretKey per HMAC-SHA
    }

    /**
     * Genera un token JWT per un dato subject (es. username).
     *
     * @param subject Il subject del token (solitamente username o user ID).
     * @return Il token JWT come stringa.
     */
    public String generateToken(String subject) {
        return generateToken(subject, Map.of()); // Chiama l'overload con claims vuoti
    }

    /**
     * Genera un token JWT per un dato subject e claims aggiuntivi.
     *
     * @param subject Il subject del token (solitamente username o user ID).
     * @param claims Una mappa di claims aggiuntivi da includere nel payload.
     * @return Il token JWT come stringa.
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claims(claims) // Aggiunge prima i claims personalizzati
                .subject(subject) // Imposta il subject standard
                .issuedAt(now) // Data di emissione
                .expiration(expiryDate) // Data di scadenza
                .signWith(key, SignatureAlgorithm.HS512) // Firma con la chiave e l'algoritmo (HS512 è robusto)
                // .signWith(key) // Metodo moderno alternativo che deduce l'algoritmo dalla chiave
                .compact(); // Costruisce la stringa del token
    }

    /**
     * Estrae il subject (es. username) da un token JWT valido.
     *
     * @param token Il token JWT.
     * @return Il subject del token.
     * @throws JwtException Se il token non è valido o scaduto.
     */
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Estrae la data di scadenza da un token JWT valido.
     *
     * @param token Il token JWT.
     * @return La data di scadenza.
     * @throws JwtException Se il token non è valido o scaduto.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Estrae un claim specifico dal token usando una funzione resolver.
     *
     * @param token Il token JWT.
     * @param claimsResolver La funzione per estrarre il claim desiderato.
     * @param <T> Il tipo del claim da estrarre.
     * @return Il valore del claim.
     * @throws JwtException Se il token non è valido o scaduto.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims (payload) da un token JWT, verificandone la firma.
     *
     * @param token Il token JWT.
     * @return L'oggetto Claims contenente il payload del token.
     * @throws MalformedJwtException      Se il token non è strutturato correttamente.
     * @throws ExpiredJwtException        Se il token è scaduto.
     * @throws SignatureException         Se la firma non è valida.
     * @throws UnsupportedJwtException    Se il token usa un algoritmo non supportato.
     * @throws IllegalArgumentException   Se il token è null, vuoto o invalido.
     */
    Claims getAllClaimsFromToken(String token) {
        // Il parser verifica automaticamente firma e scadenza
        return Jwts.parser()
                .verifyWith(key) // Specifica la chiave per la verifica
                .build()
                .parseSignedClaims(token) // Parsa e verifica
                .getPayload(); // Ottiene il payload (Claims)
    }

    /**
     * Verifica se un token JWT è valido (non scaduto, firma corretta, ecc.).
     *
     * @param token Il token JWT da validare.
     * @return true se il token è valido, false altrimenti.
     */
    public boolean validateToken(String token) {
        try {
            // Tentativo di parsare il token. Se non lancia eccezioni, è valido.
            getAllClaimsFromToken(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT non valido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT scaduto: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT non supportato: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Claims JWT vuoti o token nullo: {}", e.getMessage());
        } catch (SignatureException e) { // Cattura specificamente l'eccezione di firma
            logger.error("Firma JWT non valida: {}", e.getMessage());
        } catch (JwtException e) { // Cattura generica per altre eccezioni JWT non previste sopra
            logger.error("Errore durante la validazione del token JWT: {}", e.getMessage());
        }
        return false; // Se è stata catturata un'eccezione, il token non è valido
    }

    /**
     * Verifica se il token è scaduto senza lanciare eccezioni se la firma è valida.
     * Nota: validateToken() già controlla la scadenza. Questo è utile se vuoi
     * controllare SOLO la scadenza su un token potenzialmente valido.
     *
     * @param token Il token JWT.
     * @return true se il token è scaduto, false altrimenti. Può lanciare altre JwtException se il token è malformato o la firma non è valida.
     */
    private boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true; // Se lancia ExpiredJwtException, è scaduto
        }
        // Altre eccezioni (Malformed, Signature, etc.) verranno propagate
    }
}