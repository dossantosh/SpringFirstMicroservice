package com.dossantosh.springfirstmicroservise.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * <p>
 * This class provides methods to generate, parse, extract information from,
 * and validate JWT tokens using a secret key and expiration time
 * configured via application properties.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private String secret;
    private long expiration;

    private Key key;

    /**
     * Sets the secret key used for signing the JWT tokens.
     * This value is injected from application properties.
     *
     * @param secret the secret key string
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Sets the expiration duration (in milliseconds) for the JWT tokens.
     * This value is injected from application properties.
     *
     * @param expiration the token expiration time in milliseconds
     */
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    /**
     * Gets the secret key used for signing the JWT tokens.
     *
     * @return the secret key string
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Gets the expiration duration (in milliseconds) for the JWT tokens.
     *
     * @return the expiration time in milliseconds
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Initializes the signing key from the secret after the properties are set.
     * This method is called automatically by Spring after dependency injection.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token string
     * @return the username contained in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token string
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided claims resolver function.
     *
     * @param <T>            the type of the claim to extract
     * @param token          the JWT token string
     * @param claimsResolver a function to extract a claim from the Claims object
     * @return the extracted claim of type T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token to extract all claims.
     *
     * @param token the JWT token string
     * @return the Claims object containing all token claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token the JWT token string
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the provided user details.
     *
     * @param userDetails the user details object
     * @return the generated JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername());
    }

    /**
     * Creates a JWT token with the given subject (username),
     * issued date, expiration date, and signs it with the secret key.
     *
     * @param subject the subject (usually username) to be included in the token
     * @return the generated JWT token string
     */
    private String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT token against the provided user details.
     * Checks if the username matches and the token is not expired.
     *
     * @param token       the JWT token string
     * @param userDetails the user details to validate against
     * @return true if the token is valid and belongs to the user, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
