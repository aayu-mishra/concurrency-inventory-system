package com.inventory.system.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private final String SECRET= "Zmf@0522@Ayus!!zu@0522$test79876321";
    private final long EXPIRATION= 1000*60*60;

    private final Key key= Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username){
        return Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            System.out.println("Error validating token "+ e.getMessage());
            return false;
        }
    }
}
