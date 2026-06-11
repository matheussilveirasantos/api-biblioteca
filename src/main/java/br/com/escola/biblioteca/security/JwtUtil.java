package br.com.escola.biblioteca.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String gerarToken(String username) {
        Date agora = new Date();
        Date expira = new Date(agora.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(agora)
                .expiration(expira)
                .signWith(getKey())
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Token JWT não suportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Token JWT malformado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Token JWT inválido: {}", e.getMessage());
        }
        return false;
    }
}
