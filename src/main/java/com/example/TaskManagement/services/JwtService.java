package com.example.TaskManagement.services;

import com.example.TaskManagement.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;


/**
 * Service class for generating, validating, and extracting information from JSON Web Tokens (JWTs).
 * This class provides methods to handle token operations using a secret key for signing.
 */
@Service
public class JwtService {

    /**
     * The secret key used for signing the tokens. Loaded from application properties.
     */
    @Value("${token.signing.key}")
    private String secret;

    /**
     * The expiration time (in milliseconds) for the tokens. Loaded from application properties.
     */
    @Value("${token.signing.expiration-ms}")
    private int lifelimit;

    /**
     * Extracts the username (email) from the provided token.
     *
     * @param token The JWT token.
     * @return The username (email) extracted from the token's claims.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a JWT token for the provided user details. Includes custom claims for {@link User} entities.
     *
     * @param userDetails The user details for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Validates the provided token against the user details.
     * Checks if the email matches and if the token is not expired.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String emailFromToken = extractUserName(token);
        String emailFromDb = userDetails.getUsername();
        return (emailFromToken.equals(emailFromDb) && !isTokenExpired(token));
    }

    /**
     * Extracts a specific claim from the token using the provided resolver function.
     *
     * @param <T>            The type of the claim to extract.
     * @param token          The JWT token.
     * @param claimsResolver A function to resolve the desired claim from the token's claims.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token with the specified extra claims and user details.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user details to associate with the token.
     * @return The generated JWT token.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String email;
        if (userDetails instanceof User) {
            email = ((User) userDetails).getEmail();
        } else {
            email = userDetails.getUsername();
        }

        return builder()
                .setClaims(extraClaims)
                .setSubject(email)  // Устанавливаем email в качестве subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifelimit))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if the provided token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token The JWT token.
     * @return The claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates the signing key from the configured secret.
     *
     * @return The {@link SecretKey} used for signing the tokens.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}