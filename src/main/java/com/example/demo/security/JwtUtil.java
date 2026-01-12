package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Secret key (Base64 encoded - phải đủ 256 bit)
    private static final String SECRET_KEY =
            "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5MTIzNDU2";

    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    // ===== Get signing key =====
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ===== Generate token =====
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ===== Extract email =====
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ===== Extract role =====
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ===== Extract expiration =====
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ===== Extract specific claim =====
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // ===== Extract all claims =====
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===== Check expired =====
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ===== Validate token =====
    public boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }
}