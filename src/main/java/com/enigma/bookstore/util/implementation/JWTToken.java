package com.enigma.bookstore.util.implementation;

import com.enigma.bookstore.exception.JWTException;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.util.ITokenGenerator;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JWTToken implements ITokenGenerator {

    @Autowired
    private ApplicationProperties applicationProperties;

    public String generateToken(Integer id, Date expirationTime) {
        Date currentDate = Calendar.getInstance().getTime();
        JwtBuilder jwtBuilder = Jwts.builder().setId(Integer.toString(id)).setIssuedAt(currentDate)
                .signWith(SignatureAlgorithm.HS256, applicationProperties.getSecretKey()).setExpiration(expirationTime);
        return jwtBuilder.compact();
    }

    public int verifyToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(applicationProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            return Integer.parseInt(claims.getId());
        } catch (ExpiredJwtException e) {
            throw new JWTException("Token Expired");
        } catch (Exception e) {
            throw new JWTException("Token Not Valid");
        }
    }
}
