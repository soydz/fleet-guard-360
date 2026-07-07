package com.fleetguard360.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.key}") String secret) {
        // HMAC256 expects a non-empty secret; fail fast if missing
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret (jwt.key) is not configured");
        }
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public boolean isTokenValid(String token) {
        try {
            // verify signature and standard claims (exp, nbf, iat) if present
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp != null && exp.before(new Date());
    }

    private Date extractExpiration(String token) {
        DecodedJWT decoded = JWT.decode(token);
        return decoded.getExpiresAt();
    }

    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        DecodedJWT decoded = JWT.decode(token);
        return claimsResolver.apply(decoded);
    }
}